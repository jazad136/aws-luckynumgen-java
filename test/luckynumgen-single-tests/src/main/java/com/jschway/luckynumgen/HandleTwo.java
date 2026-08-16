package com.jschway.luckynumgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import tools.jackson.databind.ObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class HandleTwo implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> { 
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        String BUCKETNAME = System.getenv("BUCKETNAME");
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        Map<String, List<String>> multiValueHeaders = Map.of(
            "Access-Control-Allow-Origin",List.of("*")
        );
        String lastX = "";
        if(input.getPathParameters() != null)
            for (var x : input.getPathParameters().values())  
                lastX = x;
            
        String numberIn = "";
        if (!lastX.isBlank()) { 
            numberIn = switch(lastX) { 
                case "4" -> "4";
                case "5" -> "5";
                case "6" -> "6";
                default -> "";
            };
        }
        String output;
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withMultiValueHeaders(multiValueHeaders);
        if(numberIn.isEmpty()) { 
            output = String.format("{ \"message\": \"Value %s is out of range\" }", lastX);
        }
        else {
            List<String> previous = new LinkedList<>(); 
            List<String> newNumbers = new LinkedList<>();
            
            String newNumber = newLuckyNumber(numberIn, previous);
            if(!newNumber.isEmpty()) {
                newNumbers.add(newNumber); previous.add(newNumber);
            }
            newNumber = newLuckyNumber(numberIn, previous);
            if(!newNumber.isEmpty()) {
                newNumbers.add(newNumber); previous.add(newNumber);
            }
            newNumber = newLuckyNumber(numberIn, previous);
            if(!newNumber.isEmpty()) { 
                newNumbers.add(newNumber); previous.add(newNumber);
            }
            
            // https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/best-practices-s3-uploads.html
            for(String digit : affectedDigits(newNumbers)) { 
                S3Client s3Client = S3Client.builder()
                    .region(Region.US_EAST_1)
                    .build();
                String bucketKey = "0" + digit + ".json";
                String result = uploadToS3(s3Client, BUCKETNAME, bucketKey, remainderFileString(bucketKey, previous));
                if(!result.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    output = mapper.writeValueAsString(new LuckyNumberMessages(result));
                    return response
                        .withStatusCode(502)
                        .withBody(output);
                }
            }
            
            String messagePart = "\"message\": \"Lucky Number\"";
            String luckyNum1Part = String.format("\"number1\": \"%s\"", newNumbers.get(0));
            String luckyNum2Part = String.format("\"number2\": \"%s\"", newNumbers.get(1));
            String luckyNum3Part = String.format("\"number3\": \"%s\"", newNumbers.get(2));
            output = String.format("{ %s,%s,%s,%s }", messagePart, luckyNum1Part, luckyNum2Part, luckyNum3Part);
        }              
        
        return response
                .withStatusCode(200)
                .withBody(output);
    }
    public static String uploadToS3(S3Client s3Client, String bucketName, String bucketKey, String content) {
        try {
            RequestBody body = RequestBody.fromString(content);
            s3Client.putObject(b -> b.bucket(System.getenv("BUCKET_NAME")).key(bucketKey), body);
            return "";
        } catch(S3Exception | SdkClientException e) {
            return e.getMessage();
        }
    }
    public static LuckyNumberMessages getMessagesCollection(List<LuckyNumberMessage> simpleMessages) { 
        return new LuckyNumberMessages(simpleMessages.stream()
            .map(LuckyNumberMessage::getMessage)
            .collect(Collectors.toList()));
    }
    private Collection<String> affectedDigits(Collection<String> newNumbers) { 
        Set<String> affected = new TreeSet<>();
        for(String numStr : newNumbers) 
            for(char c : numStr.toCharArray())
                affected.add(""+c);
        
        return affected;
    }
    
    public String remainderFileString(String numberIn, List<String> previous) { 
        final LinkedList<String> starts = new LinkedList<>();
        final LinkedList<String> ends = new LinkedList<>();
        for(int j = 1; j <= 9; j++) 
            if(previous.contains(numberIn+j)) 
                starts.add(numberIn+j);
        for (int k = 1; k <= 9; k++)
            if(previous.contains(k + numberIn))
                ends.add(k+numberIn);
        ListBundleMessage generated = new ListBundleMessage(starts, ends);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(generated);
    }
    
    public static String newLuckyNumber(String numberIn, List<String> previous) {
        List<String> lis = new ArrayList<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) 
            lis.add(numberIn + j);
        lis.add(numberIn);
        for (int k = 9; k >= 1; k--) 
            lis.add(k+ numberIn);
        // do not consider previously picked. 
        lis.removeAll(previous);
        if(lis.isEmpty())
            return "";
        
        // select a number
        Random r = new Random();
        return lis.get((int)r.nextInt(lis.size()));
    }
}

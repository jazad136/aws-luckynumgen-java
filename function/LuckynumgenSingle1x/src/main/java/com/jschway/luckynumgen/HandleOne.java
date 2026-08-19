package com.jschway.luckynumgen;
/*
Copyright 2026 Jonathan Saddler

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.jschway.luckynumgen.response.LuckyNumberMaxout;
import com.jschway.luckynumgen.response.LuckyNumberMessage;
import com.jschway.luckynumgen.response.LuckyNumberMessages;
import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import com.jschway.luckynumgen.s3model.ListBundleMessage;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import tools.jackson.databind.ObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class HandleOne implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private ObjectMapper mapper;
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        mapper = new ObjectMapper();
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
                case "1" -> "1";
                case "2" -> "2";
                case "3" -> "3";
                default -> "";
            };
        }
        S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
            .forcePathStyle(true)
            .build();
        
        String output;
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withMultiValueHeaders(multiValueHeaders);
        if(numberIn.isEmpty()) { 
            output = String.format("{ \"message\": \"Value %s is out of range\" }", lastX);
        }
        else {
            String readKey = String.format("single/0%s.json", numberIn);
            String generatedContent = getFromS3(s3Client, BUCKETNAME, readKey);
            ListBundleMessage startsEnds = mapper.readValue(generatedContent, ListBundleMessage.class);

            List<String> previous = gatherPrevious(startsEnds);
            
            List<String> newNumbers = new LinkedList<>();
            // improvement: have a single method update both lists.
//            String newNumber = newLuckyNumber(numberIn, previous);
//            if(!newNumber.isEmpty()) {
//                newNumbers.add(newNumber); previous.add(newNumber);
//            }
//            newNumber = newLuckyNumber(numberIn, previous);
//            if(!newNumber.isEmpty()) {
//                newNumbers.add(newNumber); previous.add(newNumber);
//            }
//            newNumber = newLuckyNumber(numberIn, previous);
//            if(!newNumber.isEmpty()) { 
//                newNumbers.add(newNumber); previous.add(newNumber);
//            }
            String num1 = newLuckyNumber(numberIn, newNumbers, previous);
            String num2 = newLuckyNumber(numberIn, newNumbers, previous);
            String num3 = newLuckyNumber(numberIn, newNumbers, previous);
            if(num1.isBlank()) {
                output = mapper.writeValueAsString(new LuckyNumberMaxout(
                        String.format("no more %s's", numberIn)));
            }
            // upload to S3
            for(String digit : affectedDigits(newNumbers)) { 
                String bucketKey = "single/0" + digit + ".json";
                String result = uploadToS3(s3Client, BUCKETNAME, bucketKey, remainderFileString(digit, previous));
                if(!result.isEmpty()) {
                    output = mapper.writeValueAsString(new LuckyNumberMessages(result));
                    return response
                        .withStatusCode(502)
                        .withBody(output);
                }
            }
            // improvement, use JSON Object

            var returnVal = new LuckyNumbersResponseType("Lucky Number", num1, num2, num3);
            output = mapper.writeValueAsString(returnVal);
//            String messagePart = "\"message\": \"Lucky Number\"";
//            String luckyNum1Part = String.format("\"number1\": \"%s\"", num1);
//            String luckyNum2Part = String.format("\"number2\": \"%s\"", num2);
//            String luckyNum3Part = String.format("\"number3\": \"%s\"", num3);
//            output = String.format("{ %s,%s,%s,%s }", messagePart, luckyNum1Part, luckyNum2Part, luckyNum3Part);
        }
        return response
                .withStatusCode(200)
                .withBody(output);
    }
    
    
    public String remainderFileString(String numberIn, List<String> previous) { 
        final LinkedList<String> starts = new LinkedList<>();
        final LinkedList<String> ends = new LinkedList<>();
        for(int j = 1; j <= 9; j++) 
            if(previous.contains(numberIn+j)) 
                starts.add(numberIn+j);
        for (int k = 1; k <= 9; k++) {
            if(!(""+k).equals(numberIn)) // don't insert repeats in second list
                if(previous.contains(k + numberIn))
                    ends.add(k+numberIn);
        }
        ListBundleMessage generated = new ListBundleMessage(starts, ends);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(generated);
    }
    public static String getFromS3(S3Client s3Client, String bucketName, String bucketKey)  {
        try (ResponseInputStream<GetObjectResponse> body = s3Client.getObject(b -> b.bucket(bucketName).key(bucketKey));) { 
            String toReturn = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            body.abort();
            return toReturn;
        } catch(IOException iex) { 
            return iex.getMessage();
        } catch(S3Exception | SdkClientException e) {
            return e.getMessage();
        }
    }
    public static String uploadToS3(S3Client s3Client, String bucketName, String bucketKey, String content) {
        try {
            RequestBody body = RequestBody.fromString(content);
            s3Client.putObject(b -> b.bucket(bucketName).key(bucketKey), body);
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
    private static String newLuckyNumber(String numberIn, List<String> previous, List<String> newNumber) { 
        LinkedHashSet<String> picks = new LinkedHashSet<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) 
            picks.add(numberIn + j);
        for (int k = 9; k >= 1; k--) 
            picks.add(k+ numberIn);
        
        // do not consider previously picked. 
        picks.removeAll(previous);
        if(picks.isEmpty())
            return "";
        
        // select a number
        SecureRandom r = new SecureRandom();
        int pickIdx = r.nextInt(picks.size());
        String pick = new LinkedList<>(picks).get(pickIdx);
        previous.add(pick);
        newNumber.add(pick);
        return pick;
    }
//    public static String newLuckyNumber(String numberIn, List<String> previous, List<String> newNumber) { 
//        String newLuckyNumber = newLuckyNumber(numberIn, previous);
//        if(!newLuckyNumber.isBlank()) {
//            previous.add(newLuckyNumber);
//            newNumber.add(newLuckyNumber);
//        }
//        return newLuckyNumber;
//    }
    public static String newLuckyNumber(String numberIn, List<String> previous) {
        LinkedHashSet<String> picks = new LinkedHashSet<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) {
            picks.add(numberIn + j);
        }
        for (int k = 9; k >= 1; k--)
            picks.add(k+ numberIn);
        // do not consider previously picked. 
        picks.removeAll(previous);
        if(picks.isEmpty())
            return "";

        // select a number
        Random r = new Random();
        return (new LinkedList<>(picks)).get((int)r.nextInt(picks.size()));
    }

    private List<String> gatherPrevious(ListBundleMessage startsEnds) {
        TreeSet<String> previous = new TreeSet<>();
        previous.addAll(startsEnds.getGenerated().getStarts());
        previous.addAll(startsEnds.getGenerated().getEnds());
        return new LinkedList<>(previous);
    }
}

package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import tools.jackson.databind.ObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class HandleThree implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static LambdaLogger log;
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        log = context.getLogger();
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
                case "7" -> "7";
                case "8" -> "8";
                case "9" -> "9";
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
            log.log(String.format("Added Numbers %s\n", newNumbers));
//            List<String> previous = new LinkedList<>(); 
//            List<String> newNumbers = new LinkedList<>();
//            for(int i = 0; i < 3; i++) {
//                String newNumber = newLuckyNumber(numberIn, previous);
//                newNumbers.add(newNumber); previous.add(newNumber);
//            }
            
            S3AsyncClient s3AClient = S3AsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
                    .forcePathStyle(true)
                    .build();
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
            Collection<String> affectedDigits = affectedDigits(newNumbers);
            final List<LuckyNumberMessage> simpleMessages = new LinkedList<>();
            List<CompletableFuture<String>> s3Writes = affectedDigits.stream().map(digit -> {
                String nextKey = String.format("single/0%s.json", digit);
                return uploadToS3(s3AClient, BUCKETNAME, nextKey, remainderFileString(digit, previous));
            }).collect(Collectors.toList());
            CompletableFuture<?>[] futuresArray = s3Writes.toArray(new CompletableFuture<?>[0]);
            CompletableFuture<List<String>> listWrites = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> s3Writes.stream().map(CompletableFuture::join).collect(Collectors.toList()));
            final List<String> messages = listWrites.join();
//            
//            CompletableFuture<Void> responses = CompletableFuture.allOf(cfs)
//                .exceptionally(e -> {
//                    if (e != null)
//                        simpleMessages.add(new LuckyNumberMessage("" + e.getClass().getSimpleName() + ": " + e.getMessage()));
//                    return null;
//                }
//            );
//            try { 
//            responses.join();
//            } catch(ExecutionException e) { 
//                
//            }
            List<String> issues = getIssues(messages);
            if(!issues.isEmpty()) {
                LuckyNumberMessages issueMessages = new LuckyNumberMessages(issues);
                ObjectMapper mapper = new ObjectMapper();
                output = mapper.writeValueAsString(messages);
            }
//            for(String digit : affectedDigits) {
//                AsyncRequestBody s3Body = AsyncRequestBody.fromString(remainderFileString(digit, previous));
//                String key = String.format("single/0%s.json", digit);
//                s3Writes.add(s3AsyncClient.putObject(r -> r.bucket("mybucket-jschway939").key(key), s3Body));
            
//            final String uploadKey = "1";
//            CompletableFuture<PutObjectResponse> responseFuture =
//          https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/best-practices-s3-uploads.html
//          ExecutorService executor = Executors.newSingleThreadExecutor();

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
    
    public static List<String> getIssues(final List<String> messages) { 
        IntStream.range(0, messages.size()).boxed().map(idx -> {
                if(messages.get(idx).isBlank()) {
                    return String.format("%d : %s", idx, messages.get(idx));
                };
                return "";
        }).filter(msg -> !msg.isEmpty()).collect(Collectors.toList());
        return null;
    }
    public static CompletableFuture<String> uploadToS3(S3AsyncClient s3AsyncClient, String bucketName, String bucketKey, String content) {
        AsyncRequestBody s3Body = AsyncRequestBody.fromString(content);
        return s3AsyncClient.putObject(r -> r.bucket(bucketName).key(bucketKey), s3Body)
            .handle((putResponse, throwable) -> { 
                if(throwable != null) 
                    return throwable.getMessage();
                return "";
        });
    }
//    public CompletableFuture<PutObjectResponse> addToS3Bucket(S3AsyncClient client, String bucketName) { 
//        return client.putObject(r -> r.bucket("mybucket-jschway939").key(key), s3Body)
//            .exceptionally(e -> {
//                if (e != null)
//                    simpleMessage.setMessage("" + e.getClass().getSimpleName() + ": " + e.getMessage());
//                return null;
//            });
//    }
    
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

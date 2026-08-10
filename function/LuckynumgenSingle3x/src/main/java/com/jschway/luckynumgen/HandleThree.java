package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import tools.jackson.databind.ObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class HandleThree implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
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
//            List<String> previous = new LinkedList<>(); 
//            List<String> newNumbers = new LinkedList<>();
//            for(int i = 0; i < 3; i++) {
//                String newNumber = newLuckyNumber(numberIn, previous);
//                newNumbers.add(newNumber); previous.add(newNumber);
//            }
            
            AsyncRequestBody s3Body = AsyncRequestBody.fromString(remainderFileString(numberIn, previous));
            
            S3AsyncClient s3AsyncClient = S3AsyncClient.builder().build();
            final String uploadKey = "1";
            CompletableFuture<PutObjectResponse> responseFuture =
            s3AsyncClient.putObject(r -> r.bucket("mybucket-jschway939").key(""), body)
                    .exceptionally(e -> {
                        if (e != null){
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
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
    public Collection<String> affectedDigits(List<String> newNumbers) { 
        Set<String> affected = new HashSet<>();
//        for(String s )
        return affected;
    }
    public String remainderFileString(String numberIn, List<String> previous) { 
        String remainderFile = "";
        LinkedList<String> lis = new LinkedList<>();
        final LinkedList<String> starts = new LinkedList<>();
        final LinkedList<String> ends = new LinkedList<>();
        for(int j = 1; j <= 9; j++) 
            if(previous.contains(numberIn+j)) 
                starts.add(numberIn+j);
        for (int k = 9; k >= 1; k--) 
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
    public static class ListBundleMessage { 
        public ListBundle generated;
        public ListBundleMessage(List<String> starts, List<String> ends) {
            generated = new ListBundle(starts, ends);
        }
        @JsonGetter("generated")
        public ListBundle getGenerated() { return generated; } 
        @JsonSetter("generated")
        public void setGenerated(ListBundle value) { this.generated = value; } 
    }
    public static class ListBundle { 
        public List<String> starts;
        public List<String> ends; 
        
        public ListBundle(List<String> starts, List<String> ends) {
            this.starts = starts; 
            this.ends = ends;
        }
        @JsonGetter("starts")
        public List<String> getStarts() { return starts; }
        
        @JsonSetter("starts")
        public void setStarts(List<String> starts) { this.starts = starts; }

        @JsonGetter("ends")
        public List<String> getEnds() { return ends; }
        @JsonSetter("ends")
        public void setEnds(List<String> ends) { this.ends = ends; }
        
    }
}

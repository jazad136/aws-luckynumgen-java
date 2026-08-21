package com.jschway.luckynumgen;

import com.jschway.luckynumgen.response.LuckyNumberMessage;
import com.jschway.luckynumgen.s3model.ListBundleMessage;
import com.jschway.luckynumgen.response.LuckyNumberMessages;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jschway.luckynumgen.response.LuckyNumberMaxout;
import com.jschway.luckynumgen.response.LuckyNumbersAttributes;
import com.jschway.luckynumgen.response.LuckyNumbersAttrsResponseType;
import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
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
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Handler for requests to Lambda function.
 */
public class HandleTwoNoS3 implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> { 
    private static ObjectMapper mapper;
    
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
                case "4" -> "4";
                case "5" -> "5";
                case "6" -> "6";
                default -> "";
            };
        }
//        S3Client s3Client = S3Client.builder()
//            .region(Region.US_EAST_1)
//            .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
//            .forcePathStyle(true)
//            .build();
        S3Client s3Client = null;
        
        String output = null;
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withMultiValueHeaders(multiValueHeaders);
        if(numberIn.isEmpty()) { 
            output = String.format("{ \"message\": \"Value %s is out of range\" }", lastX);
        }
        else {
            String readKey = String.format("single/0%s.json", numberIn);
//            String generatedContent = getFromS3(s3Client, BUCKETNAME, readKey);
//            ObjectMapper mapper = new ObjectMapper();
//            ListBundleMessage startsEnds = mapper.readValue(generatedContent, ListBundleMessage.class);
//
//            List<String> previous = gatherPrevious(startsEnds);
            List<String> previous = new LinkedList<>();
            List<String> newNumbers = new LinkedList<>();
            // improvement: have a single method update both lists. 
            String num1 = newLuckyNumber(numberIn, newNumbers, previous);
            String num2 = newLuckyNumber(numberIn, newNumbers, previous);
            String num3 = newLuckyNumber(numberIn, newNumbers, previous);
            if(num1.isBlank()) {
                try {
                output = mapper.writeValueAsString(new LuckyNumberMaxout(
                        String.format("no more %s's", numberIn)));
                } catch(JsonProcessingException e) { 
                    throw new RuntimeException(e);
                }
                return response
                        .withStatusCode(429) // too many requests
                        .withBody(output);
            }
            // upload to S3
            LinkedList<String> maxedout = new LinkedList<>();
            for(String digit : affectedDigits(newNumbers)) { 
                String bucketKey = "single/0" + digit + ".json";
                
                ListBundleMessage counts = remainderFile(digit, previous);
                String result;
                try {
                    result = HistoryPull.uploadToS3(s3Client, BUCKETNAME, bucketKey, mapper.writeValueAsString(counts));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                if(!result.isEmpty()) {
                    try {
                        output = mapper.writeValueAsString(new LuckyNumberMessages(result));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                    return response
                        .withStatusCode(502)
                        .withBody(output);
                }
                if(PrelimChecks.bundleFilled(counts.getGenerated(), numberIn))
                    maxedout.add(digit);
            }
            if(!maxedout.isEmpty()) {
                LuckyNumbersAttrsResponseType numbersMsg = new LuckyNumbersAttrsResponseType("Lucky Number", num1, num2, num3);
                numbersMsg.setAttributes(new LuckyNumbersAttributes("maxedout", maxedout));
                try {
                    output = mapper.writeValueAsString(numbersMsg);
                } catch (JsonProcessingException ex) {
                    System.getLogger(HandleTwoNoS3.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            else
                try {
                    output = mapper.writeValueAsString(new LuckyNumbersResponseType("Lucky Number", num1, num2, num3));
                } catch (JsonProcessingException ex) {
                    System.getLogger(HandleTwoNoS3.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
        }
        return response
                .withStatusCode(200)
                .withBody(output);
    }
    
    public ListBundleMessage remainderFile(String numberIn, List<String> previous) { 
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
        return new ListBundleMessage(starts, ends);
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
    private static String newLuckyNumber(String numberIn, List<String> previous) {
        List<String> lis = new ArrayList<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) 
            lis.add(numberIn + j);
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

    private List<String> gatherPrevious(ListBundleMessage startsEnds) {
        TreeSet<String> previous = new TreeSet<>();
        previous.addAll(startsEnds.getGenerated().getStarts());
        previous.addAll(startsEnds.getGenerated().getEnds());
        return new LinkedList<>(previous);
    }
}

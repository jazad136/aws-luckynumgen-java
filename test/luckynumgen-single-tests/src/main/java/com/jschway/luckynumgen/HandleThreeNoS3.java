package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jschway.luckynumgen.HistoryPull.getFromS3;
import static com.jschway.luckynumgen.HistoryPull.uploadToS3;
import com.jschway.luckynumgen.response.LuckyNumberMaxout;
import com.jschway.luckynumgen.response.LuckyNumberMessage;
import com.jschway.luckynumgen.response.LuckyNumberMessages;
import com.jschway.luckynumgen.response.LuckyNumbersAttributes;
import com.jschway.luckynumgen.response.LuckyNumbersAttrsResponseType;
import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import com.jschway.luckynumgen.s3model.ListBundleMessage;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Handler for requests to Lambda function.
 */
public class HandleThreeNoS3 implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static ObjectMapper mapper;
    private static String numberInKey;
    private static String numberInBucket;
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        String output;
        mapper = new ObjectMapper();
        var response = Setup.response();
        String readParam = PrelimChecks.getReadParameter(input.getPathParameters());
        String numberIn = switch(readParam) { 
            case "7" -> "7";
            case "8" -> "8";
            case "9" -> "9";
            default -> "";
        };
        if(numberIn.isEmpty()) { 
            output = """
                {  "type": "exception", "message": "Value %s is out of range" }""".formatted(numberIn);
            return response
                .withStatusCode(400) // bad request
                .withBody(output);
        }
        S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
            .forcePathStyle(true)
            .build();
        
        numberInKey = getReadKey(numberIn);
        numberInBucket = getReadBucket();
        String generatedContent = getFromS3(s3Client, numberInBucket, numberInKey);
        ListBundleMessage startsEnds;
        try {
            startsEnds = mapper.readValue(generatedContent, ListBundleMessage.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        List<String> previous = gatherPrevious(startsEnds);
        List<String> newNumbers = new LinkedList<>();
        
        String num1 = newLuckyNumber(numberIn, newNumbers, previous);
        String num2 = newLuckyNumber(numberIn, newNumbers, previous);
        String num3 = newLuckyNumber(numberIn, newNumbers, previous);
        if(num1.isBlank()) {
            try {
                output = mapper.writeValueAsString(new LuckyNumberMaxout(String.format("no more %s's", numberIn)));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        List<String> maxedout = new LinkedList<>();
        // upload to S3
        for(String digit : affectedDigits(newNumbers)) { 
            String bucketKey = getReadKey(digit);
            ListBundleMessage counts = remainderFile(digit, previous);
            String result;
            try {
                result = uploadToS3(s3Client, numberInBucket, bucketKey, mapper.writeValueAsString(counts));
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
                throw new RuntimeException(ex);
            }
        }
        else
            try {
                output = mapper.writeValueAsString(new LuckyNumbersResponseType("Lucky Number", num1, num2, num3));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
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

    private List<String> gatherPrevious(ListBundleMessage startsEnds) {
        TreeSet<String> previous = new TreeSet<>();
        previous.addAll(startsEnds.getGenerated().getStarts());
        previous.addAll(startsEnds.getGenerated().getEnds());
        return new LinkedList<>(previous);
    }
    public static String getReadKey(String numberIn) { return String.format("single/0%s.json", numberIn); } 
    public static String getReadBucket() { return System.getenv("BUCKETNAME");} 
}

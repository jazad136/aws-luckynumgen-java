package com.jschway.luckynumgen.status;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.jschway.luckynumgen.HistoryPull;
import static com.jschway.luckynumgen.PrelimChecks.getReadParameter;
import com.jschway.luckynumgen.Setup;
import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import com.jschway.luckynumgen.s3model.ListBundle;
import com.jschway.luckynumgen.s3model.ListBundleMessage;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.s3.S3Client;
import tools.jackson.databind.ObjectMapper;

public class NumberStatus {
    
    private static Map<String, String> headers;
    private static Map<String, List<String>> multiValueHeaders;
    private static ObjectMapper mapper;
    
    public static APIGatewayProxyResponseEvent maxout(final APIGatewayProxyRequestEvent input, final Context context) {
        String output;
        mapper = new ObjectMapper();
        var response = Setup.response();
        String numberIn = getReadParameter(input.getPathParameters());
        if(numberIn.isEmpty()) { 
            output = "{ \"message\": \"Expected an integer but it was not provided\" }";
            return response
                .withStatusCode(502)
                .withBody(output);
        }
        List<String> digits = numberIn.chars().boxed().map(String::valueOf).collect(Collectors.toList());
        List<String> maxedOut = new LinkedList<>();
        for(String digit : digits) { 
            String numberInKey;
            String numberInBucket;
            numberInKey = getReadKey(digit);
            numberInBucket = getReadBucket();
            S3Client s3Client = Setup.s3Client();
            String s3Outcome = HistoryPull.getFromS3(s3Client, numberInBucket,numberInKey);

            if(s3Outcome.contains("bucket")) { 
                output = String.format("{ \"message\": \"Bucket key %s could not be found in bucket [%s]\" }", numberInKey, numberInBucket);
                return response
                    .withStatusCode(502)
                    .withBody(output);
            }
            ListBundle counts = mapper.readValue(s3Outcome, ListBundleMessage.class).getGenerated();
            if(bundleFilled(counts, numberIn))
                maxedOut.add(digit);
        }
        
        var returnVal = new LuckyNumbersResponseType("maxout", maxedOut);
        output = mapper.writeValueAsString(returnVal);
        return response
                .withStatusCode(200)
                .withBody(output);
    }
    
    
    public static boolean bundleFilled(ListBundle remainder, String numberIn) { 
        LinkedHashSet<String> picks = new LinkedHashSet<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) {
            picks.add(numberIn + j);
        }
        for (int k = 9; k >= 1; k--)
            picks.add(k+ numberIn);
        for(String p : picks) { 
            if(checkBundleFor(remainder, numberIn))
                return true;
        }
        return false;
    }
    private static boolean checkBundleFor(ListBundle remainder, String bundleItem) { 
        return remainder.getStarts().contains(bundleItem) || remainder.getEnds().contains(bundleItem);
    }
    public static String getReadKey(String numberIn) { return String.format("single/0%s.json", numberIn); } 
    public static String getReadBucket() { return System.getenv("BUCKETNAME");} 
    
}

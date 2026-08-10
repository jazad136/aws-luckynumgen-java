package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Handler for requests to Lambda function.
 */
public class HandleOne implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
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
                case "1" -> "1";
                case "2" -> "2";
                case "3" -> "3";
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
//        int randomPart = r.nextInt(0,9)+1;
//        int position = r.nextInt(2);
//        String stringOut = "" + randomPart;
//        if(position == 0) { 
//            stringOut = numberIn + stringOut;
//        }
//        else 
//            stringOut = stringOut + numberIn;
//        return stringOut;
    }
    
    // not needed anymore
//    public static String newLuckyNumber(String numberIn, List<String> previous) {
//        String stringOut = "";
//        do { 
//            stringOut = newLuckyNumber(numberIn);
//        } while(previous.contains(stringOut)); // do not regenerate a number
//        return stringOut;
//    }
}

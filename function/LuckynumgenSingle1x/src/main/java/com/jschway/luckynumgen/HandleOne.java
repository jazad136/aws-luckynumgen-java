package com.jschway.luckynumgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.LinkedList;
import java.util.List;

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
            previous.add(newLuckyNumber(numberIn));
            previous.add(newLuckyNumber(numberIn, previous));
            previous.add(newLuckyNumber(numberIn, previous));
            String messagePart = "\"message\": \"Lucky Number\"";
            String luckyNum1Part = String.format("\"number1\": \"%s\"", previous.get(0));
            String luckyNum2Part = String.format("\"number2\": \"%s\"", previous.get(1));
            String luckyNum3Part = String.format("\"number3\": \"%s\"", previous.get(2));
            output = String.format("{ %s,%s,%s,%s }", messagePart, luckyNum1Part, luckyNum2Part, luckyNum3Part);
//            output = String.format("{ \"message\": \"Lucky Number\", \"number\": \"%s\" }", luckyNum1);
        }
        return response
                .withStatusCode(200)
                .withBody(output);
    }
    
    public static String newLuckyNumber(String numberIn) { 
        Random r = new Random();
        int randomPart = r.nextInt(0,9)+1;
        int position = r.nextInt(2);
        String stringOut = "" + randomPart;
        if(position == 0) { 
            stringOut = numberIn + stringOut;
        }
        else 
            stringOut = stringOut + numberIn;
        return stringOut;
    }
    
    public static String newLuckyNumber(String numberIn, List<String> previous) { 
        String stringOut = "";
        do { 
            stringOut = newLuckyNumber(numberIn);
        } while(previous.contains(stringOut)); // do not regenerate a number
        return stringOut;
    }
}

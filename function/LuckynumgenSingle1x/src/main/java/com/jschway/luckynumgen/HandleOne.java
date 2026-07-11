package com.jschway.luckynumgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
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
//            "Access-Control-Allow-Origin",List.of("*")
            "Access-Control-Allow-Origin",List.of("https://jschway.com","https://mybucket-jschway813.s3.us-east-1.amazonaws.com")
        );
        String lastX = null;
        for (var x : input.getPathParameters().values()) { 
            lastX = x;
        }
        String numberIn = "";
        if (lastX != null) { 
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
            Random r = new Random();
            int numberOut = r.nextInt(0,10);
            int position = r.nextInt(2);
            String stringOut = "" + numberOut;
            if(position == 0) { 
                stringOut = "1" + stringOut;
            }
            else 
                stringOut = stringOut + "1";
            
            output = String.format("{ \"message\": \"Lucky Number\", \"number\": \"%s\" }", stringOut);
        }
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

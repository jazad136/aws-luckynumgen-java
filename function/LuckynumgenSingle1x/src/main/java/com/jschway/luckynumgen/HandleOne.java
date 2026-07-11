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
        
        Random r = new Random();
        int numberOut = r.nextInt(0,10);
        int position = r.nextInt(2);
        String stringOut = "" + numberOut;
        if(position == 0) { 
            stringOut = "1" + stringOut;
        }
        else 
            stringOut = stringOut + "1";
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withMultiValueHeaders(multiValueHeaders);
        String output = String.format("{ \"message\": \"Lucky Number\", \"number\": \"%s\" }", stringOut);
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

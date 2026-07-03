package com.jschway.luckynumgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class HandleOne implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        Random r = new Random();
        int numberOut = r.nextInt(0,10);
        int position = r.nextInt(2);
        String stringOut = "" + numberOut;
        if(position == 0) { 
            stringOut = "1" + numberOut;
        }
        else 
            stringOut = numberOut + "1";
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
       
        String output = String.format("{ \"message\": \"Lucky Number\", \"number\": \"%s\" }", stringOut);
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

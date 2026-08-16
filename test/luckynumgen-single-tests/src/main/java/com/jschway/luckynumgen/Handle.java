package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Handle {
    public static APIGatewayProxyRequestEvent getRequest(String numberIn) { 
        APIGatewayProxyRequestEvent newEv = new APIGatewayProxyRequestEvent();
        newEv.setPathParameters(Map.of("1", numberIn));
        return newEv;
    }

    public static Context getTestContext() {
        return null;
    }
    public Handle() { 
        
    }
    public List<String> generateThree(String numberIn) { 
        List<String> previous = new LinkedList<>();
        previous.add(newLuckyNumber(numberIn));
        previous.add(newLuckyNumber(numberIn, previous));
        previous.add(newLuckyNumber(numberIn, previous));
        return previous;
    }
    public String newLuckyNumber(String numberIn) { 
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
    
    public String newLuckyNumber(String numberIn, List<String> previous) { 
        String stringOut = "";
        do { 
            stringOut = newLuckyNumber(numberIn);
        } while(previous.contains(stringOut)); // do not regenerate a number
        return stringOut;
    }
}

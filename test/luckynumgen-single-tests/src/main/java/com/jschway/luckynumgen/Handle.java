package com.jschway.luckynumgen;

import java.util.Random;

public class Handle {
    public static String generateOne(String input) { 
        Random r = new Random();
        int numberOut = r.nextInt(0,10);
        int position = r.nextInt(2);
        String stringOut = "" + numberOut;
        if(position == 0) { 
            stringOut = input + stringOut;
        }
        else 
            stringOut = stringOut + input;
        return stringOut;
    }
}

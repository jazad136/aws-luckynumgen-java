package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LuckyNumberMaxedout extends LuckyNumbersResponseType { 
    
    public LuckyNumberMaxedout(String message) { 
        setMessage(message);
        String[] split3 = message.split(" ");
        setNumber1(split3[0]);
        setNumber2(split3[1]);
        setNumber3(split3[2]);
    }
    
    @JsonGetter("type")
    public String getType() { return "exception"; }
}
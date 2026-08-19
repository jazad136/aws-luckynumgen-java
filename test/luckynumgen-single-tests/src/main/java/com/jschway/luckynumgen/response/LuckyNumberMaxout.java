package com.jschway.luckynumgen.response;

import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import com.fasterxml.jackson.annotation.JsonGetter;

public class LuckyNumberMaxout extends LuckyNumbersResponseType { 
    
    public LuckyNumberMaxout(String message) { 
        super(message);
        String[] split3 = message.split(" ");
        setNumber1(split3[0]);
        setNumber2(split3[1]);
        setNumber3(split3[2]);
    }
    
    @JsonGetter("type")
    public String getType() { return "exception"; }
}
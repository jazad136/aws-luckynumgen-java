package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LuckyNumberMaxedout extends LuckyNumberMessage { 
    
    public LuckyNumberMaxedout(String message) { super(message); }
    
    @JsonGetter("type")
    public String getType() { return "error"; }
}
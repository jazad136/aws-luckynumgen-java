package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LuckyNumberMessage { 
    private String message;
    public LuckyNumberMessage(String message) { this.message = message; } 
    public LuckyNumberMessage() { }
    @JsonGetter("message")
    public String getMessage() { return message; }
    @JsonSetter("message")
    public void setMessage(String message) { this.message = message; }
}
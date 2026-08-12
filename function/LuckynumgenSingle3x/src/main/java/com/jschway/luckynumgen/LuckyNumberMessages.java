package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;

public class LuckyNumberMessages { 
    private HashMap<String, String> messages;
    public LuckyNumberMessages(List<String> messages) { 
        this.messages = new HashMap<>();
        for(int i = 1; i <= messages.size(); i++)  
            this.messages.put(""+i, messages.get(i));
    }

    @JsonAnyGetter
    public HashMap<String, String> getMessageMap() { return messages; } 
    @JsonAnySetter
    public void addMessage(String key, Object value) { messages.put(key, (String)value); } 
}
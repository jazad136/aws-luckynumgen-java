package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;

public class LuckyNumberMessages { 
    private HashMap<String, String> messages;
    public LuckyNumberMessages(String... messages) { 
        this.messages = new HashMap<>();
        for(int i = 0; i < messages.length; i++)  
            this.messages.put(""+i, messages[i]);
    }
    public LuckyNumberMessages(List<String> messages) { 
        this.messages = new HashMap<>();
        for(int i = 0; i < messages.size(); i++)  
            this.messages.put(""+(i+1), messages.get(i));
    }

    @JsonAnyGetter
    public HashMap<String, String> getMessageMap() { return messages; } 
    @JsonAnySetter
    public void addMessage(String key, Object value) { messages.put(key, (String)value); } 
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jschway.luckynumgen.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author jsaddle
 */
public class LuckyNumbersAttributes {
    private LinkedHashMap<String, Object> attributes;
    public LuckyNumbersAttributes() { 
        attributes = new LinkedHashMap<>();
    }
    public LuckyNumbersAttributes(String key1, Object value1) { 
        attributes = new LinkedHashMap<>();
        attributes.put(key1, value1);
    }
    public LuckyNumbersAttributes(String key1, Object value1, String key2, Object value2) { 
        attributes = new LinkedHashMap<>();
        attributes.put(key1, value1);
        attributes.put(key2, value2);
    }
    public LuckyNumbersAttributes(String key, List<String> values) { 
        attributes = new LinkedHashMap<>();
        attributes.put(key, values);
    }

    @JsonAnyGetter
    public HashMap<String, Object> getAttributes() { return attributes; }
   
    @JsonAnySetter
    public void setAttributes(String key, Object value) { attributes.put(key, (String)value); } 
}

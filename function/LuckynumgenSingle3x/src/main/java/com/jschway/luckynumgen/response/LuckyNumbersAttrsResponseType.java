/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jschway.luckynumgen.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class LuckyNumbersAttrsResponseType {
    /*
    {
    "message": "Lucky Number",
    "number1": "18",
    "number2": "41",
    "number3": "91",
    "attributes": { 
        "maxout": true
    }
  }
    */
    private String message;
    private String number1;
    private String number2;
    private String number3;
    private LuckyNumbersAttributes attributes;
    
    public LuckyNumbersAttrsResponseType(String message, String num1, String num2, String num3) { 
        this.message = message;
        this.number1 = num1;
        this.number2 = num2;
        this.number3 = num3;
        this.attributes = new LuckyNumbersAttributes();
    }
    public LuckyNumbersAttrsResponseType(String message) { 
        this.message = message;
        this.number1 = "";
        this.number2 = "";
        this.number3 = "";
        this.attributes = new LuckyNumbersAttributes();
    } 
    @JsonGetter
    public String getMessage() { return message; }

    @JsonSetter
    public void setMessage(String message) { this.message = message; }

    @JsonGetter
    public String getNumber1() { return number1; }

    @JsonSetter
    public void setNumber1(String number1) { this.number1 = number1; }

    @JsonGetter
    public String getNumber2() { return number2; }

    @JsonSetter
    public void setNumber2(String number2) { this.number2 = number2; }

    @JsonGetter
    public String getNumber3() { return number3; }

    @JsonSetter
    public void setNumber3(String number3) { this.number3 = number3; }
    
    @JsonGetter
    public LuckyNumbersAttributes getAttributes() { return attributes; } 
    
    @JsonSetter
    public void setAttributes(LuckyNumbersAttributes value) { this.attributes = value; } 
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jschway.luckynumgen.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.List;


public class LuckyNumbersResponseType {
    /*
    {
    "message": "Lucky Number",
    "number1": "18",
    "number2": "41",
    "number3": "91"
  }
    */
    private String message;
    private String number1;
    private String number2;
    private String number3;
    public LuckyNumbersResponseType(String message, List<String> numbers) { 
        this.message = message;
        if(!numbers.isEmpty()) {
            number1 = numbers.get(0);
            if(numbers.size() > 1) { 
                number2 = numbers.get(1);
                if(numbers.size() > 2)
                    number3 = numbers.get(2);
            }
        }
    }
    public LuckyNumbersResponseType(String message, String num1, String num2, String num3) { 
        this.message = message;
        this.number1 = num1;
        this.number2 = num2;
        this.number3 = num3;
    }
    public LuckyNumbersResponseType(String message) { 
        this.message = message;
        this.number1 = "";
        this.number2 = "";
        this.number3 = "";
    } 
    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getNumber1() { return number1; }

    public void setNumber1(String number1) { this.number1 = number1; }

    public String getNumber2() { return number2; }

    public void setNumber2(String number2) { this.number2 = number2; }

    public String getNumber3() { return number3; }

    public void setNumber3(String number3) { this.number3 = number3; }
    
    @JsonGetter("type")
    public String getType() { return "exception"; }
}

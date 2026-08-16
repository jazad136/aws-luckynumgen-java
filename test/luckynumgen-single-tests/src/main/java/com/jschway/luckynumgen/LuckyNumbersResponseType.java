package com.jschway.luckynumgen;

/**
 * 
 * @author Jonathan Saddler
 */
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
    
    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getNumber1() { return number1; }

    public void setNumber1(String number1) { this.number1 = number1; }

    public String getNumber2() { return number2; }

    public void setNumber2(String number2) { this.number2 = number2; }

    public String getNumber3() { return number3; }

    public void setNumber3(String number3) { this.number3 = number3; }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jschway.luckynumgen.tests.config;

import java.lang.reflect.Method;

/**
 *
 * @author jsaddle
 */
public class TestDataProvider {
    public static Object[][] methodDataProviderLogicSuite(Method method) {
        // separate data for each test
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("GENTHREENUMBERS")) { 
            data = new Object[9][2];
            data[0][0] = "1";
            data[0][1] = "Gen Three Numbers: Input 1";
            data[1][0] = "2";
            data[1][1] = "Gen Three Numbers: Input 2";
            data[2][0] = "3";
            data[2][1] = "Gen Three Numbers: Input 3";
            data[3][0] = "4";
            data[3][1] = "Gen Three Numbers: Input 4";
            data[4][0] = "5";
            data[4][1] = "Gen Three Numbers: Input 5";
            data[5][0] = "6";
            data[5][1] = "Gen Three Numbers: Input 6";
            data[6][0] = "7";
            data[6][1] = "Gen Three Numbers: Input 7";
            data[7][0] = "8";
            data[7][1] = "Gen Three Numbers: Input 8";
            data[8][0] = "9";
            data[8][1] = "Gen Three Numbers: Input 9";
        }
        return data;
    }
    /** Test Data Providers */
    public static String getTestNameLoginSuite(String methodName, Object[] params) {
        if(methodName.toUpperCase().equals("GENTHREENUMBERS")) { 
            if(params != null && params.length > 0)
                return params[1].toString();
        }
        return "Login Test";
    }
}

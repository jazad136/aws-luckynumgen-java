/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jschway.luckynumgen.tests.config;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

/**
 *
 * @author jsaddle
 */
public class TestDataProvider {
    
    @DataProvider
    public static Object[][] dataProviderLogicSuite() { 
        return new Object[][]{{"Logic Suite"}};
    }
    @DataProvider
    public static Object[][] methodDataProviderLogicSuite(Method method) {
        // separate data for each test
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("GENTHREENUMBERS1X")) { 
            data = new Object[][]{{"1"},{"1"},{"1"}};
        }
        if(method.getName().toUpperCase().equals("GENTHREENUMBERS2X")) { 
            data = new Object[3][1];
            data[0][0] = "4";
            data[1][0] = "4";
            data[2][0] = "4";
        }
        if(method.getName().toUpperCase().equals("GENTHREENUMBERS3X")) { 
            data = new Object[3][1];
            data[0][0] = "7";
            data[1][0] = "7";
            data[2][0] = "7";
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

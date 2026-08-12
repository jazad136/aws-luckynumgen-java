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
    public static Object[][] methodDataProviderPositiveSuite(Method method) {
        // separate data for each test
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBERSIMPLE")) { 
            data = new Object[2][3];
            data[0][0] = "1";
            data[0][1] = "1";
            data[0][2] = "Positive Numbers: Input 1";
        }
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBER")) { 
            data = new Object[9][3];
            data[0][0] = "1";
            data[0][1] = "1";
            data[0][2] = "Positive Numbers: Input 1";
            data[1][0] = "2";
            data[1][1] = "2";
            data[1][2] = "Gen Three Numbers: Input 2";
            data[2][0] = "3";
            data[2][1] = "3";
            data[2][2] = "Gen Three Numbers: Input 3";
            data[3][0] = "4";
            data[3][1] = "4";
            data[3][2] = "Gen Three Numbers: Input 4";
            data[4][0] = "5";
            data[4][1] = "5";
            data[4][2] = "Gen Three Numbers: Input 5";
            data[5][0] = "6";
            data[5][1] = "6";
            data[5][2] = "Gen Three Numbers: Input 6";
            data[6][0] = "7";
            data[6][1] = "7";
            data[6][2] = "Gen Three Numbers: Input 7";
            data[7][0] = "8";
            data[7][1] = "8";
            data[7][2] = "Gen Three Numbers: Input 8";
            data[8][0] = "9";
            data[8][1] = "9";
            data[8][2] = "Gen Three Numbers: Input 9";
        }
        return data;
    }
    /** Test Data Providers */
    public static String getTestNamePositiveSuite(String methodName, Object[] params) {
        if(methodName.toUpperCase().equals("TESTMESSAGE")) { 
            if(params != null && params.length > 0)
                return params[0].toString();
        }
        if(methodName.toUpperCase().equals("TESTNUMBER")) { 
            if(params != null && params.length > 0)
                return params[1].toString();
        }
        return "Login Test";
    }
    public static String getTestNameNullSuite(String methodName, Object[] params) {
        if(methodName.toUpperCase().equals("TESTNULLMESSAGE")) { 
            if(params != null && params.length > 0)
                return params[0].toString();
        }
        if(methodName.toUpperCase().equals("TESTERRORMESSAGE")) { 
            if(params != null && params.length > 0)
                return params[0].toString();
        }
        return "Login Test";
    }
//    @DisplayName("Get the root element")
//    @Test
//    public void getRoot() {
//        Map<String, ?> rootElement = JsonPath.read(jsonResponse, "$");
//        print(rootElement.toString());
//    }
//
//    @DisplayName("Get the total value from the response")
//    @Test
//    public void getTotalFromResponse() {
//        int totalValue = JsonPath.read(jsonResponse, "$.total");
//        print(totalValue + "");
//    }
//    
//    @DisplayName("Get all the data elements")
//    @Test
//    public void getAllDataElements() { 
//        List<HashMap<String, Object>> dataElements = JsonPath.read(jsonResponse, "$.data");
//        dataElements.stream().forEach(System.out::println);
//    }
//    
//    @DisplayName("Get firstDataElement")
//    @Test
//    public void getFirstDataElement() { 
//        Map<String,?> firstDataElement = JsonPath.read(jsonResponse, "$.data[0]");
//        print(firstDataElement.toString());
//    }
//    @DisplayName("Get lastDataElement")
//    @Test
//    public void getLastDataElement() { 
//        Map<String,?> firstDataElement = JsonPath.read(jsonResponse, "$.data[-1]");
//        print(firstDataElement.toString());
//    }
//    
//    @DisplayName("Get all ids in the data")
//    @Test
//    public void getAllIdsUnderData() { 
//        List<String> dataElements = JsonPath.read(jsonResponse, "$.data[*].id");
//        print(dataElements.toString());
//    }
//    
//    @DisplayName("Get all ids in the data")
//    @Test
//    public void getAllIds() { 
//        List<String> dataElements = JsonPath.read(jsonResponse, "$..id");
//        print(dataElements.toString());
//    }
}

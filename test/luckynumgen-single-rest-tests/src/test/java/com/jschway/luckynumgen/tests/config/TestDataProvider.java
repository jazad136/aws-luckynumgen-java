package com.jschway.luckynumgen.tests.config;
/*
Copyright 2026 Jonathan Saddler

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

/**
 * @author Jonathan Saddler
 */
public class TestDataProvider {
    
    @DataProvider
    public static Object[][] methodDataProviderPositive3xSuite(Method method) {
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBER")) { 
            data = new Object[3][3];
            data[0][0] = "7";
            data[0][1] = "7";
            data[0][2] = "Positive Numbers 3x: Input 7";
            data[1][0] = "8";
            data[1][1] = "8";
            data[1][2] = "Positive Numbers 3x: Input 8";
            data[2][0] = "9";
            data[2][1] = "9";
            data[2][2] = "Positive Numbers 3x: Input 9";
        }
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBERSIMPLE")) { 
            data = new Object[1][3];
            data[0][0] = "9";
            data[0][1] = "9";
            data[0][2] = "Positive Numbers 3x: Input 9";
        }
        return data;
    }
    @DataProvider
    public static Object[][] methodDataProviderPositive2xSuite(Method method) {
        // separate data for each test
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBER")) { 
            data = new Object[3][3];
            data[0][0] = "4";
            data[0][1] = "4";
            data[0][2] = "Positive Numbers 2x: Input 4";
            data[1][0] = "5";
            data[1][1] = "5";
            data[1][2] = "Positive Numbers 2x: Input 5";
            data[2][0] = "6";
            data[2][1] = "6";
            data[2][2] = "Positive Numbers 2x: Input 6";
        }
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBERSIMPLE")) { 
            data = new Object[1][3];
            data[0][0] = "4";
            data[0][1] = "4";
            data[0][2] = "Positive Numbers 2x: Input 4";
        }
        return data;
    }
    @DataProvider
    public static Object[][] methodDataProviderPositive1xSuite(Method method) {
        // separate data for each test
        System.out.println("Test method name: " + method.getName()); 
        Object data[][] = null;
        if(method.getName().toUpperCase().equals("TESTGENCONTAINSNUMBERSIMPLE")) { 
//            data = new Object[3][3];
            data = new Object[2][3];
            data[0][0] = "1";
            data[0][1] = "1";
            data[0][2] = "Positive Numbers: Input 1 (1st attempt)";
            data[1][0] = "1";
            data[1][1] = "1";
            data[1][2] = "Positive Numbers: Input 1 (2nd attempt)";
//            data[2][0] = "1";
//            data[2][1] = "1";
//            data[2][2] = "Positive Numbers: Input 1 (3rd attempt)";
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
        }
        return data;
    }
    
    /** Test Name Providers */
    
    public static String getTestNamePositiveSuite(String methodName, Object[] params) {
        if(methodName.toUpperCase().equals("TESTGENCONTAINSNUMBER")) { 
            if(params != null && params.length > 2)
                return params[2].toString();
        }
        if(methodName.toUpperCase().equals("TESTGENCONTAINSNUMBERSIMPLE")) { 
            if(params != null && params.length > 2)
                return params[2].toString();
        }
        return "Positive Suite";
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
}

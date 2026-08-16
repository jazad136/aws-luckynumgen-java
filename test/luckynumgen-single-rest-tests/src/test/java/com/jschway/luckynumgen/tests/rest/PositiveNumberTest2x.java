package com.jschway.luckynumgen.tests.rest;
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
import com.jschway.luckynumgen.tests.TestBase;
import com.jschway.luckynumgen.tests.config.TestDataProvider;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author JonathanSaddler
 */
public class PositiveNumberTest2x extends TestBase{
    @BeforeClass(dependsOnMethods = "init")
    public void setBasePath() { 
        RestAssured.basePath = "/twox";
    }
    
    @Test(dataProviderClass = TestDataProvider.class, 
          dataProvider = "methodDataProviderPositive2xSuite")
    public void testGenContainsNumberSimple(String numberIn, String numberContains, String testName) {
        i("Testing response is 200");
        var response = given()
         .pathParam("numberIn", numberIn)
         .when()
         .get("/{numberIn}")
         .then()
         .statusCode(200);
        i("Testing number1 contains " + numberContains);
        response
         .and()
         .body("number1", containsString(numberContains));
        i("Testing number2 contains " + numberContains);
        response
         .and()
         .body("number2", containsString(numberContains));
        i("Testing number3 contains " + numberContains);
        response
         .body("number3", containsString(numberContains));
    }
    public void testGenContainsNumber(String numberIn, String numberContains, String testName) {
        i("Testing response is 200");
        var response = given()
         .pathParam("numberIn", numberIn)
         .when()
         .get("/{numberIn}")
         .then()
         .statusCode(200);
        i("Testing number1 contains " + numberContains);
        response
         .and()
         .body("number1", contains(numberContains));
        i("Testing number2 contains " + numberContains);
        response
         .and()
         .body("number2", contains(numberContains));
        i("Testing number3 contains " + numberContains);
        response
         .body("number3", contains(numberContains));
    }

//    @DisplayName("Get the total value from the response")
//    @Test
//    public void getTotalFromResponse() {
//        int totalValue = JsonPath.read(jsonResponse, "$.total");
//        print(totalValue + "");
//    }
    
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

    @Override
    public String retrieveTestNameSuffix(ITestResult res) {
        return TestDataProvider.getTestNamePositiveSuite(
                res.getMethod().getMethodName(), 
                res.getParameters()
        );
    }
    
    
}

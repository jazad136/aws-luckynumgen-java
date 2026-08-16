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
public class PositiveNumberTest3x extends TestBase{
    @BeforeClass(dependsOnMethods = "init")
    public void setBasePath() { 
        RestAssured.basePath = "/threex";
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

    @Override
    public String retrieveTestNameSuffix(ITestResult res) {
        return TestDataProvider.getTestNamePositiveSuite(
                res.getMethod().getMethodName(), 
                res.getParameters()
        );
    }
    
    
}

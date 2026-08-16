package com.jschway.luckynumgen.tests;
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
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.jschway.luckynumgen.tests.config.ExtentReportManager;
import io.restassured.RestAssured;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

/**
 *
 * @author jsaddle
 */
public abstract class TestBase {
    protected ExtentReports extent;
    protected ExtentTest testReport;
    protected SoftAssert softAssert;
    private String testNameSuffix;
    
    @BeforeClass 
    public void init() { 
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
//        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(objectMapperConfig().defaultObjectMapperType(ObjectMapperType.GSON))
    }
    @BeforeMethod
    @Parameters({"testNamePrepend"})
    public void setup(ITestResult res, @Optional String testNamePrepend) {
        this.extent = ExtentReportManager.getReporter();
        testNamePrepend = testNamePrepend != null && !testNamePrepend.equals("testNamePrepend") ? testNamePrepend : "";
        this.softAssert = new SoftAssert();
        setTestNameSuffix(retrieveTestNameSuffix(res));
        this.testReport = extent.createTest(getFullTestName(testNamePrepend));
        res.setAttribute("reporterObject", testReport);
    }
    
    @AfterMethod
    public void teardown() { 
        extent.flush();
    }
    
    protected void setTestNameSuffix(String testNameSuffix) { this.testNameSuffix = testNameSuffix; } 
    protected String getTestNameSuffix() { return testNameSuffix; }
    public String getFullTestName(String prepend) {  return prepend + " " + testNameSuffix; }
    public abstract String retrieveTestNameSuffix(ITestResult res);
    
    public void i(String msg) {
        System.out.println(msg);
        testReport.info(msg);
    }
    public void iFormat(String formatMsg, Object... formatObjs) {
        System.out.printf(formatMsg + "\n", formatObjs);
        testReport.info(String.format(formatMsg + "\n", formatObjs));
    }
    public void pass(String msg) { 
        System.out.println(msg);
        testReport.pass(msg);
    }
    public void fail(String msg) { 
        System.out.println(msg);
        testReport.fail(msg);
    }
    public void skip(String msg) { 
        System.out.println(msg);
        testReport.skip(msg);
    }
}

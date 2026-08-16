package com.jschway.luckynumgen.tests.listener;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestInfoPrintListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) { 
        System.out.println("******** TEST FAILURE ********");
        ExtentTest test = (ExtentTest) result.getAttribute("reporterObject");
        test.log(Status.INFO, "Test Case Name : " + result.getName());
        System.out.println("Test Case Name : " + result.getName());
        test.fail("Error: " + result.getThrowable().getMessage());
        System.out.println("Error: " + result.getThrowable().getMessage());
    }
    @Override
    public void onTestSuccess(ITestResult result) { 
        System.out.println("******** TEST SUCCESS ********");
        ExtentTest test = (ExtentTest) result.getAttribute("reporterObject");
        test.log(Status.INFO, "Test Case Name : " + result.getName());
        test.pass("Test Status is Success");
    }
}

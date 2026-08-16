package com.jschway.luckynumgen.tests.handle;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jschway.luckynumgen.Handle;
import com.jschway.luckynumgen.HandleOne;
import com.jschway.luckynumgen.LuckyNumbersResponseType;
import com.jschway.luckynumgen.tests.TestBase;
import com.jschway.luckynumgen.tests.TestHelpMethods;
import com.jschway.luckynumgen.tests.config.TestDataProvider;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleTest2 extends TestBase {
    
    Random gen;
    boolean[][] coverage;
    private int rowLimit;
    private int colLimit;
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        gen = new Random();
        colLimit = rowLimit = 10;
        coverage = new boolean[rowLimit][colLimit];
    }
    @Test(dataProviderClass = TestDataProvider.class, 
          dataProvider = "methodDataProviderLogicSuite")
    public void genThreeNumbers1x(String inputNumberIn) throws JsonProcessingException { 
        HandleOne handleOne = new HandleOne();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        APIGatewayProxyResponseEvent response = handleOne.handleRequest(Handle.getRequest(inputNumberIn), Handle.getTestContext());
        ObjectMapper mapper = new ObjectMapper();
        LuckyNumbersResponseType body = mapper.readValue(response.getBody(), LuckyNumbersResponseType.class);
//        selections.addAll();
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
    public void genThreeNumbers2x(String inputNumberIn) { 
        Handle handle = new Handle();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        List<String> outTexts = handle.generateThree(inputNumberIn);
        selections.addAll(outTexts);
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
    public void genThreeNumbers3x(String inputNumberIn) { 
        Handle handle = new Handle();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        List<String> outTexts = handle.generateThree(inputNumberIn);
        selections.addAll(outTexts);
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
    @Test
    public void getThreeNumbersOld() throws InterruptedException { 
        int select = gen.nextInt(9)+1;
        Handle handle = new Handle();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        List<String> outTexts = handle.generateThree(""+select);
        selections.addAll(outTexts);
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
    public void testTenSets() { 
        
    }
    public List<String> newSetLuckyNumbers() { 
        int select = gen.nextInt(9)+1;
        Handle handle = new Handle();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        List<String> outTexts = handle.generateThree(""+select);
        selections.addAll(outTexts);
        
        try{Thread.sleep(1000);} catch(InterruptedException e) {throw new RuntimeException(e);}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
        return new LinkedList<>(selections);
    }
    public String mark(String outText, boolean[][] coverage, int limitRow, int limitCol) { 
        if(coverage == null)
            coverage = new boolean[limitRow][limitCol];
        int markInt = Integer.parseInt(outText);
        int markRow = markInt / 10;
        int markCol = markInt % 10;
        if(markRow > limitRow) { 
            return "Row too large";
        }
        if(markCol > limitCol) { 
            return "Col too large";
        }
        if(markRow < 0 || markCol < 0) { 
            return "Row or Col is negative";
        }
        TestHelpMethods.mark(markRow, markCol, coverage);
        return "";
    }

    @Override
    public String retrieveTestNameSuffix(ITestResult res) {
        return TestDataProvider.getTestNameLoginSuite(
                res.getMethod().getMethodName(), res.getParameters());
    }
    
}

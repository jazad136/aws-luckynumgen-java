package com.jschway.luckynumgen.tests.handle;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jschway.luckynumgen.Handle;
import com.jschway.luckynumgen.HandleOneNoS3;
import com.jschway.luckynumgen.HandleTwoNoS3;
import com.jschway.luckynumgen.response.LuckyNumbersResponseType;
import com.jschway.luckynumgen.tests.TestBase;
import com.jschway.luckynumgen.tests.TestHelpMethods;
import com.jschway.luckynumgen.tests.config.TestDataProvider;
import java.util.LinkedHashSet;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleTest2 extends TestBase {
    
    boolean[][] coverage;
    private int rowLimit;
    private int colLimit;
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        colLimit = rowLimit = 10;
        coverage = new boolean[rowLimit][colLimit];
    }
    @Test(dataProviderClass = TestDataProvider.class, 
          dataProvider = "methodDataProviderLogicSuite")
    public void genThreeNumbers1x(String inputNumberIn) throws JsonProcessingException { 
        HandleOneNoS3 handleOne = new HandleOneNoS3();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        APIGatewayProxyResponseEvent response = handleOne.handleRequest(Handle.getRequest(inputNumberIn), Handle.getTestContext());
        ObjectMapper mapper = new ObjectMapper();
        LuckyNumbersResponseType body = mapper.readValue(response.getBody(), LuckyNumbersResponseType.class);
        selections.add(body.getNumber1());
        selections.add(body.getNumber2());
        selections.add(body.getNumber3());
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
    
    @Test(dataProviderClass = TestDataProvider.class, 
          dataProvider = "methodDataProviderLogicSuite")
    public void genThreeNumbers2x(String inputNumberIn) throws JsonProcessingException { 
        HandleTwoNoS3 handler = new HandleTwoNoS3();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        APIGatewayProxyResponseEvent response = handler.handleRequest(Handle.getRequest(inputNumberIn), Handle.getTestContext());
        ObjectMapper mapper = new ObjectMapper();
        LuckyNumbersResponseType body = mapper.readValue(response.getBody(), LuckyNumbersResponseType.class);
        selections.add(body.getNumber1());
        selections.add(body.getNumber2());
        selections.add(body.getNumber3());
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
    public void genThreeNumbers3x(String inputNumberIn) throws JsonProcessingException { 
        HandleTwoNoS3 handler = new HandleTwoNoS3();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        APIGatewayProxyResponseEvent response = handler.handleRequest(Handle.getRequest(inputNumberIn), Handle.getTestContext());
        ObjectMapper mapper = new ObjectMapper();
        LuckyNumbersResponseType body = mapper.readValue(response.getBody(), LuckyNumbersResponseType.class);
        selections.add(body.getNumber1());
        selections.add(body.getNumber2());
        selections.add(body.getNumber3());
        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        int genNum = 1;
        for(String outText : selections) {
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", genNum, result));
            genNum++;
        }
    }
//    public List<String> newSetLuckyNumbers() { 
//        int select = gen.nextInt(9)+1;
//        Handle handle = new Handle();
//        LinkedHashSet<String> selections = new LinkedHashSet<>();
//        List<String> outTexts = handle.generateThree(""+select);
//        selections.addAll(outTexts);
//        
//        try{Thread.sleep(1000);} catch(InterruptedException e) {throw new RuntimeException(e);}
//        int genNum = 1;
//        for(String outText : selections) {
//            String result = mark(outText, coverage, rowLimit, colLimit);
//            if(!result.isEmpty())
//                fail(String.format("Gen %d: %s", genNum, result));
//            genNum++;
//        }
//        return new LinkedList<>(selections);
//    }
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

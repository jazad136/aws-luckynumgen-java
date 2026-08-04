package com.jschway.luckynumgen.tests;

import com.jschway.luckynumgen.Handle;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MethodologyTest {
    
    Random gen;
    boolean[][] coverage;
    private int rowLimit;
    private int colLimit;
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        gen = new Random();
        rowLimit = 10;
        coverage = new boolean[rowLimit][colLimit];
        colLimit = 10;
    }
    
    @Test
    public void testJustGettingNumbers() throws InterruptedException { 
        int select = gen.nextInt(9)+1;
        Handle handle = new Handle();
        LinkedHashSet<String> selections = new LinkedHashSet<>();
        List<String> outTexts = handle.generateThree(""+select);
        selections.addAll(outTexts);
        
        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
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
}

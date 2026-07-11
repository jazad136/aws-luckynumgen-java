package com.jschway.luckynumgen;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MethodologyTest {
    
    Random gen;
    boolean[][] coverage;
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        gen = new Random();
    }
    @Test
    public void testJustGettingNumbers() throws InterruptedException { 
        int select = gen.nextInt(9)+1;
        Set<String> selections = new TreeSet<>();
        String outText = Handle.generateOne(""+select);
        
        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        
        selections.add(outText);
        TestHelpMethods.mark(outText, coverage);
    }
}

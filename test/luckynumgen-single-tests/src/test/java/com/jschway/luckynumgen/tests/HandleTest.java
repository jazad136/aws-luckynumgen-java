package com.jschway.luckynumgen.tests;

import static com.jschway.luckynumgen.tests.TestHelpMethods.setupLogger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.openqa.selenium.devtools.latest.animation.Animation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import static org.testng.Assert.fail;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleTest {
    private WebDriver driver;
    private Logger logger;
    private Wait<WebDriver> wait;
    private int rowLimit;
    private int colLimit;
    private boolean[][] coverage;
    Random gen;
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        setupLogger();
        driver = new ChromeDriver();
        gen = new Random();
        colLimit = rowLimit = 10;
        coverage = new boolean[rowLimit][colLimit];
        wait = TestHelpMethods.setupWait(driver);
        driver.get("file:///Users/jsaddle/SrcCode/tryout/java/samjava-example5/LuckynumgenIntegration3/webapp/LuckynumgenWebapp/local-test/index.html");
        
    }
    
    @AfterMethod(alwaysRun=true)
    public void tearDown() { 
        driver.quit();
    }
    

    
    @Test
    public void testOneNumber() throws InterruptedException { 
        WebElement[] selectors = setUpButtons(driver);
        WebElement[] outputs = setUpOutputs(driver);
        WebElement generate = generateButton(driver);
        Set<String> selections = new TreeSet<>();
        int select = gen.nextInt(10);
        // click the button
        selectors[select].click();
        // wait a second on purpose
        try{Thread.sleep(3000);} catch(InterruptedException e) {throw e;}
        // click the generate button
        generate.click();
        wait.until(ExpectedConditions.attributeToBeNotEmpty(outputs[0], "value"));
        for(int i = 0; i < outputs.length; i++) { 
            String outText = outputs[i].getAttribute("value");
            if(outText.isEmpty()) 
                fail(String.format("Gen %d: %s", i, "Empty output retrieved"));
            if(outText.equals("undefined")) 
                fail(String.format("Gen %d: %s", i, "Undefined output retrieved"));
            selections.add(outText);
            String result = mark(outText, coverage, rowLimit, colLimit);
            if(!result.isEmpty())
                fail(String.format("Gen %d: %s", i, result));
        }
        String outText = outputs[0].getAttribute("value");
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
    
    /**
     * Return false if one of the cells in the coverage area
     * is not covered, else return true. 
     * @param coverageRows
     * @param coverageCols
     * @return 
     */
    public boolean testCompleted(int coverageRows, int coverageCols) { 
        for(int i = 0; i < coverageRows; i++) { 
            for(int j = 0; j < coverageCols; j++) { 
                if(coverage[i][j] == false)
                    return false;
            }
        }
        return true;
    }
    public boolean covered(String input) { 
        int toMark = Integer.parseInt(input);
        int row = toMark / 10;
        int col = toMark % 10;
        return coverage[row][col];
    }
    private WebElement generateButton(WebDriver driver) { 
        return driver.findElement(By.id("generate"));
    }
    private WebElement[] setUpOutputs(WebDriver driver) { 
        WebElement[] outputs = new WebElement[3];
        outputs[0] = driver.findElement(By.id("output1"));
        outputs[1] = driver.findElement(By.id("output2"));
        outputs[2] = driver.findElement(By.id("output3"));
        return outputs;
    }
    private WebElement[] setUpButtons(WebDriver driver) { 
        WebElement[] selectors = new WebElement[10];
        selectors[0] = driver.findElement(By.id("sel1"));
        selectors[1] = driver.findElement(By.id("sel2"));
        selectors[2] = driver.findElement(By.id("sel3"));
        selectors[3] = driver.findElement(By.id("sel4"));
        selectors[4] = driver.findElement(By.id("sel5"));
        selectors[5] = driver.findElement(By.id("sel6"));
        selectors[6] = driver.findElement(By.id("sel7"));
        selectors[7] = driver.findElement(By.id("sel8"));
        selectors[8] = driver.findElement(By.id("sel9"));
        return selectors;
    }
}

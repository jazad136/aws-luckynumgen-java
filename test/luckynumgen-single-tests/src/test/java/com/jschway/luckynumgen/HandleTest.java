package com.jschway.luckynumgen;

import static com.jschway.luckynumgen.TestHelpMethods.setupLogger;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleTest {
    private WebDriver driver;
    private Logger logger;
    private Wait<WebDriver> wait;
    private boolean[][] coverage;
    Random gen;
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() { 
        setupLogger();
        driver = new ChromeDriver();
        coverage = new boolean[10][10];
        wait = TestHelpMethods.setupWait(driver);
        driver.get("https://jschway.com/");
        gen = new Random();
    }
    
    @AfterMethod(alwaysRun=true)
    public void tearDown() { 
        driver.quit();
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
    @Test
    public void testAllNumbers() throws InterruptedException { 
        WebElement[] selectors = setUpButtons(driver);
        WebElement[] outputs = setUpOutputs(driver);
        WebElement generate = generateButton(driver);
        Set<String> selections = new TreeSet<>();
        int select = gen.nextInt(10);
        // click the button
        selectors[select].click();
        // wait a second on purpose
        try{Thread.sleep(1000);} catch(InterruptedException e) {throw e;}
        // click the generate button
        generate.click();
        String outText = outputs[0].getText();
        selections.add(outText);
        TestHelpMethods.mark(outText, coverage);
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
        WebElement[] outputs = new WebElement[1];
        outputs[0] = driver.findElement(By.id("output1"));
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

package com.jschway.luckynumgen;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class TestHelpMethods {
    
    public static Wait<WebDriver> setupWait(WebDriver driver) { 
        return new FluentWait<>(driver)
                .withTimeout(Duration.of(10000, ChronoUnit.MILLIS))
                .pollingEvery(Duration.of(2000, ChronoUnit.MILLIS))
                .ignoring(NoSuchElementException.class);
    }
    
    public static void setupLogger() {
        
    }
    public static class Log { 
        static Logger logger;
        public Log() { 
            logger = Logger.getLogger(HandleTest.class.getName());
        }
    }
    
    public  static void mark(int toMark, boolean[][] coverage) { 
        int row = toMark / 10;
        int col = toMark % 10;
        coverage[row][col] = true;
    }
    public static void mark(String input, boolean[][] coverage) { 
        int toMark = Integer.parseInt(input);
        int row = toMark / 10;
        int col = toMark % 10;
        coverage[row][col] = true;
    }
}

package com.jschway.luckynumgen.tests.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 *
 * @author Jonathan Saddler
 */
public class ExtentReportManager {
    private static ExtentReports extent;
    private ExtentReportManager() { } 
    
    public static ExtentReports getReporter() {
        if(extent == null) { 
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter("extent-report.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setReportName("Lucky Num Gen Tests Extent Report");
            spark.config().setDocumentTitle("Lucky Num Gen Tests Extent Report");
            extent.attachReporter(spark);
        }
        return extent;
    }
    
}

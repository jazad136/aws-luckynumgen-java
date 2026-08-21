package com.jschway.luckynumgen.tests;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.jschway.luckynumgen.tests.config.ExtentReportManager;
import com.jschway.luckynumgen.tests.config.TestDataProvider;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.asserts.SoftAssert;

/**
 *
 * @author JonathanSaddler
 */
public abstract class ExTestBase {
    protected ExtentReports extent;
    protected ExtentTest testReport;
    protected SoftAssert softAssert;
    private String testNameSuffix;
    
    WebDriver driver;
    String testNamePrepend;
    String testUrl;
    
    @Factory(dataProviderClass = TestDataProvider.class, dataProvider = "dataProviderScenario1Tests")
    public ExTestBase(String testPrepend) {
        this.testNamePrepend = testPrepend;
        this.testUrl = "https://jqueryui.com"; // default is jQueryUI.com
    }
    
    @BeforeMethod
    public void setup(ITestResult res) {
        this.extent = ExtentReportManager.getReporter();
        this.softAssert = new SoftAssert();
        this.testNameSuffix = retrieveTestNameSuffix(res);
        this.testReport = extent.createTest(getFullTestName(testNamePrepend));
        res.setAttribute("reporterObject", testReport);
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
//        driver.get(testUrl);
    }
    
    @AfterMethod
    public void finish() { 
        extent.flush();
//        driver.quit();
    }
    public String getFullTestName(String prepend) {  return prepend + " " + testNameSuffix; }
    protected void setTestNameSuffix(String testNameSuffix) { this.testNameSuffix = testNameSuffix; } 
    protected String getTestNameSuffix() { return testNameSuffix; }
    public abstract String retrieveTestNameSuffix(ITestResult res);
    public void setTestUrl(String testUrl) { this.testUrl = testUrl; } 
    
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

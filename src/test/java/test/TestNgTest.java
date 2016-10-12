package test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.vimalselvam.testng.listener.ExtentTestNgFormatter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * Created by vimalrajselvam on 10/10/16.
 */
public class TestNgTest {

    @BeforeSuite
    public void beforeSuite() {
        ExtentTestNgFormatter.addSystemInfo("Environment", "Sandbox");
        ExtentTestNgFormatter.addSystemInfo("Selenium Version", "2.53.1");
    }

    @BeforeMethod
    public void beforeMethod(ITestResult iTestResult) {
//        this.iTestResult = iTestResult;
    }

    @AfterMethod
    public void afterMethod(ITestResult iTestResult) {
        ExtentTestNgFormatter.attachScreenshot(iTestResult, "/Users/vimalrajselvam/development/extentreports/target/test-classes/1.png");
        Reporter.log("After Method: " + iTestResult.getMethod().getMethodName());
//        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
//
//        test.addScreenCapture("/Users/vselvam/development/extentreports/target/test-classes/1.png");
    }

    @Test(groups = {"group1"})
    public void simpleTest() {
        System.err.println("This is simple test - Thread: " + Thread.currentThread().getId());
        Reporter.log("Simple test");
        Reporter.log("Another log");
    }

    @Test(enabled = true)
    public void anotherSimpleTest() {
        System.err.println("This is another simple test - Thread: " + Thread.currentThread().getId());
        Reporter.log("Another Simple test");
    }
}

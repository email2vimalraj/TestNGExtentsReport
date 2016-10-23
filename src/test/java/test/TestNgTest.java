package test;

import com.vimalselvam.testng.listener.ExtentTestNgFormatter;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * This is a sample testng test.
 */
public class TestNgTest {

    @BeforeSuite
    public void beforeSuite() {
        ExtentTestNgFormatter.setSystemInfo("Environment", "Sandbox");
        ExtentTestNgFormatter.setSystemInfo("Selenium Version", "2.53.1");
    }

    @AfterMethod
    public void afterMethod(ITestResult iTestResult) throws IOException {
        ExtentTestNgFormatter.addScreenCaptureFromPath(iTestResult,
                "/Users/vimalrajselvam/development/extentreports/target/test-classes/1.png");
        Reporter.log("After Method: " + iTestResult.getMethod().getMethodName());
    }

    @Test(groups = {"group1"})
    public void simpleTest() {
        System.err.println("This is simple test - Thread: " + Thread.currentThread().getId());
        Reporter.log("Simple test");
        Reporter.log("Another log");
    }

    @Test(groups = {"group2"})
    public void anotherSimpleTest() {
        System.err.println("This is another simple test - Thread: " + Thread.currentThread().getId());
        Reporter.log("Another Simple test");
    }
}

package com.vimalselvam.testng.listener;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class houses the listener for the TestNG which generates the html report by using Extent Report.
 */
public class ExtentTestNgFormatter implements ISuiteListener, ITestListener, IClassListener {
//    private ExtentReports reporter;
//    private ExtentTest test;
    private static Map<String, String> systemInfo;

    public ExtentTestNgFormatter() {
        systemInfo = new HashMap<String, String>();
    }

    public void onStart(ISuite iSuite) {
//        System.err.println("Suite Name: " + iSuite.getName());
//        System.out.println("Report Path: " + iSuite.getParameter("report_path"));
        ExtentReports reporter = new ExtentReports(iSuite.getParameter("report_path"));
        ExtentTest suite = reporter.startTest(iSuite.getName());
        iSuite.setAttribute("reporter", reporter);
        iSuite.setAttribute("suite", suite);
    }

    public void onFinish(ISuite iSuite) {
//        System.err.println("Suite Status: ");
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest suite = (ExtentTest) iSuite.getAttribute("suite");
        reporter.endTest(suite);
        reporter.addSystemInfo(systemInfo);
        reporter.flush();
    }


    public void onTestStart(ITestResult iTestResult) {
//        System.err.println("Test Name: " + iTestResult.getName());
        ITestContext iTestContext = iTestResult.getTestContext();
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest testContext = (ExtentTest) iTestContext.getAttribute("testContext");
        ExtentTest test = reporter.startTest(iTestResult.getName());
        String[] groups = iTestResult.getMethod().getGroups();
        test.assignCategory("Group 1");
        testContext.appendChild(test);
        iTestResult.setAttribute("test", test);
    }

    public void onTestSuccess(ITestResult iTestResult) {
        ITestContext iTestContext = iTestResult.getTestContext();
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.log(LogStatus.PASS, "Passed");
        List<String> logs = Reporter.getOutput(iTestResult);
        for (String log : logs) {
            test.log(LogStatus.INFO, log);
        }
//        test.addScreenCapture("/Users/vselvam/development/extentreports/target/test-classes/1.png");
        reporter.endTest(test);
    }

    public void onTestFailure(ITestResult iTestResult) {
        ITestContext iTestContext = iTestResult.getTestContext();
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.log(LogStatus.FAIL, iTestResult.getThrowable());
        reporter.endTest(test);
    }

    public void onTestSkipped(ITestResult iTestResult) {
        ITestContext iTestContext = iTestResult.getTestContext();
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.log(LogStatus.SKIP, "Skipped");
        reporter.endTest(test);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    public void onStart(ITestContext iTestContext) {
//        System.err.println("Test Context Name: " + iTestContext.getName());
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest suite = (ExtentTest) iSuite.getAttribute("suite");
        ExtentTest testContext = reporter.startTest(iTestContext.getName());
        suite.appendChild(testContext);
        iTestContext.setAttribute("testContext", testContext);
    }

    public void onFinish(ITestContext iTestContext) {
        ISuite iSuite = iTestContext.getSuite();
        ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
        ExtentTest testContext = (ExtentTest) iTestContext.getAttribute("testContext");

        if (iTestContext.getFailedTests().size() > 0) {
            testContext.log(LogStatus.FAIL, "Failed");
        } else if (iTestContext.getSkippedTests().size() > 0) {
            testContext.log(LogStatus.SKIP, "Skipped");
        } else {
            testContext.log(LogStatus.PASS, "Passed");
        }
        reporter.endTest(testContext);
    }

    public void onBeforeClass(ITestClass iTestClass) {
//        System.err.println("Test class name: " + iTestClass.getName());
    }

    public void onAfterClass(ITestClass iTestClass) {

    }

    public static void attachScreenshot(ITestResult iTestResult, String filePath) {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.log(LogStatus.INFO, test.addScreenCapture(filePath));
    }

    public static void addSystemInfo(String param, String value) {
        systemInfo.put(param, value);
    }

}

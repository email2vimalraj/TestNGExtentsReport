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
public class ExtentTestNgFormatter implements ISuiteListener, ITestListener, IInvokedMethodListener {
//    private ExtentReports reporter;
//    private ExtentTest test;
    private static Map<String, String> systemInfo;

    public ExtentTestNgFormatter() {
        systemInfo = new HashMap<String, String>();
    }

    public void onStart(ISuite iSuite) {
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
        reporter.close();
    }


    public void onTestStart(ITestResult iTestResult) {

    }

    public void onTestSuccess(ITestResult iTestResult) {

    }

    public void onTestFailure(ITestResult iTestResult) {

    }

    public void onTestSkipped(ITestResult iTestResult) {

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

    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            ITestContext iTestContext = iTestResult.getTestContext();
            ISuite iSuite = iTestContext.getSuite();
            ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
            ExtentTest testContext = (ExtentTest) iTestContext.getAttribute("testContext");
            ExtentTest test = reporter.startTest(iTestResult.getName());
            testContext.appendChild(test);
            iTestResult.setAttribute("test", test);
        }
    }

    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            ITestContext iTestContext = iTestResult.getTestContext();
            ISuite iSuite = iTestContext.getSuite();
            ExtentReports reporter = (ExtentReports) iSuite.getAttribute("reporter");
            ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
            List<String> logs = Reporter.getOutput(iTestResult);
            for (String log : logs) {
                test.log(LogStatus.INFO, log);
            }

            int status = iTestResult.getStatus();
            if (ITestResult.SUCCESS == status) {
                test.log(LogStatus.PASS, "Passed");
            } else if (ITestResult.FAILURE == status) {
                test.log(LogStatus.FAIL, "Failed", iTestResult.getThrowable());
            } else {
                test.log(LogStatus.SKIP, "Skipped");
            }

            reporter.endTest(test);
        }
    }

    public static void attachScreenshot(ITestResult iTestResult, String filePath) {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.log(LogStatus.INFO, test.addScreenCapture(filePath));
    }

    public static void addSystemInfo(String param, String value) {
        systemInfo.put(param, value);
    }
}

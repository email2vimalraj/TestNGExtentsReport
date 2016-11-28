package com.vimalselvam.testng.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class houses the listener for the TestNG which generates the html report by using Extent Report.
 */
public class ExtentTestNgFormatter implements ISuiteListener, ITestListener, IInvokedMethodListener, IReporter {
    private ExtentReports reporter;
    private static Map<String, String> systemInfo;
    private static List<String> testRunnerOutput;

    public ExtentTestNgFormatter() throws IOException {
        systemInfo = new HashMap<String, String>();
        testRunnerOutput = new ArrayList<String>();
        String reportPath = System.getProperty("extentReportPath");
        if (reportPath == null) {
            File file = new File(TestNG.DEFAULT_OUTPUTDIR);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new IOException("Failed to create output run directory");
                }
            }
            reportPath = file.getAbsolutePath() + File.separator + "report.html";
        }
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
        reporter = new ExtentReports();
        reporter.attachReporter(htmlReporter);
    }

    public void onStart(ISuite iSuite) {
        ExtentTest suite = reporter.createTest(iSuite.getName());
        iSuite.setAttribute("reporter", reporter);
        iSuite.setAttribute("suite", suite);
    }

    public void onFinish(ISuite iSuite) {
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
        ISuite iSuite = iTestContext.getSuite();
        ExtentTest suite = (ExtentTest) iSuite.getAttribute("suite");
        ExtentTest testContext = suite.createNode(iTestContext.getName());
        iTestContext.setAttribute("testContext", testContext);
    }

    public void onFinish(ITestContext iTestContext) {
        ExtentTest testContext = (ExtentTest) iTestContext.getAttribute("testContext");
        if (iTestContext.getFailedTests().size() > 0) {
            testContext.fail("Failed");
        } else if (iTestContext.getSkippedTests().size() > 0) {
            testContext.skip("Skipped");
        } else {
            testContext.pass("Passed");
        }
    }

    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            ITestContext iTestContext = iTestResult.getTestContext();
            ExtentTest testContext = (ExtentTest) iTestContext.getAttribute("testContext");
            ExtentTest test = testContext.createNode(iTestResult.getName());
            iTestResult.setAttribute("test", test);
        }
    }

    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
            List<String> logs = Reporter.getOutput(iTestResult);
            for (String log : logs) {
                test.info(log);
            }

            int status = iTestResult.getStatus();
            if (ITestResult.SUCCESS == status) {
                test.pass("Passed");
            } else if (ITestResult.FAILURE == status) {
                test.fail(iTestResult.getThrowable());
            } else {
                test.skip("Skipped");
            }

            for (String group : iInvokedMethod.getTestMethod().getGroups()) {
                test.assignCategory(group);
            }
        }
    }

    public static void addScreenCaptureFromPath(ITestResult iTestResult, String filePath) throws IOException {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.addScreenCaptureFromPath(filePath);
    }

    public static void setTestRunnerOutput(String message) {
        testRunnerOutput.add(message);
    }

    public static void setSystemInfo(String param, String value) {
        systemInfo.put(param, value);
    }

    public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
        for (Map.Entry<String, String> entry : systemInfo.entrySet()) {
            reporter.setSystemInfo(entry.getKey(), entry.getValue());
        }
        reporter.setTestRunnerOutput(testRunnerOutput);
        reporter.flush();
        reporter.close();
    }
}

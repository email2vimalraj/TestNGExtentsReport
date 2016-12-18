package com.vimalselvam.testng.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.vimalselvam.testng.NodeName;
import com.vimalselvam.testng.SystemInfo;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class houses the listener for the TestNG which generates the html report by using Extent Report.
 */
public class ExtentTestNgFormatter implements ISuiteListener, ITestListener, IInvokedMethodListener, IReporter {
    private static final String REPORTER_ATTR = "extentTestNgReporter";
    private static final String SUITE_ATTR = "extentTestNgSuite";
    private ExtentReports reporter;
    private List<String> testRunnerOutput;
    private Map<String, String> systemInfo;
    private ExtentHtmlReporter htmlReporter;

    private static ExtentTestNgFormatter instance;

    public ExtentTestNgFormatter() {
        setInstance(this);
        testRunnerOutput = new ArrayList<>();
        String reportPath = System.getProperty("extentReportPath");
        if (reportPath == null) {
            File file = new File(TestNG.DEFAULT_OUTPUTDIR);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new RuntimeException("Failed to create output run directory");
                }
            }
            reportPath = file.getAbsolutePath() + File.separator + "report.html";
        }
        htmlReporter = new ExtentHtmlReporter(reportPath);
        reporter = new ExtentReports();
        reporter.attachReporter(htmlReporter);
    }

    /**
     * Gets the instance of the {@link ExtentTestNgFormatter}
     *
     * @return The instance of the {@link ExtentTestNgFormatter}
     */
    public static ExtentTestNgFormatter getInstance() {
        return instance;
    }

    private static void setInstance(ExtentTestNgFormatter formatter) {
        instance = formatter;
    }

    public void onStart(ISuite iSuite) {
        ExtentTest suite = reporter.createTest(iSuite.getName());

        String configFile = iSuite.getParameter("report.config");

        if (!Strings.isNullOrEmpty(configFile)) {
            htmlReporter.loadXMLConfig(configFile);
        }

        String systemInfoCustomImplName = iSuite.getParameter("system.info");
        if (!Strings.isNullOrEmpty(systemInfoCustomImplName)) {
            generateSystemInfo(systemInfoCustomImplName);
        }

        iSuite.setAttribute(REPORTER_ATTR, reporter);
        iSuite.setAttribute(SUITE_ATTR, suite);
    }

    private void generateSystemInfo(String systemInfoCustomImplName) {
        try {
            Class<?> systemInfoCustomImplClazz = Class.forName(systemInfoCustomImplName);
            if (!SystemInfo.class.isAssignableFrom(systemInfoCustomImplClazz)) {
                throw new IllegalArgumentException("The given system.info class name <" + systemInfoCustomImplName +
                        "> should implement the interface <" + SystemInfo.class.getName() + ">");
            }

            SystemInfo t = (SystemInfo) systemInfoCustomImplClazz.newInstance();
            this.systemInfo = t.getSystemInfo();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
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
        ExtentTest suite = (ExtentTest) iSuite.getAttribute(SUITE_ATTR);
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

    /**
     * Adds a screen shot image file to the report. This method should be used only in the configuration method
     * and the {@link ITestResult} is the mandatory parameter
     *
     * @param iTestResult The {@link ITestResult} object
     * @param filePath The image file path
     * @throws IOException
     */
    public void addScreenCaptureFromPath(ITestResult iTestResult, String filePath) throws IOException {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.addScreenCaptureFromPath(filePath);
    }

    /**
     * Adds a screen shot image file to the report. This method should be used only in the
     * {@link org.testng.annotations.Test} annotated method
     *
     * @param filePath The image file path
     * @throws IOException
     */
    public void addScreenCaptureFromPath(String filePath) throws IOException {
        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Preconditions.checkState(iTestResult != null);
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.addScreenCaptureFromPath(filePath);
    }

    /**
     * Sets the test runner output
     *
     * @param message The message to be logged
     */
    public void setTestRunnerOutput(String message) {
        testRunnerOutput.add(message);
    }

    public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
        if (systemInfo != null) {
            for (Map.Entry<String, String> entry : systemInfo.entrySet()) {
                reporter.setSystemInfo(entry.getKey(), entry.getValue());
            }
        }
        reporter.setTestRunnerOutput(testRunnerOutput);
        reporter.flush();
    }

    /**
     * Adds the new node to the test. The node name should have been set already using {@link NodeName}
     */
    public void addNewNodeToTest() {
        addNewNodeToTest(NodeName.getNodeName());
    }

    /**
     * Adds the new node to the test with the given node name.
     *
     * @param nodeName The name of the node to be created
     */
    public void addNewNodeToTest(String nodeName) {
        addNewNode("test", nodeName);
    }

    /**
     * Adds a new node to the suite. The node name should have been set already using {@link NodeName}
     */
    public void addNewNodeToSuite() {
        addNewNodeToSuite(NodeName.getNodeName());
    }

    /**
     * Adds a new node to the suite with the given node name
     *
     * @param nodeName The name of the node to be created
     */
    public void addNewNodeToSuite(String nodeName) {
        addNewNode(SUITE_ATTR, nodeName);
    }

    private void addNewNode(String parent, String nodeName) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest parentNode = (ExtentTest) result.getAttribute(parent);
        ExtentTest childNode = parentNode.createNode(nodeName);
        result.setAttribute(nodeName, childNode);
    }

    /**
     * Adds a info log message to the node. The node name should have been set already using {@link NodeName}
     *
     * @param logMessage The log message string
     */
    public void addInfoLogToNode(String logMessage) {
        addInfoLogToNode(logMessage, NodeName.getNodeName());
    }

    /**
     * Adds a info log message to the node
     * @param logMessage The log message string
     * @param nodeName The name of the node
     */
    public void addInfoLogToNode(String logMessage, String nodeName) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.info(logMessage);
    }

    /**
     * Marks the node as failed. The node name should have been set already using {@link NodeName}
     *
     * @param t The {@link Throwable} object
     */
    public void failTheNode(Throwable t) {
        failTheNode(NodeName.getNodeName(), t);
    }

    /**
     * Marks the given node as failed
     *
     * @param t The {@link Throwable} object
     */
    public void failTheNode(String nodeName, Throwable t) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.fail(t);
    }

    /**
     * Marks the node as failed. The node name should have been set already using {@link NodeName}
     *
     * @param logMessage The message to be logged
     */
    public void failTheNode(String logMessage) {
        failTheNode(NodeName.getNodeName(), logMessage);
    }

    /**
     * Marks the given node as failed
     *
     * @param logMessage The message to be logged
     */
    public void failTheNode(String nodeName, String logMessage) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.fail(logMessage);
    }
}

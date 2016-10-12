package test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

/**
 * Created by vimalrajselvam on 12/10/16.
 */
public class ExtentReportTest {
    public static void main(String[] args) {
        ExtentReports reporter = new ExtentReports("/Users/vimalrajselvam/development/TestNgExtentReporter/output/report_1.html");
        ExtentTest suite = reporter.startTest("Suite");
        suite.assignCategory("Suite Group");

        ExtentTest testInstance = reporter.startTest("TestInstance");
        testInstance.assignCategory("Test Instance Group");
        suite.appendChild(testInstance);

        ExtentTest test = reporter.startTest("Test");
        test.assignCategory("Test Group");
        testInstance.appendChild(test);

        reporter.endTest(test);
        reporter.endTest(testInstance);
        reporter.endTest(suite);
        reporter.flush();
        reporter.close();
    }
}

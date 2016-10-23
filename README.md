# TestNG Extents Report
The TestNG Extents report is a listener plugin which you can add it as a listener to your testng suite to generate a nice Extent report.

This listener uses the [ExtentReports](http://extentreports.relevantcodes.com/), a library developed by Anshoo Arora for reporting.

## Build Status
[![Build Status](https://travis-ci.org/email2vimalraj/TestNGExtentsReport.svg?branch=master)](https://travis-ci.org/email2vimalraj/TestNGExtentsReport)

## Why another library?
You don't have to spend time in re-inventing on how to define what. Just add this as a listener or [service loader](http://testng.org/doc/documentation-main.html#listeners-service-loader). You will get the nice looking report generated at the end of your test execution.

## Usage
For maven, add the following as dependency:

```
<dependency>
    <groupId>com.vimalselvam</groupId>
    <artifactId>testng-extentsreport</artifactId>
    <version>1.0.0</version>
</dependency>
```

Either in your **testng.xml**, add the listener:

```
<listeners>
    <listener class-name="com.vimalselvam.testng.listener.ExtentTestNgFormatter" />
</listeners>
```

or add as a [service loader](http://testng.org/doc/documentation-main.html#listeners-service-loader).

By default, the report will be generated at `output/Run_<timestamp>` directory.

In case you want to generate the report in a different location, make sure you pass the JVM argument called `reportPath` with the absolute file path.

*For example*: I run my maven test as: `mvn clean test -DreportPath=<filepath>`

In case you want to add system information, call this in your `@BeforeSuite`:

```
ExtentTestNgFormatter.setSystemInfo("Environment", "Sandbox");
ExtentTestNgFormatter.setSystemInfo("Selenium Version", "2.53.1");
```

To add the screenshot,

```
ExtentTestNgFormatter.addScreenCaptureFromPath(iTestResult, filePath);
```

Please note that `ITestResult` is the required parameter. For example, I attach the screenshots usually in the `@AfterMethod` method like this:

```
@AfterMethod
public void afterMethod(ITestResult iTestResult) throws IOException {
    ExtentTestNgFormatter.addScreenCaptureFromPath(iTestResult, filePath);
}
```

If any case, you want to attach the `info` log on your test method, you simply call TestNg's `Reporter.log` method which will be added to your test log. For example:

```
@Test
public void testMethod() {
    Reporter.log("Custom log");
}
```

To add the test runner output, simply call from anywhere:

```
ExtentTestNgFormatter.setTestRunnerOutput("My output");
```

## Cucumber?
Refer my another library which generates the same report for the cucumber based BDD tests: [CucumberExtentReporter](https://github.com/email2vimalraj/CucumberExtentReporter)
package test;

import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * Created by vimalrajselvam on 10/10/16.
 */
public class AnotherTestNgTest {
    @Test(dependsOnMethods = {"newTest"})
    public void anotherTestNgSimpleTest() {
        System.err.println("This is another testng simple test - Thread: " + Thread.currentThread().getId());
        Reporter.log("Another Testng Simple Test");
    }

    @Test()
    public void newTest() {
        System.err.println("This is new test - Thread: " + Thread.currentThread().getId());
        Reporter.log("New test");
    }
}

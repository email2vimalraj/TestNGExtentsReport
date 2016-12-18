package test;

import com.vimalselvam.testng.NodeName;
import com.vimalselvam.testng.listener.ExtentTestNgFormatter;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.testng.internal.collections.Ints;

import java.util.List;

/**
 * This test class demonstrates on how do we create new nodes under the test
 */
public class DemoTestClass {

    // The instance of the ExtentTestNgFormatter can be obtained as follows
    private ExtentTestNgFormatter formatter = ExtentTestNgFormatter.getInstance();

    @DataProvider
    public Object[][] myDp() {
        return new Object[][] {
            {
                    Ints.asList(1, 2, 3)
            },
            {
                    Ints.asList(4, 5, 6)
            }
        };
    }

    @Test(dataProvider = "myDp")
    public void testMethod(List<Integer> myList) {
        for (Integer item : myList) {
            String nodeName = "Iteration - " + item;

            // Create a new node name
            NodeName.setNodeName(nodeName);

            // Add a new node under the current test
            formatter.addNewNodeToTest();

            // The info logs can be added to the node using addInfoLogToNode
            formatter.addInfoLogToNode("Test Log");

            try {
                if (item == 5) {
                    Assert.assertTrue(false);
                }
                Assert.assertTrue(true);
            } catch (AssertionError e) {
                // The node can be failed using failTheNode method by bassing either the throwable or log message
                formatter.failTheNode(e);
                throw e;
            }
        }
    }
}

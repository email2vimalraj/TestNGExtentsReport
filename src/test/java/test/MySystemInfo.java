package test;

import com.vimalselvam.testng.SystemInfo;
import org.testng.collections.Maps;

import java.util.Map;

/**
 * This is a small utility class to prepare the system information
 */
public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {
        Map<String, String> systemInfo = Maps.newHashMap();
        systemInfo.put("Test Env", "QA");
        systemInfo.put("Browser", "firefox");
        return systemInfo;
    }
}

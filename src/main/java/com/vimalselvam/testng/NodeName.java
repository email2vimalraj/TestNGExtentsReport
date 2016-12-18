package com.vimalselvam.testng;

/**
 * This is a helper class to set and get the node name
 */
public class NodeName {
    private static final ThreadLocal<String> nodeName = new ThreadLocal<>();

    public static String getNodeName() {
        return nodeName.get();
    }

    public static void setNodeName(String name) {
        nodeName.set(name);
    }
}

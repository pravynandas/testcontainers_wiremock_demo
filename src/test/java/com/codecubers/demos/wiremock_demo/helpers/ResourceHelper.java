package com.codecubers.demos.wiremock_demo.helpers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHelper {

    public static ClassLoader getCallerClassLoader() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Iterate through the stack trace to find the actual caller
        // The first element is getStackTrace(), the second is getCallerClassLoader(),
        // so the actual caller is typically at index 2 or higher, depending on utility methods.
        // Adjust the index based on your specific call stack.
        int callerIndex = 2; // Assuming direct call to getCallerClassLoader()

        if (stackTrace.length > callerIndex) {
            String callerClassName = stackTrace[callerIndex].getClassName();
            try {
                Class<?> callerClass = Class.forName(callerClassName);
                return callerClass.getClassLoader();
            } catch (ClassNotFoundException e) {
                System.err.println("Caller class not found: " + callerClassName);
            }
        }
        return null; // Or throw an exception if caller class loader cannot be determined
    }

    public static Path getResourcePath(String resourceName) throws IOException, URISyntaxException {
        //ClassLoader classLoader = WiremockDemoApplicationTests.class.getClassLoader();
        ClassLoader classLoader = getCallerClassLoader();
        assert classLoader != null;
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        URI resourceUri = resourceUrl.toURI();
        return Paths.get(resourceUri);
    }
}

package org.netbeans.gradle.model.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import org.gradle.api.Project;

public final class ClassLoaderUtils {
    private static final AtomicReference<File> JAR_OF_THIS_PROJECT = new AtomicReference<File>(null);

    public static Class<?> tryGetClass(Project project, String className) {
        ClassLoader classLoaderOfScript = project.getBuildscript().getClassLoader();

        try {
            return classLoaderOfScript.loadClass(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static File findClassPathOfClass(Class<?> cl) {
        String className = cl.getName();
        String classFileName = cl.getName().replace('.', '/') + ".class";
        URL urlOfClassPath = cl.getClassLoader().getResource(classFileName);
        if (urlOfClassPath == null) {
            throw new IllegalArgumentException("Unable to locate classpath of " + cl);
        }

        File fileOfURL = extractPathFromURL(urlOfClassPath);

        String protocol = urlOfClassPath.getProtocol();
        if ("jar".equals(protocol)) {
            return fileOfURL;
        }
        else if ("file".equals(protocol)) {
            String[] pathParts = className.split("\\.");
            pathParts[pathParts.length - 1] = pathParts[pathParts.length - 1] + ".class";

            File root = fileOfURL;
            for (int i = pathParts.length - 1; i >= 0; i--) {
                String part = pathParts[i];
                if (!root.getName().equalsIgnoreCase(part)) {
                    throw new IllegalArgumentException("Unexpected path returned for class " + cl + ": " + urlOfClassPath);
                }

                root = root.getParentFile();
                if (root == null) {
                    throw new IllegalArgumentException("Too short path returned for class " + cl + ": " + urlOfClassPath);
                }
            }
            return root;
        }

        throw new IllegalArgumentException("Unexpected protocol for URL: " + urlOfClassPath);
    }

    private static File urlToFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static File extractPathFromURL(URL url) {
        String protocol = url.getProtocol();
        if ("jar".equals(protocol)) {
            String[] pathParts = url.getPath().split("!", 2);
            try {
                URL jarURL = new URL(pathParts[0]);
                return urlToFile(jarURL);
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Unexpected URL: " + url);
            }
        }
        else if ("file".equals(protocol)) {
            return urlToFile(url);
        }
        else {
            throw new IllegalArgumentException("Unexpected URL: " + url);
        }
    }

    public static File getLocationOfClassPath() {
        File result = JAR_OF_THIS_PROJECT.get();
        if (result == null) {
            JAR_OF_THIS_PROJECT.set(findClassPathOfClass(ClassLoaderUtils.class));
            result = JAR_OF_THIS_PROJECT.get();
        }
        return result;
    }

    private ClassLoaderUtils() {
        throw new AssertionError();
    }
}

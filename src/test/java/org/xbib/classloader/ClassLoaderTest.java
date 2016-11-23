package org.xbib.classloader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xbib.classloader.jar.JarFileUrlConnection;
import org.xbib.classloader.uri.URIClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 */
public class ClassLoaderTest {

    @Test
    public void testEmptyClassLoaderString() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        assertEquals("URIClassLoader:", classLoader.toString());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testEmptyClassLoader() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        Class<?> cl = classLoader.loadClass("org.xbib.classloader.uri.URIClassLoader");
    }

    @Test
    public void testClassLoader() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/libs/classloader-2.0.0.jar");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        Class<?> cl = classLoader.loadClass("org.xbib.classloader.uri.URIClassLoader");
        assertTrue(cl != null);
    }

    @Test
    public void testClassLoaderString() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/libs/classloader-2.0.0.jar");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        assertTrue(classLoader.toString().startsWith("URIClassLoader:[file:"));
        assertTrue(classLoader.toString().endsWith("build/libs/classloader-2.0.0.jar]"));
    }

    @Test
    public void testDirectoryClassLoader() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/classes/main");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        Class<?> cl = classLoader.loadClass("org.xbib.classloader.uri.URIClassLoader");
        assertTrue(cl != null);
    }

    @Test
    public void testResourceJarFileURLConnection() throws ClassNotFoundException, IOException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/libs/classloader-2.0.0.jar");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        URL url = classLoader.findResource("org/xbib/classloader/uri/URIClassLoader.class");
        assertTrue(url != null);
        URLConnection urlConnection = url.openConnection();
        assertTrue(urlConnection instanceof JarFileUrlConnection);
        JarFileUrlConnection jarFileUrlConnection = (JarFileUrlConnection) urlConnection;
        jarFileUrlConnection.getJarFile();
        jarFileUrlConnection.getLastModified();
        jarFileUrlConnection.getEntryName();
        jarFileUrlConnection.getManifest();
        jarFileUrlConnection.getAttributes();
        jarFileUrlConnection.getMainAttributes();
        jarFileUrlConnection.getCertificates();
        jarFileUrlConnection.getPermission();
        jarFileUrlConnection.getContentLength();
        jarFileUrlConnection.getInputStream();
        jarFileUrlConnection.getURL();
        assertNotNull(jarFileUrlConnection.toString());
    }

    @Test
    public void testResourceEnumeration() throws ClassNotFoundException, IOException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/libs/classloader-2.0.0.jar");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        Enumeration<URL> urlEnumeration = classLoader.findResources("org/xbib/classloader/uri/URIClassLoader.class");
        assertTrue(urlEnumeration != null);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            URLConnection urlConnection = url.openConnection();
            assertTrue(urlConnection instanceof JarFileUrlConnection);
        }
    }

    @Test
    public void testNativeLibraryLoad() throws Exception {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/classes/test");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        if (System.getProperty("os.name").contains("Mac")) {
            Class<?> cl = classLoader.loadClass("org.xbib.classloader.MacNativeLibraryLoader");
            assertTrue(cl != null);
            Object o = cl.newInstance(); // we can instantiate but not cast
            assertEquals("class org.xbib.classloader.MacNativeLibraryLoader", o.getClass().toString());
        } else {
            System.err.println("still to do, test for " + System.getProperty("os.name"));
        }
    }

    @Test
    public void testSealing() throws ClassNotFoundException {
        URIClassLoader classLoader = new URIClassLoader();
        File f = new File("build/libs/classloader-2.0.0.jar");
        assertTrue(f.exists());
        classLoader.addURI(f.toURI());
        // two clases from same jar
        Class<?> cl1 = classLoader.loadClass("org.xbib.classloader.uri.URIClassLoader");
        assertTrue(cl1 != null);
        Class<?> cl2 = classLoader.loadClass("org.xbib.classloader.uri.URIResourceFinder");
        assertTrue(cl2 != null);
    }
}

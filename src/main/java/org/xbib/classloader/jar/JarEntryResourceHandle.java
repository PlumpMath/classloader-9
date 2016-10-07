package org.xbib.classloader.jar;

import org.xbib.classloader.AbstractResourceHandle;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 */
class JarEntryResourceHandle extends AbstractResourceHandle {

    private final JarEntry jarEntry2;
    private final JarInputStream is;
    private final URL codeSource;

    public JarEntryResourceHandle(JarEntry jarEntry2, JarInputStream is, URL codeSource) {
        this.jarEntry2 = jarEntry2;
        this.is = is;
        this.codeSource = codeSource;
    }

    @Override
    public String getName() {
        return jarEntry2.getName();
    }

    @Override
    public URL getUrl() {
        try {
            return new URL("jar", "", -1, codeSource + "!/" + jarEntry2.getName());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDirectory() {
        return jarEntry2.isDirectory();
    }

    @Override
    public URL getCodeSourceUrl() {
        return codeSource;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return is;
    }

    @Override
    public int getContentLength() {
        return (int) jarEntry2.getSize();
    }
}

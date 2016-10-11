package org.xbib.classloader;

public class MacNativeLibraryLoader {

    public MacNativeLibraryLoader() {
        System.loadLibrary("jdns_sd");
    }
}

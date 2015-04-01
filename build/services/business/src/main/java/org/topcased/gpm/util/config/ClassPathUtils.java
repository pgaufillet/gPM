/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 ******************************************************************/

package org.topcased.gpm.util.config;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang.ArrayUtils;

/**
 * Helper class used to dynamically add entries in the system classpath.
 * <p>
 * This code is severely inspired by a (freely useable ?) example published at
 * http://forum.java.sun.com/thread.jspa?threadID=300557&start=0&tstart=0
 * 
 * @author llatil
 */

public class ClassPathUtils {

    private static final Class<?>[] PARAMS_TYPE = new Class<?>[] { URL.class };

    /**
     * Add a new entry in the system classpath
     * 
     * @param pFilename
     *            Filename to add
     */
    public static void add(String pFilename) {
        File lFile = new File(pFilename);
        add(lFile);
    }

    /**
     * Add a new entry in the system classpath
     * 
     * @param pFile
     *            File to add
     */
    public static void add(File pFile) {
        URL lUrl = null;
        try {
            lUrl = pFile.toURI().toURL();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        add(lUrl);
    }

    /**
     * Add a new entry in the system classpath
     * 
     * @param pUrl
     *            URL to add
     */
    public static void add(URL pUrl) {

        ClassLoader lSysClassLoader = ClassLoader.getSystemClassLoader();
        if (!(lSysClassLoader instanceof URLClassLoader)) {
//            if (staticLogger.isInfoEnabled()) {
//                staticLogger.info("The system class loader does not "
//                        + "inherit from URLClassLoader. URL not added.");
//            }
            return;
        }

        URLClassLoader lUrlClassLoader = (URLClassLoader) lSysClassLoader;
        URL[] lPresentUrls = lUrlClassLoader.getURLs();
        if (ArrayUtils.contains(lPresentUrls, pUrl)) {
//            if (staticLogger.isInfoEnabled()) {
//                staticLogger.info(pUrl
//                        + " is already in the classpath. Not added.");
//            }
            return;
        }

        Class<?> lURLClassLoaderClass = URLClassLoader.class;

        try {
            Method lAddUrlMethod =
                    lURLClassLoaderClass.getDeclaredMethod("addURL",
                            PARAMS_TYPE);
            lAddUrlMethod.setAccessible(true);
            lAddUrlMethod.invoke(lSysClassLoader, new Object[] { pUrl });
//            if (staticLogger.isInfoEnabled()) {
//                staticLogger.info(pUrl + " has been added to the classpath.");
//            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Error, could not add URL to system classloader");
        }
    }
}

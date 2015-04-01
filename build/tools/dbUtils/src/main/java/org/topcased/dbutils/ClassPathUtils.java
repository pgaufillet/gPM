/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 ******************************************************************/

package org.topcased.dbutils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang.ArrayUtils;
//import org.apache.log4j.Logger;

/**
 * Helper class used to dynamically add entries in the system classpath.
 * <p>
 * This code is severely inspired by a (freely usable ?) example published at
 * http://forum.java.sun.com/thread.jspa?threadID=300557&start=0&tstart=0
 * 
 * @author llatil
 */

public class ClassPathUtils {
    // The log4j logger object for this class.
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(ClassPathUtils.class);

    @SuppressWarnings("rawtypes")
    private static final Class[] PARAMS_TYPE = new Class[] { URL.class };

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
        /*try {
			lUrlClassLoader.close();
		} catch (IOException e1) {
			// Should not happen
		}*/
        if (ArrayUtils.contains(lPresentUrls, pUrl)) {
//            if (staticLogger.isInfoEnabled()) {
//                staticLogger.info(pUrl
//                        + " is already in the classpath. Not added.");
//            }
            return;
        }

        Class<URLClassLoader> lURLClassLoaderClass = URLClassLoader.class;

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

    /**
     * Add all JARs present in a given directory
     * 
     * @param pDirName
     *            Path of the JAR directory
     */
    public static void addJarDirectory(String pDirName) {
        File lDir = new File(pDirName);

        if (!lDir.exists() || !lDir.isDirectory()) {
//            staticLogger.error("Invalid directory path '" + pDirName + "'");
            return;
        }

        String[] lJarFilenames = lDir.list(new FilenameFilter() {
            public boolean accept(File pDir, String pName) {
                return pName.endsWith(".jar");
            }
        });

        for (String lJarFilename : lJarFilenames) {
            ClassPathUtils.add(new File(lDir, lJarFilename));
        }
    }
}

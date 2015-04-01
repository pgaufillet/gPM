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
import java.io.FilenameFilter;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * Bean that can be used in a Spring configuration to add entries in the system
 * class path.
 * 
 * @author llatil
 */
public class ClassPathAppender implements InitializingBean {
    /** The log4j logger object for this class. */
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(ClassPathAppender.class);

    private Resource[] jars;

    private String[] dirs;

    /** Single JAR directory. */
    private String dir;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

//        if (staticLogger.isInfoEnabled()) {
//            staticLogger.info("afterPropertiesSet");
//        }

        if (null != jars) {
            for (Resource lJar : jars) {

                if (lJar.exists()) {
//                    if (staticLogger.isInfoEnabled()) {
//                        staticLogger.info("Add JAR '" + lJar + "'.");
//                    }
                    ClassPathUtils.add(lJar.getURL());
                }
                else {
//                    if (staticLogger.isInfoEnabled()) {
//                        staticLogger.info("JAR '" + lJar + "' do not exist.");
//                    }
                }
            }
        }

        if (null != dirs) {
            for (String lDir : dirs) {
                if (StringUtils.isNotEmpty(lDir)) {
                    addJarDirectory(lDir);
                }
            }
        }

        if (StringUtils.isNotEmpty(dir)) {
            addJarDirectory(dir);
        }
    }

    /**
     * Add all JARs present in a given directory
     * 
     * @param pDirName
     *            Path of the JAR directory
     */
    private void addJarDirectory(String pDirName) {
        File lDir = new File(pDirName);

        if (!lDir.exists() || !lDir.isDirectory()) {
//            staticLogger.error("Invalid directory path '" + pDirName + "'");
            return;
        }

        String[] lJarFilenames = lDir.list(new FilenameFilter() {
            public boolean accept(File pDir, String pName) {
                return pName.toLowerCase().endsWith(".jar");
            }
        });

        for (String lJarFilename : lJarFilenames) {
            ClassPathUtils.add(new File(lDir, lJarFilename));
        }
    }

    /**
     * Set entries to be added to the classpath.
     * 
     * @param pJars
     *            the entries to set
     */
    public final void setJars(final Resource[] pJars) {
        jars = pJars.clone();
    }

    /**
     * Set a list of JAR directories. All JARs files present in these
     * directories are added to the classpath.
     * 
     * @param pDirs
     *            the dirs to set
     */
    public final void setDirs(String[] pDirs) {
        dirs = pDirs;
    }

    /**
     * Set a JAR directory. All JARs files present in this directory are added
     * to the classpath.
     * 
     * @param pDir
     *            the JAR directory to set
     */
    public final void setDir(String pDir) {
        dir = pDir;
    }
}

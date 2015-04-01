/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Bousquet(Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.resetinstance;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.instantiation.Instantiate;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.tests.TestUtils;

/**
 * Reset an instance of gPM as the instance was when first create. If the
 * instance doesn't exist, create it. Use DbUtils, InitDB, InitLifecycle &
 * InitExtension to do the job. Need a properties file for configuration of the
 * different tools.
 * 
 * @author nbousquet
 */
public class ResetInstance {
    /** The Logger. */
    private static final Logger LOGGER = Logger.getLogger(ResetInstance.class);

    /** Is the first execution ? */
    private static boolean staticFirstExecution = true;

    /**
     * Resets the instance.
     * 
     * @param pPropertyFileURL The property file.
     */
    public synchronized void execute(String pPropertyFileURL) {

        try {
            // Load properties used to locate configuration of each tool.
            Properties lProps = new Properties();

            InputStream lInputStream;

            // We try to Open a file and if doesn't work, we try as a resource.
            try {
                lInputStream = new FileInputStream(pPropertyFileURL);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Loaded " + pPropertyFileURL + " as a file");
                }
                lProps.load(lInputStream);
                lInputStream.close();
            } catch (java.io.FileNotFoundException e) {
                lInputStream = this.getClass().getClassLoader().getResourceAsStream(pPropertyFileURL);
                if (null == lInputStream) {
                    throw new GDMException("Cannot find " + pPropertyFileURL);
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Loaded " + pPropertyFileURL + " as a resource");
                }
                lProps.load(lInputStream);
                lInputStream.close();
            }

            
            execute(lProps);
            
        }
        catch (IOException e) {
            throw new GDMException("Cannot read resource");
        }
    }

    /**
     * Resets the instance.
     * 
     * @param pProperties
     *            The properties.
     */
    public synchronized void execute(Properties pProperties) {
        try {
            String lDBUtilsProperties =
                    pProperties.getProperty("dbUtils.properties.file");
            String lProcessName = pProperties.getProperty("Process.name");
            String lProductName = pProperties.getProperty("Product.name");
            String lSheetsCountStr =
                    pProperties.getProperty("Instantiation.autocreatedSheetsCount");
            String lSheetTypeName =
                    pProperties.getProperty("Instantiation.autocreatedTypeName");

            if (null == lDBUtilsProperties || null == lProcessName) {
                throw new Exception("properties file invalid");
            }

            if (staticFirstExecution
                    && StringUtils.isNotBlank(lDBUtilsProperties)) {
                // Call to DbUtils
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Complete purge of existing database\n"
                            + "--== Call to BdUtils with " + lDBUtilsProperties
                            + " ==--");
                }

                Instantiate.dropDataBase(lDBUtilsProperties);

                staticFirstExecution = false;
            }

            LOGGER.info("--== Recreate schema ==--");
            TestUtils.recreateDatabaseSchema();

            // Close current Hibernate session
            LOGGER.info("--== Close current session (if open) ==--");
            TestUtils.closeHibernateSession();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Database structure initialization ...");
            }

            Instantiate.createAdminUser("admin");

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Test process instantiation ...");
            }
            InstantiateOptions lInstOpts;
            lInstOpts = new InstantiateOptions();

            lInstOpts.setUser("admin", "admin");
            lInstOpts.setProcessName(lProcessName);
            //Skip dynamic schema for tests
            lInstOpts.setAutoGenerateDynamicDb(false);

            if (null != lSheetsCountStr) {
                lInstOpts.setProductName(lProductName);
                lInstOpts.setAutoCreatedSheetsCount(Integer.parseInt(lSheetsCountStr));
                lInstOpts.setTypeName(lSheetTypeName);
            }

            // Meta data and data can separated for
            // auto generate dynamic model only one time
            final String lMetaDataFiles =
                    pProperties.getProperty("Instantiation.metadata");
            final String lDataFiles =
                    pProperties.getProperty("Instantiation.data");
            // Other field : for retro compatibility
            final String lInstantiationFiles =
                    pProperties.getProperty("Instantiation.file");

            if (lMetaDataFiles != null) {
                // Generate meta data
                for (String lMetaDataFile : lMetaDataFiles.split(",")) {
                    lInstOpts.setFilename(lMetaDataFile);
                    // Create the instance
                    Instantiate.createInstance(lInstOpts);
                }
                // Force the dynamic schema generation
                // if meta data has been instantiated
                Instantiate.generateDynamicDbSchema(lInstOpts);
            }

            if (lDataFiles != null) {
                // Generate data
                for (String lDataFile : lDataFiles.split(",")) {
                    lInstOpts.setFilename(lDataFile);
                    // Create the instance
                    Instantiate.createInstance(lInstOpts);
                }
            }

            // Other files are instantiate with auto generation
            // of dynamic model
            if (lInstantiationFiles != null) {
                lInstOpts.setAutoGenerateDynamicDb(true);
                for (String lInstantiationFile : lInstantiationFiles.split(",")) {
                    lInstOpts.setFilename(lInstantiationFile);
                    // Create the instance
                    Instantiate.createInstance(lInstOpts);
                }
            }

            if (lInstOpts.isPopulateDb()) {
                // Create content automatically
                Instantiate.populateDb(lInstOpts);
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("--== ResetInstance Finished ==--");
            }
        }
        catch (Exception e) {
            String lMsg = "ResetInstance failed";
            LOGGER.error(lMsg, e);
            throw new GDMException(lMsg, e);
        }
    }

    /**
     * Describe the proper usage of the tool if incorrect arguments are given.
     */
    private static void usage() {
        System.out.println("Usage: ResetInstance <connection properties file>");
    }

    /**
     * Allow for execution as a standalone java program.
     * 
     * @param pArgs
     *            arguments of the program. Should be the properties file used
     *            to configure other tools.
     */
    public static void main(String[] pArgs) {
        if (pArgs.length != 1 || StringUtils.isBlank(pArgs[0])) {
            usage();
        }
        else {
            ResetInstance lResetInstance = new ResetInstance();
            lResetInstance.execute(pArgs[0]);
        }
    }
}

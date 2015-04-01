/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.dbutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/**
 * Tools for the DB.
 * 
 * @author x
 */
public class DbUtils {
    /** The logger. */
//    private static final Logger LOGGER = Logger.getLogger(DbUtils.class);

    /** The DB backend support. */
    private DbOperations dbOps;

    private static void usage() {
        System.out.println("Usage: DropTables <connection properties file>");
    }

    /**
     * Drop the tables.
     * 
     * @param pPropertyFileURL
     *            URL of the properties config file.
     */
    public void dropTables(String pPropertyFileURL) {
        Properties lProps;

        lProps = new Properties();
        InputStream lIs;

        // We try to Open a file and if doesn't work, we try as a resource.
        try {
            lIs = new FileInputStream(pPropertyFileURL);
            lProps.load(lIs);
//            LOGGER.log(Level.INFO, "Loaded " + pPropertyFileURL + " as a file");
        }
        catch (IOException e) {
            lIs =
                    this.getClass().getClassLoader().getResourceAsStream(
                            pPropertyFileURL);
//            LOGGER.log(Level.INFO, "Loaded " + pPropertyFileURL
//                    + " as a resource");
        }

        try {
            lProps.load(lIs);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read parameters file "
                    + pPropertyFileURL);
        }
        
        ConnectionParams lConnParams = new ConnectionParams(lProps);

        // Add the JDBC driver JAR in the Classpath.
        String lJarPath = (String) lProps.get("jdbc.driverJarResource");
        if (StringUtils.isNotBlank(lJarPath)) {
            File lJarFile = new File(lJarPath);
            if (!lJarFile.canRead()) {
                throw new RuntimeException("File '" + lJarPath
                        + "' does not exist");
            }
            else {
                ClassPathUtils.add(lJarFile);
            }
        }

        // Add all JARs in the 'lib' directory
        String lGpmHome = System.getProperty("GPM_HOME");
        if (StringUtils.isNotBlank(lGpmHome)) {
            String lLibDir = lGpmHome + File.separator + "lib";
            ClassPathUtils.addJarDirectory(lLibDir);
        }

        String lDbType = (String) lProps.get("db.type");
        if (StringUtils.isBlank(lDbType)) {
            System.err.println("The 'db.type' property must be set");
            return;
        }

        // Hugh ! This is a dirty way to do it
        if (lDbType.equals("oracle")) {
            dbOps = new OracleOps(lConnParams);
        }
        else if (lDbType.equals("pgsql")) {
            dbOps = new PgsqlOps(lConnParams);
        }
        else {
            System.err.println("The database type '" + lDbType
                    + "' is not supported");
            return;
        }

        dbOps.connect();

        int lDroppedTablesCount;
        int lDroppedSequencesCount;

        lDroppedTablesCount = dbOps.dropAllTables();
        lDroppedSequencesCount = dbOps.dropAllSequences();

        System.out.println(lDroppedTablesCount + " table(s) & "
                + lDroppedSequencesCount + " sequence(s) dropped successfully.");
        
        try {
			lIs.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot close parameters file "
                    + pPropertyFileURL);
		}
    }

    /**
     * Main.
     * 
     * @param pArgs
     *            Cmd line arguments
     */
    public static void main(String[] pArgs) {
        if (pArgs.length != 1) {
            usage();
            System.exit(1);
        }
        else {
            DbUtils lDbUtils = new DbUtils();
            lDbUtils.dropTables(pArgs[0]);
        }
    }
}

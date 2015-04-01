/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.business.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.topcased.business.AbstractSerializationTestCase;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.instantiation.Instantiate;
import org.topcased.gpm.resetinstance.ResetInstance;
import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.exportation.Exportation;
import org.topcased.gpm.serialization.importation.Importation;
import org.topcased.gpm.util.xml.SchemaValidator;

/**
 * TestMigration
 * 
 * @author nveillet
 */
public class TestMigration extends AbstractSerializationTestCase {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(TestMigration.class);

    private final static String MIGRATION_BASE_DIRECTORY =
            "src" + File.separator + "test" + File.separator + "resources"
                    + File.separator + "export";

    private static final String TEST_NORMAL_CASE_PROPERTIES_1 =
            "properties/testMigrationNormalCase1.properties";

    private static final String TEST_NORMAL_CASE_PROPERTIES_2 =
            "properties/testMigrationNormalCase2.properties";

    /**
     * Constructor (without rollback)
     */
    public TestMigration() {
        super(true);
    }

    /**
     * Compare two files
     * 
     * @param pFirstFile
     *            the first export file
     * @param pSecondFile
     *            the second export file
     */
    private void compareFiles(File pFirstFile, File pSecondFile) {
        // Compare exports...
        try {
            // validate files with XSD
            FileInputStream lFileInputStream1 = new FileInputStream(pFirstFile);
            FileInputStream lFileInputStream2 =
                    new FileInputStream(pSecondFile);
            SchemaValidator lSchemaValidator = new SchemaValidator();
            lSchemaValidator.validate(lFileInputStream1, Boolean.FALSE);
            lSchemaValidator.validate(lFileInputStream2, Boolean.FALSE);

            // XML Converter
            lFileInputStream1 = new FileInputStream(pFirstFile);
            lFileInputStream2 = new FileInputStream(pSecondFile);
            XMLConverter lXMLConverter1 = new XMLConverter(lFileInputStream1);
            XMLConverter lXMLConverter2 = new XMLConverter(lFileInputStream2);

            // get object list of exported files
            List<Object> lObjectList1 =
                    getObjectList(lXMLConverter1,
                            lXMLConverter1.createHierarchicalStreamReader());
            List<Object> lObjectList2 =
                    getObjectList(lXMLConverter2,
                            lXMLConverter2.createHierarchicalStreamReader());

            // compare files
            for (Object lObject : lObjectList1) {
                assertTrue("The object " + lObject.toString()
                        + " of the first export is not in the second export",
                        lObjectList2.contains(lObject));
            }
            for (Object lObject : lObjectList2) {
                assertTrue("The object " + lObject.toString()
                        + " of the second export is not in the first export",
                        lObjectList1.contains(lObject));
            }
        }
        catch (FileNotFoundException e) {
            fail(e);
        }
        catch (IOException e) {
            fail(e);
        }
        catch (ClassNotFoundException e) {
            fail(e);
        }
        catch (InstantiateException e) {
            // File not validate by XSD
            fail(e.getErrorMessage());
        }
    }

    /**
     * Test migration
     */
    public void testNormalCase() {
        File lBaseDirectory1 =
                new File(MIGRATION_BASE_DIRECTORY + "NormalMigration1");
        lBaseDirectory1.mkdir();

        File lBaseDirectory2 =
                new File(MIGRATION_BASE_DIRECTORY + "NormalMigration2");
        lBaseDirectory2.mkdir();

        try {
            /**
             * First Export
             */
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Export the instance...");
            }
            // Export Instance
            ArrayList<String> lOptions = new ArrayList<String>();
            lOptions.add("-N");
            lOptions.add(getProcessName());
            lOptions.add("-U");
            lOptions.add(ADMIN_LOGIN[0]);
            lOptions.add("-P");
            lOptions.add(ADMIN_LOGIN[1]);
            lOptions.add("-F");
            lOptions.add(TEST_NORMAL_CASE_PROPERTIES_1);

            Exportation.main(lOptions.toArray(new String[lOptions.size()]));

            commit();
            clearCache();

            /**
             * Instantiation
             */
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Drop tables...");
            }
            // Reset Data Base
            Instantiate.dropDataBase("dropTable.properties");

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("ReInstanciate...");
            }
            ResetInstance lResetInstance = new ResetInstance();
            lResetInstance.execute("tests_db_migration.properties");

            /**
             * Import
             */
            createTransaction();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Import datas...");
            }
            // Import Instance
            lOptions = new ArrayList<String>();
            lOptions.add("-N");
            lOptions.add(getProcessName());
            lOptions.add("-U");
            lOptions.add(ADMIN_LOGIN[0]);
            lOptions.add("-P");
            lOptions.add(ADMIN_LOGIN[1]);
            lOptions.add("-F");
            lOptions.add(TEST_NORMAL_CASE_PROPERTIES_1);

            Importation.main(lOptions.toArray(new String[lOptions.size()]));

            commit();
            createTransaction();

            /**
             * Second export
             */
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Export the instance...");
            }
            // Export Instance
            lOptions = new ArrayList<String>();
            lOptions.add("-N");
            lOptions.add(getProcessName());
            lOptions.add("-U");
            lOptions.add(ADMIN_LOGIN[0]);
            lOptions.add("-P");
            lOptions.add(ADMIN_LOGIN[1]);
            lOptions.add("-F");
            lOptions.add(TEST_NORMAL_CASE_PROPERTIES_2);

            Exportation.main(lOptions.toArray(new String[lOptions.size()]));

            commit();
            createTransaction();

            assertEquals(lBaseDirectory1.listFiles().length,
                    lBaseDirectory2.listFiles().length);

            for (int i = 0; i < lBaseDirectory1.listFiles().length; i++) {
                File lElementDirectory1 = lBaseDirectory1.listFiles()[i];
                assertTrue(lElementDirectory1.isDirectory());

                File lElementDirectory2 = lBaseDirectory2.listFiles()[i];
                assertTrue(lElementDirectory2.isDirectory());

                assertEquals(lElementDirectory1.listFiles().length,
                        lElementDirectory2.listFiles().length);

                assertEquals(lElementDirectory1.getName(),
                        lElementDirectory2.getName());

                for (int j = 0; j < lElementDirectory1.listFiles().length; j++) {
                    File lElementFile1 = lElementDirectory1.listFiles()[j];
                    assertTrue(lElementFile1.isFile());

                    File lElementFile2 = lElementDirectory1.listFiles()[j];
                    assertTrue(lElementFile2.isFile());

                    assertEquals(lElementFile1.getName(),
                            lElementFile2.getName());

                    if (!MigrationElementType.ATTACHED_FILE.getDefaultDirectory().equals(
                            lElementDirectory1.getName())) {
                        compareFiles(lElementFile1, lElementFile2);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            deleteFile(lBaseDirectory1);
            deleteFile(lBaseDirectory2);
        }
    }
}

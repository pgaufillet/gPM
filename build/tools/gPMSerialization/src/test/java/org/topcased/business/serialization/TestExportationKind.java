/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
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
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductLink;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.UserRole;
import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.exportation.Exportation;
import org.topcased.gpm.util.xml.SchemaValidator;

/**
 * TestExportationKind
 * 
 * @author nveillet
 */
public class TestExportationKind extends AbstractSerializationTestCase {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER =
            Logger.getLogger(TestExportationKind.class);

    private static final String TEST_KIND_DICTIONARY_PROPERTIES =
            "properties/testExportationKindDictionary.properties";

    private static final String TEST_KIND_ENVIRONMENTS_PROPERTIES =
            "properties/testExportationKindEnvironments.properties";

    private static final String TEST_KIND_PRODUCTS_PROPERTIES =
            "properties/testExportationKindProducts.properties";

    private static final String TEST_KIND_PRODUCT_LINKS_PROPERTIES =
            "properties/testExportationKindProductLinks.properties";

    private static final String TEST_KIND_USERS_PROPERTIES =
            "properties/testExportationKindUsers.properties";

    private static final String TEST_KIND_FILTERS_PROPERTIES =
            "properties/testExportationKindFilters.properties";

    private static final String TEST_KIND_SHEET_PROPERTIES =
            "properties/testExportationKindSheets.properties";

    private static final String TEST_KIND_SHEET_LINKS_PROPERTIES =
            "properties/testExportationKindSheetLinks.properties";

    /**
     * Test export dictionary kind
     */
    public void testDictionaryKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "DictionaryKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_DICTIONARY_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.DICTIONARY.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 1);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only categories.",
                            lObject instanceof Category);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export environments kind
     */
    public void testEnvironmentsKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "EnvironmentsKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_ENVIRONMENTS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(
                    MigrationElementType.ENVIRONMENT.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 1);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only environments.",
                            lObject instanceof Environment);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export product kind
     */
    public void testProductsKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "ProductsKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_PRODUCTS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.PRODUCT.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 1);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only products.",
                            lObject instanceof Product);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export product links kind
     */
    public void testProductLinksKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "ProductLinksKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_PRODUCT_LINKS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(
                    MigrationElementType.PRODUCT_LINK.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 1);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only productLinks.",
                            lObject instanceof ProductLink);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export users kind
     */
    public void testUsersKind() {

        File lBaseDirectory = new File(MIGRATION_BASE_DIRECTORY + "UsersKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_USERS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.USER.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 1);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only users or user roles.",
                            lObject instanceof User
                                    || lObject instanceof UserRole);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export filters kind
     */
    public void testFiltersKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "FiltersKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_FILTERS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.FILTER.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 2);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only filters.",
                            lObject instanceof Filter);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export sheet kind
     */
    public void testSheetsKind() {

        File lBaseDirectory = new File(MIGRATION_BASE_DIRECTORY + "SheetsKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_SHEET_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.SHEET.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 46);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only sheets.",
                            lObject instanceof SheetData);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }

    /**
     * Test export sheet links kind
     */
    public void testSheetLinksKind() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "SheetLinksKind");
        lBaseDirectory.mkdir();

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
        lOptions.add(TEST_KIND_SHEET_LINKS_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length, 1);

            File lElementDirectory = lBaseDirectory.listFiles()[0];

            assertTrue(lElementDirectory.isDirectory());

            assertEquals(MigrationElementType.SHEET_LINK.getDefaultDirectory(),
                    lElementDirectory.getName());

            assertEquals(lElementDirectory.list().length, 23);

            for (File lElementFile : lElementDirectory.listFiles()) {
                assertTrue(lElementFile.isFile());
                assertTrue(lElementFile.length() > 0);

                // validate files with XSD
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lSchemaValidator.validate(new FileInputStream(lElementFile),
                        Boolean.FALSE);

                // XML Converter
                XMLConverter lXMLConverter =
                        new XMLConverter(new FileInputStream(lElementFile));

                // get object list of exported files
                List<Object> lObjectList =
                        getObjectList(lXMLConverter,
                                lXMLConverter.createHierarchicalStreamReader());

                for (Object lObject : lObjectList) {
                    assertTrue("File '" + lElementFile.getName()
                            + "' not contains only sheet links.",
                            lObject instanceof SheetLink);
                }
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
        finally {
            deleteFile(lBaseDirectory);
        }
    }
}

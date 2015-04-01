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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.topcased.business.AbstractSerializationTestCase;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.exportation.Exportation;
import org.topcased.gpm.util.xml.SchemaValidator;

/**
 * TestExportationLimited
 * 
 * @author nveillet
 */
public class TestExportationLimited extends AbstractSerializationTestCase {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER =
            Logger.getLogger(TestExportationLimited.class);

    private static final String SHEET_TYPE_NAME = "Cat";

    private final static Map<String, Integer> TEST_LIMITED_PRODUCT_FILES;

    private static final String TEST_LIMITED_PRODUCT_PROPERTIES =
            "properties/testExportationProductLimited.properties";

    private final static Map<String, Integer> TEST_LIMITED_SHEET_TYPES_FILES;

    private static final String TEST_LIMITED_SHEET_TYPES_PROPERTIES =
            "properties/testExportationSheetTypesLimited.properties";

    static {
        TEST_LIMITED_PRODUCT_FILES = new HashMap<String, Integer>();
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.DICTIONARY.getDefaultDirectory(), 1);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.ENVIRONMENT.getDefaultDirectory(), 1);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.PRODUCT.getDefaultDirectory(), 1);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.PRODUCT_LINK.getDefaultDirectory(), 0);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.USER.getDefaultDirectory(), 1);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.FILTER.getDefaultDirectory(), 2);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.SHEET.getDefaultDirectory(), 9);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.SHEET_LINK.getDefaultDirectory(), 0);
        TEST_LIMITED_PRODUCT_FILES.put(
                MigrationElementType.ATTACHED_FILE.getDefaultDirectory(), 38);

        TEST_LIMITED_SHEET_TYPES_FILES = new HashMap<String, Integer>();
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.DICTIONARY.getDefaultDirectory(), 1);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.ENVIRONMENT.getDefaultDirectory(), 1);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.PRODUCT.getDefaultDirectory(), 1);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.PRODUCT_LINK.getDefaultDirectory(), 1);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.USER.getDefaultDirectory(), 1);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.FILTER.getDefaultDirectory(), 2);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.SHEET.getDefaultDirectory(), 4);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.SHEET_LINK.getDefaultDirectory(), 23);
        TEST_LIMITED_SHEET_TYPES_FILES.put(
                MigrationElementType.ATTACHED_FILE.getDefaultDirectory(), 2);
    }

    /**
     * Test export with limited product
     */
    public void testProductLimited() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "ProductLimited");
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
        lOptions.add(TEST_LIMITED_PRODUCT_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length,
                    TEST_LIMITED_PRODUCT_FILES.keySet().size());

            for (File lElementDirectory : lBaseDirectory.listFiles()) {
                assertTrue(lElementDirectory.isDirectory());
                assertTrue(TEST_LIMITED_PRODUCT_FILES.containsKey(lElementDirectory.getName()));
                assertEquals(lElementDirectory.list().length,
                        TEST_LIMITED_PRODUCT_FILES.get(
                                lElementDirectory.getName()).intValue());

                for (File lElementFile : lElementDirectory.listFiles()) {
                    assertTrue(lElementFile.isFile());
                    assertTrue(lElementFile.length() > 0);

                    if (!MigrationElementType.ATTACHED_FILE.getDefaultDirectory().equals(
                            lElementDirectory.getName())) {
                        // validate files with XSD
                        SchemaValidator lSchemaValidator =
                                new SchemaValidator();
                        lSchemaValidator.validate(new FileInputStream(
                                lElementFile), Boolean.FALSE);

                        // XML Converter
                        XMLConverter lXMLConverter =
                                new XMLConverter(new FileInputStream(
                                        lElementFile));

                        // get object list of exported files
                        List<Object> lObjectList =
                                getObjectList(
                                        lXMLConverter,
                                        lXMLConverter.createHierarchicalStreamReader());

                        for (Object lObject : lObjectList) {
                            // Check content of sheet export files
                            if (MigrationElementType.SHEET.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only sheets.",
                                        lObject instanceof SheetData);

                                SheetData lSheet = (SheetData) lObject;
                                assertEquals("The sheet '"
                                        + lSheet.getReference() + "' on file '"
                                        + lElementFile.getName()
                                        + "' is not in the product '"
                                        + DEFAULT_PRODUCT_NAME + "'.",
                                        DEFAULT_PRODUCT_NAME,
                                        lSheet.getProductName());
                            }
                            else if (MigrationElementType.PRODUCT.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only products.",
                                        lObject instanceof Product);

                                Product lProduct = (Product) lObject;
                                assertEquals(
                                        "Product '"
                                                + lProduct.getName()
                                                + "' has been exported on file '"
                                                + lElementFile.getName()
                                                + "', while only the product '"
                                                + DEFAULT_PRODUCT_NAME
                                                + "' should be.",
                                        DEFAULT_PRODUCT_NAME,
                                        lProduct.getName());
                            }
                        }
                    }
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
     * Test export with limited sheet types
     */
    public void testSheetTypeLimited() {

        File lBaseDirectory =
                new File(MIGRATION_BASE_DIRECTORY + "SheetTypeLimited");
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
        lOptions.add(TEST_LIMITED_SHEET_TYPES_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length,
                    TEST_LIMITED_SHEET_TYPES_FILES.keySet().size());

            // Check export files
            for (File lElementDirectory : lBaseDirectory.listFiles()) {
                assertTrue(lElementDirectory.isDirectory());
                assertTrue(TEST_LIMITED_SHEET_TYPES_FILES.containsKey(lElementDirectory.getName()));
                assertEquals(lElementDirectory.list().length,
                        TEST_LIMITED_SHEET_TYPES_FILES.get(
                                lElementDirectory.getName()).intValue());

                for (File lElementFile : lElementDirectory.listFiles()) {
                    assertTrue(lElementFile.isFile());
                    assertTrue(lElementFile.length() > 0);

                    if (!MigrationElementType.ATTACHED_FILE.getDefaultDirectory().equals(
                            lElementDirectory.getName())) {
                        // validate files with XSD
                        SchemaValidator lSchemaValidator =
                                new SchemaValidator();
                        lSchemaValidator.validate(new FileInputStream(
                                lElementFile), Boolean.FALSE);
                    }

                    // Check content of sheet export files
                    if (MigrationElementType.SHEET.getDefaultDirectory().equals(
                            lElementDirectory.getName())) {

                        // XML Converter
                        XMLConverter lXMLConverter =
                                new XMLConverter(new FileInputStream(
                                        lElementFile));

                        // get object list of exported files
                        List<Object> lObjectList =
                                getObjectList(
                                        lXMLConverter,
                                        lXMLConverter.createHierarchicalStreamReader());

                        for (Object lObject : lObjectList) {
                            assertTrue("File '" + lElementFile.getName()
                                    + "' not contains only sheets.",
                                    lObject instanceof SheetData);

                            SheetData lSheet = (SheetData) lObject;
                            assertEquals("A sheet of the type '"
                                    + lSheet.getType()
                                    + "' is exported on file ''"
                                    + lElementFile.getName() + "''.",
                                    SHEET_TYPE_NAME, lSheet.getType());
                        }
                    }
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

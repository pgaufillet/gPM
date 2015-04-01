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
 * TestExportation
 * 
 * @author nveillet
 */
public class TestExportation extends AbstractSerializationTestCase {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER =
            Logger.getLogger(TestExportation.class);

    private final static Map<String, Integer> TEST_NORMAL_CASE_FILES;

    private static final String TEST_NORMAL_CASE_PROPERTIES =
            "properties/testExportationNormalCase.properties";

    private final static Map<String, Integer> TEST_CUSTOM_CASE_FILES;

    private static final String TEST_CUSTOM_CASE_PROPERTIES =
            "properties/testExportationCustomCase.properties";

    static {
        TEST_NORMAL_CASE_FILES = new HashMap<String, Integer>();
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.DICTIONARY.getDefaultDirectory(), 1);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.ENVIRONMENT.getDefaultDirectory(), 1);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.PRODUCT.getDefaultDirectory(), 1);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.PRODUCT_LINK.getDefaultDirectory(), 1);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.USER.getDefaultDirectory(), 1);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.FILTER.getDefaultDirectory(), 2);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.SHEET.getDefaultDirectory(), 46);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.SHEET_LINK.getDefaultDirectory(), 23);
        TEST_NORMAL_CASE_FILES.put(
                MigrationElementType.ATTACHED_FILE.getDefaultDirectory(), 58);

        TEST_CUSTOM_CASE_FILES = new HashMap<String, Integer>();
        TEST_CUSTOM_CASE_FILES.put("myDictionary", 2);
        TEST_CUSTOM_CASE_FILES.put("myEnvironments", 5);
        TEST_CUSTOM_CASE_FILES.put("myProducts", 7);
        TEST_CUSTOM_CASE_FILES.put("myUsers", 15);
        TEST_CUSTOM_CASE_FILES.put("myFilters", 6);
        TEST_CUSTOM_CASE_FILES.put("mySheets", 50);
        TEST_CUSTOM_CASE_FILES.put("mySheetLinks", 35);
        TEST_CUSTOM_CASE_FILES.put("myAttachedFiles", 58);
    }

    /**
     * Test export normal case
     */
    public void testNormalCase() {

        File lBaseDirectory = new File(MIGRATION_BASE_DIRECTORY + "Normal");
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
        lOptions.add(TEST_NORMAL_CASE_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length,
                    TEST_NORMAL_CASE_FILES.keySet().size());

            for (File lElementDirectory : lBaseDirectory.listFiles()) {
                assertTrue(lElementDirectory.isDirectory());
                assertTrue(TEST_NORMAL_CASE_FILES.containsKey(lElementDirectory.getName()));
                assertEquals(
                        lElementDirectory.list().length,
                        TEST_NORMAL_CASE_FILES.get(lElementDirectory.getName()).intValue());

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
                            if (MigrationElementType.DICTIONARY.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only categories.",
                                        lObject instanceof Category);
                            }
                            else if (MigrationElementType.ENVIRONMENT.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only environments.",
                                        lObject instanceof Environment);
                            }
                            else if (MigrationElementType.PRODUCT.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only products.",
                                        lObject instanceof Product);
                            }
                            else if (MigrationElementType.PRODUCT_LINK.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only product links.",
                                        lObject instanceof ProductLink);
                            }
                            else if (MigrationElementType.USER.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue(
                                        "File '"
                                                + lElementFile.getName()
                                                + "' not contains only users or user roles.",
                                        lObject instanceof User
                                                || lObject instanceof UserRole);
                            }
                            else if (MigrationElementType.FILTER.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only filters.",
                                        lObject instanceof Filter);
                            }
                            else if (MigrationElementType.SHEET.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only sheets.",
                                        lObject instanceof SheetData);
                            }
                            else if (MigrationElementType.SHEET_LINK.getDefaultDirectory().equals(
                                    lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only sheet links.",
                                        lObject instanceof SheetLink);
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
     * Test export normal case
     */
    public void testCustomCase() {

        File lBaseDirectory = new File(MIGRATION_BASE_DIRECTORY + "Custom");
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
        lOptions.add(TEST_CUSTOM_CASE_PROPERTIES);

        Exportation.main(lOptions.toArray(new String[lOptions.size()]));

        try {
            assertEquals(lBaseDirectory.list().length,
                    TEST_CUSTOM_CASE_FILES.keySet().size());

            for (File lElementDirectory : lBaseDirectory.listFiles()) {
                assertTrue(lElementDirectory.isDirectory());
                assertTrue(TEST_CUSTOM_CASE_FILES.containsKey(lElementDirectory.getName()));
                assertEquals(
                        lElementDirectory.list().length,
                        TEST_CUSTOM_CASE_FILES.get(lElementDirectory.getName()).intValue());

                for (File lElementFile : lElementDirectory.listFiles()) {
                    assertTrue(lElementFile.isFile());
                    assertTrue(lElementFile.length() > 0);

                    if (!"myAttachedFiles".equals(lElementDirectory.getName())) {
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
                            if ("myDictionary".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only categories.",
                                        lObject instanceof Category);
                            }
                            else if ("myEnvironments".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only environments.",
                                        lObject instanceof Environment);
                            }
                            else if ("myProducts".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only products.",
                                        lObject instanceof Product);
                            }
                            else if ("myProductLinks".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only product links.",
                                        lObject instanceof ProductLink);
                            }
                            else if ("myUsers".equals(lElementDirectory.getName())) {
                                assertTrue(
                                        "File '"
                                                + lElementFile.getName()
                                                + "' not contains only user or user roles.",
                                        lObject instanceof User
                                                || lObject instanceof UserRole);
                            }
                            else if ("myFilters".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only filters.",
                                        lObject instanceof Filter);
                            }
                            else if ("mySheets".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only sheets.",
                                        lObject instanceof SheetData);
                            }
                            else if ("mySheetLinks".equals(lElementDirectory.getName())) {
                                assertTrue("File '" + lElementFile.getName()
                                        + "' not contains only sheet links.",
                                        lObject instanceof SheetLink);
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
}

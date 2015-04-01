/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * Test export service
 * <ul>
 * <li>use 'admin' and 'notadmin' roles.</li>
 * <li>2 sheet's type 'testExportSheet' and 'testExportSheet_2'.</li>
 * <li>Rights on fields (confidential, export)</li>
 * <li>UUID option</li>
 * <li>limited export (identifiers, products and types)</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class TestExportationService extends AbstractBusinessServiceTestCase {
    /** Expected fields for 'notadmin' role on 'testExportSheet' sheet's type. */
    private static final Set<String> EXPECTED_NOTADMIN_FIELDS;

    /**
     * Fields that are not expected for 'notadmin' role on 'testExportSheet'
     * sheet's type.
     */
    private static final Set<String> NOT_EXPECTED_NOTADMIN_FIELDS;

    private static final String[] PRODUCTS_NAME =
            new String[] { GpmTestValues.PRODUCT_STORE1_NAME,
                          GpmTestValues.PRODUCT1_NAME };

    private static final String[] TYPES_NAME =
            new String[] { GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET,
                          GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET_2 };

    /**
     * Expected sheet's reference for 'admin' role and limited products and
     * types export.
     */
    private static final Set<String> EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS;

    /**
     * Expected sheet's reference for 'notadmin' role and limited products and
     * types export.
     */
    private static final Set<String> EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS;

    private static final Set<String> EMPTY = Collections.emptySet();

    static {
        EXPECTED_NOTADMIN_FIELDS = new HashSet<String>();
        EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field1");
        EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field3");
        EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field5");
        EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field6");

        NOT_EXPECTED_NOTADMIN_FIELDS = new HashSet<String>();
        NOT_EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field2");
        NOT_EXPECTED_NOTADMIN_FIELDS.add("testExportSheet_field4");

        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS = new HashSet<String>();
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_01");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_02");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_03");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_04");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_10");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_11");
        EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS.add("testExportSheet_2_01");

        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS = new HashSet<String>();
        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS.add("testExportSheet_01");
        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS.add("testExportSheet_02");
        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS.add("testExportSheet_03");
        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS.add("testExportSheet_10");
        EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS.add("testExportSheet_11");
        //No 'testExportSheet_04' because of its product (notadmin rights)
        //No 'testExportSheet_2_01' because of its type (notadmin rights)
    }

    private ExportService exportService;

    /**
     * The supported export tag
     */
    private Set<String> supportedTags = new HashSet<String>();

    /**
     * The supported export tag container
     */
    private Set<String> supportedContainerTags = new HashSet<String>();

    private SheetServiceImpl sheetService =
            (SheetServiceImpl) ContextLocator.getContext().getBean(
                    "sheetServiceImpl");

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        exportService = serviceLocator.getExportService();
        supportedTags.add("products");
        supportedTags.add("productLinks");
        supportedTags.add("sheets");
        supportedTags.add("sheetLinks");
        supportedTags.add("users");
        supportedTags.add("userRoles");
        supportedTags.add("filters");
        supportedTags.add("categories");
        supportedTags.add("environments");
        supportedContainerTags.add("dictionary");
    }

    private static final String[] LIMITED_SHEETS =
            new String[] { "testExportSheet_01", "testExportSheet_02",
                          "testExportSheet_03" };

    private static final String[] LIMITED_SHEETS_POINTER =
            new String[] { "pointer:1", "pointer:2" };

    private static final String LIMITED_SHEET_PRODUCT_NAME =
            GpmTestValues.PRODUCT1_NAME;

    private static final String LIMITED_EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testAdminLimitedSheetExport.xml";

    /**
     * Test limited export on identifiers for 'admin' roles.
     * <p>
     * Additionally test UUID option (UUID not exported)
     */
    public void testAdminLimitedSheetExport() {
        final Set<String> lSheets = new HashSet<String>();

        for (String lSheetRef : LIMITED_SHEETS_POINTER) {
            lSheets.add(sheetService.getSheetIdByReference(getProcessName(),
                    LIMITED_SHEET_PRODUCT_NAME, lSheetRef));
        }
        for (String lSheetRef : LIMITED_SHEETS) {
            lSheets.add(sheetService.getSheetIdByReference(getProcessName(),
                    LIMITED_SHEET_PRODUCT_NAME, lSheetRef));
        }
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.LIMITED);
        lExportProperties.setLimitedElementsId(lSheets.toArray(new String[lSheets.size()]));
        lExportProperties.setExportUID(false);

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(LIMITED_EXPORT_FILE);
            exportService.exportData(adminRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        Set<String> lExpectedSheets = new HashSet<String>();
        lExpectedSheets.addAll(Arrays.asList(LIMITED_SHEETS));
        lExpectedSheets.addAll(Arrays.asList(LIMITED_SHEETS_POINTER));
        validExport(LIMITED_EXPORT_FILE, lExpectedSheets, EMPTY, EMPTY, true);
    }

    private static final String LIMITED_NOT_ADMIN_EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testNotAdminLimitedSheetExport.xml";

    /**
     * Test limited export on identifiers for 'notadmin' roles.
     * <p>
     * Additionally test UUID option (UUID not exported)
     */
    public void testNotAdminLimitedSheetExport() {
        final Set<String> lSheets = new HashSet<String>();
        for (String lSheetRef : LIMITED_SHEETS) {
            lSheets.add(sheetService.getSheetIdByReference(getProcessName(),
                    LIMITED_SHEET_PRODUCT_NAME, lSheetRef));
        }
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.LIMITED);
        lExportProperties.setLimitedElementsId(lSheets.toArray(new String[lSheets.size()]));
        lExportProperties.setExportUID(false);

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(LIMITED_NOT_ADMIN_EXPORT_FILE);
            exportService.exportData(normalRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        Set<String> lExpectedSheets = new HashSet<String>();
        lExpectedSheets.addAll(Arrays.asList(LIMITED_SHEETS));
        validExport(LIMITED_NOT_ADMIN_EXPORT_FILE, lExpectedSheets,
                EXPECTED_NOTADMIN_FIELDS, NOT_EXPECTED_NOTADMIN_FIELDS, true);
    }

    private static final String LIMITED_PRODUCT_TYPE_EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testLimitedTypeProductSheetExport_admin.xml";

    /**
     * Test limited export on products and types for 'admin' roles.
     */
    public void testAdminLimitedProductTypeSheetExport() {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.ALL);
        lExportProperties.setLimitedProductsName(PRODUCTS_NAME);
        lExportProperties.setLimitedTypesName(TYPES_NAME);

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(LIMITED_PRODUCT_TYPE_EXPORT_FILE);
            exportService.exportData(adminRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        //Valid exportation (only with sheets reference)
        Collection<String> lEmptyCollection = Collections.emptySet();
        validExport(LIMITED_PRODUCT_TYPE_EXPORT_FILE,
                EXPECTED_PRODUCT_TYPE_ADMIN_SHEETS, lEmptyCollection,
                lEmptyCollection, false);
    }

    private static final String LIMITED_PRODUCT_TYPE_EXPORT_FILE_2 =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testLimitedTypeProductSheetExport_notadmin.xml";

    /**
     * Test limited export on products and types for 'notadmin' roles.
     */
    public void testNotAdminLimitedProductTypeSheetExport() {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.ALL);
        lExportProperties.setLimitedProductsName(PRODUCTS_NAME);
        lExportProperties.setLimitedTypesName(TYPES_NAME);

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(LIMITED_PRODUCT_TYPE_EXPORT_FILE_2);
            exportService.exportData(normalRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        //Valid exportation (only with sheets reference)
        validExport(LIMITED_PRODUCT_TYPE_EXPORT_FILE_2,
                EXPECTED_PRODUCT_TYPE_NOTADMIN_SHEETS, EMPTY, EMPTY, false);
    }

    private static final String NO_EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testNoSheetExport_admin.xml";

    /**
     * Test export flag 'NO' for 'admin' roles with limited products and types.
     */
    public void testAdminNoSheetExport() {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.NO);
        lExportProperties.setLimitedProductsName(PRODUCTS_NAME);
        lExportProperties.setLimitedTypesName(TYPES_NAME);

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(NO_EXPORT_FILE);
            exportService.exportData(adminRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        //Valid exportation (only with sheets reference)
        validExport(NO_EXPORT_FILE, EMPTY, EMPTY, EMPTY, false);
    }

    private static final String NO_NOT_ADMIN_EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "testNoSheetExport_notadmin.xml";

    /**
     * Test export flag 'NO' for 'notadmin' roles with limited identifiers.
     */
    public void testNoNotAdminSheetExport() {
        final Set<String> lSheets = new HashSet<String>();
        for (String lSheetRef : LIMITED_SHEETS) {
            lSheets.add(sheetService.getSheetIdByReference(getProcessName(),
                    LIMITED_SHEET_PRODUCT_NAME, lSheetRef));
        }

        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setSheetsFlag(ExportFlag.NO);
        lExportProperties.setLimitedElementsId(lSheets.toArray(new String[lSheets.size()]));

        FileOutputStream lFile;
        try {
            lFile = new FileOutputStream(NO_NOT_ADMIN_EXPORT_FILE);
            exportService.exportData(normalRoleToken, lFile, lExportProperties);
            lFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }

        //Valid exportation (only with sheets reference)
        validExport(NO_NOT_ADMIN_EXPORT_FILE, EMPTY, EMPTY, EMPTY, false);
    }

    /**
     * Retrieve object from the gPM XML file.
     * 
     * @param pConverter
     *            Converter
     * @param pHierarchicalReader
     *            Hierarchical reader
     * @return List of objects presents in the file.
     * @throws IOException
     *             Error during reading
     * @throws ClassNotFoundException
     *             No class found to an object
     */
    private List<Object> getObjectList(XMLConverter pConverter,
            HierarchicalStreamReader pHierarchicalReader) throws IOException,
        ClassNotFoundException {

        List<Object> lList = new ArrayList<Object>();

        while (pHierarchicalReader.hasMoreChildren()) {
            pHierarchicalReader.moveDown();
            String lCurrentNodeName = pHierarchicalReader.getNodeName();

            if (supportedTags.contains(lCurrentNodeName)) {
                ObjectInputStream lOis = pConverter.createObjectInputStream();
                try {
                    while (true) {
                        Object lObject = lOis.readObject();
                        lList.add(lObject);
                    }
                }
                catch (EOFException e) {
                    // End of the object stream
                }
            }
            else if (supportedContainerTags.contains(lCurrentNodeName)) {
                lList.addAll(getObjectList(pConverter, pHierarchicalReader));
            }

            pHierarchicalReader.moveUp();
        }

        return lList;
    }

    /**
     * Valid the export.
     * <p>
     * Check the UUID, sheets, and sheet's fields.
     * 
     * @param pExportFile
     *            File to valid
     * @param pExpectedSheets
     *            Expected sheets reference
     * @param pExpectedFields
     *            Expected sheet's fields
     * @param pNotExpectedFields
     *            Not expected sheet's fields
     * @param pCheckUUID
     *            True to test UUID, false to skip this test.
     */
    private void validExport(final String pExportFile,
            final Collection<String> pExpectedSheets,
            final Collection<String> pExpectedFields,
            final Collection<String> pNotExpectedFields, boolean pCheckUUID) {
        //Read file
        try {
            FileInputStream lExportedFile = new FileInputStream(pExportFile);
            XMLConverter lXMLConverter = new XMLConverter(lExportedFile);
            List<Object> lExportedObjects =
                    getObjectList(lXMLConverter,
                            lXMLConverter.createHierarchicalStreamReader());
            assertEquals("Limited export is not respected.",
                    pExpectedSheets.size(), lExportedObjects.size());

            if (CollectionUtils.isNotEmpty(lExportedObjects)) {
                final Set<String> lExportedSheets = new HashSet<String>();
                for (Object lExportedObject : lExportedObjects) {
                    //Only sheets
                    assertTrue(
                            "The exported file does not contain only sheets.",
                            lExportedObject instanceof SheetData);
                    SheetData lSheet = (SheetData) lExportedObject;
                    if (pCheckUUID) {
                        checkUUID(lSheet);
                    }

                    //Check fields
                    Set<String> lLabelKeys = getFieldsLabelKey(lSheet);
                    if (CollectionUtils.isNotEmpty(pExpectedFields)) {
                        assertTrue(
                                "Exported sheet's fields are not the same as expected.",
                                CollectionUtils.isEqualCollection(
                                        pExpectedFields, lLabelKeys));
                    }
                    for (String lNotExpectedField : pNotExpectedFields) {
                        assertFalse("The sheet '" + lSheet.getReference()
                                + "' must not contains the field '"
                                + lNotExpectedField + "'.",
                                lLabelKeys.contains(lNotExpectedField));
                    }
                    lExportedSheets.add(lSheet.getReference());
                }

                assertTrue("Exported sheets are not the same as expected.",
                        CollectionUtils.isEqualCollection(pExpectedSheets,
                                lExportedSheets));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check UUI on a values container.
     * <p>
     * Check also pointer's fields UUID.
     * 
     * @param pValuesContainer
     *            Values container to test.
     */
    private void checkUUID(ValuesContainerData pValuesContainer) {
        assertNull(pValuesContainer.getId());
        if (CollectionUtils.isNotEmpty(pValuesContainer.getFieldValues())) {
            for (FieldValueData lField : pValuesContainer.getFieldValues()) {
                if (lField instanceof PointerFieldValueData) {
                    checkPointerUUID((PointerFieldValueData) lField);
                }
                if (CollectionUtils.isNotEmpty(lField.getFieldValues())) {
                    for (FieldValueData lSubField : lField.getFieldValues()) {
                        if (lSubField instanceof PointerFieldValueData) {
                            checkPointerUUID((PointerFieldValueData) lSubField);
                        }
                    }
                }
            }
        }
    }

    /**
     * Check pointer's field UUID.
     * <p>
     * If no UUID, the 'referencedContainerRef' and 'referencedProduct'
     * attributes must be fill.
     * 
     * @param pPointerField
     *            Pointer's field to test
     */
    private void checkPointerUUID(PointerFieldValueData pPointerField) {
        assertNull(pPointerField.getReferencedContainerId());
        assertFalse(StringUtils.isBlank(pPointerField.getReferencedContainerRef()));
        assertFalse(StringUtils.isBlank(pPointerField.getReferencedProduct()));
    }

    /**
     * Get fields label key.
     * 
     * @param pValuesContainer
     *            Values container to parse.
     * @return Values container's fields label key.
     */
    private Set<String> getFieldsLabelKey(
            final ValuesContainerData pValuesContainer) {
        final Set<String> lLabelKeys = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(pValuesContainer.getFieldValues())) {
            for (FieldValueData lField : pValuesContainer.getFieldValues()) {
                lLabelKeys.add(lField.getName());
                if (CollectionUtils.isNotEmpty(lField.getFieldValues())) {
                    for (FieldValueData lSubField : lField.getFieldValues()) {
                        lLabelKeys.add(lSubField.getName());
                    }
                }
            }
        }
        return lLabelKeys;
    }
}

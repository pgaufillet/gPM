/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.sheet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.importation.AbstractValuesContainerImportTest;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSheetImport
 * <ul>
 * <li>use 'admin' and 'notadmin' roles</li>
 * <li>3 sheets ('testExportSheet_10', 'testExportSheet_11', 'pointer:2') which
 * are in test XML files 'sheetsToImport.xml' and 'sheetsToUpdate.xml'</li>
 * <li>Rights on fields (confidential and update))</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class TestSheetImport extends AbstractValuesContainerImportTest {

    private static final String IMPORTED_PRODUCT_NAME =
            GpmTestValues.PRODUCT1_NAME;

    private static final String IMPORT_FILE =
            "importation/sheet/sheetsToImport.xml";

    private static final String IMPORT_FILE_TO_UPDATE =
            "importation/sheet/sheetsToUpdate.xml";

    /** ERASE_FIELD_TO_UPDATE */
    private static final String ERASE_FIELD_TO_UPDATE =
            "testExportSheet_field1";

    /** ERASE_NEW_VALUE */
    private static final String ERASE_NEW_VALUE = "to be erase";

    private static final Set<String[]> IMPORTED_SHEETS;

    /** Fields that should be import for 'admin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;

    /** Fields that should be import for 'notadmin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    /** Fields that should not be import for 'notadmin' role */
    private static final Map<String, String> NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    static {
        IMPORTED_SHEETS = new HashSet<String[]>();
        IMPORTED_SHEETS.add(new String[] { "testExportSheet_10" });
        IMPORTED_SHEETS.add(new String[] { "testExportSheet_11" });

        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN = new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put(ERASE_FIELD_TO_UPDATE,
                "testExportSheet_field1_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportSheet_field2",
                "testExportSheet_field2_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportSheet_field3",
                "testExportSheet_field3_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportSheet_field4",
                "testExportSheet_field4_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportSheet_field5",
                "testExportSheet_field5_UPDATED");

        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(ERASE_FIELD_TO_UPDATE,
                "testExportSheet_field1_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(
                "testExportSheet_field3", "testExportSheet_field3_UPDATED");

        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(
                "testExportSheet_field2", "testExportSheet_field1_UPDATED");
        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(
                "testExportSheet_field4", "testExportSheet_field3_UPDATED");
    }

    private SheetServiceImpl sheetService =
            (SheetServiceImpl) ContextLocator.getContext().getBean(
                    "sheetServiceImpl");

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#eraseAssertion(java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
	@Override
    protected void eraseAssertion(Object... pArgs) {
        CacheableSheet lSheet = (CacheableSheet) pArgs[0];
        Map<String, Integer> lSheets = (Map<String, Integer>) pArgs[1];
        String lSheetRef = ((String[]) pArgs[2])[0];
        if (lSheets.containsKey(lSheetRef)) {
            assertFalse("Version must be 0 in ERASE mode",
                    lSheets.get(lSheetRef) == lSheet.getVersion());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Object erasePreCondition() {
        //Retrieve sheets identifiers.
        final Map<String, Integer> lSheets = new HashMap<String, Integer>();
        for (String[] lSheetRef : getImportedElement()) {
            String lSheetId = getElementId(lSheetRef);
            CacheableSheet lSheet =
                    sheetService.getCacheableSheet(normalRoleToken, lSheetId,
                            CacheProperties.MUTABLE);
            FieldValueData lFieldValue =
                    (FieldValueData) lSheet.getValue(ERASE_FIELD_TO_UPDATE);
            if (lFieldValue != null) {
                lFieldValue.setValue(ERASE_NEW_VALUE);
                sheetService.updateSheet(normalRoleToken, lSheet, CONTEXT);
                lSheet =
                        sheetService.getCacheableSheet(normalRoleToken,
                                lSheetId, CacheProperties.IMMUTABLE);
                lSheets.put(lSheetRef[0], lSheet.getVersion());
            }
        }
        return lSheets;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected CacheableValuesContainer getBusinessObject(String pRoleToken,
            String pElementId) {
        CacheableSheet lSheet =
                sheetService.getCacheableSheet(pRoleToken, pElementId,
                        CacheProperties.IMMUTABLE);
        return lSheet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String)
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lRef = pElementRef[0];
        String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        IMPORTED_PRODUCT_NAME, lRef);
        return lSheetId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportNotAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFile()
     */
    @Override
    protected String getImportFile() {
        return IMPORT_FILE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFileForUpdating()
     */
    @Override
    protected String getImportFileForUpdating() {
        return IMPORT_FILE_TO_UPDATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        return IMPORTED_SHEETS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getNotExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getNotExpectedFieldsToImportNotAdmin() {
        return NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElement) {
        sheetService.deleteSheet(adminRoleToken, pElement, CONTEXT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setSheetsFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        doTestEraseImport(normalRoleToken, getExpectedFieldsToImportNotAdmin(),
                getNotExpectedFieldsToImportNotAdmin());
    }

}

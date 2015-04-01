/******************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (ATOS), Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.exception.StaleSheetDataException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>updateSheet<CODE> of the Sheet Service.
 * 
 * @author nsamson
 */
public class TestUpdateSheetWithSheetDataParameter extends
        AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The Name of a field. */
    private static final String FIELD_NAME = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestUpdateSheetServiceConfidentialAccess.xml";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_UPDATABLE_TEST =
            "sheet/TestUpdateSheetServiceNotUpdatable.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /**
     * Tests the updateSheet method.
     */
    public void testNormalCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        // Retrieving the sheet
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB.",
                lSheetData);

        // Get current version of the sheet.
        int lVersion = lSheetData.getVersion();

        // Modifying and updating the sheet
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldDatas =
                lSheetData.getFieldValues();
        lFieldDatas.get(1).setValue(FIELD_NAME);

        // Updating the sheet 

        startTimer();
        sheetService.updateSheet(lAdminRoleToken, getProcessName(), lSheetData,
                Context.getContext());
        stopTimer();

        lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetData.getId() + " does not exist in DB",
                lSheetData);

        // Check that the sheet version has been incremented
        assertTrue(lSheetData.getVersion() == lVersion + 1);

        String lValue = lSheetData.getFieldValues().get(1).getValue();
        assertNotNull("Sheet #" + lSheetData.getId()
                + " has not the right field values", lValue);
        assertEquals(lValue, FIELD_NAME);

    }

    /**
     * Tests the updateSheet method with a bad version.
     */
    public void testBadVersionCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        // Retrieving the sheet
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB.",
                lSheetData);

        // Modifying and updating the sheet
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldDatas =
                lSheetData.getFieldValues();
        lFieldDatas.get(1).setValue(FIELD_NAME);

        lSheetData.setVersion(0);
        try {

            sheetService.updateSheet(lAdminRoleToken, getProcessName(),
                    lSheetData, Context.getContext());
        }
        catch (Exception lGDMException) {
            assertTrue("The thrown exception is not a GDMException.",
                    lGDMException instanceof GDMException);
        }

    }

    //   /**
    //    * Tests the updateSheet method when the sheet type is confidential.
    //    */
    public void testConfidentialAccessCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        // Retrieving the sheet
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB.",
                lSheetData);

        // Get current version of the sheet.
        int lVersion = lSheetData.getVersion();

        // Modifying and updating the sheet
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldDatas =
                lSheetData.getFieldValues();
        lFieldDatas.get(1).setValue(FIELD_NAME);

        // set confidential access on sheet type SHEET_TYPE_NAME
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Updating the sheet
        try {
            sheetService.updateSheet(lRoleToken, getProcessName(), lSheetData,
                    Context.getContext());
            fail("An authorization exception should have been thrown.");
        }
        catch (AuthorizationException e) {
        }

        finally {
            authorizationService.logout(lUserToken);
        }
        lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetData.getId() + " does not exist in DB",
                lSheetData);
        assertNotNull("Sheet #" + lSheetData.getId() + " does not exist in DB",
                lSheetData);

        // Check that the sheet version has not been incremented
        assertTrue(lSheetData.getVersion() == lVersion);

    }

    /**
     * Tests the updateSheet method when the sheet type is not updatable.
     */
    public void testNotUpdatableAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        // Retrieving the sheet
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB.",
                lSheetData);

        // Get current version of the sheet.
        int lVersion = lSheetData.getVersion();

        // Modifying and updating the sheet
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldDatas =
                lSheetData.getFieldValues();
        lFieldDatas.get(1).setValue(FIELD_NAME);

        // set confidential access on sheet type SHEET_TYPE_NAME
        instantiate(getProcessName(), XML_INSTANCE_UPDATABLE_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Update the sheet
        try {
            sheetService.updateSheet(lRoleToken, getProcessName(), lSheetData,
                    Context.getContext());
            fail("An authorization exception should have been thrown.");
        }
        catch (AuthorizationException e) {
        }

        finally {
            authorizationService.logout(lUserToken);
        }

        lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetData.getId() + " does not exist in DB",
                lSheetData);
        assertNotNull("Sheet #" + lSheetData.getId() + " does not exist in DB",
                lSheetData);

        // Check that the sheet version has not been incremented
        assertTrue(lSheetData.getVersion() == lVersion);

    }

    /**
     * Tests the updateSheet method by the user2 with a READ_WRITE_LOCK set by
     * user1
     */
    public void testReadWriteLockOnSheetCase() {
        sheetService = serviceLocator.getSheetService();
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());
        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB",
                lSheetData);

        try {
            sheetService.updateSheet(lRoleToken, getProcessName(), lSheetData,
                    Context.getContext());
        }
        catch (LockException ex) {
            // ok
        }

    }

    /**
     * Tests the update sheet method with an incorrect sheet data version.
     */
    public void testIncorrectSheetDataVersionCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        // Retrieving the sheet
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);
        assertNotNull("Sheet #" + lSheetId + " does not exist in DB.",
                lSheetData);

        // Modifying and updating the sheet
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldDatas =
                lSheetData.getFieldValues();
        lFieldDatas.get(1).setValue(FIELD_NAME);

        // Get current version of the sheet.
        lSheetData.setVersion(lSheetData.getVersion() + 1);

        try {
            sheetService.updateSheet(lAdminRoleToken, getProcessName(),
                    lSheetData, Context.getContext());
        }
        catch (StaleSheetDataException ex) {
            // ok
            assertNotSame(
                    "The expected version and the sheet version must not be equals",
                    ex.getExpectedVersion(), ex.getSheetVersion());
        }
        catch (Throwable e) {
        }
    }

    /**
     * Tests the update sheet when the multivalued fields are not referenced in
     * the sheet. Fields must be not updated.
     */
    public void testUpdateWithoutMultivaluedFields() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();
        CacheableSheetType lSheetType =
                getSheetService().getCacheableSheetTypeByName(normalRoleToken,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);

        // Get the sheet
        BusinessSheet lOldSheet =
                new CacheableSheetAccess(normalRoleToken, lSheetType,
                        getSheetService().getCacheableSheet(normalRoleToken,
                                lSheetId, CacheProperties.IMMUTABLE),
                        ValuesAccessProperties.NOT_CHECKED_READ_ONLY);
        CacheableSheet lModifiedSheet =
                getSheetService().getCacheableSheet(normalRoleToken, lSheetId,
                        CacheProperties.MUTABLE);

        // Delete all multivalued fields to the sheet
        for (Field lField : lSheetType.getFields()) {
            if (lField.isMultivalued()) {
                lModifiedSheet.removeFieldValue(lField.getLabelKey());
            }
        }

        // Save sheet without multivalued fields

        getSheetService().updateSheet(normalRoleToken, lModifiedSheet,
                Context.getContext());

        // Get the sheet
        BusinessSheet lNewSheet =
                new CacheableSheetAccess(normalRoleToken, lSheetType,
                        getSheetService().getCacheableSheet(normalRoleToken,
                                lSheetId, CacheProperties.IMMUTABLE),
                        ValuesAccessProperties.NOT_CHECKED_READ_ONLY);

        // Compare fields
        for (BusinessField lBusinessField : lOldSheet) {
            assertTrue(lBusinessField.hasSameValues(lNewSheet.getField(lBusinessField.getFieldName())));
        }

    }
}
/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Nicolas Samson (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>deleteSheet<CODE> of the Sheet Service.
 * 
 * @author nsamson, mmennad
 */
public class TestDeleteSheet extends AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The Sheet indice. */
    private static final int SHEET_INDICE = 0;

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestDeleteSheetServiceConfidentialAccess.xml";

    /** The XML used to instantiate the not deletable test case */
    private static final String XML_INSTANCE_NOT_DELETABLE_TEST =
            "sheet/TestDeleteSheetNotDeletableService.xml";

    /** Role name for not creatable test */
    private static final String ROLE_NAME = "notadmin";

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The read lock sheet */
    private static final String SHEET_REF = "Milou";

    /**
     * Tests the deleteSheet method in normal case.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.MUTABLE);

        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(SHEET_INDICE).getId();

        // Gets the sheet #SHEET_ID to put it in the cache
        CacheableSheet lSheet =
                sheetService.getCacheableSheet(adminRoleToken, lId,
                        CacheProperties.MUTABLE);
        assertNotNull("getSheetByKey returns a sheet null, it should not",
                lSheet);

        // Deleting the sheet
        sheetService.deleteSheet(adminRoleToken, lId, Context.getContext());

        // Try to get the sheet #SHEET_ID
        try {
            lSheet =
                    sheetService.getCacheableSheet(adminRoleToken, lId,
                            CacheProperties.MUTABLE);
        }
        catch (NullPointerException lNPE) {
            lSheet = null;
        }
        catch (InvalidIdentifierException e) {
            lSheet = null;
        }

        assertNull("Sheet #" + lId + " has not been deleted in DB.", lSheet);
    }

    /**
     * Tests the deleteSheet method with a confidential access.
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Gets the sheet type
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.MUTABLE);

        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);

        // Gets a Id

        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(SHEET_INDICE).getId();

        // Gets the sheet #SHEET_ID to put it in the cache
        CacheableSheet lSheet =
                sheetService.getCacheableSheet(adminRoleToken, lId,
                        CacheProperties.MUTABLE);
        assertNotNull("getSheetByKey returns a sheet null, it should not",
                lSheet);

        // set confidential access on sheet type SHEET_TYPE
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Deleting the sheet
        try {
            sheetService.deleteSheet(lRoleToken, lId, Context.getContext());
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok
        }
        authorizationService.logout(lUserToken);

        // Try to get the sheet #SHEET_ID
        try {
            lSheet =
                    sheetService.getCacheableSheet(adminRoleToken, lId,
                            CacheProperties.MUTABLE);
        }
        catch (NullPointerException lNPE) {
            fail("The sheet has been deleted.");
        }

        assertNotNull("Sheet #" + lId + " has not been deleted in DB.", lSheet);
    }

    /**
     * Tests the deleteSheet method with a not deletable restriction.
     */
    public void testNotDeletableAuthorizationCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Gets the sheet type
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.MUTABLE);

        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);
        // Gets a Id

        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(SHEET_INDICE).getId();

        // Gets the sheet #SHEET_ID to put it in the cache
        CacheableSheet lSheet =
                sheetService.getCacheableSheet(adminRoleToken, lId,
                        CacheProperties.MUTABLE);
        assertNotNull("getSheetByKey returns a sheet null, it should not",
                lSheet);

        // set not deletetable authorization on sheet type SHEET_TYPE
        instantiate(getProcessName(), XML_INSTANCE_NOT_DELETABLE_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Deleting the sheet
        try {
            sheetService.deleteSheet(lRoleToken, lId, Context.getContext());
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok
        }
        finally {
            authorizationService.logout(lUserToken);
        }

        // Try to get the sheet #SHEET_ID
        try {
            lSheet =
                    sheetService.getCacheableSheet(adminRoleToken, lId,
                            CacheProperties.MUTABLE);
        }
        catch (NullPointerException lNPE) {
            fail("The sheet has been deleted.");
        }

        assertNotNull("Sheet #" + lId + " has not been deleted in DB.", lSheet);
    }

    /**
     * Tests the deleteSheet method by the user2 with a READ_WRITE_LOCK set by
     * user1
     */
    public void testReadWriteLockOnSheetCase() {
        sheetService = serviceLocator.getSheetService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Search Milou sheet data

        String lId = sheetService.getSheetIdByReference(lRoleToken, SHEET_REF);

        // Gets the sheet #SHEET_ID to put it in the cache
        CacheableSheet lSheet =
                sheetService.getCacheableSheet(adminRoleToken, lId,
                        CacheProperties.MUTABLE);
        assertNotNull("getSheetByKey returns a sheet null, it should not",
                lSheet);

        try {
            sheetService.deleteSheet(lRoleToken, lId, Context.getContext());
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
    }
}
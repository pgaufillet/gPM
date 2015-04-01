/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision;

import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>getRevisionDataByLabel<CODE> of the Revision Service.
 * 
 * @author mfranche
 */
public class TestGetRevisionDataByLabelService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The created revision label */
    private static final String REVISION_LABEL = "Label_1";

    /** The incorrect revision label */
    private static final String REVISION_INCORRECT_LABEL = "IncorrectLabel";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the getRevisionDataByLabel method in a normal case.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Verify if the sheet contains at least one revision
        int lRevisionsCount =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        if (lRevisionsCount == 0) {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABEL);
        }

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        startTimer();
        RevisionData lRevisionData =
                revisionService.getRevisionDataByLabel(adminRoleToken,
                        lSheetId, REVISION_LABEL);
        stopTimer();

        assertNotNull("Revision #" + lRevisionId + " does not exist in DB.",
                lRevisionData);
    }

    /**
     * Tests the method on a sheet type with revisionEnabled set to false.
     */
    public void testRevisionNotEnabledCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        try {
            revisionService.getRevisionDataByLabel(adminRoleToken, lSheetId,
                    REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok -> revision not enabled
        }
        catch (InvalidIdentifierException ex) {
            // ok -> revision not enabled so no label
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Tests the getRevisionDataByLabel with an incorrect container id
     */
    public void testIncorrectContainerIdCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Verify if the sheet contains at least one revision
        int lRevisionsCount =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        if (lRevisionsCount == 0) {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABEL);
        }

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        try {
            revisionService.getRevisionDataByLabel(adminRoleToken,
                    INVALID_CONTAINER_ID, REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Tests the getRevisionDataByLabel with an incorrect label
     */
    public void testIncorrectLabelCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Verify if the sheet contains at least one revision
        int lRevisionsCount =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        if (lRevisionsCount == 0) {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABEL);
        }

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        try {

            revisionService.getRevisionDataByLabel(adminRoleToken, lSheetId,
                    REVISION_INCORRECT_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException ex) {
            // ok
        }
    }

    /**
     * Tests the getRevisionDataByLabel with an invalid token
     */
    public void testInvalidTokenCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Verify if the sheet contains at least one revision
        int lRevisionsCount =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        if (lRevisionsCount == 0) {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABEL);
        }

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        try {

            revisionService.getRevisionDataByLabel("", lSheetId, REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (InvalidTokenException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an InvalidTokenException.");
        }
    }

    /**
     * Tests the getRevisionDataByLabel method by the user2 with a
     * READ_WRITE_LOCK set by user1
     */
    public void testReadWriteLockOnSheetCase() {
        testLockOnSheetCase(SHEET_READ_WRITE_LOCK_REF);
    }

    /**
     * Tests the getRevisionDataByLabel method on locked sheet.
     * 
     * @param pSheetReference
     *            The sheet reference
     */
    protected void testLockOnSheetCase(String pSheetReference) {
        sheetService = serviceLocator.getSheetService();

        revisionService = serviceLocator.getRevisionService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Search sheet data
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Bernard's store", 
                    SHEET_READ_WRITE_LOCK_REF);

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        try {
            revisionService.getRevisionDataByLabel(lRoleToken, lSheetId,
                    lRevisionId);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests the getRevisionDataByLabel method.
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        // set confidential access on sheet type SHEET_TYPE
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        try {
            revisionService.getRevisionDataByLabel(lRoleToken, lId, "");
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok -> can access to sheet
        }
        catch (InvalidIdentifierException ex) {
            // ok -> label not exist
        }
        finally {
            authorizationService.logout(lUserToken);
        }
    }
}

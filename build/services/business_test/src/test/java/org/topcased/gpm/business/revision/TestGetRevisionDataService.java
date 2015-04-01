/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
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
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>getRevisionData<CODE> of the Revision Service.
 * 
 * @author mfranche
 */
public class TestGetRevisionDataService extends AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The revision label */
    private static final String REVISION_LABEL = "Label_1";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the getSheetByKey method.
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

            //            AttributeData lAttributeData =
            //                    new AttributeData("attribute_Name",
            //                            new String[] { "Attribute_Name_Value" });
            //            AttributeData[] lAttributeDataTab =
            //                    new AttributeData[] { lAttributeData };
            //            lRevisionData.setAttributeDatas(lAttributeDataTab);

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
                revisionService.getRevisionData(adminRoleToken, lSheetId,
                        lRevisionId);
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
        AttributesService lAttributeService = serviceLocator.getAttributesService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        AttributeData[] lAttributedatas = 
        {new AttributeData("revisionSupport", new String[] {"false"})};
        lAttributeService.set(lSheetSummary.get(0).getSheetTypeId(), lAttributedatas);
        
        try {
            revisionService.getRevisionData(adminRoleToken, lSheetId, "");
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Tests the method getRevisionData with an incorrect container id
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
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        try {
            revisionService.getRevisionData(adminRoleToken,
                    INVALID_CONTAINER_ID, lRevisionId);
            fail("The exception has not been thrown.");
        }

        catch (InvalidIdentifierException ex) {
            assertEquals(ex.getIdentifier(), INVALID_CONTAINER_ID);
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Tests the method getRevisionData with an incorrect revision id
     */
    public void testIncorrectRevisionIdCase() {
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
            revisionService.getRevisionData(adminRoleToken, lSheetId, "");
            fail("The exception has not been thrown.");
        }

        catch (InvalidIdentifierException ex) {
            // ok
        }
    }

    /**
     * Tests the method getRevisionData with an invalid token
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
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        // Retrieving the revision
        try {
            revisionService.getRevisionData("", lSheetId, lRevisionId);
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
     * Tests the getRevisionData method by the user2 with a READ_WRITE_LOCK set
     * by user1
     */
    public void testReadWriteLockOnSheetCase() {
        testLockOnSheetCase(SHEET_READ_WRITE_LOCK_REF);
    }

    /**
     * Tests the getRevisionData method on locked sheet.
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
        assertFalse("getRevisionsSummary returns no revision",
                lRevisionSummary.isEmpty());
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        try {
            revisionService.getRevisionData(lRoleToken, lSheetId, lRevisionId);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("Unexpected exception raised: " + e.getMessage());
        }
    }

    /**
     * Tests the getRevisionData method.
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
            revisionService.getRevisionData(lRoleToken, lId, "");
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok
        }
        finally {
            authorizationService.logout(lUserToken);
        }
    }
}

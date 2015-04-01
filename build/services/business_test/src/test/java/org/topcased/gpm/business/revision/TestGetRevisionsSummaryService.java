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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestGetRevisionsSummaryService
 * 
 * @author mfranche
 */
public class TestGetRevisionsSummaryService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The revisions labels. */
    private static final String[] REVISION_LABELS = { "LABEL_1", "LABEL_2" };

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";
    
    /**
     * Tests the getRevisionsSummary method
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

        final int lExpectedRevisionsSize = REVISION_LABELS.length;

        if (lRevisionsCount <= lExpectedRevisionsSize) {
            //            RevisionData lRevisionData =
            //                    revisionService.getRevisionModel(roleToken, lSheetType,
            //                            getProductName());
            //            lRevisionData.setLabel(REVISION_LABELS[0]);
            //            AttributeData lAttributeData =
            //                    new AttributeData("attribute_Name",
            //                            new String[] { "Attribute_Name_Value" });
            //            AttributeData[] lAttributeDataTab =
            //                    new AttributeData[] { lAttributeData };
            //            lRevisionData.setAttributeDatas(lAttributeDataTab);
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABELS[0]);

            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABELS[1]);
        }

        // The main goal of the test
        Collection<RevisionSummaryData> lRevsSummaries =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);

        assertNotNull(
                "getRevisionsSummary returns an null collection instead of a list of revisions",
                lRevsSummaries);
        assertFalse("getRevisionsSummary returns an empty revision list",
                lRevsSummaries.isEmpty());
        assertEquals("getRevisionsSummary returns a collection of size "
                + lRevsSummaries.size() + " instead of "
                + lExpectedRevisionsSize, lRevsSummaries.size(),
                lExpectedRevisionsSize);

        for (RevisionSummaryData lRevisionsSummaryData : lRevsSummaries) {
            assertTrue("The revision label " + lRevisionsSummaryData.getLabel()
                    + " is not an expected revision.", Arrays.asList(
                    REVISION_LABELS).contains(lRevisionsSummaryData.getLabel()));

            if (lRevisionsSummaryData.getLabel().compareTo(REVISION_LABELS[0]) == 0) {
                verifyRevisionSummaryDataLabel1(lRevisionsSummaryData, lSheetId);
            }
        }
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
            revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
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
     * Verify the revision summary data with label1
     * 
     * @param pRevSummaryData
     *            The revisionSummaryData to be tested.
     * @param pSheetId
     *            The sheet id containing the revision
     */
    protected void verifyRevisionSummaryDataLabel1(
            RevisionSummaryData pRevSummaryData, String pSheetId) {
        RevisionData lRevData =
                revisionService.getRevisionDataByLabel(adminRoleToken,
                        pSheetId, pRevSummaryData.getLabel());

        assertEquals(
                "The author attribute of the revisionSummaryData is incorrect.",
                pRevSummaryData.getAuthor(), lRevData.getAuthor());
        assertEquals(
                "The creation date attribute of the revisionSummaryData is incorrect.",
                pRevSummaryData.getCreationDate(), lRevData.getCreationDate());
        assertEquals(
                "The id attribute of the revisionSummaryData is incorrect.",
                pRevSummaryData.getId(), lRevData.getId());
    }

    /**
     * Tests the getRevisions summary with an incorrect container id
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
        if (lRevisionsCount <= 2) {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABELS[0]);

            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABELS[1]);
        }

        try {
            revisionService.getRevisionsSummary(adminRoleToken,
                    INVALID_CONTAINER_ID);
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
     * Tests the getRevisionsSummary method by the user2 with a READ_WRITE_LOCK
     * set by user1
     */
    public void testReadWriteLockOnSheetCase() {
        testLockOnSheetCase(SHEET_READ_WRITE_LOCK_REF);
    }

    /**
     * Tests the getRevisionsSummary method on locked sheet.
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

        try {
            revisionService.getRevisionsSummary(lRoleToken, lSheetId);
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
     * Tests the getRevisionsSummary method.
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
            revisionService.getRevisionsSummary(lRoleToken, lId);
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

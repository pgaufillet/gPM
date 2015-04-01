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
 * Tests the method <CODE>deleteRevision<CODE> of the Revision Service
 * 
 * @author mfranche
 */
public class TestDeleteRevisionService extends AbstractBusinessServiceTestCase {

    /** The sheet type used for normal case. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The Sheet indice. */
    private static final int SHEET_INDICE = 0;

    private static final String REVISION_LABEL1 = "Label1";

    private static final String REVISION_LABEL2 = "Label2";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the deleteRevision method in normal case.
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
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(SHEET_INDICE).getId();

        // Create a revision on the sheet
        revisionService.createRevision(adminRoleToken, lSheetId,
                REVISION_LABEL1);

        // Create a snd revision on the sheet
        String lCreatedRevisionId2 =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL2);

        // Verify the sheet contains two revisions at least
        int lRevisionNbBeforeDeletion =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        assertTrue("The sheet must contain at least two revisions",
                lRevisionNbBeforeDeletion >= 2);

        // Deleting the latest revision (ie revision of id lCreatedRevisionId2)
        revisionService.deleteRevision(adminRoleToken, lSheetId);

        // Verify that the revision count of the sheet has been decreased.
        int lRevisionNbAfterDeletion =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        assertTrue("The revision count of the sheet has not been decreased",
                lRevisionNbAfterDeletion == lRevisionNbBeforeDeletion - 1);

        // Try to get the revision lCreatedRevisionId2
        RevisionData lRevision = null;
        try {
            lRevision =
                    revisionService.getRevisionData(adminRoleToken, lSheetId,
                            lCreatedRevisionId2);
        }
        catch (InvalidIdentifierException ex) {
            lRevision = null;
        }

        assertNull("Revision #" + lCreatedRevisionId2
                + " has not been deleted in DB.", lRevision);

    }

    /**
     * Tests the method in revision not enabled case.
     */
    public void testRevisionNotEnabledCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        AttributesService lAttributeService = serviceLocator.getAttributesService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(SHEET_INDICE).getId();
        try {
            AttributeData[] lAttributedatas = 
            {new AttributeData("revisionSupport", new String[] {"false"})};
            lAttributeService.set(lSheetSummary.get(SHEET_INDICE).getSheetTypeId(), lAttributedatas);
            
            revisionService.deleteRevision(adminRoleToken, lSheetId);
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
     * Tests the deleteRevision with an invalid container case
     */
    public void testInvalidContainerIdCase() {
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
        assertFalse("getSheets returns an empty list", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(SHEET_INDICE).getId();

        // Create a revision on the sheet
        revisionService.createRevision(adminRoleToken, lSheetId,
                REVISION_LABEL1);

        // Create a snd revision on the sheet
        revisionService.createRevision(adminRoleToken, lSheetId,
                REVISION_LABEL2);

        // Verify the sheet contains two revisions at least
        int lRevisionNbBeforeDeletion =
                revisionService.getRevisionsCount(adminRoleToken, lSheetId);
        assertTrue("The sheet must contain at least two revisions",
                lRevisionNbBeforeDeletion >= 2);

        try {
            // Deleting the latest revision (ie revision of id lCreatedRevisionId2)
            revisionService.deleteRevision(adminRoleToken, INVALID_CONTAINER_ID);
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
     * Tests the DeleteRevision method by the user2 with a READ_WRITE_LOCK set
     * by user1
     */
    public void testReadWriteLockOnSheetCase() {
        sheetService = serviceLocator.getSheetService();

        revisionService = serviceLocator.getRevisionService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Bernard's store", 
                    SHEET_READ_WRITE_LOCK_REF);

        try {
            revisionService.deleteRevision(lRoleToken, lSheetId);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
    }

    /**
     * Tests the delete revision method.
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
            revisionService.deleteRevision(lRoleToken, lId);
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

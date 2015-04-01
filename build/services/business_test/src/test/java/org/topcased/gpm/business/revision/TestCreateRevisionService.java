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

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>createRevision<CODE> of the Revision Service.
 * 
 * @author mfranche
 */
public class TestCreateRevisionService extends AbstractBusinessServiceTestCase {

    /** The sheet type used for normal case. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The label set on the new revision */
    private static final String REVISION_LABEL = "Label_Revision_10";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** Admin login */
    private static final String ADMIN_LOGIN = GpmTestValues.USER_ADMIN;

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the creation of a revision in a normal way.
     */
    public void testNormalCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // The main goal of this test...
        startTimer();
        String lRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL);
        stopTimer();

        // We must verify that the revision has been truely created ;)
        RevisionData lCreatedRevisionData =
                revisionService.getRevisionData(adminRoleToken, lSheetId,
                        lRevisionId);
        assertNotNull("Revision #" + lRevisionId + " hasn't been created.",
                lCreatedRevisionData);

        assertEquals("The created revision has not the expected author.",
                ADMIN_LOGIN, lCreatedRevisionData.getAuthor());
        assertEquals("The created revision has not the expected label.",
                REVISION_LABEL, lCreatedRevisionData.getLabel());
    }

    /**
     * Tests the method with an incorrect label.
     */
    public void testIncorrectLabelCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // The main goal of this test...
        try {
            revisionService.createRevision(adminRoleToken, lSheetId, "");
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Tests the creation of a revision with an existing label. (A revision with
     * the same label already exists on the sheet).
     */
    public void testAlreadyExistingLabel() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.

        // The main goal of this test...

        revisionService.createRevision(adminRoleToken, lSheetId, REVISION_LABEL);

        try {
            revisionService.createRevision(adminRoleToken, lSheetId,
                    REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Tests the creation of a revision with revisionEnabled property set to
     * false.
     */
    public void testRevisionNotEnabledCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();
        AttributesService lAttributeService = serviceLocator.getAttributesService();
        
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.

        // The main goal of this test...

        String lRevisionId = null;
        AttributeData[] lAttributedatas = 
        {new AttributeData("revisionSupport", new String[] {"false"})};
        lAttributeService.set(lSheetSummary.get(0).getSheetTypeId(), lAttributedatas);

        try {
            lRevisionId =
                    revisionService.createRevision(adminRoleToken, lSheetId,
                            REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }

        assertNull("The revision isn't null", lRevisionId);

    }

    /**
     * Tests the method createRevision with an invalid token.
     */
    public void testInvalidTokenCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.

        // The main goal of this test...
        try {
            revisionService.createRevision("", lSheetId, REVISION_LABEL);
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
     * Tests the createRevision method with an incorrect container id.
     */
    public void testIncorrectContainerIdCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        lSheetSummary.get(0).getId();

        // Next the model of a revision.

        // The main goal of this test...
        try {
            revisionService.createRevision(adminRoleToken,
                    INVALID_CONTAINER_ID, "Label");
            fail("The exception has not been thrown.");
        }

        catch (InvalidIdentifierException ex) {
            assertEquals(ex.getIdentifier(), INVALID_CONTAINER_ID);
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Tests the CreateRevision method by the user2 with a READ_WRITE_LOCK set
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
            revisionService.createRevision(lRoleToken, lSheetId, REVISION_LABEL);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // Ok, we got a lock exception.
            // Check that the identifier in this exception is correct.
            assertEquals(ex.getContainerId(), lSheetId);
        }
        catch (Throwable e) {
            fail("The exception thrown is not a LockException.");
        }
    }

    /**
     * Tests the createRevision method.
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
            revisionService.createRevision(lRoleToken, lId, "l");
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

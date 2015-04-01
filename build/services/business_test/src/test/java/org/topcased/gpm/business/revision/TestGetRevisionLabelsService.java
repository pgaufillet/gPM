/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
 * TestGetRevisionLabelsService
 * 
 * @author mfranche
 */
public class TestGetRevisionLabelsService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The revisions labels. */
    private static final String[] REVISION_LABELS =
            { "LABEL_1", "LABEL_2", "LABEL_3" };

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the getRevisionLabels method
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
            RevisionData lRevData1 = new RevisionData();
            lRevData1.setLabel(REVISION_LABELS[0]);
            Calendar lCal1 = new GregorianCalendar();
            lCal1.set(Calendar.HOUR, 10);
            lRevData1.setCreationDate(lCal1.getTime());
            lRevData1.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData1);

            RevisionData lRevData2 = new RevisionData();
            lRevData2.setLabel(REVISION_LABELS[1]);
            Calendar lCal2 = new GregorianCalendar();
            lCal2.set(Calendar.HOUR, 11);
            lRevData2.setCreationDate(lCal2.getTime());
            lRevData2.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData2);

            RevisionData lRevData3 = new RevisionData();
            lRevData3.setLabel(REVISION_LABELS[2]);
            Calendar lCal3 = new GregorianCalendar();
            lCal3.set(Calendar.HOUR, 12);
            lRevData3.setCreationDate(lCal3.getTime());
            lRevData3.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData3);
        }

        // The main goal of the test
        List<String> lRevLabels =
                revisionService.getRevisionLabels(adminRoleToken, lSheetId, 2);

        assertNotNull(
                "getRevisionLabels returns an null collection instead of a list of labels",
                lRevLabels);
        assertFalse("getRevisionLabels returns an empty revision list",
                lRevLabels.isEmpty());
        assertTrue("getRevisionLabels return a list of size "
                + lRevLabels.size() + " instead of a list of 2 element.",
                lRevLabels.size() == 2);

        assertEquals("First revision label is incorrect.", REVISION_LABELS[2],
                lRevLabels.get(0));

        assertEquals("Snd revision label is incorrect.", REVISION_LABELS[1],
                lRevLabels.get(1));
    }

    /**
     * Test the getRevisionLabels method when no revision has been set.
     */
    public void testNotRevisionCase() {
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

        // The main goal of the test
        List<String> lRevLabels =
                revisionService.getRevisionLabels(adminRoleToken, lSheetId, 2);

        assertNotNull(
                "getRevisionLabels returns an null collection instead of a list of labels",
                lRevLabels);
        assertTrue("getRevisionLabels should return an empty list",
                lRevLabels.isEmpty());
    }

    /**
     * Tests the method getRevisionLabels : the sheet contains 3 revisions, the
     * method asks for 5 revisions. Check that the returned list contains only 3
     * revisions, in correct order.
     */
    public void testNotEnoughRevisionCase() {
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

            RevisionData lRevData1 = new RevisionData();
            lRevData1.setLabel(REVISION_LABELS[0]);
            Calendar lCal1 = new GregorianCalendar();
            lCal1.set(Calendar.HOUR, 10);
            lRevData1.setCreationDate(lCal1.getTime());
            lRevData1.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData1);

            RevisionData lRevData2 = new RevisionData();
            lRevData2.setLabel(REVISION_LABELS[1]);
            Calendar lCal2 = new GregorianCalendar();
            lCal2.set(Calendar.HOUR, 11);
            lRevData2.setCreationDate(lCal2.getTime());
            lRevData2.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData2);

            RevisionData lRevData3 = new RevisionData();
            lRevData3.setLabel(REVISION_LABELS[2]);
            Calendar lCal3 = new GregorianCalendar();
            lCal3.set(Calendar.HOUR, 12);
            lRevData3.setCreationDate(lCal3.getTime());
            lRevData3.setAuthor(ADMIN_LOGIN[0]);
            revisionService.createRevision(adminRoleToken, lSheetId, lRevData3);
        }

        // The main goal of the test
        List<String> lRevLabels =
                revisionService.getRevisionLabels(adminRoleToken, lSheetId, 5);

        assertNotNull(
                "getRevisionLabels returns an null collection instead of a list of labels",
                lRevLabels);
        assertFalse("getRevisionLabels returns an empty revision list",
                lRevLabels.isEmpty());
        assertTrue("getRevisionLabels return a list of size "
                + lRevLabels.size() + " instead of a list of 2 element.",
                lRevLabels.size() == 3);

        assertEquals("First revision label is incorrect.", REVISION_LABELS[2],
                lRevLabels.get(0));

        assertEquals("Snd revision label is incorrect.", REVISION_LABELS[1],
                lRevLabels.get(1));

        assertEquals("Third revision label is incorrect.", REVISION_LABELS[0],
                lRevLabels.get(2));
    }

    /**
     * Tests the method on a sheet type with revisionEnabled set to false.
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
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        try {
            AttributeData[] lAttributedatas = 
            {new AttributeData("revisionSupport", new String[] {"false"})};
            lAttributeService.set(lSheetSummary.get(0).getSheetTypeId(), lAttributedatas);
            
            revisionService.getRevisionLabels(adminRoleToken, lSheetId, 1);
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
     * Tests the getRevisionLabels with an incorrect container id
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
            revisionService.getRevisionLabels(adminRoleToken,
                    INVALID_CONTAINER_ID, 1);
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
     * Tests the getRevisionLabels method by the user2 with a READ_WRITE_LOCK
     * set by user1
     */
    public void testReadWriteLockOnSheetCase() {
        testLockOnSheetCase(SHEET_READ_WRITE_LOCK_REF);
    }

    /**
     * Tests the getRevisionLabels method on locked sheet.
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
                    pSheetReference);

        try {
            revisionService.getRevisionLabels(lRoleToken, lSheetId, 1);
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
     * Tests the getRevisionLabels method.
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
            revisionService.getRevisionLabels(lRoleToken, lId, 1);
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

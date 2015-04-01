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
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.sheet.service.sheetAccess.SheetDataAccess;
import org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData;

/**
 * Tests the method
 * <CODE>updateContainerFromRevision<CODE> of the Revision Service.
 * 
 * @author mfranche
 */
public class TestUpdateContainerFromRevisionService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used for normal case. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The label set on the first revision */
    private static final String REVISION_LABEL1 = "Label_Revision_1";

    /** The field name of the sheet */
    private static final String FIELD_NAME = "DOG_ref";

    /** The new field value */
    private static final String NEW_FIELD_VALUE = "newDogRefValue";

    /**
     * The old field value (before the modification)
     */
    private static final String OLD_FIELD_VALUE = "Lassie";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /**
     * The reference field name
     */
    private static final String REFERENCE_FIELD_NAME = "DOG_name";

    /** The new reference field value */
    private static final String NEW_REFERENCE_FIELD_VALUE = "newDogNameValue";

    /**
     * The old reference field value (before the modification)
     */
    private static final String OLD_REFERENCE_FIELD_VALUE = "Lassie";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /**
     * Tests the updateContainerFromRevision method in a normal Case.
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

        // Next the model of a revision.
        SheetData lSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // Create the first revision
        String lRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL1);

        // Modify a field of the sheet data
        SheetDataAccess lSheetDataAccess = new SheetDataAccess(lSheetData);
        ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).setValue(NEW_FIELD_VALUE);

        // Modify a field reference of the sheet data
        ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                REFERENCE_FIELD_NAME)).setValue(NEW_REFERENCE_FIELD_VALUE);

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        lSheetData = sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // The main goal of the test
        revisionService.updateContainerFromRevision(adminRoleToken, lSheetId,
                lRevisionId);

        // Verify that the current sheet data has been corretly updated with the revision content.
        SheetData lUpdatedSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);
        lSheetDataAccess = new SheetDataAccess(lUpdatedSheetData);

        String lFieldValue =
                ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).getValue();
        assertNotNull("UpdateContainerFromRevision test is not OK.",
                lFieldValue);
        assertTrue("UpdateContainerFromRevision test is not OK.",
                lFieldValue.compareTo(OLD_FIELD_VALUE) == 0);

        String lRefValue =
                ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                        REFERENCE_FIELD_NAME)).getValue();
        assertNotNull("UpdateContainerFromRevision test is not OK.", lRefValue);
        assertTrue("UpdateContainerFromRevision test is not OK.",
                lRefValue.compareTo(OLD_REFERENCE_FIELD_VALUE) == 0);
    }

    /**
     * Tests the UpdateContainerFromRevision method by the user2 with a
     * READ_WRITE_LOCK set by user1
     */

    public void testReadWriteLockOnSheet() {
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
            revisionService.updateContainerFromRevision(lRoleToken, lSheetId,
                    lRevisionId);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
    }

    /**
     * Tests the updateContainerFromRevision method.
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
            revisionService.updateContainerFromRevision(lRoleToken, lId, "");
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

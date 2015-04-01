/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.List;

import org.topcased.gpm.ws.v2.client.FieldValueData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.RevisionData;
import org.topcased.gpm.ws.v2.client.RevisionSummaryData;
import org.topcased.gpm.ws.v2.client.SheetData;

/**
 * Test revision.
 * 
 * @author ogehin
 */
public class TestWSRevision extends AbstractWSTestCase {

    /** The label set on the new revision */
    private static final String REVISION_LABEL = "Label_Revision_1";

    /**
     * Test the createRevision and the deleteRevision methods in normal
     * conditions.
     */
    public void testCreateDeleteRevisionNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            String lRevisionId =
                    staticServices.createRevision(roleToken, lSheetKey,
                            REVISION_LABEL);

            // We must verify that the revision has been truely created.
            RevisionData lCreatedRevisionData =
                    staticServices.getRevision(roleToken, lSheetKey,
                            lRevisionId);
            assertNotNull("Revision #" + lRevisionId + " hasn't been created.",
                    lCreatedRevisionData);

            assertEquals("The created revision has not the expected author.",
                    DEFAULT_LOGIN[0], lCreatedRevisionData.getAuthor());
            assertEquals("The created revision has not the expected label.",
                    REVISION_LABEL, lCreatedRevisionData.getLabel());

            staticServices.deleteRevision(roleToken, lSheetKey);

            try {
                //RevisionData lDeletedRevisionData =
                staticServices.getRevision(roleToken, lSheetKey, lRevisionId);
            }
            catch (GDMException_Exception e) {
                assertTrue("The deleteRevision failed in normal conditions.",
                        true);
            }

        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getRevisionByLabel method in normal conditions.
     */
    public void testGetRevisionByLabelNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            String lRevisionId =
                    staticServices.createRevision(roleToken, lSheetKey,
                            REVISION_LABEL);
            assertNotNull("An error occured when creating a revision",
                    lRevisionId);
            RevisionData lRevision =
                    staticServices.getRevisionByLabel(roleToken, lSheetKey,
                            REVISION_LABEL);
            assertNotNull(
                    "The getRevisionByLabel method failed in normal conditions.",
                    lRevision);
            assertEquals(
                    "The getRevisionByLabel method returns a bad identifier : "
                            + lRevision.getId() + "instead of " + lRevisionId,
                    lRevision.getId(), lRevisionId);
            staticServices.deleteRevision(roleToken, lSheetKey);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getRevisionsCount method in normal conditions.
     */
    public void testGetRevisionsCountNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            int lRevisionsCount =
                    staticServices.getRevisionsCount(roleToken, lSheetKey);
            int lExpectedRes = 2;
            assertEquals("The getRevisionsCount method returns "
                    + lRevisionsCount + " instead of " + lExpectedRes,
                    lRevisionsCount, lExpectedRes);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getRevisionsSummary method in normal conditions.
     */
    public void testGetRevisionsSummaryNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            List<RevisionSummaryData> lRevisionSummaries =
                    staticServices.getRevisionsSummary(roleToken, lSheetKey);
            int lExpectedRes = 2;
            assertEquals(
                    "The getRevisionsCount method returns "
                            + lRevisionSummaries.size() + " instead of "
                            + lExpectedRes, lRevisionSummaries.size(),
                    lExpectedRes);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetDataInRevision method in normal conditions.
     */
    public void testGetSheetDataInRevisionNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            String lRevisionId =
                    staticServices.createRevision(roleToken, lSheetKey,
                            REVISION_LABEL);

            List<FieldValueData> lFieldValues = lSheets.get(0).getFieldValues();
            String lOldDate = "";
            for (FieldValueData lFieldValueData : lFieldValues) {
                if ("DOG_birthdate".equals(lFieldValueData.getName())) {
                    lOldDate = lFieldValueData.getValue();
                    lFieldValueData.setValue("2008-06-04");
                    break;
                }
            }

            staticServices.updateSheets(roleToken, DEFAULT_PROCESS_NAME,
                    lSheets);

            SheetData lSheet =
                    staticServices.getSheetDataInRevision(roleToken, lSheetKey,
                            lRevisionId);
            String lNewDate = "";
            for (FieldValueData lFieldValueData : lSheet.getFieldValues()) {
                if ("DOG_birthdate".equals(lFieldValueData.getName())) {
                    lNewDate = lFieldValueData.getValue();
                    break;
                }
            }
            assertEquals("GetRevisionAsSheetData is not OK.", lNewDate,
                    lOldDate);
            staticServices.deleteRevision(roleToken, lSheetKey);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetDataByRevisionLabel method in normal conditions.
     */
    public void testGetSheetDataByRevisionLabelNormalCase() {
        try {
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            generateList("Medor", null));
            String lSheetKey = lSheets.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);

            staticServices.createRevision(roleToken, lSheetKey, REVISION_LABEL);

            List<FieldValueData> lFieldValues = lSheets.get(0).getFieldValues();
            String lOldDate = "";
            for (FieldValueData lFieldValueData : lFieldValues) {
                if ("DOG_birthdate".equals(lFieldValueData.getName())) {
                    lOldDate = lFieldValueData.getValue();
                    lFieldValueData.setValue("2008-06-04");
                    break;
                }
            }

            staticServices.updateSheets(roleToken, DEFAULT_PROCESS_NAME,
                    lSheets);

            SheetData lSheet =
                    staticServices.getSheetDataByRevisionLabel(roleToken,
                            lSheetKey, REVISION_LABEL);
            String lNewDate = "";
            for (FieldValueData lFieldValueData : lSheet.getFieldValues()) {
                if ("DOG_birthdate".equals(lFieldValueData.getName())) {
                    lNewDate = lFieldValueData.getValue();
                    break;
                }
            }
            assertEquals("GetRevisionAsSheetData is not OK.", lNewDate,
                    lOldDate);
            staticServices.deleteRevision(roleToken, lSheetKey);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }
}
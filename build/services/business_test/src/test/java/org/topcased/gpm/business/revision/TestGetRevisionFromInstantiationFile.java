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
import java.util.Iterator;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.sheet.service.FieldGroupData;

/**
 * Tests the revision creation in the instantiation file
 * 
 * @author mfranche
 */
public class TestGetRevisionFromInstantiationFile extends
        AbstractBusinessServiceTestCase {

    // The sheet reference
    private static final String SHEET_REF = "Medor";

    // The revisions author
    private static final String REVISION_AUTHOR = GpmTestValues.USER_ADMIN;

    // The label for the first revision
    private static final String REVISION_LABEL_1 = "revision_label1";

    // The label for the snd revision
    private static final String REVISION_LABEL_2 = "revision_label2";

    private static final int NB_MULTIPLE_LINE_FIELDS = 5;

    // The ref for the first revision
    private static final String REVISION_REF_LABEL_1 = "Medor_Label1";

    // The ref for the snd revision
    private static final String REVISION_REF_LABEL_2 = "Medor_Label2";

    // The attribute name
    private static final String ATT_NAME = "log";

    // The attribute value for the first revision
    private static final String ATT_VALUE_LABEL_1 = "valeurDuLog_Label1";

    // The attribute value for the snd revision
    private static final String ATT_VALUE_LABEL_2 = "valeurDuLog_Label2";

    /**
     * Tests it in normal case
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Get Sheet data associated to SHEET_REF
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Bernard's store", 
                    SHEET_REF);

        Collection<RevisionSummaryData> lRevSummaryCollection =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertTrue("The sheet with ref " + SHEET_REF
                + " doen't contain at least two revisions.",
                lRevSummaryCollection.size() >= 2);

        Iterator<RevisionSummaryData> lIterator =
                lRevSummaryCollection.iterator();
        RevisionSummaryData lRevisionSummaryData = null;
        RevisionData lRevisionData = null;

        boolean lFirstRev = true;

        while (lIterator.hasNext()) {
            lRevisionSummaryData = lIterator.next();
            lRevisionData =
                    revisionService.getRevisionData(adminRoleToken, lSheetId,
                            lRevisionSummaryData.getId());
            // Verify revision author
            assertTrue(lRevisionData.getAuthor().compareTo(REVISION_AUTHOR) == 0);

            if (lRevisionSummaryData.getLabel().equals(REVISION_LABEL_1)) {
                lFirstRev = true;
            }
            else if (lRevisionSummaryData.getLabel().equals(REVISION_LABEL_2)) {
                lFirstRev = false;
            }

            // Verify revision label and revision creation date
            if (lFirstRev) {
                assertTrue(
                        "The first revision has an incorrect label.",
                        lRevisionData.getLabel().compareTo(REVISION_LABEL_1) == 0);
            }
            else if (lRevisionSummaryData.getLabel().equals(REVISION_LABEL_2)) {
                assertTrue(
                        "The snd revision has an incorrect label.",
                        lRevisionData.getLabel().compareTo(REVISION_LABEL_2) == 0);
            }

            String lNbRevString = "First";
            if (!lFirstRev) {
                lNbRevString = "Snd";
            }

            // Verify revision DOG_Ref field
            assertTrue("The " + lNbRevString
                    + " revision has incorrect fields.",
                    lRevisionData.getFieldGroupDatas().length >= 1);
            FieldGroupData lFieldGroupData =
                    lRevisionData.getFieldGroupDatas()[0];
            assertTrue(
                    "The " + lNbRevString + " revision has incorrect fields.",
                    lFieldGroupData.getMultipleLineFieldDatas().length >= NB_MULTIPLE_LINE_FIELDS);
            MultipleLineFieldData lMultipleLineFieldData =
                    lFieldGroupData.getMultipleLineFieldDatas()[0];
            assertTrue("The " + lNbRevString
                    + " revision has incorrect fields.",
                    lMultipleLineFieldData.getLineFieldDatas().length >= 1);
            LineFieldData lLineFieldData =
                    lMultipleLineFieldData.getLineFieldDatas()[0];
            assertTrue("The " + lNbRevString
                    + " revision has incorrect fields.",
                    lLineFieldData.getFieldDatas().length >= 1);
            FieldData lFieldData = lLineFieldData.getFieldDatas()[0];

            if (lFirstRev) {
                assertTrue(
                        "The " + lNbRevString
                                + " revision has incorrect fields.",
                        lFieldData.getValues().getValues()[0].compareTo(REVISION_REF_LABEL_1) == 0);
            }
            else {
                assertTrue(
                        "The " + lNbRevString
                                + " revision has incorrect fields.",
                        lFieldData.getValues().getValues()[0].compareTo(REVISION_REF_LABEL_2) == 0);
            }

            // Verify revision attributes
            verifyLogAttribute(lRevisionData, lFirstRev, lNbRevString);

        }

    }

    /**
     * Verify the attributes
     * 
     * @param pRevisionData
     *            The revision data
     * @param pFirstRev
     *            Boolean to know if first rev
     * @param pNbRevString
     *            String defining "first" or "snd" according to the position of
     *            the revision
     */
    protected void verifyLogAttribute(RevisionData pRevisionData,
            boolean pFirstRev, String pNbRevString) {

        if (pFirstRev) {
            assertTrue("The first revision has incorrect attributes.",
                    pRevisionData.getAttributeDatas().length >= 2);
        }
        else {
            assertTrue("The second revision has incorrect attributes.",
                    pRevisionData.getAttributeDatas().length >= 1);
        }

        AttributeData lLogAttributeData =
                getLogAttribute(pRevisionData.getAttributeDatas());
        assertNotNull("The log attribute has not been found on the "
                + pNbRevString + " revision.", lLogAttributeData);

        if (pFirstRev) {
            assertTrue(
                    "The log attribute is not correct on the first revision",
                    lLogAttributeData.getValues()[0].compareTo(ATT_VALUE_LABEL_1) == 0);
        }
        else {
            assertTrue(
                    "The log attribute is not correct on the snd revision",
                    lLogAttributeData.getValues()[0].compareTo(ATT_VALUE_LABEL_2) == 0);
        }
    }

    /**
     * Search AttributeData with log name
     * 
     * @param pAttributeDataTab
     *            The attribute data tab
     * @return The AttributeData which name is log (or null if the attribute
     *         data has not been found)
     */
    protected AttributeData getLogAttribute(AttributeData[] pAttributeDataTab) {
        boolean lFound = false;
        int i = 0;
        AttributeData lAttributeData = null;

        while (!lFound && i < pAttributeDataTab.length) {
            lAttributeData = pAttributeDataTab[i];
            if (lAttributeData.getName().compareTo(ATT_NAME) == 0) {
                lFound = true;
            }
            else {
                i++;
            }
        }

        if (!lFound) {
            lAttributeData = null;
        }

        return lAttributeData;
    }
}

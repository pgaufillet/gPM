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
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestGetRevisionIdFromRevisionLabelService
 * 
 * @author mfranche
 */
public class TestGetRevisionIdFromRevisionLabelService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The revisions labels. */
    private static final String REVISION_LABEL = "LABEL_1";

    /**
     * Tests the method in normal conditions
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

        if (lRevisionsCount <= 1) {
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
                    REVISION_LABEL);
        }

        String lRevisionId =
                revisionService.getRevisionIdFromRevisionLabel(adminRoleToken,
                        lSheetId, REVISION_LABEL);

        assertNotNull("The revision id should not be null.", lRevisionId);
    }
}

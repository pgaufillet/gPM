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
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the preCreateRevision extension point
 * 
 * @author mfranche
 */
public class TestPreCreateRevisionExtensionPoint extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used for normal case. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_MOUSE;

    /** The label set on the new revision */
    private static final String REVISION_LABEL = "Label_Revision_1";

    /** The name of the attribute set in the extension point */
    private static final String ATTRIBUTE_NAME = "myAttr";

    /** The value of the attribute set in the extension point */
    private static final String ATTRIBUTE_VALUE = "myAttrValue";

    /**
     * Test the preCreateRevision extension point.
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
        String lRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL);

        // We must verify that the revision
        RevisionData lRevisionData =
                revisionService.getRevisionData(adminRoleToken, lSheetId,
                        lRevisionId);

        assertNotNull("Revision #" + lRevisionId + "is not in DB..",
                lRevisionData);

        AttributeData[] lAttributeDataTab = lRevisionData.getAttributeDatas();
        assertNotNull("Revision #" + lRevisionId
                + " has not been modified by the extension point.",
                lAttributeDataTab);
        assertTrue("Revision #" + lRevisionId
                + " has not been modified by the extension point.",
                lAttributeDataTab.length > 0);
        assertEquals("Revision #" + lRevisionId
                + " has not been modified by the extension point.",
                ATTRIBUTE_NAME, lAttributeDataTab[0].getName());
        assertTrue("Revision #" + lRevisionId
                + " has not been modified by the extension point.",
                lAttributeDataTab[0].getValues().length > 0);
        assertEquals("Revision #" + lRevisionId
                + " has not been modified by the extension point.",
                ATTRIBUTE_VALUE, lAttributeDataTab[0].getValues()[0]);
    }
}

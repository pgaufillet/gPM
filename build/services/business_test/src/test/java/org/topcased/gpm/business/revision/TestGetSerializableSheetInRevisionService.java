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
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.sheet.service.sheetAccess.SheetDataAccess;
import org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData;

/**
 * TestGetSerializableSheetInRevisionService
 * 
 * @author mfranche
 */
public class TestGetSerializableSheetInRevisionService extends
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

    /**
     * Tests the method in normal conditions
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

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        org.topcased.gpm.business.serialization.data.SheetData lSerializableSheetData =
                revisionService.getSerializableSheetInRevision(adminRoleToken,
                        lSheetId, lRevisionId);

        checkFieldValue(lSerializableSheetData);
    }

    /**
     * Check field value
     * 
     * @param pSerializableSheetData
     *            sheet data
     */
    protected void checkFieldValue(
            org.topcased.gpm.business.serialization.data.SheetData pSerializableSheetData) {
        FieldValueData lFoundFieldValueData = null;
        for (FieldValueData lFieldValueData : pSerializableSheetData.getFieldValues()) {
            if (lFieldValueData.getName().compareTo(FIELD_NAME) == 0) {
                lFoundFieldValueData = lFieldValueData;
            }
        }
        assertEquals(FIELD_NAME + " value is incorrect.",
                lFoundFieldValueData.getValue(), OLD_FIELD_VALUE);
    }
}

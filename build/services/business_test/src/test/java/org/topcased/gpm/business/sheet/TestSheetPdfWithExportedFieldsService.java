/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSheetPdfWithExportedFieldsService
 * 
 * @author mfranche
 */
public class TestSheetPdfWithExportedFieldsService extends
        AbstractBusinessServiceTestCase {

    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /**
     * Test the method in a normal case.
     */
    public void testNormalCase() {
        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        try {
            sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIds,
                    SheetExportFormat.PDF, null);
            fail("The exception has not been thrown.");
        }
        catch (MethodNotImplementedException e) {
            // ok
            assertNotNull("The method name attribute is incorrect.",
                    e.getMethodName());
        }
        catch (Throwable e) {
            fail("The exception thrown is not a MethodNotImplementedException.");
        }
    }
}

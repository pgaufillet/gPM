/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Mimoun Mennad(Atos)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>getSheetsByType<CODE> of the Sheet Service.
 * 
 * @author nsamson
 */
public class TestGetSheetsByTypeService extends AbstractBusinessServiceTestCase {

    /** The Id of the first sheet used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The sheet references. */
    private static final String[] SHEET_REFERENCES =
            { "Gros Minet", "Garfield", "Tom", "testSheet", "testLargeString",
             "Cat1", "Cat2", "Cat3", "CatA", "CatB", "Cat001 link with Cat002",
             "Cat002 link with Cat001", "Cat003" };

    /** The Sheet Service. */
    private SheetService sheetService;

    /**
     * Tests the getSheetsByType method.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheets
        List<SheetSummaryData> lSheetList =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);

        assertNotNull(
                "getSheetsByType returns null instead of a list of id of sheets",
                lSheetList);

        int lSize = lSheetList.size();
        int lExpectedSize = SHEET_REFERENCES.length;

        //Check
        assertEquals("getSheetsByType returns a list of " + lSize
                + " sheets instead of a list of " + lExpectedSize + " sheets",
                lExpectedSize, lSize);

        for (SheetSummaryData lSheet : lSheetList) {
            assertTrue("The sheet " + lSheet.getSheetReference()
                    + " is not an expected Sheet.", Arrays.asList(
                    SHEET_REFERENCES).contains(lSheet.getSheetReference()));
        }

    }
}

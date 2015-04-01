/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method the update(final String pRoleToken, final CacheableSheet
 * pSheetData, Context pCtx, boolean pIgnoreVersion) of the Sheet Service. This
 * method takes a boolean to choose if user wants to ignore version or not
 * 
 * @author mmennad
 */
public class TestUpdateSheetWithTransitionParameter extends
        AbstractBusinessServiceTestCase {

    private final static String FUNCTIONAL_REF_VALUES = "Test_Update";

    /**
     * Test the method with correct parameters. It updates an existing sheet
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Get an existing cacheable sheet
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(adminRoleToken,
                        lSheetSummary.get(0).getId(), CacheProperties.MUTABLE);
        //Modify a parameter    
        lCacheableSheet.setFunctionalReference(FUNCTIONAL_REF_VALUES);

        //Updating part
        sheetService.updateSheet(adminRoleToken, lCacheableSheet, "Validate",
                Context.getContext());
        assertEquals(FUNCTIONAL_REF_VALUES,
                lCacheableSheet.getFunctionalReference());

    }

    /**
     * Test the method with a wrong role token
     */
    public void testWrongRoleCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Get an existing cacheable sheet
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(adminRoleToken,
                        lSheetSummary.get(0).getId(), CacheProperties.MUTABLE);
        //Modify a parameter    
        lCacheableSheet.setFunctionalReference(FUNCTIONAL_REF_VALUES);

        try {
            //Updating part
            sheetService.updateSheet(INVALID_CONTAINER_ID, lCacheableSheet,
                    "Validate", Context.getContext());
            fail("There souldn't be a correct role now");
        }
        catch (InvalidTokenException e) {

        }

    }

    /**
     * Test the method with a wrong transition name
     */
    public void testWrongTransitionNameCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Get an existing cacheable sheet
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(adminRoleToken,
                        lSheetSummary.get(0).getId(), CacheProperties.MUTABLE);
        //Modify a parameter    
        lCacheableSheet.setFunctionalReference(FUNCTIONAL_REF_VALUES);

        try {
            //Updating part
            sheetService.updateSheet(adminRoleToken, lCacheableSheet,
                    INVALID_CONTAINER_ID, Context.getContext());
            fail("There souldn't be a correct transition name");
        }
        catch (IllegalStateException e) {

        }

    }

}
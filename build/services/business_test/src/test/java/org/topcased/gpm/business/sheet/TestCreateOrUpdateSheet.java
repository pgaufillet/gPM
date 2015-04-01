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
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method createOrUpdateSheet() of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestCreateOrUpdateSheet extends AbstractBusinessServiceTestCase {

    private final static String FUNCTIONAL_REF_VALUES = "TEST";

    /**
     * Test the method with correct parameters. It updates an existing sheet
     */
    public void testUpdateNormalCase() {
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
        String lNewId =
                sheetService.createOrUpdateSheet(adminRoleToken,
                        lCacheableSheet, null);
        //Call the newly created/updated Sheet
        assertNotNull(lNewId);

        assertEquals(FUNCTIONAL_REF_VALUES,
                lCacheableSheet.getFunctionalReference());

    }

    /**
     * Test the method with correct parameters. It creates an new sheet
     */
    public void testCreateNormalCase() {
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
        lCacheableSheet.setFunctionalReference("TEST");

        //Creating part

        //Clearing the current Identifier
        lCacheableSheet.setId(null);

        String lNewId2 =
                sheetService.createOrUpdateSheet(adminRoleToken,
                        lCacheableSheet, null);
        assertNotNull(lNewId2);
        //Check it didn't just update
        assertNotSame(lNewId2, lSheetSummary.get(0).getId());
    }

}
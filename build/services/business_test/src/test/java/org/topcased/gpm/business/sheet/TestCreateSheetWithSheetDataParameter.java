/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS),  Nicolas Veillet (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method CreateSheetWithSheetData of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestCreateSheetWithSheetDataParameter extends
        AbstractBusinessServiceTestCase {

    /**
     * Tests the method in case of correct parameters were sent
     */
    public void testNormalCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                sheetService.getSerializableSheet(lAdminRoleToken, lSheetId);

        //Creates the Sheet
        String lSheetOriginId =
                sheetService.createSheet(lAdminRoleToken, getProcessName(),
                        lSheetData, Context.getContext());

        assertNotNull(lSheetOriginId);
        //The newly created sheet has the same reference than the sheet it's based on
        assertEquals(lSheetSummary.iterator().next().getSheetReference(),
                sheetService.getSheetRefStringByKey(lAdminRoleToken,
                        lSheetOriginId));

    }
}

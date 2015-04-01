/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos), Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method addSheetHistory() of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestAddSheetHistoryWithTimestampParameter extends
        AbstractBusinessServiceTestCase {
    private static final String ORIGIN_NAME = "Or";

    private static final String DESTINATION_NAME = "Dest";

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        sheetService = serviceLocator.getSheetService();

        // create the timestamp
        Calendar lCalendar = Calendar.getInstance();
        Date lNow = lCalendar.getTime();
        Timestamp lCurrentTimestamp = new java.sql.Timestamp(lNow.getTime());

        // Retrieving the sheet.
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_CAT);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Add a sheet history
        sheetService.addSheetHistory(adminRoleToken, lSheetId,
                getAdminLogin()[0], ORIGIN_NAME, DESTINATION_NAME,
                lCurrentTimestamp, "default transition");

        SheetHistoryData[] lSheetHistoryDataTab =
                sheetService.getSheetHistory(adminRoleToken, lSheetId);

        assertNotNull("The sheet history should not be null.",
                lSheetHistoryDataTab);
        assertEquals("The sheet history length is incorrect.", 1,
                lSheetHistoryDataTab.length);
        SheetHistoryData lSheetHistoryData = lSheetHistoryDataTab[0];
        assertEquals("The login name is incorrect.",
                lSheetHistoryData.getLoginName(), getAdminLogin()[0]);
        assertEquals("The origin state is incorrect.",
                lSheetHistoryData.getOriginState(), ORIGIN_NAME);
        assertEquals("The destination state is incorrect.",
                lSheetHistoryData.getDestinationState(), DESTINATION_NAME);
        assertEquals("the time stamp is incorrect.",
                lSheetHistoryData.getChangeDate(), lCurrentTimestamp);
    }

}
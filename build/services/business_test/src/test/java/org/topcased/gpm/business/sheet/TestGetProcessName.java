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
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * This class tests the getProcessName method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetProcessName extends AbstractBusinessServiceTestCase {
    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        sheetService = serviceLocator.getSheetService();

        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(GpmTestValues.PROCESS_NAME,
                        GpmTestValues.SHEET_TYPE_CAT);

        String lProcessName =
                sheetService.getProcessName(adminUserToken,
                        lSheets.get(0).getId());
        assertNotNull(lProcessName);
        assertEquals(GpmTestValues.PROCESS_NAME, lProcessName);

    }

    /**
     * Test the method with a wrong Id
     */
    public void testWrongIdCase() {

        String lProcessName = null;
        sheetService = serviceLocator.getSheetService();

        try {
            lProcessName =
                    sheetService.getProcessName(adminUserToken,
                            INVALID_CONTAINER_ID);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lProcessName);
        }

    }

}

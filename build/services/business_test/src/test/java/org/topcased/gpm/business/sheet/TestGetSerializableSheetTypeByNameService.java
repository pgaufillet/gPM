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

import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.State;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * TestGetSerializableSheetTypeByNameService
 * 
 * @author mfranche
 */
public class TestGetSerializableSheetTypeByNameService extends
        AbstractBusinessServiceTestCase {

    /** The sheet Service. */
    private SheetService sheetService;

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        SheetType lSheetType =
                sheetService.getSerializableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE);

        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);
        assertEquals("Sheet type Name does not match with " + SHEET_TYPE,
                SHEET_TYPE, lSheetType.getName());
        assertEquals(
                "Sheet type Description does not match with " + SHEET_TYPE,
                SHEET_TYPE, lSheetType.getDescription());

        // Check States
        Collection<String> lStatesName =
                serviceLocator.getLifeCycleService().getAllStateNames(
                        lSheetType.getId());
        assertNotNull("Sheet type " + lSheetType.getName()
                + " must have states.", lSheetType.getStates());
        assertEquals("Sheet type " + lSheetType.getName() + " must have "
                + lStatesName.size() + " states.", lStatesName.size(),
                lSheetType.getStates().size());
        for (State lState : lSheetType.getStates()) {
            assertTrue("Sheet type " + lSheetType.getName() + " must not have "
                    + lState.getName() + " state.",
                    lStatesName.contains(lState.getName()));
        }
    }
}

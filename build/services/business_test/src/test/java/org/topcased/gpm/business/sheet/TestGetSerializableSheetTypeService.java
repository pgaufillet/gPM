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
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.State;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetSerializableSheetTypeService
 * 
 * @author mfranche
 */
public class TestGetSerializableSheetTypeService extends
        AbstractBusinessServiceTestCase {

    /** The sheet Service. */
    private SheetService sheetService;

    /**
     * Tests the retrieving of the sheet type.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<CacheableSheetType> lSheetTypeDataList =
                sheetService.getSheetTypes(adminRoleToken, getProcessName(),
                        CacheProperties.IMMUTABLE);
        assertNotNull(
                "getSheetTypes returns null instead of a list of id of sheet types",
                lSheetTypeDataList);
        assertFalse("getSheetTypes returns no sheetType",
                lSheetTypeDataList.isEmpty());
        String lId = lSheetTypeDataList.get(0).getId();

        // Retrieving the sheet type
        SheetType lSheetType = sheetService.getSerializableSheetType(lId);
        assertNotNull("Sheet type #" + lId + " does not exist in DB",
                lSheetType);

        assertEquals("Sheet type does not match with " + lId, lId,
                lSheetType.getId());

        // Check States
        Collection<String> lStatesName =
                serviceLocator.getLifeCycleService().getAllStateNames(lId);
        assertNotNull("Sheet type #" + lId + " must have states.",
                lSheetType.getStates());
        assertEquals("Sheet type #" + lId + " must have " + lStatesName.size()
                + " states.", lStatesName.size(), lSheetType.getStates().size());
        for (State lState : lSheetType.getStates()) {
            assertTrue("Sheet type #" + lId + " must not have "
                    + lState.getName() + " state.",
                    lStatesName.contains(lState.getName()));
        }
    }
}
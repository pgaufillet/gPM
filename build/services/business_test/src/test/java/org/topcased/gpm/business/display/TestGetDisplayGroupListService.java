/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Anne Haugommard
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getDisplayGroupList<CODE> of the Display Service.
 * 
 * @author nsamson
 */
public class TestGetDisplayGroupListService extends
        AbstractBusinessServiceTestCase {

    /** The DisplayGroup Service. */
    private DisplayService displayService;

    /** the sheet type. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** the bad sheet type id. */
    private static final String BAD_SHEET_TYPE_ID = "bad type id";

    /** the expected group. */
    private static final String[] GROUP_LABEL = { "Identity", "Description" };

    /**
     * Tests the method in a normal way.
     */
    public void testNormalCase() {
        // Gets the display service.
        displayService = serviceLocator.getDisplayService();

        // Gets the sheet type
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);

        // Retrieving the display group
        List<DisplayGroupData> lDisplayGroups =
                displayService.getDisplayGroupList(adminUserToken,
                        lSheetType.getId());

        assertNotNull("Method getEnvironmentNames returns null.",
                lDisplayGroups);
        int lSize = lDisplayGroups.size();
        int lExpectedSize = GROUP_LABEL.length;
        assertEquals(
                "The method getDisplayGroupList does not return the number of group expected.",
                lExpectedSize, lSize);

        for (DisplayGroupData lDGD : lDisplayGroups) {
            assertTrue("The group " + lDGD.getLabelKey()
                    + " is not an expected group.",
                    Arrays.asList(GROUP_LABEL).contains(lDGD.getLabelKey()));
        }

    }

    /**
     * Tests the createEnvironment method with a bad type Id.
     */
    public void testBadSheetTypeIdCase() {
        // Gets the display service.
        displayService = serviceLocator.getDisplayService();

        // Retrieving the display group

        try {
            displayService.getDisplayGroupList(adminUserToken,
                    BAD_SHEET_TYPE_ID);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }

    }

}

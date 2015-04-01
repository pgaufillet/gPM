/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * TestGetProcessStateNameService
 * 
 * @author ahaugomm
 */
public class TestGetProcessStateNameService extends
        AbstractBusinessServiceTestCase {

    /** The lifeCycle service */
    private LifeCycleService lifeCycleService;

    /** The sheet service */
    private SheetService sheetService;

    /** The expected state name */
    private static final String STATE_NAME = "Temporary";

    /**
     * Test the method in a normal way
     */
    public void testNormalCase() {
        lifeCycleService = serviceLocator.getLifeCycleService();

        sheetService = serviceLocator.getSheetService();

        final String lSheetId =
                sheetService.getSheetIdByReference("PET STORE", "Happy Mouse", "Tom");

        assertNotNull("The sheet data has a null id", lSheetId);
        String lStateName = lifeCycleService.getProcessStateName(lSheetId);

        assertEquals("The final state ( '" + lStateName
                + "' ) does not match the expected one : '" + STATE_NAME
                + "' .", lStateName, STATE_NAME);
    }

    /**
     * When the given ID is null
     */
    public void testWithNullId() {
        lifeCycleService = serviceLocator.getLifeCycleService();

        try {
            lifeCycleService.getProcessStateName(null);
            fail("No exception has been thrown with null ID.");
        }
        catch (IllegalArgumentException e) {
        }

    }
}

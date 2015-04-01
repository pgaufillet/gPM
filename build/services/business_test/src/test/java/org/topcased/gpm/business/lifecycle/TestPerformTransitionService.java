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
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * TestPerformTransitionService
 * 
 * @author ahaugomm
 */
public class TestPerformTransitionService extends
        AbstractBusinessServiceTestCase {

    /** The lifeCycle service */
    private LifeCycleService lifeCycleService;

    /** The sheet service */
    private SheetService sheetService;

    /** the transition name */
    private static final String TRANSITION_NAME = "Validate";

    /** an invalid transition name */
    private static final String INVALID_TRANSITION_NAME = "Transition";

    /** the expected state name */
    private static final String STATE_NAME = "Open";

    /**
     * Test the method in a normal way
     */
    public void testNormalCase() {
        lifeCycleService = serviceLocator.getLifeCycleService();
        sheetService = serviceLocator.getSheetService();

        // Get a sheet data
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Happy Mouse", "Tom");

        assertNotNull("The sheet data has a null ID", lSheetId);

        lifeCycleService.performTransition(adminRoleToken, lSheetId,
                TRANSITION_NAME);

        String lStateName = lifeCycleService.getProcessStateName(lSheetId);
        assertEquals("The final state ( '" + lStateName
                + "' ) does not match the expected one : '" + STATE_NAME
                + "' .", lStateName, STATE_NAME);

    }

    /**
     * Test with a null role token.
     */
    public void testWithNullRoleToken() {

        lifeCycleService = serviceLocator.getLifeCycleService();

        sheetService = serviceLocator.getSheetService();

        // Get a sheet data
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Happy Mouse", "Tom");
        assertNotNull("The sheet data has a null ID", lSheetId);

        /*
         * An InvalidTokenException should be thrown
         */
        try {
            lifeCycleService.performTransition(null, lSheetId, TRANSITION_NAME);
            fail("No exception has been thrown.");
        }
        catch (InvalidTokenException e) {
        }
    }

    /**
     * Test with a null role token.
     */
    public void testWithNullSheetId() {

        lifeCycleService = serviceLocator.getLifeCycleService();

        /*
         * An IllegalArgumentException should be thrown.
         */

        try {
            lifeCycleService.performTransition(adminRoleToken, null,
                    TRANSITION_NAME);
            fail("No exception has been thrown.");
        }
        catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test with an invalid transition name.
     */
    public void testWithInvalidTransitionName() {
        lifeCycleService = serviceLocator.getLifeCycleService();
        sheetService = serviceLocator.getSheetService();

        // Get a sheet data
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Happy Mouse", "Tom");

        assertNotNull("The sheet data has a null ID", lSheetId);

        try {
            lifeCycleService.performTransition(adminRoleToken, lSheetId,
                    INVALID_TRANSITION_NAME);
            fail("No exception has been thrown.");
        }
        catch (IllegalStateException e) {

        }
    }

}

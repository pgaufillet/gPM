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

import java.util.Arrays;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * TestGetProcessInstanceInformationService
 * 
 * @author ahaugomm
 */
public class TestGetProcessInstanceInformationService extends
        AbstractBusinessServiceTestCase {

    /** The lifeCycle service */
    private LifeCycleService lifeCycleService;

    /** The sheet service */
    private SheetService sheetService;

    private static final String EXPECTED_STATE = "Temporary";

    private static final String[] EXPECTED_TRANSITIONS =
            { "Delete", "Validate" };

    /**
     * Test the method in normal way.
     */
    public void testNormalCase() {
        lifeCycleService = serviceLocator.getLifeCycleService();

        sheetService = serviceLocator.getSheetService();

        final String lId =
            sheetService.getSheetIdByReference("PET STORE", "Happy Mouse", "Tom");

        ProcessInformation lProcessInformation =
                lifeCycleService.getProcessInstanceInformation(adminRoleToken,
                        lId);

        // Check current state
        String lState = lProcessInformation.getCurrentState();
        assertEquals("Current state ('" + lState
                + "') does not match with expected one ('" + EXPECTED_STATE
                + "')", lState, EXPECTED_STATE);

        // check available transitions
        String[] lTransitions = lProcessInformation.getTransitions();
        for (String lTransition : EXPECTED_TRANSITIONS) {
            assertTrue("Transition '" + lTransition + "' is not found.",
                    Arrays.asList(lTransitions).contains(lTransition));
        }
    }
}

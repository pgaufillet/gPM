/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * Tests the method <CODE>changeState<CODE> of the Sheet Service.
 * 
 * @author nsamson
 */
public class TestChangeStateService extends AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The process transition. */
    private static final String PROCESS_TRANSITION = "Delete";

    /**
     * Tests the changeState method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Uses a empty token.

        try {
            sheetService.changeState("", "", PROCESS_TRANSITION, null);
            fail("The exception has not been thrown.");
        }
        catch (InvalidTokenException e) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a InvalidTokenException.");
        }
    }
}
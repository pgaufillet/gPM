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
import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * Tests the method <CODE>deleteSheetLinkType</CODE> of the Sheet Service.
 * 
 * @author sidjelli
 */
public class TestDeleteSheetLinkTypeService extends
        AbstractBusinessServiceTestCase {

    /** An invalid sheet link type. */
    private static final String INVALID_SHEET_LINK_TYPE = "";

    /**
     * Tests the deleteSheetLinkType method with an incorrect sheetLinkType
     */
    public void testWithIncorrectLinkType() {
        try {
            linkService.deleteLinkType(adminRoleToken, getProcessName(),
                    INVALID_SHEET_LINK_TYPE, true);
            fail("The exception has not been thrown.");
        }
        catch (InvalidNameException lInvalidNameException) {
            assertEquals("The attribute invalidName is incorrect.",
                    lInvalidNameException.getInvalidName(),
                    INVALID_SHEET_LINK_TYPE);
        }
        catch (Throwable e) {

            fail("The exception thrown is not an InvalidNameException.");
        }
    }
}
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

import static org.topcased.gpm.business.GpmTestValues.INVALID_SHEET_REF;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;

/**
 * TestGetSerializableSheetByRefCase
 * 
 * @author mfranche
 */
public class TestGetSerializableSheetByRefService extends
        AbstractBusinessServiceTestCase {

    /**
     * Tests the method with an invalid sheet ref
     */
    public void testInvalidSheetRefCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        org.topcased.gpm.business.serialization.data.SheetData lSerializableSheetData =
                sheetService.getSerializableSheetByRef(adminRoleToken,
                        getProcessName(), getProductName(), INVALID_SHEET_REF);

        assertNull("The returned serializable sheet data should be null.",
                lSerializableSheetData);
    }
}

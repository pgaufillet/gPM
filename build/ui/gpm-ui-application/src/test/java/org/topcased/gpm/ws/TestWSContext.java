/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.ws.v2.client.Context;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.SheetData;

/**
 * TestWSContext
 * 
 * @author ogehin
 */
public class TestWSContext extends AbstractWSTestCase {
    /**
     * Test of WS methods using an execution context.
     */
    public void testSkipExtensionPoints() {
        try {
            SheetData lSheet =
                    staticServices.getSheetByRef(roleToken,
                            DEFAULT_PROCESS_NAME, GpmTestValues.SHEET1_PRODUCT,
                            GpmTestValues.SHEET1_REF);

            Context lCtx = new Context();

            lCtx.put("gpm.SkipExtensionPoints", true);

            staticServices.createSheet(roleToken, DEFAULT_PROCESS_NAME,
                    DEFAULT_PRODUCT_NAME, lSheet, lCtx);
        }
        catch (GDMException_Exception e) {
            fail("Unexpected exception: " + e.getClass().getName() + ":"
                    + e.getMessage());
        }
    }
}
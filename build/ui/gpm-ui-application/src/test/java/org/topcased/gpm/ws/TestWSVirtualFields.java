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

import java.util.Arrays;
import java.util.List;

/**
 * TestWSVirtualFields
 * 
 * @author jeballar
 */
public class TestWSVirtualFields extends AbstractWSTestCase {

    /**
     * Test of WS methods using an execution context.
     */
    public void testGetAllVirtualFields() {
        List<String> lExpectedVirtualFieldTypes =
                Arrays.asList("$PRODUCT_NAME", "$PRODUCT_DESCRIPTION",
                        "$PRODUCT_HIERARCHY", "$SHEET_STATE", "$SHEET_TYPE",
                        "$SHEET_REFERENCE", "$PRODUCT_FIELD", "$LINK_FIELD",
                        "$LINKED_SHEET_FIELD", "$ORIGIN_SHEET_REF",
                        "$ORIGIN_PRODUCT", "$DEST_SHEET_REF", "$DEST_PRODUCT",
                        "$NOT_SPECIFIED", "$CURRENT_PRODUCT",
                        "$CURRENT_USER_LOGIN", "$CURRENT_USER_NAME");
        List<String> lTypes = staticServices.getAllVirtualFieldTypes();

        assertEquals("Check that virtual field types are as expected",
                lExpectedVirtualFieldTypes, lTypes);
    }
}
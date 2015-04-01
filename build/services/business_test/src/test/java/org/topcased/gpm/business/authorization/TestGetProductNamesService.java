/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;

/**
 * Tests the method <CODE>getProductNames<CODE> of the Authorization
 * Service.
 * 
 * @author nsamson
 */
public class TestGetProductNamesService extends AbstractBusinessServiceTestCase {

    /** The products names. */
    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
             GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME };

    /**
     * Tests the getProductNames method.
     */
    public void testNormalCase() {
        // Retrieving the products names
        Collection<String> lProductNames =
                authorizationService.getProductNames(adminUserToken,
                        getProcessName());

        assertNotNull("The method getProductNames returns null.", lProductNames);

        int lSize = lProductNames.size();
        int lExpectedSize = PRODUCT_NAMES.length;
        assertTrue("The method getProductNames returns less than "
                + lExpectedSize + " results.", lExpectedSize <= lSize);
        assertTrue(
                "The method getProductNames does not return the expected product names.",
                lProductNames.containsAll(Arrays.asList(PRODUCT_NAMES)));
    }
}
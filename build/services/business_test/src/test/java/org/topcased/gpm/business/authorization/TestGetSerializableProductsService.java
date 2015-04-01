/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.Product;

/**
 * TestGetSerializableProductsService
 * 
 * @author mfranche
 */
public class TestGetSerializableProductsService extends
        AbstractBusinessServiceTestCase {

    /** The products names. */
    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
             GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME };

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        // Retrieving the products
        Collection<Product> lProducts =
                authorizationService.getSerializableProducts(adminUserToken,
                        getProcessName());

        assertNotNull("The method getProductNames returns null.", lProducts);

        int lSize = lProducts.size();
        int lExpectedSize = PRODUCT_NAMES.length;
        assertTrue("The method getProductNames returns less than "
                + lExpectedSize + " results.", lExpectedSize <= lSize);

        Collection<String> lProductNames = new ArrayList<String>();
        for (Product lProduct : lProducts) {
            lProductNames.add(lProduct.getName());
        }
        assertTrue(
                "The method getProductNames does not return the expected product names.",
                lProductNames.containsAll(Arrays.asList(PRODUCT_NAMES)));
    }
}

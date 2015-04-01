/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.serialization.data.Product;

/**
 * TestGetSerializableProductService
 * 
 * @author mfranche
 */
public class TestGetSerializableProductService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The Product Name. */
    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_BERNARD_STORE_NAME;

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        String lProductId =
                productService.getProductId(adminRoleToken, PRODUCT_NAME);

        Product lSerializableProduct =
                productService.getSerializableProduct(adminRoleToken,
                        lProductId);

        assertNotNull("Product #" + lProductId + " is not in DB.",
                lSerializableProduct);

    }
}

/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * TestProductExistence Test
 * {@link ProductService#isProductExists(String, String)}
 * 
 * @author mkargbo
 */
public class TestProductExistence extends AbstractBusinessServiceTestCase {

    private static final String EXISTING_PRODUCT = GpmTestValues.PRODUCT1_NAME;

    private static final String NOT_EXISTING_PRODUCT = "notaproduct";

    private ProductService productService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        productService = serviceLocator.getProductService();
    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.product.impl.ProductServiceImpl #isProductExists(java.lang.String, java.lang.String)}
     * . Existing
     */
    public void testIsProductExists() {
        assertTrue(productService.isProductExists(adminRoleToken,
                EXISTING_PRODUCT));
    }

    /**
     * * Test method for
     * {@link org.topcased.gpm.business.product.impl.ProductServiceImpl #isProductExists(java.lang.String, java.lang.String)}
     * . Not existing
     */
    public void testNotExists() {
        assertFalse(productService.isProductExists(adminRoleToken,
                NOT_EXISTING_PRODUCT));
    }

}

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
import org.topcased.gpm.business.product.service.ProductService;

/**
 * TestGetSheetCountService
 * 
 * @author mkargbo
 */
public class TestGetSheetCountService extends AbstractBusinessServiceTestCase {

    private static final String INSTANCE_FILE =
            "product/TestGetSheetCountService.xml";

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

    private static final String PRODUCT_WITH_SHEET_NAME = "gPM";

    private static final Integer EXPECTED_SHEET_COUNT = new Integer(2);

    /**
     * Test method for
     * {@link org.topcased.gpm.business.product.impl. ProductServiceImpl#getSheetCount(java.lang.String, java.lang.String)}
     * .
     */
    public void testGetSheetCount() {
        instantiate(getProcessName(), INSTANCE_FILE);
        String lProductId =
                productService.getProductId(adminRoleToken,
                        PRODUCT_WITH_SHEET_NAME);
        Integer lCount =
                productService.getSheetCount(adminRoleToken, lProductId);
        assertEquals(EXPECTED_SHEET_COUNT, lCount);
    }

    private static final String PRODUCT_WITH_NO_SHEET_NAME = "Virgile";

    private static final Integer EXPECTED_NO_SHEET = new Integer(0);

    /**
     * Test method for
     * {@link org.topcased.gpm.business.product.impl. ProductServiceImpl#getSheetCount(java.lang.String, java.lang.String)}
     * . No Sheets
     */
    public void testNoSheets() {
        instantiate(getProcessName(), INSTANCE_FILE);
        String lProductId =
                productService.getProductId(adminRoleToken,
                        PRODUCT_WITH_NO_SHEET_NAME);
        Integer lCount =
                productService.getSheetCount(adminRoleToken, lProductId);
        assertEquals(EXPECTED_NO_SHEET, lCount);
    }

}
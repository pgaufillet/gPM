/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the isProductUpdatable(String pRoleToken, String
 * pProductName) method from the ProductService implementation.
 * 
 * @author mmennad
 */
public class TestIsProductUpdatableService extends
        AbstractBusinessServiceTestCase {

    /**
     * This tests the method with correct parameters
     */
    public void testNormalCase() {

        ProductService lProductService = serviceLocator.getProductService();

        boolean lIsUpdatable =
                lProductService.isProductExists(adminRoleToken,
                        GpmTestValues.PRODUCT1_NAME);
        assertTrue(lIsUpdatable);
    }

}

/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around products.
 * 
 * @author nsamson
 */
public class ProductTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetProductTypesService.class);
        lSuite.addTestSuite(TestGetProductTypeByNameService.class);
        lSuite.addTestSuite(TestGetProductsByTypeService.class);
        lSuite.addTestSuite(TestCreateProductTypeService.class);
        lSuite.addTestSuite(TestGetProductService.class);
        lSuite.addTestSuite(TestGetProductTypeByProductKeyService.class);
        lSuite.addTestSuite(TestGetProductIdService.class);
        lSuite.addTestSuite(TestSetProductEnvironmentService.class);
        lSuite.addTestSuite(TestGetProductHierarchy.class);
        lSuite.addTestSuite(TestUpdateProductParentsService.class);
        lSuite.addTestSuite(TestProductExistence.class);

        lSuite.addTestSuite(TestGetSerializableProductService.class);
        lSuite.addTestSuite(TestGetSerializableProductTypeService.class);

        lSuite.addTestSuite(TestGetProductsService.class);
        // cf. bug #225
        // lSuite.addTestSuite(TestDeleteProductTypeService.class);

        lSuite.addTestSuite(TestDeleteProductService.class);
        lSuite.addTestSuite(TestGetSheetCountService.class);

        lSuite.addTestSuite(TestProductExtensionPoint.class);
        lSuite.addTestSuite(TestProductHierarchyValidation.class);

        return lSuite;
    }
}

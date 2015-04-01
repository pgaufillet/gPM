/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.product;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ProductFacadeTestSuite
 * 
 * @author nveillet
 */
public class ProductFacadeTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("ProductFacade test suite");
        lSuite.addTestSuite(TestGetProductByTypeFacade.class);
        lSuite.addTestSuite(TestCreateProductFacade.class);
        lSuite.addTestSuite(TestGetProductFacade.class);
        lSuite.addTestSuite(TestUpdateProductFacade.class);
        lSuite.addTestSuite(TestDeleteProductFacade.class);
        lSuite.addTestSuite(TestCountSheetsFacade.class);
        lSuite.addTestSuite(TestGetSubProductsFacade.class);
        lSuite.addTestSuite(TestGetCreatableProductTypesFacade.class);
        lSuite.addTestSuite(TestGetProductNameFacade.class);
        return lSuite;
    }
}

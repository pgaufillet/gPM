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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around prodcuts.
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
        lSuite.addTestSuite(TestGetBusinessProcessNamesService.class);
        lSuite.addTestSuite(TestGetProductNamesService.class);
        lSuite.addTestSuite(TestGetProductsAsTreeService.class);

        lSuite.addTestSuite(TestGetSerializableProductsService.class);

        return lSuite;
    }

}

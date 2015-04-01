/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.attribute;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AuthorizationFacadeTestSuite
 * 
 * @author nveillet
 */
public class AttributeFacadeTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("AttributeFacade test suite");
        lSuite.addTestSuite(TestGetAttributesFacade.class);
        lSuite.addTestSuite(TestGetAttributeFacade.class);
        lSuite.addTestSuite(TestGetHomepageUrlFacade.class);
        lSuite.addTestSuite(TestSetAttributesFacade.class);
        return lSuite;
    }
}

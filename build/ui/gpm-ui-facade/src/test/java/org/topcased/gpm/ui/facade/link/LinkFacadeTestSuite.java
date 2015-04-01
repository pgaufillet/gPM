/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.link;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * LinkFacadeTestSuite
 * 
 * @author jlouisy
 */
public class LinkFacadeTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("LinkFacade test suite");
        lSuite.addTestSuite(TestCreateLinkFacade.class);
        lSuite.addTestSuite(TestDeleteLinkFacade.class);
        lSuite.addTestSuite(TestUpdateLinkFacade.class);
        lSuite.addTestSuite(TestGetLinkGroupsFacade.class);
        lSuite.addTestSuite(TestGetLinksFacade.class);
        lSuite.addTestSuite(TestGetCreatableLinkTypesFacade.class);
        lSuite.addTestSuite(TestGetDeletableLinkTypesFacade.class);
        return lSuite;
    }
}

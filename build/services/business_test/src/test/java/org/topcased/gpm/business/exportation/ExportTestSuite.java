/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Exportation service tests
 * 
 * @author mkargbo
 */
public class ExportTestSuite extends TestSuite{

    /**
     * Test
     * 
     * @return Tests to run
     */
    public static Test suite() {
        TestSuite lSuite =
                new TestSuite("Test for org.topcased.gpm.business.exportation");
        //$JUnit-BEGIN$
        lSuite.addTestSuite(TestExportationService.class);
        lSuite.addTestSuite(TestProductExport.class);
        lSuite.addTestSuite(TestProductLinkExport.class);
        lSuite.addTestSuite(TestSheetExport.class);
        lSuite.addTestSuite(TestSheetLinkExport.class);
        lSuite.addTestSuite(TestFilterExport.class);
        lSuite.addTestSuite(TestUserExport.class);
        lSuite.addTestSuite(TestCategoryExport.class);
        lSuite.addTestSuite(TestEnvironmentExport.class);
        //$JUnit-END$
        return lSuite;
    }
}
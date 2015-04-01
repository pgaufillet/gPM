/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.report;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ReportTestSuite
 * 
 * @author ahaugomm
 */
public class ReportTestSuite extends TestSuite {

    /**
     * the test suite.
     * 
     * @return the suite
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestGenerateReportService.class);
        lSuite.addTestSuite(TestGetCompatibleModelsService.class);
        lSuite.addTestSuite(TestCreateReportModelService.class);
        lSuite.addTestSuite(TestUpdateReportModelService.class);
        lSuite.addTestSuite(TestDeleteReportModelService.class);
        lSuite.addTestSuite(TestGetReportModelService.class);

        return lSuite;
    }

}

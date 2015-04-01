/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.exportimport;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ExportImportFacadeTestSuite
 * 
 * @author jlouisy
 */
public class ExportImportFacadeTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("ExportImportFacade test suite");
        lSuite.addTestSuite(TestExportFilterResultFacade.class);
        lSuite.addTestSuite(TestExportSheetsFacade.class);
        lSuite.addTestSuite(TestGetAvailableReportModelsFacade.class);
        lSuite.addTestSuite(TestGetExportableFieldsFacade.class);
        lSuite.addTestSuite(TestImportProductFacade.class);
        lSuite.addTestSuite(TestGetAttachedFileFacade.class);
        lSuite.addTestSuite(TestExportFileFacade.class);
        return lSuite;
    }
}

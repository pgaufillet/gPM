/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.topcased.gpm.business.importation.dictionary.TestCategoryImport;
import org.topcased.gpm.business.importation.dictionary.TestEnvironmentImport;
import org.topcased.gpm.business.importation.product.TestProductImport;
import org.topcased.gpm.business.importation.product.link.TestProductLinkImport;
import org.topcased.gpm.business.importation.sheet.TestSheetImport;
import org.topcased.gpm.business.importation.sheet.link.TestSheetLinkImport;

/**
 * Importation service test suite
 * 
 * @author mkargbo
 */
public class ImportTestSuite {

    /**
     * Tests
     * 
     * @return Tests to run
     */
    public static Test suite() {
        TestSuite lSuite =
                new TestSuite("Test for org.topcased.gpm.business.importation");
        //$JUnit-BEGIN$
        lSuite.addTestSuite(TestSheetImport.class);
        lSuite.addTestSuite(TestSheetLinkImport.class);
        lSuite.addTestSuite(TestProductImport.class);
        lSuite.addTestSuite(TestProductLinkImport.class);
        lSuite.addTestSuite(TestFilterImport.class);
        lSuite.addTestSuite(TestCategoryImport.class);
        lSuite.addTestSuite(TestEnvironmentImport.class);
        //$JUnit-END$
        return lSuite;
    }

}

/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around revisions.
 * 
 * @author mfranche
 */
public class RevisionTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestCreateRevisionService.class);
        lSuite.addTestSuite(TestGetRevisionDataService.class);
        lSuite.addTestSuite(TestGetRevisionDataByLabelService.class);
        lSuite.addTestSuite(TestGetRevisionsSummaryService.class);
        lSuite.addTestSuite(TestGetDifferencesBetweenRevisionsService.class);
        lSuite.addTestSuite(TestGetCacheableSheetInRevisionService.class);
        lSuite.addTestSuite(TestUpdateContainerFromRevisionService.class);
        lSuite.addTestSuite(TestGetRevisionFromInstantiationFile.class);
        lSuite.addTestSuite(TestDeleteRevisionService.class);
        lSuite.addTestSuite(TestGetRevisionIdFromRevisionLabelService.class);
        lSuite.addTestSuite(TestGetSerializableSheetInRevisionService.class);
        lSuite.addTestSuite(TestGetRevisionLabelsService.class);
        return lSuite;
    }
}

/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests;

import java.util.List;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestMtGetSheetRefByKey
 * 
 * @author srene
 */
public class TestMtGetSheetRefByKey extends MultithreadedTestBase {

    private class TestGetSheetRefByKeyService extends ThreadedTest {

        /** The sheet type used. */
        private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

        /** The Sheet Service. */
        private SheetService sheetServiceLocal;

        TestGetSheetRefByKeyService(String pId) {
            super(pId);
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void runTest() {
            try {
                // Gets the sheet service.
                sheetServiceLocal = serviceLocatorLocal.getSheetService();

                // Gets a Id
                List<SheetSummaryData> lSheetSummary =
                        sheetService.getSheetsByType(getProcessName(),
                                SHEET_TYPE);
                assertNotNull(
                        "getSheets returns null instead of a list of id of sheets",
                        lSheetSummary);
                assertFalse("getSheets returns no sheet",
                        lSheetSummary.isEmpty());
                String lId = lSheetSummary.get(0).getId();

                // Retrieving the sheet reference
                LineFieldData lRef =
                        sheetServiceLocal.getSheetRefByKey(adminRoleToken, lId);

                assertNotNull("Sheet #" + lId + " does not exist in DB", lRef);
            }
            catch (Exception lExl) {
                setTestFailed(threadId, lExl);
            }
        }

    }

    /**
     * testHibernateSession
     * 
     * @throws Exception
     */
    public void testHibernateSession() {
        ThreadedTest lTest1 = new TestGetSheetRefByKeyService("thread1");
        ThreadedTest lTest2 = new TestGetSheetRefByKeyService("thread2");
        ThreadedTest lTest3 = new TestGetSheetRefByKeyService("thread3");

        runThreadedTests(new ThreadedTest[] { lTest1, lTest2, lTest3 });

        // Check for a possible failed test.
        if (isFailed()) {
            logExceptions();
        }
    }
}

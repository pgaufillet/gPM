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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetReportModelService
 * 
 * @author ahaugomm
 */
public class TestGetReportModelService extends AbstractBusinessServiceTestCase {

    /** The reporting service */
    private ReportingService reportingService;

    private static final String[] SHEET_TYPE_NAMES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_DOG };

    private static final String REPORT_NAME = "myReport";

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {

        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // create a report model
        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData =
                new ReportModelData(REPORT_NAME, "my first report4",
                        "report/generateReport/myReport.jasper",
                        lFieldsContainerIds, lExportTypes, null);
        reportingService.createReportModel(adminRoleToken, lReportModelData);

        // Get the reportModel

        ReportModelData lModelData2 =
                reportingService.getReportModel(REPORT_NAME, getProcessName());
        assertNotNull("The report model found is null.", lModelData2);
        assertNotNull("The report model found has a null ID.",
                lModelData2.getId());
        assertTrue("The report model found does not have the correct ID.",
                lModelData2.getId().equals(lReportModelData.getId()));

    }
}

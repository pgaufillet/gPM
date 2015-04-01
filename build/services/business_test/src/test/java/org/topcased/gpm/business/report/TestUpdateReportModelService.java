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

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.domain.export.ExportTypeEnum;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestUpdateReportModelService
 * 
 * @author ahaugomm
 */
public class TestUpdateReportModelService extends
        AbstractBusinessServiceTestCase {

    /** The reporting service */
    private ReportingService reportingService;

    /** The sheet service */
    private SheetService sheetService;

    private static final String[] SHEET_TYPE_NAMES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_DOG };

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        // get services
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // Create the test environment :
        /*
         * ReportModelData : - name = "myReport" - description = "my first
         * report" - path = "report/generateReport/" - fields containers : Dog,
         * Cat, Mouse - export types : PDF, XLS
         */
        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData =
                new ReportModelData("myReport", "my first report5",
                        "report/generateReport/myReport.jasper",
                        lFieldsContainerIds, lExportTypes, null);

        // Performing the creation in DB ...
        reportingService.createReportModel(adminRoleToken, lReportModelData);

        // Modify the report model :
        /*
         * export types = XLS
         */
        String[] lNewExportTypes = { "XLS" };
        lReportModelData.setExportTypes(lNewExportTypes);

        // Perform the update.
        reportingService.updateReportModel(adminRoleToken, lReportModelData);

        // Find the report models for type PDF
        // The model should not be found.
        List<ReportModelData> lReportModelDatas =
                reportingService.getCompatibleModels(adminRoleToken,
                        lFieldsContainerIds,
                        reportingService.getExportType(ExportTypeEnum.PDF));

        if (lReportModelDatas != null && lReportModelDatas.size() > 0) {

            boolean lEqual = false;
            for (ReportModelData lReport : lReportModelDatas) {
                lEqual =
                        lEqual
                                || lReport.getId().equals(
                                        lReportModelData.getId());
            }
            assertFalse("Newly created model not found.", lEqual);
        }

        // Find the report models for type XLS
        // The model should be found.

        List<ReportModelData> lReportModelDatas_XLS =
                reportingService.getCompatibleModels(adminRoleToken,
                        lFieldsContainerIds,
                        reportingService.getExportType(ExportTypeEnum.XLS));

        assertNotNull("No compatibles models found.", lReportModelDatas_XLS);
        assertTrue("No compatibles models found.",
                lReportModelDatas_XLS.size() >= 1);

        boolean lEqual = false;
        for (ReportModelData lReport : lReportModelDatas_XLS) {
            lEqual = lEqual || lReport.getId().equals(lReportModelData.getId());
        }
        assertTrue("Newly created model not found.", lEqual);

    }

}

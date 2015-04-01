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
 * TestDeleteReportModelService
 * 
 * @author ahaugomm
 */
public class TestDeleteReportModelService extends
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
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData =
                new ReportModelData("myReport", "my first report2",
                        "report/generateReport/myReport.jasper",
                        lFieldsContainerIds, lExportTypes, null);

        reportingService.createReportModel(adminRoleToken, lReportModelData);

        reportingService.deleteReportModel(adminRoleToken, lReportModelData);

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
            assertFalse("Deleted model has been found.", lEqual);
        }

    }
}

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
import org.topcased.gpm.domain.export.ExportType;
import org.topcased.gpm.domain.export.ExportTypeEnum;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetCompatibleModels
 * 
 * @author ahaugomm
 */
public class TestGetCompatibleModelsService extends
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

    public void testNormalCases() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        String lBug1 =
                sheetService.getSheetsByType(getProcessName(),
                        SHEET_TYPE_NAMES[0]).get(0).getId();
        String lAnalyse1 =
                sheetService.getSheetsByType(getProcessName(),
                        SHEET_TYPE_NAMES[1]).get(0).getId();

        String lBugType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_NAMES[0],
                        CacheProperties.IMMUTABLE).getId();
        String lAnalyseType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_NAMES[1],
                        CacheProperties.IMMUTABLE).getId();

        // Create report models in DB for this test.

        // Report model on 1 bug
        String[] lContainersIds1 = { lBugType };
        String[] lExportTypes1 =
                { ExportTypeEnum.PDF.getValue(), ExportTypeEnum.XLS.getValue() };

        ReportModelData lReportModelDataOneBug =
                new ReportModelData("un_bug", "rapport décrivant un bug.",
                        "report/bugs/a_bug/un_bug.jasper", lContainersIds1,
                        lExportTypes1, null);
        reportingService.createReportModel(adminRoleToken,
                lReportModelDataOneBug);

        // Report model on a list of bugs
        String[] lContainersIds2 = { lBugType };
        String[] lExportTypes2 = { ExportTypeEnum.PDF.getValue() };

        ReportModelData lReportModelDataBugs =
                new ReportModelData("bugs", "rapport décrivant des bugs.",
                        "report/bugs/all_bugs/bugs.jasper", lContainersIds2,
                        lExportTypes2, null);
        reportingService.createReportModel(adminRoleToken, lReportModelDataBugs);

        // Report model on a list of sheets
        String[] lContainersIds3 = { lBugType, lAnalyseType };
        String[] lExportTypes3 = { ExportTypeEnum.PDF.getValue() };

        ReportModelData lReportModelDataSheets =
                new ReportModelData("bugs_all",
                        "rapport décrivant des bugs et des analyses.",
                        "report/bugs/all_sheets/bugs.jasper", lContainersIds3,
                        lExportTypes3, null);
        reportingService.createReportModel(adminRoleToken,
                lReportModelDataSheets);

        // main test
        /*
         * CONTEXT Models created : 
         *     NAME        |  Containers  | ExportTypes
         *  ---------------|--------------|------------ 
         * 1 one_bug       |      Bug     | PDF, XLS 
         * 2   bugs        |      Bug     |    PDF 
         * 3   bugs        | Bug, Analyse |    PDF
         *          +++ Already in database : +++
         * 4  AllBugsInPDF |      Bug     |     PDF
         * 5   AllSheets   | Bug, Analyse |  PDF, XLS
         */

        //All models for 'bugs' and 'analyses' in PDF -> should return models 3 and 5 only.
        ExportType lPDFExportType = reportingService.getExportType("PDF");
        String[] lBugAnalyse = { lBug1, lAnalyse1 };
        List<ReportModelData> lReports1 =
                reportingService.getCompatibleModels(adminRoleToken,
                        lBugAnalyse, lPDFExportType);

        assertTrue("less than two reports returned", lReports1.size() >= 2);

        //All models for bugs in PDF => should return models 1, 2, 3, 4 and 5.
        String[] lBugs = { lBug1 };
        List<ReportModelData> lReports2 =
                reportingService.getCompatibleModels(adminRoleToken, lBugs,
                        lPDFExportType);

        assertTrue(lReports2.size() >= 5);

    }

}

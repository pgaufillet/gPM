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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test the generateReport method in the reporting service
 * 
 * @author ahaugomm
 */
public class TestGenerateReportService extends AbstractBusinessServiceTestCase {

    /** The reporting service */
    private ReportingService reportingService;

    /** The sheet service */
    private SheetService sheetService;

    private static final String[] SHEET_TYPE_NAMES =
            { GpmTestValues.SHEET_TYPE_DOG, GpmTestValues.SHEET_TYPE_CAT,
             GpmTestValues.SHEET_TYPE_MOUSE };

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(getProcessName(),
                        SHEET_TYPE_NAMES[0]);
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[1]));
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[2]));
        List<String> lSheetIds = new ArrayList<String>(lSheets.size());
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds.add(lSheet.getId());
        }

        String[] lExportTypes = { "XML", "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[3];
        for (int i = 0; i < 3; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }

        ReportModelData lReportModelData =
                new ReportModelData("myReport", "my first report3",
                        "report/generateReport/myReport.jasper",
                        lFieldsContainerIds, lExportTypes, null);

        // PDF EXPORT

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        reportingService.generateReport(adminRoleToken, getProcessName(),
                lOutputStream, lSheetIds, SheetExportFormat.PDF, Locale.FRENCH,
                lReportModelData, new ContextBase());
        assertTrue("Empty report generated in PDF ",
                lOutputStream.toByteArray().length != 0);

        // XLS EXPORT

        lOutputStream = new ByteArrayOutputStream();
        reportingService.generateReport(adminRoleToken, getProcessName(),
                lOutputStream, lSheetIds, SheetExportFormat.EXCEL,
                Locale.FRENCH, lReportModelData, new ContextBase());
        assertTrue("Empty report generated in XLS ",
                lOutputStream.toByteArray().length != 0);
    }

    /**
     * Test the sheet export when the ReportModel is null
     */
    public void testNullReportModelDataCase() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(getProcessName(),
                        SHEET_TYPE_NAMES[0]);
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[1]));
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[2]));
        List<String> lSheetIds = new ArrayList<String>(lSheets.size());
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds.add(lSheet.getId());
        }

        String[] lFieldsContainerIds = new String[3];
        for (int i = 0; i < 3; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }

        // Test with outputstream
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        try {
            reportingService.generateReport(adminRoleToken, getProcessName(),
                    lOutputStream, lSheetIds, SheetExportFormat.PDF,
                    Locale.FRENCH, null, new ContextBase());
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }

    }

    /**
     * Test the sheet export when the outputStream is null
     */
    public void testNullOutputStream() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(getProcessName(),
                        SHEET_TYPE_NAMES[0]);
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[1]));
        lSheets.addAll(sheetService.getSheetsByType(getProcessName(),
                SHEET_TYPE_NAMES[2]));
        List<String> lSheetIds = new ArrayList<String>(lSheets.size());
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds.add(lSheet.getId());
        }

        String[] lExportTypes = { "XML", "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[3];
        for (int i = 0; i < 3; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }

        ReportModelData lReportModelData =
                new ReportModelData("myReport", "my first report3",
                        "report/generateReport/myReport.jasper",
                        lFieldsContainerIds, lExportTypes, null);

        try {
            reportingService.generateReport(adminRoleToken, getProcessName(),
                    null, lSheetIds, SheetExportFormat.PDF, Locale.FRENCH,
                    lReportModelData, new ContextBase());
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }
}

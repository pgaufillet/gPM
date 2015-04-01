/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests.integration;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSheetExport
 * 
 * @author ahaugommard
 */
public class TestSheetExport extends GenericIntegrationTest {

    /** Name for the default filter to find. */
    private final static String FILTER_NAME =
            GpmTestValues.FILTER_TEST_FILTER_1;

    /** Name for the product in which the default filter to find is visible. */
    public static final String FILTER_PRODUCT_NAME =
            GpmTestValues.PRODUCT_BERNARD_STORE_NAME;

    /** Name of the report model to find. */
    private static final String REPORT_MODEL_NAME = "myPDFReport";

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (with report model) - logout
     */
    public void testWithoutCacheWithReportTemplate() {
        clearCache();
        startTimer("testWithoutCacheWithReportTemplate");

        //Get services
        SearchService lSearchService = serviceLocator.getSearchService();
        ReportingService lReportingService =
                serviceLocator.getReportingService();

        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        clearCache();
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        ReportModelData lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        clearCache();

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        lReportingService.generateReport(adminRoleToken, getProcessName(),
                lOutputStream, lSheetIds, SheetExportFormat.PDF, Locale.FRANCE,
                lReportModelData, new ContextBase());

        //empty cache
        clearCache();

        // do it again
        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        clearCache();
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        clearCache();
        ByteArrayOutputStream lByteArrayOutputStream =
                new ByteArrayOutputStream();

        lReportingService.generateReport(adminRoleToken, getProcessName(),
                lByteArrayOutputStream, lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        stopTimer();
    }

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (with report model) - logout
     */
    public void testWithCacheWithReportTemplate() {
        clearCache();
        startTimer("testWithCacheWithReportTemplate");

        //Get services
        SearchService lSearchService = serviceLocator.getSearchService();
        ReportingService lReportingService =
                serviceLocator.getReportingService();

        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        ReportModelData lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        lReportingService.generateReport(adminRoleToken, getProcessName(),
                lOutputStream, lSheetIds, SheetExportFormat.PDF, Locale.FRANCE,
                lReportModelData, new ContextBase());

        // do it again
        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        ByteArrayOutputStream lByteArrayOutputStream =
                new ByteArrayOutputStream();

        lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        lReportingService.generateReport(adminRoleToken, getProcessName(),
                lByteArrayOutputStream, lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        stopTimer();
    }

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (without report model) - logout
     */
    public void testWithoutCacheWithoutReportTemplateWithOutputstream() {
        clearCache();
        startTimer("testWithoutCacheWithoutReportTemplate");
        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);

        clearCache();
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }
        sheetService.exportSheets(adminRoleToken, new ByteArrayOutputStream(),
                lSheetIds, SheetExportFormat.PDF);

        clearCache();

        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        clearCache();
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }
        sheetService.exportSheets(adminRoleToken, new ByteArrayOutputStream(),
                lSheetIds, SheetExportFormat.PDF);

        stopTimer();
    }

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (without report model) - logout
     */
    public void testWithCacheWithoutReportTemplateWithOutputstream() {
        clearCache();
        startTimer("testWithCacheWithoutReportTemplate");
        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }
        sheetService.exportSheets(adminRoleToken, new ByteArrayOutputStream(),
                lSheetIds, SheetExportFormat.PDF);

        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }
        sheetService.exportSheets(adminRoleToken, new ByteArrayOutputStream(),
                lSheetIds, SheetExportFormat.PDF);

        stopTimer();
    }

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (with report model) - logout
     */
    public void testWithoutCacheWithReportTemplateWithOutputstream() {
        clearCache();
        startTimer("testWithoutCacheWithReportTemplate");

        //Get services
        SearchService lSearchService = serviceLocator.getSearchService();
        ReportingService lReportingService =
                serviceLocator.getReportingService();

        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        clearCache();
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        ReportModelData lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        clearCache();
        lReportingService.generateReport(adminRoleToken, getProcessName(),
                new ByteArrayOutputStream(), lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        //empty cache
        clearCache();

        // do it again
        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        clearCache();
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);
        clearCache();
        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        clearCache();
        lReportingService.generateReport(adminRoleToken, getProcessName(),
                new ByteArrayOutputStream(), lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        stopTimer();
    }

    /**
     * Scenario (executed twice, for cache reasons): - login - get filter
     * TEST_FILTER_1 - execute the filter - export in PDF all result sheets
     * (with report model) - logout
     */
    public void testWithCacheWithReportTemplateWithOutputstream() {
        clearCache();
        startTimer("testWithCacheWithReportTemplate");

        //Get services
        SearchService lSearchService = serviceLocator.getSearchService();
        ReportingService lReportingService =
                serviceLocator.getReportingService();

        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        List<String> lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        ReportModelData lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());

        lReportingService.generateReport(adminRoleToken, getProcessName(),
                new ByteArrayOutputStream(), lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        // do it again
        lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);
        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0],
                        lExecutableFilterData);

        assertNotNull("no result for sheet filter.", lResults);
        lSheetIds = new ArrayList<String>(lResults.size());
        for (SheetSummaryData lSheet : lResults) {
            lSheetIds.add(lSheet.getId());
        }

        lReportModelData =
                lReportingService.getReportModel(REPORT_MODEL_NAME,
                        getProcessName());
        lReportingService.generateReport(adminRoleToken, getProcessName(),
                new ByteArrayOutputStream(), lSheetIds, SheetExportFormat.PDF,
                Locale.FRANCE, lReportModelData, new ContextBase());

        stopTimer();
    }
}

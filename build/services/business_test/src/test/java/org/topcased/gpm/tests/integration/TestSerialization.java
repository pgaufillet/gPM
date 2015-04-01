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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSerialization
 * 
 * @author ahaugommard
 */
public class TestSerialization extends GenericIntegrationTest {

    /** Name for the default filter to find. */
    private final static String FILTER_NAME =
            GpmTestValues.FILTER_TEST_FILTER_1;

    /** Name for the product in which the default filter to find is visible. */
    public static final String FILTER_PRODUCT_NAME =
            GpmTestValues.PRODUCT_BERNARD_STORE_NAME;

    /**
     * Scenario (executed twice without cache): - login - get a filter - execute
     * the filter - serialize all sheet results - logout
     * 
     * @throws IOException
     */
    public void testWithoutCache() throws IOException {
        startTimer("testWithoutCache");
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

        File lFile;
        lFile = File.createTempFile("pre", "suffix");
        OutputStream lOutputStream = new FileOutputStream(lFile);
        serviceLocator.getSerializationService().serializeSheets(
                adminRoleToken, lSheetIds, lOutputStream);
        lOutputStream.close();
        lFile.delete();

        clearCache();

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

        lFile = File.createTempFile("pre", "suffix");
        lOutputStream = new FileOutputStream(lFile);
        serviceLocator.getSerializationService().serializeSheets(
                adminRoleToken, lSheetIds, lOutputStream);
        lOutputStream.close();
        lFile.delete();
        stopTimer();
    }

    /**
     * Scenario (executed twice with cache): - login - get a filter - execute
     * the filter - serialize all sheet results - logout
     * 
     * @throws IOException
     */
    public void testWithCache() throws IOException {
        startTimer("testWithCache");
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

        File lFile;
        lFile = File.createTempFile("pre", "suffix");
        OutputStream lOutputStream = new FileOutputStream(lFile);
        serviceLocator.getSerializationService().serializeSheets(
                adminRoleToken, lSheetIds, lOutputStream);
        lOutputStream.close();
        lFile.delete();

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

        lFile = File.createTempFile("pre", "suffix");
        lOutputStream = new FileOutputStream(lFile);
        serviceLocator.getSerializationService().serializeSheets(
                adminRoleToken, lSheetIds, lOutputStream);
        lOutputStream.close();
        lFile.delete();
        stopTimer();
    }
}

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

import java.util.Collection;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestFilters
 * 
 * @author ahaugommard
 */
public class TestFilters extends GenericIntegrationTest {

    /** Name for the default filter to find. */
    public static final String FILTER_NAME = GpmTestValues.FILTER_TEST_FILTER_1;

    /** Name for the product in which the default filter to find is visible. */
    public static final String FILTER_PRODUCT_NAME =
            GpmTestValues.PRODUCT_BERNARD_STORE_NAME;

    /**
     * Scenario (executed twice without cache): - login - select a product -
     * find the list of all visible filters for the current user and product -
     * select one of the filters - execute it - logout
     */
    public void testWithoutCache() {
        startTimer("testWithoutCache");

        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lFilterToUse =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);

        assertNotNull("filter " + FILTER_NAME + " not found.", lFilterToUse);

        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0], lFilterToUse);
        assertNotNull("no results for filter " + FILTER_NAME + ".", lResults);

        // empty the cache
        clearCache();
        //Execute the same steps again

        lFilterToUse =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);

        assertNotNull("filter " + FILTER_NAME + " not found.", lFilterToUse);

        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0], lFilterToUse);
        assertNotNull("no results for filter " + FILTER_NAME + ".", lResults);

        stopTimer();
    }

    /**
     * Scenario (executed twice with cache): - login - select a product - find
     * the list of all visible filters for the current user and product - select
     * one of the filters - execute it - logout
     */
    public void testWithCache() {
        startTimer("testWithCache");

        SearchService lSearchService = serviceLocator.getSearchService();

        ExecutableFilterData lFilterToUse =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);

        assertNotNull("filter " + FILTER_NAME + " not found.", lFilterToUse);

        Collection<SheetSummaryData> lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0], lFilterToUse);
        assertNotNull("no results for filter " + FILTER_NAME + ".", lResults);

        //Execute the same steps again

        lFilterToUse =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), FILTER_PRODUCT_NAME, null,
                        FILTER_NAME);

        assertNotNull("filter " + FILTER_NAME + " not found.", lFilterToUse);

        lResults =
                executeSheetFilter(adminRoleToken, getProcessName(),
                        getProductName(), getAdminLogin()[0], lFilterToUse);
        assertNotNull("no results for filter " + FILTER_NAME + ".", lResults);

        stopTimer();
    }

}

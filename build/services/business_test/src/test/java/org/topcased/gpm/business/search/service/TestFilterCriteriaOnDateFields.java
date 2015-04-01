/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * @author phtsaan
 */
public class TestFilterCriteriaOnDateFields extends FiltersCreationUtils {

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_GT =
            "search/TestFilterCriteriaOnDateGreaterThan.xml";

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS_GT =
            "Filter with additional constraints on date greater than";

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_EQ =
            "search/TestFilterCriteriaOnDateEqual.xml";

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS_EQ =
            "Filter with additional constraints on date equal";

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_LT =
            "search/TestFilterCriteriaOnDateLessThan.xml";

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS_LT =
            "Filter with additional constraints on date lessThan";

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_MCR =
            "search/TestFilterMultiCriteriaWithResult.xml";

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS_MCR =
            "Filter with additional constraints with result";

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_MCNR =
            "search/TestFilterMultiCriteriaWithOutResult.xml";

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS_MCNR =
            "Filter with additional constraints with out result";

    private SearchService searchService;

    private String adminRoleToken524;

    private static final String ADMINISTRATOR_ROLE = "admin";

    private static final String[] VIEWER_ROLE_LOGIN = { "admin", "admin" };

    public void setUp() {
        super.setUp();

        searchService = serviceLocator.getSearchService();
        sheetService = serviceLocator.getSheetService();
        String lUserToken =
                authorizationService.login(VIEWER_ROLE_LOGIN[0],
                        VIEWER_ROLE_LOGIN[1]);

        adminRoleToken524 =
                authorizationService.selectRole(lUserToken, ADMINISTRATOR_ROLE,
                        null, getProcessName());

    }

    @SuppressWarnings("unchecked")
	public void testDateFieldGreaterThan() {

        // instantiate filter
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_GT);

        // create the filter executable data
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken524,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS_GT);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        adminRoleToken524,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        assertNotNull(lFilterResultIterator);

        /* 
         * the filter should return all the sheet that DOJO_Test_simpleDate field 
         * are greater than 2012-03-18 and the sheet state is Temporary 
         * expected result size 3.
         * expected result values [RET524_1,RET524_2,RET524_3]
         * 
         */
        String[] lExpectedResult = { "RET524_1", "RET524_2", "RET524_3" };
        List<String> llExpectedResultList = Arrays.asList(lExpectedResult);
        List<SheetSummaryData> lResultList =
                IteratorUtils.toList(lFilterResultIterator);
        assertTrue("Incorrect filter result size the expected size is 3: ",
                3 == lResultList.size());
        for (SheetSummaryData lSheetSummaryData : lResultList) {
            assertTrue(
                    "Incorrect filter result : ",
                    llExpectedResultList.contains(lSheetSummaryData.getSheetReference()));
        }
    }

    @SuppressWarnings("unchecked")
	public void testDateFieldEqual() {
        // instantiate filter
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_EQ);

        // create the filter executable data
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken524,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS_EQ);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        adminRoleToken524,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        assertNotNull(lFilterResultIterator);

        /* 
         * the filter should return only the sheet that DOJO_Test_simpleDate is 
         * equal to 2012-03-25 and the sheet state is Temporary 
         * expected result size 1.
         * expected result values [RET524_1]
         * 
         */
        String[] lExpectedResult = { "RET524_1" };
        List<SheetSummaryData> lResultList =
                IteratorUtils.toList(lFilterResultIterator);
        assertTrue("Incorrect filter result size the expected size is 1: ",
                1 == lResultList.size());
        for (SheetSummaryData lSheetSummaryData : lResultList) {
            assertTrue(
                    "Incorrect filter result : ",
                    lExpectedResult[0].equals(lSheetSummaryData.getSheetReference()));
        }
    }

    @SuppressWarnings("unchecked")
	public void testDateFieldLessThan() {
        // instantiate filter
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_LT);

        // create the filter executable data
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken524,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS_LT);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        adminRoleToken524,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        assertNotNull(lFilterResultIterator);

        /* 
         * the filter should return all the sheet that DOJO_Test_simpleDate field 
         * are less than 2012-03-25 and the sheet state is Temporary 
         * expected result size 2.
         * expected result values [RET524_2,RET524_3]
         * 
         */
        String[] lExpectedResult = { "RET524_2", "RET524_3" };
        List<String> llExpectedResultList = Arrays.asList(lExpectedResult);
        List<SheetSummaryData> lResultList =
                IteratorUtils.toList(lFilterResultIterator);
        assertTrue("Incorrect filter result size the expected size is 2: ",
                2 == lResultList.size());
        for (SheetSummaryData lSheetSummaryData : lResultList) {
            assertTrue(
                    "Incorrect filter result : ",
                    llExpectedResultList.contains(lSheetSummaryData.getSheetReference()));
        }
    }

    @SuppressWarnings("unchecked")
	public void testFieldWithMultiCriteria() {
        // instantiate filter
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_MCR);

        // create the filter executable data
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken524,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS_MCR);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        adminRoleToken524,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        assertNotNull(lFilterResultIterator);

        /* 
         * the filter should return all the sheet that respect criteria defined in the TestFilterMultiCriteriaWithResult.xml
         * and the sheet state is Temporary 
         * expected result size 2.
         * expected result values [RET524_1,RET524_2]
         * 
         */
        String[] lExpectedResult = { "RET524_1", "RET524_2" };
        List<String> llExpectedResultList = Arrays.asList(lExpectedResult);
        List<SheetSummaryData> lResultList =
                IteratorUtils.toList(lFilterResultIterator);
        assertTrue(
                "Incorrect filter result size, the expected size is 2: instead of "
                        + lResultList.size(), 2 == lResultList.size());
        for (SheetSummaryData lSheetSummaryData : lResultList) {
            assertTrue(
                    "Incorrect filter result : ",
                    llExpectedResultList.contains(lSheetSummaryData.getSheetReference()));
        }
    }

    @SuppressWarnings("unchecked")
	public void testFieldWithMultiCriteriaNoResult() {
        // instantiate filter
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS_MCNR);

        // create the filter executable data
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken524,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS_MCNR);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        adminRoleToken524,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        assertNotNull(lFilterResultIterator);

        /* 
         * the filter should return all the sheet that respect criteria defined in the TestFilterMultiCriteriaWithResult.xml
         * and the sheet state is Temporary 
         * expected result size 0.
         * expected result values []
         * 
         */
        List<SheetSummaryData> lResultList = IteratorUtils.toList(lFilterResultIterator);
        assertTrue(
                "Incorrect filter result size, the expected size is 0: instead of "
                        + lResultList.size(), 0 == lResultList.size());
    }

}

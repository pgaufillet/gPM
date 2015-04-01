/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.link.service.LinkSummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;

/**
 * Test the filter links of the Search Service
 * 
 * @author tpanuel
 */
public class TestLinkFilters extends AbstractBusinessServiceTestCase {

    /** MAX_RESULTS */
    private static final int FILTER_MAX_NUMBER_RESULTS = 500;

    private static final String FILTER_TEST_1 = "LINK_FILTER_TEST_1";

    private static final String FILTER_TEST_2 = "LINK_FILTER_TEST_2";

    private static final String FILTER_TEST_3 = "LINK_FILTER_TEST_3";

    private static final String FILTER_TEST_4 = "LINK_FILTER_TEST_4";

    private static final String INSTANCE_FILE = "search/TestLinkFilters.xml";

    private static final String[] SHEETS_REFERENCE = new String[] { "A", "B" };

    private static final String[] PRODUCTS_NAME =
            new String[] { GpmTestValues.PRODUCT1_NAME,
                          GpmTestValues.PRODUCT_PRODUCT2 };

    private static final String[][] LINKS_INFO =
            new String[][] {
                            // ORIGIN_REFERENCE , DEST_REFERENCE,
                            // ORIGIN_PRODUCT_NAME, DEST_PRODUCT_NAME
                            { SHEETS_REFERENCE[0], SHEETS_REFERENCE[0],
                             PRODUCTS_NAME[0], PRODUCTS_NAME[0] },
                            { SHEETS_REFERENCE[1], SHEETS_REFERENCE[0],
                             PRODUCTS_NAME[0], PRODUCTS_NAME[0] },
                            { SHEETS_REFERENCE[1], SHEETS_REFERENCE[1],
                             PRODUCTS_NAME[0], PRODUCTS_NAME[0] },
                            { SHEETS_REFERENCE[0], SHEETS_REFERENCE[0],
                             PRODUCTS_NAME[0], PRODUCTS_NAME[1] },
                            { SHEETS_REFERENCE[1], SHEETS_REFERENCE[0],
                             PRODUCTS_NAME[1], PRODUCTS_NAME[0] },
                            { SHEETS_REFERENCE[0], SHEETS_REFERENCE[0],
                             PRODUCTS_NAME[1], PRODUCTS_NAME[1] },
                            { SHEETS_REFERENCE[1], SHEETS_REFERENCE[1],
                             PRODUCTS_NAME[1], PRODUCTS_NAME[1] },
                            { SHEETS_REFERENCE[0], SHEETS_REFERENCE[1],
                             PRODUCTS_NAME[1], PRODUCTS_NAME[1] } };

    /**
     * Tests the method executeLinkFilter and getResultForLinkFilter on normal
     * way. Moreover, an instantiation file is used so the method of creation of
     * a link filter from a such file are tested
     */
    public void testNormalCase() {
        // For each following filter (defined on the instance file) a specific 
        // criteria is added to a simple field of the link, for make the 
        // difference with values of the other tests
        instantiate(getProcessName(), INSTANCE_FILE);

        // First filter: 
        // - No criteria
        // - Sorting (and displaying) on all the links virtual fields
        List<Object[]> lExpectedResults1 = new ArrayList<Object[]>();
        lExpectedResults1.add(LINKS_INFO[7]);
        lExpectedResults1.add(LINKS_INFO[3]);
        lExpectedResults1.add(LINKS_INFO[0]);
        lExpectedResults1.add(LINKS_INFO[5]);
        lExpectedResults1.add(LINKS_INFO[2]);
        lExpectedResults1.add(LINKS_INFO[6]);
        lExpectedResults1.add(LINKS_INFO[1]);
        lExpectedResults1.add(LINKS_INFO[4]);
        checkFilterResult(FILTER_TEST_1, lExpectedResults1);

        // Second filter:
        // - Criteria on the linked sheet's references
        // - Sorting (and displaying) on the linked sheet's products name
        List<Object[]> lExpectedResults2 = new ArrayList<Object[]>();
        lExpectedResults2.add(LINKS_INFO[3]);
        lExpectedResults2.add(LINKS_INFO[0]);
        lExpectedResults2.add(LINKS_INFO[5]);
        checkFilterResult(FILTER_TEST_2, lExpectedResults2);

        // Third filter:
        // - Criteria on the linked sheet's products name
        // - Sorting (and displaying) on the linked sheet's references
        List<Object[]> lExpectedResults3 = new ArrayList<Object[]>();
        lExpectedResults3.add(LINKS_INFO[0]);
        lExpectedResults3.add(LINKS_INFO[2]);
        lExpectedResults3.add(LINKS_INFO[1]);
        checkFilterResult(FILTER_TEST_3, lExpectedResults3);

        // Fourth filter:
        // - Criteria on the linked sheet's products name and references
        // - Sorting (and displaying) on the linked sheet's products name and references
        List<Object[]> lExpectedResults4 = new ArrayList<Object[]>();
        lExpectedResults4.add(LINKS_INFO[0]);
        checkFilterResult(FILTER_TEST_4, lExpectedResults4);
    }

    /**
     * Tests the method executeLinkFilter specifying the number max of results
     * returned
     */
    public void testWithSpecificNumberResultCase() {
        // For each following filter (defined on the instance file) a specific 
        // criteria is added to a simple field of the link, for make the 
        // difference with values of the other tests
        instantiate(getProcessName(), INSTANCE_FILE);

        // Use the first filter with only 4 results
        List<Object[]> lExpectedResults1 = new ArrayList<Object[]>();
        lExpectedResults1.add(LINKS_INFO[7]);
        lExpectedResults1.add(LINKS_INFO[3]);
        lExpectedResults1.add(LINKS_INFO[0]);
        lExpectedResults1.add(LINKS_INFO[5]);
        checkFilterResult(FILTER_TEST_1, 4, 0, lExpectedResults1);

        // Use the first filter without the two firsts
        List<Object[]> lExpectedResults2 = new ArrayList<Object[]>();
        lExpectedResults2.add(LINKS_INFO[0]);
        lExpectedResults2.add(LINKS_INFO[5]);
        lExpectedResults2.add(LINKS_INFO[2]);
        lExpectedResults2.add(LINKS_INFO[6]);
        lExpectedResults2.add(LINKS_INFO[1]);
        lExpectedResults2.add(LINKS_INFO[4]);
        checkFilterResult(FILTER_TEST_1, FILTER_MAX_NUMBER_RESULTS, 2,
                lExpectedResults2);

        // Use the first filter with only 2 results and without the two firsts
        List<Object[]> lExpectedResults3 = new ArrayList<Object[]>();
        lExpectedResults3.add(LINKS_INFO[0]);
        lExpectedResults3.add(LINKS_INFO[5]);
        checkFilterResult(FILTER_TEST_1, 2, 2, lExpectedResults3);
    }

    /**
     * Sub Test
     * 
     * @param pFilterName
     *            The name of the filter to execute
     * @param pExpectedResults
     *            The value of the (same as the values returned but without the
     *            link id)
     */
    @SuppressWarnings("unchecked")
	private void checkFilterResult(String pFilterName,
            List<Object[]> pExpectedResults) {
        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lFilter =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, pFilterName);

        FilterResultIterator<LinkSummaryData> lResult =
                lSearchService.executeFilter(adminRoleToken, lFilter,
                        new FilterVisibilityConstraintData(null,
                                getProcessName(), null),
                        new FilterQueryConfigurator());
        Collection<LinkSummaryData> lResults1 = IteratorUtils.toList(lResult);

        compareFilterResult(lResults1, pExpectedResults);
    }

    /**
     * Sub Test
     * 
     * @param pFilterName
     *            The name of the filter to execute
     * @param pNumberResultMax
     *            The number max of result
     * @param pFirstResult
     *            The first result
     * @param pExpectedResults
     *            The value of the (same as the values returned but without the
     *            link id)
     */
    @SuppressWarnings("unchecked")
	private void checkFilterResult(String pFilterName, int pNumberResultMax,
            int pFirstResult, List<Object[]> pExpectedResults) {
        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lFilter =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, pFilterName);

        FilterResultIterator<LinkSummaryData> lResult =
                lSearchService.executeFilter(adminRoleToken, lFilter,
                        new FilterVisibilityConstraintData(null,
                                getProcessName(), null),
                        new FilterQueryConfigurator(0, pNumberResultMax,
                                pFirstResult));
        Collection<LinkSummaryData> lResults = IteratorUtils.toList(lResult);

        compareFilterResult(lResults, pExpectedResults);
    }

    /**
     * Compare results
     * 
     * @param pResults
     *            Results to test:
     * @param pExpectedResults
     *            Expected results
     */
    private void compareFilterResult(Collection<LinkSummaryData> pResults,
            List<Object[]> pExpectedResults) {
        assertNotNull("Invalid test values: expected result is null",
                pExpectedResults);
        assertNotNull("Invalid filter result", pResults);
        assertEquals(
                "Invalid filter result: not the same number of lines between "
                        + "expected result and filter result",
                pExpectedResults.size(), pResults.size());

        // The lines of the filter result
        int i = 0;
        for (LinkSummaryData lResult : pResults) {
            Object[] lExpectedResult = pExpectedResults.get(i);
            FieldSummaryData[] lFieldSummaryDatas =
                    lResult.getFieldSummaryDatas();

            assertNotNull(
                    "Invalid test values: expected result contains null values",
                    lExpectedResult);
            assertNotNull("Invalid filter result: contains a list null",
                    lResult);
            assertEquals("Invalid filter result: not similar format between "
                    + "expected result and filter result",
                    lFieldSummaryDatas.length, lExpectedResult.length);

            // The content of the current line of the filter result lResult
            for (int j = 0; j < lFieldSummaryDatas.length; j++) {
                FieldSummaryData lResultElement = lFieldSummaryDatas[j];
                Object lExpectedResultElement = lExpectedResult[j];

                if (lResultElement != null || lExpectedResultElement != null) {
                    assertTrue("Invalid filter result: bad value returned",
                            lResultElement != null
                                    && lExpectedResultElement != null);
                    // Only string on the test
                    assertTrue("Invalid filter result "
                            + lResultElement.getValue() + "     "
                            + lExpectedResultElement + ": bad value returned",
                            lResultElement.getValue().equals(
                                    (String) lExpectedResultElement));
                }
            }
            i++;
        }
    }

    /**
     * Tests the result of the execution of a queryLL
     */
    public void testQueryLLCase() {
        // For each following filter (defined on the instance file) a specific 
        // criteria is added to a simple field of the link, for make the 
        // difference with values of the other tests
        instantiate(getProcessName(), INSTANCE_FILE);

        // First filter: 
        // - No criteria
        // - Sorting (and displaying) on all the links virtual fields
        compareQueryLLAndFilterResult(FILTER_TEST_1);

        // Second filter:
        // - Criteria on the linked sheet's references
        // - Sorting (and displaying) on the linked sheet's products name
        compareQueryLLAndFilterResult(FILTER_TEST_2);

        // Third filter:
        // - Criteria on the linked sheet's products name
        // - Sorting (and displaying) on the linked sheet's references
        compareQueryLLAndFilterResult(FILTER_TEST_3);

        // Fourth filter:
        // - Criteria on the linked sheet's products name and references
        // - Sorting (and displaying) on the linked sheet's products name and references
        compareQueryLLAndFilterResult(FILTER_TEST_4);
    }

    /**
     * Check that the result of the queryLL are the same than the one of the
     * filter
     * 
     * @param pFilterName
     *            The name of the filter
     */
    @SuppressWarnings("unchecked")
	private void compareQueryLLAndFilterResult(String pFilterName) {
        SearchService lSearchService = serviceLocator.getSearchService();
        ExecutableFilterData lFilter =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, pFilterName);

        FilterResultIterator<LinkSummaryData> lResult =
                lSearchService.executeFilter(adminRoleToken, lFilter,
                        new FilterVisibilityConstraintData(null,
                                getProcessName(), null),
                        new FilterQueryConfigurator());
        Collection<LinkSummaryData> lResultsFromFilter =
                IteratorUtils.toList(lResult);

        FilterResultIdIterator lFilterResultIdIterator =
                lSearchService.executeFilterIdentifier(adminRoleToken, lFilter,
                        new FilterVisibilityConstraintData(StringUtils.EMPTY,
                                getProcessName(), StringUtils.EMPTY),
                        new FilterQueryConfigurator(0, 0, 0));
        List<String> lResultsFromQueryLL =
                IteratorUtils.toList(lFilterResultIdIterator);

        assertNotNull("Invalid queryLL result", lResultsFromQueryLL);
        assertNotNull("Invalid filter result", lResultsFromFilter);
        assertEquals(
                "Invalid filter result: not the same number of lines between "
                        + "expected result and filter result",
                lResultsFromFilter.size(), lResultsFromQueryLL.size());

        int i = 0;
        for (LinkSummaryData lResultFromFilter : lResultsFromFilter) {
            assertNotNull("Invalid filter result", lResultFromFilter);
            assertEquals(
                    "Incoherence between filter result and queryLL result",
                    lResultFromFilter.getId(), lResultsFromQueryLL.get(i));
            i++;
        }
    }
}

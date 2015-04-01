/***************************************************************
 * Copyright (c) 2008-2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos), Michael Kargbo (Atos), Thibault Landre (Atos)
 ******************************************************************/
package org.topcased.gpm.business.search;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestOrderByClause
 */
public class TestOrderByClause extends AbstractBusinessServiceTestCase {

    private static final int FILTER_MAX_NUMBER_RESULTS = 500;

    private static final String INSTANCE_FILE =
            "search/TestOrderByCategoryDefinition.xml";

    /* Filters used by this test. Those filters are defined in the file : 
      search/TestOrderByCategoryDefinition.xml */
    /**
     * A basic filter.
     * <p>
     * <b>Displays</b> Sheets reference
     * </p>
     * <p>
     * <b>Order by (sort)</b> Sheets reference <code>asc</code>
     * </p>
     */
    private static final String FILTER_ORDER_BY = "OrderByCategorySheet_filter";

    /**
     * A filter to test the "asc order by" on simple field and multivalued
     * fields. Only the sheets reference are displayed in order to test that the
     * query is correctly created with a correct select clause even if the
     * fields used to order the results are not displayed.
     * <p>
     * <b>Displays</b> Sheets reference
     * </p>
     * <p>
     * <b>Order by (sort) asc</b>
     * <ul>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_CATEGORY_ORDER_NAME_ASC =
            "OrderByCategorySheet_filter_asc";

    /**
     * A filter to test the "desc order by" on simple field and multivalued
     * fields. Only the sheets reference are displayed in order to test that the
     * query is correctly created with a correct select clause even if the
     * fields used to order the results are not displayed.
     * <p>
     * <b>Displays</b> Sheets reference
     * </p>
     * <p>
     * <b>Order by (sort) desc</b>
     * <ul>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_CATEGORY_ORDER_NAME_DESC =
            "OrderByCategorySheet_filter_desc";

    /**
     * A filter to test the "asc order by" on simple field and multivalued
     * fields.
     * <p>
     * <b>Displays</b>
     * <ul>
     * <li>Sheets reference</li>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     * <p>
     * <b>Order by (sort) asc</b>
     * <ul>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_CATEGORY_ORDER_NAME_SHOW_ASC =
            "OrderByCategorySheet_filter_show_asc";

    /**
     * A filter to test the "desc order by" on simple field and multivalued
     * fields.
     * <p>
     * <b>Displays</b>
     * <ul>
     * <li>Sheets reference</li>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     * <p>
     * <b>Order by (sort) desc</b>
     * <ul>
     * <li>OrderByCategorySheet_STRING</li>
     * <li>OrderByCategorySheet_CHOICE</li>
     * <li>OrderByCategorySheet_BOOLEAN</li>
     * <li>OrderByCategorySheet_INTEGER</li>
     * <li>OrderByCategorySheet_REAL</li>
     * <li>OrderByCategorySheet_DATE</li>
     * <li>OrderByCategorySheet_ATTACHEDFIELD</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_BOOLEAN</li>
     * <li>OrderByCategorySheet_MULTIVALUED_STRING</li>
     * <li>OrderByCategorySheet_MULTIVALUED_INTEGER</li>
     * <li>OrderByCategorySheet_MULTIVALUED_DATE</li>
     * <li>OrderByCategorySheet_MULTIVALUED_ATTACHEDFIELD</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_CATEGORY_ORDER_NAME_SHOW_DESC =
            "OrderByCategorySheet_filter_show_desc";

    /**
     * A filter to test the "def asc order by" on choice fields.
     * <p>
     * <b>Displays</b>
     * <ul>
     * <li>Sheets reference</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * </ul>
     * </p>
     * <p>
     * <b>Order by (sort)</b>
     * <ul>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE order by def asc</li>
     * <li>Sheets reference order by asc</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_MULTI_CATEGORY_ORDER_NAME_ASC =
            "OrderByCategorySheet_filter_multi_asc";

    /**
     * A filter to test the "def desc order by" on choice fields.
     * <p>
     * <b>Displays</b>
     * <ul>
     * <li>Sheets reference</li>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE</li>
     * </ul>
     * </p>
     * <p>
     * <b>Order by (sort)</b>
     * <ul>
     * <li>OrderByCategorySheet_MULTIPLE_CHOICE order by def desc</li>
     * <li>Sheets reference order by desc</li>
     * </ul>
     * </p>
     */
    private static final String FILTER_MULTI_CATEGORY_ORDER_NAME_DESC =
            "OrderByCategorySheet_filter_multi_desc";

    /*
     * Sheets used by this test. Those sheets are defined in the file : 
     * /gPM-tests-framework/src/main/resources/data/sheets/instance_test_base_sheets.xml
     * 
     * Between each sheet, an unique field value is changed to be able to sort on several fields. See the xml file for the definition of each sheet. 
     */
    private static final String S1 = "OrderByCategorySheet_Sheet01";

    private static final String S2 = "OrderByCategorySheet_Sheet02";

    private static final String S3 = "OrderByCategorySheet_Sheet03";

    private static final String S4 = "OrderByCategorySheet_Sheet04";

    private static final String S5 = "OrderByCategorySheet_Sheet05";

    private static final String S6 = "OrderByCategorySheet_Sheet06";

    private static final String S7 = "OrderByCategorySheet_Sheet07";

    private static final String S8 = "OrderByCategorySheet_Sheet08";

    private static final String S9 = "OrderByCategorySheet_Sheet09";

    private static final String S10 = "OrderByCategorySheet_Sheet10";

    private static final String S11 = "OrderByCategorySheet_Sheet11";

    private static final String S12 = "OrderByCategorySheet_Sheet12";

    private static final String S13 = "OrderByCategorySheet_Sheet13";

    private static final String S14 = "OrderByCategorySheet_Sheet14";

    private static final String S15 = "OrderByCategorySheet_Sheet15";

    private static final String S16 = "OrderByCategorySheet_Sheet16";

    private static final String S17 = "OrderByCategorySheet_Sheet17";

    private static final String S18 = "OrderByCategorySheet_Sheet18";

    private static final String S19 = "OrderByCategorySheet_Sheet19";

    /*
     * Expected results for those tests
     */
    private static final String[] FILTER_ORDER_BY_RESULT =
            { S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15,
             S16, S17, S18, S19 };

    private static final String[] FILTER_CATEGORY_ORDER_ASC_RESULT =
            { S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15,
             S16, S17, S18, S19 };

    private static final String[] FILTER_CATEGORY_ORDER_DESC_RESULT =
            { S19, S18, S17, S16, S15, S14, S13, S12, S11, S10, S9, S8, S7, S6,
             S5, S4, S3, S2, S1 };

    /**
     * Results are sorted here by def asc only, which means by the order of the
     * category used for the choice field.
     */
    private static final String[] FILTER_CATEGORY_MULTI_ORDER_ASC_RESULT =
            { S1, S2, S3, S4, S5, S6, S7, S8, S9, S19, S10, S11, S12, S13, S14,
             S15, S16, S17, S18 };

    /**
     * Results are sorted here by def desc only, which means by the order of the
     * category used for the choice field.
     */
    private static final String[] FILTER_CATEGORY_MULTI_ORDER_DESC_RESULT =
            { S18, S17, S16, S15, S14, S13, S12, S11, S10, S19, S9, S8, S7, S6,
             S5, S4, S3, S2, S1 };

    /**
     * Test method executeFilterIdentifier(...) with a filter with an orderBy
     * clause. The sheet ID found as a result of this filter should be sorted.
     */
    public void testOrderByClauseInQueryLLExecution() {
        executiveTest(FILTER_ORDER_BY, FILTER_ORDER_BY_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_asc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (asc).
     */
    public void testOrderByClauseWithCategoryOrderAsc() {
        executiveTest(FILTER_CATEGORY_ORDER_NAME_ASC,
                FILTER_CATEGORY_ORDER_ASC_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_asc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (asc).
     */
    public void testOrderByClauseWithCategoryOrderShowAsc() {
        executiveTest(FILTER_CATEGORY_ORDER_NAME_SHOW_ASC,
                FILTER_CATEGORY_ORDER_ASC_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_desc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (desc).
     */
    public void testOrderByClauseWithCategoryOrderDesc() {
        executiveTest(FILTER_CATEGORY_ORDER_NAME_DESC,
                FILTER_CATEGORY_ORDER_DESC_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_desc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (desc).
     */
    public void testOrderByClauseWithCategoryOrderShowDesc() {
        executiveTest(FILTER_CATEGORY_ORDER_NAME_SHOW_DESC,
                FILTER_CATEGORY_ORDER_DESC_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_asc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (def_asc).
     */
    public void testMultiOrderByClauseWithCategoryOrderAsc() {
        executiveTest(FILTER_MULTI_CATEGORY_ORDER_NAME_ASC,
                FILTER_CATEGORY_MULTI_ORDER_ASC_RESULT);
    }

    /**
     * Test method executeFilterIdentifier(...) with a filter with 'def_desc' as
     * orderBy clause. The sheet name found as a result of this filter should be
     * sorted by category definition (def_desc).
     */
    public void testMultiOrderByClauseWithCategoryOrderDesc() {
        executiveTest(FILTER_MULTI_CATEGORY_ORDER_NAME_DESC,
                FILTER_CATEGORY_MULTI_ORDER_DESC_RESULT);
    }

    /**
     * Execute the given filter name and check that the result is equals to the
     * result expected.
     * 
     * @param pFilterName
     *            the name of the filter to test
     * @param pResult
     *            the result expected.
     */
    @SuppressWarnings("unchecked")
    private void executiveTest(String pFilterName, String[] pResult) {

        // Instantiate the file containing the definition of filters which will be tested. 
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get the SearchService 
        SearchService lSearchService = serviceLocator.getSearchService();

        // Get the executable filter data for the given filter name
        ExecutableFilterData lExecutableFilterData =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, pFilterName);

        // Check that the filter exists. 
        assertNotNull("No filter has been found with name '" + pFilterName
                + "'.", lExecutableFilterData);

        // Execute the filter
        FilterResultIdIterator lFilterResultIdIterator =
                lSearchService.executeFilterIdentifier(adminRoleToken,
                        lExecutableFilterData,
                        new FilterVisibilityConstraintData(StringUtils.EMPTY,
                                getProcessName(), StringUtils.EMPTY),
                        new FilterQueryConfigurator(0,
                                FILTER_MAX_NUMBER_RESULTS, 0));

        // Get the result sheets id
        List<String> lResultSheetIds =
                IteratorUtils.toList(lFilterResultIdIterator);

        // Check that the result is not null
        assertNotNull("No result has been found for filter '" + pFilterName
                + "'.", lResultSheetIds);

        // Get the reference of the sheets .
        String[] lResultSheetReferences = new String[lResultSheetIds.size()];
        int i = 0;
        for (String lResultSheetId : lResultSheetIds) {
            lResultSheetReferences[i] =
                    sheetService.getCacheableSheet(adminRoleToken,
                            lResultSheetId, CacheProperties.IMMUTABLE).getFunctionalReference();
            i++;
        }

        // Check that the result is equals to the result expected
        assertTrue("The result obtained was not expected.", Arrays.equals(
                pResult, lResultSheetReferences));
    }

}

/*******************************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Lesser Gnu
 * Public License (LGPL) which accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 * Contributors: Anne Haugommard (Atos
 * Origin)
 * *********************************************/
package org.topcased.gpm.business.search;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.AssertionFailedError;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Test multi-level filters
 * 
 * @author ahaugomm
 */
public class TestMultiLevelFilters extends AbstractBusinessServiceTestCase {

    private static final String XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE =
        "search/multiLevelFilters.xml";

    private SearchService searchService;

    /**
     * Override method from Abstract test case. Filters defined for this class
     * are in file
     * {@link TestMultiLevelFilters#XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        searchService.setMaxFieldsDepth(3);
        instantiate(getProcessName(), XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE);

    }

    /**
     * test with Simple fields on a 2-level hierarchy: <br>
     * <ul>
     * <li>hierarchy:
     * <ul>
     * <li>CAT - link Cat-Cat (bidirectional) - CAT - link CAT_SHEETTYPE1 -
     * SheetType1,
     * </ul>
     * <li>fields in filter result:</li>
     * <ul>
     * <li>sheet reference
     * <li>simple string field
     * <li>simple integer field
     * <li>simple boolean field
     * <li>simple real field
     * <li>simple date field
     * </ul>
     * <li>all with order ASC or DESC
     * <ul>
     * <li>choice field, with DEF_desc order
     * </ul>
     * </ul>
     */
    public void testSimpleFieldsResultsLevel2() {
        filterExecutionAndResultComparison(
                SIMPLE_FIELDS_LEVEL2_RESULT_FILTER_NAME,
                SIMPLE_FIELDS_LEVEL2_RESULT);
    }

    /**
     * Check that multivalued fields are supported in filter results -> correct
     * cross product
     */
    public void testMultiValuedFields() {
        filterExecutionAndResultComparison(MULTIVALUED_FIELDS_FILTER_NAME,
                MULTIVALUED_FIELDS_FILTER_RESULTS);
    }

    /** Check that maxNb filter results is effective with Multi-Level filters */
    public void testMoreThanMaxFilterNbResults() {
        /* This filter should return more than 1 result if no limitation,
         * but maxNbResult is limited to 1,
         * so only 1 result must be returned (Tom). */
        filterExecutionAndResultComparison(MORE_THAN_MAX_NB_FILTER_NAME, 1,
                null, 1);
    }

    /**
     * Test with Product field on a 2-level hierarchy (bug 503) <br>
     * <ul>
     * <li>hierarchy:
     * <ul>
     * <li>CAT Reference,
     * <li>CAT - link Cat-Cat (bidirectional) - Cat - Store - Location,
     * </ul>
     * </ul>
     */
    public void testLevel2ProductField() {
        filterExecutionAndResultComparison(LEVEL2_PRODUCT_FIELD_FILTER_NAME,
                LEVEL2_PRODUCT_FIELD_RESULT);
    }

    /**
     * Test with several Product field on different levels (bug 499) <br>
     * <ul>
     * <li>hierarchy:
     * <ul>
     * <li>CAT - Store - Location,
     * <li>CAT - link Cat-Cat (bidirectional) - Cat - Store - Location,
     * </ul>
     * </ul>
     */
    public void testSeveralProductFields() {
        filterExecutionAndResultComparison(SEVERAL_PRODUCT_FIELDS_FILTER_NAME,
                SEVERAL_PRODUCT_FIELDS_RESULT);
    }

    /**
     * Generic method of instance's filter execution and comparison of expected
     * results and effective results.
     * 
     * @param pFilterName
     *            Name of the instance's filter in DB.
     * @param pExpectedSize
     *            Expected result size
     * @param pExpectedResults
     *            Table of resulting field values (sorted).
     */
    @SuppressWarnings("unchecked")
    private void filterExecutionAndResultComparison(String pFilterName,
            int pExpectedSize, String[][] pExpectedResults, int pMaxNbResults) {
        ExecutableFilterData lFilter =
            searchService.getExecutableFilterByName(adminRoleToken,
                    getProcessName(), null, null, pFilterName);
        long lTimer = System.currentTimeMillis();
        FilterResultIterator<SheetSummaryData> lFilterResult =
            searchService.executeFilter(adminRoleToken, lFilter,
                    new FilterVisibilityConstraintData(null,
                            getProcessName(), null),
                            new FilterQueryConfigurator(0, pMaxNbResults, 0));
        Collection<SheetSummaryData> lSheetSummaryDatas =
            IteratorUtils.toList(lFilterResult);

        lTimer = System.currentTimeMillis() - lTimer;

        assertNotNull("No results found for filter '" + pFilterName + "'.",
                lSheetSummaryDatas);
        assertEquals(pExpectedSize, lSheetSummaryDatas.size());

        if (pExpectedResults != null) {
            int i = 0;
            for (SheetSummaryData lResult : lSheetSummaryDatas) {
                int j = 0;
                String[] lValues =
                    new String[lResult.getFieldSummaryDatas().length];
                for (FieldSummaryData lField : lResult.getFieldSummaryDatas()) {
                    lValues[j] = lField.getValue();
                    j++;
                }

                int lCompare = 0;
                boolean lFound = false;
                while ((!lFound) && (i < pExpectedResults.length)) {
                    if (!Arrays.deepEquals(pExpectedResults[lCompare], lValues)) {
                        lCompare++;
                    }
                    else {
                        lFound = true;
                    }
                }
                if ((!lFound) && (i >= pExpectedResults.length)) {
                    StringBuilder lErrorMsg =
                        new StringBuilder("Actual value '");
                    lErrorMsg.append(Arrays.toString(lValues));
                    lErrorMsg.append("' (" + i + "," + j
                            + ") not present in the expected values.");
                    throw new AssertionFailedError(lErrorMsg.toString());
                }
                i++;
            }
        }
    }

    /**
     * Generic method of instance's filter execution and comparison of expected
     * results and effective results.
     * 
     * @param pFilterName
     *            Name of the filter in DB.
     * @param pExpectedResults
     *            Table of resulting field values (sorted).
     */
    private void filterExecutionAndResultComparison(String pFilterName,
            String[][] pExpectedResults) {

        //Max filter nb results is fixed to 500 (user config is not taken into account).
        filterExecutionAndResultComparison(pFilterName,
                pExpectedResults.length, pExpectedResults, 500);
    }

    private final static String MORE_THAN_MAX_NB_FILTER_NAME =
        "MORE_THAN_MAX_NB_FILTER";

    private final static String MULTIVALUED_FIELDS_FILTER_NAME =
        "MULTIVALUED_FIELDS";

    private final static String SIMPLE_FIELDS_LEVEL2_RESULT_FILTER_NAME =
        "SIMPLE_FIELDS_LEVEL2_RESULT";

    private final static String LEVEL2_PRODUCT_FIELD_FILTER_NAME =
        "LEVEL2_PRODUCT_FIELD_FILTER";

    private final static String SEVERAL_PRODUCT_FIELDS_FILTER_NAME =
        "SEVERAL_PRODUCT_FIELDS_FILTER";

    private final static String[][] MULTIVALUED_FIELDS_FILTER_RESULTS =
        new String[][] {
                        /* Cat1 - Cat3 */
                        new String[] {
                                      "Cat1",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      "Cat1",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        /* Cat2 - Cat3 */
                        new String[] {
                                      "Cat2",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      "Cat2",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        /* Cat3 - Cat2 */
                        new String[] {
                                      "Cat2",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      "Cat3",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE
                        },
                        new String[] {
                                      "Cat3",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY
                        },
                        new String[] {
                                      "Cat3",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },

                        /* CatA - CatB */
                        new String[] {
                                      "CatA",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      null 
                        },
                        new String[] {
                                      "CatB",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      null
                        },

                        /* Garfield - Tom */
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE
                        },
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY
                        },
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE
                        },
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY
                        },
                        /* Garfield - Gros minet */
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      GpmTestValues.SHEET_REF_GARFIELD,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },

                        /* Gros minet - Garfield */
                        new String[] {
                                      "Gros Minet",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED
                        },
                        new String[] {
                                      "Gros Minet",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      "Gros Minet",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED
                        },
                        new String[] {
                                      "Gros Minet",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] { "testSheet", null, null
                        },
                        /* Tom - Garfield */
                        new String[] {
                                      "Tom",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED
                        },
                        new String[] {
                                      "Tom",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },
                        new String[] {
                                      "Tom",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_RED
                        },
                        new String[] {
                                      "Tom",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK
                        },  

                        new String[] {
                                      "testLargeString", null, null
                        }, 

                        new String[] {
                                      "Cat003", null, null
                        }, 

                        new String[] {
                                      "Cat001 link with Cat002", null, null
                        },  
                        new String[] {
                                      "Cat002 link with Cat001", null, null
                        }};

    private final static String[][] SIMPLE_FIELDS_LEVEL2_RESULT =
        new String[][] {
                        new String[] { "Tom", "12", "12.0", "Def_string",
                                       "2007-12-31", null, "true" 
                        },
                        new String[] { "testSheet", null, null, null, null,
                                       null, null 
                        },
                        new String[] { "testLargeString", null, null, null,
                                       null, null, null 
                        },
                        new String[] { "Gros Minet", "12", "12.0",
                                       "Def_string", "2007-12-31", null,
                        "true" },
                        new String[] { GpmTestValues.SHEET_REF_GARFIELD,
                                       null, null, null, null, null, null
                        },
                        new String[] {
                                      "Cat3",
                                      "21",
                                      "21.0",
                                      "vingt-et-un",
                                      "2008-11-21",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
                                      "true"
                        },
                        new String[] {
                                      "Cat3",
                                      "22",
                                      "22.0",
                                      "vingt-deux",
                                      "2008-11-22",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
                        "false" },
                        new String[] {
                                      "Cat3",
                                      "23",
                                      "23.0",
                                      "vingt-trois",
                                      "2008-11-23",
                                      GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
                                      "true"
                        },
                        new String[] { "Cat2", null, null, null, null,
                                       null, null 
                        },
                        new String[] { "Cat1", null, null, null, null,
                                       null, null 
                        },
                        new String[] { "CatA", null, null, null, null,
                                       null, null
                        },
                        new String[] { "CatB", null, null, null, null,
                                       null, null },
                                       new String[] { "Cat001 link with Cat002", null,
                                                      null, null, null, null, null 
                        },
                        new String[] { "Cat002 link with Cat001", null,
                                       null, null, null, null, null
                        },
                        new String[] { "Cat003", null, null, null, null,
                                       null, null 
                        } 
    };

    private final static String[][] LEVEL2_PRODUCT_FIELD_RESULT =
        new String[][] { new String[] { "Tom", "Lund" },
                         new String[] { "Tom", "Toulouse" },
                         new String[] { "testSheet", null },
                         new String[] { "testLargeString", null },
                         new String[] { "Gros Minet", "Lund" },
                         new String[] { "Gros Minet", "Toulouse" },
                         new String[] { "Garfield", "Lund" },
                         new String[] { "Garfield", "Toulouse" },
                         new String[] { "Cat3", "Lund" },
                         new String[] { "Cat3", "Toulouse" },
                         new String[] { "Cat2", "Lund" },
                         new String[] { "Cat2", "Toulouse" },
                         new String[] { "Cat1", "Lund" },
                         new String[] { "Cat1", "Toulouse" },
                         new String[] { "CatB", null },
                         new String[] { "CatA", null },
                         new String[] { "Cat001 link with Cat002", null },
                         new String[] { "Cat002 link with Cat001", null },
                         new String[] { "Cat003", null } };

    private final static String[][] SEVERAL_PRODUCT_FIELDS_RESULT =
        new String[][] { new String[] { null, null },
                         new String[] { null, null },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Lund" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", "Toulouse" },
                         new String[] { "Toulouse", null },
                         new String[] { "Lyon", null },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Lund" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", "Toulouse" },
                         new String[] { "Lund", null },
                         new String[] { "location", null },
                         new String[] { "location", null },
                         new String[] { "location", null } };
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test sorted fields that are not display.
 * <p>
 * For all tests, there is one multi-containers filter with one result field
 * (displayed) and one sorted field (not displayed).
 * </p>
 * 
 * @author mkargbo
 */
public class TestSortFilterService extends AbstractBusinessServiceTestCase {
    private SearchService searchService;

    private FilterVisibilityConstraintData filterVisibilityConstraint;

    private FilterQueryConfigurator filterQueryConfigurator;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        filterVisibilityConstraint =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        getProcessName(), StringUtils.EMPTY);
        filterQueryConfigurator = new FilterQueryConfigurator();
    }

    /**
     * Creates a multi-container sheet's filter with:
     * <ul>
     * <li>1 result's field (display)</li>
     * <li>1 sorted field (not display)</li>
     * </ul>
     * 
     * @param pType1
     *            Sheet type name
     * @param pType2
     *            Sheet type name
     * @param pDisplayField
     *            Name of the result field
     * @param pSortedField
     *            Name of the sorted field
     * @param pOrder
     *            Order of the sorted field
     * @return Created filter
     */
    private ExecutableFilterData createFilter(final String pType1,
            final String pType2, final String pDisplayField,
            final String pSortedField, final String pOrder) {

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), pType1, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + pType1 + " not found.", lSheetTypeId);
        CacheableSheetType lType2 =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), pType2, CacheProperties.IMMUTABLE);
        String lSheetTypeId2 = lType2.getId();

        Collection<String> lTypeId = new HashSet<String>();
        lTypeId.add(lSheetTypeId);
        lTypeId.add(lSheetTypeId2);
        UsableFieldData lDisplayedUsableField =
                searchService.getUsableField(getProcessName(), pDisplayField,
                        lTypeId);
        UsableFieldData lSortedUsableField =
                searchService.getUsableField(getProcessName(), pSortedField,
                        lTypeId);
        ResultSummaryData lResultSummary = new ResultSummaryData();
        lResultSummary.setFilterVisibilityConstraintData(filterVisibilityConstraint);
        lResultSummary.setUsableFieldDatas(new UsableFieldData[] { lDisplayedUsableField });

        SortingFieldData lSortingField = new SortingFieldData();
        lSortingField.setOrder(pOrder);
        lSortingField.setUsableFieldData(lSortedUsableField);
        ResultSortingData lResultSorting = new ResultSortingData();
        lResultSorting.setFilterVisibilityConstraintData(filterVisibilityConstraint);
        lResultSorting.setSortingFieldDatas(new SortingFieldData[] { lSortingField });

        ExecutableFilterData lExecutableFilter = new ExecutableFilterData();
        lExecutableFilter.setFilterVisibilityConstraintData(filterVisibilityConstraint);
        lExecutableFilter.setResultSummaryData(lResultSummary);
        lExecutableFilter.setResultSortingData(lResultSorting);
        FilterData lFilter = new FilterData();
        lFilter.setType(FilterTypeData.SHEET);
        lFilter.setFieldsContainerIds(lTypeId.toArray(new String[2]));
        lExecutableFilter.setFilterData(lFilter);
        return lExecutableFilter;
    }

    //Expected 14 because the last one 'testLargeString' is in 'store1' product and user2 has no rights on it.
    private static final int EXPECTED_RESULTS_STATE_TYPE = 14;

    /** the sheet types to filter */
    protected static final String[] SHEET_TYPES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_PRICE };

    /**
     * Execute a filter with
     * <ul>
     * <li>$SHEET_REFERENCE as result field</li>
     * <li>$SHEET_STATE as sorted field</li>
     * </ul>
     */
    public void testStateVirtualField() {
        ExecutableFilterData lFilter =
                createFilter(SHEET_TYPES[0], SHEET_TYPES[1],
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        VirtualFieldType.$SHEET_STATE.getValue(), Operators.ASC);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        assertEquals(EXPECTED_RESULTS_STATE_TYPE,
                IteratorUtils.toList(lResults).size());
    }

    /**
     * Execute a filter with
     * <ul>
     * <li>$SHEET_REFERENCE as result field</li>
     * <li>$SHEET_TYPE as sorted field</li>
     * </ul>
     */
    public void testTypeVirtualField() {
        ExecutableFilterData lFilter =
                createFilter(SHEET_TYPES[0], SHEET_TYPES[1],
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        VirtualFieldType.$SHEET_TYPE.getValue(), Operators.ASC);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        assertEquals(EXPECTED_RESULTS_STATE_TYPE,
                IteratorUtils.toList(lResults).size());
    }

    /**
     * Execute a filter with
     * <ul>
     * <li>$SHEET_TYPE as result field</li>
     * <li>$SHEET_REFERENCE as sorted field</li>
     * </ul>
     */
    public void testReferenceVirtualField() {
        ExecutableFilterData lFilter =
                createFilter(SHEET_TYPES[0], SHEET_TYPES[1],
                        VirtualFieldType.$SHEET_TYPE.getValue(),
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        Operators.ASC);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        assertEquals(EXPECTED_RESULTS_STATE_TYPE,
                IteratorUtils.toList(lResults).size());
    }

    /**
     * Execute a filter with
     * <ul>
     * <li>$SHEET_REFERENCE as result field</li>
     * <li>$PRODUCT_NAME as sorted field</li>
     * </ul>
     */
    public void testProductNameVirtualField() {
        ExecutableFilterData lFilter =
                createFilter(SHEET_TYPES[0], SHEET_TYPES[1],
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        VirtualFieldType.$PRODUCT_NAME.getValue(),
                        Operators.ASC);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        assertEquals(EXPECTED_RESULTS_STATE_TYPE,
                IteratorUtils.toList(lResults).size());
    }

    private static final String TYPE_01 = GpmTestValues.SHEET_TYPE_CONTROL_TYPE;

    private static final String TYPE_02 =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    private static final String SORTED_FIELD_NAME = "Field2";

    private static final int EXPECTED_RESULTS_STRING = 4;

    /**
     * Test filter's execution with a string field setting as sorted field.
     */
    public void testStringField() {
        ExecutableFilterData lFilter =
                createFilter(TYPE_01, TYPE_02,
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        SORTED_FIELD_NAME, Operators.ASC);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        assertEquals(EXPECTED_RESULTS_STRING,
                IteratorUtils.toList(lResults).size());
    }
}
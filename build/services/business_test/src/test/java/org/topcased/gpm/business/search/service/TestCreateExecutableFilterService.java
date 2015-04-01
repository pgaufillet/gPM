/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createExecutableFilter<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestCreateExecutableFilterService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    /** the filter name */
    private static final String FILTER_NAME = "my_filter";

    /** the filter description */
    private static final String FILTER_DESCR =
            "this is a filter used to test the method createExecutableFilter";

    /** the criteria suffix */
    private static final String CRITERIA_SUFFIX = "_CRIT";

    /** the sorting suffix */
    private static final String SORTING_SUFFIX = "_SORTER";

    /** the summary suffix */
    private static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the sheet type to filter */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** the user login for filter constraint : all users */
    private static final String ALL_USERS = null;

    /** the product name for filter constraint : all products */
    private static final String ALL_PRODUCTS = null;

    /** the properties of the first criterion field */
    private static final String[] FIRST_CRITERIA_FIELD =
            { "CAT_description", "Pet description",
             FieldTypes.SIMPLE_STRING_FIELD };

    /** the properties of the second criterion field */
    private static final String[] SECOND_CRITERIA_FIELD =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR,
             FieldTypes.CHOICE_FIELD };

    /** the value for the first criterion */
    private static final String VALUE_FOR_FIRST_CRITERIA = "%BLACK%";

    /** the value for the first criterion */
    private static final String VALUE_FOR_SECOND_CRITERIA = "%cat%";

    /** result sorting data */
    private static final String[] FIRST_RESULT_SORTING_DATA =
            { "CAT_ref", "Ref", FieldTypes.SIMPLE_STRING_FIELD, Operators.ASC };

    /** the properties of the result fields */
    private static final String[] THIRD_RESULT_FIELD =
            { "CAT_furlength", "Fur Length" };

    /*
     * VIRTUAL FIELDS IN CRITERIA :
     *
     * - a product field
     * - the sheet state
     * - a field from a liked sheet
     * - a field from a link
     * */

    /** the properties for the first virtual field criterion */
    private static final String[] FIRST_VIRTUAL_CRITERIA_FIELD =
            { "product_location", "Product.Location",
             FieldTypes.SIMPLE_STRING_FIELD, Operators.LIKE };

    private static final ScalarValueData[] VALUES_FOR_VIRTUAL_FIELDS =
            { new StringValueData("%"), new StringValueData("%"),
             new RealValueData(0.), new StringValueData("%") };

    /** the properties for the second virtual field criterion */
    private static final String[] SECOND_VIRTUAL_CRITERIA_FIELD =
            { VirtualFieldType.$SHEET_STATE.getValue(),
             VirtualFieldType.$SHEET_STATE.getValue(), FieldTypes.CHOICE_FIELD,
             Operators.LIKE };

    private static final String[] VALUES_FOR_VIRTUAL_2 =
            { "Temporary", "Open", "Closed", "NoAction", "Deleted" };

    /** the properties for the third virtual field criterion */
    private static final String FIELDS_CONTAINER_NAME_PRICE =
            GpmTestValues.SHEET_TYPE_PRICE;

    private static final String[] THIRD_VIRTUAL_CRITERIA_FIELD =
            { GpmTestValues.SHEETLINK_PRICE_PRICE, "Linked-Price.Price",
             FieldTypes.SIMPLE_REAL_FIELD, Operators.GT };

    /** the properties for the fourth virtual field criterion */
    private static final String FIELDS_CONTAINER_NAME_CAT_PRICE =
            GpmTestValues.SHEETLINK_CAT_PRICE;

    private static final String[] FOURTH_VIRTUAL_CRITERIA_FIELD =
            { "CAT_PRICE_author", "Link-Cat-Price.Justification.Author",
             FieldTypes.SIMPLE_STRING_FIELD, Operators.LIKE };

    private static final String[] VIEWER_ROLE_LOGIN =
            { GpmTestValues.USER_USER5, "pwd5" };

    private static final String VIEWER_ROLE = GpmTestValues.VIEWER_ROLE;

    private String viewerRoleToken;

    private static final String INSTANCE_FILE =
            "search/TestFilterAccessNonEditable.xml";

    private static final String CONFIDENTIAL_SHEET_TYPE =
            GpmTestValues.SHEET_TYPE_DOG;

    private static final String CONFIDENTIAL_FIELD =
            GpmTestValues.CATEGORY_CAT_PEDIGRE;

    /**
     * Set the viewer role token. {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    public void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();

        String lUserToken =
                authorizationService.login(VIEWER_ROLE_LOGIN[0],
                        VIEWER_ROLE_LOGIN[1]);
        viewerRoleToken =
                authorizationService.selectRole(lUserToken, VIEWER_ROLE,
                        getProductName(), getProcessName());
    }

    /**
     * Tests the method in a normal way.
     */
    public void testNormalCase() {
        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save

        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        String lFieldsContainerStoreId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, getProductTypeName());
        FilterFieldsContainerInfo lStoreFilterFieldsContainerInfo =
                new FilterFieldsContainerInfo();
        lStoreFilterFieldsContainerInfo.setId(lFieldsContainerStoreId);
        lStoreFilterFieldsContainerInfo.setLabelKey(getProductTypeName());
        lStoreFilterFieldsContainerInfo.setType(FieldsContainerType.PRODUCT);

        UsableFieldData lVirtual1 = new UsableFieldData();
        lVirtual1.setFieldName(FIRST_VIRTUAL_CRITERIA_FIELD[0]);
        lVirtual1.setFieldType(FieldType.valueOf(FIRST_VIRTUAL_CRITERIA_FIELD[2]));
        lVirtual1.setLevel(1);
        List<FilterFieldsContainerInfo> lFieldsContainerHierarchy =
                new ArrayList<FilterFieldsContainerInfo>(1);
        lFieldsContainerHierarchy.add(lStoreFilterFieldsContainerInfo);
        lVirtual1.setFieldsContainerHierarchy(lFieldsContainerHierarchy);

        VirtualFieldData lVirtual2 =
                (VirtualFieldData) searchService.getUsableField(
                        getProcessName(),
                        VirtualFieldType.$SHEET_STATE.getValue(),
                        Arrays.asList(lFieldsContainerIds));
        lVirtual2.setPossibleValues(Arrays.asList(VALUES_FOR_VIRTUAL_2));
        lVirtual2.setLevel(1);

        //Fields container identifiers
        String lFieldsContainerCatPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT_PRICE);
        String lFieldsContainerPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_PRICE);

        FilterFieldsContainerInfo lPriceFilterFieldsContainerInfo =
                new FilterFieldsContainerInfo();
        lPriceFilterFieldsContainerInfo.setId(lFieldsContainerPriceId);
        lPriceFilterFieldsContainerInfo.setLabelKey(FIELDS_CONTAINER_NAME_PRICE);
        lPriceFilterFieldsContainerInfo.setType(FieldsContainerType.SHEET);

        FilterFieldsContainerInfo lCatPriceFilterFieldsContainerInfo =
                new FilterFieldsContainerInfo();
        lCatPriceFilterFieldsContainerInfo.setId(lFieldsContainerCatPriceId);
        lCatPriceFilterFieldsContainerInfo.setLabelKey(FIELDS_CONTAINER_NAME_CAT_PRICE);
        lCatPriceFilterFieldsContainerInfo.setType(FieldsContainerType.LINK);
        lCatPriceFilterFieldsContainerInfo.setLinkDirection(LinkDirection.DESTINATION);

        //The linked sheet
        UsableFieldData lVirtual3 = new UsableFieldData();
        lVirtual3.setFieldName(THIRD_VIRTUAL_CRITERIA_FIELD[0]);
        lVirtual3.setFieldType(FieldType.valueOf(THIRD_VIRTUAL_CRITERIA_FIELD[2]));
        lVirtual3.setLevel(1);
        lFieldsContainerHierarchy = new ArrayList<FilterFieldsContainerInfo>(2);
        lFieldsContainerHierarchy.add(lCatPriceFilterFieldsContainerInfo);
        lFieldsContainerHierarchy.add(lPriceFilterFieldsContainerInfo);
        lVirtual3.setFieldsContainerHierarchy(lFieldsContainerHierarchy);

        //The link
        UsableFieldData lVirtual4 = new UsableFieldData();
        lVirtual4.setFieldName(FOURTH_VIRTUAL_CRITERIA_FIELD[0]);
        lVirtual4.setFieldType(FieldType.valueOf(FOURTH_VIRTUAL_CRITERIA_FIELD[2]));
        lVirtual4.setLevel(1);
        lFieldsContainerHierarchy = new ArrayList<FilterFieldsContainerInfo>(1);
        lFieldsContainerHierarchy.add(lCatPriceFilterFieldsContainerInfo);
        lVirtual4.setFieldsContainerHierarchy(lFieldsContainerHierarchy);

        // Create the 6 criteria
        CriteriaData[] lCrits = new CriteriaData[6];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUchoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        lUstring);
        lCrits[2] =
                new CriteriaFieldData(FIRST_VIRTUAL_CRITERIA_FIELD[3],
                        Boolean.TRUE, VALUES_FOR_VIRTUAL_FIELDS[0], lVirtual1);
        lCrits[3] =
                new CriteriaFieldData(SECOND_VIRTUAL_CRITERIA_FIELD[3],
                        Boolean.TRUE, VALUES_FOR_VIRTUAL_FIELDS[1], lVirtual2);
        lCrits[4] =
                new CriteriaFieldData(THIRD_VIRTUAL_CRITERIA_FIELD[3],
                        Boolean.TRUE, VALUES_FOR_VIRTUAL_FIELDS[2], lVirtual3);
        lCrits[5] =
                new CriteriaFieldData(FOURTH_VIRTUAL_CRITERIA_FIELD[3],
                        Boolean.TRUE, VALUES_FOR_VIRTUAL_FIELDS[3], lVirtual4);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 3;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUchoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(lUstring);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 4;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_RESULT_SORTING_DATA[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUsableFieldDatas[2] = new UsableFieldData();
        lUsableFieldDatas[2].setFieldName(THIRD_RESULT_FIELD[0]);
        lUsableFieldDatas[lSize - 1] = new UsableFieldData();
        lUsableFieldDatas[lSize - 1].setFieldName(FIRST_CRITERIA_FIELD[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        // Create the executable filter in the database
        String lId =
                searchService.createExecutableFilter(adminRoleToken,
                        lExecutableFilterData);

        assertNotNull("createExecutableFilter returns a null Id", lId);

    }

    /**
     * Tests the metho with an incorrect id on executable filter
     */
    public void testWithIncorrectExecutableFilterDataIdCase() {
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save

        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUchoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 3;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUchoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(lUstring);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 4;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_RESULT_SORTING_DATA[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUsableFieldDatas[2] = new UsableFieldData();
        lUsableFieldDatas[2].setFieldName(THIRD_RESULT_FIELD[0]);
        lUsableFieldDatas[lSize - 1] = new UsableFieldData();
        lUsableFieldDatas[lSize - 1].setFieldName(FIRST_CRITERIA_FIELD[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData("", FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        searchService.createExecutableFilter(adminRoleToken,
                lExecutableFilterData);
    }

    /**
     * Tests the method with an incorrect label key on executable filter
     */
    public void testWithIncorrectExecutableFilterDataLabelKeyCase() {
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save

        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUchoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 3;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUchoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(lUstring);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 4;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_RESULT_SORTING_DATA[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUsableFieldDatas[2] = new UsableFieldData();
        lUsableFieldDatas[2].setFieldName(THIRD_RESULT_FIELD[0]);
        lUsableFieldDatas[lSize - 1] = new UsableFieldData();
        lUsableFieldDatas[lSize - 1].setFieldName(FIRST_CRITERIA_FIELD[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, "", FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        try {
            searchService.createExecutableFilter(adminRoleToken,
                    lExecutableFilterData);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Tests to create an already existing filter
     */
    public void testCreateAlreadyExistingFilterCase() {
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save

        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUchoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 3;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUchoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(lUstring);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 4;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_RESULT_SORTING_DATA[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUsableFieldDatas[2] = new UsableFieldData();
        lUsableFieldDatas[2].setFieldName(THIRD_RESULT_FIELD[0]);
        lUsableFieldDatas[lSize - 1] = new UsableFieldData();
        lUsableFieldDatas[lSize - 1].setFieldName(FIRST_CRITERIA_FIELD[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        searchService.createExecutableFilter(adminRoleToken,
                lExecutableFilterData);

        try {
            searchService.createExecutableFilter(adminRoleToken,
                    lExecutableFilterData);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Test the creation filter when the user don't have access on visibilities
     */
    public void testWithConfidentialVisibility() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        // Create the criteria
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        CriteriaData[] lCrits = new CriteriaData[1];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, null);

        // Create the sorting field data
        final int lLength = 1;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas, null);

        // creation of the filter result field data
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[2];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(FIRST_RESULT_SORTING_DATA[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas, null);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        null, null);

        /**
         * PROCESS VISIBILITY
         */
        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);
        lExecutableFilterData.getFilterData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSortingData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSummaryData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);

        // Create the executable filter in the database
        try {
            searchService.createExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
        }
        catch (AuthorizationException e) {
            fail("The filter can not be create with 'process' visibility.");
        }

        /**
         * PRODUCT VISIBILITY
         */
        lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        getProductName());
        lExecutableFilterData.getFilterData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSortingData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSummaryData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);

        // Create the executable filter in the database
        try {
            searchService.createExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
        }
        catch (AuthorizationException e) {
            fail("The filter can not be create with 'product' visibility.");
        }

        /**
         * USER VISIBILITY
         */
        lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(USER2_LOGIN[0],
                        getProcessName(), ALL_PRODUCTS);
        lExecutableFilterData.getFilterData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSortingData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.getResultSummaryData().setFilterVisibilityConstraintData(
                lFilterVisibilityConstraintData);
        lExecutableFilterData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);

        // Create the executable filter in the database
        try {
            searchService.createExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
            fail("The filter can be create with 'user' visibility.");
        }
        catch (AuthorizationException e) {
            // Ok
        }
    }

    /**
     * Test the creation filter when the user don't have access on type
     */
    public void testWithConfidentialType() {
        // Instantiate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), CONFIDENTIAL_SHEET_TYPE,
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + CONFIDENTIAL_SHEET_TYPE + " not found.",
                lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the criteria
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        CriteriaData[] lCrits = new CriteriaData[1];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 1;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[2];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(FIRST_RESULT_SORTING_DATA[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        // Create the executable filter in the database
        try {
            searchService.createExecutableFilter(viewerRoleToken,
                    lExecutableFilterData);
            fail("The filter can be create with confidential type.");
        }
        catch (AuthorizationException e) {
            // Ok
        }
    }

    /**
     * Test the creation filter when the user don't have access on field
     */
    public void testWithConfidentialField() {
        // Instantiate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the criteria
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        CriteriaData[] lCrits = new CriteriaData[1];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 1;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[2];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(CONFIDENTIAL_FIELD);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(FIRST_RESULT_SORTING_DATA[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        // Create the executable filter in the database
        try {
            searchService.createExecutableFilter(viewerRoleToken,
                    lExecutableFilterData);
            fail("The filter can be create with confidential field.");
        }
        catch (AuthorizationException e) {
            // Ok
        }
    }
}

/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.ws.v2.client.BooleanValueData;
import org.topcased.gpm.ws.v2.client.CriteriaData;
import org.topcased.gpm.ws.v2.client.CriteriaFieldData;
import org.topcased.gpm.ws.v2.client.DateValueData;
import org.topcased.gpm.ws.v2.client.ExecutableFilterData;
import org.topcased.gpm.ws.v2.client.FieldSummaryData;
import org.topcased.gpm.ws.v2.client.FieldType;
import org.topcased.gpm.ws.v2.client.FieldsContainerType;
import org.topcased.gpm.ws.v2.client.FilterData;
import org.topcased.gpm.ws.v2.client.FilterExecutionReport;
import org.topcased.gpm.ws.v2.client.FilterFieldsContainerInfo;
import org.topcased.gpm.ws.v2.client.FilterProductScope;
import org.topcased.gpm.ws.v2.client.FilterResult;
import org.topcased.gpm.ws.v2.client.FilterScope;
import org.topcased.gpm.ws.v2.client.FilterTypeData;
import org.topcased.gpm.ws.v2.client.FilterVisibilityConstraintData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.IntegerValueData;
import org.topcased.gpm.ws.v2.client.LinkDirection;
import org.topcased.gpm.ws.v2.client.OperationData;
import org.topcased.gpm.ws.v2.client.RealValueData;
import org.topcased.gpm.ws.v2.client.ResultSortingData;
import org.topcased.gpm.ws.v2.client.ResultSummaryData;
import org.topcased.gpm.ws.v2.client.ScalarValueData;
import org.topcased.gpm.ws.v2.client.SheetType;
import org.topcased.gpm.ws.v2.client.SortingFieldData;
import org.topcased.gpm.ws.v2.client.StringValueData;
import org.topcased.gpm.ws.v2.client.SummaryData;
import org.topcased.gpm.ws.v2.client.UsableFieldData;
import org.topcased.gpm.ws.v2.client.UsableTypeData;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * Test filters.
 * 
 * @author nveillet
 */
public class TestWSFilters extends AbstractWSTestCase {
    private static final String SHEET_TYPE_CAT = "Cat";

    private static final String SHEET_TYPE_DOG = "Dog";

    private static final int FILTER_NUMBER = 5;

    private static final Set<String> FILTER_NAMES;

    private static final int FILTER_NUMBER_INSTANCE = 3;

    private static final Set<String> FILTER_NAMES_INSTANCE;

    private static final int FILTER_NUMBER_PRODUCT = 1;

    private static final Set<String> FILTER_NAMES_PRODUCT;

    private static final int FILTER_NUMBER_USER = 1;

    private static final Set<String> FILTER_NAMES_USER;

    private static final Set<String> EXPECTED_SEARCHABLE_FIELDS_CAT;

    private static final Set<String> EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG;

    private static final Set<String> EXPECTED_EXECUTABLE_SCOPES;

    private static final Set<String> EXPECTED_EDITABLE_SCOPES;

    private static final List<String> EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN;

    private static final String[] EXPECTED_SUMMARY_LABELS;

    private static final String[][] EXPECTED_SUMMARY_VALUES;

    private static final String[] EXPECTED_SUMMARY_TYPES;

    private static final int EXPECTED_FILTER_MAX_DEPTH = 3;

    private static final String EXPECTED_HMI_FILTER_DESCRIPTION;

    private static final int EXPECTED_FILTER_STRING_MAX_LENGTH = 255;

    private static final XMLGregorianCalendar EXPECTED_FILTER_CRITERIA5_DATE;

    static {
        FILTER_NAMES = new HashSet<String>();
        FILTER_NAMES.add("OPENED_CATS");
        FILTER_NAMES.add("TEST_FILTER_1");
        FILTER_NAMES.add("TEST_FILTER_2");
        FILTER_NAMES.add("TEST_FILTER_WITH_SAME_NAME");

        FILTER_NAMES_INSTANCE = new HashSet<String>();
        FILTER_NAMES_INSTANCE.add("OPENED_CATS");
        FILTER_NAMES_INSTANCE.add("TEST_FILTER_2");
        FILTER_NAMES_INSTANCE.add("TEST_FILTER_WITH_SAME_NAME");

        FILTER_NAMES_PRODUCT = new HashSet<String>();
        FILTER_NAMES_PRODUCT.add("TEST_FILTER_1");

        FILTER_NAMES_USER = new HashSet<String>();
        FILTER_NAMES_USER.add("TEST_FILTER_WITH_SAME_NAME");

        EXPECTED_SEARCHABLE_FIELDS_CAT = new HashSet<String>();
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_picture");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_ref");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("__Reference");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_name");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_description");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_furlength");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_ishappy");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_color");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_birthdate");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("CAT_pedigre");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("$SHEET_TYPE");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("$SHEET_STATE");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("$SHEET_REFERENCE");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("$PRODUCT_NAME");
        EXPECTED_SEARCHABLE_FIELDS_CAT.add("$PRODUCT_HIERARCHY");

        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG = new HashSet<String>();
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("__Reference");
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("$SHEET_TYPE");
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("$SHEET_STATE");
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("$SHEET_REFERENCE");
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("$PRODUCT_NAME");
        EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.add("$PRODUCT_HIERARCHY");

        EXPECTED_EXECUTABLE_SCOPES = new HashSet<String>();
        EXPECTED_EXECUTABLE_SCOPES.add("INSTANCE_FILTER");
        EXPECTED_EXECUTABLE_SCOPES.add("PRODUCT_FILTER");
        EXPECTED_EXECUTABLE_SCOPES.add("USER_FILTER");

        EXPECTED_EDITABLE_SCOPES = new HashSet<String>();
        EXPECTED_EDITABLE_SCOPES.add("INSTANCE_FILTER");
        EXPECTED_EDITABLE_SCOPES.add("PRODUCT_FILTER");
        EXPECTED_EDITABLE_SCOPES.add("USER_FILTER");

        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN = new ArrayList<String>();
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("environment test store");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("substore");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("product1");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("product1_1");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("product2");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("product1_2");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("product3");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("store1");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("store1_1");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("productWithNoUsers");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("store2");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("Happy Mouse");
        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.add("Bernard's store");

        EXPECTED_SUMMARY_LABELS =
                new String[] { "$PRODUCT_NAME", "$SHEET_REFERENCE",
                              "CAT_description" };

        EXPECTED_SUMMARY_TYPES = new String[] { "CHOICE", "STRING", "STRING" };

        EXPECTED_HMI_FILTER_DESCRIPTION =
                "\n\t\t\t\tFilter using criteria and result fields on several\n"
                        + "\t\t\t\tlevel.\n"
                        + "\t\t\t\tOn 'SheetWithSomeConfidentialFields' fields container.\n"
                        + "\t\t\t\tWith\n"
                        + "\t\t\t\t'store1', 'store1_1' and 'store2' as product scope.\n"
                        + "\t\t\t\tCriteria:\n"
                        + "\t\t\t\t'SheetWithSomeConfidentialFields_Field1',\n"
                        + "\t\t\t\t'SheetLinkWithSomeConfidentialFields|"
                        + "SheetLinkWithSomeConfidentialFields_Field3',\n"
                        + "\t\t\t\t'SheetLinkWithSomeConfidentialFields|"
                        + "SheetMultipleWithSomeConfidentialFields|"
                        + "SheetLinkWithSomeConfidentialFields|"
                        + "SheetLinkWithSomeConfidentialFields_Field3',\n"
                        + "\t\t\t\t'SheetLinkWithSomeConfidentialFields|"
                        + "SheetMultipleWithSomeConfidentialFields|"
                        + "SheetMultipleWithSomeConfidentialFields_multiple1_Field1',\n"
                        + "\t\t\t\t'SheetLinkWithSomeConfidentialFields|"
                        + "SheetMultipleWithSomeConfidentialFields|$SHEET_STATE',\n"
                        + "\t\t\t\t'SheetLinkWithSomeConfidentialFields|"
                        + "SheetMultipleWithSomeConfidentialFields|"
                        + "SheetLinkWithSomeConfidentialFields|"
                        + "SheetWithSomeConfidentialFields|"
                        + "SheetWithSomeConfidentialFields_Field3'\n\n"
                        + "\t\t\t\tResult field (for the moment):\n"
                        + "\t\t\t\t'SheetWithSomeConfidentialFields_ref'\n\t\t\t";

        // To avoid checkstyle warning
        final int lYear = 2009;
        final int lMonth = 4;
        final int lDay = 4;
        EXPECTED_FILTER_CRITERIA5_DATE =
                XMLGregorianCalendarImpl.createDate(lYear, lMonth, lDay,
                        DatatypeConstants.FIELD_UNDEFINED);

        EXPECTED_SUMMARY_VALUES =
                new String[][] {
                                new String[] { "Happy Mouse", "Cat1",
                                              "Famous little cat." },
                                new String[] { "Happy Mouse", "Cat2",
                                              " no desc." },
                                new String[] { "Happy Mouse", "Cat3", null },
                                new String[] {
                                              "Happy Mouse",
                                              "Garfield",
                                              "Huge cat that sleeps 23 hour a day, "
                                                      + "and eats rest of the time" },
                                new String[] { "Happy Mouse", "Gros Minet",
                                              "Gentle famous little cat. Loves Titi." },
                                new String[] {
                                              "Happy Mouse",
                                              "testSheet",
                                              "previousDescription preCreateSheetLink "
                                                      + "postCreateSheetLink preCreateSheetLink "
                                                      + "postCreateSheetLink preCreateSheetLink "
                                                      + "postCreateSheetLink preCreateSheetLink "
                                                      + "postCreateSheetLink preDelete postDelete "
                                                      + "preDelete postDelete" },
                                new String[] { "Happy Mouse", "Tom",
                                              "Famous little cat." },
                                new String[] {
                                              "store1",
                                              "testLargeString",
                                              "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstu"
                                                      + "vwxyzabcdefghijklmnopqrstuvwxyzabcdefghij"
                                                      + "klmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"
                                                      + "z abcdefghijklmnopqrstuvwxyzabcdefghijklm"
                                                      + "nopqrstuvwxyzabcdefghijklmnopqrstuvwxyzab"
                                                      + "cdefghijklmnopqrstuvwxyzabcdefghijklmnopq"
                                                      + "rstuvwxyz abcdefghijklmnopqrstuvwxyzabcde"
                                                      + "fghijklmnopqrstuvwxyzabcdefghijklmno" } };

    }

    /**
     * getVisibleFilterNamesBySheetType for all scope
     */
    public void testGetVisibleFilterNamesBySheetTypeAllScope() {

        // get the filter list
        List<String> lFilterNames =
                staticServices.getVisibleFilterNamesBySheetType(roleToken,
                        SHEET_TYPE_CAT, null);

        assertNotNull(
                "The method getVisibleFilterNamesBySheetType no return filters.",
                lFilterNames);

        assertEquals("The method getVisibleFilterNamesBySheetType must return "
                + FILTER_NUMBER + " filter for the sheet type '"
                + SHEET_TYPE_CAT + "'.", FILTER_NUMBER, lFilterNames.size());

        // Control the filter list
        for (String lFilterName : lFilterNames) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has returned the filter '"
                            + lFilterName + "'.",
                    FILTER_NAMES.contains(lFilterName));
        }
        for (String lFilterName : FILTER_NAMES) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has not returned the filter '"
                            + lFilterName + "'.",
                    lFilterNames.contains(lFilterName));
        }
    }

    /**
     * getVisibleFilterNamesBySheetType for instance scope
     */
    public void testGetVisibleFilterNamesBySheetTypeInstanceScope() {

        // get the filter list
        List<String> lFilterNames =
                staticServices.getVisibleFilterNamesBySheetType(roleToken,
                        SHEET_TYPE_CAT, FilterScope.INSTANCE_FILTER);

        assertNotNull(
                "The method getVisibleFilterNamesBySheetType no return filters.",
                lFilterNames);

        assertEquals("The method getVisibleFilterNamesBySheetType must return "
                + FILTER_NUMBER_INSTANCE + " filter for the sheet type '"
                + SHEET_TYPE_CAT + "'.", FILTER_NUMBER_INSTANCE,
                lFilterNames.size());

        // Control the filter list
        for (String lFilterName : lFilterNames) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has returned the filter '"
                            + lFilterName + "'.",
                    FILTER_NAMES_INSTANCE.contains(lFilterName));
        }
        for (String lFilterName : FILTER_NAMES_INSTANCE) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has not returned the filter '"
                            + lFilterName + "'.",
                    lFilterNames.contains(lFilterName));
        }
    }

    /**
     * getVisibleFilterNamesBySheetType for product scope
     */
    public void testGetVisibleFilterNamesBySheetTypeProductScope() {
        // get the filter list
        List<String> lFilterNames =
                staticServices.getVisibleFilterNamesBySheetType(roleToken,
                        SHEET_TYPE_CAT, FilterScope.PRODUCT_FILTER);

        assertNotNull(
                "The method getVisibleFilterNamesBySheetType no return filters.",
                lFilterNames);

        assertEquals("The method getVisibleFilterNamesBySheetType must return "
                + FILTER_NUMBER_PRODUCT + " filter for the sheet type '"
                + SHEET_TYPE_CAT + "'.", FILTER_NUMBER_PRODUCT,
                lFilterNames.size());

        // Control the filter list
        for (String lFilterName : lFilterNames) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has returned the filter '"
                            + lFilterName + "'.",
                    FILTER_NAMES_PRODUCT.contains(lFilterName));
        }
        for (String lFilterName : FILTER_NAMES_PRODUCT) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has not returned the filter '"
                            + lFilterName + "'.",
                    lFilterNames.contains(lFilterName));
        }
    }

    /**
     * getVisibleFilterNamesBySheetType for user scope
     */
    public void testGetVisibleFilterNamesBySheetTypeUserScope() {
        // get the filter list
        List<String> lFilterNames =
                staticServices.getVisibleFilterNamesBySheetType(roleToken,
                        SHEET_TYPE_CAT, FilterScope.USER_FILTER);

        assertNotNull(
                "The method getVisibleFilterNamesBySheetType no return filters.",
                lFilterNames);

        assertEquals("The method getVisibleFilterNamesBySheetType must return "
                + FILTER_NUMBER_USER + " filter for the sheet type '"
                + SHEET_TYPE_CAT + "'.", FILTER_NUMBER_USER,
                lFilterNames.size());

        // Control the filter list
        for (String lFilterName : lFilterNames) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has returned the filter '"
                            + lFilterName + "'.",
                    FILTER_NAMES_USER.contains(lFilterName));
        }
        for (String lFilterName : FILTER_NAMES_USER) {
            assertTrue(
                    "The method getVisibleFilterNamesBySheetType has not returned the filter '"
                            + lFilterName + "'.",
                    lFilterNames.contains(lFilterName));
        }
    }

    /**
     * getFilterFieldsMaxDepth
     */
    public void testGetFilterFieldsMaxDepth() {
        assertEquals("Check that the getFilterFieldsMaxDepth method returned ",
                EXPECTED_FILTER_MAX_DEPTH,
                staticServices.getFilterFieldsMaxDepth());
    }

    /**
     * getExecutableFilter
     * 
     * @throws GDMException_Exception
     *             if an error occurs
     */
    public void testGetExecutableFilter() throws GDMException_Exception {
        // Check for TEST_FILTER_1
        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_1");

        lFilter =
                staticServices.getExecutableFilter(roleToken, lFilter.getId());

        ExecutableFilterData lExpectedFilter = buildTestFilter1();

        assertFilterEquals("Check that TEST_FILTER_1 is correctly received:",
                lExpectedFilter, lFilter);
    }

    /**
     * getExecutableFilterByName
     * 
     * @throws GDMException_Exception
     *             if an error occurs
     */
    public void testGetExecutableFilterByName() throws GDMException_Exception {
        // Check for TEST_FILTER_1
        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_1");

        ExecutableFilterData lExpectedFilter = buildTestFilter1();

        assertFilterEquals("Check that TEST_FILTER_1 is correctly received:",
                lExpectedFilter, lFilter);

        // Check for Filter "HMI - FilterOnSeveralLevel"
        lExpectedFilter = buildExpectedHMIFilterOnSeveralLevel();

        lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), null, null,
                        "HMI - FilterOnSeveralLevel");

        assertFilterEquals(
                "Check that HMI - FilterOnSeveralLevel is correctly received:",
                lExpectedFilter, lFilter);
    }

    /**
     * createOrUpdateExecutableFilter
     * 
     * @throws GDMException_Exception
     *             if an error occurs
     */
    public void testCreateExecutableFilter() throws GDMException_Exception {

        // DELETE PREVIOUS FILTER IF EXISTS
        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");
        if (lFilter != null) {
            staticServices.deleteExecutableFilter(roleToken, lFilter.getId());
        }

        // SET INFOS OF THE FILTER AS TEST_FILTER_1
        lFilter = buildTestFilter1();

        // CHANGE THE FIELD PROPERTIES
        lFilter.setId(null); // Avoid update (creation of new filter)
        lFilter.setLabelKey("TEST_FILTER_CREATION");
        lFilter.getFilterData().setLabelKey("TEST_FILTER_CREATION");

        // MODIFY FILTER FIELDS (ADD COLOR FIELD)

        CriteriaFieldData lColorFieldCriteria = new CriteriaFieldData();
        lColorFieldCriteria.setOperator(Operators.EQ);
        lColorFieldCriteria.setCaseSensitive(false);
        StringValueData lColorValue = new StringValueData();
        lColorValue.setStringValue("black");
        lColorFieldCriteria.setScalarValueData(lColorValue);
        UsableFieldData lColorField =
                buildUsableFieldData("", EXPECTED_FILTER_STRING_MAX_LENGTH,
                        FieldType.SIMPLE_STRING_FIELD, null, -1, false, false,
                        new ArrayList<String>(), "CAT_color");
        lColorFieldCriteria.setUsableFieldData(lColorField);
        lFilter.getFilterData().setCriteriaData(lColorFieldCriteria);

        List<String> lPossibleColors = new ArrayList<String>();
        lPossibleColors.add("$NOT_SPECIFIED");
        lPossibleColors.add("WHITE");
        lPossibleColors.add("GREY");
        lPossibleColors.add("GREEN");
        lPossibleColors.add("YELLOW");
        lPossibleColors.add("RED");
        lPossibleColors.add("BLACK");

        lFilter.getResultSummaryData().getUsableFieldDatas().add(
                buildUsableFieldData("Color", -1, FieldType.CHOICE_FIELD, null,
                        0, false, true, lPossibleColors, "CAT_color"));
        lFilter.getResultSummaryData().setLabelKey("TEST_FILTER_CREATION");
        lFilter.getResultSortingData().setLabelKey("TEST_FILTER_CREATION");

        // CREATE NEW FILTER
        staticServices.createOrUpdateExecutableFilter(roleToken, lFilter);

        // GET ADDED FILTER AND CHECK FIELDS
        ExecutableFilterData lCreatedFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");

        lColorField.setCategoryName("Color"); // Add the category name calculated by services
        lColorField.setFieldSize(-1); // Set the field size calculated by services
        lColorField.setFieldType(FieldType.CHOICE_FIELD); // Set field type calculated by services
        lColorField.setLevel(0); // Set level calculated by services
        lColorField.setMultivalued(true); // Set multivalued calculated by services
        // Set the possible colors calculated with the field name by the services
        lColorField.getPossibleValues().addAll(lPossibleColors);

        // CHECK
        assertFilterEquals("Check that new Filter is as expected", lFilter,
                lCreatedFilter);
    }

    /**
     * createOrUpdateExecutableFilter
     * 
     * @throws GDMException_Exception
     *             if an error occurs
     */
    public void testUpdateExecutableFilter() throws GDMException_Exception {

        // Reset the "TEST_FILTER_CREATION" in database
        insertNewTestFilterCreation();

        // GET PREVIOUSLY CREATED FILTER
        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");

        // MODIFY FILTER FIELDS
        StringValueData lColorValue = new StringValueData();
        lColorValue.setStringValue("white");
        CriteriaFieldData lColorFieldCriteria = null;
        lColorFieldCriteria =
                (CriteriaFieldData) lFilter.getFilterData().getCriteriaData();

        lColorFieldCriteria.setScalarValueData(lColorValue);

        // UPDATE FILTER
        staticServices.createOrUpdateExecutableFilter(roleToken, lFilter);

        // GET UPDATED FILTER AND CHECK FIELDS
        ExecutableFilterData lUpdatedFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");
        assertFilterEquals("Check that new Filter is as expected", lFilter,
                lUpdatedFilter);
    }

    /**
     * executeFilter
     */
    public void testExecuteFilter() {
        ExecutableFilterData lExecutableFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_1");
        FilterResult lResult =
                staticServices.executeFilter(roleToken, getProcessName(),
                        getProductName(), DEFAULT_LOGIN[0], lExecutableFilter);
        assertNotNull("Check that execution result is not null",
                lResult.getFilterExecutionResult());

        // ASSERTS ON EXECUTION REPORT :

        FilterExecutionReport lReport = lResult.getExecutionReport();

        // Checks on non executable products
        assertEquals("Check that non executable products list is empty", 0,
                lReport.getNonExecutableProducts().size());

        // Checks on executable products
        assertEquals("Check executable product key #1", "admin",
                lReport.getExecutableProducts().getKeys().get(0));
        assertEquals("Check executable product key size", 1,
                lReport.getExecutableProducts().getKeys().size());
        ArrayList<String> lExecutableProductsForAdmin =
                (ArrayList<String>) lReport.getExecutableProducts().getValues().get(
                        0).getItem();

        assertTrue("Check executable product",
                CollectionUtils.isEqualCollection(
                        EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN,
                        lExecutableProductsForAdmin));

        assertEquals("Check that executable product list size for key #1",
                EXPECTED_EXECUTABLE_PRODUCTS_FOR_ADMIN.size(),
                lExecutableProductsForAdmin.size());

        // Checks on additionalConstraints
        UsableTypeData lType =
                lReport.getAdditionalConstraints().getKeys().get(0);
        assertEquals("Check full name in AdditionalConstraints type", "Cat",
                lType.getFullName());
        assertEquals("Check name in AdditionalConstraints type", "Cat",
                lType.getName());
        assertEquals("Check hierarchy size in AdditionalConstraints type", 0,
                lType.getHierarchy().size());
        assertEquals("Check linkDirection in AdditionalConstraints type",
                LinkDirection.UNDEFINED, lType.getLinkDirection());
        assertEquals("Check type in AdditionalConstraints type",
                FieldsContainerType.SHEET, lType.getType());

        assertEquals("Check that only one type is in AdditionalConstraints", 1,
                lReport.getAdditionalConstraints().getKeys().size());

        assertEquals(
                "Check only constraint role in AdditionalConstraints",
                "admin",
                lReport.getAdditionalConstraints().getValues().get(0).getKeys().get(
                        0));
        assertEquals(
                "Check only constraint constraint size in AdditionnalConstraints",
                0,
                lReport.getAdditionalConstraints().getValues().get(0).getValues().get(
                        0).getItem().size());

        // ASSERTS ON EXECUTION RESULT

        List<SummaryData> lFilterResult = lResult.getFilterExecutionResult();
        for (int i = 0; i < EXPECTED_SUMMARY_LABELS.length; i++) {
            for (int j = 0; j < EXPECTED_SUMMARY_VALUES[i].length; j++) {
                FieldSummaryData lSummaryData =
                        lFilterResult.get(i).getFieldSummaryDatas().get(j);
                assertEquals("Check summary result #" + i + " Field #" + j
                        + " Label", EXPECTED_SUMMARY_LABELS[j],
                        lSummaryData.getLabelKey());
                assertEquals(
                        "Check summary result #" + i + " Field #" + j + " Type",
                        EXPECTED_SUMMARY_TYPES[j],
                        lFilterResult.get(i).getFieldSummaryDatas().get(j).getType());
                // Comparison with startsWith for long chains
                if (EXPECTED_SUMMARY_VALUES[i][j] != null) { // non Null value
                    assertTrue("Check summary result #" + i + " Field #" + j
                            + " Value expect [" + lSummaryData.getValue()
                            + "] starts with [" + EXPECTED_SUMMARY_VALUES[i][j]
                            + "]", lSummaryData.getValue().startsWith(
                            EXPECTED_SUMMARY_VALUES[i][j]));
                }
                else { // Null value
                    assertNull("Check that summary result #" + i + " Field #"
                            + j + " is null", lSummaryData.getValue());
                }
            }
            assertEquals("Check number of fields for summary data #" + i,
                    EXPECTED_SUMMARY_VALUES[i].length,
                    lFilterResult.get(i).getFieldSummaryDatas().size());
            assertEquals("Check summary result #" + i + " Lock", null,
                    lFilterResult.get(i).getLock());
        }
        assertEquals("Check number of results (SummaryData)",
                EXPECTED_SUMMARY_VALUES.length, lFilterResult.size());
    }

    /**
     * deleteExecutableFilter
     */
    public void testDeleteExecutableFilter() {

        // Reset the "TEST_FILTER_CREATION" in database
        insertNewTestFilterCreation();

        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");
        if (lFilter != null) {
            staticServices.deleteExecutableFilter(roleToken, lFilter.getId());
        }
    }

    /**
     * getEditableFilterScope
     */
    public void testGetEditableFilterScope() {
        List<FilterScope> lScopes =
                staticServices.getEditableFilterScope(roleToken);
        for (FilterScope lScope : lScopes) {
            assertTrue("Check that scope is expected: " + lScope.name(),
                    EXPECTED_EDITABLE_SCOPES.contains(lScope.name()));
        }
        assertEquals("Check the number of found scopes",
                EXPECTED_EDITABLE_SCOPES.size(), lScopes.size());
    }

    /**
     * getExecutableFilterScope
     */
    public void testGetExecutableFilterScope() {
        List<FilterScope> lScopes =
                staticServices.getExecutableFilterScope(roleToken);
        for (FilterScope lScope : lScopes) {
            assertTrue("Check that scope is expected: " + lScope.name(),
                    EXPECTED_EXECUTABLE_SCOPES.contains(lScope.name()));
        }
        assertEquals("Check the number of found scopes",
                EXPECTED_EXECUTABLE_SCOPES.size(), lScopes.size());
    }

    /**
     * getSearcheableContainers
     * 
     * @throws GDMException_Exception
     *             If and error occurs
     */
    public void testGetSearcheableContainers() throws GDMException_Exception {
        Set<String> lExpectedContainers = new HashSet<String>();
        List<SheetType> lSheetTypes =
                staticServices.getSheetTypes(secondaryRoleToken,
                        getProcessName());
        for (SheetType lType : lSheetTypes) {
            lExpectedContainers.add(lType.getId());
        }

        // Get containers list from service
        List<String> lFoundContainers =
                staticServices.getSearcheableContainers(secondaryRoleToken,
                        FilterTypeData.SHEET);

        // Checks
        Iterator<String> lIte = lFoundContainers.iterator();
        String lConfidentialContainer = null;
        while (lIte.hasNext()) {
            String lContainer = lIte.next();
            if (!lExpectedContainers.contains(lContainer)) {
                // If container not expected
                // Only one sheet must be confidential, and not in "Expected"
                if (lConfidentialContainer == null) {
                    // Container was confidential => OK
                    lConfidentialContainer = lContainer;
                    lIte.remove();
                }
                else {
                    // Container was not found and is not supposed to be confidential => KO
                    fail("Found sheet was not in expected Sheets and was not supposed"
                            + " to be confidential :" + lContainer);
                }
            }
        }
        assertNotNull("Check that one confidential container was found",
                lConfidentialContainer);
        assertEquals(
                "Check that expected list has the same size as found list "
                        + ", minus 1 confidential container ID: "
                        + lConfidentialContainer, lExpectedContainers.size(),
                lFoundContainers.size());
    }

    /**
     * getSearcheableFieldsLabel
     * 
     * @throws GDMException_Exception
     *             if an error occurs
     */
    public void testGetSearcheableFieldsLabel() throws GDMException_Exception {
        // Test for a unique container
        List<String> lContainersIds = new ArrayList<String>();
        lContainersIds.add(staticServices.getSheetTypeWithAccessControls(
                roleToken, getProcessName(), getProductName(), null,
                SHEET_TYPE_CAT).getId());
        List<String> lSearchableLabels =
                staticServices.getSearcheableFieldsLabel(roleToken,
                        lContainersIds);
        for (String lField : lSearchableLabels) {
            assertTrue("Check that the field is expected: " + lField,
                    EXPECTED_SEARCHABLE_FIELDS_CAT.contains(lField));
        }
        assertEquals("Check the number of found fields",
                EXPECTED_SEARCHABLE_FIELDS_CAT.size(), lSearchableLabels.size());

        // Test for two containers
        lContainersIds.add(staticServices.getSheetTypeWithAccessControls(
                roleToken, getProcessName(), getProductName(), null,
                SHEET_TYPE_DOG).getId());

        lSearchableLabels =
                staticServices.getSearcheableFieldsLabel(roleToken,
                        lContainersIds);
        for (String lField : lSearchableLabels) {
            assertTrue("Check that the field is expected: " + lField,
                    EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.contains(lField));
        }
        assertEquals("Check the number of found fields",
                EXPECTED_SEARCHABLE_FIELDS_CAT_AND_DOG.size(),
                lSearchableLabels.size());
    }

    /**
     * Utility method to build a filter scope
     * 
     * @param pProductName
     *            the product name to define
     * @param pIncludeSubProduct
     *            the include sub product value
     * @return the builded ProductScope
     */
    private static FilterProductScope buildProductScope(String pProductName,
            boolean pIncludeSubProduct) {
        FilterProductScope lScope = new FilterProductScope();
        lScope.setIncludeSubProducts(pIncludeSubProduct);
        lScope.setProductName(pProductName);
        return lScope;
    }

    /**
     * Build a FilterVisibilityConstraint with given arguments
     * 
     * @param pProcess
     *            The process name
     * @param pProduct
     *            The product name
     * @param pLogin
     *            The user login
     * @return a FilterVisibilityConstraint object with given values
     */
    private static FilterVisibilityConstraintData buildFilterVisibilityConstraintData(
            String pProcess, String pProduct, String pLogin) {
        FilterVisibilityConstraintData lConstraint =
                new FilterVisibilityConstraintData();
        lConstraint.setBusinessProcessName(pProcess);
        lConstraint.setProductName(pProduct);
        lConstraint.setUserLogin(pLogin);
        return lConstraint;
    }

    /**
     * Check that a FilterProductScope is contained in given collection making
     * the corresponding JUnit assertions
     * 
     * @param pAssertPrefix
     *            Prefix of the potential error message
     * @param pExpectedScopes
     *            The scope
     * @param pTestedScopes
     *            The collection
     */
    private static void assertScopesEquals(String pAssertPrefix,
            Collection<FilterProductScope> pExpectedScopes,
            Collection<FilterProductScope> pTestedScopes) {
        Iterator<FilterProductScope> lIteTested = pTestedScopes.iterator();

        /** Filter wrapper for comparison */
        final class ScopeWrapper extends FilterProductScope {
            ScopeWrapper(FilterProductScope pScope) {
                productName = pScope.getProductName();
                includeSubProducts = pScope.isIncludeSubProducts();
            }

            public boolean equals(Object pO) {
                if (pO instanceof FilterProductScope) {
                    FilterProductScope lScope = (FilterProductScope) pO;

                    return lScope.getProductName().equals(productName)
                            && lScope.isIncludeSubProducts() == includeSubProducts;
                }
                return false;
            }

            public int hashCode() { // Implemented to avoid checkstyle warning
                return super.hashCode();
            }
        }
        String lMsg = null;
        while (lIteTested.hasNext() && lMsg == null) {
            ScopeWrapper lScope = new ScopeWrapper(lIteTested.next());
            assertTrue("Check that product " + lScope.getProductName()
                    + " is expected", pExpectedScopes.contains(lScope));
        }
        lMsg =
                buildDifferenceMessageIfNecessary("product scopes size",
                        pExpectedScopes.size(), pTestedScopes.size());
        if (lMsg != null) {
            fail(pAssertPrefix + lMsg);
        }
    }

    /**
     * Build a FilterData object
     * 
     * @param pCriteriaData
     *            associated criterias
     * @param pFilterVisibilityConstraintData
     *            visibility constraints
     * @param pIsFilterModel
     *            isFilterModel
     * @param pLabelKey
     *            label
     * @param pType
     *            type
     * @return the corresponding FilterData object
     */
    private static FilterData buildFilterData(CriteriaData pCriteriaData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            boolean pIsFilterModel, String pLabelKey, FilterTypeData pType) {
        FilterData lData = new FilterData();
        lData.setCriteriaData(pCriteriaData);
        lData.setFilterVisibilityConstraintData(pFilterVisibilityConstraintData);
        lData.setIsFilterModel(pIsFilterModel);
        lData.setLabelKey(pLabelKey);
        lData.setType(pType);
        return lData;
    }

    /**
     * Check if both filterData in arguments are the same
     * 
     * @param pExpectedFilterData
     *            first FilterData
     * @param pFilterData
     *            second FilterData
     * @return a message indicating the difference or <code>null</code> if no
     *         difference found
     */
    private String equalsFilterData(FilterData pExpectedFilterData,
            FilterData pFilterData) {
        String lTmp = null;
        String lReturn = null;

        if ((pExpectedFilterData == null && pFilterData != null)
                || (pExpectedFilterData != null && pFilterData == null)) {
            return buildDifferenceMessageIfNecessary("filter data",
                    pExpectedFilterData, pFilterData);
        }
        if (lReturn == null) {
            lTmp =
                    equalsCriteriaData(pExpectedFilterData.getCriteriaData(),
                            pFilterData.getCriteriaData());
            if (lTmp != null) {
                return lTmp;
            }
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("isFilterModel",
                            pExpectedFilterData.isIsFilterModel(),
                            pFilterData.isIsFilterModel());
        }

        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("FieldsContainerIds",
                            pExpectedFilterData.getFieldsContainerIds(),
                            pFilterData.getFieldsContainerIds());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("label key",
                            pExpectedFilterData.getLabelKey(),
                            pFilterData.getLabelKey());
        }
        if (lReturn == null) {
            lTmp =
                    equalsFilterVisibilityConstraint(
                            pExpectedFilterData.getFilterVisibilityConstraintData(),
                            pFilterData.getFilterVisibilityConstraintData());
            if (lTmp != null) {
                return "FilterData filter visiblity constraint: " + lTmp;
            }
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("filter data type",
                            pExpectedFilterData.getType(),
                            pFilterData.getType());
        }

        return lReturn;
    }

    /**
     * Check if CriteriaData in argument are the same. If it is an
     * OperationData, makes recursive calls
     * 
     * @param pExpectedCriteriaData
     *            expected criteria data
     * @param pCriteriaData
     *            tested criteria data
     * @return a message indicating the difference or <code>null</code> if no
     *         difference found
     */
    private String equalsCriteriaData(CriteriaData pExpectedCriteriaData,
            CriteriaData pCriteriaData) {
        String lReturn = null;
        if (pCriteriaData == null && pExpectedCriteriaData == null) {
            return null;
        }
        if ((pExpectedCriteriaData == null && pCriteriaData != null)
                || (pExpectedCriteriaData != null && pCriteriaData == null)) {
            lReturn =
                    buildDifferenceMessageIfNecessary("criteria data",
                            pExpectedCriteriaData, pCriteriaData);
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("criteria class",
                            pExpectedCriteriaData.getClass(),
                            pCriteriaData.getClass());
        }
        if (lReturn != null) { // Break the test now for Classcast exception
            return lReturn;
        }
        // if OperationData
        if (pExpectedCriteriaData instanceof OperationData) {
            OperationData lExpected = (OperationData) pExpectedCriteriaData;
            OperationData lTested = (OperationData) pCriteriaData;

            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary("operator",
                                lExpected.getOperator(), lTested.getOperator());
            }

            Iterator<CriteriaData> lIte1 =
                    lExpected.getCriteriaDatas().iterator();
            Iterator<CriteriaData> lIte2 =
                    lTested.getCriteriaDatas().iterator();
            int i = 0;
            while (lReturn == null && lIte1.hasNext() && lIte2.hasNext()) {
                String lTmp = equalsCriteriaData(lIte1.next(), lIte2.next());
                if (lTmp != null) {
                    return "OperationData.criteriaData[" + i
                            + "] is different: " + lTmp;
                }
                i++;
            }
            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary(
                                "CriteriaData list size",
                                lExpected.getCriteriaDatas().size(),
                                lTested.getCriteriaDatas().size());
            }
        }
        // if CriteriaFieldData
        else {
            CriteriaFieldData lExpected =
                    (CriteriaFieldData) pExpectedCriteriaData;
            CriteriaFieldData lTested = (CriteriaFieldData) pCriteriaData;

            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary("operator",
                                lExpected.getOperator(), lTested.getOperator());
            }
            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary(
                                "case sensitive value",
                                lExpected.isCaseSensitive(),
                                lTested.isCaseSensitive());
            }
            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary(
                                lExpected.getScalarValueData(),
                                lTested.getScalarValueData());
            }
            if (lReturn == null) {
                String lTmp =
                        equalsUsableFieldData(lExpected.getUsableFieldData(),
                                lTested.getUsableFieldData());
                if (lTmp != null) {
                    return "UsableFieldData was different: " + lTmp;
                }
            }
        }
        return lReturn;
    }

    /**
     * Builds a message explaining the difference of the arguments if they are
     * different, or returns <code>null</code>, using the java
     * <code>equals()</code> method on the objects
     * 
     * @param pExpected
     *            The expected value
     * @param pValue
     *            The tested value
     * @return The explaining message if different of <code>null</code> if
     *         arguments are equals
     */
    private static String buildDifferenceMessageIfNecessary(
            ScalarValueData pExpected, ScalarValueData pValue) {

        String lMsg = null;
        if (pExpected != null) {
            // Check class
            lMsg =
                    buildDifferenceMessageIfNecessary("ScalarValueClass",
                            pExpected.getClass(), pValue.getClass());
            if (lMsg == null && pExpected instanceof StringValueData) {
                lMsg =
                        buildDifferenceMessageIfNecessary("StringValueData",
                                ((StringValueData) pExpected).getStringValue(),
                                ((StringValueData) pValue).getStringValue());
            }
            if (lMsg != null && pExpected instanceof DateValueData) {
                lMsg =
                        buildDifferenceMessageIfNecessary("DateValueData",
                                ((DateValueData) pExpected).getDateValue(),
                                ((DateValueData) pValue).getDateValue());
            }
            if (lMsg != null && pExpected instanceof IntegerValueData) {
                lMsg =
                        buildDifferenceMessageIfNecessary(
                                "IntegerValueData",
                                ((IntegerValueData) pExpected).getIntegerValue(),
                                ((IntegerValueData) pValue).getIntegerValue());
            }
            if (lMsg != null && pExpected instanceof RealValueData) {
                lMsg =
                        buildDifferenceMessageIfNecessary("RealValueData",
                                ((RealValueData) pExpected).getRealValue(),
                                ((RealValueData) pValue).getRealValue());
            }
            if (lMsg != null && pExpected instanceof BooleanValueData) {
                lMsg =
                        buildDifferenceMessageIfNecessary(
                                "BooleanValueData",
                                ((BooleanValueData) pExpected).isBooleanValue(),
                                ((BooleanValueData) pValue).isBooleanValue());
            }
        }
        else if (pValue == null) {
            return null;
        }
        return lMsg;
    }

    /**
     * Check if UsableFieldData in argument are the same
     * 
     * @param pExpectedData
     *            expected usable field data
     * @param pData
     *            tested usable field data
     * @return a message indicating the difference or <code>null</code> if no
     *         difference found
     */
    private String equalsUsableFieldData(UsableFieldData pExpectedData,
            UsableFieldData pData) {
        if (pExpectedData == null && pData == null) {
            return null;
        }
        if ((pExpectedData == null && pData != null)
                || (pExpectedData != null && pData == null)) {
            return buildDifferenceMessageIfNecessary("usableFieldData",
                    pExpectedData, pData);
        }
        String lReturn =
                buildDifferenceMessageIfNecessary("category name",
                        pExpectedData.getCategoryName(),
                        pData.getCategoryName());
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("field name",
                            pExpectedData.getFieldName(), pData.getFieldName());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("field size",
                            pExpectedData.getFieldSize(), pData.getFieldSize());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("field type",
                            pExpectedData.getFieldType(), pData.getFieldType());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("label",
                            pExpectedData.getLabel(), pData.getLabel());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("level",
                            pExpectedData.getLevel(), pData.getLevel());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary(
                            "is multiple field multi value",
                            pExpectedData.isMultipleFieldMultivalued(),
                            pData.isMultipleFieldMultivalued());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("is multivalued",
                            pExpectedData.isMultivalued(),
                            pData.isMultivalued());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("possible values",
                            pExpectedData.getPossibleValues(),
                            pData.getPossibleValues());
        }
        Iterator<FilterFieldsContainerInfo> lIte1 =
                pExpectedData.getFieldsContainerHierarchy().iterator();
        Iterator<FilterFieldsContainerInfo> lIte2 =
                pData.getFieldsContainerHierarchy().iterator();
        int i = 0;
        while (lReturn == null && lIte1.hasNext() && lIte2.hasNext()) {
            String lMessage =
                    equalsFilterFieldsContainerInfo(lIte1.next(), lIte2.next());
            if (lMessage != null) {
                return "UsableFieldData.fieldsContainerHierarchy[" + i
                        + "] was different: " + lMessage;
            }
            i++;
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary(
                            "fields container hierarchy size",
                            pExpectedData.getFieldsContainerHierarchy().size(),
                            pData.getFieldsContainerHierarchy().size());
        }

        return lReturn;
    }

    /**
     * Check if FilterFieldsContainerInfo in argument are the same
     * 
     * @param pExpectedInfo
     *            first info
     * @param pInfo
     *            second info
     * @return a message indicating the difference or <code>null</code> if no
     *         difference found
     */
    private String equalsFilterFieldsContainerInfo(
            FilterFieldsContainerInfo pExpectedInfo,
            FilterFieldsContainerInfo pInfo) {
        if (pExpectedInfo == null && pInfo == null) {
            return null;
        }
        if ((pExpectedInfo == null && pInfo != null)
                || (pExpectedInfo != null && pInfo == null)) {
            return buildDifferenceMessageIfNecessary(
                    "filterFieldsContainerInfo", pExpectedInfo, pInfo);
        }
        String lReturn =
                buildDifferenceMessageIfNecessary("label key",
                        pExpectedInfo.getLabelKey(), pInfo.getLabelKey());
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("type",
                            pExpectedInfo.getType(), pInfo.getType());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("link direction",
                            pExpectedInfo.getLinkDirection(),
                            pInfo.getLinkDirection());
        }
        return lReturn;
    }

    /**
     * Builds a message explaining the difference of the arguments if they are
     * different, or returns <code>null</code>, using the java
     * <code>equals()</code> method on the objects
     * 
     * @param pPropertyName
     *            The name of the property in the message
     * @param pExpected
     *            The expected value
     * @param pValue
     *            The tested value
     * @return The explaining message if different of <code>null</code> if
     *         arguments are equals
     */
    private static String buildDifferenceMessageIfNecessary(
            String pPropertyName, Object pExpected, Object pValue) {

        if (pExpected != null) {
            if (pExpected.equals(pValue)) {
                return null;
            }
        }
        else if (pValue == null) {
            return null;
        }
        return "expected '" + pPropertyName + "' is [" + pExpected
                + "] but was [" + pValue + "]";
    }

    /**
     * Check if ResultSortingData in argument are the same
     * 
     * @param pExpectedData
     *            expected value
     * @param pData
     *            tested value
     * @return The explaining message if different of <code>null</code> if
     *         arguments are equals
     */
    private String equalsResultSortingData(ResultSortingData pExpectedData,
            ResultSortingData pData) {
        String lTmp = null;
        if (pExpectedData == null && pData == null) {
            return null;
        }
        if ((pExpectedData == null && pData != null)
                || (pExpectedData != null && pData == null)) {
            return buildDifferenceMessageIfNecessary("resultSortingData",
                    pExpectedData, pData);
        }
        String lReturn =
                buildDifferenceMessageIfNecessary("label",
                        pExpectedData.getLabelKey(), pData.getLabelKey());

        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("fields container ids",
                            pExpectedData.getFieldsContainerIds(),
                            pData.getFieldsContainerIds());
        }
        if (lReturn == null) {
            lTmp =
                    equalsFilterVisibilityConstraint(
                            pExpectedData.getFilterVisibilityConstraintData(),
                            pData.getFilterVisibilityConstraintData());
            if (lTmp != null) {
                return "ResultSortingData.filterVisiblityConstraint: " + lTmp;
            }
        }
        if (lReturn == null) {
            lTmp =
                    equalsSortingFieldData(
                            pExpectedData.getSortingFieldDatas(),
                            pData.getSortingFieldDatas());
            if (lTmp != null) {
                return "ResultSortingData.sortingFieldDatas: " + lTmp;
            }
        }
        return lReturn;
    }

    /**
     * Check if ResultSortingData in argument are the same
     * 
     * @param pExpectedData
     *            expected constraint
     * @param pTestedData
     *            tested constraint
     * @returnThe explaining message if different of <code>null</code> if
     *            arguments are equals
     */
    private String equalsFilterVisibilityConstraint(
            FilterVisibilityConstraintData pExpectedData,
            FilterVisibilityConstraintData pTestedData) {
        if (pExpectedData == null && pTestedData == null) {
            return null;
        }
        if ((pExpectedData == null && pTestedData != null)
                || (pExpectedData != null && pTestedData == null)) {
            return buildDifferenceMessageIfNecessary(
                    "filterVisibilityConstraint", pExpectedData, pTestedData);
        }
        String lReturn =
                buildDifferenceMessageIfNecessary("business process name",
                        pExpectedData.getBusinessProcessName(),
                        pTestedData.getBusinessProcessName());
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("product name",
                            pExpectedData.getProductName(),
                            pTestedData.getProductName());
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("user login",
                            pExpectedData.getUserLogin(),
                            pTestedData.getUserLogin());
        }
        return lReturn;
    }

    /**
     * Check if SortingFieldDatas in argument are the same
     * 
     * @param pExpectedData
     *            expected sorting data
     * @param pTestedData
     *            tested sorting data
     * @return explaining message if different of <code>null</code> if arguments
     *         are equals
     */
    private String equalsSortingFieldData(List<SortingFieldData> pExpectedData,
            List<SortingFieldData> pTestedData) {
        String lReturn = null;
        if (pExpectedData == null && pTestedData == null) {
            return null;
        }
        if ((pExpectedData == null && pTestedData != null)
                || (pExpectedData != null && pTestedData == null)) {
            return buildDifferenceMessageIfNecessary("sortingFieldData",
                    pExpectedData, pTestedData);
        }
        Iterator<SortingFieldData> lIteExpected = pExpectedData.iterator();
        Iterator<SortingFieldData> lIteTested = pTestedData.iterator();
        while (lReturn == null && lIteExpected.hasNext()
                && lIteTested.hasNext()) {
            SortingFieldData lExpected = lIteExpected.next();
            SortingFieldData lTested = lIteTested.next();

            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary(
                                "sorting field data order",
                                lExpected.getOrder(), lTested.getOrder());
            }
            if (lReturn == null) {
                String lTmp =
                        equalsUsableFieldData(lExpected.getUsableFieldData(),
                                lTested.getUsableFieldData());
                if (lTmp != null) {
                    return "SortingFieldData.usableFieldData: " + lTmp;
                }

            }
        }
        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary(
                            "Sorting field data size", pExpectedData.size(),
                            pTestedData.size());
        }
        return lReturn;
    }

    /**
     * Build a ResultSortingData object with values in arguments
     * 
     * @param pLabel
     *            The label
     * @param pContainerIds
     *            List of the containers Ids (can be null)
     * @param pConstraint
     *            A FilterVisilibilityConstraint
     * @return a ResultSortingData object with values in arguments
     */
    private static ResultSortingData buildResultSortingData(String pLabel,
            List<String> pContainerIds,
            FilterVisibilityConstraintData pConstraint) {
        ResultSortingData lSortingData = new ResultSortingData();
        lSortingData.setLabelKey(pLabel);
        lSortingData.setFilterVisibilityConstraintData(pConstraint);
        if (pContainerIds != null) {
            lSortingData.getFieldsContainerIds().addAll(pContainerIds);
        }
        return lSortingData;
    }

    /**
     * Build a SortingFieldData object with values in arguments
     * 
     * @param pOrder
     *            order
     * @param pUsableFieldValue
     *            usable field values
     * @return a SortingFieldData object with values in arguments
     */
    private SortingFieldData buildSortingFieldData(String pOrder,
            UsableFieldData pUsableFieldValue) {
        SortingFieldData lData = new SortingFieldData();
        lData.setOrder(pOrder);
        lData.setUsableFieldData(pUsableFieldValue);
        return lData;
    }

    /**
     * Build a UsableFieldData object with values in arguments
     * 
     * @param pCategoryName
     *            category name
     * @param pFieldSize
     *            field size
     * @param pFieldType
     *            field type
     * @param pFieldsContainerHierarchy
     *            container hierarchy
     * @param pLevel
     *            level
     * @param pMultipleFieldMultivalued
     *            multiple field multivalued
     * @param pMultivalued
     *            multivalued
     * @param pPossibleValues
     *            possible values
     * @param pFieldName
     *            field name
     * @return the build UsableFieldData
     */
    private static UsableFieldData buildUsableFieldData(String pCategoryName,
            int pFieldSize, FieldType pFieldType,
            List<FilterFieldsContainerInfo> pFieldsContainerHierarchy,
            int pLevel, Boolean pMultipleFieldMultivalued,
            Boolean pMultivalued, List<String> pPossibleValues,
            String pFieldName) {
        UsableFieldData lData = new UsableFieldData();
        lData.setFieldName(pFieldName);
        lData.setCategoryName(pCategoryName);
        lData.setFieldSize(pFieldSize);
        lData.setFieldType(pFieldType);
        if (pFieldsContainerHierarchy != null) {
            lData.getFieldsContainerHierarchy().addAll(
                    pFieldsContainerHierarchy);
        }
        lData.setLevel(pLevel);
        lData.setMultipleFieldMultivalued(pMultipleFieldMultivalued);
        lData.setMultivalued(pMultivalued);
        if (pPossibleValues != null) {
            lData.getPossibleValues().addAll(pPossibleValues);
        }
        return lData;
    }

    /**
     * Check if ResultSummaryData in argument are the same
     * 
     * @param pExpectedSummaryData
     *            expected summary data
     * @param pTestedSummaryData
     *            tested summary data
     * @return explaining message if different of <code>null</code> if arguments
     *         are equals
     */
    private String equalsResultSummaryData(
            ResultSummaryData pExpectedSummaryData,
            ResultSummaryData pTestedSummaryData) {
        String lReturn =
                buildDifferenceMessageIfNecessary("label",
                        pExpectedSummaryData.getLabelKey(),
                        pTestedSummaryData.getLabelKey());

        if (lReturn == null) {
            lReturn =
                    buildDifferenceMessageIfNecessary("fields container ids",
                            pExpectedSummaryData.getFieldsContainerIds(),
                            pTestedSummaryData.getFieldsContainerIds());
        }
        if (lReturn == null) {
            String lTmp =
                    equalsFilterVisibilityConstraint(
                            pExpectedSummaryData.getFilterVisibilityConstraintData(),
                            pTestedSummaryData.getFilterVisibilityConstraintData());
            if (lTmp != null) {
                return "ResultSummaryData.filterVisibilityConstraint: " + lTmp;
            }
        }
        if (lReturn == null) {
            Iterator<UsableFieldData> lIteExpected =
                    pExpectedSummaryData.getUsableFieldDatas().iterator();
            Iterator<UsableFieldData> lIteTested =
                    pTestedSummaryData.getUsableFieldDatas().iterator();
            String lTmp = null;
            int i = 0;
            while (lIteExpected.hasNext() && lIteTested.hasNext()) {
                lTmp =
                        equalsUsableFieldData(lIteExpected.next(),
                                lIteTested.next());
                if (lTmp != null) {
                    return "ResultSummaryData.usableFieldDatas[" + i + "]: "
                            + lTmp;
                }
                i++;
            }
            if (lReturn == null) {
                lReturn =
                        buildDifferenceMessageIfNecessary(
                                "UsableFieldData size",
                                pExpectedSummaryData.getUsableFieldDatas().size(),
                                pTestedSummaryData.getUsableFieldDatas().size());
            }
        }
        return lReturn;
    }

    /**
     * Check if ResultSummaryData in argument are the same, executing the
     * corresponding asserts
     * 
     * @param pAssertPrefix
     *            Prefix text for the assert messages
     * @param pExpectedFilter
     *            expected filter
     * @param pFilter
     *            tested filter
     */
    private void assertFilterEquals(String pAssertPrefix,
            ExecutableFilterData pExpectedFilter, ExecutableFilterData pFilter) {
        assertEquals(pAssertPrefix
                + " Check that description is correctly filled",
                pExpectedFilter.getDescription(), pFilter.getDescription());
        assertEquals(pAssertPrefix + " Check that label is correctly filled",
                pExpectedFilter.getLabelKey(), pFilter.getLabelKey());
        assertEquals(pAssertPrefix + " Check that usage is correctly filled",
                pExpectedFilter.getUsage(), pFilter.getUsage());

        assertScopesEquals(pAssertPrefix + "product scopes",
                pExpectedFilter.getFilterProductScopes(),
                pFilter.getFilterProductScopes());

        // Checks on FilterData
        String lCompareMessage =
                equalsFilterVisibilityConstraint(
                        pExpectedFilter.getFilterVisibilityConstraintData(),
                        pFilter.getFilterVisibilityConstraintData());
        if (lCompareMessage != null) {
            fail(pAssertPrefix
                    + " ExecutableFilterData.filterVisibilityConstraint: "
                    + lCompareMessage);
        }

        lCompareMessage =
                equalsFilterData(pExpectedFilter.getFilterData(),
                        pFilter.getFilterData());
        if (lCompareMessage != null) {
            fail(pAssertPrefix + " ExecutableFilterData.filterData: "
                    + lCompareMessage);
        }

        // Construction of ResultSortingData
        ResultSortingData lExpectedSortingData =
                pExpectedFilter.getResultSortingData();

        // Checks about ResultSortingData
        lCompareMessage =
                equalsResultSortingData(lExpectedSortingData,
                        pFilter.getResultSortingData());
        if (lCompareMessage != null) {
            fail(pAssertPrefix + " ResultSortingData: " + lCompareMessage);
        }

        // Construction of ResultSummaryData
        ResultSummaryData lExpectedSummaryData =
                pExpectedFilter.getResultSummaryData();
        ;
        // Checks about ResultSummaryData
        lCompareMessage =
                equalsResultSummaryData(lExpectedSummaryData,
                        pFilter.getResultSummaryData());
        if (lCompareMessage != null) {
            fail(pAssertPrefix + " ResultSummaryData: " + lCompareMessage);
        }

    }

    /**
     * Build the ExecutableFilterData corresponding to the TEST_FILTER_1
     * 
     * @return the ExecutableFilterData corresponding to the TEST_FILTER_1
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ExecutableFilterData buildTestFilter1()
        throws GDMException_Exception {
        ExecutableFilterData lFilter = new ExecutableFilterData();
        lFilter.setLabelKey("TEST_FILTER_1");
        lFilter.setUsage("TABLE_VIEW");

        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(),
                        getProductName(), null);
        lFilter.setFilterData(buildFilterData(null, lConstraint, false,
                "TEST_FILTER_1", FilterTypeData.SHEET));
        lFilter.setFilterVisibilityConstraintData(lConstraint);
        lFilter.setResultSortingData(buildTestFilter1ResultSortingData());
        lFilter.setResultSummaryData(buildFilter1ResultSummaryData());

        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null, "Cat").getId();
        lFilter.getFilterData().getFieldsContainerIds().add(lContainerId);

        staticServices.getSheetTypeWithAccessControls(roleToken,
                getProcessName(), getProductName(), null, "Cat").getId();

        return lFilter;
    }

    /**
     * Build the expected image from TEST_FILTER_1 ResultSummaryData in data of
     * test instance
     * 
     * @return The ResultSummaryData of TEST_FILTER_1
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ResultSummaryData buildFilter1ResultSummaryData()
        throws GDMException_Exception {
        ResultSummaryData lData = new ResultSummaryData();
        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(),
                        getProductName(), null);
        lData.setFilterVisibilityConstraintData(lConstraint);
        lData.setLabelKey("TEST_FILTER_1");
        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null, "Cat").getId();
        lData.getFieldsContainerIds().add(lContainerId);
        List<UsableFieldData> lUsableFieldDatas =
                new ArrayList<UsableFieldData>();
        List<String> lPossible1 = new ArrayList<String>();
        lPossible1.add("$CURRENT_PRODUCT");
        lPossible1.add("$NOT_SPECIFIED");
        lUsableFieldDatas.add(buildUsableFieldData("", -1,
                FieldType.CHOICE_FIELD, null, 0, false, false, lPossible1,
                "$PRODUCT_NAME"));
        lUsableFieldDatas.add(buildUsableFieldData("", -1,
                FieldType.SIMPLE_STRING_FIELD, null, 0, false, false, null,
                "$SHEET_REFERENCE"));
        lUsableFieldDatas.add(buildUsableFieldData(null, -1,
                FieldType.SIMPLE_STRING_FIELD, null, 0, false, false, null,
                "CAT_description"));
        lData.getUsableFieldDatas().addAll(lUsableFieldDatas);
        return lData;
    }

    /**
     * Build the expected image from TEST_FILTER_1 ResultSortingData in data of
     * test instance
     * 
     * @return The ResultSortingData of the TEST_FILTER_1
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ResultSortingData buildTestFilter1ResultSortingData()
        throws GDMException_Exception {
        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(),
                        getProductName(), null);
        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null, "Cat").getId();
        ResultSortingData lExpectedSortingData =
                buildResultSortingData("TEST_FILTER_1", null, lConstraint);
        lExpectedSortingData.getFieldsContainerIds().add(lContainerId);
        lExpectedSortingData.setFilterVisibilityConstraintData(lConstraint);
        List<String> lPossibleValues = new ArrayList<String>();
        lPossibleValues.add("$CURRENT_PRODUCT");
        lPossibleValues.add("$NOT_SPECIFIED");
        UsableFieldData lUsableFieldData1 =
                buildUsableFieldData("", -1, FieldType.CHOICE_FIELD, null, 0,
                        false, false, lPossibleValues, "$PRODUCT_NAME");
        lExpectedSortingData.getSortingFieldDatas().add(
                buildSortingFieldData("asc", lUsableFieldData1));
        UsableFieldData lUsableFieldData2 =
                buildUsableFieldData("", -1, FieldType.SIMPLE_STRING_FIELD,
                        null, 0, false, false, null, "$SHEET_REFERENCE");
        lExpectedSortingData.getSortingFieldDatas().add(
                buildSortingFieldData("asc", lUsableFieldData2));
        return lExpectedSortingData;
    }

    /**
     * Build the ExecutableFilterData corresponding to the Filter
     * "HMI - FilterOnSeveralLevel"
     * 
     * @return the ExecutableFilterData corresponding to the Filter
     *         "HMI - FilterOnSeveralLevel"
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ExecutableFilterData buildExpectedHMIFilterOnSeveralLevel()
        throws GDMException_Exception {

        ExecutableFilterData lFilter = new ExecutableFilterData();
        lFilter.setLabelKey("HMI - FilterOnSeveralLevel");
        lFilter.setUsage("BOTH_VIEWS");
        lFilter.setDescription(EXPECTED_HMI_FILTER_DESCRIPTION);

        lFilter.getFilterProductScopes().add(buildProductScope("store1", false));
        lFilter.getFilterProductScopes().add(
                buildProductScope("store1_1", false));
        lFilter.getFilterProductScopes().add(buildProductScope("store2", false));

        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(), null,
                        null);
        lFilter.setFilterData(buildFilterData(
                buildExpectedHMIFilterOnSeveralLevelCriteriaData(),
                lConstraint, false, "HMI - FilterOnSeveralLevel",
                FilterTypeData.SHEET));
        lFilter.setFilterVisibilityConstraintData(lConstraint);
        lFilter.setResultSortingData(buildExpectedHMIFilterOnSeveralLevelResultSortingData());
        lFilter.setResultSummaryData(buildExpectedHMIFilterOnSeveralLevelResultSummaryData());

        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null,
                        "SheetWithSomeConfidentialFields").getId();
        lFilter.getFilterData().getFieldsContainerIds().add(lContainerId);

        return lFilter;
    }

    /**
     * Build the expected image from Filter "HMI - FilterOnSeveralLevelResult"
     * CriteriaData in data of test instance Filter using criteria and result
     * fields on several level.
     * 
     * @return The CriteriaData of Filter "HMI - FilterOnSeveralLevelResult"
     */
    CriteriaData buildExpectedHMIFilterOnSeveralLevelCriteriaData() {
        OperationData lData = new OperationData();

        // Criteria 0
        CriteriaFieldData lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        StringValueData lStringValue = new StringValueData();
        lStringValue.setStringValue("value1");
        lCrit.setScalarValueData(lStringValue);
        lCrit.setUsableFieldData(buildUsableFieldData(null,
                EXPECTED_FILTER_STRING_MAX_LENGTH,
                FieldType.SIMPLE_STRING_FIELD, null, 0, false, false, null,
                "SheetWithSomeConfidentialFields_Field1"));
        lData.getCriteriaDatas().add(lCrit);

        // Criteria 1
        lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        IntegerValueData lIntegerValue = new IntegerValueData();
        lIntegerValue.setIntegerValue(0);
        lCrit.setScalarValueData(lIntegerValue);
        List<FilterFieldsContainerInfo> lContainers =
                new ArrayList<FilterFieldsContainerInfo>();
        FilterFieldsContainerInfo lContainerInfos =
                new FilterFieldsContainerInfo();
        lContainerInfos.setLabelKey("SheetLinkWithSomeConfidentialFields");
        lContainerInfos.setLinkDirection(LinkDirection.DESTINATION);
        lContainerInfos.setType(FieldsContainerType.LINK);
        lContainers.add(lContainerInfos);
        lCrit.setUsableFieldData(buildUsableFieldData(null, -1,
                FieldType.SIMPLE_INTEGER_FIELD, lContainers, 0, false, false,
                null, "SheetLinkWithSomeConfidentialFields_Field3"));
        lData.getCriteriaDatas().add(lCrit);

        // Criteria 2
        lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        lIntegerValue = new IntegerValueData();
        lIntegerValue.setIntegerValue(1);
        lCrit.setScalarValueData(lIntegerValue);
        lContainers = new ArrayList<FilterFieldsContainerInfo>(lContainers);
        lContainerInfos = new FilterFieldsContainerInfo();
        lContainerInfos.setLabelKey("SheetMultipleWithSomeConfidentialFields");
        lContainerInfos.setLinkDirection(LinkDirection.UNDEFINED);
        lContainerInfos.setType(FieldsContainerType.SHEET);
        lContainers.add(lContainerInfos);
        lContainerInfos = new FilterFieldsContainerInfo();
        lContainerInfos.setLabelKey("SheetLinkWithSomeConfidentialFields");
        lContainerInfos.setLinkDirection(LinkDirection.ORIGIN);
        lContainerInfos.setType(FieldsContainerType.LINK);
        lContainers.add(lContainerInfos);
        lCrit.setUsableFieldData(buildUsableFieldData(null, -1,
                FieldType.SIMPLE_INTEGER_FIELD, lContainers, 0, false, false,
                null, "SheetLinkWithSomeConfidentialFields_Field3"));
        lData.getCriteriaDatas().add(lCrit);

        // Criteria 3
        lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        BooleanValueData lBooleanValue = new BooleanValueData();
        lBooleanValue.setBooleanValue(false);
        lCrit.setScalarValueData(lBooleanValue);
        lContainers =
                new ArrayList<FilterFieldsContainerInfo>(lContainers).subList(
                        0, 2);

        lCrit.setUsableFieldData(buildUsableFieldData(null, -1,
                FieldType.SIMPLE_BOOLEAN_FIELD, lContainers, 0, false, false,
                null,
                "SheetMultipleWithSomeConfidentialFields_multiple1_Field1"));
        lData.getCriteriaDatas().add(lCrit);

        // Criteria 4
        lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        lStringValue = new StringValueData();
        lStringValue.setStringValue("Temporary");
        lCrit.setScalarValueData(lStringValue);
        List<String> lPossibleValues = new ArrayList<String>();
        lPossibleValues.add("$NOT_SPECIFIED");
        lPossibleValues.add("Temporary");
        lPossibleValues.add("Open");
        lPossibleValues.add("Closed");
        lPossibleValues.add("NoAction");
        lPossibleValues.add("Deleted");
        lContainers = new ArrayList<FilterFieldsContainerInfo>(lContainers);
        lCrit.setUsableFieldData(buildUsableFieldData("", -1,
                FieldType.CHOICE_FIELD, lContainers, 0, false, false,
                lPossibleValues, "$SHEET_STATE"));
        lData.getCriteriaDatas().add(lCrit);

        // Criteria 5
        lCrit = new CriteriaFieldData();
        lCrit.setOperator(Operators.EQ);
        lCrit.setCaseSensitive(true);
        DateValueData lDateValue = new DateValueData();
        lDateValue.setDateValue(EXPECTED_FILTER_CRITERIA5_DATE);
        lCrit.setScalarValueData(lDateValue);
        lContainers = new ArrayList<FilterFieldsContainerInfo>(lContainers);
        lContainerInfos = new FilterFieldsContainerInfo();
        lContainerInfos.setLabelKey("SheetLinkWithSomeConfidentialFields");
        lContainerInfos.setLinkDirection(LinkDirection.ORIGIN);
        lContainerInfos.setType(FieldsContainerType.LINK);
        lContainers.add(lContainerInfos);
        lContainerInfos = new FilterFieldsContainerInfo();
        lContainerInfos.setLabelKey("SheetWithSomeConfidentialFields");
        lContainerInfos.setLinkDirection(LinkDirection.UNDEFINED);
        lContainerInfos.setType(FieldsContainerType.SHEET);
        lContainers.add(lContainerInfos);
        lCrit.setUsableFieldData(buildUsableFieldData(null, -1,
                FieldType.SIMPLE_DATE_FIELD, lContainers, 0, false, false,
                null, "SheetWithSomeConfidentialFields_Field3"));
        lData.getCriteriaDatas().add(lCrit);

        lData.setOperator(Operators.AND);
        return lData;
    }

    /**
     * Build the expected image from Filter "HMI - FilterOnSeveralLevelResult"
     * ResultSummaryData in data of test instance
     * 
     * @return The ResultSummaryData of Filter
     *         "HMI - FilterOnSeveralLevelResult"
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ResultSummaryData buildExpectedHMIFilterOnSeveralLevelResultSummaryData()
        throws GDMException_Exception {
        ResultSummaryData lData = new ResultSummaryData();

        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(), null,
                        null);
        lData.setFilterVisibilityConstraintData(lConstraint);
        lData.setLabelKey("HMI - FilterOnSeveralLevel");

        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null,
                        "SheetWithSomeConfidentialFields").getId();
        lData.getFieldsContainerIds().add(lContainerId);

        lData.getUsableFieldDatas().add(
                buildUsableFieldData(null, EXPECTED_FILTER_STRING_MAX_LENGTH,
                        FieldType.SIMPLE_STRING_FIELD, null, 0, false, false,
                        null, "SheetWithSomeConfidentialFields_ref"));

        return lData;
    }

    /**
     * Build the expected image from Filter "HMI - FilterOnSeveralLevelResult"
     * ResultSortingData in data of test instance
     * 
     * @return The ResultSortingData of the Filter
     *         "HMI - FilterOnSeveralLevelResult"
     * @throws GDMException_Exception
     *             if an error occurs
     */
    private ResultSortingData buildExpectedHMIFilterOnSeveralLevelResultSortingData()
        throws GDMException_Exception {
        FilterVisibilityConstraintData lConstraint =
                buildFilterVisibilityConstraintData(getProcessName(), null,
                        null);
        // Get filter container ID
        String lContainerId =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null,
                        "SheetWithSomeConfidentialFields").getId();
        ResultSortingData lExpectedSortingData =
                buildResultSortingData("HMI - FilterOnSeveralLevel", null,
                        lConstraint);
        lExpectedSortingData.getFieldsContainerIds().add(lContainerId);
        lExpectedSortingData.setFilterVisibilityConstraintData(lConstraint);

        return lExpectedSortingData;
    }

    /**
     * This method removes the filter named TEST_FILTER_CREATION if it exists,
     * and creates a new one.
     */
    private void insertNewTestFilterCreation() {
        // DELETE PREVIOUS FILTER IF EXISTS
        ExecutableFilterData lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_CREATION");
        if (lFilter != null) {
            staticServices.deleteExecutableFilter(roleToken, lFilter.getId());
        }

        lFilter =
                staticServices.getExecutableFilterByName(roleToken,
                        getProcessName(), getProductName(), null,
                        "TEST_FILTER_1");

        // CHANGE THE FIELD PROPERTIES
        lFilter.setId(null); // Avoid update (creation of new filter)
        lFilter.setLabelKey("TEST_FILTER_CREATION");
        lFilter.getFilterData().setLabelKey("TEST_FILTER_CREATION");

        // MODIFY FILTER FIELDS (ADD COLOR FIELD)

        CriteriaFieldData lColorFieldCriteria = new CriteriaFieldData();
        lColorFieldCriteria.setOperator(Operators.EQ);
        lColorFieldCriteria.setCaseSensitive(false);
        StringValueData lColorValue = new StringValueData();
        lColorValue.setStringValue("white");
        lColorFieldCriteria.setScalarValueData(lColorValue);
        UsableFieldData lColorField =
                buildUsableFieldData("", EXPECTED_FILTER_STRING_MAX_LENGTH,
                        FieldType.SIMPLE_STRING_FIELD, null, -1, false, false,
                        new ArrayList<String>(), "CAT_color");
        lColorFieldCriteria.setUsableFieldData(lColorField);
        lFilter.getFilterData().setCriteriaData(lColorFieldCriteria);

        List<String> lPossibleColors = new ArrayList<String>();
        lPossibleColors.add("$NOT_SPECIFIED");
        lPossibleColors.add("WHITE");
        lPossibleColors.add("GREY");
        lPossibleColors.add("GREEN");
        lPossibleColors.add("YELLOW");
        lPossibleColors.add("RED");
        lPossibleColors.add("BLACK");

        lFilter.getResultSummaryData().getUsableFieldDatas().add(
                buildUsableFieldData("Color", -1, FieldType.CHOICE_FIELD, null,
                        0, false, true, lPossibleColors, "CAT_color"));
        lFilter.getResultSummaryData().setLabelKey("TEST_FILTER_CREATION");
        lFilter.getResultSortingData().setLabelKey("TEST_FILTER_CREATION");

        // CREATE NEW FILTER
        staticServices.createOrUpdateExecutableFilter(roleToken, lFilter);
    }
}
/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getUsableFieldsForCriteria<CODE> of the Search
 * Service.
 * 
 * @author ahaugomm
 */
public class TestGetUsableFieldsService extends AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    private FieldsContainerService fieldsContainerService;

    private static final String[] SHEET_TYPE_NAMES =
            {
             GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS,
             GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS };

    private static final String[] SHEET_MULTIPLE_USABLE_FIELD_DATA_NAMES =
            new String[] {
                          "$SHEET_STATE",
                          "$SHEET_TYPE",
                          "$SHEET_REFERENCE",
                          "$PRODUCT_HIERARCHY",
                          "$PRODUCT_NAME",
                          "Store|product_name",
                          // "Store|product_location", //Because its multi-valued
                          "Store|product_storetype",
                          "Store|Store_Field3",
                          "Store|postGetModelOK",
                          "Store|preCreateOK",
                          "Store|postCreateOK",
                          "Store|preUpdateOK",
                          "Store|postUpdateOK",
                          //Link
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field3",

            };

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = getServiceLocator().getSearchService();
        fieldsContainerService =
                getServiceLocator().getFieldsContainerService();
    }

    /**
     * Tests the method when there are multiple fields containers
     */
    public void testWithMultipleFieldsContainers() {
        searchService.setMaxFieldsDepth(2);

        String[] lIds = new String[SHEET_TYPE_NAMES.length];
        for (int i = 0; i < SHEET_TYPE_NAMES.length; i++) {
            lIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
            assertNotNull("sheet type " + SHEET_TYPE_NAMES[i] + " not found.",
                    lIds[i]);
        }

        // main test
        Map<String, UsableFieldData> lUsableFieldDatas =
                this.searchService.getUsableFields(adminRoleToken, lIds,
                        getProcessName());

        assertNotNull("Null result for getUsableFieldsForCriteria().",
                lUsableFieldDatas);
        // The values that should be return

        Collection<String> lUsableFieldDatasName = lUsableFieldDatas.keySet();
        assertEqualsUnordered(
                transformIntoUsableFieldDataId(adminRoleToken,
                        SHEET_MULTIPLE_USABLE_FIELD_DATA_NAMES),
                lUsableFieldDatasName.toArray(new String[lUsableFieldDatasName.size()]));
    }

    /**
     * Tests the method with null container id
     */
    public void testWithNullFieldContainerIdsCase() {
        try {
            searchService.getUsableFields(adminRoleToken, null,
                    getProcessName());
            fail("The exception has not been thrown.");
        }
        catch (GDMException lException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    private static final String SHEET_NAME =
            GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS;

    private String[] transformIntoUsableFieldDataId(String pRoleToken,
            String[] pUsableFieldDataNames) {
        String[] lUsableFieldDataIds = new String[pUsableFieldDataNames.length];
        for (int i = 0; i < pUsableFieldDataNames.length; i++) {
            lUsableFieldDataIds[i] =
                    searchService.getUsableFieldDataId(pRoleToken,
                            getProcessName(), pUsableFieldDataNames[i]);
        }

        return lUsableFieldDataIds;
    }

    private static final String[] SHEET_USABLE_FIELD_DATA_NAMES =
            new String[] {
                          "__Reference",
                          "SheetWithSomeConfidentialFields_ref",
                          "SheetWithSomeConfidentialFields_Field1",
                          "SheetWithSomeConfidentialFields_Field2",
                          "SheetWithSomeConfidentialFields_Field3",
                          "$SHEET_STATE",
                          "$SHEET_TYPE",
                          "$SHEET_REFERENCE",
                          "$PRODUCT_HIERARCHY",
                          "$PRODUCT_NAME",
                          "Store|product_name",
                          "Store|product_location",
                          "Store|product_storetype",
                          "Store|Store_Field3",
                          "Store|postGetModelOK",
                          "Store|preCreateOK",
                          "Store|postCreateOK",
                          "Store|preUpdateOK",
                          "Store|postUpdateOK",
                          //Link
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field3",

                          //Linked sheet
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "__Reference",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_ref",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple1_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple1_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple2_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields_multiple2_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|$SHEET_STATE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|$SHEET_TYPE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|$SHEET_REFERENCE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|$PRODUCT_NAME",
                          //Level 2
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields_Field3",

                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|__Reference",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields_ref",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields_Field1",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields_Field2",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields_Field3",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "$SHEET_STATE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "$SHEET_TYPE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "$SHEET_REFERENCE",
                          "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetMultipleWithSomeConfidentialFields|"
                                  + "SheetLinkWithSomeConfidentialFields|"
                                  + "SheetWithSomeConfidentialFields|"
                                  + "$PRODUCT_NAME" };

    /**
     * Test getUsableFieldsData with a SheetType as container, user role, depth
     * max at 2.
     */
    public void testSheetUserDepth() {
        //Change filter max depth
        searchService.setMaxFieldsDepth(2);

        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, SHEET_NAME);

        assertNotNull("sheet type " + SHEET_NAME + " not found.", lContainerId);
        String[] lIds = { lContainerId };

        // main test
        startTimer();
        Map<String, UsableFieldData> lUsableFieldDatas =
                searchService.getUsableFields(adminRoleToken, lIds,
                        getProcessName());
        stopTimer();

        assertNotNull("Null result for getUsableFieldsForCriteria().",
                lUsableFieldDatas);

        Collection<String> lUsableFieldDatasName = lUsableFieldDatas.keySet();
        assertEqualsUnordered(
                transformIntoUsableFieldDataId(adminRoleToken,
                        SHEET_USABLE_FIELD_DATA_NAMES),
                lUsableFieldDatasName.toArray(new String[lUsableFieldDatasName.size()]));

        String lUsableFieldId =
                searchService.getUsableFieldDataId(adminRoleToken,
                        getProcessName(), SHEET_USABLE_FIELD_DATA_NAMES[14]);
        assertTrue(lUsableFieldDatas.keySet().contains(lUsableFieldId));
        lUsableFieldId =
                searchService.getUsableFieldDataId(adminRoleToken,
                        getProcessName(), SHEET_USABLE_FIELD_DATA_NAMES[16]);
        assertTrue(lUsableFieldDatas.keySet().contains(lUsableFieldId));
    }

    private static final String PRODUCT_NAME = "Store";

    private static final String[] PRODUCT_USABLE_FIELD_DATA_NAMES =
            new String[] { "product_name", "product_location",
                          "product_storetype", "Store_Field3",
                          "postGetModelOK", "preCreateOK", "postCreateOK",
                          "preUpdateOK", "postUpdateOK", "$PRODUCT_DESCRIPTION" };

    /**
     * Test getUsableFieldsData with a ProductType as container and admin role.
     */
    public void testProduct() {
        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, PRODUCT_NAME);

        assertNotNull("product type " + PRODUCT_NAME + " not found.",
                lContainerId);
        String[] lIds = { lContainerId };

        // main test
        startTimer();
        Map<String, UsableFieldData> lUsableFieldDatas =
                searchService.getUsableFields(adminRoleToken, lIds,
                        getProcessName());
        stopTimer();

        assertNotNull("Null result for getUsableFieldsForCriteria().",
                lUsableFieldDatas);

        Collection<String> lUsableFieldDatasName = lUsableFieldDatas.keySet();
        assertEqualsUnordered(
                PRODUCT_USABLE_FIELD_DATA_NAMES,
                lUsableFieldDatasName.toArray(new String[lUsableFieldDatasName.size()]));
    }

    private static final String LINK_NAME =
            "SheetLinkWithSomeConfidentialFields";

    private static final String[] LINK_USABLE_FIELD_DATA_NAMES =
            new String[] { "SheetLinkWithSomeConfidentialFields_Field1",
                          "SheetLinkWithSomeConfidentialFields_Field2",
                          "SheetLinkWithSomeConfidentialFields_Field3",
                          "$ORIGIN_SHEET_REF", "$ORIGIN_PRODUCT",
                          "$DEST_SHEET_REF", "$DEST_PRODUCT" };

    /**
     * Test getUsableFieldsData with a LinkType as container and admin role.
     */
    public void testLink() {
        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, LINK_NAME);

        assertNotNull("sheet link type " + LINK_NAME + " not found.",
                lContainerId);
        String[] lIds = { lContainerId };

        // main test
        startTimer();
        Map<String, UsableFieldData> lUsableFieldDatas =
                searchService.getUsableFields(adminRoleToken, lIds,
                        getProcessName());
        stopTimer();

        assertNotNull("Null result for getUsableFieldsForCriteria().",
                lUsableFieldDatas);

        Collection<String> lUsableFieldDatasName = lUsableFieldDatas.keySet();
        assertEqualsUnordered(
                LINK_USABLE_FIELD_DATA_NAMES,
                lUsableFieldDatasName.toArray(new String[lUsableFieldDatasName.size()]));
    }

    private static final String SHEET_NAME_ORDER =
            GpmTestValues.SHEET_TYPE_SHEET_USABLE_FIELD_ORDER;

    private static final String[] SHEET_USABLE_FIELD_DATA_NAMES_ORDER =
            new String[] { "__Reference", "ref", "field1", "multiple",
                          "multiple_field1", "multiple_field2", "$SHEET_STATE",
                          "$SHEET_TYPE", "$SHEET_REFERENCE", "$PRODUCT_NAME",
                          "$PRODUCT_HIERARCHY", };

    /**
     * Test getUsableFieldsData with a SheetType as container and admin role<br />
     * Test the order of usable fields (sheet type only).
     */
    public void testSheetOrder() {
        //Change filter max depth
        int lOldDepth = searchService.getMaxFieldsDepth();
        searchService.setMaxFieldsDepth(1);

        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, SHEET_NAME_ORDER);

        assertNotNull("sheet type " + SHEET_NAME_ORDER + " not found.",
                lContainerId);
        String[] lIds = { lContainerId };

        // main test
        startTimer();
        Map<String, UsableFieldData> lUsableFieldDatas =
                searchService.getUsableFields(adminRoleToken, lIds,
                        getProcessName());
        stopTimer();

        assertNotNull("Null result for getUsableFieldsForCriteria().",
                lUsableFieldDatas);

        Collection<String> lUsableFieldDatasName = lUsableFieldDatas.keySet();
        //Remove all usable fields without sheet fields.
        if (lUsableFieldDatasName.retainAll(Arrays.asList(SHEET_USABLE_FIELD_DATA_NAMES_ORDER))) {
            assertEqualsOrdered(
                    SHEET_USABLE_FIELD_DATA_NAMES_ORDER,
                    lUsableFieldDatasName.toArray(new String[lUsableFieldDatasName.size()]));
        }
        else {
            fail("Fields are not present");
        }
        searchService.setMaxFieldsDepth(lOldDepth);
    }

    private static final String SHEET_TYPE_MV_01 = "sameSheetTypeM_01";

    private static final String SHEET_TYPE_MV_02 = "sameSheetTypeM_02";

    private static final String FIELD_MV = "stringField";

    private static final String[] MV_USABLE_FIELD_DATA_NAMES =
            new String[] { "__Reference", "ref", "$SHEET_STATE", "$SHEET_TYPE",
                          "$SHEET_REFERENCE", "$PRODUCT_HIERARCHY",
                          "$PRODUCT_NAME", "Store|product_name",
                          "Store|product_storetype", "Store|Store_Field3",
                          "Store|postGetModelOK", "Store|preCreateOK",
                          "Store|postCreateOK", "Store|preUpdateOK",
                          "Store|postUpdateOK" };

    /**
     * Test that multi-valued fields are not returned as usable fields for
     * multi-container fields.
     */
    public void testMultiContainerMultiValued() {
        Collection<String> lFieldsContainerId = new HashSet<String>(2);
        String lId01 =
                fieldsContainerService.getFieldsContainerId(adminRoleToken,
                        SHEET_TYPE_MV_01);
        String lId02 =
                fieldsContainerService.getFieldsContainerId(adminRoleToken,
                        SHEET_TYPE_MV_02);
        lFieldsContainerId.add(lId01);
        lFieldsContainerId.add(lId02);
        UsableFieldData lUsableField =
                searchService.getUsableField(getProcessName(), FIELD_MV,
                        lFieldsContainerId);
        assertNull("Multi-valued fields ('" + FIELD_MV
                + "') cannot be retrieve for multi-container filters",
                lUsableField);

        Map<String, UsableFieldData> lUsableFields =
                searchService.getUsableFields(adminRoleToken,
                        new String[] { lId01, lId02 }, getProcessName());

        Collection<String> lUsableFieldsName = lUsableFields.keySet();
        assertEqualsUnordered(transformIntoUsableFieldDataId(adminRoleToken,
                MV_USABLE_FIELD_DATA_NAMES),
                lUsableFieldsName.toArray(new String[lUsableFieldsName.size()]));
    }
}

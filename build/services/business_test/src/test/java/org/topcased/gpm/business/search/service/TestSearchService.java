/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.SearchServiceTestUtils;
import org.topcased.gpm.domain.search.FieldsContainerId;

/**
 * Test SearchService methods on attributes of instance.
 * 
 * @author mkargbo
 */
public class TestSearchService extends AbstractBusinessServiceTestCase {

    private static final int NEW_FIELDS_MAX_DEPTH = 4;

    private static final int FIELDS_MAX_DEPTH_EXPECTED = 3;

    private SearchService searchService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = getServiceLocator().getSearchService();
    }

    /**
     * SearchService must get the depth max from Global attribute for its first
     * call.
     */
    public void testGetMaxFieldsDepth() {
        assertEquals(FIELDS_MAX_DEPTH_EXPECTED,
                searchService.getMaxFieldsDepth());
    }

    /**
     * SearchService must get the depth max from setter value when the set
     * method has been called before get method.
     */
    public void testSetMaxFieldsDepth() {
        searchService.setMaxFieldsDepth(NEW_FIELDS_MAX_DEPTH);

        assertEquals(NEW_FIELDS_MAX_DEPTH, searchService.getMaxFieldsDepth());
    }

    private static final String FIELDS_CONTAINER_NAME_PRICE =
            GpmTestValues.SHEET_TYPE_PRICE;

    private static final String FIELDS_CONTAINER_NAME_CAT_PRICE =
            GpmTestValues.SHEETLINK_CAT_PRICE;

    private static final String FIELDS_CONTAINER_NAME_CAT =
            GpmTestValues.SHEET_TYPE_CAT;

    private static final String FIELD_LABEL = "CAT_PRICE|Price|PRICE_price";

    private static final String EXPECTED_ID = "|PRICE_price";

    private static final String FIELD_LABEL_2 =
            "CAT_PRICE|Price|CAT_PRICE|Cat|CAT_PRICE|Price|PRICE_price";

    private static final String EXPECTED_ID_2 = "|PRICE_price";

    private static final String FIELD_LABEL_3 = GpmTestValues.SHEET_TYPE_PRICE;

    private static final String EXPECTED_ID_3 = GpmTestValues.SHEET_TYPE_PRICE;

    /**
     * Test get usable fields id. Three case:
     * <ul>
     * <li>One level</li>
     * <li>Three level</li>
     * <li>No level</li>
     * </ul>
     */
    public void testGetUsableFieldDataId() {
        //Construct expected result
        String lFieldsContainerCatPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT_PRICE);
        String lFieldsContainerPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_PRICE);
        String lFieldsContainerCatId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT);
        String lExpectedId =
                lFieldsContainerCatPriceId + "|" + lFieldsContainerPriceId
                        + EXPECTED_ID;
        String lExpectedId2 =
                lFieldsContainerCatPriceId + "|" + lFieldsContainerPriceId
                        + "|" + lFieldsContainerCatPriceId + "|"
                        + lFieldsContainerCatId + "|"
                        + lFieldsContainerCatPriceId + "|"
                        + lFieldsContainerPriceId + EXPECTED_ID_2;

        //Test with one level
        String lUsableFieldDataId =
                searchService.getUsableFieldDataId(adminRoleToken,
                        getProcessName(), FIELD_LABEL);
        assertEquals(lExpectedId, lUsableFieldDataId);

        //Test with three level
        String lUsableFieldDataId2 =
                searchService.getUsableFieldDataId(adminRoleToken,
                        getProcessName(), FIELD_LABEL_2);
        assertEquals(lExpectedId2, lUsableFieldDataId2);

        //Test no hierarchy
        String lUsableFieldDataId3 =
                searchService.getUsableFieldDataId(adminRoleToken,
                        getProcessName(), FIELD_LABEL_3);
        assertEquals(EXPECTED_ID_3, lUsableFieldDataId3);
    }

    /**
     * Test method for
     * {@link SearchService#toFilterFieldsContainerInfos(Collection))} .
     */
    public void testToFilterFieldsContainerInfos() {
        String lFieldsContainerCatPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT_PRICE);
        String lFieldsContainerPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_PRICE);
        String lFieldsContainerCatId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT);

        FilterFieldsContainerInfo[] lFieldsContainerInfos =
                new FilterFieldsContainerInfo[3];
        lFieldsContainerInfos[0] =
                searchService.createFilterFieldsContainerInfo(
                        lFieldsContainerCatPriceId, lFieldsContainerPriceId);
        lFieldsContainerInfos[1] =
                searchService.createFilterFieldsContainerInfo(
                        lFieldsContainerPriceId, lFieldsContainerCatId);
        lFieldsContainerInfos[2] =
                searchService.createFilterFieldsContainerInfo(
                        lFieldsContainerCatId, null);

        Collection<FieldsContainerId> lFieldsContainerIds =
                new ArrayList<FieldsContainerId>(3);
        FieldsContainerId lFieldsContainerId = FieldsContainerId.newInstance();
        lFieldsContainerId.setIdentificator(String.valueOf(lFieldsContainerCatPriceId));
        lFieldsContainerIds.add(lFieldsContainerId);

        lFieldsContainerId = FieldsContainerId.newInstance();
        lFieldsContainerId.setIdentificator(String.valueOf(lFieldsContainerPriceId));
        lFieldsContainerIds.add(lFieldsContainerId);

        lFieldsContainerId = FieldsContainerId.newInstance();
        lFieldsContainerId.setIdentificator(String.valueOf(lFieldsContainerCatId));
        lFieldsContainerIds.add(lFieldsContainerId);

        List<FilterFieldsContainerInfo> lFilterFieldsContainerInfoList =
                getServiceLocator().getSearchService().toFilterFieldsContainerInfos(
                        null, lFieldsContainerIds);
        SearchServiceTestUtils.arraysAreEqual(
                lFieldsContainerInfos,
                lFilterFieldsContainerInfoList.toArray(new FilterFieldsContainerInfo[3]));
    }

    /**
     * Test method for
     * {@link SearchService#createFilterFieldsContainerInfo(String, String))} .
     */
    public void testCreateFilterFieldsContainerInfo() {
        String lFieldsContainerCatPriceId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, FIELDS_CONTAINER_NAME_CAT_PRICE);

        FilterFieldsContainerInfo lExpectedFilterFieldsContainerInfo =
                new FilterFieldsContainerInfo();
        lExpectedFilterFieldsContainerInfo.setId(lFieldsContainerCatPriceId);
        lExpectedFilterFieldsContainerInfo.setLabelKey(FIELDS_CONTAINER_NAME_CAT_PRICE);
        lExpectedFilterFieldsContainerInfo.setType(FieldsContainerType.LINK);
        lExpectedFilterFieldsContainerInfo.setLinkDirection(null);

        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                searchService.createFilterFieldsContainerInfo(
                        lFieldsContainerCatPriceId, null);

        SearchServiceTestUtils.areEqual(lExpectedFilterFieldsContainerInfo,
                lFilterFieldsContainerInfo);
    }

}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.search.impl.SearchServiceImpl;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.serialization.data.Filter;

/**
 * Tests the filter export.
 * 
 * @author tpanuel
 */
public class TestFilterExport extends AbstractTestExport<Filter> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> byProductIds = new HashSet<String>();

    private final static Set<String> limitedProductNames =
            new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private SearchServiceImpl searchService;

    /**
     * Create TestFilterExport.
     */
    public TestFilterExport() {
        super("filters", Filter.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        searchService =
                (SearchServiceImpl) ContextLocator.getContext().getBean(
                        "searchServiceImpl");
        if (!init) {
            // Fill limited product names
            limitedProductNames.add(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME);
            limitedProductNames.add(GpmTestValues.PRODUCT1_NAME);
            // Fill all elements ids
            init(GpmTestValues.FILTER_TEST_FILTER_WITH_SAME_NAME,
                    GpmTestValues.PROCESS_NAME,
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, null);
            init("Test_CreateFilter_CF_001_SAVED", GpmTestValues.PROCESS_NAME,
                    GpmTestValues.PRODUCT1_NAME, GpmTestValues.USER_VIEWER3);
            init("Test_RemoveFilter_CF_002_FILTER", GpmTestValues.PROCESS_NAME,
                    null, GpmTestValues.USER_VIEWER3);
            init(GpmTestValues.FILTER_TYPE_VFD_SHEET_TYPE_FILTER,
                    GpmTestValues.PROCESS_NAME, null, null);
            init("ProductFilterWithSameName", GpmTestValues.PROCESS_NAME, null,
                    GpmTestValues.USER_ADMIN);
            init("FILTER_TEST_2", GpmTestValues.PROCESS_NAME, null, null);
            init("Test_RemoveFilter_CF_004_FILTER_PRODUCT",
                    GpmTestValues.PROCESS_NAME, GpmTestValues.PRODUCT1_NAME,
                    null);
            init(GpmTestValues.FILTER_TEST_FILTER_WITH_SAME_NAME,
                    GpmTestValues.PROCESS_NAME, null, null);
            init("ProductFilter01", GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_OPENED_CATS, GpmTestValues.PROCESS_NAME,
                    null, null);
            init("autolocking filter", GpmTestValues.PROCESS_NAME,
                    GpmTestValues.PRODUCT1_NAME, null);
            init(GpmTestValues.FILTER_TYPE_VFD_SHEET_STATE_FILTER,
                    GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_TEST_FILTER_1,
                    GpmTestValues.PROCESS_NAME,
                    GpmTestValues.PRODUCT_BERNARD_STORE_NAME, null);
            init(GpmTestValues.FILTER_TEST_FILTER_2,
                    GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_TEST_FILTER_WITH_SAME_NAME,
                    GpmTestValues.PROCESS_NAME, null, GpmTestValues.USER_ADMIN);
            init(GpmTestValues.FILTER_ALL_SHEETS, GpmTestValues.PROCESS_NAME,
                    null, null);
            init("Test_RemoveFilter_CF_001_FILTER", GpmTestValues.PROCESS_NAME,
                    null, GpmTestValues.USER_VIEWER3);
            init("Test_RemoveFilter_CF_003_FILTER", GpmTestValues.PROCESS_NAME,
                    null, GpmTestValues.USER_VIEWER3);
            init("FILTER_TEST_UNIDIRECTIONNAL", GpmTestValues.PROCESS_NAME,
                    null, null);
            init(GpmTestValues.FILTER_SHEETTYPE1, GpmTestValues.PROCESS_NAME,
                    null, null);
            init("Test_RemoveFilter_CF_004_FILTER_USER",
                    GpmTestValues.PROCESS_NAME, null,
                    GpmTestValues.USER_VIEWER3);
            init(GpmTestValues.FILTER_TEST_MIGRATION,
                    GpmTestValues.PROCESS_NAME, null, null);
            init("ProductFilterWithSameName", GpmTestValues.PROCESS_NAME,
                    GpmTestValues.PRODUCT_BERNARD_STORE_NAME, null);
            init(GpmTestValues.FILTER_SHEETS_WITH_POINTERS,
                    GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_TEST_REMOVEFILTER_CF_004_FILTER_INSTANCE,
                    GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_HMI_FILTER_ON_SEVERAL_LEVEL,
                    GpmTestValues.PROCESS_NAME, null, null);
            init(GpmTestValues.FILTER_TYPE_VFD_SHEET_REFERENCE_FILTER,
                    GpmTestValues.PROCESS_NAME, null, null);
            init("ProductFilterWithSameName", GpmTestValues.PROCESS_NAME, null,
                    null);
            init("FILTER_TEST_1", GpmTestValues.PROCESS_NAME, null, null);
            init = true;
        }
    }

    private void init(final String pFilterName, final String pProcessName,
            final String pProductName, final String pUserLogin) {
        final String lFilterId =
                searchService.getExecutableFilterByName(adminRoleToken,
                        pProcessName, pProductName, pUserLogin, pFilterName).getId();

        allIds.add(lFilterId);
        if (limitedProductNames.contains(pProductName)) {
            byProductIds.add(lFilterId);
        }
        if (productNamesWithRoleOn.contains(pProductName)
                || GpmTestValues.USER_VIEWER3.equals(pUserLogin)) {
            idsWithRoleOn.add(lFilterId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setFiltersFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final Filter pObject,
            final ExportProperties pProperties) {
        return searchService.getExecutableFilterByName(adminRoleToken,
                getProcessName(), pObject.getProductName(),
                pObject.getUserLogin(), pObject.getLabelKey()).getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForAll()
     */
    protected Set<String> getExpectedIdsForAll() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByProduct()
     */
    protected Set<String> getExpectedIdsForLimitedByProduct() {
        return byProductIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedProductNames()
     */
    protected Set<String> getLimitedProductNames() {
        return limitedProductNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByType()
     */
    protected Set<String> getExpectedIdsForLimitedByType() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedTypeNames()
     */
    protected Set<String> getLimitedTypeNames() {
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getIdsWithRoleOn()
     */
    protected Set<String> getIdsWithRoleOn() {
        return idsWithRoleOn;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getElementInfo(java.lang.String)
     */
    protected String getElementInfo(final String pElementId) {
        if (pElementId == null) {
            return "Filter id is null";
        }
        else {
            final ExecutableFilterData lFilter =
                    searchService.getExecutableFilter(adminRoleToken,
                            pElementId);

            if (lFilter == null) {
                return "Filter not found";
            }
            else {
                return lFilter.getId()
                        + "#"
                        + lFilter.getLabelKey()
                        + "#"
                        + lFilter.getFilterVisibilityConstraintData().getBusinessProcessName()
                        + "#"
                        + lFilter.getFilterVisibilityConstraintData().getProductName()
                        + "#"
                        + lFilter.getFilterVisibilityConstraintData().getUserLogin();
            }
        }
    }
}
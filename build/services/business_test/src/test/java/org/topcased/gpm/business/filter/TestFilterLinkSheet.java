/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Charlotte Rambaud (Atos)
 ******************************************************************/
package org.topcased.gpm.business.filter;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;

/**
 * TestFilterLinkSheet When a field of product type is displayed in a filter.
 * The result must contain all sheets, not just those with a link.
 * 
 * @author crambaud
 */
public class TestFilterLinkSheet extends AbstractBusinessServiceTestCase {

    private FilterVisibilityConstraintData filterVisibilityConstraint;

    private FilterQueryConfigurator filterQueryConfigurator;

    private static final String INSTANCE_FILE_FILTER =
            "filter/TestExecuteSheetFilterWithNoSystem.xml";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();

        // Instantiate two filters "Cat00NumberWithStore" and "Cat00NumberWithoutStore"
        instantiate(getProcessName(), INSTANCE_FILE_FILTER);

        filterVisibilityConstraint =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        getProcessName(), StringUtils.EMPTY);
        filterQueryConfigurator = new FilterQueryConfigurator();

    }

    /**
     * Execute a filter without Store
     * <ul>
     * <li>$SHEET_REFERENCE</li>
     * <li>$PRODUCT_NAME</li>
     * <li>Cat-Cat|Cat|$SHEET_REFERENCE</li>
     * </ul>
     */
    public void testcheckFilterResultNoStore() {
        SearchService lSearchService = serviceLocator.getSearchService();

        ExecutableFilterData lFilter =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, "Cat00NumberWithoutStore");
        serviceLocator.getSheetService().getSheetsByType(getProcessName(),
                "Cat");

        FilterResultIterator<SummaryData> lFilterResultIterator =
                lSearchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);

        // verify the existence of three sheets.
        int lNumberWithoutStore = 3;
        assertEquals(IteratorUtils.toList(lFilterResultIterator).size(),
                lNumberWithoutStore);
    }

    /**
     * Execute a filter with Store
     * <ul>
     * <li>$SHEET_REFERENCE</li>
     * <li>$PRODUCT_NAME</li>
     * <li>Cat-Cat|Cat|$SHEET_REFERENCE</li>
     * <li>Cat-Cat|Cat|Store|product_name</li>
     * </ul>
     */
    public void testcheckFilterResultStore() {
        SearchService lSearchService = serviceLocator.getSearchService();

        ExecutableFilterData lFilter =
                lSearchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, "Cat00NumberWithStore");

        FilterResultIterator<SummaryData> lFilterResultIterator =
                lSearchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);

        // verify the existence of three sheets.
        int lNumberWithStore = 3;
        assertEquals(IteratorUtils.toList(lFilterResultIterator).size(),
                lNumberWithStore);
    }

}

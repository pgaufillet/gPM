/***************************************************************
 * Copyright (c) 2012 ATOS. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

public class TestLevel3Filter extends AbstractBusinessServiceTestCase {

    private SearchService searchService;

    private static final String XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE =
            "search/multiLevelFilters.xml";

    private static int EXPECTED_SHEET_RESULTS = 67;

    public void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        instantiate(getProcessName(), XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE);
    }

    /**
     * This test checks that in a level-3 filter: - no duplicate is returned
     * (bug 416/502) - no missing entry in large set of results (bug 502) - no
     * false result is returned (bug 516)
     */
    @SuppressWarnings("unchecked")
    public void testNormalCase() {

        // Retrieve and execute the following filter:
        //  - Chief_Name
        //  - Chief-Chief|Chief|Chief_Name
        //  - Chief-Chief|Chief|Chief-Starter|Starter|Starter_Name
        //  - Chief-Chief|Chief|Chief-MainDish|MainDish|MainDish_Name
        //  - Chief-Chief|Chief|Chief-Dessert|Dessert|Dessert_Name
        ExecutableFilterData lFirstFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, "LEVEL3_FILTER_RESULT");
        FilterResultIterator<SummaryData> lFirstFilterResultsIterator =
                searchService.executeFilter(adminRoleToken, lFirstFilter,
                        lFirstFilter.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        // Verify that filter results match the 67 expected results
        List<SheetSummaryData> lFirstResultList =
                IteratorUtils.toList(lFirstFilterResultsIterator);

        assertEquals("There is not 67 results but " + lFirstResultList.size(),
                lFirstResultList.size(), EXPECTED_SHEET_RESULTS);
    }

}

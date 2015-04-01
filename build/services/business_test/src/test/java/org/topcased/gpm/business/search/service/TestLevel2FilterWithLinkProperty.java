/***************************************************************
 * Copyright (c) 2012 ATOS. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

public class TestLevel2FilterWithLinkProperty extends
        AbstractBusinessServiceTestCase {

    private SearchService searchService;

    private static final String XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE =
            "search/multiLevelFilters.xml";

    private static int EXPECTED_SIZE = 4;

    public void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        instantiate(getProcessName(), XML_INSTANCE_MULTI_LEVEL_FILTERS_FILE);
    }

    /**
     * This test checks that in filter containing a level-2 criteria and a link
     * criteria, no incorrect result is returned (bug 539)
     */
    @SuppressWarnings("unchecked")
    public void testNormalCase() {

        // Retrieve and execute the following filter:
        //  - Chief_Name
        //  - Chief-Chief|Chief|Chief_Name
        //  - Chief-Chief|comment
        ExecutableFilterData lFirstFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null,
                        "LEVEL2_FILTER_WITH_LINK_PROPERTIES_RESULT");
        FilterResultIterator<SummaryData> lResultsIterator =
                searchService.executeFilter(adminRoleToken, lFirstFilter,
                        lFirstFilter.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        // Verify that there is the correct number of results
        List<SheetSummaryData> lResuls = IteratorUtils.toList(lResultsIterator);

        assertEquals("Retrieved " + lResuls.size() + " instead of "
                + EXPECTED_SIZE + ".", lResuls.size(), EXPECTED_SIZE);
    }

}

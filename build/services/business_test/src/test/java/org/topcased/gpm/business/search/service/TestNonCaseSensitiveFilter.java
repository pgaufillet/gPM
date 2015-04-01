/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestNonCaseSensitiveFilter
 * 
 * @author nveillet
 */
public class TestNonCaseSensitiveFilter extends AbstractBusinessServiceTestCase {

    private static final String INSTANCE_FILE =
            "search/TestNonCaseSensitiveFilter.xml";

    private static final String FILTER_NAME = "NonCaseSensitiveFilter";

    /**
     * Test the filter non case sensitive
     */
    public void testNormalCase() {
        // Instanciate filter
        instantiate(getProcessName(), INSTANCE_FILE);

        ExecutableFilterData lExecutableFilterData =
                getServiceLocator().getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME);

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(null, getProcessName(), null);

        FilterResultIterator<SheetSummaryData> lResults =
                getServiceLocator().getSearchService().executeFilter(
                        normalRoleToken, lExecutableFilterData,
                        lFilterVisibilityConstraintData,
                        new FilterQueryConfigurator());

        assertTrue("Filter " + FILTER_NAME + " don't have any result",
                lResults.hasNext());
    }
}

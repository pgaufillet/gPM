/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * TestGetFiltersFacade
 * 
 * @author nveillet
 */
public class TestGetFiltersFacade extends AbstractFacadeTestCase {

    private static final int FILTER_TABLE_PROCESS_NUMBER = 6;

    private static final int FILTER_TABLE_PRODUCT_NUMBER = 0;

    private static final int FILTER_TABLE_USER_NUMBER = 0;

    private static final int FILTER_TREE_NUMBER = 4;

    /**
     * Test the method getFilters
     */
    public void testNormalCase() {
        UiFilterSummaries lFilterSummaries =
                getFacadeLocator().getFilterFacade().getFilters(
                        getAdminUserSession().getSession(getProductName()),
                        FilterType.SHEET);

        assertNotNull(lFilterSummaries);

        assertEquals(FILTER_TABLE_PROCESS_NUMBER,
                lFilterSummaries.getTableProcessFilters().size());

        assertEquals(FILTER_TABLE_PRODUCT_NUMBER,
                lFilterSummaries.getTableProductFilters().size());

        assertEquals(FILTER_TABLE_USER_NUMBER,
                lFilterSummaries.getTableUserFilters().size());

        assertEquals(FILTER_TREE_NUMBER,
                lFilterSummaries.getTreeFilters().size());
    }
}

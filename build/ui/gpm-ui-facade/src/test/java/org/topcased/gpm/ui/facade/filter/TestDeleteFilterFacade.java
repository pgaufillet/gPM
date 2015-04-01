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

import java.util.List;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;

/**
 * TestDeleteFilterFacade
 * 
 * @author nveillet
 */
public class TestDeleteFilterFacade extends AbstractFacadeTestCase {

    private static final String FILTER_NAME = "SHEET_1 LIST TABLE";

    /**
     * Test the method deleteFilter
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lSession = getAdminUserSession().getSession(getProductName());

        UiFilterSummaries lFilterSummaries =
                lFilterFacade.getFilters(lSession, FilterType.SHEET);

        List<UiFilterSummary> lFilterSummariesList =
                getFilterSummariesAsList(lFilterSummaries);

        String lFilterId = null;

        for (UiFilterSummary lFilterSummary : lFilterSummariesList) {
            if (FILTER_NAME.equals(lFilterSummary.getName())) {
                lFilterId = lFilterSummary.getId();
                break;
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get filter.");
        }
        UiFilter lFilter = lFilterFacade.getFilter(lSession, lFilterId);
        assertNotNull(lFilter);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete filter.");
        }
        lFilterFacade.deleteFilter(lSession, lFilterId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Try to get deleted filter.");
        }
        try {
            lFilterFacade.getFilter(lSession, lFilterId);
        }
        catch (Exception lE) {
            assertTrue(lE.getCause().getMessage().startsWith(
                    "No row with the given identifier exists"));
        }
    }
}

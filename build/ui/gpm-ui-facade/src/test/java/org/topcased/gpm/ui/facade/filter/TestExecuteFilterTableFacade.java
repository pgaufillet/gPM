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
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;

/**
 * TestExecuteFilterTableFacade
 * 
 * @author nveillet
 */
public class TestExecuteFilterTableFacade extends AbstractFacadeTestCase {

    private static final String FILTER_NAME = "SHEET_1 LIST TABLE";

    private static final int COUNT_SHEET_RESULT = 2;

    /**
     * @throws NotSpecifiedCriteriaException
     * @throws EmptyResultFieldException
     * @throws NotSpecifiedScopeException
     * @throws NotExistFilterException
     */
    public void testNormalCase() throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {
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

        UiFilterTableResult lFilterResult =
                lFilterFacade.executeFilterTable(lSession, lFilterId, false);

        assertNotNull(lFilterResult);
        assertEquals(COUNT_SHEET_RESULT, lFilterResult.getResultValues().size());
    }
}

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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

/**
 * TestExecuteFilterLinkDeletionFacade
 * 
 * @author nveillet
 */
public class TestExecuteFilterLinkDeletionFacade extends AbstractFacadeTestCase {

    private static final int COUNT_SHEET_RESULT = 0;

    private static final String LINK_TYPE_NAME = "Sheet1Sheet1Link";

    private static final String ORIGIN_SHEET_REFERENCE = "REF_Origin_Sheet";

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

        // Get origin sheet
        String lSheetId =
                getSheetService().getSheetIdByReference(
                        lSession.getRoleToken(), ORIGIN_SHEET_REFERENCE);

        UiFilterTableResult lFilterResult =
                lFilterFacade.executeFilterLinkDeletion(lSession, lSheetId,
                        LINK_TYPE_NAME);

        assertNotNull(lFilterResult);
        assertEquals(COUNT_SHEET_RESULT, lFilterResult.getResultValues().size());
    }
}

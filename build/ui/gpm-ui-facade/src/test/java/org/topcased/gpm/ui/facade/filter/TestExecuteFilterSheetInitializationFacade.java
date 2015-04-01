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
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

/**
 * TestExecuteFilterSheetInitializationFacade
 * 
 * @author nveillet
 */
public class TestExecuteFilterSheetInitializationFacade extends
        AbstractFacadeTestCase {

    private static final int COUNT_SHEET_RESULT = 2;

    private static final String SHEET_TYPE_NAME = "SHEET_1";

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

        // Get sheet in creation
        UiSheet lSheet =
                getFacadeLocator().getSheetFacade().getSheetByType(lSession,
                        SHEET_TYPE_NAME);

        UiFilterTableResult lFilterResult =
                lFilterFacade.executeFilterSheetInitialization(lSession,
                        lSheet.getId(), SHEET_TYPE_NAME);

        assertNotNull(lFilterResult);
        assertEquals(COUNT_SHEET_RESULT, lFilterResult.getResultValues().size());
    }
}

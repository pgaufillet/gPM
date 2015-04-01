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

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestExecuteFilterLinkCreationFacade
 * 
 * @author nveillet
 */
public class TestExecuteFilterLinkCreationFacade extends AbstractFacadeTestCase {

    private static final int COUNT_SHEET_RESULT = 1;

    private static final String SHEET_1_SHEET_1_LINK_TYPE_NAME =
            "Sheet1Sheet1Link";

    private static final String SHEET_1_CONFIDENTIAL_SHEET_LINK_TYPE_NAME =
            "Sheet1ConfidentialSheetLink";

    private static final String ORIGIN_SHEET_REFERENCE = "REF_Origin_Sheet";

    /**
     * @throws NotSpecifiedCriteriaException
     * @throws EmptyResultFieldException
     * @throws NotSpecifiedScopeException
     * @throws NotExistFilterException
     */
    public void testSheet1ConfidentialSheetCase()
        throws NotExistFilterException, NotSpecifiedScopeException,
        EmptyResultFieldException, NotSpecifiedCriteriaException {
        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lSession = getAdminUserSession().getSession(getProductName());
        SheetService lSheetService = getSheetService();

        // Get origin sheet
        String lSheetId =
                lSheetService.getSheetIdByReference(lSession.getRoleToken(),
                        ORIGIN_SHEET_REFERENCE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Execute filter for SHEET_1/CONFIDENTIAL_SHEET link.");
        }
        UiFilterTableResult lFilterResult =
                lFilterFacade.executeFilterLinkCreation(lSession, lSheetId,
                        SHEET_1_CONFIDENTIAL_SHEET_LINK_TYPE_NAME);

        assertNotNull(lFilterResult);
        assertEquals(COUNT_SHEET_RESULT, lFilterResult.getResultValues().size());

        String lConfidentialSheetId =
                lFilterResult.getResultValues().get(0).getFilterResultId().getId();
        assertFalse(lConfidentialSheetId.equals(lSheetId));

        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lSession.getRoleToken(),
                        lConfidentialSheetId, CacheProperties.IMMUTABLE);
        CacheableSheetType lCacheableSheetType =
                lSheetService.getCacheableSheetType(lSession.getRoleToken(),
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);

        assertEquals("CONFIDENTIAL_SHEET", lCacheableSheetType.getName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Execute filter for CONFIDENTIAL_SHEET/SHEET_1 link.");
        }
        lFilterResult =
                lFilterFacade.executeFilterLinkCreation(lSession,
                        lConfidentialSheetId,
                        SHEET_1_CONFIDENTIAL_SHEET_LINK_TYPE_NAME);

        assertNotNull(lFilterResult);
        assertEquals(2, lFilterResult.getResultValues().size());

        for (FilterResult lResult : lFilterResult.getResultValues()) {

            lSheetId = lResult.getFilterResultId().getId();

            lCacheableSheet =
                    lSheetService.getCacheableSheet(lSession.getRoleToken(),
                            lSheetId, CacheProperties.IMMUTABLE);
            lCacheableSheetType =
                    lSheetService.getCacheableSheetType(
                            lSession.getRoleToken(),
                            lCacheableSheet.getTypeId(),
                            CacheProperties.IMMUTABLE);

            assertEquals("SHEET_1", lCacheableSheetType.getName());
        }
    }

    /**
     * @throws NotSpecifiedCriteriaException
     * @throws EmptyResultFieldException
     * @throws NotSpecifiedScopeException
     * @throws NotExistFilterException
     */
    public void testSheet1Sheet1LinkCase() throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {
        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lSession = getAdminUserSession().getSession(getProductName());

        // Get origin sheet
        String lSheetId =
                getSheetService().getSheetIdByReference(
                        lSession.getRoleToken(), ORIGIN_SHEET_REFERENCE);

        UiFilterTableResult lFilterResult =
                lFilterFacade.executeFilterLinkCreation(lSession, lSheetId,
                        SHEET_1_SHEET_1_LINK_TYPE_NAME);

        assertNotNull(lFilterResult);
        assertEquals(COUNT_SHEET_RESULT, lFilterResult.getResultValues().size());

        String lOtherSheet1 =
                lFilterResult.getResultValues().get(0).getFilterResultId().getId();
        assertFalse(lOtherSheet1.equals(lSheetId));
    }
}

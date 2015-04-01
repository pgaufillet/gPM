/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.extendedaction;

import java.util.Arrays;

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.shared.extendedaction.AbstractUiExtendedActionResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFilterEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiMessageEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiSheetsEAResult;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestExecuteExtendedActionFacade
 * 
 * @author jlouisy
 */
public class TestExecuteExtendedActionFacade extends AbstractFacadeTestCase {

    private static final String EXTENDED_ACTION_SHEETS = "ExtendedActionVIEW";

    private static final String EXTENDED_ACTION_MESSAGE =
            "GLOBAL_Extended_Action_ALWAYS";

    private static final String EXTENDED_ACTION_FILTER = "ExtendedActionALWAYS";

    /**
     * File Result case
     */
    public void testFileResultCase() {
        // TODO
        // fail("Missing test case.");
    }

    /**
     * Filter Result case
     */
    public void testFilterResultCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Execute Extended Action.");
        }
        AbstractUiExtendedActionResult lExtendedActionResult =
                lExtendedActionFacade.executeExtendedAction(lUiSession,
                        EXTENDED_ACTION_FILTER, lCacheableSheet.getTypeId(),
                        null, null, null, Arrays.asList(lOriginSheetId), null,
                        null, null,null);

        assertTrue(lExtendedActionResult instanceof UiFilterEAResult);
        UiFilterEAResult lResult = (UiFilterEAResult) lExtendedActionResult;
        assertEquals("SHEET_1 LIST TABLE", lResult.getFilter().getName());
        assertEquals(2, lResult.getFilterResult().getResultValues().size());
        assertEquals(lResult.getFilter().getId(),
                lResult.getFilterResult().getFilterId());
    }

    /**
     * Message Result case
     */
    public void testMessageResultCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Execute Extended Action.");
        }
        AbstractUiExtendedActionResult lExtendedActionResult =
                lExtendedActionFacade.executeExtendedAction(lUiSession,
                        EXTENDED_ACTION_MESSAGE, null, null, null, null, null,
                        null, null, null, null);

        assertTrue(lExtendedActionResult instanceof UiMessageEAResult);
        UiMessageEAResult lResult = (UiMessageEAResult) lExtendedActionResult;
        assertEquals("printMessage has been executed.", lResult.getMessage());
    }

    /**
     * Sheet creation Result case
     */
    public void testSheetCreationResultCase() {
        // TODO
        // fail("Missing test case.");
    }

    /**
     * Sheets Result case
     */
    public void testSheetsResultCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Execute Extended Action.");
        }
        AbstractUiExtendedActionResult lExtendedActionResult =
                lExtendedActionFacade.executeExtendedAction(lUiSession,
                        EXTENDED_ACTION_SHEETS, lCacheableSheet.getTypeId(),
                        null, null, lOriginSheetId, null, null, null,null, null);

        assertTrue(lExtendedActionResult instanceof UiSheetsEAResult);
        UiSheetsEAResult lResult = (UiSheetsEAResult) lExtendedActionResult;
        assertEquals(1, lResult.getSheetIds().size());
        assertTrue(lResult.getSheetIds().contains(lOriginSheetId));
        assertEquals(DisplayMode.VISUALIZATION, lResult.getDisplayMode());
    }
}

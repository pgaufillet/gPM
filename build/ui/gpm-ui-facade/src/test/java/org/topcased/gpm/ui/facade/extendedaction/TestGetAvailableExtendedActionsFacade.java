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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetAvailableExtendedActionsFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableExtendedActionsFacade extends
        AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testGetAvailableExtendedActionsUiSession() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available actions.");
        }
        List<UiAction> lUiActions =
                lExtendedActionFacade.getAvailableExtendedActions(lUiSession);

        assertEquals(1, lUiActions.size());
        assertEquals(1,
                ((UiActionWithSubMenu) lUiActions.get(0)).getActions().size());
        assertEquals(
                "GLOBAL_ExtendedActionALWAYS",
                ((UiActionWithSubMenu) lUiActions.get(0)).getActions().get(0).getName());
    }

    /**
     * Normal case
     */
    public void testGetAvailableExtendedActionsUiSessionSheetIdDisplayMode() {
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
            LOGGER.info("Get available actions.");
        }
        List<UiAction> lUiActions =
                lExtendedActionFacade.getAvailableExtendedActions(lUiSession,
                        lCacheableSheet.getTypeId(), DisplayMode.EDITION);

        assertEquals(1, lUiActions.size());
        List<UiAction> lActions =
                ((UiActionWithSubMenu) lUiActions.get(0)).getActions();
        assertEquals(3, lActions.size());

        List<String> lActionNameList = new ArrayList<String>();
        for (UiAction lAction : lActions) {
            lActionNameList.add(lAction.getName());
        }

        assertTrue(lActionNameList.contains("GLOBAL_ExtendedActionEDIT"));
        assertTrue(lActionNameList.contains("ExtendedActionALWAYS"));
        assertTrue(lActionNameList.contains("ExtendedActionEDIT"));
    }

    /**
     * Normal case
     */
    public void testGetAvailableExtendedActionsUiSessionUiFilterContainerTypes() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lOriginCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);
        String lDestinationSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Destination_Sheet");
        CacheableSheet lDestinationCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lDestinationSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available actions.");
        }
        ArrayList<UiFilterContainerType> lContainerIds =
                new ArrayList<UiFilterContainerType>();
        lContainerIds.add(new UiFilterContainerType(
                lOriginCacheableSheet.getTypeId(), null));
        lContainerIds.add(new UiFilterContainerType(
                lDestinationCacheableSheet.getTypeId(), null));

        List<UiAction> lUiActions =
                lExtendedActionFacade.getAvailableExtendedActions(lUiSession,
                        lContainerIds);

        assertEquals(1, lUiActions.size());
        List<UiAction> lActions =
                ((UiActionWithSubMenu) lUiActions.get(0)).getActions();

        assertEquals(3, lActions.size());

        List<String> lActionNameList = new ArrayList<String>();
        for (UiAction lAction : lActions) {
            lActionNameList.add(lAction.getName());
        }

        assertTrue(lActionNameList.contains("GLOBAL_ExtendedActionLIST"));
        assertTrue(lActionNameList.contains("ExtendedActionALWAYS"));
        assertTrue(lActionNameList.contains("ExtendedActionLIST"));
    }

}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.sheet;

import java.util.List;

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetInitializableSheetTypesFacade
 * 
 * @author jlouisy
 */
public class TestGetInitializableSheetTypesFacade extends
        AbstractFacadeTestCase {

    private static final String SHEET_TYPE_1 = "SHEET_1";

    private static final Object SHEET_TYPE_2 = "CONFIDENTIAL_SHEET";

    /**
     * Normal case
     */
    public void testConfidentialCase() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get initializable Sheet types.");
        }
        List<String> lLinkTypesList =
                lSheetFacade.getInitializableSheetTypes(lUiSession,
                        lCacheableSheet.getTypeId());

        //Link Attributes
        assertEquals(1, lLinkTypesList.size());
        assertTrue(lLinkTypesList.contains(SHEET_TYPE_1));

        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get initializable Sheet types.");
        }
        List<String> lLinkTypesList =
                lSheetFacade.getInitializableSheetTypes(lUiSession,
                        lCacheableSheet.getTypeId());

        //Link Attributes
        assertEquals(2, lLinkTypesList.size());
        assertTrue(lLinkTypesList.contains(SHEET_TYPE_1));
        assertTrue(lLinkTypesList.contains(SHEET_TYPE_2));
    }
}

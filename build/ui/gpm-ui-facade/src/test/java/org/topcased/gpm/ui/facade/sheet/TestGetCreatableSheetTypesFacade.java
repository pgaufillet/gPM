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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;

/**
 * TestGetCreatableSheetTypesFacade
 * 
 * @author jlouisy
 */
public class TestGetCreatableSheetTypesFacade extends AbstractFacadeTestCase {

    private static final String SHEET_TYPE_1 = "SHEET_1";

    private static final Object SHEET_TYPE_2 = "CONFIDENTIAL_SHEET";

    /**
     * Normal case
     */
    public void testConfidentialorNotCreatableSheetsCase() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get creatable Sheet types.");
        }
        List<String> lLinkTypesList =
                lSheetFacade.getCreatableSheetTypes(lUiSession);

        //Creatable sheet types :
        // SHEET_1 not creatable, CONFIDENTIAL_SHEET confidential
        assertEquals(0, lLinkTypesList.size());

        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get creatable Sheet types.");
        }
        List<String> lLinkTypesList =
                lSheetFacade.getCreatableSheetTypes(lUiSession);

        //Link Attributes
        assertEquals(2, lLinkTypesList.size());
        assertTrue(lLinkTypesList.contains(SHEET_TYPE_1));
        assertTrue(lLinkTypesList.contains(SHEET_TYPE_2));
    }
}

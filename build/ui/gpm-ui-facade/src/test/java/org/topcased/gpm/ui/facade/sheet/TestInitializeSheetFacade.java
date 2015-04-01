/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.sheet;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * TestInitializeSheetFacade
 * 
 * @author nveillet
 */
public class TestInitializeSheetFacade extends AbstractFacadeTestCase {

    /**
     * Confidential sheet case
     */
    public void testConfidentialSheet() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // get sheet for creation
        UiSheet lUiSheet = lSheetFacade.getSheetByType(lUiSession, "SHEET_1");

        // Get source sheet identifier
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Source Confidential Sheet.");
        }
        String lSourceSheetId =
                getSheetService().getSheetIdByReference(
                        lUiSession.getRoleToken(), "REF_Confidential_sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Initialize from Confidential Sheet.");
        }
        try {
            lSheetFacade.initializeSheet(lUiSession, lUiSheet.getId(),
                    lSourceSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the source sheet " + lSourceSheetId
                            + " : the access is confidential";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }
        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // get sheet for creation
        UiSheet lUiSheet = lSheetFacade.getSheetByType(lUiSession, "SHEET_1");

        // Get source sheet identifier
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Source Sheet.");
        }
        String lSourceSheetId =
                getSheetService().getSheetIdByReference(
                        lUiSession.getRoleToken(), "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Initialize Sheet.");
        }
        UiSheet lUiInitializedSheet =
                lSheetFacade.initializeSheet(lUiSession, lUiSheet.getId(),
                        lSourceSheetId);

        // Sheet attributes
        assertEquals(lUiSheet.getId(), lUiInitializedSheet.getId());
        assertEquals(lUiSheet.getBusinessProcessName(),
                lUiInitializedSheet.getBusinessProcessName());
        assertEquals(lUiSheet.getTypeName(), lUiInitializedSheet.getTypeName());
        assertEquals(lUiSheet.isDeletable(), lUiInitializedSheet.isDeletable());
        assertEquals(lUiSheet.isUpdatable(), lUiInitializedSheet.isUpdatable());
        assertEquals(lUiSheet.getProductName(),
                lUiInitializedSheet.getProductName());

        // Check some fields
        assertEquals(lUiSheet.getStringField("STRING_SIMPLE_FIELD").get(),
                lUiInitializedSheet.getStringField("STRING_SIMPLE_FIELD").get());
        assertEquals(
                lUiSheet.getStringField("STRING_SIMPLE_FIELD").getFieldDescription(),
                lUiInitializedSheet.getStringField("STRING_SIMPLE_FIELD").getFieldDescription());
    }

    /**
     * Sheet not in cache case
     */
    public void testSheetNotInCacheCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Sheet creation
        UiSheet lUiSheet = lSheetFacade.getSheetByType(lUiSession, "SHEET_1");
        String lSheetId = lUiSheet.getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Empty cache.");
        }
        lSheetFacade.clearCache(lUiSession, lSheetId);

        // Get source sheet identifier
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Source Confidential Sheet.");
        }
        String lSourceSheetId =
                getSheetService().getSheetIdByReference(
                        lUiSession.getRoleToken(), "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Initialize Sheet.");
        }
        try {
            lSheetFacade.initializeSheet(lUiSession, lUiSheet.getId(),
                    lSourceSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet " + lSheetId
                            + " : the sheet does not exist in user cache";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }
    }

}

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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * SheetFacade.getSheet test
 * 
 * @author jlouisy
 */
public class TestCreateSheetFacade extends AbstractFacadeTestCase {

    private static final String SHEET_STATE = "CREATED";

    private static final String SHEET_TYPE_NAME = "SHEET_1";

    private static final String F_STRING_SIMPLE_FIELD = "STRING_SIMPLE_FIELD";

    private static final String F_STRING_SIMPLE_FIELD_NEW_VALUE =
            "NEW VALUE IN FIELD";

    private static final String F_STRING_SIMPLE_FIELD_DESC =
            "STRING simple field MAX_SIZE=20";

    private static final String F_MANDATORY_FIELD = "SHEET_NAME";

    private static final String F_MANDATORY_FIELD_NEW_VALUE =
            "SHEET NAME NEW VALUE";

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Sheet creation
        UiSheet lUiSheet =
                lSheetFacade.getSheetByType(lUiSession, SHEET_TYPE_NAME);
        String lSheetId = lUiSheet.getId();

        List<UiField> lUpdatedFields = new ArrayList<UiField>();

        BusinessStringField lField1 =
                lUiSheet.getStringField(F_STRING_SIMPLE_FIELD);
        lField1.set(F_STRING_SIMPLE_FIELD_NEW_VALUE);
        lUpdatedFields.add((UiField) lField1);

        BusinessStringField lField2 =
                lUiSheet.getStringField(F_MANDATORY_FIELD);
        lField2.set(F_MANDATORY_FIELD_NEW_VALUE);
        lUpdatedFields.add((UiField) lField2);

        // Creation
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Sheet.");
        }
        lSheetFacade.createSheet(lUiSession, lUiSheet.getId(), lUpdatedFields);

        // Get new sheet
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get created Sheet.");
        }
        lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        // Sheet attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiSheet.getBusinessProcessName());
        assertEquals(lSheetId, lUiSheet.getId());
        assertEquals(SHEET_TYPE_NAME, lUiSheet.getTypeName());
        assertTrue(lUiSheet.isDeletable());
        assertTrue(lUiSheet.isUpdatable());
        assertEquals(DEFAULT_PRODUCT_NAME, lUiSheet.getProductName());
        assertEquals(SHEET_STATE, lUiSheet.getState());

        // Check some fields
        assertEquals(F_STRING_SIMPLE_FIELD_NEW_VALUE, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").get());
        assertEquals(F_STRING_SIMPLE_FIELD_DESC, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").getFieldDescription());
    }

    /**
     * Normal case
     */
    public void testSheetNotCreatableCase() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Sheet creation
        UiSheet lUiSheet =
                lSheetFacade.getSheetByType(lUiSession, SHEET_TYPE_NAME);
        String lSheetId = lUiSheet.getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create sheet.");
        }
        try {
            lSheetFacade.createSheet(lUiSession, lSheetId,
                    new ArrayList<UiField>());
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            assertEquals(
                    "Illegal access: cannot create a sheet type 'SHEET_1'",
                    lException.getMessage());
        }
        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Sheet not in cache
     */
    public void testSheetNotInCacheCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Sheet creation
        UiSheet lUiSheet =
                lSheetFacade.getSheetByType(lUiSession, SHEET_TYPE_NAME);
        String lSheetId = lUiSheet.getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Empty cache.");
        }
        lSheetFacade.clearCache(lUiSession, lSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create sheet.");
        }
        try {
            lSheetFacade.createSheet(lUiSession, lSheetId,
                    new ArrayList<UiField>());
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

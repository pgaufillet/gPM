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
import org.topcased.gpm.business.sheet.service.SheetService;
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
public class TestUpdateSheetFacade extends AbstractFacadeTestCase {

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
     * Confidential sheet case
     */
    public void testConfidentialSheet() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Confidential_sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update Confidential Sheet.");
        }
        try {
            lSheetFacade.updateSheet(lUiSession, lSheetId, 1,
                    new ArrayList<UiField>());
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet "
                            + lSheetId
                            + " : the access is confidential or update is not allowed";
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

        // Get origin sheet
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Modify fields.");
        }
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
            LOGGER.info("Update Sheet.");
        }
        lSheetFacade.updateSheet(lUiSession, lSheetId, lUiSheet.getVersion(),
                lUpdatedFields);

        // Get new sheet
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get updated sheet.");
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
    public void testNotUpdatableSheetCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Modify fields.");
        }
        List<UiField> lUpdatedFields = new ArrayList<UiField>();

        // update
        lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update Sheet.");
        }
        try {
            lSheetFacade.updateSheet(lUiSession, lSheetId,
                    lUiSheet.getVersion(), lUpdatedFields);
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet "
                            + lSheetId
                            + " : the access is confidential or update is not allowed";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }

        logoutAsUser(lUiSession.getParent());
    }

}

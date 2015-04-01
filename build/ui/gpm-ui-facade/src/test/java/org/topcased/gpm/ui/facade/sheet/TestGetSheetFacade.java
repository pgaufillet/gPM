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
import java.util.Date;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.DateDisplayHintType;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceTreeField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceTreeFieldValue;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * SheetFacade.getSheetByType test
 * 
 * @author jlouisy
 */
public class TestGetSheetFacade extends AbstractFacadeTestCase {

    private static final String SHEET_STATE = "CREATED";

    private static final String SHEET_REFERENCE = "REF_Origin_Sheet";

    private static final String SHEET_TYPE_NAME = "SHEET_1";

    private static final String F_STRING_SIMPLE_FIELD_VALUE = "plop";

    private static final String F_STRING_SIMPLE_FIELD_DESC =
            "STRING simple field MAX_SIZE=20";

    private static final String F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_VALUE =
            "m\r\nu\r\nl\r\nt\r\ni\r\nl\r\ni\r\nn\r\ne\r\n\r\n\r\n1";

    private static final String F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_DESC =
            "Multiline line display hint";

    private static final int F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_WIDTH = 100;

    private static final int F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HEIGHT = 20;

    private static final StringDisplayHintType F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HINT_TYPE =
            StringDisplayHintType.MULTI_LINE;

    private static final Date F_LONG_DISPLAY_HINT_VALUE =
            new Date(Long.parseLong("1277968500000"));

    private static final String F_LONG_DISPLAY_HINT_DESC = "long hint";

    private static final DateDisplayHintType F_LONG_DISPLAY_HINT_HINT_TYPE =
            DateDisplayHintType.DATE_TIME_LONG;

    // Hierarchy is (* means selectable)
    //  |- ROOT_1*
    //      |-NODE_1
    //          |- LEAF_1_1*
    //          |- LEAF_1_2*
    //      |- NODE_2*
    //          |- LEAF_2_1*
    //          |- LEAF_2_2*
    //      |- NODE_3*
    //  |- ROOT_2
    //      |-NODE_1
    //          |- LEAF_1_1*
    //          |- LEAF_1_2*
    //      |- NODE_2
    //          |- LEAF_2_1*
    //      |- NODE_3*
    private static List<UiChoiceTreeFieldValue> staticChoiceTreeValues;

    static {

        List<UiChoiceTreeFieldValue> lRoot1Node1Leafs =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot1Node1Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_1/NODE_1/LEAF_1_1", "LEAF_1_1", true, null));
        lRoot1Node1Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_1/NODE_1/LEAF_1_2", "LEAF_1_2", true, null));

        List<UiChoiceTreeFieldValue> lRoot1Node2Leafs =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot1Node2Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_1/NODE_2/LEAF_2_1", "LEAF_2_1", true, null));
        lRoot1Node2Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_1/NODE_2/LEAF_2_2", "LEAF_2_2", true, null));

        List<UiChoiceTreeFieldValue> lRoot1Nodes =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot1Nodes.add(new UiChoiceTreeFieldValue("ROOT_1/NODE_1", "NODE_1",
                false, lRoot1Node1Leafs));
        lRoot1Nodes.add(new UiChoiceTreeFieldValue("ROOT_1/NODE_2", "NODE_2",
                true, lRoot1Node2Leafs));
        lRoot1Nodes.add(new UiChoiceTreeFieldValue("ROOT_1/NODE_3", "NODE_3",
                true, null));

        List<UiChoiceTreeFieldValue> lRoot2Node1Leafs =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot2Node1Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_2/NODE_1/LEAF_1_1", "LEAF_1_1", true, null));
        lRoot2Node1Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_2/NODE_1/LEAF_1_2", "LEAF_1_2", true, null));

        List<UiChoiceTreeFieldValue> lRoot2Node2Leafs =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot2Node2Leafs.add(new UiChoiceTreeFieldValue(
                "ROOT_2/NODE_2/LEAF_2_1", "LEAF_2_1", true, null));

        List<UiChoiceTreeFieldValue> lRoot2Nodes =
                new ArrayList<UiChoiceTreeFieldValue>();
        lRoot2Nodes.add(new UiChoiceTreeFieldValue("ROOT_2/NODE_1", "NODE_1",
                false, lRoot2Node1Leafs));
        lRoot2Nodes.add(new UiChoiceTreeFieldValue("ROOT_2/NODE_2", "NODE_2",
                false, lRoot2Node2Leafs));
        lRoot2Nodes.add(new UiChoiceTreeFieldValue("ROOT_2/NODE_3", "NODE_3",
                true, null));

        staticChoiceTreeValues = new ArrayList<UiChoiceTreeFieldValue>();
        staticChoiceTreeValues.add(new UiChoiceTreeFieldValue("ROOT_1",
                "ROOT_1", true, lRoot1Nodes));
        staticChoiceTreeValues.add(new UiChoiceTreeFieldValue("ROOT_2",
                "ROOT_2", false, lRoot2Nodes));
    }

    private void checkChoiceTreeValues(List<UiChoiceTreeFieldValue> pExpected,
            List<UiChoiceTreeFieldValue> pActual) {
        if (pExpected == null) {
            assertNull(pActual);
        }
        else {
            assertEquals(pExpected.size(), pActual.size());
            for (int i = 0; i < pExpected.size(); i++) {
                UiChoiceTreeFieldValue lExpectedValue = pExpected.get(i);
                UiChoiceTreeFieldValue lActualValue = pActual.get(i);
                assertEquals(lExpectedValue.getValue(), lActualValue.getValue());
                assertEquals(lExpectedValue.getTranslatedValue(),
                        lActualValue.getTranslatedValue());
                assertEquals(lExpectedValue.isSelectable(),
                        lActualValue.isSelectable());
                checkChoiceTreeValues(lExpectedValue.getChildren(),
                        lActualValue.getChildren());
            }
        }
    }

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
            LOGGER.info("Get Confidential Sheet.");
        }
        try {
            lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet " + lSheetId
                            + " : the access is confidential ";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }
        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testGetSheetFromCacheable() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lSheetId, CacheProperties.MUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheetFromCacheable(lUiSession, lCacheableSheet);

        // Sheet attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiSheet.getBusinessProcessName());
        assertEquals(lSheetId, lUiSheet.getId());
        assertEquals(SHEET_TYPE_NAME, lUiSheet.getTypeName());
        assertTrue(lUiSheet.isDeletable());
        assertTrue(lUiSheet.isUpdatable());
        assertEquals(DEFAULT_PRODUCT_NAME, lUiSheet.getProductName());
        assertEquals(SHEET_REFERENCE, lUiSheet.getFunctionalReference());
        assertEquals(SHEET_STATE, lUiSheet.getState());

        // Check some fields
        assertEquals(F_STRING_SIMPLE_FIELD_VALUE, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").get());
        assertEquals(F_STRING_SIMPLE_FIELD_DESC, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").getFieldDescription());

        assertEquals(F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_VALUE,
                lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0).get());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_DESC,
                lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0).getFieldDescription());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_WIDTH,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getWidth());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HEIGHT,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getHeight());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HINT_TYPE,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getStringDisplayHintType());

        assertEquals(F_LONG_DISPLAY_HINT_VALUE, lUiSheet.getDateField(
                "LONG_DISPLAY_HINT").get());
        assertEquals(F_LONG_DISPLAY_HINT_DESC, lUiSheet.getDateField(
                "LONG_DISPLAY_HINT").getFieldDescription());
        assertEquals(
                F_LONG_DISPLAY_HINT_HINT_TYPE,
                ((UiDateField) lUiSheet.getDateField("LONG_DISPLAY_HINT")).getDateDisplayHintType());

        //Check that confidential fields are not available
        assertNull(lUiSheet.getStringField("CONFIDENTIAL_FIELD_IN_AUTHORIZATION"));
        assertNull(lUiSheet.getStringField("CONFIDENTIAL_FIELD_IN_SHEET_TYPE"));
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        // Sheet attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiSheet.getBusinessProcessName());
        assertEquals(lSheetId, lUiSheet.getId());
        assertEquals(SHEET_TYPE_NAME, lUiSheet.getTypeName());
        assertTrue(lUiSheet.isDeletable());
        assertTrue(lUiSheet.isUpdatable());
        assertEquals(DEFAULT_PRODUCT_NAME, lUiSheet.getProductName());
        assertEquals(SHEET_REFERENCE, lUiSheet.getFunctionalReference());
        assertEquals(SHEET_STATE, lUiSheet.getState());

        // Check some fields
        assertEquals(F_STRING_SIMPLE_FIELD_VALUE, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").get());
        assertEquals(F_STRING_SIMPLE_FIELD_DESC, lUiSheet.getStringField(
                "STRING_SIMPLE_FIELD").getFieldDescription());

        assertEquals(F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_VALUE,
                lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0).get());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_DESC,
                lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0).getFieldDescription());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_WIDTH,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getWidth());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HEIGHT,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getHeight());
        assertEquals(
                F_MULTIVALUED_MULTI_LINE_DISPLAY_HINT_HINT_TYPE,
                ((UiStringField) lUiSheet.getMultivaluedStringField(
                        "MULTIVALUED_MULTI_LINE_DISPLAY_HINT").get(0)).getStringDisplayHintType());

        assertEquals(F_LONG_DISPLAY_HINT_VALUE, lUiSheet.getDateField(
                "LONG_DISPLAY_HINT").get());
        assertEquals(F_LONG_DISPLAY_HINT_DESC, lUiSheet.getDateField(
                "LONG_DISPLAY_HINT").getFieldDescription());
        assertEquals(
                F_LONG_DISPLAY_HINT_HINT_TYPE,
                ((UiDateField) lUiSheet.getDateField("LONG_DISPLAY_HINT")).getDateDisplayHintType());

        //Check CHOICE TREE FIELD
        BusinessChoiceField lBusinessChoiceField =
                lUiSheet.getChoiceField("TREE_DISPLAY_HINT");
        assertTrue(lBusinessChoiceField instanceof UiChoiceTreeField);
        UiChoiceTreeField lChoiceTreeField =
                (UiChoiceTreeField) lBusinessChoiceField;
        List<UiChoiceTreeFieldValue> lRoots =
                new ArrayList<UiChoiceTreeFieldValue>();
        for (Translation lTrans : lChoiceTreeField.getPossibleValues()) {
            lRoots.add((UiChoiceTreeFieldValue) lTrans);
        }
        checkChoiceTreeValues(staticChoiceTreeValues, lRoots);

        //Check that confidential fields are not available
        assertNull(lUiSheet.getStringField("CONFIDENTIAL_FIELD_IN_AUTHORIZATION"));
        assertNull(lUiSheet.getStringField("CONFIDENTIAL_FIELD_IN_SHEET_TYPE"));
    }
}

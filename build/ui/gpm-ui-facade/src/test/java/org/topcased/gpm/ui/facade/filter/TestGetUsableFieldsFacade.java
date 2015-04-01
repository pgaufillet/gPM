/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetUsableFieldsFacade
 * 
 * @author jlouisy
 */
public class TestGetUsableFieldsFacade extends AbstractFacadeTestCase {

    private static final String SHEET_TYPE_NAME = "SHEET_1";

    private static final String CONFIDENTIAL_SHEET_TYPE_NAME =
            "CONFIDENTIAL_SHEET";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String SHEET_1_SHEET_1_LINK_TYPE_NAME =
            "Sheet1Sheet1Link";

    private static final String SHEET_1_SHEET_1_2_LINK_TYPE_NAME =
            "Sheet1Sheet1Link_2";

    private List<String> getUsableFieldLabels(
            Collection<UiFilterUsableField> pResult) {
        List<String> lResult = new ArrayList<String>();
        for (UiFilterUsableField lUiFilterUsableField : pResult) {
            lResult.add(lUiFilterUsableField.getName());
            List<UiFilterUsableField> lSubFields =
                    lUiFilterUsableField.getSubFields();
            if (lSubFields != null) {
                for (UiFilterUsableField lSubFilterUsableField : lSubFields) {
                    lResult.add("SUBFIELD____"
                            + lSubFilterUsableField.getName());
                }
            }
        }
        return lResult;
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        String lSheet1Id =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(), SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        String lProductId =
                getProductService().getCacheableProductTypeByName(
                        lUiSession.getRoleToken(),
                        lUiSession.getParent().getProcessName(),
                        PRODUCT_TYPE_NAME, CacheProperties.IMMUTABLE).getId();

        String lSheet1Sheet1LinkId =
                getLinkService().getLinkTypeByName(lUiSession.getRoleToken(),
                        SHEET_1_SHEET_1_LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for SHEET_1.");
        }
        Map<String, UiFilterUsableField> lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(Arrays.asList(lSheet1Id)));
        List<String> lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check confidential fields
        assertFalse(lStringResult.contains("CONFIDENTIAL_FIELD_IN_AUTHORIZATION"));
        assertFalse(lStringResult.contains("CONFIDENTIAL_FIELD_IN_SHEET_TYPE"));

        //Check that product, link and linked sheets fields are not retrieved
        assertFalse(lStringResult.contains("PRODUCT_FIELD_1"));
        assertFalse(lStringResult.contains("Sheet1Sheet1_F3"));

        //Check virtual fields
        assertTrue(lStringResult.contains("$SHEET_STATE"));
        assertTrue(lStringResult.contains("$SHEET_TYPE"));
        assertTrue(lStringResult.contains("$SHEET_REFERENCE"));
        assertTrue(lStringResult.contains("$PRODUCT_NAME"));
        assertTrue(lStringResult.contains("$PRODUCT_HIERARCHY"));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for PRODUCT.");
        }
        lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(Arrays.asList(lProductId)));
        lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check subfields of multi fields
        assertTrue(lStringResult.contains("SUBFIELD____PRODUCT_FIELD_1"));
        assertTrue(lStringResult.contains("SUBFIELD____PRODUCT_FIELD_2"));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for SHEET_1-SHEET_1 LINK.");
        }
        lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(
                                Arrays.asList(lSheet1Sheet1LinkId)));
        lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check virtual fields
        assertTrue(lStringResult.contains("$ORIGIN_SHEET_REF"));
        assertTrue(lStringResult.contains("$ORIGIN_PRODUCT"));
        assertTrue(lStringResult.contains("$DEST_SHEET_REF"));
        assertTrue(lStringResult.contains("$DEST_PRODUCT"));
    }

    /**
     * User rights
     */
    public void testRestrictedRightsCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession = loginAsUser().getSession(getProductName());

        String lConfidentialSheetId =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(),
                        CONFIDENTIAL_SHEET_TYPE_NAME, CacheProperties.IMMUTABLE).getId();

        String lProductId =
                getProductService().getCacheableProductTypeByName(
                        lUiSession.getRoleToken(),
                        lUiSession.getParent().getProcessName(),
                        PRODUCT_TYPE_NAME, CacheProperties.IMMUTABLE).getId();

        String lSheet1Sheet12LinkId =
                getLinkService().getLinkTypeByName(lUiSession.getRoleToken(),
                        SHEET_1_SHEET_1_2_LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for CONFIDENTIAL_SHEET.");
        }
        Map<String, UiFilterUsableField> lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(
                                Arrays.asList(lConfidentialSheetId)));
        List<String> lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check confidential fields
        assertFalse(lStringResult.contains("REF_id"));

        //Check virtual fields
        assertTrue(lStringResult.contains("$SHEET_STATE"));
        assertTrue(lStringResult.contains("$SHEET_TYPE"));
        assertTrue(lStringResult.contains("$SHEET_REFERENCE"));
        assertTrue(lStringResult.contains("$PRODUCT_NAME"));
        assertTrue(lStringResult.contains("$PRODUCT_HIERARCHY"));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for PRODUCT.");
        }
        lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(Arrays.asList(lProductId)));
        lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check subfields of multi fields
        assertTrue(lStringResult.contains("SUBFIELD____PRODUCT_FIELD_1"));
        assertTrue(lStringResult.contains("SUBFIELD____PRODUCT_FIELD_2"));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Usable Fields for SHEET_1-SHEET_1-2 LINK.");
        }
        lResult =
                lFilterFacade.getUsableFields(lUiSession,
                        new ArrayList<String>(
                                Arrays.asList(lSheet1Sheet12LinkId)));
        lStringResult = getUsableFieldLabels(lResult.values());

        for (String lStr : lStringResult) {
            System.out.println(lStr);
        }

        //Check virtual fields
        assertTrue(lStringResult.contains("$ORIGIN_SHEET_REF"));
        assertTrue(lStringResult.contains("$ORIGIN_PRODUCT"));
        assertTrue(lStringResult.contains("$DEST_SHEET_REF"));
        assertTrue(lStringResult.contains("$DEST_PRODUCT"));

        logoutAsUser(lUiSession.getParent());
    }
}

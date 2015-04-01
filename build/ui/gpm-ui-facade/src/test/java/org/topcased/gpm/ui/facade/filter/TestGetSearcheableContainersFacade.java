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
import java.util.List;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * TestGetUsableFieldsFacade
 * 
 * @author jlouisy
 */
public class TestGetSearcheableContainersFacade extends AbstractFacadeTestCase {

    private static final String[] EXPECTED_SEARCHEABLE_SHEETS_ADMIN_ROLE =
            { "SHEET_1", "CONFIDENTIAL_SHEET" };

    private static final String[] EXPECTED_SEARCHEABLE_LINKS_ADMIN_ROLE =
            { "Sheet1Sheet1Link", "Sheet1Sheet1Link_2",
             "Sheet1ConfidentialSheetLink", "ProductProductLink" };

    private static final String[] EXPECTED_SEARCHEABLE_LINKS_USER_ROLE =
            { "Sheet1Sheet1Link", "Sheet1Sheet1Link_2",
             "Sheet1ConfidentialSheetLink" };

    /**
     * Normal case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable products.");
        }
        List<UiFilterContainerType> lSearcheableProducts =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.PRODUCT);
        assertEquals(1, lSearcheableProducts.size());
        assertEquals("PRODUCT",
                lSearcheableProducts.get(0).getName().getValue());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable sheets.");
        }
        List<UiFilterContainerType> lSearcheableSheets =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.SHEET);
        assertEquals(2, lSearcheableSheets.size());
        List<String> lContainerNames = new ArrayList<String>();
        for (UiFilterContainerType lContainerType : lSearcheableSheets) {
            lContainerNames.add(lContainerType.getName().getValue());
        }
        assertTrue(lContainerNames.containsAll(Arrays.asList(EXPECTED_SEARCHEABLE_SHEETS_ADMIN_ROLE)));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable links.");
        }
        List<UiFilterContainerType> lSearcheableLinks =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.LINK);
        lContainerNames = new ArrayList<String>();
        for (UiFilterContainerType lContainerType : lSearcheableLinks) {
            lContainerNames.add(lContainerType.getName().getValue());
        }
        assertEquals(4, lSearcheableLinks.size());
        assertTrue(lContainerNames.containsAll(Arrays.asList(EXPECTED_SEARCHEABLE_LINKS_ADMIN_ROLE)));
    }

    /**
     * User rights Case
     */
    public void testRestrictedRightsCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession = loginAsUser().getSession(getProductName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable products.");
        }
        List<UiFilterContainerType> lSearcheableProducts =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.PRODUCT);
        assertEquals(0, lSearcheableProducts.size());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable sheets.");
        }
        List<UiFilterContainerType> lSearcheableSheets =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.SHEET);
        assertEquals(1, lSearcheableSheets.size());
        assertEquals("SHEET_1", lSearcheableSheets.get(0).getName().getValue());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get searcheable links.");
        }
        List<UiFilterContainerType> lSearcheableLinks =
                lFilterFacade.getSearcheableContainers(lUiSession,
                        FilterType.LINK);
        List<String> lContainerNames = new ArrayList<String>();
        for (UiFilterContainerType lContainerType : lSearcheableLinks) {
            lContainerNames.add(lContainerType.getName().getValue());
        }
        assertEquals(3, lSearcheableLinks.size());
        assertTrue(lContainerNames.containsAll(Arrays.asList(EXPECTED_SEARCHEABLE_LINKS_USER_ROLE)));

        logoutAsUser(lUiSession.getParent());
    }
}

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

import java.util.List;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;

/**
 * TestGetAvailableVisibilitiesFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableVisibilitiesFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Available Visibilities.");
        }
        List<UiFilterVisibility> lResult =
                lFilterFacade.getAvailableVisibilities(lUiSession, FilterType.SHEET);

        assertEquals(3, lResult.size());
        assertTrue(lResult.contains(UiFilterVisibility.INSTANCE));
        assertTrue(lResult.contains(UiFilterVisibility.PRODUCT));
        assertTrue(lResult.contains(UiFilterVisibility.USER));
    }

    /**
     * Rights on filter
     */
    public void testRestrictedAccessCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Available Visibilities.");
        }
        List<UiFilterVisibility> lResult =
                lFilterFacade.getAvailableVisibilities(lUiSession, FilterType.SHEET);

        assertEquals(1, lResult.size());
        assertFalse(lResult.contains(UiFilterVisibility.INSTANCE));
        assertFalse(lResult.contains(UiFilterVisibility.PRODUCT));
        assertTrue(lResult.contains(UiFilterVisibility.USER));

    }
}

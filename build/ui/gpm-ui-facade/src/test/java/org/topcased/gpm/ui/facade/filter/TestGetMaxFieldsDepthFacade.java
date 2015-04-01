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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;

/**
 * TestGetMaxFieldsDepthFacade
 * 
 * @author jlouisy
 */
public class TestGetMaxFieldsDepthFacade extends AbstractFacadeTestCase {

    private static final int MAX_DEPTH = 42;

    /**
     * Test the method get max depth
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Set max depth.");
        }
        getSearchService().setMaxFieldsDepth(MAX_DEPTH);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get max depth.");
        }
        int lDepth = lFilterFacade.getMaxFieldsDepth();

        assertEquals(MAX_DEPTH, lDepth);
    }
}

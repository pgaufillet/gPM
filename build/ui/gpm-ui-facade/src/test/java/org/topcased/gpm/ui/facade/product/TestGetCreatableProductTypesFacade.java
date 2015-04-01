/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.product;

import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;

/**
 * ProductFacade.getCreatableProductTypes test
 * 
 * @author nveillet
 */
public class TestGetCreatableProductTypesFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_TYPE = "PRODUCT";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession =
                getAdminUserSession().getDefaultSession(getProductName());
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get creatable product types
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get creatable product types.");
        }
        List<String> lCreatableProductTypes =
                lProductFacade.getCreatableProductTypes(lSession);

        assertEquals(1, lCreatableProductTypes.size());

        assertEquals(PRODUCT_TYPE, lCreatableProductTypes.get(0));
    }

}

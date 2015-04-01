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

import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;

/**
 * ProductFacade.getProductName test
 * 
 * @author nveillet
 */
public class TestGetProductNameFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_NAME = "ROOT_PRODUCT";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lSession.getRoleToken(),
                        PRODUCT_NAME);

        // get product name
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get product name.");
        }
        String lProductName =
                lProductFacade.getProductName(lSession, lProductId);

        assertEquals(PRODUCT_NAME, lProductName);
    }

}

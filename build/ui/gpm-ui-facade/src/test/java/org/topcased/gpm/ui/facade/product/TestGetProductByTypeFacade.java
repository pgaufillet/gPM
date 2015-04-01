/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.product;

import java.util.Collections;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * SheetFacade.getSheetByType test
 * 
 * @author jlouisy
 */
public class TestGetProductByTypeFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String PRODUCT_TYPE_DESC = "product_desc";

    private static final List<String> ENVIRONMENT_NAMES =
            Collections.singletonList("default");

    private static final String TEMP_PRODUCT_NAME = "* " + PRODUCT_TYPE_NAME;

    /**
     * Confidential product case
     */
    public void testConfidentialProduct() {
        //login as USER
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Empty Confidential Product.");
        }
        try {
            lProductFacade.getProductByType(lUiSession, PRODUCT_TYPE_NAME,
                    ENVIRONMENT_NAMES);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to this type 'PRODUCT' : "
                            + "the access is confidential ";
            String lExceptionMessage = lException.getMessage();
            assertEquals("Bad exception.", lMessage, lExceptionMessage);

            //logout USER
            logoutAsUser(lUiSession.getParent());
        }
    }

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get empty product.");
        }
        UiProduct lUiProduct =
                lProductFacade.getProductByType(lUiSession, PRODUCT_TYPE_NAME,
                        ENVIRONMENT_NAMES);

        assertEquals(DEFAULT_PROCESS_NAME, lUiProduct.getBusinessProcessName());
        assertEquals(PRODUCT_TYPE_NAME, lUiProduct.getTypeName());
        assertTrue(lUiProduct.isDeletable());
        assertTrue(lUiProduct.isUpdatable());
        assertEquals(PRODUCT_TYPE_DESC, lUiProduct.getTypeDescription());
        assertEquals(TEMP_PRODUCT_NAME, lUiProduct.getName());
    }
}

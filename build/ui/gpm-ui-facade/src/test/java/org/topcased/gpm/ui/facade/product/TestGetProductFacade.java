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

import java.text.SimpleDateFormat;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * ProductFacade.getProductByType test
 * 
 * @author jlouisy
 */
public class TestGetProductFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_NAME = "ROOT_PRODUCT";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String PRODUCT_TYPE_DESC = "product_desc";

    private static final String PRODUCT_MULTI = "PRODUCT_MULTI";

    private static final String PRODUCT_FIELD_1 = "PRODUCT_FIELD_1";

    private static final String PRODUCT_FIELD_2 = "PRODUCT_FIELD_2";

    private static final String PRODUCT_NAME_FIELD = "PRODUCT_NAME";

    /**
     * Confidential product case
     */
    public void testConfidentialProduct() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Confidential Product.");
        }
        try {
            lProductFacade.getProduct(lUiSession, lProductId,
                    DisplayMode.EDITION);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the product " + lProductId
                            + " : the access is confidential ";
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
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Product.");
        }
        UiProduct lUiProduct =
                lProductFacade.getProduct(lUiSession, lProductId,
                        DisplayMode.EDITION);

        // Product attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiProduct.getBusinessProcessName());
        assertEquals(lProductId, lUiProduct.getId());
        assertEquals(PRODUCT_TYPE_NAME, lUiProduct.getTypeName());
        assertTrue(lUiProduct.isDeletable());
        assertTrue(lUiProduct.isUpdatable());
        assertEquals(PRODUCT_TYPE_DESC, lUiProduct.getTypeDescription());
        assertEquals(DEFAULT_PRODUCT_NAME, lUiProduct.getName());

        // Check some fields
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        BusinessMultivaluedField<BusinessMultipleField> lMultivaluedField =
                lUiProduct.getMultivaluedMultipleField(PRODUCT_MULTI);
        assertEquals(3, lMultivaluedField.size());

        String[] lField1Values =
                new String[] { "field 1", "field 1 bis", "field 1 ter" };
        String[] lField2Values =
                new String[] { "2010-06-23", "2010-06-24", "2019-06-23" };
        BusinessStringField lField1;
        BusinessDateField lField2;

        for (int i = 0; i < lMultivaluedField.size(); i++) {
            lField1 = lMultivaluedField.get(i).getStringField(PRODUCT_FIELD_1);
            lField2 = lMultivaluedField.get(i).getDateField(PRODUCT_FIELD_2);

            assertEquals(lField1Values[i], lField1.get());
            assertEquals(lField2Values[i],
                    lSimpleDateFormat.format(lField2.get()));
        }

        assertEquals("Root Product", lUiProduct.getStringField(
                PRODUCT_NAME_FIELD).get());
    }
}

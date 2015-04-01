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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * ProductFacade.getProduct test
 * 
 * @author jlouisy
 */
public class TestUpdateProductFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_NAME = "ROOT_PRODUCT";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String PRODUCT_TYPE_DESC = "product_desc";

    private static final String PRODUCT_MULTI = "PRODUCT_MULTI";

    private static final String PRODUCT_FIELD_1 = "PRODUCT_FIELD_1";

    private static final String PRODUCT_NAME_FIELD = "PRODUCT_NAME";

    private static final String NEW_VALUE = "new value";

    /**
     * Confidential product case
     */
    public void testConfidentialProduct() {
        //login as USER
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update Confidential Product.");
        }
        try {
            lProductFacade.updateProduct(lUiSession, lProductId, null,
                    new ArrayList<UiField>(), new ArrayList<String>(),
                    new ArrayList<String>());
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage = "Unsufficient rights to update product.";
            assertEquals("Bad exception.", lMessage, lException.getMessage());

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

        // Get product
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Modify fields.");
        }
        List<UiField> lUpdatedFields = new ArrayList<UiField>();

        BusinessStringField lField1 =
                lUiProduct.getStringField(PRODUCT_NAME_FIELD);
        lField1.set(NEW_VALUE);
        lUpdatedFields.add((UiField) lField1);

        BusinessMultivaluedField<BusinessMultipleField> lField2 =
                lUiProduct.getMultivaluedMultipleField(PRODUCT_MULTI);
        lField2.get(0).getStringField(PRODUCT_FIELD_1).set(NEW_VALUE);
        lUpdatedFields.add((UiField) lField2);

        // Update
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update Product.");
        }

        // There is no parent as it is the root product. 
        List<String> lParentList = new ArrayList<String>();

        List<String> lChildrenList = new ArrayList<String>();
        lChildrenList.add("CHILD_PRODUCT_1");
        lChildrenList.add("CHILD_PRODUCT_2");

        lProductFacade.updateProduct(lUiSession, lProductId, null,
                lUpdatedFields, lParentList, lChildrenList);

        // Get new product
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get updated product.");
        }
        lUiProduct =
                lProductFacade.getProduct(lUiSession, lProductId,
                        DisplayMode.EDITION);

        // Product attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiProduct.getBusinessProcessName());
        assertEquals(lProductId, lUiProduct.getId());
        assertEquals(PRODUCT_TYPE_NAME, lUiProduct.getTypeName());
        assertTrue(lUiProduct.isDeletable());
        assertTrue(lUiProduct.isUpdatable());
        assertEquals(PRODUCT_TYPE_DESC, lUiProduct.getTypeDescription());
        assertEquals(PRODUCT_NAME, lUiProduct.getName());

        //Check hierarchy

        // Check some fields
        assertEquals(NEW_VALUE,
                lUiProduct.getStringField(PRODUCT_NAME_FIELD).get());
        assertEquals(NEW_VALUE, lUiProduct.getMultivaluedMultipleField(
                PRODUCT_MULTI).get(0).getStringField(PRODUCT_FIELD_1).get());
    }

}

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
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
public class TestCreateProductFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final List<String> ENVIRONMENT_NAMES =
            Collections.singletonList("default");

    private static final String PRODUCT_MULTI_FIELD = "PRODUCT_MULTI";

    private static final String PRODUCT_MULTI_FIELD_DESC = "multi field desc";

    private static final String PRODUCT_STRING_FIELD = "PRODUCT_FIELD_1";

    private static final String PRODUCT_DATE_FIELD = "PRODUCT_FIELD_2";

    private static final String PRODUCT_STRING_FIELD_NEW_VALUE = "New Value";

    private static final String NEW_PRODUCT_NAME = "NEW_PRODUCT";

    private static final String PRODUCT_TYPE_DESC = "product_desc";

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Product creation
        UiProduct lUiProduct =
                lProductFacade.getProductByType(lUiSession, PRODUCT_TYPE_NAME,
                        ENVIRONMENT_NAMES);
        String lProductId = lUiProduct.getId();

        List<UiField> lUpdatedFields = new ArrayList<UiField>();

        BusinessMultivaluedField<BusinessMultipleField> lField =
                lUiProduct.getMultivaluedMultipleField(PRODUCT_MULTI_FIELD);
        lField.get(0).getStringField(PRODUCT_STRING_FIELD).set(
                PRODUCT_STRING_FIELD_NEW_VALUE);
        lField.get(0).getDateField(PRODUCT_DATE_FIELD).set(new Date());

        lUpdatedFields.add((UiField) lField);

        // Creation
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Product.");
        }
        List<String> lParentList = new ArrayList<String>();
        lParentList.add("ROOT_PRODUCT");
        List<String> lChildrenList = new ArrayList<String>();
        lChildrenList.add("CHILD_PRODUCT_1");
        lChildrenList.add("CHILD_PRODUCT_2");

        lProductFacade.createProduct(lUiSession, lUiProduct.getId(),
                NEW_PRODUCT_NAME, null, lUpdatedFields, lParentList,
                lChildrenList);

        // Get new product
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get created Product.");
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
        assertEquals(NEW_PRODUCT_NAME, lUiProduct.getName());

        //Check hierarchy

        // Check some fields
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        assertEquals(
                PRODUCT_STRING_FIELD_NEW_VALUE,
                lUiProduct.getMultivaluedMultipleField(PRODUCT_MULTI_FIELD).get(
                        0).getStringField(PRODUCT_STRING_FIELD).get());
        assertEquals(
                lSimpleDateFormat.format(new Date()),
                lSimpleDateFormat.format(lUiProduct.getMultivaluedMultipleField(
                        PRODUCT_MULTI_FIELD).get(0).getDateField(
                        PRODUCT_DATE_FIELD).get()));
        assertEquals(
                PRODUCT_MULTI_FIELD_DESC,
                lUiProduct.getMultivaluedMultipleField(PRODUCT_MULTI_FIELD).getFieldDescription());
    }

    /**
     * Product not in cache case
     */
    public void testProductNotInCacheCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Product creation
        UiProduct lUiProduct =
                lProductFacade.getProductByType(lUiSession, PRODUCT_TYPE_NAME,
                        ENVIRONMENT_NAMES);
        String lProductId = lUiProduct.getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Empty cache.");
        }
        lProductFacade.clearCache(lUiSession, lProductId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create product.");
        }
        try {
            lProductFacade.createProduct(lUiSession, lUiProduct.getId(),
                    NEW_PRODUCT_NAME, null, new ArrayList<UiField>(),
                    new ArrayList<String>(), new ArrayList<String>());
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the product " + lProductId
                            + " : the product does not exist in user cache";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }
    }

}

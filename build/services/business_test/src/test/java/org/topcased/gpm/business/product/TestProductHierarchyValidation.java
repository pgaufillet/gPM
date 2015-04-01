/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/
package org.topcased.gpm.business.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Validation of product hierarchy
 * 
 * @author phtsaan
 */
public class TestProductHierarchyValidation extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    private static final String PRODUCT_TYPE_NAME = "Store";

    private static final List<String> ENVIRONMENT_NAMES =
            Collections.singletonList(GpmTestValues.ENVIRONMENT_PROFESSIONAL);

    /** 1. Data for product relationship persistence during creation */
    private static final String[] PRODUCT_CREATION_NAME = { "productItSelf" };

    private static final String[] PRODUCT_CREATION_PARENT_LIST =
            { "parent2Product1", "parent3Product1", "parent1Product1",
             "parent4Product1" };

    private static final String[] PRODUCT_CREATION_CHILDREN_LIST =
            { "child1Product1", "child2Product1", "child3Product1" };

    private static final String[] PRODUCT_TO_CREATIE_LIST =
            { "parent1Product1", "parent2Product1", "parent3Product1",
             "parent4Product1", "child1Product1", "child2Product1",
             "child3Product1" };

    /** 2. Data for basic relationship safety */
    private static final String[] PRODUCT_BASIC_CHECK_NAME = { "aProduct" };

    /** 3. Data for level 1 reflexivity */
    private static final String[] PRODUCT_LEVEL_1_CHECK_PARENT_LIST =
            { "theProduct" };

    private static final String[] PRODUCT_LEVEL_1_CHECK_CHILDREN_LIST =
            { "itsChild" };

    /** 4. Data for level n reflexivity, n in Integer */
    private static final String[] PRODUCT_LEVEL_N_CHECK_NAME =
            { "pp", "cc", "aa", "zz", "bb", "ff" };

    private static final Map<String, String> PRODUCT_CACHE =
            new HashMap<String, String>(PRODUCT_LEVEL_N_CHECK_NAME.length);

    /** Data mapping for level n reflexivity */
    // mapping for product pp
    private static final String[] PP_NAME = { "pp" };

    private static final String[] PP_PARENT = {};

    private static final String[] PP_CHILDREN = { "cc", "aa", "zz" };

    // mapping for product cc
    private static final String[] CC_NAME = { "cc" };

    private static final String[] CC_PARENT = { "pp" };

    private static final String[] CC_CHILDREN = { "aa" };

    // mapping for product aa
    private static final String[] AA_NAME = { "aa" };

    private static final String[] AA_PARENT = { "pp", "cc" };

    private static final String[] AA_CHILDREN = { "zz", "bb" };

    // mapping for product bb
    private static final String[] BB_NAME = { "bb" };

    private static final String[] BB_PARENT = { "aa" };

    private static final String[] BB_CHILDREN = { "zz", "ff" };

    /** Data for level n reflexivity */
    // while creating a product
    private static final String[] TT_CREATION_NAME = { "tt" };

    private static final String[] TT_CREATION_PARENT = { "bb", "zz" };

    private static final String[] TT_CREATION_CHILDREN = { "pp", "cc", "aa" };

    // while reverse updating a product
    private static final String[] TT_REVERSE_UPDATE_NAME = { "tt" };

    private static final String[] TT_REVERSE_UPDATE_PARENT =
            { "pp", "cc", "aa" };

    private static final String[] TT_REVERSE_UPDATE_CHILDREN = { "bb", "zz" };

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        productService = serviceLocator.getProductService();
    }

    /**
     * <B>1. Data for product relationship persistence during creation</B></br>
     * 1.1 create product to set as relationship</br> 1.2 create the product
     * with relationship</br> 1.3 retrieve relationship </br>
     */
    public void testProductRelationPersistence() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        for (String lName : PRODUCT_TO_CREATIE_LIST) {
            final CacheableProduct lProductModel =
                    productService.getProductModel(adminRoleToken, lType,
                            ENVIRONMENT_NAMES, null);
            lProductModel.setProductName(lName);
            serviceLocator.getProductService().createProduct(adminRoleToken,
                    lProductModel, null);
        }
        final CacheableProduct lModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lModel.setProductName(PRODUCT_CREATION_NAME[0]);
        lModel.setChildrenNames(reverseCopyValue(PRODUCT_CREATION_CHILDREN_LIST));

        final String lProductId =
                serviceLocator.getProductService().createProduct(
                        adminRoleToken, lModel, null);
        serviceLocator.getProductService().setProductParents(getProcessName(),
                PRODUCT_CREATION_NAME[0],
                reverseCopyValue(PRODUCT_CREATION_PARENT_LIST));

        final CacheableProduct lNewProduct =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lProductId, CacheProperties.IMMUTABLE);
        /* test if the children and parent list are equal to the expected values */
        List<String> lPrents =
                serviceLocator.getProductService().getProductParentsNames(
                        getProcessName(), lNewProduct.getProductName());
        List<String> lChildren = lNewProduct.getChildrenNames();
        assertEquals(PRODUCT_CREATION_PARENT_LIST.length, lPrents.size());
        for (String lValue : PRODUCT_CREATION_PARENT_LIST) {
            assertEquals(true, lPrents.contains(lValue));
        }
        assertEquals(PRODUCT_CREATION_CHILDREN_LIST.length, lChildren.size());
        for (String lValue : PRODUCT_CREATION_CHILDREN_LIST) {
            assertEquals(true, lChildren.contains(lValue));
        }
    }

    /**
     * <b>2. test the product relationship with itself</b> </br> 2.1
     * relationship with product which doesn't exist :
     * <CODE>testCreateProductRelationWithItSelf() </CODE></br> 2.2 relationship
     * with product which already exist :
     * <CODE>testUpdateProductRelationWithItSelf() </CODE>
     */
    public void testCreateProductRelationWithItSelf() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lModel.setProductName(PRODUCT_BASIC_CHECK_NAME[0]);
        lModel.setChildrenNames(reverseCopyValue(PRODUCT_BASIC_CHECK_NAME));
        lModel.setParentNames(reverseCopyValue(PRODUCT_BASIC_CHECK_NAME));
        try {
            serviceLocator.getProductService().createProduct(adminRoleToken,
                    lModel, null);
        }
        catch (GDMException ex) {
            // exception for product which doesn't exist
            assertEquals(GDMException.class, ex.getClass());
        }
    }

    public void testUpdateProductRelationWithItSelf() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lModel.setProductName(PRODUCT_BASIC_CHECK_NAME[0]);
        serviceLocator.getProductService().createProduct(adminRoleToken,
                lModel, null);

        lModel.setChildrenNames(reverseCopyValue(PRODUCT_BASIC_CHECK_NAME));
        lModel.setParentNames(reverseCopyValue(PRODUCT_BASIC_CHECK_NAME));

        try {
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lModel, null);
        }
        catch (GDMException ex) {
            // exception for product relationship with itself
            assert true;
        }
    }

    /**
     * <b> 3. Test for level 1 reflexivity </b></br> 3.1 create parent and child
     * relation between 2 products </br> 3.2 create reversed relation between
     * the 2: GDMException should be risen here</br>
     */
    public void testLevelOneRelation() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lModel1 =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lModel1.setProductName(PRODUCT_LEVEL_1_CHECK_CHILDREN_LIST[0]);
        final String lChildProductId =
                serviceLocator.getProductService().createProduct(
                        adminRoleToken, lModel1, null);

        final CacheableProduct lModel2 =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lModel2.setProductName(PRODUCT_LEVEL_1_CHECK_PARENT_LIST[0]);
        lModel2.setChildrenNames(reverseCopyValue(PRODUCT_LEVEL_1_CHECK_CHILDREN_LIST));
        serviceLocator.getProductService().createProduct(adminRoleToken,
                lModel2, null);

        // retrieves the child 
        final CacheableProduct lChildProduct =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lChildProductId,
                        CacheProperties.MUTABLE);
        lChildProduct.setChildrenNames(reverseCopyValue(PRODUCT_LEVEL_1_CHECK_PARENT_LIST));

        try {
            serviceLocator.getProductService().createProduct(adminRoleToken,
                    lChildProduct, null);
        }
        catch (GDMException ex) {
            assert true;
        }

    }

    /**
     * <b> 4. Test for level n reflexivity </b></br> 4.1 create products :
     * <CODE>testCreateProductForReflexivity()</CODE> </br> 4.2 update products
     * including mapping: <CODE>mapProductRelation()</CODE></br> 4.3 catch bad
     * mapping while creation : <CODE>badMappingWhileCreation()</CODE></br> 4.4
     * catch bad mapping while updating : <CODE>badMappingWhileUpdating()</CODE>
     * </br> 4.5 catch bad mapping while reversed updating :
     * <CODE>badMappingWhileReversedUpdating()</CODE></br>
     */
    public void testCreateProductForReflexivity() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        for (String lName : PRODUCT_LEVEL_N_CHECK_NAME) {
            final CacheableProduct lProductModel =
                    productService.getProductModel(adminRoleToken, lType,
                            ENVIRONMENT_NAMES, null);
            lProductModel.setProductName(lName);
            final String lCurrentProductId =
                    serviceLocator.getProductService().createProduct(
                            adminRoleToken, lProductModel, null);
            PRODUCT_CACHE.put(lName, lCurrentProductId);
        }

        mapProductRelation();
    }

    private void mapProductRelation() {
        // mapping for product pp
        String lPPId = PRODUCT_CACHE.get(PP_NAME[0]);
        final CacheableProduct lPp =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lPPId, CacheProperties.MUTABLE);
        lPp.setChildrenNames(reverseCopyValue(PP_CHILDREN));
        lPp.setParentNames(reverseCopyValue(PP_PARENT));
        try {
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lPp, null);
            assert true;
        }
        catch (GDMException ex) {
            assert false;
        }

        // mapping for product cc
        String lCCId = PRODUCT_CACHE.get(CC_NAME[0]);
        final CacheableProduct lCc =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lCCId, CacheProperties.MUTABLE);
        lCc.setChildrenNames(reverseCopyValue(CC_CHILDREN));
        lCc.setParentNames(reverseCopyValue(CC_PARENT));

        try {
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lCc, null);
            assert true;
        }
        catch (GDMException ex) {
            assert false;
        }

        // mapping for product aa
        String lAAId = PRODUCT_CACHE.get(AA_NAME[0]);
        final CacheableProduct lAa =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lAAId, CacheProperties.MUTABLE);
        lAa.setChildrenNames(reverseCopyValue(AA_CHILDREN));
        lAa.setParentNames(reverseCopyValue(AA_PARENT));

        try {
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lAa, null);
            assert true;
        }
        catch (GDMException ex) {
            assert false;
        }

        // mapping for product bb
        String lBBId = PRODUCT_CACHE.get(BB_NAME[0]);
        final CacheableProduct lBb =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lBBId, CacheProperties.MUTABLE);
        lBb.setChildrenNames(reverseCopyValue(BB_CHILDREN));
        lBb.setParentNames(reverseCopyValue(BB_PARENT));

        try {
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lBb, null);
            assert true;
        }
        catch (GDMException ex) {
            assert false;
        }
    }

    public void testBadMappingWhileCreation() {
        // while creating a product
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lProductModel.setProductName(TT_CREATION_NAME[0]);
        lProductModel.setChildrenNames(reverseCopyValue(TT_CREATION_CHILDREN));
        try {
            serviceLocator.getProductService().createProduct(adminRoleToken,
                    lProductModel, null);
            productService.setProductParents(getProcessName(),
                    TT_CREATION_NAME[0], reverseCopyValue(TT_CREATION_PARENT));
            fail(" >> testBadMappingWhileCreation: The exception has not been thrown.");
        }
        catch (GDMException ex) {
            assert true;
        }
    }

    public void testBadMappingWhileUpdating() {
        // while creating a product
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lProductModel.setProductName(TT_CREATION_NAME[0]);
        try {

            final String lCurrentProductId =
                    serviceLocator.getProductService().createProduct(
                            adminRoleToken, lProductModel, null);
            CacheableProduct lCurrentProduct =
                    serviceLocator.getProductService().getCacheableProduct(
                            adminRoleToken, lCurrentProductId,
                            CacheProperties.MUTABLE);
            lCurrentProduct.setParentNames(reverseCopyValue(TT_CREATION_PARENT));
            lCurrentProduct.setChildrenNames(reverseCopyValue(TT_CREATION_CHILDREN));
            productService.setProductParents(getProcessName(),
                    lCurrentProduct.getProductName(),
                    reverseCopyValue(TT_CREATION_PARENT));
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lCurrentProduct, null);
            fail(" >> testBadMappingWhileUpdating: The exception has not been thrown.");
        }
        catch (GDMException ex) {
            assert true;
        }
    }

    public void testBadMappingWhileReversedUpdating() {
        // while reverse updating a product
        // while creating a product
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        lProductModel.setProductName(TT_REVERSE_UPDATE_NAME[0]);

        try {

            final String lCurrentProductId =
                    serviceLocator.getProductService().createProduct(
                            adminRoleToken, lProductModel, null);

            CacheableProduct lCurrentProduct =
                    serviceLocator.getProductService().getCacheableProduct(
                            adminRoleToken, lCurrentProductId,
                            CacheProperties.MUTABLE);
            lCurrentProduct.setParentNames(reverseCopyValue(TT_REVERSE_UPDATE_PARENT));
            lCurrentProduct.setChildrenNames(reverseCopyValue(TT_REVERSE_UPDATE_CHILDREN));
            serviceLocator.getProductService().updateProduct(adminRoleToken,
                    lCurrentProduct, null);
            assert true;

        }
        catch (GDMException ex) {
            assert false;
        }
    }

    private List<String> reverseCopyValue(String[] pValues) {
        List<String> lreturns = new ArrayList<String>(pValues.length);
        for (String lValue : pValues) {
            lreturns.add(lValue);
        }
        return lreturns;
    }
}

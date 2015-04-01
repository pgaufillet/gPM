/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.product.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ProductHierarchyException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.product.BusinessProduct;
import org.topcased.gpm.business.values.product.impl.cacheable.CacheableProductAccess;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.exception.UiBusinessException;
import org.topcased.gpm.ui.facade.shared.exception.UiUnexpectedException;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * ProductFacade
 * 
 * @author nveillet
 */
public class ProductFacadeImpl extends AbstractFacade implements ProductFacade {

    /**
     * Add a product to cache
     * 
     * @param pSession
     *            Current user session
     * @param pProduct
     *            the product
     */
    private void addToCache(UiSession pSession, CacheableProduct pProduct) {
        setValuesContainerId(pProduct);
        setTemporaryFunctionalReference(pSession, pProduct);
        getUserCacheManager().getUserCache(pSession.getParent()).getProductCache().put(
                pProduct.getId(), pProduct);
    }

    /**
     * Clear a product from cache
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            the product identifier
     */
    public void clearCache(UiSession pSession, String pProductId) {
        getUserCacheManager().getUserCache(pSession.getParent()).getProductCache().remove(
                pProductId);
    }

    /**
     * count sheets on Product
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            Product to count sheets on
     * @return number of sheets on this product
     */
    public int countSheets(UiSession pSession, String pProductId) {
        return getProductService().getSheetCount(pSession.getRoleToken(),
                pProductId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.product.ProductFacade#createProduct(org.topcased.gpm.ui.facade.server.authorization.UiSession,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.util.List, java.util.List, java.util.List)
     */
    public void createProduct(UiSession pSession, String pProductId,
            String pProductName, String pDescription,
            List<UiField> pFieldsList, List<String> pParentNamesList,
            List<String> pChildrenNamesList) {
        /* make sure that Parent Names List and Children Names List are different each other*/
        if (pParentNamesList.size() < pChildrenNamesList.size()) {
            performProductRelationShip(pParentNamesList, pChildrenNamesList,
                    pProductName);
        }
        else {
            performProductRelationShip(pChildrenNamesList, pParentNamesList,
                    pProductName);
        }

        CacheableProduct lCacheableProduct = getFromCache(pSession, pProductId);
        lCacheableProduct.setDescription(pDescription);
        // set product children and parent List.
        lCacheableProduct.setParentNames(pParentNamesList);
        lCacheableProduct.setChildrenNames(pChildrenNamesList);
        fillCacheableProduct(pSession, lCacheableProduct, pFieldsList);

        // set product name
        lCacheableProduct.setProductName(pProductName);

        getProductService().createProduct(pSession.getRoleToken(), lCacheableProduct, getContext(pSession));
        // Set parents products
        if (!lCacheableProduct.getParentNames().isEmpty()) {
            getProductService().setProductParents(
                    pSession.getParent().getProcessName(),
                    lCacheableProduct.getProductName(),
                    lCacheableProduct.getParentNames());

        }

        // Set children products
        if (!lCacheableProduct.getChildrenNames().isEmpty()) {
            List<String> lParent =
                    Collections.singletonList(lCacheableProduct.getProductName());
            for (String lChild : lCacheableProduct.getChildrenNames()) {
                getProductService().setProductParents(
                        pSession.getParent().getProcessName(), lChild, lParent);
            }
        }

        clearCache(pSession, pProductId);
    }

    /**
     * Delete Product in database
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            Id of the Product to delete
     * @param pDeleteSubProducts
     *            should sub-products also be deleted ?
     */
    public void deleteProduct(UiSession pSession, String pProductId,
            boolean pDeleteSubProducts) {
        getProductService().deleteProduct(pSession.getRoleToken(), pProductId,
                pDeleteSubProducts, getContext(pSession));
    }

    private void fillCacheableProduct(UiSession pSession,
            CacheableProduct pCacheableProduct, List<UiField> pFieldsList) {

        String lRoleToken = pSession.getRoleToken();

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        pCacheableProduct.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        pCacheableProduct.getId()));

        CacheableProductType lCacheableProductType =
                getProductService().getCacheableProductTypeByName(lRoleToken,
                        pSession.getParent().getProcessName(),
                        pCacheableProduct.getTypeName(), lProperties);

        BusinessProduct lBusinessProduct =
                new CacheableProductAccess(lRoleToken, lCacheableProductType,
                        pCacheableProduct,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        //Container attributes and fields
        initFieldsContainer(lBusinessProduct, pFieldsList);
    }

    /**
     * Get creatable product types
     * 
     * @param pSession
     *            the session
     * @return the creatable product type names
     */
    public List<String> getCreatableProductTypes(UiSession pSession) {
        List<CacheableFieldsContainer> lCacheableProductTypes =
                getFieldsContainerService().getFieldsContainer(
                        pSession.getRoleToken(),
                        ProductType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.CREATE);

        ArrayList<String> lProductTypeNames =
                new ArrayList<String>(lCacheableProductTypes.size());
        for (CacheableFieldsContainer lCacheableSheetType : lCacheableProductTypes) {
            lProductTypeNames.add(lCacheableSheetType.getName());
        }

        Collections.sort(lProductTypeNames);

        return lProductTypeNames;
    }

    /**
     * Get a product from cache
     * 
     * @param pSession
     *            the session
     * @param pProductId
     *            the product id
     * @return product from cache
     */
    private CacheableProduct getFromCache(UiSession pSession, String pProductId) {
        CacheableProduct lCacheableProduct =
                getUserCacheManager().getUserCache(pSession.getParent()).getProductCache().get(
                        pProductId);
        if (lCacheableProduct == null) {
            throw new AuthorizationException("Illegal access to the product "
                    + pProductId
                    + " : the product does not exist in user cache");
        }
        return lCacheableProduct;
    }

    /**
     * Get a Product by its Id
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            The Product Id
     * @param pDisplayMode
     *            display mode
     * @return the Product
     */
    public UiProduct getProduct(UiSession pSession, String pProductId,
            DisplayMode pDisplayMode) {

        String lRoleToken = pSession.getRoleToken();

        CacheableProduct lCacheableProduct =
                getProductService().getCacheableProduct(lRoleToken, pProductId,
                        CacheProperties.MUTABLE);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        lCacheableProduct.getId()));

        CacheableProductType lCacheableProductType =
                getProductService().getCacheableProductType(lRoleToken,
                        lCacheableProduct.getTypeId(), lProperties);

        BusinessProduct lBusinessProduct =
                new CacheableProductAccess(lRoleToken, lCacheableProductType,
                        lCacheableProduct,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiProduct(pSession, lBusinessProduct, pDisplayMode);
    }

    /**
     * Get empty Product of given Type
     * 
     * @param pSession
     *            Current user session
     * @param pProductTypeName
     *            Product type name
     * @param pEnvironmentNames
     *            List of environment names
     * @return the empty Product
     */
    public UiProduct getProductByType(UiSession pSession,
            String pProductTypeName, List<String> pEnvironmentNames) {

        String lRoleToken = pSession.getRoleToken();

        CacheableProductType lCacheableProductType =
                getProductService().getCacheableProductTypeByName(lRoleToken,
                        pSession.getParent().getProcessName(),
                        pProductTypeName, CacheProperties.IMMUTABLE);

        CacheableProduct lCacheableProduct =
                getProductService().getProductModel(lRoleToken,
                        lCacheableProductType, pEnvironmentNames,
                        getContext(pSession));

        addToCache(pSession, lCacheableProduct);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        lCacheableProductType.getId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        CacheProperties.ACCESS_CONTROL_NOT_USED));

        lCacheableProductType =
                getProductService().getCacheableProductTypeByName(lRoleToken,
                        pSession.getParent().getProcessName(),
                        pProductTypeName, lProperties);

        BusinessProduct lBusinessProduct =
                new CacheableProductAccess(lRoleToken, lCacheableProductType,
                        lCacheableProduct,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiProduct(pSession, lBusinessProduct, DisplayMode.CREATION);
    }

    /**
     * Get the product name by it identifier
     * 
     * @param pSession
     *            the session
     * @param pProductId
     *            the product identifier
     * @return the product name
     */
    public String getProductName(UiSession pSession, String pProductId) {
        String lProductName = null;

        if (getFieldsContainerService().isValuesContainerExists(pProductId)) {
            CacheableProduct lCacheableProduct =
                    getProductService().getCacheableProduct(
                            pSession.getRoleToken(), pProductId,
                            CacheProperties.IMMUTABLE);

            if (lCacheableProduct != null) {
                lProductName = lCacheableProduct.getFunctionalReference();
            }
        }

        return lProductName;
    }

    /**
     * Get sub-Products list
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            The Product Id
     * @param pDisplayMode
     *            display mode
     * @return sub-products list
     */
    public List<UiProduct> getSubProducts(UiSession pSession,
            String pProductId, DisplayMode pDisplayMode) {

        String lRoleToken = pSession.getRoleToken();

        CacheableProduct lCacheableProduct =
                getProductService().getCacheableProduct(
                        pSession.getRoleToken(), pProductId,
                        CacheProperties.IMMUTABLE);

        List<UiProduct> lChildrenProducts = new ArrayList<UiProduct>();
        List<String> lChildrenNames = lCacheableProduct.getChildrenNames();
        for (String lChildName : lChildrenNames) {
            String lChildProductId =
                    getProductService().getProductId(lRoleToken, lChildName);
            lChildrenProducts.add(getProduct(pSession, lChildProductId,
                    pDisplayMode));
        }
        return lChildrenProducts;
    }

    private UiProduct getUiProduct(UiSession pSession,
            BusinessProduct pBusinessProduct, DisplayMode pDisplayMode) {

        // Check Access Rights (Admin access and classic access)
        DisplayMode lDisplayMode = pDisplayMode;
        if ((!pBusinessProduct.isUpdatable() || DisplayMode.EDITION.equals(lDisplayMode))
                && !(FacadeLocator.instance().getAuthorizationFacade().hasSpecifiedAdminAccess(
                		pBusinessProduct.getName(), pSession, AdministrationAction.PRODUCT_UPDATE))) {
            lDisplayMode = DisplayMode.VISUALIZATION;
        }
        if ((pBusinessProduct.isConfidential() || DisplayMode.VISUALIZATION.equals(lDisplayMode))
        		&& !(FacadeLocator.instance().getAuthorizationFacade().hasSpecifiedAdminAccess(
                		pBusinessProduct.getName(), pSession, AdministrationAction.PRODUCT_UPDATE))) {
            throw new AuthorizationException("Illegal access to the product "
                    + pBusinessProduct.getId()
                    + " : the access is confidential ");
        }

        UiProduct lProduct = new UiProduct();

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        Map<String, List<CategoryValue>> lCategoryCache =
                new HashMap<String, List<CategoryValue>>();

        //Container attributes and fields
        initUiContainer(lProduct, pBusinessProduct, pSession, pDisplayMode,
                lTranslationManager, lCategoryCache, getContext(pSession));

        //Product attributes
        // encode  HTML tags and java script code before set them to the browser
        lProduct.setName(StringEscapeUtils.escapeHtml(pBusinessProduct.getName()));
        lProduct.setDescription(pBusinessProduct.getDescription());
        lProduct.setParents(pBusinessProduct.getParents());
        lProduct.setChildren(pBusinessProduct.getChildren());

        //Adding groups for product
        //display group is used to display fields
        //in order of instantiation
        for (String lGroupName : pBusinessProduct.getFieldGroupNames()) {
            BusinessFieldGroup lBusinessFieldGroup =
                    pBusinessProduct.getFieldGroup(lGroupName);
            lProduct.addFieldGroup(new UiFieldGroup(
                    lTranslationManager.getTextTranslation(lGroupName),
                    lBusinessFieldGroup.getFieldNames(),
                    lBusinessFieldGroup.isOpen()));
        }

        return lProduct;
    }

    /**
     * Import products.
     * 
     * @param pSession
     *            The session.
     * @param pImportFile
     *            The file to import.
     */
    public void importProducts(UiSession pSession, byte[] pImportFile) {
        final ImportProperties lImportProperties = new ImportProperties();

        // Only create products
        lImportProperties.setAllFlags(ImportFlag.SKIP);
        lImportProperties.setProductsFlag(ImportFlag.CREATE_ONLY);
        try {
            getImportService().importData(pSession.getRoleToken(),
                    new ByteArrayInputStream(pImportFile), lImportProperties,
                    null);
        }
        catch (SchemaValidationException e) {
            throw new UiUnexpectedException(e);
        }
        catch (ImportException e) {
            throw new UiBusinessException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.product.ProductFacade#updateProduct(org.topcased.gpm.ui.facade.server.authorization.UiSession,
     *      java.lang.String, java.lang.String, java.util.List, java.util.List,
     *      java.util.List)
     */
    public void updateProduct(UiSession pSession, String pProductId,
            String pDescription, List<UiField> pFields,
            List<String> pParentNamesList, List<String> pChildrenNamesList) {

        CacheableProduct lCacheableProduct =
                getProductService().getCacheableProduct(
                        pSession.getRoleToken(), pProductId,
                        CacheProperties.MUTABLE);
        fillCacheableProduct(pSession, lCacheableProduct, pFields);

        lCacheableProduct.setDescription(pDescription);

        // Update product
        getProductService().updateProduct(pSession.getRoleToken(),
                lCacheableProduct, getContext(pSession));

        // Update the product parents
        if (!CollectionUtils.isEqualCollection(
                lCacheableProduct.getParentNames(), pParentNamesList)) {
            getProductService().updateProductParents(pSession.getRoleToken(),
                    pSession.getParent().getProcessName(),
                    lCacheableProduct.getProductName(), pParentNamesList);
        }

        // Update the product children
        if (!CollectionUtils.isEqualCollection(
                lCacheableProduct.getChildrenNames(), pChildrenNamesList)) {
            if (!pChildrenNamesList.isEmpty()) {
                getProductService().updateChildren(pSession.getRoleToken(),
                        lCacheableProduct.getProductName(), pChildrenNamesList);

            }
            else {
                //Remove all children
                getProductService().removeChildren(pSession.getRoleToken(),
                        lCacheableProduct.getProductName());
            }
        }
    }

    /**
     * make sure that Parent Names List and Children Names List are different
     * each other
     * 
     * @param pShortList
     *            list with smaller size
     * @param pLongList
     *            list with bigger size
     */
    private void performProductRelationShip(final List<String> pShortList,
            final List<String> pLongList, final String pProductionName) {
        Iterator<String> lShortListIter = pShortList.iterator();
        while (lShortListIter.hasNext()) {
            final String lCurrentValue = lShortListIter.next();
            if (pLongList.contains(lCurrentValue)) {
                throw new ProductHierarchyException("'" + lCurrentValue
                        + "' can not be used as Parent and Children of '"
                        + pProductionName + "'");
            }
        }
    }

    /**
     * Clear a product from cache based on the product name
     * 
     * @param pSession
     *            Current user session
     * @param pProductName
     *            the product identifier
     */
    @Override
    public void removeProductFromCache(UiSession pSession, String pProductName) {

        String lProductId =
                getProductService().getProductId(pSession.getRoleToken(),
                        pProductName);
        getProductService().removeElementFromCache(lProductId);
    }
    
    @Override
    public String[] getNonAssignableRolesForNonAdmins() {
    	String lRoleLine = getProductService().getNonAssignableRolesForNonAdmins();
    	if (lRoleLine == null) {
    		return new String[0];
    	}
    	String[] lRoles = lRoleLine.split(",");
    	for (int i=0; i<lRoles.length; i++) {
    		lRoles[i] = lRoles[i].trim();
    	}
    	return lRoles;
    }
}

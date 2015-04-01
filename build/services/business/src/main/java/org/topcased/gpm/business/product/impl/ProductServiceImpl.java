/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import static org.apache.commons.collections.CollectionUtils.subtract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.cache.AbstractCachedObjectFactory;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.exception.ProductHierarchyException;
import org.topcased.gpm.business.exception.UndeletableElementException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.product.service.ProductTypeData;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.facilities.DisplayGroupDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.product.ProductType;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Implementation of the product management service.
 * 
 * @author llatil
 */
public class ProductServiceImpl extends ServiceImplBase implements
        ProductService {

    /** The product factory. */
    private final ProductFactory productFactory = new ProductFactory();

    /** The product type factory. */
    private final ProductTypeFactory productTypeFactory =
            new ProductTypeFactory();

    /**
     * Constructs a new product service impl.
     */
    public ProductServiceImpl() {
        registerFactories(productFactory, productTypeFactory);
    }

    private String nonAssignableRolesForNonAdmins;
    
    /** The display group dao. */
    private DisplayGroupDao displayGroupDao;


    @Override
	public String getNonAssignableRolesForNonAdmins() {
		return nonAssignableRolesForNonAdmins;
	}

	/**
	 * List of roles any with user role edition ri
	 * 
	 * @param pRoles comma
	 */
	public void setNonAssignableRolesForNonAdmins(String pRoles) {
		nonAssignableRolesForNonAdmins = pRoles;
	}
	
    /**
     * getDisplayGroupDao.
     * 
     * @return Returns the displayGroupDao.
     */
    public DisplayGroupDao getDisplayGroupDao() {
        return displayGroupDao;
    }

    /**
     * setDisplayGroupDao.
     * 
     * @param pDisplayGroupDao
     *            The displayGroupDao to set.
     */
    public void setDisplayGroupDao(final DisplayGroupDao pDisplayGroupDao) {
        displayGroupDao = pDisplayGroupDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductModel(java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProductType,
     *      java.util.List,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public CacheableProduct getProductModel(String pRoleToken,
            CacheableProductType pProductType, List<String> pEnvironmentNames,
            Context pContext) throws AuthorizationException {
        getAuthService().assertValidRoleToken(pRoleToken);

        //Test access control
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setContainerTypeId(pProductType.getId());

        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);
        if (lTypeAccessControlData.getConfidential()) {
            throw new AuthorizationException("Illegal access to this type '"
                    + pProductType.getName()
                    + "' : the access is confidential ");
        }

        final CacheableProduct lCacheableProduct =
                (CacheableProduct) fieldsContainerServiceImpl.getValuesContainerModel(
                        pRoleToken, pProductType, null, pEnvironmentNames);
        // Extension point postGetSheetModel or postGetModel
        final ExtensionPoint lPostGetModel =
                getExecutableExtensionPoint(pProductType.getId(),
                        ExtensionPointNames.POST_GET_MODEL, pContext);

        if (lPostGetModel != null) {
            final ContextBase lContext = new ContextBase(pContext);

            lContext.put(ExtensionPointParameters.PRODUCT, lCacheableProduct);
            lContext.put(ExtensionPointParameters.PRODUCT_TYPE, pProductType);

            getExtensionsService().execute(pRoleToken, lPostGetModel, lContext);
        }

        return lCacheableProduct;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#setProductEnvironment(java.lang.String,
     *      java.lang.String, java.lang.String[])
     */
    @Override
    public void setProductEnvironment(String pRoleToken, String pProductId,
            String[] pEnvironmentNames) {
        getAuthService().assertAdminRole(pRoleToken);

        Product lProduct = getProduct(pProductId);

        lProduct.getEnvironments().clear();

        // Then add the new environment
        for (String lEnvironmentName : pEnvironmentNames) {
            lProduct.addToEnvironmentList(getEnvironmentDao().getEnvironment(
                    lProduct.getBusinessProcess().getName(), lEnvironmentName));
        }

        // Invalid cache for the product
        removeElementFromCache(pProductId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCurrentProductSummary(java.lang.String)
     */
    @Override
    public ProductSummaryData getCurrentProductSummary(String pRoleToken) {
        final Product lProduct =
                getAuthService().getProductFromSessionToken(pRoleToken);
        final ProductSummaryData lProdSummary = new ProductSummaryData();

        lProdSummary.setId(lProduct.getId());
        lProdSummary.setName(lProduct.getName());

        final String[] lEnvironmentNames =
                new String[lProduct.getEnvironments().size()];
        int i = 0;

        for (Environment lEnv : lProduct.getEnvironments()) {
            lEnvironmentNames[i] = lEnv.getName();
            i++;
        }
        lProdSummary.setEnvironments(lEnvironmentNames);

        return lProdSummary;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#createProduct(java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProduct,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createProduct(final String pRoleToken,
            CacheableProduct pProduct, Context pCtx) {
        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        final CacheableProductType lProductType =
                getCacheableProductTypeByName(pRoleToken, lProcessName,
                        pProduct.getTypeName(), CacheProperties.IMMUTABLE);

        // Check that no product with same name exist in database
        if (getProductDao().isProductExists(lProcessName,
                pProduct.getProductName())) {
            throw new InvalidValueException(pProduct.getProductName(),
                    "A product with name '" + pProduct.getProductName()
                            + "' already exists.");
        }

        // Check that the product type is not confidential and is creatable
        getAuthService().checkCreatableAccessControl(pRoleToken,
                lProductType.getId(), lProductType.getName(), lProcessName,
                null, null);

        // Extension point preCreate
        final ExtensionPoint lPreCreate =
                getExecutableExtensionPoint(lProductType.getId(),
                        ExtensionPointNames.PRE_CREATE, pCtx);

        if (lPreCreate != null) {
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.PRODUCT, pProduct);
            lCtx.put(ExtensionPointParameters.PRODUCT_TYPE, lProductType);

            // Execute the 'preCreate' extension.
            getExtensionsService().execute(pRoleToken, lPreCreate, lCtx);
        }

        final ProductType lProductTypeEntity =
                getProductType(lProductType.getId());

        String lProductId =
                doCreateProduct(pRoleToken, pProduct, lProductTypeEntity,
                        lProductType, pCtx);

        // Extension point postCreate
        final ExtensionPoint lPostCreate =
                getExecutableExtensionPoint(lProductType.getId(),
                        ExtensionPointNames.POST_CREATE, pCtx);

        if (lPostCreate != null) {
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.PRODUCT_ID, lProductId);
            lCtx.put(ExtensionPointParameters.PRODUCT_TYPE, lProductType);

            getExtensionsService().execute(pRoleToken, lPostCreate, lCtx);
        }

        return lProductId;
    }

    /**
     * Do the actual product creation in DB
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCacheableProduct
     *            Product data
     * @param pProductTypeEntity
     *            Product type entity
     * @param pCacheableProductType
     *            Product type (from cache)
     * @param pContext
     *            Execution context
     * @return Product identifier
     */
    private String doCreateProduct(String pRoleToken,
            CacheableProduct pCacheableProduct, ProductType pProductTypeEntity,
            CacheableProductType pCacheableProductType, Context pContext) {

        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        final BusinessProcess lInstance = getBusinessProcess(lProcessName);

        // Try to get the product entity from DB. If found in database, the product
        // content is 'reseted' by this method.
        Product lProductEntity =
                getAndReset(pCacheableProduct.getId(), Product.class);
        if (null == lProductEntity) {
            // Create the sheet entity (not found in database)
            lProductEntity =
                    getProductDao().getNewProduct(pCacheableProduct.getTypeId());
        }

        lProductEntity.setId(pCacheableProduct.getId());
        lProductEntity.setName(pCacheableProduct.getProductName());
        lProductEntity.setDefinition(pProductTypeEntity);
        lProductEntity.setDescription(pCacheableProduct.getDescription());
        lProductEntity.setReference(pCacheableProduct.getFunctionalReference());
        lProductEntity.setBusinessProcess(lInstance);
        lProductEntity.setVersion(0);

        List<String> lChildrenHierarchy =
                getProductHierarchy(lProcessName,
                        pCacheableProduct.getChildrenNames());
        List<String> lParents = pCacheableProduct.getParentNames();
        checkProductHierarchy(lParents, lChildrenHierarchy);

        // If the children have already been created, link them
        if (pCacheableProduct.getChildrenNames() != null) {
            for (String lChildName : pCacheableProduct.getChildrenNames()) {
                Product lChildren =
                        getProductDao().getProduct(lProcessName, lChildName);
                if (lChildren != null) {
                    lProductEntity.addToProductList(lChildren);
                }
            }
        }

        // Set environments
        for (String lEnvironmentName : pCacheableProduct.getEnvironmentNames()) {
            Environment lEnv =
                    getEnvironmentDao().getEnvironment(lProcessName,
                            lEnvironmentName);
            if (lEnv != null) {
                lProductEntity.addToEnvironmentList(lEnv);
            }
        }

        getProductDao().create(lProductEntity);

        // Create the product field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                pCacheableProductType.getId()).updateDomainFromBusiness(
                lProductEntity, pCacheableProduct, pContext);

        // Create the product attributes
        getAttributesService().set(lProductEntity.getId(),
                pCacheableProduct.getAllAttributes());

        clearProductNamesCache();

        return lProductEntity.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#updateProduct(java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProduct,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void updateProduct(final String pRoleToken,
            CacheableProduct pProductData, Context pCtx) {
        //if (!isProductUpdatable(pRoleToken, pProductData.getProductName())) {
        //    throw new AuthorizationException("Unsufficient rights to update product.");
        //}

        final String lProcessName = getAuthService().getProcessNameFromToken(pRoleToken);
        final CacheableProductType lProductType = getCacheableProductTypeByName(
        		pRoleToken, lProcessName, pProductData.getTypeName(), CacheProperties.IMMUTABLE);

        // Check that product already exists
        if (!isProductExists(pRoleToken, pProductData.getProductName())) {
            throw new InvalidNameException(pProductData.getProductName());
        }

        // Check that the product type in not confidential
        //getAuthService().checkNotConfidentialAccessControl(pRoleToken,
        //        lProductType.getId(), lProductType.getName(), lProcessName,
        //        null, null);

        // Extension point preUpdate
        final ExtensionPoint lPreUpdate = getExecutableExtensionPoint(
        		lProductType.getId(), ExtensionPointNames.PRE_UPDATE, pCtx);

        if (lPreUpdate != null) {
        	final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.PRODUCT, pProductData);
            lCtx.put(ExtensionPointParameters.PRODUCT_TYPE, lProductType);

            // Execute the 'preUpdate' extension
            getExtensionsService().execute(pRoleToken, lPreUpdate, lCtx);
        }

        final Product lProduct = getProductDao().getProduct(lProcessName, pProductData.getProductName());

        // Set description value
        lProduct.setDescription(pProductData.getDescription());

        // Create the product field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                lProductType.getId()).updateDomainFromBusiness(lProduct,
                pProductData, pCtx);

        // Update the product attributes
        getAttributesService().update(lProduct.getId(), pProductData.getAllAttributes());

        removeElementFromCache(pProductData.getId());
        clearProductNamesCache();

        // Extension point postUpdate
        final ExtensionPoint lPostUpdate =
                getExecutableExtensionPoint(lProductType.getId(),
                        ExtensionPointNames.POST_UPDATE, pCtx);

        if (lPostUpdate != null) {
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.PRODUCT_ID, pProductData.getId());
            lCtx.put(ExtensionPointParameters.PRODUCT_TYPE, lProductType);

            // Execute the postUpdate extension
            getExtensionsService().execute(pRoleToken, lPostUpdate, lCtx);
        }
    }

    /**
     * Check if a product is updatable. (A product is updatable for admin role,
     * for an admin access set on global instance, or for an admin access set on
     * this specified product.)
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductName
     *            Product Name
     * @return true if product is updatable, false otherwise
     */
    // TODO declare in superclass
    public boolean isProductUpdatable(String pRoleToken, String pProductName) {
        return authorizationService.hasGlobalAdminRole(pRoleToken)
                || authorizationService.isAdminAccessDefinedOnInstance(
                        pRoleToken,
                        AdministrationAction.PRODUCT_UPDATE.getActionKey())
                || authorizationService.isAdminAccessDefinedOnProduct(
                        pRoleToken,
                        AdministrationAction.PRODUCT_UPDATE.getActionKey(),
                        pProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#deleteProduct(java.lang.String,
     *      java.lang.String, boolean,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void deleteProduct(String pRoleToken, String pProductId,
            boolean pCascade, Context pCtx) {
        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        final Product lProduct = getProduct(pProductId);

        if (pCascade) {
            deleteProductCascade(pRoleToken, lProcessName, lProduct, pCtx);
        }
        else {
            deleteProduct(pRoleToken, lProcessName, lProduct, pCtx);
        }
    }

    /**
     * Delete a product in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            The name of the process
     * @param pProduct
     *            The product
     * @param pCtx
     *            The context
     */
    private void deleteProduct(String pRoleToken, String pProcessName,
            Product pProduct, Context pCtx) {

        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        ProductType lProductType = pProduct.getProductType();

        // Get Access Control of the product type
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setContainerTypeId(lProductType.getId());
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(pRoleToken));
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        if (!lTypeAccessControlData.getConfidential() && lTypeAccessControlData.getDeletable()) {

            if (pProduct.getChildren().size() == 0) {
                if (getSheetDao().getSheetsCount(pProduct.getId()) == 0) {
                    final String lProductId = pProduct.getId();
                    final CacheableProductType lCacheableProductType =
                            getCacheableProductType(lProductType.getId(),
                                    CacheProperties.IMMUTABLE);

                    // Extension point preDelete
                    final ExtensionPoint lPreDelete =
                            getExecutableExtensionPoint(
                                    lCacheableProductType.getId(),
                                    ExtensionPointNames.PRE_DELETE, pCtx);

                    if (lPreDelete != null) {
                        final Context lCtx = new ContextBase(pCtx);

                        lCtx.put(ExtensionPointParameters.PRODUCT_ID,
                                lProductId);
                        lCtx.put(ExtensionPointParameters.PRODUCT_TYPE,
                                lCacheableProductType);

                        getExtensionsService().execute(pRoleToken, lPreDelete,
                                lCtx);
                    }

                    // Extension point postDelete
                    final ExtensionPoint lPostDelete =
                            getExecutableExtensionPoint(
                                    lCacheableProductType.getId(),
                                    ExtensionPointNames.POST_DELETE, pCtx);

                    final CacheableProduct lCacheableProduct;

                    // Get product before deleting
                    if (lPostDelete == null) {
                        lCacheableProduct = null;
                    }
                    else {
                        lCacheableProduct =
                                getCacheableProduct(lProductId,
                                        CacheProperties.MUTABLE);
                    }

                    // set parents : Remove the product in the children list
                    List<String> lParentsNames =
                            getProductDao().getParentNamesFromId(
                                    pProduct.getId());
                    for (String lParentName : lParentsNames) {
                        Product lParentProduct =
                                getProduct(pProcessName, lParentName);
                        lParentProduct.getChildren().remove(pProduct);
                        removeElementFromCache(lParentProduct.getId());
                    }

                    // Remove all OverriddenRoles for the product
                    authorizationService.deleteOverriddenRolesFromContainerId(pProduct.getId());

                    // Remove all RolesForProduct for the product
                    authorizationService.deleteRolesForProductByProduct(pProduct);

                    // Remove all AccessControls of the product
                    getAccessControlDao().removeAllFromProduct(pProduct);

                    // Delete all links
                    List<String> lLinksId =
                            getLinkService().getLinkDao().getLinks(
                                    pProduct.getId());
                    for (String lLinkId : lLinksId) {
                        getLinkService().deleteLink(pRoleToken, lLinkId,
                                ContextBase.getEmptyContext());
                    }

                    // Delete the product in DB
                    getProductDao().remove(pProduct);

                    // refresh cache
                    removeElementFromCache(pProduct.getId());
                    clearProductNamesCache();

                    if (lPostDelete != null) {
                        final Context lCtx = new ContextBase(pCtx);

                        lCtx.put(ExtensionPointParameters.PRODUCT_ID,
                                lProductId);
                        lCtx.put(ExtensionPointParameters.PRODUCT,
                                lCacheableProduct);
                        lCtx.put(ExtensionPointParameters.PRODUCT_TYPE,
                                lCacheableProductType);

                        getExtensionsService().execute(pRoleToken, lPostDelete,
                                lCtx);
                    }
                }
                else {
                    throw new AuthorizationException(
                            pProduct.getName(),
                            "The product '"
                                    + pProduct.getName()
                                    + "' can't be deleted because it contains sheet(s).");
                }
            }
            else {
                throw new AuthorizationException(
                        pProduct.getName(),
                        "The product '"
                                + pProduct.getName()
                                + "' can't be deleted because it contains sub-product(s).");
            }
        }
        else {
            throw new AuthorizationException(pProduct.getName(),
                    "The type of the product '" + pProduct.getName()
                            + "' can not be deleted.");
        }
    }

    /**
     * Delete a product and sub-product in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            The name of the process
     * @param pProduct
     *            The product
     * @param pCtx
     *            The context
     */
    private void deleteProductCascade(String pRoleToken, String pProcessName,
    		Product pProduct, Context pCtx) {

        Collection<Product> lChildren =
                new ArrayList<Product>(pProduct.getChildren());
        for (Product lProduct : lChildren) {
            deleteProductCascade(pRoleToken, pProcessName, lProduct, pCtx);
        }
        deleteProduct(pRoleToken, pProcessName, pProduct, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.getProductType#getProductType(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public ProductTypeData getProductTypeByName(String pRoleToken,
            String pProcessName, String pProductTypeName) {

        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }
        ProductType lProductType =
                getProductType(pProcessName, pProductTypeName, true);
        if (null == lProductType) {
            return null;
        }
        return createProductTypeData(pRoleToken, lProductType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductTypes(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CacheableProductType> getProductTypes(String pRoleToken,
            String pProcessName, CacheProperties pProperties) {

        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        BusinessProcess lBusinessProc = getBusinessProcess(pProcessName);
        List<ProductType> lTypes =
                getProductTypeDao().getProductTypes(lBusinessProc);
        List<CacheableProductType> lProductTypes =
                new ArrayList<CacheableProductType>(lTypes.size());

        for (ProductType lType : lTypes) {
            // check that the product type is not confidential
            TypeAccessControlData lTypeAccessControl = getAuthService().getTypeAccessControl(
            		pRoleToken, pProcessName, null, null, lType.getId());
            if (lTypeAccessControl == null || !lTypeAccessControl.getConfidential()) {
                // if not confidential, add the product to the list
                lProductTypes.add(getCacheableProductType(pRoleToken, lType.getId(), pProperties));
            }
        }
        return lProductTypes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#createProductType(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.product.service.ProductTypeData)
     */
    @Override
    public String createProductType(String pRoleToken,
            String pBusinessProcName, ProductTypeData pData) {

        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        BusinessProcess lBusinessProc = getBusinessProcess(pBusinessProcName);
        ProductType lProductType;

        if (null == pData.getName()) {
            throw new InvalidNameException("Name is null");
        }

        lProductType =
                getProductTypeDao().getProductType(lBusinessProc,
                        pData.getName());

        if (null == lProductType) {
            lProductType = ProductType.newInstance();
            lProductType.setName(pData.getName());
            lProductType.setBusinessProcess(lBusinessProc);
        }

        lProductType.setDescription(pData.getDescription());
        getProductTypeDao().create(lProductType);

        lBusinessProc.setProductType(lProductType);
        return lProductType.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#createProductType(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProductType)
     */
    @Override
    public String createProductType(String pRoleToken,
            String pBusinessProcessName, CacheableProductType pProductType) {
        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        ProductType lProductType;

        if (null == pProductType.getName()) {
            throw new InvalidNameException("Name is null");
        }

        lProductType = getProductTypeDao().getProductType(lBusinessProcess, pProductType.getName());

        if (null == lProductType) {
            lProductType = ProductType.newInstance();
            lProductType.setName(pProductType.getName());
            lProductType.setBusinessProcess(lBusinessProcess);
        }

        lProductType.setDescription(pProductType.getDescription());
        getProductTypeDao().create(lProductType);

        removeElementFromCache(lProductType.getId());

        lBusinessProcess.setProductType(lProductType);
        return lProductType.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#deleteProductType(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void deleteProductType(String pRoleToken, String pBusinessProcName,
            String pProductTypeName, boolean pDeleteProducts) {

        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        ProductType lProductType = getProductType(pBusinessProcName, pProductTypeName);

        if (getValuesContainerDao().getCount(lProductType.getId()) > 0
                && !pDeleteProducts) {
            throw new UndeletableElementException(pProductTypeName);
        }

        // TODO Delete associated product and sheet associated
        getProductTypeDao().remove(lProductType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductTypeByProductKey(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableProductType getProductTypeByProductKey(String pRoleToken,
            String pProductId, CacheProperties pProperties) {
        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        String lProductTypeId =
                getCacheableProduct(pRoleToken, pProductId,
                        CacheProperties.IMMUTABLE).getTypeId();
        return getCacheableProductType(pRoleToken, lProductTypeId, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProductsByType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CacheableProduct> getCacheableProductsByType(String pRoleToken,
            String pProductTypeId, CacheProperties pProperties) {
        // FIXME : implement the roleToken
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        final ProductType lProductType = getProductType(pProductTypeId);
        final List<Product> lProducts =
                getProductDao().getProducts(lProductType);
        final List<CacheableProduct> lProductDataList =
                new ArrayList<CacheableProduct>(lProducts.size());

        for (Product lProduct : lProducts) {
            lProductDataList.add(getCacheableProduct(pRoleToken,
                    lProduct.getId(), pProperties));
        }

        return lProductDataList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductHierarchy(String,
     *      String[])
     */
    @Override
    public List<String> getProductHierarchy(final String pProcessName,
            final List<String> pProductNames) {
        // Parents product are returned too
        final Set<String> lProductNames = new HashSet<String>();
        if (pProductNames != null) {
            // Get all sub product
            for (final String lProductName : pProductNames) {
                final ProductHierarchyElement lElement =
                        productHierarchyManager.getElement(new ProductHierarchyKey(
                                pProcessName, lProductName));

                // Don't add product name, if the product doesn't exist
                if (lElement != null) {
                    lProductNames.add(lProductName);
                    lProductNames.addAll(lElement.getAllSubProductNames());
                }
            }
        }

        return new ArrayList<String>(lProductNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#setProductParents(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setProductParents(String pBusinessProcessName,
            String pProductName, List<String> pParentsNames) {
        Product lCurrentProduct =
                getProduct(pBusinessProcessName, pProductName);

        List<String> lOldParentNames = getProductDao().getParentNamesFromId(lCurrentProduct.getId());
        Product lParent;

        // Remove the product from the removed parents children list
        Collection<String> lRemovedParents;
        lRemovedParents = subtract(lOldParentNames, pParentsNames);
        for (String lRemovedParentName : lRemovedParents) {
            lParent = getProduct(pBusinessProcessName, lRemovedParentName);
            lParent.removeFromProductList(lCurrentProduct);
            removeElementFromCache(lParent.getId());
        }

        // Add the product as new child for its newly added parents
        Collection<String> lNewParents;
        lNewParents = subtract(pParentsNames, lOldParentNames);
        for (String lNewParentName : lNewParents) {

            lParent = getProduct(pBusinessProcessName, lNewParentName);
            if (!lCurrentProduct.getName().equals(lParent.getName())) {
                lParent.addToProductList(lCurrentProduct);
                removeElementFromCache(lParent.getId());
            }
            else {
                throw new ProductHierarchyException("Parent relation between '"
                        + lCurrentProduct.getName()
                        + "' and itself : Operation refused");
            }

        }

        removeElementFromCache(lCurrentProduct.getId());
        clearProductNamesCache();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#updateProductParents(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @Override
    public void updateProductParents(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            List<String> pParentsNames) {

        String lBusinessProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);

        /*if (!isProductUpdatable(pRoleToken, pProductName)) {
            throw new AuthorizationException(
                    "Unsufficient rights to update parents products");
        }*/

        Product lCurrentProduct = getProduct(pBusinessProcessName, pProductName);

        // Get current product children
        Collection<Product> lChildren = lCurrentProduct.getChildren();
        List<String> lCurrentProductChildrenNames =
                new ArrayList<String>(lChildren.size());
        for (Product lChildrenProduct : lChildren) {
            lCurrentProductChildrenNames.add(lChildrenProduct.getName());
        }

        // Check if the new parent is not already a child of parent.
        if (pParentsNames != null) {
            for (String lNewParentName : pParentsNames) {
                if (lCurrentProductChildrenNames.contains(lNewParentName)) {
                    throw new ProductHierarchyException("Impossible to set "
                            + lNewParentName + " as parent of " + pProductName
                            + " since " + lNewParentName
                            + " is already child of " + pProductName);
                }
            }
        }

        // Get current product parents names and remove
        List<String> lCurrentProductParentsNames =
                getProductParentsNames(pBusinessProcessName, pProductName);
        for (String lParentName : lCurrentProductParentsNames) {
            Product lParent = getProduct(pBusinessProcessName, lParentName);
            lParent.removeFromProductList(lCurrentProduct);
            removeElementFromCache(lParent.getId());
        }

        List<String> lChildrenHierarchy =
                getProductHierarchy(lBusinessProcessName,
                        lCurrentProductChildrenNames);
        checkProductHierarchy(pParentsNames, lChildrenHierarchy);

        if (pParentsNames != null) {
            // Add new parents names

            if (pParentsNames.contains(lCurrentProduct.getName())) {
                throw new ProductHierarchyException("Parent relation between '"
                        + lCurrentProduct.getName()
                        + "' and itself : Operation refused");
            }
            else {
                for (String lNewParentName : pParentsNames) {
                    Product lParent =
                            getProduct(pBusinessProcessName, lNewParentName);
                    lParent.addToProductList(lCurrentProduct);
                    removeElementFromCache(lParent.getId());
                }

            }
        }

        removeElementFromCache(lCurrentProduct.getId());
        clearProductNamesCache();

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#updateChildren(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void updateChildren(String pRoleToken, String pProductName,
            List<String> pChildrenNames) throws AuthorizationException,
        GDMException {
        if (!isProductUpdatable(pRoleToken, pProductName)) {
            throw new AuthorizationException(
                    "Unsufficient rights to update children products");
        }

        String lBusinessProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        String lProductId = getProductId(pRoleToken, pProductName);
        CacheableProduct lProduct =
                getCacheableProduct(lProductId, CacheProperties.MUTABLE);

        // No child as parents
        if (!CollectionUtils.intersection(pChildrenNames,
                lProduct.getParentNames()).isEmpty()) {
            throw new ProductHierarchyException(
                    "Impossible to set new children since some children are already parent of "
                            + pProductName);
        }

        Product lCurrentProduct =
                getProduct(lBusinessProcessName, pProductName);

        Collection<String> lOldChildren = lProduct.getChildrenNames();
        Collection<String> lAlwaysChildren =
                CollectionUtils.intersection(lOldChildren, pChildrenNames);
        Collection<String> lNewChildren = new HashSet<String>(pChildrenNames);

        List<String> lChildrenHierarchy =
                getProductHierarchy(lBusinessProcessName, pChildrenNames);
        List<String> lParents = lProduct.getParentNames();
        checkProductHierarchy(lParents, lChildrenHierarchy);

        if (!lAlwaysChildren.isEmpty()) {
            lOldChildren.removeAll(lAlwaysChildren);
            lNewChildren.removeAll(lAlwaysChildren);
        }

        for (String lOldChild : lOldChildren) {
            Product lChild = getProduct(lBusinessProcessName, lOldChild);
            lCurrentProduct.removeFromProductList(lChild);
        }

        if (lNewChildren.contains(lCurrentProduct.getName())) {
            throw new ProductHierarchyException("Children relation between "
                    + lCurrentProduct.getName()
                    + " and itself : Operation refused");
        }
        else {
            for (String lNewChild : lNewChildren) {
                Product lChild = getProduct(lBusinessProcessName, lNewChild);
                lCurrentProduct.addToProductList(lChild);
            }
        }

        removeElementFromCache(lCurrentProduct.getId());
        clearProductNamesCache();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#removeChildren(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void removeChildren(String pRoleToken, String pProductName)
        throws AuthorizationException {
        if (!isProductUpdatable(pRoleToken, pProductName)) {
            throw new AuthorizationException(
                    "Unsufficient rights to update children products");
        }
        String lProductId = getProductId(pRoleToken, pProductName);
        CacheableProduct lProduct =
                getCacheableProduct(lProductId, CacheProperties.IMMUTABLE);
        String lBusinessProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        Product lCurrentProduct =
                getProduct(lBusinessProcessName, pProductName);
        for (String lChildName : lProduct.getChildrenNames()) {
            Product lChild = getProduct(lBusinessProcessName, lChildName);
            lCurrentProduct.removeFromProductList(lChild);
        }
        removeElementFromCache(lCurrentProduct.getId());
        clearProductNamesCache();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductParentsNames(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<String> getProductParentsNames(String pBusinessProcessName,
            String pProductName) {
        return getProductDao().getParentNames(pBusinessProcessName,
                pProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProducts(java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProduct,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CacheableProduct> getCacheableProducts(
            String pBusinessProcessName, CacheableProduct pParentProduct,
            CacheProperties pProperties) {

        List<Product> lProducts = null;

        if (null == pParentProduct) {
            lProducts =
                    getProductDao().getProductsWithoutParent(
                            pBusinessProcessName);
        }
        else {
            Product lParentProduct = getProduct(pParentProduct.getId());
            Query lQuery =
                    GpmSessionFactory.getHibernateSession().createFilter(
                            lParentProduct.getChildren(),
                            "order by this.name asc");
            lProducts = lQuery.list();
        }
        if (lProducts == null) {
            return null;
        }

        List<CacheableProduct> lCacheableProducts =
                new ArrayList<CacheableProduct>(lProducts.size());
        for (Product lProduct : lProducts) {
            lCacheableProducts.add(getCacheableProduct(lProduct.getId(),
                    pProperties));
        }
        return lCacheableProducts;
    }

    /**
     * Gets the cacheable product.
     * 
     * @param pProductId
     *            the product id
     * @param pCacheProperties
     *            Cache properties
     * @return the cacheable product
     */
    // TODO declare in superclass
    public CacheableProduct getCacheableProduct(String pProductId,
            CacheProperties pCacheProperties) {
        CacheableProduct lCacheableProduct =
                (CacheableProduct) getCachedValuesContainer(pProductId,
                        pCacheProperties.getCacheFlags());

        if (null == lCacheableProduct) {
            final Product lProduct = getProduct(pProductId);
            final CacheableProductType lCachedProductType =
                    getCacheableProductType(lProduct.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);

            lCacheableProduct =
                    new CacheableProduct(lProduct, lCachedProductType,
                            getProductDao().getParentNamesFromId(pProductId));
            addElementInCache(lCacheableProduct);
        }

        return lCacheableProduct;
    }

    /**
     * Gets the cacheable product from its name.
     * 
     * @param pProductName
     *            Name of the product
     * @param pBusinessProcess
     *            Name of the instance
     * @param pProperties
     *            Cache properties
     * @return the cacheable product
     */
    // TODO declare in superclass
    public CacheableProduct getCacheableProductByName(String pProductName,
            String pBusinessProcess, CacheProperties pProperties) {
        final Product lProduct =
                getProduct(pBusinessProcess, pProductName, true);

        // Return null object if the product doesn't exists
        if (lProduct != null) {
            return getCacheableProduct(lProduct.getId(), pProperties);
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProduct(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableProduct getCacheableProduct(String pRoleToken,
            String pProductId, CacheProperties pProperties) {
        return (CacheableProduct) fieldsContainerServiceImpl.getValuesContainer(
                pRoleToken, pProductId,
                FieldsContainerService.FIELD_NOT_CONFIDENTIAL, pProperties);
    }

    /**
     * Gets the cacheable product type.
     * 
     * @param pProductTypeId
     *            The product type id
     * @param pCacheProperties
     *            The cache properties
     * @return The cacheable product type
     */
    // TODO declare in superclass
    public CacheableProductType getCacheableProductType(String pProductTypeId,
            CacheProperties pCacheProperties) {
        CacheableProductType lCachedProductType =
                getCachedElement(CacheableProductType.class, pProductTypeId,
                        pCacheProperties.getCacheFlags());

        if (null == lCachedProductType) {
            ProductType lProductType = getProductType(pProductTypeId);
            //use the factory to create a product type
            lCachedProductType =
                    (CacheableProductType) productTypeFactory.createCacheableObject(lProductType);
            addElementInCache(lCachedProductType);
        }

        return lCachedProductType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProductType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableProductType getCacheableProductType(String pRoleToken,
            String pProductTypeId, CacheProperties pProperties) {
        final CacheableProductType lProductType =
                getCacheableProductType(pProductTypeId, pProperties);

        if (pProperties.getSpecificAccessControl() == null) {
            return lProductType;
        }
        // else
        return authorizationService.getCheckedFieldsContainer(pRoleToken,
                pProperties.getSpecificAccessControl(), lProductType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProductTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableProductType getCacheableProductTypeByName(
            String pRoleToken, String pProcessName, String pProductTypeName,
            CacheProperties pCacheProperties) {
        ProductType lProductType =
                getProductTypeDao().getProductType(
                        getBusinessProcess(pProcessName), pProductTypeName);

        if (null == lProductType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pProductTypeName);
        }
        return getCacheableProductType(pRoleToken, lProductType.getId(),
                pCacheProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getCacheableProductTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    // TODO declare in superclass
    public CacheableProductType getCacheableProductTypeByName(
            String pRoleToken, String pProductTypeName,
            CacheProperties pCacheProperties) {
        String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        return getCacheableProductTypeByName(pRoleToken, lProcessName,
                pProductTypeName, pCacheProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getSerializableProduct(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.Product getSerializableProduct(
            String pRoleToken, String pProductId) {
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }
        //TODO correct access control.
        org.topcased.gpm.business.serialization.data.Product lProduct =
                new org.topcased.gpm.business.serialization.data.Product();
        getCacheableProduct(pProductId, CacheProperties.IMMUTABLE).marshal(
                lProduct);
        return lProduct;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getSerializableProductType(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.ProductType getSerializableProductType(
            String pRoleToken, String pProductTypeId) {
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setContainerTypeId(pProductTypeId);
        TypeAccessControlData lAccessControl =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);
        if (lAccessControl == null || !lAccessControl.getConfidential()) {
            org.topcased.gpm.business.serialization.data.ProductType lProductType =
                    new org.topcased.gpm.business.serialization.data.ProductType();
            getCacheableProductType(pProductTypeId, CacheProperties.IMMUTABLE).marshal(
                    lProductType);
            return lProductType;
        }
        return null;
    }

    /**
     * Create a new ProductTypeData from a ProductType entity
     * 
     * @param pRoleToken
     *            Role token (use for i18nService)
     * @param pProductType
     *            Product type entity
     * @return The created product type data
     */
    private ProductTypeData createProductTypeData(final String pRoleToken,
            ProductType pProductType) {
        final ProductTypeData lProductType = new ProductTypeData();

        lProductType.setId(pProductType.getId());
        lProductType.setName(pProductType.getName());

        if ((StringUtils.isNotBlank(pProductType.getDescription()))
                && (StringUtils.isNotBlank(pRoleToken))) {
            final ServiceLocator lServiceLocator = ServiceLocator.instance();
            final I18nService lI18nService = lServiceLocator.getI18nService();

            //For upward compatibility. Previous method signature doesn't have roletoken parameter. 
            lProductType.setDescription(lI18nService.getValueForUser(
                    pRoleToken, pProductType.getDescription()));
        }
        else {
            lProductType.setDescription(pProductType.getDescription());
        }

        return lProductType;
    }

    /**
     * A factory for creating ProductType objects.
     */
    private class ProductTypeFactory extends AbstractCachedObjectFactory {

        /**
         * Constructs a new product type factory.
         */
        public ProductTypeFactory() {
            super(ProductType.class);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.cache.CacheableFactory#createCacheableObject(java.lang.Object)
         */
        public Object createCacheableObject(Object pEntityObject) {
            ProductType lProductType = (ProductType) pEntityObject;
            //get display groups from dao (database)
            List<org.topcased.gpm.domain.facilities.DisplayGroup> lDisplayGroups;
            lDisplayGroups = getDisplayGroupDao().getDisplayGroup(lProductType);
            return new CacheableProductType(lProductType, lDisplayGroups);
        }
    }

    /**
     * A factory for creating Product objects.
     */
    private class ProductFactory extends AbstractCachedObjectFactory {

        /**
         * Constructs a new product factory.
         */
        public ProductFactory() {
            super(Product.class);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.cache.CacheableFactory#createCacheableObject(java.lang.Object)
         */
        public Object createCacheableObject(Object pEntityObject) {
            Product lProduct = (Product) pEntityObject;
            CacheableProductType lProductType =
                    getCacheableProductType(lProduct.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);
            return new CacheableProduct(lProduct, lProductType,
                    getProductDao().getParentNamesFromId(lProduct.getId()));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#isProductExists(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean isProductExists(final String pRoleToken,
            final String pProductName) {
        return getProductDao().isProductExists(
                getAuthService().getProcessNameFromToken(pRoleToken),
                pProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getProductId(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getProductId(String pRoleToken, String pProductName) {
        if (getProductDao().isProductExists(
                getAuthService().getProcessNameFromToken(pRoleToken),
                pProductName)) {
            return getProductDao().getProduct(
                    getAuthService().getProcessNameFromToken(pRoleToken),
                    pProductName).getId();
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.product.service.ProductService#getSheetCount(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Integer getSheetCount(final String pRoleToken,
            final String pProductId) {
        return getSheetDao().getSheetsCount(pProductId);
    }

    /**
     * Test the product existence.
     * <p>
     * A product exists if:
     * <ul>
     * <li>its identifier exists OR</li>
     * <li>there are a product with the same name and attached to the same
     * business process</li>
     * </ul>
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pProduct
     *            Product to test
     * @return True if the product exists, false otherwise.
     */
    // TODO declare in superclass
    public boolean isExists(final String pRoleToken,
            final org.topcased.gpm.business.serialization.data.Product pProduct) {
        final boolean lRes;
        if (StringUtils.isNotBlank(pProduct.getId())) {
            lRes = getProductDao().exist(pProduct.getId());
        }
        else if (StringUtils.isNotBlank(pProduct.getName())) {
            String lProcessName =
                    authorizationService.getProcessNameFromToken(pRoleToken);
            lRes =
                    getProductDao().isProductExists(lProcessName,
                            pProduct.getName());
        }
        else {
            lRes = false;
        }
        return lRes;
    }

    @SuppressWarnings("unchecked")
    private void checkProductHierarchy(List<String> pParents,
            List<String> pChildrenHierarchy) {
        Collection<String> lParents = (Collection<String>) pParents;
        Collection<String> lChildrenHierarchy =
                (Collection<String>) pChildrenHierarchy;
        Collection<String> lDoublon = CollectionUtils.EMPTY_COLLECTION;
        if (lParents != null && lChildrenHierarchy != null) {
            lDoublon = CollectionUtils.retainAll(pParents, pChildrenHierarchy);
        }
        // check if there element in children hierarchy
        // which is also assign as current product parent
        // in that case, lDoublon is not empty
        if (lDoublon.size() > 0) {
            // there is a cycle so throw and exception
            StringBuilder lBuffer = new StringBuilder(lDoublon.size());
            lBuffer.append(" Product(s) ");
            for (String lElement : lDoublon) {
                lBuffer.append(" ");
                lBuffer.append(lElement);
                lBuffer.append(",");
            }
            lBuffer.append(" already has(ve) relationship with current product's parent.");
            throw new ProductHierarchyException(lBuffer.toString());
        }
    }

	public void triggerRolesChanged(String pRoleToken, String lProductName, String pLoginName,
			List<String> pOldRoles, List<String> pNewRoles, Context pCtx) {

		// Do not trigger anything if roles have not changed
		if (pOldRoles.size() == pNewRoles.size()) {
			List<String> lRolesCopy = new ArrayList<String>(pNewRoles);
			for (String lRole : pOldRoles) {
				lRolesCopy.remove(lRole);
			}
			if (lRolesCopy.isEmpty()) {
				// No change
				return;
			}
		}
		
		String lProcessName = getAuthService().getProcessNameFromToken(pRoleToken);
		Product lProduct = getProduct(lProcessName, lProductName);
		CacheableProductType lProductType = getCacheableProductType(lProduct.getProductType().getId(), CacheProperties.IMMUTABLE);
 		
        // Extension point POST_UPDATE_USERS
        ExtensionPoint lPostUpdateUsers = getExecutableExtensionPoint(
        		lProductType.getId(), ExtensionPointNames.POST_UPDATE_ROLES, pCtx);

        if (lPostUpdateUsers != null) {
            ContextBase lCtx = new ContextBase(pCtx);
            lCtx.put(ExtensionPointParameters.PRODUCT_NAME, lProduct.getName());
            lCtx.put(ExtensionPointParameters.USER_LOGIN, pLoginName);
            lCtx.put(ExtensionPointParameters.PREVIOUS_ROLES, pOldRoles);
            lCtx.put(ExtensionPointParameters.NEW_ROLES, pNewRoles);

            // Execute the postUpdate extension
            getExtensionsService().execute(pRoleToken, lPostUpdateUsers, lCtx);
        }
	}
}

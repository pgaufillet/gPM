/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.product;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * ProductImportManager handles product importation.
 * 
 * @author mkargbo
 */
public class ProductImportManager
        extends
        AbstractValuesContainerImportManager<Product, CacheableProduct, CacheableProductType> {

    /** NO_RIGHTS */
    private static final String NO_RIGHTS =
            "User cannot import this product (no rights).";

    private ProductServiceImpl productServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#getBusinessTypeObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    @Override
    protected CacheableProductType getBusinessTypeObject(String pRoleToken,
            Product pElement) {
        CacheableProductType lProductType =
                productServiceImpl.getCacheableProductTypeByName(pRoleToken,
                        pElement.getType(), CacheProperties.IMMUTABLE);
        return lProductType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createElement(String pRoleToken,
            CacheableProduct pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        String lElementId =
                productServiceImpl.createProduct(pRoleToken, pBusinessElement,
                        pContext);
        return lElementId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getProductsFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#isElementExists(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      ImportExecutionReport)
     */
    @Override
    protected String isElementExists(String pRoleToken, Product pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (productServiceImpl.isExists(pRoleToken, pElement)) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                break;
            case UPDATE_ONLY:
                if (!productServiceImpl.isExists(pRoleToken, pElement)) {
                    throw new ImportException(ImportMessage.OBJECT_NOT_EXISTS,
                            pElement);
                }
                if (StringUtils.isBlank(pElement.getId())) {
                    lId =
                            productServiceImpl.getProductId(pRoleToken,
                                    pElement.getName());

                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (fieldsContainerServiceImpl.isValuesContainerExists(pElement.getId())) {
                    lId = pElement.getId();
                }
                else if (productServiceImpl.isExists(pRoleToken, pElement)) {
                    lId =
                            productServiceImpl.getProductId(pRoleToken,
                                    pElement.getName());
                }
                else {
                    lId = StringUtils.EMPTY;
                }
                break;
            default:
        }
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#removeElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void removeElement(String pRoleToken, String pElementId,
            Context pContext, String... pAdditionalArguments) {
        productServiceImpl.deleteProduct(pRoleToken, pElementId, false, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#updateElement(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void updateElement(String pRoleToken,
            CacheableProduct pBusinessElement, String pElementId,
            Context pContext, boolean pSheetsIgnoreVersion,
            String... pAdditionalArguments) {
        pBusinessElement.setId(pElementId);
        productServiceImpl.updateProduct(pRoleToken, pBusinessElement, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#doGetBusinessObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      org.topcased.gpm.business.fields.impl.CacheableFieldsContainer,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected CacheableProduct doGetBusinessObject(String pRoleToken,
            Product pElement, CacheableProductType pBusinessType,
            ImportProperties pProperties) {
        CacheableProduct lProduct =
                new CacheableProduct(pElement, pBusinessType);
        return lProduct;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Set the product's parents.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#doAdditionalImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    protected void doAdditionalImport(String pRoleToken, Product pElement,
            String pElementId, Context pContext) {
        // Set the product parents
        if (!CollectionUtils.isEmpty(pElement.getParents())) {
            String lProcessName =
                    authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
            productServiceImpl.setProductParents(lProcessName,
                    pElement.getName(), pElement.getParentsAsStrings());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Only 'admin' or user who has admin role on the product can import the
     * product.
     * </p>
     * 
     * @throws ImportException
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#canImport(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, Product pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        boolean lCanImport = true;

        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
            case ERASE:
            case CREATE_OR_UPDATE:
                if (StringUtils.isBlank(pElementId)) {
                    if (!authorizationServiceImpl.hasAdminAccess(pRoleToken)) {
                        try {
                            authorizationServiceImpl.assertHasAdminRoleOnProduct(
                                    pRoleToken, pElement.getName());
                        }
                        catch (AuthorizationException e) {
                            onFailure(pElement, pProperties, pReport, NO_RIGHTS);
                            lCanImport = false;
                        }
                    }
                }
                else {
                    if (!productServiceImpl.isProductUpdatable(pRoleToken,
                            pElement.getName())) {
                        onFailure(pElement, pProperties, pReport, NO_RIGHTS);
                        lCanImport = false;
                    }
                }
                break;
            case UPDATE_ONLY:
                if (!productServiceImpl.isProductUpdatable(pRoleToken,
                        pElement.getName())) {
                    onFailure(pElement, pProperties, pReport, NO_RIGHTS);
                    lCanImport = false;
                }
                break;

            default: //Access controls have been checked later.
        }

        return lCanImport
                && super.canImport(pRoleToken, pElement, pElementId,
                        pProperties, pReport);
    }

    public void setProductServiceImpl(ProductServiceImpl pProductServiceImpl) {
        productServiceImpl = pProductServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(Product pElement) {
        final String lIdentifier;
        if (StringUtils.isBlank(pElement.getId())) {
            lIdentifier = pElement.getName();
        }
        else {
            lIdentifier = pElement.getId();
        }
        return lIdentifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.PRODUCT;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final Product pElement) {
        return Collections.singletonList(pElement.getName());
    }
}
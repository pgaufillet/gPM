/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Product service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface ProductService {

    /**
     * Get the identifier of a product.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductName
     *            Name of product.
     * @return Product identifier.
     */
    @Transactional(readOnly = true)
    public String getProductId(final String pRoleToken,
            final String pProductName);

    /**
     * Get a product content by the product identifier.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductId
     *            Identifier of the product.
     * @param pProperties
     *            The cache properties.
     * @return CacheableProduct object.
     */
    @Transactional(readOnly = true)
    public CacheableProduct getCacheableProduct(final String pRoleToken,
            final String pProductId, final CacheProperties pProperties);

    /**
     * Get an empty ProductData modeled after the product type. Fields are
     * filled with their default value (if any), or an empty string otherwise.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductType
     *            Product type Name of the product.
     * @param pEnvironmentNames
     *            Name of associated environment.
     * @param pContext
     *            The context.
     * @return A model of product.
     */
    @Transactional(readOnly = true)
    public CacheableProduct getProductModel(final String pRoleToken,
            final CacheableProductType pProductType,
            final List<String> pEnvironmentNames, final Context pContext);

    /**
     * Create a new product.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProduct
     *            Product.
     * @param pCtx
     *            Execution context.
     * @return The technical identifier of the newly created product.
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public String createProduct(final String pRoleToken,
            final CacheableProduct pProduct, final Context pCtx)
        throws FieldValidationException;

    /**
     * Update an existing product.
     * <p>
     * Update attributes.
     * </p>
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductData
     *            Product data.
     * @param pCtx
     *            Execution context.
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public void updateProduct(final String pRoleToken,
            final CacheableProduct pProductData, final Context pCtx)
        throws FieldValidationException;

    /**
     * Delete a product in the database.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductId
     *            The ID of the product.
     * @param pCascade
     *            Define if the deletion is in cascade : delete all
     *            sub-products.
     * @param pCtx
     *            The context.
     * @throws AuthorizationException
     *             If the product is confidential or can be delete.<br />
     *             If the product contains sheets.<br />
     *             If the product contains sub-products (no cascade option).
     */
    public void deleteProduct(final String pRoleToken, final String pProductId,
            final boolean pCascade, final Context pCtx)
        throws AuthorizationException;

    /**
     * Change the environment used by a product
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductId
     *            Identifier of the product.
     * @param pEnvironmentNames
     *            Names of environments.
     */
    public void setProductEnvironment(String pRoleToken, String pProductId,
            String[] pEnvironmentNames);

    /**
     * Get the product type data defined for a given process.
     * 
     * @param pRoleToken
     *            Role token session
     * @param pProcessName
     *            Name of the process
     * @param pProductTypeName
     *            Name of product type.
     * @return The ProductTypeData
     * @deprecated
     * @since 1.8.3
     * @see ProductService#getCacheableProductTypeByName(String, String, String,
     *      CacheProperties)
     */
    @Transactional(readOnly = true)
    public ProductTypeData getProductTypeByName(String pRoleToken,
            String pProcessName, String pProductTypeName);

    /**
     * Creates a new product type in the database. The product type created has
     * no fields.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcName
     *            Name of the business process
     * @param pData
     *            Contains the data of the product type to create.
     * @return Identifier of the newly created product type.
     * @deprecated
     * @since 1.8.3
     * @see ProductService#createProductType(String, String,
     *      CacheableProductType)
     */
    public String createProductType(String pRoleToken,
            String pBusinessProcName, ProductTypeData pData);

    /**
     * Creates a new product type in the database. The product type created has
     * no fields.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Name of the business process
     * @param pProductType
     *            Contains the data of the product type to create.
     * @return Identifier of the newly created product type.
     */
    public String createProductType(String pRoleToken,
            String pBusinessProcessName, CacheableProductType pProductType);

    /**
     * Delete a product type in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcName
     *            Name of the business process
     * @param pProductTypeName
     *            Name of the type to remove.
     * @param pDeleteProducts
     *            Force the deletion of all products of this type
     */
    public void deleteProductType(String pRoleToken, String pBusinessProcName,
            String pProductTypeName, boolean pDeleteProducts);

    /**
     * Get the product types associated with a process.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of business process
     * @param pProperties
     *            The cache properties
     * @return List of product types data
     */
    @Transactional(readOnly = true)
    public List<CacheableProductType> getProductTypes(String pRoleToken,
            String pProcessName, CacheProperties pProperties);

    /**
     * Get info on a product type from a product identifier.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductId
     *            Product identifier
     * @param pProperties
     *            The cache properties
     * @return The product type data of the product
     */
    @Transactional(readOnly = true)
    public CacheableProductType getProductTypeByProductKey(String pRoleToken,
            String pProductId, CacheProperties pProperties);

    /**
     * Get a list of products whose type passed in parameter.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductTypeId
     *            Identifier of the product type
     * @param pProperties
     *            The cache properties
     * @return List of products
     */
    public List<CacheableProduct> getCacheableProductsByType(String pRoleToken,
            String pProductTypeId, CacheProperties pProperties);

    /**
     * Get the list of products and subproduct names from a list of products
     * name.
     * 
     * @param pBusinessProcessName
     *            Business process name.
     * @param pProductNames
     *            List of product names.
     * @return The list of products and subproducts names
     */
    public List<String> getProductHierarchy(String pBusinessProcessName,
            List<String> pProductNames);

    /**
     * Set the parents of a product
     * 
     * @param pBusinessProcessName
     *            Business process name
     * @param pProductName
     *            Product name.
     * @param pParentsNames
     *            List of parents.
     */
    public void setProductParents(String pBusinessProcessName,
            String pProductName, List<String> pParentsNames);

    /**
     * Update the parents of a product (remove old product parents and set new
     * product parents).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Business process name
     * @param pProductName
     *            Product name
     * @param pParentsNames
     *            List of parents
     */
    public void updateProductParents(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            List<String> pParentsNames);

    /**
     * Update children of the specified product.
     * 
     * @param pRoleToken
     *            Role token
     * @param pProductName
     *            Name of the product to update
     * @param pChildrenNames
     *            New children
     * @throws AuthorizationException
     *             If the product can be updated
     * @throws GDMException
     *             If children are already defined as parents.
     */
    public void updateChildren(String pRoleToken, String pProductName,
            List<String> pChildrenNames) throws AuthorizationException,
        GDMException;

    /**
     * Remove children of the specified product
     * 
     * @param pRoleToken
     *            Role token
     * @param pProductName
     *            Name of the product containing the children to remove
     * @throws AuthorizationException
     *             If the product can be updated
     */
    public void removeChildren(String pRoleToken, String pProductName)
        throws AuthorizationException;

    /**
     * Get the list of parents for a given product
     * 
     * @param pBusinessProcessName
     *            Business process name
     * @param pProductName
     *            Product name
     * @return List of parents (names)
     */
    public List<String> getProductParentsNames(String pBusinessProcessName,
            String pProductName);

    /**
     * Get the list of products belonging to a given parent product
     * 
     * @param pBusinessProcessName
     *            Business process name
     * @param pParentProduct
     *            Parent product. If null, the first level products are returned
     *            (products with no parent)
     * @param pProperties
     *            The cache properties
     * @return A list of products.
     */
    public List<CacheableProduct> getCacheableProducts(
            String pBusinessProcessName, CacheableProduct pParentProduct,
            CacheProperties pProperties);

    /**
     * Gets the serializable product.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductId
     *            the product id
     * @return the serializable product
     */
    public org.topcased.gpm.business.serialization.data.Product getSerializableProduct(
            String pRoleToken, String pProductId);

    /**
     * Gets the serializable product type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductTypeId
     *            the product type id
     * @return the serializable product type
     */
    public org.topcased.gpm.business.serialization.data.ProductType getSerializableProductType(
            String pRoleToken, String pProductTypeId);

    /**
     * Get the summary of the currently selected product.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Product summary
     */
    public ProductSummaryData getCurrentProductSummary(String pRoleToken);

    /**
     * Gets the cacheable product type with its id
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProductId
     *            The product type id
     * @param pProperties
     *            The properties of the cacheable object
     * @return The cacheable product type
     */
    public CacheableProductType getCacheableProductType(String pRoleToken,
            String pProductId, CacheProperties pProperties);

    /**
     * Gets the cacheable product type with its name
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            The name of the process
     * @param pProductTypeName
     *            The name product type
     * @param pProperties
     *            The properties of the cacheable object
     * @return The cacheable product type
     */
    public CacheableProductType getCacheableProductTypeByName(
            String pRoleToken, String pProcessName, String pProductTypeName,
            CacheProperties pProperties);

    /**
     * Test if the product exists.
     * <p>
     * The name of a product must be unique: try to get a product by its name.
     * <br/>
     * 
     * @param pRoleToken
     *            Role token
     * @param pProductName
     *            Name of the product to test
     * @return True if the product exists, false otherwise
     */
    public boolean isProductExists(final String pRoleToken,
            final String pProductName);

    /**
     * Get the number of sheet for the specified products
     * 
     * @param pRoleToken
     *            Role token
     * @param pProductId
     *            Identifier of the product
     * @return Number of sheet that have the specified product as product
     */
    public Integer getSheetCount(final String pRoleToken,
            final String pProductId);

    /**
     * Remove an element from the cache. This method does nothing if the
     * specified element is not in cache.<br />
     * 
     * @param pElemId
     *            Identifier of the element to remove
     * @return true if the element was in cached (and has been removed).
     */
    public boolean removeElementFromCache(String pElemId);

    /**
     * Get a comma separated list of roles that should not be assignable by non admins
     * 
     * @return a list
     */
	public String getNonAssignableRolesForNonAdmins();
}

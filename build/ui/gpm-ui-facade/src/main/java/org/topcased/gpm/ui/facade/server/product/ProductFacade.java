/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.product;

import java.util.List;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * ProductFacade
 * 
 * @author nveillet
 */
public interface ProductFacade {

    /**
     * Clear a product from cache
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            the product identifier
     */
    public void clearCache(UiSession pSession, String pProductId);

    /**
     * count sheets on Product
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            Product to count sheets on
     * @return number of sheets on this product
     */
    public int countSheets(UiSession pSession, String pProductId);

    /**
     * Create Product in database
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            Product to create
     * @param pFieldsList
     *            the fields to create
     * @param pProductName
     *            name of created product
     * @param pDescription
     *            description of the product
     * @param pParentNamesList
     *            list of parent products names
     * @param pChildrenNamesList
     *            list of children products names
     */
    public void createProduct(UiSession pSession, String pProductId,
            String pProductName, String pDescription,
            List<UiField> pFieldsList, List<String> pParentNamesList,
            List<String> pChildrenNamesList);

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
            boolean pDeleteSubProducts);

    /**
     * Get creatable product types
     * 
     * @param pSession
     *            the session
     * @return the creatable product type names
     */
    public List<String> getCreatableProductTypes(UiSession pSession);

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
            DisplayMode pDisplayMode);

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
            String pProductTypeName, List<String> pEnvironmentNames);

    /**
     * Get the product name by it identifier
     * 
     * @param pSession
     *            the session
     * @param pProductId
     *            the product identifier
     * @return the product name
     */
    public String getProductName(UiSession pSession, String pProductId);

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
            String pProductId, DisplayMode pDisplayMode);

    /**
     * Import products.
     * 
     * @param pSession
     *            The session.
     * @param pImportFile
     *            The file to import.
     */
    public void importProducts(UiSession pSession, byte[] pImportFile);

    /**
     * Update Product in database
     * 
     * @param pSession
     *            Current user session
     * @param pProductId
     *            Product to update
     * @param pDescription
     *            Description to update
     * @param pFields
     *            the fields to update
     * @param pParentNamesList
     *            list of parent products names
     * @param pChildrenNamesList
     *            list of children products names
     */
    public void updateProduct(UiSession pSession, String pProductId,
            String pDescription, List<UiField> pFields,
            List<String> pParentNamesList, List<String> pChildrenNamesList);

    /**
     * Clear a product from cache based on the product name
     * 
     * @param pSession
     *            Current user session
     * @param pProductName
     *            the product name
     */
    public void removeProductFromCache(UiSession pSession, String pProductName);
    
    /**
     * Get a table of all non assignable roles for non admins.
     * This list contains regexps.
     * 
     * @return a regexp list
     */
    public String[] getNonAssignableRolesForNonAdmins();
}

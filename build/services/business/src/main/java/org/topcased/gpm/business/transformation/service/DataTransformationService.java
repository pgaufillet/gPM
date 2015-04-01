/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.transformation.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductData;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.revision.impl.CacheableRevision;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.SheetData;

/**
 * Public interface of the data transformation service
 * 
 * @author tpanuel
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface DataTransformationService {

    /**
     * Get an array of FieldGroupData from a CacheableSheet
     * 
     * @param pRoleToken
     *            The role token
     * @param pCacheableSheet
     *            The CacheableSheet
     * @return an array of FieldGroupData
     * @since 3.0.0
     * @see CacheableSheet
     */
    public FieldGroupData[] getFieldGroupData(String pRoleToken,
            CacheableSheet pCacheableSheet);

    /**
     * Get SheetData from CacheableSheet
     * 
     * @param pRoleToken
     *            The role token, if null SheetData will not contain field's
     *            data
     * @param pCacheableSheet
     *            The CacheableSheet
     * @return The SheetData
     * @deprecated
     * @since 1.8.3
     * @see CacheableSheet
     */
    public SheetData getSheetDataFromCacheableSheet(String pRoleToken,
            CacheableSheet pCacheableSheet);

    /**
     * Get LinkData from CacheableLink
     * 
     * @param pRoleToken
     *            The role token, if null LinkData will not contain field's data
     * @param pCacheableLink
     *            The CacheableLink
     * @return The CacheableLink
     * @deprecated
     * @since 1.8.3
     * @see CacheableLink
     */
    public LinkData getLinkDataFromCacheableLink(String pRoleToken,
            CacheableLink pCacheableLink);

    /**
     * Get ProductData from CacheableProduct
     * 
     * @param pRoleToken
     *            The role token, if null ProductData will not contain field's
     *            data
     * @param pCacheableProduct
     *            The CacheableProduct
     * @return The ProductData
     * @deprecated
     * @since 1.8.3
     * @see CacheableProduct
     */
    public ProductData getProductDataFromCacheableProduct(String pRoleToken,
            CacheableProduct pCacheableProduct);

    /**
     * Get RevisionData from CacheableRevision
     * 
     * @param pRoleToken
     *            The role token, if null RevisionData will not contain field's
     *            data
     * @param pCacheableRevision
     *            The CacheableRevision
     * @param pRevisedContainerTId
     *            The id of the revised container
     * @return The RevisionData
     */
    public RevisionData getRevisionDataFromCacheableRevision(String pRoleToken,
            CacheableRevision pCacheableRevision, String pRevisedContainerTId);

    /**
     * Get CacheableSheet from SheetData
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetData
     *            The SheetData
     * @return The CacheableSheet
     * @deprecated
     * @since 1.8.3
     * @see CacheableSheet
     */
    public CacheableSheet getCacheableSheetFromSheetData(String pRoleToken,
            SheetData pSheetData);

    /**
     * Get CacheableLink from LinkData
     * 
     * @param pRoleToken
     *            The role token
     * @param pLinkData
     *            The LinkData
     * @return The CacheableProduct
     * @deprecated
     * @since 1.8.3
     * @see CacheableLink
     */
    public CacheableLink getCacheableLinkFromLinkData(String pRoleToken,
            LinkData pLinkData);

    /**
     * Get CacheableProduct from ProductData
     * 
     * @param pRoleToken
     *            The role token
     * @param pProductData
     *            The ProductData
     * @return The CacheableProduct
     * @deprecated
     * @since 1.8.3
     * @see CacheableProduct
     */
    public CacheableProduct getCacheableProductFromProductData(
            String pRoleToken, ProductData pProductData);

    /**
     * Get CacheableRevision from RevisionData
     * 
     * @param pRoleToken
     *            The role token
     * @param pRevisionData
     *            The RevisionData
     * @param pRevisedContainerId
     *            The id of the revised container
     * @return The CacheableRevision
     */
    public CacheableRevision getCacheableRevisionFromRevisionData(
            String pRoleToken, RevisionData pRevisionData,
            String pRevisedContainerId);
}

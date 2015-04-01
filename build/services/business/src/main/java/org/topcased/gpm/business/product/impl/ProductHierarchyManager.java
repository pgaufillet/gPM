/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;

import org.topcased.gpm.business.cache.AbstractCacheManager;

/**
 * Used to managed the product hierarchy. For each product name (and business
 * process) the cache contains a ProductHierarchyElement.
 * 
 * @author tpanuel
 */
public class ProductHierarchyManager extends
        AbstractCacheManager<ProductHierarchyKey, ProductHierarchyElement> {
    private Cache productHierarchyCache;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#getCache()
     */
    @Override
    protected Cache getCache() {
        return productHierarchyCache;
    }

    /**
     * Setter on cache used for Spring injection.
     * 
     * @param pCache
     *            The cache.
     */
    public void setProductHierarchyCache(final Cache pCache) {
        productHierarchyCache = pCache;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#load(java.io.Serializable)
     */
    @Override
    protected ProductHierarchyElement load(final ProductHierarchyKey pElementKey) {
        final String lProcessName = pElementKey.getProcessName();
        final String lProductName = pElementKey.getProductName();

        // Null if the product doesn't exists
        if (getProductDao().isProductExists(lProcessName, lProductName)) {
            final ProductHierarchyElement lElement =
                    new ProductHierarchyElement();
            final List<String> lSubProductNames =
                    getProductDao().getSubProductsName(lProcessName,
                            lProductName);

            // Process and product name
            lElement.setProcessName(lProcessName);
            lElement.setProductName(lProductName);
            // First level sub products
            lElement.setDirectSubProductNames(lSubProductNames);

            // Recursive for get all sub products
            final Set<String> lAllSubProductsName =
                    new HashSet<String>(lSubProductNames);

            for (final String lSubProductName : lSubProductNames) {
                final ProductHierarchyElement lSubElement =
                        getElement(new ProductHierarchyKey(lProcessName,
                                lSubProductName));

                lAllSubProductsName.addAll(lSubElement.getAllSubProductNames());
            }
            lElement.setAllSubProductNames(lAllSubProductsName);

            return lElement;
        }
        else {
            return null;
        }
    }
}
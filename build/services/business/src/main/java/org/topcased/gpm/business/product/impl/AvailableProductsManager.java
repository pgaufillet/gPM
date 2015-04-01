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

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;

import org.topcased.gpm.business.cache.AbstractCacheManager;

/**
 * Used to managed all available product name. For each login the cache contains
 * the list of available products name.
 * 
 * @author tpanuel
 */
public class AvailableProductsManager extends
        AbstractCacheManager<AvailableProductsKey, List<CacheableProduct>> {
    private Cache availableProductsCache;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#getCache()
     */
    @Override
    protected Cache getCache() {
        return availableProductsCache;
    }

    /**
     * Setter on cache used for Spring injection.
     * 
     * @param pCache
     *            The cache.
     */
    public void setAvailableProductsCache(final Cache pCache) {
        availableProductsCache = pCache;
    }

    /**
     * Specific method to return only product names
     * 
     * @return product names
     */
    public List<String> getProductNames(AvailableProductsKey pKey) {
        List<String> lList = new ArrayList<String>();
        for (CacheableProduct lP : getElement(pKey)) {
            lList.add(lP.getProductName());
        }
        return lList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#load(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected List<CacheableProduct> load(final AvailableProductsKey pElementKey) {
        List<CacheableProduct> lProducts = new ArrayList<CacheableProduct>();
        for (Object[] lRes : (List<Object[]>) getEndUserDao().getAvailableProducts(
                pElementKey.getLogin(), pElementKey.getProcessName())) {
            CacheableProduct lProd = new CacheableProduct();
            lProd.setProductName((String) lRes[0]);
            lProd.setDescription((String) lRes[1]);
            lProducts.add(lProd);
        }
        return lProducts;
    }
}
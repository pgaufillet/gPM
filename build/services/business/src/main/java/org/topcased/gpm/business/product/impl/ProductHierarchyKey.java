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

import org.topcased.gpm.business.cache.CacheKey;

/**
 * Key used to access on the cache of the ProductHierarchyManager.
 * 
 * @author tpanuel
 */
public class ProductHierarchyKey extends CacheKey {
    private static final long serialVersionUID = 5910689490101210187L;

    private final String processName;

    private final String productName;

    /**
     * Create a key to access on the cache of the ProductHierarchyManager. All
     * the fields are final.
     * 
     * @param pProcessName
     *            The name of the process.
     * @param pProductName
     *            The name of the product.
     */
    public ProductHierarchyKey(final String pProcessName,
            final String pProductName) {
        super(pProcessName + '|' + pProductName);
        processName = pProcessName;
        productName = pProductName;
    }

    /**
     * Get the process name.
     * 
     * @return The process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Get the product name.
     * 
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }
}
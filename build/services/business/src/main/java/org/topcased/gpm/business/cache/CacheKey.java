/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.cache;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Key that can be used by a cache manager.
 * 
 * @author tpanuel
 */
public class CacheKey implements Serializable {
    private static final long serialVersionUID = 1886668321526551078L;

    protected String keyValue;

    /**
     * Create an empty key using by a cache manager.
     */
    public CacheKey() {

    }

    /**
     * Create a key for used by a cache manager.
     * 
     * @param pKeyValue
     *            The value of the key.
     */
    public CacheKey(final String pKeyValue) {
        keyValue = pKeyValue;
    }

    /**
     * Get the key value used to identify an element on a cache.
     * 
     * @return The key value.
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getKeyValue().hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object pOther) {
        return pOther instanceof CacheKey
                && StringUtils.equals(getKeyValue(),
                        ((CacheKey) pOther).getKeyValue());
    }
}
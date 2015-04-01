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

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * Abstract implementation of a cache manager using an EH cache.
 * 
 * @author tpanuel
 * @param <K>
 *            The type of the key.
 * @param <E>
 *            The type of the element.
 */
public abstract class AbstractCacheManager<K extends CacheKey, E> extends
        ServiceImplBase implements ICacheManager<K, E> {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#getElement(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public E getElement(final K pElementKey) {
        final Element lCachedElement = getCache().get(pElementKey);
        final E lValue;

        if (lCachedElement == null) {
            lValue = CopyUtils.getImmutableCopy(load(pElementKey));
            getCache().put(new Element(pElementKey, lValue));
        }
        else {
            lValue = (E) lCachedElement.getValue();
        }

        return lValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateElement(java.io.Serializable)
     */
    public void depreciateElement(final K pElementKey) {
        getCache().remove(pElementKey);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateAll()
     */
    public void depreciateAll() {
        getCache().removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#getElementVersion(java.io.Serializable)
     */
    public long getElementVersion(final K pElementKey) {
        return getCache().get(pElementKey).getVersion();
    }

    /**
     * The EH cache used to store the elements.
     * 
     * @return The EH cache.
     */
    abstract protected Cache getCache();

    /**
     * Load an element from a key.
     * 
     * @param pElementKey
     *            The key.
     * @return The element.
     */
    abstract protected E load(final K pElementKey);
}
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

/**
 * Interface for manager using a cache.
 * 
 * @author tpanuel
 * @param <K>
 *            The type of the key.
 * @param <E>
 *            The type of the element.
 */
public interface ICacheManager<K extends CacheKey, E> {
    /**
     * Get an element store in cache from its id. This element is immutable.
     * 
     * @param pElementKey
     *            The key.
     * @return The immutable element.
     */
    public E getElement(final K pElementKey);

    /**
     * Depreciate an element store in cache from its id. This element will
     * reload at the next getElement call.
     * 
     * @param pElementKey
     *            The key.
     */
    public void depreciateElement(final K pElementKey);

    /**
     * Depreciate all the elements store in cache. The cache is empty.
     */
    public void depreciateAll();

    /**
     * Get the element version
     * 
     * @param pElementKey
     *            The key.
     * @return The version.
     */
    public long getElementVersion(final K pElementKey);
}
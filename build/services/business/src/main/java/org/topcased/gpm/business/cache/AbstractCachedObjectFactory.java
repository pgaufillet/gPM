/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.cache;

/**
 * Base class to help implementing factories for creating AbstractCachedObject
 * objects.
 * 
 * @author llatil
 */
public abstract class AbstractCachedObjectFactory implements CacheableFactory {

    /** The Constant EMPTY. */
    static final private Class<?>[] EMPTY = new Class[] {};

    /** The supported classes. */
    private final Class<?>[] supportedClasses;

    /**
     * Constructs a new abstract cached object factory.
     */
    public AbstractCachedObjectFactory() {
        supportedClasses = null;
    }

    /**
     * Constructs a new abstract cached object factory.
     * 
     * @param pSupportedClass
     *            the single class supported by this factory
     */
    public AbstractCachedObjectFactory(Class<?> pSupportedClass) {
        supportedClasses = new Class[] { pSupportedClass };
    }

    /**
     * Constructs a new abstract cached object factory.
     * 
     * @param pSupportedClasses
     *            the classes supported by this factory
     */
    public AbstractCachedObjectFactory(Class<?>[] pSupportedClasses) {
        supportedClasses = pSupportedClasses.clone();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.CacheableFactory#getSupportedClasses()
     */
    public Class<?>[] getSupportedClasses() {
        if (null != supportedClasses) {
            return supportedClasses.clone();
        }
        return EMPTY;
    }
}

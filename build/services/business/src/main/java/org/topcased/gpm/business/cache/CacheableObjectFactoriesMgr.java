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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class CacheableObjectFactoriesMgr.
 * 
 * @author llatil
 */
public class CacheableObjectFactoriesMgr {

    /** List of factories registered */
    private final List<CacheableFactory> factories =
            new ArrayList<CacheableFactory>();

    /** The factories map. */
    private final Map<Class<Object>, CacheableFactory> factoriesMap =
            new HashMap<Class<Object>, CacheableFactory>();

    /**
     * Gets the factory for entity.
     * 
     * @param pEntityObject
     *            the entity object
     * @return the factory for entity
     */
    public CacheableFactory getFactoryForEntity(Object pEntityObject) {
        Class<? extends Object> lClass = pEntityObject.getClass();
        return getFactoryForClass(lClass);
    }

    /**
     * Gets the factory for class.
     * 
     * @param pEntityClass
     *            the entity class
     * @return the factory for class
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CacheableFactory getFactoryForClass(Class pEntityClass) {
        CacheableFactory lCachedFactory = factoriesMap.get(pEntityClass);
        if (null != lCachedFactory) {
            return lCachedFactory;
        }
        for (CacheableFactory lCurrentFactory : factories) {
            for (Class lSupportedClass : lCurrentFactory.getSupportedClasses()) {
                if (lSupportedClass.isAssignableFrom(pEntityClass)) {
                    factoriesMap.put(pEntityClass, lCurrentFactory);
                    return lCurrentFactory;
                }
            }
        }
        throw new RuntimeException("Cannot find suitable factory for class '"
                + pEntityClass.getName() + "'");
    }

    /**
     * Register a list of factories.
     * 
     * @param pNewFactories
     *            the new factories
     */
    public void register(CacheableFactory... pNewFactories) {
        for (CacheableFactory lFactory : pNewFactories) {
            if (null == lFactory.getSupportedClasses()) {
                continue;
            }
            factories.add(lFactory);
        }
    }
}

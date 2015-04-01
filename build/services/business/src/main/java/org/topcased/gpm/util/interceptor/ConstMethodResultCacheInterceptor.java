/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.interceptor;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * This interceptor can be used on constant method (ONLY !) to automatically
 * cache the result. When this method is called, the complete call context
 * (method name and all parameters value) is used as a key to cache the call
 * result. Note: This class is a custom adaptation of a code available on the
 * Spring Wiki web site.
 * 
 * @author llatil
 */
public class ConstMethodResultCacheInterceptor implements MethodInterceptor,
        InitializingBean, Serializable {

    private static final long serialVersionUID = -3732427193926606322L;

    /**
     * The log4j logger object for this class.
     */
    private static Logger staticLogger =
            org.apache.log4j.Logger.getLogger(ConstMethodResultCacheInterceptor.class);

    private transient Cache cache;

    /**
     * Set cache name to be used
     * 
     * @param pCache
     *            Cache to use.
     */
    public void setCache(Cache pCache) {
        this.cache = pCache;
    }

    /**
     * Checks if required attributes are provided.
     * 
     * @throws AssertionError
     *             if the cache to use is not specified.
     */
    public void afterPropertiesSet() throws AssertionError {
        Assert.notNull(cache,
                "A cache is required. Use setCache(Cache) to provide one.");
    }

    /**
     * Main method caches method result if method is configured for caching
     * method results must be serializable
     */
    public Object invoke(MethodInvocation pInvocation) throws Throwable {
        String lClassName = pInvocation.getThis().getClass().getName();
        String lMethodName = pInvocation.getMethod().getName();
        Object[] lArguments = pInvocation.getArguments();
        Object lResult;

        String lCacheKey = getCacheKey(lClassName, lMethodName, lArguments);

        if (staticLogger.isDebugEnabled()) {
            staticLogger.debug("looking for method result in cache  (cache key : "
                    + lCacheKey);
        }

        Element lElement = cache.get(lCacheKey);
        if (lElement == null) {
            // call target/sub-interceptor
            if (staticLogger.isDebugEnabled()) {
                staticLogger.debug("calling intercepted method and caching the result");
            }

            lResult = pInvocation.proceed();

            // cache method result
            lElement = new Element(lCacheKey, (Serializable) lResult);
            cache.put(lElement);
        }
        return lElement.getValue();
    }

    /**
     * Creates cache key: targetName.methodName.argument0.argument1...
     * 
     * @param pClassName
     *            Name of the class
     * @param pMethodName
     *            Name of the method.
     * @param pArguments
     *            Array of arguments passed to the method call.
     */
    private String getCacheKey(String pClassName, String pMethodName,
            Object[] pArguments) {
        StringBuilder lSb = new StringBuilder();

        // Put the class and method names
        lSb.append(pClassName).append(".").append(pMethodName);

        // If method calls has arguments, append them all
        if ((pArguments != null) && (pArguments.length != 0)) {
            for (int i = 0; i < pArguments.length; i++) {
                lSb.append(".").append(pArguments[i]);
            }
        }
        return lSb.toString();
    }
}

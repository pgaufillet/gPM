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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * This advice simply checks that no null parameter values are passed to a
 * method. Otherwise, it throws an IllegalArgumentException.
 * 
 * @author llatil
 * @see java.lang.IllegalArgumentException
 */
public class CheckNullBeforeAdvice implements MethodBeforeAdvice {

    /**
     * List of method patterns to check.
     */
    private Collection<Pattern> lPatterns;

    /**
     * Cache storing if we do a check or not for a given Method. (avoiding
     * regexp matching)
     */
    private Map<Method, Boolean> lCache;

    /**
     * Callback before a given method is invoked. It checks that no parameter
     * value is null (throws a IllegalArgumentException otherwise).
     * 
     * @param pMethod
     *            method being invoked
     * @param pArgs
     *            arguments to the method
     * @param pTarget
     *            target of the method invocation. May be null.
     * @throws Throwable
     *             throws a IllegalArgumentException if a parameter is null.
     */
    public void before(Method pMethod, Object[] pArgs, Object pTarget)
        throws Throwable {
        boolean lDoCheck = true;

        if (null != lPatterns) {
            Boolean lCachedRes = lCache.get(pMethod);
            if (null != lCachedRes) {
                lDoCheck = lCachedRes.booleanValue();
            }
            else {
                lDoCheck = false;
                for (Pattern lPat : lPatterns) {
                    if (lPat.matcher(pMethod.getName()).matches()) {
                        lDoCheck = true;
                        break;
                    }
                }
                lCache.put(pMethod, Boolean.valueOf(lDoCheck));
            }
        }
        if (lDoCheck && (null != pArgs)) {
            for (int i = 0; i < pArgs.length; ++i) {
                if (null == pArgs[i]) {
                    String lMsg =
                            pMethod.getName() + ":argument " + (i + 1)
                                    + " is null";
                    throw new IllegalArgumentException(lMsg);
                }
            }
        }
    }

    /**
     * Define the list of method patterns to check Each entry in this list is a
     * regexp the mathod name should match in order to be checked. If not
     * defined, all methods are checked.
     * 
     * @param pPatterns
     *            List of string patterns (regexp)
     */
    public void setMethodPatterns(Collection<String> pPatterns) {
        lPatterns = new ArrayList<Pattern>(pPatterns.size());
        lCache = new Hashtable<Method, Boolean>();

        for (String lPatternStr : pPatterns) {
            lPatterns.add(Pattern.compile(lPatternStr));
        }
    }
}

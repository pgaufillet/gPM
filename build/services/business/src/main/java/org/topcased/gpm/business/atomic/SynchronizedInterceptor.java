/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.atomic;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Synchronized the call of a method including the other interceptors.
 * 
 * @author tpanuel
 */
public class SynchronizedInterceptor implements MethodInterceptor {
    /**
     * {@inheritDoc}
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation pMethodInvocation)
        throws Throwable {
        if (pMethodInvocation.getMethod().isAnnotationPresent(
                Synchronized.class)) {
            return synchronizedInvoke(pMethodInvocation);
        }
        else {
            return pMethodInvocation.proceed();
        }
    }

    /**
     * Synchronize a method invocation.
     * 
     * @param pMethodInvocation
     *            The method invocation.
     * @return The method result.
     * @throws Throwable
     *             An exception.
     */
    synchronized private Object synchronizedInvoke(
            final MethodInvocation pMethodInvocation) throws Throwable {
        return pMethodInvocation.proceed();
    }
}
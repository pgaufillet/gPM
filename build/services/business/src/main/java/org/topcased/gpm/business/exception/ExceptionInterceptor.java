/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

/**
 * Interceptor used to changed exception thrown.
 * 
 * @author tpanuel
 */
public class ExceptionInterceptor implements MethodInterceptor {
    /**
     * {@inheritDoc}
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation pMethodInvocation)
        throws Throwable {
        try {
            return pMethodInvocation.proceed();
        }
        catch (HibernateObjectRetrievalFailureException e) {
            throw new InvalidIdentifierException(e.getIdentifier().toString(),
                    e);
        }
    }
}
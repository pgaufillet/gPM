/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.performance;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Abstract interceptor used to analyze performance on interface of the business
 * or the domain layer.
 * 
 * @author tpanuel
 */
public abstract class AbstractPerformanceInterceptor implements
        MethodInterceptor {
    private PerformanceAnalyzer performanceAnalyzer;

    /**
     * Setter on the analyzer for Spring injection.
     * 
     * @param pPerformanceAnalyser
     *            The analyzer.
     */
    public void setPerformanceAnalyzer(
            final PerformanceAnalyzer pPerformanceAnalyser) {
        performanceAnalyzer = pPerformanceAnalyser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation pMethodInvocation) throws Throwable {
        // Check is the analyzer is enabled for improve performance
        if (performanceAnalyzer.isEnabled(getAnalyzeLevel())) {
            // The name of the element is <CLASS_NAME>.<METHOD_NAME>
            performanceAnalyzer.startAnalyze(
                    pMethodInvocation.getThis().getClass().getSimpleName()
                            + '.' + pMethodInvocation.getMethod().getName(),
                    getAnalyzeLevel());
            try {
                return pMethodInvocation.proceed();
            }
            finally {
                performanceAnalyzer.endAnalyze(getAnalyzeLevel());
            }
        }
        else {
            return pMethodInvocation.proceed();
        }
    }

    /**
     * Get the analyze level.
     * 
     * @return The analyze level.
     */
    abstract public int getAnalyzeLevel();
}
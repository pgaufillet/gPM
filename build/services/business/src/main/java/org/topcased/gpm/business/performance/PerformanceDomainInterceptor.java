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

/**
 * Abstract interceptor used to analyze performance on interface of the domain
 * layer.
 * 
 * @author tpanuel
 */
public class PerformanceDomainInterceptor extends
        AbstractPerformanceInterceptor {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.performance.AbstractPerformanceInterceptor#getAnalyzeLevel()
     */
    @Override
    public int getAnalyzeLevel() {
        return PerformanceAnalyzer.DOMAIN_DAO_LEVEL;
    }
}
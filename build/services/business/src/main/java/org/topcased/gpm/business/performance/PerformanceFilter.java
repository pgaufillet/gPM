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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filter used to analyze performance on HTTP call.
 * 
 * @author tpanuel
 */
public class PerformanceFilter implements Filter {
    private PerformanceAnalyzer performanceAnalyzer;

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse,
            FilterChain pChain) throws IOException, ServletException {
        performanceAnalyzer.startAnalyze("HTTP call",
                PerformanceAnalyzer.HTTP_REQUEST_LEVEL);
        try {
            pChain.doFilter(pRequest, pResponse);
        }
        finally {
            performanceAnalyzer.endAnalyze(PerformanceAnalyzer.HTTP_REQUEST_LEVEL);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig pFilterConfig) throws ServletException {
        performanceAnalyzer = PerformanceAnalyzer.getInstance();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        performanceAnalyzer = null;
    }
}
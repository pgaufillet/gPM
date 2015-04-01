/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

import com.google.inject.Singleton;

/**
 * Filter to clear temporary uploaded files.
 * 
 * @author tpanuel
 */
@Singleton
public class ClearTemporaryUploadedFilesFilter implements Filter {
    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse,
            FilterChain pChain) throws IOException, ServletException {
        try {
            pChain.doFilter(pRequest, pResponse);
        }
        finally {
            final UiUserSession lUserSession =
                    ((UiUserSession) ((HttpServletRequest) pRequest).getSession().getAttribute(
                            SessionAttributesEnum.GPM_USER_SESSION_ATTR.name()));

            // At the end of each action, remove the temporary uploaded files 
            // unless keep temporary file is set to true 
            if (lUserSession != null) {
                if (lUserSession.getKeepTemporaryFiles()) {
                    // the temporary files are kept only once
                    lUserSession.setKeepTemporaryFiles(false);
                }
                else {
                    lUserSession.clearTemporaryUploadedFile();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig pArg0) throws ServletException {
    }
}
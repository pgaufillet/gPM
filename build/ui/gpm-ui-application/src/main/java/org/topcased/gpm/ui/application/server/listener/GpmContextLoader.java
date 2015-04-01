/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.listener;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * GpmContextLoader
 * 
 * @author jlouisy
 */
public class GpmContextLoader extends ContextLoader {

    private WebApplicationContext context;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.web.context.ContextLoader#initWebApplicationContext(javax.servlet.ServletContext)
     */
    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext pArg0)
        throws IllegalStateException, BeansException {
        context = super.initWebApplicationContext(pArg0);
        return context;
    }

    /**
     * Get Context.
     * 
     * @return context
     */
    public WebApplicationContext getContext() {
        return context;
    }

}

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

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.topcased.gpm.ui.facade.server.FacadeLocator;

/**
 * GpmContextLoaderListener
 * 
 * @author jlouisy
 */
public class GpmContextLoaderListener extends ContextLoaderListener {

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent pEvent) {
        super.contextInitialized(pEvent);
        ContextLoader lContextLoader = getContextLoader();
        // if Gpm Loader, init locators
        if (lContextLoader instanceof GpmContextLoader) {
            WebApplicationContext lContext =
                    ((GpmContextLoader) lContextLoader).getContext();
            FacadeLocator.setContext(lContext);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.web.context.ContextLoaderListener#createContextLoader()
     */
    @Override
    protected ContextLoader createContextLoader() {
        return new GpmContextLoader();
    }

}

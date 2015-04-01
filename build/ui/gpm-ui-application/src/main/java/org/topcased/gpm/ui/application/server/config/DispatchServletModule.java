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

import org.topcased.gpm.ui.application.server.servlet.DownloadFileServlet;
import org.topcased.gpm.ui.application.server.servlet.UploadFileServlet;

import com.google.inject.servlet.ServletModule;

/**
 * DispatchServletModule
 * 
 * @author mkargbo
 */
public class DispatchServletModule extends ServletModule {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.inject.servlet.ServletModule#configureServlets()
     */
    @Override
    protected void configureServlets() {
        // Dispatch
        serve("/gpm/dispatch").with(GpmDispatchServiceServlet.class);
        filter("/gpm/dispatch").through(ClearTemporaryUploadedFilesFilter.class);
        // Download
        serve("/gpm/download").with(DownloadFileServlet.class);
        // Upload
        serve("/gpm/upload").with(UploadFileServlet.class);
    }
}
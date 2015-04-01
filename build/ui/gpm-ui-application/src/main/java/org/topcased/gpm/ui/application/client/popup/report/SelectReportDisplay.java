/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.report;

import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the SelectReportView.
 * 
 * @author tpanuel
 */
public interface SelectReportDisplay extends PopupDisplay {

    /**
     * Set the report names.
     * 
     * @param pReportModels
     *            The report names.
     */
    public void setReportNames(final List<Translation> pReportModels);

    /**
     * Get the selected report name.
     * 
     * @return The selected report name.
     */
    public String getSelectedReportName();

    /**
     * Set the export button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setExportButtonHandler(ClickHandler pHandler);
}
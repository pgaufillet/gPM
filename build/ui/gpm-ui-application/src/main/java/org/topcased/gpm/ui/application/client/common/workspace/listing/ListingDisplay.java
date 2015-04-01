/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.listing;

import java.util.List;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;
import org.topcased.gpm.ui.facade.shared.filter.result.UiFilterExecutionReport;

/**
 * Display interface for the ListingView.
 * 
 * @author tpanuel
 */
public interface ListingDisplay extends WidgetDisplay, IResizableLayoutPanel {
    /**
     * Set the tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(GpmToolBar pToolBar);

    /**
     * Set the filter info.
     * 
     * @param pFilterName
     *            The filter name.
     * @param pFilterDescription
     *            The filter description.
     */
    public void setFilterInfo(final String pFilterName,
            final String pFilterDescription);

    /**
     * Initialize or reset the table.
     */
    public void initTable();

    /**
     * Add a column.
     * 
     * @param pColumnName
     *            The name of the column.
     */
    public void addValuesColumn(String pColumnName);

    /**
     * Set data.
     * 
     * @param pData
     *            The data
     */
    public void setData(List<FilterResult> pData);

    /**
     * Set the handler call when the image is clicked.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setVisuHandler(GpmBasicActionHandler<String> pHandler);

    /**
     * Set the handler call when the image is clicked.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setEditHandler(GpmBasicActionHandler<String> pHandler);

    /**
     * Set the filter execution report
     * 
     * @param pReport
     *            The report
     */
    public void setFilterExecutionReport(UiFilterExecutionReport pReport);

    /**
     * Set the URL of the welcome page.
     * 
     * @param pURL
     *            The URL of the welcome page.
     */
    public void setIFrameURL(String pURL);

    /**
     * Get the selected elements id.
     * 
     * @return The selected elements id.
     */
    public List<String> getSelectedElementIds();
}
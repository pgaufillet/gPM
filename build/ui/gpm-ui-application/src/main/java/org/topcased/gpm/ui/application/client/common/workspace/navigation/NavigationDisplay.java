/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.navigation;

import java.util.List;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.application.client.common.tree.TreeFilterItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Display interface for the SheetNavigationView.
 * 
 * @author tpanuel
 */
public interface NavigationDisplay extends WidgetDisplay, IResizableLayoutPanel {
    /**
     * Set the table tool bar.
     * 
     * @param pTableToolBar
     *            The table tool bar.
     */
    public void setTableToolBar(GpmToolBar pTableToolBar);

    /**
     * Set the tree tool bar.
     * 
     * @param pTreeToolBar
     *            The tree tool bar.
     */
    public void setTreeToolBar(GpmToolBar pTreeToolBar);

    /**
     * Add a process table filter.
     * 
     * @param pFilterId
     *            The table process filter id.
     * @param pFilterName
     *            The table process filter name.
     * @param pFilterDescription
     *            The table process filter description.
     * @param pExecuteHandler
     *            The handler to execute the table process filter.
     * @param pEditHandler
     *            The handler to edit the table process filter. If null, no
     *            button.
     * @param pDeleteHandler
     *            The handler to delete the table process filter. If null, no
     *            button.
     */
    public void addTableProcessFilter(String pFilterId, String pFilterName,
            String pFilterDescription, ClickHandler pExecuteHandler,
            ClickHandler pEditHandler, ClickHandler pDeleteHandler);

    /**
     * Add a product table filter.
     * 
     * @param pFilterId
     *            The table product filter id.
     * @param pFilterName
     *            The table product filter name.
     * @param pFilterDescription
     *            The table product filter description.
     * @param pExecuteHandler
     *            The handler to execute the table product filter.
     * @param pEditHandler
     *            The handler to edit the table product filter. If null, no
     *            button.
     * @param pDeleteHandler
     *            The handler to delete the table product filter. If null, no
     *            button.
     */
    public void addTableProductFilter(String pFilterId, String pFilterName,
            String pFilterDescription, ClickHandler pExecuteHandler,
            ClickHandler pEditHandler, ClickHandler pDeleteHandler);

    /**
     * Add a user table filter.
     * 
     * @param pFilterId
     *            The table user filter id.
     * @param pFilterName
     *            The table user filter name.
     * @param pFilterDescription
     *            The table user filter description.
     * @param pExecuteHandler
     *            The handler to execute the table user filter.
     * @param pEditHandler
     *            The handler to edit the table user filter. If null, no button.
     * @param pDeleteHandler
     *            The handler to delete the table user filter. If null, no
     *            button.
     */
    public void addTableUserFilter(String pFilterId, String pFilterName,
            String pFilterDescription, ClickHandler pExecuteHandler,
            ClickHandler pEditHandler, ClickHandler pDeleteHandler);

    /**
     * Add a tree filter.
     * 
     * @param pFilterId
     *            The tree filter id.
     * @param pFilterName
     *            The tree filter name.
     */
    public void addTreeFilter(String pFilterId, String pFilterName);

    /**
     * Initialize the tree filters list.
     */
    public void initTreeFilterList();

    /**
     * Reset the tree filter.
     */
    public void resetTreeFilter();

    /**
     * Set the tree filter manager.
     * 
     * @param pManager
     *            The manager.
     */
    public void setTreeFilterManager(TreeFilterManager pManager);

    /**
     * Set the root items on the tree filter.
     * 
     * @param pRootItems
     *            The root items.
     */
    public void setTreeFilterRootItems(List<TreeFilterItem> pRootItems);

    /**
     * Add a handler on the selection event.
     * 
     * @param pHandler
     *            The handler.
     * @return The handler registration.
     */
    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Integer> pHandler);

    /**
     * Add a handler on filter value selection.
     * 
     * @param pHandler
     *            The handler.
     */
    public void addFilterChangeHandler(ValueChangeHandler<String> pHandler);

    /**
     * Get id of the selected tree filter.
     * 
     * @return The id of selected tree filter.
     */
    public String getSelectedTreeFilter();

    /**
     * Clear the filters.
     */
    public void clearFilters();
}
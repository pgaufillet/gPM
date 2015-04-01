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

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AbstractFilterEditionExecuteCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.menu.AbstractNavigationTableMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.AbstractNavigationTreeMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTreeFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResult;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultNode;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for the NavigationView.
 * 
 * @author tpanuel
 * @param <N>
 *            The navigation display type.
 */
public abstract class NavigationPresenter<N extends NavigationDisplay> extends
        AbstractPresenter<N> {
    private final ClickHandler executeTableFilter;

    private final ClickHandler editTableFilter;

    private final ClickHandler deleteTableFilter;

    private AbstractFilterEditionExecuteCommand filterEditionExecuteCommand;

    protected final AbstractNavigationTableMenuBuilder tableMenuBuilder;

    protected final AbstractNavigationTreeMenuBuilder treeMenuBuilder;

    protected boolean addingFilterEnable;

    /**
     * Create a presenter for the NavigationView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pExecuteTreeFilterCommand
     *            Command to execute a tree filter.
     * @param pExecuteTableFilterCommand
     *            Command to execute a table filter.
     * @param pEditTableFilterCommand
     *            Command to edit a filter.
     * @param pDeleteTableFilterCommand
     *            Command to delete a filter.
     * @param pTableMenuBuilder
     *            table menu builder
     * @param pTreeMenuBuilder
     *            tree menu builder
     * @param pFilterEditionExecuteCommand
     *            The command to execute a filter in edition mode.
     */
    public NavigationPresenter(
            final N pDisplay,
            final EventBus pEventBus,
            final ValueChangeHandler<String> pExecuteTreeFilterCommand,
            final ClickHandler pExecuteTableFilterCommand,
            final ClickHandler pEditTableFilterCommand,
            final ClickHandler pDeleteTableFilterCommand,
            final AbstractNavigationTableMenuBuilder pTableMenuBuilder,
            final AbstractNavigationTreeMenuBuilder pTreeMenuBuilder,
            final AbstractFilterEditionExecuteCommand pFilterEditionExecuteCommand) {
        super(pDisplay, pEventBus);
        executeTableFilter = pExecuteTableFilterCommand;
        editTableFilter = pEditTableFilterCommand;
        deleteTableFilter = pDeleteTableFilterCommand;
        // Add handler to filter selection
        getDisplay().addFilterChangeHandler(pExecuteTreeFilterCommand);
        tableMenuBuilder = pTableMenuBuilder;
        treeMenuBuilder = pTreeMenuBuilder;
    }

    /**
     * Set the process table filters.
     * 
     * @param pFilters
     *            The filters.
     */
    public void setTableProcessFilters(final List<UiFilterSummary> pFilters) {
        for (final UiFilterSummary lFilter : pFilters) {
            ClickHandler lExecuteHandler = null;
            ClickHandler lEditHandler = null;
            ClickHandler lDeleteHandler = null;
            if (lFilter.isExecutable()) {
                lExecuteHandler = executeTableFilter;
            }
            if (lFilter.isEditable()) {
                lEditHandler = editTableFilter;
            }
            if (lFilter.isDeletable()) {
                lDeleteHandler = deleteTableFilter;
            }
            getDisplay().addTableProcessFilter(lFilter.getId(),
                    lFilter.getName(), lFilter.getDescription(),
                    lExecuteHandler, lEditHandler, lDeleteHandler);
        }
    }

    /**
     * Set the product table filters.
     * 
     * @param pFilters
     *            The filters.
     */
    public void setTableProductFilters(final List<UiFilterSummary> pFilters) {
        for (final UiFilterSummary lFilter : pFilters) {
            ClickHandler lExecuteHandler = null;
            ClickHandler lEditHandler = null;
            ClickHandler lDeleteHandler = null;
            if (lFilter.isExecutable()) {
                lExecuteHandler = executeTableFilter;
            }
            if (lFilter.isEditable()) {
                lEditHandler = editTableFilter;
            }
            if (lFilter.isDeletable()) {
                lDeleteHandler = deleteTableFilter;
            }
            getDisplay().addTableProductFilter(lFilter.getId(),
                    lFilter.getName(), lFilter.getDescription(),
                    lExecuteHandler, lEditHandler, lDeleteHandler);
        }
    }

    /**
     * Set the user table filters.
     * 
     * @param pFilters
     *            The filters.
     */
    public void setTableUserFilters(final List<UiFilterSummary> pFilters) {
        for (final UiFilterSummary lFilter : pFilters) {
            ClickHandler lExecuteHandler = null;
            ClickHandler lEditHandler = null;
            ClickHandler lDeleteHandler = null;
            if (lFilter.isExecutable()) {
                lExecuteHandler = executeTableFilter;
            }
            if (lFilter.isEditable()) {
                lEditHandler = editTableFilter;
            }
            if (lFilter.isDeletable()) {
                lDeleteHandler = deleteTableFilter;
            }
            getDisplay().addTableUserFilter(lFilter.getId(), lFilter.getName(),
                    lFilter.getDescription(), lExecuteHandler, lEditHandler,
                    lDeleteHandler);
        }
    }

    /**
     * Set the tree filters.
     * 
     * @param pFilters
     *            The filters.
     */
    public void setTreeFilters(final List<UiFilterSummary> pFilters) {
        for (final UiFilterSummary lFilter : pFilters) {
            getDisplay().addTreeFilter(lFilter.getId(), lFilter.getName());
        }
        getDisplay().initTreeFilterList();
    }

    /**
     * Set if it's possible to create new filter.
     * 
     * @param pAddingFilterEnable
     *            If it's possible to create new filter.
     */
    public void setAddingFilterEnable(final boolean pAddingFilterEnable) {
        addingFilterEnable = pAddingFilterEnable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected void onBind() {
        // Auto close or open, the stack layout panel
        getDisplay().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(final SelectionEvent<Integer> pEvent) {
                if (pEvent.getSelectedItem() == 0) {
                    fireEvent(getOpenCloseWorkspaceType(),
                            new OpenCloseWorkspacePanelAction(
                                    WorkspacePanelType.LISTING,
                                    WorkspacePanelAction.RESTORE));
                }
                else {
                    fireEvent(getOpenCloseWorkspaceType(),
                            new OpenCloseWorkspacePanelAction(
                                    WorkspacePanelType.LISTING,
                                    WorkspacePanelAction.MINIMIZE));
                }
            }
        });
        // Add listener to filter selection
        addEventHandler(getExecuteFilterType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {

                        if (pResult instanceof ExecuteTreeFilterResult) {
                            displayTreeFilterResult(((ExecuteTreeFilterResult) pResult).getFilterResult());
                        }
                        else {
                            Application.INJECTOR.getPopupManager().editFilter(
                                    pResult, filterEditionExecuteCommand);
                        }
                    }
                });
        // Add listener to filter selection
        addEventHandler(getRemoveTreeFilterType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(final EmptyAction pResult) {
                        getDisplay().resetTreeFilter();
                        getDisplay().setTreeToolBar(
                                treeMenuBuilder.buildMenu(false, false, false,
                                        addingFilterEnable));
                    }
                });
        // Add listener on delete filter action
        addEventHandler(getDeleteFilterType(),
                new ActionEventHandler<DeleteFilterResult>() {
                    @Override
                    public void execute(final DeleteFilterResult pResult) {
                        // Reset the filters
                        getDisplay().resetTreeFilter();
                        getDisplay().setTreeToolBar(
                                treeMenuBuilder.buildMenu(false, false, false,
                                        addingFilterEnable));
                        // Set the filters
                        getDisplay().clearFilters();
                        setTableProcessFilters(pResult.getFilters().getTableProcessFilters());
                        setTableProductFilters(pResult.getFilters().getTableProductFilters());
                        setTableUserFilters(pResult.getFilters().getTableUserFilters());
                        setTreeFilters(pResult.getFilters().getTreeFilters());
                        getDisplay().initTreeFilterList();
                    }
                });
        // No filter selected -> refresh and edit tool bar are not available
        getDisplay().setTableToolBar(
                tableMenuBuilder.buildMenu(addingFilterEnable));
        getDisplay().setTreeToolBar(
                treeMenuBuilder.buildMenu(false, false, false,
                        addingFilterEnable));
    }

    private void displayTreeFilterResult(final UiFilterTreeResult pFilterResult) {
        // Clear filter
        getDisplay().resetTreeFilter();
        // Display new filter
        getDisplay().setTreeFilterManager(getTreeFilterManager());
        final List<TreeFilterItem> lRootItems = new ArrayList<TreeFilterItem>();
        for (final UiFilterTreeResultNode lNode : pFilterResult.getResultNodes()) {
            lRootItems.add(new TreeFilterItem(lNode));
        }
        getDisplay().setTreeFilterRootItems(lRootItems);
        getDisplay().setTreeToolBar(
                treeMenuBuilder.buildMenu(true, pFilterResult.isEditable(),
                        pFilterResult.isEditable(), addingFilterEnable));
    }

    /**
     * Get the open close workspace type.
     * 
     * @return The open close workspace type.
     */
    abstract protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType();

    /**
     * Get the open close workspace type.
     * 
     * @return The open close workspace type.
     */
    abstract protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteFilterType();

    /**
     * Get the remove tree filter type type.
     * 
     * @return The remove tree filter type type.
     */
    @SuppressWarnings("rawtypes")
	abstract protected Type<ActionEventHandler<EmptyAction>> getRemoveTreeFilterType();

    /**
     * Get the delete filter type.
     * 
     * @return The delete filter type.
     */
    abstract protected Type<ActionEventHandler<DeleteFilterResult>> getDeleteFilterType();

    /**
     * Get the tree filter manager.
     * 
     * @return The tree filter manager.
     */
    abstract protected TreeFilterManager getTreeFilterManager();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to unbind
    }

    /**
     * Get the selected tree filter id.
     * 
     * @return The selected tree filter.id
     */
    public String getSelectedTreeFilterId() {
        return getDisplay().getSelectedTreeFilter();
    }
    
    /**
     * Refresh current displayed filter
     */
    public void refreshFilter() {
    	((ExecuteSheetTableFilterCommand) executeTableFilter).execute();
    }
}
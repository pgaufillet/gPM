/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.navigation;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.DeleteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.EditSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.validation.IsSheetOpenValidator;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager;
import org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.menu.user.SheetNavigationTableMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetNavigationTreeMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationAction;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the SheetNavigationView.
 * 
 * @author tpanuel
 */
public class SheetNavigationPresenter extends
        NavigationPresenter<SheetNavigationDisplay> {
    private String productName;

    /**
     * Create a presenter for the SheetNavigationView.
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
     *            The table menu builder.
     * @param pTreeMenuBuilder
     *            The tree menu builder.
     * @param pFilterEditionExecuteSheetFilterCommand
     *            The command to execute a sheet filter in edition mode.
     */
    @Inject
    public SheetNavigationPresenter(
            final SheetNavigationDisplay pDisplay,
            final EventBus pEventBus,
            final ExecuteSheetTreeFilterCommand pExecuteTreeFilterCommand,
            final ExecuteSheetTableFilterCommand pExecuteTableFilterCommand,
            final EditSheetTableFilterCommand pEditTableFilterCommand,
            final DeleteSheetTableFilterCommand pDeleteTableFilterCommand,
            final SheetNavigationTableMenuBuilder pTableMenuBuilder,
            final SheetNavigationTreeMenuBuilder pTreeMenuBuilder,
            final FilterEditionExecuteProductFilterCommand pFilterEditionExecuteSheetFilterCommand) {
        super(pDisplay, pEventBus, pExecuteTreeFilterCommand,
                pExecuteTableFilterCommand, pEditTableFilterCommand,
                pDeleteTableFilterCommand, pTableMenuBuilder, pTreeMenuBuilder,
                pFilterEditionExecuteSheetFilterCommand);
    }

    /**
     * Set the product name.
     * 
     * @param pProductName
     *            The product name.
     */
    public void setProductName(final String pProductName) {
        productName = pProductName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#onBind()
     */
    @Override
    protected void onBind() {
        super.onBind();

        // Add listener on save filter action
        addEventHandler(GlobalEvent.NEW_FILTER_SAVE.getType(),
                new ActionEventHandler<SaveFilterResult>() {
                    @Override
                    public void execute(final SaveFilterResult pResult) {
                        if (FilterType.SHEET.equals(pResult.getFilterType())
                                && productName.equals(pResult.getProductName())) {
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
                    }
                });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getOpenCloseWorkspaceType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType() {
        return LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getExecuteFilterType()
     */
    @Override
    protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteFilterType() {
        return LocalEvent.EXECUTE_SHEET_TREE_FILTER.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getRemoveTreeFilterType()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected Type<ActionEventHandler<EmptyAction>> getRemoveTreeFilterType() {
        return LocalEvent.REMOVE_SHEET_TREE_FILTER.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getDeleteFilterType()
     */
    @Override
    protected Type<ActionEventHandler<DeleteFilterResult>> getDeleteFilterType() {
        return LocalEvent.DELETE_SHEET_FILTER.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getTreeFilterManager()
     */
    @Override
    protected TreeFilterManager getTreeFilterManager() {
        return new TreeFilterManager() {
            @Override
            public void onSelection(final TreeFilterItem pItem) {
                if (pItem.isLeaf()) {
                    fireEvent(GlobalEvent.LOAD_SHEET.getType(),
                            new GetSheetVisualizationAction(productName,
                                    pItem.getId()),
                            IsSheetOpenValidator.getInstance());
                }
            }
        };
    }
}
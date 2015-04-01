/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.product.navigation;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.DeleteProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.EditProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.ExecuteProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.ExecuteProductTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductFilterCommand;
import org.topcased.gpm.ui.application.client.command.validation.IsProductOpenValidator;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager;
import org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductNavigationTableMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductNavigationTreeMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationAction;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the ProductNavigationView.
 * 
 * @author tpanuel
 */
public class ProductNavigationPresenter extends
        NavigationPresenter<ProductNavigationDisplay> {
    /**
     * Create a presenter for the ProductNavigationView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pExecuteTreeFilter
     *            Command to execute a tree filter.
     * @param pExecuteTableFilter
     *            Command to execute a table filter.
     * @param pEditTableFilter
     *            Command to edit a filter.
     * @param pDeleteTableFilter
     *            Command to delete a filter.
     * @param pTableMenuBuilder
     *            The table menu builder.
     * @param pTreeMenuBuilder
     *            The tree menu builder.
     * @param pFilterEditionExecuteProductFilterCommand
     *            The command to execute a product filter in edition mode.
     */
    @Inject
    public ProductNavigationPresenter(
            final ProductNavigationDisplay pDisplay,
            final EventBus pEventBus,
            final ExecuteProductTreeFilterCommand pExecuteTreeFilter,
            final ExecuteProductTableFilterCommand pExecuteTableFilter,
            final EditProductTableFilterCommand pEditTableFilter,
            final DeleteProductTableFilterCommand pDeleteTableFilter,
            final ProductNavigationTableMenuBuilder pTableMenuBuilder,
            final ProductNavigationTreeMenuBuilder pTreeMenuBuilder,
            final FilterEditionExecuteProductFilterCommand pFilterEditionExecuteProductFilterCommand) {
        super(pDisplay, pEventBus, pExecuteTreeFilter, pExecuteTableFilter,
                pEditTableFilter, pDeleteTableFilter, pTableMenuBuilder,
                pTreeMenuBuilder, pFilterEditionExecuteProductFilterCommand);
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
                        if (FilterType.PRODUCT.equals(pResult.getFilterType())) {
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
        return GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getExecuteFilterType()
     */
    @Override
    protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteFilterType() {
        return GlobalEvent.EXECUTE_PRODUCT_TREE_FILTER.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getRemoveTreeFilterType()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected Type<ActionEventHandler<EmptyAction>> getRemoveTreeFilterType() {
        return GlobalEvent.REMOVE_PRODUCT_TREE_FILTER.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.navigation.NavigationPresenter#getDeleteFilterType()
     */
    @Override
    protected Type<ActionEventHandler<DeleteFilterResult>> getDeleteFilterType() {
        return GlobalEvent.DELETE_PRODUCT_FILTER.getType();
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
                    fireEvent(GlobalEvent.LOAD_PRODUCT.getType(),
                            new GetProductVisualizationAction(pItem.getId()),
                            IsProductOpenValidator.getInstance());
                }
            }
        };
    }
}
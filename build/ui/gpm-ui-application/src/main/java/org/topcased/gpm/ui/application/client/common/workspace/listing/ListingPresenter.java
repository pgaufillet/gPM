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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AbstractFilterEditionExecuteCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterResult;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for the ListingView.
 * 
 * @author tpanuel
 * @param <L>
 *            The listing display type.
 */
public abstract class ListingPresenter<L extends ListingDisplay> extends
        AbstractPresenter<L> {
    private final AbstractMenuBuilder menuBuilder;

    private final GpmBasicActionHandler<String> visu;

    private final GpmBasicActionHandler<String> edit;

    private AbstractFilterEditionExecuteCommand filterEditionExecuteCommand;

    private String currentFilterId;

    /**
     * Create a presenter for the ListingView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            Command to open a container on visualization.
     * @param pEditCommand
     *            Command to open a container on edition.
     * @param pFilterEditionExecuteCommand
     *            The command to execute a filter in edition mode.
     */
    public ListingPresenter(
            final L pDisplay,
            final EventBus pEventBus,
            final AbstractMenuBuilder pMenuBuilder,
            final GpmBasicActionHandler<String> pVisuCommand,
            final GpmBasicActionHandler<String> pEditCommand,
            final AbstractFilterEditionExecuteCommand pFilterEditionExecuteCommand) {
        super(pDisplay, pEventBus);
        menuBuilder = pMenuBuilder;
        visu = pVisuCommand;
        edit = pEditCommand;
        filterEditionExecuteCommand = pFilterEditionExecuteCommand;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        addEventHandler(getExecuteTableFilterType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        if (pResult instanceof ExecuteTableFilterResult) {
                            displayFilter((ExecuteTableFilterResult) pResult);
                            fireEvent(getOpenCloseWorkspaceType(),
                                    new OpenCloseWorkspacePanelAction(
                                            WorkspacePanelType.LISTING,
                                            WorkspacePanelAction.RESTORE));
                        }
                        else {
                            Application.INJECTOR.getPopupManager().editFilter(
                                    pResult, filterEditionExecuteCommand);
                        }
                    }
                });
        getDisplay().setToolBar(menuBuilder.buildMenu(null, null));
    }

    /**
     * Get the open/close event type for the workspace
     * 
     * @return he open/close event type for the workspace
     */
    abstract protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType();

    /**
     * Get the execute table filter type.
     * 
     * @return The execute table filter type.
     */
    abstract protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteTableFilterType();

    /**
     * Display a filter.
     * 
     * @param pResult
     *            The filter result
     */
    public void displayFilter(final ExecuteTableFilterResult pResult) {
        currentFilterId = pResult.getFilterId();
        // Title
        getDisplay().setFilterInfo(pResult.getFilterName(),
                pResult.getFilterDescription());
        // Table
        getDisplay().initTable();
        for (String lColumnName : pResult.getFilterResult().getColumnNames()) {
            getDisplay().addValuesColumn(lColumnName);
        }
        getDisplay().setData(pResult.getFilterResult().getResultValues());
        getDisplay().setVisuHandler(visu);
        getDisplay().setEditHandler(edit);
        getDisplay().setFilterExecutionReport(
                pResult.getFilterResult().getFilterExecutionReport());
        // Menu
        getDisplay().setToolBar(
                menuBuilder.buildMenu(pResult.getActions(),
                        pResult.getExtendedActions()));
    }

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
     * Get the selected table filter id.
     * 
     * @return The selected table filter.id
     */
    public String getSelectedTableFilterId() {
        return currentFilterId;
    }

    /**
     * Get the selected elements id.
     * 
     * @return The selected elements id.
     */
    public List<String> getSelectedElementIds() {
        return getDisplay().getSelectedElementIds();
    }
}
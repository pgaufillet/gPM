/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.detail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.container.ContainerDisplay;
import org.topcased.gpm.ui.application.client.common.container.ContainerPresenter;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.common.tab.TabLayoutPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for the DetailView.
 * 
 * @author tpanuel
 * @param <P>
 *            The type of sub element.
 * @param <D>
 *            The type of the detail.
 * @param <SD>
 *            The type of display of sub element.
 */
public abstract class DetailPresenter<P extends ContainerPresenter<SD>, D extends DetailDisplay<SD>, SD extends ContainerDisplay>
        extends TabLayoutPresenter<D, SD> {
    /**
     * Create a presenter for the DetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    public DetailPresenter(final D pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Add a container.
     * 
     * @param pResult
     *            The container to add.
     */
    public void addContainer(final GetContainerResult<?> pResult) {
        final P lPresenter;

        // Get good presenter type
        switch (pResult.getDisplayMode()) {
            case CREATION:
                lPresenter = getCreationPresenter();
                break;
            case VISUALIZATION:
                lPresenter = getVisualizationPresenter();
                break;
            case EDITION:
            default:
                lPresenter = getEditionPresenter();
                break;
        }
        // Initialize presenter
        lPresenter.initContainer(pResult);
        addOrReplaceTab(lPresenter);
    }

    /**
     * Get the creation presenter.
     * 
     * @return The creation presenter.
     */
    abstract protected P getCreationPresenter();

    /**
     * Get the visualization presenter.
     * 
     * @return The visualization presenter.
     */
    abstract protected P getVisualizationPresenter();

    /**
     * Get the edition presenter.
     * 
     * @return the edition presenter
     */
    abstract protected P getEditionPresenter();

    /**
     * Handles the confirmation behavior for closing a detailed view
     * 
     * @param pTabId
     *            the Tab ID to close
     * @return The message needed when closing a Detailed View if necessary, or
     *         returns null
     */
    abstract protected String needCloseConfirmation(String pTabId);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutPresenter#getCloseClickHandler(java.lang.String)
     */
    @Override
    protected ClickHandler getCloseClickHandler(final String pTabId) {
        return new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pArg0) {
                String lMessage = needCloseConfirmation(pTabId);
                if (lMessage != null) {
                    fireEvent(getCloseTabType(), new CloseTabAction(pTabId,
                            true), lMessage);
                }
                else {
                    fireEvent(getCloseTabType(), new CloseTabAction(pTabId,
                            true));
                }
            }
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutPresenter#addOrReplaceTab(org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter)
     */
    @Override
    public void addOrReplaceTab(final TabElementPresenter<SD> pTabPresenter) {

        super.addOrReplaceTab(pTabPresenter);
        // Make detail zone visible
        fireEvent(getOpenCloseWorkspaceType(),
                new OpenCloseWorkspacePanelAction(WorkspacePanelType.DETAIL,
                        WorkspacePanelAction.MAKEVISIBLE));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutPresenter#removeTab(java.lang.String)
     */
    @Override
    public void removeTab(final String pTabId) {
        super.removeTab(pTabId);
        // Close detail zone
        if (getNbTab() == 0) {
            fireEvent(getOpenCloseWorkspaceType(),
                    new OpenCloseWorkspacePanelAction(
                            WorkspacePanelType.DETAIL,
                            WorkspacePanelAction.HIDE));
        }
    }

    /**
     * Get the current container.
     * 
     * @return The current container.
     */
    @SuppressWarnings("unchecked")
    public P getCurrentContainer() {
        return (P) getSelectedTab();
    }

    /**
     * Get the close tab type.
     * 
     * @return The close tab type.
     */
    abstract protected Type<ActionEventHandler<CloseTabAction>> getCloseTabType();

    /**
     * Get the open close workspace type.
     * 
     * @return The open close workspace type.
     */
    abstract protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType();
}
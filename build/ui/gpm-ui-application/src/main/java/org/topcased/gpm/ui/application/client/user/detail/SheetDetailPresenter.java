/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.detail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.container.ContainerPresenter;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksResult;
import org.topcased.gpm.ui.application.shared.command.sheet.CloseSheetAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the SheetDetailView.
 * 
 * @author tpanuel
 */
public class SheetDetailPresenter extends
        DetailPresenter<SheetPresenter, SheetDetailDisplay, SheetDisplay> {

    private String productName;

    /**
     * Create a presenter for the SheetDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public SheetDetailPresenter(final SheetDetailDisplay pDisplay,
            final EventBus pEventBus) {
        super(pDisplay, pEventBus);
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
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getCreationPresenter()
     */
    @Override
    protected SheetPresenter getCreationPresenter() {
        return Application.INJECTOR.getSheetCreationPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getVisualizationPresenter()
     */
    @Override
    protected SheetPresenter getVisualizationPresenter() {
        return Application.INJECTOR.getSheetVisualizationPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getEditionPresenter()
     */
    @Override
    protected SheetPresenter getEditionPresenter() {
        return Application.INJECTOR.getSheetEditionPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getCloseTabType()
     */
    @Override
    protected Type<ActionEventHandler<CloseTabAction>> getCloseTabType() {
        return LocalEvent.CLOSE_SHEET.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getOpenCloseWorkspaceType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType() {
        return LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    protected void onBind() {
        // Add handler for close tab
        addEventHandler(LocalEvent.CLOSE_SHEET.getType(productName),
                new ActionEventHandler<CloseTabAction>() {
                    @Override
                    public void execute(final CloseTabAction pResult) {
                        removeTab(pResult.getTabId());
                        if (pResult.isSendServer()) {
                            fireEvent(GlobalEvent.CLOSE_CONTAINER.getType(),
                                    new CloseSheetAction(productName,
                                            pResult.getTabId()));
                        }
                    }
                });
        // Add handler for get links
        addEventHandler(LocalEvent.LOAD_SHEET_LINKS.getType(productName),
                new ActionEventHandler<GetLinksResult>() {
                    @Override
                    public void execute(final GetLinksResult pResult) {
                        getCurrentContainer().initLinks(
                                pResult.getValuesContainerId(),
                                pResult.getLinkTypeName(), pResult.getLinks());
                    }
                });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#needCloseConfirmation(java.lang.String)
     */
    @Override
    protected String needCloseConfirmation(String pTabId) {
        if (getPresenterById(pTabId) instanceof SheetEditionPresenter
                || getPresenterById(pTabId) instanceof SheetCreationPresenter) {
            ContainerPresenter<?> lContainer =
                    (ContainerPresenter<?>) getPresenterById(pTabId);
            if (lContainer.getUpdatedFields().size() != 0
                    || lContainer.getUpdatedLinkFields().size() != 0) {
                return Ui18n.MESSAGES.confirmationSheetClose();
            }
        }
        return null;
    }
}

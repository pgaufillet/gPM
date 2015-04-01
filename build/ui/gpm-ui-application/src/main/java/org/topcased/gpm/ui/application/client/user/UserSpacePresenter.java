/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user;

import java.util.ArrayList;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.connection.LaunchSheetAction;
import org.topcased.gpm.ui.application.client.command.connection.LaunchWorkspaceAction;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.common.tab.TabLayoutPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductAction;
import org.topcased.gpm.ui.application.shared.command.extendedaction.AbstractExecuteExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.FilterExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetSheetsExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * The presenter for the UserSpaceView.
 * 
 * @author tpanuel
 */
public class UserSpacePresenter extends
        TabLayoutPresenter<UserSpaceDisplay, ProductWorkspaceDisplay> {
    /**
     * Create a presenter for the UserSpaceView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public UserSpacePresenter(final UserSpaceDisplay pDisplay,
            final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().getAddTabButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pArg0) {
                fireEvent(GlobalEvent.CONNECTION.getType(),
                        new SelectProductAction());
            }
        });
        addEventHandler(GlobalEvent.LAUNCH_WORKSPACE.getType(),
                new ActionEventHandler<LaunchWorkspaceAction>() {
                    @Override
                    public void execute(LaunchWorkspaceAction pResult) {
                        openWorkspace(pResult.getConnectResult());
                    }
                });
        addEventHandler(GlobalEvent.LAUNCH_SHEET.getType(),
                new ActionEventHandler<LaunchSheetAction>() {
                    @Override
                    public void execute(LaunchSheetAction pResult) {
                        openSheet(pResult.getSheetResult());
                    }
                });
        // Add handler for close product
        addEventHandler(GlobalEvent.CLOSE_PRODUCT_WORKSPACE.getType(),
                new ActionEventHandler<DisconnectProductResult>() {
                    @Override
                    public void execute(final DisconnectProductResult pResult) {
                        removeTab(pResult.getProductName());
                    }
                });
        // Add handler to open a sheet on the good workspace
        addEventHandler(GlobalEvent.LOAD_SHEET.getType(),
                new ActionEventHandler<GetSheetResult>() {
                    @Override
                    public void execute(final GetSheetResult pResult) {
                        //Check if the sheetResult contains a locked sheet
                        if (pResult.isConfirmationRequired()
                                && DisplayMode.VISUALIZATION.equals(pResult.getDisplayMode())) {
                            openLockedConfirmation(pResult);
                        }
                        else {
                            //If no lock is detected the sheet is opened in edition mode
                            openSheet(pResult);
                        }

                    }
                });

        // Add handler to open a sheet or a filter after an extended action
        addEventHandler(GlobalEvent.EXECUTE_EXTENDED_ACTION.getType(),
                new ActionEventHandler<AbstractExecuteExtendedActionResult>() {
                    @Override
                    public void execute(
                            final AbstractExecuteExtendedActionResult pResult) {
                        if (pResult instanceof GetSheetsExtendedActionResult) {
                            // Open a list of sheet
                            final GetSheetsExtendedActionResult lSheetResults =
                                    (GetSheetsExtendedActionResult) pResult;

                            for (final GetSheetResult lSheetResult : lSheetResults.getSheetResults()) {
                                openSheet(lSheetResult);
                            }
                        }
                        else if (pResult instanceof FilterExtendedActionResult) {
                            // Open a filter result on the current product
                            getCurrentProductWorkspace().getListing().displayFilter(
                                    ((FilterExtendedActionResult) pResult).getExecuteTableFilterResult());
                        }
                    }
                });
    }

    /**
     * This method will prompt a pop-up in order to ask if the sheet shall be
     * opened in visualization mode instead of edition (due to edition lock on
     * sheet)
     * 
     * @param pResult
     *            the {@link GetSheetResult}
     */
    protected void openLockedConfirmation(final GetSheetResult pResult) {
        // Ui18n.CONSTANTS
        Application.INJECTOR.getConfirmationMessagePresenter().displayQuestion(
                Ui18n.CONSTANTS.sheetLockedConfirmation(), new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent pClickEvent) {
                        // Execute the action
                        openSheet(pResult);
                    }
                });
    }

    private void openSheet(final GetSheetResult pResult) {
        final String lProductName = pResult.getContainer().getProductName();
        final ProductWorkspacePresenter lProductWorkspacePresenter;

        if (containTab(lProductName)) {
            // If the product work space is already open
            // Only select id
            lProductWorkspacePresenter =
                    (ProductWorkspacePresenter) selectTab(lProductName);
        }
        else {
            // Else open a new product workspace
            lProductWorkspacePresenter =
                    Application.INJECTOR.getProductWorkspacePresenter();
            lProductWorkspacePresenter.init(pResult.getConnectResult(), true);
            addOrReplaceTab(lProductWorkspacePresenter);
        }
        // Open the sheet
        lProductWorkspacePresenter.getDetail().addContainer(pResult);
    }

    /**
     * Search a sheet presenter in all the workspace.
     * 
     * @param pSheetId
     *            The sheet id.
     * @return The sheet presenter. Null, if not found.
     */
    public SheetPresenter getSheetPresenter(final String pSheetId) {
        // Search on all workspace
        for (TabElementPresenter<ProductWorkspaceDisplay> lProduct : getPresenters()) {
            final SheetDetailPresenter lDetail =
                    ((ProductWorkspacePresenter) lProduct).getDetail();

            // Search on the detail zone
            for (TabElementPresenter<SheetDisplay> lSheet : lDetail.getPresenters()) {
                if (lSheet.getTabId().equals(pSheetId)) {
                    // Select the product workspace and the sheet, if found
                    selectTab(lProduct.getTabId());
                    lDetail.selectTab(pSheetId);

                    return (SheetPresenter) lSheet;
                }
            }
        }

        return null;
    }

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
                // Check if one sheet at least has been modified
                final ProductWorkspacePresenter lCurrentProductPresenter =
                        (ProductWorkspacePresenter) getPresenterById(pTabId);
                ArrayList<String> lOpenedSheetIds =
                        new ArrayList<String>(
                                lCurrentProductPresenter.getDetail().getPresenterIds());
                boolean lModified = false;
                for (String lId : lOpenedSheetIds) {
                    SheetPresenter lSheetPresenter =
                            (SheetPresenter) lCurrentProductPresenter.getDetail().getPresenterById(
                                    lId);
                    if (lSheetPresenter != null
                            && lSheetPresenter.getUpdatedFields().size() != 0
                            || lSheetPresenter.getUpdatedLinkFields().size() != 0) {
                        lModified = true;
                    }
                }

                if (lModified) { // One sheet modified : ask confirmation
                    fireEvent(
                            GlobalEvent.CLOSE_PRODUCT_WORKSPACE.getType(),
                            new DisconnectProductAction(pTabId, lOpenedSheetIds),
                            Ui18n.MESSAGES.confirmationCloseProduct());
                }
                else {
                    fireEvent(
                            GlobalEvent.CLOSE_PRODUCT_WORKSPACE.getType(),
                            new DisconnectProductAction(pTabId, lOpenedSheetIds));
                }
            }
        };
    }

    /**
     * Get the current product workspace.
     * 
     * @return The current product workspace.
     */
    public ProductWorkspacePresenter getCurrentProductWorkspace() {
        return (ProductWorkspacePresenter) getSelectedTab();
    }

    /**
     * Open a workspace
     * 
     * @param pResult
     *            Workspace informations.
     */
    private void openWorkspace(ConnectResult pResult) {
        final ProductWorkspacePresenter lProductWorkspacePresenter =
                Application.INJECTOR.getProductWorkspacePresenter();

        lProductWorkspacePresenter.init(pResult, false);
        addOrReplaceTab(lProductWorkspacePresenter);
    }
}
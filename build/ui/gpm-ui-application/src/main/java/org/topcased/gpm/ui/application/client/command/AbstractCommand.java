/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.AdminPresenter;
import org.topcased.gpm.ui.application.client.admin.product.ProductAdminPresenter;
import org.topcased.gpm.ui.application.client.command.validation.ViewValidator;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEvent;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.popup.PopupManager;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.application.client.user.UserSpacePresenter;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFile;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Create an abstract command.
 * 
 * @author tpanuel
 */
public abstract class AbstractCommand {
    private final static UserSpacePresenter USER_SPACE =
            Application.INJECTOR.getUserSpacePresenter();

    private final static AdminPresenter ADMIN_SPACE =
            Application.INJECTOR.getAdminPresenter();

    private final static PopupManager POPUP_MANAGER =
            Application.INJECTOR.getPopupManager();

    protected final EventBus eventBus;

    /**
     * Create an AbstractCommand.
     * 
     * @param pEventBus
     *            The event bus.
     */
    public AbstractCommand(final EventBus pEventBus) {
        eventBus = pEventBus;
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction) {
        eventBus.fireEvent(new ActionEvent<A, R>(pType, pAction));
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param <NA>
     *            The type of next action.
     * @param <NR>
     *            The type of next result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pNextType
     *            The type of next event.
     * @param pNextAction
     *            The next action.
     */
    public <A extends Action<R>, R extends Result, NA extends Action<NR>, NR extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final Type<ActionEventHandler<NR>> pNextType, final NA pNextAction) {
        final ActionEvent<A, R> lAction = new ActionEvent<A, R>(pType, pAction);

        lAction.setNextEvent(new ActionEvent<NA, NR>(pNextType, pNextAction));
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator) {
        eventBus.fireEvent(new ActionEvent<A, R>(pType, pAction,
                CollectionUtil.singleton(pViewValidator)));
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator, String pConfirmationMessage) {
        ActionEvent<A, R> lAction =
                new ActionEvent<A, R>(pType, pAction,
                        CollectionUtil.singleton(pViewValidator));

        lAction.setConfirmationMessage(pConfirmationMessage);
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param <NA>
     *            The type of next action.
     * @param <NR>
     *            The type of next result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pNextType
     *            The type of next event.
     * @param pNextAction
     *            The next action.
     */
    public <A extends Action<R>, R extends Result, NA extends Action<NR>, NR extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator,
            final Type<ActionEventHandler<NR>> pNextType, final NA pNextAction) {
        final ActionEvent<A, R> lAction =
                new ActionEvent<A, R>(pType, pAction,
                        CollectionUtil.singleton(pViewValidator));

        lAction.setNextEvent(new ActionEvent<NA, NR>(pNextType, pNextAction));
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final String pConfirmationMessage) {
        final ActionEvent<A, R> lAction = new ActionEvent<A, R>(pType, pAction);

        lAction.setConfirmationMessage(pConfirmationMessage);
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param <NA>
     *            The type of next action.
     * @param <NR>
     *            The type of next result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pNextType
     *            The type of next event.
     * @param pNextAction
     *            The next action.
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public <A extends Action<R>, R extends Result, NA extends Action<NR>, NR extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final Type<ActionEventHandler<NR>> pNextType, final NA pNextAction,
            final String pConfirmationMessage) {
        final ActionEvent<A, R> lAction = new ActionEvent<A, R>(pType, pAction);

        lAction.setNextEvent(new ActionEvent<NA, NR>(pNextType, pNextAction));
        lAction.setConfirmationMessage(pConfirmationMessage);
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param <NA>
     *            The type of next action.
     * @param <NR>
     *            The type of next result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pNextType
     *            The type of next event.
     * @param pNextAction
     *            The next action.
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public <A extends Action<R>, R extends Result, NA extends Action<NR>, NR extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator,
            final Type<ActionEventHandler<NR>> pNextType, final NA pNextAction,
            final String pConfirmationMessage) {
        final ActionEvent<A, R> lAction =
                new ActionEvent<A, R>(pType, pAction,
                        CollectionUtil.singleton(pViewValidator));

        lAction.setNextEvent(new ActionEvent<NA, NR>(pNextType, pNextAction));
        lAction.setConfirmationMessage(pConfirmationMessage);
        eventBus.fireEvent(lAction);
    }

    /**
     * Upload files and fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pUploadRegisters
     *            The upload registers.
     */
    public <A extends Action<R>, R extends Result> void uploadFileAndFireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator,
            final List<GpmUploadFileRegister> pUploadRegisters) {
        uploadFiles(pUploadRegisters, new ActionEvent<A, R>(pType, pAction,
                CollectionUtil.singleton(pViewValidator)));
    }

    /**
     * Upload files and fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pUploadRegisters
     *            The upload registers.
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public <A extends Action<R>, R extends Result> void uploadFileAndFireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator,
            final List<GpmUploadFileRegister> pUploadRegisters,
            final String pConfirmationMessage) {
        ActionEvent<A, R> lAction =
                new ActionEvent<A, R>(pType, pAction,
                        CollectionUtil.singleton(pViewValidator));
        lAction.setConfirmationMessage(pConfirmationMessage);
        uploadFiles(pUploadRegisters, lAction);
    }

    /**
     * Upload files and fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param <NA>
     *            The type of next action.
     * @param <NR>
     *            The type of next result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     * @param pNextType
     *            The type of next event.
     * @param pNextAction
     *            The next action.
     * @param pUploadRegisters
     *            The upload registers.
     */
    public <A extends Action<R>, R extends Result, NA extends Action<NR>, NR extends Result> void uploadFileAndFireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator,
            final Type<ActionEventHandler<NR>> pNextType, final NA pNextAction,
            final List<GpmUploadFileRegister> pUploadRegisters) {
        final ActionEvent<A, R> lAction =
                new ActionEvent<A, R>(pType, pAction,
                        CollectionUtil.singleton(pViewValidator));

        lAction.setNextEvent(new ActionEvent<NA, NR>(pNextType, pNextAction));
        uploadFiles(pUploadRegisters, lAction);
    }

    private void uploadFiles(
            final List<GpmUploadFileRegister> pUploadRegisters,
            final GwtEvent<?> pAction) {
        final Stack<GpmUploadFile> lUploadFiles = new Stack<GpmUploadFile>();

        // Search all the file to upload
        for (final GpmUploadFileRegister lRegister : pUploadRegisters) {
            for (final GpmUploadFile lUploadFile : lRegister) {
                if (lUploadFile.getWidget().hasFileToUpload()) {
                    lUploadFiles.push(lUploadFile);
                }
            }
            // Clear the register
            lRegister.unregisterAll();
        }
        // Launch the file upload
        launchUploadFiles(lUploadFiles, pAction);
    }

    private void launchUploadFiles(final Stack<GpmUploadFile> pUploadFiles,
            final GwtEvent<?> pAction) {
        if (pUploadFiles.isEmpty()) {
            // Hide the upload file popup
            Application.INJECTOR.getUploadMessagePresenter().unbind();
            // Launch the event
            eventBus.fireEvent(pAction);
        }
        else {
            final GpmUploadFile lUploadFile = pUploadFiles.pop();

            // Display the upload file popup
            Application.INJECTOR.getUploadMessagePresenter().displayUpload(
                    lUploadFile.getFileName());
            // Upload the next file
            lUploadFile.getWidget().sendFile(Application.UPLOAD_URL,
                    new SubmitCompleteHandler() {
                        @Override
                        public void onSubmitComplete(
                                final SubmitCompleteEvent pEvent) {
                            launchUploadFiles(pUploadFiles, pAction);
                        }
                    });
        }
    }

    /**
     * Get the product name of the current workspace.
     * 
     * @return The product name of the current workspace.
     */
    public String getCurrentProductWorkspaceName() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return null;
        }
        else {
            return lCurrentProductWorkspace.getTabId();
        }
    }

    /**
     * Get the current sheet id on the detail panel.
     * 
     * @return The current sheet id.
     */
    public String getCurrentSheetId() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return null;
        }
        else {
            final SheetPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();

            if (lCurrentSheet == null) {
                return null;
            }
            else {
                return lCurrentSheet.getTabId();
            }
        }
    }

    /**
     * Get the current sheet version on the detail panel.
     * 
     * @return The current sheet version.
     */
    public int getCurrentSheetVersion() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return -1;
        }
        else {
            final SheetPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();

            if (lCurrentSheet == null) {
                return -1;
            }
            else {
                return lCurrentSheet.getVersion();
            }
        }
    }

    /**
     * Get the updated fields of the current sheet on the detail panel.
     * 
     * @return The current sheet id.
     */
    public List<UiField> getCurrentUpdatedSheetFields() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return new ArrayList<UiField>();
        }
        else {
            final SheetPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();

            if (lCurrentSheet == null) {
                return new ArrayList<UiField>();
            }
            else {
                return lCurrentSheet.getUpdatedFields();
            }
        }
    }

    /**
     * Get the upload registers for the current sheet.
     * 
     * @return The upload registers for the current sheet.
     */
    public List<GpmUploadFileRegister> getCurrentSheetUploadRegisters() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return new ArrayList<GpmUploadFileRegister>();
        }
        else {
            final SheetPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();

            if (lCurrentSheet == null) {
                return new ArrayList<GpmUploadFileRegister>();
            }
            else {
                return lCurrentSheet.getUploadRegisters();
            }
        }
    }

    /**
     * Get the update link fields updated of the current sheet on the detail
     * panel.
     * 
     * @return the Map of the fields by link id
     */
    public Map<String, List<UiField>> getCurrentUpdatedLinkFields() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();
        if (lCurrentProductWorkspace != null) {
            final SheetPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();
            if (lCurrentSheet != null) {
                return lCurrentSheet.getUpdatedLinkFields();
            }
        }
        return new HashMap<String, List<UiField>>();
    }

    /**
     * Indicates if current sheet has been modified by the user since last save
     * 
     * @return <CODE>true</CODE>if current sheet has been modified by the user
     *         since last save
     */
    public boolean isCurrentSheetModified() {
        return getCurrentUpdatedLinkFields().size() == 0
                && getCurrentUpdatedSheetFields().size() == 0;
    }

    /**
     * Get the current sheets id on the listing panel.
     * 
     * @return The current sheets id.
     */
    public List<String> getCurrentSheetIds() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return new ArrayList<String>();
        }
        else {
            return lCurrentProductWorkspace.getListing().getSelectedElementIds();
        }
    }

    /**
     * Get the executed sheet tree filter id.
     * 
     * @return The executed sheet tree filter id.
     */
    public String getCurrentSheetTreeFilterId() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return null;
        }
        else {
            return lCurrentProductWorkspace.getNavigation().getSelectedTreeFilterId();
        }
    }

    /**
     * Get the executed sheet table filter id.
     * 
     * @return The executed sheet table filter id.
     */
    public String getCurrentSheetTableFilterId() {
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                USER_SPACE.getCurrentProductWorkspace();

        if (lCurrentProductWorkspace == null) {
            return null;
        }
        else {
            return lCurrentProductWorkspace.getListing().getSelectedTableFilterId();
        }
    }

    /**
     * Get the current product id on the detail panel of the admin space.
     * 
     * @return The current product id on the detail panel of the admin space.
     */
    public String getCurrentProductId() {
        final ProductPresenter lCurrentProduct =
                ADMIN_SPACE.getProductAdmin().getDetail().getCurrentContainer();

        if (lCurrentProduct == null) {
            return null;
        }
        else {
            return lCurrentProduct.getTabId();
        }
    }

    /**
     * Get the executed product tree filter id.
     * 
     * @return The executed product tree filter id.
     */
    public String getCurrentProductTreeFilterId() {
        return ADMIN_SPACE.getProductAdmin().getNavigation().getSelectedTreeFilterId();
    }

    /**
     * Get the current product id on the listing panel.
     * 
     * @return The current product id.
     */
    public List<String> getCurrentProductIds() {
        return ADMIN_SPACE.getProductAdmin().getListing().getSelectedElementIds();
    }

    /**
     * Get the executed product table filter id.
     * 
     * @return The executed product table filter id.
     */
    public String getCurrentProductTableFilterId() {
        return ADMIN_SPACE.getProductAdmin().getListing().getSelectedTableFilterId();
    }

    /**
     * Get the presenter of the current product on the detail panel.
     * 
     * @return The current product presenter
     */
    public ProductPresenter getCurrentProductPresenter() {
        return ADMIN_SPACE.getProductAdmin().getDetail().getCurrentContainer();
    }

    /**
     * Returns the list of currently edited product modified fields
     * 
     * @return The list of currently edited product modified fields
     */
    public List<UiField> getCurrentUpdatedProductFields() {
        final ProductAdminPresenter lCurrentProductWorkspace =
                ADMIN_SPACE.getProductAdmin();
        if (lCurrentProductWorkspace == null) {
            return new ArrayList<UiField>();
        }
        else {
            final ProductPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();
            if (lCurrentSheet == null) {
                return new ArrayList<UiField>();
            }
            else {
                return lCurrentSheet.getUpdatedFields();
            }
        }
    }

    /**
     * Returns the list of currently edited product modified link fields
     * 
     * @return The list of currently edited product modified link fields
     */
    public Map<String, List<UiField>> getCurrentUpdatedProductLinkFields() {
        final ProductAdminPresenter lCurrentProductWorkspace =
                ADMIN_SPACE.getProductAdmin();
        if (lCurrentProductWorkspace != null) {
            final ProductPresenter lCurrentSheet =
                    lCurrentProductWorkspace.getDetail().getCurrentContainer();
            if (lCurrentSheet != null) {
                return lCurrentSheet.getUpdatedLinkFields();
            }
        }
        return new HashMap<String, List<UiField>>();
    }

    /**
     * Indicates if current sheet has been modified by the user since last save
     * 
     * @return <CODE>true</CODE>if current sheet has been modified by the user
     *         since last save
     */
    public boolean isCurrentProductModified() {
        return getCurrentUpdatedProductLinkFields().size() == 0
                && getCurrentUpdatedProductFields().size() == 0;
    }

    /**
     * Indicate of one at least of all elements in workspace has been modified
     * by the user. It includes all sheets in all opened products in user space,
     * and all opened product in administration space
     * 
     * @return <CODE>true</CODE> if one at least of all elements in workspace
     *         has been modified
     */
    protected boolean isOneOfWorkspaceElementsModified() {
        // ADMIN_SPACE
        final ProductAdminPresenter lCurrentAdminWorkspace =
                ADMIN_SPACE.getProductAdmin();
        if (lCurrentAdminWorkspace != null) {
            for (String lProductID : lCurrentAdminWorkspace.getDetail().getPresenterIds()) {
                final ProductPresenter lCurrentProduct =
                        (ProductPresenter) lCurrentAdminWorkspace.getDetail().getPresenterById(
                                lProductID);
                if (lCurrentProduct != null
                        && lCurrentProduct.getUpdatedFields().size() != 0
                        || lCurrentProduct.getUpdatedLinkFields().size() != 0) {
                    return true;
                }
            }
        }
        // PRODUCT WORKSPACES
        for (String lProductTabId : USER_SPACE.getPresenterIds()) {
            ProductWorkspacePresenter lCurrentProductWorkspace =
                    (ProductWorkspacePresenter) USER_SPACE.getPresenterById(lProductTabId);
            if (lCurrentProductWorkspace != null) {
                for (String lSheetId : lCurrentProductWorkspace.getDetail().getPresenterIds()) {
                    final SheetPresenter lCurrentSheet =
                            (SheetPresenter) lCurrentProductWorkspace.getDetail().getPresenterById(
                                    lSheetId);
                    if (lCurrentSheet != null
                            && lCurrentSheet.getUpdatedFields().size() != 0
                            || lCurrentSheet.getUpdatedLinkFields().size() != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Get the popup manager.
     * 
     * @return The popup manager.
     */
    public PopupManager getPopupManager() {
        return POPUP_MANAGER;
    }
}
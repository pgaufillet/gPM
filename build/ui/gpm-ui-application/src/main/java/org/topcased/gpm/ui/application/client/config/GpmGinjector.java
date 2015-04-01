/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.config;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.admin.AdminPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductCreationPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductEditionPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductVisualizationPresenter;
import org.topcased.gpm.ui.application.client.command.admin.product.product.AddProductLinkCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.DeleteProductLinkCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.connection.LogoutCommand;
import org.topcased.gpm.ui.application.client.command.extended.ExecuteExtendedActionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.AddSheetLinkCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ChangeSheetStateCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DeleteSheetLinkCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DuplicateSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.InitializeSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.CloseSheetsCommand;
import org.topcased.gpm.ui.application.client.main.MainPresenter;
import org.topcased.gpm.ui.application.client.popup.PopupManager;
import org.topcased.gpm.ui.application.client.popup.message.ConfirmationMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.ErrorMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.InfoMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.UploadMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfilePresenter;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.application.client.user.UserSpacePresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetCreationPresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetEditionPresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetVisualizationPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * Injector for element that are not injected by constructor.
 * 
 * @author tpanuel
 */
@GinModules( { GpmCommandModule.class, GpmBuilderModule.class,
              GpmPresenterModule.class })
public interface GpmGinjector extends Ginjector {
    /**
     * Get the event bus.
     * 
     * @return The event bus.
     */
    public EventBus getEventBus();

    /* PRESENTER */
    /**
     * Get the main presenter.
     * 
     * @return The main presenter.
     */
    public MainPresenter getMainPresenter();

    /**
     * Get the user space presenter.
     * 
     * @return The user space presenter.
     */
    public UserSpacePresenter getUserSpacePresenter();

    /**
     * Get the administration presenter.
     * 
     * @return The administration presenter.
     */
    public AdminPresenter getAdminPresenter();

    /**
     * Get a product workspace presenter.
     * 
     * @return A product workspace presenter.
     */
    public ProductWorkspacePresenter getProductWorkspacePresenter();

    /**
     * Get a sheet creation presenter.
     * 
     * @return A sheet creation presenter.
     */
    public SheetCreationPresenter getSheetCreationPresenter();

    /**
     * Get a sheet visualization presenter.
     * 
     * @return A sheet visualization presenter.
     */
    public SheetVisualizationPresenter getSheetVisualizationPresenter();

    /**
     * Get a sheet edition presenter.
     * 
     * @return A sheet edition presenter.
     */
    public SheetEditionPresenter getSheetEditionPresenter();

    /**
     * Get a product creation presenter.
     * 
     * @return A product creation presenter.
     */
    public ProductCreationPresenter getProductCreationPresenter();

    /**
     * Get a product visualization presenter.
     * 
     * @return A product visualization presenter.
     */
    public ProductVisualizationPresenter getProductVisualizationPresenter();

    /**
     * Get a product edition presenter.
     * 
     * @return A product edition presenter.
     */
    public ProductEditionPresenter getProductEditionPresenter();

    /* MESSAGES */
    /**
     * Get an error message presenter.
     * 
     * @return An error message presenter.
     */
    public ErrorMessagePresenter getErrorMessagePresenter();

    /**
     * Get an error session message presenter.
     * 
     * @return An error session message presenter.
     */
    public ErrorSessionMessagePresenter getErrorSessionMessagePresenter();

    /**
     * Get an info message presenter.
     * 
     * @return An info message presenter.
     */
    public InfoMessagePresenter getInfoMessagePresenter();

    /**
     * Get a confirmation message presenter.
     * 
     * @return A confirmation message presenter.
     */
    public ConfirmationMessagePresenter getConfirmationMessagePresenter();

    /**
     * Get the upload message presenter.
     * 
     * @return The upload message presenter.
     */
    public UploadMessagePresenter getUploadMessagePresenter();

    /**
     * Get the user profile edition presenter
     * 
     * @return the user profile edition presenter
     */
    public UserProfilePresenter getUserProfilePresenter();

    /* MANAGER */
    /**
     * Get the popup manager.
     * 
     * @return The popup manager.
     */
    public PopupManager getPopupManager();

    /* COMMAND */
    /**
     * Get an open sheet on creation command.
     * 
     * @return An open sheet on creation command.
     */
    public OpenSheetOnCreationCommand getOpenSheetOnCreationCommand();

    /**
     * Get an initialize sheet command.
     * 
     * @return An initialize sheet command.
     */
    public InitializeSheetCommand getInitializeSheetCommand();

    /**
     * Get a duplicate sheet command.
     * 
     * @return A duplicate sheet command.
     */
    public DuplicateSheetCommand getDuplicateSheetCommand();

    /**
     * Get an add sheet link command.
     * 
     * @return An add sheet link command.
     */
    public AddSheetLinkCommand getAddSheetLinkCommand();

    /**
     * Get a delete sheet link command.
     * 
     * @return A delete sheet link command.
     */
    public DeleteSheetLinkCommand getDeleteSheetLinkCommand();

    /**
     * Get a change sheet state command.
     * 
     * @return A change sheet state command.
     */
    public ChangeSheetStateCommand getChangeSheetStateCommand();

    /**
     * Get an execute extended action command.
     * 
     * @return The execute extended action command.
     */
    public ExecuteExtendedActionCommand getExecuteExtendedActionCommand();

    /**
     * Get an open product on creation command.
     * 
     * @return An open product on creation command.
     */
    public OpenProductOnCreationCommand getOpenProductOnCreationCommand();

    /**
     * Get an add product link command.
     * 
     * @return An add product link command.
     */
    public AddProductLinkCommand getAddProductLinkCommand();

    /**
     * Get a delete product link command.
     * 
     * @return A delete product link command.
     */
    public DeleteProductLinkCommand getDeleteProductLinkCommand();

    /**
     * Get the logout command.
     * 
     * @return The logout command.
     */
    public LogoutCommand getLogoutCommand();
    
    /**
     * Get the close opend sheets command
     * 
     * @return
     */
    public CloseSheetsCommand getCloseSheetsCommand();
}
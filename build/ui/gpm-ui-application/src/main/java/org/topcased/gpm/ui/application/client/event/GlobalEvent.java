/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.event;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.connection.LaunchSheetAction;
import org.topcased.gpm.ui.application.client.command.connection.LaunchWorkspaceAction;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AddCriterionAction;
import org.topcased.gpm.ui.application.shared.command.CloseContainerResult;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesResult;
import org.topcased.gpm.ui.application.shared.command.attribute.UpdateAttributesResult;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutResult;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.CreateEnvironmentResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoriesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoriesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateDictionaryCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateEnvironmentCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.export.AbstractExportResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportFileResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.AbstractExecuteExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetUsableFieldsResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.PreSaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectSortingFieldResult;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksResult;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoResult;
import org.topcased.gpm.ui.application.shared.command.mail.SendMailResult;
import org.topcased.gpm.ui.application.shared.command.product.DeleteProductResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.application.shared.command.product.ImportProductResult;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.user.CreateUserResult;
import org.topcased.gpm.ui.application.shared.command.user.DeleteUserResult;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationResult;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileResult;
import org.topcased.gpm.ui.application.shared.command.user.GetUserResult;

import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The global action event types.
 * 
 * @author tpanuel
 * @param <R>
 *            The type of result.
 */
public final class GlobalEvent<R extends Result> {

    /**
     * Launch a workspace (product's connection)
     */
    public static final GlobalEvent<LaunchWorkspaceAction> LAUNCH_WORKSPACE =
            new GlobalEvent<LaunchWorkspaceAction>();

    /**
     * Launch a sheet event
     */
    public static final GlobalEvent<LaunchSheetAction> LAUNCH_SHEET =
            new GlobalEvent<LaunchSheetAction>();

    /**
     * Initialize the administration space.
     */
    public static final GlobalEvent<OpenAdminModuleResult> INIT_ADMIN_SPACE =
            new GlobalEvent<OpenAdminModuleResult>();

    /**
     * Open the administration space.
     */
    @SuppressWarnings("rawtypes")
	public static final GlobalEvent<EmptyAction> OPEN_ADMIN_SPACE =
            new GlobalEvent<EmptyAction>();

    /**
     * Open the user space.
     */
    @SuppressWarnings("rawtypes")
	public static final GlobalEvent<EmptyAction> OPEN_USER_SPACE =
            new GlobalEvent<EmptyAction>();

    /**
     * Load a sheet : the product name must be return to be displayed on the
     * good product workspace.
     */
    public final static GlobalEvent<GetSheetResult> LOAD_SHEET =
            new GlobalEvent<GetSheetResult>();

    /**
     * Load a product for be displayed on the admin space.
     */
    public final static GlobalEvent<GetProductResult> LOAD_PRODUCT =
            new GlobalEvent<GetProductResult>();

    /**
     * Close a product.
     */
    public final static GlobalEvent<CloseTabAction> CLOSE_PRODUCT =
            new GlobalEvent<CloseTabAction>();

    /**
     * Connection : can open process selection popup, product selection popup or
     * a product workspace.
     */
    public final static GlobalEvent<AbstractConnectionResult> CONNECTION =
            new GlobalEvent<AbstractConnectionResult>();

    /**
     * Logout : Logout to the application.
     */
    public final static GlobalEvent<LogoutResult> LOGOUT =
            new GlobalEvent<LogoutResult>();

    /**
     * Launch an export for a sheet.
     */
    public final static GlobalEvent<AbstractExportResult> EXPORT_SHEET =
            new GlobalEvent<AbstractExportResult>();

    /**
     * Launch an export for a list of sheets.
     */
    public final static GlobalEvent<AbstractExportResult> EXPORT_SHEETS =
            new GlobalEvent<AbstractExportResult>();

    /**
     * Launch an export for a filter result.
     */
    public final static GlobalEvent<ExportFileResult> EXPORT_FILTER =
            new GlobalEvent<ExportFileResult>();

    /**
     * Launch an export for a product.
     */
    public final static GlobalEvent<ExportFileResult> EXPORT_PRODUCT =
            new GlobalEvent<ExportFileResult>();

    /**
     * Launch an export for a list of products.
     */
    public final static GlobalEvent<ExportFileResult> EXPORT_PRODUCTS =
            new GlobalEvent<ExportFileResult>();

    /**
     * Open mail popup for a sheet.
     */
    public final static GlobalEvent<GetMailingInfoResult> MAIL_SHEET =
            new GlobalEvent<GetMailingInfoResult>();

    /**
     * Open mail popup for a list of sheets.
     */
    public final static GlobalEvent<GetMailingInfoResult> MAIL_SHEETS =
            new GlobalEvent<GetMailingInfoResult>();

    /**
     * Send a mail.
     */
    public final static GlobalEvent<SendMailResult> SEND_MAIL =
            new GlobalEvent<SendMailResult>();

    /**
     * Open filter result popup for link a sheet.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> LINK_SHEET =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Open filter result popup for unlink a sheet.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> UNLINK_SHEET =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Open filter result popup for link a product.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> LINK_PRODUCT =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Open filter result popup for unlink a product.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> UNLINK_PRODUCT =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Open filter result popup for initialize a sheet.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> INIT_SHEET =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Execute an extended action.
     */
    public final static GlobalEvent<AbstractExecuteExtendedActionResult> EXECUTE_EXTENDED_ACTION =
            new GlobalEvent<AbstractExecuteExtendedActionResult>();

    /**
     * Close a product.
     */
    public final static GlobalEvent<DisconnectProductResult> CLOSE_PRODUCT_WORKSPACE =
            new GlobalEvent<DisconnectProductResult>();

    /**
     * Close the select process popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_SELECT_PROCESS_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the select product popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_SELECT_PRODUCT_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the filter popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_FILTER_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the input data popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_INPUT_DATA_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the select report model popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_REPORT_MODEL_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the exported fields selection popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_EXPORTED_FIELDS_SELECTION_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the send mail popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_SEND_MAIL_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Open the CSV popup.
     */
    @SuppressWarnings("rawtypes")
	public final static GlobalEvent<EmptyAction> OPEN_CSV_POPUP =
            new GlobalEvent<EmptyAction>();

    /**
     * Close the CSV popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_CSV_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Open the popup to select the environment of a new product.
     */
    public static final GlobalEvent<SelectEnvironmentsResult> OPEN_SELECT_ENV_POPUP =
            new GlobalEvent<SelectEnvironmentsResult>();

    /**
     * Close the select environments popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_SELECT_ENV_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Create a new filter.
     */
    public final static GlobalEvent<SelectContainerResult> NEW_SHEET_FILTER =
            new GlobalEvent<SelectContainerResult>();

    /**
     * Create a new filter.
     */
    public final static GlobalEvent<SelectContainerResult> NEW_PRODUCT_FILTER =
            new GlobalEvent<SelectContainerResult>();

    /**
     * Open or close a workspace panel (navigation ,listing or detail) of the
     * product administration.
     */
    public final static GlobalEvent<OpenCloseWorkspacePanelAction> OPEN_CLOSE_PRODUCT_WORKSPACE =
            new GlobalEvent<OpenCloseWorkspacePanelAction>();

    /**
     * Execute a product table filter.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> EXECUTE_PRODUCT_TABLE_FILTER =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Execute a product tree filter.
     */
    public final static GlobalEvent<AbstractCommandFilterResult> EXECUTE_PRODUCT_TREE_FILTER =
            new GlobalEvent<AbstractCommandFilterResult>();

    /**
     * Delete a product filter.
     */
    public final static GlobalEvent<DeleteFilterResult> DELETE_PRODUCT_FILTER =
            new GlobalEvent<DeleteFilterResult>();

    /**
     * Remove the executed tree filter of the admin space.
     */
    @SuppressWarnings("rawtypes")
	public final static GlobalEvent<EmptyAction> REMOVE_PRODUCT_TREE_FILTER =
            new GlobalEvent<EmptyAction>();

    /**
     * edit sheet filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_SHEET_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Create a new filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_PRODUCT_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Update a sheet link creation filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_SHEET_LINK_CREATION_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Update a sheet link deletion filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_SHEET_LINK_DELETION_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Update a product link creation filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_PRODUCT_LINK_CREATION_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Update a product link deletion filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_PRODUCT_LINK_DELETION_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Update a sheet initialization filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> EDIT_SHEET_INITIALIZATION_FILTER =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Get searcheable containers for a new filter.
     */
    public final static GlobalEvent<SelectContainerResult> NEW_FILTER_SELECT_CONTAINER =
            new GlobalEvent<SelectContainerResult>();

    /**
     * Get product scope for a new filter.
     */
    public final static GlobalEvent<SelectScopeResult> NEW_FILTER_SELECT_PRODUCT =
            new GlobalEvent<SelectScopeResult>();

    /**
     * Get result selection for a new filter.
     */
    public final static GlobalEvent<SelectResultFieldResult> NEW_FILTER_SELECT_RESULT =
            new GlobalEvent<SelectResultFieldResult>();

    /**
     * Get result selection for a new filter.
     */
    public final static GlobalEvent<GetUsableFieldsResult> NEW_FILTER_SELECT_USABLE_FIELD =
            new GlobalEvent<GetUsableFieldsResult>();

    /**
     * Get criteria selection for a new filter.
     */
    public final static GlobalEvent<SelectCriteriaFieldResult> NEW_FILTER_SELECT_CRITERIA =
            new GlobalEvent<SelectCriteriaFieldResult>();

    /**
     * Get result selection for a new filter.
     */
    public final static GlobalEvent<AddCriterionAction> NEW_FILTER_ADD_CRITERION =
            new GlobalEvent<AddCriterionAction>();

    /**
     * Get category values for a filter.
     */
    public final static GlobalEvent<GetCategoryValuesResult> NEW_FILTER_GET_CATEGORY_VALUES =
            new GlobalEvent<GetCategoryValuesResult>();

    /**
     * Get result selection for a new filter.
     */
    public final static GlobalEvent<SelectSortingFieldResult> NEW_FILTER_SELECT_SORTING =
            new GlobalEvent<SelectSortingFieldResult>();

    /**
     * Get pre save popup for a new filter.
     */
    public final static GlobalEvent<PreSaveFilterResult> NEW_FILTER_PRESAVE =
            new GlobalEvent<PreSaveFilterResult>();

    /**
     * Save a filter.
     */
    public final static GlobalEvent<SaveFilterResult> NEW_FILTER_SAVE =
            new GlobalEvent<SaveFilterResult>();

    /**
     * Delete a product.
     */
    public final static GlobalEvent<DeleteProductResult> DELETE_PRODUCT =
            new GlobalEvent<DeleteProductResult>();

    /**
     * Load a new user
     */
    @SuppressWarnings("rawtypes")
	public final static GlobalEvent<EmptyAction> LOAD_NEW_USER =
            new GlobalEvent<EmptyAction>();

    /**
     * Load a user
     */
    public final static GlobalEvent<GetUserResult> LOAD_USER =
            new GlobalEvent<GetUserResult>();

    /**
     * Open user profile edition popup
     */
    public static final GlobalEvent<GetUserProfileResult> OPEN_USER_PROFILE_POPUP =
            new GlobalEvent<GetUserProfileResult>();

    /**
     * Save the user profile
     */
    public static final GlobalEvent<GetUserProfileResult> SAVE_USER_PROFILE =
            new GlobalEvent<GetUserProfileResult>();

    /**
     * Close the popup to edit user profile.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_USER_PROFILE_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Create a user
     */
    public final static GlobalEvent<CreateUserResult> CREATE_USER =
            new GlobalEvent<CreateUserResult>();

    /**
     * Delete a user.
     */
    public final static GlobalEvent<DeleteUserResult> DELETE_USER =
            new GlobalEvent<DeleteUserResult>();

    /**
     * Open the popup to edit user affectation.
     */
    public static final GlobalEvent<GetAffectationResult> OPEN_USER_AFFECTATION_POPUP =
            new GlobalEvent<GetAffectationResult>();

    /**
     * Close the popup to edit user affectation.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_USER_AFFECTATION_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Load the dictionary
     */
    public final static GlobalEvent<GetDictionaryCategoriesResult> LOAD_DICTIONARY =
            new GlobalEvent<GetDictionaryCategoriesResult>();

    /**
     * Load a dictionary category
     */
    public final static GlobalEvent<GetDictionaryCategoryValuesResult> LOAD_DICTIONARY_CATEGORY =
            new GlobalEvent<GetDictionaryCategoryValuesResult>();

    /**
     * Update a dictionary category
     */
    public final static GlobalEvent<UpdateDictionaryCategoryValuesResult> SAVE_DICTIONARY_CATEGORY =
            new GlobalEvent<UpdateDictionaryCategoryValuesResult>();

    /**
     * Load a environment
     */
    public final static GlobalEvent<GetEnvironmentCategoriesResult> LOAD_ENVIRONMENT =
            new GlobalEvent<GetEnvironmentCategoriesResult>();

    /**
     * Load a environment category
     */
    public final static GlobalEvent<GetEnvironmentCategoryValuesResult> LOAD_ENVIRONMENT_CATEGORY =
            new GlobalEvent<GetEnvironmentCategoryValuesResult>();

    /**
     * Update a environment category
     */
    public final static GlobalEvent<UpdateEnvironmentCategoryValuesResult> SAVE_ENVIRONMENT_CATEGORY =
            new GlobalEvent<UpdateEnvironmentCategoryValuesResult>();

    /**
     * Hide the category view
     */
    @SuppressWarnings("rawtypes")
	public final static GlobalEvent<EmptyAction> HIDE_CATEGORY =
            new GlobalEvent<EmptyAction>();

    /**
     * Load a new environment
     */
    @SuppressWarnings("rawtypes")
	public final static GlobalEvent<EmptyAction> LOAD_NEW_ENVIRONMENT =
            new GlobalEvent<EmptyAction>();

    /**
     * Create a environment
     */
    public final static GlobalEvent<CreateEnvironmentResult> CREATE_ENVIRONMENT =
            new GlobalEvent<CreateEnvironmentResult>();

    /**
     * Open the popup to import products.
     */
    @SuppressWarnings("rawtypes")
	public static final GlobalEvent<EmptyAction> OPEN_IMPORT_PRODUCTS_POPUP =
            new GlobalEvent<EmptyAction>();

    /**
     * Close the popup to import products.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_IMPORT_PRODUCTS_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Launch products import.
     */
    public final static GlobalEvent<ImportProductResult> LAUNCH_IMPORT =
            new GlobalEvent<ImportProductResult>();

    /**
     * Load attributes
     */
    public final static GlobalEvent<GetAttributesResult> LOAD_ATTRIBUTES =
            new GlobalEvent<GetAttributesResult>();

    /**
     * Update attributes
     */
    public final static GlobalEvent<UpdateAttributesResult> SAVE_ATTRIBUTES =
            new GlobalEvent<UpdateAttributesResult>();

    /**
     * Close the attributes popup.
     */
    public final static GlobalEvent<ClosePopupAction> CLOSE_ATTRIBUTES_POPUP =
            new GlobalEvent<ClosePopupAction>();

    /**
     * Close the container.
     */
    public final static GlobalEvent<CloseContainerResult> CLOSE_CONTAINER =
            new GlobalEvent<CloseContainerResult>();

    /**
     * Load product links
     */
    public final static GlobalEvent<GetLinksResult> LOAD_PRODUCT_LINKS =
            new GlobalEvent<GetLinksResult>();

    private final Type<ActionEventHandler<R>> type;

    private GlobalEvent() {
        type = new Type<ActionEventHandler<R>>();
    }

    /**
     * Get the global type.
     * 
     * @return The type.
     */
    public Type<ActionEventHandler<R>> getType() {
        return type;
    }
}
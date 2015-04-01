/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.config;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.topcased.gpm.ui.application.server.command.attribute.GetAttributesHandler;
import org.topcased.gpm.ui.application.server.command.attribute.UpdateAttributesHandler;
import org.topcased.gpm.ui.application.server.command.authorization.ChangeRoleHandler;
import org.topcased.gpm.ui.application.server.command.authorization.ConnectProductHandler;
import org.topcased.gpm.ui.application.server.command.authorization.DisconnectProductHandler;
import org.topcased.gpm.ui.application.server.command.authorization.GetConnectionInformationHandler;
import org.topcased.gpm.ui.application.server.command.authorization.LoginHandler;
import org.topcased.gpm.ui.application.server.command.authorization.LogoutHandler;
import org.topcased.gpm.ui.application.server.command.authorization.OpenAdminModuleHandler;
import org.topcased.gpm.ui.application.server.command.authorization.SelectProductHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.CreateEnvironmentHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.DeleteEnvironmentHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.GetDictionaryCategoriesHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.GetDictionaryCategoryValuesHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.GetEnvironmentCategoriesHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.GetEnvironmentCategoryValuesHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.UpdateDictionaryCategoryValuesHandler;
import org.topcased.gpm.ui.application.server.command.dictionary.UpdateEnvironmentCategoryValuesHandler;
import org.topcased.gpm.ui.application.server.command.export.ExportFilterResultHandler;
import org.topcased.gpm.ui.application.server.command.export.ExportProductHandler;
import org.topcased.gpm.ui.application.server.command.export.ExportSheetHandler;
import org.topcased.gpm.ui.application.server.command.extendedaction.ExecuteExtendedActionHandler;
import org.topcased.gpm.ui.application.server.command.filter.DeleteFilterHandler;
import org.topcased.gpm.ui.application.server.command.filter.ExecuteTableFilterHandler;
import org.topcased.gpm.ui.application.server.command.filter.ExecuteTreeFilterHandler;
import org.topcased.gpm.ui.application.server.command.filter.GetFilterSummariesHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.GetCategoryValuesHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.GetUsableFieldsHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.PreSaveFilterHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SaveFilterHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SelectContainerHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SelectCriteriaFieldHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SelectResultFieldHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SelectScopeHandler;
import org.topcased.gpm.ui.application.server.command.filter.edit.SelectSortingFieldHandler;
import org.topcased.gpm.ui.application.server.command.link.CreateProductLinkHandler;
import org.topcased.gpm.ui.application.server.command.link.CreateSheetLinkHandler;
import org.topcased.gpm.ui.application.server.command.link.DeleteProductLinkHandler;
import org.topcased.gpm.ui.application.server.command.link.DeleteSheetLinkHandler;
import org.topcased.gpm.ui.application.server.command.link.ExecuteLinkCreationFilterHandler;
import org.topcased.gpm.ui.application.server.command.link.ExecuteLinkDeletionFilterHandler;
import org.topcased.gpm.ui.application.server.command.link.GetLinksHandler;
import org.topcased.gpm.ui.application.server.command.mail.GetMailingInfoHandler;
import org.topcased.gpm.ui.application.server.command.mail.SendMailHandler;
import org.topcased.gpm.ui.application.server.command.product.CloseProductHandler;
import org.topcased.gpm.ui.application.server.command.product.CreateProductHandler;
import org.topcased.gpm.ui.application.server.command.product.DeleteProductHandler;
import org.topcased.gpm.ui.application.server.command.product.DeleteProductsHandler;
import org.topcased.gpm.ui.application.server.command.product.GetProductCreationHandler;
import org.topcased.gpm.ui.application.server.command.product.GetProductEditionHandler;
import org.topcased.gpm.ui.application.server.command.product.GetProductVisualizationHandler;
import org.topcased.gpm.ui.application.server.command.product.ImportProductHandler;
import org.topcased.gpm.ui.application.server.command.product.SelectEnvironmentsHandler;
import org.topcased.gpm.ui.application.server.command.product.UpdateProductHandler;
import org.topcased.gpm.ui.application.server.command.sheet.ChangeStateHandler;
import org.topcased.gpm.ui.application.server.command.sheet.CloseSheetHandler;
import org.topcased.gpm.ui.application.server.command.sheet.CreateSheetHandler;
import org.topcased.gpm.ui.application.server.command.sheet.DeleteSheetHandler;
import org.topcased.gpm.ui.application.server.command.sheet.DeleteSheetsHandler;
import org.topcased.gpm.ui.application.server.command.sheet.DuplicateSheetHandler;
import org.topcased.gpm.ui.application.server.command.sheet.ExecuteSheetInitializationFilterHandler;
import org.topcased.gpm.ui.application.server.command.sheet.GetSheetCreationHandler;
import org.topcased.gpm.ui.application.server.command.sheet.GetSheetEditionHandler;
import org.topcased.gpm.ui.application.server.command.sheet.GetSheetInitializationHandler;
import org.topcased.gpm.ui.application.server.command.sheet.GetSheetVisualizationHandler;
import org.topcased.gpm.ui.application.server.command.sheet.UpdateSheetHandler;
import org.topcased.gpm.ui.application.server.command.user.CreateUserHandler;
import org.topcased.gpm.ui.application.server.command.user.DeleteUserHandler;
import org.topcased.gpm.ui.application.server.command.user.GetAffectationHandler;
import org.topcased.gpm.ui.application.server.command.user.GetUserHandler;
import org.topcased.gpm.ui.application.server.command.user.GetUserProfileHandler;
import org.topcased.gpm.ui.application.server.command.user.UpdateAffectationHandler;
import org.topcased.gpm.ui.application.server.command.user.UpdateUserHandler;
import org.topcased.gpm.ui.application.server.command.user.UpdateUserProfileHandler;

/**
 * GpmModule
 * 
 * @author mkargbo
 */
public class GpmModule extends ActionHandlerModule {
    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.guice.ActionHandlerModule#configureHandlers()
     */
    @Override
    protected void configureHandlers() {
        // SHEET
        bindHandler(ExecuteSheetInitializationFilterHandler.class);
        bindHandler(GetSheetInitializationHandler.class);
        bindHandler(CreateSheetHandler.class);
        bindHandler(GetSheetCreationHandler.class);
        bindHandler(GetSheetVisualizationHandler.class);
        bindHandler(GetSheetEditionHandler.class);
        bindHandler(UpdateSheetHandler.class);
        bindHandler(ChangeStateHandler.class);
        bindHandler(DuplicateSheetHandler.class);
        bindHandler(DeleteSheetHandler.class);
        bindHandler(DeleteSheetsHandler.class);
        bindHandler(CloseSheetHandler.class);

        // AUTHORIZATION
        bindHandler(LoginHandler.class);
        bindHandler(ChangeRoleHandler.class);
        bindHandler(SelectProductHandler.class);
        bindHandler(ConnectProductHandler.class);
        bindHandler(GetConnectionInformationHandler.class);
        bindHandler(DisconnectProductHandler.class);
        bindHandler(LogoutHandler.class);
        bindHandler(OpenAdminModuleHandler.class);

        // FILTER
        bindHandler(GetFilterSummariesHandler.class);
        bindHandler(ExecuteTableFilterHandler.class);
        bindHandler(ExecuteTreeFilterHandler.class);
        bindHandler(DeleteFilterHandler.class);

        // FILTER EDITION
        bindHandler(GetUsableFieldsHandler.class);
        bindHandler(SelectContainerHandler.class);
        bindHandler(SelectScopeHandler.class);
        bindHandler(SelectResultFieldHandler.class);
        bindHandler(SelectCriteriaFieldHandler.class);
        bindHandler(SelectSortingFieldHandler.class);
        bindHandler(PreSaveFilterHandler.class);
        bindHandler(SaveFilterHandler.class);
        bindHandler(GetCategoryValuesHandler.class);

        // LINK
        bindHandler(ExecuteLinkCreationFilterHandler.class);
        bindHandler(CreateSheetLinkHandler.class);
        bindHandler(CreateProductLinkHandler.class);
        bindHandler(ExecuteLinkDeletionFilterHandler.class);
        bindHandler(DeleteSheetLinkHandler.class);
        bindHandler(DeleteProductLinkHandler.class);
        bindHandler(GetLinksHandler.class);

        // EXPORT
        bindHandler(ExportSheetHandler.class);
        bindHandler(ExportFilterResultHandler.class);
        bindHandler(ExportProductHandler.class);

        // MAIL
        bindHandler(GetMailingInfoHandler.class);
        bindHandler(SendMailHandler.class);

        // EXTENDED ACTION
        bindHandler(ExecuteExtendedActionHandler.class);

        // PRODUCT
        bindHandler(SelectEnvironmentsHandler.class);
        bindHandler(GetProductCreationHandler.class);
        bindHandler(GetProductVisualizationHandler.class);
        bindHandler(GetProductEditionHandler.class);
        bindHandler(CreateProductHandler.class);
        bindHandler(UpdateProductHandler.class);
        bindHandler(DeleteProductHandler.class);
        bindHandler(DeleteProductsHandler.class);
        bindHandler(ImportProductHandler.class);
        bindHandler(CloseProductHandler.class);

        // USER
        bindHandler(GetUserHandler.class);
        bindHandler(CreateUserHandler.class);
        bindHandler(DeleteUserHandler.class);
        bindHandler(GetAffectationHandler.class);
        bindHandler(UpdateAffectationHandler.class);
        bindHandler(UpdateUserHandler.class);
        bindHandler(UpdateUserProfileHandler.class);
        bindHandler(GetUserProfileHandler.class);

        // ENVIRONMENT
        bindHandler(CreateEnvironmentHandler.class);
        bindHandler(DeleteEnvironmentHandler.class);
        bindHandler(GetDictionaryCategoriesHandler.class);
        bindHandler(GetDictionaryCategoryValuesHandler.class);
        bindHandler(GetEnvironmentCategoriesHandler.class);
        bindHandler(GetEnvironmentCategoryValuesHandler.class);
        bindHandler(UpdateDictionaryCategoryValuesHandler.class);
        bindHandler(UpdateEnvironmentCategoryValuesHandler.class);

        // ATTRIBUTES
        bindHandler(GetAttributesHandler.class);
        bindHandler(UpdateAttributesHandler.class);
    }
}
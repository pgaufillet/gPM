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

import org.topcased.gpm.ui.application.client.command.admin.dictionary.CreateEnvironmentCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCategoryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentCategoryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.SortDictionaryCategoryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.UpdateDictionaryCategoryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.UpdateEnvironmentCategoryCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.AddProductFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.DeleteProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.DeleteProductTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.EditProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.EditProductTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.ExecuteProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.ExecuteProductTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.AddProductLinkCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.CreateProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.DeleteProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.DeleteProductLinkCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.DisplayProductAttributesCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.EditProductAttributesCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.ExportProductOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.ImportProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.UpdateProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.products.DeleteProductsCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.products.ExportProductsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductListingCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductListingCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductNavigationCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.CreateUserCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.DeleteUserCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.GetUserAffectationCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.UpdateUserCommand;
import org.topcased.gpm.ui.application.client.command.connection.AuthenticationCommand;
import org.topcased.gpm.ui.application.client.command.connection.ChangeRoleCommand;
import org.topcased.gpm.ui.application.client.command.connection.InitAdminSpaceCommand;
import org.topcased.gpm.ui.application.client.command.connection.OpenAdminSpaceCommand;
import org.topcased.gpm.ui.application.client.command.connection.OpenUserSpaceCommand;
import org.topcased.gpm.ui.application.client.command.connection.SelectProductCommand;
import org.topcased.gpm.ui.application.client.command.extended.ExecuteExtendedActionCommand;
import org.topcased.gpm.ui.application.client.command.popup.attribute.UpdateAttributesCommand;
import org.topcased.gpm.ui.application.client.command.popup.csv.LaunchCsvExportCommand;
import org.topcased.gpm.ui.application.client.command.popup.extended.ExecuteExtendedActionWithInputDataCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.AddSheetLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.DeleteSheetLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.InitializeSheetPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.mail.SendSheetByMailCommand;
import org.topcased.gpm.ui.application.client.command.popup.mail.SendSheetsByMailCommand;
import org.topcased.gpm.ui.application.client.command.popup.product.LaunchProductsImportCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetFieldsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetFieldsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetWithPdfModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetWithXlsModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsFieldsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsFieldsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsWithPdfModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsWithXlsModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.userprofile.UpdateUserProfileCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.AddSheetFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.DeleteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.DeleteSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.EditSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.EditSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnCsvCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.AddSheetLinkCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ChangeSheetStateCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DeleteSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DeleteSheetLinkCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DuplicateSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.InitializeSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.MailSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnCreationCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.UpdateSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.CloseSheetsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.DeleteSheetsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.MailSheetsCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MaximizeSheetListingCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetListingCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetNavigationCommand;
import org.topcased.gpm.ui.application.shared.command.sheet.CreateSheetAction;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Module with injection of all the command.
 * 
 * @author tpanuel
 */
public class GpmCommandModule extends AbstractGinModule {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.inject.client.AbstractGinModule#configure()
     */
    @Override
    protected void configure() {
        // Authentication
        bind(AuthenticationCommand.class).in(Singleton.class);
        bind(ChangeRoleCommand.class).in(Singleton.class);

        // Switch space
        bind(InitAdminSpaceCommand.class).in(Singleton.class);
        bind(OpenAdminSpaceCommand.class).in(Singleton.class);
        bind(OpenUserSpaceCommand.class).in(Singleton.class);

        // User workspace
        bind(MinimizeSheetNavigationCommand.class).in(Singleton.class);
        bind(MinimizeSheetListingCommand.class).in(Singleton.class);
        bind(MaximizeSheetListingCommand.class).in(Singleton.class);

        // Sheet
        bind(UpdateSheetCommand.class).in(Singleton.class);
        bind(CreateSheetAction.class).in(Singleton.class);
        bind(OpenSheetOnCreationCommand.class);
        bind(OpenSheetOnVisualizationCommand.class);
        bind(OpenSheetOnEditionCommand.class);
        bind(DeleteSheetCommand.class).in(Singleton.class);
        bind(AddSheetLinkCommand.class);
        bind(DeleteSheetLinkCommand.class);
        bind(ExportSheetOnPdfCommand.class).in(Singleton.class);
        bind(ExportSheetOnXmlCommand.class).in(Singleton.class);
        bind(ExportSheetOnXlsCommand.class).in(Singleton.class);
        bind(InitializeSheetCommand.class);
        bind(DuplicateSheetCommand.class).in(Singleton.class);
        bind(MailSheetCommand.class).in(Singleton.class);
        bind(ChangeSheetStateCommand.class);

        // Sheets
        bind(DeleteSheetsCommand.class).in(Singleton.class);
        bind(ExportSheetsOnPdfCommand.class).in(Singleton.class);
        bind(ExportSheetsOnXlsCommand.class).in(Singleton.class);
        bind(ExportSheetsOnXmlCommand.class).in(Singleton.class);
        bind(MailSheetsCommand.class).in(Singleton.class);
        bind(CloseSheetsCommand.class).in(Singleton.class);

        // Sheet filter
        bind(AddSheetFilterCommand.class).in(Singleton.class);
        bind(ExecuteSheetTreeFilterCommand.class).in(Singleton.class);
        bind(EditSheetTreeFilterCommand.class).in(Singleton.class);
        bind(DeleteSheetTreeFilterCommand.class).in(Singleton.class);
        bind(ExecuteSheetTableFilterCommand.class).in(Singleton.class);
        bind(EditSheetTableFilterCommand.class).in(Singleton.class);
        bind(DeleteSheetTableFilterCommand.class).in(Singleton.class);
        bind(ExportSheetTableFilterOnPdfCommand.class).in(Singleton.class);
        bind(ExportSheetTableFilterOnXmlCommand.class).in(Singleton.class);
        bind(ExportSheetTableFilterOnXlsCommand.class).in(Singleton.class);
        bind(ExportSheetTableFilterOnCsvCommand.class).in(Singleton.class);

        // Product workspace
        bind(MinimizeProductNavigationCommand.class).in(Singleton.class);
        bind(MinimizeProductListingCommand.class).in(Singleton.class);
        bind(MaximizeProductListingCommand.class).in(Singleton.class);
        bind(MinimizeProductDetailCommand.class).in(Singleton.class);
        bind(MaximizeProductDetailCommand.class).in(Singleton.class);

        // Product
        bind(OpenProductOnVisualizationCommand.class);
        bind(OpenProductOnEditionCommand.class);
        bind(OpenProductOnCreationCommand.class);
        bind(ImportProductCommand.class).in(Singleton.class);
        bind(DeleteProductCommand.class).in(Singleton.class);
        bind(ExportProductOnXmlCommand.class).in(Singleton.class);
        bind(CreateProductCommand.class).in(Singleton.class);
        bind(UpdateProductCommand.class).in(Singleton.class);
        bind(AddProductLinkCommand.class);
        bind(DeleteProductLinkCommand.class);
        bind(DisplayProductAttributesCommand.class).in(Singleton.class);
        bind(EditProductAttributesCommand.class).in(Singleton.class);

        // Products
        bind(DeleteProductsCommand.class).in(Singleton.class);
        bind(ExportProductsOnXmlCommand.class).in(Singleton.class);

        // Product filter
        bind(AddProductFilterCommand.class).in(Singleton.class);
        bind(ExecuteProductTreeFilterCommand.class).in(Singleton.class);
        bind(ExecuteProductTableFilterCommand.class).in(Singleton.class);
        bind(EditProductTreeFilterCommand.class).in(Singleton.class);
        bind(EditProductTableFilterCommand.class).in(Singleton.class);
        bind(DeleteProductTreeFilterCommand.class).in(Singleton.class);
        bind(DeleteProductTableFilterCommand.class).in(Singleton.class);

        // Connection
        bind(SelectProductCommand.class).in(Singleton.class);

        // Extended action
        bind(ExecuteExtendedActionCommand.class);

        // Filter popup
        bind(AddSheetLinkPopupCommand.class).in(Singleton.class);
        bind(DeleteSheetLinkPopupCommand.class).in(Singleton.class);
        bind(InitializeSheetPopupCommand.class).in(Singleton.class);

        // Report model
        bind(ExportSheetFieldsOnXlsCommand.class).in(Singleton.class);
        bind(ExportSheetFieldsOnXmlCommand.class).in(Singleton.class);
        bind(ExportSheetsFieldsOnXlsCommand.class).in(Singleton.class);
        bind(ExportSheetsFieldsOnXmlCommand.class).in(Singleton.class);
        bind(ExportSheetWithPdfModelCommand.class).in(Singleton.class);
        bind(ExportSheetWithXlsModelCommand.class).in(Singleton.class);
        bind(ExportSheetsWithPdfModelCommand.class).in(Singleton.class);
        bind(ExportSheetsWithXlsModelCommand.class).in(Singleton.class);

        // Extended action with input data
        bind(ExecuteExtendedActionWithInputDataCommand.class).in(
                Singleton.class);

        // Send mail
        bind(SendSheetByMailCommand.class).in(Singleton.class);
        bind(SendSheetsByMailCommand.class).in(Singleton.class);

        // CSV export
        bind(LaunchCsvExportCommand.class).in(Singleton.class);

        // Products import
        bind(LaunchProductsImportCommand.class).in(Singleton.class);

        // User
        bind(OpenUserOnCreationCommand.class).in(Singleton.class);
        bind(CreateUserCommand.class).in(Singleton.class);
        bind(OpenUserOnEditionCommand.class).in(Singleton.class);
        bind(UpdateUserCommand.class).in(Singleton.class);
        bind(GetUserAffectationCommand.class).in(Singleton.class);
        bind(DeleteUserCommand.class).in(Singleton.class);
        bind(UpdateUserProfileCommand.class).in(Singleton.class);

        // Dictionary       
        bind(OpenDictionaryCommand.class).in(Singleton.class);
        bind(OpenDictionaryCategoryCommand.class).in(Singleton.class);
        bind(SortDictionaryCategoryCommand.class).in(Singleton.class);
        bind(UpdateDictionaryCategoryCommand.class).in(Singleton.class);
        bind(OpenEnvironmentOnCreationCommand.class).in(Singleton.class);
        bind(CreateEnvironmentCommand.class).in(Singleton.class);
        bind(OpenEnvironmentOnEditionCommand.class).in(Singleton.class);
        bind(OpenEnvironmentCategoryCommand.class).in(Singleton.class);
        bind(UpdateEnvironmentCategoryCommand.class).in(Singleton.class);

        // Attributes
        bind(UpdateAttributesCommand.class).in(Singleton.class);

    }
}
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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.gin.AbstractPresenterModule;

import org.topcased.gpm.ui.application.client.admin.AdminDisplay;
import org.topcased.gpm.ui.application.client.admin.AdminPresenter;
import org.topcased.gpm.ui.application.client.admin.AdminView;
import org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminView;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailView;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailView;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentDetailView;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.DictionaryListingDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.DictionaryListingPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.DictionaryListingView;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingView;
import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay;
import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationView;
import org.topcased.gpm.ui.application.client.admin.product.ProductAdminDisplay;
import org.topcased.gpm.ui.application.client.admin.product.ProductAdminPresenter;
import org.topcased.gpm.ui.application.client.admin.product.ProductAdminView;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductCreationPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailView;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductEditionPresenter;
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductVisualizationPresenter;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingDisplay;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingPresenter;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingView;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationDisplay;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationPresenter;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationView;
import org.topcased.gpm.ui.application.client.admin.user.UserAdminDisplay;
import org.topcased.gpm.ui.application.client.admin.user.UserAdminPresenter;
import org.topcased.gpm.ui.application.client.admin.user.UserAdminView;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailView;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailView;
import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay;
import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingPresenter;
import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingView;
import org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay;
import org.topcased.gpm.ui.application.client.common.container.product.ProductView;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetView;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailView;
import org.topcased.gpm.ui.application.client.event.ActionEventBus;
import org.topcased.gpm.ui.application.client.main.MainDisplay;
import org.topcased.gpm.ui.application.client.main.MainPresenter;
import org.topcased.gpm.ui.application.client.main.MainView;
import org.topcased.gpm.ui.application.client.popup.PopupManager;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionDisplay;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionPresenter;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionView;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesVisualizationDisplay;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesVisualizationPresenter;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesVisualizationView;
import org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay;
import org.topcased.gpm.ui.application.client.popup.connection.LoginPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.LoginView;
import org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionDisplay;
import org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionView;
import org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionDisplay;
import org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionView;
import org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay;
import org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionView;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataDisplay;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataPresenter;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataView;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupDisplay;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupView;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupView;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupView;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupDisplay;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupView;
import org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay;
import org.topcased.gpm.ui.application.client.popup.mail.SendMailPresenter;
import org.topcased.gpm.ui.application.client.popup.mail.SendMailView;
import org.topcased.gpm.ui.application.client.popup.message.ConfirmationMessageDisplay;
import org.topcased.gpm.ui.application.client.popup.message.ConfirmationMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.ConfirmationMessageView;
import org.topcased.gpm.ui.application.client.popup.message.ErrorMessageDisplay;
import org.topcased.gpm.ui.application.client.popup.message.ErrorMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.ErrorMessageView;
import org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessageDisplay;
import org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessageView;
import org.topcased.gpm.ui.application.client.popup.message.InfoMessageDisplay;
import org.topcased.gpm.ui.application.client.popup.message.InfoMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.InfoMessageView;
import org.topcased.gpm.ui.application.client.popup.message.UploadMessageDisplay;
import org.topcased.gpm.ui.application.client.popup.message.UploadMessagePresenter;
import org.topcased.gpm.ui.application.client.popup.message.UploadMessageView;
import org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay;
import org.topcased.gpm.ui.application.client.popup.product.ImportProductsPresenter;
import org.topcased.gpm.ui.application.client.popup.product.ImportProductsView;
import org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsDisplay;
import org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsPresenter;
import org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsView;
import org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsDisplay;
import org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsPresenter;
import org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsView;
import org.topcased.gpm.ui.application.client.popup.report.SelectReportDisplay;
import org.topcased.gpm.ui.application.client.popup.report.SelectReportPresenter;
import org.topcased.gpm.ui.application.client.popup.report.SelectReportView;
import org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay;
import org.topcased.gpm.ui.application.client.popup.user.UserAffectationPresenter;
import org.topcased.gpm.ui.application.client.popup.user.UserAffectationView;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfilePresenter;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileView;
import org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.application.client.user.ProductWorkspaceView;
import org.topcased.gpm.ui.application.client.user.UserSpaceDisplay;
import org.topcased.gpm.ui.application.client.user.UserSpacePresenter;
import org.topcased.gpm.ui.application.client.user.UserSpaceView;
import org.topcased.gpm.ui.application.client.user.detail.SheetCreationPresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailDisplay;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailPresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailView;
import org.topcased.gpm.ui.application.client.user.detail.SheetEditionPresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetVisualizationPresenter;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingPresenter;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingView;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationPresenter;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationView;

import com.google.inject.Singleton;

/**
 * Module with injection of all the presenter.
 * 
 * @author tpanuel
 */
public class GpmPresenterModule extends AbstractPresenterModule {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.inject.client.AbstractGinModule#configure()
     */
    @Override
    protected void configure() {
        // Bind dispatch
        bind(DispatchAsync.class).to(GpmDispatchAsync.class).in(Singleton.class);

        // Bind event bus
        bind(EventBus.class).to(ActionEventBus.class).in(Singleton.class);

        // Bind main
        bind(MainPresenter.class).in(Singleton.class);
        bindDisplay(MainDisplay.class, MainView.class);

        // Bind authentication
        bind(LoginPresenter.class).in(Singleton.class);
        bindDisplay(LoginDisplay.class, LoginView.class);

        // Bind user space
        bind(UserSpacePresenter.class).in(Singleton.class);
        bindDisplay(UserSpaceDisplay.class, UserSpaceView.class);

        // Bind product workspace
        bind(ProductWorkspacePresenter.class);
        bindDisplay(ProductWorkspaceDisplay.class, ProductWorkspaceView.class);

        // Bind sheet navigation
        bind(SheetNavigationPresenter.class);
        bindDisplay(SheetNavigationDisplay.class, SheetNavigationView.class);

        // Bind sheet listing
        bind(SheetListingPresenter.class);
        bindDisplay(SheetListingDisplay.class, SheetListingView.class);

        // Bind sheet detail
        bind(SheetDetailPresenter.class);
        bindDisplay(SheetDetailDisplay.class, SheetDetailView.class);

        // Bind sheet detail
        bind(SheetCreationPresenter.class);
        bind(SheetVisualizationPresenter.class);
        bind(SheetEditionPresenter.class);
        bindDisplay(SheetDisplay.class, SheetView.class);

        // Bind administration
        bind(AdminPresenter.class).in(Singleton.class);
        bindDisplay(AdminDisplay.class, AdminView.class);

        // Bind product administration
        bind(ProductAdminPresenter.class).in(Singleton.class);
        bindDisplay(ProductAdminDisplay.class, ProductAdminView.class);

        // Bind product navigation administration
        bind(ProductNavigationPresenter.class).in(Singleton.class);
        bindDisplay(ProductNavigationDisplay.class, ProductNavigationView.class);

        // Bind product listing administration
        bind(ProductListingPresenter.class).in(Singleton.class);
        bindDisplay(ProductListingDisplay.class, ProductListingView.class);

        // Bind product detail administration
        bind(ProductDetailPresenter.class).in(Singleton.class);
        bindDisplay(ProductDetailDisplay.class, ProductDetailView.class);

        // Bind sheet detail
        bind(ProductCreationPresenter.class);
        bind(ProductVisualizationPresenter.class);
        bind(ProductEditionPresenter.class);
        bindDisplay(ProductDisplay.class, ProductView.class);

        // Bind user administration
        bind(UserAdminPresenter.class).in(Singleton.class);
        bindDisplay(UserAdminDisplay.class, UserAdminView.class);

        // Bind user listing administration
        bind(UserListingPresenter.class).in(Singleton.class);
        bindDisplay(UserListingDisplay.class, UserListingView.class);

        // Bind user edition detail administration
        bind(UserEditionDetailPresenter.class).in(Singleton.class);
        bindDisplay(UserEditionDetailDisplay.class, UserEditionDetailView.class);

        // Bind user creation detail administration
        bind(UserCreationDetailPresenter.class).in(Singleton.class);
        bindDisplay(UserCreationDetailDisplay.class,
                UserCreationDetailView.class);

        // Bind user listing administration
        bind(EmptyDetailPresenter.class);
        bindDisplay(EmptyDetailDisplay.class, EmptyDetailView.class);

        // Bind user profile edition
        bind(UserProfilePresenter.class).in(Singleton.class);
        bindDisplay(UserProfileDisplay.class, UserProfileView.class);

        // Bind dictionary administration
        bind(DictionaryAdminPresenter.class).in(Singleton.class);
        bindDisplay(DictionaryAdminDisplay.class, DictionaryAdminView.class);

        // Bind navigation dictionary administration
        bind(DictionaryNavigationPresenter.class).in(Singleton.class);
        bindDisplay(DictionaryNavigationDisplay.class,
                DictionaryNavigationView.class);

        // Bind listing dictionary administration
        bind(DictionaryListingPresenter.class).in(Singleton.class);
        bindDisplay(DictionaryListingDisplay.class, DictionaryListingView.class);

        // Bind listing environment administration
        bind(EnvironmentListingPresenter.class).in(Singleton.class);
        bindDisplay(EnvironmentListingDisplay.class,
                EnvironmentListingView.class);

        // Bind detail dictionary administration
        bind(DictionaryDetailPresenter.class).in(Singleton.class);
        bindDisplay(DictionaryDetailDisplay.class, DictionaryDetailView.class);

        // Bind detail environment administration
        bind(EnvironmentDetailPresenter.class).in(Singleton.class);
        bindDisplay(EnvironmentDetailDisplay.class, EnvironmentDetailView.class);

        // Bind detail environment creation administration
        bind(EnvironmentCreationDetailPresenter.class).in(Singleton.class);
        bindDisplay(EnvironmentCreationDetailDisplay.class,
                EnvironmentCreationDetailView.class);

        // Bind popup manager
        bind(PopupManager.class).in(Singleton.class);

        // Bind product selection popup
        bind(ProcessSelectionPresenter.class).in(Singleton.class);
        bindDisplay(ProcessSelectionDisplay.class, ProcessSelectionView.class);

        // Bind product selection popup
        bind(ProductSelectionPresenter.class).in(Singleton.class);
        bindDisplay(ProductSelectionDisplay.class, ProductSelectionView.class);

        // Bind filter popup
        bind(FilterPopupPresenter.class).in(Singleton.class);
        bindDisplay(FilterPopupDisplay.class, FilterPopupView.class);

        // Bind filter edition popup
        bind(FilterEditionPopupPresenter.class).in(Singleton.class);
        bindDisplay(FilterEditionPopupDisplay.class,
                FilterEditionPopupView.class);

        // Bind filter edition save popup
        bind(FilterEditionSavePopupPresenter.class).in(Singleton.class);
        bindDisplay(FilterEditionSavePopupDisplay.class,
                FilterEditionSavePopupView.class);

        // Bind filter edition select criteria group popup
        bind(FilterEditionSelectCriteriaGroupPopupPresenter.class).in(
                Singleton.class);
        bindDisplay(FilterEditionSelectCriteriaGroupPopupDisplay.class,
                FilterEditionSelectCriteriaGroupPopupView.class);

        // Bind input data popup
        bind(InputDataPresenter.class).in(Singleton.class);
        bindDisplay(InputDataDisplay.class, InputDataView.class);

        // Bind report selection popup
        bind(SelectReportPresenter.class).in(Singleton.class);
        bindDisplay(SelectReportDisplay.class, SelectReportView.class);

        // Bind exported fields selection popup
        bind(SelectExportedFieldsPresenter.class).in(Singleton.class);
        bindDisplay(SelectExportedFieldsDisplay.class,
                SelectExportedFieldsView.class);

        // Bind send mail popup
        bind(SendMailPresenter.class).in(Singleton.class);
        bindDisplay(SendMailDisplay.class, SendMailView.class);

        // Bind CSV option selection popup
        bind(CsvOptionSelectionPresenter.class).in(Singleton.class);
        bindDisplay(CsvOptionSelectionDisplay.class,
                CsvOptionSelectionView.class);

        // Bind select environments popup
        bind(SelectEnvironmentsPresenter.class).in(Singleton.class);
        bindDisplay(SelectEnvironmentsDisplay.class,
                SelectEnvironmentsView.class);

        // Bind edit user affectation popup
        bind(UserAffectationPresenter.class).in(Singleton.class);
        bindDisplay(UserAffectationDisplay.class, UserAffectationView.class);

        // Bind import products popup
        bind(ImportProductsPresenter.class).in(Singleton.class);
        bindDisplay(ImportProductsDisplay.class, ImportProductsView.class);

        // Bind error message
        bind(ErrorMessagePresenter.class);
        bindDisplay(ErrorMessageDisplay.class, ErrorMessageView.class);

        // Bind error session message
        bind(ErrorSessionMessagePresenter.class);
        bindDisplay(ErrorSessionMessageDisplay.class,
                ErrorSessionMessageView.class);

        // Bind info message
        bind(InfoMessagePresenter.class);
        bindDisplay(InfoMessageDisplay.class, InfoMessageView.class);

        // Bind confirmation message
        bind(ConfirmationMessagePresenter.class);
        bindDisplay(ConfirmationMessageDisplay.class,
                ConfirmationMessageView.class);

        // Bind upload message
        bind(UploadMessagePresenter.class).in(Singleton.class);
        bindDisplay(UploadMessageDisplay.class, UploadMessageView.class);

        // Bind attribute message
        bind(AttributesVisualizationPresenter.class).in(Singleton.class);
        bindDisplay(AttributesVisualizationDisplay.class,
                AttributesVisualizationView.class);

        // Bind attribute message
        bind(AttributesEditionPresenter.class).in(Singleton.class);
        bindDisplay(AttributesEditionDisplay.class, AttributesEditionView.class);
    }
}
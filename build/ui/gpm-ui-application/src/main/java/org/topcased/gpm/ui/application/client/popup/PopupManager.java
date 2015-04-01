/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AbstractFilterEditionExecuteCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductFilterCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductLinkFilterCreationCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductLinkFilterDeletionCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteSheetFilterCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteSheetFilterInitializationCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteSheetLinkFilterCreationCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteSheetLinkFilterDeletionCommand;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.main.MainDisplay;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionPresenter;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesVisualizationPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.LoginPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionPresenter;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupType;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupPresenter;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionStep;
import org.topcased.gpm.ui.application.client.popup.mail.SendMailPresenter;
import org.topcased.gpm.ui.application.client.popup.product.ImportProductsPresenter;
import org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsPresenter;
import org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsPresenter;
import org.topcased.gpm.ui.application.client.popup.report.SelectReportPresenter;
import org.topcased.gpm.ui.application.client.popup.user.UserAffectationPresenter;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfilePresenter;
import org.topcased.gpm.ui.application.client.user.UserSpacePresenter;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesEditionResult;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesResult;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesVisualizationResult;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProcessResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductResult;
import org.topcased.gpm.ui.application.shared.command.export.AbstractExportResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportFileResult;
import org.topcased.gpm.ui.application.shared.command.export.SelectFieldNamesResult;
import org.topcased.gpm.ui.application.shared.command.export.SelectReportModelResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.AbstractExecuteExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetFileExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetInputDataResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.MessageExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecutePopupFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeResult;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoResult;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsResult;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationResult;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileResult;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

/**
 * Manager that listen event and open popup.
 * 
 * @author tpanuel
 */
public class PopupManager {
    private final List<HandlerRegistration> handlerRegistrations;

    private final EventBus eventBus;

    private final UserSpacePresenter userSpace;

    private final LoginPresenter login;

    private final ProductSelectionPresenter productSelection;

    private final ProcessSelectionPresenter processSelection;

    private final FilterPopupPresenter filterPopup;

    private FilterEditionPopupPresenter filterEditionPopup;

    private final FilterEditionExecuteProductFilterCommand filterEditionExecuteProductFilterCommand;

    private final FilterEditionExecuteSheetFilterCommand filterEditionExecuteSheetFilterCommand;

    private final FilterEditionExecuteSheetLinkFilterCreationCommand filterEditionExecuteSheetLinkFilterCreationCommand;

    private final FilterEditionExecuteSheetLinkFilterDeletionCommand filterEditionExecuteSheetLinkFilterDeletionCommand;

    private final FilterEditionExecuteProductLinkFilterCreationCommand filterEditionExecuteProductLinkFilterCreationCommand;

    private final FilterEditionExecuteProductLinkFilterDeletionCommand filterEditionExecuteProductLinkFilterDeletionCommand;

    private final FilterEditionExecuteSheetFilterInitializationCommand filterEditionExecuteSheetFilterInitializationCommand;

    private final InputDataPresenter inputDataPopup;

    private final SelectReportPresenter selectReportPopup;

    private final SelectExportedFieldsPresenter selectExportedFieldsPopup;

    private final SendMailPresenter sendMailPopup;

    private final CsvOptionSelectionPresenter csvPopup;

    private final SelectEnvironmentsPresenter selectEnvironmentsPopup;

    private final UserAffectationPresenter userAffectationPopup;

    private final ImportProductsPresenter importProductsPopup;

    private final AttributesEditionPresenter editAttributesPopup;

    private final AttributesVisualizationPresenter viewAttributesPopup;

    private final UserProfilePresenter userProfilePopup;

    /**
     * Create the popup manager.
     * 
     * @param pEventBus
     *            The event bus.
     * @param pUserSpace
     *            User's space
     * @param pLogin
     *            The login popup.
     * @param pProcessSelection
     *            The presenter to select process.
     * @param pProductSelection
     *            The presenter to select product.
     * @param pFilterPresenter
     *            The filter popup presenter.
     * @param pFilterEditionPresenter
     *            The filter edition popup presenter.
     * @param pInputDataPopup
     *            The input data presenter.
     * @param pSelectReportPopup
     *            The select report presenter.
     * @param pSelectedExportedFieldPopup
     *            The select exported fields presenter.
     * @param pSendMailPopup
     *            The send mail presenter.
     * @param pCsvPopup
     *            The csv popup presenter.
     * @param pSelectEnvironmentsPopup
     *            The select environments for product creation popup presenter.
     * @param pUserProfilePopup
     *            The popup of user profile edition
     * @param pImportProductsPopup
     *            The popup to import products.
     * @param pFilterEditionExecuteProductFilterCommand
     *            The command to execute a product filter in edition mode.
     * @param pFilterEditionExecuteSheetFilterCommand
     *            The command to execute a sheet filter in edition mode.
     */
    @Inject
    public PopupManager(
            final EventBus pEventBus,
            final UserSpacePresenter pUserSpace,
            final LoginPresenter pLogin,
            final ProcessSelectionPresenter pProcessSelection,
            final ProductSelectionPresenter pProductSelection,
            final FilterPopupPresenter pFilterPresenter,
            final FilterEditionPopupPresenter pFilterEditionPresenter,
            final InputDataPresenter pInputDataPopup,
            final SelectReportPresenter pSelectReportPopup,
            final SelectExportedFieldsPresenter pSelectedExportedFieldPopup,
            final SendMailPresenter pSendMailPopup,
            final CsvOptionSelectionPresenter pCsvPopup,
            final SelectEnvironmentsPresenter pSelectEnvironmentsPopup,
            final UserAffectationPresenter pUserAffectationPopup,
            final UserProfilePresenter pUserProfilePopup,
            final ImportProductsPresenter pImportProductsPopup,
            final AttributesEditionPresenter pEditAttributesPopup,
            final AttributesVisualizationPresenter pViewAttributesPopup,
            final FilterEditionExecuteProductFilterCommand pFilterEditionExecuteProductFilterCommand,
            final FilterEditionExecuteSheetFilterCommand pFilterEditionExecuteSheetFilterCommand,
            final FilterEditionExecuteSheetLinkFilterCreationCommand pFilterEditionExecuteSheetLinkFilterCreationCommand,
            final FilterEditionExecuteSheetLinkFilterDeletionCommand pFilterEditionExecuteSheetLinkFilterDeletionCommand,
            final FilterEditionExecuteProductLinkFilterCreationCommand pFilterEditionExecuteProductLinkFilterCreationCommand,
            final FilterEditionExecuteProductLinkFilterDeletionCommand pFilterEditionExecuteProductLinkFilterDeletionCommand,
            final FilterEditionExecuteSheetFilterInitializationCommand pFilterEditionExecuteSheetFilterInitializationCommand) {
        handlerRegistrations = new ArrayList<HandlerRegistration>();
        eventBus = pEventBus;
        login = pLogin;
        userSpace = pUserSpace;
        processSelection = pProcessSelection;
        productSelection = pProductSelection;
        filterPopup = pFilterPresenter;
        filterEditionPopup = pFilterEditionPresenter;
        inputDataPopup = pInputDataPopup;
        selectReportPopup = pSelectReportPopup;
        selectExportedFieldsPopup = pSelectedExportedFieldPopup;
        sendMailPopup = pSendMailPopup;
        csvPopup = pCsvPopup;
        selectEnvironmentsPopup = pSelectEnvironmentsPopup;
        userAffectationPopup = pUserAffectationPopup;
        userProfilePopup = pUserProfilePopup;
        importProductsPopup = pImportProductsPopup;
        editAttributesPopup = pEditAttributesPopup;
        viewAttributesPopup = pViewAttributesPopup;

        filterEditionExecuteProductFilterCommand =
                pFilterEditionExecuteProductFilterCommand;
        filterEditionExecuteSheetFilterCommand =
                pFilterEditionExecuteSheetFilterCommand;
        filterEditionExecuteSheetLinkFilterCreationCommand =
                pFilterEditionExecuteSheetLinkFilterCreationCommand;
        filterEditionExecuteSheetLinkFilterDeletionCommand =
                pFilterEditionExecuteSheetLinkFilterDeletionCommand;
        filterEditionExecuteProductLinkFilterCreationCommand =
                pFilterEditionExecuteProductLinkFilterCreationCommand;
        filterEditionExecuteProductLinkFilterDeletionCommand =
                pFilterEditionExecuteProductLinkFilterDeletionCommand;
        filterEditionExecuteSheetFilterInitializationCommand =
                pFilterEditionExecuteSheetFilterInitializationCommand;
    }

    /**
     * Start the popup manager.
     */
    @SuppressWarnings("rawtypes")
	public void start() {
        handleConnection();
        handleExport();
        handleMailSheet();
        handleFilterEdition();
        addEventHandler(GlobalEvent.LINK_SHEET.getType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        // Open link popup
                        if (pResult instanceof ExecutePopupFilterResult) {
                            filterPopup.setPopupFilterType(FilterPopupType.LINK_SHEET);
                            filterPopup.setFilterResult((ExecutePopupFilterResult) pResult);
                            filterPopup.bind();
                        }
                        // Open filter popup
                        else {
                            editFilter(pResult,
                                    filterEditionExecuteSheetLinkFilterCreationCommand);
                        }
                    }
                });
        addEventHandler(GlobalEvent.LINK_PRODUCT.getType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        // Open link popup
                        if (pResult instanceof ExecutePopupFilterResult) {
                            filterPopup.setPopupFilterType(FilterPopupType.LINK_PRODUCT);
                            filterPopup.setFilterResult((ExecutePopupFilterResult) pResult);
                            filterPopup.bind();
                        }
                        // Open filter popup
                        else {
                            editFilter(pResult,
                                    filterEditionExecuteProductLinkFilterCreationCommand);
                        }
                    }
                });
        addEventHandler(GlobalEvent.UNLINK_SHEET.getType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        // Open unlink popup
                        if (pResult instanceof ExecutePopupFilterResult) {
                            filterPopup.setPopupFilterType(FilterPopupType.UNLINK_SHEET);
                            filterPopup.setFilterResult((ExecutePopupFilterResult) pResult);
                            filterPopup.bind();
                        }
                        // Open filter popup
                        else {
                            editFilter(pResult,
                                    filterEditionExecuteSheetLinkFilterDeletionCommand);
                        }
                    }
                });
        addEventHandler(GlobalEvent.UNLINK_PRODUCT.getType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        // Open unlink popup
                        if (pResult instanceof ExecutePopupFilterResult) {
                            filterPopup.setPopupFilterType(FilterPopupType.UNLINK_PRODUCT);
                            filterPopup.setFilterResult((ExecutePopupFilterResult) pResult);
                            filterPopup.bind();
                        }
                        // Open filter popup
                        else {
                            editFilter(pResult,
                                    filterEditionExecuteProductLinkFilterDeletionCommand);
                        }
                    }
                });
        addEventHandler(GlobalEvent.INIT_SHEET.getType(),
                new ActionEventHandler<AbstractCommandFilterResult>() {
                    @Override
                    public void execute(
                            final AbstractCommandFilterResult pResult) {
                        // Open initialization popup
                        if (pResult instanceof ExecutePopupFilterResult) {
                            filterPopup.setPopupFilterType(FilterPopupType.INIT_SHEET);
                            filterPopup.setFilterResult((ExecutePopupFilterResult) pResult);
                            filterPopup.bind();
                        }
                        // Open filter popup
                        else {
                            editFilter(pResult,
                                    filterEditionExecuteSheetFilterInitializationCommand);
                        }
                    }
                });
        addEventHandler(GlobalEvent.EXECUTE_EXTENDED_ACTION.getType(),
                new ActionEventHandler<AbstractExecuteExtendedActionResult>() {
                    @Override
                    public void execute(
                            final AbstractExecuteExtendedActionResult pResult) {
                        boolean lMessageDisplayed = false;
                        if (pResult instanceof GetInputDataResult) {
                            // Display an input data
                            inputDataPopup.initInputDataPopup(
                                    (GetInputDataResult) pResult,
                                    getProductName());
                            inputDataPopup.bind();
                        }
                        else if (pResult instanceof MessageExtendedActionResult) {
                            // Display a message
                            lMessageDisplayed = true;
                            Application.INJECTOR.getInfoMessagePresenter().displayMessage(
                                    ((MessageExtendedActionResult) pResult).getMessage());
                        }
                        else if (pResult instanceof GetFileExtendedActionResult) {
                            // Load a file
                            loadFile(
                                    ((GetFileExtendedActionResult) pResult).getFileId(),
                                    ((GetFileExtendedActionResult) pResult).getMimeType(),
                                    true);
                        }
                     
                        // Any ExtentionPoint can add a message to its return
                        if (!lMessageDisplayed
                                && pResult.getResultMessage() != null) {
                            Application.INJECTOR.getInfoMessagePresenter().displayMessage(
                                    pResult.getResultMessage());
                        }
                        
                        // Refresh filter ?
                        if (Boolean.TRUE.equals(pResult.getRefreshNeeded())) {
                            Application.INJECTOR.getProductWorkspacePresenter().getNavigation().refreshFilter();
                        }
                    }
                });
        addEventHandler(GlobalEvent.OPEN_SELECT_ENV_POPUP.getType(),
                new ActionEventHandler<SelectEnvironmentsResult>() {
                    @Override
                    public void execute(final SelectEnvironmentsResult pResult) {
                        // Open select environments popup
                        selectEnvironmentsPopup.init(pResult);
                        selectEnvironmentsPopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.OPEN_USER_AFFECTATION_POPUP.getType(),
                new ActionEventHandler<GetAffectationResult>() {
                    @Override
                    public void execute(final GetAffectationResult pResult) {
                        // Open select environments popup
                        userAffectationPopup.init(pResult);
                        userAffectationPopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.OPEN_IMPORT_PRODUCTS_POPUP.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(EmptyAction pResult) {
                        // Open import products popup
                        importProductsPopup.getDisplay().clear();
                        importProductsPopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.LOAD_ATTRIBUTES.getType(),
                new ActionEventHandler<GetAttributesResult>() {
                    @Override
                    public void execute(GetAttributesResult pResult) {
                        // Open attributes popup
                        if (pResult instanceof GetAttributesEditionResult) {
                            editAttributesPopup.init((GetAttributesEditionResult) pResult);
                            editAttributesPopup.bind();
                        }
                        else if (pResult instanceof GetAttributesVisualizationResult) {
                            viewAttributesPopup.init((GetAttributesVisualizationResult) pResult);
                            viewAttributesPopup.bind();
                        }
                    }
                });
        addEventHandler(GlobalEvent.OPEN_USER_PROFILE_POPUP.getType(),
                new ActionEventHandler<GetUserProfileResult>() {
                    @Override
                    public void execute(GetUserProfileResult pResult) {
                        userProfilePopup.init(pResult.getUser(),
                                pResult.isEditable(),
                                pResult.isPasswordEditable(),
                                pResult.getAvailableLanguages());
                        userProfilePopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.SAVE_USER_PROFILE.getType(),
                new ActionEventHandler<GetUserProfileResult>() {
                    @Override
                    public void execute(GetUserProfileResult pResult) {
                        MainDisplay lMain =
                                Application.INJECTOR.getMainPresenter().getDisplay();
                        lMain.setLoginAndLanguage(pResult.getUser().getLogin(),
                                pResult.getUser().getLanguage());

                        if (userProfilePopup.userChangedLanguage(pResult.getUser().getLanguage())) {
                            Application.INJECTOR.getInfoMessagePresenter().displayMessage(
                                    Ui18n.CONSTANTS.userProfileLanguageChanged());
                        }
                    }
                });
    }

    /**
     * Load a file
     * 
     * @param pFileId
     *            the file identifier
     * @param pFileFormat
     *            the file format
     * @param pSheetManagementModule
     *            If the action on the sheet management module (true) or in the
     *            product administration module (false). Determine the
     *            connection mode
     */
    private void loadFile(final String pFileId, final String pFileFormat,
            final boolean pSheetManagementModule) {
        final GpmUrlBuilder lUrlBuilder =
                new GpmUrlBuilder(Application.DOWNLOAD_URL);

        lUrlBuilder.setParameter(DownloadParameter.TYPE,
                DownloadParameter.TYPE_EXPORT.name().toLowerCase());
        if (pFileFormat != null) {
            lUrlBuilder.setParameter(DownloadParameter.FORMAT, pFileFormat);
        }
        lUrlBuilder.setParameter(DownloadParameter.FILE_ID, pFileId);
        if (pSheetManagementModule) {
            lUrlBuilder.setParameter(DownloadParameter.PRODUCT_NAME,
                    getProductName());
        }
        Window.open(lUrlBuilder.buildString(),
                GpmAnchorTarget.BLANK.getValue(), "");
    }

    private String getProductName() {
        if (userSpace.getCurrentProductWorkspace() != null) {
            return userSpace.getCurrentProductWorkspace().getTabId();
        }
        return null;
    }

    private <H extends EventHandler> void addEventHandler(final Type<H> pType,
            final H pHandler) {
        handlerRegistrations.add(eventBus.addHandler(pType, pHandler));
    }

    /**
     * Stop the popup manager.
     */
    public void stop() {
        for (HandlerRegistration lRegistration : handlerRegistrations) {
            lRegistration.removeHandler();
        }
        handlerRegistrations.clear();
    }

    /**
     * Get the process selection popup presenter.
     * 
     * @return the process selection popup presenter.
     */
    public ProcessSelectionPresenter getProcessSelectionPresenter() {
        return processSelection;
    }

    /**
     * Get the filter popup presenter.
     * 
     * @return The filter popup presenter.
     */
    public FilterPopupPresenter getFilterPopupPresenter() {
        return filterPopup;
    }

    /**
     * Get the filter edition popup presenter.
     * 
     * @return The filter edition popup presenter.
     */
    public FilterEditionPopupPresenter getFilterEditionPopupPresenter() {
        return filterEditionPopup;
    }

    /**
     * Get the select report presenter.
     * 
     * @return The select report presenter.
     */
    public SelectReportPresenter getSelectReportPresenter() {
        return selectReportPopup;
    }

    /**
     * Get the select exported fields presenter.
     * 
     * @return The select exported fields presenter.
     */
    public SelectExportedFieldsPresenter getSelectExportedFieldsPresenter() {
        return selectExportedFieldsPopup;
    }

    /**
     * Get the input data popup.
     * 
     * @return The input data popup.
     */
    public InputDataPresenter getInputDataPresenter() {
        return inputDataPopup;
    }

    /**
     * Get the send mail presenter.
     * 
     * @return The send mail presenter.
     */
    public SendMailPresenter getSendMailPresenter() {
        return sendMailPopup;
    }

    /**
     * Get the CSV popup presenter.
     * 
     * @return The CSV popup presenter.
     */
    public CsvOptionSelectionPresenter getCsvOptionSelectionPresenter() {
        return csvPopup;
    }

    /**
     * Get the select environments for product creation popup presenter.
     * 
     * @return the select environments popup presenter.
     */
    public SelectEnvironmentsPresenter getSelectEnvironmentsPresenter() {
        return selectEnvironmentsPopup;
    }

    /**
     * get the user affectation edition popup presenter
     * 
     * @return the user affectation popup presenter
     */
    public UserAffectationPresenter getUserAffectationPresenter() {
        return userAffectationPopup;
    }

    /**
     * Get the import products presenter.
     * 
     * @return The import products presenter.
     */
    public ImportProductsPresenter getImportProductsPresenter() {
        return importProductsPopup;
    }

    /**
     * get the attributes edition popup presenter
     * 
     * @return the attributes edition popup presenter
     */
    public AttributesEditionPresenter getAttributesPopup() {
        return editAttributesPopup;
    }

    private void handleConnection() {
        addEventHandler(GlobalEvent.CONNECTION.getType(),
                new ActionEventHandler<AbstractConnectionResult>() {
                    @Override
                    public void execute(final AbstractConnectionResult pResult) {
                        if (pResult instanceof LoginResult
                                && !((LoginResult) pResult).isLogged()) {
                            if (login.getDisplay().isShowing()) {
                                login.getDisplay().displayLoginError();
                            }
                            else {
                                login.bind();
                            }
                        }
                        else {
                            login.unbind();
                            if (pResult instanceof SelectProductResult) {
                                // Select a product
                                productSelection.initProducts((SelectProductResult) pResult);
                                productSelection.bind();
                            }
                            else if (pResult instanceof SelectProcessResult) {
                                // Select a process
                                processSelection.init((SelectProcessResult) pResult);
                                processSelection.bind();
                            }
                        }
                    }
                });
    }

    @SuppressWarnings("rawtypes")
	private void handleExport() {
        addEventHandler(GlobalEvent.EXPORT_SHEET.getType(),
                new ActionEventHandler<AbstractExportResult>() {
                    @Override
                    public void execute(final AbstractExportResult pResult) {
                        doSheetExport(pResult, false);
                    }
                });
        addEventHandler(GlobalEvent.EXPORT_SHEETS.getType(),
                new ActionEventHandler<AbstractExportResult>() {
                    @Override
                    public void execute(final AbstractExportResult pResult) {
                        doSheetExport(pResult, true);
                    }
                });
        addEventHandler(GlobalEvent.EXPORT_FILTER.getType(),
                new ActionEventHandler<ExportFileResult>() {
                    @Override
                    public void execute(final ExportFileResult pResult) {
                        // Load the export file
                        loadFile(pResult.getExportFileId(),
                                pResult.getFormat().name(), true);
                    }
                });
        addEventHandler(GlobalEvent.OPEN_CSV_POPUP.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(final EmptyAction pResult) {
                        // Open the CSV popup
                        csvPopup.reset();
                        csvPopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.EXPORT_PRODUCT.getType(),
                new ActionEventHandler<ExportFileResult>() {
                    @Override
                    public void execute(final ExportFileResult pResult) {
                        // Load the export file
                        loadFile(pResult.getExportFileId(),
                                pResult.getFormat().name(), false);
                    }
                });
        addEventHandler(GlobalEvent.EXPORT_PRODUCTS.getType(),
                new ActionEventHandler<ExportFileResult>() {
                    @Override
                    public void execute(final ExportFileResult pResult) {
                        // Load the export file
                        loadFile(pResult.getExportFileId(),
                                pResult.getFormat().name(), false);
                    }
                });
    }

    private void doSheetExport(final AbstractExportResult pResult,
            final boolean pMultivalued) {
        if (pResult instanceof ExportFileResult) {
            // Load the export file
            loadFile(((ExportFileResult) pResult).getExportFileId(),
                    ((ExportFileResult) pResult).getFormat().name(), true);
        }
        else if (pResult instanceof SelectFieldNamesResult) {
            // Select exported fields
            selectExportedFieldsPopup.init((SelectFieldNamesResult) pResult,
                    pMultivalued);
            selectExportedFieldsPopup.bind();
        }
        else if (pResult instanceof SelectReportModelResult) {
            // Select a report model
            selectReportPopup.init((SelectReportModelResult) pResult,
                    pMultivalued);
            selectReportPopup.bind();
        }
    }

    private void handleMailSheet() {
        addEventHandler(GlobalEvent.MAIL_SHEET.getType(),
                new ActionEventHandler<GetMailingInfoResult>() {
                    @Override
                    public void execute(final GetMailingInfoResult pResult) {
                        // Open mail popup
                        sendMailPopup.init(pResult, false);
                        sendMailPopup.bind();
                    }
                });
        addEventHandler(GlobalEvent.MAIL_SHEETS.getType(),
                new ActionEventHandler<GetMailingInfoResult>() {
                    @Override
                    public void execute(final GetMailingInfoResult pResult) {
                        // Open mail popup
                        sendMailPopup.init(pResult, true);
                        sendMailPopup.bind();
                    }
                });
    }

    /**
     * Get filter edition popup.
     * 
     * @param pResult
     *            Action result.
     * @param pExecuteCommand
     *            Execution command to call on click on execute button.
     */
    public void editFilter(AbstractCommandFilterResult pResult,
            AbstractFilterEditionExecuteCommand pExecuteCommand) {
        FilterEditionStep lStep = null;
        boolean lTempateFilter = false;

        filterEditionPopup.init(getProductName(), pResult.getFilterId(),
                pResult.getFilterType(), pExecuteCommand);

        if (pResult instanceof SelectContainerResult) {
            lStep = FilterEditionStep.STEP_1_CHOOSE_CONTAINERS;
            filterEditionPopup.selectContainers((SelectContainerResult) pResult);
        }
        else if (pResult instanceof SelectScopeResult) {
            lStep = FilterEditionStep.STEP_2_CHOOSE_PRODUCT;
            filterEditionPopup.selectScopes((SelectScopeResult) pResult);
            lTempateFilter = true;
        }
        else if (pResult instanceof SelectResultFieldResult) {
            lStep = FilterEditionStep.STEP_3_RESULT_FIELDS;
            filterEditionPopup.selectResultFields((SelectResultFieldResult) pResult);
        }
        else if (pResult instanceof SelectCriteriaFieldResult) {
            lStep = FilterEditionStep.STEP_4_CHOOSE_CRITERIA;
            filterEditionPopup.selectCriteriaFields((SelectCriteriaFieldResult) pResult);
            lTempateFilter = true;
        }
        else {
            return;
        }

        filterEditionPopup.setFilterTemplate(lTempateFilter);
        filterEditionPopup.setCurrentStep(lStep);
        filterEditionPopup.bind();
    }

    private void handleFilterEdition() {
        addEventHandler(GlobalEvent.NEW_SHEET_FILTER.getType(),
                new ActionEventHandler<SelectContainerResult>() {
                    @Override
                    public void execute(final SelectContainerResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteSheetFilterCommand);
                    }
                });
        addEventHandler(GlobalEvent.NEW_PRODUCT_FILTER.getType(),
                new ActionEventHandler<SelectContainerResult>() {
                    @Override
                    public void execute(final SelectContainerResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteProductFilterCommand);
                    }
                });
        addEventHandler(GlobalEvent.EDIT_SHEET_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteSheetFilterCommand);
                    }
                });
        addEventHandler(GlobalEvent.EDIT_PRODUCT_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteProductFilterCommand);
                    }
                });
        addEventHandler(GlobalEvent.EDIT_SHEET_LINK_CREATION_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteSheetLinkFilterCreationCommand);
                    }
                });
        addEventHandler(GlobalEvent.EDIT_SHEET_LINK_DELETION_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteSheetLinkFilterDeletionCommand);
                    }
                });
        addEventHandler(
                GlobalEvent.EDIT_PRODUCT_LINK_CREATION_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteProductLinkFilterCreationCommand);
                    }
                });
        addEventHandler(
                GlobalEvent.EDIT_PRODUCT_LINK_DELETION_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteProductLinkFilterDeletionCommand);
                    }
                });
        addEventHandler(GlobalEvent.EDIT_SHEET_INITIALIZATION_FILTER.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        editFilter(pResult,
                                filterEditionExecuteSheetFilterInitializationCommand);
                    }
                });
    }
}

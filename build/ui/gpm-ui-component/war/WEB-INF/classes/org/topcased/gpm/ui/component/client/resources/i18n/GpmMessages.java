/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.resources.i18n;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Interface to represent the messages contained in resource bundle:
 * GpmMessages.properties
 * 
 * @author mkargbo
 */
@DefaultLocale("en")
public interface GpmMessages extends Messages {
    @Key("field.error.mandatory")
    public String fieldErrorMandatory(String pFieldName);

    @Key("field.error.mandatory.multivalued")
    public String fieldErrorMandatoryMultivalued(String pFieldName);

    @Key("field.error.maxsize")
    public String fieldErrorMaxsize(String pFieldName,
            @PluralCount int pCharacterCount);

    @Key("field.error.url")
    public String fieldErrorUrl(String pFieldName);

    @Key("field.error.integer")
    public String fieldErrorInteger(String pFieldName);

    @Key("field.error.real")
    public String fieldErrorReal(String pFieldName);

    @Key("field.error.email")
    public String fieldErrorEmail(String pFieldName);

    @Key("field.error.time")
    public String fieldErrorTime(String pFieldName);

    @Key("field.error.password")
    public String fieldErrorPassword(String pFieldName, String pOriginFieldName);

    @Key("field.error.attachment.introduction")
    public String fieldErrorAttachmentIntroduction();

    @Key("field.error.attachment.emptyfile")
    public String fieldErrorAttachmentEmptyFile(String pFilename);

    @Key("field.error.attachment.sizeexceeded")
    public String fieldErrorAttachmentSizeExceeded(String pSize);

    @Key("field.error.attachment.invalidcharacter")
    public String fieldErrorAttachmentInvalidCharacter(String pFilename, String pInvalidCharacters);

    //#E-mail
    @Key("email.title")
    public String emailTitle(@PluralCount int pSheetCount);

    @Key("email.attachedsheets")
    public String emailAttachedSheets();

    //#Confirmation
    @Key("confirmation.sheetdeletion")
    public String confirmationSheetDeletion(@PluralCount int pSheetCount);

    @Key("confirmation.productdeletion")
    public String confirmationProductDeletion(@PluralCount int pProductCount);

    @Key("confirmation.filterdeletion")
    public String confirmationFilterDeletion();

    @Key("confirmation.sheetclose")
    public String confirmationSheetClose();

    @Key("confirmation.createremovelink")
    public String confirmationCreateOrRemoveLink();

    @Key("confirmation.productclose")
    public String confirmationCloseProduct();

    @Key("confirmation.changestate")
    public String confirmationChangeState();

    @Key("confirmation.createremoveproductlink")
    public String confirmationCreateOrRemoveProductLink();

    @Key("confirmation.userdeletion")
    public String confirmationUserDeletion();

    @Key("confirmation.categorychanged")
    public String confirmationCategoryChanged();

    @Key("confirmation.changerole")
    public String confirmationChangeRole();

    @Key("confirmation.logout")
    public String confirmationLogout();

    @Key("confirmation.close")
    public String confirmationClose();

    @Key("confirmation.multitab.forced.logout.message")
    public String confirmationMultiTabForcedReload();

    @Key("confirmation.visualize.current.edited.sheet")
    public String confirmationVisualizeCurrentEditedSheet();

    @Key("confirmation.refresh.current.edited.sheet")
    public String confirmationRefreshCurrentEditedSheet();

    @Key("confirmation.visualize.current.edited.product")
    public String confirmationVisualizeCurrentEditedProduct();

    @Key("confirmation.sheetsclose.modified")
    public String confirmationCloseSheetsModified(String pSheetRefs);

    @Key("confirmation.sheetsclose.notmodified")
    public String confirmationCloseSheetsNotModified();

    // Filter Edition Popup
    @Key("filter.edition.available.fields")
    public String filterEditionAvailableFields(String pFieldName);

    @Key("filter.edition.error.containers.empty")
    public String filterEditionErrorContainersEmpty();

    @Key("filter.edition.error.fields.empty")
    public String filterEditionErrorFieldsEmpty();

    //Error
    @Key("error.filter.needselection")
    public String errorFilterNeedSelection(@PluralCount int pNeededElementCount);

    @Key("error.sheet.needselection")
    public String errorSheetNeedSelection(@PluralCount int pNeededElementCount);

    @Key("error.product.needselection")
    public String errorProductNeedSelection(@PluralCount int pNeededElementCount);

    // TODO Remove this message
    @Key("error.send.mail.empty.field")
    @Deprecated
    public String errorSendMailEmptyField();

    @Key("error.select.environment.needselection")
    public String errorSelectEnvironmentSelectOneAtLeast();

    @Key("error.sheet.already.opened")
    public String errorSheetOpenAlreadyOpened();

    @Key("error.product.workspace.already.opened")
    public String errorProductWorkspaceOpenAlreadyOpened();

    @Key("error.product.already.opened")
    public String errorProductOpenAlreadyOpened();

    @Key("error.session")
    public String errorSession();

    // Filter execution report
    @Key("popup.filter.execution.report.non.executable.products.title")
    public String filterExecutionReportNonExecutableProductsTitle();

    @Key("popup.filter.execution.report.executable.products.title")
    public String filterExecutionReportExecutableProductsTitle();

    @Key("popup.filter.execution.report.executable.products.text")
    public String filterExecutionReportExecutableProductsText(String pRole);

    @Key("popup.filter.execution.report.constraints.title")
    public String filterExecutionReportAddConstraintsTitle();

    @Key("popup.filter.execution.report.constraints.type.text")
    public String filterExecutionReportAddConstraintsTypeText(String pType);

    @Key("popup.filter.execution.report.constraints.role.text")
    public String filterExecutionReportAddConstraintsRoleText(String pRole);

    @Key("error.category.value")
    public String errorDuplicateValue();

    @Key("error.browser.unsupported.file.protocol")
    public String errorUnsupportedProtocol();
}
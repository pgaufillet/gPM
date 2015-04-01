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

import org.topcased.gpm.business.util.action.ActionName;

import com.google.gwt.i18n.client.Constants;

/**
 * Interface to represent the constants contained in resource bundle
 * 
 * @author mkargbo
 */
public interface GpmConstants extends Constants {
    // Common
    @Key("application.title")
    public String applicationTitle();

    @Key("loading")
    public String loading();

    @Key("minimize")
    public String minimize();

    @Key("maximize")
    public String maximize();

    @Key("restore")
    public String restore();

    @Key("ok")
    public String ok();

    @Key("execute")
    public String execute();

    @Key("product")
    public String product();

    @Key("process")
    public String process();

    @Key("user")
    public String user();

    @Key("save")
    public String save();

    @Key("export")
    public String export();

    @Key("next")
    public String next();

    @Key("previous")
    public String previous();

    @Key("fields")
    public String fields();

    @Key("dictionary")
    public String dictionary();

    @Key("environment")
    public String environment();

    @Key("environments")
    public String environments();

    @Key("select")
    public String select();

    @Key("name")
    public String name();

    @Key("version")
    public String version();

    @Key("visualize")
    public String visualize();

    @Key("edit")
    public String edit();

    // Authentication
    @Key("authentication.title")
    public String authenticationTitle();

    @Key("authentication.login.text")
    public String authenticationLoginText();

    @Key("authentication.password.text")
    public String authenticationPasswordText();

    @Key("authentication.button.text")
    public String authenticationButtonText();

    @Key("authentication.error")
    public String authenticationError();

    @Key("product.selection.title")
    public String productSelectionTitle();

    @Key("product.selection.listtab.title")
    public String productSelectionListTabTitle();

    @Key("product.selection.listtab.subtitle")
    public String productSelectionListTabSubTitle();

    @Key("product.selection.hierarchytab.title")
    public String productSelectionHierarchyTabTitle();

    @Key("product.selection.hierarchytab.subtitle")
    public String productSelectionHierarchyTabSubTitle();

    @Key("workspace.user.selectrole")
    public String workspaceUserSelectRole();

    @Key("navigation.filtertab.title")
    public String navigationFilterTabTitle();

    @Key("navigation.filtertab.process.title")
    public String navigationFilterTabProcessTitle();

    @Key("navigation.filtertab.product.title")
    public String navigationFilterTabProductTitle();

    @Key("navigation.filtertab.user.title")
    public String navigationFilterTabUserTitle();

    @Key("listing.filter.execution.report.title")
    public String filterExecutionReport();

    @Key("navigation.treetab.title")
    public String navigationTreeTabTitle();

    @Key("navigation.treetab.selection")
    public String navigationTreeTabSelection();

    @Key("admin.navigation.usertab.name.title")
    public String navigationUserTabNameTitle();

    @Key("admin.navigation.usertab.login.title")
    public String navigationUserTabLoginTitle();

    @Key("admin.navigation.dictionary.title")
    public String navigationDictionaryTitle();

    @Key("error.title")
    public String errorTitle();

    @Key("error.stacktrace")
    public String errorStackTrace();

    @Key("error.invocation.exception")
    public String errorInvocationException();

    @Key("info.title")
    public String infoTitle();

    @Key("report.selectmodel.title")
    public String reportSelectModelTitle();

    @Key("report.selectfields.title")
    public String reportSelectFieldsTitle();

    @Key("error.session.title")
    public String errorSessionTitle();

    @Key("error.session.button")
    public String errorSessionButton();

    @Key("error.session.cancel.button")
    public String errorSessionCancelButton();

    //#E-mail
    @Key("email.to")
    public String emailTo();

    @Key("email.subject")
    public String emailSubject();

    @Key("email.body")
    public String emailBody();

    @Key("email.reportmodel")
    public String emailReportModel();

    @Key("email.send.button")
    public String emailSendButton();

    //  #Confirmation
    @Key("confirmation.title")
    public String confirmationTitle();

    @Key("confirmation.yes")
    public String confirmationYes();

    @Key("confirmation.no")
    public String confirmationNo();

    @Key("confirmation.sheet.locked")
    public String sheetLockedConfirmation();

    //Administration product
    @Key("admin.product.title")
    public String adminProductTitle();

    @Key("admin.product.field.productname")
    public String adminProductName();

    @Key("admin.product.field.description")
    public String adminProductDescription();

    @Key("admin.product.field.parent")
    public String adminProductFieldParent();

    @Key("admin.product.field.child")
    public String adminProductFieldChild();

    @Key("admin.product.field.properties")
    public String adminProductFieldProperties();

    //Admin dictionary
    @Key("admin.dictionary.title")
    public String adminDictionaryTitle();

    @Key("admin.dictionary.field.category")
    public String adminDictionaryFieldCategory();

    @Key("admin.dictionary.field.categoryvalues")
    public String adminDictionaryFieldCategoryValues();

    @Key("admin.dictionary.environment.public")
    public String adminDictionaryEnvironmentPublic();

    @Key("admin.dictionary.environment.private")
    public String adminDictionaryEnvironmentPrivate();

    //Admin user
    @Key("admin.user.title")
    public String adminUserTitle();

    @Key("admin.user.field.login")
    public String adminUserFieldLogin();

    @Key("admin.user.field.name")
    public String adminUserFieldName();

    @Key("admin.user.field.firstname")
    public String adminUserFieldFirstName();

    @Key("admin.user.field.mail")
    public String adminUserFieldMail();

    @Key("admin.user.field.language")
    public String adminUserFieldLanguage();

    @Key("admin.user.field.password")
    public String adminUserFieldPassword();

    @Key("admin.user.field.passwordbis")
    public String adminUserFieldPasswordBis();

    @Key("admin.user.field.newpassword")
    public String adminUserFieldNewPassword();

    @Key("admin.user.field.newpasswordbis")
    public String adminUserFieldNewPasswordBis();

    @Key("admin.user.group.properties")
    public String adminUserGroupProperties();

    @Key("admin.user.group.password")
    public String adminUserGroupPassword();

    @Key("admin.user.group.affectation")
    public String adminUserGroupAffectation();

    // Export CSV Options
    @Key("export.csv.options.title")
    public String exportCsvOptionsTitle();

    @Key("export.csv.options.separator.choice")
    public String exportCsvOptionsSeparatorCharacterChoice();

    @Key("export.csv.options.quote.choice")
    public String exportCsvOptionsQuoteCharacterChoice();

    @Key("export.csv.options.escape.choice")
    public String exportCsvOptionsEscapeCharacterChoice();

    // Filter Edition Popup
    @Key("filter.edition.popup.title")
    public String filterEditionPopupTitle();

    @Key("filter.edition.step.1")
    public String filterEditionStep1();

    @Key("filter.edition.step.2")
    public String filterEditionStep2();

    @Key("filter.edition.step.3")
    public String filterEditionStep3();

    @Key("filter.edition.step.4")
    public String filterEditionStep4();

    @Key("filter.edition.step.5")
    public String filterEditionStep5();

    @Key("filter.edition.save.title")
    public String filterEditionSaveTitle();

    @Key("filter.edition.filter.name")
    public String filterEditionFilterName();

    @Key("filter.edition.filter.name.tooltip")
    public String filterEditionFilterNameTooltip();

    @Key("filter.edition.filter.description")
    public String filterEditionFilterDescription();

    @Key("filter.edition.filter.description.tooltip")
    public String filterEditionFilterDescriptionTooltip();

    @Key("filter.edition.filter.usage")
    public String filterEditionFilterUsage();

    @Key("filter.edition.filter.usage.tooltip")
    public String filterEditionFilterUsageTooltip();

    @Key("filter.edition.save.usage.both")
    public String filterEditionSaveUsageBoth();

    @Key("filter.edition.save.usage.table")
    public String filterEditionSaveUsageTable();

    @Key("filter.edition.save.usage.tree")
    public String filterEditionSaveUsageTree();

    @Key("filter.edition.filter.visibility")
    public String filterEditionFilterVisibility();

    @Key("filter.edition.filter.visibility.tooltip")
    public String filterEditionFilterVisibilityTooltip();

    @Key("filter.edition.save.visibility.instance")
    public String filterEditionSaveVisibilityInstance();

    @Key("filter.edition.save.visibility.product")
    public String filterEditionSaveVisibilityProduct();

    @Key("filter.edition.save.visibility.user")
    public String filterEditionSaveVisibilityUser();

    @Key("filter.edition.filter.hidden")
    public String filterEditionFilterHidden();

    @Key("filter.edition.filter.hidden.tooltip")
    public String filterEditionFilterHiddenTooltip();

    @Key("filter.edition.and")
    public String filterEditionAnd();

    @Key("filter.edition.or")
    public String filterEditionOr();

    @Key("filter.edition.available.types")
    public String filterEditionAvailableTypesHierarchy();

    @Key("filter.edition.selected.fields")
    public String filterEditionSelectedFields();

    @Key("filter.edition.include.sub.products.tooltip")
    public String filterEditionIncludeSubProductsTooltip();

    @Key("filter.edition.available.products")
    public String filterEditionAvailableProducts();

    @Key("filter.edition.selected.products")
    public String filterEditionSelectedProducts();

    @Key("filter.edition.available.containers")
    public String filterEditionAvailableContainers();

    @Key("filter.edition.selected.containers")
    public String filterEditionSelectedContainers();

    @Key("filter.edition.selected.products.tick.to.include.sub.products")
    public String filterEditionSelectedProductsTickToIncSubProducts();

    @Key("filter.edition.casesensitive.tooltip")
    public String filterEditionCaseSensitiveTooltip();
    
    @Key("filter.edition.compare.with.criterion.tooltip")
    public String filterEditionCompareWithCriterionTooltip();

    @Key("filter.edition.order.ascendant")
    public String filterEditionOrderAscendant();

    @Key("filter.edition.order.ascendant.defined")
    public String filterEditionOrderAscendantDefined();

    @Key("filter.edition.order.descendant")
    public String filterEditionOrderDescendant();

    @Key("filter.edition.order.descendant.defined")
    public String filterEditionOrderDescendantDefined();

    @Key("filter.edition.criteria.operator.equal")
    public String filterEditionOperatorEqual();

    @Key("filter.edition.criteria.operator.notequal")
    public String filterEditionOperatorNotEqual();

    @Key("filter.edition.criteria.operator.greater")
    public String filterEditionOperatorGreater();

    @Key("filter.edition.criteria.operator.greaterorequals")
    public String filterEditionOperatorGreaterOrEquals();

    @Key("filter.edition.criteria.operator.less")
    public String filterEditionOperatorLess();

    @Key("filter.edition.criteria.operator.lessorequals")
    public String filterEditionOperatorLessOrEquals();

    @Key("filter.edition.criteria.operator.like")
    public String filterEditionOperatorLike();

    @Key("filter.edition.criteria.operator.notlike")
    public String filterEditionOperatorNotLike();

    @Key("filter.edition.criteria.operator.since")
    public String filterEditionOperatorSince();

    @Key("filter.edition.criteria.operator.other")
    public String filterEditionOperatorOther();

    @Key("filter.edition.criteria.period.lastMonth")
    public String filterEditionDateLastMonth();

    @Key("filter.edition.criteria.period.thisMonth")
    public String filterEditionDateThisMonth();

    @Key("filter.edition.criteria.period.lastWeek")
    public String filterEditionDateLastWeek();

    @Key("filter.edition.criteria.period.thisWeek")
    public String filterEditionDateThisWeek();

    @Key("filter.edition.criteria.period.days")
    public String filterEditionDateDays();

    @Key("filter.edition.criteria.boolean.radio.button")
    public String filterEditionBooleanRadioButton();

    // Transition history
    @Key("transitions.history.title")
    public String transitionsHistoryTitle();

    // Filter Popup
    @Key("filter.popup.initialize.sheet.title")
    public String filterPopupInitializeSheetTitle();

    @Key("filter.popup.initialize.button")
    public String filterPopupInitializeSheetButton();

    @Key("filter.popup.link.sheet.title")
    public String filterPopupLinkSheetTitle();

    @Key("filter.popup.unlink.sheet.title")
    public String filterPopupUnlinkSheetTitle();

    @Key("filter.popup.link.product.title")
    public String filterPopupLinkProductTitle();

    @Key("filter.popup.unlink.product.title")
    public String filterPopupUnlinkProductTitle();

    @Key("filter.popup.link.button")
    public String filterPopupLinkButton();

    @Key("filter.popup.unlink.button")
    public String filterPopupUnlinkButton();

    @Key("filter.popup.editfilter.button")
    public String filterPopupEditFilterButton();

    // User Admin Popup
    @Key("admin.user.popup.affectation.title")
    public String adminUserPopupAffectationTitle();

    // User Profile Popup
    @Key("user.popup.profile.title")
    public String userProfilePopupTitle();

    @Key("user.popup.profile.informations.title")
    public String userProfilePopupGroupInformations();

    @Key("user.popup.profile.password.title")
    public String userProfileGroupPassword();

    @Key("user.popup.profile.language.changed")
    public String userProfileLanguageChanged();

    // Zoom popup
    @Key("zoompopup.title")
    public String zoomPopupTitle();

    // Select Environment popup
    @Key("select.environment.popup.title")
    public String selectEnvironmentsTitle();

    // Select process popup
    @Key("select.process.popup.title")
    public String selectProcessTitle();

    // Upload
    @Key("upload.title")
    public String uploadTitle();

    @Key("upload.text")
    public String uploadText();

    // Product import
    @Key("admin.user.popup.product.import.title")
    public String productImportTitle();

    @Key("admin.user.popup.product.import.field")
    public String productImportField();

    @Key("admin.user.popup.product.import.button")
    public String productImportButton();

    @Key("admin.user.popup.product.import.message")
    public String productImportMessage();

    // Attribute Popup
    @Key("attribute.popup.title.visualization")
    public String attributePopupTitleVisualization();

    @Key("attribute.popup.title.edition")
    public String attributePopupTitleEdition();

    @Key("attribute.popup.field.name")
    public String attributePopupFieldName();

    @Key("attribute.popup.field.values")
    public String attributePopupFieldValues();

    // Pager
    @Key("pager.first")
    public String pagerFirst();

    @Key("pager.previous")
    public String pagerPrevious();

    @Key("pager.next")
    public String pagerNext();

    @Key("pager.last")
    public String pagerLast();

    @Key("pager.refresh")
    public String pagerRefresh();

    @Key("pager.nbresults")
    public String pagerNbResults();

    @Key("pager.resultsperpage")
    public String pagerResultsPerPage();

    // Banner tooltip
    @Key("banner.help")
    public String bannerHelp();

    @Key("banner.admin")
    public String bannerAdministration();

    @Key("banner.logout")
    public String bannerLogout();

    @Key("banner.contact.url")
    public String contactUrl();

    // Rich Text Box
    @Key("richtext.tooltip.bold")
    public String richTextTooltipBold();

    @Key("richtext.tooltip.italic")
    public String richTextTooltipItalic();

    @Key("richtext.tooltip.underline")
    public String richTextTooltipUnderline();

    @Key("richtext.tooltip.align.left")
    public String richTextTooltipAlignLeft();

    @Key("richtext.tooltip.align.center")
    public String richTextTooltipAlignCenter();

    @Key("richtext.tooltip.align.right")
    public String richTextTooltipAlignRight();

    @Key("richtext.tooltip.bullet")
    public String richTextTooltipBullet();

    @Key("richtext.tooltip.bullet.numbered")
    public String richTextTooltipBulletNumbered();

    @Key("richtext.tooltip.indent.right")
    public String richTextTooltipIndentRight();

    @Key("richtext.tooltip.indent.left")
    public String richTextTooltipIndentLeft();

    @Key("richtext.tooltip.url")
    public String richTextTooltipUrl();

    @Key("richtext.tooltip.horizontal.bar")
    public String richTextTooltipHorizontalBar();

    @Key("richtext.tooltip.image")
    public String richTextTooltipImage();

    @Key("richtext.tooltip.clear.format")
    public String richTextTooltipClearFormat();

    @Key("richtext.insert.url")
    public String richTextInsertUrl();

    @Key("richtext.insert.image")
    public String richTextInsertImage();

    @Key("richtext.colors")
    public String richTextTooltipColors();

    @Key("richtext.fonts")
    public String richTextTooltipFonts();

    // Tree Choice Popup
    @Key("popup.multivalued.treechoice.title")
    public String multivaluedTreePopupTitle();

    // Tool bar button toop tip
    @Key(ActionName.SHEET_CREATION)
    public String sheetCreateButton();

    @Key(ActionName.SHEET_SAVE)
    public String sheetSaveButton();

    @Key(ActionName.SHEET_DISPLAY)
    public String sheetDisplayButton();

    @Key(ActionName.SHEET_EDIT)
    public String sheetEditButton();

    @Key(ActionName.SHEET_REFRESH)
    public String sheetRefreshButton();

    @Key(ActionName.SHEET_CHANGE_STATE)
    public String sheetChangeStateButton();

    @Key(ActionName.SHEET_DELETE)
    public String sheetDeleteButton();

    @Key(ActionName.SHEET_EXPORT)
    public String sheetExportButton();

    @Key(ActionName.SHEET_INITIALIZE)
    public String sheetInitializeButton();

    @Key(ActionName.SHEET_DUPLICATE)
    public String sheetDuplicateButton();

    @Key(ActionName.SHEET_MAIL)
    public String sheetMailButton();

    @Key(ActionName.SHEET_LINK)
    public String sheetLinkButton();

    @Key(ActionName.SHEET_UNLINK)
    public String sheetUnlinkButton();

    @Key(ActionName.FILTER_SHEET_CREATE)
    public String sheetFilterCreateButton();

    @Key(ActionName.FILTER_SHEET_REFRESH)
    public String sheetFilterRefreshButton();

    @Key(ActionName.FILTER_SHEET_EDIT)
    public String sheetFilterEditButton();

    @Key(ActionName.FILTER_SHEET_DELETE)
    public String sheetFilterDeleteButton();

    @Key(ActionName.FILTER_SHEET_EXPORT)
    public String sheetFilterExportButton();

    @Key(ActionName.SHEETS_DELETE)
    public String sheetsDeleteButton();

    @Key(ActionName.SHEETS_EXPORT)
    public String sheetsExportButton();

    @Key(ActionName.SHEETS_MAIL)
    public String sheetsMailButton();

    @Key(ActionName.SHEETS_CLOSE)
    public String sheetsCloseButton();

    @Key(ActionName.PRODUCT_CREATION)
    public String productCreateButton();

    @Key(ActionName.PRODUCT_SAVE)
    public String productSaveButton();

    @Key(ActionName.PRODUCT_DISPLAY)
    public String productDisplayButton();

    @Key(ActionName.PRODUCT_EDIT)
    public String productEditButton();

    @Key(ActionName.PRODUCT_REFRESH)
    public String productRefreshButton();

    @Key(ActionName.PRODUCT_DELETE)
    public String productDeleteButton();

    @Key(ActionName.PRODUCT_EXPORT)
    public String productExportButton();

    @Key(ActionName.PRODUCT_LINK)
    public String productLinkButton();

    @Key(ActionName.PRODUCT_UNLINK)
    public String productUnlinkButton();

    @Key(ActionName.PRODUCT_ATTRIBUTES_DISPLAY)
    public String productAttributesDisplayButton();

    @Key(ActionName.PRODUCT_ATTRIBUTES_EDIT)
    public String productAttributesEditButton();

    @Key(ActionName.FILTER_PRODUCT_CREATE)
    public String productFilterCreateButton();

    @Key(ActionName.FILTER_PRODUCT_REFRESH)
    public String productFilterRefreshButton();

    @Key(ActionName.FILTER_PRODUCT_EDIT)
    public String productFilterEditButton();

    @Key(ActionName.FILTER_PRODUCT_DELETE)
    public String productFilterDeleteButton();

    @Key(ActionName.PRODUCTS_DELETE)
    public String productsDeleteButton();

    @Key(ActionName.PRODUCTS_EXPORT)
    public String productsExportButton();

    @Key(ActionName.PRODUCTS_IMPORT)
    public String productsImportButton();

    @Key(ActionName.USER_CREATION)
    public String userCreateButton();

    @Key(ActionName.USER_SAVE)
    public String userSaveButton();

    @Key(ActionName.USER_AFFECTATION)
    public String userAffectationButton();

    @Key(ActionName.USER_DELETE)
    public String userDeleteButton();

    @Key(ActionName.DICTIONARY_EDITION)
    public String userDictionaryEditButton();

    @Key(ActionName.ENVIRONMENT_EDITION)
    public String userEnvironmentEditButton();

    @Key(ActionName.ENVIRONMENT_CREATION)
    public String userEnvironmentCreateButton();

    @Key(ActionName.ENVIRONMENT_SAVE)
    public String userEnvironmentSaveButton();

    @Key(ActionName.DICTIONARY_CATEGORY_SAVE)
    public String userDictionaryCategorySaveButton();

    @Key(ActionName.DICTIONARY_CATEGORY_SORT)
    public String userDictionaryCategorySortButton();

    @Key(ActionName.ENVIRONMENT_CATEGORY_SAVE)
    public String userEnvironmentCategorySaveButton();
}
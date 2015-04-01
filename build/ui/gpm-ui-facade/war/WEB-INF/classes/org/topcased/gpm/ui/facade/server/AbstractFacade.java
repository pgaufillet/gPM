/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.help.service.HelpService;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.mail.service.MailService;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.DateDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.ExternDisplayHint;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.JAppletDisplayHint;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.AttachedDisplayHintType;
import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.util.DateDisplayHintType;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.util.log.GPMLogger;
import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableChoiceFieldAccess;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.domain.facilities.AttachedFieldDisplayType;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayType;
import org.topcased.gpm.domain.facilities.DateDisplayType;
import org.topcased.gpm.domain.facilities.TextFieldDisplayType;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.cache.UserCacheManager;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiAppletField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiAttachedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiBooleanField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceTreeField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceFieldValue;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceTreeFieldValue;
import org.topcased.gpm.ui.facade.shared.container.field.virtual.UiVirtualField;

/**
 * AbstractFacade
 * 
 * @author nveillet
 */
public class AbstractFacade {

    private AttributesService attributesService;

    private AuthorizationService authorizationService;

    private EnvironmentService environmentService;

    private ExportService exportService;

    private ExtensionsService extensionsService;

    private FieldsContainerService fieldsContainerService;

    private FieldsService fieldsService;

    private HelpService helpService;

    private I18nService i18nService;

    private ImportService importService;

    private InstanceService instanceService;

    private LifeCycleService lifeCycleService;

    private LinkService linkService;

    private MailService mailService;

    private ProductService productService;

    private ReportingService reportingService;

    private SearchService searchService;

    private SheetExportService sheetExportService;

    private SheetService sheetService;

    private UserCacheManager userCacheManager;
    
    /** GPM Logger */
    protected GPMLogger gpmLogger = GPMLogger.getLogger(AbstractFacade.class);

    /**
     * get attributesService
     * 
     * @return the attributesService
     */
    protected AttributesService getAttributesService() {
        return attributesService;
    }

    /**
     * get authorizationService
     * 
     * @return the authorizationService
     */
    protected AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /**
     * Get corresponding context
     * 
     * @param pSession
     *            the session
     * @return the context
     */
    protected Context getContext(UiSession pSession) {
        Context lContext = ContextBase.getEmptyContext();

        lContext.put("ApplicationURL", pSession.getParent().getApplicationUrl());

        return lContext;
    }

    /**
     * get environmentService
     * 
     * @return the environmentService
     */
    protected EnvironmentService getEnvironmentService() {
        return environmentService;
    }

    /**
     * get exportService
     * 
     * @return the exportService
     */
    protected ExportService getExportService() {
        return exportService;
    }

    /**
     * get extensionsService
     * 
     * @return the extensionsService
     */
    protected ExtensionsService getExtensionsService() {
        return extensionsService;
    }

    /**
     * get fieldsContainerService
     * 
     * @return the fieldsContainerService
     */
    protected FieldsContainerService getFieldsContainerService() {
        return fieldsContainerService;
    }

    /**
     * get fieldsService
     * 
     * @return the fieldsService
     */
    protected FieldsService getFieldsService() {
        return fieldsService;
    }

    /**
     * get helpService
     * 
     * @return the helpService
     */
    protected HelpService getHelpService() {
        return helpService;
    }

    /**
     * get i18nService
     * 
     * @return the i18nService
     */
    protected I18nService getI18nService() {
        return i18nService;
    }

    /**
     * get importService
     * 
     * @return the importService
     */
    protected ImportService getImportService() {
        return importService;
    }

    /**
     * get instanceService
     * 
     * @return the instanceService
     */
    protected InstanceService getInstanceService() {
        return instanceService;
    }

    /**
     * get lifeCycleService
     * 
     * @return the lifeCycleService
     */
    protected LifeCycleService getLifeCycleService() {
        return lifeCycleService;
    }

    /**
     * get linkService
     * 
     * @return the linkService
     */
    protected LinkService getLinkService() {
        return linkService;
    }

    /**
     * get mailService
     * 
     * @return the mailService
     */
    protected MailService getMailService() {
        return mailService;
    }

    /**
     * get productService
     * 
     * @return the productService
     */
    protected ProductService getProductService() {
        return productService;
    }

    /**
     * get reportingService
     * 
     * @return the reportingService
     */
    protected ReportingService getReportingService() {
        return reportingService;
    }

    /**
     * get searchService
     * 
     * @return the searchService
     */
    protected SearchService getSearchService() {
        return searchService;
    }

    /**
     * get sheetExportService
     * 
     * @return the sheetExportService
     */
    protected SheetExportService getSheetExportService() {
        return sheetExportService;
    }

    /**
     * get sheetService
     * 
     * @return the sheetService
     */
    protected SheetService getSheetService() {
        return sheetService;
    }

    /**
     * Get UiField from BusinessField
     * 
     * @param pSession
     *            user session
     * @param pBusinessField
     *            Field to get values from
     * @param pBusinessContainer
     *            Container from business
     * @param pDisplayMode
     *            display mode
     * @param pTranslationManager
     *            translation manager
     * @param pMultipleFieldName
     *            the multiple field name (null if root field)
     * @param pPointerFieldName
     *            the pointer field name (null if not pointer field)
     * @return the UiField
     */
    @SuppressWarnings("unchecked")
    private UiField getUiField(final UiSession pSession,
            final BusinessField pBusinessField,
            final BusinessContainer pBusinessContainer,
            final DisplayMode pDisplayMode,
            final I18nTranslationManager pTranslationManager,
            final Map<String, List<CategoryValue>> pCategoryCache,
            final String pMultipleFieldName, final String pPointerFieldName,
            final Context pContext) {

        CacheableFieldsContainer lCacheableFieldsContainer =
                ((AbstractCacheableContainerAccess
                        <CacheableFieldsContainer, CacheableValuesContainer>)
                        pBusinessContainer).getType();
        CacheableValuesContainer lCacheableValuesContainer =
                ((AbstractCacheableContainerAccess
                        <CacheableFieldsContainer, CacheableValuesContainer>)
                        pBusinessContainer).read();

        // Determine the field name to used for get display hint
        String lDisplayHintFieldName = pBusinessField.getFieldName();
        if (pPointerFieldName != null) {
            lDisplayHintFieldName = pPointerFieldName;
        }

        UiField lField = null;
        if (pBusinessField instanceof BusinessAttachedField) {
            UiAttachedField lAttachedField = new UiAttachedField();

            //Set display Hint
            DisplayHint lDisplayHint =
                    lCacheableFieldsContainer.getDisplayHint(lDisplayHintFieldName);

            if (lDisplayHint instanceof AttachedDisplayHint) {
                AttachedDisplayHint lAttachedDisplayHint =
                        (AttachedDisplayHint) lDisplayHint;

                //Fill attributes values
                lAttachedField.setHeight(lAttachedDisplayHint.getHeight());
                lAttachedField.setWidth(lAttachedDisplayHint.getWidth());
                if (AttachedFieldDisplayType.IMAGE.getValue().equals(
                        lAttachedDisplayHint.getDisplayType())) {
                    lAttachedField.setAttachedDisplayHintType(
                            AttachedDisplayHintType.ATTACHED_IMAGE);
                }

            }
            else if (!(lDisplayHint instanceof ExternDisplayHint)
                    && lDisplayHint != null) {
                throw new GDMException("Invalid Display Hint: "
                        + lDisplayHint.getName());
            }

            lField = lAttachedField;
        }
        else if (pBusinessField instanceof BusinessChoiceField) {

            // display Hint
            DisplayHint lDisplayHint =
                    lCacheableFieldsContainer.getDisplayHint(lDisplayHintFieldName);

            List<CategoryValue> lCategoryValues = null;
            if (pBusinessField instanceof CacheableChoiceFieldAccess) {
                // Possible Values
                String lCategoryName = ((CacheableChoiceFieldAccess)
                        pBusinessField).getFieldType().getCategoryName();

                // Category values
                lCategoryValues = pCategoryCache.get(lCategoryName);
                if (lCategoryValues == null) {
                    lCategoryValues =
                            getEnvironmentService().getCategoryValues(
                                    pSession.getRoleToken(),
                                    pSession.getParent().getProcessName(),
                                    lCacheableValuesContainer.getEnvironmentNames(),
                                    Arrays.asList(lCategoryName)).get(
                                    lCategoryName);
                    pCategoryCache.put(lCategoryName, lCategoryValues);
                }
            }

            // Translation manager
            I18nTranslationManager lTranslationManager =
                    FacadeLocator.instance().getI18nFacade().getTranslationManager(
                            pSession.getParent().getLanguage());
            if (lDisplayHint instanceof ChoiceTreeDisplayHint) {
                UiChoiceTreeField lChoiceTreeField = new UiChoiceTreeField();
                String lSeparator =
                        ((ChoiceTreeDisplayHint) lDisplayHint).getSeparator();
                if (lCategoryValues != null) {
                    List<UiChoiceTreeFieldValue> lRootList =
                            updateTreeNode(lTranslationManager,
                                    lCategoryValues, lSeparator);
                    List<Translation> lRoots = new ArrayList<Translation>();
                    for (UiChoiceTreeFieldValue lTrans : lRootList) {
                        lRoots.add(lTrans);
                    }
                    lChoiceTreeField.setPossibleValues(lRoots);
                }
                lField = lChoiceTreeField;
            }
            else {

                UiChoiceField lChoiceField = new UiChoiceField();

                if (lDisplayHint instanceof ChoiceDisplayHint) {
                    ChoiceDisplayHint lChoiceDisplayHint =
                            (ChoiceDisplayHint) lDisplayHint;
                    String lImageType = lChoiceDisplayHint.getImageType();
                    ChoiceDisplayHintType lDisplayHintType = null;
                    if (lChoiceDisplayHint.isList()) {
                        lDisplayHintType = ChoiceDisplayHintType.LIST;
                    }
                    else if (ChoiceFieldDisplayType.IMAGE.getValue().equals(
                            lImageType)) {
                        lDisplayHintType = ChoiceDisplayHintType.NOT_LIST_IMAGE;
                    }
                    else if (ChoiceFieldDisplayType.IMAGE_TEXT.getValue().equals(
                            lImageType)) {
                        lDisplayHintType =
                                ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT;
                    }
                    else if (lImageType == null) {
                        lDisplayHintType = ChoiceDisplayHintType.NOT_LIST;
                    }
                    else {
                        throw new GDMException("Invalid Choice Filed Type.");
                    }
                    lChoiceField.setChoiceDisplayHintType(lDisplayHintType);
                }
                else if (!(lDisplayHint instanceof ExternDisplayHint)
                        && lDisplayHint != null) {
                    throw new GDMException("Invalid Display Hint: "
                            + lDisplayHint.getName());
                }

                if (lCategoryValues != null) {
                	
                    // add the current value to the list 
                    if (pBusinessField.getAsString() != null) {
                        boolean lValueFound = false;
                        for (CategoryValue lCategoryValue : lCategoryValues) {
                            if (lCategoryValue.getValue().equals(
                                    pBusinessField.getAsString())) {
                                lValueFound = true;
                                break;
                            }
                        }
                        if (!lValueFound && pBusinessField != null
                                && pBusinessField.getAsString() != null) {
                            lCategoryValues.add(new CategoryValue(
                                    pBusinessField.getAsString()));
                        }
                    }
          	
                    for (CategoryValue lCategoryValue : lCategoryValues) {
                        UiChoiceFieldValue lChoiceValue =
                                new UiChoiceFieldValue();
                        lChoiceValue.setValue(lCategoryValue.getValue());
                        if (!ChoiceDisplayHintType.NOT_LIST_IMAGE.equals(
                                lChoiceField.getChoiceDisplayHintType())) {
                            String lTextTranslation =
                                    lTranslationManager.getTextTranslation(lChoiceValue.getValue());
                            lChoiceValue.setTranslatedValue(lTextTranslation);
                        }
                        if (ChoiceDisplayHintType.NOT_LIST_IMAGE.equals(
                                        lChoiceField.getChoiceDisplayHintType())
                                || ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT.equals(
                                        lChoiceField.getChoiceDisplayHintType())) {
                            String lImageTranslation =
                                    lTranslationManager.getImageTranslation(
                                            lChoiceValue.getValue());
                            lChoiceValue.setTranslatedImageValue(lImageTranslation);
                        }
                        lChoiceField.addPossibleValue(lChoiceValue);
                    }
                }

                lField = lChoiceField;
            }
        }
        else if (pBusinessField instanceof BusinessMultipleField) {
            UiMultipleField lMultipleField = new UiMultipleField();
            BusinessMultipleField lBusinessMultipleField =
                    (BusinessMultipleField) pBusinessField;

            for (BusinessField lBusinessField : lBusinessMultipleField) {
                if (!lBusinessField.isConfidential()) {
                    lMultipleField.addField(getUiField(pSession,
                            lBusinessField, pBusinessContainer, pDisplayMode,
                            pTranslationManager, pCategoryCache,
                            lBusinessMultipleField.getFieldName(),
                            pPointerFieldName, pContext));
                }
            }

            lField = lMultipleField;
        }
        else if (pBusinessField instanceof BusinessMultivaluedField<?>) {
            BusinessMultivaluedField<?> lBusinessMultivaluedField =
                    (BusinessMultivaluedField<?>) pBusinessField;

            UiField lTemplate =
                    getUiField(pSession, lBusinessMultivaluedField.getFirst(),
                            pBusinessContainer, pDisplayMode,
                            pTranslationManager, pCategoryCache,
                            pMultipleFieldName, pPointerFieldName, pContext);

            // if template is multivalued, we get the multivalued field's template
            if (lTemplate instanceof UiMultivaluedField) {
                lTemplate = ((UiMultivaluedField) lTemplate).getTemplateField();
            }

            UiMultivaluedField lMultivaluedField =
                    new UiMultivaluedField(lTemplate);

            lField = lMultivaluedField;
        }
        else if (pBusinessField instanceof BusinessPointerField) {
            BusinessPointerField lBusinessPointerField =
                    (BusinessPointerField) pBusinessField;

            if (lBusinessPointerField.getPointedContainerId() != null) {
                BusinessField lPointedField =
                        lBusinessPointerField.getPointedField();
                lField =
                        getUiField(pSession, lPointedField, pBusinessContainer,
                                pDisplayMode, pTranslationManager,
                                pCategoryCache, null,
                                pBusinessField.getFieldName(), pContext);
            }
            else {
                UiStringField lStringField = new UiStringField();
                lStringField.set("");
                lField = lStringField;
            }
            lField.setFieldDescription(
                    pTranslationManager.getTextTranslation(pBusinessField.getFieldDescription()));
            lField.setFieldName(pBusinessField.getFieldName());
            lField.setMandatory(pBusinessField.isMandatory());
            lField.setTranslatedFieldName(
                    pTranslationManager.getTextTranslation(pBusinessField.getFieldName()));
            lField.setUpdatable(false);

            return lField;
        }
        else if (pBusinessField instanceof BusinessBooleanField) {
            lField = new UiBooleanField();
        }
        else if (pBusinessField instanceof BusinessDateField) {
            UiDateField lDateField = new UiDateField();

            //Set display Hint
            DisplayHint lDisplayHint =
                    lCacheableFieldsContainer.getDisplayHint(lDisplayHintFieldName);

            if (lDisplayHint instanceof DateDisplayHint) {
                DateDisplayHint lDateDisplayHint =
                        (DateDisplayHint) lDisplayHint;
                String lFormat = lDateDisplayHint.getFormat();
                DateDisplayHintType lDisplayHintType = null;
                if (lDateDisplayHint.hasTime()
                        && lDateDisplayHint.getIncludeTime()) {
                    if (DateDisplayType.SHORT.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_TIME_SHORT;
                    }
                    else if (DateDisplayType.MEDIUM.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_TIME_MEDIUM;
                    }
                    else if (DateDisplayType.LONG.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_TIME_LONG;
                    }
                    else if (DateDisplayType.FULL.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_TIME_FULL;
                    }
                    else if (lFormat != null) {
                        throw new GDMException("Invalid Date Format");
                    }
                }
                else {
                    if (DateDisplayType.SHORT.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_SHORT;
                    }
                    else if (DateDisplayType.MEDIUM.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_MEDIUM;
                    }
                    else if (DateDisplayType.LONG.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_LONG;
                    }
                    else if (DateDisplayType.FULL.getValue().equals(lFormat)) {
                        lDisplayHintType = DateDisplayHintType.DATE_FULL;
                    }
                    else if (lFormat != null) {
                        throw new GDMException("Invalid Date Format");
                    }
                }
                lDateField.setDateDisplayHintType(lDisplayHintType);
            }
            else if (!(lDisplayHint instanceof ExternDisplayHint)
                    && lDisplayHint != null) {
                throw new GDMException("Invalid Display Hint: "
                        + lDisplayHint.getName());
            }

            lField = lDateField;
        }
        else if (pBusinessField instanceof BusinessIntegerField) {
            lField = new UiIntegerField();
        }
        else if (pBusinessField instanceof BusinessRealField) {
            lField = new UiRealField();
        }
        else if (pBusinessField instanceof BusinessStringField) {

            UiStringField lStringField = new UiStringField();

            //Set display Hint
            DisplayHint lDisplayHint =
                    lCacheableFieldsContainer.getDisplayHint(lDisplayHintFieldName);

            if (lDisplayHint instanceof TextDisplayHint) {

                TextDisplayHint lTextDisplayHint =
                        (TextDisplayHint) lDisplayHint;
                String lDisplayType = lTextDisplayHint.getDisplayType();
                StringDisplayHintType lDisplayHintType = null;

                //Fill attributes values
                lStringField.setHeight(lTextDisplayHint.getHeight());
                lStringField.setWidth(lTextDisplayHint.getWidth());

                if (TextFieldDisplayType.SINGLE_LINE.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType = StringDisplayHintType.SINGLE_LINE;
                }
                else if (TextFieldDisplayType.MULTI_LINE.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType = StringDisplayHintType.MULTI_LINE;
                }
                else if (TextFieldDisplayType.URL.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType = StringDisplayHintType.URL;
                }
                else if (TextFieldDisplayType.INTERNAL_URL.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType = StringDisplayHintType.INTERNAL_URL;
                    lStringField.setInternalUrlSheetReference(
                            ((BusinessStringField) pBusinessField).getInternalUrlSheetReference());
                }
                else if (TextFieldDisplayType.RICH_TEXT.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType = StringDisplayHintType.RICH_TEXT;
                }
                else if (TextFieldDisplayType.EXTERNAL_WEB_CONTENT.getValue().equals(
                        lDisplayType)) {
                    lDisplayHintType =
                            StringDisplayHintType.EXTERNAL_WEB_CONTENT;
                }
                else {
                    throw new GDMException("Invalid Display Type.");
                }
                lStringField.setStringDisplayHintType(lDisplayHintType);
            }
            else if (lDisplayHint instanceof ChoiceStringDisplayHint
                    && !DisplayMode.VISUALIZATION.equals(pDisplayMode)
                    && pBusinessField.isUpdatable()) {

                UiStringChoiceField lStringChoiceField =
                        new UiStringChoiceField();

                ChoiceStringDisplayHint lChoiceStringDisplayHint =
                        (ChoiceStringDisplayHint) lDisplayHint;

                String lFieldId = null;
                if (pMultipleFieldName == null) {
                    lFieldId =
                            lCacheableFieldsContainer.getFieldFromLabel(
                                    pBusinessField.getFieldName()).getId();
                }
                else {
                    Iterator<Field> lSubFieldsIterator = ((MultipleField)
                            lCacheableFieldsContainer.getFieldFromLabel(pMultipleFieldName))
                                .getFields().iterator();
                    while (lSubFieldsIterator.hasNext() && lFieldId == null) {
                        Field lSubField = lSubFieldsIterator.next();
                        if (lSubField.getLabelKey().equals(
                                pBusinessField.getFieldName())) {
                            lFieldId = lSubField.getId();
                        }
                    }
                }
                Map<String, Object> lChoiceStringData =
                        fieldsService.getChoiceStringData(
                                pSession.getRoleToken(),
                                lCacheableFieldsContainer.getId(), lFieldId,
                                Context.createContext(pContext));

                List<String> lChoiceStringList =
                        (List<String>) lChoiceStringData.get(
                                ExtensionPointParameters.CHOICES_RESULT.getParameterName());
                String lDefaultChoiceStringValue = (String) lChoiceStringData.get(
                        ExtensionPointParameters.CHOICES_RESULT_DEFAULT_VALUE.getParameterName());
                //Fill attributes values
                lStringChoiceField.setStrict(lChoiceStringDisplayHint.isStrict());
                lStringChoiceField.setPossibleValues(lChoiceStringList);

                BusinessStringField lBusinessStringField =
                        (BusinessStringField) pBusinessField;
                if (lBusinessStringField.get() == null) {
                    if (lDefaultChoiceStringValue != null
                            && !lDefaultChoiceStringValue.isEmpty()) {
                        lBusinessStringField.set(lDefaultChoiceStringValue);
                    }

                }
                lStringChoiceField.set(lBusinessStringField.get());
                lStringField = lStringChoiceField;
            }
            else if (lDisplayHint instanceof JAppletDisplayHint) {

                // set diplayHint
                JAppletDisplayHint lJAppletDisplayHint =
                        (JAppletDisplayHint) lDisplayHint;

                // get the sheet ID
                String lSheetId = null;
                if (pBusinessContainer != null) {
                    lSheetId = pBusinessContainer.getId();
                }
                UiAppletField lUiAppletField =
                        new UiAppletField(
                                lSheetId,
                                lJAppletDisplayHint.getAppletCode(),
                                lJAppletDisplayHint.getAppletCodeBase(),
                                lJAppletDisplayHint.getAppletAlter(),
                                lJAppletDisplayHint.getAppletName(),
                                lJAppletDisplayHint.getAppletArchive(),
                                lJAppletDisplayHint.getAppletParameterAsStringList());

                lStringField = lUiAppletField;
                lStringField.setStringDisplayHintType(StringDisplayHintType.APPLET);
                lField = lStringField;
            }
            else if (!(lDisplayHint instanceof ExternDisplayHint
                    || lDisplayHint instanceof ChoiceTreeDisplayHint
                    || lDisplayHint instanceof ChoiceStringDisplayHint)
                    && lDisplayHint != null) {
                throw new GDMException("Invalid Display Hint: "
                        + lDisplayHint.getName());
            }

            lField = lStringField;
            lStringField.setSize(((BusinessStringField) pBusinessField).getSize());
        }
        else if (pBusinessField instanceof BusinessVirtualField) {
            lField = new UiVirtualField();
        }
        else {
            throw new GDMException("Invalid Field Type: "
                    + pBusinessField.getFieldName());
        }

        lField.setFieldDescription(
                pTranslationManager.getTextTranslation(pBusinessField.getFieldDescription()));
        lField.setFieldName(pBusinessField.getFieldName());
        lField.setTranslatedFieldName(
                pTranslationManager.getTextTranslation(pBusinessField.getFieldName()));
        lField.setMandatory(pBusinessField.isMandatory());
        lField.setUpdatable(pBusinessField.isUpdatable());
        lField.copy(pBusinessField);

        // Force updatability to false according to display mode, 
        // business field mode and business container mode
        if (DisplayMode.VISUALIZATION.equals(pDisplayMode)
                || !pBusinessContainer.isUpdatable() && !DisplayMode.CREATION.equals(pDisplayMode) && pBusinessField.isUpdatable()) {

            lField.setUpdatable(false);
        }

        return lField;
    }

    /**
     * get userCacheManager
     * 
     * @return the userCacheManager
     */
    protected UserCacheManager getUserCacheManager() {
        return userCacheManager;
    }

    /**
     * Initialize the fields container with fields list
     * 
     * @param pBusinessContainer
     *            the container to initialize
     * @param pFields
     *            the fields
     */
    protected void initFieldsContainer(BusinessContainer pBusinessContainer,
            List<UiField> pFields) {
        for (BusinessField lField : pFields) {
            pBusinessContainer.getField(lField.getFieldName()).copy(lField);
        }
    }

    /**
     * Initialize UiContainer from BusinessContainer
     * 
     * @param pContainer
     *            container to initialize
     * @param pBusinessContainer
     *            container to get values from
     * @param pSession
     *            user session
     * @param pDisplayMode
     *            display mode
     * @param pTranslationManager
     *            translation manager
     * @param pCategoryCache
     *            Category cache
     * @param pContext
     *            Execution context
     */
    protected void initUiContainer(final UiContainer pContainer,
            final BusinessContainer pBusinessContainer,
            final UiSession pSession,
            final org.topcased.gpm.business.util.DisplayMode pDisplayMode,
            final I18nTranslationManager pTranslationManager,
            final Map<String, List<CategoryValue>> pCategoryCache,
            final Context pContext) {

        //Container attributes
        pContainer.setBusinessProcessName(pBusinessContainer.getBusinessProcessName());

        pContainer.setId(pBusinessContainer.getId());

        pContainer.setDeletable(pBusinessContainer.isDeletable());
        pContainer.setUpdatable(pBusinessContainer.isUpdatable());

        /* Checking if the user can Create the Sheet type */
        // getting the sheet type Name
        final String lTypeName = pBusinessContainer.getTypeName();

        // getting the cache able sheet type collection
        List<CacheableFieldsContainer> lCacheableSheetTypes =
                getFieldsContainerService().getFieldsContainer(
                        pSession.getRoleToken(),
                        SheetType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.CREATE);
        // building sheet type name list
        ArrayList<String> lSheetTypeNames =
                new ArrayList<String>(lCacheableSheetTypes.size());
        for (CacheableFieldsContainer lCacheableSheetType : lCacheableSheetTypes) {
            lSheetTypeNames.add(lCacheableSheetType.getName());
        }

        // checking if the list contain this sheet type name
        if (lSheetTypeNames.contains(lTypeName)) {
            // user can create this type of sheet, thus can duplicated it also
            pContainer.setDuplicable(Boolean.TRUE);
        }

        /* setting the others features */
        pContainer.setTypeDescription(pBusinessContainer.getTypeDescription());
        pContainer.setTypeId(pBusinessContainer.getTypeId());
        pContainer.setTypeName(pBusinessContainer.getTypeName());

        for (BusinessField lBusinessField : pBusinessContainer) {
            // Confidential fields are not showed
            if (!lBusinessField.isConfidential()) {
                pContainer.addField(getUiField(pSession, lBusinessField,
                        pBusinessContainer, pDisplayMode, pTranslationManager,
                        pCategoryCache, null, null, pContext));
            }
        }

    }

    /**
     * set attributesService
     * 
     * @param pAttributesService
     *            the attributesService to set
     */
    public void setAttributesService(AttributesService pAttributesService) {
        attributesService = pAttributesService;
    }

    /**
     * set authorizationService
     * 
     * @param pAuthorizationService
     *            the authorizationService to set
     */
    public void setAuthorizationService(
            AuthorizationService pAuthorizationService) {
        authorizationService = pAuthorizationService;
    }

    /**
     * set environmentService
     * 
     * @param pEnvironmentService
     *            the environmentService to set
     */
    public void setEnvironmentService(EnvironmentService pEnvironmentService) {
        environmentService = pEnvironmentService;
    }

    /**
     * set exportService
     * 
     * @param pExportService
     *            the exportService to set
     */
    public void setExportService(ExportService pExportService) {
        exportService = pExportService;
    }

    /**
     * set extensionsService
     * 
     * @param pExtensionsService
     *            the extensionsService to set
     */
    public void setExtensionsService(ExtensionsService pExtensionsService) {
        extensionsService = pExtensionsService;
    }

    /**
     * set fieldsContainerService
     * 
     * @param pFieldsContainerService
     *            the fieldsContainerService to set
     */
    public void setFieldsContainerService(
            FieldsContainerService pFieldsContainerService) {
        fieldsContainerService = pFieldsContainerService;
    }

    /**
     * set fieldsService
     * 
     * @param pFieldsService
     *            the fieldsService to set
     */
    public void setFieldsService(FieldsService pFieldsService) {
        fieldsService = pFieldsService;
    }

    /**
     * set helpService
     * 
     * @param pHelpService
     *            the helpService to set
     */
    public void setHelpService(HelpService pHelpService) {
        helpService = pHelpService;
    }

    /**
     * set i18nService
     * 
     * @param pI18nService
     *            the i18nService to set
     */
    public void setI18nService(I18nService pI18nService) {
        i18nService = pI18nService;
    }

    /**
     * set importService
     * 
     * @param pImportService
     *            the importService to set
     */
    public void setImportService(ImportService pImportService) {
        importService = pImportService;
    }

    /**
     * set instanceService
     * 
     * @param pInstanceService
     *            the instanceService to set
     */
    public void setInstanceService(InstanceService pInstanceService) {
        instanceService = pInstanceService;
    }

    /**
     * set lifeCycleService
     * 
     * @param pLifeCycleService
     *            the lifeCycleService to set
     */
    public void setLifeCycleService(LifeCycleService pLifeCycleService) {
        lifeCycleService = pLifeCycleService;
    }

    /**
     * set linkService
     * 
     * @param pLinkService
     *            the linkService to set
     */
    public void setLinkService(LinkService pLinkService) {
        linkService = pLinkService;
    }

    /**
     * set mailService
     * 
     * @param pMailService
     *            the mailService to set
     */
    public void setMailService(MailService pMailService) {
        mailService = pMailService;
    }

    /**
     * set productService
     * 
     * @param pProductService
     *            the productService to set
     */
    public void setProductService(ProductService pProductService) {
        productService = pProductService;
    }

    /**
     * set reportingService
     * 
     * @param pReportingService
     *            the reportingService to set
     */
    public void setReportingService(ReportingService pReportingService) {
        reportingService = pReportingService;
    }

    /**
     * set searchService
     * 
     * @param pSearchService
     *            the searchService to set
     */
    public void setSearchService(SearchService pSearchService) {
        searchService = pSearchService;
    }

    /**
     * set sheetExportService
     * 
     * @param pSheetExportService
     *            the sheetExportService to set
     */
    public void setSheetExportService(SheetExportService pSheetExportService) {
        sheetExportService = pSheetExportService;
    }

    /**
     * set sheetService
     * 
     * @param pSheetService
     *            the sheetService to set
     */
    public void setSheetService(SheetService pSheetService) {
        sheetService = pSheetService;
    }

    /**
     * Set a temporary functional reference to the values container
     * 
     * @param pSession
     *            the session
     * @param pCacheableValuesContainer
     *            the cacheable values container
     */
    protected void setTemporaryFunctionalReference(UiSession pSession,
            CacheableValuesContainer pCacheableValuesContainer) {
        String lReference;
        if (pCacheableValuesContainer instanceof CacheableProduct) {
            lReference = pCacheableValuesContainer.getProductName();
        }
        else {
            lReference = pCacheableValuesContainer.getFunctionalReference();
        }

        if (StringUtils.isEmpty(lReference)) {
            // Get translation manager
            I18nTranslationManager lTranslationManager =
                    FacadeLocator.instance().getI18nFacade().getTranslationManager(
                            pSession.getParent().getLanguage());
            lReference = "* "
                + lTranslationManager.getTextTranslation(pCacheableValuesContainer.getTypeName());
            if (pCacheableValuesContainer instanceof CacheableProduct) {
                pCacheableValuesContainer.setProductName(lReference);
            }
            else {
                pCacheableValuesContainer.setFunctionalReference(lReference);
            }
        }
    }

    /**
     * set userCacheManager
     * 
     * @param pUserCacheManager
     *            the userCacheManager to set
     */
    public void setUserCacheManager(UserCacheManager pUserCacheManager) {
        userCacheManager = pUserCacheManager;
    }

    /**
     * Set a new identifier to the values container
     * 
     * @param pCacheableValuesContainer
     *            the values container
     */
    protected void setValuesContainerId(
            CacheableValuesContainer pCacheableValuesContainer) {
        if (pCacheableValuesContainer.getId() == null) {
            pCacheableValuesContainer.setId(UUID.randomUUID().toString());
        }
    }

    private List<UiChoiceTreeFieldValue> updateTreeNode(
            I18nTranslationManager pTranslationManager,
            List<CategoryValue> pCategoryValues, String pSeparator) {
        List<UiChoiceTreeFieldValue> lResultList =
                new ArrayList<UiChoiceTreeFieldValue>();
        for (CategoryValue lCategoryValue : pCategoryValues) {
            updateTreeNodeRecursive(pTranslationManager, "",
                    lCategoryValue.getValue(), pSeparator, lResultList);
        }
        return lResultList;
    }

    private void updateTreeNodeRecursive(
            I18nTranslationManager pTranslationManager, String pParentValue,
            String pValue, String pSeparator,
            List<UiChoiceTreeFieldValue> pNodeList) {

        String lNodeAndChildValue = pValue.substring(pParentValue.length());
        if (lNodeAndChildValue.startsWith(pSeparator)) {
            lNodeAndChildValue = lNodeAndChildValue.substring(1);
        }
        int lFirstIndexOfSeparator = lNodeAndChildValue.indexOf(pSeparator);

        String lNodePath = null;
        String lNodeValue = null;
        if (lFirstIndexOfSeparator == -1) {
            lNodeValue = lNodeAndChildValue;
        }
        else {
            lNodeValue =
                    lNodeAndChildValue.substring(0, lFirstIndexOfSeparator);
        }

        lNodePath = pParentValue + pSeparator + lNodeValue;
        if (lNodePath.startsWith(pSeparator)) {
            lNodePath = lNodePath.substring(1);
        }

        UiChoiceTreeFieldValue lNode;
        boolean lFound = false;
        int i = 0;
        while (i < pNodeList.size() && !lFound) {
            if (lNodePath.equals(pNodeList.get(i).getValue())) {
                lFound = true;
            }
            else {
                i++;
            }
        }

        if (lFound) {
            lNode = pNodeList.get(i);
        }
        else {
            // The node does not exist
            lNode = new UiChoiceTreeFieldValue(lNodePath,
                    pTranslationManager.getTextTranslation(lNodeValue),
                    false, null);
            pNodeList.add(lNode);
        }

        if (lFirstIndexOfSeparator == -1) {
            //Last value
            lNode.setSelectable(true);
        }
        else {
            //Node has children
            if (lNode.getChildren() == null) {
                lNode.setChildren(new ArrayList<UiChoiceTreeFieldValue>());
            }
            updateTreeNodeRecursive(pTranslationManager, lNodePath, pValue,
                    pSeparator, lNode.getChildren());
        }

    }
}

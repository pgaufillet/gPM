/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.exportimport.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.domain.export.ExportType;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeCommand;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportFileFacadeCommand;
import org.topcased.gpm.ui.facade.server.exportimport.ExportFilterResultFacadeCommand;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.ui.facade.server.exportimport.ExportProductsFacadeCommand;
import org.topcased.gpm.ui.facade.server.exportimport.ExportSheetsFacadeCommand;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.export.UiExportableField;
import org.topcased.gpm.ui.facade.shared.export.UiExportableGroup;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * ExportImportFacade
 * 
 * @author nveillet
 */
public class ExportImportFacadeImpl extends AbstractFacade implements
        ExportImportFacade {

    /**
     * Add a command in cache
     * 
     * @param pSession
     *            the session
     * @param pCommand
     *            the command
     */
    private String addToCache(final UiSession pSession,
            final FacadeCommand pCommand) {

        String lId = UUID.randomUUID().toString();

        getUserCacheManager().getUserCache(pSession.getParent()).getExportCache().put(
                lId, pCommand);

        return lId;
    }

    /**
     * Clear an exported file from cache
     * 
     * @param pSession
     *            the session
     * @param pFileIdInCache
     *            the file identifier
     */
    public void clearCache(final UiSession pSession, final String pFileIdInCache) {
        getUserCacheManager().getUserCache(pSession.getParent()).getExportCache().remove(
                pFileIdInCache);
    }

    /**
     * Convert a field from business in exportable field for UI
     * 
     * @param pField
     *            the field from business
     * @param pTranslationManager
     *            the translation manager
     * @return the exportable field for UI
     */
    private UiExportableField convertExportableField(final Field pField,
            I18nTranslationManager pTranslationManager) {
        if (!pField.isConfidential() && pField.isExportable()) {
            UiExportableField lExportableField =
                    new UiExportableField(
                            pField.getLabelKey(),
                            pTranslationManager.getTextTranslation(pField.getLabelKey()));
            if (pField.getMultiple()) {
                MultipleField lMultipleField = (MultipleField) pField;
                lExportableField.setExportableFields(new ArrayList<UiExportableField>());
                for (Field lSubField : lMultipleField.getFields()) {
                    UiExportableField lExportableSubField =
                            convertExportableField(lSubField,
                                    pTranslationManager);
                    if (lExportableSubField != null) {
                        lExportableField.getExportableFields().add(
                                lExportableSubField);
                    }
                }
            }
            return lExportableField;
        }
        return null;
    }

    /**
     * Create command to export a file and put it in the cache
     * 
     * @param pSession
     *            Current user session.
     * @param pFile
     *            File to get.
     * @return Id of the command in cache
     */
    public String exportFile(final UiSession pSession, final byte[] pFile) {

        FacadeCommand lCommand = new ExportFileFacadeCommand(pFile);

        return addToCache(pSession, lCommand);
    }

    /**
     * Create command to export a filter result and put it in the cache
     * 
     * @param pSession
     *            Current user session.
     * @param pFilterId
     *            Id of filter to execute.
     * @param pExportFormat
     *            Export format.
     * @param pAdditionalParameters
     *            The additional parameters
     * @return Id of the command in cache
     */
    @SuppressWarnings("unchecked")
    public String exportFilterResult(final UiSession pSession,
            final String pFilterId, ExportFormat pExportFormat,
            Map<ExportParameter, String> pAdditionalParameters) {

        long lTime = System.currentTimeMillis();
        
        FilterFacade lFilterFacade = FacadeLocator.instance().getFilterFacade();
        ExecutableFilterData lFilter =
                lFilterFacade.getExecutableFilter(pSession, pFilterId);

        // Execute the filter
        FilterQueryConfigurator lFilterQueryConfigurator =
                new FilterQueryConfigurator();

        FilterResultIterator<SummaryData> lFilterResultIterator =
                getSearchService().executeFilter(
                        pSession.getRoleToken(),
                        lFilter,
                        new FilterVisibilityConstraintData(
                                pSession.getParent().getLogin(),
                                pSession.getParent().getProcessName(),
                                pSession.getProductName()),
                        lFilterQueryConfigurator);

        // get results
        List<SheetSummaryData> lList =
                IteratorUtils.toList(lFilterResultIterator);

        // get columns names
        Map<String, String> lLabels = new HashMap<String, String>();
        for (UsableFieldData lField : lFilter.getResultSummaryData().getUsableFieldDatas()) {
            String lLabel = this.getFieldLabelForColumn(pSession, lField);
            lLabels.put(lField.getId(), lLabel);
        }

        Context lContext = getContext(pSession);
        if (lList.size() >= getSearchService().getResultsLimit()) {
            lContext.put(SearchService.LIMIT_REACHED, true);
        }

        FacadeCommand lCommand =
                new ExportFilterResultFacadeCommand(getSheetExportService(),
                        pSession.getRoleToken(), lLabels, lList,
                        SheetExportFormat.valueOf(pExportFormat.toString()),
                        pAdditionalParameters, lContext);

        // Log
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.FILTER_EXPORT, 
                pExportFormat.toString(), lFilter.getLabelKey(), 
                (System.currentTimeMillis() - lTime) + "");
        
        return addToCache(pSession, lCommand);
    }

    /**
     * @param pSession
     *            Current user session
     * @param pField
     *            Field the column label is needed for
     * @return The label of the field, preceded by the possible container field
     *         label keys, separated by pipes.
     */
    protected String getFieldLabelForColumn(UiSession pSession,
            UsableFieldData pField) {
        StringBuilder lResult = new StringBuilder();
        //Starting with the labels of all the containers.
        for (FilterFieldsContainerInfo lContainerInfo : pField.getFieldsContainerHierarchy()) {
            if (lContainerInfo != null) {
                lResult.append(lContainerInfo.getLabelKey());
                lResult.append("|");
            }
        }
        //Actual label of the field.
        String lLabel = pField.getLabel();
        if (lLabel == null) {
            lLabel =
                    getI18nService().getValueForUser(pSession.getRoleToken(),
                            pField.getFieldName());
        }
        lResult.append(lLabel);
        return lResult.toString();
    }

    /**
     * Create command to export a list of products and put it in the cache
     * 
     * @param pSession
     *            Current user session
     * @param pProductsIds
     *            List of identifiers of products to export
     * @return Id of the command in cache
     */
    public String exportProducts(final UiSession pSession,
            final List<String> pProductsIds) {

        FacadeCommand lCommand =
                new ExportProductsFacadeCommand(getExportService(), pSession,
                        pProductsIds);

        return addToCache(pSession.getParent().getDefaultGlobalSession(),
                lCommand);
    }

    /**
     * Create command to export a list of sheets and put it in the cache
     * 
     * @param pSession
     *            Current user session
     * @param pSheetsIds
     *            List of Ids of sheets to export
     * @param pExportFormat
     *            Export format
     * @param pReportModel
     *            Report model
     * @param pFieldsNames
     *            List of fields to export
     * @param pLocale
     *            Current preferred language.
     * @return Id of the command in cache
     */
    public String exportSheets(final UiSession pSession,
            final List<String> pSheetsIds, final ExportFormat pExportFormat,
            final String pReportModel, final List<String> pFieldsNames,
            final Locale pLocale) {

        long lTime = System.currentTimeMillis();
        
        FacadeCommand lCommand =
                new ExportSheetsFacadeCommand(getReportingService(),
                        getSheetExportService(), pSession, pSheetsIds,
                        SheetExportFormat.valueOf(pExportFormat.name()),
                        pLocale, getReportingService().getReportModel(
                                pReportModel,
                                pSession.getParent().getProcessName()),
                        pFieldsNames, getContext(pSession));

        // Log
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.SHEET_EXPORT, 
                pExportFormat.toString(),
                (System.currentTimeMillis() - lTime) + "");
        
        return addToCache(pSession, lCommand);
    }

    /**
     * Get attached file content from its identifier.
     * 
     * @param pSession
     *            Current user session.
     * @param pAttachedFieldValueId
     *            Field Id.
     * @return Attached field content of given Id.
     */
    public byte[] getAttachedFile(final UiSession pSession,
            final String pAttachedFieldValueId) {

        byte[] lValue =
                getSheetService().getAttachedFileContent(
                        pSession.getRoleToken(), pAttachedFieldValueId);
        return lValue;
    }

    /**
     * Get compatible report model names
     * 
     * @param pSession
     *            Current user session.
     * @param pSheetIds
     *            List of sheet Ids to export.
     * @param pExportFormat
     *            Export format.
     * @return List of model names.
     */
    public List<Translation> getAvailableReportModels(final UiSession pSession,
            final List<String> pSheetIds, final ExportFormat pExportFormat) {

        String lExportFormat;
        if (ExportFormat.EXCEL.equals(pExportFormat)) {
            lExportFormat = "XLS";
        }
        else {
            lExportFormat = pExportFormat.toString();
        }

        ExportType lExportType =
                getReportingService().getExportType(lExportFormat);

        List<ReportModelData> lCompatibleModels =
                getReportingService().getCompatibleModels(
                        pSession.getRoleToken(),
                        pSheetIds.toArray(new String[0]), lExportType);

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        ArrayList<Translation> lCompatibleModelNames =
                new ArrayList<Translation>();
        for (ReportModelData lReportModelData : lCompatibleModels) {
            lCompatibleModelNames.add(new Translation(
                    lReportModelData.getName(),
                    lTranslationManager.getTextTranslation(lReportModelData.getName())));
        }

        return lCompatibleModelNames;
    }

    /**
     * Get exportable field organized by group for a given Fields Container.
     * 
     * @param pSession
     *            current user session
     * @param pContainerIds
     *            identifiers of containers. All container must be the same type
     * @return list groups contain the exportable fields
     */
    public List<UiExportableGroup> getExportableFields(
            final UiSession pSession, final List<String> pContainerIds) {

        Map<String, UiExportableField> lExportableFields =
                new HashMap<String, UiExportableField>();

        // Get translation manager
        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        String lTypeId = null;

        final Set<String> lTypeIds =
                getSheetService().getSheetTypeIdBySheetIds(pContainerIds);
        if (lTypeIds.size() != 1) {
            throw new ConstraintException(
                    "Containers types must be the same for all values containers"
                            + " to the selection of exportable fields.");
        }
        for (String lContainerId : pContainerIds) {
            if (null == lTypeId) {
                lTypeId = lTypeIds.iterator().next();
            }

            // Get type with access control
            // CAUTION : Don't work for the product or links...
            CacheableFieldsContainer lCacheableFieldsContainer =
                    getSheetService().getCacheableSheetType(
                            pSession.getRoleToken(),
                            lTypeId,
                            new CacheProperties(
                                    false,
                                    new AccessControlContextData(
                                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                            lContainerId)));

            // get exportable fields
            Collection<? extends Field> lFields =
                    lCacheableFieldsContainer.getFields();

            for (Field lField : lFields) {
                UiExportableField lExportableField =
                        convertExportableField(lField, lTranslationManager);
                if (lExportableField != null) {
                    UiExportableField lExistingExportableField =
                            lExportableFields.get(lExportableField.getName());
                    if (lExistingExportableField == null
                            || (lExportableField.getExportableFields() != null && lExportableField.getExportableFields().size() > lExistingExportableField.getExportableFields().size())) {
                        lExportableFields.put(lExportableField.getName(),
                                lExportableField);
                    }
                }
            }
        }

        CacheableFieldsContainer lCacheableFieldsContainer =
                getFieldsService().getCacheableFieldsContainer(
                        pSession.getRoleToken(), lTypeId);

        // Organize field by groups
        List<UiExportableGroup> lExportableGroups =
                new ArrayList<UiExportableGroup>();

        Collection<? extends DisplayGroup> lGroups =
                lCacheableFieldsContainer.getDisplayGroups();

        for (DisplayGroup lGroup : lGroups) {
            UiExportableGroup lExportableGroup =
                    new UiExportableGroup(
                            lTranslationManager.getTextTranslation(lGroup.getName()),
                            new ArrayList<UiExportableField>());
            for (FieldRef lFieldRef : lGroup.getFields()) {
                UiExportableField lExportableField =
                        lExportableFields.remove(lFieldRef.getName());
                if (lExportableField != null) {
                    lExportableGroup.getExportableFields().add(lExportableField);
                }
            }
            lExportableGroups.add(lExportableGroup);
        }

        return lExportableGroups;
    }

    /**
     * Execute command and populate {@link OutputStream}.
     * 
     * @param pSession
     *            current user session
     * @param pId
     *            Command to execute Id
     * @param pOutputStream
     *            The stream to populate
     */
    public void getExportedData(final UiSession pSession, final String pId,
            final OutputStream pOutputStream) {
        FacadeCommand lCommand = getFromCache(pSession, pId);
        lCommand.execute(pOutputStream);
        clearCache(pSession, pId);
    }

    /**
     * Get a command from cache
     * 
     * @param pSession
     *            the session
     * @param pId
     *            the command id
     * @return command from cache
     */
    private FacadeCommand getFromCache(final UiSession pSession,
            final String pId) {
        FacadeCommand lCommand =
                getUserCacheManager().getUserCache(pSession.getParent()).getExportCache().get(
                        pId);
        if (lCommand == null) {
            throw new AuthorizationException("Illegal access to the command "
                    + pId + " : the command does not exist in user cache");
        }
        return lCommand;
    }

    /**
     * Import product
     * 
     * @param pSession
     *            Current user session.
     * @param pInputStream
     *            Product data as {@link InputStream}
     * @throws SchemaValidationException
     *             When {@link InputStream} does not match the schema file.
     * @throws ImportException
     *             When Importation fails.
     */
    public void importProduct(final UiSession pSession,
            final InputStream pInputStream) throws SchemaValidationException,
        ImportException {

        ImportProperties lImportProperties = new ImportProperties();
        lImportProperties.setAllFlags(ImportFlag.ERROR);
        lImportProperties.setProductsFlag(ImportFlag.CREATE_OR_UPDATE);
        lImportProperties.setImportFileContent(true);

        getImportService().importData(pSession.getRoleToken(), pInputStream,
                lImportProperties, getContext(pSession));
    }

    /**
     * Get the maximum exportable sheets allowed.
     * 
     * @return the max allowed exportable sheets
     */
    public int getMaxExportableSheets() {
        return getSheetExportService().getMaxExportableSheets();
    }
}

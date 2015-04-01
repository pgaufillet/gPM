/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.extendedaction.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ExtensionAbortException;
import org.topcased.gpm.business.exception.ExtensionException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtendedActionData;
import org.topcased.gpm.business.extensions.service.ExtendedActionResult;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.inputdata.BusinessInputData;
import org.topcased.gpm.business.values.inputdata.impl.cacheable.CacheableInputDataAccess;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.common.extensions.ResultingScreen;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionExtended;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;
import org.topcased.gpm.ui.facade.shared.extendedaction.AbstractUiExtendedActionResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFileEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiMessageEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiSheetCreationEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiSheetsEAResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * ExtendedActionFacade
 * 
 * @author nveillet
 */
public class ExtendedActionFacadeImpl extends AbstractFacade implements
        ExtendedActionFacade {

    /**
     * Add a sheet in cache
     * 
     * @param pSession
     *            the session
     * @param pCacheableInputData
     *            the inputData
     */
    private void addToCache(UiSession pSession,
            CacheableInputData pCacheableInputData) {
        setValuesContainerId(pCacheableInputData);

        getUserCacheManager().getUserCache(pSession.getParent()).getInputDataCache().put(
                pCacheableInputData.getId(), pCacheableInputData);
    }

    /**
     * Clear a input data from cache
     * 
     * @param pSession
     *            the session
     * @param pInputDataId
     *            the input data identifier
     */
    public void clearCache(UiSession pSession, String pInputDataId) {
        getUserCacheManager().getUserCache(pSession.getParent()).getInputDataCache().remove(
                pInputDataId);
    }

    /**
     * Compare two extended actions data.
     * 
     * @param pExtendedActionData1
     *            the first object to compare
     * @param pExtendedActionData2
     *            the snd object to compare
     * @return true if all attributes are equals, false otherwise.
     */
    private boolean compareExtendedActionData(
            final ExtendedActionData pExtendedActionData1,
            final ExtendedActionData pExtendedActionData2) {
        return StringUtils.equals(pExtendedActionData1.getName(),
                pExtendedActionData2.getName())
                && StringUtils.equals(
                        pExtendedActionData1.getExtensionPointName(),
                        pExtendedActionData2.getExtensionPointName())
                && CollectionUtils.isEqualCollection(
                        pExtendedActionData1.getContexts(),
                        pExtendedActionData2.getContexts())
                && StringUtils.equals(pExtendedActionData1.getMenuEntryName(),
                        pExtendedActionData2.getMenuEntryName())
                && StringUtils.equals(
                        pExtendedActionData1.getMenuEntryParentName(),
                        pExtendedActionData2.getMenuEntryParentName())
                && StringUtils.equals(
                        pExtendedActionData1.getExtensionsContainerId(),
                        pExtendedActionData2.getExtensionsContainerId())
                && StringUtils.equals(
                        pExtendedActionData1.getInputDataTypeName(),
                        pExtendedActionData2.getInputDataTypeName());
    }

    /**
     * Convert an extended actions list from business for UI
     * 
     * @param pExtendedActionDatas
     *            the extended actions list from business
     * @return the action for UI
     */
    private List<UiAction> convertExtendedActions(UiSession pSession,
            List<ExtendedActionData> pExtendedActionDatas) {

        final List<UiAction> lActions = new ArrayList<UiAction>();

        for (ExtendedActionData lExtendedActionData : pExtendedActionDatas) {
            String lConfirmationMessage = null;
            if (lExtendedActionData.getConfirmationMessage() != null) {
                lConfirmationMessage =
                        getI18nService().getValueForUser(
                                pSession.getRoleToken(),
                                lExtendedActionData.getConfirmationMessage());
            }
            final UiActionExtended lActionExtended =
                    new UiActionExtended(
                            lExtendedActionData.getMenuEntryName(),
                            lExtendedActionData.getName(),
                            lExtendedActionData.getExtensionsContainerId(),
                            lConfirmationMessage);

            List<String> lMenuParentNames = null;
            if (lExtendedActionData.getMenuEntryParentName() != null) {
                lMenuParentNames =
                        Arrays.asList(StringUtils.split(
                                lExtendedActionData.getMenuEntryParentName(),
                                "."));
            }

            final UiActionWithSubMenu lActionWithSubMenu =
                    getAction(lActions, lMenuParentNames);

            if (lActionWithSubMenu == null) {
                lActions.add(lActionExtended);
            }
            else {
                lActionWithSubMenu.getActions().add(lActionExtended);
            }
        }

        return lActions;
    }

    /**
     * Execute a extended action
     * 
     * @param pSession
     *            the session
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container id
     * @param pInputDataId
     *            the input data identifier
     * @param pInputDataFields
     *            the input data fields
     * @param pSheetId
     *            the sheet identifier
     * @param pSheetIds
     *            the sheet identifiers
     * @param pFilterId
     *            the current filter identifier (when execute on filter result)
     * @param pCurrentSheetUpdatedFields
     *            the current sheet updated fields (when execute on sheet
     *            creation or edition)
     * @param pCurrentLinksUpdatedFields
     *            the current link updated fields
     * @param pCurrentSheetDisplayMode
     *            the current sheet display mode (when execute on sheet creation
     *            or edition)
     * @return the extended action result
     */
    public AbstractUiExtendedActionResult executeExtendedAction(
            UiSession pSession, String pExtendedActionName,
            String pExtensionContainerId, String pInputDataId,
            List<UiField> pInputDataFields, String pSheetId,
            List<String> pSheetIds, String pFilterId,
            List<UiField> pCurrentSheetUpdatedFields,
            Map<String, List<UiField>> pCurrentLinksUpdatedFields,
            DisplayMode pCurrentSheetDisplayMode) {

        InputData lInputData = null;
        if (pInputDataFields != null) {
            CacheableInputData lCacheableInputData =
                    getFromCache(pSession, pInputDataId);

            CacheableInputDataType lCacheableInputDataType =
                    getFieldsService().getCacheableInputDataType(
                            pSession.getRoleToken(),
                            lCacheableInputData.getTypeId(),
                            CacheProperties.IMMUTABLE);

            BusinessInputData lBusinessInputData =
                    new CacheableInputDataAccess(pSession.getRoleToken(),
                            lCacheableInputDataType, lCacheableInputData,
                            ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

            //Container attributes and fields
            initFieldsContainer(lBusinessInputData, pInputDataFields);

            lInputData = new InputData();
            lCacheableInputData.marshal(lInputData);
        }

        // Create Context
        Context lContext =
                getContext(pSession, pSheetId, pSheetIds, pFilterId,
                        pCurrentSheetUpdatedFields, pCurrentLinksUpdatedFields,
                        pCurrentSheetDisplayMode);

        ExtendedActionResult lExtendedActionResult =
                getExtensionsService().executeExtendedAction(
                        pSession.getRoleToken(), pExtendedActionName,
                        pExtensionContainerId, lInputData, lContext);

        // Clear the cache after the extended action execution
        clearCache(pSession, pInputDataId);

        ResultingScreen lResultingScreen =
                ResultingScreen.fromString(lExtendedActionResult.getResultingScreen());

        AbstractUiExtendedActionResult lResult = null;

        if (ResultingScreen.MESSAGE.equals(lResultingScreen)) {
            lResult = new UiMessageEAResult(lExtendedActionResult.getMessage());
        }
        else if (ResultingScreen.ERROR.equals(lResultingScreen)) {
            // an extension exception is thrown to inform about the origin of the error.
            // the cause will be an extensionAbordExcpetion with the user message to display
            throw new ExtensionException(pExtendedActionName,
                    new ExtensionAbortException(
                            lExtendedActionResult.getErrorMessage()));
        }
        else if (ResultingScreen.SHEET_CREATION.equals(lResultingScreen)) {
            if (lExtendedActionResult.getResultSheet() != null) {
                lResult =
                        new UiSheetCreationEAResult(
                                FacadeLocator.instance().getSheetFacade().getSheetFromCacheable(
                                        pSession,
                                        lExtendedActionResult.getResultSheet()));
            }
            else {
                String lSheetTypeName =
                        getSheetService().getCacheableSheetType(
                                pSession.getRoleToken(),
                                lExtendedActionResult.getResultSheetTypeId(),
                                CacheProperties.IMMUTABLE).getName();
                lResult = new UiSheetCreationEAResult(lSheetTypeName);
            }
        }
        else if (ResultingScreen.SHEET_VISUALIZATION.equals(lResultingScreen)) {
            ArrayList<String> lSheetIds = new ArrayList<String>();
            lSheetIds.add(lExtendedActionResult.getResultSheetId());
            lResult =
                    new UiSheetsEAResult(lSheetIds, DisplayMode.VISUALIZATION);
        }
        else if (ResultingScreen.SHEETS_VISUALIZATION.equals(lResultingScreen)) {
            lResult =
                    new UiSheetsEAResult(new ArrayList<String>(
                            lExtendedActionResult.getResultSheetIds()),
                            DisplayMode.VISUALIZATION);
        }
        else if (ResultingScreen.SHEET_EDITION.equals(lResultingScreen)) {
            ArrayList<String> lSheetIds = new ArrayList<String>();
            lSheetIds.add(lExtendedActionResult.getResultSheetId());
            lResult = new UiSheetsEAResult(lSheetIds, DisplayMode.EDITION);
        }
        else if (ResultingScreen.SHEETS_EDITION.equals(lResultingScreen)) {
            lResult =
                    new UiSheetsEAResult(new ArrayList<String>(
                            lExtendedActionResult.getResultSheetIds()),
                            DisplayMode.EDITION);
        }
        else if (ResultingScreen.FILE_OPENING.equals(lResultingScreen)) {
            String lFileId =
                    FacadeLocator.instance().getExportImportFacade().exportFile(
                            pSession,
                            lExtendedActionResult.getResultFileContent());
            String lFileName =
                    "exportFile." + lExtendedActionResult.getResultType();
            lResult =
                    new UiFileEAResult(lFileId, lFileName,
                            lExtendedActionResult.getResultType());
        }
        else if (ResultingScreen.FILTER_RESULT.equals(lResultingScreen)) {
            lResult =
                    FacadeLocator.instance().getFilterFacade().executeFilterExtendedAction(
                            pSession,
                            lExtendedActionResult.getResultProductName(),
                            lExtendedActionResult.getResultSummaryName(),
                            lExtendedActionResult.getResultScope(),
                            lExtendedActionResult.getResultSorterName(),
                            lExtendedActionResult.getResultSheetIds());
        }
        else {
            throw new GDMException(
                    "The result screen of the extended action is unkwown.");
        }

        // Get message whatever the extended action type
        lResult.setResultMessage(lExtendedActionResult.getResultMessage());

        // Need refresh ?
        if (Boolean.TRUE.equals(lExtendedActionResult.getResultRefreshNeeded())) {
        	lResult.setRefreshNeeded(Boolean.TRUE);
        }
        else {
        	lResult.setRefreshNeeded(Boolean.FALSE);
        }

        return lResult;
    }

    /**
     * Get the last action with sub menu of the menu names
     * 
     * @param pActions
     *            the actions list
     * @param pMenuNames
     *            the menu names
     * @return the action with sub menu
     */
    private UiActionWithSubMenu getAction(final List<UiAction> pActions,
            final List<String> pMenuNames) {
        if (pMenuNames != null && !pMenuNames.isEmpty()) {
            final String lActionName = pMenuNames.get(0);

            UiAction lAction = getAction(pActions, lActionName);

            if (lAction == null || lAction instanceof UiActionExtended) {
                lAction =
                        new UiActionWithSubMenu(lActionName,
                                new ArrayList<UiAction>());
                pActions.add(lAction);
            }

            final UiActionWithSubMenu lActionWithSubMenu =
                    (UiActionWithSubMenu) lAction;

            final List<String> lNewMenuNames =
                    new ArrayList<String>(pMenuNames);
            lNewMenuNames.remove(0);

            final UiActionWithSubMenu lParentAction =
                    getAction(lActionWithSubMenu.getActions(), lNewMenuNames);

            if (lParentAction == null) {
                return lActionWithSubMenu;
            }
            return lParentAction;
        }
        return null;
    }

    /**
     * Get an action by name on a list
     * 
     * @param pActions
     *            the actions list
     * @param pActionName
     *            the searched action name
     * @return the action
     */
    private UiAction getAction(final List<UiAction> pActions,
            final String pActionName) {
        for (UiAction lAction : pActions) {
            if (lAction.getName().equals(pActionName)) {
                return lAction;
            }
        }
        return null;
    }

    /**
     * Get the process extended actions
     * 
     * @param pSession
     *            the session
     * @return the process extended actions
     */
    public List<UiAction> getAvailableExtendedActions(final UiSession pSession) {
        String lExtensionsContainerId =
                getInstanceService().getBusinessProcessId(
                        pSession.getParent().getProcessName());

        List<GUIContext> lContexts = Arrays.asList(GUIContext.ALWAYS);

        List<ExtendedActionData> lExtendedActionDatas =
                getExtensionsService().getAvailableExtendedActions(
                        lExtensionsContainerId, lContexts);

        // TODO : Check action access

        return convertExtendedActions(pSession, lExtendedActionDatas);
    }

    /**
     * Get the sheet list extended actions
     * 
     * @param pSession
     *            the session
     * @param pContainerTypes
     *            the sheet type identifiers
     * @return the sheet list extended actions
     */
    public List<UiAction> getAvailableExtendedActions(final UiSession pSession,
            final List<UiFilterContainerType> pContainerTypes) {

        final List<ExtendedActionData> lAvailableExtendedActions =
                new ArrayList<ExtendedActionData>();

        // get extended action of process 
        final List<GUIContext> lContexts = new ArrayList<GUIContext>();
        lContexts.add(GUIContext.SHEET_LIST);
        lAvailableExtendedActions.addAll(getExtensionsService().getAvailableExtendedActions(
                getInstanceService().getBusinessProcessId(
                        pSession.getParent().getProcessName()), lContexts));

        // get extended action of sheet type
        lContexts.add(GUIContext.ALWAYS);

        List<ExtendedActionData> lSheetExtendedActions = null;

        for (UiFilterContainerType lContainerType : pContainerTypes) {
            List<ExtendedActionData> lExtendedActionDatas =
                    getExtensionsService().getAvailableExtendedActions(
                            lContainerType.getId(), lContexts);
            if (lSheetExtendedActions == null) {
                // initialize the list
                lSheetExtendedActions = lExtendedActionDatas;
            }
            else {
                // intersect the lists
                lSheetExtendedActions =
                        getExtendedActionsIntersection(lSheetExtendedActions,
                                lExtendedActionDatas);
            }
        }

        lAvailableExtendedActions.addAll(lSheetExtendedActions);

        // TODO : Check action access

        return convertExtendedActions(pSession, lAvailableExtendedActions);
    }

    /**
     * Get the sheet extended actions
     * 
     * @param pSession
     *            the session
     * @param pSheetTypeId
     *            the sheet type identifier
     * @param pDisplayMode
     *            the display mode
     * @return the sheet extended actions
     */
    public List<UiAction> getAvailableExtendedActions(final UiSession pSession,
            final String pSheetTypeId, final DisplayMode pDisplayMode) {

        final List<ExtendedActionData> lExtendedActionDatas =
                new ArrayList<ExtendedActionData>();

        // get extended action of process 
        final List<GUIContext> lContexts = new ArrayList<GUIContext>();
        switch (pDisplayMode) {
            case CREATION:
                lContexts.add(GUIContext.CTX_CREATE_SHEET);
                break;
            case EDITION:
                lContexts.add(GUIContext.CTX_EDIT_SHEET);
                break;
            default:
                lContexts.add(GUIContext.CTX_VIEW_SHEET);
                break;
        }
        lExtendedActionDatas.addAll(getExtensionsService().getAvailableExtendedActions(
                getInstanceService().getBusinessProcessId(
                        pSession.getParent().getProcessName()), lContexts));

        // get extended action of sheet type
        lContexts.add(GUIContext.ALWAYS);
        lExtendedActionDatas.addAll(getExtensionsService().getAvailableExtendedActions(
                pSheetTypeId, lContexts));

        // TODO : Check action access

        return convertExtendedActions(pSession, lExtendedActionDatas);
    }

    /**
     * get execution context
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the current sheet identifier
     * @param pSheetIds
     *            the selected sheet identifiers
     * @return the execution context
     */
    private Context getContext(final UiSession pSession, final String pSheetId,
            final List<String> pSheetIds) {
        Context lContext = getContext(pSession);

        if (pSheetId != null) {
            lContext.put(ExtensionPointParameters.SHEET_ID, pSheetId);
        }

        List<String> lValuesContainerIds = null;
        if (pSheetIds != null) {
            lValuesContainerIds = new ArrayList<String>(pSheetIds);
        }
        lContext.put(ExtensionPointParameters.SHEET_IDS, lValuesContainerIds);

        lContext.put(ExtensionPointParameters.PRODUCT_NAME,
                pSession.getProductName());
        lContext.put(ExtensionPointParameters.PROCESS_NAME,
                pSession.getParent().getProcessName());

        return lContext;
    }

    /**
     * Create context for execute an extended action
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @param pSheetIds
     *            the list of sheet identifiers
     * @param pFilterId
     *            the filter identifier
     * @param pCurrentSheetUpdatedFields
     *            the current sheet updated fields
     * @param pCurrentLinksUpdatedFields
     *            the current link updated fields
     * @param pCurrentSheetDisplayMode
     *            the current sheet display mode
     * @return the execution context
     */
    private Context getContext(final UiSession pSession, final String pSheetId,
            final List<String> pSheetIds, final String pFilterId,
            final List<UiField> pCurrentSheetUpdatedFields,
            final Map<String, List<UiField>> pCurrentLinksUpdatedFields,
            final DisplayMode pCurrentSheetDisplayMode) {
        Context lContext = getContext(pSession);

        // Sheet info
        String lSheetId = null;
        if (pCurrentSheetDisplayMode == null
                || DisplayMode.EDITION.equals(pCurrentSheetDisplayMode)) {
            lSheetId = pSheetId;
        }
        lContext.put(ExtensionPointParameters.SHEET_ID, lSheetId);

        // Sheets info
        List<String> lValuesContainerIds = null;
        if (pSheetIds != null) {
            lValuesContainerIds = new ArrayList<String>(pSheetIds);
        }
        lContext.put(ExtensionPointParameters.SHEET_IDS, lValuesContainerIds);

        // Filter info
        String lFilterName = null;
        FilterScope lFilterScope = null;
        if (pFilterId != null) {
            UiFilter lFilter =
                    FacadeLocator.instance().getFilterFacade().getFilter(
                            pSession, pFilterId);
            lFilterName = lFilter.getName();
            switch (lFilter.getVisibility()) {
                case INSTANCE:
                    lFilterScope = FilterScope.INSTANCE_FILTER;
                    break;
                case PRODUCT:
                    lFilterScope = FilterScope.PRODUCT_FILTER;
                    break;
                default:
                    lFilterScope = FilterScope.USER_FILTER;
                    break;
            }
        }
        lContext.put(ExtensionsService.FILTER_NAME, lFilterName);
        lContext.put(ExtensionsService.FILTER_SCOPE, lFilterScope);
        lContext.put(ExtensionsService.FILTER_PRODUCT_NAME,
                pSession.getProductName());

        //Add the sheet context parameter 
        lContext.put(ExtensionPointParameters.VALUES_CONTAINER, null);

        // Current sheet info
        if (pCurrentSheetDisplayMode != null) {
            CacheableSheet lUpdatedCacheableSheet =
                    FacadeLocator.instance().getSheetFacade().getUpdatedCacheableSheet(
                            pSession, pSheetId, pCurrentSheetUpdatedFields,
                            pCurrentSheetDisplayMode);
            lContext.put("currentEditedSheet", lUpdatedCacheableSheet);
        }

        // Current update links
        if (pCurrentSheetDisplayMode != null) {
            List<CacheableLink> lUpdatedCacheableLinks =
                    new ArrayList<CacheableLink>();
            for (Entry<String, List<UiField>> lEntry : pCurrentLinksUpdatedFields.entrySet()) {
                CacheableLink lCacheableLink =
                        FacadeLocator.instance().getLinkFacade().getUpdatedCacheableLink(
                                pSession, lEntry.getKey(), lEntry.getValue(),
                                pCurrentSheetDisplayMode);
                lUpdatedCacheableLinks.add(lCacheableLink);
            }
            // Put update Link list in the context
            lContext.put("currentEditedLinks", lUpdatedCacheableLinks);
        }
        return lContext;
    }

    /**
     * Get the intersection of two extended actions list
     * 
     * @param pExtendedActionDatas1
     *            The global extendedActionList (reference)
     * @param pExtendedActionDatas2
     *            The list to intersect
     * @return the intersection of two extended actions list
     */
    private List<ExtendedActionData> getExtendedActionsIntersection(
            final List<ExtendedActionData> pExtendedActionDatas1,
            final List<ExtendedActionData> pExtendedActionDatas2) {

        final List<ExtendedActionData> lExtendedActions =
                new ArrayList<ExtendedActionData>();
        if (pExtendedActionDatas2 != null) {
            for (ExtendedActionData lExtendedActionData : pExtendedActionDatas2) {
                if (isContainedInList(pExtendedActionDatas1,
                        lExtendedActionData)) {
                    lExtendedActions.add(lExtendedActionData);
                }
            }
        }
        return lExtendedActions;
    }

    /**
     * Get a input data from cache
     * 
     * @param pSession
     *            the session
     * @param pInputDataId
     *            the input data id
     * @return input data from cache
     */
    private CacheableInputData getFromCache(UiSession pSession,
            String pInputDataId) {
        CacheableInputData lCacheableInputData =
                getUserCacheManager().getUserCache(pSession.getParent()).getInputDataCache().get(
                        pInputDataId);
        if (lCacheableInputData == null) {
            throw new AuthorizationException(
                    "Illegal access to the input data " + pInputDataId
                            + " : the input data does not exist in user cache");
        }
        return lCacheableInputData;
    }

    /**
     * Get the input data of an extended action
     * 
     * @param pSession
     *            the session
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container id
     * @param pSheetId
     *            the current sheet identifier
     * @param pSheetIds
     *            the selected sheet identifiers
     * @return the input data
     */
    public UiInputData getInputData(final UiSession pSession,
            final String pExtendedActionName, String pExtensionContainerId,
            final String pSheetId, final List<String> pSheetIds) {

        String lInputDataTypeName =
                getExtensionsService().getExtendedAction(pExtensionContainerId,
                        pExtendedActionName).getInputDataTypeName();

        CacheableInputDataType lCacheableInputDataType =
                getFieldsService().getCacheableInputDataTypeByName(
                        pSession.getRoleToken(),
                        lInputDataTypeName,
                        pSession.getParent().getProcessName(),
                        new CacheProperties(false,
                                CacheProperties.ACCESS_CONTROL_NOT_USED));
        Context lContext = getContext(pSession, pSheetId, pSheetIds);
        CacheableInputData lCacheableInputData =
                getFieldsService().getInputDataModel(pSession.getRoleToken(),
                        lCacheableInputDataType, lContext);

        addToCache(pSession, lCacheableInputData);

        BusinessInputData lBusinessInputData =
                new CacheableInputDataAccess(pSession.getRoleToken(),
                        lCacheableInputDataType, lCacheableInputData,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        if (lBusinessInputData.isConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + lBusinessInputData.getId()
                    + " : the access is confidential ");
        }

        UiInputData lInputData = new UiInputData();

        // Get translation manager
        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        Map<String, List<CategoryValue>> lCategoryCache =
                new HashMap<String, List<CategoryValue>>();

        //Container attributes and fields
        initUiContainer(lInputData, lBusinessInputData, pSession,
                DisplayMode.CREATION, lTranslationManager, lCategoryCache,
                lContext);

        //Adding groups
        for (BusinessFieldGroup lBusinessFieldGroup : lBusinessInputData.getFieldGroups()) {
            lInputData.addFieldGroup(new UiFieldGroup(
                    lTranslationManager.getTextTranslation(lBusinessFieldGroup.getGroupName()),
                    lBusinessFieldGroup.getFieldNames(),
                    lBusinessFieldGroup.isOpen()));
        }

        // Translate Type Name
        lInputData.setTypeName(lTranslationManager.getTextTranslation(lInputData.getTypeName()));

        return lInputData;
    }

    /**
     * Get if the extended action has an input data
     * 
     * @param pSession
     *            the session
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container id
     * @return true if the extended action has an input data, otherwise false
     */
    public boolean hasInputData(UiSession pSession, String pExtendedActionName,
            String pExtensionContainerId) {
        return getExtensionsService().getExtendedAction(pExtensionContainerId,
                pExtendedActionName).getInputDataTypeName() != null;
    }

    /**
     * Check if an extendedActionData is contained in a extendedActionDataList.
     * 
     * @param pExtendedActionDatas
     *            the list
     * @param pExtendedActionData
     *            the searched extended action data
     * @return true if the extendedActionData is present in list, false
     *         otherwise.
     */
    private boolean isContainedInList(
            final List<ExtendedActionData> pExtendedActionDatas,
            final ExtendedActionData pExtendedActionData) {
        if (pExtendedActionDatas != null) {
            for (ExtendedActionData lExtendedActionDataInList : pExtendedActionDatas) {
                if (compareExtendedActionData(lExtendedActionDataInList,
                        pExtendedActionData)) {
                    return true;
                }
            }
        }
        return false;
    }
}

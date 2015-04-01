/**************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.sheet.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockScopeEnumeration;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.util.AttachmentInError;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.server.cache.UserCache;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.container.sheet.state.UiTransition;
import org.topcased.gpm.ui.facade.shared.exception.FacadeAttachmentException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeAttachmentTotalSizeException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeEmptyAttachmentException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeInvalidCharacterException;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;

/**
 * SheetFacade
 * 
 * @author nveillet
 */
public class SheetFacadeImpl extends AbstractFacade implements SheetFacade {

    /**
     * Add a sheet in cache
     * 
     * @param pSession
     *            the session
     * @param pCacheableSheet
     *            the sheet
     */
    private void addToCache(UiSession pSession, CacheableSheet pCacheableSheet) {
        setValuesContainerId(pCacheableSheet);

        setTemporaryFunctionalReference(pSession, pCacheableSheet);

        getUserCacheManager().getUserCache(pSession.getParent()).getSheetCache().put(
                pCacheableSheet.getId(), pCacheableSheet);
    }

    /**
     * Changes sheet state over transition.
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Sheet id to perform transition on
     * @param pTransitionName
     *            Transition name to perform
     */
    public void changeState(UiSession pSession, String pSheetId,
            String pTransitionName) {
    	CacheableSheet lSheet = getSheetService().getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE);
    	String lTypeName = 
    			lSheet.getTypeName();
    	String lSheetRef =
    			lSheet.getFunctionalReference();
    	getSheetService().changeState(pSession.getRoleToken(), pSheetId,
                pTransitionName, getContext(pSession));
        // Log
        gpmLogger.lowInfo(pSession.getParent().getLogin(), GPMActionLogConstants.SHEET_STATUS, 
        		lTypeName, pSession.getProductName(), lSheetRef);
    }

    /**
     * Clear a sheet from cache
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     */
    public void clearCache(UiSession pSession, String pSheetId) {
        final UserCache lUserCache =
                getUserCacheManager().getUserCache(pSession.getParent());
        if (lUserCache != null) {
            final Map<String, CacheableSheet> lSheetCache =
                    lUserCache.getSheetCache();
            if (lSheetCache != null) {
                lSheetCache.remove(pSheetId);
            }
        }
    }

    /**
     * Create sheet in database
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Sheet to create
     * @param pFieldsList
     *            the fields to create
     */
    public void createSheet(UiSession pSession, String pSheetId,
            List<UiField> pFieldsList) {

        CacheableSheet lCacheableSheet = getFromCache(pSession, pSheetId);

        List<UiField> lBackUp =
                copyFieldList(pSession, lCacheableSheet, pFieldsList);

        fillCacheableSheet(pSession, lCacheableSheet, pFieldsList);

        try {
            getSheetService().createSheet(pSession.getRoleToken(),
                            lCacheableSheet, getContext(pSession));
        }
        catch (GDMException ex) {
            restoreFieldList(pSession, lCacheableSheet, lBackUp);
            throw ex;
        }

        clearCache(pSession, pSheetId);
    }

    /**
     * Delete sheet in database
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Id of the sheet to delete
     */
    public void deleteSheet(UiSession pSession, String pSheetId) {
        CacheableSheet lCacheableSheet = getSheetService().getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE);
        
        getSheetService().deleteSheet(pSession.getRoleToken(), pSheetId,
                getContext(pSession));
        
        gpmLogger.lowInfo(pSession.getParent().getLogin(), GPMActionLogConstants.SHEET_DELETION, 
                lCacheableSheet.getTypeName(), lCacheableSheet.getProductName(),
                lCacheableSheet.getFunctionalReference());
    }

    /**
     * Lock a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @param pLockType
     *            the lock type
     * @param pDisplayMode
     *            the display mode
     */
    private void doLockSheet(UiSession pSession, String pSheetId,
            LockTypeEnumeration pLockType, DisplayMode pDisplayMode) {
        if (LockTypeEnumeration.READ_WRITE.equals(pLockType)
                || (LockTypeEnumeration.WRITE.equals(pLockType) && DisplayMode.EDITION.equals(pDisplayMode))) {
            getSheetService().lockSheet(
                    pSession.getRoleToken(),
                    pSheetId,
                    new LockProperties(pLockType,
                            LockScopeEnumeration.USER_SESSION));
        }
    }

    /**
     * Duplicate sheet
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Id of the sheet to duplicate
     * @return the duplicated sheet
     */
    public UiSheet duplicateSheet(UiSession pSession, String pSheetId) {
        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheetDuplicationModel(
                        pSession.getRoleToken(), pSheetId);

        addToCache(pSession, lCacheableSheet);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        lCacheableSheet.getCurrentStateName(),
                        lCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        CacheProperties.ACCESS_CONTROL_NOT_USED));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(
                        pSession.getRoleToken(), lCacheableSheet.getTypeName(),
                        lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(pSession.getRoleToken(),
                        lCacheableSheetType, lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiSheet(pSession, lBusinessSheet, DisplayMode.CREATION);
    }

    /**
     * Restore values in cacheable sheet.
     * 
     * @param pSession
     *            Current user session
     * @param pCacheableSheet
     *            The cacheable sheet
     * @param pFieldsList
     *            The field list to restore
     */
    private void restoreFieldList(UiSession pSession,
            CacheableSheet pCacheableSheet, List<UiField> pFieldsList) {
        String lRoleToken = pSession.getRoleToken();

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        pCacheableSheet.getCurrentStateName(),
                        pCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        pCacheableSheet.getId()));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(lRoleToken,
                        pCacheableSheet.getTypeName(), lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        pCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        for (BusinessField lBusinessField : pFieldsList) {
            lBusinessSheet.getField(lBusinessField.getFieldName()).copy(
                    lBusinessField);
        }
    }

    /**
     * Retrieve fields from cacheable sheet.
     * 
     * @param pSession
     *            Current user session
     * @param pCacheableSheet
     *            The cacheable sheet
     * @param pFieldsList
     *            The field list to retrieve.
     * @return The field list retrieved from cacheable sheet.
     */
    private List<UiField> copyFieldList(UiSession pSession,
            CacheableSheet pCacheableSheet, List<UiField> pFieldsList) {
        List<UiField> lCopyFields = new ArrayList<UiField>();
        String lRoleToken = pSession.getRoleToken();

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        pCacheableSheet.getCurrentStateName(),
                        pCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        pCacheableSheet.getId()));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(lRoleToken,
                        pCacheableSheet.getTypeName(), lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        pCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_ONLY);

        for (UiField lUIField : pFieldsList) {
            UiField lCopy = lUIField.getEmptyClone();
            lCopy.copy(lBusinessSheet.getField(lUIField.getFieldName()));
            lCopyFields.add(lCopy);
        }
        return lCopyFields;
    }

    /**
     * fill cacheable sheet with field list given
     * 
     * @param pSession
     *            Current user session
     * @param pCacheableSheet
     *            The cacheable sheet
     * @param pFieldsList
     *            The field list to fill
     */
    private void fillCacheableSheet(UiSession pSession,
            CacheableSheet pCacheableSheet, List<UiField> pFieldsList) {

        String lRoleToken = pSession.getRoleToken();

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        pCacheableSheet.getCurrentStateName(),
                        pCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        pCacheableSheet.getId()));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(lRoleToken,
                        pCacheableSheet.getTypeName(), lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        pCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        //Container attributes and fields
        initFieldsContainer(lBusinessSheet, pFieldsList);
    }

    /**
     * Get the available transitions of a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheet
     *            the sheet
     * @return the available transitions
     */
    public List<String> getAvailableTransitions(UiSession pSession,
            UiSheet pSheet) {
        return Arrays.asList(getLifeCycleService().getProcessInstanceInformation(
                pSession.getRoleToken(), pSheet.getId()).getTransitions());
    }

    /**
     * Get the confirmation messages for each transitions
     * 
     * @param pSession
     *            the session
     * @param pSheet
     *            the sheet
     * @return a map containing for each transition name the corresponding
     *         confirmation message
     */
    public Map<String, String> getTransitionConfirmationMessages(
            UiSession pSession, UiSheet pSheet) {
        return getLifeCycleService().getProcessInstanceInformation(
                pSession.getRoleToken(), pSheet.getId()).getTransitionConfirmationMessages();
    }

    /**
     * get execution context
     * 
     * @param pSession
     *            the session
     * @param pSheet
     *            the sheet
     * @param pDisplayMode
     *            the sheet display mode
     * @return the execution context
     */
    private Context getContext(UiSession pSession, BusinessSheet pSheet,
            DisplayMode pDisplayMode) {
        Context lContext = getContext(pSession);

        if (DisplayMode.EDITION.equals(pDisplayMode)) {
            lContext.put(ExtensionPointParameters.SHEET_ID, pSheet.getId());
            lContext.put(ExtensionPointParameters.FIELDS_CONTAINER_ID,
                    pSheet.getTypeId());
            lContext.put(ExtensionPointParameters.FIELDS_CONTAINER_NAME,
                    pSheet.getTypeName());
            lContext.put(ExtensionPointParameters.PRODUCT_NAME,
                    pSheet.getProductName());
            lContext.put(ExtensionPointParameters.PROCESS_NAME,
                    pSession.getParent().getProcessName());
        }

        return lContext;
    }

    /**
     * Get creatable sheet types
     * 
     * @param pSession
     *            the session
     * @return the creatable sheet type names
     */
    public List<String> getCreatableSheetTypes(UiSession pSession) {
        List<CacheableFieldsContainer> lCacheableSheetTypes =
                getFieldsContainerService().getFieldsContainer(
                        pSession.getRoleToken(),
                        SheetType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.CREATE);

        ArrayList<String> lSheetTypeNames =
                new ArrayList<String>(lCacheableSheetTypes.size());
        for (CacheableFieldsContainer lCacheableSheetType : lCacheableSheetTypes) {
            lSheetTypeNames.add(lCacheableSheetType.getName());
        }

        Collections.sort(lSheetTypeNames);

        return lSheetTypeNames;
    }

    /**
     * Get a sheet from cache
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet id
     * @return sheet from cache
     */
    private CacheableSheet getFromCache(UiSession pSession, String pSheetId) {
        CacheableSheet lCacheableSheet =
                getUserCacheManager().getUserCache(pSession.getParent()).getSheetCache().get(
                        pSheetId);
        if (lCacheableSheet == null) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pSheetId + " : the sheet does not exist in user cache");
        }
        return lCacheableSheet;
    }

    /**
     * Get sheet's identifier
     * 
     * @param pSession
     *            User session
     * @param pSheetRef
     *            Reference of the sheet
     * @return Identifier of the sheet if found, null otherwise
     */
    public String getId(UiSession pSession, String pSheetRef) {
        String lRoleToken = pSession.getRoleToken();

        final String lSheetId =
                getSheetService().getSheetIdByReference(lRoleToken, pSheetRef);
        return lSheetId;
    }

    /**
     * Get initializable sheet types
     * 
     * @param pSession
     *            the session
     * @param pSheetTypeId
     *            the sheet type id
     * @return the initializable sheet type names
     */
    public List<String> getInitializableSheetTypes(UiSession pSession,
            String pSheetTypeId) {
        List<String> lInitializableSheetTypeIds =
                getSheetService().getLinkableSheetTypes(pSheetTypeId);

        List<String> lInitializableSheetTypeNames =
                new ArrayList<String>(lInitializableSheetTypeIds.size());

        CacheableSheetType lCacheableSheetType;

        for (String lInitializableSheetTypeId : lInitializableSheetTypeIds) {
            lCacheableSheetType =
                    getSheetService().getCacheableSheetType(
                            pSession.getRoleToken(), lInitializableSheetTypeId,
                            CacheProperties.IMMUTABLE);

            if (!getAuthorizationService().getSheetAccessControl(
                    pSession.getRoleToken(),
                    new AccessControlContextData(pSession.getProductName(),
                            pSession.getRoleName(),
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            lInitializableSheetTypeId,
                            CacheProperties.ACCESS_CONTROL_NOT_USED,
                            CacheProperties.ACCESS_CONTROL_NOT_USED),
                    lInitializableSheetTypeId).getConfidential()) {
                lInitializableSheetTypeNames.add(lCacheableSheetType.getName());
            }
        }

        Collections.sort(lInitializableSheetTypeNames);

        return lInitializableSheetTypeNames;
    }

    /**
     * Get the process name of a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @return the process name
     */
    public String getProcessName(UiUserSession pSession, String pSheetId) {
        validateSheetId(pSession, pSheetId);
        return getSheetService().getProcessName(pSession.getToken(), pSheetId);
    }

    /**
     * Get the product name of a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @return the product name
     */
    public String getProductName(UiUserSession pSession, String pSheetId) {
        validateSheetId(pSession, pSheetId);
        return getSheetService().getProductName(pSession.getToken(), pSheetId);
    }

    /**
     * Get a sheet by its Id
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            The sheet Id
     * @param pDisplayMode
     *            display mode
     * @return the sheet
     */
    public UiSheet getSheet(UiSession pSession, String pSheetId,
            DisplayMode pDisplayMode) {

        validateSheetId(pSession.getParent(), pSheetId);

        String lRoleToken = pSession.getRoleToken();

        CacheableSheet lCacheableSheet = null;
        if (DisplayMode.VISUALIZATION.name().equals(pDisplayMode.name())) {
            lCacheableSheet =
                    getSheetService().getCacheableSheet(lRoleToken, pSheetId,
                            CacheProperties.IMMUTABLE);
        }
        else {
            lCacheableSheet =
                    getSheetService().getCacheableSheet(lRoleToken, pSheetId,
                            CacheProperties.MUTABLE);
        }

        // Lock Sheet
        lockSheet(pSession, lCacheableSheet.getTypeId(), pSheetId, pDisplayMode);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        lCacheableSheet.getCurrentStateName(),
                        lCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        lCacheableSheet.getId()));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(lRoleToken,
                        lCacheableSheet.getTypeId(), lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        // if the user cannot update the sheet the lock is removed
        if (!lBusinessSheet.isUpdatable()) {
            unLockSheet(pSession, pSheetId);
        }

        return getUiSheet(pSession, lBusinessSheet, pDisplayMode);
    }

    /**
     * Get empty sheet of given Type
     * 
     * @param pSession
     *            Current user session
     * @param pSheetTypeName
     *            Sheet type name
     * @return the empty sheet
     */
    public UiSheet getSheetByType(UiSession pSession, String pSheetTypeName) {

        String lRoleToken = pSession.getRoleToken();

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(lRoleToken,
                        pSheetTypeName, CacheProperties.IMMUTABLE);

        // if the CacheableSheetType was not found 
        if (null == lCacheableSheetType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pSheetTypeName);
        }

        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheetModel(lRoleToken,
                        lCacheableSheetType, pSession.getProductName(),
                        getContext(pSession));

        addToCache(pSession, lCacheableSheet);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        lCacheableSheet.getCurrentStateName(),
                        lCacheableSheetType.getId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        CacheProperties.ACCESS_CONTROL_NOT_USED));

        lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(lRoleToken,
                        pSheetTypeName, lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiSheet(pSession, lBusinessSheet, DisplayMode.CREATION);
    }

    /**
     * Get a sheet by the cacheable sheet for the extended action result in
     * sheet creation mode
     * 
     * @param pSession
     *            Current user session
     * @param pSheet
     *            The cacheable sheet
     * @return the sheet
     */
    public UiSheet getSheetFromCacheable(UiSession pSession,
            CacheableSheet pSheet) {

        String lRoleToken = pSession.getRoleToken();

        addToCache(pSession, pSheet);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        pSheet.getCurrentStateName(), pSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        CacheProperties.ACCESS_CONTROL_NOT_USED));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(lRoleToken,
                        pSheet.getTypeId(), lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        pSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiSheet(pSession, lBusinessSheet, DisplayMode.CREATION);
    }

    /**
     * Get the sheet type name from the identifier
     * 
     * @param pSession
     *            the session
     * @param pSheetTypeId
     *            the sheet type identifier
     * @return the sheet type name
     */
    public String getSheetTypeName(UiSession pSession, String pSheetTypeId) {
        return getSheetService().getCacheableSheetType(pSession.getRoleToken(),
                pSheetTypeId, CacheProperties.IMMUTABLE).getName();
    }

    private UiSheet getUiSheet(UiSession pSession,
            BusinessSheet pBusinessSheet, DisplayMode pDisplayMode) {

        if (pBusinessSheet.isConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pBusinessSheet.getId() + " : the access is confidential ");
        }

        UiSheet lSheet = new UiSheet();

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        Context lContext = getContext(pSession, pBusinessSheet, pDisplayMode);

        Map<String, List<CategoryValue>> lCategoryCache =
                new HashMap<String, List<CategoryValue>>();

        //Container attributes and fields
        initUiContainer(lSheet, pBusinessSheet, pSession, pDisplayMode,
                lTranslationManager, lCategoryCache, lContext);

        //Sheet attributes
        lSheet.setProductName(pBusinessSheet.getProductName());
        lSheet.setFunctionalReference(pBusinessSheet.getFunctionalReference());
        lSheet.setState(lTranslationManager.getTextTranslation(pBusinessSheet.getState()));

        //Adding groups
        for (String lGroupName : pBusinessSheet.getFieldGroupNames()) {
            BusinessFieldGroup lBusinessFieldGroup =
                    pBusinessSheet.getFieldGroup(lGroupName);
            lSheet.addFieldGroup(new UiFieldGroup(
                    lTranslationManager.getTextTranslation(lGroupName),
                    lBusinessFieldGroup.getFieldNames(),
                    lBusinessFieldGroup.isOpen()));
        }

        // Version
        // TODO : Add version to the BusinessSheet interface
        lSheet.setVersion(((CacheableSheetAccess) pBusinessSheet).read().getVersion());

        // convert transition historic
        // TODO : Add transition historic to the BusinessSheet interface
        ArrayList<UiTransition> lTransitionHistoric =
                new ArrayList<UiTransition>();
        for (TransitionHistoryData lTransition : ((CacheableSheetAccess) pBusinessSheet).read().getTransitionsHistory()) {
            lTransitionHistoric.add(new UiTransition(
                    lTranslationManager.getTextTranslation(lTransition.getOriginState()),
                    lTranslationManager.getTextTranslation(lTransition.getDestinationState()),
                    lTransition.getLogin(), lTransition.getTransitionDate()));
        }
        lSheet.setTransitionHistoric(lTransitionHistoric);

        // Lock
        if (pBusinessSheet.getId() != null) {
            Lock lLock =
                    getSheetService().getLock(pSession.getRoleToken(),
                            pBusinessSheet.getId());
            if (lLock != null) { // Lock exists
                String lLockLogin = lLock.getOwner();
                String lUserLogin = pSession.getParent().getLogin();
                // If login not case sensitive > Send to lower case before comparison
                if (!getAuthorizationService().isLoginCaseSensitive()) {
                    lLockLogin = lLockLogin.toLowerCase();
                    lUserLogin = lUserLogin.toLowerCase();
                }
                if (!lLockLogin.equals(lUserLogin)) {
                    lSheet.setLockUserLogin(lLock.getOwner());
                    if (!getAuthorizationService().hasGlobalAdminRole(
                            pSession.getRoleToken())) {
                        lSheet.setDeletable(false);
                        lSheet.setUpdatable(false);
                    }
                }
            }
        }

        return lSheet;
    }

    /**
     * Get cacheable sheet with updated fields for extended action context
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @param pFields
     *            the fields
     * @param pDisplayMode
     *            the display mode
     * @return the cacheableSheet
     */
    public CacheableSheet getUpdatedCacheableSheet(final UiSession pSession,
            final String pSheetId, final List<UiField> pFields,
            final DisplayMode pDisplayMode) {

        CacheableSheet lCacheableSheet;
        switch (pDisplayMode) {
            case CREATION:
                lCacheableSheet = getFromCache(pSession, pSheetId);
                lCacheableSheet.setId(null);
                break;
            default:
                lCacheableSheet =
                        getSheetService().getCacheableSheet(
                                pSession.getRoleToken(), pSheetId,
                                CacheProperties.MUTABLE);
                break;
        }

        fillCacheableSheet(pSession, lCacheableSheet, pFields);

        return lCacheableSheet;
    }

    /**
     * Initialize a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @param pSourceSheetId
     *            the source sheet identifier
     * @return a initialized sheet
     */
    public UiSheet initializeSheet(UiSession pSession, String pSheetId,
            String pSourceSheetId) {

        String lRoleToken = pSession.getRoleToken();

        String lSheetTypeId = getFromCache(pSession, pSheetId).getTypeId();

        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheetInitializationModel(
                        lRoleToken, lSheetTypeId, pSourceSheetId);

        // Set the id
        lCacheableSheet.setId(pSheetId);

        // Set the product name
        lCacheableSheet.setProductName(pSession.getProductName());

        addToCache(pSession, lCacheableSheet);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        lCacheableSheet.getCurrentStateName(),
                        lCacheableSheet.getTypeId(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        CacheProperties.ACCESS_CONTROL_NOT_USED));

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetTypeByName(
                        pSession.getRoleToken(), lCacheableSheet.getTypeName(),
                        lProperties);

        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(pSession.getRoleToken(),
                        lCacheableSheetType, lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        return getUiSheet(pSession, lBusinessSheet, DisplayMode.CREATION);
    }

    /**
     * Lock a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetTypeId
     *            the sheet type identifier
     * @param pSheetId
     *            the sheet identifier
     * @param pDisplayMode
     *            the display mode
     */
    private void lockSheet(UiSession pSession, String pSheetTypeId,
            String pSheetId, DisplayMode pDisplayMode) {

        // Unlock before lock
        unLockSheet(pSession, pSheetId);

        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        pSession.getRoleToken(), pSheetTypeId,
                        CacheProperties.IMMUTABLE);

        if (lCacheableSheetType.isAutolocking()) {
            //For administrator, if the sheet is already locked by another user,
            // he doesn't have to take the lock.
            if (getAuthorizationService().hasGlobalAdminRole(
                    pSession.getRoleToken())) {
                Lock lLock =
                        getSheetService().getLock(pSession.getRoleToken(),
                                pSheetId);
                if (lLock == null
                        || pSession.getParent().getLogin().equals(
                                lLock.getOwner())) {
                    doLockSheet(pSession, pSheetId,
                            lCacheableSheetType.getLockType(), pDisplayMode);
                }
            }
            else {
                doLockSheet(pSession, pSheetId,
                        lCacheableSheetType.getLockType(), pDisplayMode);
            }
        }
    }

    /**
     * Unlock a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     */
    public void unLockSheet(UiSession pSession, String pSheetId) {
        Lock lLock =
                getSheetService().getLock(pSession.getRoleToken(), pSheetId);

        // Unlock before lock
        if (lLock != null) {
            if (ServiceLocator.instance().getAuthorizationService().isLoginCaseSensitive()) {
                if (pSession.getParent().getLogin().equals(lLock.getOwner())) {
                    getSheetService().removeLock(pSession.getRoleToken(),
                            pSheetId);
                }
            }
            else {
                if (pSession.getParent().getLogin().equalsIgnoreCase(
                        lLock.getOwner())) {
                    getSheetService().removeLock(pSession.getRoleToken(),
                            pSheetId);
                }
            }
        }
    }

    /**
     * Update sheet in database
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Sheet to update
     * @param pVersion
     *            Sheet version
     * @param pFields
     *            the fields to update
     */
    public void updateSheet(UiSession pSession, String pSheetId, int pVersion,
            List<UiField> pFields) {

        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pSession.getRoleToken(),
                        pSheetId, CacheProperties.MUTABLE);

        List<UiField> lBackUp =
                copyFieldList(pSession, lCacheableSheet, pFields);

        fillCacheableSheet(pSession, lCacheableSheet, pFields);

        lCacheableSheet.setVersion(pVersion);

        try {
            getSheetService().updateSheet(pSession.getRoleToken(),
                    lCacheableSheet, getContext(pSession));
            
            // Log
            gpmLogger.lowInfo(pSession.getParent().getLogin(), GPMActionLogConstants.SHEET_UPDATE, 
                    lCacheableSheet.getTypeName(), lCacheableSheet.getProductName(), 
                    lCacheableSheet.getFunctionalReference());
        }
        catch (GDMException ex) {
            restoreFieldList(pSession, lCacheableSheet, lBackUp);
            throw ex;
        }
    }

    /**
     * Check if a sheet exist in database
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     */
    private void validateSheetId(UiUserSession pSession, String pSheetId) {
        if (!getSheetService().isSheetIdExist(pSession.getToken(), pSheetId)) {
            throw new InvalidIdentifierException(pSheetId);
        }
    }

    /**
     * This method will return true is the sheet is not lock
     * 
     * @param pSession
     *            the current session
     * @param pSheetId
     *            the id of the sheet to display
     * @param pDisplayMode
     *            the display mode wished of the sheet
     * @return a boolean
     */
    @Override
    public boolean isSheetLocked(UiSession pSession, String pSheetId,
            DisplayMode pDisplayMode) {

        return getSheetService().isSheetLocked(pSession.getRoleToken(),
                pSheetId, pDisplayMode);
    }

    /**
     * Get the error attached field list (convert it to strings)
     * 
     * @param pSheetId
     *            The Sheet Id containing the list
     * @return the error attached field and their content
     */
    @Override
    public List<FacadeAttachmentException> getAndAcknowledgeAttachedFilesInError(String pSheetId) {
    	List<AttachmentInError> lErrors = getSheetService().getAndAcknowledgeAttachedFilesInError(pSheetId);
    	if (lErrors == null || lErrors.isEmpty()) {
    		return null;
    	}
    	List<FacadeAttachmentException> lExceptionList = new ArrayList<FacadeAttachmentException>();
    	for (AttachmentInError lError : lErrors) {
    		switch (lError.getErrorStatus()) {
    		case ZERO_SIZE :
    			lExceptionList.add(new FacadeEmptyAttachmentException(lError.getItem(0).toString()));
    			break;
    		case EXCEEDED_SIZE :
    			lExceptionList.add(new FacadeAttachmentTotalSizeException(((Integer) lError.getItem(0)).intValue()));
    			break;
    		case INVALID_NAME :
    			lExceptionList.add(new FacadeInvalidCharacterException(lError.getItem(0).toString()));
    			break;
    		default : // Should not happen
    			break;
    		}
    	}
    	return lExceptionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSheetTypeIdBySheetId(final String pSheetId) {
        return getSheetService().getSheetTypeIdBySheetId(pSheetId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getSheetTypeIdBySheetIds(final List<String> pSheetIds) {
        return getSheetService().getSheetTypeIdBySheetIds(pSheetIds);
    }
}

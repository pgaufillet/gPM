/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.sheet;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.lifecycle.impl.LifeCycleServiceImpl;
import org.topcased.gpm.business.revision.impl.RevisionServiceImpl;
import org.topcased.gpm.business.serialization.data.RuleData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.TransitionData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.business.util.AttachmentUtils;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * SheetImportManager handles importation for sheet.
 * 
 * @author mkargbo
 */
public class SheetImportManager
extends
AbstractValuesContainerImportManager<SheetData, CacheableSheet, CacheableSheetType> {

    /** CANNOT_IMPORT_SHEET */
    private static final String CANNOT_IMPORT_SHEET =
        "Raisons can be: 1. No rights to set the transition history "
        + "(You cannot perform the last transition.";

    /** CANNOT_PERFORM_TRANSITIONS */
    private static final String CANNOT_PERFORM_TRANSITIONS =
        "Cannot perform transitions.";

    private SheetServiceImpl sheetServiceImpl;

    private RevisionServiceImpl revisionServiceImpl;

    private LifeCycleServiceImpl lifeCycleServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @throws ImportException
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected boolean canImport(String pRoleToken, SheetData pElement,
            final String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        boolean lCanImport =
            super.canImport(pRoleToken, pElement, pElementId, pProperties,
                    pReport);
        //Can import sheet only if this user can perform the last transition.
        // NECESSARY FOR MIGRATION NEED: Skip verification for global admin 
        String lTypeId =
            fieldsContainerServiceImpl.getFieldsContainerId(pRoleToken,
                    pElement.getType());
        if (!authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)
                && !lifeCycleServiceImpl.canPerformTransitionHistory(
                        pRoleToken, lTypeId, pElement.getTransitionsHistory())) {
            onFailure(pElement, pProperties, pReport,
                    CANNOT_PERFORM_TRANSITIONS);
            return false;
        }
        return lCanImport;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fill stateName attribute.
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#getAccessControlContext(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    @Override
    protected AccessControlContextData getAccessControlContext(
            String pRoleToken, SheetData pElement) {
        final AccessControlContextData lAccessControlContext =
            super.getAccessControlContext(pRoleToken, pElement);
        lAccessControlContext.setStateName(pElement.getState());
        return lAccessControlContext;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createElement(String pRoleToken,
            CacheableSheet pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
    	AttachmentUtils.correctAttachmentNames(pBusinessElement);
        String lNewSheetId =
            sheetServiceImpl.createSheet(pRoleToken, pBusinessElement,
                    pContext);
        return lNewSheetId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createErasedElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createErasedElement(String pRoleToken,
            CacheableSheet pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        pBusinessElement.setId(null);
        String lNewSheetId =
            sheetServiceImpl.createSheet(pRoleToken, pBusinessElement,
                    pContext);
        return lNewSheetId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getSheetsFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#isElementExists(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    protected String isElementExists(String pRoleToken, SheetData pElement,
            final ImportProperties pProperties, ImportExecutionReport pReport)
    throws ImportException {
        //Get sheet information (identifier,...)
        //Throw errors if the import cannot be done.
        String lId = StringUtils.EMPTY;
        final String lProcessName =
            authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (fieldsContainerServiceImpl.isValuesContainerExists(pElement.getId())) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                if (StringUtils.isBlank(pElement.getId())
                        && sheetServiceImpl.isReferenceExists(
                                pElement.getType(), pElement.getProductName(),
                                pElement.getReference())) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                lId = StringUtils.EMPTY;
                break;
            case UPDATE_ONLY:
                if (!(fieldsContainerServiceImpl.isValuesContainerExists(pElement.getId()))
                        && !(sheetServiceImpl.isReferenceExists(
                                pElement.getType(), pElement.getProductName(),
                                pElement.getReference()))) {
                    throw new ImportException(ImportMessage.OBJECT_NOT_EXISTS,
                            pElement);
                }

                if (StringUtils.isBlank(pElement.getId())) {
                    lId =
                        sheetServiceImpl.getSheetIdByReference(
                                lProcessName, pElement.getProductName(),
                                pElement.getReference());
                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (fieldsContainerServiceImpl.isValuesContainerExists(pElement.getId())) {
                    lId = pElement.getId();
                }
                else if (StringUtils.isBlank(pElement.getId())
                        && sheetServiceImpl.isReferenceExists(
                                pElement.getType(), pElement.getProductName(),
                                pElement.getReference())) {
                    lId =
                        sheetServiceImpl.getSheetIdByReference(
                                lProcessName, pElement.getProductName(),
                                pElement.getReference());
                }
                else {
                    lId = StringUtils.EMPTY;
                }
                break;
            default:
                lId = StringUtils.EMPTY;
        }
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#removeElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void removeElement(String pRoleToken, String pElementId,
            Context pContext, String... pAdditionalArguments) {
        sheetServiceImpl.deleteSheet(pRoleToken, pElementId, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#updateElement(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void updateElement(String pRoleToken,
            CacheableSheet pBusinessElement, String pElementId,
            Context pContext, boolean pSheetsIgnoreVersion,
            String... pAdditionalArguments) {
        pBusinessElement.setId(pElementId);
        sheetServiceImpl.updateSheet(pRoleToken, pBusinessElement, pContext,
                pSheetsIgnoreVersion);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#doAdditionalImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    protected void doAdditionalImport(String pRoleToken, SheetData pElement,
            String pElementId, Context pContext) {
        boolean lIsSheetCreation = false;
        if (StringUtils.isBlank(pElement.getId())
                && StringUtils.isBlank(pElement.getReference())) {
            lIsSheetCreation = true;
        }
        pElement.setId(pElementId);
        CacheableSheet lNewSheet =
            sheetServiceImpl.getCacheableSheet(pElementId,
                    CacheProperties.MUTABLE);
        if (lNewSheet.getExtentionPointsToExclude() != null) {
            lNewSheet.setExtentionPointsToExclude(
                    pElement.getExtensionPointsToExclude().retrieveCommandsToExclude());
        }
        if (pElement.getLock() != null) {
            sheetServiceImpl.lockSheet(pRoleToken, pElementId,
                    pElement.getLock());
        }

        // Create (or reset) the transition history
        if (!lNewSheet.getTransitionsHistory().isEmpty()) {
            sheetServiceImpl.setSheetHistory(pRoleToken, pElementId,
                    lNewSheet.getTransitionsHistory());
        }
        else if (lNewSheet.getTransitionsHistory().isEmpty() && 
                pElement.getTransitionsHistory() != null &&
                !pElement.getTransitionsHistory().isEmpty()) {
            sheetServiceImpl.setSheetHistory(pRoleToken, pElementId,
                    pElement.getTransitionsHistory());
        }
        String lOriginState =
            lifeCycleServiceImpl.getProcessStateName(pElementId);
        if (!pElement.getRules().isEmpty()) {
            // The list size is equal to 1 if the rule is exclusive to the sheet
            if (!pElement.getState().equals(lOriginState)) {
                if (pElement.getRules().size() == 1) {
                    RuleData lRuleData = pElement.getRules().get(0);
                    if (!lRuleData.getTransition().get(0).getOriginState().equals(
                            lOriginState)) {
                        throw new GDMException(
                                "The origin state defined in XML does not correspond to " +
                        "the origin state in Base");
                    }
                    if (!lRuleData.getTransition().get(
                            lRuleData.getTransition().size() - 1).getFinalState().equals(
                                    pElement.getState())) {
                        throw new GDMException(
                                "The final state defined in XML does not correspond to " +
                        "the final state in Base");
                    }
                    playTransitions(lRuleData.getTransition(), pElement, pRoleToken,
                            pElementId, pContext);
                }
                // The list size is > 1 if rules are global rules
                else {
                    RuleData lRuleToUse = null;
                    for (RuleData lRuleData : pElement.getRules()) {
                        String lFirstState = lRuleData.getTransition().get(0).getOriginState();
                        String lLastState = lRuleData.getTransition().get(
                                lRuleData.getTransition().size() - 1).getFinalState();
                        if (lFirstState.equals(lOriginState) && 
                                lLastState.equals(pElement.getState())) {
                            lRuleToUse = lRuleData;
                            break;
                        }
                    }
                    if (lRuleToUse == null) {
                        throw new GDMException(
                                "No rules exist having a state beginning with " + lOriginState
                                + " and finishing with " + pElement.getState());
                    }
                    playTransitions(lRuleToUse.getTransition(), 
                            pElement, pRoleToken, pElementId, pContext);
                }
            }
        }
        else if (pElement.getState() != null
                && lOriginState != null && !pElement.getState().equals(lOriginState)
                && pElement.getRules().isEmpty()) {
            if (!lIsSheetCreation) {
                List<TransitionData> lTransitions =
                    lifeCycleServiceImpl.getUniquePath(
                            lNewSheet.getTypeId(), lOriginState,
                            pElement.getState());
                if (lTransitions == null) {
                    throw new GDMException("The path between " + lOriginState
                            + " and " + pElement.getState() + " is not unique.");
                }
                playTransitions(lTransitions, pElement, pRoleToken, pElementId, pContext);
            }
        }

        if (authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)) {
            sheetServiceImpl.removeElementFromCache(pElementId);

            revisionServiceImpl.importRevisions(pRoleToken, pElementId,
                    lNewSheet.getTypeId(), pElement.getRevisions(), pContext);
        }
    }

    /**
     * Play transitions in order to change Sheet state
     * 
     * @param pTransitions The transitions to play
     * @param pElement The sheet data
     * @param pRoleToken The role token
     * @param pElementId The sheet data identifier
     * @param pContext The context
     */
    private void playTransitions(List<TransitionData> pTransitions,
            SheetData pElement,
            String pRoleToken,
            String pElementId,
            Context pContext) {
        for (TransitionData lTransitionData : pTransitions) {
            Set<String> lCommandsToIgnore = new HashSet<String>();
            if (pElement.getExtensionPointsToExclude() != null) {
                lCommandsToIgnore.addAll(
                        pElement.getExtensionPointsToExclude().retrieveCommandsToExclude());
            }
            sheetServiceImpl.changeState(pRoleToken, pElementId,
                    lTransitionData.getName(), lCommandsToIgnore, pContext);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getCanImportErrorMessage()
     */
    @Override
    protected String getCanImportErrorMessage() {
        StringBuilder lMessage =
            new StringBuilder(super.getCanImportErrorMessage());
        lMessage.append(CANNOT_IMPORT_SHEET);
        return lMessage.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#getBusinessTypeObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    @Override
    protected CacheableSheetType getBusinessTypeObject(String pRoleToken,
            SheetData pElement) {
        final CacheableSheetType lSheetType =
            sheetServiceImpl.getTypeByName(pRoleToken, pElement.getType());
        return lSheetType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#doGetBusinessObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      org.topcased.gpm.business.fields.impl.CacheableFieldsContainer,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected CacheableSheet doGetBusinessObject(String pRoleToken,
            SheetData pElement, CacheableSheetType pBusinessType,
            ImportProperties pProperties) {
        final CacheableSheet lSheet =
            new CacheableSheet(pElement, pBusinessType);
        return lSheet;
    }

    public void setSheetServiceImpl(SheetServiceImpl pSheetServiceImpl) {
        sheetServiceImpl = pSheetServiceImpl;
    }

    public void setRevisionServiceImpl(RevisionServiceImpl pRevisionServiceImpl) {
        revisionServiceImpl = pRevisionServiceImpl;
    }

    public void setLifeCycleServiceImpl(
            LifeCycleServiceImpl pLifeCycleServiceImpl) {
        lifeCycleServiceImpl = pLifeCycleServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(SheetData pElement) {
        final String lIdentifier;
        if (StringUtils.isBlank(pElement.getId())) {
            StringBuilder lId = new StringBuilder(pElement.getReference());
            lId.append(" (").append(pElement.getProductName()).append(")");
            lIdentifier = lId.toString();
        }
        else {
            lIdentifier = pElement.getId();
        }
        return lIdentifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.SHEET;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final SheetData pElement) {
        return Collections.singletonList(pElement.getProductName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CacheableSheet getBusinessObject(String pRoleToken,
            SheetData pElement, ImportProperties pProperties) {
        CacheableSheet lCacheableSheet =
            super.getBusinessObject(pRoleToken, pElement, pProperties);

        if (pElement.getExtensionPointsToExclude() != null) {
            lCacheableSheet.setExtentionPointsToExclude(
                    pElement.getExtensionPointsToExclude().retrieveCommandsToExclude());
        }

        return lCacheableSheet;
    }
}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.extendedaction;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * ExecuteExtendedActionAction
 * 
 * @author nveillet
 */
public class ExecuteExtendedActionAction extends
        AbstractCommandAction<AbstractExecuteExtendedActionResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 4049663086216030284L;

    private List<UiField> currentSheetUpdatedFields;

    private String extendedActionName;

    private ExtendedActionType extendedActionType;

    private String extensionContainerId;

    private String filterId;

    private List<UiField> inputDataFields;

    private String inputDataId;

    private String sheetId;

    private List<String> sheetIds;
    
    private Map<String, List<UiField>> currentLinksUpdatedFields;

    /**
     * Create action
     */
    public ExecuteExtendedActionAction() {
    }

    /**
     * Create action with a sheet list
     * 
     * @param pProductName
     *            the product name
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container identifier
     */
    public ExecuteExtendedActionAction(String pProductName,
            String pExtendedActionName, String pExtensionContainerId) {
        super(pProductName);
        extendedActionName = pExtendedActionName;
        extensionContainerId = pExtensionContainerId;
    }

    /**
     * get current sheet updated fields
     * 
     * @return the current sheet updated fields
     */
    public List<UiField> getCurrentSheetUpdatedFields() {
        return currentSheetUpdatedFields;
    }

    /**
     * get extended action name
     * 
     * @return the extended action name
     */
    public String getExtendedActionName() {
        return extendedActionName;
    }

    /**
     * get extended action type
     * 
     * @return the extended action type
     */
    public ExtendedActionType getExtendedActionType() {
        return extendedActionType;
    }

    /**
     * get extension container identifier
     * 
     * @return the extension container identifier
     */
    public String getExtensionContainerId() {
        return extensionContainerId;
    }

    /**
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get input data fields
     * 
     * @return the input data fields
     */
    public List<UiField> getInputDataFields() {
        return inputDataFields;
    }

    /**
     * get input data identifier
     * 
     * @return the input data identifier
     */
    public String getInputDataId() {
        return inputDataId;
    }

    /**
     * get sheet identifier
     * 
     * @return the sheet identifier
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * get sheet identifiers
     * 
     * @return the sheet identifiers
     */
    public List<String> getSheetIds() {
        return sheetIds;
    }

    /**
     * set current sheet updated fields
     * 
     * @param pCurrentSheetUpdatedFields
     *            the current sheet updated fields to set
     */
    public void setCurrentSheetUpdatedFields(
            List<UiField> pCurrentSheetUpdatedFields) {
        currentSheetUpdatedFields = pCurrentSheetUpdatedFields;
    }

    /**
     * set extended action name
     * 
     * @param pExtendedActionName
     *            the extended action name to set
     */
    public void setExtendedActionName(String pExtendedActionName) {
        extendedActionName = pExtendedActionName;
    }

    /**
     * set extended action type
     * 
     * @param pExtendedActionType
     *            the extended action type to set
     */
    public void setExtendedActionType(ExtendedActionType pExtendedActionType) {
        extendedActionType = pExtendedActionType;
    }

    /**
     * set extension container identifier
     * 
     * @param pExtensionContainerId
     *            the extension container identifier to set
     */
    public void setExtensionContainerId(String pExtensionContainerId) {
        extensionContainerId = pExtensionContainerId;
    }

    /**
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }

    /**
     * set input data fields
     * 
     * @param pInputDataFields
     *            the input data fields to set
     */
    public void setInputDataFields(List<UiField> pInputDataFields) {
        inputDataFields = pInputDataFields;
    }

    /**
     * set input data identifier
     * 
     * @param pInputDataId
     *            the input data identifier to set
     */
    public void setInputDataId(String pInputDataId) {
        inputDataId = pInputDataId;
    }

    /**
     * set sheet identifier
     * 
     * @param pSheetId
     *            the sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }

    /**
     * set sheet identifiers
     * 
     * @param pSheetIds
     *            the sheet identifiers to set
     */
    public void setSheetIds(List<String> pSheetIds) {
        sheetIds = pSheetIds;
    }

    /**
     * Retrieves the current updated link fields
     * 
     * @return Map<String, List<UiField>>
     */
    public Map<String, List<UiField>> getCurrentLinksUpdatedFields() {
        return currentLinksUpdatedFields;
    }

    /**
     * Sets the current updated link fields
     * 
     * @param pCurrentLinksUpdatedFields
     *         the current update link fields
     */
    public void setCurrentLinksUpdatedFields(
            final Map<String, List<UiField>> pCurrentLinksUpdatedFields) {
        this.currentLinksUpdatedFields = pCurrentLinksUpdatedFields;
    }
}

/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import java.io.Serializable;
import java.util.Collection;

import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.common.extensions.ResultingScreen;

/**
 * An object used to store the result of an extended action.
 * <P>
 * Result of an extended action is used to sent back to the caller of the
 * extended action (commonly the UI) the next 'screen' to display to the end
 * user.
 * <P>
 * According to the resulting screen, only the following fields are filled :
 * <ol>
 * <li>resultScreen = FILTER_RESULT
 * <ul>
 * <li>resultSheetIds : the list of technical IDs of the sheets to display
 * <li>resultSummaryName : the filter result summary name
 * <li>resultSorterName : the filter result sorter name
 * </ul>
 * <li>resultScreen = SHEET_VISUALIZATION
 * <ul>
 * <li>resultSheetId : the technical ID of the sheet to view
 * </ul>
 * <li>resultScreen = SHEETS_VISUALIZATION
 * <ul>
 * <li>resultSheetId : the technical ID of the sheet to view
 * <li>resultSheetIds : the technical IDs of the sheet to view
 * </ul>
 * <li>resultScreen = SHEET_EDITION
 * <ul>
 * <li>resultSheetId : the technical ID of the sheet to edit
 * </ul>
 * <li>resultScreen = SHEETS_EDITION
 * <ul>
 * <li>resultSheetId : the technical ID of the sheet to edit
 * <li>resultSheetIds : the technical IDs of the sheet to edit
 * </ul>
 * <li>resultScreen = SHEET_CREATION
 * <ul>
 * <li>resultSheetTypeId : The technical ID of a sheet type.
 * </ul>
 * <li>resultScreen = MESSAGE
 * <ul>
 * <li>message : value of the message to display
 * </ul>
 * <li>resultScreen = ERROR
 * <ul>
 * <li>errorMessage : value of the error message to display
 * </ul>
 * </ol>
 * On every result (except MESSAGE), resultMessage can be set to display a
 * message in HMI when the action ends successfully.
 * 
 * @author ahaugommard
 */

public class ExtendedActionResult implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8618691310446008926L;

    /**
     * Resulting screen for the extended action, value to be chosen among :
     * FILTER_RESULT, SHEET_VISUALIZATION, SHEETS_VISUALIZATION, SHEET_EDITION,
     * SHEETS_EDITION, SHEET_CREATION, MESSAGE, ERROR
     */
    private String resultingScreen;

    /** ID of the resulting sheet */
    private String resultSheetId;

    /** list of IDs of the resulting sheets */
    private java.util.Collection<String> resultSheetIds;

    /** Name of the result summary (for FILTER_RESULT only) */
    private String resultSummaryName;

    /** Name of the result sorter (for FILTER_RESULT only) */
    private String resultSorterName;

    /** Scope of the result sorter and summary (for FILTER_RESULT only) */
    private FilterScope resultScope;

    /** Type of the filter results */
    private FilterTypeData resultFilterType;

    /** Product name of the result sorter and summary (for FILTER_RESULT only) */
    private String resultProductName;

    /** Message to display (for MESSAGE only) */
    private String message;

    /** Message to display (for all except MESSAGE when successful */
    private String resultMessage;

    /** Error message to display (for ERROR only) */
    private String errorMessage;

    /** Result sheet type ID (for SHEET_CREATION only) */
    private String resultSheetTypeId;

    /** Result File content (for FILE_OPENING only) */
    private byte[] resultFileContent;

    /** Refresh needed ? */
    private Boolean refreshNeeded;
    
    /** Result type (for FILE_OPENING only) */
    private String resultType;

    /** Result sheet (for sheet creation) */
    private CacheableSheet resultSheet;

    /**
     * Constructs an empty ExtendedActionResult
     */
    public ExtendedActionResult() {
    }

    /**
     * Constructs an ExtendedActionResult.
     * <P>
     * The context values are used to fill this ExtendedActionResult.
     * 
     * @param pCtx
     *            Context holding the parameters used in the
     *            ExtendedActionResult
     */
    @SuppressWarnings("unchecked")
    public ExtendedActionResult(Context pCtx) {
        String lResultScrName;
        lResultScrName =
                ((ResultingScreen) pCtx.get(ExtensionsService.RESULT_SCREEN)).getValue();

        setErrorMessage((String) pCtx.get(ExtensionsService.ERROR_MESSAGE));
        setMessage((String) pCtx.get(ExtensionsService.MESSAGE));
        setResultMessage((String) pCtx.get(ExtensionsService.RESULT_MESSAGE));
        setResultingScreen(lResultScrName);
        setResultSheetId((String) pCtx.get(ExtensionsService.RESULT_SHEET_ID));
        setResultSheetIds((Collection<String>) pCtx.get(ExtensionsService.RESULT_SHEET_IDS));
        setResultSheetTypeId((String) pCtx.get(ExtensionsService.RESULT_SHEET_TYPE_ID));
        setResultSorterName((String) pCtx.get(ExtensionsService.RESULT_SORTER_NAME));
        setResultSummaryName((String) pCtx.get(ExtensionsService.RESULT_SUMMARY_NAME));
        setResultProductName((String) pCtx.get(ExtensionsService.RESULT_PRODUCT_NAME));
        setResultScope((FilterScope) pCtx.get(ExtensionsService.RESULT_SCOPE));
        setResultFileContent((byte[]) pCtx.get(ExtensionsService.RESULT_FILE_CONTENT));
        setResultType((String) pCtx.get(ExtensionsService.RESULT_TYPE));
        setResultSheet((CacheableSheet) pCtx.get(ExtensionPointParameters.VALUES_CONTAINER));
        setResultFilterType((FilterTypeData) pCtx.get(ExtensionsService.RESULT_FILTER_TYPE));
        setResultRefreshNeeded((Boolean) pCtx.get(ExtensionsService.RESULT_REFRESH_NEEDED)); 
    }

    /**
     * get resultingScreen
     * 
     * @return the resultingScreen
     */
    public String getResultingScreen() {
        return resultingScreen;
    }

    /**
     * set resultingScreen
     * 
     * @param pResultingScreen
     *            the resultingScreen to set
     */
    public void setResultingScreen(String pResultingScreen) {
        resultingScreen = pResultingScreen;
    }

    /**
     * get resultSummaryName
     * 
     * @return the resultSummaryName
     */
    public String getResultSummaryName() {
        return resultSummaryName;
    }

    /**
     * set resultSummaryName
     * 
     * @param pResultSummaryName
     *            the resultSummaryName to set
     */
    public void setResultSummaryName(String pResultSummaryName) {
        resultSummaryName = pResultSummaryName;
    }

    /**
     * get resultSorterName
     * 
     * @return the resultSorterName
     */
    public String getResultSorterName() {
        return resultSorterName;
    }

    /**
     * set resultSorterName
     * 
     * @param pResultSorterName
     *            the resultSorterName to set
     */
    public void setResultSorterName(String pResultSorterName) {
        resultSorterName = pResultSorterName;
    }

    /**
     * get resultScope
     * 
     * @return the resultScope
     */
    public FilterScope getResultScope() {
        return resultScope;
    }

    /**
     * set resultScope
     * 
     * @param pResultScope
     *            the result scope to set
     */
    public void setResultScope(FilterScope pResultScope) {
        resultScope = pResultScope;
    }

    /**
     * get resultProductName
     * 
     * @return the result product name
     */
    public String getResultProductName() {
        return resultProductName;
    }

    /**
     * set result product name
     * 
     * @param pResultProductName
     *            the result product name to set
     */
    public void setResultProductName(String pResultProductName) {
        resultProductName = pResultProductName;
    }

    /**
     * get message
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message
     * 
     * @param pMessage
     *            the message to set
     */
    public void setMessage(String pMessage) {
        message = pMessage;
    }

    /**
     * get resultSheetId
     * 
     * @return the resultSheetId
     */
    public String getResultSheetId() {
        return resultSheetId;
    }

    /**
     * set resultSheetId
     * 
     * @param pResultSheetId
     *            the resultSheetId to set
     */
    public void setResultSheetId(String pResultSheetId) {
        resultSheetId = pResultSheetId;
    }

    /**
     * get resultSheetIds
     * 
     * @return the resultSheetIds
     */
    public java.util.Collection<String> getResultSheetIds() {
        return resultSheetIds;
    }

    /**
     * set resultSheetIds
     * 
     * @param pResultSheetIds
     *            the resultSheetIds to set
     */
    public void setResultSheetIds(Collection<String> pResultSheetIds) {
        resultSheetIds = pResultSheetIds;
    }

    /**
     * get errorMessage
     * 
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * set errorMessage
     * 
     * @param pErrorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String pErrorMessage) {
        errorMessage = pErrorMessage;
    }

    /**
     * get resultSheetTypeId
     * 
     * @return the resultSheetTypeId
     */
    public String getResultSheetTypeId() {
        return resultSheetTypeId;
    }

    /**
     * set resultSheetTypeId
     * 
     * @param pResultSheetTypeId
     *            the resultSheetTypeId to set
     */
    public void setResultSheetTypeId(String pResultSheetTypeId) {
        resultSheetTypeId = pResultSheetTypeId;
    }

    /**
     * get resultFileContent
     * 
     * @return the resultFileContent
     */
    public byte[] getResultFileContent() {
        return resultFileContent;
    }

    /**
     * set resultFileContent
     * 
     * @param pResultFileContent
     *            the resultFileContent to set
     */
    public void setResultFileContent(byte[] pResultFileContent) {
        this.resultFileContent = pResultFileContent;
    }

    /**
     * get resultType
     * 
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * set resultType
     * 
     * @param pResultType
     *            the resultType to set
     */
    public void setResultType(String pResultType) {
        this.resultType = pResultType;
    }

    /**
     * get resultSheet
     * 
     * @return the resultSheet
     */
    public CacheableSheet getResultSheet() {
        return resultSheet;
    }

    /**
     * set resultSheet
     * 
     * @param pResultSheet
     *            the resultSheet to set
     */
    public void setResultSheet(CacheableSheet pResultSheet) {
        this.resultSheet = pResultSheet;
    }

    /**
     * set resultFilterType
     * 
     * @param pResultFilterType
     *            the resultFilterType to set
     */
    public void setResultFilterType(FilterTypeData pResultFilterType) {
        resultFilterType = pResultFilterType;
    }

    /**
     * get resultFilterType
     * 
     * @return the resultFilterType
     */
    public FilterTypeData getResultFilterType() {
        return resultFilterType;
    }
    
    /**
     * set refresh needed
     * 
     * @param pResultRefreshNeeded true if filter needs to be refreshed
     */
    public void setResultRefreshNeeded(Boolean pResultRefreshNeeded) {
    	refreshNeeded = pResultRefreshNeeded;
    }

    /**
     * get refresh needed
     * 
     * @return true if refresh is needed
     */
    public Boolean getResultRefreshNeeded() {
        return refreshNeeded;
    }

    /**
     * get resultMessage
     * 
     * @return the resultMessage
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * set resultMessage
     * 
     * @param pResultMessage
     *            the resultMessage to set
     */
    public void setResultMessage(String pResultMessage) {
        resultMessage = pResultMessage;
    }
}

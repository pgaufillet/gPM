/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetLinksByType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.util.AttachmentInError;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.domain.fields.AttachedFieldValue;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;

/**
 * Public interface of the sheet service.
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface SheetService {

    /**
     * Get the cacheable sheet types associated with a process.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of business process
     * @param pCacheProperties
     *            The properties for load the cache object
     * @return List of cacheable sheet types
     */
    @Transactional(readOnly = true)
    public List<CacheableSheetType> getSheetTypes(String pRoleToken,
            java.lang.String pProcessName, CacheProperties pCacheProperties);

    /**
     * Get the sheet types associated to a process
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of the business process
     * @return List of serializable sheet types
     */
    @Transactional(readOnly = true)
    public List<org.topcased.gpm.business.serialization.data.SheetType> getSerializableSheetTypes(
            final String pRoleToken, final String pProcessName);

    /**
     * Get the sheet types associated with a process a given role can create.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of business process
     * @return List of sheet types data
     */
    @Transactional(readOnly = true)
    public List<org.topcased.gpm.business.serialization.data.SheetType> getCreatableSerializableSheetTypes(
            final String pRoleToken, final String pProcessName);

    /**
     * Get a list of sheet summaries whose type passed in parameter.
     * 
     * @param pProcessName
     *            Business process name.
     * @param pSheetTypeName
     *            Name of sheet type.
     * @return List of sheet summaries
     */
    @Transactional(readOnly = true)
    public List<SheetSummaryData> getSheetsByType(String pProcessName,
            String pSheetTypeName);

    /**
     * Get the content of an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pAttachedFieldValueId
     *            Identifier of the sheet in the database.
     * @return Byte array containing the file content.
     */
    @Transactional(readOnly = true)
    public byte[] getAttachedFileContent(String pRoleToken,
            String pAttachedFieldValueId);

    /**
     * Get an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pAttachedFieldValueId
     *            Identifier of the field in the database.
     * @return The {@link AttachedFieldValue} with the given Id.
     */
    @Transactional(readOnly = true)
    public AttachedFieldValue getAttachedFileValue(String pRoleToken,
            String pAttachedFieldValueId);

    /**
     * Set the content of an attached file.
     * 
     * @param pRoleToken
     *            the role token session.
     * @param pSheetId
     *            The sheet id
     * @param pAttachedFieldValueId
     *            the identifier of the sheet in the database.
     * @param pByteArray
     *            the byte array containing the file content.
     */
    @Transactional(readOnly = true)
    public void setAttachedFileContent(String pRoleToken, String pSheetId,
            String pAttachedFieldValueId, byte[] pByteArray);

    /**
     * Set the content of an attached file.
     * 
     * @param pRoleToken
     *            the role token session.
     * @param pAttachedFieldValueId
     *            the identifier of the sheet in the database.
     * @param pByteArray
     *            the byte array containing the file content.
     */
    @Transactional(readOnly = true)
    public void setAttachedFileContent(String pRoleToken,
            String pAttachedFieldValueId, byte[] pByteArray);

    /**
     * Retrieve a sheet by its Id.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the sheet in the database.
     * @return The SheetData containing the structure and values of this sheet.
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @deprecated
     * @since 1.8.3
     * @see SheetService#getCacheableSheet(String, String, CacheProperties)
     * @see SheetService#getSheetRefByKey(String, String)
     */
    @Transactional(readOnly = true)
    public SheetData getSheetByKey(String pRoleToken, String pSheetId)
        throws AuthorizationException;

    /**
     * Get the sheet identifier from its reference.
     * <p>
     * A sheet can be identify by combining process, product and reference.
     * <p>
     * The role token is used to retrieve the current process and product's
     * name.
     * 
     * @param pRoleToken
     *            role token
     * @param pReference
     *            Sheet's reference
     * @return Identifier if found, null otherwise
     */
    @Transactional(readOnly = true)
    public String getSheetIdByReference(final String pRoleToken,
            final String pReference);

    /**
     * Retrieve the identifier of the sheet from its reference.
     * 
     * @param pProcessName
     *            Process name
     * @param pProductName
     *            Product name
     * @param pReference
     *            Sheet's reference
     * @return Identifier of the sheet.
     */
    @Transactional(readOnly = true)
    public String getSheetIdByReference(final String pProcessName,
            final String pProductName, final String pReference);

    /**
     * Retrieve a sheet reference its Id.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the sheet in the database.
     * @return The LineFieldData containing the structure and values of the
     *         sheet ref.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    @Transactional(readOnly = true)
    public LineFieldData getSheetRefByKey(String pRoleToken, String pSheetId)
        throws AuthorizationException;

    /**
     * Retrieve a string containing the sheet reference by the sheet Id.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the sheet in the database.
     * @return Sheet ref. as string.
     */
    @Transactional(readOnly = true)
    public String getSheetRefStringByKey(String pRoleToken, String pSheetId);

    /**
     * Update a sheet reference.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the sheet in the database.
     * @param pRef
     *            LineFieldData containing the new values of the sheet ref.
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public void updateSheetRefByKey(String pRoleToken, String pSheetId,
            LineFieldData pRef) throws AuthorizationException,
        FieldValidationException;

    /**
     * Retrieve a sheet summary using the sheet Id.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the sheet in the database.
     * @return The SheetData containing the structure and values of this sheet.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    @Transactional(readOnly = true)
    public SheetSummaryData getSheetSummaryByKey(String pRoleToken,
            String pSheetId) throws AuthorizationException;

    /**
     * Get a Cacheable Sheet initialized using the values of another sheet.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @param pSourceSheetId
     *            Identifier of the source sheet.
     * @return The initialized sheet.
     * @throws AuthorizationException
     *             Illegal access to a sheet or sheetType.
     */
    @Transactional(readOnly = true)
    public CacheableSheet getCacheableSheetInitializationModel(
            final String pRoleToken, final String pSheetTypeId,
            final String pSourceSheetId) throws AuthorizationException;

    /**
     * Get a Cacheable Sheet cloned from the values of another sheet.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSourceSheetId
     *            Identifier of the source sheet.
     * @return The duplicated sheet.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    @Transactional(readOnly = true)
    public CacheableSheet getCacheableSheetDuplicationModel(String pRoleToken,
            String pSourceSheetId) throws AuthorizationException;

    /**
     * Creates a new sheet in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pData
     *            Contains the data of the sheet.
     * @param pCtx
     *            Execution context.
     * @return Identifier of the newly created sheet.
     * @throws AuthorizationException
     *             illegal sheet creation
     * @deprecated
     * @since 1.8.3
     * @see SheetService#createSheet(String, CacheableSheet, Context)
     */
    public String createSheet(String pRoleToken, SheetData pData, Context pCtx)
        throws AuthorizationException;

    /**
     * Create the content of the sheet in the database.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pCacheableSheetData
     *            Updated values to store in the DB.
     * @param pCtx
     *            Execution context.
     * @return Identifier of the created sheet.
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @throws MandatoryValuesException
     *             Some fields defined as mandatory are not valued
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public String createSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx)
        throws AuthorizationException, MandatoryValuesException,
        FieldValidationException;

    /**
     * Create the content of the sheet in the database.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProcessName
     *            Process name.
     * @param pSheetData
     *            Updated values to store in the DB.
     * @param pCtx
     *            Execution Context.
     * @return Identifier of the created sheet.
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @throws MandatoryValuesException
     *             Some fields defined as mandatory are not valued
     */
    public String createSheet(
            final String pRoleToken,
            String pProcessName,
            final org.topcased.gpm.business.serialization.data.SheetData pSheetData,
            Context pCtx) throws AuthorizationException,
        MandatoryValuesException;

    /**
     * Creates a new sheet type in the database. The sheet type created has no
     * fields.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcName
     *            Name of the business process
     * @param pSheetType
     *            Contains the data of the sheet type to create.
     * @return Identifier of the newly created sheet type.
     */
    public String createSheetType(String pRoleToken, String pBusinessProcName,
            CacheableSheetType pSheetType);

    /**
     * Define (or change) the field to use as reference field for a sheet type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @param pRefFieldId
     *            Identifier of the reference field
     */
    public void setSheetTypeReferenceField(String pRoleToken,
            String pSheetTypeId, String pRefFieldId);

    /**
     * Delete a sheet type in the database.
     * 
     * @param pRoleToken
     *            Role session token *
     * @param pBusinessProcName
     *            Name of the business process
     * @param pSheetTypeName
     *            Name of the type to remove.
     * @param pDeleteSheets
     *            Force the deletion of all sheets of this type
     */
    public void deleteSheetType(String pRoleToken, String pBusinessProcName,
            String pSheetTypeName, boolean pDeleteSheets);

    /**
     * Update the values contained in a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pData
     *            Contains the new value of data to set in the sheet.
     * @param pCtx
     *            Execution context.
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @deprecated
     * @since 1.8.3
     * @see SheetService#updateSheet(String, CacheableSheet, Context)
     */
    public void updateSheet(String pRoleToken, SheetData pData, Context pCtx)
        throws AuthorizationException;

    /**
     * Update the values contained in a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Process name
     * @param pData
     *            Contains the new value of data to set in the sheet.
     * @param pCtx
     *            Execution context.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    public void updateSheet(String pRoleToken, String pProcessName,
            org.topcased.gpm.business.serialization.data.SheetData pData,
            Context pCtx) throws AuthorizationException;

    /**
     * Remove a sheet from the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet to remove.
     * @param pContext
     *            Execution context
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void deleteSheet(final String pRoleToken, final String pSheetId,
            final Context pContext) throws AuthorizationException;

    /**
     * Duplicate a sheet. The created sheet is a copy of the source sheet (same
     * field values).
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the source sheet.
     * @return Identifier of the newly created sheet.
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public String duplicateSheet(String pRoleToken, String pSheetId)
        throws AuthorizationException;

    /**
     * Get the process information regarding the life cycle of a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @return Process information
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    @Transactional(readOnly = true)
    public ProcessInformation getSheetProcessInformation(String pRoleToken,
            String pSheetId) throws AuthorizationException;

    /**
     * Get the history of a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @return Array containing the history info
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    @Transactional(readOnly = true)
    public SheetHistoryData[] getSheetHistory(String pRoleToken, String pSheetId)
        throws AuthorizationException;

    /**
     * Set the transition history of a sheet.
     * <p>
     * This method replaces any existing transition history for a sheet. It
     * requires 'admin' privileges.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @param pHistory
     *            Array containing the history info
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void setSheetHistory(final String pRoleToken, final String pSheetId,
            final SheetHistoryData[] pHistory) throws AuthorizationException;

    /**
     * Set the transition history of a sheet.
     * <p>
     * This method replaces any existing transition history for a sheet. It
     * requires 'admin' privileges.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @param pTransitionData
     *            History transition
     */
    public void setSheetHistory(final String pRoleToken, final String pSheetId,
            final List<? extends TransitionHistoryData> pTransitionData);

    /**
     * Add a line in the sheet history with current change date
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @param pLoginName
     *            Sheet history enty login name
     * @param pOriginState
     *            Sheet history entry origin state
     * @param pDestinationState
     *            Sheet history entry destination state
     * @throws AuthorizationException
     *             Illegal access to the sheet.
     */
    public void addSheetHistory(final String pRoleToken, final String pSheetId,
            String pLoginName, String pOriginState, String pDestinationState,
            final String pTransitionNme) throws AuthorizationException;

    /**
     * Add an element in the sheet history, with the possibility to add all
     * elements (user, originState, changeState, changeDate)
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @param pLoginName
     *            Sheet history entry login name
     * @param pOriginState
     *            Sheet history entry origin state
     * @param pDestinationState
     *            Sheet history entry destination state
     * @param pChangeDate
     *            Sheet history entry change date
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void addSheetHistory(final String pRoleToken, final String pSheetId,
            String pLoginName, String pOriginState, String pDestinationState,
            Timestamp pChangeDate, final String pTransitionName)
        throws AuthorizationException;

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @param pContext
     *            Application context
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void changeState(String pRoleToken, String pSheetId,
            String pTransition, Context pContext) throws AuthorizationException;

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @param pCommandsToExclude
     *            Commands to exclude
     * @param pContext
     *            Application context
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void changeState(String pRoleToken, String pSheetId,
            String pTransition, Set<String> pCommandsToExclude, Context pContext)
        throws AuthorizationException;

    /**
     * Exports a list of sheets into various format.
     * 
     * @param pRoleToken
     *            The role session token
     * @param pOutputStream
     *            The output stream to be written
     * @param pSheetIds
     *            The sheet ids to export
     * @param pSheetExportFormat
     *            The format.
     * @throws AuthorizationException
     *             illegal access to all the sheets
     */
    @Transactional(readOnly = true)
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pSheetExportFormat)
        throws AuthorizationException;

    /**
     * Exports a list of sheets - only fields label key contained in the
     * exportedFieldsLabel list are exported - into various format.
     * 
     * @param pRoleToken
     *            The role session token
     * @param pOutputStream
     *            The output stream to be written
     * @param pSheetIds
     *            The sheet ids to export
     * @param pSheetExportFormat
     *            The format.
     * @param pExportedFieldsLabel
     *            The exported fields names
     * @throws AuthorizationException
     *             illegal access to all the sheets
     */
    @Transactional(readOnly = true)
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            String[] pSheetIds, SheetExportFormat pSheetExportFormat,
            List<String> pExportedFieldsLabel) throws AuthorizationException;

    /**
     * Export sheet summaries.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pOutputStream
     *            The output stream to be write
     * @param pLabels
     *            the map of labels, linking labelkey with optional label.
     * @param pSheetSummaries
     *            The sheet summaries collection to export
     * @param pExportFormat
     *            The format
     * @param pContext
     *            The context. Usefull to pass option. Can be null
     * @throws AuthorizationException
     *             illegal access to all the sheet summaries
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext)
        throws AuthorizationException;

    /**
     * Get a list of sheet types identifiers possibly linkable to a given sheet
     * type.
     * 
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @return List of sheet type identifiers
     */
    public List<String> getLinkableSheetTypes(String pSheetTypeId);

    /**
     * Get a list of possible links selectable for a given sheet type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @return List of sheet link types IDs
     */
    public List<String> getPossibleLinkTypes(String pRoleToken,
            String pSheetTypeId);

    /**
     * Get a list of possible links selectable for a given sheet type.
     * 
     * @param pRoleToken
     *            the role token
     * @param pProcessName
     *            the process name
     * @param pProductName
     *            the product name
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @return List of sheet link types IDs
     */
    public List<String> getPossibleLinkTypes(String pRoleToken,
            String pProcessName, String pProductName, String pSheetTypeId);

    /**
     * Add (or replace) an extension point on a container.
     * 
     * @param pToken
     *            Session token
     * @param pFieldsContainerId
     *            Sheet type or sheet link type identifier
     * @param pExtensionPointName
     *            Name of the extension
     * @param pCommandNames
     *            List of commands to be executed for this extension
     */
    public void addExtension(String pToken, String pFieldsContainerId,
            String pExtensionPointName, List<String> pCommandNames);

    /**
     * Remove an extension point on a sheet type.
     * 
     * @param pToken
     *            Session token
     * @param pFieldsContainerId
     *            FieldsContainer identifier
     * @param pExtensionPointName
     *            Name of the extension to remove
     */
    public void removeExtension(String pToken, String pFieldsContainerId,
            String pExtensionPointName);

    /**
     * Get an empty CacheableSheet modeled after the given sheet type. Fields
     * are filled with their default value (if any).
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pCacheableSheetType
     *            Type of the sheet.
     * @param pProductName
     *            Name of the product.
     * @param pContext
     *            Execution context.
     * @return An empty CacheableSheet.
     * @throws AuthorizationException
     *             illegal access to the sheet type
     */
    public CacheableSheet getCacheableSheetModel(String pRoleToken,
            CacheableSheetType pCacheableSheetType, String pProductName,
            Context pContext);

    /**
     * Get a sheet from its ID, with the confidential values removed
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pProperties
     *            the cache properties
     * @return the cached sheet
     */
    public CacheableSheet getCacheableSheet(String pRoleToken, String pSheetId,
            CacheProperties pProperties);

    /**
     * Create a CacheableSheet object from a serialization sheet
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of the instance
     * @param pSheetData
     *            Serializable sheet
     * @return CacheableSheet
     */
    public CacheableSheet getCacheableSheet(
            final String pRoleToken,
            final String pProcessName,
            final org.topcased.gpm.business.serialization.data.SheetData pSheetData);

    /**
     * Get a sheet type from its name.
     * 
     * @param pRoleToken
     *            the session token
     * @param pProcessName
     *            the business process name
     * @param pSheetTypeName
     *            the sheet type name
     * @param pProperties
     *            The properties for load the cache object
     * @return the cached sheet type or null if the cached sheet type was not
     *         found.
     */
    public CacheableSheetType getCacheableSheetTypeByName(String pRoleToken,
            String pProcessName, String pSheetTypeName,
            CacheProperties pProperties);

    /**
     * Get a sheet type from its name.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetTypeName
     *            the sheet type name
     * @param pCacheProperties
     *            The properties for load the cache object
     * @return the cached sheet type.
     */
    public CacheableSheetType getCacheableSheetTypeByName(String pRoleToken,
            String pSheetTypeName, CacheProperties pCacheProperties);

    /**
     * Get a sheet type from its id.
     * 
     * @param pSheetTypeId
     *            The sheet type identifier
     * @return the cached sheet type.
     * @deprecated
     * @see SheetService#getCacheableSheetType(String, String, CacheProperties)
     */
    public CacheableSheetType getCacheableSheetType(String pSheetTypeId);

    /**
     * Get CacheableSheetType without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pSheetTypeId
     *            The id of the sheet type
     * @param pProperties
     *            The cache properties
     * @return The CacheableSheetType
     */
    public CacheableSheetType getCacheableSheetType(String pSheetTypeId,
            CacheProperties pProperties);

    /**
     * Get a sheet type from its id.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetTypeId
     *            the id of the sheet type
     * @param pProperties
     *            The properties for load the cache object
     * @return the cached sheet type.
     */
    public CacheableSheetType getCacheableSheetType(String pRoleToken,
            String pSheetTypeId, CacheProperties pProperties);

    /**
     * Update the content of the sheet in the database.
     * <p>
     * Update attributes.
     * </p>
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetData
     *            Updated values to store in the DB.
     * @param pCtx
     *            Execution context
     * @param pIgnoreVersion
     *            <tt>true</tt> if version must be ignored, <tt>false</tt>
     *            otherwise
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public void updateSheet(final String pRoleToken,
            final CacheableSheet pSheetData, Context pCtx,
            boolean pIgnoreVersion) throws AuthorizationException,
        FieldValidationException;

    /**
     * Update the content of the sheet in the database.
     * <p>
     * Update attributes.
     * </p>
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pCacheableSheetData
     *            Updated values to store in the DB.
     * @param pCtx
     *            Execution context
     * @throws AuthorizationException
     *             illegal access to the sheet
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public void updateSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx);

    /**
     * Create or update the content of the sheet in the database.
     * <p>
     * This method uses the identifier specified in the sheet data to test if
     * the sheet must be created (does not exist in DB yet) or updated.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pCacheableSheetData
     *            Updated values to store in the DB.
     * @param pCtx
     *            Execution context (containing the transition name if
     *            applicable).
     * @return Technical identifier of the created / updated sheet
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    public String createOrUpdateSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx);

    /**
     * Update the content of the sheet in the database, and change its current
     * state in its lifecycle.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetData
     *            Updated values to store in the DB.
     * @param pTransition
     *            Name of the transition to perform (can be null or empty if no
     *            transition is needed).
     * @param pCtx
     *            Execution context.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    public void updateSheet(final String pRoleToken,
            final CacheableSheet pSheetData, final String pTransition,
            Context pCtx) throws AuthorizationException;

    /**
     * Get CacheableSheet without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pSheetId
     *            The sheet id
     * @param pCacheProperties
     *            The cache properties
     * @return The cacheable sheet
     */
    public CacheableSheet getCacheableSheet(String pSheetId,
            CacheProperties pCacheProperties);

    /**
     * Lock or unlock a sheet. This method is only a convenient way to take or
     * release a lock for the current user.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pLockProperties
     *            LProperties of the lock
     * @throws LockException
     *             Sheet is already locked
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void lockSheet(final String pRoleToken, String pSheetId,
            LockProperties pLockProperties) throws LockException,
        AuthorizationException;

    /**
     * Take or release a lock on a sheet.
     * <p>
     * This method is used to set or remove a lock on a given sheet. The
     * following rules are checked:
     * <ul>
     * <li>Any user can lock a currently unlocked sheet.
     * <li>Only the owner of a lock, or an administrator can change a lock
     * (remove it or change its type). Otherwise a <i>LockException</i> is
     * thrown.
     * </ul>
     * An administrator can set / remove lock on any sheet, and can specify any
     * lock owner (written differently, an administrator can remove a lock, no
     * matter the actual owner, and can set a lock on a sheet on behalf on any
     * user).
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pLock
     *            Lock details. Please note that if the current user is not
     *            admin, the <i>owner</i> attribute in this lock object
     *            <b>must</b> be equals to the current user login.
     * @throws LockException
     *             Sheet is already locked and user is not allowed to update or
     *             remove the lock
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    public void lockSheet(final String pRoleToken, String pSheetId,
            org.topcased.gpm.business.serialization.data.Lock pLock)
        throws LockException, AuthorizationException;

    /**
     * Get the lock infos
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Technical identifier of the sheet
     * @return Lock infos (or null if no lock exists on the sheet)
     */
    public org.topcased.gpm.business.serialization.data.Lock getLock(
            final String pRoleToken, final String pSheetId);

    /**
     * Remove a lock on a sheet
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Technical identifier of the sheet
     */
    public void removeLock(String pRoleToken, String pSheetId);

    /**
     * Get a sheet from its ID, with the confidential values removed.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @return the serializable sheet.
     */
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheet(
            String pRoleToken, String pSheetId);

    /**
     * Retrieve a sheet through its functional reference.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pProcessName
     *            Name of the business process
     * @param pProductName
     *            Name of the product
     * @param pSheetRef
     *            Stringified reference of the sheet.
     * @return The serializable sheet. containing the structure and values of
     *         this sheet, or null if no sheet has the given reference.
     */
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheetByRef(
            final String pRoleToken, final String pProcessName,
            final String pProductName, final String pSheetRef);

    /**
     * Get a sheet type from its ID.
     * 
     * @param pSheetTypeId
     *            the sheet type id
     * @return the serializable sheet type.
     */
    public org.topcased.gpm.business.serialization.data.SheetType getSerializableSheetType(
            String pSheetTypeId);

    /**
     * Get a sheet type from its name.
     * 
     * @param pRoleToken
     *            the session token
     * @param pProcessName
     *            the business process name
     * @param pSheetTypeName
     *            the sheet type name
     * @return the serializable sheet type.
     */
    public org.topcased.gpm.business.serialization.data.SheetType getSerializableSheetTypeByName(
            String pRoleToken, String pProcessName, String pSheetTypeName);

    /**
     * Filter the CacheableSheet structure to keep only expected fields
     * (identified by their labelkeys).
     * 
     * @param pCacheableSheetData
     *            The CacheableSheet object to update.
     * @param pLabelKeys
     *            The list of labelkey of expected fields to keep.
     */
    public void filterCacheableSheet(CacheableSheet pCacheableSheetData,
            List<String> pLabelKeys);

    /**
     * Get a the links associated to a sheet from its ID, with the confidential
     * values removed. The links are scheduled using filters associated to link
     * type
     * 
     * @param pProcessName
     *            The name of the process
     * @param pSheetId
     *            The sheet id
     * @param pLinkTypeId
     *            The link type id
     * @return The links list cached
     */
    public CacheableSheetLinksByType getCacheableSheetLinksByType(
            String pProcessName, String pSheetId, String pLinkTypeId);

    /**
     * The destination sheet's values are initialized with the values of the
     * origin one. The mapping defined during the instantiation is used.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pOriginSheetType
     *            The type of the origin sheet.
     * @param pOriginSheet
     *            The origin sheet.
     * @param pDestinationSheetType
     *            The type of the destination sheet.
     * @param pDestinationSheet
     *            The destination sheet.
     * @param pContext
     *            The context.
     */
    public void initializeSheetValues(String pRoleToken,
            CacheableSheetType pOriginSheetType, CacheableSheet pOriginSheet,
            CacheableSheetType pDestinationSheetType,
            CacheableSheet pDestinationSheet, Context pContext);

    /**
     * Determine if a sheet exist with a given identifier
     * 
     * @param pUserToken
     *            the user token
     * @param pSheetId
     *            the sheet identifier
     * @return if the sheet exist
     */
    public boolean isSheetIdExist(String pUserToken, String pSheetId);

    /**
     * Get the process name of a sheet
     * 
     * @param pUserToken
     *            the user token
     * @param pSheetId
     *            the sheet identifier
     * @return the process name
     */
    public String getProcessName(String pUserToken, String pSheetId);

    /**
     * Get the product name of a sheet
     * 
     * @param pUserToken
     *            the user token
     * @param pSheetId
     *            the sheet identifier
     * @return the product name
     */
    public String getProductName(String pUserToken, String pSheetId);

    /**
     * This method will return true is the sheet is locked for this role
     * 
     * @param pRoleToken
     *            the sheet accessing role
     * @param pSheetId
     *            the id of the sheet
     * @param pDisplayMode
     *            the wished display mode
     * @return a boolean
     */
    public boolean isSheetLocked(String pRoleToken, String pSheetId,
            DisplayMode pDisplayMode);

    /**
     * Retrieves the MaxAttachedFileSize.
     * 
     * @return MaxAttachedFileSize as double in Byte
     */
    public double getMaxAttachedFileSize();

    /**
     * Get the attached fields in error
     * 
     * @param pSheetId
     *            The sheet Id
     * @return the attached fields in error and for each file, determine the error
     */
    public List<AttachmentInError> getAndAcknowledgeAttachedFilesInError(String pSheetId);

    /**
     * Retrieves the sheet type Id, sheet Id given
     * 
     * @param pSheetId
     *            The sheet Id
     * @return sheet type Id or null
     */
    public String getSheetTypeIdBySheetId(final String pSheetId);

    /**
     * Retrieves the sheet type Ids, sheets Ids given
     * 
     * @param pSheetIds
     *            The sheets Ids
     * @return sheet type Ids or Empty set
     */
    public Set<String> getSheetTypeIdBySheetIds(List<String> pSheetIds);
    
    /**
     * Get the type sheets count.
     * 
     * @param pSheetTypeId The sheet type id
     * 
     * @return The sheets count
     */
    public int getSheetsCount(String pSheetTypeId);
}

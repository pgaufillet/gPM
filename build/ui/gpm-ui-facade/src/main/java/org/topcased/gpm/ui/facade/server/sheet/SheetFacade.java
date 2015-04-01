/**************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.sheet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.exception.FacadeAttachmentException;

/**
 * SheetFacade
 * 
 * @author nveillet
 */
public interface SheetFacade {

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
            String pTransitionName);

    /**
     * Clear a sheet from cache
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     */
    public void clearCache(UiSession pSession, String pSheetId);

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
            List<UiField> pFieldsList);

    /**
     * Delete sheet in database
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Id of the sheet to delete
     */
    public void deleteSheet(UiSession pSession, String pSheetId);

    /**
     * Duplicate sheet
     * 
     * @param pSession
     *            Current user session
     * @param pSheetId
     *            Id of the sheet to duplicate
     * @return the duplicated sheet
     */
    public UiSheet duplicateSheet(UiSession pSession, String pSheetId);

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
            UiSheet pSheet);

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
    public Map<String, String> getTransitionConfirmationMessages(UiSession pSession,
            UiSheet pSheet);

    /**
     * Get creatable sheet types
     * 
     * @param pSession
     *            the session
     * @return the creatable sheet type names
     */
    public List<String> getCreatableSheetTypes(UiSession pSession);

    /**
     * Get sheet's identifier
     * 
     * @param pSession
     *            User session
     * @param pSheetRef
     *            Reference of the sheet
     * @return Identifier of the sheet if found, null otherwise
     */
    public String getId(UiSession pSession, String pSheetRef);

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
            String pSheetTypeId);

    /**
     * Get the process name of a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @return the process name
     */
    public String getProcessName(UiUserSession pSession, String pSheetId);

    /**
     * Get the product name of a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     * @return the product name
     */
    public String getProductName(UiUserSession pSession, String pSheetId);

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
            DisplayMode pDisplayMode);

    /**
     * Get empty sheet of given Type
     * 
     * @param pSession
     *            Current user session
     * @param pSheetTypeName
     *            Sheet type name
     * @return the empty sheet
     */
    public UiSheet getSheetByType(UiSession pSession, String pSheetTypeName);

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
            CacheableSheet pSheet);

    /**
     * Get the sheet type name from the identifier
     * 
     * @param pSession
     *            the session
     * @param pSheetTypeId
     *            the sheet type identifier
     * @return the sheet type name
     */
    public String getSheetTypeName(UiSession pSession, String pSheetTypeId);

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
            final DisplayMode pDisplayMode);

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
            String pSourceSheetId);

    /**
     * Unlock a sheet
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the sheet identifier
     */
    public void unLockSheet(UiSession pSession, String pSheetId);

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
            List<UiField> pFields);

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
    public boolean isSheetLocked(UiSession pSession, String pSheetId,
            DisplayMode pDisplayMode);

    /**
     * Get the attached fields in error, and remove the entry from the list.
     * 
     * @param pSheetId
     *            The sheet Id
     * @return the attached fields in error and their content
     */
    public List<FacadeAttachmentException> getAndAcknowledgeAttachedFilesInError(String pSheetId);

    /**
     * Retrieves the sheet type Id, sheet Id given
     * 
     * @param pSheetId
     *            The sheet Id
     * @return sheet type Id or null
     */
    public String getSheetTypeIdBySheetId(final String pSheetId);

    /**
     * Retrieves the sheet type Ids, sheet Ids given
     * 
     * @param pSheetIds
     *            The sheets Ids
     * @return sheet types Ids or Empty Set
     */
    public Set<String> getSheetTypeIdBySheetIds(final List<String> pSheetIds);
}

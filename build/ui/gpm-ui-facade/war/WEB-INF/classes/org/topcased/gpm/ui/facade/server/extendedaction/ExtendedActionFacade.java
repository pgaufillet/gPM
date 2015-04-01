/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.extendedaction;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;
import org.topcased.gpm.ui.facade.shared.extendedaction.AbstractUiExtendedActionResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * ExtendedActionFacade
 * 
 * @author nveillet
 */
public interface ExtendedActionFacade {

    /**
     * Clear a input data from cache
     * 
     * @param pSession
     *            the session
     * @param pInputDataId
     *            the input data identifier
     */
    public void clearCache(UiSession pSession, String pInputDataId);

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
     *@param pCurrentLinksUpdatedFields
     *            the current Link updated fields (when execute on sheet edition)
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
            DisplayMode pCurrentSheetDisplayMode);

    /**
     * Get the process extended actions
     * 
     * @param pSession
     *            the session
     * @return the process extended actions
     */
    public List<UiAction> getAvailableExtendedActions(final UiSession pSession);

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
            final List<UiFilterContainerType> pContainerTypes);

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
            final String pSheetTypeId, final DisplayMode pDisplayMode);

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
            final String pSheetId, final List<String> pSheetIds);

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
            String pExtensionContainerId);
}

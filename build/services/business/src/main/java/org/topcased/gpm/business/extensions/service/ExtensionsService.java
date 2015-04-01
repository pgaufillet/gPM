/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Vincent Hémery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.extensions.ExtensionsContainer;

/**
 * Extensions service This service is used to invoke commands attached to an
 * extension point.
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface ExtensionsService {

    /** The parameter indicating the resulting screen */
    public final static String RESULT_SCREEN = "resultScreen";

    /** The transition name available in the context */
    public final static String TRANSITION_NAME = "transitionName";

    /** The filter name available in the context */
    public final static String FILTER_NAME = "filterName";

    /** The name of the filter's product available in the context */
    public final static String FILTER_PRODUCT_NAME = "filterProductName";

    /** The filter's scope available in the context */
    public final static String FILTER_SCOPE = "filterScope";

    /** The message in context in case of message screen */
    public final static String MESSAGE = "message";

    /**
     * The result message in context in case the Extended Action indicates to
     * display a message in addition to the result treatment
     */
    public final static String RESULT_MESSAGE = "resultMessage";

    /** The Error message in context in case of error */
    public final static String ERROR_MESSAGE = "errorMessage";

    /** The resulting Sheet id available in the context */
    public final static String RESULT_SHEET_ID = "resultSheetId";

    /** The list of resulting sheet ids available in the context */
    public final static String RESULT_SHEET_IDS = "resultSheetIds";

    /** The name of the result summary to use for filter results */
    public final static String RESULT_SUMMARY_NAME = "resultSummaryName";

    /** The name of the result sorter to use for filter results */
    public final static String RESULT_SORTER_NAME = "resultSorterName";

    /** The name of the product to use for filter results */
    public final static String RESULT_PRODUCT_NAME = "resultProductName";

    /** The filter scope to use for filter results */
    public final static String RESULT_SCOPE = "resultScope";

    /** The filter type to use for filter results */
    public final static String RESULT_FILTER_TYPE = "resultFilterType";

    /** The result file content in case of opening file result screen */
    public final static String RESULT_FILE_CONTENT = "resultFileContent";

    /** The result type in case of opening file result screen */
    public final static String RESULT_TYPE = "resultType";

    /** The input data object */
    public final static String INPUT_DATA = "inputData";

    /** The sheet type id available in the context */
    public final static String SHEET_TYPE_ID = "sheetTypeId";

    /**
     * The resulting sheet type id available in the context
     */
    public final static String RESULT_SHEET_TYPE_ID = "resultSheetTypeId";

    /** True if Refresh needed (sheet needs to be reloaded (TODO) or filter needs to be replayed */
	public final static String RESULT_REFRESH_NEEDED = "refreshNeeded";
	
    /**
     * Execute the command chain associated to an extension point.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pExtPoint
     *            Extension point to execute
     * @param pCtx
     *            Context to pass to the extension point implementation
     */
    public void execute(String pRoleToken, ExtensionPoint pExtPoint,
            Context pCtx);

    /**
     * Execute the command chain associated to an extension point.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pExtensionsContainer
     *            Object containing the extension
     * @param pExtPointName
     *            Name of the extension point to execute (contained in the
     *            container)
     * @param pCtx
     *            Context to pass to the extension point implementation
     */
    public void execute(String pRoleToken,
            ExtensionsContainer pExtensionsContainer, String pExtPointName,
            Context pCtx);

    /**
     * Execute the command chain associated to an extension point.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Identifier of the extensions container object
     * @param pExtPointName
     *            Name of the extension point to execute (contained in the
     *            container)
     * @param pCtx
     *            Context to pass to the extension point implementation (or null
     *            for default context)
     */
    public void execute(String pRoleToken, String pContainerId,
            String pExtPointName, Context pCtx);

    /**
     * Create a new Java action command
     * 
     * @param pAction
     *            Java action to create
     */
    public void createCommand(ActionData pAction);

    /**
     * Create a new scripted command
     * 
     * @param pScript
     *            Script to create
     */
    public void createCommand(ScriptData pScript);

    /**
     * Create several commands
     * 
     * @param pCommands
     *            List of commands to create (must be ActionData or ScriptData)
     */
    public void createCommands(CommandData[] pCommands);

    /**
     * Remove an existing command from the database
     * 
     * @param pCmdName
     *            Name of the command to remove.
     * @param pDelete
     *            If true, delete command in all extension points containing it.
     */
    public void removeCommand(String pCmdName, boolean pDelete);

    /**
     * Remove several commands from the database
     * 
     * @param pCmdNames
     *            List of command names to remove
     * @param pDelete
     *            If true, delete commands in all extension points containing
     *            it.
     */
    public void removeCommands(String[] pCmdNames, boolean pDelete);

    /**
     * Get the content of a given command
     * 
     * @param pCommandName
     *            Name of the command to retrieve.
     * @return The data of the command.
     */
    public CommandData getCommand(String pCommandName);

    /**
     * Get the content of a list of commands
     * 
     * @param pCommandNames
     *            List of the name of commands to retrieve.
     * @return Command create (must be ActionData or ScriptData)
     */
    public Collection<CommandData> getCommands(String[] pCommandNames);

    /**
     * Define an extension point to a container
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pExtensionPointName
     *            Extension point name
     * @param pCommandNames
     *            List of command names to execute in the extension point.
     */
    public void setExtension(String pRoleToken, String pContainerId,
            String pExtensionPointName, List<String> pCommandNames);

    /**
     * Define an extension point with extended attributes
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pExtensionPointName
     *            Extension point name
     * @param pCommandNames
     *            Command names associated to the extension point
     * @param pAttributes
     *            Attributes of the extension point
     * @return the identifier of the newly created extension point.
     */
    public String setExtension(final String pRoleToken,
            final String pContainerId, final String pExtensionPointName,
            final List<String> pCommandNames,
            final List<AttributeData> pAttributes);

    /**
     * Remove an extension defined on an object.
     * <P>
     * This method does nothing if the extension point name does not exist.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pExtensionsContainerId
     *            Identifier of the element containing the extension to remove
     * @param pExtensionPointName
     *            Name of the extension to remove.
     */
    public void removeExtension(String pRoleToken,
            String pExtensionsContainerId, String pExtensionPointName);

    /**
     * Get the command names associated to an extension
     * 
     * @param pExtensionsContainerId
     *            Identifier of the element containing the extension to remove
     * @param pExtensionPointName
     *            Name of the extension to retrieve
     * @return List of command names
     */
    public Collection<String> getExtensionCommandNames(
            String pExtensionsContainerId, String pExtensionPointName);

    /**
     * Get all the extended actions available for the given contexts, in a
     * extensionsContainer.
     * 
     * @param pExtensionsContainerId
     *            the extensionsContainerId
     * @param pContexts
     *            the GUI contexts
     * @return a list of extended actions.
     */
    public List<ExtendedActionData> getAvailableExtendedActions(
            String pExtensionsContainerId, List<GUIContext> pContexts);

    /**
     * Create an extended action in DB.
     * 
     * @param pRoleToken
     *            Role token
     * @param pProcessName
     *            The process name
     * @param pExtendedActionData
     *            Extended action structure data.
     */
    public void createExtendedAction(String pRoleToken, String pProcessName,
            ExtendedActionData pExtendedActionData);

    /**
     * Update an extended action in DB.
     * 
     * @param pRoleToken
     *            Role token.
     * @param pExtendedActionData
     *            Extended action.
     */
    public void updateExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedActionData);

    /**
     * Remove an extended action from DB.
     * 
     * @param pRoleToken
     *            Role token.
     * @param pExtendedActionData
     *            Extended action.
     */
    public void deleteExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedActionData);

    /**
     * Get an extended action from its name
     * 
     * @param pExtensionsContainerId
     *            Identifier of the extensionsContainer.
     * @param pExtendedActionName
     *            Extended action name.
     * @return the extended action, or null if not found.
     */
    public ExtendedActionData getExtendedAction(String pExtensionsContainerId,
            String pExtendedActionName);

    /**
     * Execute an extended action
     * 
     * @param pRoleToken
     *            Role token.
     * @param pExtendedAction
     *            Extended action data.
     * @param pCtx
     *            ExtensionPoint context
     */
    public void executeExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedAction, Context pCtx);

    /**
     * Execute an extended action
     * 
     * @param pRoleToken
     *            the current user role token.
     * @param pExtendedActionName
     *            the extended action name.
     * @param pExtensionsContainerId
     *            the ID of the extensionContainer (if null, the extended action
     *            is supposed to be contained in the business process)
     * @param pSheetId
     *            the current sheet ID (if the action is executed on a sheet).
     * @param pSheetIds
     *            the current sheet IDS (if the action is executed on a filter
     *            result).
     * @param pInputData
     *            the parameter values for action execution.
     * @return an ExtendedActionResult gives parameters according to the
     *         resultingScreen
     */
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pExtensionsContainerId,
            String pSheetId, Collection<String> pSheetIds, InputData pInputData);

    /**
     * Execute an extended action
     * 
     * @param pRoleToken
     *            the current user role token.
     * @param pExtendedActionName
     *            the extended action name.
     * @param pExtensionsContainerId
     *            the ID of the extensionContainer (if null, the extended action
     *            is supposed to be contained in the business process)
     * @param pInputData
     *            the parameter values for action execution.
     * @param pCtx
     *            ExtensionPoint context
     * @return an ExtendedActionResult gives parameters according to the
     *         resultingScreen
     */
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pExtensionsContainerId,
            InputData pInputData, Context pCtx);

    /**
     * Retrieve the full list of extensions defined on a given container.
     * Associate the command names to each extension.
     * 
     * @param pContainerId
     *            Identifier of the container
     * @return Map of Extension point name -> List of commands attached on this
     *         point
     */
    public Map<String, List<String>> getAllExtensions(String pContainerId);

    /**
     * Get all the extendedActions defined in a process
     * 
     * @param pBusinessProcessName
     *            Name of the business process
     * @return a list of all the extendedActions
     */
    public List<ExtendedActionData> getAllExtendedActions(
            String pBusinessProcessName);

    /**
     * Get an extension point from its container and its name
     * 
     * @param pExtensionContainerId
     *            Identifier of the extensions container
     * @param pExtPointName
     *            name of the extension point
     * @return the extension point (as a serialization object)
     */
    public org.topcased.gpm.business.serialization.data.ExtensionPoint getExtensionPointByName(
            String pExtensionContainerId, String pExtPointName);
}

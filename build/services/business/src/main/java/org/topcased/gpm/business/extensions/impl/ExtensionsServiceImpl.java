/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Vincent Hémery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.exception.ExpectedException;
import org.topcased.gpm.business.exception.ExtensionException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.CommandData;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtendedActionData;
import org.topcased.gpm.business.extensions.service.ExtendedActionResult;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.extensions.service.ScriptData;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.performance.PerformanceAnalyzer;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.extensions.Action;
import org.topcased.gpm.domain.extensions.ActionDao;
import org.topcased.gpm.domain.extensions.Command;
import org.topcased.gpm.domain.extensions.CommandDao;
import org.topcased.gpm.domain.extensions.ContextDao;
import org.topcased.gpm.domain.extensions.ExtendedAction;
import org.topcased.gpm.domain.extensions.ExtendedActionDao;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.extensions.ExtensionPointDao;
import org.topcased.gpm.domain.extensions.ExtensionsContainer;
import org.topcased.gpm.domain.extensions.ExtensionsContainerDao;
import org.topcased.gpm.domain.extensions.Script;
import org.topcased.gpm.domain.extensions.ScriptDao;
import org.topcased.gpm.domain.fields.InputDataType;

/**
 * Extensions service implementation.
 * 
 * @author llatil
 */
public class ExtensionsServiceImpl extends ServiceImplBase implements
        ExtensionsService {
    /** The log4j logger object for this class. */
    private static Logger staticLogger =
            Logger.getLogger(ExtensionsServiceImpl.class);

    /** Map class name to the class instance */
    private final Map<String, Class<GDMExtension>> clazzMap =
            new HashMap<String, Class<GDMExtension>>();

    /**
     * Check if an extension point is enabled in the given context or not.
     * 
     * @param pExtPointName
     *            Name of the extension point
     * @param pContext
     *            Current execution context (can be null)
     * @return true if the extension point is enabled (and must be invoked).
     */
    // TODO declare in interface
    public final boolean isExtensionPointEnabled(final String pExtPointName,
            final Context pContext) {
        return isEnabled(pExtPointName, pContext, Context.GPM_SKIP_EXT_PTS);
    }

    /**
     * Check if an extension point is enabled in the given context or not.
     * 
     * @param pExtPointName
     *            Name of the extension point
     * @param pContext
     *            Current execution context (can be null)
     * @param pParamName
     *            Name of the context parameter
     * @return true if the extension point is enabled (and must be invoked).
     */
    // TODO declare in interface
    @SuppressWarnings("unchecked")
    public final boolean isEnabled(final String pExtPointName,
            final Context pContext, final String pParamName) {

        // Check if the context contains the param
        if (null == pContext || !pContext.contains(pParamName)) {
            return true;
        }

        final Object lValue = pContext.get(pParamName);
        if (Boolean.TRUE.equals(lValue) || Boolean.FALSE.equals(lValue)) {
            return !(Boolean) lValue;
        }

        if (lValue instanceof Collection) {
            Collection<String> lSkipExtensionsList =
                    (Collection<String>) lValue;
            return !lSkipExtensionsList.contains(pExtPointName);
        }

        if (lValue instanceof String[]) {
            return !ArrayUtils.contains((String[]) lValue, pExtPointName);
        }
        return true;
    }

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
    @Override
    @SuppressWarnings("unchecked")
    public void execute(final String pRoleToken, final ExtensionPoint pExtPoint, final Context pCtx) {

        if (!isEnabled(pExtPoint.getName(), pCtx, Context.GPM_SKIP_EXT_PTS)) {
            return;
        }

        final String lRoleName = getAuthService().getRoleNameFromToken(pRoleToken);
        final String lProcessName = getAuthService().getProcessNameFromToken(pRoleToken);
        String lProductName = getAuthService().getProductNameFromSessionToken(pRoleToken);

        String lUserToken = getAuthService().getUserSessionFromRoleSession(pRoleToken);

        Context lCtx = new ContextBase(pCtx);

        if (lProductName == null) {
            if (pCtx != null) {
                CacheableSheet lCacheableSheet = pCtx.get(ExtensionPointParameters.SHEET);
                if (lCacheableSheet != null && lCacheableSheet.getProductName() != null) {
                    lProductName = lCacheableSheet.getProductName();
                }
            }
        }

        // Add the name of the extension point to the context,
        // service locator and some services entry points.
        lCtx.put(ExtensionPointParameters.PROCESS_NAME, lProcessName);
        lCtx.put(ExtensionPointParameters.EXTENSION_POINT_NAME, pExtPoint.getName());
        lCtx.put(ExtensionPointParameters.SERVICE_LOCATOR, ServiceLocator.instance());
        lCtx.put(ExtensionPointParameters.ROLE_TOKEN, pRoleToken);
        lCtx.put(ExtensionPointParameters.USER_TOKEN, lUserToken);
        lCtx.put(ExtensionPointParameters.ROLE_NAME, lRoleName);
        lCtx.put(ExtensionPointParameters.ADMIN_ROLE_TOKEN, getAuthService().getAdminRoleToken(lProcessName));
        if (lCtx.get(ExtensionPointParameters.PRODUCT_NAME) == null) {
        	lCtx.put(ExtensionPointParameters.PRODUCT_NAME, lProductName);
        }

        for (Attribute lAttribute : pExtPoint.getAttributes()) {
            if (lAttribute.getAttributeValues() != null) {
                if (lAttribute.getAttributeValues().size() == 1) {
                    AttributeValue lAttrValue = lAttribute.getAttributeValues().toArray(new AttributeValue[0])[0];
                    if (lAttrValue != null) {
                        lCtx.put(lAttribute.getName(), lAttrValue.getValue());
                    }
                }
                else {
                    String[] lAttributeValues = new String[lAttribute.getAttributeValues().size()];
                    int i = 0;
                    for (AttributeValue lAttributeValue : lAttribute.getAttributeValues()) {
                        lAttributeValues[i] = lAttributeValue.getValue();
                        i++;
                    }
                }
            }
        }

        Object lSkipCommands = lCtx.get(Context.GPM_SKIP_COMMANDS);
        Collection<String> lSkipCommandsList;

        if (lSkipCommands instanceof Collection<?>) {
            lSkipCommandsList = (Collection<String>) lSkipCommands;
        }
        else {
            lSkipCommandsList = Collections.EMPTY_SET;
        }

        if (lCtx.get(ExtensionPointParameters.VALUES_CONTAINER) instanceof CacheableValuesContainer) {
            CacheableValuesContainer lCacheableValuesContainer =
            		lCtx.get(ExtensionPointParameters.VALUES_CONTAINER);
            if (lCacheableValuesContainer != null) {
                Set<String> lCommandsToExlucde = lCacheableValuesContainer.getExtentionPointsToExclude();
                if (lSkipCommandsList.isEmpty()) {
                    lSkipCommandsList = new ArrayList<String>();
                }
                if (lCommandsToExlucde != null) {
                    lSkipCommandsList.addAll(lCommandsToExlucde);
                }
            }
        }

        // Execute all commands defined for the given extension point.
        for (Command lCmd : pExtPoint.getCommands()) {

            // Check if the current command is to be skipped or not.
            if (lSkipCommandsList.contains(lCmd.getName())) {
                if (staticLogger.isDebugEnabled()) {
                    staticLogger.debug("Execution of command '"
                            + lCmd.getName() + "' is skipped");
                }
                continue;
            }

            if (staticLogger.isDebugEnabled()) {
                staticLogger.debug("Executing command '" + lCmd.getName() + "'");
            }

            if (lCmd instanceof Action) {
                Action lAction = (Action) lCmd;

                Class<GDMExtension> lClazz = getClass(lAction.getClassName());
                GDMExtension lExtPoint = null;

                try {
                    lExtPoint = lClazz.newInstance();
                }
                catch (InstantiationException e) {
                    throw new ExtensionException(pExtPoint.getName(), e);
                }
                catch (IllegalAccessException e) {
                    throw new ExtensionException(pExtPoint.getName(), e);
                }

                try {
                    performanceAnalyzer.startAnalyze("Extension Point "
                            + lCmd.getName(),
                            PerformanceAnalyzer.EXTENSION_POINT_LEVEL);

                    lExtPoint.execute(lCtx);
                }
                // Catch GDM Exception
                catch (GDMException e) {
                    // Expected exception corresponds to extensions points exception
                    // => encapsulate this exception in an Extension Exception.
                    if (e instanceof ExpectedException) {
                        throw new ExtensionException(pExtPoint.getName(), e);
                    }
                    // All others exceptions do not need to be encapsulated.
                    throw e;
                }
                // Catch others exceptions (encapsulated in an extension exception)
                catch (Throwable e) {
                    staticLogger.info("Action '" + lAction.getName()
                            + "' raised an exception.", e);
                    throw new ExtensionException(pExtPoint.getName(), e);
                }
                finally {
                    performanceAnalyzer.endAnalyze(PerformanceAnalyzer.EXTENSION_POINT_LEVEL);
                }
            }
            else if (lCmd instanceof Script) {
                Script lScript = (Script) lCmd;

                BSFManager lBSFMgr = new BSFManager();
                try {
                    lBSFMgr.declareBean("context", lCtx, Context.class);

                    performanceAnalyzer.startAnalyze("Extension Point "
                            + lCmd.getName(),
                            PerformanceAnalyzer.EXTENSION_POINT_LEVEL);

                    lBSFMgr.exec(lScript.getLanguage(), pExtPoint.getName()
                            + " [script:" + lScript.getName() + "]", 0, 0,
                            lScript.getScript());
                    lCtx.get("sheetData");
                    lCtx = (Context) lBSFMgr.lookupBean("context");
                }
                catch (BSFException e) {
                    staticLogger.info("Script '" + lScript.getName()
                            + "' raised an exception.", e);
                    throw new ExtensionException(pExtPoint.getName(), e);
                }
                finally {
                    performanceAnalyzer.endAnalyze(PerformanceAnalyzer.EXTENSION_POINT_LEVEL);
                }
            }
            else {
                staticLogger.error("Command type not supported");
                throw new GDMException("Command type not supported");
            }
        }
    }

    /**
     * Execute the command chain associated to an extension point.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pExtensionsContainer
     *            Extensions container object
     * @param pExtPointName
     *            Name of the extension point to execute (contained in the
     *            container)
     * @param pCtx
     *            Context to pass to the extension point implementation
     */
    @Override
    public void execute(final String pRoleToken,
            final ExtensionsContainer pExtensionsContainer,
            final String pExtPointName, final Context pCtx) {
        // Check if the extension point is defined
        ExtensionPoint lExtPoint;
        lExtPoint =
                getExtensionsContainerDao().getExtensionPoint(
                        pExtensionsContainer, pExtPointName);

        if (null != lExtPoint) {
            // If defined, let's execute it.
            execute(pRoleToken, lExtPoint, pCtx);
        }
    }

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
     *            Context to pass to the extension point implementation
     */
    @Override
    public void execute(final String pRoleToken, final String pContainerId,
            final String pExtPointName, final Context pCtx) {

        ExtensionsContainer lExtContainer =
                getExtensionsContainer(pContainerId);
        // Check if the extension point is defined
        ExtensionPoint lExtPoint;
        lExtPoint =
                getExtensionsContainerDao().getExtensionPoint(lExtContainer,
                        pExtPointName);

        if (null != lExtPoint) {
            // If defined, let's execute it.
            execute(pRoleToken, lExtPoint, pCtx);
        }
    }

    /**
     * Create a new Java action command
     * 
     * @param pActionData
     *            Java action to create
     */
    @Override
    public void createCommand(final ActionData pActionData) {
        Command lCmd = getCommandDao().getCommand(pActionData.getName());
        Action lAction;

        if (null != lCmd) {
            if (!(lCmd instanceof Action)) {
                throw new GDMException(
                        "A command named '"
                                + pActionData.getName()
                                + "' already "
                                + "exist in database with a different type. Impossible to update.");
            }
            // else
            lAction = (Action) lCmd;
            lAction.setClassName(pActionData.getClassName());
        }
        else {
            lAction = Action.newInstance();
            lAction.setName(pActionData.getName());
            lAction.setClassName(pActionData.getClassName());

            getActionDao().create(lAction);
        }
    }

    /**
     * Create a new scripted command
     * 
     * @param pScriptData
     *            Script to create
     */
    @Override
    public void createCommand(final ScriptData pScriptData) {
        Command lCmd = getCommandDao().getCommand(pScriptData.getName());
        Script lScript;

        if (null != lCmd) {
            if (!(lCmd instanceof Script)) {
                throw new GDMException(
                        "A command named '"
                                + pScriptData.getName()
                                + "' already "
                                + "exist in database with a different type. Impossible to update.");
            }
            // else
            lScript = (Script) lCmd;
            lScript.setLanguage(pScriptData.getLanguage());
            lScript.setScript(pScriptData.getCode());
        }
        else {
            lScript = Script.newInstance();

            lScript.setName(pScriptData.getName());
            lScript.setLanguage(pScriptData.getLanguage());
            lScript.setScript(pScriptData.getCode());

            getScriptDao().create(lScript);
        }
    }

    /**
     * Create several commands
     * 
     * @param pCommands
     *            List of commands to create (must be ActionData or ScriptData)
     */
    @Override
    public void createCommands(final CommandData[] pCommands) {
        for (int i = 0; i < pCommands.length; ++i) {
            CommandData lCmd = pCommands[i];
            if (lCmd instanceof ScriptData) {
                createCommand((ScriptData) lCmd);
            }
            else if (lCmd instanceof ActionData) {
                createCommand((ActionData) lCmd);
            }
            else {
                throw new GDMException("Command type unsupported");
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService
     *      #removeCommands(java.lang.String[], boolean)
     */
    @Override
    public void removeCommands(final String[] pCmdNames,
            final boolean pDeleteInAll) {
        for (int i = 0; i < pCmdNames.length; ++i) {
            removeCommand(pCmdNames[i], pDeleteInAll);
        }
    }

    /**
     * Remove an existing command from the database
     * 
     * @param pCmdName
     *            Name of the command to remove
     * @param pDeleteInAll
     *            Delete in all extension points that contain this command.
     */
    @Override
    public void removeCommand(final String pCmdName, boolean pDeleteInAll) {
        Command lCmd = getCommandDao().getCommand(pCmdName);

        if (null != lCmd) {
            List<ExtensionPoint> lExtensionPoints =
                    getCommandDao().getExtensionPointsWithCommand(pCmdName);

            if (!pDeleteInAll && lExtensionPoints.size() > 0) {
                throw new GDMException("The command '" + pCmdName
                        + "' cannot be deleted. It is used in "
                        + lExtensionPoints.size() + " extensionPoints.");
            }

            for (ExtensionPoint lExtensionPoint : lExtensionPoints) {
                if (pDeleteInAll) {
                    lExtensionPoint.removeFromCommandList(lCmd);
                }
            }
            getCommandDao().remove(lCmd);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#setExtension(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void setExtension(final String pRoleToken,
            final String pContainerId, final String pExtensionPointName,
            final List<String> pCommandNames) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        ExtensionsContainer lContainer = getExtensionsContainer(pContainerId);
        setExtensionPoint(lContainer, pExtensionPointName, pCommandNames, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#setExtension(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public String setExtension(final String pRoleToken,
            final String pContainerId, final String pExtensionPointName,
            final List<String> pCommandNames,
            final List<AttributeData> pAttributes) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        ExtensionsContainer lContainer = getExtensionsContainer(pContainerId);
        return setExtensionPoint(lContainer, pExtensionPointName,
                pCommandNames, pAttributes);
    }

    private String setExtensionPoint(
            final ExtensionsContainer pExtensionsContainer,
            final String pExtensionPointName, final List<String> pCommandNames,
            final List<AttributeData> pAttributes) {

        ExtensionPoint lExtPoint;

        lExtPoint =
                getExtensionsContainerDao().getExtensionPoint(
                        pExtensionsContainer, pExtensionPointName);

        // If an extension point with this name already exist, remove it first.
        if (null != lExtPoint) {
            pExtensionsContainer.removeFromExtensionPointList(lExtPoint);
            getExtensionPointDao().remove(lExtPoint);
        }

        // Create the extension point.
        lExtPoint = ExtensionPoint.newInstance();
        lExtPoint.setName(pExtensionPointName);

        for (String lCommandName : pCommandNames) {
            Command lCmd = getCommandDao().getCommand(lCommandName);

            if (null == lCmd) {
                throw new InvalidNameException(lCommandName,
                        "Command {0} invalid");
            }
            lExtPoint.addToCommandList(lCmd);
        }

        getExtensionPointDao().create(lExtPoint);
        pExtensionsContainer.addToExtensionPointList(lExtPoint);

        String lExtensionPointId = lExtPoint.getId();
        if (pAttributes != null) {
            getAttributesService().set(lExtensionPointId,
                    pAttributes.toArray(new AttributeData[pAttributes.size()]));
        }

        removeElementFromCache(pExtensionsContainer.getId(), true);
        return lExtensionPointId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getCommand(java.lang.String)
     */
    @Override
    public CommandData getCommand(final String pCommandName) {
        Command lCmd = commandDao.getCommand(pCommandName);
        if (null == lCmd) {
            throw new InvalidNameException(pCommandName);
        }

        CommandData lCmdData = null;

        if (lCmd instanceof Action) {
            Action lAction = (Action) lCmd;
            ActionData lActionData = new ActionData();
            lActionData.setName(lAction.getName());
            lActionData.setClassName(lAction.getClassName());
            lCmdData = lActionData;
        }
        else {
            Script lScript = (Script) lCmd;
            ScriptData lScriptData = new ScriptData();

            lScriptData.setName(lScript.getName());
            lScriptData.setLanguage(lScript.getLanguage());
            lScriptData.setCode(lScript.getScript());

            lCmdData = lScriptData;
        }
        return lCmdData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getCommands(java.lang.String[])
     */
    @Override
    public Collection<CommandData> getCommands(final String[] pCommandNames) {
        Collection<CommandData> lCmdData =
                new ArrayList<CommandData>(pCommandNames.length);
        for (String lName : pCommandNames) {
            lCmdData.add(getCommand(lName));
        }
        return lCmdData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getExtensionCommandNames(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @Override
    public Collection<String> getExtensionCommandNames(
            final String pExtensionsContainerId,
            final String pExtensionPointName) {
        ExtensionsContainer lContainer =
                getExtensionsContainer(pExtensionsContainerId);
        return getExtensionCommandNames(lContainer, pExtensionPointName);
    }

    private Collection<String> getExtensionCommandNames(
            final ExtensionsContainer pExtensionsContainer,
            final String pExtensionPointName) {
        ExtensionPoint lExtPoint;
        lExtPoint =
                extensionsContainerDao.getExtensionPoint(pExtensionsContainer,
                        pExtensionPointName);

        Collection<Command> lCmdList = lExtPoint.getCommands();
        Collection<String> lNames = new ArrayList<String>(lCmdList.size());
        for (Command lCmd : lCmdList) {
            lNames.add(lCmd.getName());
        }
        return lNames;
    }

    /**
     * Retrieve the full list of extensions defined on a given container.
     * Returns a map to associate the command names to each extension.
     * 
     * @param pContainerId
     *            Identifier of the container
     * @return Map of Extension point name -> List of commands attached on this
     *         point (keys sorted alphabetically), or null if no defined
     *         extension points
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<String>> getAllExtensions(String pContainerId) {

        final Collection<ExtensionPoint> lExtensionPoints =
                getExtensionsContainerDao().getAllExtensionPoints(pContainerId);

        if (lExtensionPoints.size() == 0) {
            return null;
        }
        // else
        Map<String, List<String>> lExtMap =
                new LinkedHashMap<String, List<String>>();
        for (ExtensionPoint lExtPoint : lExtensionPoints) {
            Collection<Command> lCommands = lExtPoint.getCommands();
            List<String> lCommandNamesList =
                    new ArrayList<String>(lCommands.size());

            for (Command lCmd : lCommands) {
                lCommandNamesList.add(lCmd.getName());
            }
            lExtMap.put(lExtPoint.getName(), lCommandNamesList);
        }
        return lExtMap;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#removeExtension(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @Override
    public void removeExtension(final String pToken,
            final String pExtensionsContainerId,
            final String pExtensionPointName) {
        getAuthService().assertGlobalAdminRole(pToken);
        ExtensionsContainer lExtensionContainer;
        lExtensionContainer = getExtensionsContainer(pExtensionsContainerId);

        removeExtension(lExtensionContainer, pExtensionPointName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#removeExtension(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    private void removeExtension(
            final ExtensionsContainer pExtensionsContainer,
            final String pExtensionPointName) {
        ExtensionPoint lExtPoint =
                extensionsContainerDao.getExtensionPoint(pExtensionsContainer,
                        pExtensionPointName);
        if (null == lExtPoint) {
            return;
        }

        pExtensionsContainer.removeFromExtensionPointList(lExtPoint);
        getExtensionPointDao().remove(lExtPoint);
    }

    /**
     * Get an extensions container instance from its unique identifier
     * 
     * @param pContainerId
     *            Identifier of the container
     * @return Extensions container object
     * @throws InvalidIdentifierException
     *             If no container with given identifier exist
     */
    private ExtensionsContainer getExtensionsContainer(final String pContainerId) {
        AttributesContainer lContainer;
        lContainer = extensionsContainerDao.load(pContainerId);

        if (!(lContainer instanceof ExtensionsContainer)) {
            throw new InvalidIdentifierException(pContainerId);
        }
        return (ExtensionsContainer) lContainer;
    }

    /**
     * Get a Java class of extension point from its name
     * 
     * @param pClassName
     *            Name of the java class
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<GDMExtension> getClass(final String pClassName) {
        Class<GDMExtension> lClazz;

        ClassLoader lClassLoader = getClassLoader();

        lClazz = clazzMap.get(pClassName);
        if (null == lClazz) {
            // Load the class
            try {
                lClazz =
                        (Class<GDMExtension>) lClassLoader.loadClass(pClassName);
            }
            catch (ClassNotFoundException e) {
                throw new InvalidNameException(pClassName,
                        "Class {0} cannot be loaded");
            }

            if (!GDMExtension.class.isAssignableFrom(lClazz)) {
                throw new GDMException("Class " + pClassName
                        + " must implements GDMExtension interface");
            }

            // Store it in the map
            clazzMap.put(pClassName, lClazz);
        }
        return lClazz;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getAvailableExtendedActions(java.lang.String,
     *      org.topcased.gpm.common.extensions.GUIContext)
     */
    @Override
    public List<ExtendedActionData> getAvailableExtendedActions(
            String pExtensionsContainerId, List<GUIContext> pContexts) {

        List<ExtendedAction> lExtendedActions =
                getExtendedActionDao().getExtendedActions(
                        pExtensionsContainerId, pContexts);
        if (lExtendedActions == null) {
            return null;
        }
        List<ExtendedActionData> lExtendedActionDatas =
                new ArrayList<ExtendedActionData>();
        for (ExtendedAction lExtendedAction : lExtendedActions) {
            lExtendedActionDatas.add(getExtendedActionData(lExtendedAction));
        }
        return lExtendedActionDatas;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getAllExtendedActions(java.lang.String,
     *      org.topcased.gpm.common.extensions.GUIContext)
     */
    @Override
    public List<ExtendedActionData> getAllExtendedActions(
            String pBusinessProcessName) {

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);

        Collection<ExtendedAction> lExtendedActions =
                getExtendedActionDao().getAllExtendedActions(lBusinessProcess);
        if (lExtendedActions == null) {
            return Collections.emptyList();
        }
        List<ExtendedActionData> lExtendedActionDatas =
                new ArrayList<ExtendedActionData>();
        for (ExtendedAction lExtendedAction : lExtendedActions) {
            lExtendedActionDatas.add(getExtendedActionData(lExtendedAction));
        }
        return lExtendedActionDatas;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#createExtendedAction(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.ExtendedActionData)
     */
    @Override
    public void createExtendedAction(String pRoleToken, String pProcessName,
            ExtendedActionData pExtendedActionData) {

        if (null != getExtendedActionDao().getExtendedAction(
                pExtendedActionData.getExtensionsContainerId(),
                pExtendedActionData.getName())) {
            throw new GDMException("An extended action with name '"
                    + pExtendedActionData.getName() + "' already exists in DB.");
        }

        ExtendedAction lExtendedAction = ExtendedAction.newInstance();
        lExtendedAction.setName(pExtendedActionData.getName());
        lExtendedAction.setActionOrder(pExtendedActionData.getIndex());
        lExtendedAction.setMenuEntryName(pExtendedActionData.getMenuEntryName());
        lExtendedAction.setMenuEntryParentName(pExtendedActionData.getMenuEntryParentName());
        ExtensionsContainer lExtensionsContainer =
                (ExtensionsContainer) getExtensionsContainerDao().load(
                        pExtendedActionData.getExtensionsContainerId());
        lExtendedAction.setExtensionsContainer(lExtensionsContainer);
        lExtendedAction.setExtensionPointName(pExtendedActionData.getExtensionPointName());
        lExtendedAction.setConfirmationMessage(pExtendedActionData.getConfirmationMessage());
        if (pExtendedActionData.getInputDataTypeName() != null) {

            InputDataType lInputDataType =
                    (InputDataType) getFieldsContainer(pProcessName,
                            pExtendedActionData.getInputDataTypeName(), true);
            lExtendedAction.setInputDataType(lInputDataType);
        }
        if (pExtendedActionData.getContexts() != null) {
            for (GUIContext lGuiContext : pExtendedActionData.getContexts()) {
                org.topcased.gpm.domain.extensions.Context lContext =
                        getContextDao().getContext(lGuiContext);
                if (lContext == null) {
                    lContext =
                            org.topcased.gpm.domain.extensions.Context.newInstance();
                    lContext.setGuiContext(lGuiContext);
                    getContextDao().create(lContext);
                }
                lExtendedAction.addToContextList(lContext);
            }
        }
        getExtendedActionDao().create(lExtendedAction);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#updateExtendedAction(java.lang.String,
     *      org.topcased.gpm.business.extensions.service.ExtendedActionData)
     */
    @Override
    public void updateExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedActionData) {
        ExtendedAction lExtendedAction =
                getExtendedActionDao().getExtendedAction(
                        pExtendedActionData.getExtensionsContainerId(),
                        pExtendedActionData.getName());
        lExtendedAction.setName(pExtendedActionData.getName());
        lExtendedAction.setMenuEntryName(pExtendedActionData.getMenuEntryName());
        lExtendedAction.setMenuEntryParentName(pExtendedActionData.getMenuEntryParentName());
        lExtendedAction.setExtensionsContainer((ExtensionsContainer) getExtensionsContainerDao().load(
                pExtendedActionData.getExtensionsContainerId()));
        lExtendedAction.setExtensionPointName(pExtendedActionData.getExtensionPointName());

        for (GUIContext lGuiContext : pExtendedActionData.getContexts()) {
            org.topcased.gpm.domain.extensions.Context lContext =
                    getContextDao().getContext(lGuiContext);
            if (lContext == null) {
                lContext =
                        org.topcased.gpm.domain.extensions.Context.newInstance();
                lContext.setGuiContext(lGuiContext);
                getContextDao().create(lContext);
            }
            lExtendedAction.addToContextList(lContext);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#deleteExtendedAction(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.ExtendedActionData)
     */
    @Override
    public void deleteExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedActionData) {
        ExtendedAction lExtendedAction =
                getExtendedActionDao().getExtendedAction(
                        pExtendedActionData.getExtensionsContainerId(),
                        pExtendedActionData.getName());
        if (lExtendedAction != null) {
            getExtendedActionDao().remove(lExtendedAction);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getExtendedAction(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ExtendedActionData getExtendedAction(String pExtensionsContainerId,
            String pExtendedActionName) {
        ExtendedAction lExtendedAction =
                getExtendedActionDao().getExtendedAction(
                        pExtensionsContainerId, pExtendedActionName);
        if (null == lExtendedAction) {
            return null;
        }
        // else
        return getExtendedActionData(lExtendedAction);
    }

    private ExtendedActionData getExtendedActionData(
            ExtendedAction pExtendedAction) {
        ExtendedActionData lExtendedActionData = new ExtendedActionData();
        lExtendedActionData.setName(pExtendedAction.getName());
        List<GUIContext> lContexts = new ArrayList<GUIContext>();
        for (org.topcased.gpm.domain.extensions.Context lContext : pExtendedAction.getContexts()) {
            lContexts.add(lContext.getGuiContext());
        }
        lExtendedActionData.setContexts(lContexts);
        lExtendedActionData.setExtensionPointName(pExtendedAction.getExtensionPointName());
        lExtendedActionData.setConfirmationMessage(pExtendedAction.getConfirmationMessage());
        lExtendedActionData.setMenuEntryName(pExtendedAction.getMenuEntryName());
        lExtendedActionData.setMenuEntryParentName(pExtendedAction.getMenuEntryParentName());
        lExtendedActionData.setExtensionsContainerId(pExtendedAction.getExtensionsContainer().getId());

        if (pExtendedAction.getInputDataType() != null) {
            lExtendedActionData.setInputDataTypeName(pExtendedAction.getInputDataType().getName());
        }
        return lExtendedActionData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#executeExtendedAction(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.ExtendedActionData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void executeExtendedAction(String pRoleToken,
            ExtendedActionData pExtendedAction, Context pCtx) {
        final ExtensionPoint lExtensionPoint =
                getExecutableExtensionPoint(
                        pExtendedAction.getExtensionsContainerId(),
                        pExtendedAction.getExtensionPointName(), pCtx);

        if (lExtensionPoint == null) {
            throw new ExpectedException("Extension point '"
                    + pExtendedAction.getExtensionPointName() + "' not found .");
        }

        execute(pRoleToken, lExtensionPoint, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService
     *      #executeExtendedAction(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.util.Collection,
     *      org.topcased.gpm.business.serialization.data.InputData)
     */
    @Override
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pExtensionsContainerId,
            String pSheetId, Collection<String> pSheetIds, InputData pInputData) {
        // Create a context with available extra information
        Context lContext = new ContextBase();
        List<String> lValuesContainerIds = null;
        if (pSheetIds != null) {
            lValuesContainerIds = new ArrayList<String>(pSheetIds);
        }
        lContext.put(ExtensionPointParameters.SHEET_ID, pSheetId).put(
                ExtensionPointParameters.SHEET_IDS, lValuesContainerIds);
        return executeExtendedAction(pRoleToken, pExtendedActionName,
                pExtensionsContainerId, pInputData, lContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#executeExtendedAction(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.serialization.data.InputData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pExtensionsContainerId,
            InputData pInputData, Context pCtx) {
        ExtendedActionData lExtendedAction;

        // If pExtensionsContainerId is not filled, we consider that the extensions container
        // is the Business process.
        if (pExtensionsContainerId != null) {
            lExtendedAction =
                    getExtendedAction(pExtensionsContainerId,
                            pExtendedActionName);
        }
        else {
            lExtendedAction =
                    getExtendedActionOnProcess(pRoleToken, pExtendedActionName);
        }

        final ExtensionPoint lExtensionPoint =
                getExecutableExtensionPoint(
                        lExtendedAction.getExtensionsContainerId(),
                        lExtendedAction.getExtensionPointName(), pCtx);

        if (lExtensionPoint == null) {
            throw new ExpectedException("Extension point '"
                    + lExtendedAction.getExtensionPointName() + "' not found .");
        }

        // create the execution context
        Context lContext = new ContextBase(pCtx);

        CacheableInputDataType lInputDataType =
                getFieldsService().getCacheableInputDataType(pRoleToken,
                        lExtendedAction.getInputDataTypeName(),
                        getAuthService().getProcessNameFromToken(pRoleToken));
        CacheableInputData lCacheableInputData = null;
        if (pInputData != null) {
            lCacheableInputData =
                    new CacheableInputData(pInputData, lInputDataType);
        }

        lContext.put(ExtensionPointParameters.INPUT_DATA.getParameterName(),
                lCacheableInputData);

        String[] lResultParameters =
                new String[] { ERROR_MESSAGE, MESSAGE, RESULT_MESSAGE,
                              RESULT_SCREEN, RESULT_SHEET_ID, RESULT_SHEET_IDS,
                              RESULT_SHEET_TYPE_ID, RESULT_SORTER_NAME,
                              RESULT_SUMMARY_NAME, RESULT_PRODUCT_NAME,
                              RESULT_SCOPE, RESULT_FILE_CONTENT, RESULT_TYPE,
                              RESULT_FILTER_TYPE, RESULT_REFRESH_NEEDED };
        lContext.put(lResultParameters);

        execute(pRoleToken, lExtensionPoint, lContext);

        ExtendedActionResult lExtendedActionResult =
                new ExtendedActionResult(lContext);
        return lExtendedActionResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.ExtensionsService#getExtensionPointByName(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.ExtensionPoint getExtensionPointByName(
            String pExtensionContainerId, String pExtPointName) {
        org.topcased.gpm.business.serialization.data.ExtensionPoint lExtensionPoint =
                new org.topcased.gpm.business.serialization.data.ExtensionPoint();
        ExtensionPoint lExtPointEntity =
                getExecutableExtensionPoint(pExtensionContainerId,
                        pExtPointName, null);

        // Add extension point attributes
        List<org.topcased.gpm.business.serialization.data.Attribute> lAttributes =
                new ArrayList<org.topcased.gpm.business.serialization.data.Attribute>();
        for (Attribute lAttribute : lExtPointEntity.getAttributes()) {
            List<org.topcased.gpm.business.serialization.data.AttributeValue> lAttributesValues =
                    new ArrayList<org.topcased.gpm.business.serialization.data.AttributeValue>();
            for (AttributeValue lAttributeValue : lAttribute.getAttributeValues()) {
                lAttributesValues.add(new org.topcased.gpm.business.serialization.data.AttributeValue(
                        lAttributeValue.getValue()));
            }
            org.topcased.gpm.business.serialization.data.Attribute lNewAttribute =
                    new org.topcased.gpm.business.serialization.data.Attribute(
                            lAttribute.getName(), lAttributesValues);
            lAttributes.add(lNewAttribute);
        }
        lExtensionPoint.setAttributes(lAttributes);

        // Add extension point commands
        List<NamedElement> lCommands = new ArrayList<NamedElement>();
        for (Command lCommand : lExtPointEntity.getCommands()) {
            lCommands.add(new NamedElement(lCommand.getName()));
        }
        lExtensionPoint.setCommands(lCommands);

        // Set extension point name and ID
        lExtensionPoint.setName(lExtPointEntity.getName());
        lExtensionPoint.setId(lExtPointEntity.getId());

        return lExtensionPoint;
    }

    private ExtendedActionData getExtendedActionOnProcess(String pRoleToken,
            String pName) {
        String lProcessId =
                getAuthService().getRole(pRoleToken).getBusinessProcess().getId();
        return getExtendedAction(lProcessId, pName);
    }

    private ClassLoader getClassLoader() {
        return ExtensionsServiceImpl.class.getClassLoader();
    }

    private CommandDao commandDao;

    private ActionDao actionDao;

    private ScriptDao scriptDao;

    /**
     * Get the Action DAO
     * 
     * @return Returns the actionDao.
     */
    public ActionDao getActionDao() {
        return actionDao;
    }

    /**
     * set the Action DAO
     * 
     * @param pActionDao
     *            The actionDao to set.
     */
    public void setActionDao(final ActionDao pActionDao) {
        actionDao = pActionDao;
    }

    /**
     * Get the Script DAO
     * 
     * @return Returns the scriptDao.
     */
    public ScriptDao getScriptDao() {
        return scriptDao;
    }

    /**
     * Set the Script DAO
     * 
     * @param pScriptDao
     *            The scriptDao to set.
     */
    public void setScriptDao(final ScriptDao pScriptDao) {
        scriptDao = pScriptDao;
    }

    /**
     * Get the Command DAO
     * 
     * @return Returns the commandDao.
     */
    public CommandDao getCommandDao() {
        return commandDao;
    }

    /**
     * Set the Command DAO
     * 
     * @param pCommandDao
     *            The commandDao to set.
     */
    public void setCommandDao(final CommandDao pCommandDao) {
        commandDao = pCommandDao;
    }

    private ContextDao contextDao;

    /**
     * getContextDao
     * 
     * @return the contextDao
     */
    public ContextDao getContextDao() {
        return contextDao;
    }

    /**
     * setContextDao
     * 
     * @param pContextDao
     *            the contextDao to set
     */
    public void setContextDao(ContextDao pContextDao) {
        contextDao = pContextDao;
    }

    private ExtensionsContainerDao extensionsContainerDao;

    /**
     * Get the ExtensionsContainer DAO
     * 
     * @return Returns the extensionsContainerDao.
     */
    @Override
    public ExtensionsContainerDao getExtensionsContainerDao() {
        return extensionsContainerDao;
    }

    /**
     * Set the ExtensionsContainer DAO
     * 
     * @param pExtensionsContainerDao
     *            The extensionsContainerDao to set.
     */
    @Override
    public void setExtensionsContainerDao(
            final ExtensionsContainerDao pExtensionsContainerDao) {
        extensionsContainerDao = pExtensionsContainerDao;
    }

    private ExtensionPointDao extensionPointDao;

    /**
     * Get the ExtensionsPoint DAO
     * 
     * @return Returns the extensionPointDao.
     */
    public ExtensionPointDao getExtensionPointDao() {
        return extensionPointDao;
    }

    /**
     * Set the ExtensionsPoint DAO
     * 
     * @param pExtensionPointDao
     *            The extensionPointDao to set.
     */
    public void setExtensionPointDao(final ExtensionPointDao pExtensionPointDao) {
        extensionPointDao = pExtensionPointDao;
    }

    private ExtendedActionDao extendedActionDao;

    /**
     * getExtendedActionDao
     * 
     * @return the ExtendedActionDao
     */
    public ExtendedActionDao getExtendedActionDao() {
        return extendedActionDao;
    }

    /**
     * setExtendedActionDao
     * 
     * @param pExtendedActionDao
     *            the ExtendedActionDao to set
     */
    public void setExtendedActionDao(ExtendedActionDao pExtendedActionDao) {
        extendedActionDao = pExtendedActionDao;
    }

    private PerformanceAnalyzer performanceAnalyzer;

    /**
     * Setter on the analyzer for Spring injection.
     * 
     * @param pPerformanceAnalyser
     *            The analyzer.
     */
    public void setPerformanceAnalyzer(
            final PerformanceAnalyzer pPerformanceAnalyser) {
        performanceAnalyzer = pPerformanceAnalyser;
    }
}

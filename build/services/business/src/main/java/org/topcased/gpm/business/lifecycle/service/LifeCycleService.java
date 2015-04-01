/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.serialization.data.TransitionData;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * Lifecycle service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface LifeCycleService {

    /**
     * Create a new process definition in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Name of business process
     * @param pInputStream
     *            Stream containing the XML def. of the process.
     */
    public void createProcessDefinition(String pRoleToken,
            String pBusinessProcessName, java.io.InputStream pInputStream);

    /**
     * Create a new process definition in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Name of business process
     * @param pDefinitionContent
     *            Contains the JBPM XML definition of the process.
     */
    public void createProcessDefinition(String pRoleToken,
            String pBusinessProcessName, byte[] pDefinitionContent);

    /**
     * Create a process instance in the initial state.
     * 
     * @param pProcessDefinitionName
     *            Name of process definition
     * @return Id of the created process instance.
     */
    //public long createProcessInstance(String pProcessDefinitionName);
    /**
     * Create a process instance in a given state
     * 
     * @param pProcessDefinitionName
     *            Name of the process definition
     * @param pState
     *            Name of the state
     * @return Id of the created process instance.
     */
    //public long createProcessInstance(String pProcessDefinitionName,
    //        String pState);
    /**
     * Get life cycle information on a sheet.
     * 
     * @param pSheetId
     *            Identifier of the sheet
     * @return The process information (current state & transitions)
     * @deprecated
     * @see LifeCycleService#getProcessInstanceInformation(String, String)
     */
    @Deprecated
    @Transactional(readOnly = true)
    public ProcessInformation getProcessInstanceInformation(String pSheetId);

    /**
     * Get life cycle information on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @return The process information (current state & transitions)
     */
    @Transactional(readOnly = true)
    public ProcessInformation getProcessInstanceInformation(String pRoleToken,
            String pSheetId);

    /**
     * Get the next transition available from a state
     * 
     * @param pSheetTypeId
     *            The id of the sheet type
     * @param pStateName
     *            The name of the state
     * @return The list of available transitions
     */
    public List<String> getNextTransitionNames(final String pSheetTypeId,
            final String pStateName);

    /**
     * Get the JBPM process definition for a given sheetType.
     * <p>
     * It should be noted that, to avoid inconsistencies between the JBPM
     * process and the gPM managed states, this definition must not be modified
     * directly through JBPM API.
     * 
     * @param pElementId
     *            Sheet or type internal identifier
     * @return JBPM Process definition
     */
    public org.topcased.gpm.domain.process.ProcessDefinition getProcessDefinition(
            String pElementId);

    /**
     * Get the name of the current state of a process
     * 
     * @param pSheetId
     *            Technical identifier of the sheet.
     * @return Name of the state.
     */
    public String getProcessStateName(String pSheetId);

    /**
     * Get the initial state name of a given sheet type
     * 
     * @param pSheetTypeId
     *            Technical identifier of the sheet type.
     * @return Name of the initial state, or an empty String ("") if the type
     *         does not define a lifecycle.
     */
    public String getInitialStateName(String pSheetTypeId);

    /**
     * Get names of all states in the lifecycle of the sheet type.
     * 
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @return Set of all state names
     */
    public Collection<String> getAllStateNames(String pSheetTypeId);

    /**
     * Perform a transition on a process instance.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @param pTransitionName
     *            Name of the transition to follow.
     */
    public void performTransition(String pRoleToken, String pSheetId,
            String pTransitionName);

    /**
     * Force a sheet in a specific state.
     * <p>
     * This method is used to forcibly set the current state on any state
     * defined in the lifecycle. The sheet history is not modified during this
     * call.
     * <p>
     * This method requires 'admin' privileges.
     * 
     * @param pRoleToken
     *            Role session
     * @param pSheetId
     *            Technical identifier of the sheet
     * @param pTargetStateName
     *            New state of the sheet
     * @throws InvalidNameException
     *             If the target state name is invalid
     * @throws AuthorizationException
     *             If the caller has no admin privilege
     * @throws GDMException
     *             If the specified sheet has no lifecycle
     */
    public void setCurrentState(String pRoleToken, String pSheetId,
            String pTargetStateName) throws InvalidNameException,
        AuthorizationException, GDMException;

    /**
     * Add (or replace) an extension point on a sheet type state.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetTypeId
     *            Sheet type identifier
     * @param pStateName
     *            State name of the sheet where the extension point is added
     * @param pExtensionPointName
     *            Name of the extension
     * @param pCommandNames
     *            List of commands to be executed for this extension
     */
    public void setExtension(String pRoleToken, String pSheetTypeId,
            String pStateName, String pExtensionPointName,
            List<String> pCommandNames);

    /**
     * Test if a transition exist for a given state
     * 
     * @param pSheetTypeId
     *            The sheet type Id
     * @param pStateName
     *            The State name
     * @param pTransitionName
     *            The Transition name
     * @return if the transition exist
     */
    public Boolean existTransition(String pSheetTypeId, String pStateName,
            String pTransitionName);

    /**
     * if exists, get the unique path between pOriginState and pFinalState. If
     * more than one path exists, return null.
     * 
     * @param pSheetType
     *            The Sheet type
     * @param pOriginState
     *            The originState
     * @param pDestinationState
     *            The destination state
     * @return The path.
     */
    public List<TransitionData> getUniquePath(String pSheetType,
            String pOriginState, String pDestinationState);

    /**
     * Retrieves the Node list for the given Sheet Type
     * 
     * @param pSheetType
     * @return node list or empty list
     */
    public Set<Node> getNodes(SheetType pSheetType);
}

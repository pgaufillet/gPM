/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.TransitionAccessControlData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.TransitionData;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.process.ProcessDefinition;
import org.topcased.gpm.domain.process.ProcessDefinition.stateType;
import org.topcased.gpm.domain.process.Transition;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Life Cycle Service Implementation.
 * 
 * @author llatil
 */
public class LifeCycleServiceImpl extends ServiceImplBase implements
        LifeCycleService {

    /** The logger. */
//    private static Logger staticLogger =
//            Logger.getLogger(LifeCycleServiceImpl.class);

    /**
     * Create a new process definition in the database.
     * 
     * @param pToken
     *            Session token
     * @param pBusinessProcessName
     *            Name of business process
     * @param pInputStream
     *            Stream containing the XML def. of the process.
     */
    public void createProcessDefinition(String pToken,
            String pBusinessProcessName, InputStream pInputStream) {
        getAuthService().assertGlobalAdminRole(pToken);

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);

        /*
         *  Read the process definition from the definition file 
         *  then create Serializable Process definition .
         */

        XMLConverter lXMLConter = new XMLConverter(pInputStream);
        org.topcased.gpm.business.serialization.data.ProcessDefinition lDefinition =
                (org.topcased.gpm.business.serialization.data.ProcessDefinition)
                        lXMLConter.fromXML();

        if (null == lDefinition) {
            throw new GDMException("Cannot read process definition");
        }
        else if (lDefinition.getStartState() == null) {
            throw new GDMException(
                    "Process definition must have a start state !");
        }

        // Retrieve the sheet type from the name of the Life cycle definition
        SheetType lSheetType =
                getSheetTypeDao().getSheetType(lBusinessProcess,
                        lDefinition.getName());
        if (null == lSheetType) {
            throw new InvalidNameException(lDefinition.getName(),
                    "No sheet type ''{0}'' defined");
        }

        // Initially, the nodes to remove array contains all nodes of the
        // life cycle.
        String[] lStatesToRemove = getNodeDao().getNodeNames(lSheetType);

        boolean lLifecycleUpdated = false;

        // Iterate on the nodes defined in the life cycle.
        List<org.topcased.gpm.business.serialization.data.State> lNodes =
                lDefinition.getProcessDefinitionStates();
        for (org.topcased.gpm.business.serialization.data.State lNewNode : lNodes) {
            // If the state already exist, do not recreate it
            if (ArrayUtils.contains(lStatesToRemove, lNewNode.getName())) {
                lStatesToRemove =
                        (String[]) ArrayUtils.removeElement(lStatesToRemove,
                                lNewNode.getName());
            }
            else {
                lLifecycleUpdated = true;
            }

            //Must verify the transition existence 
            if (!lNewNode.hasNoLeavingTransitions()) {
                for (org.topcased.gpm.business.serialization.data.Transition lTransition :
                    (List<org.topcased.gpm.business.serialization.data.Transition>)
                        lNewNode.getOutgoingTransitions()) {
                    lTransition.setFromState(lNewNode);
                    lTransition.setToState(lDefinition.getStateByName(lTransition.getTo()));

                    if (lTransition.getToState() == null) {
                        throw new InvalidNameException(lTransition.getName(),
                                "Transition ''{0}'' go to undefined state.");
                    }
                    if (lTransition.getName() == null) {
                        throw new InvalidNameException(
                                lTransition.getFromState().getName(),
                                "Transition starting from ''{0}'' has no name");
                    }
                }
            }
        }

        if (lLifecycleUpdated) {
            saveProcessDefinition(lSheetType, lDefinition);
        }
    }

    /**
     * Create a new process definition in the database.
     * 
     * @param pBusinessProcessName
     *            Name of business process
     * @param pDefinitionContent
     *            Byte array containing the XML def. of the process.
     * @param pToken
     *            the token
     */
    public void createProcessDefinition(String pToken,
            String pBusinessProcessName, byte[] pDefinitionContent) {
        getAuthService().assertGlobalAdminRole(pToken);

        InputStream lIs = new ByteArrayInputStream(pDefinitionContent);
        createProcessDefinition(pToken, pBusinessProcessName, lIs);
        // Don't forget to close the stream...
        try {
            lIs.close();
        }
        catch (IOException e) {
//            staticLogger.warn(e);
        }
    }

    /**
     * Create a process instance and put it in a given state.
     * <p>
     * This method requires 'admin' privileges.
     * 
     * @param pRoleToken the role token
     * @param pSheetType the sheet type
     * @param pProcessDefinitionName
     *            Name of the process definition
     * @param pStateName
     *            Name of the state. If 'null', the process is created in
     *            initial state.
     * @return Id of the created process instance.
     */
    public String createProcessInstance(String pRoleToken,
            CacheableSheetType pSheetType, String pProcessDefinitionName,
            String pStateName) {
        org.topcased.gpm.domain.process.ProcessDefinition lProcDef;
        lProcDef =
                getSheetTypeDao().getProcessDefinitionBySheetTypeId(
                        pSheetType.getId());
        if (lProcDef == null) {
            // FIXME we should actually throw an exception here, but this
            // would require a JBPM process for each sheet type.
//            staticLogger.error("Process definition '" + pProcessDefinitionName
//                    + "' invalid.");
            return null;
        }

        /* FIXME when reading data from a xml file the returned
         process contains a nodelist which includes a null value that
         shouldn't exist
         removes the null values (if exists)
        lProcDef.getNodes().remove(null)*/

        /* make sure that the node is correct */
        Node lState = null;
        if (null != pStateName) {
            lState =
                    getProcessDefinitionDao().getNode(lProcDef.getId(),
                            pStateName);
            if (null == lState) {
                throw new InvalidNameException(pStateName,
                        "State ''{0}'' does not exist");
            }
        }
        return lProcDef.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.lifecycle.service.LifeCycleService#
     *      getProcessInstanceInformation(java.lang.String)
     */
    public ProcessInformation getProcessInstanceInformation(String pSheetId) {
        ProcessInformation lProcInfo = new ProcessInformation();

        Node lCurrentNode = getCurrentNode(pSheetId);

        // If no life cycle is associated to the type returns an 'empty' ProcessInformation object.
        if (null == lCurrentNode) {
            return new ProcessInformation(StringUtils.EMPTY,
                    ArrayUtils.EMPTY_STRING_ARRAY, null);
        }

        // Get the list of leaving transition for this node, and create
        // an array with the transitions names.
        List<String> lTransitions =
                getNodeDao().getTransitionsNames(lCurrentNode);
        String[] lTransitionsNames = (String[]) lTransitions.toArray(new String[0]);
        lProcInfo.setCurrentState(lCurrentNode.getName());
        lProcInfo.setTransitions(lTransitionsNames);

        return lProcInfo;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.lifecycle.service.LifeCycleService#getNextTransitionNames(java.lang.String,
     *      java.lang.String)
     */
    public List<String> getNextTransitionNames(final String pSheetTypeId,
            final String pStateName) {
        // Get the transition node associated to the state name
        final Node lCurrentNode =
                getProcessDefinitionDao().getNode(pSheetTypeId, pStateName);

        if (lCurrentNode == null) {
            throw new GDMException("Unknow state " + pStateName
                    + " for sheet type " + lCurrentNode);
        }

        // Compute the list of next transition
        final Set<org.topcased.gpm.domain.process.Transition> lNextTransitions =
                getNodeDao().getTransitions(lCurrentNode);
        final List<String> lNextTransitionNames = new ArrayList<String>();

        for (org.topcased.gpm.domain.process.Transition lNextTransition : lNextTransitions) {
            if (lNextTransition.isOutgoingTransition()) {
                lNextTransitionNames.add(lNextTransition.getName());
            }
        }

        return lNextTransitionNames;
    }

    /**
     * Get names of all states in the life cycle of the sheet type.
     * 
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @return Set of all state names
     */
    public Collection<String> getAllStateNames(String pSheetTypeId) {
        Collection<String> lStateNames;

        ProcessDefinition lProcDef =
                getSheetTypeDao().getProcessDefinitionBySheetTypeId(
                        pSheetTypeId);
        if (null != lProcDef) {
            final Set<String> lStates =
                    getProcessDefinitionDao().getNodesNames(lProcDef.getId());
            lStateNames = new HashSet<String>(lStates);
        }
        else {
            lStateNames = Collections.emptyList();
        }
        return lStateNames;
    }

    /**
     * Get a process instance information.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheetId
     *            The Id of the sheet.
     * @return The process Information.
     */
    public ProcessInformation getProcessInstanceInformation(String pRoleToken,
            String pSheetId) {
        ProcessInformation lProcInfo = new ProcessInformation();

        org.topcased.gpm.domain.process.Node lCurrentNode =
                getCurrentNode(pSheetId);
        CacheableSheet lSheet =
                getSheetService().getCacheableSheet(pSheetId,
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lSheet.getTypeId();

        String lStateName = lCurrentNode.getName();

        // Get the list of leaving transition for this node, and create
        // an array with the transitions names.
        Set<org.topcased.gpm.domain.process.Transition> lTransitions =
                getNodeDao().getTransitions(lCurrentNode);

        ArrayList<String> lAuthorizedTransitions =
                new ArrayList<String>(lTransitions.size());

        Map<String,String> lTransitionConfirmationMessages = new HashMap<String, String>();

        for (org.topcased.gpm.domain.process.Transition lTrans : lTransitions) {

            if (lTrans.isOutgoingTransition()) {
                if (getAuthService().getTransitionAccessControl(
                        pRoleToken,
                        getAuthService().getAccessControlContextData(
                                pRoleToken, lStateName, lSheetTypeId, null,
                                pSheetId), lTrans.getName()).getAllowed()) {
                    lAuthorizedTransitions.add(lTrans.getName());
                    if (lTrans.getConfirmationMessage() != null) {
                        lTransitionConfirmationMessages.put(lTrans.getName(), lTrans.getConfirmationMessage());
                    }
                }
            }
        }
        // Convert the list of String into an array
        String[] lTransitionsNames;
        lTransitionsNames =
                lAuthorizedTransitions.toArray(new String[lAuthorizedTransitions.size()]);

        // procInfo.setId(pProcId);
        lProcInfo.setCurrentState(lStateName);

        lProcInfo.setTransitions(lTransitionsNames);
        lProcInfo.setTransitionConfirmationMessages(lTransitionConfirmationMessages);
        return lProcInfo;
    }

    /**
     * Get the name of the current state of a process
     * 
     * @param pSheetId
     *            Identifier of the sheet.
     * @return Name of the state.
     */
    public String getProcessStateName(String pSheetId) {
        return getCurrentNode(pSheetId).getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.lifecycle.service.LifeCycleService#getInitialStateName(java.lang.String)
     */
    public String getInitialStateName(String pSheetTypeId) {
        SheetType lSheetType = getSheetType(pSheetTypeId);
        String lInitialStateName = StringUtils.EMPTY;
        ProcessDefinition lProcDef = getProcessDefinition(lSheetType);
        if (null != lProcDef) {
            lInitialStateName =
                    getProcessDefinitionDao().getStartState(lProcDef.getId()).getName();
        }

        // If no process definition exists for this element, returns an empty string as
        // initial state name.
        return lInitialStateName;
    }

    /**
     * Perform a transition on a process instance.
     * 
     * @param pSheetId
     *            Identifier of the sheet.
     * @param pTransitionName
     *            Name of the transition to follow.
     */
    private void performTransition(String pSheetId, String pTransitionName) {
        Sheet lSheet = getSheet(pSheetId);
        Node lPreviousNode = lSheet.getCurrentNode();

        String lProcessDefinitionId =
                lPreviousNode.getProcessDefinition().getId();
        org.topcased.gpm.domain.process.Transition lTransition =
                getNodeDao().getTransitionByName(lPreviousNode, pTransitionName);
        if (null != lTransition) {
            String lToNodeName = lTransition.getToName();

            Node lNextNode =
                    getProcessDefinitionDao().getNode(lProcessDefinitionId,
                            lToNodeName);
            lSheet.setCurrentNode(lNextNode);

            int lPrevVersion = lSheet.getVersion();
            lSheet.setVersion(lPrevVersion + 1);

            // Invalidate the cache
            removeElementFromCache(pSheetId);
        }
        else {
            throw new IllegalStateException("Invalid Transition Name ");
        }

    }

    /**
     * Perform a transition on a process instance.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheetId
     *            The sheet ID.
     * @param pTransitionName
     *            Name of the transition to follow.
     */
    public void performTransition(String pRoleToken, String pSheetId,
            String pTransitionName) {
        if (StringUtils.isBlank(pTransitionName)) {
            return;
        }
        final Node lCurrentJbpmNode = getCurrentNode(pSheetId);
        if (null == lCurrentJbpmNode) {
            throw new GDMException("Type id '" + pSheetId
                    + "' has no associated lifecycle and "
                    + "cannot perform any transition");
        }

        final String lStateName = lCurrentJbpmNode.getName();
        final CacheableValuesContainer lValuesContainer =
                getCachedValuesContainer(pSheetId, CACHE_IMMUTABLE_OBJECT);
        final String lSheetTypeId = lValuesContainer.getTypeId();

        final TransitionAccessControlData lTacd =
                getAuthService().getTransitionAccessControl(
                        pRoleToken,
                        getAuthService().getAccessControlContextData(
                                pRoleToken, lStateName, lSheetTypeId, null,
                                pSheetId), pTransitionName);
        if (null != lTacd) {
            if (!lTacd.getAllowed()) {
                throw new AuthorizationException(
                        "Illegal access : transition '" + pTransitionName
                                + "' cannot be performed.");
            }
        }
        else {
            throw new InvalidTokenException("The User token is null");
        }
        performTransition(pSheetId, pTransitionName);
    }

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
            String pTargetStateName) {
        getAuthService().assertAdminRole(pRoleToken);

        // get Process definition;
        Sheet lSheet = getSheetDao().load(pSheetId);

        if (null == lSheet) {
            throw new GDMException(" Invalide sheet Id ");
        }

        SheetType lSheetType = getSheetTypeDao().load(lSheet.getSheetTypeId());
        ProcessDefinition lProcDef = lSheetType.getProcessDefinition();

        if (null == lProcDef) {
            throw new GDMException("Sheet has no associated lifecycle and "
                    + "cannot perform any transition");
        }

        Node lTargetNode =
                getProcessDefinitionDao().getNode(
                        lSheetType.getProcessDefinition().getId(),
                        pTargetStateName);

        if (null == lTargetNode) {
            throw new InvalidNameException(pTargetStateName,
                    "Invalid target state name ''{0}''");
        }
        lSheetType.getProcessDefinition().getId();
        lSheet.setCurrentNode(lTargetNode);
    }

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
            List<String> pCommandNames) {
        Node lState = getNode(pSheetTypeId, pStateName);

        getExtensionsService().setExtension(pRoleToken, lState.getId(),
                pExtensionPointName, pCommandNames);
    }

    /**
     * Get a state node of a sheet type
     * 
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @param pStateName
     *            State name
     * @return Node object.
     */
    private Node getNode(String pSheetTypeId, String pStateName) {
        SheetType lSheetType = getSheetType(pSheetTypeId);

        Node lNode = getSheetTypeDao().getNode(lSheetType.getId(), pStateName);
        if (null == lNode) {
            throw new InvalidNameException(pStateName,
                    "State name {0} does not exist");
        }
        return lNode;
    }

    /**
     * Get the current node of the token in the given ProcessInstance.
     * 
     * @param pSheetId
     *            Sheet identifier.
     * @return node of the process.
     */
    private Node getCurrentNode(String pSheetId) {
        Node lCurrentNode = getSheet(pSheetId).getCurrentNode();

        if (null == lCurrentNode) {
            return null;
        }

        // Get the current node of the sheet.
        return lCurrentNode;
    }

    /**
     * Get the process definition for a given sheetType.
     * <p>
     * 
     * @param pElementId
     *            Sheet or type internal identifier
     * @return Process definition
     */
    public ProcessDefinition getProcessDefinition(String pElementId) {
        SheetType lSheetType;
        lSheetType = getSheetType(pElementId, true);

        if (null == lSheetType) {
            lSheetType = getSheetType(getSheet(pElementId));
        }
        return getProcessDefinition(lSheetType);
    }

    /**
     * Get the Process Definition for a given sheetType.
     * 
     * @param pSheetType
     *            Sheet type entity
     * @return Process definition
     */
    private ProcessDefinition getProcessDefinition(final SheetType pSheetType) {
        return pSheetType.getProcessDefinition();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.lifecycle.service.LifeCycleService#existTransition(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public Boolean existTransition(String pSheetTypeId, String pStateName,
            String pTransitionName) {
        Boolean lRet = Boolean.FALSE;
        org.topcased.gpm.domain.process.Node lNode =
                getProcessDefinitionDao().getNode(getProcessDefinition(pSheetTypeId).getId(),
                        pStateName);
        if (null != lNode) {
            org.topcased.gpm.domain.process.Transition lTransition =
                    getNodeDao().getTransitionByName(lNode, pTransitionName);
            if (lTransition != null) {
                lRet = Boolean.TRUE;
            }
        }
        return lRet;
    }

    /**
     * Determines if the last transition could be perform the user.
     * 
     * @param pRoleToken
     *            Role token
     * @param pSheetTypeId
     *            Sheet's type identifier
     * @param pTransitionsHistory
     *            Transitions history. (Can be null or empty)
     * @return True if the last transition can be perform (and null or empty),
     *         false otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean canPerformTransitionHistory(String pRoleToken,
            final String pSheetTypeId,
            final List<TransitionHistoryData> pTransitionsHistory) {
        Boolean lRes;
        if (CollectionUtils.isNotEmpty(pTransitionsHistory)) {
            TransitionHistoryData lLastTransitionHistory =
                    pTransitionsHistory.get(pTransitionsHistory.size() - 1);

            org.topcased.gpm.domain.process.Node lLastNode =
                    getProcessDefinitionDao().getNode(pSheetTypeId,
                            lLastTransitionHistory.getOriginState());

            Set<Transition> lLastNodeTransitionList =
                    getNodeDao().getTransitions(lLastNode);

            // seperating transitions into leaving and arriving tansition
            Collection<Transition> lLeavingTransitions =
                    CollectionUtils.EMPTY_COLLECTION;
            Collection<Transition> lArrivingTransitions =
                    CollectionUtils.EMPTY_COLLECTION;

            for (Transition lTransition : lLastNodeTransitionList) {
                if (lTransition.isOutgoingTransition()) {
                    lLeavingTransitions.add(lTransition);
                }
                else {
                    lArrivingTransitions.add(lTransition);
                }
            }

            Collection<Transition> lTransitions =
                    CollectionUtils.intersection(lLeavingTransitions,
                            lArrivingTransitions);

            final String lTransitionName;
            if (lTransitions.size() != 1) {
                throw new GDMException("No transition between '"
                        + lLastTransitionHistory.getOriginState() + "' and '"
                        + lLastTransitionHistory.getDestinationState()
                        + "' (bad transition history object).");
            }
            else {
                lTransitionName = lTransitions.iterator().next().getName();
            }

            final TransitionAccessControlData lTacd =
                    getAuthService().getTransitionAccessControl(
                            pRoleToken,
                            getAuthService().getAccessControlContextData(
                                    pRoleToken,
                                    lLastTransitionHistory.getOriginState(),
                                    pSheetTypeId, null, StringUtils.EMPTY),
                            lTransitionName);

            lRes = (lTacd != null) && lTacd.getAllowed();
        }
        else {
            lRes = true;
        }

        return lRes;
    }

    private List<List<TransitionData>> transitionsGlobals;

    /** {@inheritDoc} */
    @Override
    public List<TransitionData> getUniquePath(String pSheetType,
            String pOriginState, String pDestinationState) {
        transitionsGlobals = new ArrayList<List<TransitionData>>();
        List<TransitionData> lTransitionDataList =
                new ArrayList<TransitionData>();

        ProcessDefinition lProcessDefinition = getProcessDefinition(pSheetType);

        Node lOriginNode = null;
        Node lDestinationNode = null;
        Set<Node> lNodes = getProcessDefinitionDao().getNodes(lProcessDefinition.getId());
        for (Node lNode : lNodes) {
            if (lNode.getName().equals(pOriginState)) {
                lOriginNode = lNode;
            }
            if (lNode.getName().equals(pDestinationState)) {
                lDestinationNode = lNode;
            }
        }
        getPathRecursive(lOriginNode, lDestinationNode, new HashSet<Node>(),
                lTransitionDataList);
        if (transitionsGlobals.size() > 1) {
            return null;
        }
        else if (transitionsGlobals.size() == 1) {
            return transitionsGlobals.get(0);
        }
        return lTransitionDataList;
    }

    private void getPathRecursive(Node pStartNode, Node pFinalNode,
            Set<Node> pNodes, List<TransitionData> pTransitionDataList) {
        // final State.
        if (pStartNode.getName().equals(pFinalNode.getName())) {
            transitionsGlobals.add(new ArrayList<TransitionData>(
                    pTransitionDataList));
            pTransitionDataList.remove(pTransitionDataList.size() - 1);
            return;
        }
        Set<Node> lNodesCpy = new HashSet<Node>(pNodes);
        List<TransitionData> lTransitionDataListCpy =
                new ArrayList<TransitionData>(pTransitionDataList);
        Set<Transition> lTransitionList = getNodeDao().getTransitions(pStartNode);
        final String lProcessDefinitionId =
                pStartNode.getProcessDefinition().getId();
        for (Transition lTransition : lTransitionList) {
            if (lTransition.isOutgoingTransition()) {
                Node lToNode =
                        getProcessDefinitionDao().getNode(lProcessDefinitionId,
                                lTransition.getToName());
                lNodesCpy.add(pStartNode);
                if (lNodesCpy.contains(lToNode)) {
                    continue;
                }

                TransitionData lTransitionData = new TransitionData();
                lTransitionData.setOriginState(pStartNode.getName());
                lTransitionData.setFinalState(lTransition.getToName());
                lTransitionData.setName(lTransition.getName());
                lTransitionDataListCpy.add(lTransitionData);

                getPathRecursive(lToNode, pFinalNode, lNodesCpy,
                        lTransitionDataListCpy);
            }
        }
        if (!pTransitionDataList.isEmpty()) {
            pTransitionDataList.remove(pTransitionDataList.size() - 1);
        }
    }

    /**
     * Save a process definition in the data base
     * 
     * @param pSheetType
     *            the sheet type
     * @param pProcessDefinition
     *            serializable process definition
     */
    private void saveProcessDefinition(
            SheetType pSheetType,
            org.topcased.gpm.business.serialization.data.ProcessDefinition pProcessDefinition) {
        boolean lIsExist = false;
        /** create process definition Entity */
        ProcessDefinition lProcessDefinition =
                getProcessDefinitionDao().getProcessDefinitionByName(
                        pProcessDefinition.getName());
        if (null == lProcessDefinition) {
            lProcessDefinition =
                    org.topcased.gpm.domain.process.ProcessDefinition.newInstance();
            getProcessDefinitionDao().create(lProcessDefinition);
        }
        else {
            lIsExist = true;
        }

        /** Node entities */
        Set<org.topcased.gpm.domain.process.Node> lNodeEntities =
                new HashSet<org.topcased.gpm.domain.process.Node>();

        // Save the start state 
        lNodeEntities.add(saveNode(pSheetType, lProcessDefinition,
                pProcessDefinition.getStartState(),
                ProcessDefinition.stateType.STARTSTATE));

        // Save the end states
        lNodeEntities.addAll(saveNodes(pSheetType, lProcessDefinition,
                pProcessDefinition.getEndState(),
                ProcessDefinition.stateType.ENDSTATE));

        // Save the state
        lNodeEntities.addAll(saveNodes(pSheetType, lProcessDefinition,
                pProcessDefinition.getStates(),
                ProcessDefinition.stateType.STATE));

        // Save the set of states
        lProcessDefinition.setNodes(lNodeEntities);

        // build the process definition
        lProcessDefinition.setName(pProcessDefinition.getName());

        // save the process definition into the data base
        if (!lIsExist) {
            getProcessDefinitionDao().create(lProcessDefinition);
        }
        pSheetType.setProcessDefinition(lProcessDefinition);
    }

    private Set<Node> saveNodes(SheetType pSheetType,
            ProcessDefinition pProcessDefinition,
            List<org.topcased.gpm.business.serialization.data.State> pNodes,
            stateType pStateType) {
        Set<Node> lNodes = new HashSet<Node>(pNodes.size());
        for (org.topcased.gpm.business.serialization.data.State lNode : pNodes) {
            lNodes.add(saveNode(pSheetType, pProcessDefinition, lNode,
                    pStateType));
        }
        return lNodes;
    }

    private Node saveNode(SheetType pSheetType,
            ProcessDefinition pProcessDefinition,
            org.topcased.gpm.business.serialization.data.State pNode,
            stateType pStateType) {
        List<org.topcased.gpm.business.serialization.data.Transition> lTransitions =
                pNode.getTransitions();

        // create node entity
        org.topcased.gpm.domain.process.Node lCurrentNodeEntity =
                org.topcased.gpm.domain.process.Node.newInstance();
        lCurrentNodeEntity.setName(pNode.getName());
        lCurrentNodeEntity.setProcessDefinition(pProcessDefinition);

        /** Transition entities */
        Set<org.topcased.gpm.domain.process.Transition> lTransitionEntities =
                new HashSet<org.topcased.gpm.domain.process.Transition>();

        for (org.topcased.gpm.business.serialization.data.Transition lTransition : lTransitions) {

            // create transition entity
            org.topcased.gpm.domain.process.Transition lCurrentTransitionEntity =
                    org.topcased.gpm.domain.process.Transition.newInstance();
            lCurrentTransitionEntity.setName(lTransition.getName());
            lCurrentTransitionEntity.setOutgoingTransition(lTransition.isOutTransiton());
            lCurrentTransitionEntity.setFromNode(lCurrentNodeEntity);
            lCurrentTransitionEntity.setToName(lTransition.getTo());
            lCurrentTransitionEntity.setConfirmationMessage(lTransition.getConfirmationMessage());

            // save the transition in the data base
            getTransitionDao().create(lCurrentTransitionEntity);
            lTransitionEntities.add(lCurrentTransitionEntity);
        }
        lCurrentNodeEntity.setTransitions(lTransitionEntities);
        lCurrentNodeEntity.setType(pStateType.name());
        // create the node in the data base.
        getNodeDao().create(lCurrentNodeEntity);

        return lCurrentNodeEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> getNodes(SheetType pSheetType) {
        Set<Node> lNodes =
                getProcessDefinitionDao().getNodes(
                        pSheetType.getProcessDefinition().getId());
        if (null == lNodes) {
            lNodes = new HashSet<Node>();
        }
        return lNodes;
    }
}

/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.process;

import java.util.Set;

/**
 * @see org.topcased.gpm.domain.process.ProcessDefinition
 * @author Atos
 */
public interface ProcessDefinitionDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.process.ProcessDefinition, java.lang.String> {
    /**
     * Get a node by its process definition id and its state name.
     * 
     * @param pProcessDefinitionId
     *            the process definition id
     * @param pStateName
     *            the state name
     * @return the node or null if an exception was raised.
     */
    public org.topcased.gpm.domain.process.Node getNode(
            java.lang.String pProcessDefinitionId, java.lang.String pStateName);

    /**
     * Get the nodes associated to a process definition id.
     * 
     * @param pProcessDefinitionId
     *            the process definition id where nodes belong
     * @return the set of nodes or null if an exception was raised.
     */
    public Set<org.topcased.gpm.domain.process.Node> getNodes(
            java.lang.String pProcessDefinitionId);

    /**
     * Get a process definition by its name.
     * 
     * @param pProcessDefinitionName
     *            the name of the process definition to find
     * @return the process definition or null if an exception was raised.
     */
    public org.topcased.gpm.domain.process.ProcessDefinition getProcessDefinitionByName(
            java.lang.String pProcessDefinitionName);

    /**
     * Get the start state of a process definition.
     * 
     * @param pProcessDefinitionId
     *            the process definition id
     * @return the start state or null if an exception was raised.
     */
    public org.topcased.gpm.domain.process.Node getStartState(
            java.lang.String pProcessDefinitionId);

    /**
     * Get a set containing the end states of a process definition. The set can
     * be empty.
     * 
     * @param pProcessDefinitionId
     *            the process definition id
     * @return the set of end states or null if an exception was raised.
     */
    public Set<org.topcased.gpm.domain.process.Node> getEndStates(
            java.lang.String pProcessDefinitionId);

    /**
     * Delete a node from process definition
     * 
     * @param pNode
     *            the node to be deleted
     * @return false if an exception was raised
     */
    public boolean deleteNodeFromProcessDefinition(final Node pNode);

    /**
     * Retrieves nodes names, process definition given.
     * 
     * @param pProcessDefinitionId
     *            process definition on which the nodes belongs to
     * @return set of nodes names or empty set if and exception was raised
     */
    public Set<String> getNodesNames(String pProcessDefinitionId);
}
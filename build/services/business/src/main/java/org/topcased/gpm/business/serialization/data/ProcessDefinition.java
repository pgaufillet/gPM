/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/

package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Life cycle process definition
 * 
 * @author phtsaan
 */
@XStreamAlias("process-definition")
public class ProcessDefinition extends Gpm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * process definition name
     */
    @XStreamAsAttribute
    private String name;

    /**
     * A process definition has a single start state which is mandatory
     */
    @XStreamAlias("start-state")
    private State startState;

    /**
     * list of states
     */
    @XStreamImplicit(itemFieldName = "state")
    private List<State> states;

    /**
     * list of end states
     */
    @XStreamImplicit(itemFieldName = "end-state")
    private List<State> endStates;

    /**
     * list of all states
     */
    private List<State> processDefinitionStates;

    /**
     * Process definition constructor
     */
    public ProcessDefinition() {
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State pStartState) {
        startState = pStartState;
    }

    /**
     * Retrieves a list of end states
     * 
     * @return List<State> or empty Array list if no end state was defined
     */
    public List<State> getEndState() {
        if (endStates == null) {
            endStates = new ArrayList<State>();
        }
        return endStates;
    }

    /**
     * Set the end state list
     * 
     * @param pEndStates
     *            list of the end states
     */
    public void setEndState(List<State> pEndStates) {
        endStates = pEndStates;
    }

    /**
     * Set the state list
     * 
     * @param pStates
     *            list of state
     */
    public void setStates(List<State> pStates) {
        states = pStates;
    }

    /**
     * retrieves a list of States
     * 
     * @return List<State> or empty list if no state was defined
     */
    public List<State> getStates() {
        if (states == null) {
            states = new ArrayList<State>();
        }
        return states;
    }

    /**
     * Retrieves the process definition states list independantly to thier type
     * 
     * @return List<State> all the state defined in the process definition or
     *         empty list if no state was defined
     */
    public List<State> getProcessDefinitionStates() {
        if (processDefinitionStates == null) {
            initProcessDefinitionStates();
        }
        return processDefinitionStates;
    }

    private void initProcessDefinitionStates() {
        processDefinitionStates = new ArrayList<State>();
        // add start state in State list
        if (startState != null) {
            processDefinitionStates.add(startState);
        }

        // add end states in the state list
        if (endStates != null) {
            processDefinitionStates.addAll(endStates);
        }

        if (states != null) {
            processDefinitionStates.addAll(states);
        }
    }

    /**
     * Retrieves a state with its name
     * 
     * @param pStateName
     *            State name
     * @return the state or null if no state with this name exists in this
     *         process definition
     */
    public State getStateByName(String pStateName) {
        /* if the State is a simple State */
        for (State lCurrentState : getProcessDefinitionStates()) {
            if (lCurrentState.getName().equalsIgnoreCase(pStateName)) {
                return lCurrentState;
            }
        }
        return null;
    }

}

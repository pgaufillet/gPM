/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * The Class State.
 * 
 * @author llatil
 */
@XStreamAlias("state")
public class State extends ExtensionsContainer {

    private static final long serialVersionUID = 5964575023698469314L;

    /**
     * state's name
     */
    @XStreamAsAttribute
    private String name;

    /**
     * arriving transition list
     */
    private List<Transition> incomingTransitions;

    /**
     * leaving transition list
     */
    @XStreamImplicit(itemFieldName = "transition")
    private List<Transition> outgoingTransitions;

    /**
     * State model constructor
     */
    public State() {
    }

    /**
     * Retrieves incoming Transitions
     * 
     * @return List<Transition> or null if no transition was loaded
     */
    public List<Transition> getIncomingTransitions() {
        return incomingTransitions;
    }

    /**
     * Set incoming Transitions list
     * 
     * @param pIncomingTransitions incoming transitions
     */
    public void setIncomingTransitions(List<Transition> pIncomingTransitions) {
        this.incomingTransitions = pIncomingTransitions;
    }

    /**
     * Retrieves outgoing Transitions
     * 
     * @return List<Transition> or null if no transition was loaded
     */
    public List<Transition> getOutgoingTransitions() {
        return outgoingTransitions;
    }

    /**
     * Set outgoing Transitions list
     * 
     * @param pOutgoingTransitions outgoing transitions
     */
    public void setOutgoingTransitions(List<Transition> pOutgoingTransitions) {
        this.outgoingTransitions = pOutgoingTransitions;
    }

    /**
     * Set state name
     * 
     * @param pName
     *            state name
     */
    public void setName(String pName) {
        this.name = pName;
    }

    /**
     * Retrieves state name
     * 
     * @return name state name
     */
    public String getName() {
        return name;
    }

    /**
     * Check if the node has the outgoing transition
     * 
     * @return boolean : true if the node has no outgoing transition
     */
    public boolean hasNoLeavingTransitions() {
        boolean lRet = false;
        if (outgoingTransitions == null || outgoingTransitions.isEmpty()) {
            lRet = true;
        }
        return lRet;
    }

    /**
     * Retrieves Transitions list
     * 
     * @return List<Transition> all transitions that belong to this State or
     *         empty list in the case no transition was defined
     */
    public List<Transition> getTransitions() {
        List<Transition> lTransitions = new ArrayList<Transition>();
        if (outgoingTransitions != null) {
            for (Transition lTout : outgoingTransitions) {
                lTout.setOutTransiton(true);
                lTransitions.add(lTout);
            }
        }
        if (incomingTransitions != null) {
            lTransitions.addAll(incomingTransitions);
        }
        return lTransitions;
    }
}

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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Transition
 * 
 * @author phtsaan
 */
@XStreamAlias("transition")
public class Transition extends Gpm implements Serializable {

    /**
     * Default ID
     */
    private static final long serialVersionUID = 1L;

    @XStreamAsAttribute
    private String name;

    /* transition started node */
    private State fromState;

    /* transition ended node */
    private State toState;

    /* transition direction*/
    private boolean isOutTransiton = false;

    @XStreamAsAttribute
    private String to;

    private String from;

    @XStreamAsAttribute
    private String confirmationMessage;

    /**
     * Transition constructor
     */
    public Transition() {
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        this.name = pName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String pTo) {
        this.to = pTo;
    }

    public State getFromState() {
        return fromState;
    }

    public void setFromState(State pFromState) {
        this.fromState = pFromState;
    }

    public State getToState() {
        return toState;
    }

    public void setToState(State pToState) {
        this.toState = pToState;
    }

    public boolean isOutTransiton() {
        return isOutTransiton;
    }

    public void setOutTransiton(boolean pIsOutTransiton) {
        this.isOutTransiton = pIsOutTransiton;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String pFrom) {
        this.from = pFrom;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String pConfirmationMessage) {
        this.confirmationMessage = pConfirmationMessage;
    }
}

/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Cyril Marchive (Atos)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Class mapping a transition content.
 * 
 * @author cmarchive
 */
@XStreamAlias("transition")
public class TransitionData implements Serializable {
    
    /**
     * Serialization ID 
     */
    private static final long serialVersionUID = 1L;

    @XStreamAsAttribute
    private String originState;

    @XStreamAsAttribute
    private String finalState;

    @XStreamAsAttribute
    private String name;

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String pOriginState) {
        this.originState = pOriginState;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String pFinalState) {
        this.finalState = pFinalState;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        this.name = pName;
    }

}

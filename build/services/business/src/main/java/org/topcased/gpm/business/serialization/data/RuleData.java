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
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Class mapping a rule content.
 * 
 * @author cmarchive
 */
@XStreamAlias("rule")
public class RuleData implements Serializable {

    /**
     * Default serial ID 
     */
    private static final long serialVersionUID = 1L;

    /** product name of the element. */
    @XStreamAsAttribute
    private String type;

    /** Transition rule of the sheet. */
    @XStreamImplicit(itemFieldName = "transition")
    private List<TransitionData> transition;

    /**
     * Get The transition list
     * 
     * @return The transition list
     */
    public List<TransitionData> getTransition() {
        if (transition == null) {
            transition = new ArrayList<TransitionData>();
        }
        return transition;
    }

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        this.type = pType;
    }
}

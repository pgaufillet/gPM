/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class TransitionAccessCtl.
 * 
 * @author llatil
 */
@XStreamAlias("transitionAccess")
public class TransitionAccessCtl extends AccessCtl {

    /** The enabled. */
    @XStreamAsAttribute
    private Boolean enabled;

    /** The transition name. */
    @XStreamAsAttribute
    private String transitionName;

    /**
     * Checks if is enabled.
     * 
     * @return the boolean
     */
    public Boolean isEnabled() {
        if (null == enabled) {
            return true;
        }
        return enabled;
    }

    /**
     * Gets the transition name.
     * 
     * @return the transition name
     */
    public String getTransitionName() {
        return transitionName;
    }
}

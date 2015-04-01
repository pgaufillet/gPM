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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class ActionAccessCtl.
 * 
 * @author llatil
 */
@XStreamAlias("actionAccess")
public class ActionAccessCtl extends AccessCtl {

    /** The action key. */
    @XStreamAsAttribute
    private String actionKey;

    /** The enabled. */
    @XStreamAsAttribute
    private Boolean enabled;

    /** The confidential. */
    @XStreamAsAttribute
    private Boolean confidential;

    /**
     * Constructs a new action access control
     * 
     * @param pActionKey
     *            Label key of the action
     * @param pEnabled
     *            Action is enabled ? (or null if this access is not changed)
     * @param pConfidential
     *            Action is confidential ? (or null if this access is not
     *            changed)
     */
    public ActionAccessCtl(String pActionKey, Boolean pEnabled,
            Boolean pConfidential) {
        super();
        actionKey = pActionKey;
        enabled = pEnabled;
        confidential = pConfidential;
    }

    /**
     * Gets the action key.
     * 
     * @return the action key
     */
    public String getActionKey() {
        return actionKey;
    }

    /**
     * Checks if is confidential.
     * 
     * @return the boolean
     */
    public Boolean isConfidential() {
        return confidential;
    }

    /**
     * Checks if is enabled.
     * 
     * @return the boolean
     */
    public Boolean isEnabled() {
        return enabled;
    }
}

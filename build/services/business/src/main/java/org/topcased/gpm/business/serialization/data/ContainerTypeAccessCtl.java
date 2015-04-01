/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class ContainerTypeAccessCtl.
 * 
 * @author ahaugomm
 */
@XStreamAlias("containerTypeAccess")
public class ContainerTypeAccessCtl extends AccessCtl {

    /** The updatable. */
    @XStreamAsAttribute
    private Boolean updatable;

    /** The confidential. */
    @XStreamAsAttribute
    private Boolean confidential;

    /** The deletable. */
    @XStreamAsAttribute
    private Boolean deletable;

    /** The creatable. */
    @XStreamAsAttribute
    private Boolean creatable;

    /**
     * Checks if is creatable.
     * 
     * @return the creatable
     */
    public Boolean isCreatable() {
        if (null == creatable) {
            return true;
        }
        return creatable;
    }

    /**
     * Sets the creatable.
     * 
     * @param pCreatable
     *            the creatable to set
     */
    public void setCreatable(Boolean pCreatable) {
        this.creatable = pCreatable;
    }

    /**
     * Checks if is deletable.
     * 
     * @return the deletable
     */
    public Boolean isDeletable() {
        if (null == deletable) {
            return true;
        }
        return deletable;
    }

    /**
     * Sets the deletable.
     * 
     * @param pDeletable
     *            the deletable to set
     */
    public void setDeletable(Boolean pDeletable) {
        this.deletable = pDeletable;
    }

    /**
     * Checks if is confidential.
     * 
     * @return the boolean
     */
    public Boolean isConfidential() {
        if (null == confidential) {
            return false;
        }
        return confidential;
    }

    /**
     * Checks if is updatable.
     * 
     * @return the boolean
     */
    public Boolean isUpdatable() {
        if (null == updatable) {
            return true;
        }
        return updatable;
    }

}

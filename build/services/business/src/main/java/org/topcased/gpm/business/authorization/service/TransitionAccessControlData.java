/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.authorization.service;

/**
 * @author Atos
 */
public class TransitionAccessControlData extends
        org.topcased.gpm.business.authorization.service.AccessControlData
        implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public TransitionAccessControlData() {
    }

    /**
     * Constructor taking all properties.
     */
    public TransitionAccessControlData(final java.lang.Boolean pAllowed,
            final java.lang.String pTransitionName) {
        this.allowed = pAllowed;
        this.transitionName = pTransitionName;
    }

    /**
     * Copies constructor from other TransitionAccessControlData
     */
    public TransitionAccessControlData(TransitionAccessControlData pOtherBean) {
        if (pOtherBean != null) {
            this.allowed = pOtherBean.getAllowed();
            this.transitionName = pOtherBean.getTransitionName();
        }
    }

    private java.lang.Boolean allowed;

    /**
     * 
     */
    public java.lang.Boolean getAllowed() {
        return this.allowed;
    }

    public void setAllowed(java.lang.Boolean pAllowed) {
        this.allowed = pAllowed;
    }

    private java.lang.String transitionName;

    /**
     * 
     */
    public java.lang.String getTransitionName() {
        return this.transitionName;
    }

    public void setTransitionName(java.lang.String pTransitionName) {
        this.transitionName = pTransitionName;
    }

}
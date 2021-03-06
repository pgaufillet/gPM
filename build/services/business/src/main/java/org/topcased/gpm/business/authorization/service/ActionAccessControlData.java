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
public class ActionAccessControlData extends
        org.topcased.gpm.business.authorization.service.AccessControlData
        implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ActionAccessControlData() {
    }

    /**
     * Constructor taking all properties.
     */
    public ActionAccessControlData(final java.lang.Boolean pEnabled,
            final java.lang.Boolean pConfidential,
            final java.lang.String pLabelKey) {
        this.enabled = pEnabled;
        this.confidential = pConfidential;
        this.labelKey = pLabelKey;
    }

    /**
     * Copies constructor from other ActionAccessControlData
     */
    public ActionAccessControlData(ActionAccessControlData pOtherBean) {
        if (pOtherBean != null) {
            this.enabled = pOtherBean.getEnabled();
            this.confidential = pOtherBean.getConfidential();
            this.labelKey = pOtherBean.getLabelKey();
        }
    }

    private java.lang.Boolean enabled;

    /**
     * <p>
     * Specify if the action is allowed for the user or not.
     * </p>
     * <p>
     * UI client may choose to hide or disable the actions not allowed.
     * </p>
     */
    public java.lang.Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(java.lang.Boolean pEnabled) {
        this.enabled = pEnabled;
    }

    private java.lang.Boolean confidential;

    /**
     * 
     */
    public java.lang.Boolean getConfidential() {
        return this.confidential;
    }

    public void setConfidential(java.lang.Boolean pConfidential) {
        this.confidential = pConfidential;
    }

    private java.lang.String labelKey;

    /**
     * 
     */
    public java.lang.String getLabelKey() {
        return this.labelKey;
    }

    public void setLabelKey(java.lang.String pLabelKey) {
        this.labelKey = pLabelKey;
    }

}
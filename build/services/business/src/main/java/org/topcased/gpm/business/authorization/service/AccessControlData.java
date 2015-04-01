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
public class AccessControlData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AccessControlData() {
    }

    /**
     * Constructor taking all properties.
     */
    public AccessControlData(
            final java.lang.String pBusinessProcessName,
            final org.topcased.gpm.business.authorization.service.AccessControlContextData pContext,
            final org.topcased.gpm.business.attributes.AttributeData[] pExtendedAttributes) {
        this.businessProcessName = pBusinessProcessName;
        this.context = pContext;
        this.extendedAttributes = pExtendedAttributes;
    }

    /**
     * Copies constructor from other AccessControlData
     */
    public AccessControlData(AccessControlData pOtherBean) {
        if (pOtherBean != null) {
            this.businessProcessName = pOtherBean.getBusinessProcessName();
            this.context = pOtherBean.getContext();
            this.extendedAttributes = pOtherBean.getExtendedAttributes();
        }
    }

    private java.lang.String businessProcessName;

    /**
     * 
     */
    public java.lang.String getBusinessProcessName() {
        return this.businessProcessName;
    }

    public void setBusinessProcessName(java.lang.String pBusinessProcessName) {
        this.businessProcessName = pBusinessProcessName;
    }

    private org.topcased.gpm.business.authorization.service.AccessControlContextData context;

    /**
     * Get the context
     */
    public org.topcased.gpm.business.authorization.service.AccessControlContextData getContext() {
        return this.context;
    }

    /**
     * Set the context
     */
    public void setContext(
            org.topcased.gpm.business.authorization.service.AccessControlContextData pContext) {
        this.context = pContext;
    }

    private org.topcased.gpm.business.attributes.AttributeData[] extendedAttributes;

    /**
     * Get the extendedAttributes
     */
    public org.topcased.gpm.business.attributes.AttributeData[] getExtendedAttributes() {
        return this.extendedAttributes;
    }

    /**
     * Set the extendedAttributes
     */
    public void setExtendedAttributes(
            org.topcased.gpm.business.attributes.AttributeData[] pExtendedAttributes) {
        this.extendedAttributes = pExtendedAttributes;
    }

}
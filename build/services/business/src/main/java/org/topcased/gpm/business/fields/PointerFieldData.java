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
package org.topcased.gpm.business.fields;

/**
 * @author Atos
 */
public class PointerFieldData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public PointerFieldData() {
    }

    /**
     * Constructor taking all properties.
     */
    public PointerFieldData(final java.lang.String pReferencedLinkType,
            final java.lang.String pReferencedFieldLabel) {
        this.referencedLinkType = pReferencedLinkType;
        this.referencedFieldLabel = pReferencedFieldLabel;
    }

    /**
     * Copies constructor from other PointerFieldData
     */
    public PointerFieldData(PointerFieldData pOtherBean) {
        if (pOtherBean != null) {
            this.referencedLinkType = pOtherBean.getReferencedLinkType();
            this.referencedFieldLabel = pOtherBean.getReferencedFieldLabel();
        }
    }

    private java.lang.String referencedLinkType;

    /**
     * 
     */
    public java.lang.String getReferencedLinkType() {
        return this.referencedLinkType;
    }

    public void setReferencedLinkType(java.lang.String pReferencedLinkType) {
        this.referencedLinkType = pReferencedLinkType;
    }

    private java.lang.String referencedFieldLabel;

    /**
     * 
     */
    public java.lang.String getReferencedFieldLabel() {
        return this.referencedFieldLabel;
    }

    public void setReferencedFieldLabel(java.lang.String pReferencedFieldLabel) {
        this.referencedFieldLabel = pReferencedFieldLabel;
    }

}
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
public class FieldValueData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FieldValueData() {
    }

    /**
     * Constructor taking all properties.
     */
    public FieldValueData(final java.lang.String[] pValues) {
        this.values = pValues;
    }

    /**
     * Copies constructor from other FieldValueData
     */
    public FieldValueData(FieldValueData pOtherBean) {
        if (pOtherBean != null) {
            this.values = pOtherBean.getValues();
        }
    }

    private java.lang.String[] values;

    /**
     * 
     */
    public java.lang.String[] getValues() {
        return this.values;
    }

    public void setValues(java.lang.String[] pValues) {
        this.values = pValues;
    }

}
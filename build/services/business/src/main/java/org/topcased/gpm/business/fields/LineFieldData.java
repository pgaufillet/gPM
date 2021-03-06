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
public class LineFieldData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public LineFieldData() {
    }

    /**
     * Constructor taking all properties.
     */
    public LineFieldData(final long pRef,
            final org.topcased.gpm.business.fields.FieldData[] pFieldDatas) {
        this.ref = pRef;
        this.fieldDatas = pFieldDatas;
    }

    /**
     * Copies constructor from other LineFieldData
     */
    public LineFieldData(LineFieldData pOtherBean) {
        if (pOtherBean != null) {
            this.ref = pOtherBean.getRef();
            this.fieldDatas = pOtherBean.getFieldDatas();
        }
    }

    private long ref;

    /**
     * 
     */
    public long getRef() {
        return this.ref;
    }

    public void setRef(long pRef) {
        this.ref = pRef;
    }

    private org.topcased.gpm.business.fields.FieldData[] fieldDatas;

    /**
     * Get the fieldDatas
     */
    public org.topcased.gpm.business.fields.FieldData[] getFieldDatas() {
        return this.fieldDatas;
    }

    /**
     * Set the fieldDatas
     */
    public void setFieldDatas(
            org.topcased.gpm.business.fields.FieldData[] pFieldDatas) {
        this.fieldDatas = pFieldDatas;
    }

}
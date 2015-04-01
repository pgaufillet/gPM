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
package org.topcased.gpm.business.facilities;

/**
 * @author Atos
 */
public class GridColumnData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public GridColumnData() {
    }

    /**
     * Constructor taking all properties.
     */
    public GridColumnData(
            final java.lang.String pName,
            final org.topcased.gpm.domain.facilities.GridColumnEditorType pEditorType) {
        this.name = pName;
        this.editorType = pEditorType;
    }

    /**
     * Copies constructor from other GridColumnData
     */
    public GridColumnData(GridColumnData pOtherBean) {
        if (pOtherBean != null) {
            this.name = pOtherBean.getName();
            this.editorType = pOtherBean.getEditorType();
        }
    }

    private java.lang.String name;

    /**
     * 
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    private org.topcased.gpm.domain.facilities.GridColumnEditorType editorType;

    /**
     * 
     */
    public org.topcased.gpm.domain.facilities.GridColumnEditorType getEditorType() {
        return this.editorType;
    }

    public void setEditorType(
            org.topcased.gpm.domain.facilities.GridColumnEditorType pEditorType) {
        this.editorType = pEditorType;
    }

}
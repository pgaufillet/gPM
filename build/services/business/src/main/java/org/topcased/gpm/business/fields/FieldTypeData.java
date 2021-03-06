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
public class FieldTypeData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FieldTypeData() {
    }

    /**
     * Constructor taking all properties.
     */
    public FieldTypeData(
            final java.lang.String pId,
            final java.lang.String pLabelKey,
            final boolean pMultipleValueSupport,
            final java.lang.String pDescription,
            final boolean pPointerField,
            final org.topcased.gpm.business.fields.FieldAccessData pDefaultAccess,
            final org.topcased.gpm.business.fields.PointerFieldData pPointerFieldData) {
        this.id = pId;
        this.labelKey = pLabelKey;
        this.multipleValueSupport = pMultipleValueSupport;
        this.description = pDescription;
        this.pointerField = pPointerField;
        this.defaultAccess = pDefaultAccess;
        this.pointerFieldData = pPointerFieldData;
    }

    /**
     * Copies constructor from other FieldTypeData
     */
    public FieldTypeData(FieldTypeData pOtherBean) {
        if (pOtherBean != null) {
            this.id = pOtherBean.getId();
            this.labelKey = pOtherBean.getLabelKey();
            this.multipleValueSupport = pOtherBean.isMultipleValueSupport();
            this.description = pOtherBean.getDescription();
            this.pointerField = pOtherBean.isPointerField();
            this.defaultAccess = pOtherBean.getDefaultAccess();
            this.pointerFieldData = pOtherBean.getPointerFieldData();
        }
    }

    private java.lang.String id;

    /**
     * <p>
     * Identifier of this field definition.
     * </p>
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
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

    private boolean multipleValueSupport;

    /**
     * 
     */
    public boolean isMultipleValueSupport() {
        return this.multipleValueSupport;
    }

    public void setMultipleValueSupport(boolean pMultipleValueSupport) {
        this.multipleValueSupport = pMultipleValueSupport;
    }

    private java.lang.String description;

    /**
     * <p>
     * Translated value of description if exists. The description non translated
     * value otherwise.
     * </p>
     */
    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String pDescription) {
        this.description = pDescription;
    }

    private boolean pointerField;

    /**
     * 
     */
    public boolean isPointerField() {
        return this.pointerField;
    }

    public void setPointerField(boolean pPointerField) {
        this.pointerField = pPointerField;
    }

    private org.topcased.gpm.business.fields.FieldAccessData defaultAccess;

    /**
     * Get the defaultAccess
     */
    public org.topcased.gpm.business.fields.FieldAccessData getDefaultAccess() {
        return this.defaultAccess;
    }

    /**
     * Set the defaultAccess
     */
    public void setDefaultAccess(
            org.topcased.gpm.business.fields.FieldAccessData pDefaultAccess) {
        this.defaultAccess = pDefaultAccess;
    }

    private org.topcased.gpm.business.fields.PointerFieldData pointerFieldData;

    /**
     * Get the pointerFieldData
     */
    public org.topcased.gpm.business.fields.PointerFieldData getPointerFieldData() {
        return this.pointerFieldData;
    }

    /**
     * Set the pointerFieldData
     */
    public void setPointerFieldData(
            org.topcased.gpm.business.fields.PointerFieldData pPointerFieldData) {
        this.pointerFieldData = pPointerFieldData;
    }

}
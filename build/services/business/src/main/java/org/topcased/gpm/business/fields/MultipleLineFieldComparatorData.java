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
public class MultipleLineFieldComparatorData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public MultipleLineFieldComparatorData() {
    }

    /**
     * Constructor without default values.
     */
    public MultipleLineFieldComparatorData(
            final java.lang.String pLabelKey,
            final long pRef,
            final java.lang.String pI18nName,
            final org.topcased.gpm.business.fields.LineFieldComparatorData[] pLineFieldComparatorDatas) {
        this.labelKey = pLabelKey;
        this.ref = pRef;
        this.i18nName = pI18nName;
        this.lineFieldComparatorDatas = pLineFieldComparatorDatas;
    }

    /**
     * Constructor taking all properties.
     */
    public MultipleLineFieldComparatorData(
            final java.lang.String pLabelKey,
            final long pRef,
            final boolean pMultiLined,
            final boolean pMultiField,
            final boolean pExportable,
            final java.lang.String pI18nName,
            final boolean pConfidential,
            final org.topcased.gpm.business.fields.LineFieldComparatorData[] pLineFieldComparatorDatas) {
        this.labelKey = pLabelKey;
        this.ref = pRef;
        this.multiLined = pMultiLined;
        this.multiField = pMultiField;
        this.exportable = pExportable;
        this.i18nName = pI18nName;
        this.confidential = pConfidential;
        this.lineFieldComparatorDatas = pLineFieldComparatorDatas;
    }

    /**
     * Copies constructor from other MultipleLineFieldComparatorData
     */
    public MultipleLineFieldComparatorData(
            MultipleLineFieldComparatorData pOtherBean) {
        if (pOtherBean != null) {
            this.labelKey = pOtherBean.getLabelKey();
            this.ref = pOtherBean.getRef();
            this.multiLined = pOtherBean.isMultiLined();
            this.multiField = pOtherBean.isMultiField();
            this.exportable = pOtherBean.isExportable();
            this.i18nName = pOtherBean.getI18nName();
            this.confidential = pOtherBean.isConfidential();
            this.lineFieldComparatorDatas =
                    pOtherBean.getLineFieldComparatorDatas();
        }
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

    private boolean multiLined = false;

    /**
     * 
     */
    public boolean isMultiLined() {
        return this.multiLined;
    }

    public void setMultiLined(boolean pMultiLined) {
        this.multiLined = pMultiLined;
    }

    private boolean multiField = false;

    /**
     * 
     */
    public boolean isMultiField() {
        return this.multiField;
    }

    public void setMultiField(boolean pMultiField) {
        this.multiField = pMultiField;
    }

    private boolean exportable = true;

    /**
     * 
     */
    public boolean isExportable() {
        return this.exportable;
    }

    public void setExportable(boolean pExportable) {
        this.exportable = pExportable;
    }

    private java.lang.String i18nName;

    /**
     * 
     */
    public java.lang.String getI18nName() {
        return this.i18nName;
    }

    public void setI18nName(java.lang.String pI18nName) {
        this.i18nName = pI18nName;
    }

    private boolean confidential = false;

    /**
     * 
     */
    public boolean isConfidential() {
        return this.confidential;
    }

    public void setConfidential(boolean pConfidential) {
        this.confidential = pConfidential;
    }

    private org.topcased.gpm.business.fields.LineFieldComparatorData[] lineFieldComparatorDatas;

    /**
     * Get the lineFieldComparatorDatas
     */
    public org.topcased.gpm.business.fields.LineFieldComparatorData[] getLineFieldComparatorDatas() {
        return this.lineFieldComparatorDatas;
    }

    /**
     * Set the lineFieldComparatorDatas
     */
    public void setLineFieldComparatorDatas(
            org.topcased.gpm.business.fields.LineFieldComparatorData[] pLineFieldComparatorDatas) {
        this.lineFieldComparatorDatas = pLineFieldComparatorDatas;
    }

}
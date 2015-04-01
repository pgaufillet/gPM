/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.business.sheet.service;

/**
 * @author Atos
 */
public class FieldGroupData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FieldGroupData() {
    }

    /**
     * Constructor taking all properties.
     */
    public FieldGroupData(
            final java.lang.String pLabelKey,
            final java.lang.String pI18nName,
            final boolean pOpened,
            final org.topcased.gpm.business.fields.MultipleLineFieldData[] pMultipleLineFieldDatas) {
        this.labelKey = pLabelKey;
        this.i18nName = pI18nName;
        this.opened = pOpened;
        this.multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

    /**
     * Copies constructor from other FieldGroupData
     */
    public FieldGroupData(FieldGroupData pOtherBean) {
        if (pOtherBean != null) {
            this.labelKey = pOtherBean.getLabelKey();
            this.i18nName = pOtherBean.getI18nName();
            this.opened = pOtherBean.isOpened();
            this.multipleLineFieldDatas =
                    pOtherBean.getMultipleLineFieldDatas();
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

    private boolean opened;

    /**
     * 
     */
    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean pOpened) {
        this.opened = pOpened;
    }

    private org.topcased.gpm.business.fields.MultipleLineFieldData[] multipleLineFieldDatas;

    /**
     * Get the multipleLineFieldDatas
     */
    public org.topcased.gpm.business.fields.MultipleLineFieldData[] getMultipleLineFieldDatas() {
        return this.multipleLineFieldDatas;
    }

    /**
     * Set the multipleLineFieldDatas
     */
    public void setMultipleLineFieldDatas(
            org.topcased.gpm.business.fields.MultipleLineFieldData[] pMultipleLineFieldDatas) {
        this.multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

}
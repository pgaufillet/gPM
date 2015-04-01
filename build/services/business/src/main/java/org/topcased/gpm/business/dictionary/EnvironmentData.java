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
package org.topcased.gpm.business.dictionary;

/**
 * @author Atos
 */
public class EnvironmentData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public EnvironmentData() {
    }

    /**
     * Constructor taking all properties.
     */
    public EnvironmentData(
            final java.lang.String pId,
            final java.lang.String pLabelKey,
            final java.lang.String pI18nName,
            final java.lang.String pBusinessProcess,
            final java.lang.String pCategoryKey,
            final boolean pIsPublic,
            final org.topcased.gpm.business.dictionary.CategoryValueData[] pCategoryValueDatas) {
        this.id = pId;
        this.labelKey = pLabelKey;
        this.i18nName = pI18nName;
        this.businessProcess = pBusinessProcess;
        this.categoryKey = pCategoryKey;
        this.isPublic = pIsPublic;
        this.categoryValueDatas = pCategoryValueDatas;
    }

    /**
     * Copies constructor from other EnvironmentData
     */
    public EnvironmentData(EnvironmentData pOtherBean) {
        if (pOtherBean != null) {
            this.id = pOtherBean.getId();
            this.labelKey = pOtherBean.getLabelKey();
            this.i18nName = pOtherBean.getI18nName();
            this.businessProcess = pOtherBean.getBusinessProcess();
            this.categoryKey = pOtherBean.getCategoryKey();
            this.isPublic = pOtherBean.isIsPublic();
            this.categoryValueDatas = pOtherBean.getCategoryValueDatas();
        }
    }

    private java.lang.String id;

    /**
     * 
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

    private java.lang.String i18nName;

    /**
     * <p>
     * Name of the busines process this category belongs to.
     * </p>
     */
    public java.lang.String getI18nName() {
        return this.i18nName;
    }

    public void setI18nName(java.lang.String pI18nName) {
        this.i18nName = pI18nName;
    }

    private java.lang.String businessProcess;

    /**
     * 
     */
    public java.lang.String getBusinessProcess() {
        return this.businessProcess;
    }

    public void setBusinessProcess(java.lang.String pBusinessProcess) {
        this.businessProcess = pBusinessProcess;
    }

    private java.lang.String categoryKey;

    /**
     * 
     */
    public java.lang.String getCategoryKey() {
        return this.categoryKey;
    }

    public void setCategoryKey(java.lang.String pCategoryKey) {
        this.categoryKey = pCategoryKey;
    }

    private boolean isPublic;

    /**
     * 
     */
    public boolean isIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(boolean pIsPublic) {
        this.isPublic = pIsPublic;
    }

    private org.topcased.gpm.business.dictionary.CategoryValueData[] categoryValueDatas;

    /**
     * Get the categoryValueDatas
     */
    public org.topcased.gpm.business.dictionary.CategoryValueData[] getCategoryValueDatas() {
        return this.categoryValueDatas;
    }

    /**
     * Set the categoryValueDatas
     */
    public void setCategoryValueDatas(
            org.topcased.gpm.business.dictionary.CategoryValueData[] pCategoryValueDatas) {
        this.categoryValueDatas = pCategoryValueDatas;
    }

}
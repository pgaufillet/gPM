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
public class ChoiceFieldData extends
        org.topcased.gpm.business.fields.FieldTypeData implements
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ChoiceFieldData() {
    }

    /**
     * Constructor taking all properties.
     */
    public ChoiceFieldData(final java.lang.String pCategoryName,
            final java.lang.String pDefaultValue,
            final java.lang.String pValuesSeparator) {
        this.categoryName = pCategoryName;
        this.defaultValue = pDefaultValue;
        this.valuesSeparator = pValuesSeparator;
    }

    /**
     * Copies constructor from other ChoiceFieldData
     */
    public ChoiceFieldData(ChoiceFieldData pOtherBean) {
        if (pOtherBean != null) {
            this.categoryName = pOtherBean.getCategoryName();
            this.defaultValue = pOtherBean.getDefaultValue();
            this.valuesSeparator = pOtherBean.getValuesSeparator();
        }
    }

    private java.lang.String categoryName;

    /**
     * <p>
     * Name of the category containing the valid values for this field.
     * </p>
     */
    public java.lang.String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(java.lang.String pCategoryName) {
        this.categoryName = pCategoryName;
    }

    private java.lang.String defaultValue;

    /**
     * 
     */
    public java.lang.String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(java.lang.String pDefaultValue) {
        this.defaultValue = pDefaultValue;
    }

    private java.lang.String valuesSeparator;

    /**
     * 
     */
    public java.lang.String getValuesSeparator() {
        return this.valuesSeparator;
    }

    public void setValuesSeparator(java.lang.String pValuesSeparator) {
        this.valuesSeparator = pValuesSeparator;
    }

}
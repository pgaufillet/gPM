/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field;

import java.io.Serializable;
import java.util.List;

import org.topcased.gpm.business.util.FieldType;

/**
 * UiFitlerUsableField
 * 
 * @author nveillet
 */
public class UiFilterUsableField implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 8562611363337562757L;

    private String categoryName;

    private FieldType fieldType;

    private String id;

    private String name;

    private String parentFieldName;

    private List<UiFilterUsableField> subFields;

    private String translatedName;

    private boolean virtualField;

    private List<String> virtualPossibleValues;

    /**
     * Constructor
     */
    public UiFilterUsableField() {
    }

    /**
     * get category name
     * 
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * get field type
     * 
     * @return the field type
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * get identifier
     * 
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get parent field name
     * 
     * @return the parent field name
     */
    public String getParentFieldName() {
        return parentFieldName;
    }

    /**
     * get sub fields
     * 
     * @return the sub fields
     */
    public List<UiFilterUsableField> getSubFields() {
        return subFields;
    }

    /**
     * Get translated name.
     * 
     * @return translated name.
     */
    public String getTranslatedName() {
        return translatedName;
    }

    /**
     * get virtualPossibleValues
     * 
     * @return the virtualPossibleValues
     */
    public List<String> getVirtualPossibleValues() {
        return virtualPossibleValues;
    }

    /**
     * get virtualField
     * 
     * @return the virtualField
     */
    public boolean isVirtualField() {
        return virtualField;
    }

    /**
     * set category name
     * 
     * @param pCategoryName
     *            the category name to set
     */
    public void setCategoryName(String pCategoryName) {
        categoryName = pCategoryName;
    }

    /**
     * set field type
     * 
     * @param pFieldType
     *            field type
     */
    public void setFieldType(FieldType pFieldType) {
        fieldType = pFieldType;
    }

    /**
     * set identifier
     * 
     * @param pId
     *            the identifier to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set parent field name
     * 
     * @param pParentFieldName
     *            the parent field name to set
     */
    public void setParentFieldName(String pParentFieldName) {
        parentFieldName = pParentFieldName;
    }

    /**
     * set sub fields
     * 
     * @param pSubFields
     *            the sub fields to set
     */
    public void setSubFields(List<UiFilterUsableField> pSubFields) {
        this.subFields = pSubFields;
    }

    /**
     * Set translated name.
     * 
     * @param pTranslatedName
     *            translated name.
     */
    public void setTranslatedName(String pTranslatedName) {
        translatedName = pTranslatedName;
    }

    /**
     * set virtualField
     * 
     * @param pVirtualField
     *            the virtualField to set
     */
    public void setVirtualField(boolean pVirtualField) {
        virtualField = pVirtualField;
    }

    /**
     * set virtualPossibleValues
     * 
     * @param pVirtualPossibleValues
     *            the virtualPossibleValues to set
     */
    public void setVirtualPossibleValues(List<String> pVirtualPossibleValues) {
        virtualPossibleValues = pVirtualPossibleValues;
    }

}

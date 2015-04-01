/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.criteria;

import java.io.Serializable;
import java.util.LinkedList;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;

/**
 * UiFilterCriterion
 * 
 * @author nveillet
 */
public class UiFilterCriterion implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -1162477276827633555L;

    private boolean caseSensitive;

    private String categoryName;

    private FieldType fieldType;

    private String id;

    private LinkedList<UiFilterFieldName> name;

    private UiFilterOperator operator;

    private Serializable value;

    /**
     * Constructor
     */
    public UiFilterCriterion() {
    }

    /**
     * Constructor with values
     * 
     * @param pId
     *            the identifier
     * @param pName
     *            the name
     * @param pOperator
     *            the operator
     * @param pValue
     *            the value
     * @param pCaseSensitive
     *            the case sensitivity
     * @param pFieldType
     *            Field type.
     * @param pCategoryName
     *            Category name for choice fileds only (null for other fields)
     */
    public UiFilterCriterion(String pId, LinkedList<UiFilterFieldName> pName,
            UiFilterOperator pOperator, Serializable pValue,
            boolean pCaseSensitive, FieldType pFieldType, String pCategoryName) {
        id = pId;
        name = pName;
        operator = pOperator;
        value = pValue;
        caseSensitive = pCaseSensitive;
        fieldType = pFieldType;
        categoryName = pCategoryName;
    }

    /**
     * get category name.
     * 
     * @return category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

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
    public LinkedList<UiFilterFieldName> getName() {
        return name;
    }

    /**
     * get operator
     * 
     * @return the operator
     */
    public UiFilterOperator getOperator() {
        return operator;
    }

    /**
     * get value
     * 
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * get case sensitivity
     * 
     * @return the case sensitivity
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * set case sensitivity
     * 
     * @param pCaseSensitive
     *            the case sensitivity to set
     */
    public void setCaseSensitive(boolean pCaseSensitive) {
        caseSensitive = pCaseSensitive;
    }

    /**
     * set category name
     * 
     * @param pCategoryName
     *            category name.
     */
    public void setCategoryName(String pCategoryName) {
        categoryName = pCategoryName;
    }

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
    public void setName(LinkedList<UiFilterFieldName> pName) {
        name = pName;
    }

    /**
     * set operator
     * 
     * @param pOperator
     *            the operator to set
     */
    public void setOperator(UiFilterOperator pOperator) {
        operator = pOperator;
    }

    /**
     * set value
     * 
     * @param pValue
     *            the value to set
     */
    public void setValue(String pValue) {
        value = pValue;
    }

}

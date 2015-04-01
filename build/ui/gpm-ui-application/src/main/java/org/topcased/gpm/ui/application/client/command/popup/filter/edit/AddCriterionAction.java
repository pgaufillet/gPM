/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter.edit;

import java.io.Serializable;
import java.util.List;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.ui.application.client.event.EmptyAction;

/**
 * Add a criterion.
 * 
 * @author jlouisy
 */
public class AddCriterionAction extends EmptyAction<AddCriterionAction> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4839729466523229743L;

    private String categoryName;

    private String fieldName;

    private String fieldPath;

    private FieldType fieldType;

    private int groupIndex;

    private boolean isCaseSensitive;

    private String operator;

    private Serializable value;

    private boolean virtualField;

    private List<String> virtualPossibleValues;

    private String notTranslatedFieldName;

    /**
     * Default constructor
     */
    public AddCriterionAction() {

    }

    /**
     * create AddCriterionAction with parameters.
     * 
     * @param pIndexOfGroup
     *            index of criteria group.
     * @param pPath
     *            path to field.
     * @param pFieldName
     *            field name.
     * @param pFieldType
     *            field type.
     * @param pOperator
     *            operator.
     * @param pValue
     *            value to compare with.
     * @param pIsCaseSensitive
     *            case sensitivity.
     * @param pCategoryName
     *            category name.
     * @param pVirtualField
     *            virtual field.
     * @param pVirtualPossibleValues
     *            possibles values for the virtual fields.
     * @param pNotTranslatedFieldName
     *            name of the field nor translated
     */
    public AddCriterionAction(int pIndexOfGroup, String pPath,
            String pFieldName, FieldType pFieldType, String pOperator,
            Serializable pValue, boolean pIsCaseSensitive,
            String pCategoryName, boolean pVirtualField,
            List<String> pVirtualPossibleValues, String pNotTranslatedFieldName) {
        groupIndex = pIndexOfGroup;
        fieldPath = pPath;
        fieldName = pFieldName;
        fieldType = pFieldType;
        operator = pOperator;
        value = pValue;
        isCaseSensitive = pIsCaseSensitive;
        categoryName = pCategoryName;
        virtualField = pVirtualField;
        virtualPossibleValues = pVirtualPossibleValues;
        notTranslatedFieldName = pNotTranslatedFieldName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public String getOperator() {
        return operator;
    }

    public Serializable getValue() {
        return value;
    }

    /**
     * get virtualPossibleValues
     * 
     * @return the virtualPossibleValues
     */
    public List<String> getVirtualPossibleValues() {
        return virtualPossibleValues;
    }

    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    /**
     * get virtualField
     * 
     * @return the virtualField
     */
    public boolean isVirtualField() {
        return virtualField;
    }

    public void setGroupIndex(int pIndex) {
        groupIndex = pIndex;
    }

    /**
     * get notTranslatedFieldName
     * 
     * @return the notTranslatedFieldName
     */
    public String getNotTranslatedFieldName() {
        return notTranslatedFieldName;
    }

    /**
     * set notTranslatedFieldName
     * 
     * @param pNotTranslatedFieldName
     *            the notTranslatedFieldName to set
     */
    public void setNotTranslatedFieldName(String pNotTranslatedFieldName) {
        this.notTranslatedFieldName = pNotTranslatedFieldName;
    }
}
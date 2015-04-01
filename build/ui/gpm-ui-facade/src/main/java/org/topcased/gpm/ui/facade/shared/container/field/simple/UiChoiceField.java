/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.util.GpmStringUtils;

/**
 * UiChoiceField
 * 
 * @author nveillet
 */
public class UiChoiceField extends UiField implements BusinessChoiceField {

    /** serialVersionUID */
    private static final long serialVersionUID = 7308531794188833851L;

    private String categoryValue;

    private ChoiceDisplayHintType choiceDisplayHintType;

    protected List<Translation> possibleValues;

    /**
     * Create new UiChoiceField
     */
    public UiChoiceField() {
        super(FieldType.CHOICE);
        possibleValues = new ArrayList<Translation>();
        choiceDisplayHintType = ChoiceDisplayHintType.LIST;
    }

    /**
     * Add a possible value
     * 
     * @param pValue
     *            The value to add
     */
    public void addPossibleValue(Translation pValue) {
        boolean lContains = false;
        for (Translation lPossibleValue : possibleValues) {
            if (lPossibleValue.getValue().equals(pValue.getValue())) {
                lContains = true;
            }
        }

        if (!lContains) {
            possibleValues.add(pValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        categoryValue = pOther.getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.UiField#getAsString()
     */
    @Override
    public String getAsString() {
        return categoryValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    @Override
    public String getCategoryValue() {
        return categoryValue;
    }

    /**
     * Get the choice display hint type
     * 
     * @return The choice display hint type
     */
    public ChoiceDisplayHintType getChoiceDisplayHintType() {
        return choiceDisplayHintType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiChoiceField getEmptyClone() {
        UiChoiceField lField = new UiChoiceField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        lField.setChoiceDisplayHintType(choiceDisplayHintType);

        for (Translation lPossibleValue : getPossibleValues()) {
            lField.addPossibleValue(lPossibleValue);
        }

        return lField;
    }

    /**
     * Get possible value list
     * 
     * @return The possible value list
     */
    public List<Translation> getPossibleValues() {
        return possibleValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.UiField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        return GpmStringUtils.getEmptyIfNull(categoryValue).equals(
                GpmStringUtils.getEmptyIfNull(pOther.getAsString()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(String pValue) {
        categoryValue = pValue;
    }

    /**
     * Set the choice display hint type
     * 
     * @param pChoiceDisplayHintType
     *            The choice display hint type to set
     */
    public void setChoiceDisplayHintType(
            ChoiceDisplayHintType pChoiceDisplayHintType) {
        choiceDisplayHintType = pChoiceDisplayHintType;
    }
}

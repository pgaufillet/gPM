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

import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.ui.facade.shared.exception.NotImplementedException;

/**
 * UiStringChoiceField
 * 
 * @author nveillet
 */
public class UiStringChoiceField extends UiStringField {

    /** serialVersionUID */
    private static final long serialVersionUID = -7312062615010621730L;

    private List<String> possibleValues;

    private boolean strict;

    /**
     * Create new UiStringChoiceField
     */
    public UiStringChoiceField() {
        super();
        super.setStringDisplayHintType(StringDisplayHintType.CHOICE_STRING);
        possibleValues = new ArrayList<String>();
    }

    /**
     * Add a possible value
     * 
     * @param pValue
     *            The possible value to add
     */
    public void addPossibleValue(String pValue) {
        if (!possibleValues.contains(pValue)) {
            possibleValues.add(pValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField#getEmptyClone()
     */
    @Override
    public UiStringChoiceField getEmptyClone() {
        UiStringChoiceField lField = new UiStringChoiceField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        lField.setSize(getSize());
        lField.setHeight(getHeight());
        lField.setWidth(getWidth());
        lField.setStrict(strict);

        for (String lPossibleValues : getPossibleValues()) {
            lField.addPossibleValue(lPossibleValues);
        }

        return lField;
    }

    /**
     * Get possible value list
     * 
     * @return The possible value list
     */
    public List<String> getPossibleValues() {
        return possibleValues;
    }

    /**
     * Get strict property
     * 
     * @return The strict property
     */
    public boolean isStrict() {
        return strict;
    }

    /**
     * Set possible value list
     * 
     * @param pChoiceStringList
     *            All possible values
     */
    public void setPossibleValues(List<String> pChoiceStringList) {
        possibleValues = pChoiceStringList;
    }

    /**
     * Set strict property
     * 
     * @param pStrict
     *            The strict property to set
     */
    public void setStrict(boolean pStrict) {
        strict = pStrict;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField#setStringDisplayHintType(org.topcased.gpm.business.util.StringDisplayHintType)
     */
    @Override
    public void setStringDisplayHintType(
            StringDisplayHintType pStringDisplayHintType) {
        throw new NotImplementedException("Not implemented method");
    }
}

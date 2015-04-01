/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;
import java.util.Map.Entry;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.component.client.field.GpmRadioWidget;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.user.client.ui.RadioButton;

/**
 * GpmTextBox refers a TextBox in the meaning of GWT but adapted for the gPM
 * core.
 * 
 * @author frosier
 */
public class GpmRadioBox extends AbstractGpmField<GpmRadioWidget> implements
        BusinessChoiceField, IGpmChoiceBox {
    private static Long staticRadioBoxCounter = new Long(0);

    private ChoiceDisplayHintType displayHintType;

    private List<GpmChoiceBoxValue> possibleValues;

    /**
     * Creates an empty gPM radio box.
     * 
     * @param pChoiceDisplayHintType
     *            The choice display hint type
     */
    public GpmRadioBox(ChoiceDisplayHintType pChoiceDisplayHintType) {
        super(new GpmRadioWidget(staticRadioBoxCounter.toString(),
                pChoiceDisplayHintType));
        staticRadioBoxCounter++;
        displayHintType = pChoiceDisplayHintType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        setCategoryValue(((BusinessChoiceField) pOther).getCategoryValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        return GpmStringUtils.getEmptyIfNull(pOther.getAsString()).equals(
                GpmStringUtils.getEmptyIfNull(getCategoryValue()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Return:
     * <ul>
     * <li>the text of the RadioButtonValue if radio box type is NONE</li>
     * <li>the image url of the RadioButtonValue if radio box type is IMAGE</li>
     * <li>the text of the RadioButtonValue if radio box type is IMAGE_TEXT</li>
     * </ul>
     * </p>
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    @Override
    public String getCategoryValue() {
        // Look in the map for the checked radio button.
        for (Entry<GpmChoiceBoxValue, RadioButton> lEntry : getWidget().getRadioButtons().entrySet()) {
            if (lEntry.getValue().getValue()) {
                return lEntry.getKey().getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(final String pValue) {
        for (GpmChoiceBoxValue lKey : getWidget().getRadioButtons().keySet()) {
            if (lKey.getValue().equals(pValue)) {
                getWidget().getRadioButtons().get(lKey).setValue(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox#setPossibleValues(java.util.List)
     */
    @Override
    public void setPossibleValues(List<GpmChoiceBoxValue> pPossibleValues) {
        possibleValues = pPossibleValues;
        getWidget().clear();
        getWidget().init(pPossibleValues);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        for (RadioButton lButton : getWidget().getRadioButtons().values()) {
            lButton.setEnabled(pEnabled);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmRadioBox getEmptyClone() {
        final GpmRadioBox lClone = new GpmRadioBox(displayHintType);

        initEmptyClone(lClone);
        lClone.setPossibleValues(possibleValues);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        boolean lResult = true;
        for (RadioButton lButton : getWidget().getRadioButtons().values()) {
            lResult = lResult && lButton.isEnabled();
        }
        return lResult;
    }
}
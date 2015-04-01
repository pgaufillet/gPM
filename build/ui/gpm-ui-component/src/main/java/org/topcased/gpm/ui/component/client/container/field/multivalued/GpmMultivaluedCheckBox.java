/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field.multivalued;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * GpmMultivaluedRadioBox
 * 
 * @author jeballar
 */
public class GpmMultivaluedCheckBox extends
        AbstractGpmMultivaluedChoiceField<FlowPanel> implements IGpmChoiceBox {
    private boolean enabled;

    private List<GpmChoiceBoxValue> possibleValues;

    /**
     * Constructor
     */
    public GpmMultivaluedCheckBox() {
        super(new FlowPanel());
        enabled = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox#setPossibleValues(java.util.List)
     */
    @Override
    public void setPossibleValues(final List<GpmChoiceBoxValue> pPossibleValues) {
        possibleValues = pPossibleValues;
        getWidget().clear();
        for (final GpmChoiceBoxValue lValue : possibleValues) {
            final CheckBox lCheckBox = new CheckBox();

            lCheckBox.setText(lValue.getDisplayedValue());
            lCheckBox.getElement().getStyle().setFloat(
                    com.google.gwt.dom.client.Style.Float.LEFT);
            lCheckBox.getElement().getStyle().setProperty("clear", "left");
            lCheckBox.setEnabled(enabled);
            getWidget().add(lCheckBox);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(final boolean pEnabled) {
        int i = 0;

        enabled = pEnabled;
        while (i < getWidget().getWidgetCount()) {
            ((CheckBox) getWidget().getWidget(i++)).setEnabled(pEnabled);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmMultivaluedCheckBox getEmptyClone() {
        final GpmMultivaluedCheckBox lClone = new GpmMultivaluedCheckBox();

        initEmptyClone(lClone);
        lClone.setPossibleValues(possibleValues);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField#getValues()
     */
    @Override
    public List<String> getSelectedValues() {
        final List<String> lList = new ArrayList<String>();

        for (int i = 0; i < possibleValues.size(); i++) {
            if (((CheckBox) getWidget().getWidget(i)).getValue()) {
                lList.add(possibleValues.get(i).getValue());
            }
        }

        return lList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField#setWidgetValueSelected(java.lang.String,
     *      boolean)
     */
    @Override
    public void setWidgetValueSelected(final String pValue,
            final boolean pSelected) {
        for (int i = 0; i < possibleValues.size(); i++) {
            if (possibleValues.get(i).getValue().equals(pValue)) {
                ((CheckBox) getWidget().getWidget(i)).setValue(pSelected);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        boolean lResult = true;
        int i = 0;
        while (i < getWidget().getWidgetCount()) {
            lResult =
                    lResult
                            && ((CheckBox) getWidget().getWidget(i++)).isEnabled();
        }
        return lResult;
    }
}
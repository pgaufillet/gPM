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
import org.topcased.gpm.ui.component.client.field.GpmListBoxWidget;

/**
 * GpmMultivaluedListBox
 * 
 * @author jeballar
 */
public class GpmMultivaluedListBox extends
        AbstractGpmMultivaluedChoiceField<GpmListBoxWidget> implements
        IGpmChoiceBox {
    private List<GpmChoiceBoxValue> possibleValues;

    /**
     * Constructor
     */
    public GpmMultivaluedListBox() {
        super(new GpmListBoxWidget(true));
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
        for (GpmChoiceBoxValue lValue : pPossibleValues) {
            getWidget().addItem(lValue.getDisplayedValue(), lValue.getValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmMultivaluedListBox getEmptyClone() {
        final GpmMultivaluedListBox lClone = new GpmMultivaluedListBox();

        initEmptyClone(lClone);
        lClone.setPossibleValues(possibleValues);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(final boolean pEnabled) {
        getWidget().setEnabled(pEnabled);
    }

    /**
     * Select or unselect a value.
     * 
     * @param pValue
     *            The value.
     * @param pSelected
     *            If it's a select action.
     */
    public void setWidgetValueSelected(final String pValue,
            final boolean pSelected) {
        for (int i = 0; i < getWidget().getItemCount(); i++) {
            if (getWidget().getValue(i).equals(pValue)) {
                getWidget().setItemSelected(i, pSelected);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField#getSelectedValues()
     */
    @Override
    protected List<String> getSelectedValues() {
        final List<String> lList = new ArrayList<String>();

        for (int i = 0; i < getWidget().getItemCount(); i++) {
            if (getWidget().isItemSelected(i)) {
                lList.add(getWidget().getValue(i));
            }
        }

        return lList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }
}
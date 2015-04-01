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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.field.GpmListBoxWidget;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.event.dom.client.ChangeHandler;

/**
 * GpmTextBox refers a ListBox in the meaning of GWT but adapted for the gPM
 * core.
 * 
 * @author frosier
 */
public class GpmListBox extends AbstractGpmField<GpmListBoxWidget> implements
        BusinessStringField, BusinessChoiceField, IGpmChoiceBox {

    /**
     * Creates an empty gPM TextBox.
     */
    public GpmListBox() {
        super(new GpmListBoxWidget(false));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        if (getWidget().getSelectedIndex() == -1) {
            return null;
        }
        else {
            return getWidget().getValue(getWidget().getSelectedIndex());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    @Override
    public String getCategoryValue() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
    	if (pValue == null) {
    		return;
    	}
    	
        boolean lSelected = false;
        int i = 0;

        while (i < getWidget().getItemCount() && !lSelected) {
            if (getWidget().getValue(i).equals(pValue)) {
                getWidget().setSelectedIndex(i);
                lSelected = true;
            }
            i++;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final String pValue) {
        setAsString(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(final String pValue) {
        setAsString(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox#setPossibleValues(java.util.List)
     */
    @Override
    public void setPossibleValues(List<GpmChoiceBoxValue> pPossibleValues) {
        getWidget().clear();
        for (GpmChoiceBoxValue lValue : pPossibleValues) {
            getWidget().addItem(lValue.getDisplayedValue(), lValue.getValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            setCategoryValue(((BusinessChoiceField) pOther).getCategoryValue());
        }
        else {
            set(((BusinessStringField) pOther).get());
        }
    }

    /**
     * The category value of the business choice field must be the same as
     * selected. {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            return GpmStringUtils.getEmptyIfNull(
                    ((BusinessChoiceField) pOther).getCategoryValue()).equals(
                    GpmStringUtils.getEmptyIfNull(getCategoryValue()));
        }
        else {
            return GpmStringUtils.getEmptyIfNull(
                    ((BusinessStringField) pOther).get()).equals(
                    GpmStringUtils.getEmptyIfNull(get()));
        }
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
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmListBox getEmptyClone() {
        final GpmListBox lClone = new GpmListBox();
        final List<GpmChoiceBoxValue> lPossibleValues =
                new ArrayList<GpmChoiceBoxValue>();

        initEmptyClone(lClone);
        for (int i = 0; i < getWidget().getItemCount(); i++) {
            lPossibleValues.add(new GpmChoiceBoxValue(getWidget().getValue(i),
                    getWidget().getItemText(i)));
        }
        lClone.setPossibleValues(lPossibleValues);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    @Override
    public String getInternalUrlSheetReference() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    @Override
    public int getSize() {
        throw new NotImplementedException("Not implemented method");
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

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#clear()
     */
    @Override
    public void clear() {
        getWidget().clear();
    }

    /**
     * add a change handler
     * 
     * @param pHandler
     *            the change handler
     */
    public void addChangeHandler(ChangeHandler pHandler) {
        getWidget().addChangeHandler(pHandler);
    }
}
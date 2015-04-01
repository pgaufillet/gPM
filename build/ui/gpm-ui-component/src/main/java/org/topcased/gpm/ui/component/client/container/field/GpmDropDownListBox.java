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
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.field.GpmDropDownListBoxWidget;
import org.topcased.gpm.ui.component.client.field.GpmDropDownListBoxWidget.ListPopupElement;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

/**
 * GpmComboBox refers a TextBox with a relative list popup to aliment the text
 * box content.
 * <p>
 * This component adapts GpmComboBoxWidget for the gPM core.
 * <p/>
 * 
 * @author frosier
 */
public class GpmDropDownListBox extends
        AbstractGpmField<GpmDropDownListBoxWidget<String>> implements
        BusinessStringField, IGpmChoiceBox {

    /**
     * Create a GpmDropDownListBox.
     */
    public GpmDropDownListBox() {
        super(new GpmDropDownListBoxWidget<String>(
                GpmStringFormatter.getInstance()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return getWidget().getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(String pValue) {
        getWidget().setValue(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        set(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox#setPossibleValues(java.util.List)
     */
    @Override
    public void setPossibleValues(List<GpmChoiceBoxValue> pPossibleValues) {
        getWidget().reset();
        for (GpmChoiceBoxValue lValue : pPossibleValues) {
            getWidget().addItem(lValue.getDisplayedValue(),
                    lValue.getDisplayedValue(), lValue.getValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        set(((BusinessStringField) pOther).get());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        return GpmStringUtils.getEmptyIfNull(pOther.getAsString()).equals(
                GpmStringUtils.getEmptyIfNull(get()));
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
    public void setEnabled(final boolean pEnabled) {
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @SuppressWarnings("rawtypes")
	@Override
    public GpmDropDownListBox getEmptyClone() {
        final GpmDropDownListBox lClone = new GpmDropDownListBox();
        final List<GpmChoiceBoxValue> lPossibleValues =
                new ArrayList<GpmChoiceBoxValue>();

        initEmptyClone(lClone);
        for (ListPopupElement lItem : getWidget().getItems()) {
            lPossibleValues.add(new GpmChoiceBoxValue(
                    lItem.getValueObject().toString(), lItem.getPopupText()));
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
     * The number of pixel for the width.
     * 
     * @param pWidth
     *            The width in pixel.
     */
    public void setPixelWidth(final int pWidth) {
        if (pWidth > 0) {
            getWidget().setWidth(String.valueOf(pWidth) + "px");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        getWidget().setValue(null);
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
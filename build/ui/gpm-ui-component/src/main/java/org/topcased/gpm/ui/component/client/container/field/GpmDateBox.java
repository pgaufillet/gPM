/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 *               Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.Date;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.ui.component.client.field.GpmDateBoxWidget;
import org.topcased.gpm.ui.component.client.field.HasFormat;
import org.topcased.gpm.ui.component.client.util.GpmDateTimeHelper;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GpmDateBox refers a DateBox in the meaning of GWT but adapted for the gPM
 * core.
 * 
 * @author frosier
 */
public class GpmDateBox extends AbstractGpmField<GpmDateBoxWidget> implements
        BusinessDateField, HasFormat {
    /**
     * Creates an empty gPM Date Box.
     */
    public GpmDateBox() {
        super(new GpmDateBoxWidget());
    }

    /**
     * Creates an empty gPM Date Box with a specific format.
     * 
     * @param pFormat
     *            The format.
     */
    public GpmDateBox(final DateTimeFormat pFormat) {
        super(new GpmDateBoxWidget());
        setFormat(pFormat);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public Date get() {
        return getWidget().get();
    }

    /**
     * Get the string value of the widget (not formatted).
     * 
     * @return the string value of the date box field
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final Date pValue) {
        getWidget().set(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        set(getWidget().getFormat().parse(pValue));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        set(((BusinessDateField) pOther).get());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        if (get() == null) {
            if (((BusinessDateField) pOther).get() == null) {
                return true;
            }
            return false;
        }
        return GpmDateTimeHelper.getDailyAccuracy(get()).equals(
                GpmDateTimeHelper.getDailyAccuracy(((BusinessDateField) pOther).get()));
    }

    /**
     * Get the date format.
     * 
     * @param pFormat
     *            The date format.
     */
    public void setFormat(final DateTimeFormat pFormat) {
        getWidget().setFormat(pFormat);
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
    public GpmDateBox getEmptyClone() {
        final GpmDateBox lClone = new GpmDateBox(getWidget().getFormat());

        initEmptyClone(lClone);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.HasFormat#getFormat()
     */
    @Override
    public DateTimeFormat getFormat() {
        return getWidget().getFormat();
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
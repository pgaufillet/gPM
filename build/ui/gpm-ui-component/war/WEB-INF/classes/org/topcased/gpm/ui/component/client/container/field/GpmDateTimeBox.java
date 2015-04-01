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

import java.util.Date;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.ui.component.client.field.GpmDateTimeBoxWidget;
import org.topcased.gpm.ui.component.client.field.HasFormat;
import org.topcased.gpm.ui.component.client.util.GpmDateTimeHelper;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GpmDateTimeBox refers a DateTimeBox but adapted for the gPM core.
 * <p>
 * It is composed of 2 text boxes with popup pickers :
 * <ul>
 * <li>one for the date : day, month and year,</li>
 * <li>one for the time : hours and minutes.</li>
 * </ul>
 * </p>
 * 
 * @author frosier
 */
public class GpmDateTimeBox extends AbstractGpmField<GpmDateTimeBoxWidget>
        implements BusinessDateField, HasFormat {
    /**
     * Creates an empty date time box.
     * <p>
     * This widget is composed of two boxes, one for the date (daily accuracy),
     * the other for the time.
     * </p>
     */
    public GpmDateTimeBox() {
        super(new GpmDateTimeBoxWidget());
    }

    /**
     * Creates an empty gPM Date Box with a specific format.
     * 
     * @param pFormat
     *            The format.
     */
    public GpmDateTimeBox(final DateTimeFormat pFormat) {
        super(new GpmDateTimeBoxWidget());
        setFormat(pFormat);
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

    /** BusinessDateField implementation **/

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        if (get() == null) {
            if (((BusinessDateField) pOther).get() == null) {
                return true;
            }
            return false;
        }
        return GpmDateTimeHelper.getMinuteAccuracy(
                ((BusinessDateField) pOther).get()).equals(get());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note: <br/>
     * The accuracy of the date is on minutes. Seconds are always set at 00.
     * </p>
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public Date get() {
        return getWidget().getValue();
    }

    /**
     * get the date not formatted
     * 
     * @return the string no formatted
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getValueNotFormatted();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final Date pValue) {
        getWidget().setValue(pValue);
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
    public GpmDateTimeBox getEmptyClone() {
        final GpmDateTimeBox lClone =
                new GpmDateTimeBox(getWidget().getFormat());

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

    /**
     * getter for the date
     * 
     * @return the string value of the date
     */
    public String getDateAsString() {
        return getWidget().getDateAsString();
    }
}
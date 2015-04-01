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

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.user.client.ui.TextBox;

/**
 * GpmTextBox refers a TextBox in the meaning of GWT but adapted for the gPM
 * core.
 * 
 * @param <T>
 *            The type of value.
 * @author frosier
 */
public class GpmTextBox<T> extends AbstractGpmField<TextBox> implements
        BusinessSimpleField<T> {
    private final GpmFormatter<T> formatter;

    private boolean maxSizeDefined = false;

    private int width;

    /**
     * Creates an empty gPM Text Box.
     * 
     * @param pFormatter
     *            The formatter.
     */
    public GpmTextBox(final GpmFormatter<T> pFormatter) {
        super(new TextBox());
        formatter = pFormatter;
        width = -1;
        getWidget().addStyleName(
                ComponentResources.INSTANCE.css().gpmTextArea());
    }
    
    /**
     * Creates an empty gPM Text Box with a maximum size
     * @param pFormatter
     *        The formatter
     * @param pSize
     *        The maximum length of the textbox
     */
    public GpmTextBox(final GpmFormatter<T> pFormatter, int pSize){
        this(pFormatter);
        this.setSize(pSize);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public T get() {
        return formatter.parse(getAsString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        getWidget().setValue(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final T pValue) {
        setAsString(formatter.format(pValue));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(final BusinessField pOther) {
        set(((BusinessSimpleField<T>) pOther).get());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        // case for business string fields (comparison not null string values).
        if (pOther instanceof BusinessStringField) {
            return GpmStringUtils.getEmptyIfNull(pOther.getAsString()).equals(
                    getAsString());
        }
        else {
            if (isBothNull((BusinessSimpleField<?>) pOther)) {
                return true;
            }
            else if (isOneOfBothNull((BusinessSimpleField<?>) pOther)) {
                return true;
            }
        }

        return ((BusinessSimpleField<?>) pOther).get().equals(get());
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
    public GpmTextBox<T> getEmptyClone() {
        final GpmTextBox<T> lClone = new GpmTextBox<T>(formatter);

        initEmptyClone(lClone);
        if (maxSizeDefined) {
            lClone.setSize(getSize());
        }
        lClone.setPixelWidth(getPixelWidth());

        return lClone;
    }

    /**
     * Get the maximum character number of the field
     * 
     * @return The size of the field
     */
    public int getSize() {
        return getWidget().getMaxLength();
    }

    /**
     * Set the maximum character number of the field
     * 
     * @param pSize
     *            The size of the field
     */
    public void setSize(int pSize) {
        if (pSize >= 0) {
            maxSizeDefined = true;
            getWidget().setMaxLength(pSize);
            getWidget().setTitle(
                    getWidget().getTitle() + "(max. " + pSize + ")");
        }
    }

    /**
     * Get the number of pixel for the width.
     * 
     * @return the pixel width
     */
    public int getPixelWidth() {
        return width;
    }

    /**
     * The number of pixel for the width.
     * 
     * @param pWidth
     *            The width in pixel.
     */
    public void setPixelWidth(final int pWidth) {
        width = pWidth;
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
        set(null);
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
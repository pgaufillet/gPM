/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier JUIN (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * GpmTextBox refers a TextBox in the meaning of GWT but adapted for the gPM
 * core, plus a label.
 * 
 * @param <T>
 *            the Text type
 * @author ojuin
 */
public class GpmLabelledTextBox<T> extends AbstractGpmField<HorizontalPanel>
        implements BusinessSimpleField<T> {

    private final GpmFormatter<T> formatter;

    private boolean maxSizeDefined = false;

    private int width;

    private TextBox textBox;

    private String labelString;

    /**
     * Creates an empty gPM Text Box.
     * 
     * @param pFormatter
     *            the formatter.
     * @param pLabelText
     *            the text label
     */
    public GpmLabelledTextBox(final GpmFormatter<T> pFormatter,
            String pLabelText) {
        super(new HorizontalPanel());
        formatter = pFormatter;
        labelString = pLabelText;
        width = -1;
        textBox = new TextBox();
        textBox.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        getWidget().add(textBox);
        Label lLabel = new Label(pLabelText);
        lLabel.setHorizontalAlignment(Label.ALIGN_LEFT);
        getWidget().add(lLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getAsString()
     */
    @Override
    public String getAsString() {
        return textBox.getValue();
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
        textBox.setValue(pValue);
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
        textBox.setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmLabelledTextBox<T> getEmptyClone() {
        final GpmLabelledTextBox<T> lClone =
                new GpmLabelledTextBox<T>(formatter, labelString);

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
        return textBox.getMaxLength();
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
            textBox.setMaxLength(pSize);
            textBox.setTitle(getWidget().getTitle() + "(max. " + pSize + ")");
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
        return textBox.isEnabled();
    }

    /**
     * Get the internal text box
     * 
     * @return the internal text box
     */
    public TextBox getTextBox() {
        return textBox;
    }
}
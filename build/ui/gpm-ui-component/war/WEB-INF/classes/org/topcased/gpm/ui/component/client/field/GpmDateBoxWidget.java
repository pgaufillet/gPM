/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import java.util.Date;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * GpmDateBox refers a DateBox in the meaning of GWT but adapted for the gPM
 * core.
 * 
 * @author tpanuel
 */
public class GpmDateBoxWidget extends TextBox {
    private static final String STYLE_FORMAT_ERROR =
            ComponentResources.INSTANCE.css().gpmDateBoxFormatError();

    private static final String STYLE_DATE_BOX =
            ComponentResources.INSTANCE.css().gpmDateBox();

    private static final String NULL_DATE_STRING = "";

    private DateTimeFormat dateTimeFormat;

    /**
     * Creates an empty gPM Date Box.
     */
    public GpmDateBoxWidget() {
        super();

        setStylePrimaryName(STYLE_DATE_BOX);
        addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        sinkEvents(Event.ONFOCUS | Event.ONCHANGE);
    }

    /**
     * Creates an empty gPM Date Box initialized with a date/time format
     * pattern.
     * 
     * @param pFormat
     *            The date/time format pattern.
     */
    public GpmDateBoxWidget(final DateTimeFormat pFormat) {
        this();
        dateTimeFormat = pFormat;
        addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.TextBoxBase#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(final Event pEvent) {
        super.onBrowserEvent(pEvent);
        switch (DOM.eventGetType(pEvent)) {
            case Event.ONFOCUS:
                GpmDatePickerPopup.getInstance().showRelativeTo(this);
                break;
            case Event.ONCHANGE:
                GpmDatePickerPopup.getInstance().hide();

                final String lText = getText().trim();

                try {
                    if (!lText.isEmpty()) {
                        getFormat().parse(lText);
                    }
                    removeStyleName(STYLE_FORMAT_ERROR);
                }
                catch (IllegalArgumentException e) {
                    addStyleName(STYLE_FORMAT_ERROR);
                }
                catch (StringIndexOutOfBoundsException e) {
                    addStyleName(STYLE_FORMAT_ERROR);
                }
                break;
            default:
                // Do nothing !
        }
    }

    /**
     * Get the date format.
     * 
     * @return The date format.
     */
    public DateTimeFormat getFormat() {
        if (dateTimeFormat == null) {
            dateTimeFormat = DateTimeFormat.getMediumDateFormat();
        }
        return dateTimeFormat;
    }

    /**
     * Get the date format.
     * 
     * @param pFormat
     *            The date format.
     */
    public void setFormat(DateTimeFormat pFormat) {
        dateTimeFormat = pFormat;
    }

    /**
     * Get the value of the field NOT FORMATTED
     * 
     * @return the string
     */
    public String getAsString() {
        return getText().trim();
    }

    /**
     * Getter on the date.
     * 
     * @return The date.
     */
    public Date get() {
        final String lText = getText().trim();
        if (lText.isEmpty()) {
            return null;
        }
        else {
            try {
                return getFormat().parse(lText);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
            catch (StringIndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    /**
     * Setter on the date.
     * 
     * @param pDate
     *            The new date.
     */
    public void set(final Date pDate) {

        if (pDate == null) {
            setValue(NULL_DATE_STRING, true);
        }
        else {
            setValue(getFormat().format(pDate), true);
        }
    }
}
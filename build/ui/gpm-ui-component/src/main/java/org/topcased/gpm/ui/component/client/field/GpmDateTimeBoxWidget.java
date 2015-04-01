/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import java.util.Date;

import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.util.GpmDateTimeHelper;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;

/**
 * GpmDateTimeBoxWidget is a gPM widget used to define the date with a time.
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
public class GpmDateTimeBoxWidget extends FlowPanel implements HasValue<Date>,
        HasChangeHandlers {

    private static final double RIGHT_MARGIN = 5;

    private GpmDateBoxWidget dateBox;

    private GpmTimePickerWidget timePicker;

    /**
     * Creates an empty date time box.
     */
    public GpmDateTimeBoxWidget() {
        super();

        dateBox = new GpmDateBoxWidget();

        add(dateBox);
        dateBox.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        dateBox.getElement().getStyle().setMarginRight(RIGHT_MARGIN, Unit.PX);

        timePicker = new GpmTimePickerWidget();
        add(timePicker);
        timePicker.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);

        dateBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> pEvent) {
                if (!dateBox.getValue().isEmpty()
                        && timePicker.getValue().isEmpty()) {
                    timePicker.setValue(GpmDateTimeHelper.DEFAULT_TIME_FORMAT.format(new Date()));
                }
                //Fire manually a changeEvent in order to enable the validation of the timebox
                DomEvent.fireNativeEvent(Document.get().createChangeEvent(),
                        timePicker);
            }
        });

        dateBox.sinkEvents(Event.ONFOCUS | Event.ONCHANGE);
        timePicker.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent pEvent) {
                if ((dateBox.getValue().equals(""))
                        && (!timePicker.getValue().equals(""))) {
                    dateBox.set(new Date());
                }
            }
        });
        timePicker.sinkEvents(Event.ONFOCUS | Event.ONCHANGE | Event.ONBLUR);

    }

    /**
     * Creates an empty date time box with a specified date/time format.
     * 
     * @param pFormat
     *            The date/time format pattern.
     */
    public GpmDateTimeBoxWidget(final DateTimeFormat pFormat) {
        this();
        setFormat(pFormat);
    }

    /**
     * Get the date format.
     * 
     * @return The date format.
     */
    public DateTimeFormat getFormat() {
        return dateBox.getFormat();
    }

    /**
     * Get the date format.
     * 
     * @param pFormat
     *            The date format.
     */
    public void setFormat(final DateTimeFormat pFormat) {
        dateBox.setFormat(pFormat);
    }

    /**
     * Getter on the date.
     * <p>
     * It add the time of the date box and the time of the date picker
     * </p>
     * 
     * @return The date.
     */
    @SuppressWarnings("deprecation")
    public Date getValue() {
        try {
            // The Day/Month/Year date components.
            final Date lDate = dateBox.get();

            if (lDate != null) {
                // The Hours/Minutes date components.
                final Date lTimePickerDate =
                        GpmDateTimeHelper.DEFAULT_TIME_FORMAT.parse(timePicker.getValue());

                // Assign only hours and minutes to the returned value. 
                lDate.setHours(lTimePickerDate.getHours());
                lDate.setMinutes(lTimePickerDate.getMinutes());
                lDate.setSeconds(0);
            }

            return lDate;
        }
        catch (IllegalArgumentException e) {
            return null;
        }
        catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get the text value of the rime picker
     * 
     * @return the string of the date picker, not formatted
     */
    public String getValueNotFormatted() {
        String lValueNotFormatted = "";
        if (!dateBox.getText().isEmpty()) {
            lValueNotFormatted = dateBox.getText();
            if (!timePicker.getText().isEmpty()) {
                lValueNotFormatted =
                        lValueNotFormatted + " " + timePicker.getText();
            }
        }
        return lValueNotFormatted;
    }

    /**
     * Setter on the date.
     * 
     * @param pDate
     *            The new date.
     */
    public void setValue(final Date pDate) {
        if (pDate != null) {
            dateBox.set(pDate);
            timePicker.setValue(GpmDateTimeHelper.DEFAULT_TIME_FORMAT.format(pDate));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
     *      boolean)
     */
    @Override
    public void setValue(Date pDate, boolean pFireEvents) {
        dateBox.set(GpmDateTimeHelper.getDailyAccuracy(pDate));
        timePicker.setValue(
                GpmDateTimeHelper.DEFAULT_TIME_FORMAT.format(pDate),
                pFireEvents);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Date> pHandler) {
        throw new NotImplementedException();
    }

    /**
     * Enable disable field
     * 
     * @param pEnabled
     *            <code>true</code> to enable field, <code>false</code> to
     *            disable
     */
    public void setEnabled(boolean pEnabled) {
        dateBox.setEnabled(pEnabled);
        timePicker.setEnabled(pEnabled);
    }

    public boolean isEnabled() {
        return dateBox.isEnabled() && timePicker.isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasChangeHandlers#addChangeHandler(com.google.gwt.event.dom.client.ChangeHandler)
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler pHandler) {
        return timePicker.addChangeHandler(pHandler);
    }

    /**
     * getter for the date
     * 
     * @return the string value of the date
     */
    public String getDateAsString() {
        return dateBox.getText();
    }

}

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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Pop up including a date picker.
 * 
 * @author tpanuel
 */
public final class GpmDatePickerPopup extends PopupPanel {
    private final static GpmDatePickerPopup INSTANCE = new GpmDatePickerPopup();

    private final GpmDatePicker picker;

    private GpmDateBoxWidget currentDateBox;

    private GpmDatePickerPopup() {
        super(true);
        picker = new GpmDatePicker();
        picker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Date> pEvent) {
                currentDateBox.set(pEvent.getValue());
                hide();
            }
        });
        setWidget(picker);
    }

    /**
     * Get the common date picker pop up.
     * 
     * @return The common date picker pop up.
     */
    public final static GpmDatePickerPopup getInstance() {
        return INSTANCE;
    }

    /**
     * Get the date picker.
     * 
     * @return The date pricker.
     */
    public DatePicker getDatePicker() {
        return picker;
    }

    /**
     * Show the popup for a specific date box.
     * 
     * @param pCurrentDateBox
     *            The specific date box.
     */
    public void showRelativeTo(final GpmDateBoxWidget pCurrentDateBox) {
        if (currentDateBox != null) {
            removeAutoHidePartner(currentDateBox.getElement());
        }
        currentDateBox = pCurrentDateBox;
        addAutoHidePartner(pCurrentDateBox.getElement());
        refresh();
        super.showRelativeTo(pCurrentDateBox);
    }

    /**
     * Refresh the value of date pricker.
     */
    public void refresh() {

        Date lCurrent = currentDateBox.get();

        if (lCurrent == null) {
            lCurrent = new Date();
        }

        picker.setCurrentMonth(lCurrent);
        picker.setValue(lCurrent);

        picker.getGpmCalendarView().getGrid().setSelected(
                picker.getGpmCalendarView().getCell(lCurrent));
    }
}
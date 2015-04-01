/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * GpmDatePicker. Class implementing a new DatePicker to override CSS in GWT
 * DatePicker
 * 
 * @see DatePicker
 * @author jeballar
 */
public class GpmDatePicker extends DatePicker {

    private static final String STYLE_DATE_PICKER =
            ComponentResources.INSTANCE.css().gpmDatePickerPopup();

    GpmDatePicker() {
        super(new GpmMonthSelector(), new GpmCalendarView(),
                new CalendarModel());
        setStyleName(STYLE_DATE_PICKER);

    }

    /**
     * Refresh all components
     */
    public void refresh() {
        super.refreshAll();
        ((GpmCalendarView) getView()).clearSelected();

    }

    public GpmCalendarView getGpmCalendarView() {
        return ((GpmCalendarView) getView());
    }
}

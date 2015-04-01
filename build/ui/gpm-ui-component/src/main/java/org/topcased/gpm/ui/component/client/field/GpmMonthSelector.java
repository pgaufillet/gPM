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

import java.util.Date;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.MonthSelector;

/**
 * GpmMonthSelector A simple {@link MonthSelector} used for the default date
 * picker.
 * 
 * @see com.google.gwt.user.datepicker.client.DefaultMonthSelector
 * @author jeballar
 */
public class GpmMonthSelector extends MonthSelector {

    private static final String STYLE_PREVIOUS_BUTTON =
            ComponentResources.INSTANCE.css().gpmMonthSelectorPreviousButton();

    private static final String STYLE_NEXT_BUTTON =
            ComponentResources.INSTANCE.css().gpmMonthSelectorNextButton();

    private static final String STYLE_MONTH =
            ComponentResources.INSTANCE.css().gpmMonthSelectorMonth();

    private static final String STYLE_MONTH_SELECTOR =
            ComponentResources.INSTANCE.css().gpmMonthSelector();

    private PushButton backwards;

    private PushButton forwards;

    private Grid grid;

    /**
     * Constructor.
     */
    public GpmMonthSelector() {
    }

    @Override
    protected void refresh() {
        String lFormattedMonth = getModel().formatCurrentMonth();
        grid.setText(0, 1, lFormattedMonth);
    }

    @Override
    protected void setup() {
        // Set up backwards.
        backwards = new PushButton();
        backwards.addClickHandler(new ClickHandler() {
            @SuppressWarnings("deprecation")
            public void onClick(ClickEvent pEvent) {
                GpmDatePicker lGpmDatePicker =
                        (GpmDatePicker) GpmDatePickerPopup.getInstance().getDatePicker();

                getModel().shiftCurrentMonth(-1);
                lGpmDatePicker.refresh();
                refresh();

                // HighLight selected value
                Date lCurrentDate = lGpmDatePicker.getValue();
                if (lCurrentDate.getMonth() == getModel().getCurrentMonth().getMonth()) {
                    lGpmDatePicker.getGpmCalendarView().getGrid().setSelected(
                            lGpmDatePicker.getGpmCalendarView().getCell(
                                    lCurrentDate));
                }
            }
        });

        backwards.getUpFace().setHTML("&laquo;");
        backwards.setStyleName(STYLE_PREVIOUS_BUTTON);

        forwards = new PushButton();
        forwards.getUpFace().setHTML("&raquo;");
        forwards.setStyleName(STYLE_NEXT_BUTTON);
        forwards.addClickHandler(new ClickHandler() {
            @SuppressWarnings("deprecation")
            public void onClick(ClickEvent pEvent) {
                GpmDatePicker lGpmDatePicker =
                        (GpmDatePicker) GpmDatePickerPopup.getInstance().getDatePicker();

                getModel().shiftCurrentMonth(1);
                lGpmDatePicker.refresh();
                refresh();

                // HighLight selected value
                Date lCurrentDate = lGpmDatePicker.getValue();
                if (lCurrentDate.getMonth() == getModel().getCurrentMonth().getMonth()) {
                    lGpmDatePicker.getGpmCalendarView().getGrid().setSelected(
                            lGpmDatePicker.getGpmCalendarView().getCell(
                                    lCurrentDate));
                }
            }
        });

        // Set up grid.
        grid = new Grid(1, 3);
        grid.setWidget(0, 0, backwards);
        grid.setWidget(0, 2, forwards);

        CellFormatter lFormatter = grid.getCellFormatter();
        lFormatter.setStyleName(0, 1, STYLE_MONTH);
        lFormatter.setWidth(0, 0, "1");
        lFormatter.setWidth(0, 1, "100%");
        lFormatter.setWidth(0, 2, "1");
        grid.setStyleName(STYLE_MONTH_SELECTOR);
        initWidget(grid);
    }
}

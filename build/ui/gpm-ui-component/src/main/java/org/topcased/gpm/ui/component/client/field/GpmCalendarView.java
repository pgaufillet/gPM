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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.impl.ElementMapperImpl;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.CalendarView;

/**
 * Simple calendar view taken from GWT DefaultCalendarView
 * 
 * @see com.google.gwt.user.datepicker.client.DefaultCalendarView
 * @author jeballar
 */
public class GpmCalendarView extends CalendarView {

    public static final String STYLE_DAY =
            ComponentResources.INSTANCE.css().gpmCalendarViewDay();

    public static final String STYLE_DAY_WEEKEND =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsWeekend();

    public static final String STYLE_DAY_FILLER =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsFiller();

    public static final String STYLE_DAY_HIGHLIGHT =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsHighlighted();

    public static final String STYLE_DAY_DISABLED =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsDisabled();

    public static final String STYLE_DAY_VALUED_AND_HIGHLIGTHED =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsValueAndHighlighted();

    public static final String STYLE_DAY_SELECTED =
            ComponentResources.INSTANCE.css().gpmCalendarViewDayIsSelected();

    private static final String STYLE_DAYS =
            ComponentResources.INSTANCE.css().gpmCalendarViewDays();

    private static final int DAYS_IN_WEEK = 7;

    /**
     * Cell grid.
     */
    // Javac bug requires that date be fully specified here.
    protected class CellGrid extends Grid {
        /**
         * A cell representing a date.
         */
        protected class DateCell extends Cell {
            DateCell(Element pTd, boolean pIsWeekend) {
                super(pTd, new Date());
                setStyleName(STYLE_DAY);
                if (pIsWeekend) {
                    addStyleName(STYLE_DAY_WEEKEND);
                }
            }

            public boolean isFiller() {
                return !getModel().isInCurrentMonth(getValue());
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.topcased.gpm.ui.component.client.field.GpmCalendarView.CellGrid.Cell#onHighlighted(boolean)
             */
            @Override
            public void onHighlighted(boolean pHighlighted) {
                setHighlightedDate(getValue());
                updateStyle();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.topcased.gpm.ui.component.client.field.GpmCalendarView.CellGrid.Cell#onSelected(boolean)
             */
            @Override
            public void onSelected(boolean pSelected) {
                if (pSelected) {
                    GpmDatePickerPopup.getInstance().getDatePicker().setValue(
                            getValue(), true);
                    if (isFiller()) {
                        GpmDatePickerPopup.getInstance().getDatePicker().setCurrentMonth(
                                getValue());
                    }
                }
                updateStyle();
            }

            /**
             * Set the current date
             * 
             * @param pCurrent
             *            the current date
             */
            public void update(Date pCurrent) {
                setEnabled(true);
                getValue().setTime(pCurrent.getTime());
                String lValue = getModel().formatDayOfMonth(getValue());
                setText(lValue);
                if (isFiller()) {
                    addStyleName(STYLE_DAY_FILLER);
                }
                else {
                    removeStyleName(STYLE_DAY_FILLER);
                }
                updateStyle();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.topcased.gpm.ui.component.client.field.GpmCalendarView.CellGrid.Cell#updateStyle()
             */
            @Override
            public void updateStyle() {
                setStyleName(STYLE_DAY);
                if (isHighlighted()) {
                    addStyleName(STYLE_DAY_HIGHLIGHT);

                    if (isHighlighted() && isSelected()) {
                        addStyleName(STYLE_DAY_VALUED_AND_HIGHLIGTHED);
                    }
                    else {
                        removeStyleName(STYLE_DAY_VALUED_AND_HIGHLIGTHED);
                    }
                }
                else {
                    removeStyleName(STYLE_DAY_HIGHLIGHT);
                }
                if (isSelected()) {
                    addStyleName(STYLE_DAY_SELECTED);
                }
                else {
                    removeStyleName(STYLE_DAY_SELECTED);
                }
                if (isEnabled()) {
                    removeStyleName(STYLE_DAY_DISABLED);
                }
                else {
                    addStyleName(STYLE_DAY_DISABLED);
                }
            }

            private void setText(String pValue) {
                DOM.setInnerText(getElement(), pValue);
            }
        }

        /**
         * Constructor
         */
        public CellGrid() {
            super();
            setCellPadding(0);
            setCellSpacing(0);
            setBorderWidth(0);
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
            resize(CalendarModel.WEEKS_IN_MONTH + 1, CalendarModel.DAYS_IN_WEEK);
        }

        /**
         * Action when selected
         * 
         * @param pLastSelected
         *            previously selected
         * @param pCell
         *            currently selected
         */
        protected void onSelected(Cell pLastSelected, Cell pCell) {
        }

        /* UP NON ABSTRACT , DOWN ABSTRACT PASTED*/

        /**
         * Cell type.
         */
        public abstract class Cell extends UIObject {
            private boolean enabled = true;

            private Date value;

            private int index;

            /**
             * Create a cell grid.
             * 
             * @param pElem
             *            the wrapped element
             * @param pValue
             *            the value
             */
            public Cell(Element pElem, Date pValue) {
                this.value = pValue;
                Cell lCurrent = this;
                index = cellList.size();
                cellList.add(lCurrent);

                setElement(pElem);
                elementToCell.put(lCurrent);
            }

            public Date getValue() {
                return value;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public boolean isHighlighted() {
                return this == highlightedCell;
            }

            public boolean isSelected() {
                return selectedCell == this;
            }

            /**
             * Enable disable
             * 
             * @param pEnabled
             *            enable or disable
             */
            public final void setEnabled(boolean pEnabled) {
                enabled = pEnabled;
                onEnabled(pEnabled);
            }

            /**
             * Keyboard navigation method
             * 
             * @param pKeyCode
             *            the key code
             */
            public void verticalNavigation(int pKeyCode) {
                switch (pKeyCode) {
                    case KeyCodes.KEY_UP:
                        setHighlighted(previousItem());
                        break;
                    case KeyCodes.KEY_DOWN:
                        setHighlighted(nextItem());
                        break;
                    case KeyCodes.KEY_ESCAPE:
                        // Figure out new event for this.
                        break;
                    case KeyCodes.KEY_ENTER:
                        setSelected(this);
                        break;
                    default:
                        // DO NOTHING

                }
            }

            /**
             * Get next item
             * 
             * @return the next item
             */
            protected Cell nextItem() {
                if (index == getLastIndex()) {
                    return cellList.get(0);
                }
                else {
                    return cellList.get(index + 1);
                }
            }

            /**
             * Action when set enabled/disabled
             * 
             * @param pEnabled
             *            enabled or disabled
             */
            protected void onEnabled(boolean pEnabled) {
                updateStyle();
            }

            /**
             * Action when set highlighted or un-highlighted
             * 
             * @param pHighlighted
             *            highlighted un-highlighted
             */
            protected void onHighlighted(boolean pHighlighted) {
                updateStyle();
            }

            /**
             * Action when selected or unselected
             * 
             * @param pSelected
             *            selected or not
             */
            protected void onSelected(boolean pSelected) {
                updateStyle();
            }

            /**
             * Get previous item
             * 
             * @return The previous item
             */
            protected Cell previousItem() {
                if (index != 0) {
                    return cellList.get(index - 1);
                }
                else {
                    return cellList.get(getLastIndex());
                }
            }

            /**
             * Updatestyle
             */
            protected abstract void updateStyle();

            private int getLastIndex() {
                return cellList.size() - 1;
            }
        }

        private Cell highlightedCell;

        private Cell selectedCell;

        private ElementMapperImpl<Cell> elementToCell =
                new ElementMapperImpl<Cell>();

        private ArrayList<Cell> cellList = new ArrayList<Cell>();

        /**
         * Get Cell associated with Element
         * 
         * @param pElement
         *            the element to transform
         * @return the associated Cell
         */
        public Cell getCell(Element pElement) {
            // This cast is always valid because both Element types are JSOs and have
            // no new fields are added in the subclass.
            return elementToCell.get((com.google.gwt.user.client.Element) pElement);
        }

        /**
         * The Cell associated to the Event
         * 
         * @param pEvent
         *            the Event to get the Cell from
         * @return the associated Cell
         */
        public Cell getCell(Event pEvent) {
            // Find out which cell was actually clicked.
            Element lTd = getEventTargetCell(pEvent);
            if (lTd != null) {
                return elementToCell.get((com.google.gwt.user.client.Element) lTd);
            }
            return null;
        }

        /**
         * Get Cell at specified index
         * 
         * @param pI
         *            index
         * @return the corresponding cell
         */
        public Cell getCell(int pI) {
            return cellList.get(pI);
        }

        public Iterator<Cell> getCells() {
            return cellList.iterator();
        }

        public Cell getHighlightedCell() {
            return highlightedCell;
        }

        public int getNumCells() {
            return cellList.size();
        }

        public Cell getSelectedCell() {
            return selectedCell;
        }

        public Date getSelectedValue() {
            return getValue(selectedCell);
        }

        /**
         * Get the date of specified Cell
         * 
         * @param pCell
         *            the cell to get the value from
         * @return the corresponding date
         */
        public Date getValue(Cell pCell) {
            if (pCell == null) {
                return null;
            }
            return pCell.getValue();
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
         */
        @Override
        public void onBrowserEvent(Event pEvent) {
            switch (DOM.eventGetType(pEvent)) {
                case Event.ONCLICK: {
                    Cell lCell = getCell(pEvent);
                    if (isActive(lCell)) {
                        setSelected(lCell);
                    }
                    break;
                }
                case Event.ONMOUSEOUT: {
                    Element lE = DOM.eventGetFromElement(pEvent);
                    if (lE != null) {
                        Cell lCell =
                                elementToCell.get((com.google.gwt.user.client.Element) lE);
                        if (lCell == highlightedCell) {
                            setHighlighted(null);
                        }
                    }
                    break;
                }
                case Event.ONMOUSEOVER: {
                    Element lE = DOM.eventGetToElement(pEvent);
                    if (lE != null) {
                        Cell lCell =
                                elementToCell.get((com.google.gwt.user.client.Element) lE);
                        if (isActive(lCell)) {
                            setHighlighted(lCell);
                        }
                    }
                    break;
                }
                default:
                    // Do nothing
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.Panel#onUnload()
         */
        @Override
        public void onUnload() {
            setHighlighted(null);
        }

        /**
         * Set the new highlighted Cell
         * 
         * @param pNextHighlighted
         *            the cell to highlight
         */
        public final void setHighlighted(Cell pNextHighlighted) {
            if (pNextHighlighted == highlightedCell) {
                return;
            }
            Cell lOldHighlighted = highlightedCell;
            highlightedCell = pNextHighlighted;
            if (lOldHighlighted != null) {
                lOldHighlighted.onHighlighted(false);
            }
            if (highlightedCell != null) {
                highlightedCell.onHighlighted(true);
            }
        }

        /**
         * Set the Cell selected
         * 
         * @param pCell
         *            the selected cell
         */
        public final void setSelected(Cell pCell) {
            Cell lLast = getSelectedCell();
            selectedCell = pCell;

            if (lLast != null) {
                lLast.onSelected(false);
            }
            if (selectedCell != null) {
                selectedCell.onSelected(true);
            }
            onSelected(lLast, selectedCell);
        }

        /**
         * Action when key pressed
         * 
         * @param pLastHighlighted
         *            last highlighted Cell
         * @param pEvent
         *            the Key event
         */
        protected void onKeyDown(Cell pLastHighlighted, KeyDownEvent pEvent) {
            if (pEvent.isAnyModifierKeyDown()) {
                return;
            }
            int lKeyCode = pEvent.getNativeKeyCode();
            if (pLastHighlighted == null) {
                if (lKeyCode == KeyCodes.KEY_DOWN && cellList.size() > 0) {
                    setHighlighted(cellList.get(0));
                }
            }
            else {
                pLastHighlighted.verticalNavigation(lKeyCode);
            }
        }

        private boolean isActive(Cell pCell) {
            return pCell != null && pCell.isEnabled();
        }

    }

    private CellGrid grid = new CellGrid();

    private Date firstDisplayed;

    private Date lastDisplayed = new Date();

    /**
     * Constructor.
     */
    public GpmCalendarView() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.CalendarView#addStyleToDate(java.lang.String,
     *      java.util.Date)
     */
    @Override
    public void addStyleToDate(String pStyleName, Date pDate) {
        getCell(pDate).addStyleName(pStyleName);
        refresh();
    }

    @Override
    public Date getFirstDate() {
        return firstDisplayed;
    }

    @Override
    public Date getLastDate() {
        return lastDisplayed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.CalendarView#isDateEnabled(java.util.Date)
     */
    @Override
    public boolean isDateEnabled(Date pDate) {
        return getCell(pDate).isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.DatePickerComponent#refresh()
     */
    @SuppressWarnings("deprecation")
    @Override
    public void refresh() {
        firstDisplayed = getModel().getCurrentFirstDayOfFirstWeek();

        if (firstDisplayed.getDate() == 1) {
            // show one empty week if date is Monday is the first in month.
            CalendarUtil.addDaysToDate(firstDisplayed, -DAYS_IN_WEEK);
        }

        lastDisplayed.setTime(firstDisplayed.getTime());

        for (int i = 0; i < grid.getNumCells(); i++) {
            if (i != 0) {
                CalendarUtil.addDaysToDate(lastDisplayed, 1);
            }
            CellGrid.DateCell lCell = (CellGrid.DateCell) grid.getCell(i);
            lCell.update(lastDisplayed);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.CalendarView#removeStyleFromDate(java.lang.String,
     *      java.util.Date)
     */
    @Override
    public void removeStyleFromDate(String pStyleName, Date pDate) {
        getCell(pDate).removeStyleName(pStyleName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.CalendarView#setEnabledOnDate(boolean,
     *      java.util.Date)
     */
    @Override
    public void setEnabledOnDate(boolean pEnabled, Date pDate) {
        getCell(pDate).setEnabled(pEnabled);
    }

    /**
     * Is a day in the week a weekend?
     * 
     * @param pDayOfWeek
     *            day of week
     * @return is the day of week a weekend?
     */
    public static final boolean isWeekend(int pDayOfWeek) {
        final DateTimeConstants lIntlConstants =
                LocaleInfo.getCurrentLocale().getDateTimeConstants();
        final int lFirstDayOfWeekend =
                Integer.parseInt(lIntlConstants.weekendRange()[0]) - 1;
        final int lLastDayOfWeekend =
                Integer.parseInt(lIntlConstants.weekendRange()[1]) - 1;
        return pDayOfWeek == lFirstDayOfWeekend
                || pDayOfWeek == lLastDayOfWeekend;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.datepicker.client.DatePickerComponent#setup()
     */
    @Override
    public void setup() {
        // Preparation
        CellFormatter lFormatter = grid.getCellFormatter();
        int lWeekendStartColumn = -1;
        int lWeekendEndColumn = -1;

        // Set up the day labels.
        for (int i = 0; i < CalendarModel.DAYS_IN_WEEK; i++) {
            int lShift = CalendarUtil.getStartingDayOfWeek();
            int lDayIdx = i + lShift;
            if (i + lShift >= CalendarModel.DAYS_IN_WEEK) {
                lDayIdx -= CalendarModel.DAYS_IN_WEEK;
            }
            grid.setText(0, i, getModel().formatDayOfWeek(lDayIdx));
            lFormatter.setStyleName(0, i, STYLE_DAYS);

            if (isWeekend(lDayIdx)) {
                lFormatter.addStyleName(0, i, STYLE_DAY_WEEKEND);
                if (lWeekendStartColumn == -1) {
                    lWeekendStartColumn = i;
                }
                else {
                    lWeekendEndColumn = i;
                }
            }
        }

        // Set up the calendar grid.
        for (int lRow = 1; lRow <= CalendarModel.WEEKS_IN_MONTH; lRow++) {
            for (int lColumn = 0; lColumn < CalendarModel.DAYS_IN_WEEK; lColumn++) {
                // set up formatter.
                Element lElem = lFormatter.getElement(lRow, lColumn);
                grid.new DateCell(lElem, lColumn == lWeekendStartColumn
                        || lColumn == lWeekendEndColumn);
            }
        }
        initWidget(grid);
    }

    /**
     * Get the cell for the date in parameter
     * 
     * @param pDate
     *            the date to get the representing cell
     * @return the DateCell representing the date
     */
    @SuppressWarnings("deprecation")
    public CellGrid.DateCell getCell(Date pDate) {
        int lIndex = CalendarUtil.getDaysBetween(firstDisplayed, pDate);
        assert (lIndex >= 0);

        CellGrid.DateCell lCell = (CellGrid.DateCell) grid.getCell(lIndex);
        if (lCell.getValue().getDate() != pDate.getDate()) {
            throw new IllegalStateException(pDate
                    + " cannot be associated with cell " + lCell
                    + " as it has date " + lCell.getValue());
        }
        return lCell;
    }

    /**
     * Clear the selected cell
     */
    public void clearSelected() {
        grid.setSelected(null);

    }

    public CellGrid getGrid() {
        return grid;
    }

}

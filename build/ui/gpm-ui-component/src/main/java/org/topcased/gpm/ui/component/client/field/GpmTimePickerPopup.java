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

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Pop up including a time picker.
 * 
 * @author frosier
 */
public final class GpmTimePickerPopup extends PopupPanel {
    private static GpmTimePickerPopup staticInstance;

    private static final int TIMEPICKER_ELEM_CENTER_DELTA = 9;

    private static final String TIME_SEPARATOR = ":";

    private static final String STYLE_GPM_POPUP =
            ComponentResources.INSTANCE.css().gpmDropDownListPopup();

    private static final String STYLE_POPUP_ELEMENT =
            ComponentResources.INSTANCE.css().gpmListPopupElement();

    private static final String STYLE_TIME_POPUP =
            ComponentResources.INSTANCE.css().gpmTimePickerPopup();

    private static final String STYLE_TIME_POPUP_ELEMENT =
            ComponentResources.INSTANCE.css().gpmTimePickerPopupElement();

    private static final String STYLE_TIME_POPUP_ELEMENT_LARGE =
            ComponentResources.INSTANCE.css().gpmTimePickerPopupElementLarge();

    private static final String STYLE_TIME_POPUP_ELEMENT_TINY =
            ComponentResources.INSTANCE.css().gpmTimePickerPopupElementTiny();

    private static final String STYLE_TIME_POPUP_ELEMENT_HOVER =
            ComponentResources.INSTANCE.css().gpmTimePickerPopupElementHover();

    private static final String STYLE_TIME_POPUP_ELEMENT_ACTIVE =
            ComponentResources.INSTANCE.css().gpmTimePickerPopupElementActive();

    private GpmTimePickerWidget currentTimeBox;

    private final FlowPanel listContainer;

    /**
     * Get the common time picker pop up.
     * 
     * @return The common date picker pop up.
     */
    public final static GpmTimePickerPopup getInstance() {
        if (staticInstance == null) {
            staticInstance = new GpmTimePickerPopup();
        }
        return staticInstance;
    }

    /**
     * Inner constructor.
     */
    private GpmTimePickerPopup() {
        super(true);

        listContainer = new FlowPanel();
        setWidget(listContainer);

        initRange();

        setStylePrimaryName(STYLE_GPM_POPUP);
        addStyleName(STYLE_TIME_POPUP);
        setStylePrimaryName(getContainerElement(),
                ComponentResources.INSTANCE.css().gpmPopupContent());
    }

    /**
     * Set the list popup with time elements.
     */
    private void initRange() {
        final int lMaxHours = 24;
        final int lMaxMinutes = 60;
        final int lMinutesIncrement = 15;

        for (int lHour = 0; lHour < lMaxHours; lHour++) {
            for (int lMinutes = 0; lMinutes < lMaxMinutes; lMinutes =
                    lMinutes + lMinutesIncrement) {
                listContainer.add(new TimePickerPopupElement(lHour, lMinutes));
            }
        }
    }

    /**
     * Show the popup relative to the UI object in parameter. <br />
     * Add all hide partners initialized to the popup.
     * 
     * @param pTimePicker
     *            The objet to show relative to.
     */
    public void showRelativeTo(final GpmTimePickerWidget pTimePicker) {
        if (currentTimeBox != null) {
            removeAutoHidePartner(currentTimeBox.getElement());
        }
        currentTimeBox = pTimePicker;

        addAutoHidePartner(currentTimeBox.getElement());
        initPopupSize();

        super.showRelativeTo(currentTimeBox);

        // Center and select the closer hours element or the exact one if it exists. 
        setAllNotSelected();
        final TimePickerPopupElement lToSelectAndCenterElement =
                getElementByTextValue(currentTimeBox.getValue());
        if (lToSelectAndCenterElement != null) {
            //            FIXME: Check if origin of JS pb on Ie7 !
            scrollCenter(lToSelectAndCenterElement,
                    TIMEPICKER_ELEM_CENTER_DELTA);
            lToSelectAndCenterElement.setSelected(true);
        }
    }

    /**
     * Scroll the list popup to center the element.
     * 
     * @param pElem
     *            The time picker element to center vertically.
     * @param pElemDelta
     *            The delta of the bottom element which has to scroll into view
     *            in order to center the other one.
     */
    private void scrollCenter(final TimePickerPopupElement pElem,
            final int pElemDelta) {
        // Scroll in view on "delta" element bottom.
        int lIndex = listContainer.getWidgetIndex(pElem) + pElemDelta;
        if (lIndex >= listContainer.getWidgetCount()) {
            lIndex = listContainer.getWidgetCount() - 1;
        }

        listContainer.getWidget(lIndex).getElement().scrollIntoView();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#hide()
     */
    @Override
    public void hide() {
        removeAutoHidePartner(currentTimeBox.getElement());
        super.hide();
    }

    /**
     * Set all popup element as not selected.
     */
    public void setAllNotSelected() {
        for (int i = 0; i < listContainer.getWidgetCount(); i++) {
            final TimePickerPopupElement lElem =
                    (TimePickerPopupElement) listContainer.getWidget(i);
            lElem.setSelected(false);
        }
    }

    private void initPopupSize() {
        setWidth(currentTimeBox.getOffsetWidth() + Unit.PX.name());
    }

    private TimePickerPopupElement getElementByTextValue(final String pValue) {
        // Exit conditions : null or empty string.
        if (pValue == null || (pValue != null && "".equals(pValue.trim()))) {
            return null;
        }

        try {
            TimePickerPopupElement lElement = null;

            final String[] lSplitedValue = pValue.split(TIME_SEPARATOR);

            final int lHours = Integer.parseInt(lSplitedValue[0]);
            int lMinutes = 0;
            if (lSplitedValue.length > 1) {
                lMinutes = Integer.parseInt(lSplitedValue[1]);
            }

            int i = 0;
            while (i < listContainer.getWidgetCount() && lElement == null) {
                final TimePickerPopupElement lIterElem =
                        (TimePickerPopupElement) listContainer.getWidget(i);

                if (lIterElem.getHours() == lHours
                        && lIterElem.getMinutes() == lMinutes) {
                    lElement = lIterElem;
                }
                i++;
            }

            return lElement;
        }
        catch (NumberFormatException lNumberException) {
            //if the text is not well formated no value can be selected in the time picker
            return null;
        }
    }

    // ================ TimePicker Popup Element ===================== //

    private class TimePickerPopupElement extends HTML {
        private final int hours;

        private final int minutes;

        private boolean selected = false;

        private static final int TWO_DIGITS_LIMIT = 10;

        private static final String DIGIT_PREFIX = "0";

        /**
         * Creates a time picker popup element.
         * 
         * @param pHour
         *            The hours.
         * @param pMinutes
         *            The minutes.
         */
        public TimePickerPopupElement(final int pHour, final int pMinutes) {
            super();
            hours = pHour;
            minutes = pMinutes;

            setHTML(toString());

            setStylePrimaryName(STYLE_POPUP_ELEMENT);
            addStyleName(STYLE_TIME_POPUP_ELEMENT);

            if (minutes == 0) {
                addStyleName(STYLE_TIME_POPUP_ELEMENT_LARGE);
            }
            else {
                addStyleName(STYLE_TIME_POPUP_ELEMENT_TINY);
            }

            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.UIObject#toString()
         */
        @Override
        public String toString() {
            String lHours;
            String lMinutes;

            // insert "0" before 1 digit numbers.
            if (hours < TWO_DIGITS_LIMIT) {
                lHours = DIGIT_PREFIX + hours;
            }
            else {
                lHours = "" + hours;
            }
            if (minutes < TWO_DIGITS_LIMIT) {
                lMinutes = DIGIT_PREFIX + minutes;
            }
            else {
                lMinutes = "" + minutes;
            }

            return lHours + TIME_SEPARATOR + lMinutes;
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
         */
        @Override
        public void onBrowserEvent(Event pEvent) {
            super.onBrowserEvent(pEvent);

            switch (DOM.eventGetType(pEvent)) {
                case Event.ONCLICK:
                case Event.ONCHANGE:
                    currentTimeBox.setValue(getText(), true);
                    //Fire manually a changeEvent in order to enable the validation of the timebox
                    DomEvent.fireNativeEvent(
                            Document.get().createChangeEvent(), currentTimeBox);

                    // set all time picker elements not selected.
                    setAllNotSelected();
                    setSelected(true);
                    hide();
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOVER:
                    addStyleName(STYLE_TIME_POPUP_ELEMENT_HOVER);
                    break;
                case Event.ONMOUSEOUT:
                    removeStyleName(STYLE_TIME_POPUP_ELEMENT_HOVER);
                    break;
                default:
                    break;
            }
        }

        /**
         * Set the element as selected and affect correct style decoration.
         * 
         * @param pSelected
         *            The selected state.
         */
        public void setSelected(boolean pSelected) {
            selected = pSelected;
            if (selected) {
                addStyleName(STYLE_TIME_POPUP_ELEMENT_ACTIVE);
            }
            else {
                removeStyleName(STYLE_TIME_POPUP_ELEMENT_ACTIVE);
                removeStyleName(STYLE_TIME_POPUP_ELEMENT_HOVER);
            }
        }

        /**
         * Get the hours.
         * 
         * @return The hours.
         */
        public int getHours() {
            return hours;
        }

        /**
         * Get the minutes.
         * 
         * @return The minutes.
         */
        public int getMinutes() {
            return minutes;
        }
    }

}
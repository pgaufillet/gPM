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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * GpmTimePickerWidget is a text box to type times.
 * <p>
 * On click events, a popup is shown to help the typing of a time. The time
 * format is fix : "HH:MM".
 * </p>
 * 
 * @author frosier
 */
public class GpmTimePickerWidget extends TextBox {

    private static final String STYLE_TIME_PICKER =
            ComponentResources.INSTANCE.css().gpmTimePicker();

    /**
     * Creates an empty Date time picker.
     * <p>
     * On the first click event, the static instance of GpmTimePickerPopup is
     * opened and attached to this text box.
     * </p>
     */
    public GpmTimePickerWidget() {
        super();

        setStylePrimaryName(STYLE_TIME_PICKER);
        addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        sinkEvents(Event.ONCLICK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.TextBoxBase#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(Event pEvent) {
        super.onBrowserEvent(pEvent);

        switch (DOM.eventGetType(pEvent)) {
            case Event.ONCLICK:
                GpmTimePickerPopup.getInstance().showRelativeTo(this);
                break;
            default:
                break;
        }
    }
}

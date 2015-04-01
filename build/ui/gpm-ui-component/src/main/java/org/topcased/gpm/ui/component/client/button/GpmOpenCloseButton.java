/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.button;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;

/**
 * A button with two position : open and close. The button is not displayed
 * until the setIsOpen method is called.
 * 
 * @author tpanuel
 */
public class GpmOpenCloseButton extends HTML {
    private static final String SHOW_BUTTON_STRING =
            AbstractImagePrototype.create(INSTANCE.images().show()).getHTML();

    private static final String HIDE_BUTTON_STRING =
            AbstractImagePrototype.create(INSTANCE.images().hide()).getHTML();

    private boolean isOpen;

    private String buttonText;

    private GpmBasicActionHandler<Object> open;

    private GpmBasicActionHandler<Object> close;

    private Object valueEvent;

    /**
     * <p>
     * Bug when click (Event.ONCLICK) on image (arrow) of the title.
     * </p>
     * <p>
     * So new implementation of click has been implemented with
     * Event.ONMOUSEDOWN and Event.ONMOUSEUP :
     * </p>
     * <ul>
     * <li>Event.ONMOUSEDOWN : boolean "isClick" to true.</li>
     * <li>Event.ONMOUSEUP : open/close group if boolean "isClick" is true.</li>
     * <li>Event.ONMOUSEOUT : boolean "isClick" reset to false.</li>
     * </ul>
     * 
     * @see gPM-BUG-291.
     */
    private boolean isClick;

    /**
     * Create an open/close button.
     */
    public GpmOpenCloseButton() {
        super();
        //By default the widget is closed and the mouse is considered as not over it
        isOpen = false;
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmGroupName());
        sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOVER);

        valueEvent = null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(final Event pEvent) {
        super.onBrowserEvent(pEvent);

        switch (DOM.eventGetType(pEvent)) {
            //Detection of the click a ONMOUSEDOWN event followed by a ONMOUSEUP
            case Event.ONMOUSEDOWN:
                isClick = true;
                break;
            case Event.ONMOUSEUP:
                if (isClick) {
                    if (isOpen) {
                        //If the widget was open close it
                        close.execute(valueEvent);
                    }
                    else {
                        //else open this widget
                        open.execute(valueEvent);
                    }
                }
                //reset the click tag
                isClick = false;
                break;
            default:
                // Do nothing !
        }
    }

    /**
     * Set the button text.
     * 
     * @param pButtonText
     *            The button text.
     */
    public void setButtonText(final String pButtonText) {
        buttonText = pButtonText;
    }

    /**
     * Set action call when the button is on close position.
     * 
     * @param pOpenHandler
     *            The new handler.
     */
    public void setOpenHandler(final GpmBasicActionHandler<Object> pOpenHandler) {
        open = pOpenHandler;
    }

    /**
     * Set action call when the button is on open position.
     * 
     * @param pCloseHandler
     *            The new handler.
     */
    public void setCloseHandler(
            final GpmBasicActionHandler<Object> pCloseHandler) {
        close = pCloseHandler;
    }

    /**
     * Set if the button is on open position. The button is not displayed until
     * the setIsOpen method is called.
     * 
     * @param pIsOpen
     *            If the button is on open position.
     */
    public void setIsOpen(final boolean pIsOpen) {
        isOpen = pIsOpen;
        refresh();
    }

    private void refresh() {
        final String lImage;
        // A different image if the button is open/close
        if (isOpen) {
            lImage = HIDE_BUTTON_STRING;
        }
        else {
            lImage = SHOW_BUTTON_STRING;
        }
        //The <div> is manually added in order to group the image and the text
        //If not, the event handler is not properly working with the image part
        setHTML("<div>" + lImage + buttonText + "</div>");
    }

    public void setValueEvent(Object pValueEvent) {
        valueEvent = pValueEvent;
    }
}
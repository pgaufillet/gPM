/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.button;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;

/**
 * GpmImageToggleButton
 * 
 * @author jeballar
 */
public class GpmImageToggleButton extends Image {

    private static final String STYLE_DOWN =
            ComponentResources.INSTANCE.css().gpmImageToggleButtonDown();

    private static final String STYLE_DOWN_HOVER =
            ComponentResources.INSTANCE.css().gpmImageToggleButtonDownHover();

    private static final String STYLE_UP =
            ComponentResources.INSTANCE.css().gpmImageToggleButtonUp();

    private static final String STYLE_UP_HOVER =
            ComponentResources.INSTANCE.css().gpmImageToggleButtonUpHover();

    private ClickHandler handler;

    private final String id;

    private boolean enabled = true;

    private boolean down = false;

    /**
     * Creates an image button with "stay push-stay pull" behavior and tooltip
     * generated from "pTitle" parameter.
     * 
     * @param pImageResource
     *            The image resource.
     */
    public GpmImageToggleButton(final ImageResource pImageResource) {
        this(null, pImageResource);
    }

    /**
     * Set the button up or down
     * 
     * @param pDown
     *            <code>true</code> to set the button down, <code>false</code>
     *            to set it up
     */
    public void setDown(boolean pDown) {
        if (pDown) {
            removeStyleName(STYLE_UP);
            addStyleName(STYLE_DOWN);
        }
        else if (!pDown) {
            removeStyleName(STYLE_DOWN);
            addStyleName(STYLE_UP);
        }
        down = pDown;
    }

    /**
     * Creates an image button with "stay push-stay pull" behavior and tooltip
     * generated from "pTitle" parameter.
     * 
     * @param pId
     *            The button id.
     * @param pImageResource
     *            The image resource.
     */
    public GpmImageToggleButton(final String pId,
            final ImageResource pImageResource) {
        super(pImageResource);
        id = pId;
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmImageToggleButton());
        // Filtered events
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
                | Event.ONMOUSEDOWN | Event.ONMOUSEUP);
    }

    /**
     * Override to add the "push" and "pull" behavior on buttons in the menu
     * bar.<br />
     * 
     * @param pEvent
     *            The event caught when browsing on the bar.
     */
    @Override
    public void onBrowserEvent(final Event pEvent) {
        super.onBrowserEvent(pEvent);

        switch (DOM.eventGetType(pEvent)) {
            case Event.ONCLICK:
                removeStyleName(STYLE_UP_HOVER);
                removeStyleName(STYLE_DOWN_HOVER);
                if (down) {
                    setDown(false);
                }
                else {
                    setDown(true);
                }
                if (handler != null && enabled) {
                    handler.onClick(new GpmClickEvent(this));
                }
                break;
            case Event.ONMOUSEDOWN:
                break;
            case Event.ONMOUSEUP:
            case Event.ONMOUSEOVER:
                if (enabled) {
                    if (down) {
                        addStyleName(STYLE_DOWN_HOVER);
                    }
                    else {
                        addStyleName(STYLE_UP_HOVER);
                    }
                }
                break;
            case Event.ONMOUSEOUT:
                removeStyleName(STYLE_UP_HOVER);
                removeStyleName(STYLE_DOWN_HOVER);
                break;
            default:
                // Do nothing !
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Image#addClickHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public HandlerRegistration addClickHandler(final ClickHandler pHandler) {
        handler = pHandler;
        return null;
    }

    /**
     * Get the id.
     * 
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set button enabled or disabled
     * 
     * @param pEnabled
     *            enable or disable
     */
    public void setEnabled(boolean pEnabled) {
        enabled = pEnabled;
    }
}

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

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;

/**
 * A button with two images.
 * 
 * @author tpanuel
 */
public class GpmDoubleImageButton extends Image {
    private final ImageResource imageResource;

    private final ImageResource imageResourceHover;

    private ClickHandler handler;

    /**
     * Create a double image button.
     * 
     * @param pImageResource
     *            The standard image.
     * @param pImageResourceHover
     *            The hover image.
     */
    public GpmDoubleImageButton(final ImageResource pImageResource,
            final ImageResource pImageResourceHover) {
        super(pImageResource);
        imageResource = pImageResource;
        imageResourceHover = pImageResourceHover;
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmDoubleImageButton());
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
                | Event.ONMOUSEDOWN | Event.ONMOUSEUP);
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
            case Event.ONCLICK:
                setResource(imageResource);
                if (handler != null) {
                    handler.onClick(new GpmClickEvent(this));
                }
                break;
            case Event.ONMOUSEUP:
            case Event.ONMOUSEOVER:
                setResource(imageResourceHover);
                break;
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEOUT:
                setResource(imageResource);
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
}
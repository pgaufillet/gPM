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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * A button displayed has a simple text.
 * 
 * @author tpanuel
 */
public class GpmTextButton extends HTML {
    private ClickHandler handler;

    private String id;

    /**
     * Default constructor.
     */
    public GpmTextButton() {
        this(null, null);
    }

    /**
     * Create a GpmTextButton.
     * 
     * @param pButtonId
     *            The button id.
     * @param pButtonText
     *            The button text.
     */
    public GpmTextButton(final String pButtonId, final String pButtonText) {
        super(pButtonText);
        id = pButtonId;
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTextButton());
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
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
                if (handler != null) {
                    removeStyleName(ComponentResources.INSTANCE.css().gpmTextButtonHover());
                    handler.onClick(new GpmClickEvent(this));
                }
                break;
            case Event.ONMOUSEOVER:
                addStyleName(ComponentResources.INSTANCE.css().gpmTextButtonHover());
                break;
            case Event.ONMOUSEOUT:
                removeStyleName(ComponentResources.INSTANCE.css().gpmTextButtonHover());
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
     * Set the id.
     * 
     * @param pId
     *            The id.
     */
    public void setId(final String pId) {
        id = pId;
    }
}
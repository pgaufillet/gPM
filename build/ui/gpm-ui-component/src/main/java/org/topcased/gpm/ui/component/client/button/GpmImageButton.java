/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
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
 * Image with button "push & pull" behavior.
 * <p>
 * The different css definition as regards mouse event are managed by this
 * class.
 * </p>
 * Suffix dependencies must be defined to decorate the image on both following
 * cases :
 * <ul>
 * <li>active</li>
 * <li>hover</li>
 * </ul>
 * <h3>CSS style rules</h3>
 * <dl>
 * <dt>gpm-GpmImageButton</dt>
 * <dd>The image itself.</dd>
 * <dt>gpm-GpmImageButton-active</dt>
 * <dd>Style append on hover event.</dd>
 * <dt>gpm-GpmImageButton-hover</dt>
 * <dd>Style append on active event.</dd>
 * </dl>
 * 
 * @author frosier
 */
public class GpmImageButton extends Image {

    private static final String STYLE_IMAGE_BUTTON =
            ComponentResources.INSTANCE.css().gpmImageButton();

    private static final String STYLE_DISABLED =
            ComponentResources.INSTANCE.css().gpmImageButtonDisabled();

    private static final String STYLE_HOVER =
            ComponentResources.INSTANCE.css().gpmImageButtonHover();

    private static final String STYLE_ACTIVE =
            ComponentResources.INSTANCE.css().gpmImageButtonActive();

    private ClickHandler handler;

    private final String id;

    private boolean enabled = true;

    /**
     * Creates an image button with "push-pull" behavior and tooltip generated
     * from "pTitle" parameter.
     * 
     * @param pImageResource
     *            The image resource.
     */
    public GpmImageButton(final ImageResource pImageResource) {
        this(null, pImageResource);
    }

    /**
     * Creates an image button with "push-pull" behavior and tooltip generated
     * from "pTitle" parameter.
     * 
     * @param pId
     *            The button id.
     * @param pImageResource
     *            The image resource.
     */
    public GpmImageButton(final String pId, final ImageResource pImageResource) {
        super(pImageResource);
        id = pId;
        setStylePrimaryName(STYLE_IMAGE_BUTTON);
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
                removeStyleName(STYLE_ACTIVE);
                removeStyleName(STYLE_HOVER);
                if (handler != null && enabled) {
                    handler.onClick(new GpmClickEvent(this));
                }
                break;
            case Event.ONMOUSEDOWN:
                addStyleName(STYLE_ACTIVE);
                break;
            case Event.ONMOUSEUP:
            case Event.ONMOUSEOVER:
                if (enabled) {
                    addStyleName(STYLE_HOVER);
                }
                break;
            case Event.ONMOUSEOUT:
                removeStyleName(STYLE_ACTIVE);
                removeStyleName(STYLE_HOVER);
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
        if (enabled) {
            addStyleName(STYLE_DISABLED);
        }
        else {
            removeStyleName(STYLE_DISABLED);
        }
    }

    /**
     * get enabled
     * 
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
}
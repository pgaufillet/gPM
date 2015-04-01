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

import org.topcased.gpm.ui.component.client.popup.GpmToolTipPanel;
import org.topcased.gpm.ui.component.client.util.IToolTipCreationHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;

/**
 * GpmToolTipImageButton
 * 
 * @author jeballar
 */
public class GpmToolTipImageButton extends GpmImageButton implements
        ClickHandler {

    /** The associated tooltip */
    private GpmToolTipPanel tooltip = null;

    /** Indicates if the content must be generated each click */
    private boolean isDynamicTooltip = false;

    /** The associated creation handler */
    private IToolTipCreationHandler widgetCreationHandler = null;

    /**
     * Default Constructor with image in argument
     * 
     * @param pImageResource
     *            Image of the button
     */
    public GpmToolTipImageButton(ImageResource pImageResource) {
        super(pImageResource);
        addClickHandler(this);
    }

    /**
     * Sets the widgetCreationHandler. The handler will be called only once for
     * the first click on the button before displaying the tooltip popup. For
     * dynamic content, refreshed on each click, see
     * setDynamicWidgetCreationHandler.
     * 
     * @param pWidgetCreationHandler
     *            the widgetCreationHandler to set
     */
    public void setWidgetCreationHandler(
            IToolTipCreationHandler pWidgetCreationHandler) {
        widgetCreationHandler = pWidgetCreationHandler;
        isDynamicTooltip = false;
    }

    /**
     * Sets the widgetCreationHandler. The handler will be called each time the
     * button is clicked before displaying the tooltip popup.
     * 
     * @param pWidgetCreationHandler
     *            the widgetCreationHandler to set
     */
    public void setDynamicWidgetCreationHandler(
            IToolTipCreationHandler pWidgetCreationHandler) {
        setWidgetCreationHandler(pWidgetCreationHandler);
        isDynamicTooltip = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        boolean lFirstClick = (tooltip == null);
        if (lFirstClick) { // First creation
            tooltip = new GpmToolTipPanel();
        }
        if (widgetCreationHandler != null && (isDynamicTooltip || lFirstClick)) {
            widgetCreationHandler.fillPopupContent(tooltip);
        }
        final int lLeft = getAbsoluteLeft() + getOffsetWidth();
        final int lTop = getAbsoluteTop() + getOffsetHeight();

        tooltip.setVisible(false); // Put in page without being visible
        tooltip.show();
        tooltip.setPopupPosition(lLeft, lTop);

        int lHeight = tooltip.getElement().getClientHeight();
        int lWidth = tooltip.getElement().getClientWidth();

        // Reposition popup if out of screen
        int lNewTop = lTop;
        int lNewLeft = lLeft;

        // Move up if height is too big
        if (lTop + lHeight > Window.getClientHeight()) {
            lNewTop = getAbsoluteTop() - lHeight;
            // If out of screen
            if (lNewTop < 0) {
                lNewTop = 0;
                if (lHeight > Window.getClientHeight()) {
                    lHeight = Window.getClientHeight();
                }
            }
        }
        // Move left if width is too big
        if (lLeft + lWidth > Window.getClientWidth()) {
            lNewLeft = getAbsoluteLeft() - tooltip.getOffsetWidth();
            // If out of screen
            if (lNewLeft < 0) {
                lNewLeft = 0;
                if (lWidth > Window.getClientWidth()) {
                    lWidth = Window.getClientWidth();
                }
            }
        }

        tooltip.setPopupPosition(lNewLeft, lNewTop);
        tooltip.setPixelSize(lWidth, lHeight);

        tooltip.setVisible(true); // Make visible now positionning is ok
    }
}

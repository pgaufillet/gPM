/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This container is able to provide its dimensions.
 * <p>
 * Display its elements horizontally, and vertically. It knows its size,
 * calculated from the position of its components. It also knows its minimum
 * height and width, calculated from the size of its components.
 * </p>
 * 
 * @author frr
 */
public class GpmFlowLayoutPanel extends FlowPanel implements ISizeAware {

    /**
     * Creates an empty FlowLayoutPanel.
     */
    public GpmFlowLayoutPanel() {
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmMenuFloat());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        final int lHeight;

        // OffsetHeight == 0 -> Component is not displayed yet
        if (getOffsetHeight() > 0) {
            final Widget lLastElement = getLastElement();

            if (lLastElement == null) {
                lHeight = 0;
            }
            else {
                lHeight =
                        lLastElement.getAbsoluteTop()
                                + lLastElement.getOffsetHeight()
                                - getAbsoluteTop() + 1;
            }
        }
        else {
            lHeight = 0;
        }

        return lHeight;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        return this.getOffsetWidth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        // TODO : to confirm with Thomas
        return getHeight();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        int lElemMaxSize = 0;
        for (Widget lElem : getChildren()) {
            lElemMaxSize += lElem.getOffsetWidth() + 4; // emulated 2px margin on each side
        }
        return lElemMaxSize;
    }

    private Widget getLastElement() {
        if (getChildren().size() == 0) {
            return null;
        }
        return getChildren().get(getChildren().size() - 1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

}

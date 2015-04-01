/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Decorator for layout panel.
 * 
 * @author tpanuel
 */
public class GpmDecoratorLayoutPanel extends LayoutPanel {
    private final static int BORDER_SIZE = 5;

    /**
     * Create a decorator for layout panel.
     * 
     * @param pCenter
     *            The content to decorate.
     */
    public GpmDecoratorLayoutPanel(final Widget pCenter) {
        super();

        final LayoutPanel lLeft = new LayoutPanel();
        final LayoutPanel lRight = new LayoutPanel();

        lLeft.setStyleName(ComponentResources.INSTANCE.css().left());
        pCenter.setStyleName(ComponentResources.INSTANCE.css().center());
        lRight.setStyleName(ComponentResources.INSTANCE.css().right());

        add(lLeft);
        add(pCenter);
        add(lRight);

        setWidgetLeftWidth(lLeft, 0, Unit.PX, BORDER_SIZE, Unit.PX);
        setWidgetLeftRight(pCenter, BORDER_SIZE, Unit.PX, BORDER_SIZE, Unit.PX);
        setWidgetRightWidth(lRight, 0, Unit.PX, BORDER_SIZE, Unit.PX);

        setStyleName(ComponentResources.INSTANCE.css().gpmDecoratedMenu());
    }
}
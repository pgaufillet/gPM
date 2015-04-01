/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout.stack;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Stack layout panel used by gPM.
 * 
 * @author tpanuel
 */
public class GpmStackLayoutPanel extends StackLayoutPanel {
    private final static double HEADER_SIZE = 28;

    /**
     * Create a stack layout panel.
     */
    public GpmStackLayoutPanel() {
        super(Unit.PX);
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmStackLayoutPanel());
    }

    /**
     * Add a panel.
     * 
     * @param pTitle
     *            The title of the panel.
     * @param pPanel
     *            The content.
     * @param pIsFirst
     *            If it's the first element.
     */
    public void addPanel(final String pTitle, final Widget pPanel,
            final boolean pIsFirst) {
        pPanel.addStyleName(ComponentResources.INSTANCE.css().gpmStackContent());
        add(pPanel, new GpmStackHeader(pTitle, pIsFirst), HEADER_SIZE);
    }
}
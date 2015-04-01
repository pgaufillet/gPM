/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * IFrame on a layout panel.
 * 
 * @author tpanuel
 */
public class GpmIFrameLayoutPanel extends LayoutPanel {
    private final Frame iFrame;

    /**
     * Create a layout panel with an IFrame.
     * 
     * @param pURL
     *            The URL for the IFrame.
     */
    public GpmIFrameLayoutPanel(final String pURL) {
        super();
        iFrame = new Frame(pURL);
        iFrame.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmIFrame());
        iFrame.getElement().setAttribute("frameborder", "0");
        add(iFrame);
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                onResize();
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.LayoutPanel#onResize()
     */
    @Override
    public void onResize() {
        iFrame.setPixelSize(getOffsetWidth(), getOffsetHeight());
    }
}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: PANUEL Thomas (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout.stack;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmDecoratorPanel;

import com.google.gwt.user.client.ui.HTML;

/**
 * A header for the GpmStackLayoutPanel.
 * 
 * @author tpanuel
 */
public class GpmStackHeader extends GpmDecoratorPanel {
    /**
     * Create a header.
     * 
     * @param pText
     *            The text of the header.
     * @param pIsFirst
     *            If it's the first element.
     */
    public GpmStackHeader(final String pText, final boolean pIsFirst) {
        super();
        setWidget(new HTML(pText));
        setStyleName(ComponentResources.INSTANCE.css().gpmStackHeader());
        if (pIsFirst) {
            addStyleName(ComponentResources.INSTANCE.css().gpmStackFirstHeader());
        }
    }
}
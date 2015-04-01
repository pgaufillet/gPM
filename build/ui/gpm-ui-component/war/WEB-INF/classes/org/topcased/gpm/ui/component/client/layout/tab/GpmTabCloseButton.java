/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout.tab;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

/**
 * This class defines a button widget to close the attached tab in a
 * GpmTabLayoutPanel.
 * <p>
 * The action must be configure by the constructor using addClickHandler method.
 * </p>
 * <h3>CSS style rules</h3>
 * <dl>
 * <dt>gpm-GpmTabCloseButton</dt>
 * <dd>Widget default style</dd>
 * </dl>
 * 
 * @author frosier
 */
public class GpmTabCloseButton extends GpmDoubleImageButton {
    private final String tabId;

    /**
     * Creates a close tab button for a given tab layout panel and a tab.
     * 
     * @param pTabId
     *            The tab identifier that the close tab button must be attached.
     */
    public GpmTabCloseButton(final String pTabId) {
        super(INSTANCE.images().close(), INSTANCE.images().closeHover());
        tabId = pTabId;
        addStyleName(ComponentResources.INSTANCE.css().gpmTabCloseButton());
    }

    /**
     * Getter on the tab id.
     * 
     * @return The tab id.
     */
    public String getTabId() {
        return tabId;
    }
}
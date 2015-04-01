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
 * Panel that displays its contents horizontally. Override HorizontalPanel to be
 * compatible with layout system. <h3>CSS style rules</h3>
 * <dl>
 * <dt>gpmHorizontalPanel</dt>
 * <dd>The panel itself.</dd>
 * <dt>gpmHorizontalPanelContent</dt>
 * <dd>Style append to each content widgets.</dd>
 * </dl>
 * 
 * @author mkargbo
 */
public class GpmHorizontalPanel extends FlowPanel {
    /**
     * Construct the panel
     */
    public GpmHorizontalPanel() {
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmHorizontalPanel());
    }

    /**
     * Add a widget and append the content style.
     * 
     * @param pWidget
     *            Widget to add
     */
    public void add(Widget pWidget) {
        pWidget.addStyleName(ComponentResources.INSTANCE.css().gpmHorizontalPanelContent());
        super.add(pWidget);
    }
}

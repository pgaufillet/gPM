/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: PANUEL Thomas (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout.tab;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmDecoratorPanel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

/**
 * A tab header for the GpmTabLayoutPanel.
 * 
 * @author tpanuel
 */
public class GpmTab extends GpmDecoratorPanel {
    /**
     * Create a tab header.
     * 
     * @param pChild
     *            The tab header content.
     * @param pExpand
     *            indicates if the tab must expand to its maximum width or not
     */
    public GpmTab(final Widget pChild, boolean pExpand) {
        super();
        setWidget(pChild);
        setStyleName(ComponentResources.INSTANCE.css().gpmTabHeader());
        if (pExpand) {
            getElement().getStyle().setWidth(100, Unit.PCT);
        }
        else {
            getElement().getStyle().setFloat(Style.Float.LEFT);
        }
    }

    /**
     * Add a click handler.
     * 
     * @param pHandler
     *            The click handler.
     * @return The click handler.
     */
    public HandlerRegistration addClickHandler(ClickHandler pHandler) {
        return addDomHandler(pHandler, ClickEvent.getType());
    }

    /**
     * Set if the tab is selected.
     * 
     * @param pSelected
     *            If the tab is selected.
     */
    public void setSelected(boolean pSelected) {
        if (pSelected) {
            addStyleName(ComponentResources.INSTANCE.css().gpmTabHeaderSelected());
        }
        else {
            removeStyleName(ComponentResources.INSTANCE.css().gpmTabHeaderSelected());
        }
    }
}
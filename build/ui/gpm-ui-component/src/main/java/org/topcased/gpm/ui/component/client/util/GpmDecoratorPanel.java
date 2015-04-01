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

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * A Decorator panel.
 * 
 * @author tpanuel
 */
public class GpmDecoratorPanel extends SimplePanel {

    // CSS Styles
    private static final String STYLE_HEAVY_CENTER =
            ComponentResources.INSTANCE.css().center();

    private static final String STYLE_HEAVY_RIGHT =
            ComponentResources.INSTANCE.css().right();

    private static final String STYLE_HEAVY_LEFT =
            ComponentResources.INSTANCE.css().left();

    private static final String STYLE_LIGHT_CENTER =
            ComponentResources.INSTANCE.css().gpmLightDisclosurePanelHeaderCenter();

    private static final String STYLE_LIGHT_RIGHT =
            ComponentResources.INSTANCE.css().gpmLightDisclosurePanelHeaderRight();

    private final Element containerElem;

    private final Element rightCell;

    /**
     * Create a decorator panel.
     */
    public GpmDecoratorPanel() {
        this(true);
    }

    /**
     * Called when the content of the panel is hidden if necessary
     */
    public void minimize() {
        rightCell.getStyle().setVisibility(Visibility.HIDDEN);
    }

    /**
     * Called when the content of the panel is shown if necessary
     */
    public void maximize() {
        rightCell.getStyle().setVisibility(Visibility.VISIBLE);
    }

    /**
     * Create a decorator panel.
     * 
     * @param pFullMode
     *            Indicates if the decorator must be a light or heavy decorator,
     *            <CODE>true</CODE> for heavy and <CODE>false</CODE> for light.
     */
    public GpmDecoratorPanel(boolean pFullMode) {
        super(DOM.createTable());

        final Element lTable = getElement();
        final Element lBody = DOM.createTBody();
        final Element lRow = DOM.createTR();

        if (pFullMode) {
            DOM.appendChild(lRow, createTD(STYLE_HEAVY_LEFT));
            containerElem = createTD(STYLE_HEAVY_CENTER);
            rightCell = createTD(STYLE_HEAVY_RIGHT);
        }
        else {
            containerElem = createTD(STYLE_LIGHT_CENTER);
            rightCell = createTD(STYLE_LIGHT_RIGHT);
            rightCell.getStyle().setVisibility(Visibility.HIDDEN);
        }
        DOM.appendChild(lRow, containerElem);
        DOM.appendChild(lRow, rightCell);

        DOM.appendChild(lTable, lBody);
        DOM.setElementPropertyInt(lTable, "cellSpacing", 0);
        DOM.setElementPropertyInt(lTable, "cellPadding", 0);
        DOM.appendChild(lBody, lRow);
    }

    private Element createTD(final String pStyleName) {
        final Element lTd = DOM.createTD();
        final Element lContent = DOM.createDiv();

        DOM.appendChild(lTd, lContent);
        setStyleName(lTd, pStyleName);

        return lTd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.SimplePanel#getContainerElement()
     */
    @Override
    protected Element getContainerElement() {
        return containerElem;
    }
}
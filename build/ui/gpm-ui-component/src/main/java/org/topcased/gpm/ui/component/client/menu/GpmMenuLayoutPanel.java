/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import org.topcased.gpm.ui.component.client.button.GpmToolTipImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmFlowLayoutPanel;
import org.topcased.gpm.ui.component.client.layout.ISizeAware;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

/**
 * Component to display the menu area. It is composed in two sub components :
 * <ul>
 * <li>the titles area that is left-aligned</li>
 * <li>the tool bars area that is right-aligned</li>
 * </ul>
 * 
 * @author frosier
 */
public class GpmMenuLayoutPanel extends GpmFlowLayoutPanel implements
        ISizeAware {
    private static final double BUTTON_MARGIN = 4;

    private static final int MIN_WIDTH_ERROR_MARGIN = 10;

    private GpmFlowLayoutPanel titlesPanel;

    private GpmFlowLayoutPanel toolBarsPanel;

    /**
     * Creates an empty menu layout panel.
     */
    public GpmMenuLayoutPanel() {
        titlesPanel = new GpmFlowLayoutPanel();
        toolBarsPanel = new GpmFlowLayoutPanel();

        add(toolBarsPanel);
        add(titlesPanel);

        toolBarsPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        titlesPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);

        titlesPanel.addStyleName(ComponentResources.INSTANCE.css().gpmMenuTitles());
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmMenu());
    }

    /**
     * Adds a new child widget to the titles layout panel.
     * 
     * @param pTitle
     *            The title widget.
     */
    public void addTitle(final Widget pTitle) {
        titlesPanel.add(pTitle);
    }

    /**
     * Get a tool bar.
     * 
     * @param pIndex
     *            The tool bar index.
     * @return the tool bar.
     */
    public GpmToolBar getToolBar(final int pIndex) {
        return (GpmToolBar) toolBarsPanel.getWidget(pIndex);
    }

    /**
     * Set a unique tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(final GpmToolBar pToolBar) {
        toolBarsPanel.clear();
        toolBarsPanel.add(pToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        int lTitlePanelHeight = 0;
        int lToolBarPanelHeight = 0;

        if (titlesPanel != null) {
            lTitlePanelHeight = titlesPanel.getHeight();
        }
        if (toolBarsPanel != null) {
            lToolBarPanelHeight = toolBarsPanel.getHeight();
        }

        return Math.max(lTitlePanelHeight, lToolBarPanelHeight);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        int lWidth = 0;

        if (titlesPanel != null) {
            lWidth += titlesPanel.getWidth();
        }
        if (toolBarsPanel != null) {
            lWidth += toolBarsPanel.getWidth();
        }

        return lWidth;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        int lTitlePanelMinHeight = 0;
        int lToolBarPanelMinHeight = 0;

        if (titlesPanel != null) {
            lTitlePanelMinHeight = titlesPanel.getMinHeight();
        }
        if (toolBarsPanel != null) {
            lToolBarPanelMinHeight = toolBarsPanel.getMinHeight();
        }

        return Math.max(lTitlePanelMinHeight, lToolBarPanelMinHeight);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        int lMinWidth = 0;

        if (titlesPanel != null) {
            lMinWidth += titlesPanel.getMinWidth();
        }
        if (toolBarsPanel != null) {
            lMinWidth += toolBarsPanel.getMinWidth();
        }

        return lMinWidth + MIN_WIDTH_ERROR_MARGIN;
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

    /**
     * Similar to addTitle, but set the desired styles to the button in argument
     * 
     * @param pButton
     *            the button to add
     */
    public void addTitleButton(GpmToolTipImageButton pButton) {
        pButton.setStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        pButton.getElement().getStyle().setMargin(BUTTON_MARGIN, Unit.PX);
        titlesPanel.add(pButton);
    }
}
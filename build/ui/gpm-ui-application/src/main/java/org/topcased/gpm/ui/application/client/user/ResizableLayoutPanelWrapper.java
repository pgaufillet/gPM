/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmFlowLayoutPanel;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrap Gpm Widgets for minimize, restore actions. Do not use the add method of
 * this class.
 * 
 * @author jeballar
 */
public class ResizableLayoutPanelWrapper extends LayoutPanel implements
        IResizableLayoutPanel, RequiresResize {
    private static final int MINIMIZED_HEIGHT = 20;

    private static final int MINIMIZED_WIDTH = 20;

    private LayoutPanel contentPanel = new LayoutPanel();

    private GpmFlowLayoutPanel buttonsPanel = new GpmFlowLayoutPanel();

    private boolean minimized = false;

    private IResizableLayoutPanel widget;

    /**
     * Wraps the widget in argument
     * 
     * @param pWidget
     *            the widget to wrap
     */
    public ResizableLayoutPanelWrapper(IResizableLayoutPanel pWidget) {
        widget = pWidget;
        build();
    }

    /**
     * Build the DeckPanel structure
     */
    private void build() {
        add(contentPanel);
        add(buttonsPanel);
        contentPanel.add(widget.asWidget());
        buttonsPanel.setStyleName(ComponentResources.INSTANCE.css().gpmMinimizedMenu());
        showContent();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getHeight()
     */
    public int getHeight() {
        if (minimized) {
            return MINIMIZED_HEIGHT;
        }
        return widget.getHeight();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinHeight()
     */
    public int getMinHeight() {
        if (minimized) {
            return MINIMIZED_HEIGHT;
        }
        return widget.getMinHeight();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinWidth()
     */
    public int getMinWidth() {
        if (minimized) {
            return MINIMIZED_WIDTH;
        }
        return widget.getMinWidth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getWidth()
     */
    public int getWidth() {
        if (minimized) {
            return MINIMIZED_WIDTH;
        }
        return widget.getWidth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.RequiresResize#onResize()
     */
    public void onResize() {
        if (!minimized) {
            for (Widget lChild : contentPanel) {
                if (lChild instanceof RequiresResize) {
                    ((RequiresResize) lChild).onResize();
                }
            }
        }
        else {
            for (Widget lChild : buttonsPanel) {
                if (lChild instanceof RequiresResize) {
                    ((RequiresResize) lChild).onResize();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    public void doMinimize() {
        widget.doMinimize();
        showMenu();
        minimized = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    public void doRestore() {
        widget.doRestore();
        showContent();
        minimized = false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    public void doMaximize() {
        widget.doMaximize();
        showContent();
        minimized = false;
    }

    /**
     * Sets the button for the minimized status of the panel.
     * 
     * @param pMinimizedButton
     *            The button for the minimized status of the panel.
     */
    public void setMinimizedButton(final GpmImageButton pMinimizedButton) {
        pMinimizedButton.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        buttonsPanel.clear();
        pMinimizedButton.getElement().getStyle().setFloat(Float.RIGHT);
        buttonsPanel.add(pMinimizedButton);
    }

    /**
     * Show the content
     */
    private void showContent() {
        buttonsPanel.setVisible(false);
        UIObject.setVisible(DOM.getParent(buttonsPanel.getElement()), false);
        UIObject.setVisible(DOM.getParent(contentPanel.getElement()), true);
        contentPanel.setVisible(true);
    }

    /**
     * Shows the menu
     */
    private void showMenu() {
        contentPanel.setVisible(false);
        UIObject.setVisible(DOM.getParent(contentPanel.getElement()), false);
        UIObject.setVisible(DOM.getParent(buttonsPanel.getElement()), true);
        buttonsPanel.setVisible(true);
    }
}

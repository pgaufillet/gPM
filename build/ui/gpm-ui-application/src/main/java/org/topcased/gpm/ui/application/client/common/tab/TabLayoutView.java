/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tab;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.I18N;

import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.layout.GpmTabLayoutPanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for the tab layout.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of tab element display.
 */
public class TabLayoutView<T extends TabElementDisplay> extends
        GpmTabLayoutPanel implements TabLayoutDisplay<T> {
    /**
     * Create a tab layout view.
     * 
     * @param pAddButton
     *            If a add button exists.
     */
    public TabLayoutView(final boolean pAddButton) {
        super(pAddButton, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutDisplay#addTab(org.topcased.gpm.ui.application.client.common.tab.TabElementDisplay,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addTab(final T pTabDisplay, final ClickHandler pCloseHandler) {
        addAndSelect(pTabDisplay.asWidget(),
                I18N.getConstant(pTabDisplay.getTabTitle()), pCloseHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutDisplay#removeTab(java.lang.String)
     */
    @Override
    public void removeTab(final String pTabId) {
        remove(pTabId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabLayoutDisplay#getCurrentTab()
     */
    @SuppressWarnings("unchecked")
    @Override
    public T getCurrentTab() {
        return (T) getCurrentWidget();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    @Override
    public void doMaximize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    @Override
    public void doMinimize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void doRestore() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        // Not used
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        int lSize = 0;
        if (getCurrentTab() != null) { // At least one tab is displayed
            lSize +=
                    TAB_HEADER_SIZE
                            + ((GpmLayoutPanelWithMenu) getCurrentTab()).getMinHeight();
        }
        return lSize;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        int lSize = 0;
        if (getCurrentTab() != null) { // At least one tab is displayed
            lSize = ((GpmLayoutPanelWithMenu) getCurrentTab()).getMinWidth();
        }
        return lSize;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        // Not used
        return 0;
    }
}
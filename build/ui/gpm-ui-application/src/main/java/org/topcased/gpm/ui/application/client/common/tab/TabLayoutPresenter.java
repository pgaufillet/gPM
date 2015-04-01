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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.common.AbstractPresenter;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Presenter of a tab layout.
 * 
 * @author tpanuel
 * @param <D>
 *            The type of tab layout display.
 * @param <T>
 *            The type of tab element display.
 */
public abstract class TabLayoutPresenter<D extends TabLayoutDisplay<T>, T extends TabElementDisplay>
        extends AbstractPresenter<D> {
    private final Map<String, TabElementPresenter<T>> elementPresenters;

    /**
     * Create a tab layout presenter.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            the event bus.
     */
    public TabLayoutPresenter(final D pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
        elementPresenters = new LinkedHashMap<String, TabElementPresenter<T>>();
    }

    /**
     * Add or replace a tab.
     * 
     * @param pTabPresenter
     *            The presenter of the tab to add.
     */
    public void addOrReplaceTab(final TabElementPresenter<T> pTabPresenter) {
        final String lTabId = pTabPresenter.getTabId();
        // Remove the old presenter if exists
        final TabElementPresenter<T> lOldPresenter =
                elementPresenters.remove(lTabId);

        // Unbind old presenter, if exists
        if (lOldPresenter != null) {
            lOldPresenter.unbind();
        }
        // Initialize and add the new one
        elementPresenters.put(lTabId, pTabPresenter);
        pTabPresenter.getDisplay().asWidget().getElement().setId(lTabId);
        pTabPresenter.bind();
        getDisplay().addTab(pTabPresenter.getDisplay(),
                getCloseClickHandler(lTabId));
    }

    /**
     * Get the number of tab.
     * 
     * @return The number of tab.
     */
    protected int getNbTab() {
        return elementPresenters.size();
    }

    /**
     * Get the close click handler for a tab.
     * 
     * @param pTabId
     *            The tab id.
     * @return The handler.
     */
    abstract protected ClickHandler getCloseClickHandler(final String pTabId);

    /**
     * Test if a tab is already open.
     * 
     * @param pTabId
     *            The tab id.
     * @return If the tab is already open.
     */
    public boolean containTab(final String pTabId) {
        return elementPresenters.containsKey(pTabId);
    }

    /**
     * Select a tab.
     * 
     * @param pTabId
     *            The tab id.
     * @return The selected tab.
     */
    public TabElementPresenter<T> selectTab(final String pTabId) {
        getDisplay().selectTab(pTabId);
        return elementPresenters.get(pTabId);
    }

    /**
     * Get the selected tab.
     * 
     * @return The selected tab.
     */
    protected TabElementPresenter<T> getSelectedTab() {
        final T lCurrentTab = getDisplay().getCurrentTab();

        if (lCurrentTab == null) {
            return null;
        }
        else {
            return elementPresenters.get(lCurrentTab.asWidget().getElement().getId());
        }
    }

    /**
     * Remove a tab.
     * 
     * @param pTabId
     *            The tab id.
     */
    public void removeTab(final String pTabId) {
        final TabElementPresenter<T> lElementPresenter =
                elementPresenters.remove(pTabId);

        if (lElementPresenter != null) {
            lElementPresenter.unbind();
            getDisplay().removeTab(pTabId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        for (final TabElementPresenter<T> lSubPresenter : elementPresenters.values()) {
            lSubPresenter.unbind();
        }
        elementPresenters.clear();
    }

    /**
     * Get the presenter ids.
     * 
     * @return The presenter ids.
     */
    public Collection<String> getPresenterIds() {
        return elementPresenters.keySet();
    }

    /**
     * Get the presenters.
     * 
     * @return The presenters.
     */
    public Collection<TabElementPresenter<T>> getPresenters() {
        return elementPresenters.values();
    }

    /**
     * Get a presenter by Id
     * 
     * @param pId
     *            Identifier of the tab to open
     * @return the TabPresenter corresponding to the given Id or null
     */
    public TabElementPresenter<T> getPresenterById(String pId) {
        return elementPresenters.get(pId);
    }
}
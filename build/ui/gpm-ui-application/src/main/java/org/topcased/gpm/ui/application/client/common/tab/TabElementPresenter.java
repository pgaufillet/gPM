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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.common.AbstractPresenter;

/**
 * Interface for presenter of a tab element.
 * 
 * @author tpanuel
 * @param <D>
 *            The type of display.
 */
public abstract class TabElementPresenter<D extends TabElementDisplay> extends
        AbstractPresenter<D> {
    /**
     * Create a tab element presenter.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            the event bus.
     */
    public TabElementPresenter(final D pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Get the tab id.
     * 
     * @return The tab id.
     */
    public abstract String getTabId();
}
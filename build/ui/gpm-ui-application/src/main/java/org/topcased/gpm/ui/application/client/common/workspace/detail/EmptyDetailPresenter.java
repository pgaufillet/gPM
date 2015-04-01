/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.detail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.common.AbstractPresenter;

import com.google.inject.Inject;

/**
 * The presenter for the EmptyDetailView.
 * 
 * @author nveillet
 */
public class EmptyDetailPresenter extends AbstractPresenter<EmptyDetailDisplay> {

    /**
     * Create a presenter for the EmptyDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public EmptyDetailPresenter(EmptyDetailDisplay pDisplay, EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // TODO Auto-generated method stub
    }
}

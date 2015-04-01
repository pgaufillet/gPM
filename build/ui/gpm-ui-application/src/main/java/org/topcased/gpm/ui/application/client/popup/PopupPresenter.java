/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * The presenter for the popup.
 * 
 * @author tpanuel
 * @param <D>
 *            The display type.
 */
public abstract class PopupPresenter<D extends PopupDisplay> extends
        AbstractPresenter<D> {
    /**
     * Create a presenter for the popup.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public PopupPresenter(final D pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().center();
        // Add handler on the close event
        if (getClosePopupEvent() != null) {
            addEventHandler(getClosePopupEvent().getType(),
                    new ActionEventHandler<ClosePopupAction>() {
                        @Override
                        public void execute(final ClosePopupAction pResult) {
                            unbind();
                        }
                    });
        }
        // Add event on the close button
        if (getClosePopupEvent() != null) {
            getDisplay().addCloseButtonHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    unbind();
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        getDisplay().hide();
    }

    /**
     * Get the close popup event.
     * 
     * @return The close popup event.
     */
    abstract protected GlobalEvent<ClosePopupAction> getClosePopupEvent();
}
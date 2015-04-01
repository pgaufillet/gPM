/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * The presenter for the ErrorMessageView.
 * 
 * @author tpanuel
 */
public class ErrorMessagePresenter extends PopupPresenter<ErrorMessageDisplay> {
    /**
     * Create a presenter for the ErrorMessageView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public ErrorMessagePresenter(final ErrorMessageDisplay pDisplay,
            final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Display a message.
     * 
     * @param pMessage
     *            The message.
     */
    public void displayMessage(final String pMessage) {
        getDisplay().setErrorMessage(pMessage);
        bind();
    }

    /**
     * Display an exception.
     * 
     * @param pMessage
     *            The message.
     * @param pStackTrace
     *            The stack trace.
     */
    public void displayException(final String pMessage,
            final List<String> pStackTrace) {
        getDisplay().setErrorMessage(pMessage);
        if (!pStackTrace.isEmpty()) {
            getDisplay().setStackTrace(pStackTrace);
        }
        bind();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().getOkButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                unbind();
            }
        });
        super.onBind();
    }
}
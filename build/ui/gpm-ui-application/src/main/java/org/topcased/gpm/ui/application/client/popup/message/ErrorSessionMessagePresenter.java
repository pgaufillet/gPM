/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * The presenter for the ErrorSessionMessageView.
 * 
 * @author nveillet
 */
public class ErrorSessionMessagePresenter extends
        PopupPresenter<ErrorSessionMessageDisplay> {
    /**
     * Create a presenter for the ErrorMessageView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public ErrorSessionMessagePresenter(
            final ErrorSessionMessageDisplay pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
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
        getDisplay().getLogoutButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                fireEvent(GlobalEvent.LOGOUT.getType(), new LogoutAction());
            }
        });
        getDisplay().getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                unbind();
            }
        });
        super.onBind();
    }
}
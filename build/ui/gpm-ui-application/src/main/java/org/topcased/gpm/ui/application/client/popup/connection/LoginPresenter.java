/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;

import org.topcased.gpm.ui.application.client.command.connection.AuthenticationCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.inject.Inject;

/**
 * Presents the authentication feature. <h4>Binding</h4>
 * <ul>
 * <li>Validate button with {@link AuthenticationCommand}</li>
 * <li>Fire <code>AUTHENTICATE</code> event or <code>CONNECTION</code> event or
 * display login error message</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class LoginPresenter extends PopupPresenter<LoginDisplay> {
    private AuthenticationCommand authenticationCommand;

    /**
     * Presenter constructor
     * 
     * @param pDisplay
     *            Display
     * @param pEventBus
     *            Event bus
     * @param pAuthenticationCommand
     *            Authentication command
     */
    @Inject
    public LoginPresenter(LoginDisplay pDisplay, EventBus pEventBus,
            AuthenticationCommand pAuthenticationCommand) {
        super(pDisplay, pEventBus);
        authenticationCommand = pAuthenticationCommand;
        authenticationCommand.setDisplay(display);
        display.getAuthenticationButton().addClickHandler(authenticationCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#getPlace()
     */
    @Override
    public Place getPlace() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Presenter#refreshDisplay()
     */
    @Override
    public void refreshDisplay() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Presenter#revealDisplay()
     */
    @Override
    public void revealDisplay() {
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

}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import java.util.HashMap;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

/**
 * Command for authentication feature.
 * <p>
 * </p>
 * <h4>Handle</h4>
 * <ul>
 * <li>login and password value validation</li>
 * <li>Launch <code>CONNECTION</code> event or <code>AUTHENTICATE</code> event
 * if <code>authenticateOnly</code> attribute is set</li>
 * <li>Show error message if the values are invalid.</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class AuthenticationCommand extends AbstractCommand implements
        ClickHandler {
    private LoginDisplay display;

    /**
     * Command constructor
     * 
     * @param pEventBus
     *            Event bus
     */
    @Inject
    public AuthenticationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final String lLogin = display.getLogin().getValue();

        // Clean old error
        display.hideLoginError();

        if (!isValueValid(lLogin)) {
            // Error : login must be filled
            showError(CONSTANTS.authenticationLoginText());
        }
        else {
            final String lPassword = display.getPassword().getValue();

            if (!isValueValid(lPassword)) {
                // Error : password must be filled
                showError(CONSTANTS.authenticationPasswordText());
            }
            else {
                LoginAction lLoginAction =
                        new LoginAction(lLogin, lPassword,
                                GWT.getHostPageBaseURL());

                // Set parameters
                lLoginAction.setParameters(new HashMap<String, String>(
                        CollectionUtil.toCaseInsensitiveMap(Window.Location.getParameterMap())));

                // Set current locale
                lLoginAction.setLocaleName(LocaleInfo.getCurrentLocale().getLocaleName());
                lLoginAction.setAvailableLocale(LocaleInfo.getAvailableLocaleNames());

                fireEvent(GlobalEvent.CONNECTION.getType(), lLoginAction);
            }
        }
    }

    /**
     * Test <code>null</code> and value length.
     * 
     * @param pValue
     *            Value to test.
     * @return True if the value is valid, false otherwise.
     */
    private boolean isValueValid(final String pValue) {
        return pValue != null && !pValue.isEmpty();
    }

    private void showError(final String pFieldName) {
        Window.alert(MESSAGES.fieldErrorMandatory(pFieldName));
    }

    public LoginDisplay getDisplay() {
        return display;
    }

    public void setDisplay(LoginDisplay pDisplay) {
        display = pDisplay;
    }
}
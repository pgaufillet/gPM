/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for authentication feature.
 * <p>
 * Use {@link PopupView}
 * </p>
 * <h4>Display</h4>
 * <ul>
 * <li>login text box</li>
 * <li>password text box</li>
 * <li>validate text box</li>
 * <li>error message label</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class LoginView extends PopupView implements LoginDisplay {

    private static final int POPUP_HEIGHT = 150;

    private static final int POPUP_WIDTH = 330;

    private final TextBox loginBox;

    private final PasswordTextBox passwordBox;

    private final ButtonBase authenticationButton;

    private Label errorMessage;

    /**
     * View constructor
     */
    public LoginView() {
        // Popup that cannot be close.
        super(false);
        setAutoHideEnabled(false);

        // Header
        setHeaderText(CONSTANTS.authenticationTitle());

        // Content
        final FlowPanel lForm = new FlowPanel();
        final GpmFormPanel lGrid = new GpmFormPanel(2);

        loginBox = new TextBox();
        loginBox.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());

        passwordBox = new PasswordTextBox();
        passwordBox.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());

        errorMessage = new InlineLabel(CONSTANTS.authenticationError());
        errorMessage.addStyleName(INSTANCE.css().gpmPopupErrorMessage());
        errorMessage.setVisible(false);

        lGrid.addField(CONSTANTS.authenticationLoginText(), loginBox);

        lGrid.addField(CONSTANTS.authenticationPasswordText(), passwordBox);

        lForm.add(lGrid);
        lForm.add(errorMessage);
        lForm.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        final SimplePanel lPanel = new SimplePanel();

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lForm);
        setContent(lPanel);

        // Button
        authenticationButton = addButton(CONSTANTS.authenticationButtonText());

        // Add Key Handler on ENTER key : Quick Submit
        KeyPressHandler lEnterKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent pEvent) {
                if (KeyCodes.KEY_ENTER == pEvent.getCharCode()) {
                    ((Button) authenticationButton).click();
                }
            }
        };

        loginBox.addKeyPressHandler(lEnterKeyPressHandler);
        passwordBox.addKeyPressHandler(lEnterKeyPressHandler);

        setPixelSize(POPUP_WIDTH, POPUP_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay#getAuthenticationButton()
     */
    @Override
    public HasClickHandlers getAuthenticationButton() {
        return authenticationButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay#getLogin()
     */
    @Override
    public HasValue<String> getLogin() {
        return loginBox;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay#getPassword()
     */
    @Override
    public HasValue<String> getPassword() {
        return passwordBox;
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
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay#displayLoginError()
     */
    @Override
    public void displayLoginError() {
        errorMessage.setVisible(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.LoginDisplay#hideLoginError()
     */
    @Override
    public void hideLoginError() {
        errorMessage.setVisible(false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.popup.GpmPopupPanel#show()
     */
    @Override
    public void show() {
        super.show();

        // Set the focus to the login
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                loginBox.setCursorPos(loginBox.getText().length());
                loginBox.setFocus(true);
            }
        });
    }
}
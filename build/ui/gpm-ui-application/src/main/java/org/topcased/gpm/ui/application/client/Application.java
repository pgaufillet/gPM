/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client;

import java.util.HashMap;

import org.topcased.gpm.ui.application.client.config.GpmGinjector;
import org.topcased.gpm.ui.application.client.event.ActionEvent;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginAction;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point for the gPM web application.
 * 
 * @author tpanuel
 */
public class Application implements EntryPoint {
    /**
     * Injector with all the presenters.
     */
    public static final GpmGinjector INJECTOR = GWT.create(GpmGinjector.class);

    private static final String SPLASH_SCREEN_ID = "splash";

    public static final String DOWNLOAD_URL =
            GWT.getModuleBaseURL() + "download";

    public static final String UPLOAD_URL = GWT.getModuleBaseURL() + "upload";

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad() {
        // Inject CSS style
        StyleInjector.inject(ComponentResources.INSTANCE.css().getText());

        // Start the popup manager
        INJECTOR.getPopupManager().start();

        // Hide the splash screen
        DOM.getElementById(Application.SPLASH_SCREEN_ID).getStyle().setProperty(
                "display", "none");

        // Display the main view
        INJECTOR.getMainPresenter().bind();
        RootLayoutPanel.get().add(
                INJECTOR.getMainPresenter().getDisplay().asWidget());

        // Create first action
        final LoginAction lLoginAction =
                new LoginAction(GWT.getHostPageBaseURL());

        // Set parameters
        lLoginAction.setParameters(new HashMap<String, String>(
                CollectionUtil.toCaseInsensitiveMap(Window.Location.getParameterMap())));
        // Set current local
        lLoginAction.setLocaleName(LocaleInfo.getCurrentLocale().getLocaleName());
        lLoginAction.setAvailableLocale(LocaleInfo.getAvailableLocaleNames());
        // Launch first action
        INJECTOR.getEventBus().fireEvent(
                new ActionEvent<LoginAction, AbstractConnectionResult>(
                        GlobalEvent.CONNECTION.getType(), lLoginAction));
    }
}
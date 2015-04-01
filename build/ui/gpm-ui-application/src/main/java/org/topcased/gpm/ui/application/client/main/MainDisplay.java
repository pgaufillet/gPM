/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.main;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * The display interface for the main frame.
 * 
 * @author tpanuel
 */
public interface MainDisplay extends WidgetDisplay {
    /**
     * Set the loading panel visibility.
     * 
     * @param pVisibility
     *            The visibility.
     */
    public void setLoadingPanelVisibility(boolean pVisibility);

    /**
     * Set the content.
     * 
     * @param pContent
     *            The content.
     */
    public void setContent(Widget pContent);

    /**
     * Add the button to logout.
     * 
     * @param pClickHandler
     *            The associated click handler.
     */
    public void addLogoutButton(ClickHandler pClickHandler);

    /**
     * Add a click handler to the login button
     * 
     * @param pHandler
     *            the login button click handler
     */
    public void addUserProfileEditionHandler(ClickHandler pHandler);

    /**
     * Set the handler on the button to switch between space.
     * 
     * @param pClickHandler
     *            The associated click handler.
     */
    public void setSwitchButtonHandler(ClickHandler pClickHandler);

    /**
     * Add the help button.
     * 
     * @param pClickHandler
     *            The associated click handler.
     */
    public void addHelpButton(ClickHandler pClickHandler);

    /**
     * Set the process name text displayed in banner
     * 
     * @param pProcessName
     *            the process name
     */
    public void setProcessName(String pProcessName);

    /**
     * Set version displayed in banner
     * 
     * @param pVersion
     *            the version text
     */
    public void setVersion(String pVersion);

    /**
     * Set the login and associated language displayed in banner
     * 
     * @param pLogin
     *            login
     * @param pLanguage
     *            language
     */
    public void setLoginAndLanguage(String pLogin, String pLanguage);

    /**
     * Set the contact URL to be displayed in Header. Not displayed if null
     * 
     * @param pContactUrl
     *            the URL to display
     */
    public void setContactUrl(String pContactUrl);
}
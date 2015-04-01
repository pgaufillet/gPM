/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.listing;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

/**
 * Display interface for the UserListingView.
 * 
 * @author nveillet
 */
public interface UserListingDisplay extends WidgetDisplay,
        IResizableLayoutPanel {

    /**
     * Add a user sorted by login
     * 
     * @param pUserLogin
     *            The user login.
     * @param pUserName
     *            The user name.
     * @param pUserFirstname
     *            The user forename.
     * @param pOpenHandler
     *            The handler to open a user.
     */
    public void addUserByLogin(String pUserLogin, String pUserName,
            String pUserFirstname, OpenUserOnEditionCommand pOpenHandler);

    /**
     * Add a user sorted by name
     * 
     * @param pUserLogin
     *            The user login.
     * @param pUserName
     *            The user name.
     * @param pUserFirstname
     *            The user forename.
     * @param pOpenHandler
     *            The handler to open a user.
     */
    public void addUserByName(String pUserLogin, String pUserName,
            String pUserFirstname, OpenUserOnEditionCommand pOpenHandler);

    /**
     * Clear the view
     */
    public void clear();
}
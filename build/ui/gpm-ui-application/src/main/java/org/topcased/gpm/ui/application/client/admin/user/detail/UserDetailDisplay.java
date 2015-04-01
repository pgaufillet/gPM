/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.detail;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmPasswordBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

/**
 * Display interface for the UserDetailView.
 * 
 * @author nveillet
 */
public interface UserDetailDisplay extends WidgetDisplay, IResizableLayoutPanel {

    /**
     * Clear the view
     */
    public void clear();

    /**
     * Get the first name
     * 
     * @return the first name
     */
    public GpmTextBox<String> getFirstName();

    /**
     * Get the language
     * 
     * @return the language
     */
    public GpmListBox getLanguage();

    /**
     * Get the login
     * 
     * @return the login
     */
    public String getLogin();

    /**
     * Get the mail address
     * 
     * @return the mail address
     */
    public GpmTextBox<String> getMail();

    /**
     * Get the name
     * 
     * @return the name
     */
    public GpmTextBox<String> getName();

    /**
     * Get the password
     * 
     * @return the password
     */
    public GpmPasswordBox getPassword();

    /**
     * Set the tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(final GpmToolBar pToolBar);

    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate();
}
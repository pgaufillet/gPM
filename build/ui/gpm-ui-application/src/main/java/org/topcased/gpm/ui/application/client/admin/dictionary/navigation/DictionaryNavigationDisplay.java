/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.navigation;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnEditionCommand;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

/**
 * Display interface for the DictionaryNavigationView.
 * 
 * @author nveillet
 */
public interface DictionaryNavigationDisplay extends WidgetDisplay,
        IResizableLayoutPanel {

    /**
     * Add a environment to the list
     * 
     * @param pEnvironment
     *            the environment
     * @param pOpenEnvironment
     *            The handler to open a environment.
     */
    public void addEnvironment(String pEnvironment,
            OpenEnvironmentOnEditionCommand pOpenEnvironment);

    /**
     * Clear the view
     */
    public void clear();

    /**
     * Set the dictionary button handler
     * 
     * @param pOpenDictionary
     *            The handler to open a environment.
     */
    public void setDictionaryButtonHandler(OpenDictionaryCommand pOpenDictionary);
}
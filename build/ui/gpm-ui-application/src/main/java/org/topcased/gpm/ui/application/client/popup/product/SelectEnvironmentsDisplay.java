/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the SelectEnvironmentsView.
 * 
 * @author nveillet
 */
public interface SelectEnvironmentsDisplay extends PopupDisplay {

    /**
     * Clear the form.
     */
    public void clear();

    /**
     * Get the environments selected by the user
     * 
     * @return the environments names
     */
    public List<String> getSelectedEnvironments();

    /**
     * Set the environments
     * 
     * @param pEnvironmentNames
     *            the environments names
     */
    public void setEnvironments(List<String> pEnvironmentNames);

    /**
     * Set the select button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setSelectButtonHandler(ClickHandler pHandler);

}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.extended;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.GpmFieldSet;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the InputDataView.
 * 
 * @author tpanuel
 */
public interface InputDataDisplay extends PopupDisplay {
    /**
     * Add a group.
     * 
     * @param pGroupName
     *            The group name.
     * @param pHandler
     *            The handler for create fields.
     * @param pIsOpen
     *            If the group is open.
     */
    public void addFieldGroup(String pGroupName, FieldCreationHandler pHandler,
            boolean pIsOpen);

    /**
     * Clear all the displayed groups.
     */
    public void clearGroups();

    /**
     * Get the field set associate to the input data.
     * 
     * @return The input data field set.
     */
    public GpmFieldSet getInputDataFieldSet();

    /**
     * Set the execute button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setExecuteButtonHandler(ClickHandler pHandler);
}
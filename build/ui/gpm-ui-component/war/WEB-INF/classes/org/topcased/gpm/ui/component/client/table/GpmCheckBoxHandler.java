/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.table;

/**
 * Handler for gPM check box.
 * 
 * @author tpanuel
 */
public interface GpmCheckBoxHandler {
    /**
     * On click event.
     * 
     * @param pSelected
     *            If the check box is selected.
     */
    public void onClick(boolean pSelected);
}
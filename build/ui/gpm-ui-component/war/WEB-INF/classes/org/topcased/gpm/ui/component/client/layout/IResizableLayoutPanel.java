/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

/**
 * IResizableLayoutPanel. Class implementing this interface can deal with three
 * display status : minimized, displayed, maximized. The container will call the
 * methods listed here when a Panel must modify its display
 * 
 * @author jeballar
 */
public interface IResizableLayoutPanel extends ISizeAware {

    /**
     * Method is called when Widget is minimized
     */
    public void doMinimize();

    /**
     * Method is called when Widget is restored
     */
    public void doRestore();

    /**
     * Method is called when Widget is maximized
     */
    public void doMaximize();
}

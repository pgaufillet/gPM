/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/

package org.topcased.gpm.business.values.link;

/**
 * Display message about unsupported protocol
 * 
 * @author phtsaan
 */
public interface IUnsupportedProtocol {
    /**
     * Display a pop up message box for unsupported protocol
     * 
     * @param pMessage
     *            message to be displayed
     */
    public void displayPopupMessage(final String pMessage);
}

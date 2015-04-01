/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * Get the connection information
 * 
 * @author mkargbo
 */
public class GetConnectionInformationAction extends
        AbstractCommandAction<ConnectResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 6762777783880597020L;

    /**
     * Default constructor
     */
    public GetConnectionInformationAction() {
    }

    /**
     * Constructor
     * 
     * @param pProductName
     *            Product name
     */
    public GetConnectionInformationAction(final String pProductName) {
        super(pProductName);
    }
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server;

import java.io.OutputStream;

/**
 * FacadeCommand
 * 
 * @author jlouisy
 */
public interface FacadeCommand {

    /**
     * execute command
     * 
     * @param pOutputStream
     *            the outputstream
     */
    public void execute(OutputStream pOutputStream);

}

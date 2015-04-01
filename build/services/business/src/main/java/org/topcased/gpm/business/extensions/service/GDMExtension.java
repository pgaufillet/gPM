/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

/**
 * Interface to be implemented by extension action java classes
 * 
 * @author llatil
 */
public interface GDMExtension {
    /**
     * Entry point of the extension.
     * 
     * @param pContext
     *            Context containing the parameters passed to the action
     * @return True if the next commands in the command chain must be executed.
     */
    boolean execute(Context pContext);
}

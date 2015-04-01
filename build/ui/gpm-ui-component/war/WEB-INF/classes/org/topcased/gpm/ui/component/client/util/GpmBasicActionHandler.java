/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

/**
 * Handler executing a basic action.
 * 
 * @author tpanuel
 * @param <P>
 *            The type of parameter.
 */
public interface GpmBasicActionHandler<P> {
    /**
     * Execute a basic action.
     * 
     * @param pParam
     *            A parameter.
     */
    public void execute(final P pParam);
}
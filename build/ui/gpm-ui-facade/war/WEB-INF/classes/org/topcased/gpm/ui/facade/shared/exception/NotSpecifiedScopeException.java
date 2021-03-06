/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.exception;

/**
 * NotSpecifiedScopeException
 * 
 * @author nveillet
 */
public class NotSpecifiedScopeException extends FilterExecutionException {

    /** serialVersionUID */
    private static final long serialVersionUID = -8317741825840284061L;

    /**
     * Constructor
     * 
     * @param pFilterId
     *            the filter identifier
     */
    public NotSpecifiedScopeException(String pFilterId) {
        super(pFilterId);
    }
}

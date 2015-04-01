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
 * EmptyResultFieldException
 * 
 * @author nveillet
 */
public class EmptyResultFieldException extends FilterExecutionException {

    /** serialVersionUID */
    private static final long serialVersionUID = 8204218591692353511L;

    /**
     * Constructor
     * 
     * @param pFilterId
     *            the filter identifier
     */
    public EmptyResultFieldException(String pFilterId) {
        super(pFilterId);
    }
}

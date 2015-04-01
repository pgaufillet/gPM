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
 * NotSpecifiedCriteriaException
 * 
 * @author nveillet
 */
public class NotSpecifiedCriteriaException extends FilterExecutionException {

    /** serialVersionUID */
    private static final long serialVersionUID = -5055977381624563329L;

    /**
     * Constructor
     * 
     * @param pFilterId
     *            the filter identifier
     */
    public NotSpecifiedCriteriaException(String pFilterId) {
        super(pFilterId);
    }
}

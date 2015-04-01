/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Base class for all constraint exceptions
 * 
 * @author llatil
 */
public class ConstraintException extends BusinessException {

    private static final long serialVersionUID = 6276135766552384675L;

    /** The constraint that is not satisfy */
    protected String constraint;

    /**
     * Constructs a new constraint exception
     * 
     * @param pMessage
     *            Error message
     */
    public ConstraintException(String pMessage) {
        super(pMessage);
    }

    /**
     * Constructs a constraint exception
     * 
     * @param pMessage
     *            Error message
     * @param pConstraint
     *            Constraint that must be satisfy
     */
    public ConstraintException(String pMessage, String pConstraint) {
        super(pMessage);
        constraint = pConstraint;
    }

    /**
     * Constructs a constraint exception
     * 
     * @param pMessage
     *            Error message
     * @param pConstraint
     *            Constraint that must be satisfy
     * @param pCause
     *            Cause
     */
    public ConstraintException(String pMessage, String pConstraint,
            Throwable pCause) {
        super(pMessage, pCause);
        constraint = pConstraint;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String pConstraint) {
        constraint = pConstraint;
    }
}
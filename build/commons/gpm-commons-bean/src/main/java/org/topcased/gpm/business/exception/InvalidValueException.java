/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;

/**
 * Exception thrown when an invalid value is passed as parameter.
 * 
 * @author llatil
 */
public class InvalidValueException extends BusinessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5696664020261331623L;

    /**
     * Construct a new invalid value exception
     * 
     * @param pValue
     *            Invalid value
     */
    public InvalidValueException(String pValue) {
        super("Invalid value " + pValue, "Invalid value " + pValue);
    }

    public InvalidValueException(String pValue, int pNum) {
        super(pValue, pValue);
    }

    /**
     * Constructs a new InvalidValueException. The exception message can be
     * tailored through the pMessage pattern. The pMessage pattern is processed
     * through a MessageFormat with the invalid value as {0} argument.
     * 
     * @param pInvalidValue
     *            Invalid value
     * @param pMessage
     *            Message pattern (ex: "The value ''{0}'' is invalid")
     * @see MessageFormat
     */
    public InvalidValueException(String pInvalidValue, String pMessage) {
        super(MessageFormat.format(pMessage, pInvalidValue),
                MessageFormat.format(pMessage, pInvalidValue));
    }

}

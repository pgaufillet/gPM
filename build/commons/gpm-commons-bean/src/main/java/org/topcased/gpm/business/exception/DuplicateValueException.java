/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: phtsaan (Atos )
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;

public class DuplicateValueException extends BusinessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 90534299480757457L;

    /**
     * Construct a new duplicate value exception
     * 
     * @param pValue
     *            duplicated value
     */
    public DuplicateValueException(String pValue) {
        super("Duplicate value " + pValue, "Duplicate value " + pValue);
    }

    /**
     * Constructs a new DuplicateValueException. The exception message can be
     * tailored through the pMessage pattern. The pMessage pattern is processed
     * through a MessageFormat with the duplicated value as {0} argument.
     * 
     * @param pDuplicateValue
     *            Duplicated value
     * @param pMessage
     *            Message pattern (ex: "The value ''{0}'' is duplicated")
     * @see MessageFormat
     */
    public DuplicateValueException(String pDuplicateValue, String pMessage) {
        super(MessageFormat.format(pMessage, pDuplicateValue),
                MessageFormat.format(pMessage, pDuplicateValue));
    }
}

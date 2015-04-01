/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.extendedaction;

/**
 * MessageExtendedActionResult
 * 
 * @author nveillet
 */
public class MessageExtendedActionResult extends
        AbstractExecuteExtendedActionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -1583445653177288236L;

    private String message;

    /**
     * Empty constructor for serialization.
     */
    public MessageExtendedActionResult() {
    }

    /**
     * Create MessageExtendedActionResult with values
     * 
     * @param pMessage
     *            the message
     */
    public MessageExtendedActionResult(String pMessage) {
        super();
        message = pMessage;
    }

    /**
     * get message
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}

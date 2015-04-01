/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.extendedaction;

/**
 * UiMessageEAResult
 * 
 * @author nveillet
 */
public class UiMessageEAResult extends AbstractUiExtendedActionResult {

    private String message;

    /**
     * Constructor with values
     * 
     * @param pMessage
     *            the message
     */
    public UiMessageEAResult(String pMessage) {
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

    /**
     * set message
     * 
     * @param pMessage
     *            the message to set
     */
    public void setMessage(String pMessage) {
        message = pMessage;
    }
}

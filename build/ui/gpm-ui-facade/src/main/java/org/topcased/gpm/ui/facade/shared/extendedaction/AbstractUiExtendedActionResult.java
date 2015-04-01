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
 * AbstractUiExtendedActionResult
 * 
 * @author nveillet
 */
public class AbstractUiExtendedActionResult {

    private String resultMessage;
    private Boolean refreshNeeded;

    /**
     * Constructor
     */
    protected AbstractUiExtendedActionResult() {
    }

    /**
     * get resultMessage
     * 
     * @return the resultMessage
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * set resultMessage
     * 
     * @param pResultMessage
     *            the resultMessage to set
     */
    public void setResultMessage(String pResultMessage) {
        resultMessage = pResultMessage;
    }
    
    /**
     * is refresh needed ?
     * 
     * @return True if refresh needed
     */
    public Boolean getRefreshNeeded() {
        return refreshNeeded;
    }
    
    /**
     * set refresh needed
     * 
     * @param pRefreshNeeded true if refresh needed
     */
    public void setRefreshNeeded(Boolean pRefreshNeeded) {
    	refreshNeeded = pRefreshNeeded;
    }
}

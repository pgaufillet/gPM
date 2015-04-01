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

import net.customware.gwt.dispatch.shared.Result;

/**
 * AbstractExecuteExtendedActionResult
 * 
 * @author nveillet
 */
public abstract class AbstractExecuteExtendedActionResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2103454464545329996L;

    private String resultMessage;
    private Boolean refreshNeeded;

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

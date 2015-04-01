/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.List;

/**
 * SelectProcessResult
 * 
 * @author nveillet
 */
public class SelectProcessResult extends AbstractConnectionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5158405554405974030L;

    private List<String> processNames;

    /**
     * Empty constructor for serialization.
     */
    public SelectProcessResult() {
    }

    /**
     * Create SelectProcessResult with values
     * 
     * @param pProcessNames
     *            the available process names
     */
    public SelectProcessResult(List<String> pProcessNames) {
        processNames = pProcessNames;
    }

    /**
     * get process names
     * 
     * @return the process names
     */
    public List<String> getProcessNames() {
        return processNames;
    }

}

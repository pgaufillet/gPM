/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.dictionary;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * Delete Environment Action
 * 
 * @author jlouisy
 */
public class DeleteEnvironmentAction extends
        AbstractCommandAction<DeleteEnvironmentResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3598666658319625943L;

    private String environmentName;

    /**
     * create action
     */
    public DeleteEnvironmentAction() {
    }

    /**
     * create action with values
     * 
     * @param pEnvironmentName
     *            environment name.
     */
    public DeleteEnvironmentAction(String pEnvironmentName) {
        super();
        environmentName = pEnvironmentName;
    }

    /**
     * Get environment name.
     * 
     * @return environment name.
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * Set environment name.
     * 
     * @param pEnvironmentName
     *            environment name.
     */
    public void setEnvironmentName(String pEnvironmentName) {
        environmentName = pEnvironmentName;
    }

}
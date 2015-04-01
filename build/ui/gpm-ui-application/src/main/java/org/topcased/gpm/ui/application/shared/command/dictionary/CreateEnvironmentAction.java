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
 * Create Environment Action
 * 
 * @author jlouisy
 */
public class CreateEnvironmentAction extends
        AbstractCommandAction<CreateEnvironmentResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3598666658319625943L;

    private String environmentName;

    private boolean isPublic;

    /**
     * create action
     */
    public CreateEnvironmentAction() {
    }

    /**
     * create action with values
     * 
     * @param pEnvironmentName
     *            environement name.
     * @param pIsPublic
     *            environement visibility.
     */
    public CreateEnvironmentAction(String pEnvironmentName, boolean pIsPublic) {
        super();
        environmentName = pEnvironmentName;
        isPublic = pIsPublic;
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

    /**
     * Get environment visibility.
     * 
     * @return environment visibility.
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Set environment visibility.
     * 
     * @param pIsPublic
     *            environment visibility.
     */
    public void setIsPublic(boolean pIsPublic) {
        isPublic = pIsPublic;
    }

}
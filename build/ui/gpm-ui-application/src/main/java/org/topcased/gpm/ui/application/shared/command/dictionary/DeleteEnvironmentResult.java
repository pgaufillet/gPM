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

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

/**
 * Delete Environment Result
 * 
 * @author jlouisy
 */
public class DeleteEnvironmentResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private List<String> environmentList;

    /**
     * Create DeleteEnvironmentResult
     */
    public DeleteEnvironmentResult() {
    }

    /**
     * Create DeleteEnvironmentResult with values
     * 
     * @param pEnvironmentList
     *            Environment list.
     */
    public DeleteEnvironmentResult(List<String> pEnvironmentList) {
        super();
        environmentList = pEnvironmentList;
    }

    /**
     *Get environment list.
     * 
     * @return environment list.
     */
    public List<String> getEnvironmentList() {
        return environmentList;
    }

}

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

import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterResult;

/**
 * FilterExtendedActionResult
 * 
 * @author nveillet
 */
public class FilterExtendedActionResult extends
        AbstractExecuteExtendedActionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -6545306690917579564L;

    private ExecuteTableFilterResult executeTableFilterResult;

    /**
     * Empty constructor for serialization.
     */
    public FilterExtendedActionResult() {
    }

    /**
     * Constructor
     * 
     * @param pExecuteTableFilterResult
     *            the executeTableFilterResult
     */
    public FilterExtendedActionResult(
            ExecuteTableFilterResult pExecuteTableFilterResult) {
        super();
        executeTableFilterResult = pExecuteTableFilterResult;
    }

    /**
     * get executeTableFilterResult
     * 
     * @return the executeTableFilterResult
     */
    public ExecuteTableFilterResult getExecuteTableFilterResult() {
        return executeTableFilterResult;
    }

    /**
     * set executeTableFilterResult
     * 
     * @param pExecuteTableFilterResult
     *            the executeTableFilterResult to set
     */
    public void setExecuteTableFilterResult(
            ExecuteTableFilterResult pExecuteTableFilterResult) {
        executeTableFilterResult = pExecuteTableFilterResult;
    }

}

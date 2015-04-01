/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter;

import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResult;

/**
 * ExecuteTreeFilterResult
 * 
 * @author nveillet
 */
public class ExecuteTreeFilterResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 5107390484110251436L;

    private UiFilterTreeResult filterResult;

    /**
     * Empty constructor for serialization.
     */
    public ExecuteTreeFilterResult() {
    }

    /**
     * Create ExecuteTableFilterResult with values
     * 
     * @param pFilterResult
     *            The filter result
     */
    public ExecuteTreeFilterResult(UiFilterTreeResult pFilterResult) {
        filterResult = pFilterResult;
    }

    /**
     * get filter result
     * 
     * @return the filter result
     */
    public UiFilterTreeResult getFilterResult() {
        return filterResult;
    }

}

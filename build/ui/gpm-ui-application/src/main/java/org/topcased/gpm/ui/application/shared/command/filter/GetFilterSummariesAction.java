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

import org.topcased.gpm.business.util.FilterType;

/**
 * GetFilterSummariesAction
 * 
 * @author nveillet
 */
public class GetFilterSummariesAction extends
        AbstractCommandFilterAction<GetFilterSummariesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3589446845196030000L;

    /**
     * create action
     */
    public GetFilterSummariesAction() {
    }

    /**
     * Constructor
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     */
    public GetFilterSummariesAction(String pProductName, FilterType pFilterType) {
        super(pProductName, pFilterType);
    }
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterAction;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * SaveFilterAction
 * 
 * @author nveillet
 */
public class SaveFilterAction extends
        AbstractCommandFilterAction<SaveFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3171564392742547427L;

    private UiFilter filter;

    /**
     * create action
     */
    public SaveFilterAction() {
    }

    /**
     * create action with product and filter
     * 
     * @param pProductName
     *            the product name
     * @param pFilter
     *            the filter
     */
    public SaveFilterAction(String pProductName, UiFilter pFilter) {
        super(pProductName, pFilter.getFilterType());
        filter = pFilter;
    }

    /**
     * get filter
     * 
     * @return the filter
     */
    public UiFilter getFilter() {
        return filter;
    }

}

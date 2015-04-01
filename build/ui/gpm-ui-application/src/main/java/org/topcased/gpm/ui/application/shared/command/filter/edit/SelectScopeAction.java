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

import org.topcased.gpm.business.util.FilterType;

/**
 * SelectScopeAction
 * 
 * @author nveillet
 */
public class SelectScopeAction extends
        AbstractCommandEditFilterAction<SelectScopeResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -146163693501757629L;

    /**
     * create action
     */
    public SelectScopeAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     */
    public SelectScopeAction(String pProductName) {
        //Only available for sheets
        super(pProductName, FilterType.SHEET);
    }

    /**
     * create action with product and filter identifier
     * 
     * @param pProductName
     *            the product name
     * @param pFilterId
     *            the filter identifier
     */
    public SelectScopeAction(String pProductName, String pFilterId) {
        //Only available for sheets
        super(pProductName, FilterType.SHEET, pFilterId);
    }
}

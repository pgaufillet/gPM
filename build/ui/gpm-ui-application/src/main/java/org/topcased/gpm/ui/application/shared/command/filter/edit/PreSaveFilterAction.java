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
 * PreSaveFilterAction
 * 
 * @author nveillet
 */
public class PreSaveFilterAction extends
        AbstractCommandEditFilterAction<PreSaveFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7117117820554967195L;

    /**
     * create action
     */
    public PreSaveFilterAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     */
    public PreSaveFilterAction(String pProductName, FilterType pFilterType) {
        super(pProductName, pFilterType);
    }

    /**
     * create action with product and filter identifier
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            the filter identifier
     */
    public PreSaveFilterAction(String pProductName, FilterType pFilterType,
            String pFilterId) {
        super(pProductName, pFilterType, pFilterId);
    }
}

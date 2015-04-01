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
 * SelectContainerAction
 * 
 * @author nveillet
 */
public class SelectContainerAction extends
        AbstractCommandEditFilterAction<SelectContainerResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7722413763311380605L;

    /**
     * create action
     */
    public SelectContainerAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type.
     */
    public SelectContainerAction(String pProductName, FilterType pFilterType) {
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
    public SelectContainerAction(String pProductName, FilterType pFilterType,
            String pFilterId) {
        super(pProductName, pFilterType, pFilterId);
    }
}

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

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterAction;

/**
 * AbstractCommandEditFilterAction
 * 
 * @param <R>
 *            The {@link Result} implementation.
 * @author nveillet
 */
public class AbstractCommandEditFilterAction<R extends Result> extends
        AbstractCommandFilterAction<R> {

    /** serialVersionUID */
    private static final long serialVersionUID = 564003230625020468L;

    private String filterId;

    /**
     * create action
     */
    protected AbstractCommandEditFilterAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     */
    protected AbstractCommandEditFilterAction(String pProductName,
            FilterType pFilterType) {
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
    protected AbstractCommandEditFilterAction(String pProductName,
            FilterType pFilterType, String pFilterId) {
        super(pProductName, pFilterType);
        filterId = pFilterId;
    }

    /**
     * get filterId
     * 
     * @return the filterId
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * set filterId
     * 
     * @param pFilterId
     *            the filterId to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }
}

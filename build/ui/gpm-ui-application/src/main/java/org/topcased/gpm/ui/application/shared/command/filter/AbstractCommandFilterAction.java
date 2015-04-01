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

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * <p>
 * Abstract super-class for {@link Action} implementations of filter commands.
 * </p>
 * 
 * @param <R>
 *            The {@link Result} implementation.
 * @author nveillet
 */
public class AbstractCommandFilterAction<R extends Result> extends
        AbstractCommandAction<R> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3618707682535045356L;

    private FilterType filterType;

    /**
     * create action
     */
    protected AbstractCommandFilterAction() {
    }

    /**
     * create filter action with product name and filter type
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     */
    protected AbstractCommandFilterAction(String pProductName,
            FilterType pFilterType) {
        super(pProductName);
        filterType = pFilterType;
    }

    /**
     * get filter type
     * 
     * @return the filter type
     */
    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * set filter type
     * 
     * @param pFilterType
     *            the filter type to set
     */
    public void setFilterType(FilterType pFilterType) {
        filterType = pFilterType;
    }
}

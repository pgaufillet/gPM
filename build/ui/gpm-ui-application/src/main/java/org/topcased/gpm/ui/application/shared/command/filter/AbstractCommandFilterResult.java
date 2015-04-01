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

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.FilterType;

/**
 * AbstractCommandFilterResult
 * 
 * @author nveillet
 */
public abstract class AbstractCommandFilterResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -7413442719621693589L;

    private String filterId;

    private FilterType filterType;

    /**
     * Empty constructor for serialization.
     */
    public AbstractCommandFilterResult() {
    }

    /**
     * Create AbstractCommandFilterResult with values
     * 
     * @param pFilterId
     *            Filter Id.
     * @param pFilterType
     *            Filter type.
     */
    public AbstractCommandFilterResult(String pFilterId, FilterType pFilterType) {
        filterId = pFilterId;
        filterType = pFilterType;
    }

    /**
     * get filter Id.
     * 
     * @return the filter Id
     */
    public String getFilterId() {
        return filterId;
    }

    public FilterType getFilterType() {
        return filterType;
    }
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util.search;

import java.io.Serializable;
import java.util.List;

/**
 * A filter result corresponding to an entry.
 * 
 * @author tpanuel
 */
public class FilterResult implements Serializable {
    private static final long serialVersionUID = 6042450232438482917L;

    private FilterResultId filterResultId;

    private List<String> values;

    /**
     * Empty constructor for serialization.
     */
    public FilterResult() {
    }

    /**
     * Constructor initializing all fields.
     * 
     * @param pFilterResultId
     *            The entry id.
     * @param pValues
     *            The values.
     */
    public FilterResult(final FilterResultId pFilterResultId,
            final List<String> pValues) {
        filterResultId = pFilterResultId;
        values = pValues;
    }

    /**
     * Get the entry id.
     * 
     * @return The entry id.
     */
    public FilterResultId getFilterResultId() {
        return filterResultId;
    }

    /**
     * Get the values.
     * 
     * @return The values.
     */
    public List<String> getValues() {
        return values;
    }
}
/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import org.topcased.gpm.business.search.criterias.impl.GPMQuery;

/**
 * IQueryGenerator implementations generates a SQL query.
 * 
 *@author mkargbo
 */
public interface IQueryGenerator {

    /**
     * Generate a SQL query
     * 
     * @param pFilterQueryConfigurator
     *            Filter configurator
     * @return Generated query (SQL)
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator);

    /**
     * Generate a SQL query.
     * <p>
     * Fill the given GPMQuery object.
     * 
     * @param pQuery
     *            Query to fill.
     * @param pFilterQueryConfigurator
     *            Filter configurator
     */
    public void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator);
}
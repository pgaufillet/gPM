/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;

/**
 * IFieldQueryGenerator implementations generates a SQL query for criteria,
 * results and sorting results
 * 
 * @author mkargbo
 */
public interface IFieldQueryGenerator {

    /**
     * Generates query for results
     * 
     * @param pQuery
     *            Query to fill
     */
    public void generateResult(GPMQuery pQuery);

    /**
     * Generates query for criterion
     * 
     * @param pQuery
     *            Query to fill
     * @param pCriteriaFieldData
     *            Field criterion to analyze for query generation
     * @param pFilterQueryConfigurator
     *            Filter configurator
     * @return condition expression. (ie. field1 <> 'a')
     */
    public String generateCriterion(GPMQuery pQuery,
            CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator);

    /**
     * Generates query for sorting results
     * 
     * @param pQuery
     *            Query to fill
     * @param pOrder
     *            Order operator of the result.
     */
    public void generateOrder(GPMQuery pQuery, String pOrder);

    /**
     * Generates query's part for results for sub-query using in Union
     * (multi-container)
     * 
     * @param pQuery
     *            Query to fill
     */
    public void generateUnionResult(final GPMQuery pQuery);

    /**
     * Generates query's part for order for sub-query using in Union
     * (multi-container)
     * 
     * @param pQuery
     *            Query to fill
     * @param pOrder
     *            Order operator of the result
     */
    public void generateUnionOrder(final GPMQuery pQuery, final String pOrder);

    /**
     * Generate sort query part for multi-container filter.
     * 
     * @param pQuery
     *            Query to fill
     */
    public void generateMultiContainerSort(final GPMQuery pQuery);

    /**
     * Generates result query part for multi-container filter.
     * 
     * @param pQuery
     *            Query to fill
     */
    public void generateMultiContainerResult(final GPMQuery pQuery);

    /**
     * Set the criterion object.
     * 
     * @param pCriteriaFieldData
     *            Criterion to set
     */
    public void setCriteriaFieldData(CriteriaFieldData pCriteriaFieldData);
}
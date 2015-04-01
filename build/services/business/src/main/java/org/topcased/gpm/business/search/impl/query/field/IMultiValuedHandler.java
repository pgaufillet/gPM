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
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * IMultiValuedHandler implementations handle multi-valued fields as criterion
 * and result.
 * 
 * @author mkargbo
 */
public interface IMultiValuedHandler {

    /**
     * Generates query for result multi-valued field.
     * 
     * @param pQuery
     *            Query to fill
     * @param pUsableFieldData
     *            Usable field to analyze for query generation
     */
    public void handleMultiValuedResult(GPMQuery pQuery,
            UsableFieldData pUsableFieldData);

    /**
     * Generates query for criterion multi-valued field.
     * 
     * @param pQuery
     *            Query to fill
     * @param pCriteriaFieldData
     *            Usable field to analyze for query generation
     * @param pFilterQueryConfigurator
     *            Filter configurator
     * @return condition expression. (ie. field1 <> 'a')
     */
    public String handleMultiValuedCriterion(GPMQuery pQuery,
            CriteriaFieldData pCriteriaFieldData,
            FilterQueryConfigurator pFilterQueryConfigurator);

    /**
     * Generates SELECT query fragment for multi-valued field
     * 
     * @param pAlias
     *            Container alias
     * @return SELECT query fragment
     */
    public String getMultiValuedSelectClause(String pAlias);

    /**
     * Generates FROM query fragment for multi-valued field
     * 
     * @param pAlias
     *            Container alias
     * @return FROM query fragment
     */
    public String getMultiValuedFromClause(String pAlias);

    /**
     * Generates WHERE query fragment for multi-valued field
     * 
     * @param pAlias
     *            Container alias
     * @return WHERE query fragment
     */
    public String getMultiValuedWhereClause(String pAlias);

    /**
     * Generates ORDER_BY query fragment for multi-valued field
     * 
     * @param pAlias
     *            Container alias
     * @return ORDER_BY query fragment
     */
    public String getMultiValuedOrderByClause(String pAlias);
}

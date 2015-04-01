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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.QueryParameter;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;

/**
 * Defines clause query methods (SELECT, FROM, WHERE and ORDERBY clauses)
 * 
 * @author mkargbo
 */
public abstract class AbstractFieldQueryGenerator implements
        IFieldQueryGenerator {

    protected final UsableFieldData usableFieldData;

    protected CriteriaFieldData criteriaFieldData;

    protected String containerAlias;

    protected List<QueryParameter> parameters = new ArrayList<QueryParameter>();

    protected Map<SqlFunction, String> sqlFunctions =
            new HashMap<SqlFunction, String>();

    /**
     * Create an AbstractFieldQueryGenerator.
     * 
     * @param pUsableFieldData
     *            The usable field data.
     */
    public AbstractFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        usableFieldData = pUsableFieldData;
    }

    /**
     * Generate FROM clause query
     * 
     * @return FROM clause query fragment
     */
    protected String getFromClause() {
        throw new MethodNotImplementedException(
                "No FROM clause for this field.");
    }

    /**
     * Generate SELECT clause query
     * 
     * @return SELECT clause query fragment
     */
    protected abstract String getSelectClause();

    /**
     * Generate WHERE clause query
     * 
     * @return WHERE clause query fragment
     */
    protected abstract String getWhereClause();

    /**
     * Generate ORDER_BY clause query
     * 
     * @return ORDER_BY clause query fragment
     */
    protected abstract String getOrderByClause();

    public void setCriteriaFieldData(CriteriaFieldData pCriteriaFieldData) {
        criteriaFieldData = pCriteriaFieldData;
    }

    /**
     * Create the AS clause.
     * 
     * @return The AS clause.
     */
    protected String getAsClause() {
        return " AS "
                + DynamicObjectNamesUtils.getInstance().getDynamicColumnName(
                        usableFieldData.getFieldId());
    }

    /**
     * Check if a criteria field is filled
     * 
     * @return boolean
     */
    protected boolean isCriteriaFieldDataEmpty() {
        return (criteriaFieldData.getScalarValueData() == null)
                || (StringUtils.isEmpty(((StringValueData) criteriaFieldData.getScalarValueData()).getValue()));
    }

    /**
     * Compute the 'where' clause with 'is null' condition if the operator is
     * 'negative'
     * 
     * @param pContainerAlias
     *            The field name of the criterion
     * @param pWhereClause
     *            The where clause
     * @return The complete where clause
     */
    protected String computeNullWhereClause(String pContainerAlias,
            String pWhereClause) {
        // get the value of the parameter
        Object lValue = criteriaFieldData.getScalarValueData().getValue();
        // Add null condition if is an 'negative' operator
        if (criteriaFieldData.getOperator().equals(Operators.NEQ)
                || criteriaFieldData.getOperator().equals(Operators.NOT_LIKE)) {

            if (lValue instanceof String) {
                if (!((String) lValue).trim().isEmpty()) {
                    return "((" + pWhereClause + ") " + Operators.OR + " "
                            + pContainerAlias + " is null)";
                }
                else {
                    // special case if parameter is a blank string
                    return "((" + pWhereClause + ") " + Operators.AND + " "
                            + pContainerAlias + " is not null)";
                }
            }
            else {
                return "((" + pWhereClause + ") " + Operators.OR + " "
                        + pContainerAlias + " is null)";
            }
        }
        else if (criteriaFieldData.getOperator().equals(Operators.EQ)
                || criteriaFieldData.getOperator().equals(Operators.LIKE)) {
            // And add null condition if is an 'positive' operator
            // only for empty or blank strings
            if (lValue instanceof String) {
                if (((String) lValue).trim().isEmpty()) {
                    return "((" + pWhereClause + ") " + Operators.OR + " "
                            + pContainerAlias + " is null)";
                }
            }
        }
        return pWhereClause;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateUnionResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateUnionResult(final GPMQuery pQuery) {
        final String lSelectElement;

        containerAlias = pQuery.getAlias();
        if (pQuery.hasAlias(usableFieldData.getId())) {
            lSelectElement =
                    containerAlias + "."
                            + pQuery.getFieldAlias(usableFieldData.getId());
        }
        else {
            lSelectElement = getSelectClause();
        }
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateUnionOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateUnionOrder(GPMQuery pQuery, String pOrder) {
        final String lOrderBy;

        containerAlias = pQuery.getAlias();
        if (pQuery.hasAlias(usableFieldData.getId())) {
            lOrderBy =
                    containerAlias + "."
                            + pQuery.getFieldAlias(usableFieldData.getId());
        }
        else {
            lOrderBy = getOrderByClause();
        }
        if (!pQuery.isSelectClauseContains(lOrderBy)) {
            final String lSelectElement;

            if (pQuery.hasAlias(usableFieldData.getId())) {
                lSelectElement =
                        containerAlias + "."
                                + pQuery.getFieldAlias(usableFieldData.getId());
            }
            else {
                lSelectElement = getSelectClause();
            }
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        pQuery.addOrderByElement(lOrderBy, pOrder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateMultiContainerSort(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateMultiContainerSort(final GPMQuery pQuery) {
        generateResult(pQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateMultiContainerResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateMultiContainerResult(final GPMQuery pQuery) {
        generateResult(pQuery);
    }

    /**
     * Get the container alias
     * 
     * @return the container alias
     */
    public String getContainerAlias() {
        return containerAlias;
    }

    /**
     * Set the container alias
     * 
     * @param pContainerAlias
     *            the container alias to set
     */
    public void setContainerAlias(final String pContainerAlias) {
        this.containerAlias = pContainerAlias;
    }

    /**
     * Get the container column name as "<code>pAlias</code>.
     * <code>dynamicColumnName</code>"
     * 
     * @param pAlias
     *            the alias used
     * @return the column name
     */
    protected String getContainerColumnName(String pAlias) {
        return pAlias + "." + getDynamicColumnName();
    }

    /**
     * Get the dynamic column name associated with the usableFieldData field id.
     * 
     * @return the column name
     */
    protected String getDynamicColumnName() {
        return DynamicObjectNamesUtils.getInstance().getDynamicColumnName(
                usableFieldData.getFieldId());
    }

}
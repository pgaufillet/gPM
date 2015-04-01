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

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Attached</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class AttachedFieldQueryGenerator extends
        AbstractMultiValuedFieldQueryGenerator {

    /** Attribute to use for query (SELECT and WHERE) */
    private static final String RESULT_ATTRIBUTE = ".name";

    private static final String ATTACHED_FIELD_VALUE_TABLE_NAME =
            "ATTACHED_FIELD_VALUE";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * AttachedFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public AttachedFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    private String getFieldName(final String pAlias) {
        return pAlias + RESULT_ATTRIBUTE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    @Override
    public String getSelectClause() {
        return getFieldName(getContainerAlias()) + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedSelectClause()
     */
    public String getMultiValuedSelectClause(final String pAlias) {
        return getFieldName(pAlias) + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getFromClause()
     */
    @Override
    protected String getFromClause() {
        return ATTACHED_FIELD_VALUE_TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    @Override
    public String getWhereClause() {
        return getMultiValuedWhereClause(getContainerAlias());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        String lFieldName = getFieldName(pAlias);
        String lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        final String lContainerColumnName = lFieldName;

        if (criteriaFieldData.getCaseSensitive() != null
                && !criteriaFieldData.getCaseSensitive()) {
            lFieldName =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + '('
                            + lFieldName + ')';
            lScalarValue =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + '('
                            + lScalarValue + ')';
        }

        // Create clause
        final String lClause =
                lFieldName + ' ' + criteriaFieldData.getOperator() + ' '
                        + lScalarValue;

        // For an empty criteria : add clause for null test
        if (isCriteriaFieldDataEmpty()) {
            if (Operators.EQ.equals(criteriaFieldData.getOperator())
                    || (Operators.LIKE.equals(criteriaFieldData.getOperator()))) {
                return "(" + lClause + ") " + Operators.OR + " " + lFieldName
                        + " is null)";
            }
            else if (Operators.NEQ.equals(criteriaFieldData.getOperator())
                    || (Operators.NOT_LIKE.equals(criteriaFieldData.getOperator()))) {
                return "(" + lClause + ") " + Operators.OR + " " + lFieldName
                        + " is not null)";
            }
        }

        return computeNullWhereClause(lContainerColumnName, lClause);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.query.fiekld.IFieldQueryGenerator#generateCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData)
     */
    public String generateCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        criteriaFieldData = pCriteriaFieldData;
        Object lScalarValue = "";
        if (!isCriteriaFieldDataEmpty()) {
            lScalarValue = criteriaFieldData.getScalarValueData().getValue();
        }
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(lScalarValue,
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);
        sqlFunctions = pFilterQueryConfigurator.getSqlFunctions();
        if (usableFieldData.getMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            return getMultiValuedWhereClause(generateJoin(pQuery,
                    pQuery.getAlias()));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(final GPMQuery pQuery) {
        if (usableFieldData.getMultivalued()) {
            handleMultiValuedResult(pQuery, usableFieldData);
        }
        else {
            String lAlias = StringUtils.EMPTY;
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                lAlias = pQuery.getMappedAlias(usableFieldData.getId());
            }
            else {
                lAlias = generateJoin(pQuery, pQuery.getAlias());
                pQuery.putMappedAlias(usableFieldData.getId(), lAlias);
            }
            setContainerAlias(lAlias);
            appendElementToSelectClauseAndGroupByClause(pQuery,
                    getSelectClause(), getSelectClause());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(final GPMQuery pQuery, final String pOrder) {
        if (usableFieldData.getMultivalued()) {
            handleMultiValuedOrder(pQuery, pOrder);
        }
        else {
            String lAlias = StringUtils.EMPTY;
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                lAlias = pQuery.getMappedAlias(usableFieldData.getId());
            }
            else {
                lAlias = generateJoin(pQuery, pQuery.getAlias());
                pQuery.putMappedAlias(usableFieldData.getId(), lAlias);
            }
            setContainerAlias(lAlias);
            appendElementToSelectClauseAndGroupByClause(pQuery,
                    getSelectClause(), getSelectClause());
        }
        pQuery.addOrderByElement(getOrderByClause(), pOrder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        if (usableFieldData.getMultivalued()) {
            return getDynamicColumnName();
        }
        else {
            return getContainerAlias() + RESULT_ATTRIBUTE;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValued(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.service.UsableFieldData)
     */
    public void handleMultiValuedResult(final GPMQuery pQuery,
            final UsableFieldData pUsableFieldData) {

        // Get the alias
        final String lAlias = getAlias(pQuery);

        // Update the select and group by clause
        appendElementToSelectClauseAndGroupByClause(pQuery,
                getMultiValuedSelectClause(lAlias), pQuery.getAlias() + ".id");
    }

    /**
     * Generates query for order multi-valued field
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValued(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.service.UsableFieldData)
     */
    protected void handleMultiValuedOrder(final GPMQuery pQuery,
            final String pOrder) {

        // Get the alias
        String lAlias = getAlias(pQuery);

        // Update the select and group by clause
        appendElementToSelectClauseAndGroupByClause(pQuery,
                getMultiValuedSelectClause(lAlias), pQuery.getAlias() + ".id");

        //Set the containerAlias in order to use it to generate order clause
        setContainerAlias(lAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValuedCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.service.UsableFieldData,
     *      FilterQueryConfigurator)
     */
    public String handleMultiValuedCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {

        // Get the alias
        final String lAlias = getAlias(pQuery);

        return getMultiValuedWhereClause(lAlias);
    }

    /**
     * Get the alias of the query for the attachedField field.
     * <p>
     * Handle multivalued.
     * 
     * @param pQuery
     *            the query
     * @return the alias
     */
    private String getAlias(final GPMQuery pQuery) {
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            lAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }
        else {
            //Generate multivalued virtual table.
            lAlias =
                    pQuery.generateAlias(usableFieldData.getId()
                            + GPMQuery.AliasType.FIELD,
                            GPMQuery.AliasType.FIELD);
            // Add the element in FROM clause
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClause(pQuery.getAlias()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + ".parent_id");
            //Add join ATTACHED_FIELD table to MULTI_VALUED virtual table
            lAlias = generateJoin(pQuery, lAlias);
            // Save the alias in the map.
            pQuery.putMappedAlias(usableFieldData.getId(), lAlias);
        }

        return lAlias;
    }

    /**
     * Generate join query between ATTACHED_FIELD table and container table.
     * <p>
     * the container alias become the ATTACHED_FIELD table alias.
     * 
     * @param pQuery
     *            Query to fill
     */
    private String generateJoin(final GPMQuery pQuery, final String pAlias) {
        final String lAlias =
                pQuery.generateAlias(usableFieldData.getId(),
                        AliasType.ATTACHED_FIELD);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, getFromClause(),
                lAlias, "(" + getContainerColumnName(pAlias) + " = " + lAlias
                        + ".id)");

        return lAlias;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateUnionResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generateUnionResult(final GPMQuery pQuery) {
        // Get the orderElement
        final String lOrderElement = getContainerColumnName(pQuery.getAlias());
        // Add the orderElement to the 'select' and 'groupBy' clause
        appendElementToSelectClauseAndGroupByClause(pQuery, lOrderElement,
                lOrderElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateUnionOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    @Override
    public void generateUnionOrder(final GPMQuery pQuery, final String pOrder) {

        // Get the orderElement
        final String lOrderElement = getContainerColumnName(pQuery.getAlias());

        // Add the orderElement to the 'select' and 'groupBy' clause
        appendElementToSelectClauseAndGroupByClause(pQuery, lOrderElement,
                lOrderElement);

        // Add the orderElement to the 'orderBy' clause 
        pQuery.addOrderByElement(lOrderElement, pOrder);
    }

    /**
     * Append an element to the 'select' and 'groupBy' clause.
     * 
     * @param pQuery
     *            the query
     * @param pSelectElement
     *            the element to add to the select clause
     * @param lGroupByElement
     *            the element to add to the groupBy clause
     */
    private void appendElementToSelectClauseAndGroupByClause(
            final GPMQuery pQuery, final String pSelectElement,
            final String lGroupByElement) {
        if (!pQuery.isSelectClauseContains(pSelectElement)) {
            pQuery.appendToSelectClause(pSelectElement);
            pQuery.appendToGroupByClause(lGroupByElement);
        }
    }

}

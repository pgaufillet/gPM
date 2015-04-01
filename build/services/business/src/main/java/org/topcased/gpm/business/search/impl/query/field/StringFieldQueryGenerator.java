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
import org.topcased.gpm.domain.dynamic.util.ColumnSizeUtils;
import org.topcased.gpm.domain.util.driver.DriverSQL;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Generator for <b>String</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class StringFieldQueryGenerator extends
        AbstractMultiValuedFieldQueryGenerator {
    private static final String SCALAR_VALUE_TABLE_NAME = "SCALAR_VALUE";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    private final DriverSQL driverSQL;

    /**
     * StringFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public StringFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
        driverSQL = GpmSessionFactory.getInstance().getDriverSQL();
    }

    private boolean isInfiniteString() {
        return ColumnSizeUtils.getInstance().isInfineStringField(
                usableFieldData.getFieldSize());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    @Override
    public String getSelectClause() {
        return getMultiValuedSelectClause(containerAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedSelectClause()
     */
    public String getMultiValuedSelectClause(final String pAlias) {
        if (isInfiniteString()) {
            return driverSQL.getLargeStringForSelectClause(pAlias);
        }
        else {
            return super.getMultiValuedSelectClause(pAlias);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getFromClause()
     */
    @Override
    protected String getFromClause() {
        if (isInfiniteString()) {
            return SCALAR_VALUE_TABLE_NAME;
        }
        else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    @Override
    public String getWhereClause() {
        return getMultiValuedWhereClause(containerAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        String lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        String lFieldName;

        if (isInfiniteString()) {
            lFieldName = driverSQL.getLargeStringForWhereClause(pAlias);
        }
        else {
            lFieldName = getContainerColumnName(pAlias);
        }

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
                return "((" + lClause + ") " + Operators.OR + " " + lFieldName
                        + " is null)";
            }
            else if (Operators.NEQ.equals(criteriaFieldData.getOperator())
                    || (Operators.NOT_LIKE.equals(criteriaFieldData.getOperator()))) {
                return "((" + lClause + ") " + Operators.OR + " " + lFieldName
                        + " is not null)";
            }
        }

        return computeNullWhereClause(lContainerColumnName, lClause);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData)
     */
    public String generateCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        criteriaFieldData = pCriteriaFieldData;
        sqlFunctions = pFilterQueryConfigurator.getSqlFunctions();

        Object lScalarValue = "";
        if (!isCriteriaFieldDataEmpty()) {
            lScalarValue = criteriaFieldData.getScalarValueData().getValue();
        }
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(lScalarValue,
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);
        if (usableFieldData.getMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            containerAlias = pQuery.getAlias();
            pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            criteriaFieldData = pCriteriaFieldData;
            containerAlias = handleScalarValue(pQuery, containerAlias);
            return getWhereClause();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        if (usableFieldData.getMultivalued()) {
            handleMultiValuedResult(pQuery, usableFieldData);
        }
        else {
            containerAlias = handleScalarValue(pQuery, containerAlias);
            pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            final String lSelectElement = getSelectClause();
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
    }

    /**
     * Generates scalar value queryL
     * 
     * @param pQuery
     *            Query to fill
     * @param pAlias
     *            New alias value.
     * @return Updated alias value.
     */
    private String handleScalarValue(final GPMQuery pQuery, final String pAlias) {
        final String lFromClause = getFromClause();

        if (StringUtils.isNotBlank(lFromClause)) {
            if (pQuery.isAlreadyMapped(SCALAR_VALUE_TABLE_NAME
                    + usableFieldData.getId())) {
                return pQuery.getMappedAlias(SCALAR_VALUE_TABLE_NAME
                        + usableFieldData.getId());
            }
            else {
                final String lScalarAlias =
                        pQuery.getOrGenerateAlias(SCALAR_VALUE_TABLE_NAME
                                + usableFieldData.getId(),
                                AliasType.SCALAR_VALUE);
                final String lField = getDynamicColumnName();

                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, lFromClause,
                        lScalarAlias, pAlias + "." + lField + " = "
                                + lScalarAlias + ".id");

                return lScalarAlias;
            }
        }

        return pAlias;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        if (isInfiniteString()) {
            return driverSQL.getLargeStringForOrderByClause(containerAlias);
        }
        else {
            return getContainerColumnName(containerAlias);
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
            //Generate the new containerAlias
            handleMultiValuedOrder(pQuery, pOrder);
        }
        else {
            containerAlias = pQuery.getAlias();
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
                containerAlias = handleScalarValue(pQuery, containerAlias);
            }
            else {
                containerAlias = handleScalarValue(pQuery, containerAlias);
                pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            }
        }
        final String lSelectElement = getSelectClause();
        if (!pQuery.isSelectClauseContains(lSelectElement)) {
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        pQuery.addOrderByElement(getOrderByClause(), pOrder);

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
        //Add to from clause multi-valued class
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            lAlias = pQuery.getMappedAlias(usableFieldData.getId());

        }
        else {
            lAlias =
                    pQuery.generateAlias(usableFieldData.getId(),
                            GPMQuery.AliasType.FIELD);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClause(pQuery.getAlias()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + ".parent_id");
        }

        //Add join to container

        lAlias = handleScalarValue(pQuery, lAlias);
        pQuery.appendToSelectClause(getMultiValuedSelectClause(lAlias));
        pQuery.appendToGroupByClause(getMultiValuedSelectClause(lAlias));
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
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            lAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }
        else {
            lAlias =
                    pQuery.generateAlias(usableFieldData.getId(),
                            GPMQuery.AliasType.FIELD);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClause(pQuery.getAlias()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + ".parent_id");

            pQuery.putMappedAlias(usableFieldData.getId(), lAlias);
        }

        lAlias = handleScalarValue(pQuery, lAlias);
        //Add join to container

        return getMultiValuedWhereClause(lAlias);
    }
}
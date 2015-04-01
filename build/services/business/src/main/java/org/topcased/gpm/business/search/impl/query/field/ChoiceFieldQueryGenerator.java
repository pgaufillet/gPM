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
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.util.bean.GpmPair;

/**
 * Generator for <b>Choice</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class ChoiceFieldQueryGenerator extends
        AbstractMultiValuedFieldQueryGenerator {
    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    private static final String VALUE = ".value";

    private static final String CHOICE_FIELD_VALUE_TABLE_NAME =
            "CATEGORY_VALUE";

    private static final String CHOICE_FIELD_VALUE_ALIAS = "category_value";

    /**
     * AttachedFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public ChoiceFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + VALUE + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        String lFieldName = containerAlias + VALUE;
        String lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        final String lContainerColumnName = lFieldName;

        if (criteriaFieldData.getCaseSensitive() == null
                || !criteriaFieldData.getCaseSensitive()) {
            lFieldName =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lFieldName + ") ";
            lScalarValue =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lScalarValue + ") ";
        }

        final String lWhereClause =
                lFieldName + " " + criteriaFieldData.getOperator() + " "
                        + lScalarValue;

        return computeNullWhereClause(lContainerColumnName, lWhereClause);
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
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(
                criteriaFieldData.getScalarValueData().getValue(),
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);

        if (usableFieldData.getMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            containerAlias = pQuery.getAlias();
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
            }
            else {
                generateJoin(pQuery);
            }

            criteriaFieldData = pCriteriaFieldData;
            return getWhereClause();
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
            containerAlias = pQuery.getAlias();

            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
            }
            else {
                generateJoin(pQuery);
            }
            final String lSelectElement = getSelectClause();
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateUnionResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generateUnionResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();

        generateJoin(pQuery);
        final String lSelectElement = getSelectClause();
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateUnionOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    @Override
    public void generateUnionOrder(final GPMQuery pQuery, final String pOrder) {
        containerAlias = pQuery.getAlias();

        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }
        else {
            generateJoin(pQuery);
        }
        final GpmPair<String, String> lClauses = getOrderClauses(pOrder);
        final String lSelectClause = lClauses.getFirst();
        final String lOrderClause = lClauses.getSecond();

        pQuery.appendToSelectClause(lSelectClause);
        pQuery.appendToGroupByClause(lSelectClause);
        pQuery.addOrderByElement(lOrderClause, getOrder(pOrder));
    }

    /**
     * Generates the join query.
     * <p>
     * left join with 'category_value' table on identifiers.
     * </p>
     * <p>
     * Update the alias.
     * </p>
     * 
     * @param pQuery
     *            Query to fill.
     */
    private void generateJoin(final GPMQuery pQuery) {
        final String lAlias =
                pQuery.generateAlias(usableFieldData.getId(),
                        AliasType.CHOICE_FIELD);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                CHOICE_FIELD_VALUE_TABLE_NAME, lAlias,
                getContainerColumnName(containerAlias) + " = " + lAlias + ".id");
        containerAlias = lAlias;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        return containerAlias + VALUE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(final GPMQuery pQuery, final String pOrder) {
        final String lSelectClause;
        final String lOrderClause;
        containerAlias = pQuery.getAlias();
        //If the field is multivalued
        if (usableFieldData.getMultivalued()) {
            //Generate the new containerAlias
            handleMultiValuedOrder(pQuery, pOrder);
        }
        else {
            //if the containerAlias already exists
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                containerAlias = pQuery.getMappedAlias(usableFieldData.getId());

            }
            else {
                generateJoin(pQuery);
            }

        }
        final GpmPair<String, String> lClauses = getOrderClauses(pOrder);
        lSelectClause = lClauses.getFirst();
        lOrderClause = lClauses.getSecond();
        pQuery.appendToSelectClause(lSelectClause);
        pQuery.appendToGroupByClause(lSelectClause);
        pQuery.addOrderByElement(lOrderClause, getOrder(pOrder));
    }

    /**
     * Creates clauses 'SELECT' and 'ORDER' for order generation.
     * 
     * @param pOrder
     *            Order operator.
     * @return Order clauses.
     */
    private GpmPair<String, String> getOrderClauses(final String pOrder) {
        final String lSelectClause;
        final String lOrderClause;
        if (Operators.DEF_ASC.equals(pOrder)
                || Operators.DEF_DESC.equals(pOrder)) {
            lSelectClause = containerAlias + ".category_value_idx";
            lOrderClause = lSelectClause;
        }
        else {
            lOrderClause = getOrderByClause();
            lSelectClause = getSelectClause();
        }
        return new GpmPair<String, String>(lSelectClause, lOrderClause);
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

        pQuery.appendToSelectClause(getMultiValuedSelectClause(lAlias));
        pQuery.appendToGroupByClause(getMultiValuedSelectClause(lAlias));
        pQuery.appendToGroupByClause(pQuery.getAlias() + ".id");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedSelectClause()
     */
    public String getMultiValuedSelectClause(final String pAlias) {
        return pAlias + VALUE + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedFromClause(java.lang.String)
     */
    public String getMultiValuedFromClause(final String pAlias) {
        //Get values of the multi-valued field ordering by 'num_line' attribute and 'ASC' order.
        //
        final GPMQuery lQuery = new GPMQuery();
        final String lAlias =
                lQuery.generateAlias(usableFieldData.getId(),
                        AliasType.FIELD_MV);
        final String lChoiceField = getDynamicColumnName();
        lQuery.appendToSelectClause(lAlias + "." + lChoiceField);
        lQuery.appendToSelectClause(lAlias + ".parent_id");
        lQuery.appendToSelectClause(CHOICE_FIELD_VALUE_ALIAS + VALUE);
        lQuery.appendToSelectClause(CHOICE_FIELD_VALUE_ALIAS
                + ".category_value_idx");

        final String lMvTable =
                DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                        usableFieldData.getFieldId());
        lQuery.appendToFromClause(lMvTable + " " + lAlias + " left join "
                + CHOICE_FIELD_VALUE_TABLE_NAME + " "
                + CHOICE_FIELD_VALUE_ALIAS + " ON " + lAlias + "."
                + lChoiceField + " = " + CHOICE_FIELD_VALUE_ALIAS + ".id");
        lQuery.addOrderByElement(lAlias + ".num_line", "ASC");

        return "(" + lQuery.getCompleteQuery(false) + ")";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        final String lContainerColumnName = pAlias + VALUE;
        final String lWhereClause =
                lContainerColumnName
                        + " "
                        + criteriaFieldData.getOperator()
                        + org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        return computeNullWhereClause(lContainerColumnName, lWhereClause);
    }

    private String getOrder(final String pOrder) {
        String lOrder = pOrder;
        if (Operators.DEF_ASC.equals(pOrder)
                || Operators.DEF_DESC.equals(pOrder)) {
            lOrder = StringUtils.substringAfter(pOrder, "_");
        }
        return lOrder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateMultiContainerResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generateMultiContainerResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        final String lSelectElement = getContainerColumnName(containerAlias);
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateMultiContainerSort(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generateMultiContainerSort(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        final String lSelectElement = getContainerColumnName(containerAlias);
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }
}

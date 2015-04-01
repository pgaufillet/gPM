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
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Boolean</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class BooleanFieldQueryGenerator extends
        AbstractMultiValuedFieldQueryGenerator {

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * BooleanFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public BooleanFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return getContainerColumnName(containerAlias) + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        final String lContainerColumnName =
                getContainerColumnName(containerAlias);
        String lParam =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        final String lWhereClause =
                lContainerColumnName + " " + criteriaFieldData.getOperator()
                        + " " + lParam;
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
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(
                criteriaFieldData.getScalarValueData().getValue(),
                FieldType.SIMPLE_BOOLEAN_FIELD, parameterIndex);
        if (usableFieldData.getMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            containerAlias = pQuery.getAlias();
            pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
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
            pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            final String lSelectElement = getSelectClause();
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        return getContainerColumnName(containerAlias);
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

        pQuery.appendToSelectClause(getMultiValuedSelectClause(lAlias));
        pQuery.appendToGroupByClause(pQuery.getAlias() + ".id");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        final String lContainerColumnName = getContainerColumnName(pAlias);
        String lParam =
                " " + org.topcased.gpm.util.lang.StringUtils.UNI_CODE_TOKEN
                        + this.parameterIndex + " ";
        final String lWhereClause =
                lContainerColumnName + " " + criteriaFieldData.getOperator()
                        + " " + lParam;
        return computeNullWhereClause(lContainerColumnName, lWhereClause);
    }
}

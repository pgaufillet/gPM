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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.util.driver.DriverSQL;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Generator for <b>Date</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class DateFieldQueryGenerator extends
        AbstractMultiValuedFieldQueryGenerator {

    private final DriverSQL driverSQL;

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private Integer[] parameterIndex = { -1, -1 };

    /**
     * DateFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public DateFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
        driverSQL = GpmSessionFactory.getInstance().getDriverSQL();
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
        return getWhereClause(lContainerColumnName);
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

        if (usableFieldData.getMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            createDateValue(pFilterQueryConfigurator,
                    pCriteriaFieldData.getOperator());
            containerAlias = pQuery.getAlias();
            pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            criteriaFieldData = pCriteriaFieldData;

            return getWhereClause();
        }

    }

    /**
     * Creates the criterion date value.
     * <p>
     * Add it as query parameter
     * </p>
     * 
     * @param pFilterQueryConfigurator
     *            Update query parameter
     */
    private void createDateValue(
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final String pOperator) {
        final Date lDate =
                (Date) criteriaFieldData.getScalarValueData().getValue();
        final Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTime(lDate);
        lCalendar.add(Calendar.DAY_OF_YEAR, 1);
        lCalendar.add(Calendar.MILLISECOND, -1);
        final java.sql.Date lSQLDateInf = new java.sql.Date(lDate.getTime());
        final java.sql.Date lSQLDateSup =
                new java.sql.Date(lCalendar.getTime().getTime());

        if (Operators.EQ.equals(pOperator) || Operators.LT.equals(pOperator)
                || Operators.GE.equals(pOperator)
                || Operators.NEQ.equals(pOperator)) {
            this.parameterIndex[0] = pFilterQueryConfigurator.increment();
            pFilterQueryConfigurator.addParameters(lSQLDateInf,
                    FieldType.SIMPLE_DATE_FIELD, parameterIndex[0]);
        }
        if (Operators.EQ.equals(pOperator) || Operators.LE.equals(pOperator)
                || Operators.GT.equals(pOperator)
                || Operators.NEQ.equals(pOperator)) {
            this.parameterIndex[1] = pFilterQueryConfigurator.increment();
            pFilterQueryConfigurator.addParameters(lSQLDateSup,
                    FieldType.SIMPLE_DATE_FIELD, parameterIndex[1]);
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

        final String lSelectElement = getMultiValuedSelectClause(lAlias);
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
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
        createDateValue(pFilterQueryConfigurator,
                pCriteriaFieldData.getOperator());
        return super.handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                pFilterQueryConfigurator);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        final String lContainerColumnName = getContainerColumnName(pAlias);
        return getWhereClause(lContainerColumnName);
    }

    private String getWhereClause(final String pContainerColumnName) {
        final int lExpectedCapacity = 100;
        StringBuffer lWhereClause = new StringBuffer(lExpectedCapacity);
        String lToDate = driverSQL.getDateForWhereClause(pContainerColumnName);
        String[] lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
        if (Operators.EQ.equals(criteriaFieldData.getOperator())) {
            lWhereClause.append("(");
            lWhereClause.append(lToDate);
            lWhereClause.append(Operators.GE);
            lWhereClause.append(lScalarValue[0] + "AND ");
            lWhereClause.append(lToDate);
            lWhereClause.append(Operators.LE);
            lWhereClause.append(lScalarValue[1] + ")");
        }
        else if (Operators.NEQ.equals(criteriaFieldData.getOperator())) {
            lWhereClause.append("(");
            lWhereClause.append(lToDate);
            lWhereClause.append(Operators.LT);
            lWhereClause.append(lScalarValue[0] + " OR ");
            lWhereClause.append(lToDate);
            lWhereClause.append(Operators.GT);
            lWhereClause.append(lScalarValue[1] + ")");
        }
        else {
            lWhereClause.append(lToDate);
            lWhereClause.append(criteriaFieldData.getOperator());
            lWhereClause.append(lScalarValue[0]);
        }
        return computeNullWhereClause(pContainerColumnName,
                lWhereClause.toString());
    }
}

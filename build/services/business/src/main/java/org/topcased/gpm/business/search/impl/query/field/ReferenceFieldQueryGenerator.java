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

import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Reference</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class ReferenceFieldQueryGenerator extends
        AbstractVirtualFieldQueryGenerator {
    private static final String VALUES_CONTAINER_TABLE_NAME =
            "VALUES_CONTAINER";

    private static final String REFERENCE_FIELD_NAME = "reference";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * ReferenceFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public ReferenceFieldQueryGenerator(UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + "." + REFERENCE_FIELD_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getFromClause()
     */
    @Override
    protected String getFromClause() {
        return VALUES_CONTAINER_TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        String lFieldName = getSelectClause();
        String lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);

        if (criteriaFieldData.getCaseSensitive() != null
                && !criteriaFieldData.getCaseSensitive()) {
            lFieldName =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lFieldName + ")";
            lScalarValue =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lScalarValue + ") ";
        }

        return lFieldName + " " + criteriaFieldData.getOperator() + " "
                + lScalarValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        return getSelectClause();
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
        containerAlias = pQuery.getAlias();
        sqlFunctions = pFilterQueryConfigurator.getSqlFunctions();
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(
                criteriaFieldData.getScalarValueData().getValue(),
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);

        generateJoint(pQuery);

        return getWhereClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractVirtualFieldQueryGenerator#generateJoint(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    protected void generateJoint(final GPMQuery pQuery) {
        final String lValuesContainerAlias =
                pQuery.generateAlias(usableFieldData.getId(),
                        AliasType.VALUES_CONTAINER);

        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                VALUES_CONTAINER_TABLE_NAME, lValuesContainerAlias,
                containerAlias + ".id = " + lValuesContainerAlias + ".id");
        containerAlias = lValuesContainerAlias;
    }
}
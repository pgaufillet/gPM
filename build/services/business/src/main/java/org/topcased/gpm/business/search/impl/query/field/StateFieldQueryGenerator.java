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
 * Generator for <b>State</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class StateFieldQueryGenerator extends
        AbstractVirtualFieldQueryGenerator {
    private static final String NODE_TABLE_NAME = "NODE";

    private static final String SHEET_TABLE_NAME = "SHEET";

    private static final String FIELD_NAME = "name";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * StateFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public StateFieldQueryGenerator(UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + "." + FIELD_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        String lFieldName = containerAlias + "." + FIELD_NAME;
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
        return containerAlias + "." + FIELD_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData)
     */
    public String generateCriterion(GPMQuery pQuery,
            CriteriaFieldData pCriteriaFieldData,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        criteriaFieldData = pCriteriaFieldData;
        containerAlias = pQuery.getAlias();
        criteriaFieldData = pCriteriaFieldData;
        sqlFunctions = pFilterQueryConfigurator.getSqlFunctions();
        parameterIndex = pFilterQueryConfigurator.increment();
        pFilterQueryConfigurator.addParameters(
                criteriaFieldData.getScalarValueData().getValue(),
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);

        generateJoint(pQuery);
        return getWhereClause();
    }

    protected void generateJoint(GPMQuery pQuery) {
        String lSheetAlias =
                pQuery.generateAlias(SHEET_TABLE_NAME, AliasType.SHEET);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, SHEET_TABLE_NAME,
                lSheetAlias, containerAlias + ".id = " + lSheetAlias + ".id");

        //Node join
        String lNodeAlias =
                pQuery.generateAlias(usableFieldData.getId(),
                        AliasType.JBPM_NODE);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, NODE_TABLE_NAME,
                lNodeAlias, lSheetAlias + ".CURRENT_NODE_FK = " + lNodeAlias
                        + ".id");

        containerAlias = lNodeAlias;
    }
}

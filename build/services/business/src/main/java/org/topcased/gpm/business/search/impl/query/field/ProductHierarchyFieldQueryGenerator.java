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

import org.apache.commons.lang.text.StrBuilder;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Product hierarchy</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class ProductHierarchyFieldQueryGenerator extends
        AbstractFieldQueryGenerator {
    private static final String SHEET_TABLE_NAME = "SHEET";

    private static final String PRODUCT_TABLE_NAME = "PRODUCT";

    private static final String CHILDREN2PARENTS_TABLE_NAME =
            "CHILDREN2PARENTS";

    private static final String CHILDREN2PARENTS_ALIAS = "c2p";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * ProductHierarchyFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public ProductHierarchyFieldQueryGenerator(UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        throw new MethodNotImplementedException(
                "No SELECT clause for PRODUCT HIERARCHY field.");
    }

    @Override
    protected String getFromClause() {
        return PRODUCT_TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        String lFieldName = containerAlias + ".name";
        String lScalarValue =
                org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);

        if (!criteriaFieldData.getCaseSensitive()) {
            lFieldName =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lFieldName + ")";
            lScalarValue =
                    sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                            + lScalarValue + ") ";
        }

        StrBuilder lStrBuilder = new StrBuilder();
        lStrBuilder.append("((");
        lStrBuilder.append(lFieldName + " = ").append(lScalarValue);
        lStrBuilder.append(") OR (");
        lStrBuilder.append(containerAlias + ".id IN (");

        //Sub query for children
        GPMQuery lQuery = new GPMQuery();
        lQuery.appendToSelectClause(CHILDREN2PARENTS_ALIAS + ".children_fk");
        lQuery.addElementInFromClause(CHILDREN2PARENTS_TABLE_NAME,
                CHILDREN2PARENTS_ALIAS);
        lStrBuilder.append(lQuery.getCompleteQuery());
        lStrBuilder.append(")");
        lStrBuilder.append("))");
        return lStrBuilder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        throw new MethodNotImplementedException(
                "No ORDER BY clause for PRODUCT HIERARCHY field.");
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
                criteriaFieldData.getScalarValueData().getValue().toString(),
                FieldType.SIMPLE_STRING_FIELD, parameterIndex);

        generateJoint(pQuery);
        return getWhereClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(GPMQuery pQuery) {
        throw new MethodNotImplementedException(
                "Cannot set PRODUCT HIERARCHY field as result.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(GPMQuery pQuery, String pOrder) {
        throw new MethodNotImplementedException(
                "Cannot set PRODUCT HIERARCHY field as sorted result.");
    }

    private void generateJoint(GPMQuery pQuery) {
        String lSheetAlias =
                pQuery.generateAlias(SHEET_TABLE_NAME, AliasType.SHEET);
        pQuery.addElementInFromClause(JOIN_TYPE.JOIN, SHEET_TABLE_NAME,
                lSheetAlias, containerAlias + ".id = " + lSheetAlias + ".id");
        //Join to dynamic table

        //Fields container
        String lProductAlias =
                pQuery.generateAlias(PRODUCT_TABLE_NAME, AliasType.PRODUCT);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, getFromClause(),
                lProductAlias, lSheetAlias + ".product_fk = " + lProductAlias
                        + ".id");
        //Join to values container
        containerAlias = lProductAlias;
    }
}

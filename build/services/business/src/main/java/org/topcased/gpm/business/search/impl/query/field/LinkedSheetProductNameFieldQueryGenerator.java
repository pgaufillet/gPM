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

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.scalar.MultiStringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Generator for <b>Linked sheet product name</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class LinkedSheetProductNameFieldQueryGenerator extends
        AbstractFieldQueryGenerator {
    private static final String LINK_TABLE_NAME = "LINK";

    private static final String VALUES_CONTAINER_TABLE_NAME =
            "VALUES_CONTAINER";

    private static final String SHEET_TABLE_NAME = "SHEET";

    private static final String PRODUCT_TABLE_NAME = "PRODUCT";

    private static final String PRODUCT_NAME_FIELD = "name";

    private VirtualFieldType virtualFieldType;

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * ProductNameFieldQueryGenerator constructor
     * 
     * @param pVirtualFieldData
     *            Virtual field to analyze for generation
     */
    public LinkedSheetProductNameFieldQueryGenerator(
            VirtualFieldData pVirtualFieldData) {
        super(pVirtualFieldData);
        virtualFieldType = pVirtualFieldData.getVirtualFieldType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + "." + PRODUCT_NAME_FIELD;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        String lFieldName = containerAlias + "." + PRODUCT_NAME_FIELD;
        String lScalarValue;

        if (criteriaFieldData.getOperator().equalsIgnoreCase(Operators.IN)) {
            lScalarValue =
                    ((MultiStringValueData) criteriaFieldData.getScalarValueData()).getInClause();
        }
        else {
            if (criteriaFieldData.getCaseSensitive()) {
                lScalarValue =
                        org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex);
            }
            else {
                lFieldName =
                        sqlFunctions.get(SqlFunction.CASE_SENSITIVE) + "("
                                + lFieldName + ")";
                lScalarValue =
                        sqlFunctions.get(SqlFunction.CASE_SENSITIVE)
                                + "("
                                + org.topcased.gpm.util.lang.StringUtils.getParameterTag(parameterIndex)
                                + ")";
            }
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
        return containerAlias + "." + PRODUCT_NAME_FIELD;
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

        if (!criteriaFieldData.getOperator().equalsIgnoreCase(Operators.IN)) {
            parameterIndex = pFilterQueryConfigurator.increment();
            pFilterQueryConfigurator.addParameters(
                    criteriaFieldData.getScalarValueData().getValue().toString(),
                    FieldType.SIMPLE_STRING_FIELD, parameterIndex);
        }

        generateJoint(pQuery);
        return getWhereClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        generateJoint(pQuery);
        String lSelectElement = getSelectClause();
        pQuery.appendToSelectClause(lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(GPMQuery pQuery, String pOrder) {
        containerAlias = pQuery.getAlias();
        if (!pQuery.isAlreadyMapped(PRODUCT_TABLE_NAME
                + usableFieldData.getId())) {
            generateJoint(pQuery);
            String lSelectElement = getSelectClause();
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        else {
            containerAlias =
                    pQuery.getMappedAlias(PRODUCT_TABLE_NAME
                            + usableFieldData.getId());
        }

        pQuery.addOrderByElement(getOrderByClause(), pOrder);
    }

    private void generateJoint(GPMQuery pQuery) {
        if (!pQuery.isAlreadyMapped(PRODUCT_TABLE_NAME
                + usableFieldData.getId())) {
            String lGenericLinkAlias =
                    pQuery.generateAlias(LINK_TABLE_NAME
                            + usableFieldData.getId(), AliasType.LINK);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, LINK_TABLE_NAME,
                    lGenericLinkAlias, containerAlias + ".id = "
                            + lGenericLinkAlias + ".id");
            //Join to dynamic table

            //Values container
            String lValuesContainerAlias =
                    pQuery.generateAlias(VALUES_CONTAINER_TABLE_NAME
                            + usableFieldData.getId(),
                            AliasType.VALUES_CONTAINER);

            //Join to values container
            String lCondition = StringUtils.EMPTY;
            switch (virtualFieldType) {
                case $ORIGIN_PRODUCT:
                    lCondition =
                            lGenericLinkAlias + ".origin_fk = "
                                    + lValuesContainerAlias + ".id";
                    break;
                case $DEST_PRODUCT:
                    lCondition =
                            lGenericLinkAlias + ".destination_fk = "
                                    + lValuesContainerAlias + ".id";
                    break;
                default:
                    throw new NotImplementedException("Type '"
                            + virtualFieldType + "' is not handled");
            }
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    VALUES_CONTAINER_TABLE_NAME, lValuesContainerAlias,
                    lCondition);
            //Sheet table
            String lSheetAlias =
                    pQuery.generateAlias(SHEET_TABLE_NAME
                            + usableFieldData.getId(), AliasType.SHEET);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    SHEET_TABLE_NAME, lSheetAlias, lValuesContainerAlias
                            + ".id = " + lSheetAlias + ".id");
            //Join to values container

            //Product table
            String lProductAlias =
                    pQuery.generateAlias(PRODUCT_TABLE_NAME
                            + usableFieldData.getId(), AliasType.PRODUCT);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    PRODUCT_TABLE_NAME, lProductAlias, lSheetAlias
                            + ".product_fk = " + lProductAlias + ".id");
            //Join to sheet table
            containerAlias = lProductAlias;
        }
        else {
            containerAlias =
                    pQuery.getMappedAlias(PRODUCT_TABLE_NAME
                            + usableFieldData.getId());
        }

    }
}

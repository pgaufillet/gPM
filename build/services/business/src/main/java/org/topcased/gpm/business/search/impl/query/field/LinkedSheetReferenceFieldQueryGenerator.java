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
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Generator for <b>Linked sheet reference</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class LinkedSheetReferenceFieldQueryGenerator extends
        AbstractFieldQueryGenerator {
    private static final String LINK_TABLE_NAME = "LINK";

    private static final String VALUES_CONTAINER_TABLE_NAME =
            "VALUES_CONTAINER";

    private VirtualFieldType virtualFieldType;

    /**
     * ProductNameFieldQueryGenerator constructor
     * 
     * @param pVirtualFieldData
     *            Virtual field to analyze for generation
     */
    public LinkedSheetReferenceFieldQueryGenerator(
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
        return containerAlias + ".reference";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {

        String lFieldName = containerAlias + ".reference";
        String lScalarValue =
                "'"
                        + criteriaFieldData.getScalarValueData().getValue().toString()
                        + "'";

        if (!criteriaFieldData.getCaseSensitive()) {
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
        return containerAlias + ".reference";
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
        if (!pQuery.isAlreadyMapped(VALUES_CONTAINER_TABLE_NAME
                + usableFieldData.getId())) {
            generateJoint(pQuery);
            String lSelectElement = getSelectClause();
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        else {
            containerAlias =
                    pQuery.getMappedAlias(VALUES_CONTAINER_TABLE_NAME
                            + usableFieldData.getId());
        }

        pQuery.addOrderByElement(getOrderByClause(), pOrder);

    }

    private void generateJoint(GPMQuery pQuery) {
        if (!pQuery.isAlreadyMapped(VALUES_CONTAINER_TABLE_NAME
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

            String lCondition = StringUtils.EMPTY;
            //Join to values container
            switch (virtualFieldType) {
                case $ORIGIN_SHEET_REF:
                    lCondition =
                            lGenericLinkAlias + ".origin_fk = "
                                    + lValuesContainerAlias + ".id";
                    break;
                case $DEST_SHEET_REF:
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
            containerAlias = lValuesContainerAlias;
        }
        else {
            containerAlias =
                    pQuery.getMappedAlias(VALUES_CONTAINER_TABLE_NAME
                            + usableFieldData.getId());
        }

    }
}

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

import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Type</b> virtual fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class TypeFieldQueryGenerator extends AbstractVirtualFieldQueryGenerator {
    private static final String FIELDS_CONTAINER_TABLE_NAME =
            "FIELDS_CONTAINER";

    private static final String VALUES_CONTAINER_TABLE_NAME =
            "VALUES_CONTAINER";

    /**
     * TypeFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public TypeFieldQueryGenerator(UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + ".name";
    }

    @Override
    protected String getFromClause() {
        return FIELDS_CONTAINER_TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {

        String lFieldName = containerAlias + ".name";
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
                            + lScalarValue + ")";
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
        return containerAlias + ".name";
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

    protected void generateJoint(GPMQuery pQuery) {
        String lValuesContainerAlias =
                pQuery.generateAlias(VALUES_CONTAINER_TABLE_NAME,
                        AliasType.VALUES_CONTAINER);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                VALUES_CONTAINER_TABLE_NAME, lValuesContainerAlias,
                containerAlias + ".id = " + lValuesContainerAlias + ".id");
        //Join to dynamic table

        //Fields container
        String lFieldsContainerAlias =
                pQuery.generateAlias(usableFieldData.getId(),
                        AliasType.FIELDS_CONTAINER);
        pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, getFromClause(),
                lFieldsContainerAlias, lValuesContainerAlias
                        + ".definition_fk = " + lFieldsContainerAlias + ".id");
        //Join to values container
        containerAlias = lFieldsContainerAlias;
    }
}

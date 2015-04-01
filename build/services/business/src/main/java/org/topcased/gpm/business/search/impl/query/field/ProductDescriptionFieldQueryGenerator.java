/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.scalar.MultiStringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for <b>Product description</b> virtual field.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author jeballar
 */
public class ProductDescriptionFieldQueryGenerator extends
        AbstractVirtualFieldQueryGenerator {
    private static final String SHEET_TABLE_NAME = "SHEET";

    private static final String PRODUCT_TABLE_NAME = "PRODUCT";

    /**
     * Current query parameter index, -1 is an incorrect index value which is
     * use to detect all the parameters that are incorrectly set
     */
    private int parameterIndex = -1;

    /**
     * ProductDescriptionFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public ProductDescriptionFieldQueryGenerator(
            UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return containerAlias + ".description";
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
        String lFieldName = containerAlias + ".description ";
        String lScalarValue;

        if (criteriaFieldData.getOperator().equalsIgnoreCase(Operators.IN)) {
            lScalarValue =
                    ((MultiStringValueData) criteriaFieldData.getScalarValueData()).getInClause();
        }
        else {
            if (criteriaFieldData.getCaseSensitive() != null
                    && !criteriaFieldData.getCaseSensitive()) {

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
        return containerAlias + ".description";
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

    protected void generateJoint(GPMQuery pQuery) {
        switch (pQuery.getAliasType(containerAlias)) {
            case UNION: //Union only for Sheet so the same case.
            case SHEET: {
                final String lSheetAlias =
                        pQuery.generateAlias(SHEET_TABLE_NAME, AliasType.SHEET);

                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        SHEET_TABLE_NAME, lSheetAlias, containerAlias
                                + ".id = " + lSheetAlias + ".id");

                // Fields container
                final String lProductAlias =
                        pQuery.generateAlias(usableFieldData.getId(),
                                AliasType.PRODUCT);

                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        getFromClause(), lProductAlias, lSheetAlias
                                + ".product_fk = " + lProductAlias + ".id");

                // Join to values container
                containerAlias = lProductAlias;
                break;
            }
            case PRODUCT: {
                final String lProductAlias =
                        pQuery.generateAlias(PRODUCT_TABLE_NAME,
                                AliasType.PRODUCT);

                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        getFromClause(), lProductAlias, containerAlias
                                + ".id = " + lProductAlias + ".id");

                // Join to values container
                containerAlias = lProductAlias;
                break;
            }
            default:
                throw new NotImplementedException("Invalid type "
                        + pQuery.getAliasType(containerAlias));
        }
    }
}
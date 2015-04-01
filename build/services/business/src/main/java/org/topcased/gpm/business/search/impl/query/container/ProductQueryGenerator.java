/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.container;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;

/**
 * Generator for PRODUCT container.
 * 
 * @author mkargbo
 */
public class ProductQueryGenerator extends AbstractContainerQueryGenerator {
    private static final String PRODUCT_TABLE_NAME = "PRODUCT";

    private static final String SHEET_TABLE_NAME = "SHEET";

    /**
     * Create a container query generator for products.
     * 
     * @param pFieldsContainerId
     *            The type id.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public ProductQueryGenerator(final String pFieldsContainerId,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        super(pFieldsContainerId, pAdditionalConstraints);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        alias = pQuery.generateAlias(fieldsContainerId, AliasType.PRODUCT);
        pQuery.appendToSelectClause(getSelectClause());
        pQuery.appendToGroupByClause(getSelectClause());
        pQuery.addElementInFromClause(getFromClause(), alias);

        //Join to PRODUCT table for name attribute
        String lAlias =
                pQuery.generateAlias(PRODUCT_TABLE_NAME, AliasType.PRODUCT);
        pQuery.appendToSelectClause(lAlias + ".name");
        pQuery.appendToGroupByClause(lAlias + ".name");
        pQuery.addElementInFromClause(JOIN_TYPE.JOIN, PRODUCT_TABLE_NAME,
                lAlias, lAlias + ".id = " + alias + ".id");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#getFromClause()
     */
    public String getFromClause() {
        return DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                fieldsContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        return alias + ".id";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        throw new MethodNotImplementedException(
                "Not implemented WHERE clause for PRODUCT");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.IContainerQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo,
     *      org.topcased.gpm.business.search.service.UsableFieldData,
     *      java.lang.String, int)
     */
    public String generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator, String pJoinTo,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo,
            UsableFieldData pUsableFieldData, String pPreviousContainerAlias,
            int pLevel) {
        //For container from several level,
        //the identifier of the alias is the fields container identifier + level

        String lBaseId = pUsableFieldData.getId();
        // The +1 ensures that the substring does not crash if '|' is not found.
        lBaseId = lBaseId.substring(0, lBaseId.lastIndexOf('|') + 1);
        String lAliasIdentifier = lBaseId + pUsableFieldData.getLevel();
        if (!pQuery.isAlreadyMapped(lAliasIdentifier)) {
            alias = pQuery.generateAlias(lAliasIdentifier, AliasType.PRODUCT);

            if (!FieldsContainerType.LINK.equals(pFilterFieldsContainerInfo.getType())) {
                String lSheetAlias =
                        pQuery.generateAlias(SHEET_TABLE_NAME, AliasType.SHEET);
                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        SHEET_TABLE_NAME, lSheetAlias, pJoinTo + ".id = "
                                + lSheetAlias + ".id");
                //Join to dynamic table

                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        getSubRequest(pFilterFieldsContainerInfo,
                                pUsableFieldData, pFilterQueryConfigurator),
                        alias, lSheetAlias + ".product_fk = " + alias + ".id");

                return alias;
            }
            else {
                pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                        getSubRequest(pFilterFieldsContainerInfo,
                                pUsableFieldData, pFilterQueryConfigurator),
                        alias, "("
                                + getJoinClause(pJoinTo,
                                        pFilterFieldsContainerInfo) + " AND "
                                + alias + ".id <> " + pQuery.getAlias()
                                + ".id)");

                return alias;
            }
        }
        else {
            return pQuery.getMappedAlias(lAliasIdentifier);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#getJoinClause(java.lang.String,
     *      org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo)
     */
    @Override
    protected String getJoinClause(String pJoinTo,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo) {
        switch (pFilterFieldsContainerInfo.getLinkDirection()) {
            case ORIGIN:
                return pJoinTo + ".destination_fk = " + alias + ".id";
            case DESTINATION:
                return pJoinTo + ".origin_fk = " + alias + ".id";
            case UNDEFINED:
                return pJoinTo + ".destination_fk = " + alias + ".id" + " OR "
                        + pJoinTo + ".origin_fk = " + alias + ".id";
            default:
                throw new NotImplementedException("Direction type '"
                        + pFilterFieldsContainerInfo.getLinkDirection()
                        + "' is not supported.");
        }
    }
}

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
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import
    org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator.AdditionalInformationType;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.util.lang.StringUtils;

/**
 * Generator for SHEET container.
 * 
 * @author mkargbo
 */
public class SheetQueryGenerator extends AbstractContainerQueryGenerator {
    private static final String VALUES_CONTAINER_TABLE_NAME =
            "VALUES_CONTAINER";

    private static final String FIELDS_CONTAINER_TABLE_NAME =
            "FIELDS_CONTAINER";

    private static final String LOCK_TABLE_NAME = "ACCESS_LOCK";

    private static final String SHEET_TABLE_NAME = "sheet";

    /**
     * Create a container query generator for sheets.
     * 
     * @param pFieldsContainerId
     *            The type id.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public SheetQueryGenerator(final String pFieldsContainerId,
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
        alias = pQuery.generateAlias(fieldsContainerId, AliasType.SHEET);
        pQuery.appendToSelectClause(getSelectClause());
        pQuery.appendToGroupByClause(getSelectClause());
        pQuery.addElementInFromClause(getFromClause(), alias);

        if (!pFilterQueryConfigurator.isOnlyIdentifier()) {
            //Join to VALUES_CONTAINER table for reference, lock information
            String lValuesContainerAlias =
                    pQuery.generateAlias(VALUES_CONTAINER_TABLE_NAME,
                            AliasType.VALUES_CONTAINER);
            pQuery.appendToSelectClause(lValuesContainerAlias + ".reference"
                    + GPMQuery.SELECT_AS_CLAUSE + "ref");
            pQuery.appendToGroupByClause(lValuesContainerAlias + ".reference");
            pQuery.addElementInFromClause(JOIN_TYPE.JOIN,
                    VALUES_CONTAINER_TABLE_NAME, lValuesContainerAlias,
                    lValuesContainerAlias + ".id = " + alias + ".id");

            //Join to LOCK table for lock information
            String lLockAlias =
                    pQuery.generateAlias(LOCK_TABLE_NAME, AliasType.LOCK);
            pQuery.appendToSelectClause(lLockAlias + ".owner");
            pQuery.appendToGroupByClause(lLockAlias + ".owner");
            pQuery.appendToSelectClause(lLockAlias + ".lock_type");
            pQuery.appendToGroupByClause(lLockAlias + ".lock_type");

            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, LOCK_TABLE_NAME,
                    lLockAlias, lValuesContainerAlias + ".id = " + lLockAlias
                            + ".container_id");

            // Remove line locked on visualization only if user has not global admin role
            if (!pFilterQueryConfigurator.hasGlobalAdminRole()) {
                final String lSheetAlias =
                        pQuery.generateAlias(SHEET_TABLE_NAME, AliasType.SHEET);

                pQuery.addElementInFromClause(JOIN_TYPE.JOIN, SHEET_TABLE_NAME,
                        lSheetAlias, alias + ".id = " + lSheetAlias + ".id");

                //First where clause 'Lock type'
                StringBuilder lLockTypeClause = new StringBuilder("(");
                lLockTypeClause.append(lLockAlias + ".lock_type is null");
                lLockTypeClause.append(" OR ");
                lLockTypeClause.append(lLockAlias + ".lock_type <> '"
                        + LockType.READ_WRITE + "'");
                lLockTypeClause.append(")");

                //Second where clause 'owner'
                final int lParameterIndex = pFilterQueryConfigurator.increment();
                StringBuilder lOwnerClause = new StringBuilder("(");
                lOwnerClause.append(lLockAlias + ".owner is null");
                lOwnerClause.append(" OR ");
                lOwnerClause.append(lLockAlias + ".owner = "
                        + StringUtils.getParameterTag(lParameterIndex));
                lOwnerClause.append(")");

                StringBuilder lWhereClause = new StringBuilder("(");
                lWhereClause.append(lLockTypeClause).append(" OR ");
                lWhereClause.append(lOwnerClause);
                lWhereClause.append(")");
                pQuery.appendToWhereClause(lWhereClause.toString());

                pFilterQueryConfigurator.addParameters(
                        pFilterQueryConfigurator.getInformation(
                                AdditionalInformationType.CURRENT_USER_LOGIN),
                        FieldType.SIMPLE_STRING_FIELD, lParameterIndex);
            }

            //Join to FIELDS_CONTAINER table for type id and type name information
            String lFieldsContainerAlias =
                    pQuery.generateAlias(FIELDS_CONTAINER_TABLE_NAME,
                            AliasType.FIELDS_CONTAINER);
            pQuery.appendToSelectClause(lFieldsContainerAlias + ".id"
                    + GPMQuery.SELECT_AS_CLAUSE + "sheetTypeId");
            pQuery.appendToGroupByClause(lFieldsContainerAlias + ".id");
            pQuery.appendToSelectClause(lFieldsContainerAlias + ".name"
                    + GPMQuery.SELECT_AS_CLAUSE + "sheetTypeName");
            pQuery.appendToGroupByClause(lFieldsContainerAlias + ".name");
            pQuery.addElementInFromClause(JOIN_TYPE.JOIN,
                    FIELDS_CONTAINER_TABLE_NAME, lFieldsContainerAlias,
                    lFieldsContainerAlias + ".id = " + lValuesContainerAlias
                            + ".definition_fk");
        }
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
                "Not implemented WHERE clause for SHEET");
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
        //the identifier of the alias is the fields container identifier + level + link
        String lAliasIdentifier =
                pFilterFieldsContainerInfo.getId()
                        + pFilterFieldsContainerInfo.getLinkDirection()
                        + pLevel + pJoinTo;

        if (!pQuery.isAlreadyMapped(lAliasIdentifier)) {
            alias = pQuery.generateAlias(lAliasIdentifier, AliasType.SHEET);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, getSubRequest(
                    pFilterFieldsContainerInfo, pUsableFieldData,
                    pFilterQueryConfigurator), alias, "(("
                    + getJoinClause(pJoinTo, pFilterFieldsContainerInfo)
                    + ") AND " + alias + ".id <> " + pPreviousContainerAlias
                    + ".id)");
            return alias;
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

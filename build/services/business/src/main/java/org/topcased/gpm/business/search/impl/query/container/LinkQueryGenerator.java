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
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.link.Link;

/**
 * Generator for LINK container.
 * 
 * @author mkargbo
 */
public class LinkQueryGenerator extends AbstractContainerQueryGenerator {
    /**
     * Create a container query generator for links.
     * 
     * @param pFieldsContainerId
     *            The type id.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public LinkQueryGenerator(final String pFieldsContainerId,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        super(pFieldsContainerId, pAdditionalConstraints);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#getFromClause()
     */
    @Override
    protected String getFromClause() {
        return DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                fieldsContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#getSelectClause()
     */
    @Override
    protected String getSelectClause() {
        return alias + ".id";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#getWhereClause()
     */
    @Override
    protected String getWhereClause() {
        throw new MethodNotImplementedException(
                "Not implemented WHERE clause for LINK");
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

        // adding complete link path to the alias
        String lId = pUsableFieldData.getId();
        int lStartIndex = lId.indexOf(pFilterFieldsContainerInfo.getId());

        // Insure suitable substring operation
        if (lStartIndex == -1) {
            lStartIndex = 0;
        }
        String lLinkPath = lId.substring(0, lStartIndex).trim()
        + pFilterFieldsContainerInfo.getId() + '|' + pLevel;

        if ((!pQuery.isAlreadyMapped(lLinkPath))) {
            GPMQuery lQuery = new GPMQuery();
            String lGenericLinkAlias =
                    lQuery.generateAlias(AliasType.LINK.name()
                            + pUsableFieldData.getLevel(), AliasType.LINK);
            String lDynamicAlias =
                    lQuery.getOrGenerateAlias(lLinkPath, AliasType.LINK);
            lQuery.appendToSelectClause(lGenericLinkAlias + ".origin_fk");
            lQuery.appendToSelectClause(lGenericLinkAlias + ".destination_fk");
            lQuery.appendToSelectClause(lDynamicAlias + ".*");
            lQuery.addElementInFromClause(Link.class.getSimpleName(),
                    lGenericLinkAlias);
            lQuery.addElementInFromClause(getSubRequest(
                    pFilterFieldsContainerInfo, pUsableFieldData,
                    pFilterQueryConfigurator), lDynamicAlias);
            lQuery.addElementInWhereClause(getJoinLinks(lGenericLinkAlias,
                    lDynamicAlias));

            String lLinkAlias = pQuery.generateAlias(lLinkPath, AliasType.LINK);
            alias = lLinkAlias;
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN, "("
                    + lQuery.getCompleteQuery() + ")", lLinkAlias, "("
                    + getJoinClause(pJoinTo, pFilterFieldsContainerInfo) + ")");
            pQuery.putMappedAlias(lLinkPath, lLinkAlias);
            return lLinkAlias;
        }
        else {
            return pQuery.getMappedAlias(lLinkPath);
        }
    }

    /**
     * Join the link tables. Join generic link table (information about origin
     * and destination) with dynamic link table (corresponding to this link
     * container).
     * 
     * @param pGenericAlias
     *            Link table
     * @param pDynamicAlias
     *            Dynamic link table
     * @return Generated joint query
     */
    private String getJoinLinks(String pGenericAlias, String pDynamicAlias) {
        return pGenericAlias + ".id = " + pDynamicAlias + ".id";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#getJoinClause()
     */
    @Override
    protected String getJoinClause(String pJoinTo,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo) {
        switch (pFilterFieldsContainerInfo.getLinkDirection()) {
            case ORIGIN:
                return alias + ".destination_fk = " + pJoinTo + ".id";
            case DESTINATION:
                return alias + ".origin_fk = " + pJoinTo + ".id";
            case UNDEFINED:
                return alias + ".destination_fk = " + pJoinTo + ".id" + " OR "
                        + alias + ".origin_fk = " + pJoinTo + ".id";
            default:
                throw new NotImplementedException("Direction type '"
                        + pFilterFieldsContainerInfo.getLinkDirection()
                        + "' is not supported");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.AbstractContainerQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        alias = pQuery.generateAlias(fieldsContainerId, AliasType.LINK);
        pQuery.appendToSelectClause(getSelectClause());
        pQuery.appendToGroupByClause(getSelectClause());
        pQuery.addElementInFromClause(getFromClause(), alias);
    }
}
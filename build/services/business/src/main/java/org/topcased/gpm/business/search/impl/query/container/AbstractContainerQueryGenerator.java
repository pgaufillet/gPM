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

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.criteria.CriteriaQueryGeneratorFactory;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Abstract implementation of {@link IContainerQueryGenerator}.<br />
 * Implements {@link IContainerQueryGenerator#getAlias()} method.<br />
 * Defined abstract methods for SQL clause generation.
 * <p>
 * A container can be join to another (filter on several level)
 * 
 *@author mkargbo
 */
public abstract class AbstractContainerQueryGenerator implements
        IContainerQueryGenerator {
    protected final String fieldsContainerId;

    protected final FilterAdditionalConstraints additionalConstraints;

    protected String alias;

    /**
     * Create an abstract container query generator.
     * 
     * @param pFieldsContainerId
     *            The type id.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public AbstractContainerQueryGenerator(final String pFieldsContainerId,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        fieldsContainerId = pFieldsContainerId;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.container.IContainerQueryGenerator#getAlias()
     */
    public String getAlias() {
        return alias;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate()
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator) {
        throw new MethodNotImplementedException(
                "Cannot use this method for query generation on containers");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public abstract void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator);

    /**
     * Generate the FROM clause query fragment.
     * 
     * @return FROM clause query
     */
    protected abstract String getFromClause();

    /**
     * Generate the SELECT clause query fragment.
     * 
     * @return SELECT clause query
     */
    protected abstract String getSelectClause();

    /**
     * Generate the WHERE clause query fragment.
     * 
     * @return WHERE clause query
     */
    protected abstract String getWhereClause();

    /**
     * Generate the joint query fragment.
     * 
     * @param pJoinTo
     *            Alias of the other container to join to.
     * @param pFilterFieldsContainerInfo
     *            Fields container to join.
     * @return Alias the fields container joined.
     */
    protected abstract String getJoinClause(String pJoinTo,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo);

    /**
     * Generate the sub request used for non top level fields container.
     * 
     * @param pFilterFieldsContainerInfo a container info
     * @param pFieldData the usable field data
     * @param pConfig the filter configurator
     * @return The sub request.
     */
    @SuppressWarnings("unchecked")
    protected String getSubRequest(
            final FilterFieldsContainerInfo pFilterFieldsContainerInfo,
            final UsableFieldData pFieldData,
            final FilterQueryConfigurator pConfig) {
        // Compute container full id : with hierarchy
        final StringBuffer lFullId = new StringBuffer();
        final String lContainerId = pFilterFieldsContainerInfo.getId();

        for (FilterFieldsContainerInfo lContainerInfo : pFieldData.getFieldsContainerHierarchy()) {
            if (lContainerInfo.getId().equals(lContainerId)) {
                lFullId.append(lContainerInfo.getId());
                break;
            }
            else {
                lFullId.append(lContainerInfo.getId());
                lFullId.append('|');
            }
        }

        final CriteriaData lAdditionalConstraint =
                additionalConstraints.getAdditionalConstraints().get(
                        lFullId.toString());

        if (lAdditionalConstraint == null) {
            return getFromClause();
        }
        else {
            final GPMQuery lSubQuery = new GPMQuery(false);
            final FilterAdditionalConstraints lEmptyConstraints =
                    new FilterAdditionalConstraints(null, Collections.EMPTY_MAP);

            // Return only id -> no lock
            final boolean lIdOnly = pConfig.isOnlyIdentifier();
            pConfig.setOnlyIdentifier(true);

            // Generate from clause
            final IContainerQueryGenerator lContainerQueryGenerator =
                    ContainerQueryGeneratorFactory.getInstance().getContainerQueryGenerator(
                            lContainerId, pFilterFieldsContainerInfo.getType(),
                            lEmptyConstraints);

            lContainerQueryGenerator.generate(lSubQuery, pConfig);
            lSubQuery.setMainAlias(lContainerQueryGenerator.getAlias());

            // Generate where clause
            final String lConstraint =
                    CriteriaQueryGeneratorFactory.getInstance().getCriteriaQueryGenerator(
                            lAdditionalConstraint, pConfig, lEmptyConstraints).generateCriterion(
                            lSubQuery, pConfig);
            String lSubRequest =
                    "SELECT " + lContainerQueryGenerator.getAlias()
                            + ".* FROM " + lSubQuery.getFromClause();

            if (!StringUtils.isEmpty(lConstraint)) {
                lSubRequest += " WHERE " + lConstraint;
            }

            pConfig.setOnlyIdentifier(lIdOnly);

            return '(' + lSubRequest + ')';
        }
    }
}
/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.util.Collection;

import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator;
import org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;

/**
 * Generator for filter that more than one fields container.
 * <p>
 * Generates query for each fields container using
 * {@link MonoFieldsContainerQueryGenerator} and link them with a UNION.
 * 
 * @author mkargbo
 */
public class MultiFieldsContainerQueryGenerator extends
        AbstractFilterQueryGenerator {
    /**
     * MultiFieldsContainerQueryGenerator constructor.
     * 
     * @param pFilter
     *            Filter to analyze for query generation.
     * @param pAdditionalConstraints
     *            The additional constraints to add on the filter.
     */
    public MultiFieldsContainerQueryGenerator(
            final ExecutableFilterData pFilter,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        super(pFilter, pAdditionalConstraints, new GPMQuery(false));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate()
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator) {
        // Generate query for each fields container
        // query included criteria and results (no sort).
        final GPMQuery lUnionQuery = new GPMQuery();

        for (String lFieldsContainerId : filter.getFilterData().getFieldsContainerIds()) {
            final GPMQuery lTmpQuery = new GPMQuery(false);
            final String lQuery =
                    new MonoFieldsContainerQueryGenerator(filter,
                            additionalConstraints, lFieldsContainerId, true,
                            lTmpQuery).generate(pFilterQueryConfigurator);

            lUnionQuery.appendToUnionClause(lQuery);
            // Get all the generated field alias
            gpmQuery.copyFieldAlias(lTmpQuery);
        }

        final String lAlias =
                gpmQuery.generateAlias(AliasType.UNION.name(), AliasType.UNION);

        gpmQuery.setMainAlias(lAlias);
        // Append id of the container
        gpmQuery.appendToSelectClause(lAlias + ".id");
        gpmQuery.appendToGroupByClause(lAlias + ".id");

        // Append additional information according to the type
        switch (filter.getFilterData().getType()) {
            case SHEET:
                gpmQuery.appendToSelectClause(lAlias + ".ref");
                gpmQuery.appendToGroupByClause(lAlias + ".ref");
                gpmQuery.appendToSelectClause(lAlias + ".owner");
                gpmQuery.appendToGroupByClause(lAlias + ".owner");
                gpmQuery.appendToSelectClause(lAlias + ".lock_type");
                gpmQuery.appendToGroupByClause(lAlias + ".lock_type");
                gpmQuery.appendToSelectClause(lAlias + ".sheetTypeId");
                gpmQuery.appendToGroupByClause(lAlias + ".sheetTypeId");
                gpmQuery.appendToSelectClause(lAlias + ".sheetTypeName");
                gpmQuery.appendToGroupByClause(lAlias + ".sheetTypeName");
                break;
            default:
                // Do nothing
        }

        // The union represents a new table on which the DB will 
        // looking results and/or sorting results
        // criteria have been already used.
        gpmQuery.addElementInFromClause("(" + lUnionQuery.getCompleteQuery()
                + ")", lAlias);

        handleResults(lAlias, pFilterQueryConfigurator);
        handleSortingResult(pFilterQueryConfigurator);

        if (pFilterQueryConfigurator.isOnlyIdentifier()) {
            return gpmQuery.getCompleteQuery(true);
        }
        else {
            return gpmQuery.getCompleteQuery();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#generate(org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator,
     *      java.util.Collection)
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers) {
        //Generate query for each fields container
        //query included criteria and results (no sort).
        GPMQuery lUnionQuery = new GPMQuery();
        for (String lFieldsContainerId : filter.getFilterData().getFieldsContainerIds()) {
            String lQuery =
                    new MonoFieldsContainerQueryGenerator(filter,
                            additionalConstraints, lFieldsContainerId, true,
                            new GPMQuery(false)).generate(
                            pFilterQueryConfigurator, pContainerIdentifiers);
            lUnionQuery.appendToUnionClause(lQuery);
        }

        String lAlias =
                gpmQuery.generateAlias(AliasType.UNION.name(), AliasType.UNION);
        gpmQuery.setMainAlias(lAlias);
        //Append id of the container
        gpmQuery.appendToSelectClause(lAlias + ".id");
        gpmQuery.appendToGroupByClause(lAlias + ".id");

        //Append additional information according to the type
        switch (filter.getFilterData().getType()) {
            case SHEET:
                gpmQuery.appendToSelectClause(lAlias + ".ref");
                gpmQuery.appendToGroupByClause(lAlias + ".ref");
                gpmQuery.appendToSelectClause(lAlias + ".owner");
                gpmQuery.appendToGroupByClause(lAlias + ".owner");
                gpmQuery.appendToSelectClause(lAlias + ".lock_type");
                gpmQuery.appendToGroupByClause(lAlias + ".lock_type");
                gpmQuery.appendToSelectClause(lAlias + ".sheetTypeId");
                gpmQuery.appendToGroupByClause(lAlias + ".sheetTypeId");
                gpmQuery.appendToSelectClause(lAlias + ".sheetTypeName");
                gpmQuery.appendToGroupByClause(lAlias + ".sheetTypeName");
                break;
            default:
                //Do nothing
        }

        //The union represents a new table on which the DB will 
        //looking results and/or sorting results
        //criteria have been already used.
        gpmQuery.addElementInFromClause("(" + lUnionQuery.getCompleteQuery()
                + ")", lAlias);

        handleResults(lAlias, pFilterQueryConfigurator);
        handleSortingResult(pFilterQueryConfigurator);

        return gpmQuery.getCompleteQuery();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#doGeneratorOrder(org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator,
     *      java.lang.String)
     */
    @Override
    protected void doGeneratorOrder(IFieldQueryGenerator pFieldQueryGenerator,
            String pOrder) {
        pFieldQueryGenerator.generateUnionOrder(gpmQuery, pOrder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#doGeneratorResult(org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator)
     */
    @Override
    protected void doGeneratorResult(IFieldQueryGenerator pFieldQueryGenerator) {
        pFieldQueryGenerator.generateUnionResult(gpmQuery);
    }
}

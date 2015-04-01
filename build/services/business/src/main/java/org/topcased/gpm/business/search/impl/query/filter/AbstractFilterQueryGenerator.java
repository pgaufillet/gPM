/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.filter;

import java.util.Collection;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.IQueryGenerator;
import org.topcased.gpm.business.search.impl.query.field.FieldQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator;
import org.topcased.gpm.business.search.impl.query.util.LevelHandler;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * This implementation offers some basic implementation for:
 * <ul>
 * <li>Generating results</li>
 * <li>Generating sorting results</li>
 * <li>Generating level</li>
 * </ul>
 * This generator handle filter on several level.
 * 
 * @author mkargbo
 */
public abstract class AbstractFilterQueryGenerator implements IQueryGenerator {
    protected final ExecutableFilterData filter;

    protected final FilterAdditionalConstraints additionalConstraints;

    protected final GPMQuery gpmQuery;

    /**
     * Constructs a filter query generator.
     * 
     * @param pFilter
     *            Filter to generate.
     * @param pAdditionalConstraints
     *            Constraints to append to the filter
     * @param pGpmQuery
     *            Query to fill.
     */
    public AbstractFilterQueryGenerator(final ExecutableFilterData pFilter,
            final FilterAdditionalConstraints pAdditionalConstraints,
            final GPMQuery pGpmQuery) {
        filter = pFilter;
        gpmQuery = pGpmQuery;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate()
     */
    public abstract String generate(
            FilterQueryConfigurator pFilterQueryConfigurator);

    /**
     * Generates a SQL query for the specified container identifiers.
     * 
     * @param pFilterQueryConfigurator
     *            Filter query configurator
     * @param pContainerIdentifiers
     *            Container identifiers to use to for the query.
     * @return Generated query
     */
    public abstract String generate(
            FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        throw new NotImplementedException(
                "This method cannot generate a filter query");
    }

    /**
     * Generate sorting results
     * 
     * @param pFilterQueryConfigurator
     *            Filter configurator
     */
    protected void handleSortingResult(
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        if (filter.getResultSortingData() != null) {
            String lAlias = gpmQuery.getMainAlias();
            if (filter.getResultSortingData().getSortingFieldDatas() != null) {
                //Handle sorting results
                for (SortingFieldData lSortingFieldData : filter.getResultSortingData().getSortingFieldDatas()) {
                    //Handle hierarchy
                    lAlias =
                            LevelHandler.handleLevel(gpmQuery,
                                    pFilterQueryConfigurator, lAlias,
                                    lSortingFieldData.getUsableFieldData(),
                                    additionalConstraints);
                    gpmQuery.setAlias(lAlias);
                    IFieldQueryGenerator lFieldQueryGenerator =
                            FieldQueryGeneratorFactory.getInstance().getFieldQueryGenerator(
                                    lSortingFieldData.getUsableFieldData());
                    doGeneratorOrder(lFieldQueryGenerator,
                            lSortingFieldData.getOrder());
                }
            }
        }
    }

    /**
     * Generate results.
     * <p>
     * The alias is updated if the result is on several level.
     * 
     * @param pAlias
     *            Alias of the container principal container
     * @param pFilterQueryConfigurator
     *            Filter configurator
     */
    protected void handleResults(final String pAlias,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        String lAlias = pAlias;
        //Create results query
        for (UsableFieldData lUsableFieldData : filter.getResultSummaryData().getUsableFieldDatas()) {
            //Handle hierarchy
            lAlias =
                    LevelHandler.handleLevel(gpmQuery,
                            pFilterQueryConfigurator, lAlias, lUsableFieldData,
                            additionalConstraints);
            gpmQuery.setAlias(lAlias);
            IFieldQueryGenerator lFieldQueryGenerator =
                    FieldQueryGeneratorFactory.getInstance().getFieldQueryGenerator(
                            lUsableFieldData);
            doGeneratorResult(lFieldQueryGenerator);
        }
    }

    /**
     * Method to use to generate the result query part.
     * 
     * @param pFieldQueryGenerator
     *            Filter configurator
     */
    protected abstract void doGeneratorResult(
            final IFieldQueryGenerator pFieldQueryGenerator);

    /**
     * Method to use to generate the sort query part.
     * 
     * @param pFieldQueryGenerator
     *            Filter configurator
     * @param pOrder
     *            Order operator
     */
    protected abstract void doGeneratorOrder(
            final IFieldQueryGenerator pFieldQueryGenerator, final String pOrder);
}
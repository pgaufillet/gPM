/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.container.ContainerQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.container.IContainerQueryGenerator;
import org.topcased.gpm.business.search.impl.query.criteria.CriteriaQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.field.FieldQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator;
import org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator;
import org.topcased.gpm.business.search.impl.query.util.LevelHandler;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Generator for filter that has at least one fields container.
 * 
 * @author mkargbo
 */
public class MonoFieldsContainerQueryGenerator extends
        AbstractFilterQueryGenerator {
    private String fieldsContainerId;

    private boolean union = false;

    /**
     * MonoFieldsContainerQueryGenerator constructor.
     * 
     * @param pFilter
     *            Filter to analyze for query generation.
     * @param pAdditionalConstraints
     *            The additional constraints to add on the filter.
     * @param pFieldsContainerId
     *            Identifier of the only fields container.
     * @param pForUnion
     *            True for generating sorting results, false otherwise.
     * @param pQuery
     *            The query.
     */
    public MonoFieldsContainerQueryGenerator(
            final ExecutableFilterData pFilter,
            final FilterAdditionalConstraints pAdditionalConstraints,
            final String pFieldsContainerId, final boolean pForUnion,
            final GPMQuery pQuery) {
        super(pFilter, pAdditionalConstraints, pQuery);
        fieldsContainerId = pFieldsContainerId;
        union = pForUnion;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#generate()
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator) {
        //Create container query
        IContainerQueryGenerator lContainerQueryGenerator =
                ContainerQueryGeneratorFactory.getInstance().getContainerQueryGenerator(
                        fieldsContainerId,
                        FieldsContainerType.valueOf(filter.getFilterData().getType().name()),
                        additionalConstraints);
        lContainerQueryGenerator.generate(gpmQuery, pFilterQueryConfigurator);
        String lAlias = lContainerQueryGenerator.getAlias();
        gpmQuery.setMainAlias(lAlias);
        gpmQuery.setAlias(lAlias);

        // Create criteria query
        final CriteriaData lFilterCriteria =
                filter.getFilterData().getCriteriaData();
        final CriteriaData lAdditionnalCriteria =
                additionalConstraints.getAdditionalConstraints().get(
                        fieldsContainerId);
        final CriteriaData lGlobalCriteria;

        if (lFilterCriteria == null) {
            if (lAdditionnalCriteria == null) {
                lGlobalCriteria = null;
            }
            else {
                lGlobalCriteria = lAdditionnalCriteria;
            }
        }
        else {
            if (lAdditionnalCriteria == null) {
                lGlobalCriteria = lFilterCriteria;
            }
            else {
                lGlobalCriteria =
                        new OperationData(Operators.AND,
                                new CriteriaData[] { lAdditionnalCriteria,
                                                    lFilterCriteria });
            }
        }
        if (lGlobalCriteria != null) {
            final String lCriterion =
                    CriteriaQueryGeneratorFactory.getInstance().getCriteriaQueryGenerator(
                            lGlobalCriteria, pFilterQueryConfigurator,
                            additionalConstraints).generateCriterion(gpmQuery,
                            pFilterQueryConfigurator);

            if (StringUtils.isNotBlank(lCriterion)) {
                gpmQuery.addElementInWhereClause(lCriterion);
            }
        }

        if (pFilterQueryConfigurator.isOnlyIdentifier()) {
            handleSortingResult(pFilterQueryConfigurator);
            return gpmQuery.getCompleteQuery(true);
        }
        else {
            if (union) {
                handleMultiContainerResults(lAlias, pFilterQueryConfigurator);
                handleMultiContainerSortingResult(pFilterQueryConfigurator);
            }
            else {
                handleResults(lAlias, pFilterQueryConfigurator);
                handleSortingResult(pFilterQueryConfigurator);
            }
            return gpmQuery.getCompleteQuery();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#generate(org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator,
     *      java.util.List)
     */
    @Override
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers) {
        //Create container query
        IContainerQueryGenerator lContainerQueryGenerator =
                ContainerQueryGeneratorFactory.getInstance().getContainerQueryGenerator(
                        fieldsContainerId,
                        FieldsContainerType.valueOf(filter.getFilterData().getType().name()),
                        additionalConstraints);
        lContainerQueryGenerator.generate(gpmQuery, pFilterQueryConfigurator);
        String lAlias = lContainerQueryGenerator.getAlias();
        gpmQuery.setMainAlias(lAlias);
        gpmQuery.setAlias(lAlias);

        if (!pContainerIdentifiers.isEmpty()) {
            Collection<String> lContainerIds =
                    new HashSet<String>(pContainerIdentifiers.size());
            for (String lIdentifier : pContainerIdentifiers) {
                lContainerIds.add("'" + lIdentifier + "'");
            }
            StrBuilder lContainers = new StrBuilder();

            lContainers.appendWithSeparators(lContainerIds, ",");
            gpmQuery.addElementInWhereClause(gpmQuery.getMainAlias() + ".id"
                    + " IN (" + lContainers.toString() + ")");
        }

        if (union) {
            handleMultiContainerResults(lAlias, pFilterQueryConfigurator);
            handleMultiContainerSortingResult(pFilterQueryConfigurator);
        }
        else {
            handleResults(lAlias, pFilterQueryConfigurator);
            handleSortingResult(pFilterQueryConfigurator);
        }

        return gpmQuery.getCompleteQuery();
    }

    /***
     * To add field in sub-query result to be used by union query.
     * 
     * @param pFilterQueryConfigurator
     */
    private void handleMultiContainerSortingResult(
            FilterQueryConfigurator pFilterQueryConfigurator) {
        if (filter.getResultSortingData() != null
                && filter.getResultSortingData().getSortingFieldDatas() != null) {
            String lAlias = gpmQuery.getMainAlias();
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
                lFieldQueryGenerator.generateMultiContainerSort(gpmQuery);
            }
        }
    }

    private void handleMultiContainerResults(final String pAlias,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        if (filter.getResultSummaryData() != null) {
            String lAlias = pAlias;
            //Create results query
            for (UsableFieldData lUsableFieldData :
                filter.getResultSummaryData().getUsableFieldDatas()) {
                //Handle hierarchy
                lAlias =
                        LevelHandler.handleLevel(gpmQuery,
                                pFilterQueryConfigurator, lAlias,
                                lUsableFieldData, additionalConstraints);
                gpmQuery.setAlias(lAlias);
                IFieldQueryGenerator lFieldQueryGenerator =
                        FieldQueryGeneratorFactory.getInstance().getFieldQueryGenerator(
                                lUsableFieldData);
                lFieldQueryGenerator.generateMultiContainerResult(gpmQuery);
            }
        }
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
        pFieldQueryGenerator.generateOrder(gpmQuery, pOrder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.filter.AbstractFilterQueryGenerator#doGeneratorResult(org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator)
     */
    @Override
    protected void doGeneratorResult(IFieldQueryGenerator pFieldQueryGenerator) {
        pFieldQueryGenerator.generateResult(gpmQuery);
    }
}

/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.criteria;

import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.field.FieldQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator;
import org.topcased.gpm.business.search.impl.query.util.LevelHandler;

/**
 * Generator for Field criterion.
 * <p>
 * Handle criterion for filter on several level.
 * 
 * @author mkargbo
 */
public class CriterionQueryGenerator extends AbstractCriteriaQueryGenerator {
    private CriteriaFieldData criteriaFieldData;

    private FilterAdditionalConstraints additionalConstraints;

    /**
     * CriterionQueryGenerator constructor.
     * 
     * @param pCriteriaFieldData
     *            Field criterion to analyze for generation.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public CriterionQueryGenerator(final CriteriaFieldData pCriteriaFieldData,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        criteriaFieldData = pCriteriaFieldData;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.criteria.AbstractCriteriaQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator)
     */
    @Override
    public String generateCriterion(final GPMQuery pQuery,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        final String lAlias =
                LevelHandler.handleLevel(pQuery, pFilterQueryConfigurator,
                        pQuery.getAlias(),
                        criteriaFieldData.getUsableFieldData(),
                        additionalConstraints);

        pQuery.setAlias(lAlias);

        final IFieldQueryGenerator lFieldQueryGenerator =
                FieldQueryGeneratorFactory.getInstance().getFieldQueryGenerator(
                        criteriaFieldData.getUsableFieldData());

        lFieldQueryGenerator.setCriteriaFieldData(criteriaFieldData);

        return lFieldQueryGenerator.generateCriterion(pQuery,
                criteriaFieldData, pFilterQueryConfigurator);
    }
}
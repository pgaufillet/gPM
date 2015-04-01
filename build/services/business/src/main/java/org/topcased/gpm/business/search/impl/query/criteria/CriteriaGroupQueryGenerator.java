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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;

/**
 * Generator for Operation criterion.
 * <p>
 * An operation is consider as a group.
 * 
 * @author mkargbo
 */
public class CriteriaGroupQueryGenerator extends AbstractCriteriaQueryGenerator {
    private OperationData operationData;

    private String criterion;

    private FilterAdditionalConstraints additionalConstraints;

    /**
     * CriteriaGroupQueryGenerator constructor
     * 
     * @param pOperationData
     *            Operation to analyze for generation
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public CriteriaGroupQueryGenerator(final OperationData pOperationData,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        operationData = pOperationData;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.criteria.AbstractCriteriaQueryGenerator#generateCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator)
     */
    @Override
    public String generateCriterion(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        if (operationData.getCriteriaDatas().length > 0) {
            Collection<String> lCriteria = new ArrayList<String>();
            for (CriteriaData lCriteriaData : operationData.getCriteriaDatas()) {
                String lCriterion =
                        CriteriaQueryGeneratorFactory.getInstance().getCriteriaQueryGenerator(
                                lCriteriaData, pFilterQueryConfigurator,
                                additionalConstraints).generateCriterion(
                                pQuery, pFilterQueryConfigurator);

                if (StringUtils.isNotBlank(lCriterion)) {
                    lCriteria.add(lCriterion);
                }
            }

            if (!lCriteria.isEmpty() && lCriteria.size() > 1) {
                StrBuilder lStrBuilder = new StrBuilder("(");
                lStrBuilder.appendWithSeparators(lCriteria, " "
                        + operationData.getOperator() + " ");
                lStrBuilder.append(")");
                criterion = lStrBuilder.toString();
            }
        }
        return criterion;
    }
}

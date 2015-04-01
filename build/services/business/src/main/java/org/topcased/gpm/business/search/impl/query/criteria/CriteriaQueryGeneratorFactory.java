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

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;

/**
 * CriteriaQueryGeneratorFactory can create criterion generator instance for
 * Operation or Field.
 * <ul>
 * <li>Operation criterion: create {@link CriteriaGroupQueryGenerator} object</li>
 * <li>Field criterion: create {@link CriterionQueryGenerator} object</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class CriteriaQueryGeneratorFactory {

    private static final CriteriaQueryGeneratorFactory INSTANCE =
            new CriteriaQueryGeneratorFactory();

    /**
     * Get the CriteriaQueryGeneratorFactory unique instance
     * 
     * @return CriteriaQueryGeneratorFactory instance
     */
    public static final CriteriaQueryGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get the criterion generator according to the criterion.
     * 
     * @param pCriteriaData
     *            Criterion to analyze for generation.
     * @param pFilterQueryConfigurator
     *            The config.
     * @param pAdditionalConstraints
     *            The additional constraints.
     * @return Criterion generator
     * @throws NotImplementedException
     *             If the criterion is not a Operation or a Field criterion
     */
    public AbstractCriteriaQueryGenerator getCriteriaQueryGenerator(
            final CriteriaData pCriteriaData,
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final FilterAdditionalConstraints pAdditionalConstraints)
        throws NotImplementedException {
        if (pCriteriaData instanceof CriteriaFieldData) {
            return new CriterionQueryGenerator(
                    (CriteriaFieldData) pCriteriaData, pAdditionalConstraints);
        }
        else if (pCriteriaData instanceof OperationData) {
            return new CriteriaGroupQueryGenerator(
                    (OperationData) pCriteriaData, pAdditionalConstraints);
        }
        else {
            throw new NotImplementedException(
                    pCriteriaData.getClass().getName() + " is not implemented");
        }
    }
}
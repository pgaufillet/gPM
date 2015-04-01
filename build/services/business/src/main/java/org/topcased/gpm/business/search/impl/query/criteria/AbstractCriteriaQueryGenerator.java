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

import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.IQueryGenerator;

/**
 * Implementation of {@link IQueryGenerator} for Criteria.
 * 
 * @author mkargbo
 */
public abstract class AbstractCriteriaQueryGenerator {
    public abstract String generateCriterion(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator);
}
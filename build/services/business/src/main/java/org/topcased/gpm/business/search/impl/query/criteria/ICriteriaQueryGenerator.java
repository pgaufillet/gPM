/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.criteria;

import org.topcased.gpm.business.search.criterias.impl.GPMQuery;

/**
 * ICriteriaQueryGenerator
 * 
 * @author mkargbo
 */
public interface ICriteriaQueryGenerator {

    public void generate(GPMQuery pQuery);
}

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

import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.IQueryGenerator;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * The IContainerQueryGenerator implementations generates query for containers.
 * 
 * @author mkargbo
 */
public interface IContainerQueryGenerator extends IQueryGenerator {

    /**
     * Get the alias
     * 
     * @return The alias
     */
    public String getAlias();

    /**
     * Generates the query for a fields container.
     * <p>
     * This fields container will be joined to the specified alias.
     * <p>
     * Use for several level usable field (virtual field)
     * 
     * @param pQuery Query to fill.
     * @param pFilterQueryConfigurator the filter configurator 
     * @param pJoinTo Alias of the container to join
     * @param pFilterFieldsContainerInfo Fields container to analyze for generation
     * @param pUsableFieldData Virtual field associated to this fields container.
     * @param pPreviousContainerAlias Alias of the previous container
     * @param pLevel the level
     * @return Alias of the fields container
     */
    public String generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator, String pJoinTo,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo,
            UsableFieldData pUsableFieldData, String pPreviousContainerAlias,
            int pLevel);
}

/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.util;

import org.apache.commons.collections.CollectionUtils;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.container.ContainerQueryGeneratorFactory;
import org.topcased.gpm.business.search.impl.query.container.IContainerQueryGenerator;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Handler for filter on several level. Classes which want to support filter on
 * several level functionality must implements this interface.
 * 
 * @author mkargbo
 */
public class LevelHandler {
    /**
     * Handle level query generation for this usable field.
     * 
     * @param pQuery
     *            Query to fill.
     * @param pFilterQueryConfigurator
     *            Config.
     * @param pAlias
     *            Current alias.
     * @param pUsableFieldData
     *            Usable field (a Virtual field).
     * @param pAdditionalConstraints
     *            The additional constraints.
     * @return Alias of the container of the field (last container in the
     *         hierarchy).
     */
    public final static String handleLevel(final GPMQuery pQuery,
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final String pAlias, final UsableFieldData pUsableFieldData,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        String lAlias = pQuery.getMainAlias();
        // We store the two previously generated aliases, and use it to avoid
        // joining a sheet with "itself" when joining sheet tables with link tables
        String lPreviousAlias = pQuery.getMainAlias();
        String lInterAlias = pQuery.getMainAlias();

        //Handle hierarchy
        if (CollectionUtils.isNotEmpty(pUsableFieldData.getFieldsContainerHierarchy())) {
            //Do not generate hierarchy a second time.
            int lLevel = 0;
            if (!pQuery.isAlreadyMapped(pUsableFieldData.getId() + "_level"
                    + lLevel)) {
                for (FilterFieldsContainerInfo lFieldsContainerInfo : pUsableFieldData.getFieldsContainerHierarchy()) {
                    final IContainerQueryGenerator lContainerQueryGenerator =
                            ContainerQueryGeneratorFactory.getInstance().getContainerQueryGenerator(
                                    lFieldsContainerInfo.getId(),
                                    lFieldsContainerInfo.getType(),
                                    pAdditionalConstraints);

                    // Use lPreviousAlias > Previous container alias
                    lAlias =
                            lContainerQueryGenerator.generate(pQuery,
                                    pFilterQueryConfigurator, lAlias,
                                    lFieldsContainerInfo, pUsableFieldData,
                                    lPreviousAlias, lLevel);
                    // Shift aliases backup
                    lPreviousAlias = lInterAlias;
                    lInterAlias = lAlias;
                    lLevel++;
                }
                pQuery.putMappedAlias(pUsableFieldData.getId() + "_level"
                        + lLevel, lAlias);
            }
            else {
                lAlias =
                        pQuery.getMappedAlias(pUsableFieldData.getId()
                                + "_level" + lLevel);
            }
        }

        //This alias corresponds to the last container of the virtual field hierarchy.
        //The container containing the field.
        return lAlias;
    }
}
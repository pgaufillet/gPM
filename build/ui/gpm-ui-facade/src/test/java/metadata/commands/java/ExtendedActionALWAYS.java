/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/

package metadata.commands.java;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.common.extensions.ResultingScreen;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class ExtendedActionALWAYS implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    @SuppressWarnings("unchecked")
	public boolean execute(Context pContext) {

        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);
        String lProcessName =
                pContext.get(ExtensionPointParameters.PROCESS_NAME);
        ServiceLocator lServiceLocator =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR);
        String lProductName =
                pContext.get(ExtensionPointParameters.PRODUCT_NAME);

        List<String> lResultIDs = new ArrayList<String>();

        ExecutableFilterData lFilterData =
                lServiceLocator.getSearchService().getExecutableFilterByName(
                        lRoleToken, lProcessName, null, null,
                        "SHEET_1 LIST TABLE");

        List<SummaryData> lResultList =
                IteratorUtils.toList(lServiceLocator.getSearchService().executeFilter(
                        lRoleToken, lFilterData,
                        lFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator()));

        if (lResultList != null) {
            for (SummaryData lSummaryData : lResultList) {
                lResultIDs.add(lSummaryData.getId());
            }
        }

        pContext.set(ExtensionsService.RESULT_SCREEN,
                ResultingScreen.FILTER_RESULT);
        pContext.set(ExtensionsService.RESULT_SHEET_IDS, lResultIDs);
        pContext.set(ExtensionsService.RESULT_SUMMARY_NAME,
                lFilterData.getResultSummaryData().getLabelKey());
        pContext.set(ExtensionsService.RESULT_SORTER_NAME,
                lFilterData.getResultSortingData().getLabelKey());
        pContext.set(ExtensionsService.RESULT_PRODUCT_NAME, lProductName);
        pContext.set(ExtensionsService.RESULT_SCOPE,
                FilterScope.INSTANCE_FILTER);
        pContext.set(ExtensionsService.RESULT_FILTER_TYPE, FilterTypeData.SHEET);

        return true;
    }

}

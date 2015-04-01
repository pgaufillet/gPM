/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin), Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.sheet.service.SheetTypeData;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * Helper methods used by sheet service.
 * 
 * @author tszadel
 * @author llatil
 */
public class SheetUtils extends ServiceImplBase {

    /**
     * Creates a SheetSummaryData.
     * 
     * @param pSheet
     *            The Sheet.
     * @return Newly created SheetSummaryData
     */
    public SheetSummaryData createSheetSummary(final Sheet pSheet) {
        SheetSummaryData lSummaryData = new SheetSummaryData();

        lSummaryData.setId(pSheet.getId());

        SheetType lSheetType = getSheetType(pSheet);

        // TODO Define a better sheet name. For now, concatenate type
        // name + id.
        lSummaryData.setSheetName(lSheetType.getName() + "-" + pSheet.getId());
        lSummaryData.setSheetReference(pSheet.getReference());

        lSummaryData.setSelectable(lSheetType.isSelectable());
        lSummaryData.setSheetTypeId(lSheetType.getId());
        lSummaryData.setSheetType(lSheetType.getName());

        return lSummaryData;
    }

    /**
     * Create a SheetTypeData from a SheetType
     * 
     * @param pType
     *            source SheetType
     * @return Newly created SheetTypeData
     */
    public SheetTypeData createSheetTypeData(final SheetType pType) {
        return createSheetTypeData(null, pType);
    }

    /**
     * Create a SheetTypeData from a SheetType
     * 
     * @param pRoleToken
     *            Role token (use for i18nService)
     * @param pType
     *            source SheetType
     * @return Newly created SheetTypeData
     */
    public SheetTypeData createSheetTypeData(final String pRoleToken,
            final SheetType pType) {
        SheetTypeData lSheetTypeData = new SheetTypeData();

        lSheetTypeData.setId(pType.getId());

        if ((StringUtils.isNotBlank(pType.getDescription()))
                && (StringUtils.isNotBlank(pRoleToken))) {
            //For upward compatibility. Previous method signature doesn't have roletoken parameter.
            lSheetTypeData.setDescription(getI18nService().getValueForUser(
                    pRoleToken, pType.getDescription()));
        }
        else {
            lSheetTypeData.setDescription(pType.getDescription());
        }
        lSheetTypeData.setName(pType.getName());
        lSheetTypeData.setSelectable(pType.isSelectable());

        return lSheetTypeData;
    }

    /**
     * Create a list of sheet summaries from a list of sheets
     * 
     * @param pSheets
     *            List of sheets
     * @return Newly created list of SheetSummaryData
     */
    public List<SheetSummaryData> createSheetSummaryList(
            final Collection<? extends Sheet> pSheets) {
        List<SheetSummaryData> lSheetSummaries =
                new ArrayList<SheetSummaryData>(pSheets.size());

        for (Sheet lSheet : pSheets) {
            lSheetSummaries.add(createSheetSummary(lSheet));
        }
        return lSheetSummaries;
    }

    /**
     * Convert a list of TransitionHistoryData into a SheetHistoryData array.
     * 
     * @param pTransitionData
     *            List of TransitionHistoryData
     * @return SheetHistoryData array
     */
    public static SheetHistoryData[] convertHistoryData(
            final List<? extends TransitionHistoryData> pTransitionData) {
        SheetHistoryData[] lHistory =
                new SheetHistoryData[pTransitionData.size()];
        int i = 0;
        for (TransitionHistoryData lTransition : pTransitionData) {
            lHistory[i++] =
                    new SheetHistoryData(lTransition.getLogin(),
                            lTransition.getOriginState(),
                            lTransition.getDestinationState(),
                            lTransition.getTransitionDate(),
                            lTransition.getTransitionName());
        }
        return lHistory;
    }

    /**
     * Convert an array of SheetHistoryData into a list of
     * TransitionHistoryData.
     * 
     * @param pSheetTransitionData
     *            Sheet history data to convert.
     * @return Transition history data.
     */
    public static List<TransitionHistoryData> convertHistoryData(
            final SheetHistoryData[] pSheetTransitionData) {
        List<TransitionHistoryData> lHistory =
                new ArrayList<TransitionHistoryData>(
                        pSheetTransitionData.length);
        for (SheetHistoryData lTransition : pSheetTransitionData) {
            TransitionHistoryData lTransitionHistoryData =
                    new TransitionHistoryData();
            lTransitionHistoryData.setLogin(lTransition.getLoginName());
            lTransitionHistoryData.setOriginState(lTransition.getOriginState());
            lTransitionHistoryData.setDestinationState(lTransition.getDestinationState());
            lTransitionHistoryData.setTransitionDate(lTransition.getChangeDate());
            lTransitionHistoryData.setTransitionName(lTransition.getTransitionName());
            lHistory.add(lTransitionHistoryData);
        }
        return lHistory;
    }
}

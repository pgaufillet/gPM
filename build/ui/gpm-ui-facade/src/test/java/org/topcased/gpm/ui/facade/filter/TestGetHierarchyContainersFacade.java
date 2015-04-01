/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetHierarchyContainersFacade
 * 
 * @author jlouisy
 */
public class TestGetHierarchyContainersFacade extends AbstractFacadeTestCase {

    private static final String SHEET_1_TYPE_NAME = "SHEET_1";

    private static final String CONFIDENTIAL_SHEET_TYPE_NAME =
            "CONFIDENTIAL_SHEET";

    private String print(Collection<UiFilterContainerHierarchy> pResult,
            String pContainerId, int pDepth, int pTabs) {
        String lResult = "";
        if (pDepth >= 0) {
            for (UiFilterContainerHierarchy lUiFilterContainerHierarchy : pResult) {
                if (pContainerId.equals(lUiFilterContainerHierarchy.getContainerId())) {
                    for (int i = 0; i < pTabs; i++) {
                        lResult += "\t";
                    }
                    lResult =
                            lResult
                                    + lUiFilterContainerHierarchy.getContainerName()
                                    + "\n";
                    if (lUiFilterContainerHierarchy.getChildren() != null) {
                        for (String[] lChild : lUiFilterContainerHierarchy.getChildren()) {
                            lResult +=
                                    print(pResult, lChild[0], pDepth - 1,
                                            pTabs + 1);
                            if (lChild[1] != null) {
                                lResult +=
                                        print(pResult, lChild[1], pDepth - 1,
                                                pTabs + 2);
                            }
                        }
                    }
                }
            }
        }
        return lResult;
    }

    /**
     * Normal case
     */
    public void testMultiContainerCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        String lSheet1Id =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(), SHEET_1_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        String lConfidentialSheetId =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(),
                        CONFIDENTIAL_SHEET_TYPE_NAME, CacheProperties.IMMUTABLE).getId();

        List<UiFilterContainerType> lContainerIdList =
                new ArrayList<UiFilterContainerType>();

        UiFilterContainerType lContainerType = new UiFilterContainerType();
        lContainerType.setId(lSheet1Id);
        lContainerType.setName(new Translation(SHEET_1_TYPE_NAME,
                SHEET_1_TYPE_NAME));
        lContainerIdList.add(lContainerType);

        lContainerType = new UiFilterContainerType();
        lContainerType.setId(lConfidentialSheetId);
        lContainerType.setName(new Translation(CONFIDENTIAL_SHEET_TYPE_NAME,
                CONFIDENTIAL_SHEET_TYPE_NAME));
        lContainerIdList.add(lContainerType);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Containers Hierarchy.");
        }
        Map<String, UiFilterContainerHierarchy> lResult =
                lFilterFacade.getHierarchyContainers(lUiSession,
                        lContainerIdList, FilterType.SHEET);

        // Make hierarchy
        String lHierarchy =
                print(lResult.values(), lSheet1Id, 0, 0)
                        + print(lResult.values(), lConfidentialSheetId, 0, 0);

        String lExpectedHierarchy = "SHEET_1\n" + "CONFIDENTIAL_SHEET\n";

        assertEquals(lExpectedHierarchy, lHierarchy);
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        String lSheet1Id =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(), SHEET_1_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        List<UiFilterContainerType> lContainerIdList =
                new ArrayList<UiFilterContainerType>();
        UiFilterContainerType lContainerType = new UiFilterContainerType();
        lContainerType.setId(lSheet1Id);
        lContainerType.setName(new Translation(SHEET_1_TYPE_NAME,
                SHEET_1_TYPE_NAME));
        lContainerIdList.add(lContainerType);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Containers Hierarchy.");
        }
        Map<String, UiFilterContainerHierarchy> lResult =
                lFilterFacade.getHierarchyContainers(lUiSession,
                        lContainerIdList, FilterType.SHEET);

        // Make hierarchy
        String lHierarchy = print(lResult.values(), lSheet1Id, 2, 0);

        String lExpectedHierarchy =
                "SHEET_1\n" + "\tSheet1ConfidentialSheetLink\n"
                        + "\t\tCONFIDENTIAL_SHEET\n" + "\t\t\tPRODUCT\n"
                        + "\tSheet1Sheet1Link\n" + "\t\tSHEET_1\n"
                        + "\t\t\tSheet1ConfidentialSheetLink\n"
                        + "\t\t\t\tCONFIDENTIAL_SHEET\n"
                        + "\t\t\tSheet1Sheet1Link\n" + "\t\t\t\tSHEET_1\n"
                        + "\t\t\tSheet1Sheet1Link_2\n" + "\t\t\t\tSHEET_1\n"
                        + "\t\t\tPRODUCT\n" + "\tSheet1Sheet1Link_2\n"
                        + "\t\tSHEET_1\n"
                        + "\t\t\tSheet1ConfidentialSheetLink\n"
                        + "\t\t\t\tCONFIDENTIAL_SHEET\n"
                        + "\t\t\tSheet1Sheet1Link\n" + "\t\t\t\tSHEET_1\n"
                        + "\t\t\tSheet1Sheet1Link_2\n" + "\t\t\t\tSHEET_1\n"
                        + "\t\t\tPRODUCT\n" + "\tPRODUCT\n";

        assertEquals(lExpectedHierarchy, lHierarchy);
    }

    /**
     * Normal case
     */
    public void testUserRightsCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession = loginAsUser().getSession(getProductName());

        String lSheet1Id =
                getSheetService().getCacheableSheetTypeByName(
                        lUiSession.getRoleToken(), SHEET_1_TYPE_NAME,
                        CacheProperties.IMMUTABLE).getId();

        List<UiFilterContainerType> lContainerIdList =
                new ArrayList<UiFilterContainerType>();
        UiFilterContainerType lContainerType = new UiFilterContainerType();
        lContainerType.setId(lSheet1Id);
        lContainerType.setName(new Translation(SHEET_1_TYPE_NAME,
                SHEET_1_TYPE_NAME));
        lContainerIdList.add(lContainerType);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Containers Hierarchy.");
        }
        Map<String, UiFilterContainerHierarchy> lResult =
                lFilterFacade.getHierarchyContainers(lUiSession,
                        lContainerIdList, FilterType.SHEET);

        // Make hierarchy
        String lHierarchy = print(lResult.values(), lSheet1Id, 2, 0);

        String lExpectedHierarchy =
                "SHEET_1\n" + "\tSheet1ConfidentialSheetLink\n"
                        + "\tSheet1Sheet1Link\n" + "\t\tSHEET_1\n"
                        + "\t\t\tSheet1ConfidentialSheetLink\n"
                        + "\t\t\tSheet1Sheet1Link\n" + "\t\t\t\tSHEET_1\n";

        assertEquals(lExpectedHierarchy, lHierarchy);
    }

}

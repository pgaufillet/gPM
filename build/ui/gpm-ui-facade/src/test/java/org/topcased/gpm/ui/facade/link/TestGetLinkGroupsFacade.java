/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.link;

import java.util.List;

import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * LinkFacade.getLinkGroups test
 * 
 * @author jlouisy
 */
public class TestGetLinkGroupsFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_LINK_TYPE_NAME = "ProductProductLink";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String LINK_TYPE_NAME_1 = "Sheet1Sheet1Link";

    private static final String LINK_TYPE_NAME_2 = "Sheet1Sheet1Link_2";

    private static final String ORIGIN_SHEET_REF = "REF_Origin_Sheet";

    private static final String DESTINATION_SHEET_REF = "REF_Destination_Sheet";

    /**
     * Normal case
     */
    public void testNotUpdatableSheetLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        ORIGIN_SHEET_REF);
        String lDestinationSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        DESTINATION_SHEET_REF);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create SHEET_1-SHEET_1_2 link.");
        }
        lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_2, lOriginSheetId,
                lDestinationSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Sheet1Sheet1_2 links.");
        }
        lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);

        List<Translation> lGroupList =
                lLinkFacade.getLinkGroups(lUiSession, lOriginSheetId);

        assertEquals(1, lGroupList.size());

        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testProductLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        String lRoleToken = lUiSession.getRoleToken();

        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        ProductService lProductService = getProductService();

        CacheableProductType lCacheableProductType =
                lProductService.getCacheableProductTypeByName(lRoleToken,
                        lUiSession.getParent().getProcessName(),
                        PRODUCT_TYPE_NAME, CacheProperties.IMMUTABLE);

        List<CacheableProduct> lProducts =
                lProductService.getCacheableProductsByType(
                        lUiSession.getRoleToken(),
                        lCacheableProductType.getId(),
                        CacheProperties.IMMUTABLE);

        CacheableProduct lProduct1 = lProducts.get(0);
        CacheableProduct lProduct2 = lProducts.get(1);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Product link.");
        }
        lLinkFacade.createLink(lUiSession, PRODUCT_LINK_TYPE_NAME,
                lProduct1.getId(), lProduct2.getId());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get link groups.");
        }
        List<Translation> lGroupList =
                lLinkFacade.getLinkGroups(lUiSession, lProduct1.getId());
        assertEquals(1, lGroupList.size());
        Translation lLinkGroup = lGroupList.get(0);
        assertEquals(PRODUCT_LINK_TYPE_NAME, lLinkGroup.getValue());
    }

    /**
     * Normal case
     */
    public void testSheetLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        ORIGIN_SHEET_REF);
        String lDestinationSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        DESTINATION_SHEET_REF);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create 2 SHEET_1-SHEET_1 links.");
        }

        lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_1, lOriginSheetId,
                lDestinationSheetId);

        lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_1,
                lDestinationSheetId, lOriginSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create 1 SHEET_1-SHEET_1_2 link.");
        }

        lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_2, lOriginSheetId,
                lDestinationSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get link groups.");
        }
        List<Translation> lGroupList =
                lLinkFacade.getLinkGroups(lUiSession, lOriginSheetId);
        assertEquals(2, lGroupList.size());
        for (Translation lLinkGroup : lGroupList) {
            assertTrue(LINK_TYPE_NAME_1.equals(lLinkGroup.getValue())
                    || LINK_TYPE_NAME_2.equals(lLinkGroup.getValue()));
        }
    }
}

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
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * LinkFacade.getLinkGroups test
 * 
 * @author jlouisy
 */
public class TestGetLinksFacade extends AbstractFacadeTestCase {

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

        List<UiLink> lLinkList =
                lLinkFacade.getLinks(lUiSession, lOriginSheetId,
                        LINK_TYPE_NAME_2, DisplayMode.EDITION);
        assertEquals(1, lLinkList.size());
        for (BusinessField lUiField : lLinkList.get(0)) {
            assertFalse(lUiField.isUpdatable());
        }

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
        UiLink lUiLink =
                lLinkFacade.createLink(lUiSession, PRODUCT_LINK_TYPE_NAME,
                        lProduct1.getId(), lProduct2.getId());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get ProductProduct links.");
        }
        List<UiLink> lLinkList =
                lLinkFacade.getLinks(lUiSession, lProduct1.getId(),
                        PRODUCT_LINK_TYPE_NAME, DisplayMode.EDITION);
        assertEquals(1, lLinkList.size());
        assertEquals(lUiLink.getId(), lLinkList.get(0).getId());
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
        UiLink lUiLink11 =
                lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_1,
                        lOriginSheetId, lDestinationSheetId);

        UiLink lUiLink12 =
                lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME_1,
                        lDestinationSheetId, lOriginSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Sheet1Sheet1 links.");
        }
        List<UiLink> lLinkList =
                lLinkFacade.getLinks(lUiSession, lOriginSheetId,
                        LINK_TYPE_NAME_1, DisplayMode.EDITION);
        assertEquals(2, lLinkList.size());
        for (UiLink lUiLink : lLinkList) {
            assertEquals(LINK_TYPE_NAME_1, lUiLink.getTypeName());
        }
        String lFirstId = lLinkList.get(0).getId();
        String lSecondId = lLinkList.get(1).getId();
        boolean lResult =
                (lUiLink11.getId().equals(lFirstId) && lUiLink12.getId().equals(
                        lSecondId))
                        || (lUiLink12.getId().equals(lFirstId) && lUiLink11.getId().equals(
                                lSecondId));
        assertTrue(lResult);
    }
}

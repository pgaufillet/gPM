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

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * LinkFacade.createLink test
 * 
 * @author jlouisy
 */
public class TestDeleteLinkFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_LINK_TYPE_NAME = "ProductProductLink";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String LINK_TYPE_NAME = "Sheet1Sheet1Link";

    private static final String LINK_TYPE_NAME_NOT_DELETABLE =
            "Sheet1Sheet1Link_2";

    private static final String ORIGIN_SHEET_REF = "REF_Origin_Sheet";

    private static final String DESTINATION_SHEET_REF = "REF_Destination_Sheet";

    /**
     * Normal case
     */
    public void testNotDeletableSheetLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        //Get services
        SheetService lSheetService = getSheetService();

        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        ORIGIN_SHEET_REF);
        String lDestinationSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        DESTINATION_SHEET_REF);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create link.");
        }
        UiLink lUiLink =
                lLinkFacade.createLink(lUiSession,
                        LINK_TYPE_NAME_NOT_DELETABLE, lOriginSheetId,
                        lDestinationSheetId);

        lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete link.");
        }
        try {
            lLinkFacade.deleteLink(lUiSession, lUiLink.getId());
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException e) {
            assertEquals(
                    "Illegal access. Type 'Sheet1Sheet1Link_2' is not deletable.",
                    e.getMessage());
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
        LinkService lLinkService = getLinkService();

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
            LOGGER.info("Delete link.");
        }
        lLinkFacade.deleteLink(lUiSession, lUiLink.getId());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("Try to get deleted link.");
        }
        List<CacheableLink> lLinksList =
                lLinkService.getLinks(lRoleToken, lProduct1.getId(),
                        CacheProperties.IMMUTABLE);
        assertTrue("Link is not deleted", lLinksList.isEmpty());
    }

    /**
     * Normal case
     */
    public void testSheetLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        //Get services
        SheetService lSheetService = getSheetService();
        LinkService lLinkService = getLinkService();

        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        ORIGIN_SHEET_REF);
        String lDestinationSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        DESTINATION_SHEET_REF);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create link.");
        }
        UiLink lUiLink =
                lLinkFacade.createLink(lUiSession, LINK_TYPE_NAME,
                        lOriginSheetId, lDestinationSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete link.");
        }
        lLinkFacade.deleteLink(lUiSession, lUiLink.getId());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("Try to get deleted link.");
        }
        List<CacheableLink> lLinksList =
                lLinkService.getLinks(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);
        assertTrue("Link is not deleted", lLinksList.isEmpty());
    }
}

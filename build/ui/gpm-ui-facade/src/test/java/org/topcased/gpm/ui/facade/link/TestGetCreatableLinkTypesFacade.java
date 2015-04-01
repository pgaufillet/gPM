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
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetCreatableLinkTypesFacade
 * 
 * @author jlouisy
 */
public class TestGetCreatableLinkTypesFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_LINK_TYPE_NAME = "ProductProductLink";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String SHEET_LINK_TYPE_NAME_1 = "Sheet1Sheet1Link";

    private static final Object SHEET_LINK_TYPE_NAME_2 =
            "Sheet1ConfidentialSheetLink";

    private static final String ORIGIN_SHEET_REF = "REF_Origin_Sheet";

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

        CacheableProduct lProduct = lProducts.get(0);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get creatable Product link types.");
        }
        List<String> lLinkTypesList =
                lLinkFacade.getCreatableLinkTypes(lUiSession,
                        lProduct.getTypeId());

        //Link Attributes
        assertEquals(1, lLinkTypesList.size());
        assertEquals(PRODUCT_LINK_TYPE_NAME, lLinkTypesList.get(0));
    }

    /**
     * Normal case
     */
    public void testSheetLink() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        ORIGIN_SHEET_REF);
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get creatable Sheet link types.");
        }
        List<String> lLinkTypesList =
                lLinkFacade.getCreatableLinkTypes(lUiSession,
                        lCacheableSheet.getTypeId());

        //Link Attributes
        assertEquals(2, lLinkTypesList.size());
        assertTrue(lLinkTypesList.contains(SHEET_LINK_TYPE_NAME_1));
        assertTrue(lLinkTypesList.contains(SHEET_LINK_TYPE_NAME_2));

        logoutAsUser(lUiSession.getParent());
    }

}

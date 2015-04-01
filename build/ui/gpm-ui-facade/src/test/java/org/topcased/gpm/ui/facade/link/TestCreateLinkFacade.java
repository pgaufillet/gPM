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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
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
public class TestCreateLinkFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_LINK_TYPE_NAME = "ProductProductLink";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String PRODUCT_LINK_TYPE_DESC = "product_link_desc";

    private static final String PRODUCT_LINK_FIELD = "ProductProduct_F";

    private static final String PRODUCT_LINK_FIELD_DESC =
            "product link field desc";

    private static final String SHEET_LINK_TYPE_NAME = "Sheet1Sheet1Link";

    private static final String SHEET_LINK_TYPE_NAME_NOT_CREATABLE =
            "Sheet1Sheet1Link_2";

    private static final String SHEET_LINK_TYPE_DESC = "description";

    private static final String ORIGIN_SHEET_REF = "REF_Origin_Sheet";

    private static final String DESTINATION_SHEET_REF = "REF_Destination_Sheet";

    private static final String F_F3_FIELD = "Sheet1Sheet1_F3";

    private static final String F_F3_FIELD_DESC = "F3 Date FIELD";

    /**
     * Normal case
     */
    public void testNotCreatableSheetLink() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

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
        try {
            lLinkFacade.createLink(lUiSession,
                    SHEET_LINK_TYPE_NAME_NOT_CREATABLE, lOriginSheetId,
                    lDestinationSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException e) {
            assertEquals(
                    "Illegal access. Type 'Sheet1Sheet1Link_2' is not creatable.",
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

        //Link Attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiLink.getBusinessProcessName());
        assertEquals(PRODUCT_LINK_TYPE_NAME, lUiLink.getTypeName());
        assertTrue(lUiLink.isDeletable());
        assertTrue(lUiLink.isUpdatable());
        assertEquals(PRODUCT_LINK_TYPE_DESC, lUiLink.getTypeDescription());

        String lTypeId =
                lLinkService.getLinkTypeByName(lUiSession.getRoleToken(),
                        PRODUCT_LINK_TYPE_NAME, CacheProperties.IMMUTABLE).getId();
        assertEquals(lTypeId, lUiLink.getTypeId());

        assertEquals(lProduct1.getId(), lUiLink.getOriginId());
        assertEquals(lProduct1.getFunctionalReference(),
                lUiLink.getOriginReference());
        assertEquals(lProduct2.getId(), lUiLink.getDestinationId());
        assertEquals(lProduct2.getFunctionalReference(),
                lUiLink.getDestinationReference());

        //Fields values
        assertEquals(PRODUCT_LINK_FIELD_DESC, lUiLink.getDateField(
                PRODUCT_LINK_FIELD).getFieldDescription());
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
            LOGGER.info("Create link.");
        }
        UiLink lUiLink =
                lLinkFacade.createLink(lUiSession, SHEET_LINK_TYPE_NAME,
                        lOriginSheetId, lDestinationSheetId);

        //Link Attributes
        assertEquals(DEFAULT_PROCESS_NAME, lUiLink.getBusinessProcessName());
        assertEquals(SHEET_LINK_TYPE_NAME, lUiLink.getTypeName());
        assertTrue(lUiLink.isDeletable());
        assertTrue(lUiLink.isUpdatable());
        assertEquals(SHEET_LINK_TYPE_DESC, lUiLink.getTypeDescription());

        LinkService lLinkService = getLinkService();
        String lTypeId =
                lLinkService.getLinkTypeByName(lUiSession.getRoleToken(),
                        SHEET_LINK_TYPE_NAME, CacheProperties.IMMUTABLE).getId();
        assertEquals(lTypeId, lUiLink.getTypeId());

        assertEquals(lOriginSheetId, lUiLink.getOriginId());
        assertEquals(ORIGIN_SHEET_REF, lUiLink.getOriginReference());
        assertEquals(lDestinationSheetId, lUiLink.getDestinationId());
        assertEquals(DESTINATION_SHEET_REF, lUiLink.getDestinationReference());

        //Fields values
        assertEquals(F_F3_FIELD_DESC,
                lUiLink.getDateField(F_F3_FIELD).getFieldDescription());

        SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyy.MM.dd");

        assertEquals(lDateFormat.format(new Date()),
                lDateFormat.format(lUiLink.getDateField(F_F3_FIELD).get()));
    }
}

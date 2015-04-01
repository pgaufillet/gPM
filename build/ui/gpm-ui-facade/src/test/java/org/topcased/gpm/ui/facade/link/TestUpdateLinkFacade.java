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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.link.BusinessLink;
import org.topcased.gpm.business.values.link.impl.cacheable.CacheableLinkAccess;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * LinkFacade.updateLink test
 * 
 * @author jlouisy
 */
public class TestUpdateLinkFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_LINK_TYPE_NAME = "ProductProductLink";

    private static final String PRODUCT_TYPE_NAME = "PRODUCT";

    private static final String SHEET_LINK_TYPE_NAME = "Sheet1Sheet1Link";

    private static final String ORIGIN_SHEET_REF = "REF_Origin_Sheet";

    private static final String DESTINATION_SHEET_REF = "REF_Destination_Sheet";

    private static final String F_FIELD = "Sheet1Sheet1";

    private static final String F_F1_FIELD = "Sheet1Sheet1_F1";

    private static final String F_F1_NEW_VALUE = "Sheet1Sheet1_F1 new value";

    private static final String F_F2_FIELD = "Sheet1Sheet1_F2";

    private static final String F_F2_NEW_VALUE = "CHOICE 5";

    private static final String PRODUCT_LINK_FIELD = "ProductProduct_F";

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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Change fields.");
        }
        List<UiField> lUpdatedFields = new ArrayList<UiField>();
        BusinessDateField lField = lUiLink.getDateField(PRODUCT_LINK_FIELD);
        lField.set(new Date());
        lUpdatedFields.add((UiField) lField);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update link.");
        }
        lLinkFacade.updateLink(lUiSession, lUiLink.getId(), lUpdatedFields);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get link.");
        }
        CacheableLink lCacheableLink =
                lLinkService.getLink(lUiSession.getRoleToken(),
                        lUiLink.getId(), CacheProperties.IMMUTABLE);
        CacheableLinkType lCacheableLinkType =
                lLinkService.getLinkTypeByName(lUiSession.getRoleToken(),
                        lUiLink.getTypeName(), CacheProperties.IMMUTABLE);
        BusinessLink lBusinessLink =
                new CacheableLinkAccess(lUiSession.getRoleToken(),
                        lCacheableLinkType, lCacheableLink,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        //Fields Values
        SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String lLinkValue =
                lDateFormat.format(lBusinessLink.getDateField(
                        PRODUCT_LINK_FIELD).get());
        assertEquals(lDateFormat.format(new Date()), lLinkValue);
    }

    /**
     * Normal case
     */
    public void testSheetLink() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

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
                lLinkFacade.createLink(lUiSession, SHEET_LINK_TYPE_NAME,
                        lOriginSheetId, lDestinationSheetId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Change fields.");
        }

        List<UiField> lUpdatedFields = new ArrayList<UiField>();

        BusinessMultivaluedField<BusinessMultipleField> lField =
                lUiLink.getMultivaluedMultipleField(F_FIELD);
        lField.get(0).getStringField(F_F1_FIELD).set(F_F1_NEW_VALUE);

        lField.get(0).getChoiceField(F_F2_FIELD).setCategoryValue(
                F_F2_NEW_VALUE);
        lUpdatedFields.add((UiField) lField);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update link.");
        }
        lLinkFacade.updateLink(lUiSession, lUiLink.getId(), lUpdatedFields);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get link.");
        }
        CacheableLink lCacheableLink =
                lLinkService.getLink(lUiSession.getRoleToken(),
                        lUiLink.getId(), CacheProperties.IMMUTABLE);
        CacheableLinkType lCacheableLinkType =
                lLinkService.getLinkTypeByName(lUiSession.getRoleToken(),
                        lUiLink.getTypeName(), CacheProperties.IMMUTABLE);
        BusinessLink lBusinessLink =
                new CacheableLinkAccess(lUiSession.getRoleToken(),
                        lCacheableLinkType, lCacheableLink,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        //Fields Values
        String lLinkValue =
                lBusinessLink.getMultivaluedMultipleField(F_FIELD).get(0).getStringField(
                        F_F1_FIELD).get();
        assertEquals(F_F1_NEW_VALUE, lLinkValue);

        lLinkValue =
                lBusinessLink.getMultivaluedMultipleField(F_FIELD).get(0).getChoiceField(
                        F_F2_FIELD).getCategoryValue();
        assertEquals(F_F2_NEW_VALUE, lLinkValue);
    }
}

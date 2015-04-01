/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.product;

import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Generic command for test all product extension point.
 * 
 * @author tpanuel
 */
public class ProductExtensionPointTest implements GDMExtension {
    /** Field for valid postGetModel. */
    public static final String POST_GET_MODEL_CHECK = "postGetModelOK";

    /** Field for valid preCreate. */
    public static final String PRE_CREATE_CHECK = "preCreateOK";

    /** Field for valid postCreate. */
    public static final String POST_CREATE_CHECK = "postCreateOK";

    /** Field for valid preUpdate. */
    public static final String PRE_UPDATE_CHECK = "preUpdateOK";

    /** Field for valid postUpdate. */
    public static final String POST_UPDATE_CHECK = "postUpdateOK";

    /** Attribute for valid preUpdate. */
    public static final String PRE_DELETE_CHECK = "preUpdateOK";

    /** Attribute for valid postUpdate. */
    public static final String POST_DELETE_CHECK = "postUpdateOK";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(final Context pContext) {
        final String lRoleToken =
                pContext.get(ExtensionPointParameters.ROLE_TOKEN);
        final String lExtName =
                pContext.get(ExtensionPointParameters.EXTENSION_POINT_NAME);
        final String lCheckElementName;
        final int lTreamentCase;

        if (lExtName.equals(ExtensionPointNames.POST_GET_MODEL)) {
            lCheckElementName = POST_GET_MODEL_CHECK;
            lTreamentCase = 0;
        }
        else if (lExtName.equals(ExtensionPointNames.PRE_CREATE)) {
            lCheckElementName = PRE_CREATE_CHECK;
            lTreamentCase = 0;
        }
        else if (lExtName.equals(ExtensionPointNames.POST_CREATE)) {
            lCheckElementName = POST_CREATE_CHECK;
            lTreamentCase = 1;
        }
        else if (lExtName.equals(ExtensionPointNames.PRE_UPDATE)) {
            lCheckElementName = PRE_UPDATE_CHECK;
            lTreamentCase = 0;
        }
        else if (lExtName.equals(ExtensionPointNames.POST_UPDATE)) {
            lCheckElementName = POST_UPDATE_CHECK;
            lTreamentCase = 1;
        }
        else if (lExtName.equals(ExtensionPointNames.PRE_DELETE)) {
            lCheckElementName = PRE_DELETE_CHECK;
            lTreamentCase = 2;
        }
        else if (lExtName.equals(ExtensionPointNames.POST_DELETE)) {
            lCheckElementName = POST_DELETE_CHECK;
            lTreamentCase = 2;
        }
        else {
            throw new GDMException("Invalid extension point name");
        }

        switch (lTreamentCase) {
            case 0: {
                final CacheableProduct lProduct =
                        pContext.get(ExtensionPointParameters.PRODUCT);

                lProduct.setValue(new FieldValueData(lCheckElementName,
                        Boolean.TRUE.toString()));
                break;
            }
            case 1: {
                final ProductService lProductService =
                        pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getProductService();
                final String lProductId =
                        pContext.get(ExtensionPointParameters.PRODUCT_ID);
                final CacheableProduct lProduct =
                        lProductService.getCacheableProduct(lRoleToken,
                                lProductId, CacheProperties.MUTABLE);
                final Context lCtx = new ContextBase(pContext);

                lCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

                lProduct.setValue(new FieldValueData(lCheckElementName,
                        Boolean.TRUE.toString()));
                lProductService.updateProduct(lRoleToken, lProduct, lCtx);
                break;
            }
            case 2: {
                final AttributesService lAttributesService =
                        pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getAttributesService();
                final CacheableProductType lType =
                        pContext.get(ExtensionPointParameters.PRODUCT_TYPE);

                lAttributesService.set(lType.getId(),
                        new AttributeData[] { new AttributeData(
                                lCheckElementName,
                                new String[] { Boolean.TRUE.toString() }) });
            }
            default: {
                break;
            }
        }

        return true;
    }
}
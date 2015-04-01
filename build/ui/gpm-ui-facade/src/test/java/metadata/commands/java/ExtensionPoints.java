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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class ExtensionPoints implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        String lExtensionPointName =
                pContext.get(ExtensionPointParameters.EXTENSION_POINT_NAME);

        CacheableSheet lSheetData =
                pContext.get(ExtensionPointParameters.SHEET);
        CacheableSheetType lSheetType =
                pContext.get(ExtensionPointParameters.SHEET_TYPE);
        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);

        BusinessSheet lSheet =
                new CacheableSheetAccess(lRoleToken, lSheetType, lSheetData,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        SimpleDateFormat lDateFormat =
                new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

        String lDate = lDateFormat.format(new Date());

        if ("preCreate".equals(lExtensionPointName)) {

            ProductService lProductService =
                    pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getProductService();

            String lProductName =
                    pContext.get(ExtensionPointParameters.PRODUCT_NAME);
            String lProductId =
                    lProductService.getProductId(lRoleToken, lProductName);

            int lSheetCount =
                    lProductService.getSheetCount(lRoleToken, lProductId);

            lSheet.getStringField("FILTER_RESULT_FIELD_1").set(
                    "FILTER_F1_" + lSheetCount);
            lSheet.getStringField("FILTER_RESULT_FIELD_2").set(
                    "FILTER_F2_" + lSheetCount);

            // Init Reference
            BusinessStringField lRef =
                    lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                            "REF_id");
            if (lRef.get() == null) {
                lRef.set(lSheetType.getName() + "-" + lProductName + "#"
                        + lSheetCount);
            }
        }
        lSheet.getMultivaluedStringField("EXTENSION_POINT_FIELD").addLine().set(
                lDate + " : [EXE] " + lExtensionPointName + " Extension Point");
        return true;
    }
}

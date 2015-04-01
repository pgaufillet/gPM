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
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class CreatePointerFields implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        CacheableSheet lSheetData =
                pContext.get(ExtensionPointParameters.SHEET);
        CacheableSheetType lSheetType =
                pContext.get(ExtensionPointParameters.SHEET_TYPE);
        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);

        BusinessSheet lSheet =
                new CacheableSheetAccess(lRoleToken, lSheetType, lSheetData,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        String lSheetID = lSheet.getId();

        BusinessPointerField lPointerField =
                lSheet.getPointerField("POINTER_FIELD_EP");
        BusinessMultivaluedField<BusinessPointerField> lMultivaluedPointerField =
                lSheet.getMultivaluedPointerField("MULTIVALUED_POINTER_FIELD_EP");

        lPointerField.setPointedContainerId(lSheetID);
        lPointerField.setPointedFieldName("POINTED_SIMPLE_FIELD");

        lMultivaluedPointerField.get(0).setPointedContainerId(lSheetID);
        lMultivaluedPointerField.get(0).setPointedFieldName(
                "POINTED_MULTIVALUED_SIMPLE_FIELD");

        SimpleDateFormat lDateFormat =
                new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

        String lDate = lDateFormat.format(new Date());

        lSheet.getMultivaluedStringField("EXTENSION_POINT_FIELD").addLine().set(
                lDate + " : [EXE] CreatePointerFields Extension Point");

        return true;
    }

}

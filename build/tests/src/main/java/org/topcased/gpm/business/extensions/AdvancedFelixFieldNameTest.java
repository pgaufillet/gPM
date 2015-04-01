/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetService;

/**
 * AdvancedFelixFieldName
 */
public class AdvancedFelixFieldNameTest implements GDMExtension {

    /** The Name of a field. */
    private static final String FIELD_NAME = "Félix";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {
        ServiceLocator serviceLocator = ServiceLocator.instance();
        SheetService sheetService = serviceLocator.getSheetService();
        SheetData lSheetData =
                sheetService.getSheetByKey((String) pContext.get("roleToken"),
                        (String) pContext.get("valuesContainerId"));

        LineFieldData lLFD = lSheetData.getReference();
        String[] lValues = { FIELD_NAME };
        FieldData[] lFieldDatas = lLFD.getFieldDatas();
        lFieldDatas[0].setValues(new FieldValueData(lValues));
        lLFD.setFieldDatas(lFieldDatas);

        sheetService.updateSheet((String) pContext.get("roleToken"),
                lSheetData, null);
        return true;
    }
}

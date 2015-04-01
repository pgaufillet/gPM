/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.sheet.service.SheetData;

/**
 * FelixFieldNameTest postGetModel
 */
public class FelixFieldNameTest implements GDMExtension {

    /** The Name of a field. */
    private static final String FIELD_NAME = "Félix";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {
        LineFieldData lLFD =
                pContext.get("sheetData", SheetData.class).getReference();
        String[] lValues = { FIELD_NAME };
        FieldData[] lFieldDatas = lLFD.getFieldDatas();
        lFieldDatas[0].setValues(new FieldValueData(lValues));
        lLFD.setFieldDatas(lFieldDatas);
        return true;
    }
}

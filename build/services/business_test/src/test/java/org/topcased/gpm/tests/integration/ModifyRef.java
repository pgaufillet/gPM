/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests.integration;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.sheetAccess.MultipleFieldDataAccess;
import org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData;

/**
 * Basic extension point that put value 'TEST' in the reference field of the
 * sheet ModifyRef.
 * 
 * @author ahaugommard
 */
public class ModifyRef implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension
     *      #execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {
        // Récupération de la sheetData
        SheetData lSheetData = (SheetData) pContext.get("sheetData");

        // récupération de l'acces au champ multiple Reference
        MultipleFieldDataAccess lReferenceFieldDataAccess =
                new MultipleFieldDataAccess(lSheetData.getReference());

        // reference.name <- ref
        ((SimpleFieldData) lReferenceFieldDataAccess.getField("CAT_name")).setValue("TEST");

        return true;
    }
}

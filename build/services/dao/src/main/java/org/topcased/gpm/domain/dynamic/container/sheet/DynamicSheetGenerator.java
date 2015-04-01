/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.sheet;

import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGenerator;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * Generator of dynamic sheets
 * 
 * @author tpanuel
 */
public class DynamicSheetGenerator extends
        DynamicValuesContainerGenerator<Sheet> {
    private static final Source SOURCE =
            new Source(DynamicSheetGenerator.class.getName());

    /**
     * Create a dynamic sheets generator
     * 
     * @param pSheetType
     *            The type of the sheet
     */
    public DynamicSheetGenerator(SheetType pSheetType) {
        super(SOURCE, pSheetType, Sheet.class, false);
    }
}
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

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGeneratorFactory;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * Factory of DynamicSheetGenerator
 * 
 * @author tpanuel
 */
public final class DynamicSheetGeneratorFactory extends
        DynamicValuesContainerGeneratorFactory<Sheet, SheetType> {
    private final static DynamicSheetGeneratorFactory INSTANCE =
            new DynamicSheetGeneratorFactory();

    private DynamicSheetGeneratorFactory() {
    }

    /**
     * DynamicSheetGeneratorFactory is a singleton
     * 
     * @return The instance
     */
    public final static DynamicSheetGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#createDynamicObjectGenerator(java.lang.Object)
     */
    @Override
    protected DynamicObjectGenerator<Sheet> createDynamicObjectGenerator(
            SheetType pMappedObjectType) {
        return new DynamicSheetGenerator(pMappedObjectType);
    }
}
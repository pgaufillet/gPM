/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.revision;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGeneratorFactory;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.revision.Revision;

/**
 * Factory of DynamicRevisionGenerator
 * 
 * @author tpanuel
 */
public final class DynamicRevisionGeneratorFactory extends
        DynamicValuesContainerGeneratorFactory<Revision, FieldsContainer> {
    private final static DynamicRevisionGeneratorFactory INSTANCE =
            new DynamicRevisionGeneratorFactory();

    private DynamicRevisionGeneratorFactory() {
    }

    /**
     * DynamicRevisionGenerator is a singleton
     * 
     * @return The instance
     */
    public final static DynamicRevisionGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#createDynamicObjectGenerator(java.lang.Object)
     */
    @Override
    protected DynamicObjectGenerator<Revision> createDynamicObjectGenerator(
            FieldsContainer pMappedObjectType) {
        return new DynamicRevisionGenerator(pMappedObjectType);
    }
}
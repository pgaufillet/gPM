/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.multivalued;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory;
import org.topcased.gpm.domain.fields.Field;

/**
 * Factory of DynamicMultivaluedFieldGenerator
 * 
 * @author tpanuel
 */
public final class DynamicMultivaluedFieldGeneratorFactory extends
        DynamicObjectGeneratorFactory<DynamicMultivaluedField, Field> {
    private final static DynamicMultivaluedFieldGeneratorFactory INSTANCE =
            new DynamicMultivaluedFieldGeneratorFactory();

    private DynamicMultivaluedFieldGeneratorFactory() {

    }

    /**
     * DynamicMultivaluedFieldGeneratorFactory is a singleton
     * 
     * @return The instance
     */
    public final static DynamicMultivaluedFieldGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#createDynamicObjectGenerator(java.lang.Object)
     */
    @Override
    protected DynamicObjectGenerator<DynamicMultivaluedField> createDynamicObjectGenerator(
            Field pMappedObjectType) {
        return new DynamicMultivaluedFieldGenerator(pMappedObjectType, false);
    }
}
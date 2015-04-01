/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.product;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGeneratorFactory;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.product.ProductType;

/**
 * Factory of DynamicProductGenerator
 * 
 * @author tpanuel
 */
public final class DynamicProductGeneratorFactory extends
        DynamicValuesContainerGeneratorFactory<Product, ProductType> {
    private final static DynamicProductGeneratorFactory INSTANCE =
            new DynamicProductGeneratorFactory();

    private DynamicProductGeneratorFactory() {
    }

    /**
     * DynamicProductGeneratorFactory is a singleton
     * 
     * @return The instance
     */
    public final static DynamicProductGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#createDynamicObjectGenerator(java.lang.Object)
     */
    @Override
    protected DynamicObjectGenerator<Product> createDynamicObjectGenerator(
            ProductType pMappedObjectType) {
        return new DynamicProductGenerator(pMappedObjectType);
    }
}
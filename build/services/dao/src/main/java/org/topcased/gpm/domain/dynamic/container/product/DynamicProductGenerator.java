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

import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGenerator;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.product.ProductType;

/**
 * Generator of dynamic products
 * 
 * @author tpanuel
 */
public class DynamicProductGenerator extends
        DynamicValuesContainerGenerator<Product> {
    private static final Source SOURCE =
            new Source(DynamicProductGenerator.class.getName());

    /**
     * Create a dynamic products generator
     * 
     * @param pProductType
     *            The type of the product
     */
    public DynamicProductGenerator(ProductType pProductType) {
        super(SOURCE, pProductType, Product.class, false);
    }
}
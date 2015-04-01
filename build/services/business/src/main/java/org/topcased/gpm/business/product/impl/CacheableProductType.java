/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import java.util.List;

import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;

/**
 * The Class CacheableProductType.
 * 
 * @author llatil
 */
@SuppressWarnings("serial")
public class CacheableProductType extends CacheableFieldsContainer {

    /**
     * Constructor for mutable / immutable switch
     */
    public CacheableProductType() {
        super();
    }

    /**
     * Constructs a new cacheable product type.
     * 
     * @param pProductType
     *            the product type
     * @param pDisplayGrpEntities
     *            the list of display group (fields group) entities
     */
    public CacheableProductType(
            org.topcased.gpm.domain.product.ProductType pProductType,
            List<? extends org.topcased.gpm.domain.facilities.DisplayGroup> pDisplayGrpEntities) {
        super(pProductType);
        //call to parent method to set the field groups of the container product
        setDisplayGroups(pDisplayGrpEntities);
    }

}

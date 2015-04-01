/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.product.impl.cacheable;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldGroupAccess;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.business.values.product.BusinessProduct;

/**
 * Access on a product.
 * 
 * @author tpanuel
 */
public class CacheableProductAccess
        extends
        AbstractCacheableContainerAccess<CacheableProductType, CacheableProduct>
        implements BusinessProduct {
    /**
     * Create an access on a product.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductType
     *            The product type to access.
     * @param pProduct
     *            The product to access.
     * @param pProperties
     *            The values access properties.
     */
    public CacheableProductAccess(final String pRoleToken,
            final CacheableProductType pProductType,
            final CacheableProduct pProduct,
            final ValuesAccessProperties pProperties) {
        super(pRoleToken, pProductType, pProduct, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getChildren()
     */
    @Override
    public List<String> getChildren() {
        return read().getChildrenNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getName()
     */
    public String getName() {
        return read().getProductName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getDescription()
     */
    public String getDescription() {
        return read().getDescription();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getParents()
     */
    @Override
    public List<String> getParents() {
        return read().getParentNames();
    }

    /**
     * Return the display groups (fields groups) of the product type.
     * 
     * @return the field group names
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getFieldGroupNames()
     */
    public List<String> getFieldGroupNames() {
        List<String> lGroupNames = new ArrayList<String>();
        for (DisplayGroup lGroup : getType().getDisplayGroups()) {
            lGroupNames.add(lGroup.getName());
        }
        return lGroupNames;
    }

    /**
     * Find a display group (fields group) by its name.
     * 
     * @param pFieldGroupName
     *            the field group name
     * @return the field group
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getFieldGroup(java.lang.String)
     */
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        return new CacheableFieldGroupAccess(getType().getDisplayGroup(
                pFieldGroupName));
    }
    
    /**
     * Role from role token cannot be trusted as current user may have several roles
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }
}
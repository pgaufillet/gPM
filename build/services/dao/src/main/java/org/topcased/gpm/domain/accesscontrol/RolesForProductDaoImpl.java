/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.accesscontrol;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.product.Product;

/**
 * RolesForProductDaoImpl
 * 
 * @author nveillet
 */
public class RolesForProductDaoImpl extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.RolesForProduct, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.RolesForProductDao {

    /**
     * Constructor
     */
    public RolesForProductDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.RolesForProduct.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RolesForProductDaoBase#getByProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("unchecked")
    public List<RolesForProduct> getByProduct(final Product pProduct) {
        final String lHqlQuery =
                "from org.topcased.gpm.domain.accesscontrol.RolesForProduct as rolesForProduct "
                        + "where rolesForProduct.product.id = :productId";
        Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameter("productId", pProduct.getId());
        return lQuery.list();
    }
}
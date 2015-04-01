/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin),
 *  Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.search;

import java.util.List;

import org.hibernate.Query;

/**
 * FilterDaoImpl overrides methods in the generated class FilterDaoBase, which
 * need to be defined manually.
 * 
 */
public class FilterDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.search.Filter, java.lang.String>
        implements org.topcased.gpm.domain.search.FilterDao {

    /**
     * Create a DAO.
     */
    public FilterDaoImpl() {
        super(org.topcased.gpm.domain.search.Filter.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterDaoBase#getVisibleFilters(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Filter> getVisibleFilters(final String pBusinessProcessName,
            final String pProductName, final String pUserLogin) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select filter from org.topcased.gpm.domain.search.Filter as filter "
                                + "left join filter.product as product "
                                + "left join filter.endUser as user where ("
                                + "filter.businessProcess.name = :businessProcessName"
                                + ") and ((product is null) or "
                                + "product.name = :productName) and ("
                                + "(user is null) or "
                                + "(user.login = :userLogin))");
        lQuery.setParameter("businessProcessName", pBusinessProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("userLogin", pUserLogin);

        return (List<Filter>) lQuery.list();
    }
}
/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.search;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * FilterComponentDaoImpl. This class overrides dao methods which need to be
 * manually implemented.
 * 
 * @see org.topcased.gpm.domain.search.FilterComponent
 * @author vhemery
 */
public class FilterComponentDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.search.FilterComponent, java.lang.String>
        implements org.topcased.gpm.domain.search.FilterComponentDao {

    /**
     * Create a DAO.
     */
    public FilterComponentDaoImpl() {
        super(org.topcased.gpm.domain.search.FilterComponent.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterComponentDao#getFilterComponent
     *      (java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, Class pType)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FilterComponent getFilterComponent(
            final String pBusinessProcessName, final String pProductName,
            final String pUserLogin, final String pName, final Class pType,
            boolean pIsCaseSensitive) {
        String lProductClause = "filterComp.product.name = :productName ";
        if (StringUtils.isBlank(pProductName)) {
            lProductClause = "filterComp.product is null ";
        }

        String lUserClause = "filterComp.endUser.login = :userLogin ";
        String lLoginParameter = pUserLogin;
        if (!pIsCaseSensitive) {
            if (lLoginParameter != null) {
                lLoginParameter = lLoginParameter.toLowerCase();
                lUserClause = "lower(filterComp.endUser.login) = :userLogin ";
            }
        }

        if (StringUtils.isBlank(pUserLogin)) {
            lUserClause = "filterComp.endUser is null ";
        }
        String lStringQuery =
                "from " + pType.getName() + " filterComp "
                        + "where filterComp.businessProcess.name = :bPName "
                        + "and " + lProductClause + "and " + lUserClause
                        + "and filterComp.name = :name ";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("name", pName);
        lQuery.setParameter("bPName", pBusinessProcessName);
        if (StringUtils.isNotBlank(pProductName)) {
            lQuery.setParameter("productName", pProductName);
        }
        if (StringUtils.isNotBlank(pUserLogin)) {
            lQuery.setParameter("userLogin", lLoginParameter);
        }
        Class<? extends FilterComponent> lTypeExtends =
                pType.asSubclass(FilterComponent.class);
        return lTypeExtends.cast(lQuery.uniqueResult());
    }
}

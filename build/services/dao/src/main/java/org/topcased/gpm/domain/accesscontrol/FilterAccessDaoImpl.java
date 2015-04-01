/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.accesscontrol;

import org.hibernate.Query;

/**
 * FilterAccessDaoImpl.
 * 
 * @author tpanuel
 */
public class FilterAccessDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.FilterAccess, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.FilterAccessDao {

    private final static String ACCESS_ON_TYPE =
            "FROM " + FilterAccess.class.getName()
                    + " WHERE (roleName = null OR roleName = :roleName)"
                    + " AND (typeName = null OR typeName = :typeName)"
                    + " AND fieldName = null AND visibility = null";

    /**
     * Constructor
     */
    public FilterAccessDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.FilterAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.FilterAccessDaoBase#getFilterAccessOnType(java.lang.String,
     *      java.lang.String)
     */
    public FilterAccess getFilterAccessOnType(final String pRoleName,
            final String pTypeName) {
        final Query lQuery = createQuery(ACCESS_ON_TYPE);

        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("typeName", pTypeName);

        return getFirst(lQuery, new FilterAccessComparator());
    }

    private final static String ACCESS_ON_FIELD =
            "FROM " + FilterAccess.class.getName()
                    + " WHERE (roleName = null OR roleName = :roleName)"
                    + " AND (typeName = :typeName)"
                    + " AND (fieldName = :fieldLabel)"
                    + " AND visibility = null";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.FilterAccessDaoBase#getFilterAccessOnField(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public FilterAccess getFilterAccessOnField(final String pRoleName,
            final String pTypeName, final String pFieldLabel) {
        final Query lQuery = createQuery(ACCESS_ON_FIELD);

        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("typeName", pTypeName);
        lQuery.setParameter("fieldLabel", pFieldLabel);

        return getFirst(lQuery, new FilterAccessComparator());
    }

    private final static String ACCESS_ON_VISIBILITY =
            "FROM " + FilterAccess.class.getName()
                    + " WHERE (roleName = null OR roleName = :roleName)"
                    + " AND (visibility = null OR visibility = :visibility)"
                    + " AND typeName = null AND fieldName = null";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.FilterAccessDaoBase#getFilterAccessOnVisibility(java.lang.String,
     *      java.lang.String)
     */
    public FilterAccess getFilterAccessOnVisibility(final String pRoleName,
            final String pVisibility) {
        final Query lQuery = createQuery(ACCESS_ON_VISIBILITY);

        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("visibility", pVisibility);

        return getFirst(lQuery, new FilterAccessComparator());
    }
}
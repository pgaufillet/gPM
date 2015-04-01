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
package org.topcased.gpm.domain.mapping;

/**
 * @see org.topcased.gpm.domain.mapping.TypeMap
 * @author tpanuel
 */
public class TypeMapDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.mapping.TypeMap, java.lang.String>
        implements org.topcased.gpm.domain.mapping.TypeMapDao {
    /**
     * Constructor
     */
    public TypeMapDaoImpl() {
        super(org.topcased.gpm.domain.mapping.TypeMap.class);
    }

    /**
     * @see org.topcased.gpm.domain.mapping.TypeMap#getTypeMapping(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.mapping.TypeMap getTypeMapping(
            final java.lang.String pOriginProcessName,
            final java.lang.String pOriginTypeName,
            final java.lang.String pDestinationProcessName,
            final java.lang.String pDestinationTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.mapping.TypeMap as typeMap where typeMap.originProcessName = :originProcessName and typeMap.originTypeName = :originTypeName and typeMap.destinationProcessName = :destinationProcessName and typeMap.destinationTypeName = :destinationTypeName";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("originProcessName", pOriginProcessName);
            lQueryObject.setParameter("originTypeName", pOriginTypeName);
            lQueryObject.setParameter("destinationProcessName",
                    pDestinationProcessName);
            lQueryObject.setParameter("destinationTypeName",
                    pDestinationTypeName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.mapping.TypeMap lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.mapping.TypeMap"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.mapping.TypeMap) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
}
/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.domain.fields;

import java.util.List;

import org.hibernate.Query;

/**
 * DAO for LOck access
 * 
 * @author tpanuel
 */
public class LockDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.Lock, java.lang.String>
        implements org.topcased.gpm.domain.fields.LockDao {

    private final static String CONTAINER_LOCK =
            "FROM " + Lock.class.getName()
                    + " WHERE containerId = :containerId";

    /**
     * COnstructor
     */
    public LockDaoImpl() {
        super(org.topcased.gpm.domain.fields.Lock.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.LockDaoBase#getLock(java.lang.String)
     */
    public Lock getLock(final String pContainerId) {
        final Query lQuery = createQuery(CONTAINER_LOCK);

        lQuery.setParameter("containerId", pContainerId);

        return getFirst(lQuery);
    }

    private final static String ALL_CONTAINER_LOCKED =
            "SELECT containerId FROM " + Lock.class.getName()
                    + " WHERE sessionToken is not null";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.LockDaoBase#getAllSessionLocks()
     */
    @SuppressWarnings("unchecked")
    public List<String> getAllContainerLockedForSession() {
        return createQuery(ALL_CONTAINER_LOCKED).list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.LockDaoBase#getLocksBySessionToken(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public List getLocksBySessionToken(final String pSessionToken) {
        if (pSessionToken == null) {
            throw new IllegalArgumentException(
                    "Lock.getLocksBySessionToken - Session Token can not be null");
        }

        final Query lQuery =
                createQuery("FROM org.topcased.gpm.domain.fields.Lock "
                        + "AS lock "
                        + "WHERE lock.sessionToken = :sessionToken");

        lQuery.setParameter("sessionToken", pSessionToken);

        return execute(lQuery);
    }
}
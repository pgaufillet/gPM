/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Latil (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.atomic.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.atomic.Synchronized;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.common.valuesContainer.LockType;

/**
 * Manager for atomic actions. All action realized by the implementation are out
 * of transaction. Method can be synchronized using an interceptor.
 * 
 * @author tpanuel
 */
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public interface AtomicActionsManager {
    /**
     * Set an attribute value if the current value equals to a given value
     * <p>
     * This method ensures that the attribute value is changed atomically
     * (thread-safe), and immediately committed in the database.
     * <p>
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pElementId
     *            Identifier of the attributes container.
     * @param pAttributeName
     *            Name of the attribute to increment.
     * @param pOldValue
     *            The old value tested with the current value
     * @param pNewValue
     *            The new value of the attribute
     * @return The value of the attribute before modification (equals pOldValue
     *         if value has been modified)
     */
    @Synchronized
    public String atomicTestAndSet(String pRoleToken, String pElementId,
            String pAttributeName, String pOldValue, String pNewValue);

    /**
     * Lock or unlock a sheet
     * 
     * @param pUserLogin
     *            The login of the user.
     * @param pLockType
     *            The type of lock. If null, the sheet is unlock.
     * @param pSessionToken
     *            The current session token. If null, permanent lock.
     * @param pSheetId
     *            The sheet id.
     * @param pIsAdmin
     *            If the user is admin and can override role.
     * @throws LockException
     *             The sheet is locked by an other user.
     * @deprecated
     * @since 1.8.1
     * @see AtomicActionsManager#lock(String, LockType, String, String, boolean)
     */
    @Synchronized
    public void lockSheet(String pUserLogin, LockType pLockType,
            String pSessionToken, String pSheetId, boolean pIsAdmin)
        throws LockException;

    /**
     * Lock or unlock a values container
     * 
     * @param pUserLogin
     *            The login of the user.
     * @param pLockType
     *            The type of lock. If null, the sheet is unlock.
     * @param pSessionToken
     *            The current session token. If null, permanent lock.
     * @param pValuesContainerId
     *            The values container id.
     * @param pIsAdmin
     *            If the user is admin and can override role.
     * @throws LockException
     *             The values container is locked by an other user.
     */
    @Synchronized
    public void lock(String pUserLogin, LockType pLockType,
            String pSessionToken, String pValuesContainerId, boolean pIsAdmin)
        throws LockException;

    /**
     * Get the sheet's lock.
     * 
     * @param pSheetId
     *            The sheet id.
     * @return The lock. Can be null, if no lock.
     * @deprecated
     * @since 1.8.1
     * @see AtomicActionsManager#getLock(String)
     */
    public Lock getSheetLock(String pSheetId);

    /**
     * Get the values container's lock.
     * 
     * @param pValuesContainerId
     *            Identifier of the values container
     * @return The lock is values container is locked, null otherwise.
     */
    public Lock getLock(String pValuesContainerId);
}
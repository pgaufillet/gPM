/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.util.bean;

import org.topcased.gpm.business.serialization.data.Lock.LockScopeEnumeration;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;

/**
 * The Class LockProperties is a bean used to set options on locks
 * 
 * @author tpanuel
 */
public class LockProperties {
    /**
     * Properties for defining a permanent lock with read and write option
     */
    public final static LockProperties PERMANENT_READ_WRITE_LOCK =
            new LockProperties(LockTypeEnumeration.READ_WRITE);

    /**
     * Properties for defining a permanent lock with write only option
     */
    public final static LockProperties PERMANENT_WRITE_LOCK =
            new LockProperties(LockTypeEnumeration.WRITE);

    /**
     * Properties for defining a permanent lock with none option
     */
    public final static LockProperties PERMANENT_NONE_LOCK =
            new LockProperties(LockTypeEnumeration.NONE);

    private final LockTypeEnumeration lockType;

    private final LockScopeEnumeration lockScope;

    /**
     * Create a LockProperties for a permanent lock
     * 
     * @param pLockType
     *            The lock type
     */
    public LockProperties(LockTypeEnumeration pLockType) {
        this(pLockType, LockScopeEnumeration.PERMANENT);
    }

    /**
     * Create a LockProperties for a session lock (except if the session token
     * is null)
     * 
     * @param pLockType
     *            The lock type
     * @param pLockScope
     *            The lock scope
     */
    public LockProperties(LockTypeEnumeration pLockType,
            LockScopeEnumeration pLockScope) {
        lockType = pLockType;
        lockScope = pLockScope;
    }

    /**
     * Get the lock type
     * 
     * @return The lock type
     */
    public LockTypeEnumeration getLockType() {
        return lockType;
    }

    /**
     * Get the lock scope
     * 
     * @return The lock scope
     */
    public LockScopeEnumeration getLockScope() {
        return lockScope;
    }
}

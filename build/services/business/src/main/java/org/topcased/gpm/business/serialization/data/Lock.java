/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ContainerLock
 * 
 * @author mfranche
 */

@XStreamAlias("lock")
public class Lock implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -2569948603716536936L;

    /** Owner of the lock */
    @XStreamAsAttribute
    private String owner;

    /** Type of the lock */
    @XStreamAlias("lockType")
    @XStreamAsAttribute
    private LockTypeEnumeration type;

    /** The scope of the lock */
    transient private LockScopeEnumeration scope;

    /**
     * Constructs an empty lock object.
     * 
     * @see Lock#setOwner(String)
     * @see Lock#setType(org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration)
     */
    public Lock() {
    }

    /**
     * Constructs a new lock object.
     * 
     * @param pOwner
     *            Owner of the lock
     * @param pLockType
     *            Type required for the lock (the LockType.NONE value is
     *            actually used to release a lock)
     * @param pScope
     *            The scope of the lock
     */
    public Lock(String pOwner, LockTypeEnumeration pLockType,
            LockScopeEnumeration pScope) {
        owner = pOwner;
        type = pLockType;
        scope = pScope;
    }

    /**
     * Constructs a new lock object.
     * 
     * @param pOwner
     *            Owner of the lock
     * @param pLockTypeAsString
     *            Type required for the lock (the LockType.NONE value is
     *            actually used to release a lock)
     * @param pScope
     *            The scope of the lock
     */
    public Lock(String pOwner, String pLockTypeAsString,
            LockScopeEnumeration pScope) {
        owner = pOwner;
        type = LockTypeEnumeration.valueOf(pLockTypeAsString);
        scope = pScope;
    }

    /**
     * Get lock owner
     * 
     * @return the lock owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the lock's owner
     * 
     * @param pOwner
     *            the lock owner to set
     */
    public void setOwner(String pOwner) {
        owner = pOwner;
    }

    /**
     * get type
     * 
     * @return the type
     */
    public LockTypeEnumeration getType() {
        return type;
    }

    /**
     * Set type of the lock
     * 
     * @param pType
     *            the type to set
     */
    public void setType(LockTypeEnumeration pType) {
        type = pType;
    }

    /**
     * Get the scope
     * 
     * @return The scope
     */
    public LockScopeEnumeration getScope() {
        return scope;
    }

    /**
     * Set the scope
     * 
     * @param pScope
     *            The new scope
     */
    public void setSessionToken(LockScopeEnumeration pScope) {
        scope = pScope;
    }

    /**
     * Get the container lock type
     * 
     * @return the container lock type
     */
    public LockTypeEnumeration getContainerLockType() {
        switch (type) {
            case WRITE:
                return LockTypeEnumeration.WRITE;
            case READ_WRITE:
                return LockTypeEnumeration.READ_WRITE;
            case NONE:
                return null;
            default:
                throw new RuntimeException("Value " + type + " invalid");
        }
    }

    /**
     * Enumerated values for the lock type.
     */
    public static enum LockTypeEnumeration {
        NONE, WRITE, READ_WRITE
    };

    /**
     * Enumerated values for the lock scope
     */
    public static enum LockScopeEnumeration {
        PERMANENT, USER_SESSION, ROLE_SESSION
    };

    /**
     * Create a data Lock from the domain representation of the lock
     * 
     * @param pLock
     *            The domain representation
     * @return The data Lock
     */
    public static Lock createLock(org.topcased.gpm.domain.fields.Lock pLock) {
        final AuthorizationService lAuthorizationService =
                (AuthorizationService) ContextLocator.getContext().getBean(
                        "authorizationServiceImpl", AuthorizationService.class);
        final String lOwner = pLock.getOwner();
        final LockTypeEnumeration lType =
                LockTypeEnumeration.valueOf(pLock.getLockType().getValue());
        final String lSessionToken = pLock.getSessionToken();

        if (lSessionToken == null) {
            return new Lock(lOwner, lType, LockScopeEnumeration.PERMANENT);
        }
        else if (lAuthorizationService.isValidUserToken(lSessionToken)) {
            return new Lock(lOwner, lType, LockScopeEnumeration.USER_SESSION);
        }
        else if (lAuthorizationService.isValidRoleToken(lSessionToken)) {
            return new Lock(lOwner, lType, LockScopeEnumeration.ROLE_SESSION);
        }
        else {
            // The session associated to the lock has expired and it will be delete
            return null;
        }
    }
}

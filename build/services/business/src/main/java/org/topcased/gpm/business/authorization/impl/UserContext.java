/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.domain.accesscontrol.EndUser;

/**
 * Class UserContext
 * 
 * @author tpanuel
 */
public class UserContext extends AbstractContext {
    private static final long SESSION_EXPIRED = -1;

    /** The id of the user */
    private final String endUserId;

    /** The creation date in nanoseconds */
    private final long nanoTimeStamp;

    /** The date of the last refresh in seconds */
    private AtomicLong lastRefreshDate;

    /** The login of the user */
    private final String login;

    /** User session attributes */
    private final Map<String, Object> attrs = new HashMap<String, Object>();

    /** The max time of a session in seconds */
    private final long sessionMaxTime;

    /**
     * Constructs a new user session context.
     * 
     * @param pEndUser
     *            the end user
     * @param pSessionToken
     *            the session token
     * @param pSessionMaxTime
     *            The time max of the session
     */
    public UserContext(EndUser pEndUser, String pSessionToken,
            long pSessionMaxTime) {
        super(pSessionToken);
        endUserId = pEndUser.getId();
        nanoTimeStamp = System.nanoTime();
        lastRefreshDate = new AtomicLong(getCurrentTimeInSecond());
        login = pEndUser.getLogin();
        sessionMaxTime = pSessionMaxTime;
    }

    /**
     * Get the session age in nanoseconds
     * 
     * @return Age in nanoseconds
     */
    public long getSessionAge() {
        return System.nanoTime() - nanoTimeStamp;
    }

    /**
     * Getter on the id of the user
     * 
     * @return The id of the user
     */
    public String getEndUserId() {
        return endUserId;
    }

    /**
     * Getter on the login of the user
     * 
     * @return The login of the user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Set session attribute
     * 
     * @param pName
     *            Name of the attribute
     * @param pValue
     *            Value of the attribute
     */
    public void setAttribute(String pName, Object pValue) {
        if (StringUtils.isBlank(pName)) {
            throw new InvalidNameException(
                    "Invalid attribute name. Attribute is null/blank");
        }
        attrs.put(pName, pValue);
    }

    /**
     * Get session attribute
     * 
     * @param pName
     *            Name of the attribute
     * @return Value of the attribute
     */
    public Object getAttribute(String pName) {
        if (StringUtils.isBlank(pName)) {
            throw new InvalidNameException(
                    "Invalid attribute name. Attribute is null/blank");
        }
        return attrs.get(pName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#refresh()
     */
    public boolean refresh() {
        long lOldRefreshDate;
        long lNewRefreshDate;
        boolean lValid;

        do {
            lOldRefreshDate = lastRefreshDate.get();
            lValid = testLastRefreshDate(lOldRefreshDate);
            if (lValid) {
                lNewRefreshDate = getCurrentTimeInSecond();
            }
            else {
                lNewRefreshDate = SESSION_EXPIRED;
            }
        } while (!lastRefreshDate.compareAndSet(lOldRefreshDate,
                lNewRefreshDate));

        return lValid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#isValid()
     */
    public boolean isValid() {
        long lOldRefreshDate;

        do {
            lOldRefreshDate = lastRefreshDate.get();
            if (testLastRefreshDate(lOldRefreshDate)) {
                return true;
            }
        } while (!lastRefreshDate.compareAndSet(lOldRefreshDate,
                SESSION_EXPIRED));

        return false;
    }

    private boolean testLastRefreshDate(long pLastRefreshDate) {
        return pLastRefreshDate != -1
                && getCurrentTimeInSecond() <= (pLastRefreshDate + sessionMaxTime);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#invalid()
     */
    public void invalid() {
        lastRefreshDate.set(SESSION_EXPIRED);
    }

    private final static long NB_MILLISECONDS_IN_SECONDS = 1000;

    private static long getCurrentTimeInSecond() {
        return System.currentTimeMillis() / NB_MILLISECONDS_IN_SECONDS;
    }
}

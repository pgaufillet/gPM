/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockScopeEnumeration;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;
import org.topcased.gpm.util.job.GpmJob;

/**
 * Tests the method lockSheet of the SheetService.
 * 
 * @author tpanuel
 */
public class TestSessionLocks extends AbstractBusinessServiceTestCase {
    private static final String PRODUCT_NAME = GpmTestValues.SHEET1_1_PRODUCT;

    private static final String SHEET_REF = GpmTestValues.SHEET1_1_REF;

    private static final String USER_LOGGIN = ADMIN_LOGIN[0];

    private static final String USER_PWD = ADMIN_LOGIN[1];

    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /**
     * Test user session lock deletion on logout
     */
    public void testUserSessionLocksAndManualDisconnect() {
        // A user lock is removed, if the user is logout
        checkLocksAndManualDisconnect(LockScopeEnumeration.USER_SESSION, true,
                false);
        // A user lock is not removed, if only the role token is removed
        checkLocksAndManualDisconnect(LockScopeEnumeration.USER_SESSION, false,
                true);
    }

    /**
     * Test role session lock deletion on logout
     */
    public void testRoleSessionLocksAndManualDisconnect() {
        // A user lock is removed, if the user is logout
        checkLocksAndManualDisconnect(LockScopeEnumeration.ROLE_SESSION, true,
                false);
        // A user lock is removed, if only the role token is removed
        checkLocksAndManualDisconnect(LockScopeEnumeration.ROLE_SESSION, true,
                true);
    }

    /**
     * Test permanent lock deletion on logout
     */
    public void testPermanentLocksAndManualDisconnect() {
        // A permanent lock is not removed, if the user is logout
        checkLocksAndManualDisconnect(LockScopeEnumeration.PERMANENT, false,
                false);
        // A permanent lock is not removed, if only the role token is removed
        checkLocksAndManualDisconnect(LockScopeEnumeration.PERMANENT, false,
                true);
    }

    private void checkLocksAndManualDisconnect(LockScopeEnumeration pLockScope,
            boolean pIsRemoved, boolean pRemoveOnlyRoleToken) {
        // Use a new connection that can be closed
        final String lUserToken =
                authorizationService.login(USER_LOGGIN, USER_PWD);
        final String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        PRODUCT_NAME, getProcessName());
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        PRODUCT_NAME, SHEET_REF);

        // Lock the sheet 
        sheetService.lockSheet(lRoleToken, lSheetId, new LockProperties(
                LockTypeEnumeration.WRITE, pLockScope));

        final Lock lLock = sheetService.getLock(adminRoleToken, lSheetId);

        // The sheet is locked with a role session lock
        assertNotNull("Sheet not locked", lLock);
        assertTrue("Lock is not a " + pLockScope,
                pLockScope.equals(lLock.getScope()));

        if (pRemoveOnlyRoleToken) {
            // Close the role session
            authorizationService.closeRoleSession(lRoleToken);
        }
        else {
            // Close the connection
            authorizationService.logout(lUserToken);
        }

        final Lock lNewLock = sheetService.getLock(adminRoleToken, lSheetId);

        if (pIsRemoved) {
            assertNull("The sheet is always locked", lNewLock);
        }
        else {
            assertNotNull("The sheet has been unlocked", lNewLock);
            sheetService.removeLock(adminRoleToken, lSheetId);
        }
    }

    /**
     * Test user session lock deletion for expired sessions
     */
    public void testUserSessionLocksAndAutomaticDisconnect() {
        checkLocksAndAutomaticDisconnect(LockScopeEnumeration.USER_SESSION,
                true);
    }

    /**
     * Test role session lock deletion for expired sessions
     */
    public void testRoleSessionLocksAndAutomaticDisconnect() {
        checkLocksAndAutomaticDisconnect(LockScopeEnumeration.ROLE_SESSION,
                true);
    }

    /**
     * Test permanent lock deletion for expired sessions
     */
    public void testPermanentLocksAndAutomaticDisconnect() {
        checkLocksAndAutomaticDisconnect(LockScopeEnumeration.PERMANENT, false);
    }

    private static final int SESSION_TIME_S = 10;

    private static final int SLEEP_TIME_MS = 11000;

    private void checkLocksAndAutomaticDisconnect(
            LockScopeEnumeration pLockScope, boolean pIsRemoved) {
        // Use a new connection with a time out of 1 seconds
        final String lUserToken =
                authorizationService.login(USER_LOGGIN, USER_PWD,
                        SESSION_TIME_S);
        final String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        PRODUCT_NAME, getProcessName());
        final String lSheetId =
                sheetService.getCacheableSheet(SHEET_REF,
                        CacheProperties.IMMUTABLE).getId();

        // Lock the sheet
        sheetService.lockSheet(lRoleToken, lSheetId, new LockProperties(
                LockTypeEnumeration.WRITE, pLockScope));

        final Lock lLock = sheetService.getLock(adminRoleToken, lSheetId);

        // The sheet is locked
        assertNotNull("Sheet has not be locked", lLock);
        assertTrue("Lock is not a " + pLockScope,
                pLockScope.equals(lLock.getScope()));

        // Wait 2 seconds that the connection is auto-closed
        try {
            Thread.sleep(SLEEP_TIME_MS);
        }
        catch (InterruptedException e) {
            fail("Test abord");
        }

        // Launch the quartz job that clean the expired session
        ((GpmJob) ContextLocator.getContext().getBean("autoLogout")).execute();

        final Lock lNewLock = sheetService.getLock(adminRoleToken, lSheetId);

        if (pIsRemoved) {
            assertNull("The sheet is always locked", lNewLock);
        }
        else {
            assertNotNull("The sheet has been unlocked", lNewLock);
            sheetService.removeLock(adminRoleToken, lSheetId);
        }
    }

    /**
     * Test the job that clean all the user session locks when the server is
     * started
     */
    public void testUserSessionLockCleaner() {
        checkUserSessionLockCleaner(LockScopeEnumeration.USER_SESSION, true);
    }

    /**
     * Test the job that clean all the role session locks when the server is
     * started
     */
    public void testRoleSessionLockCleaner() {
        checkUserSessionLockCleaner(LockScopeEnumeration.ROLE_SESSION, true);
    }

    /**
     * Test the job that clean all the permanent locks when the server is
     * started
     */
    public void testPermanentLockCleaner() {
        checkUserSessionLockCleaner(LockScopeEnumeration.PERMANENT, false);
    }

    private void checkUserSessionLockCleaner(LockScopeEnumeration pLockScope,
            boolean pIsRemoved) {
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        PRODUCT_NAME, SHEET_REF);

        // Lock the sheet
        sheetService.lockSheet(adminRoleToken, lSheetId, new LockProperties(
                LockTypeEnumeration.WRITE, pLockScope));

        final Lock lLock = sheetService.getLock(adminRoleToken, lSheetId);

        // The sheet is locked
        assertNotNull("Sheet has not be locked", lLock);
        assertTrue("Lock is not a " + pLockScope,
                pLockScope.equals(lLock.getScope()));

        // Launch the quartz job that delete all session locks
        ((GpmJob) ContextLocator.getContext().getBean("sessionLockCleaner")).execute();

        final Lock lNewLock = sheetService.getLock(adminRoleToken, lSheetId);

        if (pIsRemoved) {
            assertNull("The sheet is always locked", lNewLock);
        }
        else {
            assertNotNull("The sheet has been unlocked", lNewLock);
            sheetService.removeLock(adminRoleToken, lSheetId);
        }
    }
}

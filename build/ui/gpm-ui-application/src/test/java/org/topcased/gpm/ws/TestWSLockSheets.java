/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.LockTypeEnumeration;
import org.topcased.gpm.ws.v2.client.SheetData;

/**
 * TestWSLockSheets
 * 
 * @author mfranche
 */
public class TestWSLockSheets extends AbstractWSTestCase {

    /** The default product name. */
    protected static final String DEFAULT_PRODUCT_NAME = "Bernard's store";

    /** The sheet reference. */
    protected static final String DOG_REF = "Medor";

    /**
     * Test the lockSheet method in normal conditions : the current user puts a
     * READ_LOCK => verify that user2 cannot read the sheet. the current user
     * puts a READ_WRITE_LOCK => verify that user2 cannot read nor write the
     * sheet. the current user puts a NO_LOCK => verify that user2 can read and
     * write the sheet.
     */
    public void testLockSheetNormalCase() {
        try {
            // Get the SheetData associated to the reference DOG_REF
            List<String> lListRefs = new ArrayList<String>();
            lListRefs.add(DOG_REF);
            List<SheetData> lSheetDataList =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            lListRefs);

            // Check the result
            assertNotNull(
                    "The method getSheetsByRefs returns an incorrect result",
                    lSheetDataList);
            assertTrue(
                    "The method getSheetsByRefs returns an incorrect result",
                    lSheetDataList.size() == 1);

            SheetData lSheetData = lSheetDataList.get(0);

            // The current user locks the sheet in READ mode
            staticServices.lockSheet(roleToken, lSheetData.getId(),
                    LockTypeEnumeration.WRITE);

            // Login of another user
            String lUserToken = staticServices.login("user2", "pwd2");
            String lRoleToken2 =
                    staticServices.selectRole(lUserToken, "notadmin",
                            DEFAULT_PRODUCT_NAME, DEFAULT_PROCESS_NAME);

            // Try for the user 2 to access the sheet
            // (ok since admin user puts a write_lock)
            staticServices.getSheetsByRefs(lRoleToken2, DEFAULT_PROCESS_NAME,
                    DEFAULT_PRODUCT_NAME, lListRefs);

            List<SheetData> lListSheetData = new ArrayList<SheetData>();
            lListSheetData.add(lSheetData);

            try {
                // try for the user 2 to modify the sheet
                // (not ok since admin user puts a write lock)
                staticServices.updateSheets(lRoleToken2, DEFAULT_PROCESS_NAME,
                        lListSheetData);

                fail("An exception should have been thrown.");
            }
            catch (Exception ex) {
                // ok.
            }

            // Lock the sheet in READ_WRITE mode
            staticServices.lockSheet(roleToken, lSheetData.getId(),
                    LockTypeEnumeration.READ_WRITE);

            try {
                // Try for the user 2 to access the sheet
                // (not ok since admin user puts a read_write lock)
                staticServices.getSheetsByRefs(lRoleToken2,
                        DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME, lListRefs);
                fail("An exception should have been thrown.");
            }
            catch (Exception ex) {
                // ok.
            }

            // Try for the user 2 to modify the sheet
            lSheetData.setReference("newRef");

            lListSheetData = new ArrayList<SheetData>();
            lListSheetData.add(lSheetData);

            try {
                // try for the user 2 to modify the sheet
                // (not ok since admin user puts a read_write lock)
                staticServices.updateSheets(lRoleToken2, DEFAULT_PROCESS_NAME,
                        lListSheetData);

                fail("An exception should have been thrown.");
            }
            catch (Exception ex) {
                // ok.
            }

            // Unlock the sheet
            staticServices.lockSheet(roleToken, lSheetData.getId(),
                    LockTypeEnumeration.NONE);

            // Try for the user 2 to access the sheet (ok since no_lock)
            staticServices.getSheetsByRefs(lRoleToken2, DEFAULT_PROCESS_NAME,
                    DEFAULT_PRODUCT_NAME, lListRefs);

            lSheetDataList =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            lListRefs);

            lSheetData = lSheetDataList.get(0);

            lListSheetData = new ArrayList<SheetData>();
            lListSheetData.add(lSheetData);

            // Try for the user 2 to modify the sheet (ok since no_lock)
            staticServices.updateSheets(lRoleToken2, DEFAULT_PROCESS_NAME,
                    lListSheetData);

        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * tests the change lock method in normal conditions : user1 set READ_LOCK
     * and user2 try to set READ_WRITE_LOCK => impossible and admin try to set
     * READ_WRITE_LOCK => possible
     */
    public void testChangeLockNormalCase() {
        try {
            // Get the SheetData associated to the reference DOG_REF
            List<String> lListRefs = new ArrayList<String>();
            lListRefs.add(DOG_REF);
            List<SheetData> lSheetDataList =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            lListRefs);

            // Check the result
            assertNotNull(
                    "The method getSheetsByRefs returns an incorrect result",
                    lSheetDataList);
            assertTrue(
                    "The method getSheetsByRefs returns an incorrect result",
                    lSheetDataList.size() == 1);

            SheetData lSheetData = lSheetDataList.get(0);

            // User1 login
            String lUserToken1 = staticServices.login("user1", "pwd1");
            String lRoleToken1 =
                    staticServices.selectRole(lUserToken1, "admin",
                            DEFAULT_PRODUCT_NAME, DEFAULT_PROCESS_NAME);

            // User1 changes the lock to READ_LOCK
            staticServices.lockSheet(lRoleToken1, lSheetData.getId(),
                    LockTypeEnumeration.WRITE);

            // User2 login
            String lUserToken2 = staticServices.login("user2", "pwd2");
            String lRoleToken2 =
                    staticServices.selectRole(lUserToken2, "notadmin",
                            DEFAULT_PRODUCT_NAME, DEFAULT_PROCESS_NAME);

            // User2 changes the lock to READ_WRITE_LOCK
            // (it must not be possible since the user1 has already put a read_lock)
            try {
                staticServices.lockSheet(lRoleToken2, lSheetData.getId(),
                        LockTypeEnumeration.READ_WRITE);
                fail("An exception should have been thrown.");
            }
            catch (Exception ex) {
                // ok
            }

            // Admin changes the lock to READ_WRITE_LOCK
            // (it must be possible since the admin role can change
            // the lock even if a lock has already been set)
            staticServices.lockSheet(roleToken, lSheetData.getId(),
                    LockTypeEnumeration.READ_WRITE);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }
}
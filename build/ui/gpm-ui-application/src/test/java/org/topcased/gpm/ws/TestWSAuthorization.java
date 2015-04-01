/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.ws.v2.client.AccessControlContextData;
import org.topcased.gpm.ws.v2.client.ActionAccessControlData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.RoleProperties;
import org.topcased.gpm.ws.v2.client.SheetType;
import org.topcased.gpm.ws.v2.client.User;

/**
 * Test the authorization methods, accessing through web services.
 * 
 * @author ogehin
 */
public class TestWSAuthorization extends AbstractWSTestCase {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER =
            Logger.getLogger(TestWSAuthorization.class);

    /** The user login */
    private static final String USER_LOGIN = "user2";

    /** The user password */
    private static final String USER_PASSWORD = "pwd2";

    /*The user data (Name, forname, email, ) */
    /** The user name */
    private static final String USER_NAME = "name2";

    /** The user forname */
    private static final String USER_FORNAME = "forname2";

    /** The user e-mail */
    private static final String USER_EMAIL = "user2@acme.com";

    /** A not valid token */
    private static final String INVALIDTOKEN = new UID().toString();

    /** Expected Process names */
    private static final String[] EXPECTED_PROCESS_NAMES =
            { GpmTestValues.PROCESS_NAME };

    /** Expected Roles */
    private static final String[] EXPECTED_ROLES = { "admin" };

    private static final String[] PRODUCT_ADMIN_PRODUCT_NAMES =
            new String[] { "product3" };

    private static final String PRODUCT_ADMIN_LOGIN = "admin3";

    private static final String PRODUCT_ADMIN_PWD = "pwd3";

    private static final String PRODUCT_ADMIN_ROLE_NAME = "admin";

    private static final String ROLE_TO_SET = "viewer";

    private static final String ROLE_TO_REMOVE = "viewer";

    private static final String[] LOGINS =
            new String[] { "user1", "user2", "user3" };

    /**
     * Test the login method in normal conditions.
     */
    public void testLoginLogoutNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Login Logout in normal conditions");
        }
        try {
            String lUserToken = staticServices.login(USER_LOGIN, USER_PASSWORD);
            assertNotNull("Login failed with a correct login and password",
                    lUserToken);
            staticServices.logout(lUserToken);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the login method with a wrong password.
     */
    public void testLoginWrongPassword() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Login with a wrong password");
        }
        try {
            String lUserToken = staticServices.login(USER_LOGIN, "");
            assertNull("Login didn't failed with a wrong password", lUserToken);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the logout method with null userToken.
     */
    public void testLogoutNullUserToken() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Logout with a null userToken");
        }
        try {
            staticServices.logout("");
        }
        catch (GDMException_Exception e) {
            // ok
        }
    }

    /**
     * Test the logout method with a invalid userToken.
     */
    public void testLogoutInvalidUserToken() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Logout with an invalid userToken");
        }
        try {
            staticServices.logout(INVALIDTOKEN);
        }
        catch (GDMException_Exception e) {
            // ok
        }
    }

    /**
     * Test the getBusinessProcessNames method in normal conditions.
     */
    public void testGetBusinessProcessNamesNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get business process names in normal conditions");
        }
        try {
            List<String> lProcessNames =
                    staticServices.getBusinessProcessNames(userToken);
            assertNotNull("The method getBusinessProcessNames returns null.",
                    lProcessNames);
            int lSize = lProcessNames.size();
            int lExpectedSize = EXPECTED_PROCESS_NAMES.length;
            assertEquals("The method getBusinessProcessNames returns " + lSize
                    + "process names instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            assertTrue(
                    "",
                    lProcessNames.containsAll(Arrays.asList(EXPECTED_PROCESS_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getProductNames method in normal conditions.
     */
    public void testGetProductNamesNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get product names in normal conditions");
        }
        try {
            List<String> lProductNames =
                    staticServices.getProductNames(userToken,
                            DEFAULT_PROCESS_NAME);
            assertNotNull("The method getProductNames returns null.",
                    lProductNames);
            int lSize = lProductNames.size();
            int lExpectedSize = GpmTestValues.PRODUCT_NAMES.length;
            assertEquals("The method getBusinessProductNames returns " + lSize
                    + "process names instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            assertTrue(
                    "The method getBusinessProductNames returns not all expected product names",
                    lProductNames.containsAll(Arrays.asList(GpmTestValues.PRODUCT_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getRolesNames method in normal conditions.
     */
    public void testGetRolesNamesNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get roles names in normal conditions");
        }
        try {
            List<String> lRolesNames =
                    staticServices.getRolesNames(userToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME);
            assertNotNull("The method getProducts returns null.", lRolesNames);
            int lSize = lRolesNames.size();
            int lExpectedSize = EXPECTED_ROLES.length;
            assertEquals("The method getRolesNames returns " + lSize
                    + "process names instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            assertTrue("",
                    lRolesNames.containsAll(Arrays.asList(EXPECTED_ROLES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getRoleName method in normal conditions.
     */
    public void testGetRoleNameNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get roles names in normal conditions");
        }
        try {
            String lRoleName = staticServices.getRoleName(roleToken);
            assertNotNull("The method getRoleName returns null.", lRoleName);
            assertEquals("The method getRoleName returns " + lRoleName
                    + "role name instead of " + DEFAULT_ROLE + ".",
                    DEFAULT_ROLE, lRoleName);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the selectRole method in normal conditions.
     */
    public void testSelectRoleNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Select a role in normal conditions");
        }
        try {
            String lRoleToken =
                    staticServices.selectRole(userToken, getRole(),
                            getProductName(), getProcessName());
            assertNotNull("SelectRole failed with correct parameters",
                    lRoleToken);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the selectRole method with a wrong parameter.
     */
    public void testSelectRoleWrongParameter() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Select a role with a wrong parameter");
        }
        try {
            String lRoleToken =
                    staticServices.selectRole(INVALIDTOKEN, getRole(),
                            getProductName(), getProcessName());
            assertNull("SelectRole failed with wrong parameters", lRoleToken);
        }
        catch (GDMException_Exception e) {
            // ok
        }
    }

    /**
     * Test the getUserFromRole method in normal case.
     */
    public void testGetUserFromRole() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get an user from a role in normal case");
        }
        try {
            String lUserToken = staticServices.login(USER_LOGIN, USER_PASSWORD);
            String lRoleToken =
                    staticServices.selectRole(lUserToken, "notadmin",
                            DEFAULT_PRODUCT_NAME, DEFAULT_PROCESS_NAME);
            User lUser = staticServices.getUserFromRole(lRoleToken);
            assertNotNull("GetUserFromRole failed in normal case", lUser);
            assertEquals("The method getUserFromRole returns this login "
                    + lUser.getLogin() + " instead of " + USER_LOGIN,
                    lUser.getLogin(), USER_LOGIN);
            assertEquals("The method getUserFromRole returns this forname "
                    + lUser.getForname() + " instead of forname2",
                    lUser.getForname(), "forname2");
            assertEquals("The method getUserFromRole returns this role name "
                    + lUser.getRoles().get(0).getName()
                    + " instead of notadmin",
                    lUser.getRoles().get(0).getName(), "notadmin");
            assertEquals("The method getUserFromRole returns this mail adress "
                    + lUser.getMail() + " instead of user2@acme.com",
                    lUser.getMail(), "user2@acme.com");

        }
        catch (GDMException_Exception e) {
            // ok
        }
    }

    /**
     * Test method getAllUsers
     */
    public void testGetAllUsers() {
        try {
            List<User> lUsers = staticServices.getAllUsers();

            assertEquals(GpmTestValues.USERS_COUNT, lUsers.size());
            List<String> lUserLogins = new ArrayList<String>(lUsers.size());
            for (User lUser : lUsers) {
                lUserLogins.add(lUser.getLogin());
            }
            assertTrue(
                    "Some users are different.",
                    lUserLogins.containsAll(Arrays.asList(GpmTestValues.USERS_LOGIN)));
        }
        catch (GDMException_Exception e) {
            fail("GDMException has been thrown.");
        }

    }

    /**
     * Test method getUserFromLogin
     */
    public void testGetUserFromLogin() {
        try {
            User lUser = staticServices.getUserFromLogin(USER_LOGIN);

            assertNotNull("User '" + USER_LOGIN + "' has not been found.",
                    lUser);
            // Check that user values are equal
            assertEquals(USER_NAME, lUser.getName());
            assertEquals(USER_FORNAME, lUser.getForname());
            assertEquals(USER_EMAIL, lUser.getMail());

        }
        catch (GDMException_Exception e) {
            fail("GDMException has been thrown.");
        }
    }

    /**
     * Test methods addRoleForUsers and getUsersWithRole
     */
    public void testAddRoleForUsers() {
        try {
            String lUserToken =
                    staticServices.login(PRODUCT_ADMIN_LOGIN, PRODUCT_ADMIN_PWD);
            String lRoleToken =
                    staticServices.selectRole(lUserToken,
                            PRODUCT_ADMIN_ROLE_NAME,
                            PRODUCT_ADMIN_PRODUCT_NAMES[0], getProcessName());

            //Call tested method
            staticServices.addRoleForUsers(lRoleToken, Arrays.asList(LOGINS),
                    ROLE_TO_SET, PRODUCT_ADMIN_PRODUCT_NAMES[0],
                    getProcessName());

            // Check that this role is present for all specified users

            List<String> lLogins =
                    staticServices.getUsersWithRole(getProcessName(),
                            ROLE_TO_SET,
                            Arrays.asList(PRODUCT_ADMIN_PRODUCT_NAMES), false,
                            false);

            assertTrue("Some roles have not been set.",
                    lLogins.containsAll((List<String>) Arrays.asList(LOGINS)));
        }
        catch (GDMException_Exception e) {
            fail("GDMException has been thrown.");
        }
    }

    /**
     * Test methods removeRoleForUsers and getUsersWithRole
     */
    public void testRemoveRoleForUsers() {
        try {
            String lUserToken =
                    staticServices.login(PRODUCT_ADMIN_LOGIN, PRODUCT_ADMIN_PWD);
            String lRoleToken =
                    staticServices.selectRole(lUserToken,
                            PRODUCT_ADMIN_ROLE_NAME,
                            PRODUCT_ADMIN_PRODUCT_NAMES[0], getProcessName());

            //Add role for a list of users and check that roles are set
            staticServices.addRoleForUsers(lRoleToken, Arrays.asList(LOGINS),
                    ROLE_TO_SET, PRODUCT_ADMIN_PRODUCT_NAMES[0],
                    getProcessName());
            List<String> lLogins =
                    staticServices.getUsersWithRole(getProcessName(),
                            ROLE_TO_SET,
                            Arrays.asList(PRODUCT_ADMIN_PRODUCT_NAMES), false,
                            false);
            //Call tested method
            staticServices.removeRoleForUsers(lRoleToken,
                    Arrays.asList(LOGINS), ROLE_TO_REMOVE,
                    PRODUCT_ADMIN_PRODUCT_NAMES[0], getProcessName());

            // Check that this role is not present for all specified users
            lLogins =
                    staticServices.getUsersWithRole(getProcessName(),
                            ROLE_TO_REMOVE,
                            Arrays.asList(PRODUCT_ADMIN_PRODUCT_NAMES), false,
                            false);

            assertTrue("Some roles have not been remove.", lLogins.isEmpty());
        }
        catch (GDMException_Exception e) {
            fail("GDMException has been thrown.");
        }
    }

    private final static String[] USER = new String[] { "user1", "pwd1" };

    private final static String PRODUCT_NAME = "Happy Mouse";

    private final static String[] ROLES_NAMES_ON_PRODUCT_ONLY =
            new String[] { "viewer" };

    private final static String[] ROLES_NAMES_ON_INSTANCE_ONLY =
            new String[] { "admin" };

    private final static String[] ROLES_NAMES_ON_PRODUCT_OR_INSTANCE =
            new String[] { "viewer", "admin" };

    /**
     * Test get roles name on product
     * 
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    public void testGetRolesNamesOnProductOnly() throws GDMException_Exception {
        String lUserToken = staticServices.login(USER[0], USER[1]);
        // Get roles available on specified product
        RoleProperties lRoleProperties = new RoleProperties();
        lRoleProperties.setInstanceRole(false);
        lRoleProperties.setProductRole(true);
        // Get roles available on instance
        Collection<String> lRoles =
                staticServices.getRolesNames(lUserToken, getProcessName(),
                        PRODUCT_NAME, lRoleProperties);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_PRODUCT_ONLY.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_PRODUCT_ONLY)));
    }

    /**
     * Test get roles name on instance
     * 
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    public void testGetRolesNamesOnInstanceOnly() throws GDMException_Exception {
        String lUserToken = staticServices.login(USER[0], USER[1]);

        RoleProperties lRoleProperties = new RoleProperties();
        lRoleProperties.setInstanceRole(true);
        lRoleProperties.setProductRole(false);
        // Get roles available on instance
        Collection<String> lRoles =
                staticServices.getRolesNames(lUserToken, getProcessName(),
                        PRODUCT_NAME, lRoleProperties);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_INSTANCE_ONLY.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_INSTANCE_ONLY)));
    }

    /**
     * Test get roles name on instance or product
     * 
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    public void testGetRolesNamesOnInstanceOrProduct()
        throws GDMException_Exception {
        String lUserToken = staticServices.login(USER[0], USER[1]);
        // Get roles available on instance or specified product
        RoleProperties lRoleProperties = new RoleProperties();
        lRoleProperties.setInstanceRole(true);
        lRoleProperties.setProductRole(true);
        // Get roles available on instance
        Collection<String> lRoles =
                staticServices.getRolesNames(lUserToken, getProcessName(),
                        PRODUCT_NAME, lRoleProperties);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_PRODUCT_OR_INSTANCE.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_PRODUCT_OR_INSTANCE)));
    }

    private final static List<String> ACTION_NAMES = new ArrayList<String>();

    private final static List<Boolean> ACTION_CONFIDENTIAL_ACCESS =
            new ArrayList<Boolean>();

    private final static List<Boolean> ACTION_ENABLED_ACCESS =
            new ArrayList<Boolean>();

    static {
        ACTION_NAMES.add("Sheet.Create.Cat");
        ACTION_CONFIDENTIAL_ACCESS.add(Boolean.FALSE);
        ACTION_ENABLED_ACCESS.add(Boolean.TRUE);

        ACTION_NAMES.add("Interface.TestInputData_ChoiceString");
        ACTION_CONFIDENTIAL_ACCESS.add(Boolean.FALSE);
        ACTION_ENABLED_ACCESS.add(Boolean.TRUE);

        ACTION_NAMES.add("Interface.TestInputData");
        ACTION_CONFIDENTIAL_ACCESS.add(Boolean.FALSE);
        ACTION_ENABLED_ACCESS.add(Boolean.FALSE);

        ACTION_NAMES.add("Util.SendMail");
        ACTION_CONFIDENTIAL_ACCESS.add(Boolean.FALSE);
        ACTION_ENABLED_ACCESS.add(Boolean.TRUE);
    }

    private final static String SHEET_TYPE_NAME = "Cat";

    private final static String SHEET_TYPE_STATE = "Open";

    /**
     * Tests method getActionAccessControls
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetActionAccessControls() throws GDMException_Exception {

        SheetType lSheetType =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), SHEET_TYPE_STATE,
                        SHEET_TYPE_NAME);

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setContainerTypeId(lSheetType.getId());
        lAccessControlContextData.setStateName(SHEET_TYPE_STATE);

        List<ActionAccessControlData> lActionAccessControlDatas =
                staticServices.getActionAccessControls(roleToken,
                        lAccessControlContextData, ACTION_NAMES);

        for (int i = 0; i < ACTION_NAMES.size(); i++) {
            assertEquals(ACTION_NAMES.get(i),
                    lActionAccessControlDatas.get(i).getLabelKey());
            assertEquals("Action '" + ACTION_NAMES.get(i)
                    + "' has not good confidential access",
                    ACTION_CONFIDENTIAL_ACCESS.get(i),
                    lActionAccessControlDatas.get(i).isConfidential());
            assertEquals("Action '" + ACTION_NAMES.get(i)
                    + "' has not good enabled access",
                    ACTION_ENABLED_ACCESS.get(i),
                    lActionAccessControlDatas.get(i).isEnabled());
        }
    }
}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;

/**
 * TestUserImport: Test user importation.
 * <p>
 * Additionally test user importation with not admin user. The user only has
 * adminAccess.
 * </p>
 * 
 * @author mkargbo
 */
public class TestUserImport extends AbstractImportTest<EndUserData> {
    private static final String INSTANCE_FILE =
            "importation/user/usersToInstantiate.xml";

    private static final String FILE_TO_IMPORT =
            "importation/user/usersToImport.xml";

    private static final String FILE_TO_UPDATE =
            "importation/user/usersToUpdate.xml";

    private static final String UPDATED = "_UPDATED";

    private static final String[] USER_CREATE =
            new String[] { "userImportCreate", "pwd",
                          "roleUserCreateImportTest" };

    private static final String[] USER_UPDATE =
            new String[] { "userImportUpdate", "pwd",
                          "roleUserUpdateImportTest" };

    private static final String[] USER_DELETE =
            new String[] { "userImportDelete", "pwd",
                          "roleUserDeleteImportTest" };

    private static final String[] USER_CREATE_DELETE =
            new String[] { "userImportCreateDelete", "pwd",
                          "roleUserCreateDeleteImportTest" };

    private static final Set<String[]> IMPORTED_USER;

    private static final Map<String, String> EXPECTED_ATTRIBUTES_VALUES;

    private static final Map<String, String> EXPECTED_ATTRIBUTES_VALUES_UPDATE;

    static {
        IMPORTED_USER = new HashSet<String[]>();
        IMPORTED_USER.add(new String[] { "userImport_01", "User import 01" });
        IMPORTED_USER.add(new String[] { "userImport_02", "User import 02" });
        IMPORTED_USER.add(new String[] { "userImport_03", "User import 03" });

        EXPECTED_ATTRIBUTES_VALUES = new HashMap<String, String>(2);
        EXPECTED_ATTRIBUTES_VALUES.put("userAttribute_01", "value01");
        EXPECTED_ATTRIBUTES_VALUES.put("userAttribute_02", "value02");

        EXPECTED_ATTRIBUTES_VALUES_UPDATE = new HashMap<String, String>(2);
        EXPECTED_ATTRIBUTES_VALUES_UPDATE.put("userAttribute_01",
                "value01_UPDATED");
        EXPECTED_ATTRIBUTES_VALUES_UPDATE.put("userAttribute_02",
                "value02_UPDATED");
    }

    private AuthorizationServiceImpl authorizationServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        authorizationServiceImpl =
                (AuthorizationServiceImpl) ContextLocator.getContext().getBean(
                        "authorizationServiceImpl");
        instantiate(getProcessName(), INSTANCE_FILE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue("User '" + lRef[0] + "' is not imported.",
                    StringUtils.isNotBlank(lId));
            EndUserData lUser = getBusinessObject(adminRoleToken, lId);
            assertEquals(lRef[1], lUser.getName());
            checkAttributes(lUser.getUserAttributes(),
                    EXPECTED_ATTRIBUTES_VALUES);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        //No assertion

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#eraseAssertion(java.lang.Object[])
     */
    @Override
    protected void eraseAssertion(Object... pArgs) {
        //Compare identifier.
        String lExpectedId = (String) pArgs[0];
        String lActualId = (String) pArgs[1];
        assertNotNull("Error cannot retrieve the identifier.", lExpectedId);
        assertNotSame("Identifiers should be different.", lExpectedId,
                lActualId);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Map<String[], String> erasePreCondition() {
        //Getting identifier
        Map<String[], String> lIdentifiers = new HashMap<String[], String>();
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            lIdentifiers.put(lRef, lId);
        }
        return lIdentifiers;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected EndUserData getBusinessObject(String pRoleToken, String pElementId) {
        String lUserLogin = authorizationServiceImpl.getUserLogin(pElementId);
        EndUserData lEndUser = authorizationServiceImpl.getUserData(lUserLogin);
        return lEndUser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String[])
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lLogin = pElementRef[0];
        String lId = authorizationServiceImpl.getUserId(lLogin);
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFile()
     */
    @Override
    protected String getImportFile() {
        return FILE_TO_IMPORT;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFileForUpdating()
     */
    @Override
    protected String getImportFileForUpdating() {
        return FILE_TO_UPDATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        return IMPORTED_USER;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElementId) {
        String lLogin = authorizationServiceImpl.getUserLogin(pElementId);
        authorizationServiceImpl.removeUser(adminRoleToken, lLogin);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setUsersFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        final Map<String[], String> lElements =
                (Map<String[], String>) erasePreCondition();
        try {
            doImport(adminRoleToken, ImportFlag.ERASE, getImportFile());

            //Test elements existence
            for (String[] lRef : getImportedElement()) {
                String lId = getElementId(lRef);
                String lExpectedIdentifier = lElements.get(lRef);
                eraseAssertion(lExpectedIdentifier, lId);
            }
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyAdminAssertion(Collection<String> pElementsId) {
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue("User '" + lRef[0] + "' is not imported.",
                    StringUtils.isNotBlank(lId));
            EndUserData lUser = getBusinessObject(adminRoleToken, lId);
            assertEquals(lRef[1] + UPDATED, lUser.getName());
            checkAttributes(lUser.getUserAttributes(),
                    EXPECTED_ATTRIBUTES_VALUES_UPDATE);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        //Nothing to do.
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testCreateOnlyImportNotAdmin()
     */
    @Override
    public void testCreateOnlyImportNotAdmin() {
        deleteElements();
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY,
                    getNotAdminImportFile());
            fail("No rights to create user");
        }
        catch (ImportException e) {
            //Ok  
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testUpdateOnlyImportNotAdmin()
     */
    @Override
    public void testUpdateOnlyImportNotAdmin() {
        try {
            doImport(normalRoleToken, ImportFlag.UPDATE_ONLY,
                    getNotAdminImportFileForUpdating());
            fail("No rights to update user");
        }
        catch (ImportException e) {
            //Ok  
        }
    }

    /**
     * Test import using CREATE_ONLY flag by user who has only user.create
     * adminAccess.
     */
    public void testCreateNotAdmin() {
        deleteElements();
        String lUsertoken =
                authorizationService.login(USER_CREATE[0], USER_CREATE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken, USER_CREATE[2],
                        StringUtils.EMPTY, getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.CREATE_ONLY, getImportFile());
            createOnlyAdminAssertion();
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test import using UPDATE_ONLY flag by user who has only user.create
     * adminAccess.
     */
    public void testFailCreateNotAdmin() {
        deleteElements();
        String lUsertoken =
                authorizationService.login(USER_CREATE[0], USER_CREATE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken, USER_CREATE[2],
                        StringUtils.EMPTY, getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.UPDATE_ONLY, getImportFile());
            fail("Should not have rights to create user.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Test import using UPDATE_ONLY flag by user who has only user.update
     * adminAccess.
     */
    public void testUpdateNotAdmin() {
        String lUsertoken =
                authorizationService.login(USER_UPDATE[0], USER_UPDATE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken, USER_UPDATE[2],
                        StringUtils.EMPTY, getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.UPDATE_ONLY,
                    getImportFileForUpdating());
            updateOnlyAdminAssertion(null);
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test import using CREATE_ONLY flag by user who has only user.update
     * adminAccess.
     */
    public void testFailUpdateNotAdmin() {
        deleteElements();
        String lUsertoken =
                authorizationService.login(USER_UPDATE[0], USER_UPDATE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken, USER_UPDATE[2],
                        StringUtils.EMPTY, getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.CREATE_ONLY, getImportFile());
            fail("Should not have rights to create user.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Test import using ERASE_ONLY flag by user who has only user.create and
     * user.delete adminAccess.
     */
    public void testEraseNotAdmin() {
        deleteElements();
        String lUsertoken =
                authorizationService.login(USER_CREATE_DELETE[0],
                        USER_CREATE_DELETE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken,
                        USER_CREATE_DELETE[2], StringUtils.EMPTY,
                        getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.ERASE, getImportFile());
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test import using ERASE_ONLY flag by user who has only user.delete
     * adminAccess.
     */
    public void testFailEraseNotAdmin() {
        deleteElements();
        String lUsertoken =
                authorizationService.login(USER_DELETE[0], USER_DELETE[1]);
        String lRoletoken =
                authorizationService.selectRole(lUsertoken, USER_DELETE[2],
                        StringUtils.EMPTY, getProcessName());
        try {
            doImport(lRoletoken, ImportFlag.ERASE, getImportFile());
            fail("No rights to import user in erase mode.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Check the attributes existence and their values.
     * 
     * @param pAttributes
     *            User's attributes to check
     * @param pExpectedAttributesValues
     *            Attributes that should be import.
     */
    protected void checkAttributes(UserAttributesData[] pAttributes,
            Map<String, String> pExpectedAttributesValues) {
        if (!ArrayUtils.isEmpty(pAttributes)) {
            for (String lAttributeName : pExpectedAttributesValues.keySet()) {
                String lValue = pExpectedAttributesValues.get(lAttributeName);
                if (lValue != null) {
                    String lAttributeValue =
                            getUserAttribute(pAttributes, lAttributeName);
                    assertFalse("The attribute '" + lAttributeName
                            + "' must have a value",
                            StringUtils.isBlank(lAttributeValue));
                    assertEquals("The new value of the attribute'"
                            + lAttributeName
                            + "' is not well imported. (Expected value: '"
                            + lValue + "', Actual value: '" + lAttributeValue
                            + "'", lValue, lAttributeValue);
                }
            }
        }
    }

    /**
     * Get the value of the user's attributes.
     * <p>
     * Find the attribute and get its value.
     * </p>
     * 
     * @param pAttributes
     *            List Attributes
     * @param pAttributeName
     *            Name of the attribute to find.
     * @return Value of the attribute if found, Empty string otherwise.
     */
    protected String getUserAttribute(UserAttributesData[] pAttributes,
            String pAttributeName) {
        boolean lFound = false;
        int i = 0;
        while (!lFound && i < pAttributes.length) {
            UserAttributesData lUserAttributesData = pAttributes[i];
            if (lUserAttributesData.getName().equals(pAttributeName)) {
                lFound = true;
            }
            else {
                i++;
            }
        }
        if (lFound) {
            return pAttributes[i].getValue();
        }
        else {
            return StringUtils.EMPTY;
        }
    }

}
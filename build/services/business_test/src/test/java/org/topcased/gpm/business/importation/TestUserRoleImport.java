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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.serialization.data.UserRole;

/**
 * TestUserRoleImport: Test user's role importation.
 * 
 * @author mkargbo
 */
public class TestUserRoleImport extends AbstractImportTest<UserRole> {

    /** ID_PRODUCT_INDEX */
    private static final int ID_PRODUCT_INDEX = 2;

    /** ID_ROLE_INDEX */
    private static final int ID_ROLE_INDEX = 1;

    /** ID_LOGIN_INDEX */
    private static final int ID_LOGIN_INDEX = 0;

    /** USER_PRODUCT_INDEX */
    private static final int USER_PRODUCT_INDEX = 3;

    /** USER_ROLE_INDEX */
    private static final int USER_ROLE_INDEX = 2;

    /** USER_PASSWORD_INDEX */
    private static final int USER_PASSWORD_INDEX = 1;

    /** USER_LOGIN_INDEX */
    private static final int USER_LOGIN_INDEX = 0;

    private static final String INSTANCE_FILE =
            "importation/userrole/userroleToInstantiate.xml";

    private static final String FILE_TO_IMPORT =
            "importation/userrole/userroleToImport.xml";

    private static final String FILE_TO_ERASE =
            "importation/userrole/userroleToErase.xml";

    private static final String[] USER =
            new String[] { "userRoleImportUpdate", "pwd",
                          "userRoleUserUpdateImportTest", "userRoleProduct" };

    private static final Set<String[]> IMPORTED_ELEMENTS;

    private static final Set<String[]> IMPORTED_ELEMENTS_ERASE;

    static {
        IMPORTED_ELEMENTS = new HashSet<String[]>(2);
        IMPORTED_ELEMENTS.add(new String[] { "userRoleImport_01",
                                            "userRole_01", StringUtils.EMPTY });
        IMPORTED_ELEMENTS.add(new String[] { "userRoleImport_02",
                                            "userRole_02", "userRoleProduct" });

        IMPORTED_ELEMENTS_ERASE = new HashSet<String[]>(2);
        IMPORTED_ELEMENTS_ERASE.add(new String[] { "userRoleImport_01",
                                                  "userRole_01",
                                                  "userRoleProduct" });
        IMPORTED_ELEMENTS_ERASE.add(new String[] { "userRoleImport_02",
                                                  "userRole_02",
                                                  StringUtils.EMPTY });
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
        instantiate(getProcessName(), INSTANCE_FILE);
        authorizationServiceImpl =
                (AuthorizationServiceImpl) ContextLocator.getContext().getBean(
                        "authorizationServiceImpl");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        for (String[] lImportUser : getImportedElement()) {
            assertion(lImportUser);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        for (String[] lImportUser : getImportedElement()) {
            assertion(lImportUser);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#eraseAssertion(java.lang.Object[])
     */
    @Override
    protected void eraseAssertion(Object... pArgs) {
        for (String[] lImportUser : IMPORTED_ELEMENTS_ERASE) {
            assertion(lImportUser);
        }
    }

    private void assertion(String[] pRef) {
        String lLogin = pRef[ID_LOGIN_INDEX];
        String lRoleName = pRef[ID_ROLE_INDEX];
        String lProductName = pRef[ID_PRODUCT_INDEX];
        if (StringUtils.isBlank(lProductName)) {
            assertTrue("User '" + lLogin + "' must have the admin role '"
                    + lRoleName + "'", authorizationServiceImpl.isAssignTo(
                    lLogin, lRoleName));
        }
        else {
            assertFalse("User '" + lLogin + "' must not have the admin role '"
                    + lRoleName + "'", authorizationServiceImpl.isAssignTo(
                    lLogin, lRoleName));
            assertTrue("User '" + lLogin + "' must have the role '" + lRoleName
                    + "' for the product '" + lProductName + "'",
                    authorizationServiceImpl.isAssignTo(lLogin, lRoleName,
                            lProductName));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Object erasePreCondition() {
        //No erase pre-condition.
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected UserRole getBusinessObject(String pRoleToken, String pElementId) {
        //No business object.
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String[])
     */
    @Override
    protected String getElementId(String... pElementRef) {
        //No technical id.
        return null;
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
        //No update
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        return IMPORTED_ELEMENTS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#deleteElements()
     */
    @Override
    protected void deleteElements() {
        //Delete elements
        for (String[] lRef : getImportedElement()) {
            String lLogin = lRef[ID_LOGIN_INDEX];
            String lRoleName = lRef[ID_ROLE_INDEX];
            String lProductName = lRef[ID_PRODUCT_INDEX];
            authorizationServiceImpl.removeRoleAssignement(adminRoleToken,
                    lLogin, lRoleName, lProductName, getProcessName());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElementId) {
        //No remove using technical identifier.
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setUserRolesFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.ERASE, FILE_TO_ERASE);
            //Test elements existence
            eraseAssertion();
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
        //No assertion
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        //No assertion.
    }

    /**
     * Not implemented for UPDATE_ONLY flag.
     */
    @Override
    public void testUpdateOnlyImportAdmin() {
        try {
            doImport(adminRoleToken, ImportFlag.UPDATE_ONLY,
                    getAdminImportFile());

            fail("Cannot use 'UPDATE_ONLY' flag for user's roles importation.");
        }
        catch (NotImplementedException e) {
            //Ok
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Do nothing.
     */
    @Override
    public void testUpdateOnlyImportNotAdmin() {
        //
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testUpdateOnlyExistsImport()
     */
    @Override
    public void testUpdateOnlyExistsImport() {
        //
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testCreateOnlyImportNotAdmin()
     */
    @Override
    public void testCreateOnlyImportNotAdmin() {
        deleteElements();
        String lUserToken =
                authorizationServiceImpl.login(USER[USER_LOGIN_INDEX],
                        USER[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationServiceImpl.selectRole(lUserToken,
                        USER[USER_ROLE_INDEX], USER[USER_PRODUCT_INDEX],
                        getProcessName());
        try {
            doImport(lRoleToken, ImportFlag.CREATE_ONLY,
                    getNotAdminImportFile());
            createOnlyNotAdminAssertion();
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test importation with user that has no rights.
     */
    public void testCreateOnlyImportNoRights() {
        deleteElements();
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY,
                    getNotAdminImportFile());
            fail("No rights to import user's roles.");
        }
        catch (ImportException e) {
            //Ok
        }
    }
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.dictionary;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.AbstractImportTest;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;

/**
 * Test the environment import.
 * <p>
 * Also test the public's environment importation
 * </p>
 * 
 * @author mkargbo
 */
public class TestEnvironmentImport extends AbstractImportTest<EnvironmentData> {

    /** USER_ROLE_INDEX */
    private static final int USER_ROLE_INDEX = 2;

    /** USER_PASSWORD_INDEX */
    private static final int USER_PASSWORD_INDEX = 1;

    /** USER_LOGIN_INDEX */
    private static final int USER_LOGIN_INDEX = 0;

    private static final String INSTANCE_FILE =
            "importation/dictionary/environment/environmentToInstantiate.xml";

    private static final String FILE_TO_IMPORT =
            "importation/dictionary/environment/environmentToImport.xml";

    private static final String FILE_TO_UPDATE =
            "importation/dictionary/environment/environmentToUpdate.xml";

    private static final String FILE_TO_UPDATE_NOT_ADMIN =
            "importation/dictionary/environment/environmentToUpdateNotAdmin.xml";

    private static final String FILE_TO_IMPORT_NOT_ADMIN =
            "importation/dictionary/environment/environmentToImportNotAdmin.xml";

    private static final String FILE_TO_IMPORT_NOT_PUBLIC =
            "importation/dictionary/environment/environmentToUpdateNotPublic.xml";

    private static final String[] USER_CREATE =
            { "userEnvImport_01", "pwd", "envCreateRoleImportTest" };

    private static final String[] USER_UPDATE =
            { "userEnvImport_02", "pwd", "envModifyRoleImportTest" };

    private static final String[] ENV_1 = { "envTest_01", "true" };

    private static final String[] ENV_2 = { "envTest_02", "false" };

    private static final Map<String, String[]> EXPECTED_ADMIN_ENV_VALUES;

    private static final Map<String, String[]> EXPECTED_NOT_ADMIN_ENV_VALUES;

    private static final Set<String[]> IMPORTED_ELEMENTS;

    private static final Set<String[]> IMPORTED_NOT_ADMIN_ELEMENTS;

    private static final Set<String[]> UPDATED_ELEMENTS;

    private static final Set<String[]> UPDATED_NOT_ADMIN_ELEMENTS;

    private static final Map<String, String[]> EXPECTED_ADMIN_ENV_VALUES_UPDATED;

    private static final Map<String, String[]> EXPECTED_NOT_ADMIN_ENV_VALUES_UPDATED;

    static {
        EXPECTED_ADMIN_ENV_VALUES = new HashMap<String, String[]>();
        EXPECTED_ADMIN_ENV_VALUES.put("envTest_01",
                new String[] { "envCatTest_01_value_01",
                              "envCatTest_01_value_02",
                              "envCatTest_02_value_01",
                              "envCatTest_02_value_02" });
        EXPECTED_ADMIN_ENV_VALUES.put("envTest_02",
                new String[] { "envCatTest_03_value_01",
                              "envCatTest_03_value_02" });

        EXPECTED_NOT_ADMIN_ENV_VALUES = new HashMap<String, String[]>();
        EXPECTED_NOT_ADMIN_ENV_VALUES.put("envTest_01",
                new String[] { "envCatTest_01_value_01",
                              "envCatTest_01_value_02",
                              "envCatTest_02_value_01",
                              "envCatTest_02_value_02" });

        IMPORTED_ELEMENTS = new HashSet<String[]>();
        IMPORTED_ELEMENTS.add(ENV_1);
        IMPORTED_ELEMENTS.add(ENV_2);

        IMPORTED_NOT_ADMIN_ELEMENTS = new HashSet<String[]>();
        IMPORTED_NOT_ADMIN_ELEMENTS.add(ENV_1);

        UPDATED_ELEMENTS = new HashSet<String[]>();
        UPDATED_ELEMENTS.add(new String[] { "envTest_01", "false" });
        UPDATED_ELEMENTS.add(new String[] { "envTest_02", "true" });

        UPDATED_NOT_ADMIN_ELEMENTS = new HashSet<String[]>();
        UPDATED_NOT_ADMIN_ELEMENTS.add(new String[] { "envTest_02", "true" });

        EXPECTED_ADMIN_ENV_VALUES_UPDATED = new HashMap<String, String[]>();
        EXPECTED_ADMIN_ENV_VALUES_UPDATED.put("envTest_01",
                new String[] { "envCatTest_01_value_01",
                              "envCatTest_01_value_02",
                              "envCatTest_02_value_01" });
        EXPECTED_ADMIN_ENV_VALUES_UPDATED.put("envTest_02",
                new String[] { "envCatTest_01_value_01",
                              "envCatTest_03_value_01",
                              "envCatTest_03_value_02" });

        EXPECTED_NOT_ADMIN_ENV_VALUES_UPDATED = new HashMap<String, String[]>();
        EXPECTED_NOT_ADMIN_ENV_VALUES_UPDATED.put("envTest_02",
                new String[] { "envCatTest_01_value_01",
                              "envCatTest_03_value_01",
                              "envCatTest_03_value_02" });

    }

    private EnvironmentServiceImpl environmentServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setUp()
     */
    protected void setUp() {
        super.setUp();
        instantiate(getProcessName(), INSTANCE_FILE);
        environmentServiceImpl =
                (EnvironmentServiceImpl) ContextLocator.getContext().getBean(
                        "environmentServiceImpl");
    }

    private void checkEnv(String[] pRef, final String[] pExpectedValues) {
        String lName = pRef[0];
        boolean lIsPublic = Boolean.valueOf(pRef[1]);
        EnvironmentData lEnvironment =
                environmentServiceImpl.getEnvironmentByName(adminRoleToken,
                        getProcessName(), lName);
        assertNotNull(lEnvironment);
        assertEquals(lIsPublic, lEnvironment.isIsPublic());
        checkValues(pExpectedValues, lEnvironment.getCategoryValueDatas());
    }

    private void checkValues(final String[] pExpectedValues,
            final CategoryValueData[] pValues) {
        String[] lValues = new String[pValues.length];
        for (int i = 0; i < pValues.length; i++) {
            lValues[i] = pValues[i].getValue();
        }
        assertEqualsUnordered(pExpectedValues, lValues);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        for (String[] lRef : getImportedElement()) {
            checkEnv(lRef, EXPECTED_ADMIN_ENV_VALUES.get(lRef[0]));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        for (String[] lRef : IMPORTED_NOT_ADMIN_ELEMENTS) {
            checkEnv(lRef, EXPECTED_NOT_ADMIN_ENV_VALUES.get(lRef[0]));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#eraseAssertion(java.lang.Object[])
     */
    @Override
    protected void eraseAssertion(Object... pArgs) {
        String lExpectedId = (String) pArgs[0];
        String lId = (String) pArgs[1];
        assertTrue(StringUtils.isNotBlank(lId));
        assertNotSame(lExpectedId, lId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Map<String[], String> erasePreCondition() {
        Map<String[], String> lIdentifier = new HashMap<String[], String>();
        for (String[] lRef : getImportedElement()) {
            String lId = environmentServiceImpl.getEnvironmentId(adminRoleToken,
                            lRef[0]);
            lIdentifier.put(lRef, lId);
        }
        return lIdentifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected EnvironmentData getBusinessObject(String pRoleToken,
            String pElementId) {
        return environmentServiceImpl.getEnvironmentById(pRoleToken, pElementId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String[])
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lName = pElementRef[0];
        return lName;
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
        return IMPORTED_ELEMENTS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElementId) {
        environmentServiceImpl.deleteEnvironment(adminRoleToken,
                getProcessName(), pElementId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setEnvironmentsFlag(pFlag);
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
        for (String[] lRef : UPDATED_ELEMENTS) {
            checkEnv(lRef, EXPECTED_ADMIN_ENV_VALUES_UPDATED.get(lRef[0]));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        for (String[] lRef : UPDATED_NOT_ADMIN_ELEMENTS) {
            checkEnv(lRef, EXPECTED_NOT_ADMIN_ENV_VALUES_UPDATED.get(lRef[0]));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testSkipImport()
     */
    @Override
    public void testSkipImport() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.SKIP, getAdminImportFile());
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }

        for (String[] lRef : getImportedElement()) {
            String lId =
                    environmentServiceImpl.getCategoryId(getElementId(lRef));
            assertTrue(StringUtils.isBlank(lId));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use user with 'environment.modify' admin access
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testCreateOnlyImportNotAdmin()
     */
    @Override
    public void testCreateOnlyImportNotAdmin() {
        deleteElements();
        String lUserToken =
                authorizationService.login(USER_CREATE[USER_LOGIN_INDEX],
                        USER_CREATE[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER_CREATE[USER_ROLE_INDEX], StringUtils.EMPTY,
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
     * {@inheritDoc}
     * <p>
     * Use user with 'environment.modify' admin access
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testUpdateOnlyImportNotAdmin()
     */
    @Override
    public void testUpdateOnlyImportNotAdmin() {
        //Retrieve elements identifier.
        final Set<String> lElementsId = new HashSet<String>();
        for (String[] lRef : IMPORTED_NOT_ADMIN_ELEMENTS) {
            lElementsId.add(getElementId(lRef));
        }
        String lUserToken =
                authorizationService.login(USER_UPDATE[USER_LOGIN_INDEX],
                        USER_UPDATE[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER_UPDATE[USER_ROLE_INDEX], StringUtils.EMPTY,
                        getProcessName());
        try {
            doImport(lRoleToken, ImportFlag.UPDATE_ONLY,
                    getNotAdminImportFileForUpdating());
            updateOnlyNotAdminAssertion(lElementsId);
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test import environment with user that has no rights (not global admin or
     * admin access 'environmnent.create').
     */
    public void testCreateOnlyNoRights() {
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY, getImportFile());
            fail("Should have no rights to import environment.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Test import environment with user that has no rights (not global admin or
     * admin access 'environmnent.modify').
     */
    public void testUpdateOnlyNoRights() {
        try {
            doImport(normalRoleToken, ImportFlag.UPDATE_ONLY, getImportFile());
            fail("Should have no rights to import environment.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Test importation of a no punlic environment by a user that have admin
     * access 'environment.create'.
     */
    public void testCreateOnlyNoRightsPublic() {
        String lUserToken =
                authorizationService.login(USER_CREATE[USER_LOGIN_INDEX],
                        USER_CREATE[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER_CREATE[USER_ROLE_INDEX], StringUtils.EMPTY,
                        getProcessName());
        try {
            doImport(lRoleToken, ImportFlag.CREATE_ONLY,
                    FILE_TO_IMPORT_NOT_PUBLIC);
            fail("Should have no rights to import environment.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    @Override
    protected String getNotAdminImportFileForUpdating() {
        return FILE_TO_UPDATE_NOT_ADMIN;
    }

    @Override
    protected String getNotAdminImportFile() {
        return FILE_TO_IMPORT_NOT_ADMIN;
    }
}

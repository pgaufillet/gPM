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
import org.topcased.gpm.business.dictionary.CategoryAccessData;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.AbstractImportTest;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;

/**
 * TestCategoryImport: Test category importation.
 * <p>
 * Also test PROCESS updating.
 * </p>
 * 
 * @author mkargbo
 */
public class TestCategoryImport extends AbstractImportTest<CategoryData> {

    /** USER_ROLE_INDEX */
    private static final int USER_ROLE_INDEX = 2;

    /** USER_PASSWORD_INDEX */
    private static final int USER_PASSWORD_INDEX = 1;

    /** USER_LOGIN_INDEX */
    private static final int USER_LOGIN_INDEX = 0;

    private static final String INSTANCE_FILE =
            "importation/dictionary/categoryToInstantiate.xml";

    private static final String FILE_TO_IMPORT =
            "importation/dictionary/categoryToImport.xml";

    private static final String FILE_TO_UPDATE =
            "importation/dictionary/categoryToUpdate.xml";

    private static final String FILE_TO_UPDATE_NOT_ADMIN =
            "importation/dictionary/categoryToUpdateNotAdmin.xml";

    private static final String FILE_TO_IMPORT_NOT_ADMIN =
            "importation/dictionary/categoryToImportNotAdmin.xml";

    private static final String FILE_TO_IMPORT_ACCESS =
            "importation/dictionary/categoryToImportAccess.xml";

    private static final String[] USER =
            { "userCategoryImport_01", "pwd", "categoryRoleImportTest" };

    private static final String[] CAT_1 = { "catTest_01", "PROCESS" };

    private static final String[] CAT_2 = { "catTest_02", "PRODUCT" };

    private static final String[] CAT_3 = { "catTest_03", "USER" };

    private static final Map<String, String[]> EXPECTED_ADMIN_CATEGORY_VALUES;

    private static final Map<String, String[]> EXPECTED_NOT_ADMIN_CATEGORY_VALUES;

    private static final Set<String[]> IMPORTED_ELEMENTS;

    private static final Set<String[]> IMPORTED_NOT_ADMIN_ELEMENTS;

    private static final Set<String[]> UPDATED_ELEMENTS;

    private static final Set<String[]> UPDATED_NOT_ADMIN_ELEMENTS;

    private static final Map<String, String[]> EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED;

    private static final Map<String, String[]> EXPECTED_NOT_ADMIN_CATEGORY_VALUES_UPDATED;

    static {
        EXPECTED_ADMIN_CATEGORY_VALUES = new HashMap<String, String[]>();
        EXPECTED_ADMIN_CATEGORY_VALUES.put("catTest_01",
                new String[] { "catTest_01_value_01", "catTest_01_value_02",
                              "catTest_01_value_03" });
        EXPECTED_ADMIN_CATEGORY_VALUES.put("catTest_02",
                new String[] { "catTest_02_value_01", "catTest_02_value_02",
                              "catTest_02_value_03" });
        EXPECTED_ADMIN_CATEGORY_VALUES.put("catTest_03",
                new String[] { "catTest_03_value_01", "catTest_03_value_02",
                              "catTest_03_value_03" });

        EXPECTED_NOT_ADMIN_CATEGORY_VALUES = new HashMap<String, String[]>();
        EXPECTED_NOT_ADMIN_CATEGORY_VALUES.put("catTest_02",
                new String[] { "catTest_02_value_01", "catTest_02_value_02",
                              "catTest_02_value_03" });
        EXPECTED_NOT_ADMIN_CATEGORY_VALUES.put("catTest_03",
                new String[] { "catTest_03_value_01", "catTest_03_value_02",
                              "catTest_03_value_03" });

        IMPORTED_ELEMENTS = new HashSet<String[]>();
        IMPORTED_ELEMENTS.add(CAT_1);
        IMPORTED_ELEMENTS.add(CAT_2);
        IMPORTED_ELEMENTS.add(CAT_3);

        IMPORTED_NOT_ADMIN_ELEMENTS = new HashSet<String[]>();
        IMPORTED_NOT_ADMIN_ELEMENTS.add(CAT_2);
        IMPORTED_NOT_ADMIN_ELEMENTS.add(CAT_3);

        UPDATED_ELEMENTS = new HashSet<String[]>();
        UPDATED_ELEMENTS.add(new String[] { "catTest_01", "USER" });
        UPDATED_ELEMENTS.add(new String[] { "catTest_02", "PROCESS" });
        UPDATED_ELEMENTS.add(new String[] { "catTest_03", "PRODUCT" });

        EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED =
                new HashMap<String, String[]>();
        EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED.put("catTest_01",
                new String[] { "catTest_01_value_01", "catTest_01_value_02",
                              "catTest_01_value_03",
                              "catTest_01_value_02_UPDATED" });
        EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED.put("catTest_02",
                new String[] { "catTest_02_value_01", "catTest_02_value_02",
                              "catTest_02_value_03",
                              "catTest_02_value_03_UPDATED" });
        EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED.put("catTest_03",
                new String[] { "catTest_03_value_01", "catTest_03_value_02",
                              "catTest_03_value_03",
                              "catTest_03_value_01_UPDATED" });

        EXPECTED_NOT_ADMIN_CATEGORY_VALUES_UPDATED =
                new HashMap<String, String[]>();
        EXPECTED_NOT_ADMIN_CATEGORY_VALUES_UPDATED.put("catTest_01",
                new String[] { "catTest_01_value_01", "catTest_01_value_02",
                              "catTest_01_value_03",
                              "catTest_01_value_02_UPDATED" });
        EXPECTED_NOT_ADMIN_CATEGORY_VALUES_UPDATED.put("catTest_03",
                new String[] { "catTest_03_value_01", "catTest_03_value_02",
                              "catTest_03_value_01_UPDATED",
                              "catTest_03_value_03" });

        UPDATED_NOT_ADMIN_ELEMENTS = new HashSet<String[]>();
        UPDATED_NOT_ADMIN_ELEMENTS.add(new String[] { "catTest_01", "USER" });
        UPDATED_NOT_ADMIN_ELEMENTS.add(new String[] { "catTest_03", "PRODUCT" });
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

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        for (String[] lCat : getImportedElement()) {
            checkCategory(lCat, EXPECTED_ADMIN_CATEGORY_VALUES.get(lCat[0]));
        }
    }

    /**
     * Check the category.
     * 
     * @param pCatRef
     *            Expected category reference.
     * @param pExpectedValues
     *            Expected values of the category.
     */
    private void checkCategory(String[] pCatRef, final String[] pExpectedValues) {
        String lName = pCatRef[0];
        CategoryAccessData lAccess = CategoryAccessData.fromString(pCatRef[1]);
        CategoryData lCategory =
                environmentServiceImpl.getCategory(adminRoleToken, lName);
        assertNotNull(lCategory);
        assertEquals(lAccess, lCategory.getAccessLevel());
        checkValues(pExpectedValues, lCategory.getCategoryValueDatas());
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
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        for (String[] lCat : IMPORTED_NOT_ADMIN_ELEMENTS) {
            checkCategory(lCat, EXPECTED_NOT_ADMIN_CATEGORY_VALUES.get(lCat[0]));
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
    protected Map<String, String> erasePreCondition() {
        Map<String, String> lIdentifier = new HashMap<String, String>();
        for (String[] lCat : getImportedElement()) {
            String lId = environmentServiceImpl.getCategoryId(lCat[0]);
            lIdentifier.put(lCat[0], lId);
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
    protected CategoryData getBusinessObject(String pRoleToken,
            String pElementId) {
        CategoryData lCategory =
                environmentServiceImpl.getCategoryById(adminRoleToken,
                        pElementId);
        return lCategory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String[])
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lCatName = pElementRef[0];
        return lCatName;
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
        environmentServiceImpl.deleteCategory(adminRoleToken, getProcessName(),
                pElementId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setCategoriesFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        final Map<String, String> lElements = erasePreCondition();
        try {
            doImport(adminRoleToken, ImportFlag.ERASE, getImportFile());

            //Test elements existence
            for (String[] lRef : getImportedElement()) {
                String lId = getElementId(lRef);
                String lExpectedIdentifier = lElements.get(lRef[0]);
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
        for (String[] lCat : UPDATED_ELEMENTS) {
            checkCategory(lCat,
                    EXPECTED_ADMIN_CATEGORY_VALUES_UPDATED.get(lCat[0]));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        for (String[] lCat : UPDATED_NOT_ADMIN_ELEMENTS) {
            checkCategory(lCat,
                    EXPECTED_NOT_ADMIN_CATEGORY_VALUES_UPDATED.get(lCat[0]));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use user with 'dictionnary.modify' admin access
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testCreateOnlyImportNotAdmin()
     */
    @Override
    public void testCreateOnlyImportNotAdmin() {
        deleteElements();
        String lUserToken =
                authorizationService.login(USER[USER_LOGIN_INDEX],
                        USER[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER[USER_ROLE_INDEX], StringUtils.EMPTY,
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
     * Use user with 'dictionnary.modify' admin access
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testUpdateOnlyImportNotAdmin()
     */
    @Override
    public void testUpdateOnlyImportNotAdmin() {
        //Retrieve elements identifier.
        final Set<String> lElementsId = new HashSet<String>();
        for (String[] lRef : getImportedElement()) {
            lElementsId.add(getElementId(lRef));
        }
        String lUserToken =
                authorizationService.login(USER[USER_LOGIN_INDEX],
                        USER[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER[USER_ROLE_INDEX], StringUtils.EMPTY,
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

    @Override
    protected String getNotAdminImportFile() {
        return FILE_TO_IMPORT_NOT_ADMIN;
    }

    @Override
    protected String getNotAdminImportFileForUpdating() {
        return FILE_TO_UPDATE_NOT_ADMIN;
    }

    /**
     * Test import 'PROCESS' category with user that has no rights (not global
     * admin).
     */
    public void testCreateOnlyNoRights() {
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY,
                    FILE_TO_IMPORT_ACCESS);
            fail("Should have no rights to import category.");
        }
        catch (ImportException e) {
            //Ok
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
}

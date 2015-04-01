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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.SearchServiceImpl;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * TestFilterImport test the filter import manager.<br />
 * Additionally test the import of product defined filter and user defined
 * filter.
 * 
 * @author mkargbo
 */
public class TestFilterImport extends AbstractImportTest<ExecutableFilterData> {
    /** USER_PRODUCT_INDEX */
    private static final int USER_PRODUCT_INDEX = 3;

    /** USER_ROLE_INDEX */
    private static final int USER_ROLE_INDEX = 2;

    /** USER_PASSWORD_INDEX */
    private static final int USER_PASSWORD_INDEX = 1;

    /** USER_LOGIN_INDEX */
    private static final int USER_LOGIN_INDEX = 0;

    /** FILTER_NUMBER */
    private static final int FILTER_NUMBER = 3;

    /** CRITERIA_OPERATOR */
    private static final String CRITERIA_OPERATOR = "or";

    /** FILTER_NAME_INDEX */
    private static final int FILTER_NAME_INDEX = 0;

    /** FILTER_PROCESSNAME_INDEX */
    private static final int FILTER_PROCESSNAME_INDEX = 1;

    /** FILTER_PRODUCTNAME_INDEX */
    private static final int FILTER_PRODUCTNAME_INDEX = 2;

    /** FILTER_USERLOGIN_INDEX */
    private static final int FILTER_USERLOGIN_INDEX = 3;

    /** FILTER_FIELDS_CONTAINER_NAME */
    private static final String FILTER_FIELDS_CONTAINER_NAME =
            GpmTestValues.SHEET_TYPE_CAT;

    private static final String INSTANCE_FILE =
            "importation/filter/filtersToInstantiate.xml";

    private static final String FILE_TO_IMPORT_ADMIN =
            "importation/filter/filtersToImportAdmin.xml";

    private static final String FILE_TO_IMPORT_ADMIN_UPDATE =
            "importation/filter/filtersToImportAdminUpdate.xml";

    private static final String FILE_TO_IMPORT_PRODUCT =
            "importation/filter/filtersToImportProduct.xml";

    private static final String FILE_TO_IMPORT_USER =
            "importation/filter/filtersToImportUser.xml";

    private static final String FILE_TO_IMPORT_USER_UPDATE =
            "importation/filter/filtersToImportUserUpdate.xml";

    private static final String FILE_TO_IMPORT_NOT_PRODUCT =
            "importation/filter/filtersToImportNotProduct.xml";

    private static final String FILE_TO_IMPORT_NOT_USER =
            "importation/filter/filtersToImportNotUser.xml";

    /** Label key, process name, product's name, user's login */
    private static final String[] INSTANCE_FILTER_NAME =
            { "filterImport_01", GpmTestValues.PROCESS_NAME, null, null };

    /** Label key, process name, product's name, user's login */
    private static final String[] PRODUCT_FILTER_NAME =
            { "filterImport_01", GpmTestValues.PROCESS_NAME,
             "productFilterTestImport_01", null };

    /** Label key, process name, product's name, user's login */
    private static final String[] USER_FILTER_NAME =
            { "filterImport_01", GpmTestValues.PROCESS_NAME, null,
             GpmTestValues.USER_USER2 };

    private static final String[] EXPECTED_CRITERIA_NAME =
            { "$SHEET_REFERENCE", "$SHEET_STATE" };

    private static final Map<String, Object[]> EXPECTED_CRITERIA;

    private static final String[] EXPECTED_RESULTS_FIELDS =
            { "$PRODUCT_NAME", "Cat-Cat|$SHEET_REFERENCE" };

    private static final Map<String, Object[]> EXPECTED_CRITERIA_UPDATE;

    private static final String[] EXPECTED_RESULTS_FIELDS_UPDATE =
            { "Cat-Cat|$SHEET_REFERENCE" };

    /** Login, Password, Role, Product */
    private static final String[] USER =
            { "userImportFilter", "pwd", "roleImportFilter",
             "productFilterTestImport_01" };

    static {
        EXPECTED_CRITERIA = new HashMap<String, Object[]>(2);
        EXPECTED_CRITERIA.put(EXPECTED_CRITERIA_NAME[0],
                new Object[] { VirtualFieldData.SHEET_REFERENCE_VIRTUAL_FIELD,
                              "=", "test_value" });
        EXPECTED_CRITERIA.put(EXPECTED_CRITERIA_NAME[1],
                new Object[] { VirtualFieldData.SHEET_STATE_VIRTUAL_FIELD, "=",
                              "open" });

        EXPECTED_CRITERIA_UPDATE = new HashMap<String, Object[]>(2);
        EXPECTED_CRITERIA_UPDATE.put(EXPECTED_CRITERIA_NAME[0],
                new Object[] { VirtualFieldData.SHEET_REFERENCE_VIRTUAL_FIELD,
                              "<>", "test_value_updated" });
        EXPECTED_CRITERIA_UPDATE.put(EXPECTED_CRITERIA_NAME[1],
                new Object[] { VirtualFieldData.SHEET_STATE_VIRTUAL_FIELD,
                              "like", "open_updated" });
    }

    private SearchServiceImpl searchService;

    private FieldsContainerService fieldsContainerService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        instantiate(getProcessName(), INSTANCE_FILE);
        searchService =
                (SearchServiceImpl) ContextLocator.getContext().getBean(
                        "searchServiceImpl");
        fieldsContainerService = serviceLocator.getFieldsContainerService();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        //Get instance filter
        ExecutableFilterData lInstanceFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        INSTANCE_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The instance filter is not imported.", lInstanceFilter);

        checkCriteria(lInstanceFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA);
        checkResultSummary(lInstanceFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS);

        //Get product filter
        ExecutableFilterData lProductFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        PRODUCT_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The product filter is not imported.", lProductFilter);

        checkCriteria(lProductFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA);
        checkResultSummary(lProductFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS);

        //Get user filter
        ExecutableFilterData lUserFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        USER_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        USER_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        USER_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        USER_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The user filter is not imported.", lUserFilter);

        checkCriteria(lUserFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA);
        checkResultSummary(lUserFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS);
    }

    private void checkCriteria(final CriteriaData pCriteria,
            final String[] pExpectedCriteriaName,
            final Map<String, Object[]> pExpectedCriteria) {
        OperationData lOperation = (OperationData) pCriteria;
        assertEquals(CRITERIA_OPERATOR, lOperation.getOperator());

        assertEquals(pExpectedCriteriaName.length,
                lOperation.getCriteriaDatas().length);

        for (int i = 0; i < pExpectedCriteriaName.length; i++) {
            CriteriaFieldData lCriterion =
                    (CriteriaFieldData) lOperation.getCriteriaDatas()[i];
            Object[] lExpectedCriterion =
                    pExpectedCriteria.get(pExpectedCriteriaName[i]);
            assertEquals(lExpectedCriterion[1], lCriterion.getOperator());
            assertEquals(lExpectedCriterion[2],
                    lCriterion.getScalarValueData().getValue());
            assertUsableField((UsableFieldData) lExpectedCriterion[0],
                    lCriterion.getUsableFieldData());
        }
    }

    private void checkResultSummary(final ResultSummaryData pResultSummary,
            final String[] pExpectedResultsField) {
        assertEquals(pExpectedResultsField.length,
                pResultSummary.getUsableFieldDatas().length);

        String lFieldsContainerId =
                fieldsContainerService.getFieldsContainerId(adminRoleToken,
                        FILTER_FIELDS_CONTAINER_NAME);
        for (int i = 0; i < pExpectedResultsField.length; i++) {
            UsableFieldData lUsableField =
                    pResultSummary.getUsableFieldDatas()[i];
            String lUsableFieldDataId =
                    searchService.getUsableFieldDataId(adminRoleToken,
                            getProcessName(), pExpectedResultsField[i]);
            UsableFieldData lExpected =
                    searchService.getUsableField(getProcessName(),
                            lUsableFieldDataId,
                            Collections.singleton(lFieldsContainerId));
            assertUsableField(lExpected, lUsableField);
        }
    }

    private void assertUsableField(final UsableFieldData pExpectedUsableField,
            final UsableFieldData pUsableField) {
        assertEquals(pExpectedUsableField.getFieldName(),
                pUsableField.getFieldName());
        assertEquals(pExpectedUsableField.getLevel(), pUsableField.getLevel());
        assertEquals(pExpectedUsableField.getFieldType(),
                pUsableField.getFieldType());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        //Get user filter
        ExecutableFilterData lFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        USER_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        USER_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        USER_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        USER_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The user filter is not imported.", lFilter);

        checkCriteria(lFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA);
        checkResultSummary(lFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS);
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
    protected ExecutableFilterData getBusinessObject(String pRoleToken,
            String pElementId) {
        ExecutableFilterData lFilter =
                searchService.getExecutableFilter(pRoleToken, pElementId);
        return lFilter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String[])
     */
    @Override
    protected String getElementId(String... pElementRef) {
        //Label key, Process, Product, User
        String lId =
                searchService.getId(pElementRef[FILTER_PROCESSNAME_INDEX],
                        pElementRef[FILTER_PRODUCTNAME_INDEX],
                        pElementRef[FILTER_USERLOGIN_INDEX],
                        pElementRef[FILTER_NAME_INDEX]);
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFile()
     */
    @Override
    protected String getImportFile() {
        return FILE_TO_IMPORT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFileForUpdating()
     */
    @Override
    protected String getImportFileForUpdating() {
        return FILE_TO_IMPORT_ADMIN_UPDATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        Set<String[]> lElements = new HashSet<String[]>(FILTER_NUMBER);
        lElements.add(INSTANCE_FILTER_NAME);
        lElements.add(PRODUCT_FILTER_NAME);
        lElements.add(USER_FILTER_NAME);
        return lElements;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElement) {
        searchService.removeExecutableFilter(adminRoleToken, pElement);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setFiltersFlag(pFlag);

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
        //Get instance filter
        ExecutableFilterData lInstanceFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        INSTANCE_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        INSTANCE_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The instance filter is not imported.", lInstanceFilter);

        checkCriteria(lInstanceFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA_UPDATE);
        checkResultSummary(lInstanceFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS_UPDATE);

        //Get product filter
        ExecutableFilterData lProductFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        PRODUCT_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        PRODUCT_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The product filter is not imported.", lProductFilter);

        checkCriteria(lProductFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA_UPDATE);
        checkResultSummary(lProductFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS_UPDATE);

        //Get user filter
        ExecutableFilterData lUserFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        USER_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        USER_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        USER_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        USER_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The user filter is not imported.", lUserFilter);

        checkCriteria(lUserFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA_UPDATE);
        checkResultSummary(lUserFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS_UPDATE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        //Get user filter
        ExecutableFilterData lFilter =
                searchService.getExecutableFilterByName(adminRoleToken,
                        USER_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                        USER_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                        USER_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                        USER_FILTER_NAME[FILTER_NAME_INDEX]);
        assertNotNull("The user filter is not imported.", lFilter);

        checkCriteria(lFilter.getFilterData().getCriteriaData(),
                EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA_UPDATE);
        checkResultSummary(lFilter.getResultSummaryData(),
                EXPECTED_RESULTS_FIELDS_UPDATE);
    }

    /**
     * Test importation of product defined filters.
     */
    public void testCreateProductFilter() {
        deleteElements();
        String lUserToken =
                authorizationService.login(USER[USER_LOGIN_INDEX],
                        USER[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER[USER_ROLE_INDEX], USER[USER_PRODUCT_INDEX],
                        getProcessName());
        try {
            doImport(lRoleToken, ImportFlag.CREATE_ONLY, FILE_TO_IMPORT_PRODUCT);

            //Get product filter
            ExecutableFilterData lFilter =
                    searchService.getExecutableFilterByName(lRoleToken,
                            PRODUCT_FILTER_NAME[FILTER_PROCESSNAME_INDEX],
                            PRODUCT_FILTER_NAME[FILTER_PRODUCTNAME_INDEX],
                            PRODUCT_FILTER_NAME[FILTER_USERLOGIN_INDEX],
                            PRODUCT_FILTER_NAME[FILTER_NAME_INDEX]);
            assertNotNull("The product filter is not imported.", lFilter);

            checkCriteria(lFilter.getFilterData().getCriteriaData(),
                    EXPECTED_CRITERIA_NAME, EXPECTED_CRITERIA);
            checkResultSummary(lFilter.getResultSummaryData(),
                    EXPECTED_RESULTS_FIELDS);
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Test importation of filters with user that only has rights on a product.
     */
    public void testCreateNotProductFilter() {
        deleteElements();
        String lUserToken =
                authorizationService.login(USER[USER_LOGIN_INDEX],
                        USER[USER_PASSWORD_INDEX]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        USER[USER_ROLE_INDEX], USER[USER_PRODUCT_INDEX],
                        getProcessName());
        try {
            doImport(lRoleToken, ImportFlag.CREATE_ONLY,
                    FILE_TO_IMPORT_NOT_PRODUCT);
            fail("No rights to import filters. Can only import product filter.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    /**
     * Test importation of filters with user that has not rights on product
     * defined filter and on instance.
     */
    public void testCreateNotUserFilter() {
        deleteElements();
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY,
                    FILE_TO_IMPORT_NOT_USER);
            fail("No rights to import filters. Can only import user filter.");
        }
        catch (ImportException e) {
            //Ok
        }
    }

    @Override
    protected String getAdminImportFile() {
        return FILE_TO_IMPORT_ADMIN;
    }

    @Override
    protected String getAdminImportFileForUpdating() {
        return FILE_TO_IMPORT_ADMIN_UPDATE;
    }

    @Override
    protected String getNotAdminImportFile() {
        return FILE_TO_IMPORT_USER;
    }

    @Override
    protected String getNotAdminImportFileForUpdating() {
        return FILE_TO_IMPORT_USER_UPDATE;
    }
}

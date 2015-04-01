/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.impl.FieldsContainerServiceImpl;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test access control over sub-field
 * 
 * @author mkargbo
 */
public class TestGetSubFields extends AbstractBusinessServiceTestCase {

    /** MANDATORY_FIELD */
    private static final String MANDATORY_FIELD = "SHEETTYPE1_simpleBooleanM";

    /** UPDATABLE_FIELD */
    private static final String UPDATABLE_FIELD = "SHEETTYPE1_simpleRealM";

    /** EXPORTABLE_FIELD */
    private static final String EXPORTABLE_FIELD = "SHEETTYPE1_simpleIntegerM";

    /** CONFIDENTIAL_FIELD */
    private static final String CONFIDENTIAL_FIELD = "SHEETTYPE1_simpleStringM";

    /** MULTIPLE_FIELD */
    private static final String MULTIPLE_FIELD = "SHEETTYPE1_multiple1";

    private static final String INSTANCE_FILE =
            "authorization/TestGetSubFields.xml";

    private static final String[] USER = { "user10", "pwd10" };

    private static final String ROLE_CONFIDENTIAL = "confidential";

    private static final String ROLE_NOT_EXPORTABLE = "notExportable";

    private static final String ROLE_NOT_UPDATABLE = "notUpdatable";

    private static final String ROLE_MANDATORY = "mandatory";

    private static final String ROLE_NOT_MULTIPLE = "notMultiple";

    private static final String PRODUCT_NAME = GpmTestValues.PRODUCT1_NAME;

    private static final String SHEET_REFERENCE = "accessControlMultiple_1:9";

    private FieldsContainerServiceImpl fieldsContainerService;

    private SheetService sheetService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        fieldsContainerService =
                (FieldsContainerServiceImpl) ContextLocator.getContext().getBean(
                        "fieldsContainerServiceImpl");
        sheetService = serviceLocator.getSheetService();
        instantiate(getProcessName(), INSTANCE_FILE);
    }

    /**
     * Test get not confidential fields.
     */
    public void testConfidential() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_CONFIDENTIAL,
                        PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_NOT_CONFIDENTIAL,
                        CacheProperties.IMMUTABLE);
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, CONFIDENTIAL_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                EXPORTABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Test get not exportable fields.
     */
    public void testExportable() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        ROLE_NOT_EXPORTABLE, PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_EXPORT,
                        CacheProperties.IMMUTABLE);
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Test get not updatable fields.
     */
    public void testUpdatable() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NOT_UPDATABLE,
                        PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_UPDATE,
                        CacheProperties.IMMUTABLE);
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                EXPORTABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Test get not mandatory fields.
     */
    public void testNotMandatory() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_MANDATORY,
                        PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_NOT_MANDATORY,
                        CacheProperties.IMMUTABLE);
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD,
                EXPORTABLE_FIELD));
        assertNotNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Test get mandatory fields.
     */
    public void testMandatory() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_MANDATORY,
                        PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_MANDATORY,
                        CacheProperties.IMMUTABLE);
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, CONFIDENTIAL_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Test get not confidential, exportable, updatable multiple field.
     */
    public void testMultiple() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NOT_MULTIPLE,
                        PRODUCT_NAME, getProcessName());
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        CacheableValuesContainer lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_NOT_CONFIDENTIAL,
                        CacheProperties.IMMUTABLE);
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, CONFIDENTIAL_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));

        lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_EXPORT,
                        CacheProperties.IMMUTABLE);
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, CONFIDENTIAL_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));

        lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_UPDATE,
                        CacheProperties.IMMUTABLE);
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, CONFIDENTIAL_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertNull(lValuesContainer.getValue(MULTIPLE_FIELD, MANDATORY_FIELD));
    }
}
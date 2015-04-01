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

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.impl.FieldsContainerServiceImpl;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test sub fields of multiple multivalued field.
 * 
 * @author mkargbo
 */
public class TestGetSubFieldsMultivalued extends
        AbstractBusinessServiceTestCase {
    /** MANDATORY_FIELD */
    private static final String MANDATORY_FIELD = "SHEETTYPE1_simpleBooleanMM";

    /** UPDATABLE_FIELD */
    private static final String UPDATABLE_FIELD = "SHEETTYPE1_simpleRealMM";

    /** EXPORTABLE_FIELD */
    private static final String EXPORTABLE_FIELD = "SHEETTYPE1_simpleIntegerMM";

    /** CONFIDENTIAL_FIELD */
    private static final String CONFIDENTIAL_FIELD =
            "SHEETTYPE1_simpleStringMM";

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

    private static final String MULTIPLE_FIELD = "SHEETTYPE1_multiple1M";

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
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
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
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
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
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
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
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertTrue(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
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
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                StringUtils.EMPTY));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
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
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                StringUtils.EMPTY));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));

        lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_EXPORT,
                        CacheProperties.IMMUTABLE);
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                StringUtils.EMPTY));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));

        lValuesContainer =
                fieldsContainerService.getValuesContainer(lRoleToken, lId,
                        FieldsContainerService.FIELD_UPDATE,
                        CacheProperties.IMMUTABLE);
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                StringUtils.EMPTY));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD,
                CONFIDENTIAL_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, EXPORTABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, UPDATABLE_FIELD));
        assertFalse(hasValue(lValuesContainer, MULTIPLE_FIELD, MANDATORY_FIELD));
    }

    /**
     * Determine the value of the field.
     * <p>
     * Field can be multiple only, or sub-field.
     * </p>
     * 
     * @param pContainer
     *            Container
     * @param pMultipleMultivaluedField
     *            (cannot be null) Label key of the multiple field
     * @param pSubField
     *            (can be null) Label key of the sub-field
     * @return True if the field has a value, false otherwise.
     */
    @SuppressWarnings("unchecked")
    private boolean hasValue(final CacheableValuesContainer pContainer,
            final String pMultipleMultivaluedField, final String pSubField) {
        boolean lHasValue = false;
        List<Map<String, Object>> lLines =
                (List<Map<String, Object>>) pContainer.getValue(pMultipleMultivaluedField);
        if (StringUtils.isNotBlank(pSubField)
                && CollectionUtils.isNotEmpty(lLines)) {
            for (Map<String, Object> lLine : lLines) {
                lHasValue = lHasValue || (lLine.get(pSubField) != null);
            }
        }
        else {
            lHasValue = false;
        }
        return lHasValue;
    }
}
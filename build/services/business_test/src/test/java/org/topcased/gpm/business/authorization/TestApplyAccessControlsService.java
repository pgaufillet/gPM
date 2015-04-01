/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.HashMap;
import java.util.LinkedList;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.proxy.CheckedGpmObject;
import org.topcased.gpm.util.proxy.CheckedObjectGenerator;

/**
 * TestApplyAccessControlsService
 * 
 * @author ahaugomm
 */
public class TestApplyAccessControlsService extends
        AbstractBusinessServiceTestCase {
    private static final String FIELDS_CONTAINER_WITH_ACCESS_CONTROL =
            GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2;

    private static final String[] FIELDS_WITH_ACCESS_CONTROL =
            new String[] { "TestPointerFields2_field1",
                          "TestPointerFields2_monovalued_choice" };

    private static final String ACCESS_CONTROL_SHEET_PRODUCT =
            GpmTestValues.PRODUCT1_NAME;

    private static final String ACCESS_CONTROL_SHEET_REF = "pointed:2";

    private static final String ACCESS_CONTROL_PRODUCT_NAME =
            GpmTestValues.PRODUCT_PRODUCT1_2;

    private static final String ACCESS_CONTROL_ATTRIBUTE_NAME = "hidden";

    private static final String ACCESS_CONTROL_ATTRIBUTE_VALUE = "true";

    /**
     * Apply access controls with extended attribute on a sheet type, and check
     * that method applyAccessControls applies correctly the type access
     * controls
     */
    public void testApplyContainerAndFieldAccessControls() {
        String lRoleToken =
                authorizationService.selectRole(adminUserToken,
                        getAdminRoleName(), ACCESS_CONTROL_PRODUCT_NAME,
                        getProcessName());
        CacheableFieldsContainer lCacheableFieldsContainer =
                sheetService.getCacheableSheetTypeByName(lRoleToken,
                        FIELDS_CONTAINER_WITH_ACCESS_CONTROL,
                        CacheProperties.MUTABLE);
        AccessControlContextData lContextData =
                new AccessControlContextData(ACCESS_CONTROL_PRODUCT_NAME,
                        getAdminRoleName(), null,
                        lCacheableFieldsContainer.getId(), null, null);
        authorizationService.applyAccessControls(lRoleToken, lContextData,
                lCacheableFieldsContainer);

        assertTrue(lCacheableFieldsContainer.getConfidential());
        assertFalse(lCacheableFieldsContainer.getCreatable());
        assertFalse(lCacheableFieldsContainer.getDeletable());
        assertFalse(lCacheableFieldsContainer.getUpdatable());
        assertTrue(ACCESS_CONTROL_ATTRIBUTE_VALUE.equals(lCacheableFieldsContainer.getAttributeValues(ACCESS_CONTROL_ATTRIBUTE_NAME)[0]));
    }

    /**
     * Test that all the values of the Fields Container are kept safe with the
     * proxy 'Checked'
     */
    public void testFieldsContainerProxyChecked() {
        final CacheableFieldsContainer lFieldsContainer =
                sheetService.getCacheableSheetTypeByName(getAdminRoleToken(),
                        getProcessName(), FIELDS_CONTAINER_WITH_ACCESS_CONTROL,
                        CacheProperties.MUTABLE);
        // All the access rights of the initial Fields Container are put in True
        lFieldsContainer.setConfidential(true);
        lFieldsContainer.setCreatable(true);
        lFieldsContainer.setDeletable(true);
        lFieldsContainer.setUpdatable(true);
        lFieldsContainer.setAttributesMap(new HashMap<String, String[]>());

        final CacheableFieldsContainer lCheckedFieldsContainer =
                CheckedObjectGenerator.create(lFieldsContainer);
        // All the access rights of the proxy Checked are put in False
        lCheckedFieldsContainer.setConfidential(false);
        lCheckedFieldsContainer.setCreatable(false);
        lCheckedFieldsContainer.setDeletable(false);
        lCheckedFieldsContainer.setUpdatable(false);

        // Check the value of the initial object's access rights are not modified through the proxy
        assertTrue("Proxy Checked : FieldsContainer.confidential",
                lFieldsContainer.getConfidential());
        assertTrue("Proxy Checked : FieldsContainer.creatable",
                lFieldsContainer.getCreatable());
        assertTrue("Proxy Checked : FieldsContainer.deletable",
                lFieldsContainer.getDeletable());
        assertTrue("Proxy Checked : FieldsContainer.updatable",
                lFieldsContainer.getUpdatable());
        assertFalse("Proxy Checked : FieldsContainer.fieldsIdMap",
                lFieldsContainer.getFieldsIdMap().isEmpty());
        assertFalse("Proxy Checked : FieldsContainer.fieldsKeyMap",
                lFieldsContainer.getFieldsKeyMap().isEmpty());
        assertFalse("Proxy Checked : FieldsContainer.topLevelFields",
                lFieldsContainer.getTopLevelFields().isEmpty());
        assertNotNull("Proxy Checked : FieldsContainer.attributesMap",
                lFieldsContainer.getAttributesMap().isEmpty());

        // Check the values of the proxy
        assertFalse("Proxy Checked : FieldsContainer.confidential",
                lCheckedFieldsContainer.getConfidential());
        assertFalse("Proxy Checked : FieldsContainer.creatable",
                lCheckedFieldsContainer.getCreatable());
        assertFalse("Proxy Checked : FieldsContainer.deletable",
                lCheckedFieldsContainer.getDeletable());
        assertFalse("Proxy Checked : FieldsContainer.updatable",
                lCheckedFieldsContainer.getUpdatable());
        // Fields lists are initially empty for the proxy
        assertTrue("Proxy Checked : FieldsContainer.fieldsIdMap",
                lCheckedFieldsContainer.getFieldsIdMap().isEmpty());
        assertTrue("Proxy Checked : FieldsContainer.fieldsKeyMap",
                lCheckedFieldsContainer.getFieldsKeyMap().isEmpty());
        assertTrue("Proxy Checked : FieldsContainer.topLevelFields",
                lCheckedFieldsContainer.getTopLevelFields().isEmpty());
        // Attribute lists are initially null for the proxy
        assertNull("Proxy Checked : FieldsContainer.attributesMap",
                lCheckedFieldsContainer.getAttributesMap());
    }

    /**
     * Test that all the values of the Fields are kept safe with the proxy
     * 'Checked'
     */
    public void testFieldsProxyChecked() {
        final CacheableFieldsContainer lFieldsContainer =
                sheetService.getCacheableSheetTypeByName(getAdminRoleToken(),
                        getProcessName(), FIELDS_CONTAINER_WITH_ACCESS_CONTROL,
                        CacheProperties.MUTABLE);

        // Get a field of the field container
        final Field lField = (Field) lFieldsContainer.getFields().toArray()[0];
        // All the access rights of the initial Fields Container are put in True
        lField.setConfidential(true);
        lField.setExportable(true);
        lField.setMandatory(true);
        lField.setUpdatable(true);
        lField.setAttributes(new LinkedList<Attribute>());

        final Field lCheckedField = CheckedObjectGenerator.create(lField);
        // All the access rights of the proxy Checked are put in False
        lCheckedField.setConfidential(false);
        lCheckedField.setExportable(false);
        lCheckedField.setMandatory(false);
        lCheckedField.setUpdatable(false);

        // Check the value of the initial object's access rights are not modified through the proxy
        assertTrue("Proxy Checked : Field.confidential",
                lField.getConfidential());
        assertTrue("Proxy Checked : Field.exportable", lField.getExportable());
        assertTrue("Proxy Checked : Field.mandatory", lField.getMandatory());
        assertTrue("Proxy Checked : Field.updatable", lField.getUpdatable());
        assertNotNull("Proxy Checked : Field.attributes",
                lField.getAttributes());

        // Check the values of the proxy
        assertFalse("Proxy Checked : Field.confidential",
                lCheckedField.getConfidential());
        assertFalse("Proxy Checked : Field.exportable",
                lCheckedField.getExportable());
        assertFalse("Proxy Checked : Field.mandatory",
                lCheckedField.getMandatory());
        assertFalse("Proxy Checked : Field.updatable",
                lCheckedField.getUpdatable());
        // Attribute lists are initially null for the proxy
        assertNull("Proxy Checked : Field.attributes",
                lCheckedField.getAttributes());
    }

    /**
     * Test that all the values of the Values Container are kept safe with the
     * proxy 'Checked'
     */
    public void testValuesContainerProxyChecked() {
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        ACCESS_CONTROL_SHEET_PRODUCT, ACCESS_CONTROL_SHEET_REF);
        final CacheableValuesContainer lValuesContainer =
                sheetService.getCacheableSheet(getAdminRoleToken(), lSheetId,
                        CacheProperties.MUTABLE);
        final CacheableValuesContainer lCheckedValuesContainer =
                CheckedObjectGenerator.create(lValuesContainer);

        // Check the value of the initial object's access rights are not modified through the proxy
        assertFalse("Proxy Checked : ValuesContainer.valuesMap",
                lValuesContainer.getValuesMap().isEmpty());
        // Values list are initially empty for the proxy
        assertTrue("Proxy Checked : ValuesContainer.valuesMap",
                lCheckedValuesContainer.getValuesMap().isEmpty());
    }

    /**
     * Apply access control on a Fields Container using the proxy 'Checked'
     */
    public void testFieldsContainerAccessControl() {
        final CacheableFieldsContainer lFieldsContainer =
                sheetService.getCacheableSheetTypeByName(normalRoleToken,
                        getProcessName(), FIELDS_CONTAINER_WITH_ACCESS_CONTROL,
                        CacheProperties.MUTABLE);
        final AccessControlContextData lContextData =
                new AccessControlContextData(ACCESS_CONTROL_PRODUCT_NAME,
                        getNormalUserRoleName(), null,
                        lFieldsContainer.getId(), null, null);
        final CacheProperties lCheckedImmutable =
                new CacheProperties(false, lContextData);
        final CacheableFieldsContainer lCheckedFieldsContainer =
                sheetService.getCacheableSheetType(normalRoleToken,
                        lFieldsContainer.getId(), lCheckedImmutable);

        assertTrue("Proxy not used for Fields Containers",
                lCheckedFieldsContainer instanceof CheckedGpmObject);
        assertTrue("FieldsContainer.confidential",
                lCheckedFieldsContainer.getConfidential());
        assertFalse("FieldsContainer.creatable",
                lCheckedFieldsContainer.getCreatable());
        assertFalse("FieldsContainer.deletable",
                lCheckedFieldsContainer.getDeletable());
        assertFalse("FieldsContainer.updatable",
                lCheckedFieldsContainer.getUpdatable());
        assertTrue(
                "FieldsContainer.attributeValues",
                ACCESS_CONTROL_ATTRIBUTE_VALUE.equals(lCheckedFieldsContainer.getAttributeValues(ACCESS_CONTROL_ATTRIBUTE_NAME)[0]));

        for (Field lField : lCheckedFieldsContainer.getTopLevelFields()) {
            assertTrue("Proxy not used for Fields",
                    lField instanceof CheckedGpmObject);
            // Test access control on some fields
            if (lField.getLabelKey().equalsIgnoreCase(
                    FIELDS_WITH_ACCESS_CONTROL[0])) {
                assertFalse("Field.confidential", lField.getConfidential());
                assertFalse("Field.exportable", lField.getExportable());
                assertFalse("Field.mandatory", lField.getMandatory());
                assertFalse("Field.updatable", lField.getUpdatable());
            }
            else if (lField.getLabelKey().equalsIgnoreCase(
                    FIELDS_WITH_ACCESS_CONTROL[1])) {
                assertTrue("Field.confidential", lField.getConfidential());
                assertTrue("Field.exportable", lField.getExportable());
                assertTrue("Field.mandatory", lField.getMandatory());
                assertTrue("Field.updatable", lField.getUpdatable());
            }
        }
    }

    /**
     * Apply access control on a Values Container using the proxy 'Checked'
     */
    public void testValuesContainerAccessControl() {
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        ACCESS_CONTROL_SHEET_PRODUCT, ACCESS_CONTROL_SHEET_REF);
        final CacheableValuesContainer lCheckedValuesContainer =
                sheetService.getCacheableSheet(normalRoleToken, lSheetId,
                        CacheProperties.MUTABLE);

        assertNotNull("ValuesContainer.valuesMap",
                lCheckedValuesContainer.getValue(FIELDS_WITH_ACCESS_CONTROL[0]));
        assertNull("ValuesContainer.valuesMap",
                lCheckedValuesContainer.getValue(FIELDS_WITH_ACCESS_CONTROL[1]));
    }
}

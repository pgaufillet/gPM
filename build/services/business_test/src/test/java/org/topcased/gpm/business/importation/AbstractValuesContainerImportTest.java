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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.serialization.data.FieldValueData;

/**
 * AbstractImportTest: Basic implementation for import tests of values
 * containers.
 * 
 * @author mkargbo
 */
public abstract class AbstractValuesContainerImportTest extends
        AbstractImportTest<CacheableValuesContainer> {
    private static final String UPDATE_VALUE = "_UPDATED";

    /**
     * Get expected import fields for admin user.
     * 
     * <pre>
     * Key -&gt; Label key
     * Value-&gt; Field value
     * </pre>
     * 
     * @return Expected import fields for admin user.
     */
    protected abstract Map<String, String> getExpectedFieldsToImportAdmin();

    /**
     * Get expected import fields for not admin user.
     * 
     * <pre>
     * Key -&gt; Label key
     * Value-&gt; Field value
     * </pre>
     * 
     * @return Expected import fields for not admin user.
     */
    protected abstract Map<String, String> getExpectedFieldsToImportNotAdmin();

    /**
     * Get fields that should not be imported for not admin user.
     * 
     * <pre>
     * Key -&gt; Label key
     * Value-&gt; Field value
     * </pre>
     * 
     * @return Not expected fields for not admin user.
     */
    protected abstract Map<String, String> getNotExpectedFieldsToImportNotAdmin();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    protected abstract CacheableValuesContainer getBusinessObject(
            final String pRoleToken, final String pElementId);

    /**
     * Test 'SKIP' import flag.
     * <p>
     * Remove elements and try to import them with 'SKIP' import's flag.<br />
     * Then, test element's identifier existence.
     * </p>
     */
    public void testSkipImport() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.SKIP, getImportFile());
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }

        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(StringUtils.isBlank(lId));
        }
    }

    /**
     * Test 'ERROR' import flag.
     * <p>
     * Try to import elements with 'ERROR' import's flag.<br />
     * An ImportException must be raise with message
     * 'ImportMessage.DO_NOT_IMPORT_TYPE'.
     * </p>
     */
    public void testErrorImport() {
        try {
            doImport(adminRoleToken, ImportFlag.ERROR, getImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            assertEquals("Bad import exception. Wrong message:'"
                    + e.getMessage() + "'",
                    ImportMessage.DO_NOT_IMPORT_TYPE.getValue(), e.getMessage());
        }
    }

    /**
     * Import a file using 'ERASE' flag.
     * 
     * @param pRoleToken
     *            Role token
     * @param pExpectedFieldValues
     *            Fields that should be import.
     * @param pNotExpectedFieldValues
     *            Fields that should not be import.
     */
    protected void doTestEraseImport(final String pRoleToken,
            final Map<String, String> pExpectedFieldValues,
            final Map<String, String> pNotExpectedFieldValues) {
        //Retrieve elements identifiers.

        try {
            doImport(pRoleToken, ImportFlag.ERASE, getImportFile());

            //Test elements existence
            for (String[] lRef : getImportedElement()) {
                String lId = getElementId(lRef);
                assertTrue(
                        "Element 'lRef' has not been imported correctly (element does not exist)",
                        StringUtils.isNotBlank(lId));
                CacheableValuesContainer lValuesContainer =
                        getBusinessObject(pRoleToken, lId);

                //Check imported fields.
                checkFields(lValuesContainer, pExpectedFieldValues,
                        pNotExpectedFieldValues, false);
            }
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Check values container fields.
     * <ul>
     * <li>Fields that should be import (existence, values (optional)</li>
     * <li>Fields that should not be import</li>
     * </ul>
     * 
     * @param pValuesContainer
     *            Values container to check
     * @param pExpectedFieldsValues
     *            Fields that should be import.
     * @param pNotExpectedFieldsValues
     *            Fields that should not be import.
     * @param pCheckValue
     *            True to check the field value.
     */
    @SuppressWarnings("unchecked")
    protected void checkFields(CacheableValuesContainer pValuesContainer,
            Map<String, String> pExpectedFieldsValues,
            Map<String, String> pNotExpectedFieldsValues, boolean pCheckValue) {
        for (String lFieldLabelKey : pExpectedFieldsValues.keySet()) {
            String lValue = pExpectedFieldsValues.get(lFieldLabelKey);
            if (lValue != null) {
                Object lFieldValue = pValuesContainer.getValue(lFieldLabelKey);
                final FieldValueData lField;
                if (lFieldValue instanceof List) {
                    lField = ((List<FieldValueData>) lFieldValue).get(0);
                }
                else {
                    lField = (FieldValueData) lFieldValue;
                }
                assertNotNull("The field '" + lFieldLabelKey
                        + "' must have a value", lField);
                if (pCheckValue) {
                    assertEquals(
                            "The new value is not well imported for the field '"
                                    + lFieldLabelKey + "'", lValue,
                            lField.getValue());
                }
            }
        }

        //Check not imported fields.
        for (String lFieldLabelKey : pNotExpectedFieldsValues.keySet()) {
            String lValue = pNotExpectedFieldsValues.get(lFieldLabelKey);
            if (lValue != null) {
                FieldValueData lField =
                        (FieldValueData) pValuesContainer.getValue(lFieldLabelKey);
                if (lField != null) {
                    assertFalse("The field '" + lFieldLabelKey
                            + "' should not be import", StringUtils.contains(
                            lField.getValue(), UPDATE_VALUE));
                }
            }
        }
    }

    /**
     * Check the attributes existence and their values.
     * 
     * @param pValuesContainer
     *            Values container containing the attributes to test.
     * @param pExpectedAttributesValues
     *            Attributes that should be import.
     */
    protected void checkAttributes(CacheableValuesContainer pValuesContainer,
            Map<String, String[]> pExpectedAttributesValues) {
        for (String lAttributeName : pExpectedAttributesValues.keySet()) {
            String[] lValue = pExpectedAttributesValues.get(lAttributeName);
            if (lValue != null) {
                String[] lAttributeValues =
                        pValuesContainer.getAttributeValues(lAttributeName);
                assertFalse("The attribute '" + lAttributeName
                        + "' must have a value",
                        ArrayUtils.isEmpty(lAttributeValues));
                assertTrue("The new value of the attribute'" + lAttributeName
                        + "' has not been imported properly",
                        ArrayUtils.isEquals(lValue, lAttributeValues));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyAdminAssertion()
     */
    @Override
    protected void createOnlyAdminAssertion() {
        //Test elements existence
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(
                    "Element 'lRef' has not been imported correctly (element does not exist)",
                    StringUtils.isNotBlank(lId));
            //Test element imported fields.
            CacheableValuesContainer lValuesContainer =
                    getBusinessObject(adminRoleToken, lId);

            //Check imported fields.
            checkFields(lValuesContainer, getExpectedFieldsToImportAdmin(),
                    EMPTY_MAP, false);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#createOnlyNotAdminAssertion()
     */
    @Override
    protected void createOnlyNotAdminAssertion() {
        //Test elements existence
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(
                    "Element 'lRef' has not been imported correctly (element does not exist)",
                    StringUtils.isNotBlank(lId));
            //Test element imported fields.
            CacheableValuesContainer lValuesContainer =
                    getBusinessObject(adminRoleToken, lId);

            //Check imported fields.
            checkFields(lValuesContainer, getExpectedFieldsToImportNotAdmin(),
                    getNotExpectedFieldsToImportNotAdmin(), false);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyAdminAssertion(Collection<String> pElementsId) {
        //Test elements existence
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(
                    "Element lRel has not been imported correctly (element does not exist)",
                    StringUtils.isNotBlank(lId));
            assertTrue("Element 'lRef' has not the same identifier of its previous version",
                    pElementsId.contains(lId));
            CacheableValuesContainer lValuesContainer =
                    getBusinessObject(adminRoleToken, lId);

            //Check imported fields.
            checkFields(lValuesContainer, getExpectedFieldsToImportAdmin(),
                    EMPTY_MAP, true);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#updateOnlyNotAdminAssertion(java.util.Collection)
     */
    @Override
    protected void updateOnlyNotAdminAssertion(Collection<String> pElementsId) {
        //Test elements existence
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(
                    "Element 'lRef' has not been imported correctly (element does not exist)",
                    StringUtils.isNotBlank(lId));
            assertTrue("Element 'lRef' has not the same identifier of its previous version",
                    pElementsId.contains(lId));
            CacheableValuesContainer lValuesContainer =
                    getBusinessObject(normalRoleToken, lId);

            //Check imported fields.
            checkFields(lValuesContainer, getExpectedFieldsToImportNotAdmin(),
                    getNotExpectedFieldsToImportNotAdmin(), true);
        }
    }

}

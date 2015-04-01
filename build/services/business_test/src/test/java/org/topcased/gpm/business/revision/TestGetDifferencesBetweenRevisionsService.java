/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision;

import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.AttributeComparatorData;
import org.topcased.gpm.business.attributes.AttributeComparatorDataValue;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.fields.FieldComparatorData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldComparatorData;
import org.topcased.gpm.business.fields.MultipleLineFieldComparatorData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.sheet.service.sheetAccess.SheetDataAccess;
import org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * Tests the method
 * <CODE>getDifferencesBetweenRevisions<CODE> of the Revision Service.
 * 
 * @author mfranche
 */
public class TestGetDifferencesBetweenRevisionsService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used for normal case. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The label set on the first revision */
    private static final String REVISION_LABEL1 = "Label_Revision_1";

    /** The label set on the snd revision */
    private static final String REVISION_LABEL2 = "Label_Revision_2";

    /** The field name of the sheet */
    private static final String FIELD_NAME = "DOG_ref";

    /** The new field value */
    private static final String NEW_FIELD_VALUE = "newDogRefValue";

    /**
     * The old field value (before the modification)
     */
    private static final String OLD_FIELD_VALUE = "Lassie";

    /**
     * The reference field name
     */
    private static final String REFERENCE_FIELD_NAME = "DOG_name";

    /** The new reference field value */
    private static final String NEW_REFERENCE_FIELD_VALUE = "newDogNameValue";

    /**
     * The old reference field value (before the modification)
     */
    private static final String OLD_REFERENCE_FIELD_VALUE = "Lassie";

    /**
     * The modified display group
     */
    private static final String DISPLAY_GROUP_NAME = "Identity";

    /**
     * The attribute name
     */
    //    private static final String ATTRIBUTE_NAME = "name_attribute";
    /**
     * The attribute value for the first revision
     */
    //    private static final String ATTRIBUTE_FIRST_VALUE = "value_attribute_1";
    /**
     * The attribute value for the snd revision
     */
    //    private static final String ATTRIBUTE_SND_VALUE = "value_attribute_2";
    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetByKeyConfidentialAccess.xml";

    /** The field comparator data attributes */
    private static final String FIELD_COMP_DATA_LABEL_KEY = "DOG_ref";

    private static final String FIELD_COMP_DATA_FIELD_TYPE = "STRING";

    private static final String FIELD_COMP_DATA_DISPLAY_TYPE = "SINGLE_LINE";

    private static final String FIELD_COMP_DATA_I18N_NAME = "Ref";

    // The sheet reference
    private static final String SHEET_REF = "Medor";

    private static final String ATTRIBUTE_NAME = "log";

    private static final String ATTRIBUTE_VALUE1 = "valeurDuLog_Label1";

    private static final String ATTRIBUTE_VALUE2 = "valeurDuLog_Label2";

    /**
     * Tests the getDifferencesBetweenRevisions method in a normal way.
     */
    public void testNormalCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.
        SheetData lSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // Create the first revision
        //        RevisionData lRevisionData =
        //                revisionService.getRevisionModel(roleToken, lSheetType,
        //                        getProductName());
        //
        //        AttributeData lAttributeData =
        //                new AttributeData(ATTRIBUTE_NAME,
        //                        new String[] { ATTRIBUTE_FIRST_VALUE });
        //
        //        lRevisionData.setAttributeDatas(new AttributeData[] { lAttributeData });
        //
        //        lRevisionData.setLabel(REVISION_LABEL1);

        String lFirstRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL1);

        // Modify a field of the sheet data
        SheetDataAccess lSheetDataAccess = new SheetDataAccess(lSheetData);
        ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).setValue(NEW_FIELD_VALUE);

        // Modify a field reference of the sheet data
        ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                REFERENCE_FIELD_NAME)).setValue(NEW_REFERENCE_FIELD_VALUE);

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        // Create a new revision
        //        lRevisionData =
        //                revisionService.getRevisionModel(roleToken, lSheetType,
        //                        getProductName());
        //        lAttributeData =
        //                new AttributeData(ATTRIBUTE_NAME,
        //                        new String[] { ATTRIBUTE_SND_VALUE });
        //
        //        lRevisionData.setAttributeDatas(new AttributeData[] { lAttributeData });
        //
        //        lRevisionData.setLabel(REVISION_LABEL2);
        String lSndRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL2);

        // Get the differences between the two revisions
        startTimer();
        RevisionDifferencesData lRevisionDifferencesData =
                revisionService.getDifferencesBetweenRevisions(adminRoleToken,
                        lSheetId, lFirstRevisionId, lSndRevisionId);
        stopTimer();

        // Verifications of the returned RevisionDifferencesData
        verifyRevisionDifferencesData(lRevisionDifferencesData, lSheetId,
                lFirstRevisionId, lSndRevisionId);

    }

    /**
     * Verify if the revisionDifferencesData structure is correct.
     * 
     * @param pRevisionDifferencesData
     *            The revisionDifferencesData structure
     * @param pContainerId
     *            The container id
     * @param pFirstRevisionId
     *            The first revision id
     * @param pSndRevisionId
     *            The snd revision id
     */
    protected void verifyRevisionDifferencesData(
            RevisionDifferencesData pRevisionDifferencesData,
            String pContainerId, String pFirstRevisionId, String pSndRevisionId) {

        // Verify the simple attributes
        verifySimpleAttributes(pRevisionDifferencesData, pContainerId,
                pFirstRevisionId, pSndRevisionId);

        // Verify the values contained in the FieldComparatorGroupData
        assertTrue(
                "The method getDifferencesBetweenRevisions returns "
                        + "an incorrect FieldComparatorGroupData ",
                pRevisionDifferencesData.getFieldComparatorGroupData().length == 1);
        FieldComparatorGroupData lFieldComparatorGroupData =
                pRevisionDifferencesData.getFieldComparatorGroupData()[0];

        assertNotNull(
                "The label key attribute of the FieldComparatorGroupData is incorrect.",
                lFieldComparatorGroupData.getLabelKey());
        assertEquals(
                "The label key attribute of the FieldComparatorGroupData is incorrect.",
                lFieldComparatorGroupData.getLabelKey(), DISPLAY_GROUP_NAME);
        assertNotNull(
                "The i18nName attribute of the FieldComparatorGroupData is incorrect.",
                lFieldComparatorGroupData.getI18nName());

        assertTrue(
                "The method getDifferencesBetweenRevisions returns "
                        + "an incorrect FieldComparatorGroupData ",
                lFieldComparatorGroupData.getMultipleLineFieldComparatorDatas().length == 1);
        MultipleLineFieldComparatorData lMultipleLineFieldComparatorData =
                lFieldComparatorGroupData.getMultipleLineFieldComparatorDatas()[0];
        checkMultipleLineFieldComparatorDataAttribute(lMultipleLineFieldComparatorData);

        assertTrue(
                "The method getDifferencesBetweenRevisions returns "
                        + "an incorrect FieldComparatorGroupData ",
                lMultipleLineFieldComparatorData.getLineFieldComparatorDatas().length == 1);
        LineFieldComparatorData lLineFieldComparatorData =
                lMultipleLineFieldComparatorData.getLineFieldComparatorDatas()[0];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lLineFieldComparatorData.getFieldComparatorDatas().length == 1);

        FieldComparatorData lFieldComparatorData =
                lLineFieldComparatorData.getFieldComparatorDatas()[0];

        // Check the field comparator data attribute
        checkFieldComparatorDataAttribute(lFieldComparatorData);

        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lFieldComparatorData.getComparatorValues().length == 2);

        FieldValueData lFirstValue =
                lFieldComparatorData.getComparatorValues()[0];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lFirstValue.getValues().length == 1);
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lFirstValue.getValues()[0].compareTo(OLD_FIELD_VALUE) == 0);
        FieldValueData lSndValue =
                lFieldComparatorData.getComparatorValues()[1];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lSndValue.getValues().length == 1);
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect FieldComparatorGroupData ",
                lSndValue.getValues()[0].compareTo(NEW_FIELD_VALUE) == 0);

        FieldComparatorData[] lFieldComparatorDataTab =
                pRevisionDifferencesData.getReferenceComparator().getFieldComparatorDatas();
        // Verify the values contained in the referenceComparator.
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect referenceComparator",
                lFieldComparatorDataTab.length == 1);
        lFieldComparatorData = lFieldComparatorDataTab[0];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect referenceComparator",
                lFieldComparatorData.getComparatorValues().length == 2);
        lFirstValue = lFieldComparatorData.getComparatorValues()[0];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect referenceComparator",
                lFirstValue.getValues().length == 1);
        assertTrue(
                "The method getDifferencesBetweenRevisions returns "
                        + "an incorrect referenceComparator",
                lFirstValue.getValues()[0].compareTo(OLD_REFERENCE_FIELD_VALUE) == 0);
        lSndValue = lFieldComparatorData.getComparatorValues()[1];
        assertTrue("The method getDifferencesBetweenRevisions returns "
                + "an incorrect referenceComparator",
                lSndValue.getValues().length == 1);
        assertTrue(
                "The method getDifferencesBetweenRevisions returns "
                        + "an incorrect referenceComparator",
                lSndValue.getValues()[0].compareTo(NEW_REFERENCE_FIELD_VALUE) == 0);

        // Verify the values contained in the attribute data comparator.
        //        assertTrue(
        //                "The method getDifferencesBetweenRevisions returns "
        //                        + "an incorrect attribute comparator data",
        //                pRevisionDifferencesData.getAttributeComparatorDatas().length == 1);
        //        AttributeComparatorData lAttributeComparatorData =
        //                pRevisionDifferencesData.getAttributeComparatorDatas()[0];
        //        assertTrue(
        //                "The method getDifferencesBetweenRevisions returns "
        //                        + "an incorrect attribute comparator data",
        //                lAttributeComparatorData.getAttributeComparatorDataValues().length == 2);
        //        AttributeComparatorDataValue lFstAttributeComparatorDataValue =
        //                lAttributeComparatorData.getAttributeComparatorDataValues()[0];
        //        assertTrue("The method getDifferencesBetweenRevisions returns "
        //                + "an incorrect attribute comparator data",
        //                lFstAttributeComparatorDataValue.getValues().length == 1);
        //        String lFstCompValue = lFstAttributeComparatorDataValue.getValues()[0];
        //        assertTrue("The method getDifferencesBetweenRevisions returns "
        //                + "an incorrect attribute comparator data",
        //                lFstCompValue.compareTo(ATTRIBUTE_FIRST_VALUE) == 0);
        //
        //        AttributeComparatorDataValue lSndAttributeComparatorDataValue =
        //                lAttributeComparatorData.getAttributeComparatorDataValues()[1];
        //        assertTrue("The method getDifferencesBetweenRevisions returns "
        //                + "an incorrect attribute comparator data",
        //                lSndAttributeComparatorDataValue.getValues().length == 1);
        //        String lSndCompValue = lSndAttributeComparatorDataValue.getValues()[0];
        //        assertTrue("The method getDifferencesBetweenRevisions returns "
        //                + "an incorrect attribute comparator data",
        //                lSndCompValue.compareTo(ATTRIBUTE_SND_VALUE) == 0);
    }

    /**
     * Check the multiple line field comparator data attribute
     * 
     * @param pMultipleLineFieldComparatorData
     *            The multiple line field comparator data structure to test
     */
    protected void checkMultipleLineFieldComparatorDataAttribute(
            MultipleLineFieldComparatorData pMultipleLineFieldComparatorData) {
        assertEquals(
                "The label key attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.getLabelKey(),
                FIELD_COMP_DATA_LABEL_KEY);
        assertEquals(
                "The ref attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.getRef(), 0);
        assertFalse(
                "The multiLined attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.isMultiLined());
        assertFalse(
                "The multiField attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.isMultiField());
        assertTrue(
                "The exportable attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.isExportable());
        assertEquals(
                "The i18nName attribute of the multiple line field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.getI18nName(),
                FIELD_COMP_DATA_I18N_NAME);
        assertFalse("The confidential attribute of the multiple line "
                + "field comparator data is incorrect.",
                pMultipleLineFieldComparatorData.isConfidential());
    }

    /**
     * Check the field comparator data attributes
     * 
     * @param pFieldComparatorData
     *            The field comparator data to test
     */
    protected void checkFieldComparatorDataAttribute(
            FieldComparatorData pFieldComparatorData) {
        assertEquals(
                "The label key attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getLabelKey(), FIELD_COMP_DATA_LABEL_KEY);
        assertFalse(
                "The confidential attribute of the field comparator data is incorrect.",
                pFieldComparatorData.isConfidential());
        assertNull(
                "The default value attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getDefaultValue());
        assertNull(
                "The description attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getDescription());
        assertFalse(
                "The mandatory attribute of the field comparator data is incorrect.",
                pFieldComparatorData.isMandatory());
        assertTrue(
                "The updatbale attribute of the field comparator data is incorrect.",
                pFieldComparatorData.isUpdatable());
        assertEquals(pFieldComparatorData.getFieldType(),
                FIELD_COMP_DATA_FIELD_TYPE);
        assertEquals(
                "The display type attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getDisplayType(),
                FIELD_COMP_DATA_DISPLAY_TYPE);
        assertTrue(
                "The exportable attribute of the field comparator data is incorrect.",
                pFieldComparatorData.isExportable());
        assertEquals(pFieldComparatorData.getI18nName(),
                FIELD_COMP_DATA_I18N_NAME);
        assertNull(
                "The file value attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getFileValue());
        assertNull(
                "The text area size attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getTextAreaSize());
        assertNull(
                "The field available value attribute of the field comparator data is incorrect.",
                pFieldComparatorData.getFieldAvailableValueData());
    }

    /**
     * Verify the simple attributes of the revision differences data
     * 
     * @param pRevisionDifferencesData
     *            The revisionDifferencesData structure
     * @param pContainerId
     *            The container id
     * @param pFirstRevisionId
     *            The first revision id
     * @param pSndRevisionId
     *            The second revision id
     */
    protected void verifySimpleAttributes(
            RevisionDifferencesData pRevisionDifferencesData,
            String pContainerId, String pFirstRevisionId, String pSndRevisionId) {
        org.topcased.gpm.business.serialization.data.RevisionData lFirstRevisionData =
                revisionService.getRevision(adminRoleToken, pContainerId,
                        pFirstRevisionId);
        org.topcased.gpm.business.serialization.data.RevisionData lSndRevisionData =
                revisionService.getRevision(adminRoleToken, pContainerId,
                        pSndRevisionId);

        assertNull(
                "The author attribute of revisionDifferencesData is incorrect.",
                pRevisionDifferencesData.getAuthor());
        assertNotNull(
                "The creation date attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getCreationDate());
        assertTrue(
                "The creation date attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getCreationDate().length == 2);
        assertEquals(
                "The creation date attribute of revisionDifferencesData is incorrect",
                FieldsUtil.formatDate(pRevisionDifferencesData.getCreationDate()[0]),
                FieldsUtil.formatDate(lFirstRevisionData.getDate()));
        assertEquals(
                "The creation date attribute of revisionDifferencesData is incorrect",
                FieldsUtil.formatDate(pRevisionDifferencesData.getCreationDate()[1]),
                FieldsUtil.formatDate(lSndRevisionData.getDate()));

        assertNotNull(
                "The id attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getId());
        assertTrue("The id attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getId().length == 2);
        assertEquals(
                "The id attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getId()[0], lFirstRevisionData.getId());
        assertEquals(
                "The id attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getId()[1], lSndRevisionData.getId());

        assertNotNull(
                "The label attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getLabel());
        assertTrue(
                "The label attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getLabel().length == 2);
        assertEquals(
                "The label attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getLabel()[0],
                lFirstRevisionData.getLabel());
        assertEquals(
                "The label attribute of revisionDifferencesData is incorrect",
                pRevisionDifferencesData.getLabel()[1],
                lSndRevisionData.getLabel());
    }

    /**
     * Tests the getDifferences method with an invalid container id
     */
    public void testIncorrectContainerIdCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.
        SheetData lSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // Create the first revision
        String lFirstRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL1);

        // Modify a field of the sheet data
        SheetDataAccess lSheetDataAccess = new SheetDataAccess(lSheetData);
        ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).setValue(NEW_FIELD_VALUE);

        // Modify a field reference of the sheet data
        ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                REFERENCE_FIELD_NAME)).setValue(NEW_REFERENCE_FIELD_VALUE);

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        // Create a new revision
        String lSndRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL2);

        // Get the differences between the two revisions
        try {
            revisionService.getDifferencesBetweenRevisions(adminRoleToken,
                    INVALID_CONTAINER_ID, lFirstRevisionId, lSndRevisionId);
            fail("The exception has not been thrown.");
        }

        catch (InvalidIdentifierException ex) {
            assertEquals(ex.getIdentifier(), INVALID_CONTAINER_ID);
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Tests the getDifferences method with invalid revision ids
     */
    public void testIncorrectRevisionIdsCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.
        SheetData lSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // Create the first revision
        String lFirstRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL1);

        // Modify a field of the sheet data
        SheetDataAccess lSheetDataAccess = new SheetDataAccess(lSheetData);
        ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).setValue(NEW_FIELD_VALUE);

        // Modify a field reference of the sheet data
        ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                REFERENCE_FIELD_NAME)).setValue(NEW_REFERENCE_FIELD_VALUE);

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        // Create a new revision
        String lSndRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL2);

        try {
            // Get the difference with a null first revision id
            revisionService.getDifferencesBetweenRevisions(adminRoleToken,
                    lSheetId, "", lSndRevisionId);
            // Get the differences with a null snd revision id
            revisionService.getDifferencesBetweenRevisions(adminRoleToken,
                    lSheetId, lFirstRevisionId, "");
        }

        catch (InvalidIdentifierException ex) {
            // ok
        }
    }

    /**
     * Tests the getDifferences method with an invalid token case
     */
    public void testInvalidTokenCase() {
        // Get the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lSheetId = lSheetSummary.get(0).getId();

        // Next the model of a revision.
        SheetData lSheetData =
                sheetService.getSheetByKey(adminRoleToken, lSheetId);

        // Create the first revision
        String lFirstRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL1);

        // Modify a field of the sheet data
        SheetDataAccess lSheetDataAccess = new SheetDataAccess(lSheetData);
        ((SimpleFieldData) lSheetDataAccess.getField(FIELD_NAME)).setValue(NEW_FIELD_VALUE);

        // Modify a field reference of the sheet data
        ((SimpleFieldData) lSheetDataAccess.getReference().getField(
                REFERENCE_FIELD_NAME)).setValue(NEW_REFERENCE_FIELD_VALUE);

        sheetService.updateSheet(adminRoleToken, lSheetData, null);

        // Create a new revision
        String lSndRevisionId =
                revisionService.createRevision(adminRoleToken, lSheetId,
                        REVISION_LABEL2);

        // Get the differences between the two revisions
        try {
            revisionService.getDifferencesBetweenRevisions("", lSheetId,
                    lFirstRevisionId, lSndRevisionId);
            fail("The exception has not been thrown.");
        }

        catch (InvalidTokenException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an InvalidTokenException.");
        }
    }

    /**
     * Tests the getDifferencesBetweenRevisionsService method by the user2 with
     * a READ_WRITE_LOCK set by user1
     */
    public void testReadWriteLockOnSheetCase() {
        testLockOnSheetCase(SHEET_READ_WRITE_LOCK_REF);
    }

    /**
     * Tests the getDifferencesBetweenRevisionsService method on locked sheet.
     * 
     * @param pSheetReference
     *            The sheet reference
     */
    protected void testLockOnSheetCase(String pSheetReference) {
        sheetService = serviceLocator.getSheetService();

        revisionService = serviceLocator.getRevisionService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Search sheet data
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Bernard's store", 
                    SHEET_READ_WRITE_LOCK_REF);

        Collection<RevisionSummaryData> lRevisionSummary =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);
        assertNotNull(
                "getRevisionsCount returns 0 instead of a list of revisions",
                lRevisionSummary);
        assertFalse("getRevisionsSummary returs no revision",
                lRevisionSummary.isEmpty());
        String lRevisionId = lRevisionSummary.iterator().next().getId();

        try {
            revisionService.getDifferencesBetweenRevisions(lRoleToken,
                    lSheetId, lRevisionId, lRevisionId);
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("Unexpected exception: " + e);
        }
    }

    /**
     * Tests the getDifferencesBetweenRevisions method.
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();
        revisionService = serviceLocator.getRevisionService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        // set confidential access on sheet type SHEET_TYPE
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        try {
            revisionService.getDifferencesBetweenRevisions(lRoleToken, lId, "",
                    "");
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lEx) {
            // ok
        }
        finally {
            authorizationService.logout(lUserToken);
        }
    }

    /**
     * Tests the attribute comparator data
     */
    public void testAttributesComparatorCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the revision service.
        revisionService = serviceLocator.getRevisionService();

        // Get Sheet data associated to SHEET_REF
        final String lSheetId =
            sheetService.getSheetIdByReference("PET STORE", "Bernard's store", 
                    SHEET_REF);

        List<RevisionSummaryData> lRevisionSummaryDataList =
                revisionService.getRevisionsSummary(adminRoleToken, lSheetId);

        assertTrue("The revision summary data list size is incorrect.",
                lRevisionSummaryDataList.size() >= 2);

        String lFirstRevisionId = lRevisionSummaryDataList.get(0).getId();
        String lSndRevisionId = lRevisionSummaryDataList.get(1).getId();

        // Get the differences between those revisions
        RevisionDifferencesData lRevisionDifferencesData =
                revisionService.getDifferencesBetweenRevisions(adminRoleToken,
                        lSheetId, lFirstRevisionId, lSndRevisionId);

        assertNotNull("The attribute comparator data is incorrect.",
                lRevisionDifferencesData.getAttributeComparatorDatas());
        assertTrue(
                "The attribute comparator data size is incorrect.",
                lRevisionDifferencesData.getAttributeComparatorDatas().length >= 2);

        AttributeComparatorData lLogAttributeComparatorData = null;
        for (AttributeComparatorData lAttributeCompData : lRevisionDifferencesData.getAttributeComparatorDatas()) {
            if (lAttributeCompData.getName().compareTo(ATTRIBUTE_NAME) == 0) {
                lLogAttributeComparatorData = lAttributeCompData;
            }
        }

        assertNotNull("The attribute comparator data values is incorrect.",
                lLogAttributeComparatorData.getAttributeComparatorDataValues());
        assertTrue(
                "The attribute comparator data values size is incorrect.",
                lLogAttributeComparatorData.getAttributeComparatorDataValues().length == 2);
        AttributeComparatorDataValue[] lAttCompDataValuesTab =
                lLogAttributeComparatorData.getAttributeComparatorDataValues();

        AttributeComparatorDataValue lFstCompDataValue =
                lAttCompDataValuesTab[0];
        assertNotNull("The first comparator data value is incorrect.",
                lFstCompDataValue.getValues());
        assertTrue("The first comparator data value is incorrect.",
                lFstCompDataValue.getValues().length == 1);
        String lFstVal = lFstCompDataValue.getValues()[0];
        assertEquals("The first value is incorrect.", lFstVal, ATTRIBUTE_VALUE1);

        AttributeComparatorDataValue lSndCompDataValue =
                lAttCompDataValuesTab[1];
        assertNotNull("The snd comparator data value is incorrect.",
                lSndCompDataValue.getValues());
        assertTrue("The snd comparator data value is incorrect.",
                lSndCompDataValue.getValues().length == 1);
        String lSndVal = lSndCompDataValue.getValues()[0];
        assertEquals("The snd value is incorrect.", lSndVal, ATTRIBUTE_VALUE2);
    }
}

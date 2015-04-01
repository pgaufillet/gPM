/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.AttributeComparatorData;
import org.topcased.gpm.business.attributes.AttributeComparatorDataValue;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.impl.AttributesUtils;
import org.topcased.gpm.business.fields.FieldComparatorData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldComparatorData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldComparatorData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.revision.FieldComparatorGroupData;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.revision.RevisionDifferencesData;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.revision.Revision;

/**
 * Helper methods to create revision data instances
 * 
 * @author mfranche
 */
public class RevisionUtils {

    /**
     * Useful separator
     */
    protected static final String SEPARATOR = " : ";

    /**
     * Create a RevisionSummaryData object from a Revision entity
     * 
     * @param pRevision
     *            Revision entity
     * @return The revision summary object created from the revision
     */
    public static RevisionSummaryData createRevisionSummaryData(
            Revision pRevision) {
        RevisionSummaryData lRevisionSummaryData = new RevisionSummaryData();

        lRevisionSummaryData.setCreationDate(pRevision.getCreationDate());
        lRevisionSummaryData.setAuthor(pRevision.getAuthor());
        lRevisionSummaryData.setLabel(pRevision.getLabel());
        lRevisionSummaryData.setId(pRevision.getId());

        // Fill attribute Data
        int lAttributesDataTabLength =
                pRevision.getRevisionAttrs().getAttributes().size();
        AttributeData[] lAttributesDataTab =
                new AttributeData[lAttributesDataTabLength];
        int i = 0;
        for (Attribute lAttribute : pRevision.getRevisionAttrs().getAttributes()) {
            lAttributesDataTab[i] =
                    AttributesUtils.createAttributeData(lAttribute);
            i++;
        }
        lRevisionSummaryData.setAttributeDatas(lAttributesDataTab);

        return lRevisionSummaryData;
    }

    /**
     * Create a RevisionDifferencesData in order to compare two revisions
     * 
     * @param pRevisionFirstData
     *            First revision Data to be compared
     * @param pRevisionSndData
     *            Snd revision Data to be compared
     * @return The revision differences object
     */
    public static RevisionDifferencesData createRevisionDifferencesData(
            RevisionData pRevisionFirstData, RevisionData pRevisionSndData) {
        RevisionDifferencesData lRevisionDiffData =
                new RevisionDifferencesData();

        // Set simple attributes (id, label, creationDate and author)
        setAttributes(lRevisionDiffData, pRevisionFirstData, pRevisionSndData);

        lRevisionDiffData.setAttributeComparatorDatas(createAttributeComparatorData(
                pRevisionFirstData, pRevisionSndData));

        lRevisionDiffData.setFieldComparatorGroupData(createFieldComparatorGroupData(
                pRevisionFirstData, pRevisionSndData));

        lRevisionDiffData.setReferenceComparator(createLineFieldComparatorData(
                pRevisionFirstData, pRevisionSndData));

        return lRevisionDiffData;
    }

    /**
     * Create the attributeComparatorData array containing the attributes
     * differences between the two revisions data.
     * 
     * @param pFirstRevisionData
     *            First revision data to be compared
     * @param pSndRevisionData
     *            Snd revision data to be compared
     * @return AttributeComparatorDataTab structure
     */
    protected static AttributeComparatorData[] createAttributeComparatorData(
            RevisionData pFirstRevisionData, RevisionData pSndRevisionData) {

        ArrayList<AttributeComparatorData> lAttributeComparatorDataArrayList =
                new ArrayList<AttributeComparatorData>();

        ArrayList<String> lAlreadyParseAttributeNameArrayList =
                new ArrayList<String>();

        if (pFirstRevisionData.getAttributeDatas() != null) {
            // Through first attributes
            for (AttributeData lFirstAttributeData : pFirstRevisionData.getAttributeDatas()) {
                AttributeData lSndAttributeData =
                        searchAttributeData(lFirstAttributeData.getName(),
                                pSndRevisionData);
                AttributeComparatorData lAttributeComparatorData =
                        createAttributeComparatorData(lFirstAttributeData,
                                lSndAttributeData);
                if (lAttributeComparatorData != null) {
                    lAttributeComparatorDataArrayList.add(lAttributeComparatorData);
                }
                lAlreadyParseAttributeNameArrayList.add(lFirstAttributeData.getName());

            }
        }

        if (pSndRevisionData.getAttributeDatas() != null) {
            // Through snd attributes
            for (AttributeData lSndAttributeData : pSndRevisionData.getAttributeDatas()) {
                if (!lAlreadyParseAttributeNameArrayList.contains(lSndAttributeData.getName())) {
                    AttributeData lFstAttributeData =
                            searchAttributeData(lSndAttributeData.getName(),
                                    pFirstRevisionData);
                    AttributeComparatorData lAttributeComparatorData =
                            createAttributeComparatorData(lFstAttributeData,
                                    lSndAttributeData);
                    if (lAttributeComparatorData != null) {
                        lAttributeComparatorDataArrayList.add(lAttributeComparatorData);
                    }
                }
            }
        }
        return createAttributeComparatorDataTab(lAttributeComparatorDataArrayList);
    }

    /**
     * Create an AttributeComparatorData tab from an array list of this type
     * 
     * @param pList
     *            The array list
     * @return The created tab
     */
    protected static AttributeComparatorData[] createAttributeComparatorDataTab(
            ArrayList<AttributeComparatorData> pList) {
        AttributeComparatorData[] lAttributeComparatorDataTab =
                new AttributeComparatorData[pList.size()];
        for (int i = 0; i < pList.size(); i++) {
            lAttributeComparatorDataTab[i] = pList.get(i);
        }
        return lAttributeComparatorDataTab;
    }

    /**
     * Compare two attribute data
     * 
     * @param pFirstAttributeData
     *            First attribute data to be compared
     * @param pSndAttributeData
     *            Snd attribute data to be compared
     * @return AttributeComparatorData structure (or null if the two attributes
     *         data are equals)
     */
    protected static AttributeComparatorData createAttributeComparatorData(
            AttributeData pFirstAttributeData, AttributeData pSndAttributeData) {
        AttributeComparatorData lAttributeComparatorData = null;

        if (pFirstAttributeData == null) {
            if (pSndAttributeData != null) {
                // create the Attribute comparator data
                AttributeComparatorDataValue[] lCompTab =
                        {
                         null,
                         new AttributeComparatorDataValue(
                                 pSndAttributeData.getValues()) };
                lAttributeComparatorData =
                        new AttributeComparatorData(
                                pSndAttributeData.getName(), lCompTab);
            }
        }
        else {
            // create the Attribute comparator data
            lAttributeComparatorData = new AttributeComparatorData();
            lAttributeComparatorData.setName(pFirstAttributeData.getName());

            AttributeComparatorDataValue[] lCompTab =
                    new AttributeComparatorDataValue[2];

            AttributeComparatorDataValue lFstAttributeComparatorDataValue =
                    new AttributeComparatorDataValue(
                            pFirstAttributeData.getValues());
            lCompTab[0] = lFstAttributeComparatorDataValue;

            if (pSndAttributeData != null) {
                // Compare the values
                if (!Arrays.equals(pFirstAttributeData.getValues(),
                        pSndAttributeData.getValues())) {
                    AttributeComparatorDataValue lSndAttributeComparatorDataValue =
                            new AttributeComparatorDataValue(
                                    pSndAttributeData.getValues());
                    lCompTab[1] = lSndAttributeComparatorDataValue;
                }

            }
            else {
                lCompTab[1] = null;
            }

            lAttributeComparatorData.setAttributeComparatorDataValues(lCompTab);
        }

        return lAttributeComparatorData;
    }

    /**
     * Search the attributeData named pAttributeName in the revisionData
     * 
     * @param pAttributeName
     *            Name of the searched attribute
     * @param pRevisionData
     *            Revision data structure
     * @return The found attribute data
     */
    protected static AttributeData searchAttributeData(String pAttributeName,
            RevisionData pRevisionData) {
        boolean lFound = false;
        int i = 0;

        AttributeData lAttributeData = null;
        if (pRevisionData.getAttributeDatas() != null) {

            while (!lFound && i < pRevisionData.getAttributeDatas().length) {
                lAttributeData = pRevisionData.getAttributeDatas()[i];
                if (lAttributeData.getName().compareTo(pAttributeName) == 0) {
                    lFound = true;
                }
                else {
                    i++;
                }
            }

            if (!lFound) {
                lAttributeData = null;
            }
        }
        return lAttributeData;
    }

    /**
     * Set the attributes to the revisionDifferencesData, reading values in the
     * pRevisionFirstData and pRevisionSndData.
     * 
     * @param pRevisionDifferencesData
     *            The revision differences data structure to be updated
     * @param pRevisionFirstData
     *            The revision Data to be compared
     * @param pRevisionSndData
     *            The revision Data to be compared
     */
    protected static void setAttributes(
            RevisionDifferencesData pRevisionDifferencesData,
            RevisionData pRevisionFirstData, RevisionData pRevisionSndData) {

        // Id
        if (pRevisionFirstData.getId() != null
                && pRevisionSndData.getId() != null
                && !pRevisionFirstData.getId().equals(pRevisionSndData.getId())) {
            String[] lIdTab =
                    new String[] { pRevisionFirstData.getId(),
                                  pRevisionSndData.getId() };
            pRevisionDifferencesData.setId(lIdTab);
        }

        // Label
        if (pRevisionFirstData.getLabel().compareTo(pRevisionSndData.getLabel()) != 0) {
            String[] lLabelTab =
                    new String[] { pRevisionFirstData.getLabel(),
                                  pRevisionSndData.getLabel() };
            pRevisionDifferencesData.setLabel(lLabelTab);
        }

        // Author
        if (pRevisionFirstData.getAuthor().compareTo(
                pRevisionSndData.getAuthor()) != 0) {
            String[] lAuthorTab =
                    new String[] { pRevisionFirstData.getAuthor(),
                                  pRevisionSndData.getAuthor() };
            pRevisionDifferencesData.setAuthor(lAuthorTab);
        }

        // Creation date
        if (pRevisionFirstData.getCreationDate().compareTo(
                pRevisionSndData.getCreationDate()) != 0) {
            Date[] lDateTab =
                    new Date[] { pRevisionFirstData.getCreationDate(),
                                pRevisionSndData.getCreationDate() };
            pRevisionDifferencesData.setCreationDate(lDateTab);
        }

    }

    /**
     * Create the fieldComparatorGroupData array containing the fieldgroupData
     * differences between the two revisions data.
     * 
     * @param pRevisionFirstData
     *            First revision data to be compared
     * @param pRevisionSndData
     *            Snd revision data to be compared
     * @return Field comparator group data tab structure
     */
    protected static FieldComparatorGroupData[] createFieldComparatorGroupData(
            RevisionData pRevisionFirstData, RevisionData pRevisionSndData) {
        ArrayList<FieldComparatorGroupData> lFieldComparatorGroupDataList =
                new ArrayList<FieldComparatorGroupData>();

        FieldGroupData[] lFieldGroupFirstDataTab =
                pRevisionFirstData.getFieldGroupDatas();
        FieldGroupData[] lFieldGroupSndDataTab =
                pRevisionSndData.getFieldGroupDatas();

        // Array list containing the named of (group : multipleLineField)
        // that have already been compared.
        ArrayList<String> lAlreadyComparedArrayList = new ArrayList<String>();

        // Through the first revision groups
        for (FieldGroupData lFieldGroupData : lFieldGroupFirstDataTab) {
            FieldComparatorGroupData lFieldComparatorGroupData =
                    new FieldComparatorGroupData();
            lFieldComparatorGroupData.setLabelKey(lFieldGroupData.getLabelKey());
            lFieldComparatorGroupData.setI18nName(lFieldGroupData.getI18nName());

            ArrayList<MultipleLineFieldComparatorData> lMLFCDataList =
                    new ArrayList<MultipleLineFieldComparatorData>();

            for (MultipleLineFieldData lMLFstFData : lFieldGroupData.getMultipleLineFieldDatas()) {
                // Search the associated MultipleLineFieldData structure in the
                // snd revision data.
                MultipleLineFieldData lMultipleLineSndFieldData =
                        searchMultipleLineFieldData(pRevisionSndData,
                                lFieldGroupData.getLabelKey(),
                                lMLFstFData.getLabelKey());

                MultipleLineFieldComparatorData lMultipleLineFieldComparator =
                        compareMultipleLineFields(lMLFstFData,
                                lMultipleLineSndFieldData);

                // Add the named of the group an multilinefield in the already
                // compared array list
                lAlreadyComparedArrayList.add(lFieldGroupData.getLabelKey()
                        + SEPARATOR + lMLFstFData.getLabelKey());

                if (lMultipleLineFieldComparator != null) {
                    lMLFCDataList.add(lMultipleLineFieldComparator);
                }

            }

            if (lMLFCDataList.size() != 0) {
                MultipleLineFieldComparatorData[] lTab =
                        createMLFCDataTab(lMLFCDataList);
                lFieldComparatorGroupData.setMultipleLineFieldComparatorDatas(lTab);
                lFieldComparatorGroupDataList.add(lFieldComparatorGroupData);
            }
        }

        // Through the snd revision groups
        for (FieldGroupData lFieldGroupData : lFieldGroupSndDataTab) {
            FieldComparatorGroupData lFieldComparatorGroupData =
                    new FieldComparatorGroupData();
            lFieldComparatorGroupData.setLabelKey(lFieldGroupData.getLabelKey());
            lFieldComparatorGroupData.setI18nName(lFieldGroupData.getI18nName());
            ArrayList<MultipleLineFieldComparatorData> lFCGDataList =
                    new ArrayList<MultipleLineFieldComparatorData>();

            for (MultipleLineFieldData lMLSndFData : lFieldGroupData.getMultipleLineFieldDatas()) {

                // Verify if the group and multilinefield have not been already
                // compared
                if (!lAlreadyComparedArrayList.contains(lFieldGroupData.getLabelKey()
                        + SEPARATOR + lMLSndFData.getLabelKey())) {

                    // Search the associated MultipleLineFieldData
                    // structure in the first revision data.
                    MultipleLineFieldData lMultipleLineFirstFieldData =
                            searchMultipleLineFieldData(pRevisionFirstData,
                                    lFieldGroupData.getLabelKey(),
                                    lMLSndFData.getLabelKey());

                    MultipleLineFieldComparatorData lMultipleLineFieldComparator =
                            compareMultipleLineFields(
                                    lMultipleLineFirstFieldData, lMLSndFData);

                    if (lMultipleLineFieldComparator != null) {
                        lFCGDataList.add(lMultipleLineFieldComparator);
                    }
                }

            }
            if (lFCGDataList.size() != 0) {
                MultipleLineFieldComparatorData[] lTab =
                        createMLFCDataTab(lFCGDataList);
                lFieldComparatorGroupData.setMultipleLineFieldComparatorDatas(lTab);
                lFieldComparatorGroupDataList.add(lFieldComparatorGroupData);
            }
        }

        return createFieldComparatorGroupDataTab(lFieldComparatorGroupDataList);
    }

    /**
     * Create a FieldComparatorGroupData tab from an array list
     * 
     * @param pList
     *            The array list
     * @return The created tab
     */
    protected static FieldComparatorGroupData[] createFieldComparatorGroupDataTab(
            ArrayList<FieldComparatorGroupData> pList) {
        FieldComparatorGroupData[] lTab =
                new FieldComparatorGroupData[pList.size()];
        for (int i = 0; i < pList.size(); i++) {
            lTab[i] = pList.get(i);
        }
        return lTab;
    }

    /**
     * Create a MultipleLineFieldComparatorData tab from an array list
     * 
     * @param pList
     *            The array list
     * @return The created tab
     */
    protected static MultipleLineFieldComparatorData[] createMLFCDataTab(
            ArrayList<MultipleLineFieldComparatorData> pList) {
        MultipleLineFieldComparatorData[] lTab =
                new MultipleLineFieldComparatorData[pList.size()];
        for (int i = 0; i < pList.size(); i++) {
            lTab[i] = pList.get(i);
        }
        return lTab;
    }

    /**
     * Create a multipleLineFieldComparatorData from a MultipleLineFieldData
     * structure.
     * 
     * @param pData
     *            The MultipleLineFieldData structure
     * @return The newly created MultipleLineFieldComparatorData structure
     */
    protected static MultipleLineFieldComparatorData createMLFieldComparatorData(
            MultipleLineFieldData pData) {
        MultipleLineFieldComparatorData lMLFCData =
                new MultipleLineFieldComparatorData();

        lMLFCData.setLabelKey(pData.getLabelKey());
        lMLFCData.setRef(pData.getRef());
        lMLFCData.setMultiLined(pData.isMultiLined());
        lMLFCData.setMultiField(pData.isMultiField());
        lMLFCData.setExportable(pData.isExportable());
        lMLFCData.setI18nName(pData.getI18nName());
        lMLFCData.setConfidential(pData.isConfidential());

        return lMLFCData;
    }

    /**
     * Create a FieldComparatorData from a FieldData structure
     * 
     * @param pData
     *            The FieldData structure
     * @return The newly created FieldComparatorData structure
     */
    protected static FieldComparatorData createFieldComparatorData(
            FieldData pData) {
        FieldComparatorData lFCData = new FieldComparatorData();

        lFCData.setLabelKey(pData.getLabelKey());
        lFCData.setConfidential(pData.isConfidential());
        lFCData.setDefaultValue(pData.getDefaultValue());
        lFCData.setDescription(pData.getDescription());
        lFCData.setMandatory(pData.isMandatory());
        lFCData.setUpdatable(pData.isUpdatable());
        lFCData.setFieldType(pData.getFieldType());
        lFCData.setDisplayType(pData.getDisplayType());
        lFCData.setExportable(pData.isExportable());
        lFCData.setI18nName(pData.getI18nName());
        lFCData.setFileValue(pData.getFileValue());
        lFCData.setTextAreaSize(pData.getTextAreaSize());
        lFCData.setFieldAvailableValueData(pData.getFieldAvailableValueData());

        return lFCData;
    }

    /**
     * Compare two MultipleLineFieldData structure (return null if the
     * MultipleLineFieldDara are the same, or a MultipleLineFieldComparatorData
     * if not)
     * 
     * @param pMultipleLineFirstFieldData
     *            The first MultipleLineFieldData structure to be compared
     * @param pMultipleLineSndFieldData
     *            The snd MultipleLineFieldData structure to be compared
     * @return A multipleLineFieldComparatorData structure containing the
     *         differences between the two Data, or null if the two data are the
     *         same.
     */
    protected static MultipleLineFieldComparatorData compareMultipleLineFields(
            MultipleLineFieldData pMultipleLineFirstFieldData,
            MultipleLineFieldData pMultipleLineSndFieldData) {
        MultipleLineFieldComparatorData lMultipleLineFieldComparatorData = null;

        MultipleLineFieldComparatorData lTempMultipleLineFieldComparator =
                createMLFieldComparatorData(pMultipleLineFirstFieldData);

        LineFieldData[] lLineFieldDataFirstTab =
                pMultipleLineFirstFieldData.getLineFieldDatas();
        LineFieldData[] lLineFieldDataSndTab =
                pMultipleLineSndFieldData.getLineFieldDatas();

        ArrayList<LineFieldComparatorData> lTempLFCDataList =
                new ArrayList<LineFieldComparatorData>();

        boolean lSame = true;
        int lNbLine =
                Math.max(lLineFieldDataFirstTab.length,
                        lLineFieldDataSndTab.length);

        for (int i = 0; i < lNbLine; i++) {

            FieldData[] lFieldDataTabFirst = null;
            if (i < lLineFieldDataFirstTab.length) {
                lFieldDataTabFirst = lLineFieldDataFirstTab[i].getFieldDatas();
            }
            FieldData[] lFieldDataTabSnd = null;
            if (i < lLineFieldDataSndTab.length) {
                lFieldDataTabSnd = lLineFieldDataSndTab[i].getFieldDatas();
            }

            if (lFieldDataTabFirst == null) {
                lFieldDataTabFirst = new FieldData[lFieldDataTabSnd.length];
            }
            else if (lFieldDataTabSnd == null) {
                lFieldDataTabSnd = new FieldData[lFieldDataTabFirst.length];
            }

            LineFieldComparatorData lTempLineFieldComparatorData =
                    new LineFieldComparatorData();
            lTempLineFieldComparatorData.setRef(lLineFieldDataFirstTab[i].getRef());
            lTempLFCDataList.add(lTempLineFieldComparatorData);

            ArrayList<FieldComparatorData> lTempFCList =
                    new ArrayList<FieldComparatorData>();

            boolean lSameIntern = true;
            if (lFieldDataTabFirst.length == lFieldDataTabSnd.length) {
                for (int j = 0; j < lFieldDataTabFirst.length; j++) {
                    FieldData lFieldDataFirst = lFieldDataTabFirst[j];
                    FieldData lFieldDataSnd = lFieldDataTabSnd[j];

                    if (lFieldDataFirst == null || lFieldDataSnd == null) {
                        lSameIntern = false;
                    }
                    else if (lFieldDataFirst.getFieldType().equals("FILE")) {
                        lSameIntern =
                                (lFieldDataFirst.getFileValue() == null && lFieldDataSnd.getFileValue() == null)
                                        || StringUtils.equals(
                                                lFieldDataFirst.getFileValue().getName(),
                                                lFieldDataSnd.getFileValue().getName());
                    }
                    else {
                        lSameIntern =
                                (lFieldDataFirst.getValues() == null && lFieldDataSnd.getValues() == null)
                                        || Arrays.equals(
                                                lFieldDataFirst.getValues().getValues(),
                                                lFieldDataSnd.getValues().getValues());
                    }

                    if (!lSameIntern) {
                        lSame = false;

                        FieldComparatorData lTempFieldComparatorData = null;
                        if (lFieldDataFirst == null) {
                            lTempFieldComparatorData =
                                    createFieldComparatorData(lFieldDataSnd);
                        }
                        else {
                            lTempFieldComparatorData =
                                    createFieldComparatorData(lFieldDataFirst);
                        }

                        FieldValueData[] lFieldValueData =
                                new FieldValueData[2];

                        if (lFieldDataFirst != null
                                && lFieldDataFirst.getValues() != null) {
                            lFieldValueData[0] = lFieldDataFirst.getValues();
                        }
                        else if (lFieldDataFirst != null
                                && lFieldDataFirst.getFileValue() != null) {
                            lFieldValueData[0] =
                                    new FieldValueData(
                                            new String[] { lFieldDataFirst.getFileValue().getName() });
                        }

                        if (lFieldDataSnd != null
                                && lFieldDataSnd.getValues() != null) {
                            lFieldValueData[1] = lFieldDataSnd.getValues();
                        }
                        else if (lFieldDataSnd != null
                                && lFieldDataSnd.getFileValue() != null) {
                            lFieldValueData[1] =
                                    new FieldValueData(
                                            new String[] { lFieldDataSnd.getFileValue().getName() });
                        }

                        lTempFieldComparatorData.setComparatorValues(lFieldValueData);

                        lTempFCList.add(lTempFieldComparatorData);
                    }
                }
            }

            if (!lSame) {
                FieldComparatorData[] lTab =
                        createFieldComparatorDataTab(lTempFCList);
                lTempLineFieldComparatorData.setFieldComparatorDatas(lTab);

                LineFieldComparatorData[] lLFCDataTab =
                        createLineFieldComparatorDataTab(lTempLFCDataList);
                lTempMultipleLineFieldComparator.setLineFieldComparatorDatas(lLFCDataTab);

                lMultipleLineFieldComparatorData =
                        lTempMultipleLineFieldComparator;
            }
        }

        return lMultipleLineFieldComparatorData;
    }

    /**
     * Create a FieldComparatorData tab from an array list of this type
     * 
     * @param pList
     *            The array list
     * @return The created tab
     */
    protected static FieldComparatorData[] createFieldComparatorDataTab(
            ArrayList<FieldComparatorData> pList) {
        FieldComparatorData[] lFieldComparatorDataTab =
                new FieldComparatorData[pList.size()];
        for (int i = 0; i < pList.size(); i++) {
            lFieldComparatorDataTab[i] = pList.get(i);
        }
        return lFieldComparatorDataTab;
    }

    /**
     * Create a LineFieldComparatorData tab from an array list of this type
     * 
     * @param pList
     *            The array list
     * @return The created tab
     */
    protected static LineFieldComparatorData[] createLineFieldComparatorDataTab(
            ArrayList<LineFieldComparatorData> pList) {
        LineFieldComparatorData[] lLineFieldComparatorDataTab =
                new LineFieldComparatorData[pList.size()];
        for (int i = 0; i < pList.size(); i++) {
            lLineFieldComparatorDataTab[i] = pList.get(i);
        }
        return lLineFieldComparatorDataTab;
    }

    /**
     * Search in the pRevisionData structure the multipleLineFieldData structure
     * in the fieldGroupData named pGroupDataLabelKey and which name is
     * pMultipleLineLabelKey.
     * 
     * @param pRevisionData
     *            The revision data structure
     * @param pGroupDataLabelKey
     *            The label key of the group data
     * @param pMultipleLineLabelKey
     *            The label key of the searched multipleLineFieldData
     * @return The found MultipleLineFieldData structure or null if not found.
     */
    protected static MultipleLineFieldData searchMultipleLineFieldData(
            RevisionData pRevisionData, String pGroupDataLabelKey,
            String pMultipleLineLabelKey) {
        MultipleLineFieldData lMultipleLineFieldData = null;

        FieldGroupData lFieldGroupData =
                searchFieldGroupData(pRevisionData, pGroupDataLabelKey);

        if (lFieldGroupData != null) {
            MultipleLineFieldData[] lMultipleLineFieldDataTab =
                    lFieldGroupData.getMultipleLineFieldDatas();

            int i = 0;
            boolean lFound = false;

            while (!lFound && i < lMultipleLineFieldDataTab.length) {
                lMultipleLineFieldData = lMultipleLineFieldDataTab[i];
                if (lMultipleLineFieldData.getLabelKey().compareTo(
                        pMultipleLineLabelKey) == 0) {
                    lFound = true;
                }
                else {
                    i++;
                }
            }

            if (!lFound) {
                lMultipleLineFieldData = null;
            }
        }

        return lMultipleLineFieldData;
    }

    /**
     * Search in the revision data structure the fieldgroupdata structure named
     * pGroupDataLeblKey
     * 
     * @param pRevisionData
     *            The revision data structure
     * @param pGroupDataLabelKey
     *            The label key of the group data
     * @return The found FieldGroupData struture or null if not found
     */
    protected static FieldGroupData searchFieldGroupData(
            RevisionData pRevisionData, String pGroupDataLabelKey) {
        FieldGroupData[] lFieldGroupDataTab =
                pRevisionData.getFieldGroupDatas();
        int i = 0;
        boolean lFound = false;

        FieldGroupData lFieldGroupData = null;

        while (!lFound && i < lFieldGroupDataTab.length) {
            lFieldGroupData = lFieldGroupDataTab[i];
            if (lFieldGroupData.getLabelKey().compareTo(pGroupDataLabelKey) == 0) {
                lFound = true;
            }
            else {
                i++;
            }
        }

        if (!lFound) {
            lFieldGroupData = null;
        }

        return lFieldGroupData;

    }

    /**
     * Create the lineFielComparatorData containing the reference differences
     * between the two revisions data.
     * 
     * @param pRevisionFirstData
     *            First revision data to be compared
     * @param pRevisionSndData
     *            Snd revision data to be compared
     * @return LineFieldComparator structure
     */
    protected static LineFieldComparatorData createLineFieldComparatorData(
            RevisionData pRevisionFirstData, RevisionData pRevisionSndData) {
        LineFieldComparatorData lLineFieldComparatorData = null;

        boolean lSame;

        LineFieldData lLineFirstFieldData = pRevisionFirstData.getReference();

        FieldData[] lFieldDataTabFirst =
                pRevisionFirstData.getReference().getFieldDatas();
        FieldData[] lFieldDataTabSnd =
                pRevisionSndData.getReference().getFieldDatas();

        LineFieldComparatorData lTempLineFieldComparatorData =
                new LineFieldComparatorData();
        lTempLineFieldComparatorData.setRef(lLineFirstFieldData.getRef());

        ArrayList<FieldComparatorData> lTempFCList =
                new ArrayList<FieldComparatorData>();

        if (lFieldDataTabFirst.length == lFieldDataTabSnd.length) {
            for (int j = 0; j < lFieldDataTabFirst.length; j++) {
                FieldData lFieldDataFirst = lFieldDataTabFirst[j];
                FieldData lFieldDataSnd = lFieldDataTabSnd[j];

                lSame =
                        Arrays.equals(lFieldDataFirst.getValues().getValues(),
                                lFieldDataSnd.getValues().getValues());

                if (!lSame) {
                    FieldComparatorData lTempFieldComparatorData =
                            createFieldComparatorData(lFieldDataFirst);

                    FieldValueData[] lFieldValueData = new FieldValueData[2];
                    lFieldValueData[0] = lFieldDataFirst.getValues();
                    lFieldValueData[1] = lFieldDataSnd.getValues();

                    lTempFieldComparatorData.setComparatorValues(lFieldValueData);

                    lTempFCList.add(lTempFieldComparatorData);
                }
            }
        }

        if (lTempFCList.size() != 0) {
            FieldComparatorData[] lTab =
                    createFieldComparatorDataTab(lTempFCList);
            lTempLineFieldComparatorData.setFieldComparatorDatas(lTab);
            lLineFieldComparatorData = lTempLineFieldComparatorData;
        }

        return lLineFieldComparatorData;
    }
}

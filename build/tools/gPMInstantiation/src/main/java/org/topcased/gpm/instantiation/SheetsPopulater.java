/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.FieldAvailableValueData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.transformation.service.DataTransformationService;
import org.topcased.gpm.domain.fields.ChoiceFieldType;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * This class allows to populate the database automatically That mean's to
 * create generic sheets
 * 
 * @author ogehin
 */
public class SheetsPopulater {

    private String roleToken = null;

    private final Random random;

    private static final int MAX_NUMBER_OF_MULTIFIELDS = 5;

    private static final String SRC_STRING =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYZ\n           ";

    private static final int MAX_LENGTH_OF_STRING = 400;

    private static final int MAX_LENGTH_OF_FILENAME = 30;

    private static final int NB_BYTE = 800;

    /**
     * Default constructor
     * 
     * @param pRoleToken
     *            The RoleToken to access to different services
     */
    public SheetsPopulater(String pRoleToken) {
        roleToken = pRoleToken;
        random = new Random();
    }

    /**
     * Allow to create a new Sheet for a product and a processName with a
     * expected SheetType
     * 
     * @param pSheetServ
     *            The SheetService which allow to create and to access the
     *            Sheets
     * @param pTransService
     *            the Data Transformation service
     * @param pProcessName
     *            The name of the process
     * @param pTypeName
     *            The name of the SheetType for the new Sheet
     * @param pProductName
     *            The name of the product for which we create a Sheet
     * @param pNumber
     *            The number of Sheets to create
     */
    public void populate(SheetService pSheetServ,
            DataTransformationService pTransService, String pProcessName,
            String pTypeName, String pProductName, int pNumber) {

        CacheableSheetType lType =
                pSheetServ.getCacheableSheetTypeByName(roleToken, pProcessName,
                        pTypeName, CacheProperties.IMMUTABLE);
        if (lType == null) {
            error("The SheetType " + pTypeName + " doesn't exist");
        }
        CacheableSheet lSheet =
                pSheetServ.getCacheableSheetModel(roleToken, lType,
                        pProductName, null);
        if (lSheet == null) {
            error("The Product " + pProductName + " doesn't exist");
        }

        for (int lBn = 0; lBn < pNumber; lBn++) {
            CacheableSheet lBufferSheet = CopyUtils.deepClone(lSheet);

            // We obtain all Fields (like MultipleLineField) of the sheet selected
            Collection<MultipleLineFieldData> lAllMultipleLineFieldData =
                    new ArrayList<MultipleLineFieldData>();

            for (FieldGroupData lFieldGroupData : pTransService.getFieldGroupData(
                    roleToken, lBufferSheet)) {
                for (int i = 0; i < lFieldGroupData.getMultipleLineFieldDatas().length; i++) {
                    lAllMultipleLineFieldData.add(lFieldGroupData.getMultipleLineFieldDatas()[i]);
                }
            }

            //For each Field, we populate this with random data
            for (Object lMultiLineFieldData : lAllMultipleLineFieldData) {
                if (((MultipleLineFieldData) lMultiLineFieldData).isMultiLined()) {
                    //MultiLined

                    //We create a new LineFieldData[] with a random length >=1
                    int lNbLineFieldData =
                            1 + random.nextInt(MAX_NUMBER_OF_MULTIFIELDS);
                    LineFieldData[] lLineFieldDatas =
                            new LineFieldData[lNbLineFieldData];

                    //We full the LineFieldData[] with random data
                    for (int i = 0; i < lNbLineFieldData; i++) {
                        lLineFieldDatas[i] =
                                new LineFieldData(
                                        ((MultipleLineFieldData) lMultiLineFieldData).getLineFieldDatas()[0]);
                        FieldData[] lFieldDatas =
                                lLineFieldDatas[i].getFieldDatas().clone();

                        populateListFieldData(lFieldDatas);
                        lLineFieldDatas[i].setFieldDatas(lFieldDatas);
                    }
                    // We set the new LineFieldData[] for the current MultipleLineFieldData
                    ((MultipleLineFieldData) lMultiLineFieldData).setLineFieldDatas(lLineFieldDatas);
                }
                else { //MonoLined
                    //We get the only LineFieldData
                    LineFieldData lLineFieldData =
                            ((MultipleLineFieldData) lMultiLineFieldData).getLineFieldDatas()[0];
                    FieldData[] lFieldDatas = lLineFieldData.getFieldDatas();
                    populateListFieldData(lFieldDatas);
                    lLineFieldData.setFieldDatas(lFieldDatas);
                }
            }

            //Finally, we create the Sheet from the correctly filled SheetData
            pSheetServ.createSheet(roleToken, lBufferSheet, null);
        }
    }

    /**
     * Allow to populate a List of FieldDatas, with new instantiation of
     * FieldData from existing.
     * 
     * @param pFieldDatas
     *            the list to populate
     */
    public void populateListFieldData(FieldData[] pFieldDatas) {
        // notice: if (!isMultiField) lFielDatas contains just one element
        for (int i = 0; i < pFieldDatas.length; i++) {
            FieldData lFieldData = pFieldDatas[i];
            lFieldData = new FieldData(lFieldData);

            if (lFieldData.getFieldType().equals("FILE")) {
                AttachedFieldModificationData lFileValue =
                        new AttachedFieldModificationData();
                populateFileValue(lFileValue);
                lFieldData.setFileValue(lFileValue);
            }
            else {
                FieldValueData lFieldValueData = new FieldValueData();
                String[] lValues = populateFieldData(lFieldData);
                lFieldValueData.setValues(lValues);
                lFieldData.setValues(lFieldValueData);
            }
            pFieldDatas[i] = lFieldData;
        }
    }

    /**
     * Allow to populate a FieldData.
     * 
     * @param pFieldData
     *            The field to populate.
     * @return String[] The new random values populated for the FieldData
     */
    public String[] populateFieldData(FieldData pFieldData) {
        String lLFDataType = pFieldData.getFieldType();
        String[] lValues = new String[1];
        if (lLFDataType.equals(ChoiceFieldType.CHOICE_SINGLE.toString())) {
            FieldAvailableValueData lAvailableData =
                    pFieldData.getFieldAvailableValueData();
            if (null != lAvailableData.getValues()) {
                int lIndexValue =
                        random.nextInt(lAvailableData.getValues().length);
                lValues[0] = lAvailableData.getValues()[lIndexValue];
            }
            else {
                return null;
            }
        }
        else if (lLFDataType.equals(ChoiceFieldType.CHOICE_MULTIPLE.toString())) {
            //Particular case: lValues can have different values.
            //We are sure to have multiplicity==true
            FieldAvailableValueData lAvailableData =
                    pFieldData.getFieldAvailableValueData();

            String[] lAvailableValues = lAvailableData.getValues();
            lValues =
                    new String[1 + random.nextInt(lAvailableValues.length - 1)];

            ArrayList<String> lChoisedValue = new ArrayList<String>();
            for (int i = 0; i < lValues.length; i++) {
                int lIndexValue = random.nextInt(lAvailableValues.length);
                while (lChoisedValue.contains(lAvailableValues[lIndexValue])) {
                    lIndexValue = random.nextInt(lAvailableValues.length);
                }
                lValues[i] = lAvailableValues[lIndexValue];
                lChoisedValue.add(lAvailableValues[lIndexValue]);
            }
        }
        else if (lLFDataType.equals(FieldType.BOOLEAN.toString())) {
            boolean lBooleanValue = (random.nextInt() == 0 % 2);
            if (lBooleanValue) {
                lValues[0] = "true";
            }
            else {
                lValues[0] = "false";
            }
        }

        else if (lLFDataType.equals(FieldType.DATE.toString())) {
            Calendar lCalendar = Calendar.getInstance();
            lCalendar.set(lCalendar.get(Calendar.YEAR),
                    random.nextInt(lCalendar.getMaximum(Calendar.MONTH)),
                    random.nextInt(lCalendar.getMaximum(Calendar.DAY_OF_MONTH)));
            lValues[0] =
                    lCalendar.get(Calendar.YEAR) + "-"
                            + (lCalendar.get(Calendar.MONTH) + 1) + "-"
                            + lCalendar.get(Calendar.DAY_OF_MONTH);
        }
        else if (lLFDataType.equals(FieldType.INTEGER.toString())) {
            lValues[0] = "" + random.nextInt();
        }
        else if (lLFDataType.equals(FieldType.REAL.toString())) {
            lValues[0] = "" + random.nextFloat();
        }
        else if (lLFDataType.equals(FieldType.STRING.toString())) {
            if (pFieldData.getMaxSize() < 0) {
                lValues[0] = generateString(MAX_LENGTH_OF_STRING);
            }
            else {
                lValues[0] = generateString(pFieldData.getMaxSize());
            }
        }
        else {
            error("The type considered : " + lLFDataType
                    + " isn't taken into account !!");
        }
        return lValues;
    }

    /**
     * Allow to populate a FieldData which are from type FILE
     * 
     * @param pFileValue
     *            The FileValue to set.
     */
    public void populateFileValue(AttachedFieldModificationData pFileValue) {
        pFileValue.setName(generateString(MAX_LENGTH_OF_FILENAME));
        byte[] lByteContent = new byte[NB_BYTE];
        random.nextBytes(lByteContent);
        pFileValue.setContent(lByteContent);
    }

    /**
     * Allow to generate a random String:
     * <ul>
     * <li>with a length between 1 and pLength + 1
     * <li>with the char taken from the SRC_STRING
     * </ul>
     * 
     * @param pLength
     *            The maximum length of the generated String - 1
     * @return A random String
     */
    private String generateString(int pLength) {
        StringBuffer lStringBuffer = new StringBuffer();
        int lTaille = random.nextInt(pLength) + 1;
        int lIndex;
        for (int i = 0; i < lTaille; i++) {
            lIndex = random.nextInt(SRC_STRING.length());
            lStringBuffer.append(SRC_STRING.charAt(lIndex));
        }
        return new String(lStringBuffer);
    }

    /**
     * Reports an error.
     * 
     * @param pMsg
     *            Error message
     */
    private static void error(String pMsg) {
        System.err.println("Error: " + pMsg);
        System.exit(1);
    }
}

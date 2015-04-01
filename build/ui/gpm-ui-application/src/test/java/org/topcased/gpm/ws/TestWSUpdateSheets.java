/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.io.InputStream;
import java.util.List;

import org.topcased.gpm.ws.v2.client.AttachedFieldValueData;
import org.topcased.gpm.ws.v2.client.FieldValueData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.ProcessInformation;
import org.topcased.gpm.ws.v2.client.SheetData;
import org.topcased.gpm.ws.v2.client.SheetSummaryData;

/**
 * Test updateSheets.
 * 
 * @author ogehin
 */
public class TestWSUpdateSheets extends AbstractWSTestCase {

    private static final String DEFAULT_FILTER = "SHEETTYPE1";

    private static final String PRODUCT_NAME = "Happy Mouse";

    private static final String DEFAULT_TRANSITION = "Validate";

    private static final String UPDATED_STATE = "Open";

    /**
     * Test the createSheet method in normal conditions.
     * 
     * @throws Exception
     *             A nException
     */
    public void testCreateDeleteSheetNormalCase() throws Exception {
        SheetData lSheetData = new SheetData();
        FieldValueData lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_ref");
        lFieldValueData.setValue("Reference");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleDate");
        lFieldValueData.setValue("2007-12-25");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleInteger");
        lFieldValueData.setValue("13");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleReal");
        lFieldValueData.setValue("13.1");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleBoolean");
        lFieldValueData.setValue("true");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleString");
        lFieldValueData.setValue("simple string");
        lSheetData.getFieldValues().add(lFieldValueData);
        AttachedFieldValueData lAttachedFieldValueData =
                new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attached");
        InputStream lFileInputStream =
                (InputStream) this.getClass().getClassLoader().getResourceAsStream(
                        "attachedFile.jpg");
        byte[] lContent = new byte[1000];
        lFileInputStream.read(lContent);
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test.jpg");
        lSheetData.getFieldValues().add(lAttachedFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_choice");
        lFieldValueData.setValue("RED");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_urlField");
        lFieldValueData.setValue("http://www.google.fr");
        lSheetData.getFieldValues().add(lFieldValueData);

        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleDateMul");
        lFieldValueData.setValue("2007-12-25");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleDateMul");
        lFieldValueData.setValue("2007-11-09");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleIntegerMul");
        lFieldValueData.setValue("13");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleIntegerMul");
        lFieldValueData.setValue("14");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleRealMul");
        lFieldValueData.setValue("13.1");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleRealMul");
        lFieldValueData.setValue("13.2");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleBooleanMul");
        lFieldValueData.setValue("true");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleBooleanMul");
        lFieldValueData.setValue("false");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleStringMul");
        lFieldValueData.setValue("simple string 1");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_simpleStringMul");
        lFieldValueData.setValue("simple string 2");
        lSheetData.getFieldValues().add(lFieldValueData);
        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedMul");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test1.jpg");
        lSheetData.getFieldValues().add(lAttachedFieldValueData);
        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedMul");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test2.jpg");
        lSheetData.getFieldValues().add(lAttachedFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_choiceMul");
        lFieldValueData.setValue("RED");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_choiceMul");
        lFieldValueData.setValue("BLACK");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_urlFieldMul");
        lFieldValueData.setValue("http://www.google.fr");
        lSheetData.getFieldValues().add(lFieldValueData);
        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_urlFieldMul");
        lFieldValueData.setValue("http://www.yahoo.fr");
        lSheetData.getFieldValues().add(lFieldValueData);

        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_multiple1");
        FieldValueData lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleDateM");
        lSubFieldValueData.setValue("2007-12-25");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleIntegerM");
        lSubFieldValueData.setValue("13");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleRealM");
        lSubFieldValueData.setValue("13.1");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleBooleanM");
        lSubFieldValueData.setValue("true");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleStringM");
        lSubFieldValueData.setValue("simple string");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedM");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test.jpg");
        lFieldValueData.getFieldValues().add(lAttachedFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_choiceM");
        lSubFieldValueData.setValue("RED");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_urlFieldM");
        lSubFieldValueData.setValue("http://www.google.fr");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSheetData.getFieldValues().add(lFieldValueData);

        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_multiple1M");
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleDateMM");
        lSubFieldValueData.setValue("2007-12-25");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleIntegerMM");
        lSubFieldValueData.setValue("13");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleRealMM");
        lSubFieldValueData.setValue("13.1");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleBooleanMM");
        lSubFieldValueData.setValue("true");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleStringMM");
        lSubFieldValueData.setValue("simple string 1");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedMM");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test1.jpg");
        lFieldValueData.getFieldValues().add(lAttachedFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_choiceMM");
        lSubFieldValueData.setValue("RED");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_urlFieldMM");
        lSubFieldValueData.setValue("http://www.google.fr");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSheetData.getFieldValues().add(lFieldValueData);

        lFieldValueData = new FieldValueData();
        lFieldValueData.setName("SHEETTYPE1_multiple1M");
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleDateMM");
        lSubFieldValueData.setValue("2007-11-09");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleIntegerMM");
        lSubFieldValueData.setValue("14");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleRealMM");
        lSubFieldValueData.setValue("14.2");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleBooleanMM");
        lSubFieldValueData.setValue("false");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_simpleStringMM");
        lSubFieldValueData.setValue("simple string 2");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedMM");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test2.jpg");
        lFieldValueData.getFieldValues().add(lAttachedFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_choiceMM");
        lSubFieldValueData.setValue("BLACK");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSubFieldValueData = new FieldValueData();
        lSubFieldValueData.setName("SHEETTYPE1_urlFieldMM");
        lSubFieldValueData.setValue("http://www.yahoo.fr");
        lFieldValueData.getFieldValues().add(lSubFieldValueData);
        lSheetData.getFieldValues().add(lFieldValueData);

        lSheetData.setType("SheetType1");

        String lSheetId =
                staticServices.createSheet(roleToken, DEFAULT_PROCESS_NAME,
                        DEFAULT_PRODUCT_NAME, lSheetData);
        assertNotNull("The createSheet method returns a null id", lSheetId);

        //Retriving the created sheet.
        SheetData lCreatedSheetData =
                staticServices.getSheetsByKeys(roleToken,
                        generateList(lSheetId, null)).get(0);
        assertNotNull("Sheet #" + lSheetId + " hasn't been created.",
                lCreatedSheetData);
        // TODO add precreate extension point in order to initialize sheet reference.
        //            assertEquals("The created sheet has not the expected reference.",
        //                    lSheetData.getReference(), lCreatedSheetData.getReference());
        assertEquals("The created sheet has not the expected type.",
                lSheetData.getType(), lCreatedSheetData.getType());
        assertEquals("The created sheet has not the expected product.",
                DEFAULT_PRODUCT_NAME, lCreatedSheetData.getProductName());
        assertEquals("The created sheet has not the expected version.",
                new Integer(0), lCreatedSheetData.getVersion());

        lAttachedFieldValueData = new AttachedFieldValueData();
        lAttachedFieldValueData.setName("SHEETTYPE1_attachedMul");
        lAttachedFieldValueData.setNewContent(lContent);
        lAttachedFieldValueData.setMimeType("image/jpeg");
        lAttachedFieldValueData.setFilename("test2.jpg");
        lCreatedSheetData.getFieldValues().add(lAttachedFieldValueData);

        staticServices.deleteSheets(roleToken, generateList(lSheetId, null));

        boolean lSheetExistence =
                staticServices.isValuesContainerExists(lSheetId);
        assertFalse("The method deleteSheets doesn't supress expected sheet.",
                lSheetExistence);
    }

    /**
     * Test the updateSheet method in normal conditions.
     */
    public void testUpdateSheetNormalCase() {
        try {
            List<SheetSummaryData> lSheetSummaryDatas =
                    staticServices.executeSheetFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], DEFAULT_FILTER);
            String lSheetKey = lSheetSummaryDatas.get(0).getId();
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);
            List<SheetData> lSheetDatas =
                    staticServices.getSheetsByKeys(roleToken, generateList(
                            lSheetKey, null));
            SheetData lSheetData = lSheetDatas.get(0);
            assertNotNull("An error occcured when retriving a sheet data",
                    lSheetData);
            lSheetData.setReference("temporary");

            //TODO complete this test case with another changes.
            staticServices.updateSheets(roleToken, DEFAULT_PROCESS_NAME,
                    lSheetDatas);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the changeState in normal conditions.
     */
    public void testChangeStateNormalCase() {
        try {
            SheetData lSheetData;
            lSheetData =
                    staticServices.getSheetByRef(roleToken,
                            DEFAULT_PROCESS_NAME, PRODUCT_NAME, "Garfield");

            String lSheetKey = lSheetData.getId();

            staticServices.changeState(roleToken, lSheetKey, DEFAULT_TRANSITION);

            List<ProcessInformation> lProcessInfos =
                    staticServices.getSheetProcessInformations(roleToken,
                            generateList(lSheetKey, null));
            assertEquals("The changeState method failed in normal conditions",
                    lProcessInfos.get(0).getCurrentState(), UPDATED_STATE);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }
}
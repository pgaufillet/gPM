/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Cyril Marchive (Atos)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.topcased.gpm.business.exception.ExtensionException;
import org.topcased.gpm.ws.v2.client.ExtensionsExecutionFlag;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.ImportException_Exception;
import org.topcased.gpm.ws.v2.client.ImportExecutionReport;
import org.topcased.gpm.ws.v2.client.ImportFlag;
import org.topcased.gpm.ws.v2.client.ImportProperties;
import org.topcased.gpm.ws.v2.client.SheetData;

/**
 * Test import sheet.
 * 
 * @author cmarchive
 */
public class TestWSImportSheet extends AbstractWSTestCase {

    private static ImportProperties PROPS = null;

    private static List<String> sheetIdList = new ArrayList<String>();

    static {
        PROPS = new ImportProperties();
        PROPS.setCategoriesFlag(ImportFlag.SKIP);
        PROPS.setEnvironmentsFlag(ImportFlag.SKIP);
        PROPS.setFiltersFlag(ImportFlag.SKIP);
        PROPS.setProductsFlag(ImportFlag.SKIP);
        PROPS.setProductLinksFlag(ImportFlag.SKIP);
        PROPS.setUserRolesFlag(ImportFlag.SKIP);
        PROPS.setUsersFlag(ImportFlag.SKIP);
        PROPS.setSheetsFlag(ImportFlag.CREATE_OR_UPDATE);
        PROPS.setSheetLinksFlag(ImportFlag.SKIP);
        PROPS.setExtensionsExecutionFlag(ExtensionsExecutionFlag.EXECUTE_LIST);
        PROPS.setIgnoreVersion(true);
        PROPS.setImportFileContent(false);
    }

    /**
     * Test import Sheet in normal condition (creation sheet)
     */
    public void testImportSheetInNormalCase() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport1.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport1.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            ImportExecutionReport lReport =
                    staticServices.importData(roleToken, lBytes, PROPS);
            sheetIdList.addAll(lReport.getSheetIdList());

            List<SheetData> lSheetDataList =
                    staticServices.getSheetsByKeys(roleToken, sheetIdList);

            assertEquals("E5", lSheetDataList.get(0).getState());
            assertEquals("E4", lSheetDataList.get(1).getState());
            assertEquals("E3", lSheetDataList.get(2).getState());
            // Test OK
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet. An origin state is defined in rules but this state
     * does not correspond to the start sheet node.
     */
    public void testImportSheetOriginStateError() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport2.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport2.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
            fail("Test KO");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            // Test OK
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet. An final state is defined in rules but this state does
     * not correspond to the final sheet node.
     */
    public void testImportSheetFinalStateError() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport3.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport3.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
            fail("Test KO");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            // Test OK
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet. A rule is created containing not contiguous
     * transitions.
     */
    public void testImportSheetTransitionsNotContiguous() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport4.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport4.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
            fail("Test KO");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            // Test OK
        }
    }

    /**
     * Test import Sheet with a bad transition.
     */
    public void testImportSheetWithBadTransition() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport5.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport5.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
            fail("Test KO");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            // Test OK
        }
    }

    /**
     * Test import Sheet with a bad state.
     */
    public void testImportSheetWithBadState() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport6.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport6.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet with global rules for each sheet type and intern rules.
     */
    public void testImportSheetGlobalAndInternRules() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport7.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport7.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            ImportExecutionReport lReport =
                    staticServices.importData(roleToken, lBytes, PROPS);

            sheetIdList = new ArrayList<String>();
            sheetIdList.addAll(lReport.getSheetIdList());

            List<SheetData> lSheetDataList =
                    staticServices.getSheetsByKeys(roleToken, sheetIdList);

            assertEquals("E5", lSheetDataList.get(0).getState());
            assertEquals("E4", lSheetDataList.get(1).getState());
            assertEquals("E3", lSheetDataList.get(2).getState());
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ExtensionException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet with only global rules for each sheet type.
     */
    public void testImportSheetGlobalRules() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport8.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport8.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            ImportExecutionReport lReport =
                    staticServices.importData(roleToken, lBytes, PROPS);

            sheetIdList = new ArrayList<String>();
            sheetIdList.addAll(lReport.getSheetIdList());

            List<SheetData> lSheetDataList =
                    staticServices.getSheetsByKeys(roleToken, sheetIdList);

            assertEquals("E5", lSheetDataList.get(0).getState());
            assertEquals("E4", lSheetDataList.get(1).getState());
            assertEquals("E3", lSheetDataList.get(2).getState());
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet with extension point to exclude for each sheet.
     */
    public void testImportSheetWithExtensionPointToExclude() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport9.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport9.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet with no extension point to exclude for one sheet.
     * Others sheet exclude the extension point. The extension point throw an
     * error; so if one sheet does not exclude this extension point, the United
     * Test make an error in normal case.
     */
    public void testImportSheetWithNoExtensionPointToExclude() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport10.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport10.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, lBytes, PROPS);
            fail("Erreur");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            // Test ok
        }
        catch (ImportException_Exception ex) {
            // Test Ok
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet with intern rules for each sheet (modification sheet)
     */
    public void testImportSheetForModificationWithInternRules() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport11.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport11.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, addSheetIdInXml(lBytes), PROPS);
            // Test ok
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet in normal condition (modification sheet) modifying
     * state for each sheet.
     */
    public void testImportSheetForModificationInNormalCase() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport12.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport12.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            ImportExecutionReport lReport =
                    staticServices.importData(roleToken,
                            addSheetIdInXml(lBytes), PROPS);

            sheetIdList = new ArrayList<String>();
            sheetIdList.addAll(lReport.getSheetIdList());

            // Test ok
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    /**
     * Test import Sheet containing a role containing more than one path. An
     * error will occur.
     */
    public void testImportSheetWithRuleContainingMoreThanOnePath() {
        FileInputStream lFis = null;
        try {
            lFis = new FileInputStream("src/test/resources/testWSImport13.xml");
            int lSize =
                    (int) new File("src/test/resources/testWSImport13.xml").length();
            byte[] lBytes = new byte[lSize];
            lFis.read(lBytes);
            staticServices.importData(roleToken, addSheetIdInXml(lBytes), PROPS);
            fail("Erreur");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
            // Test Ok
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
        catch (SOAPFaultException ex) {
            ex.printStackTrace();
            fail("Erreur");
        }
    }

    private byte[] addSheetIdInXml(byte[] pInput) {
        String lRetourStr = "";
        String lInputStr = "";
        for (byte lByte : pInput) {
            lInputStr += (char) lByte;
        }

        int lCount = 0;
        int lSheetIdCount = 0;
        int lIndex = -1;
        while ((lIndex = lInputStr.indexOf("<sheet ", lCount)) != -1) {
            if (lCount == 0) {
                lRetourStr +=
                        lInputStr.substring(0, lIndex + 7) + "id=\""
                                + sheetIdList.get(lSheetIdCount) + "\"";
            }
            else {
                lRetourStr +=
                        lInputStr.substring(lCount + 5, lIndex + 7) + "id=\""
                                + sheetIdList.get(lSheetIdCount) + "\"";
            }
            lSheetIdCount++;
            lCount = lIndex + 1;
        }
        lRetourStr += lInputStr.substring(lCount + 5);

        return lRetourStr.getBytes();
    }

}

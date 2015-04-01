/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ***************************************************************/
package org.topcased.gpm.business.ehcache;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.xml.ws.WebServiceException;

import org.topcased.gpm.ws.v2.client.ExtensionsExecutionFlag;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.ImportException_Exception;
import org.topcased.gpm.ws.v2.client.ImportFlag;
import org.topcased.gpm.ws.v2.client.ImportProperties;
import org.topcased.gpm.ws.v2.client.SheetData;
import org.topcased.gpm.ws.v2.client.SheetExportFormat;
import org.topcased.gpm.ws.v2.client.WebServicesStub;

/**
 * Test the EHCACHE distributed configuration.
 * These tests shall be run both in "Peer to peer" topology mode,
 * and in "Centralized server" star topology mode.
 * The standard "Pet Store" Test environment shall be set up before
 * running these tests.
 * 
 * 7 gPM webService servers shall be set up on 4 different machines.
 * All servers excluding server 4 belong to the same EHCache configuration
 * 
 * Server 0: does not exist (in order to simulate a shut down server)
 * Server 1: on machine A
 * Server 2: on machine A
 * Server 3: on machine B
 * Server 4: on machine B, without a EHCache replication mecanism
 *  
 * @author Olivier JUIN
 */
public class TestEHCacheReplication {

    static final int NUMBER_OF_SERVERS = 5;
    static final int S0 = 0;
    static final int S1 = 1;
    static final int S2 = 2;
    static final int S3 = 3;
    static final int S4 = 4;

    private static final String PROPERTIES_FILENAME = "src/test/resources/ehcache_test.properties";
    private static final PrintStream OUT = System.out;
    private static final PrintStream ERR = System.err;
    
    private WebServicesStub[] stubs = new WebServicesStub[NUMBER_OF_SERVERS];

    private Map<WebServicesStub, String> usersToken = 
        new HashMap<WebServicesStub, String>();
    private Map<WebServicesStub, Map<String, String>> roleTokenMap = 
        new HashMap<WebServicesStub, Map<String, String>>();
    private Map<String, String> roleNameMap = new HashMap<String, String>();

    /**
     * Class entry point. This test is supposed to be run from within eclipse,
     * as a standard java application. One argument must be provided to the command line.
     * 
     * @param pOptions ("Normal" | "Performance" | "Robustness" | "Resilience")
     */
    public static void main(String[] pOptions) {
        try {
            if (pOptions.length != 1) {
                usage();
            }
            else if (pOptions[0].toLowerCase().equals("normal")) {
                new TestEHCacheReplication().testStandard();
            }
            else if (pOptions[0].toLowerCase().equals("performance")) {
                new TestEHCacheReplication().testImportPerformance();
                new TestEHCacheReplication().testExportPerformance();
            }
            else if (pOptions[0].toLowerCase().equals("robustness")) {
                new TestEHCacheReplication().testRobustness();
            }
            else if (pOptions[0].toLowerCase().equals("resilience")) {
                new TestEHCacheReplication().testResilience();                
            }
            else {
                usage();
            }
        }
        catch (GDMException_Exception ex) {
            ex.printStackTrace();
        }
        catch (ImportException_Exception ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            OUT.println("Test completed at " + new Date());
        }
    }
    
    private static void usage() {
        ERR.println("Command line options:\n"
                + "Normal | Performance | Robustness | Resilience");
    }
    
    /**
     * Constructor.
     * 
     * @throws IOException if the properties cannot be read
     */
    public TestEHCacheReplication() throws IOException {
        Properties lProperties = new Properties();
        FileReader lPropertiesReader = new FileReader(PROPERTIES_FILENAME);
        try {
            lProperties.load(lPropertiesReader);
        }
        finally {
            lPropertiesReader.close();
        }
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            try {
            stubs[i] = new WebServicesStub(lProperties.getProperty("URL" + i));
            }
            catch (WebServiceException ex) {
                continue;
            }
        }
    }
    
    /**
     * Test scenario 1: REPLICATION - Needs servers 1, 2, 3, 0
     *   - Import Sheet1 on server 1
     *   - Wait for 3 seconds
     *   - Export Sheet1 from server 2 & 3. To initialize cache values, use getSheetByRef.
     *   - Import Sheet2 (modified Sheet1, same ID) on server 1
     *   - Wait for 3 seconds
     *   - Export Sheet2 from server 2 & 3.
     *   Servers 2 and 3 should return Sheet2.
     */
    private void testStandard() throws GDMException_Exception, ImportException_Exception {
        final int lThreeSec = 3;
        
        OUT.println("Starting standard tests");
        importSheet(stubs[S1], Data.SHEET1);
        sleep(lThreeSec);
        exportSheet(stubs[S2], Data.SHEET_REF);
        exportSheet(stubs[S3], Data.SHEET_REF);
        getSheetByRef(stubs[S2], Data.SHEET_REF);
        getSheetByRef(stubs[S3], Data.SHEET_REF);
        importSheet(stubs[S1], Data.SHEET2);
        try {
            Thread.sleep(lThreeSec);
        }
        catch (InterruptedException ex) {
            // Will not happen
        }
        
        compare(exportSheet(stubs[S2], Data.SHEET_REF), Data.SHEET2,
                "Invalid exported sheet from S2");
        compare(exportSheet(stubs[S3], Data.SHEET_REF), Data.SHEET2,
                "Invalid exported sheet from S3");
    }
    
    /**
     * Test scenario 2: PERFORMANCE 1 (import) - Needs servers 1, 2, 3
     *   - Import Sheet1 on server 3 200 times
     *   - Import Sheet1 on server 4 200 times
     *   - Display duration results
     */
    private void testImportPerformance() throws GDMException_Exception, ImportException_Exception {
        final int lLoop = 200;
        final int lMilli = 1000;

        OUT.println("Starting import performance tests");
        long lTime = System.currentTimeMillis();
        for (int i = 0; i < lLoop; i++) {
            importSheet(stubs[S3], Data.SHEET1);
        }
        OUT.println("Duration of " + lLoop + " imports on REPLICATED server: "
                + ((System.currentTimeMillis() - lTime) / lMilli) + " seconds");
        lTime = System.currentTimeMillis();
        for (int i = 0; i < lLoop; i++) {
            importSheet(stubs[S4], Data.SHEET1);
        }
        OUT.println("Duration of " + lLoop + " imports on STANDALONE server: "
                + ((System.currentTimeMillis() - lTime) / lMilli) + " seconds");
    }
    
    /**
     * Test scenario 3: PERFORMANCE 2 (export) - Needs servers 1, 2, 3, 4
     *   - While importing Sheet1 on server 1 once every 0.5 second:
     *       export Sheet1 on server 3 200 times 
     *   - While importing Sheet1 on server 1 once every 0.5 second:
     *       export Sheet1 on server 4 200 times 
     *   - Display export duration results
     */
    private void testExportPerformance() throws GDMException_Exception, ImportException_Exception {
        final int lLoop = 200;
        final int lMilli = 1000;
        final long lThreeSec = 3000;

        OUT.println("Starting export performance tests");
        Thread lImporter = new Thread("Importer") {
            @Override
            public void run() {
                final long lHalfSec = 500;
                try {
                    while (true) {
                        importSheet(stubs[S1], Data.SHEET1);
                        Thread.sleep(lHalfSec);
                    }
                }
                catch (GDMException_Exception ex) {
                    ERR.print("Import on Server 1 failed");
                    ex.printStackTrace();
                }
                catch (ImportException_Exception ex) {
                    ERR.print("Import on Server 1 failed");
                    ex.printStackTrace();
                }
                catch (InterruptedException ex) {
                    // shutdown
                }
            }
        };
        lImporter.start();
        
        try {
            Thread.sleep(lThreeSec);
        }
        catch (InterruptedException ex) {
            // Will not happen
        }
        
        try {
            long lTime = System.currentTimeMillis();
            for (int i = 0; i < lLoop; i++) {
                exportSheet(stubs[S3], Data.SHEET_REF);
            }
            OUT.println("Duration of " + lLoop + " exports on REPLICATED server: "
                    + ((System.currentTimeMillis() - lTime) / lMilli) + " seconds");
            lTime = System.currentTimeMillis();
            for (int i = 0; i < lLoop; i++) {
                exportSheet(stubs[S4], Data.SHEET_REF);
            }
            OUT.println("Duration of " + lLoop + " exports on STANDALONE server: "
                    + ((System.currentTimeMillis() - lTime) / lMilli) + " seconds");
            }
        finally {
            lImporter.interrupt();
        }
    }

    /**
     * Test scenario 4: ROBUSTNESS + SCALABILITY - Needs servers 0 to 4
     *   - Randomly import/export Sheet1, Sheet2 with 0.1 second delay
     *     for 10 minutes on servers 1, 2 & 3
     *   - Wait for 20 seconds
     *   - Play "testStandard" scenario
     *   - Wait for 20 seconds
     *   === Repeat for 12 hours ===
     */
    private void testRobustness() throws GDMException_Exception, ImportException_Exception {
        final long lTwelveHours = 12 * 60 * 60 * 1000;
        final long lTenMins = 10 * 60 * 1000;
        final long lTwentySec = 20 * 1000;
        final long lThreeSec = 3 * 1000;
        final long lSmallPause = 100;
        final int lSheetMax = 2;
        final int lServerMax = 3;
        
        OUT.println("Starting robustness tests (12 hours) at " + new Date());
        
        Thread lCycleThread = new Thread("Main cycle") {
            @Override
            public void run() {
                Thread lTenThread = null;
                try {
                    while (true) {
                        lTenThread = new Thread("10 mins cycle") {
                            @Override
                            public void run() {
                                try {
                                    Random lRand = new Random();
                                    while (true) {
                                        int lSheetNb = lRand.nextInt(lSheetMax);
                                        int lServerNb = lRand.nextInt(lServerMax) + 1;
                                        String lSheet;
                                        if (lSheetNb == 0) {
                                            lSheet = Data.SHEET1;
                                        }
                                        else {
                                            lSheet = Data.SHEET2;
                                        }
                                        importSheet(stubs[lServerNb], lSheet);
                                        Thread.sleep(lSmallPause);
                                    }
                                }
                                catch (InterruptedException ex) {
                                    // Shutdown
                                }
                                catch (GDMException_Exception ex) {
                                    ERR.println("Random import failed");
                                    ex.printStackTrace();
                                }
                                catch (ImportException_Exception ex) {
                                    ERR.println("Random import failed");
                                    ex.printStackTrace();
                                }  
                            };
                        };
                        Thread.sleep(lTenMins);
                        lTenThread.interrupt();
                        Thread.sleep(lTwentySec);

                        ////////////////////
                        // "testStandard" //
                        ////////////////////
                        
                        importSheet(stubs[S1], Data.SHEET1);
                        Thread.sleep(lThreeSec);
                        exportSheet(stubs[S2], Data.SHEET_REF);
                        exportSheet(stubs[S3], Data.SHEET_REF);
                        exportSheet(stubs[S4], Data.SHEET_REF);
                        importSheet(stubs[S1], Data.SHEET2);
                        Thread.sleep(lThreeSec);
                        compare(exportSheet(stubs[S2], Data.SHEET_REF), Data.SHEET2,
                                "Invalid exported sheet from S2");
                        compare(exportSheet(stubs[S3], Data.SHEET_REF), Data.SHEET2,
                                "Invalid exported sheet from S3");
                        compare(exportSheet(stubs[S4], Data.SHEET_REF), Data.SHEET1,
                                "Invalid exported sheet from S4");

                        Thread.sleep(lTwentySec);
                    }
                }
                catch (InterruptedException ex) {
                    // Shutdown
                    if (lTenThread != null) {
                        lTenThread.interrupt();
                    }
                }
                catch (GDMException_Exception ex) {
                    ERR.println("Standard test failed at " + new Date());
                    ex.printStackTrace();
                }
                catch (ImportException_Exception ex) {
                    ERR.println("Standard test failed at " + new Date());
                    ex.printStackTrace();
                }
            }
        };
        lCycleThread.start();
        
        try {
            Thread.sleep(lTwelveHours);
            lCycleThread.interrupt();
        }
        catch (InterruptedException e) {
            // Will not happen
        }
    }

    /**
     * Test scenario 5: RESILIENCE - Needs all servers
     *   - Randomly import Sheet1, Sheet2 for 5 minutes on server 1
     *   - During this period, shut down and re-launch Tomcat server
     *   - Wait for 20 seconds
     *   - Play "testStandard" scenario
     */
    private void testResilience() throws GDMException_Exception, ImportException_Exception {
        final long lFourMinsTwenty = 260000;
        final long lTwentySec = 20000;
        final long lThreeSec = 3000;
        final long lSmallPause = 100;
        final int lSheetMax = 2;
        final int lServerMax = 3;
        
        OUT.println("Starting resilience tests at " + new Date());
        
        Thread lTenThread = null;
        try {
            while (true) {
                lTenThread = new Thread("10 mins cycle") {
                    @Override
                    public void run() {
                        try {
                            Random lRand = new Random();
                            while (true) {
                                int lSheetNb = lRand.nextInt(lSheetMax);
                                int lServerNb = lRand.nextInt(lServerMax) + 1;
                                String lSheet;
                                if (lSheetNb == 0) {
                                    lSheet = Data.SHEET1;
                                }
                                else {
                                    lSheet = Data.SHEET2;
                                }
                                importSheet(stubs[lServerNb], lSheet);
                                Thread.sleep(lSmallPause);
                            }
                        }
                        catch (InterruptedException ex) {
                            // Shutdown
                        }
                        catch (GDMException_Exception ex) {
                            ERR.println("Random import failed");
                            ex.printStackTrace();
                        }
                        catch (ImportException_Exception ex) {
                            ERR.println("Random import failed");
                            ex.printStackTrace();
                        }  
                    };
                };
                Thread.sleep(lTwentySec);
                OUT.println("You can start deconnecting/reconnecting servers.");
                Thread.sleep(lFourMinsTwenty);
                OUT.println("You should stop deconnecting/reconnecting servers now.");
                Thread.sleep(lTwentySec);
                lTenThread.interrupt();
                Thread.sleep(lTwentySec);

                ////////////////////
                // "testStandard" //
                ////////////////////
                
                importSheet(stubs[S1], Data.SHEET1);
                Thread.sleep(lThreeSec);
                exportSheet(stubs[S2], Data.SHEET_REF);
                exportSheet(stubs[S3], Data.SHEET_REF);
                exportSheet(stubs[S4], Data.SHEET_REF);
                importSheet(stubs[S1], Data.SHEET2);
                Thread.sleep(lThreeSec);
                compare(exportSheet(stubs[S2], Data.SHEET_REF), Data.SHEET2,
                        "Invalid exported sheet from S2");
                compare(exportSheet(stubs[S3], Data.SHEET_REF), Data.SHEET2,
                        "Invalid exported sheet from S3");
                compare(exportSheet(stubs[S4], Data.SHEET_REF), Data.SHEET1,
                        "Invalid exported sheet from S4");

                Thread.sleep(lTwentySec);
            }
        }
        catch (InterruptedException ex) {
            // Shutdown
            if (lTenThread != null) {
                lTenThread.interrupt();
            }
        }
        catch (GDMException_Exception ex) {
            ERR.println("Standard test failed at " + new Date());
            ex.printStackTrace();
        }
        catch (ImportException_Exception ex) {
            ERR.println("Standard test failed at " + new Date());
            ex.printStackTrace();
        }
    }
    
    private String getRoleName(WebServicesStub pStub, String pProductName)
        throws GDMException_Exception {

        // Return the role name if already exist in map.
        if (!roleNameMap.containsKey(pProductName)) {
            // Retrieve roles list of user corresponding to the given product.
            List<String> lRoles =
                pStub.getRolesNames(getUserToken(pStub), Data.DEFAULT_PROCESS_NAME, pProductName);
    
            // Get only the first role. 
            String lRoleName = lRoles.get(0);
            roleNameMap.put(pProductName, lRoleName);
        }
        return roleNameMap.get(pProductName);
    }

    private synchronized String getRoleToken(WebServicesStub pStub, final String pProductName)
        throws GDMException_Exception {

        // Retrieve the user token.
        String lUserToken = getUserToken(pStub);

        // Return the role token if already exist in map.
        Map<String, String> lRoleMap = roleTokenMap.get(pStub);
        if (lRoleMap == null) {
            lRoleMap = new HashMap<String, String>();
            roleTokenMap.put(pStub, lRoleMap);
        }
        if (!lRoleMap.containsKey(pProductName)) {
            String lRoleToken = pStub.selectRole(lUserToken, getRoleName(pStub, pProductName),
                    pProductName, Data.DEFAULT_PROCESS_NAME);
            lRoleMap.put(pProductName, lRoleToken);
        }
        return lRoleMap.get(pProductName);
    }

    private String getUserToken(WebServicesStub pStub) throws GDMException_Exception {
        // Test if the user token exist.
        if (usersToken.get(pStub) != null) {
            return usersToken.get(pStub);
        }
        else {
            // Retrieve the token name with the login session.
            String lUserToken = pStub.login(Data.LOGIN, Data.PASSWORD);
            usersToken.put(pStub, lUserToken);
            return lUserToken;
        }
    }

    private void importSheet(WebServicesStub pStub, String pSheet)
            throws GDMException_Exception, ImportException_Exception {
        
        // Log to web-services and retrieve the role token.
        String lRoleToken = getRoleToken(pStub, Data.DEFAULT_PRODUCT);

        // Set the import properties.
        ImportProperties lProps = new ImportProperties();
        lProps.setCategoriesFlag(ImportFlag.SKIP);
        lProps.setEnvironmentsFlag(ImportFlag.SKIP);
        lProps.setFiltersFlag(ImportFlag.SKIP);
        lProps.setProductsFlag(ImportFlag.SKIP);
        lProps.setProductLinksFlag(ImportFlag.SKIP);
        lProps.setUserRolesFlag(ImportFlag.SKIP);
        lProps.setUsersFlag(ImportFlag.SKIP);
        lProps.setSheetsFlag(ImportFlag.CREATE_OR_UPDATE);
        lProps.setSheetLinksFlag(ImportFlag.SKIP);
        lProps.setExtensionsExecutionFlag(ExtensionsExecutionFlag.EXECUTE_LIST);
        lProps.setIgnoreVersion(true);
        lProps.setImportFileContent(false);

        pStub.importData(lRoleToken, pSheet.getBytes(), lProps);
    }
    
    private SheetData getSheetByRef(WebServicesStub pStub, String pSheetReference)
    throws GDMException_Exception{
        // Log to web-services and retrieve the role token.
        String lRoleToken = getRoleToken(pStub, Data.DEFAULT_PRODUCT);

        // Retrieve Sheet with reference pSheetReference
        SheetData lSheetsData = pStub.getSheetByRef(lRoleToken, 
                Data.DEFAULT_PROCESS_NAME, 
                Data.DEFAULT_PRODUCT, 
                pSheetReference);
        
        return lSheetsData;
    }
    
    private String exportSheet(WebServicesStub pStub, String pSheetReference)
            throws GDMException_Exception {
        
        // Log to web-services and retrieve the role token.
        String lRoleToken = getRoleToken(pStub, Data.DEFAULT_PRODUCT);

        // Retrieve Sheet with reference pSheetReference
        SheetData lSheetData = getSheetByRef(pStub, pSheetReference);
        
        List<String> lIds = new ArrayList<String>();
        lIds.add(lSheetData.getId());
        byte[] lResult = pStub.generateSheetReport(lRoleToken, lIds, SheetExportFormat.XML, null);
        
        return new String(lResult);
    }

    private void compare(String pValue, String pReference, String pErrorMessage) {
        if (!((pValue.contains("BLACK") && pReference.contains("BLACK")) ||
                (pValue.contains("GREY") && pReference.contains("GREY")))) {
            ERR.println("TEST FAILURE at" + new Date() + ".\n"
                    + "Expected:\n" + pReference
                    + "\nResult:\n" + pValue + "\n");
        }
    }
    
    private void sleep(int pSecs) {
        final int lThousand = 1000;
        try {
            Thread.sleep(pSecs * lThousand);
        }
        catch (InterruptedException ex) {
            // Will not happen
        }
    }
}

/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.report;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.domain.export.ExportTypeEnum;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestCreateReportModelService
 * 
 * @author ahaugomm
 */
public class TestCreateReportModelService extends
        AbstractBusinessServiceTestCase {

    /** The reporting service */
    private ReportingService reportingService;

    /** Role name */
    private static final String ROLE_NAME = "notadmin";

    private static final String[] SHEET_TYPE_NAMES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_DOG };

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // Creating report model data object

        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData = new ReportModelData();

        lReportModelData.setName("myReport");
        lReportModelData.setDescription("my first report1s");
        lReportModelData.setPath("report/generateReport/myReport.jasper");
        lReportModelData.setFieldsContainerIds(lFieldsContainerIds);
        lReportModelData.setExportTypes(lExportTypes);
        lReportModelData.setId(null);

        // Performing the creation in DB ...
        reportingService.createReportModel(adminRoleToken, lReportModelData);

        // Finding compatible models...
        List<ReportModelData> lReportModelDatas =
                reportingService.getCompatibleModels(adminRoleToken,
                        lFieldsContainerIds,
                        reportingService.getExportType(ExportTypeEnum.PDF));

        assertNotNull("No compatibles models found.", lReportModelDatas);
        assertTrue("No compatibles models found.",
                lReportModelDatas.size() >= 1);

        boolean lEqual = false;
        for (ReportModelData lReport : lReportModelDatas) {
            lEqual = lEqual || lReport.getId().equals(lReportModelData.getId());
        }
        assertTrue("Newly created model not found.", lEqual);

    }

    /**
     * Tests the method with an incorrect export type
     */
    public void testIncorrectExportTypesCase() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // Creating report model data object

        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData = new ReportModelData();

        lReportModelData.setName("myReport");
        lReportModelData.setDescription("my first report1s");
        lReportModelData.setPath("report/generateReport/myReport.jasper");
        lReportModelData.setFieldsContainerIds(lFieldsContainerIds);
        //        lReportModelData.setExportTypes(lExportTypes);
        lReportModelData.setId(null);

        // Performing the creation in DB ...
        try {
            reportingService.createReportModel(adminRoleToken, lReportModelData);
            fail("The exception has not been thrown.");
        }

        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }

    }

    /**
     * Test the method with an incorrect field container id
     */
    public void testIncorrectFieldsContainerIdCase() {
        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // Creating report model data object

        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData = new ReportModelData();

        lReportModelData.setName("myReport");
        lReportModelData.setDescription("my first report1s");
        lReportModelData.setPath("report/generateReport/myReport.jasper");
        lReportModelData.setExportTypes(lExportTypes);
        lReportModelData.setId(null);

        // Performing the creation in DB ...
        try {
            reportingService.createReportModel(adminRoleToken, lReportModelData);
            fail("The exception has not been thrown.");
        }

        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }

    }

    /**
     * Test the method with an authorization problem case
     */
    public void testNoAuthorizationCase() {
        // User2 login
        String lUserToken2 =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken2 =
                authorizationService.selectRole(lUserToken2, ROLE_NAME,
                        getProductName(), getProcessName());

        reportingService = serviceLocator.getReportingService();
        sheetService = serviceLocator.getSheetService();

        // Creating report model data object

        String[] lExportTypes = { "PDF", "XLS" };
        String[] lFieldsContainerIds = new String[2];
        for (int i = 0; i < 2; i++) {
            lFieldsContainerIds[i] =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), SHEET_TYPE_NAMES[i],
                            CacheProperties.IMMUTABLE).getId();
        }
        ReportModelData lReportModelData = new ReportModelData();

        lReportModelData.setName("myReport");
        lReportModelData.setDescription("my first report1s");
        lReportModelData.setPath("report/generateReport/myReport.jasper");
        lReportModelData.setFieldsContainerIds(lFieldsContainerIds);
        lReportModelData.setExportTypes(lExportTypes);
        lReportModelData.setId(null);

        // Performing the creation in DB ...
        try {
            reportingService.createReportModel(lRoleToken2, lReportModelData);
            fail("The exception has not been thrown.");
        }

        catch (AuthorizationException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

    }

    //
    // /**
    // * Check equality between two report models.
    // * @param lModel1
    // * @param lModel2
    // * @return
    // */
    // private boolean areEqual(ReportModelData lModel1, ReportModelData
    // lModel2){
    // boolean lResult = lModel1.getName()!= null &&
    // lModel1.getName().equals(lModel2.getName());
    // lResult = lResult || (lModel1.getDescription()!= null &&
    // lModel1.getDescription().equals(lModel2.getDescription()));
    // lResult = lResult || (lModel1.getPath()!= null &&
    // lModel1.getPath().equals(lModel2.getPath()));
    // lResult = lResult || (lModel1.getExportTypes()!= null &&
    // lModel1.getExportTypes().length == lModel2.getExportTypes().length);
    // lResult = lResult ||
    // (Arrays.asList(lModel1.getExportTypes()).containsAll(Arrays.asList(
    // lModel1.getExportTypes())));
    // lResult = lResult || (lModel1.getFieldsContainerIds()!= null &&
    // lModel1.getFieldsContainerIds().length ==
    // lModel2.getFieldsContainerIds().length);
    // lResult = lResult ||
    // (Arrays.asList(lModel1.getFieldsContainerIds()).containsAll(
    // Arrays.asList(lModel1.getFieldsContainerIds())));
    // return lResult;
    // }

}

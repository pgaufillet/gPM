/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around sheets.
 * 
 * @author nsamson
 */
public class SheetTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        //        lSuite.addTestSuite(TestGetSheetTypeByKeyService.class);
        //        lSuite.addTestSuite(TestGetSheetTypeByNameService.class);
        //        lSuite.addTestSuite(TestGetSheetsByTypeService.class);
        //        lSuite.addTestSuite(TestGetSheetModelService.class);
        //        lSuite.addTestSuite(TestGetSheetsService.class);
        //        lSuite.addTestSuite(TestGetSheetByKeyService.class);
        //        lSuite.addTestSuite(TestGetSheetRefByKeyService.class);
        //        lSuite.addTestSuite(TestGetSheetSummaryByKeyService.class);
        //        lSuite.addTestSuite(TestUpdateSheetService.class);
        //        lSuite.addTestSuite(TestCreateSheetService.class);
        //        lSuite.addTestSuite(TestDeleteSheetService.class);
        //        lSuite.addTestSuite(TestGetSheetByRefService.class);
        //        lSuite.addTestSuite(TestGetSheetTypeBySheetKeyService.class);
        //        lSuite.addTestSuite(TestUpdateSheetRefByKeyService.class);
        //        lSuite.addTestSuite(TestGetSheetByCopyService.class);
        //        lSuite.addTestSuite(TestGetSheetByCloneService.class);
        //        lSuite.addTestSuite(TestDuplicateSheetService.class);
        //        lSuite.addTestSuite(TestGetSheetProcessInformationService.class);
        //        lSuite.addTestSuite(TestChangeStateService.class);
        //        lSuite.addTestSuite(TestGetSheetHistoryService.class);
        //        lSuite.addTestSuite(TestAddSheetHistoryService.class);
        //        lSuite.addTestSuite(TestCreateSheetTypeService.class);
        //        lSuite.addTestSuite(TestGetCreatableSheetTypesService.class);
        //
        //        lSuite.addTestSuite(TestCreateSheetLinkTypeService.class);
        //        lSuite.addTestSuite(TestGetSheetLinkTypeByNameService.class);
        //        lSuite.addTestSuite(TestGetSheetLinkTypesService.class);
        //        lSuite.addTestSuite(TestGetSheetLinkTypeByKeyService.class);
        //        lSuite.addTestSuite(TestGetSheetLinksByTypeService.class);
        //        lSuite.addTestSuite(TestGetPossibleLinkTypesService.class);
        //
        //        lSuite.addTestSuite(TestLockSheetService.class);
        //        lSuite.addTestSuite(TestFilterCacheableSheet.class);
        //        lSuite.addTestSuite(TestSheetAccess.class);
        //
        //        lSuite.addTestSuite(TestTransformCacheableSheet.class);
        //
        //        lSuite.addTestSuite(TestSessionLocks.class);

        // cf. bug #225
        // lSuite.addTestSuite(TestDeleteSheetLinkTypeService.class);
        // lSuite.addTestSuite(TestDeleteSheetTypeService.class);
        lSuite.addTestSuite(TestIsSheetIdExist.class);
        lSuite.addTestSuite(TestGetAttachedFileContent.class);
        lSuite.addTestSuite(TestGetAttachedFilesInError.class);
        lSuite.addTestSuite(TestGetAttachedFileValue.class);
        lSuite.addTestSuite(TestGetCacheableSheetDuplicationModel.class);
        lSuite.addTestSuite(TestGetCacheableSheetInitializationModel.class);
        lSuite.addTestSuite(TestGetCacheableSheetLinksByType.class);
        lSuite.addTestSuite(TestGetCacheableSheetModel.class);
        lSuite.addTestSuite(TestGetCacheableSheetType.class);
        lSuite.addTestSuite(TestGetCacheableSheetTypeByName.class);
        lSuite.addTestSuite(TestGetSheetTypes.class);
        lSuite.addTestSuite(TestGetCacheableSheetTypeByNameWithProcessNameParameter.class);
        lSuite.addTestSuite(TestGetCacheableSheetWithSheetDataParameter.class);
        lSuite.addTestSuite(TestGetCacheableSheetWithCachePropertiesParameter.class);
        lSuite.addTestSuite(TestGetMaxAttachedFileSize.class);
        lSuite.addTestSuite(TestGetProcessName.class);
        lSuite.addTestSuite(TestGetProductName.class);
        lSuite.addTestSuite(TestGetSheetIdByReference.class);
        lSuite.addTestSuite(TestGetSheetRefStringByKey.class);
        lSuite.addTestSuite(TestGetLock.class);
        lSuite.addTestSuite(TestIsSheetLocked.class);
        lSuite.addTestSuite(TestLockSheet.class);
        lSuite.addTestSuite(TestRemoveLock.class);
        lSuite.addTestSuite(TestUpdateSheetWithTransitionParameter.class);
        lSuite.addTestSuite(TestUpdateSheetWithVersionHandlingParameter.class);
        lSuite.addTestSuite(TestInitializeSheetValues.class);
        lSuite.addTestSuite(TestCreateOrUpdateSheet.class);
        lSuite.addTestSuite(TestCreateSheetWithCacheableSheetParameter.class);
        lSuite.addTestSuite(TestCreateSheetWithSheetDataParameter.class);
        lSuite.addTestSuite(TestAddExtension.class);
        lSuite.addTestSuite(TestAddSheetHistoryWithTimestampParameter.class);
        lSuite.addTestSuite(TestChangeState.class);
        lSuite.addTestSuite(TestDuplicateSheet.class);
        lSuite.addTestSuite(TestSetAttachedFileContent.class);
        lSuite.addTestSuite(TestSetAttachedFileContentWithSheetIdParameter.class);
        lSuite.addTestSuite(TestSetSheetHistoryWithSheetHistoryDataParameter.class);
        lSuite.addTestSuite(TestSetSheetHistoryWithTransitionHistoryDataParameter.class);
        lSuite.addTestSuite(TestSetSheetTypeReferenceField.class);
        lSuite.addTestSuite(TestDeleteSheet.class);
        return lSuite;
    }

}

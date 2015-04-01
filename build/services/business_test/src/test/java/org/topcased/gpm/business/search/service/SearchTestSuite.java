/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.topcased.gpm.business.search.TestMultiLevelFilters;
import org.topcased.gpm.business.search.TestOrderByClause;
import org.topcased.gpm.business.search.service.linkedelement.TestLinkedProductNameVirtualField;
import org.topcased.gpm.business.search.service.linkedelement.TestLinkedSheetReferenceVirtualField;
import org.topcased.gpm.business.search.service.linkedelement.TestLinkedSheetStateVirtualField;
import org.topcased.gpm.business.search.service.linkedelement.TestLinkedSheetTypeVirtualField;
import org.topcased.gpm.business.search.service.multicontainer.TestAttachedField;
import org.topcased.gpm.business.search.service.multicontainer.TestBooleanField;
import org.topcased.gpm.business.search.service.multicontainer.TestChoiceField;
import org.topcased.gpm.business.search.service.multicontainer.TestDateField;
import org.topcased.gpm.business.search.service.multicontainer.TestIntegerField;
import org.topcased.gpm.business.search.service.multicontainer.TestProductNameVirtualField;
import org.topcased.gpm.business.search.service.multicontainer.TestRealField;
import org.topcased.gpm.business.search.service.multicontainer.TestReferenceVirtualField;
import org.topcased.gpm.business.search.service.multicontainer.TestStateVirtualField;
import org.topcased.gpm.business.search.service.multicontainer.TestStringField;

/**
 * A test suite managing all tests upon Search service
 * 
 * @author ahaugomm
 */
public class SearchTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestSearchUtil.class);
        lSuite.addTestSuite(TestSearchService.class);
        lSuite.addTestSuite(TestCheckVisibilityConstraintsService.class);
        lSuite.addTestSuite(TestCreateExecutableFilterService.class);
        lSuite.addTestSuite(TestGetExecutableFilterByKeyService.class);
        lSuite.addTestSuite(TestGetExecutableFilterByNameService.class);
        lSuite.addTestSuite(TestExecuteFilterService.class);
        lSuite.addTestSuite(TestExecuteSheetFilterService.class);
        lSuite.addTestSuite(TestExecuteSheetFilterServiceWithEmptyStringCriteria.class);
        lSuite.addTestSuite(TestExecuteSheetFilterWithEmptyChoiceFieldService.class);
        lSuite.addTestSuite(TestExecuteSheetFilterWithDateCriteriaService.class);
        lSuite.addTestSuite(TestLinkFilters.class);
        lSuite.addTestSuite(TestGetUsableFieldsService.class);
        lSuite.addTestSuite(TestRemoveExecutableFilterService.class);
        lSuite.addTestSuite(TestRemoveExecutableFiltersService.class);
        lSuite.addTestSuite(TestUpdateExecutableFilterService.class);
        lSuite.addTestSuite(TestGetCompatibleOperatorsService.class);
        lSuite.addTestSuite(TestGetUsableFieldTypeService.class);
        lSuite.addTestSuite(TestGetVisibleExecutableFilterDatasService.class);
        lSuite.addTestSuite(TestMultiLevelFilters.class);
        lSuite.addTestSuite(TestOrderByClause.class);
        lSuite.addTestSuite(TestNonCaseSensitiveFilter.class);
        lSuite.addTestSuite(TestSortFilterService.class);
        lSuite.addTestSuite(TestLevel3Filter.class);
        lSuite.addTestSuite(TestLevel2FilterWithLinkProperty.class);
        //Fields for multi-container tests
        lSuite.addTestSuite(TestAttachedField.class);
        lSuite.addTestSuite(TestRealField.class);
        lSuite.addTestSuite(TestIntegerField.class);
        lSuite.addTestSuite(TestStringField.class);
        lSuite.addTestSuite(TestDateField.class);
        lSuite.addTestSuite(TestChoiceField.class);
        lSuite.addTestSuite(TestBooleanField.class);
        lSuite.addTestSuite(TestReferenceVirtualField.class);
        lSuite.addTestSuite(TestStateVirtualField.class);
        lSuite.addTestSuite(TestProductNameVirtualField.class);
        lSuite.addTestSuite(TestFilterCriteriaOnDateFields.class);

        //Fields for linked element tests
        lSuite.addTestSuite(TestLinkedProductNameVirtualField.class);
        lSuite.addTestSuite(TestLinkedSheetReferenceVirtualField.class);
        lSuite.addTestSuite(TestLinkedSheetStateVirtualField.class);
        lSuite.addTestSuite(TestLinkedSheetTypeVirtualField.class);

        return lSuite;
    }
}

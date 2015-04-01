/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.SearchServiceTestUtils;
import org.topcased.gpm.business.search.impl.SearchUtils;
import org.topcased.gpm.domain.search.FieldsContainerId;

/**
 * Test search functionals
 * 
 * @author mkargbo
 */
public class TestSearchUtil extends AbstractBusinessServiceTestCase {

    /**
     * Test extract fields container id.
     */
    public void testExtractFieldsContainerId() {
        FilterFieldsContainerInfo[] lFieldsContainerInfos =
                new FilterFieldsContainerInfo[6];
        lFieldsContainerInfos[0] =
                new FilterFieldsContainerInfo("0", "0",
                        FieldsContainerType.LINK);
        lFieldsContainerInfos[1] =
                new FilterFieldsContainerInfo("1", "1",
                        FieldsContainerType.SHEET);
        lFieldsContainerInfos[2] =
                new FilterFieldsContainerInfo("2", "2",
                        FieldsContainerType.LINK);
        lFieldsContainerInfos[3] =
                new FilterFieldsContainerInfo("3", "3",
                        FieldsContainerType.SHEET);
        lFieldsContainerInfos[4] =
                new FilterFieldsContainerInfo("4", "4",
                        FieldsContainerType.LINK);
        lFieldsContainerInfos[5] =
                new FilterFieldsContainerInfo("5", "5",
                        FieldsContainerType.SHEET);

        String lFieldsContainerId =
                SearchUtils.extractFieldsContainerId(Arrays.asList(lFieldsContainerInfos));
        assertEquals("5", lFieldsContainerId);
    }

    private static final FilterFieldsContainerInfo[] PARENT_IDS =
            {
             new FilterFieldsContainerInfo("1", "1", FieldsContainerType.SHEET),
             new FilterFieldsContainerInfo("2", "2", FieldsContainerType.SHEET),
             new FilterFieldsContainerInfo("3", "3", FieldsContainerType.SHEET) };

    private static final String FIELD_LABEL_KEY = "fieldLabelKey";

    private static final String EXPECTED_UFD_ID = "1|2|3|fieldLabelKey";

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# createUsableFieldDataId(java.util.List, java.lang.String)}
     * .
     */
    public void testCreateUsableFieldDataIdListOfStringString() {
        String lCreatedId =
                SearchUtils.createUsableFieldDataId(Arrays.asList(PARENT_IDS),
                        FIELD_LABEL_KEY);
        assertEquals(EXPECTED_UFD_ID, lCreatedId);

    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# createUsableFieldDataId(java.lang.String)}
     * .
     */
    public void testCreateUsableFieldDataIdString() {
        String lCreatedId =
                SearchUtils.createUsableFieldDataId(FIELD_LABEL_KEY);
        assertEquals(FIELD_LABEL_KEY, lCreatedId);
    }

    private static final int EXPECTED_ID = 3;

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# extractFieldsContainerId(java.util.Collection)}
     * .
     */
    public void testExtractFieldsContainerIdCollectionOfFieldsContainerId() {
        Collection<FieldsContainerId> lFieldsContainerIds =
                new ArrayList<FieldsContainerId>(EXPECTED_ID);
        for (int i = 1; i <= EXPECTED_ID; i++) {
            FieldsContainerId lFieldsContainerId =
                    FieldsContainerId.newInstance();
            lFieldsContainerId.setIdentificator(String.valueOf(i));
            lFieldsContainerIds.add(lFieldsContainerId);
        }
        String lExtractedId =
                SearchUtils.extractFieldsContainerId(lFieldsContainerIds);
        assertEquals(String.valueOf(EXPECTED_ID), lExtractedId);
    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# extractFieldsContainerId(java.util.List)}
     * .
     */
    public void testExtractFieldsContainerIdListOfString() {
        String lExtractedId =
                SearchUtils.extractFieldsContainerId(Arrays.asList(PARENT_IDS));
        assertEquals(String.valueOf(EXPECTED_ID), lExtractedId);
    }

    private static final FilterFieldsContainerInfo[] ANCESTORS_IDS =
            {
             new FilterFieldsContainerInfo("1", "1", FieldsContainerType.SHEET),
             new FilterFieldsContainerInfo("2", "2", FieldsContainerType.SHEET) };

    private static final FilterFieldsContainerInfo PARENT_ID =
            new FilterFieldsContainerInfo("3", "3", FieldsContainerType.SHEET);

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# createFieldsContainerHierarchy(java.util.List, java.lang.String)}
     * .
     */
    public void testCreateFieldsContainerHierarchyListOfStringString() {
        List<FilterFieldsContainerInfo> lCreatedList =
                SearchUtils.createFieldsContainerHierarchy(
                        Arrays.asList(ANCESTORS_IDS), PARENT_ID);
        SearchServiceTestUtils.arraysAreEqual(PARENT_IDS,
                lCreatedList.toArray(new FilterFieldsContainerInfo[0]));

    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.search.impl.SearchUtils# createFieldsContainerHierarchy(java.util.List)}
     * .
     */
    public void testCreateFieldsContainerHierarchyListOfString() {
        List<FieldsContainerId> lExpectedFieldsContainerIds =
                new ArrayList<FieldsContainerId>(EXPECTED_ID);
        for (int i = 1; i <= EXPECTED_ID; i++) {
            FieldsContainerId lFieldsContainerId =
                    FieldsContainerId.newInstance();
            lFieldsContainerId.setIdentificator(String.valueOf(i));
            lExpectedFieldsContainerIds.add(lFieldsContainerId);
        }

        List<FieldsContainerId> lCreatedList =
                (List<FieldsContainerId>) SearchUtils.createFieldsContainerHierarchy(Arrays.asList(PARENT_IDS));
        for (int i = 0; i < EXPECTED_ID; i++) {
            assertEquals(lExpectedFieldsContainerIds.get(i).getIdentificator(),
                    lCreatedList.get(i).getIdentificator());
        }
    }
}

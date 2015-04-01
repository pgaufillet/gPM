/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre-Hubert TSAAN (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetLinksByType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method getCacheableSheetLinksByType(String, String, String) of the
 * Sheet Service.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetLinksByType extends
        AbstractBusinessServiceTestCase {
    private static final int PRODUCT_NAME_POSITION = 4;

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        sheetService = serviceLocator.getSheetService();

        //Check

        String lSheet1Id =
                sheetService.getSerializableSheetByRef(adminRoleToken,
                        getProcessName(),
                        GpmTestValues.PRODUCT_NAMES[PRODUCT_NAME_POSITION],
                        GpmTestValues.CREATED_SHEET_REF[0]).getId();

        String lSheet2Id =
                sheetService.getSerializableSheetByRef(adminRoleToken,
                        getProcessName(),
                        GpmTestValues.PRODUCT_NAMES[PRODUCT_NAME_POSITION],
                        GpmTestValues.CREATED_SHEET_REF[1]).getId();

        CacheableLinkType lCacheableLinkType =
                getLinkService().getLinkTypeByName(adminRoleToken,
                        GpmTestValues.SHEET_LINK_TYPE_NAMES[0],
                        CacheProperties.IMMUTABLE);

        FieldsContainerService lFieldContaierService =
                serviceLocator.getFieldsContainerService();

        String lOriginContainerReference =
                lFieldContaierService.getValuesContainer(adminRoleToken,
                        lSheet1Id, CacheProperties.IMMUTABLE).getFunctionalReference();

        String lDestinationContainerReference =
                lFieldContaierService.getValuesContainer(adminRoleToken,
                        lSheet2Id, CacheProperties.IMMUTABLE).getFunctionalReference();

        CacheableLink lCacheableLink =
                new CacheableLink(lCacheableLinkType, lSheet1Id,
                        lOriginContainerReference, lSheet2Id,
                        lDestinationContainerReference);

        lCacheableLink =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, Context.getContext());

        CacheableSheetLinksByType lLinksList =
                sheetService.getCacheableSheetLinksByType(getProcessName(),
                        lSheet1Id, lCacheableLink.getTypeId());
        assertNotNull(lLinksList);

        boolean lTypeNameFound = false;
        for (int i = 0; i < GpmTestValues.SHEET_LINK_TYPE_NAMES.length; i++) {
            if (GpmTestValues.SHEET_LINK_TYPE_NAMES[i].equals(lCacheableLink.getTypeName())) {
                lTypeNameFound = true;
                break;
            }
        }
        assertTrue(lTypeNameFound);
    }
}

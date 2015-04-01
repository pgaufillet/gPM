/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the cached element CacheableSheetLinksByType
 * 
 * @author tpanuel
 */
public class TestCacheableSheetLinks extends AbstractBusinessServiceTestCase {

    private static final String[] SHEET_TYPE_NAMES =
            new String[] { GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1,
                          GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2,
                          "SimpleSheetType3" };

    private static final String[] SHEET_LINK_TYPE_NAMES =
            new String[] { "LINK_TYPE_FOR_TEST_FILTER_A",
                          "LINK_TYPE_FOR_TEST_FILTER_B", "Link-Type3-Type3",
                          "Link-Type3-Type3-unidirectional" };

    private void compareSheetLinks(String[] pLinksTab, List<String> pLinksList,
            boolean pCheckOrder) {
        int lNbLinksOnTab;
        int lNbLinksOnList;

        if (pLinksTab == null) {
            lNbLinksOnTab = 0;
        }
        else {
            lNbLinksOnTab = pLinksTab.length;
        }

        if (pLinksList == null) {
            lNbLinksOnList = 0;
        }
        else {
            lNbLinksOnList = pLinksList.size();
        }

        assertEquals(
                "CacheableSheetLinksByType error : cached element doesn't "
                        + "contain the good number of links", lNbLinksOnTab,
                lNbLinksOnList);

        if (lNbLinksOnTab != 0) {
            for (int i = 0; i < pLinksTab.length; i++) {
                String lLink = pLinksTab[i];
                if (pCheckOrder) {
                    assertTrue(
                            "CacheableSheetLinksByType error : cached element is not "
                                    + "in the right order",
                            pLinksList.get(i).equals(lLink));
                }
                else {
                    assertTrue(
                            "CacheableSheetLinksByType error : cached element doesn't "
                                    + "contain the good links",
                            pLinksList.contains(lLink));
                }
            }
        }
    }

    public void testLinksOrder() {

        CacheableSheetType lSimpleSheetType3 =
                getSheetService().getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAMES[2], CacheProperties.IMMUTABLE);

        // SIMPLE SHEET 1 : Ref is ORIGIN
        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        BusinessSheet lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("ORIGIN");

        String lSheetOriginId =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 1 : Ref is B-5-0
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("B");
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_2").set(5);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_3").set(0);

        String lSheet1Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 2 : Ref is B-10-0
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("B");
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_2").set(10);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_3").set(0);

        String lSheet2Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 3 : Ref is B-5-8
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("B");
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_2").set(5);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_3").set(8);

        String lSheet3Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 4 : Ref is A-999-0
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("A");
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_2").set(999);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_3").set(0);

        String lSheet4Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 5 : Ref is B-5-3
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("B");
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_2").set(5);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getIntegerField(
                "REF_3").set(3);

        String lSheet5Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        // SIMPLE SHEET 6 : Ref is DESTINATION
        lCacheableSheet =
                getSheetService().getCacheableSheetModel(adminRoleToken,
                        lSimpleSheetType3, GpmTestValues.PRODUCT_NAMES[7], null);
        lSheet =
                new CacheableSheetAccess(adminRoleToken, lSimpleSheetType3,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lSheet.getMultipleField(FieldsService.REFERENCE_FIELD_NAME).getStringField(
                "REF_1").set("DESTINATION");

        String lSheet6Id =
                getSheetService().createSheet(adminRoleToken, lCacheableSheet,
                        null);

        CacheableLinkType lCacheableLinkType1 =
                getLinkService().getLinkTypeByName(adminRoleToken,
                        SHEET_LINK_TYPE_NAMES[2], CacheProperties.IMMUTABLE);

        CacheableLinkType lCacheableLinkType2 =
                getLinkService().getLinkTypeByName(adminRoleToken,
                        SHEET_LINK_TYPE_NAMES[3], CacheProperties.IMMUTABLE);

        // LINK ORIGIN-SHEET_1
        CacheableLink lCacheableLink =
                new CacheableLink(lCacheableLinkType1, lSheetOriginId, null,
                        lSheet1Id, null);
        String lLink1Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        // LINK ORIGIN-SHEET_2
        lCacheableLink =
                new CacheableLink(lCacheableLinkType1, lSheetOriginId, "",
                        lSheet2Id, "");
        String lLink2Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        // LINK ORIGIN-SHEET_3
        lCacheableLink =
                new CacheableLink(lCacheableLinkType1, lSheetOriginId, "",
                        lSheet3Id, "");
        String lLink3Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        // LINK ORIGIN-SHEET_4
        lCacheableLink =
                new CacheableLink(lCacheableLinkType1, lSheetOriginId, "",
                        lSheet4Id, "");
        String lLink4Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        // LINK SHEET5-ORIGIN
        lCacheableLink =
                new CacheableLink(lCacheableLinkType1, lSheet5Id, "",
                        lSheetOriginId, "");
        String lLink5Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        // LINK SHEET5-ORIGIN
        lCacheableLink =
                new CacheableLink(lCacheableLinkType2, lSheetOriginId, "",
                        lSheet6Id, "");
        String lLink6Id =
                getLinkService().createLink(adminRoleToken, lCacheableLink,
                        true, null).getId();

        compareSheetLinks(
                new String[] { lLink4Id, lLink1Id, lLink5Id, lLink3Id, lLink2Id },
                sheetService.getCacheableSheetLinksByType(getProcessName(),
                        lSheetOriginId, lCacheableLinkType1.getId()).getLinksId(),
                true);

        compareSheetLinks(
                new String[] { lLink6Id },
                sheetService.getCacheableSheetLinksByType(getProcessName(),
                        lSheetOriginId, lCacheableLinkType2.getId()).getLinksId(),
                true);

        compareSheetLinks(new String[] {},
                sheetService.getCacheableSheetLinksByType(getProcessName(),
                        lSheet6Id, lCacheableLinkType2.getId()).getLinksId(),
                true);

    }
}
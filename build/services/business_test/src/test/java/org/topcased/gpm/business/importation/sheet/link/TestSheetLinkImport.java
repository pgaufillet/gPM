/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.sheet.link;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.importation.AbstractValuesContainerImportTest;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.LinkServiceImpl;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSheetLinkImport: Test sheet's link importation.
 * 
 * @author mkargbo
 */
public class TestSheetLinkImport extends AbstractValuesContainerImportTest {
    private static final String IMPORT_FILE =
            "importation/sheet/link/linksToImport.xml";

    private static final String IMPORT_FILE_TO_UPDATE =
            "importation/sheet/link/linksToUpdate.xml";

    private static final String INSTANCE_FILE =
            "importation/sheet/link/linksToInstantiate.xml";

    /** ERASE_FIELD_TO_UPDATE */
    private static final String ERASE_FIELD_TO_UPDATE = "testExportLink_field1";

    /** ERASE_NEW_VALUE */
    private static final String ERASE_NEW_VALUE = "to be erase";

    private static final String LINK_TYPE_NAME = "testExportLink";

    private static final String LINKED_SHEET_PRODUCT_NAME =
            GpmTestValues.PRODUCT1_NAME;

    private static final Set<String[]> IMPORTED_LINKS;

    /** Fields that should be import for 'admin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;

    /** Fields that should be import for 'notadmin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    /** Fields that should not be import for 'notadmin' role */
    private static final Map<String, String> NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    static {
        IMPORTED_LINKS = new HashSet<String[]>();
        IMPORTED_LINKS.add(new String[] { "testExportSheet_12",
                                         "testExportSheet_13" });
        IMPORTED_LINKS.add(new String[] { "testExportSheet_13",
                                         "testExportSheet_12" });

        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN = new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put(ERASE_FIELD_TO_UPDATE,
                "testExportLink_field1_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportLink_field2",
                "testExportLink_field2_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportLink_field3",
                "testExportLink_field3_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportLink_field4",
                "testExportLink_field4_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("testExportLink_field5",
                "testExportLink_field5_UPDATED");

        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(ERASE_FIELD_TO_UPDATE,
                "testExportLink_field1_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put("testExportLink_field3",
                "testExportLink_field3_UPDATED");

        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(
                "testExportLink_field2", "testExportLink_field1_UPDATED");
        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put(
                "testExportLink_field4", "testExportLink_field3_UPDATED");
    }

    private LinkServiceImpl linkService;

    private SheetServiceImpl sheetService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        linkService =
                (LinkServiceImpl) ContextLocator.getContext().getBean(
                        "linkServiceImpl");
        sheetService =
                (SheetServiceImpl) ContextLocator.getContext().getBean(
                        "sheetServiceImpl");
        instantiate(getProcessName(), INSTANCE_FILE);
    }


    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Map<String[], Integer> erasePreCondition() {
        //Retrieve elements identifiers.
        final Map<String[], Integer> lLinks = new HashMap<String[], Integer>();
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            CacheableLink lLink =
                    linkService.getCacheableLink(lId, CacheProperties.MUTABLE);
            FieldValueData lFieldValue =
                    (FieldValueData) lLink.getValue(ERASE_FIELD_TO_UPDATE);
            if (lFieldValue != null) {
                lFieldValue.setValue(ERASE_NEW_VALUE);
                linkService.updateLink(normalRoleToken, lLink, CONTEXT);
                lLink =
                        linkService.getCacheableLink(lId,
                                CacheProperties.IMMUTABLE);
                lLinks.put(lRef, lLink.getVersion());
            }
        }
        return lLinks;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected CacheableValuesContainer getBusinessObject(String pRoleToken,
            String pElementId) {
        return linkService.getCacheableLink(pElementId,
                CacheProperties.IMMUTABLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String)
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lOriginId = getSheetElementId(pElementRef[0]);
        String lDestinationId = getSheetElementId(pElementRef[1]);
        return linkService.getId(LINK_TYPE_NAME, lOriginId, lDestinationId);
    }

    /**
     * Retrieve the linked sheet identifier.
     * 
     * @param pElementRef
     *            Identifiers of the sheet.
     * @return Technical identifier of the sheet.
     */
    protected String getSheetElementId(String... pElementRef) {
        String lRef = pElementRef[0];
        String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        LINKED_SHEET_PRODUCT_NAME, lRef);
        return lSheetId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportNotAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFile()
     */
    @Override
    protected String getImportFile() {
        return IMPORT_FILE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFileForUpdating()
     */
    @Override
    protected String getImportFileForUpdating() {
        return IMPORT_FILE_TO_UPDATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        return IMPORTED_LINKS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getNotExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getNotExpectedFieldsToImportNotAdmin() {
        return NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElement) {
        linkService.deleteLink(adminRoleToken, pElement, CONTEXT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setSheetLinksFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        doTestEraseImport(adminRoleToken, getExpectedFieldsToImportAdmin(),
                EMPTY_MAP);
    }

    /** {@inheritDoc} */
    @Override
    protected void eraseAssertion(Object... pArgs) {
        // Unused
    }
}

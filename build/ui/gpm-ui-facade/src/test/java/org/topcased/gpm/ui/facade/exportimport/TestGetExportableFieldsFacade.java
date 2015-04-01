/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.exportimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.ui.facade.shared.export.UiExportableField;
import org.topcased.gpm.ui.facade.shared.export.UiExportableGroup;

/**
 * TestGetExportableFieldsFacade
 * 
 * @author jlouisy
 */
public class TestGetExportableFieldsFacade extends AbstractFacadeTestCase {

    private static final String NOT_EXPORTABLE_SIMPLE_FIELD =
            "CONFIDENTIAL_FIELD_IN_AUTHORIZATION";

    private static final String NOT_EXPORTABLE_MULTI_FIELD =
            "MULTIVALUED_MULTIPLE_FIELD";

    private static final String EXPORTABLE_SIMPLE_FIELD =
            "INTEGER_SIMPLE_FIELD";

    private static final String EXPORTABLE_MULTI_FIELD = "MULTIPLE_FIELD";

    private static final String EXPORTABLE_SUB_FIELD =
            "SUB_INTEGER_SIMPLE_FIELD";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExportImportFacade lExportImportFacade =
                getFacadeLocator().getExportImportFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();

        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        String lSheetId2 =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Destination_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get exportable fields.");
        }
        List<UiExportableGroup> lGroups =
                lExportImportFacade.getExportableFields(lUiSession,
                        Arrays.asList(lSheetId, lSheetId2));

        List<String> lFieldNames = new ArrayList<String>();
        List<String> lSubFieldNames = new ArrayList<String>();

        for (UiExportableGroup lGroup : lGroups) {
            assertFalse(lGroup.getExportableFields().isEmpty());
            for (UiExportableField lField : lGroup.getExportableFields()) {
                lFieldNames.add(lField.getName());
                if (lField.getExportableFields() != null) {
                    for (UiExportableField lSubField : lField.getExportableFields()) {
                        lSubFieldNames.add(lSubField.getName());
                    }
                }
            }
        }

        //Lists are not empty
        assertFalse(lFieldNames.isEmpty());
        assertFalse(lSubFieldNames.isEmpty());

        //Exportable fields are get
        assertTrue(lFieldNames.contains(EXPORTABLE_SIMPLE_FIELD));
        assertTrue(lFieldNames.contains(EXPORTABLE_MULTI_FIELD));

        //Not exportable fields are not get
        assertFalse(lFieldNames.contains(NOT_EXPORTABLE_SIMPLE_FIELD));
        assertFalse(lFieldNames.contains(NOT_EXPORTABLE_MULTI_FIELD));
        assertFalse(lSubFieldNames.contains(NOT_EXPORTABLE_SIMPLE_FIELD));
        assertFalse(lSubFieldNames.contains(NOT_EXPORTABLE_MULTI_FIELD));

        // Exportable sub fields are get
        assertTrue(lSubFieldNames.contains(EXPORTABLE_SUB_FIELD));
    }
}

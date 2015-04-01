/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.extendedaction;

import java.util.List;

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetInputDataFacade
 * 
 * @author jlouisy
 */
public class TestGetInputDataFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        SheetService lSheetService = getSheetService();
        String lOriginSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");
        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lUiSession.getRoleToken(),
                        lOriginSheetId, CacheProperties.IMMUTABLE);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Input Data.");
        }
        UiInputData lUiInputData =
                lExtendedActionFacade.getInputData(lUiSession,
                        "ExtendedActionALWAYS", lCacheableSheet.getTypeId(),
                        lCacheableSheet.getId(), null);

        // Test some fields

        assertEquals(1, lUiInputData.getFieldGroups().size());

        List<BusinessFieldGroup> lFieldGroups = lUiInputData.getFieldGroups();
        assertEquals(1, lFieldGroups.size());

        List<String> lFieldNames = lFieldGroups.get(0).getFieldNames();
        assertEquals(2, lFieldNames.size());
        assertTrue(lFieldNames.contains("SINGLE_LINE_DISPLAY_HINT"));
        assertTrue(lFieldNames.contains("MULTI_LINE_DISPLAY_HINT"));

        assertEquals("Single line display hint", lUiInputData.getStringField(
                "SINGLE_LINE_DISPLAY_HINT").getFieldDescription());
        assertEquals(
                "Multiline line display hint",
                lUiInputData.getStringField("MULTI_LINE_DISPLAY_HINT").getFieldDescription());

        UiStringField lUiField =
                (UiStringField) lUiInputData.getStringField("SINGLE_LINE_DISPLAY_HINT");
        assertEquals(StringDisplayHintType.SINGLE_LINE,
                lUiField.getStringDisplayHintType());

        lUiField =
                (UiStringField) lUiInputData.getStringField("MULTI_LINE_DISPLAY_HINT");
        assertEquals(20, lUiField.getHeight());
        assertEquals(100, lUiField.getWidth());
        assertEquals(StringDisplayHintType.MULTI_LINE,
                lUiField.getStringDisplayHintType());
    }

}

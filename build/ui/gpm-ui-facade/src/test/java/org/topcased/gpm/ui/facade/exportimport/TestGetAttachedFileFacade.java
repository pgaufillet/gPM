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

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestExportSheetsFacade
 * 
 * @author jlouisy
 */
public class TestGetAttachedFileFacade extends AbstractFacadeTestCase {

    private ExportImportFacade lExportImportFacade;

    private UiSession lUiSession;

    private static final String ATTACHED_FILE_FIELD_NAME =
            "ATTACHED_DISPLAY_HINT";

    /**
     * Normal case
     */
    public void testNormalCase() {

        lUiSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        lExportImportFacade = getFacadeLocator().getExportImportFacade();
        String lRoleToken = lUiSession.getRoleToken();

        // Get origin sheet
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lRoleToken,
                        "REF_Origin_Sheet");

        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE);
        CacheableSheetType lCacheableSheetType =
                lSheetService.getCacheableSheetType(lRoleToken,
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);
        BusinessSheet lBusinessSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        BusinessAttachedField lBusinessAttachedField =
                lBusinessSheet.getAttachedField(ATTACHED_FILE_FIELD_NAME);

        String lFieldId = lBusinessAttachedField.getId();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get attached file.");
        }
        lExportImportFacade.getAttachedFile(lUiSession, lFieldId);
    }
}

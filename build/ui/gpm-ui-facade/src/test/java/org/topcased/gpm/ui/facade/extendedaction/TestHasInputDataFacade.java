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

import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestHastInputDataFacade
 * 
 * @author jlouisy
 */
public class TestHasInputDataFacade extends AbstractFacadeTestCase {

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
        assertTrue(lExtendedActionFacade.hasInputData(lUiSession,
                "ExtendedActionALWAYS", lCacheableSheet.getTypeId()));
        assertFalse(lExtendedActionFacade.hasInputData(lUiSession,
                "ExtendedActionEDIT", lCacheableSheet.getTypeId()));
    }

}

/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/

package metadata.commands.java;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.common.extensions.ResultingScreen;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class ExtendedActionCREATE implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        SheetService lSheetService =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getSheetService();

        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);

        CacheableSheet lCacheableSheet =
                (CacheableSheet) pContext.get("currentEditedSheet");

        CacheableSheetType lCacheableSheetType =
                lSheetService.getCacheableSheetType(lRoleToken,
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);

        BusinessSheet lSheet =
                new CacheableSheetAccess(lRoleToken, lCacheableSheetType,
                        lCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        SimpleDateFormat lDateFormat =
                new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

        String lDate = lDateFormat.format(new Date());

        lSheet.getMultivaluedStringField("EXTENDED_ACTION_FIELD").addLine().set(
                lDate + " : [EXE] CREATE Extended Action");

        String lNewSheetID =
                lSheetService.createSheet(lRoleToken,
                        ((CacheableSheetAccess) lSheet).read(), pContext);

        pContext.set(ExtensionsService.RESULT_SCREEN,
                ResultingScreen.SHEET_EDITION);
        pContext.set(ExtensionsService.RESULT_SHEET_ID, lNewSheetID);
        return true;
    }

}

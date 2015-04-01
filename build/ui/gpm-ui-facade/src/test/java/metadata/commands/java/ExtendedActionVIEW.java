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
import java.util.ArrayList;
import java.util.Collection;
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
public class ExtendedActionVIEW implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        SheetService lSheetService =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getSheetService();

        String lSheetId = pContext.get(ExtensionPointParameters.SHEET_ID);
        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);

        CacheableSheet lCacheableSheet =
                lSheetService.getCacheableSheet(lRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE);

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
                lDate + " : [EXE] VIEW Extended Action");

        Collection<String> lSkippedExtensionPoints = new ArrayList<String>();
        lSkippedExtensionPoints.add(org.topcased.gpm.business.extensions.service.ExtensionPointNames.PRE_UPDATE);
        pContext.put(Context.GPM_SKIP_EXT_PTS, lSkippedExtensionPoints);

        lSheetService.updateSheet(lRoleToken,
                ((CacheableSheetAccess) lSheet).read(), pContext);

        pContext.set(ExtensionsService.RESULT_SCREEN,
                ResultingScreen.SHEET_VISUALIZATION);
        pContext.set(ExtensionsService.RESULT_SHEET_ID, lSheetId);
        return true;
    }

}

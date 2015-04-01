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
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.link.BusinessLink;
import org.topcased.gpm.business.values.link.impl.cacheable.CacheableLinkAccess;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class PreCreateSheetLink implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        SheetService lSheetService =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getSheetService();
        LinkService lLinkService =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR).getLinkService();
        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);

        CacheableLink lLink = pContext.get(ExtensionPointParameters.LINK);

        String lSheetOriginId = lLink.getOriginId();
        String lSheetDestId = lLink.getDestinationId();

        CacheableSheet lSheetOrigin =
                lSheetService.getCacheableSheet(lRoleToken, lSheetOriginId,
                        CacheProperties.MUTABLE);
        CacheableSheet lSheetDestination =
                lSheetService.getCacheableSheet(lRoleToken, lSheetDestId,
                        CacheProperties.MUTABLE);

        Collection<String> lSkippedExtensionPoints = new ArrayList<String>();
        lSkippedExtensionPoints.add(org.topcased.gpm.business.extensions.service.ExtensionPointNames.PRE_UPDATE);
        pContext.put(Context.GPM_SKIP_EXT_PTS, lSkippedExtensionPoints);

        //Sauvegarde des fiches
        lSheetService.updateSheet(lRoleToken, lSheetOrigin, pContext);
        lSheetService.updateSheet(lRoleToken, lSheetDestination, pContext);

        lSheetOrigin =
                lSheetService.getCacheableSheet(lRoleToken, lSheetOriginId,
                        CacheProperties.MUTABLE);
        lSheetDestination =
                lSheetService.getCacheableSheet(lRoleToken, lSheetDestId,
                        CacheProperties.MUTABLE);

        CacheableSheetType lSheetTypeOrigin =
                lSheetService.getCacheableSheetTypeByName(lRoleToken,
                        lSheetOrigin.getTypeName(), CacheProperties.IMMUTABLE);
        BusinessSheet lBusinessSheetOrigin =
                new CacheableSheetAccess(lRoleToken, lSheetTypeOrigin,
                        lSheetOrigin,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        CacheableSheetType lSheetTypeDestination =
                lSheetService.getCacheableSheetTypeByName(lRoleToken,
                        lSheetDestination.getTypeName(),
                        CacheProperties.IMMUTABLE);
        BusinessSheet lBusinessSheetDestination =
                new CacheableSheetAccess(lRoleToken, lSheetTypeDestination,
                        lSheetDestination,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        SimpleDateFormat lDateFormat =
                new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

        String lDate = lDateFormat.format(new Date());

        lBusinessSheetOrigin.getMultivaluedStringField("EXTENSION_POINT_FIELD").addLine().set(
                lDate + " : [EXE] PreCreateSheetLink Extension Point");
        lBusinessSheetDestination.getMultivaluedStringField(
                "EXTENSION_POINT_FIELD").addLine().set(
                lDate + " : [EXE] PreCreateSheetLink Extension Point");

        lSheetService.updateSheet(lRoleToken, lSheetOrigin, pContext);
        lSheetService.updateSheet(lRoleToken, lSheetDestination, pContext);

        CacheableLinkType lCacheableLinkType =
                lLinkService.getLinkTypeByName(lRoleToken, lLink.getTypeName(),
                        CacheProperties.IMMUTABLE);
        BusinessLink lBusinessLink =
                new CacheableLinkAccess(lRoleToken, lCacheableLinkType, lLink,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        lBusinessLink.getDateField("Sheet1Sheet1_F3").set(new Date());
        return true;
    }

}

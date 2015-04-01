/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * AttrTestBase class
 * 
 * @author llatil
 */
public class AttrTestBase extends AbstractBusinessServiceTestCase {
    private AttributesService attributesService;

    private String containerId;

    /**
     * Setup
     */
    protected void setUp() {
        super.setUp();

        attributesService = serviceLocator.getAttributesService();

        // Get an attribute container.  (we use a sheet type id for this).
        SheetService lSheetService = serviceLocator.getSheetService();

        CacheableSheetType lType =
                lSheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), GpmTestValues.SHEET_TYPE_CAT,
                        CacheProperties.IMMUTABLE);

        containerId = lType.getId();
    }

    public AttributesService getAttributesService() {
        return attributesService;
    }

    public String getContainerId() {
        return containerId;
    }
}

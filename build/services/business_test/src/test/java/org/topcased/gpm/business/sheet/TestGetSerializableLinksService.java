/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.serialization.data.Link;

/**
 * TestGetSerializableLinksService
 * 
 * @author mfranche
 */
public class TestGetSerializableLinksService extends
        AbstractBusinessServiceTestCase {

    /** The origin sheet reference. */
    private static final String SHEET_REF = GpmTestValues.SHEET_REF_GARFIELD;

    /** The Link Service. */
    private LinkService linkService;

    /**
     * Tests the method in a normal way.
     */
    public void testNormalCase() {
        linkService = serviceLocator.getLinkService();

        //      Gets the origin sheet ID
        final String lSheetId =
            sheetService.getSheetIdByReference(getProcessName(),
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, SHEET_REF);
        
        assertNotNull("Origin sheet ID is null.", lSheetId);

        List<Link> lLinkList =
                linkService.getSerializableLinks(adminRoleToken, lSheetId);

        assertNotNull("getSheetLinks returns a null list", lLinkList);

        int lExpectedSize = 8;
        int lSize = lLinkList.size();
        assertEquals("getLinks returns a list of " + lSize
                + "sheet links instead of a list of " + lExpectedSize
                + " sheet links", lExpectedSize, lSize);
    }

}

/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;

/**
 * This class tests the getMaxAttachedFile method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetMaxAttachedFileSize extends AbstractBusinessServiceTestCase {
    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {
        sheetService = serviceLocator.getSheetService();
        assertNotNull(sheetService.getMaxAttachedFileSize());
        //The value is 0.0 by default
        assertEquals(0.0, sheetService.getMaxAttachedFileSize());
    }
}

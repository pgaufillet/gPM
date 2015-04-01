/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.facilities.ChoiceTreeDisplayHintData;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSetChoiceTreeDisplayHint
 * 
 * @author jlouisy
 */
public class TestSetChoiceTreeDisplayHint extends
        AbstractBusinessServiceTestCase {

    /** The display service */
    private DisplayService displayService;

    /** The sheet type name */
    private final static String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_CAT;

    private final static String FIELD_NAME = "CAT_furlength";

    private final static String SEPARATOR = "|";

    private final static String NEW_SEPARATOR = "*";

    /** An invalid field name */
    private final static String INVALID_FIELD_NAME = "CAT_description";

    /** An invalid container id */
    private final static String INVALID_CONTAINER_ID = "";

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        displayService = serviceLocator.getDisplayService();
        sheetService = serviceLocator.getSheetService();

        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);

        DisplayHint lHint = lSheetType.getDisplayHint(FIELD_NAME);
        assertTrue(lHint instanceof ChoiceTreeDisplayHint);
        assertEquals(((ChoiceTreeDisplayHint) lHint).getSeparator(), SEPARATOR);

        ChoiceTreeDisplayHintData lHintData =
                new ChoiceTreeDisplayHintData(NEW_SEPARATOR);
        displayService.setChoiceTreeDisplayHint(adminRoleToken,
                lSheetType.getId(), FIELD_NAME, lHintData);

        // Retrieving a sheet ...
        DisplayHint lDisplayHint =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE).getDisplayHint(
                        FIELD_NAME);

        assertTrue(lDisplayHint instanceof ChoiceTreeDisplayHint);
        assertEquals(((ChoiceTreeDisplayHint) lDisplayHint).getSeparator(),
                NEW_SEPARATOR);
    }

    /**
     * Tests the method with an invalid separator
     */
    public void testInvalidSeparatorCase() {

        displayService = serviceLocator.getDisplayService();
        sheetService = serviceLocator.getSheetService();

        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        ChoiceTreeDisplayHintData lHintData = new ChoiceTreeDisplayHintData("");

        try {
            displayService.setChoiceTreeDisplayHint(adminRoleToken,
                    lSheetType.getId(), FIELD_NAME, lHintData);
        }
        catch (InvalidValueException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a InvalidIdentifierException.");
        }
    }

    /**
     * Tests the method with an invalid container id
     */
    public void testInvalidContainerIdCase() {

        displayService = serviceLocator.getDisplayService();
        sheetService = serviceLocator.getSheetService();

        ChoiceTreeDisplayHintData lHintData =
                new ChoiceTreeDisplayHintData(NEW_SEPARATOR);

        try {
            displayService.setChoiceTreeDisplayHint(adminRoleToken,
                    INVALID_CONTAINER_ID, FIELD_NAME, lHintData);
        }
        catch (InvalidIdentifierException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a InvalidIdentifierException.");
        }

    }

    /**
     * Tests the method with an invalid field name
     */
    public void testInvalidFieldNameCase() {

        displayService = serviceLocator.getDisplayService();
        sheetService = serviceLocator.getSheetService();

        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        ChoiceTreeDisplayHintData lHintData =
                new ChoiceTreeDisplayHintData(NEW_SEPARATOR);

        try {
            displayService.setChoiceTreeDisplayHint(adminRoleToken,
                    lSheetType.getId(), INVALID_FIELD_NAME, lHintData);
        }
        catch (InvalidNameException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a InvalidNameException.");
        }

    }

}

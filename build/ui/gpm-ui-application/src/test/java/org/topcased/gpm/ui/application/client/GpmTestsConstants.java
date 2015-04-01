/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * GpmFieldTestsConstants, constants used for unit tests.
 * 
 * @author frosier
 */
public class GpmTestsConstants {

    public static final String MODULE_NAME =
            "org.topcased.gpm.ui.application.Application";

    // Tests data.

    public static final Date DEFAULT_TEST_DATE =
            new Date(System.currentTimeMillis());

    public static final Date DEFAULT_OTHER_TEST_DATE = new Date(0);

    public static final List<String> DEFAULT_LIST_TEST_VALUES =
            Arrays.asList("value 1", "value 2", "value 3", "value 4",
                    "value 5", "value 6", "value 7", "value 8", "value 9");

    public static final String DEFAULT_TEST_STRING_VALUE =
            "String value to test : #~f&é'()[]{}$£¤ÿ-è_çà@";

    public static final String DEFAULT_OTHER_TEST_STRING_VALUE =
            "Other string value to test : &é'(-è_çà)='à,;:!.^^$$¨¨";

    public static final Integer DEFAULT_TEST_INT_VALUE = 1520;

    public static final Integer DEFAULT_OTHER_TEST_INT_VALUE = 1250;

    public static final Double DEFAULT_TEST_REAL_VALUE = new Double(124.45445);

    public static final Double DEFAULT_OTHER_TEST_REAL_VALUE =
            new Double(142.54554);
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GpmDateTimeHelper aims at providing static methods to help in the use of Date
 * and time elements on the HMI components package.
 * 
 * @author frosier
 */
public class GpmDateTimeHelper {

    public static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm";

    public static final DateTimeFormat DEFAULT_TIME_FORMAT =
            DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT_PATTERN);

    /**
     * Get a rounded date cut at the day.
     * <p>
     * Example : <br/>
     * 2010.July.10 AD 12:08:17 PM will be cut to 2010.July.10 AD 00:00:00 PM
     * </p>
     * 
     * @param pDate
     *            The date to get shorter.
     * @return The rounded date.
     */
    public static Date getDailyAccuracy(final Date pDate) {
        final DateTimeFormat lDailyFormat = DateTimeFormat.getShortDateFormat();
        return lDailyFormat.parse(lDailyFormat.format(pDate));
    }

    /**
     * Get a rounded date cut at the minutes.
     * <p>
     * Example : <br/>
     * 2010.July.10 AD 12:08:17 PM will be cut to 2010.July.10 AD 12:08:00 PM
     * </p>
     * 
     * @param pDate
     *            The date to get shorter.
     * @return The rounded date.
     */
    public static Date getMinuteAccuracy(final Date pDate) {
        final DateTimeFormat lMinutesFormat =
                DateTimeFormat.getShortTimeFormat();
        return lMinutesFormat.parse(lMinutesFormat.format(pDate));
    }
}

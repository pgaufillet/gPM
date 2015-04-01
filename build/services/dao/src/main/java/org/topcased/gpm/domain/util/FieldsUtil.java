/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * FieldsUtil
 * 
 * @author srene
 */
public class FieldsUtil {
    /** The date format based on ISO 8601 (calendar extended format) */
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd";

    private static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd'T'HH:mm";

    private static final String FULL_DATE_TIME_FORMAT_STR =
            "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Parse a date formatted as an ISO 8601 string.
     * <p>
     * Note that this method supports <b>only</b> the following ISO8601 subsets:
     * <ul>
     * <li>'yyyy-mm-dd', specifying a date only. In this case the time is set to
     * midnight (12:00am),
     * <li>'yyyy-mm-ddThh:mm', specifying a date / time.
     * </ul>
     * 
     * @param pDate
     *            the input string date.
     * @return Date
     * @throws ParseException
     *             error during the parsing
     */
    public static Date parseDate(final String pDate) throws ParseException {
        if (pDate == null || StringUtils.isBlank(pDate)) {
            return null;
        }
        else {
            final Date lDateResult;

            if (StringUtils.countMatches(pDate, "T") == 0) {
                lDateResult =
                        new SimpleDateFormat(DATE_FORMAT_STR).parse(pDate);
            }
            else if (StringUtils.countMatches(pDate, ":") == 1) {
                lDateResult =
                        new SimpleDateFormat(DATE_TIME_FORMAT_STR).parse(pDate);
            }
            else {
                lDateResult =
                        new SimpleDateFormat(FULL_DATE_TIME_FORMAT_STR).parse(pDate);
            }

            return lDateResult;
        }
    }

    /**
     * Format a Date as format ISO 8601 (calendar extended format)
     * 
     * @param pDate
     *            the input date object
     * @return the string date
     */
    public static String formatDate(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        // Return the time part only if the time is different from 12:00am (midnight)
        else {
            final Calendar lCalendar = new GregorianCalendar();

            // Initialize calendar
            lCalendar.setTime(pDate);

            if (lCalendar.get(Calendar.HOUR_OF_DAY)
                    + lCalendar.get(Calendar.MINUTE)
                    + lCalendar.get(Calendar.SECOND) == 0) {
                return new SimpleDateFormat(DATE_FORMAT_STR).format(pDate);
            }
            else {
                return new SimpleDateFormat(FULL_DATE_TIME_FORMAT_STR).format(pDate);
            }
        }
    }

    /**
     * Format a date as format ISO 8601 (calendar extended format).
     * <p>
     * This method returns the date & time as two distinct strings. The time
     * string can be null if the Data object defines no explicit time (time
     * defined a 00:00AM)
     * 
     * @param pDate
     *            the input date object
     * @return Array defining both date & time strings. The date is in index 0,
     *         and time in index 1
     */
    public static String[] formatDateTime(final Date pDate) {
        if (pDate == null) {
            return null;
        }
        else {
            final Calendar lCalendar = new GregorianCalendar();

            // Initialize calendar
            lCalendar.setTime(pDate);

            // Return the time part only if the time is different from 12:00am (midnight)
            if (lCalendar.get(Calendar.HOUR) + lCalendar.get(Calendar.MINUTE)
                    + lCalendar.get(Calendar.SECOND) == 0) {
                return new String[] {
                                     new SimpleDateFormat(DATE_FORMAT_STR).format(pDate),
                                     null };
            }
            else {
                return new SimpleDateFormat(FULL_DATE_TIME_FORMAT_STR).format(
                        pDate).split("T");
            }
        }
    }
}
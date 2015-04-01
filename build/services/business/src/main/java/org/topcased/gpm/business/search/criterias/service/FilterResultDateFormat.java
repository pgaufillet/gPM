/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias.service;

/**
 * FilterResultDateFormat This enumeration purpose all supported date formats
 * for filter result screen.
 * 
 * @author ogehin
 */
public enum FilterResultDateFormat {
    /** Legacy date format (corresponds to a Date.toString method call) */
    TOSTRING,

    /** Format ISO 8601 of W3C */
    ISO8601
}

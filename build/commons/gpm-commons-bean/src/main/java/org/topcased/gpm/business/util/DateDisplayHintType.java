/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

import java.io.Serializable;

/**
 * DateDisplayHintType
 * 
 * @author nveillet
 */
public enum DateDisplayHintType implements Serializable {

    /** 'MM.dd.yy' */
    DATE_SHORT,

    /** 'MMM dd, yyyy' */
    DATE_MEDIUM,

    /** 'MMMMM dd, yyyy' */
    DATE_LONG,

    /** 'EEEEE, MMMMM dd, yyyy' */
    DATE_FULL,

    /** 'MM.dd.yy HH:mm:ss' */
    DATE_TIME_SHORT,

    /** 'MMM dd, yyyy HH:mm:ss' */
    DATE_TIME_MEDIUM,

    /** 'MMMMM dd, yyyy HH:mm:ss' */
    DATE_TIME_LONG,

    /** 'EEEEE, MMMMM dd, yyyy HH:mm:ss' */
    DATE_TIME_FULL;
}

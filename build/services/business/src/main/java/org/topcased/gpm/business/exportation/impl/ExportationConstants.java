/***************************************************************
 * Copyright (c) 2012 ATOS. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Romain Ranzato (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.regex.Pattern;

/**
 * Contains constants used for exportation module
 * 
 * @author rranzato
 */
public class ExportationConstants {

    public static final String USER = "User";

    public static final String USER_LOGIN = "user";

    public static final String MAIL_PART1 = "-gpm";

    public static final String MAIL_PART2 = "@atos.net";

    public static final Pattern DECIMAL_PATTERN =
            Pattern.compile("^[0-9[.]]+$");
    
    public static final String DESCRIPTION = "_description";

    public static final String PRODUCT = "Product";

    public static final String ADMIN = "admin";

    public static final String GPM_ADMINISTRATOR = "gPM Administrator";

    public static final String ADMIN_MAIL = "admin-gpm@atos.net";

    public static final String ADMIN_MD5_PASSWORD =
            "JQ+kQ1/q3OVrf1AcpZ0PqZECaTQ=";

}
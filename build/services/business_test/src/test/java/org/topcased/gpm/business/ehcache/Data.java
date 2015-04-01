/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos Origin)
 ***************************************************************/
package org.topcased.gpm.business.ehcache;

import org.topcased.gpm.business.GpmTestValues;

final class Data {

    public static final String DEFAULT_PROCESS_NAME = GpmTestValues.PROCESS_NAME;
    public static final String DEFAULT_PRODUCT = GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;
    public static final String LOGIN = "admin";
    public static final String PASSWORD = "admin";
    public static final String SHEET_REF = "nyan-sheet-id";
    
    public static final String SHEET1 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "\n"
        + "<gpm xmlns=\"http://www.airbus.com/topcased/gPM\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"http://www.airbus.com/topcased/gPM\">\n"
        + "  <version>1.2</version>\n"
        + "  <sheets>\n"
        + "    <sheet type=\"Cat\" productName=\"Happy Mouse\" reference=\"" + SHEET_REF + "\""
            + " version=\"0\" state=\"Temporary\">\n"
        + "      <fieldValues>\n"
        + "        <fieldValue name=\"CAT_color\" value=\"WHITE\"/>\n"
        + "        <fieldValue name=\"CAT_description\" "
            + "value=\"Nyanyanyanyanyanyanya!\"/>\n"
        + "        <fieldValue name=\"CAT_furlength\" value=\"SHORT\"/>\n"
        + "        <fieldValue name=\"CAT_ref\" value=\"" + SHEET_REF + "\"/>\n"
        + "      </fieldValues>\n"
        + "    </sheet>\n"
        + "  </sheets>\n"
        + "</gpm>\n";
    
    public static final String SHEET2 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "\n"
        + "<gpm xmlns=\"http://www.airbus.com/topcased/gPM\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"http://www.airbus.com/topcased/gPM\">\n"
        + "  <version>1.2</version>\n"
        + "  <sheets>\n"
        + "    <sheet type=\"Cat\" productName=\"Happy Mouse\" reference=\"" + SHEET_REF + "\""
            + " version=\"0\" state=\"Temporary\">\n"
        + "      <fieldValues>\n"
        + "        <fieldValue name=\"CAT_color\" value=\"GREY\"/>\n"
        + "        <fieldValue name=\"CAT_description\" "
            + "value=\"Nyanyanyanyanyanyanya!\"/>\n"
        + "        <fieldValue name=\"CAT_furlength\" value=\"LONG\"/>\n"
        + "        <fieldValue name=\"CAT_ref\" value=\"" + SHEET_REF + "\"/>\n"
        + "      </fieldValues>\n"
        + "    </sheet>\n"
        + "  </sheets>\n"
        + "</gpm>\n";
}

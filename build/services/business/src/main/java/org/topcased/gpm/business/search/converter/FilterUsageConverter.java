/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.converter;

import java.util.HashMap;

/**
 * FilterUsageConverter convert filter usage used in Xml files and filter usage
 * used in gPM business
 * 
 * @author nveillet
 */
public final class FilterUsageConverter {

    private static HashMap<String, String> staticFilterUsageXmlToGpm;
    static {
        staticFilterUsageXmlToGpm = new HashMap<String, String>();
        staticFilterUsageXmlToGpm.put("table", "TABLE_VIEW");
        staticFilterUsageXmlToGpm.put("tree", "TREE_VIEW");
        staticFilterUsageXmlToGpm.put("all", "BOTH_VIEW");
    }

    private static HashMap<String, String> staticFilterUsageGpmToXml;
    static {
        staticFilterUsageGpmToXml = new HashMap<String, String>();
        staticFilterUsageGpmToXml.put("TABLE_VIEW", "table");
        staticFilterUsageGpmToXml.put("TREE_VIEW", "tree");
        staticFilterUsageGpmToXml.put("BOTH_VIEW", "all");
    }

    /**
     * private constructor
     */
    private FilterUsageConverter() {
    }

    /**
     * Convert an filter usage used in XML file to filter usage of gPM business
     * 
     * @param pXmlFilterUsage
     *            The XML filter usage
     * @return The gPM filter usage
     */
    public static String getXmltoGpm(String pXmlFilterUsage) {
        return staticFilterUsageXmlToGpm.get(pXmlFilterUsage);
    }

    /**
     * Convert an filter usage of gPM business to filter usage used in XML file
     * 
     * @param pGpmFilterUsage
     *            The gPM filter usage
     * @return The XML filter usage
     */
    public static String getGpmtoXml(String pGpmFilterUsage) {
        return staticFilterUsageGpmToXml.get(pGpmFilterUsage);
    }
}

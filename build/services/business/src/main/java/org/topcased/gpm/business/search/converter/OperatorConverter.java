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
 * OperatorConverter convert operator used in Xml files and operator used in gPM
 * business
 * 
 * @author nveillet
 */
public final class OperatorConverter {

    private static HashMap<String, String> staticOperatorsXmlToGpm;
    static {
        staticOperatorsXmlToGpm = new HashMap<String, String>();
        staticOperatorsXmlToGpm.put("=", "=");
        staticOperatorsXmlToGpm.put("!=", "<>");
        staticOperatorsXmlToGpm.put("like", "like");
        staticOperatorsXmlToGpm.put("notLike", "not like");
        staticOperatorsXmlToGpm.put("greaterThan", ">");
        staticOperatorsXmlToGpm.put("greaterOrEqual", ">=");
        staticOperatorsXmlToGpm.put("lessThan", "<");
        staticOperatorsXmlToGpm.put("lessOrEqual", "<=");
        staticOperatorsXmlToGpm.put("since", "since");
        staticOperatorsXmlToGpm.put("other", "other");
    }

    private static HashMap<String, String> staticOperatorsGpmToXml;
    static {
        staticOperatorsGpmToXml = new HashMap<String, String>();
        staticOperatorsGpmToXml.put("=", "=");
        staticOperatorsGpmToXml.put("<>", "!=");
        staticOperatorsGpmToXml.put("like", "like");
        staticOperatorsGpmToXml.put("not like", "notLike");
        staticOperatorsGpmToXml.put(">", "greaterThan");
        staticOperatorsGpmToXml.put(">=", "greaterOrEqual");
        staticOperatorsGpmToXml.put("<", "lessThan");
        staticOperatorsGpmToXml.put("<=", "lessOrEqual");
        staticOperatorsGpmToXml.put("since", "since");
        staticOperatorsGpmToXml.put("other", "other");
    }

    /**
     * private constructor
     */
    private OperatorConverter() {
    }

    /**
     * Convert an operator used in XML file to operator of gPM business
     * 
     * @param pXmlOperator
     *            The XML operator
     * @return The gPM operator
     */
    public static String getXmltoGpm(String pXmlOperator) {
        return staticOperatorsXmlToGpm.get(pXmlOperator);
    }

    /**
     * Convert an operator of gPM business to operator used in XML file
     * 
     * @param pGpmOperator
     *            The gPM operator
     * @return The XML operator
     */
    public static String getGpmtoXml(String pGpmOperator) {
        return staticOperatorsGpmToXml.get(pGpmOperator);
    }
}

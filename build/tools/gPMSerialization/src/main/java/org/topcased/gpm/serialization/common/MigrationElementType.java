/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.common;

/**
 * Migration element type
 * 
 * @author nveillet
 */
public enum MigrationElementType {

    ATTACHED_FILE("attachedfile", "attachedFiles"),

    DICTIONARY("dictionary", "dictionary"),

    ENVIRONMENT("environment", "environments"),

    FILTER("filter", "filters"),

    PRODUCT("product", "products"),

    PRODUCT_LINK("productlink", "productLinks"),

    SHEET("sheet", "sheets"),

    SHEET_LINK("sheetlink", "sheetLinks"),

    USER("user", "users");

    private String defaultDirectory;

    private String name;

    /**
     * Constructor
     * 
     * @param pName
     *            the name
     * @param pDefaultDirectory
     *            the default directory
     */
    private MigrationElementType(String pName, String pDefaultDirectory) {
        name = pName;
        defaultDirectory = pDefaultDirectory;
    }

    /**
     * get default directory
     * 
     * @return the default directory
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
}

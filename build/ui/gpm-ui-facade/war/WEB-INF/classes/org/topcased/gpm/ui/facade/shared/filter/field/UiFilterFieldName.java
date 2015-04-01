/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field;

import java.io.Serializable;

/**
 * UiFilterFieldName
 * 
 * @author nveillet
 */
public class UiFilterFieldName implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 5254360116361733200L;

    private String name;

    private String translatedName;

    private UiFilterFieldNameType type;

    /**
     * Constructor
     */
    public UiFilterFieldName() {
    }

    /**
     * Constructor with values
     * 
     * @param pName
     *            the name
     * @param pTranslatedName
     *            the translated name
     * @param pType
     *            the type
     */
    public UiFilterFieldName(String pName, String pTranslatedName,
            UiFilterFieldNameType pType) {
        name = pName;
        translatedName = pTranslatedName;
        type = pType;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get translated name.
     * 
     * @return translated name.
     */
    public String getTranslatedName() {
        return translatedName;
    }

    /**
     * get type
     * 
     * @return the type
     */
    public UiFilterFieldNameType getType() {
        return type;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Set translated name.
     * 
     * @param pTranslatedName
     *            translated name.
     */
    public void setTranslatedName(String pTranslatedName) {
        translatedName = pTranslatedName;
    }

    /**
     * set type
     * 
     * @param pType
     *            the type to set
     */
    public void setType(UiFilterFieldNameType pType) {
        type = pType;
    }
}

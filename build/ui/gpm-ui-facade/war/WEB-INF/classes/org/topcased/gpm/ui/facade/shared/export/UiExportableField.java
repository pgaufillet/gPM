/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.export;

import java.io.Serializable;
import java.util.List;

/**
 * UiExportableField
 * 
 * @author nveillet
 */
public class UiExportableField implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -6425038168065935750L;

    private List<UiExportableField> exportableFields;

    private String name;

    private String translatedName;

    /**
     * Empty constructor
     */
    public UiExportableField() {
    }

    /**
     * Constructor with values
     * 
     * @param pName
     *            the name
     * @param pTranslatedName
     *            the translated name
     */
    public UiExportableField(String pName, String pTranslatedName) {
        name = pName;
        translatedName = pTranslatedName;
    }

    /**
     * Constructor with values
     * 
     * @param pName
     *            the name
     * @param pTranslatedName
     *            the translated name
     * @param pExportableFields
     *            the exportable fields
     */
    public UiExportableField(String pName, String pTranslatedName,
            List<UiExportableField> pExportableFields) {
        name = pName;
        translatedName = pTranslatedName;
        exportableFields = pExportableFields;
    }

    /**
     * get exportable fields
     * 
     * @return the exportable fields
     */
    public List<UiExportableField> getExportableFields() {
        return exportableFields;
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
     * get translated name
     * 
     * @return the translated name
     */
    public String getTranslatedName() {
        return translatedName;
    }

    /**
     * set exportable fields
     * 
     * @param pExportableFields
     *            the exportable fields to set
     */
    public void setExportableFields(List<UiExportableField> pExportableFields) {
        exportableFields = pExportableFields;
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
     * set translated name
     * 
     * @param pTranslatedName
     *            the translated name to set
     */
    public void setTranslatedName(String pTranslatedName) {
        translatedName = pTranslatedName;
    }
}

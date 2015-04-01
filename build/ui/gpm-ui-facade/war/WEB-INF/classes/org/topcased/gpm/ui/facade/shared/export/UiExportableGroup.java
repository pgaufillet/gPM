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
 * UiExportableGroup
 * 
 * @author nveillet
 */
public class UiExportableGroup implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 4187014090419698012L;

    private List<UiExportableField> exportableFields;

    private String name;

    /**
     * Empty constructor
     */
    public UiExportableGroup() {
    }

    /**
     * Constructor with values
     * 
     * @param pName
     *            the name
     * @param pExportableFields
     *            the exportable fields
     */
    public UiExportableGroup(String pName,
            List<UiExportableField> pExportableFields) {
        name = pName;
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
}

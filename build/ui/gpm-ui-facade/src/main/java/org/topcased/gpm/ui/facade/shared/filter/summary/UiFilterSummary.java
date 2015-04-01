/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.summary;

import java.io.Serializable;

import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;

/**
 * UiFilterSummary
 * 
 * @author nveillet
 */
public class UiFilterSummary implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 2182057898740941924L;

    private boolean deletable;

    private String description;

    private boolean editable;

    private boolean executable;

    private String id;

    private String name;

    private UiFilterVisibility visibility;

    /**
     * Create UiFilterSummary
     */
    public UiFilterSummary() {
    }

    /**
     * Create UiFilterSummary with values
     * 
     * @param pId
     *            the identifier
     * @param pName
     *            the name (translated)
     * @param pDescription
     *            the description (translated)
     * @param pVisibility
     *            the filter visibility
     * @param pEditable
     *            the editable access
     * @param pExecutable
     *            the executable access
     * @param pDeletable
     *            the deletable access
     */
    public UiFilterSummary(String pId, String pName, String pDescription,
            UiFilterVisibility pVisibility, boolean pEditable,
            boolean pExecutable, boolean pDeletable) {
        id = pId;
        name = pName;
        description = pDescription;
        visibility = pVisibility;
        editable = pEditable;
        executable = pExecutable;
        deletable = pDeletable;
    }

    /**
     * get description (translated)
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * get id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * get name (translated)
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get visibility
     * 
     * @return the visibility
     */
    public UiFilterVisibility getVisibility() {
        return visibility;
    }

    /**
     * get deletable access
     * 
     * @return the deletable access
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * get editable access
     * 
     * @return the editable access
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * get executable access
     * 
     * @return the executable access
     */
    public boolean isExecutable() {
        return executable;
    }

    /**
     * set deletable access
     * 
     * @param pDeletable
     *            the deletable access to set
     */
    public void setDeletable(boolean pDeletable) {
        deletable = pDeletable;
    }

    /**
     * set description (translated)
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * set editable access
     * 
     * @param pEditable
     *            the editable access to set
     */
    public void setEditable(boolean pEditable) {
        editable = pEditable;
    }

    /**
     * set executable access
     * 
     * @param pExecutable
     *            the executable access to set
     */
    public void setExecutable(boolean pExecutable) {
        executable = pExecutable;
    }

    /**
     * set id
     * 
     * @param pId
     *            the id to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * set name (translated)
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set visibility
     * 
     * @param pVisibility
     *            the visibility to set
     */
    public void setVisibility(UiFilterVisibility pVisibility) {
        visibility = pVisibility;
    }
}

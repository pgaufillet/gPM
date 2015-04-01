/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class FieldsContainer.
 * 
 * @author llatil
 */
public abstract class FieldsContainer extends ExtensionsContainer {

    /** Generated UID */
    private static final long serialVersionUID = -5933293378824434204L;

    /** The fields. */
    private List<Field> fields;

    /** The display hints. */
    private List<DisplayHint> displayHints;

    /** The display groups. */
    private List<DisplayGroup> displayGroups;

    /**
     * Creatable access control attribute (indicates if a user can create a
     * values container from this type or not)
     */
    @XStreamAsAttribute
    private Boolean creatable;

    /**
     * Updatable access control attribute (indicates if a user can update a
     * values container from this type or not)
     */
    @XStreamAsAttribute
    private Boolean updatable;

    /**
     * Confidential access control attribute (indicates if a user can see a
     * values container from this type or not)
     */
    @XStreamAsAttribute
    private Boolean confidential;

    /**
     * Deletable access control attribute (indicates if a user can delete a
     * values container from this type or not)
     */
    @XStreamAsAttribute
    private Boolean deletable;

    /**
     * Get the list of fields defined in this container.
     * 
     * @return List of field definitions.
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Add field definitions to this container.
     * 
     * @param pFields
     *            List of field definitions to add
     */
    public void addFields(Collection<? extends Field> pFields) {
        if (null == fields) {
            fields = new ArrayList<Field>(pFields);
        }
        else {
            fields.addAll(pFields);
        }
    }

    /**
     * Get the list of display hints defined in this container.
     * 
     * @return List of display hints
     */
    public List<DisplayHint> getDisplayHints() {
        return displayHints;
    }

    /**
     * Add display hints to this container.
     * 
     * @param pHints
     *            List of display hints to add
     */
    public void addDisplayHints(Collection<? extends DisplayHint> pHints) {
        if (null == displayHints) {
            displayHints = new ArrayList<DisplayHint>(pHints);
        }
        else {
            displayHints.addAll(pHints);
        }
    }

    /**
     * Gets the display groups.
     * 
     * @return the display groups
     */
    public List<DisplayGroup> getDisplayGroups() {
        return displayGroups;
    }

    /**
     * Sets the display groups.
     * 
     * @param pDisplayGroups
     *            the display groups
     */
    public void setDisplayGroups(List<DisplayGroup> pDisplayGroups) {
        displayGroups = pDisplayGroups;
    }

    /**
     * Adds the display groups.
     * 
     * @param pDisplayGroups
     *            the display groups
     */
    public void addDisplayGroups(
            Collection<? extends DisplayGroup> pDisplayGroups) {
        if (null == displayGroups) {
            displayGroups = new ArrayList<DisplayGroup>(pDisplayGroups);
        }
        else {
            displayGroups.addAll(pDisplayGroups);
        }
    }

    /**
     * Sets the fields.
     * 
     * @param pFields
     *            the new fields
     */
    public void setFields(List<Field> pFields) {
        this.fields = pFields;
    }

    /**
     * Sets the display hints.
     * 
     * @param pDisplayHints
     *            the new display hints
     */
    public void setDisplayHints(List<DisplayHint> pDisplayHints) {
        this.displayHints = pDisplayHints;
    }

    public Boolean getCreatable() {
        return creatable;
    }

    public void setCreatable(Boolean pCreatable) {
        this.creatable = pCreatable;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean pUpdatable) {
        this.updatable = pUpdatable;
    }

    public Boolean getConfidential() {
        return confidential;
    }

    public void setConfidential(Boolean pConfidential) {
        this.confidential = pConfidential;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean pDeletable) {
        this.deletable = pDeletable;
    }

}

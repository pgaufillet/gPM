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

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A SheetType maps a Sheet in gPM and is used for XML marshalling/unmarshalling
 * of a SheetType. Here a sheet type is just a list of fields.
 * 
 * @author sidjelli
 */
@XStreamAlias("sheetType")
public class SheetType extends FieldsContainer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3744718108961054144L;

    /** The reference field. */
    private MultipleField referenceField;

    /** The selectable. */
    @XStreamAsAttribute
    private Boolean selectable;

    /** Specify if this sheet type supports versions mgt. */
    @XStreamAsAttribute
    private Boolean versionable;

    /** The states. */
    private List<State> states;

    /** Technical identifier of the sheet. */
    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String autolocking;

    /**
     * Checks if is selectable.
     * 
     * @return the boolean
     */
    public Boolean isSelectable() {
        if (null == selectable) {
            return true;
        }
        return selectable;
    }

    /**
     * Sets the selectable.
     * 
     * @param pSelectable
     *            the selectable
     */
    public void setSelectable(Boolean pSelectable) {
        selectable = pSelectable;
    }

    /** The lifecycle resource. */
    private String lifecycleResource;

    /**
     * Gets the lifecycle resource.
     * 
     * @return the lifecycle resource
     */
    public String getLifecycleResource() {
        if (null != lifecycleResource) {
            return lifecycleResource.trim();
        }
        return null;
    }

    /**
     * Gets the states.
     * 
     * @return the states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * Sets the states.
     * 
     * @param pStates
     *            The states.
     */
    public void setStates(List<State> pStates) {
        states = pStates;
    }

    /**
     * Gets the reference field.
     * 
     * @return the reference field
     */
    public MultipleField getReferenceField() {
        return referenceField;
    }

    /**
     * Set the reference field definition.
     * 
     * @param pRefField
     *            Reference field definition
     */
    public void setReferenceField(MultipleField pRefField) {
        referenceField = pRefField;
    }

    /**
     * Gets the sheet type id.
     * 
     * @return the sheet type id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the sheet type id.
     * 
     * @param pId
     *            the sheet type id.
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * Enable the revision support.
     * 
     * @param pVersionable
     *            True to enable the revision support.
     */
    public final void setVersionable(Boolean pVersionable) {
        versionable = pVersionable;
    }

    /**
     * Checks if revision support is enabled.
     * 
     * @return True if revision support enabled.
     */
    public final Boolean isVersionable() {
        return null != versionable && versionable.booleanValue();
    }

    public String getAutolocking() {
        return autolocking;
    }

    public void setAutolocking(String pAutolocking) {
        autolocking = pAutolocking;
    }
}

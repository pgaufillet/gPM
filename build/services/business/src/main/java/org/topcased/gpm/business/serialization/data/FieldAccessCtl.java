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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class FieldAccessCtl.
 * 
 * @author llatil
 */
@XStreamAlias("fieldAccess")
public class FieldAccessCtl extends AccessCtl {

    /** The field key. */
    @XStreamAsAttribute
    private String fieldKey;

    /** The updatable. */
    @XStreamAsAttribute
    private Boolean updatable;

    /** The confidential. */
    @XStreamAsAttribute
    private Boolean confidential;

    /** The mandatory. */
    @XStreamAsAttribute
    private Boolean mandatory;

    /** The exportable. */
    @XStreamAsAttribute
    private Boolean exportable;

    /**
     * The visibleType. In case of a link's field access, this is the type of
     * the sheet in which the link (and so the field) is displayed. This sheet
     * type is either the origin or the destination of the link type.
     */
    @XStreamAsAttribute
    private String visibleType;

    /**
     * Checks if is exportable.
     * 
     * @return the boolean
     */
    public Boolean isExportable() {
        return exportable;
    }

    /**
     * Sets the exportable.
     * 
     * @param pExportable
     *            the new exportable
     */
    public void setExportable(Boolean pExportable) {
        exportable = pExportable;
    }

    /**
     * Sets the updatable.
     * 
     * @param pUpdatable
     *            the new updatable
     */
    public void setUpdatable(Boolean pUpdatable) {
        updatable = pUpdatable;
    }

    /**
     * Checks if is mandatory.
     * 
     * @return the boolean
     */
    public Boolean isMandatory() {
        return mandatory;
    }

    /**
     * Sets the mandatory.
     * 
     * @param pMandatory
     *            the new mandatory
     */
    public void setMandatory(Boolean pMandatory) {
        mandatory = pMandatory;
    }

    /**
     * Checks if is confidential.
     * 
     * @return the boolean
     */
    public Boolean isConfidential() {
        return confidential;
    }

    /**
     * Checks if is updatable.
     * 
     * @return the boolean
     */
    public Boolean isUpdatable() {
        return updatable;
    }

    /**
     * Gets the field key.
     * 
     * @return the field key
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * Gets the sheet type displaying the link field.
     * 
     * @return the sheet type displaying the link field
     */
    public String getVisibleType() {
        return visibleType;
    }
}

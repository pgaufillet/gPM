/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ****************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PointerFieldValueData.
 * 
 * @author ahaugomm
 */
@XStreamAlias("pointerFieldValue")
public class PointerFieldValueData extends FieldValueData {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4859510932008008292L;

    /**
     * ID (instantiation ID or technical ID) of the container on which the value
     * of the pointer field is taken.
     */
    @XStreamAsAttribute
    private String referencedContainerId;

    /**
     * Functional reference of the container on which the value of the pointer
     * field is taken.
     */
    @XStreamAsAttribute
    private String referencedContainerRef;

    /**
     * Product of the container on which the value of the pointer field is
     * taken.
     */
    @XStreamAsAttribute
    private String referencedProduct;

    /** LabelKey of the pointed field. */
    @XStreamAsAttribute
    private String referencedFieldLabel;

    /**
     * Constructor for mutable / immutable switch
     */
    public PointerFieldValueData() {
        super();
    }

    /**
     * Create a new Pointer field value from field name.
     * 
     * @param pName
     *            pointer field name
     */
    public PointerFieldValueData(String pName) {
        super(pName);
    }

    /**
     * Gets the referenced container id.
     * 
     * @return the referenced container id
     */
    public String getReferencedContainerId() {
        return referencedContainerId;
    }

    /**
     * Sets the referenced container id.
     * 
     * @param pReferencedContainerId
     *            the new referenced container id
     */
    public void setReferencedContainerId(String pReferencedContainerId) {
        this.referencedContainerId = pReferencedContainerId;
    }

    /**
     * Gets the referenced field label.
     * 
     * @return the referenced field label
     */
    public String getReferencedFieldLabel() {
        return referencedFieldLabel;
    }

    /**
     * Sets the referenced field label.
     * 
     * @param pReferencedFieldLabel
     *            the new referenced field label
     */
    public void setReferencedFieldLabel(String pReferencedFieldLabel) {
        this.referencedFieldLabel = pReferencedFieldLabel;
    }

    public String getReferencedContainerRef() {
        return referencedContainerRef;
    }

    public void setReferencedContainerRef(String pReferencedContainerRef) {
        this.referencedContainerRef = pReferencedContainerRef;
    }

    public String getReferencedProduct() {
        return referencedProduct;
    }

    public void setReferencedProduct(String pReferencedProduct) {
        this.referencedProduct = pReferencedProduct;
    }
}

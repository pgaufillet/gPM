/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class implements a pointer field.
 * 
 * @author ahaugomm
 */
@XStreamAlias("pointerField")
public class PointerField extends Field {

    private static final long serialVersionUID = 3472844671028955197L;

    @XStreamAsAttribute
    private String linkType;

    @XStreamAsAttribute
    private String referenceFieldLabel;

    /**
     * Get the link type (NULL, or link type name if automatic fill of pointer
     * fields)
     * 
     * @return link type name
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * Set the link type name for automatic fill of pointer fields
     * 
     * @param pLinkType
     *            the link type name
     */
    public void setLinkType(String pLinkType) {
        this.linkType = pLinkType;
    }

    /**
     * Get the reference field label (NULL, or linked sheet field label if
     * automatic fill of pointer fields)
     * 
     * @return reference field label key
     */
    public String getReferenceFieldLabel() {
        return referenceFieldLabel;
    }

    /**
     * Set the reference field label for automatic fill of pointer fields
     * 
     * @param pReferenceFieldLabel reference field label
     */
    public void setReferenceFieldLabel(String pReferenceFieldLabel) {
        this.referenceFieldLabel = pReferenceFieldLabel;
    }
}

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

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class DisplayHint.
 * 
 * @author llatil
 */
public class DisplayHint extends AttributesContainer implements Serializable {

    private static final long serialVersionUID = 898920285741325148L;

    /** The label key. */
    @XStreamAsAttribute
    private String labelKey;

    /**
     * Get the label key for this display hint.
     * 
     * @return Label key for this display hint
     */
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * Set the label key for this display hint.
     * 
     * @param pLabelKey
     *            Label key
     */
    public void setLabelKey(String pLabelKey) {
        labelKey = pLabelKey;
    }
}

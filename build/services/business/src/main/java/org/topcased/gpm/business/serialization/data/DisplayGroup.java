/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * A displayGroup maps to a DisplayGroup in gPM and is used for XML
 * marshalling/unmarshalling. Here a displayGroup is just a list of Fields with
 * a name.
 * 
 * @author sidjelli
 */
@XStreamAlias("displayGroup")
public class DisplayGroup extends DescribedElement {

    /** Generated UID */
    private static final long serialVersionUID = 2618582370743014716L;

    /** The fields. */
    @XStreamImplicit(itemFieldName = "fieldRef")
    private List<FieldRef> fields;

    /** Is display group opened by default */
    @XStreamAsAttribute
    @XStreamAlias("isOpened")
    private Boolean opened = true;

    /**
     * Gets the fields.
     * 
     * @return the fields
     */
    public List<FieldRef> getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     * 
     * @param pFields
     *            the fields
     */
    public void setFields(List<FieldRef> pFields) {
        fields = pFields;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean pOpened) {
        opened = pOpened;
    }
}

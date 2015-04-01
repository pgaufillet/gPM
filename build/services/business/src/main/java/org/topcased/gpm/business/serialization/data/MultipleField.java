/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin),
 *      Nicolas Bousquet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import org.topcased.gpm.business.fields.service.FieldsService;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A MultipleField maps a Sheet in gPM and is used for XML
 * marshaling/unmarshaling of a MultipleField. Here a multipleField is just an
 * ArrayList of Field and has also a name.
 * 
 * @author nbousque
 */
@XStreamAlias("multipleField")
public class MultipleField extends Field {

    /** Generated UID */
    private static final long serialVersionUID = 6923106525080593023L;

    /** The fields. */
    private List<Field> fields;

    /** The field separator. */
    @XStreamAsAttribute
    protected String fieldSeparator = FieldsService.DEFAULT_FIELD_SEPARATOR;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.data.Field#getMultiple()
     */
    public Boolean getMultiple() {
        return true;
    }

    /**
     * Gets the field separator.
     * 
     * @return the field separator
     */
    public String getFieldSeparator() {
        return fieldSeparator;
    }

    /**
     * Sets the field separator.
     * 
     * @param pFieldSeparator
     *            the field separator
     */
    public void setFieldSeparator(String pFieldSeparator) {
        this.fieldSeparator = pFieldSeparator;
    }

    /**
     * Gets the fields.
     * 
     * @return the fields
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     * 
     * @param pFields
     *            the fields
     */
    public void setFields(List<Field> pFields) {
        fields = pFields;
    }
}

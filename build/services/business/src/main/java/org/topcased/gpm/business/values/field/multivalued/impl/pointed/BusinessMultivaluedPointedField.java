/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.multivalued.impl.pointed;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;

/**
 * BusinessMultivaluedPointedField
 * 
 * @author nveillet
 */
public class BusinessMultivaluedPointedField extends
        AbstractBusinessPointedField implements
        BusinessMultivaluedField<BusinessField> {
    private final static String DEFAULT_SEPARATOR = " ";

    final private LinkedList<BusinessField> fields;

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pFields
     *            The fields
     */
    public BusinessMultivaluedPointedField(String pFieldName,
            String pFieldDescription, List<BusinessField> pFields) {
        super(pFieldName, pFieldDescription);
        fields = new LinkedList<BusinessField>(pFields);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    public BusinessField addLine() {
        throw new MethodNotImplementedException("addLine");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#get(int)
     */
    public BusinessField get(int pIndex) {
        return fields.get(pIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public String getAsString() {
        throw new MethodNotImplementedException("getAsString");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#getFirst()
     */
    public BusinessField getFirst() {
        return fields.getFirst();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    public boolean hasSameValues(BusinessField pOther) {
        final BusinessMultivaluedField<BusinessField> lOther =
                (BusinessMultivaluedField<BusinessField>) pOther;

        // The same of number of line is needed
        if (fields.size() != lOther.size()) {
            return false;
        }
        // All elements must have same values
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).hasSameValues(lOther.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<BusinessField> iterator() {
        return fields.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    public BusinessField removeLine() {
        throw new MethodNotImplementedException("removeLine");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine(int)
     */
    public BusinessField removeLine(int pIndex) {
        throw new MethodNotImplementedException("removeLine");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#size()
     */
    public int size() {
        return fields.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        final Iterator<BusinessField> lFields = iterator();
        final StringBuffer lStringValue = new StringBuffer();
        final String lFieldSeparator = DEFAULT_SEPARATOR;

        while (lFields.hasNext()) {
            lStringValue.append(lFields.next().toString());
            if (lFields.hasNext()) {
                lStringValue.append(lFieldSeparator);
            }
        }

        return lStringValue.toString();
    }

}

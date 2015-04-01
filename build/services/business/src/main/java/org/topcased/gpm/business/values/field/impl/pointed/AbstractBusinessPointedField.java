/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.impl.pointed;

import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.values.field.BusinessField;

/**
 * AbstractBusinessPointedField
 * 
 * @author nveillet
 */
public abstract class AbstractBusinessPointedField implements BusinessField {

    final private String fieldDescription;

    final private String fieldName;

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     */
    public AbstractBusinessPointedField(String pFieldName,
            String pFieldDescription) {
        fieldName = pFieldName;
        fieldDescription = pFieldDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        throw new MethodNotImplementedException("clear");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(BusinessField pOther) {
        throw new MethodNotImplementedException("copy");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    public abstract String getAsString();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    public String getFieldDescription() {
        return fieldDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isConfidential()
     */
    public boolean isConfidential() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        throw new MethodNotImplementedException("isEmpty");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    public boolean isExportable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    public Boolean isMandatory() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    public boolean isUpdatable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        throw new MethodNotImplementedException("reset");
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}

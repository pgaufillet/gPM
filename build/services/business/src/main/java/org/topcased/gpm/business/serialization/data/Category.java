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

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A category maps to a Category in gPM. Contains a list of CategoryValue.
 * 
 * @author llatil
 */
@XStreamAlias("category")
public class Category extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = -4068768070318482714L;

    /** The values. */
    private List<CategoryValue> values;

    /** The access. */
    @XStreamAsAttribute
    private String access;

    /**
     * Gets the values.
     * 
     * @return the values
     */
    public List<CategoryValue> getValues() {
        return values;
    }

    /**
     * Sets the values.
     * 
     * @param pValues
     *            the values
     */
    public void setValues(List<CategoryValue> pValues) {
        values = pValues;
    }

    /**
     * Gets the access level of the category.
     * 
     * @return the access (can be "Process", "Product" or "User")
     */
    public final String getAccess() {
        return access;
    }

    /**
     * Sets the access level of the category.
     * 
     * @param pAccess
     *            the new access
     */
    public final void setAccess(String pAccess) {
        access = pAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.data.NamedElement#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Category) {
            Category lOther = (Category) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(access, lOther.access)) {
                return false;
            }
            if (!ListUtils.isEqualList(values, lOther.values)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // NamedElement hashcode is sufficient
        return super.hashCode();
    }
}

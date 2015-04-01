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

import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A environment maps to a Environment in gPM and is used for XML
 * marshalling/unmarshalling. Here a environment is just a list of Fields with a
 * name and environment.
 * 
 * @author sidjelli
 */
@XStreamAlias("environment")
public class Environment extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = 3954015936979780417L;

    /** The categories. */
    private List<Category> categories;

    /** Is this environment public. */
    @XStreamAsAttribute
    private Boolean isPublic;

    /**
     * Gets the categories.
     * 
     * @return the categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     * 
     * @param pCategories
     *            the categories
     */
    public void setCategories(List<Category> pCategories) {
        categories = pCategories;
    }

    /**
     * get isPublic
     * 
     * @return the isPublic
     */
    public Boolean getIsPublic() {
        return isPublic;
    }

    /**
     * set isPublic
     * 
     * @param pIsPublic
     *            the isPublic to set
     */
    public void setIsPublic(Boolean pIsPublic) {
        isPublic = pIsPublic;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.data.NamedElement#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Environment) {
            Environment lOther = (Environment) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (isPublic != lOther.isPublic) {
                return false;
            }
            if (!CollectionUtils.equals(categories, lOther.categories)) {
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
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}

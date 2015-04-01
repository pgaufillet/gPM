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

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class FieldResult.
 * 
 * @author llatil
 */
@XStreamAlias("fieldResult")
public class FieldResult extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = 8831126352760193782L;

    @XStreamAsAttribute
    private Boolean displayed;

    /** The label used to filter column name */
    @XStreamAsAttribute
    private String label;

    /** The sort. */
    @XStreamAsAttribute
    private String sort;

    /**
     * get displayed
     * 
     * @return the displayed
     */
    public Boolean getDisplayed() {
        return displayed;
    }

    /**
     * Gets label
     * 
     * @return the label used to filter column name
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the sort.
     * 
     * @return the sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * set displayed
     * 
     * @param pDisplayed
     *            the displayed to set
     */
    public void setDisplayed(Boolean pDisplayed) {
        this.displayed = pDisplayed;
    }

    /**
     * Set label
     * 
     * @param pLabel
     *            the label used to filter column name
     */
    public void setLabel(String pLabel) {
        this.label = pLabel;
    }

    /**
     * set sort
     * 
     * @param pSort
     *            the sort to set
     */
    public void setSort(String pSort) {
        this.sort = pSort;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof FieldResult) {
            FieldResult lOther = (FieldResult) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(label, lOther.label)) {
                return false;
            }
            if (!StringUtils.equals(sort, lOther.sort)) {
                return false;
            }
            if (displayed != lOther.displayed) {
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

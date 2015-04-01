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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class implement a concrete choice field.
 * 
 * @author sidjelli
 */
@XStreamAlias("choiceField")
public class ChoiceField extends Field {

    private static final long serialVersionUID = 3099665104179057262L;

    /** The category of the choiceField. */
    @XStreamAsAttribute
    private String categoryName;

    /** Default value for the field. */
    @XStreamAsAttribute
    private String defaultValue;

    /**
     * Get the default value of this field.
     * 
     * @return Default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value.
     * 
     * @param pDefaultValue
     *            the new default value
     */
    public void setDefaultValue(String pDefaultValue) {
        defaultValue = pDefaultValue;
    }

    /**
     * Sets the category name.
     * 
     * @param pCategoryName
     *            the new category name
     */
    public void setCategoryName(String pCategoryName) {
        categoryName = pCategoryName;
    }

    /**
     * Get the category name defining the acceptable values for this field.
     * 
     * @return Category name.
     */
    public String getCategoryName() {
        return categoryName;
    }
}

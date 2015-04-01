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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A dictionary stores the list of categories, and the list of environments.
 * 
 * @author llatil
 */
@XStreamAlias("dictionary")
public class Dictionary {

    /** List of categories. */
    private List<Category> categories;

    /** List of environments. */
    private List<Environment> environments;

    /**
     * Gets the categories.
     * 
     * @return the categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Gets the environments.
     * 
     * @return the environments
     */
    public List<Environment> getEnvironments() {
        return environments;
    }

    /**
     * set categories
     * 
     * @param pCategories
     *            the categories to set
     */
    public void setCategories(List<Category> pCategories) {
        this.categories = pCategories;
    }

    /**
     * set environments
     * 
     * @param pEnvironments
     *            the environments to set
     */
    public void setEnvironments(List<Environment> pEnvironments) {
        this.environments = pEnvironments;
    }
}

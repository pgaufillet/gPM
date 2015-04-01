/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.dictionary;

import java.io.Serializable;

/**
 * Category for management.
 * 
 * @author jlouisy
 */
public class UiCategory implements Serializable, Comparable<UiCategory> {

    private static final long serialVersionUID = 1682064817868279958L;

    private String id;

    private String name;

    private String translatedName;

    /**
     * Empty constructor for serialization.
     */
    public UiCategory() {
    }

    /**
     * Create UiCategory.
     * 
     * @param pId
     *            Category Id.
     * @param pName
     *            Category Name.
     * @param pTranslatedName
     *            Translated name.
     */
    public UiCategory(String pId, String pName, String pTranslatedName) {
        id = pId;
        name = pName;
        translatedName = pTranslatedName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(UiCategory pOther) {
        return this.getName().compareTo(pOther.getName());
    }

    /**
     * get id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get translated name
     * 
     * @return the translated name
     */
    public String getTranslatedName() {
        return translatedName;
    }

    /**
     * set id
     * 
     * @param pId
     *            the id to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set translated name
     * 
     * @param pTranslatedName
     *            the translated name to set
     */
    public void setTranslatedName(String pTranslatedName) {
        translatedName = pTranslatedName;
    }
}

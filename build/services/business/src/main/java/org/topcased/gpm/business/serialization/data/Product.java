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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Product.
 * 
 * @author llatil
 */
@XStreamAlias("product")
public class Product extends ValuesContainerData {

    /** Generated UID */
    private static final long serialVersionUID = 9041844882754211038L;

    /** The parents. */
    @XStreamAlias(value = "parents", impl = ArrayList.class)
    @XStreamAsAttribute
    private List<Parent> parents;

    /** The parent name. */
    @XStreamAlias("parentName")
    @XStreamAsAttribute
    private String parentName;

    /** The environment names. */
    @XStreamAlias(value = "environmentNames", impl = LinkedList.class)
    @XStreamAsAttribute
    private List<EnvironmentName> environmentNames;

    /** The environment name. */
    @XStreamAlias("envName")
    @XStreamAsAttribute
    private String envName;

    /** The description */
    @XStreamAlias("productDescription")
    @XStreamAsAttribute
    private String productDescription;

    /**
     * Gets the parent name.
     * 
     * @return the parent name
     */
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String pParentName) {
        parentName = pParentName;
    }

    /**
     * Gets the parents.
     * 
     * @return the parents
     */
    public final List<NamedElement> getParents() {
        if (parents == null) {
            if (StringUtils.isNotEmpty(parentName)) {
                return Collections.singletonList(new NamedElement(parentName));
            }
            return null;
        }
        // else
        return new ArrayList<NamedElement>(parents);
    }

    /**
     * get names of parent products as a string list
     * 
     * @return the parent names
     */
    public List<String> getParentsAsStrings() {
        if (null == getParents()) {
            return Collections.emptyList();
        }
        final List<String> lParentNames = new ArrayList<String>();

        for (NamedElement lElem : getParents()) {
            lParentNames.add(lElem.getName());
        }
        return lParentNames;
    }

    /**
     * Sets the parents.
     * 
     * @param pParents
     *            the new parents
     */
    public final void setParents(List<NamedElement> pParents) {
        if (null == parents) {
            parents = new ArrayList<Parent>();
        }
        else {
            parents.clear();
        }
        for (NamedElement lCurrentParentName : pParents) {
            parents.add(new Parent(lCurrentParentName.getName()));
        }
    }

    /**
     * Copy the given list into the parents.
     * 
     * @param pParents
     *            the new parents
     */
    public final void copyInParents(Collection<? extends String> pParents) {
        // Cannot have both parentName & parents list defined.
        parentName = null;

        if (pParents == null || pParents.isEmpty()) {
            parents = null;
        }
        else {
            if (null == parents) {
                parents = new ArrayList<Parent>();
            }
            else {
                parents.clear();
            }
            for (String lCurrentParentName : pParents) {
                parents.add(new Parent(lCurrentParentName));
            }
        }
    }

    /**
     * get environmentNames
     * 
     * @return the environmentNames
     */
    public List<NamedElement> getEnvironmentNames() {
        final List<NamedElement> lEnvNames = new ArrayList<NamedElement>();

        if (environmentNames != null) {
            lEnvNames.addAll(environmentNames);
        }
        if (envName != null) {
            lEnvNames.add(new NamedElement(envName));
        }
        return lEnvNames;
    }

    /**
     * get names of environments as a string list
     * 
     * @return the environment names
     */
    public List<String> getEnvironmentNamesAsStrings() {
        final List<String> lEnvNames = new ArrayList<String>();

        for (NamedElement lElem : getEnvironmentNames()) {
            lEnvNames.add(lElem.getName());
        }
        return lEnvNames;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // ValuesContainerData hashcode is sufficient
        return super.hashCode();
    }
    
    /**
     * set environmentNames
     * 
     * @param pEnvironmentNames
     *            the environmentNames to set
     */
    public void setEnvironmentNames(List<NamedElement> pEnvironmentNames) {
        envName = null;
        if (pEnvironmentNames == null) {
            environmentNames = null;
        }
        else {
            environmentNames = new ArrayList<EnvironmentName>();
            for (NamedElement lEnvName : pEnvironmentNames) {
                environmentNames.add(new EnvironmentName(lEnvName.getName()));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Product) {
            Product lOther = (Product) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(parentName, lOther.parentName)) {
                return false;
            }
            if (!StringUtils.equals(envName, lOther.envName)) {
                return false;
            }
            if (!CollectionUtils.equals(environmentNames,
                    lOther.environmentNames)) {
                return false;
            }
            if (!CollectionUtils.equals(parents, lOther.parents)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getDescription() {
        return productDescription;
    }

    /**
     * set description
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        productDescription = pDescription;
    }
}

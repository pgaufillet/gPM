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
import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Filter.
 * 
 * @author llatil
 */
@XStreamAlias("filter")
public class Filter extends DescribedElement {

    /** Generated UID */
    private static final long serialVersionUID = 1368916224548782885L;

    /** The label key. */
    @XStreamAsAttribute
    private String labelKey;

    /** The user login. */
    private String userLogin;

    /** The product name. */
    private String productName;

    /** The filter usage. */
    @XStreamAsAttribute
    private String filterUsage;

    /** The result summary. */
    private List<FieldResult> resultSummary;

    /** The scope. */
    private List<ProductScope> scope;

    /** The containers. */
    private List<NamedElement> containers;

    /** Hidden filter. */
    @XStreamAsAttribute
    private Boolean hidden;

    /**
     * Gets the label key.
     * 
     * @return the label key
     */
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * set labelKey
     * 
     * @param pLabelKey
     *            the labelKey to set
     */
    public void setLabelKey(String pLabelKey) {
        labelKey = pLabelKey;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * set productName
     * 
     * @param pProductName
     *            the productName to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * Gets the user login.
     * 
     * @return the user login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * set userLogin
     * 
     * @param pUserLogin
     *            the userLogin to set
     */
    public void setUserLogin(String pUserLogin) {
        this.userLogin = pUserLogin;
    }

    /**
     * Gets the containers.
     * 
     * @return the containers
     */
    public List<NamedElement> getContainers() {
        return containers;
    }

    /**
     * set containers
     * 
     * @param pContainers
     *            the containers to set
     */
    public void setContainers(List<NamedElement> pContainers) {
        containers = pContainers;
    }

    /**
     * Gets the scope.
     * 
     * @return the scope
     */
    public List<ProductScope> getScope() {
        return scope;
    }

    /**
     * set scope
     * 
     * @param pScope
     *            the scope to set
     */
    public void setScope(List<ProductScope> pScope) {
        scope = pScope;
    }

    /** The criteria. */
    private List<CriteriaGroup> criteria;

    /**
     * Gets the criteria groups.
     * 
     * @return the criteria groups
     */
    public List<CriteriaGroup> getCriteriaGroups() {
        return criteria;
    }

    /**
     * set criteria groups
     * 
     * @param pCriteriaGroup
     *            the criteria groups to set
     */
    public void setCriteriaGroups(List<CriteriaGroup> pCriteriaGroup) {
        criteria = pCriteriaGroup;
    }

    /**
     * Gets the result summary.
     * 
     * @return the result summary
     */
    public List<FieldResult> getResultSummary() {
        return resultSummary;
    }

    /**
     * set resultSummary
     * 
     * @param pResultSummary
     *            the resultSummary to set
     */
    public void setResultSummary(List<FieldResult> pResultSummary) {
        this.resultSummary = pResultSummary;
    }

    /**
     * Gets the filter usage.
     * 
     * @return the filter usage
     */
    public String getFilterUsage() {
        return filterUsage;
    }

    /**
     * set filterUsage
     * 
     * @param pFilterUsage
     *            the filterUsage to set
     */
    public void setFilterUsage(String pFilterUsage) {
        filterUsage = pFilterUsage;
    }

    /**
     * get hidden.
     * 
     * @return the hidden
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * set hidden.
     * 
     * @param pHidden
     *            the hidden to set
     */
    public void setHidden(Boolean pHidden) {
        this.hidden = pHidden;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Filter) {
            Filter lOther = (Filter) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(labelKey, lOther.labelKey)) {
                return false;
            }
            if (!StringUtils.equals(userLogin, lOther.userLogin)) {
                return false;
            }
            if (!StringUtils.equals(productName, lOther.productName)) {
                return false;
            }
            if (!StringUtils.equals(filterUsage, lOther.filterUsage)) {
                return false;
            }
            if (hidden != lOther.hidden) {
                return false;
            }
            if (!CollectionUtils.equals(containers, lOther.containers)) {
                return false;
            }
            if (!CollectionUtils.equals(scope, lOther.scope)) {
                return false;
            }
            if (!ListUtils.isEqualList(resultSummary, lOther.resultSummary)) {
                return false;
            }

            return true;
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // DescribedElement hashcode is sufficient
        return super.hashCode();
    }
}

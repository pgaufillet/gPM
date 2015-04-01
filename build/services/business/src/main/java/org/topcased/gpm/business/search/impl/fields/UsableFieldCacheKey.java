/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.fields;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.cache.CacheKey;
import org.topcased.gpm.business.search.impl.SearchUtils;

/**
 * Usable fields cache key.
 * 
 * @author mkargbo
 */
public class UsableFieldCacheKey extends CacheKey {

    /** serialVersionUID */
    private static final long serialVersionUID = 3063733049904252942L;

    /** Prefix of the virtual fields */
    private static final String VIRTUAL_FIELD_PREFIX = "$";

    protected static final String DEFAULT_ROLE = "NO_FILTER_ACCESS_ROLE_NAME";

    private String roleName;

    /** Process name */
    private String processName;

    /** Fields container id of the criterion */
    private String criterionFieldsContainerId;

    /** Criterion label key */
    private String criterionLabelKey;

    /**
     * Label of the field (same as criterion label key if it is not a criterion
     * on multi level)
     */
    private String fieldLabelKey;

    /**
     * Fields container id of the field label (same as
     * criterionFieldsContainerId if it is not a criterion on multi level,
     * direct parent of the field label key otherwise)
     */
    private String fieldsContainerId;

    private List<String> hierarchy;

    /**
     * Construct of the key
     * 
     * @param pRoleName
     *            Role name
     * @param pProcess
     *            Process name
     * @param pFieldsContainerId
     *            Identifier of the fields container
     * @param pCriterionLabelKey
     *            Label key of the criterion
     */
    public UsableFieldCacheKey(final String pRoleName, final String pProcess,
            final String pFieldsContainerId, final String pCriterionLabelKey) {
        super();
        roleName = pRoleName;
        processName = pProcess;
        criterionLabelKey = pCriterionLabelKey;
        criterionFieldsContainerId = pFieldsContainerId;

        String[] lHierarchy =
                StringUtils.split(pCriterionLabelKey, "\\"
                        + SearchUtils.HIERARCHY_SEPARATOR);
        if (lHierarchy.length < 2) { //no multi-level
            fieldLabelKey = pCriterionLabelKey;
            fieldsContainerId = pFieldsContainerId;
        }
        else {
            fieldLabelKey = lHierarchy[lHierarchy.length - 1];
            fieldsContainerId = lHierarchy[lHierarchy.length - 2];
            List<String> lHierarchyAsList = Arrays.asList(lHierarchy);
            hierarchy = lHierarchyAsList.subList(0, lHierarchy.length - 1);
        }
        keyValue = roleName + "|" + processName + "|" + fieldsContainerId;
    }

    /**
     * Construct of the key.
     * 
     * @param pProcess
     *            Process name
     * @param pFieldsContainerId
     *            Identifier of the fields container
     * @param pCriterionLabelKey
     *            Label key of the criterion
     */
    public UsableFieldCacheKey(final String pProcess,
            final String pFieldsContainerId, final String pCriterionLabelKey) {
        this(DEFAULT_ROLE, pProcess, pFieldsContainerId, pCriterionLabelKey);
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String pProcessName) {
        processName = pProcessName;
    }

    public String getFieldsContainerId() {
        return fieldsContainerId;
    }

    public void setFieldsContainerId(String pFieldsContainerId) {
        fieldsContainerId = pFieldsContainerId;
    }

    public String getCriterionLabelKey() {
        return criterionLabelKey;
    }

    public void setCriterionLabelKey(String pCriterionLabelKey) {
        criterionLabelKey = pCriterionLabelKey;
    }

    public String getCriterionFieldsContainerId() {
        return criterionFieldsContainerId;
    }

    public void setCriterionFieldsContainerId(String pCriterionFieldsContainerId) {
        criterionFieldsContainerId = pCriterionFieldsContainerId;
    }

    public String getFieldLabelKey() {
        return fieldLabelKey;
    }

    public void setFieldLabelKey(String pFieldLabelKey) {
        fieldLabelKey = pFieldLabelKey;
    }

    public List<String> getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(List<String> pHierarchy) {
        hierarchy = pHierarchy;
    }

    /**
     * Test the criterion
     * 
     * @return True if the criterion is on multi-level, false otherwise
     */
    public boolean isMultiLevelCriterion() {
        return criterionLabelKey.contains(SearchUtils.HIERARCHY_SEPARATOR);
    }

    /**
     * Test the field label key
     * 
     * @return True if it's a virtual field (stjart with '$' character), false
     *         otherwise
     */
    public boolean isVirtualField() {
        return fieldLabelKey.startsWith(VIRTUAL_FIELD_PREFIX);
    }

    /**
     * Get the default cache key (with default role name) from the existing
     * cache key.
     * 
     * @param pCacheKey
     *            Cache key with a specific role name.
     * @return Cache key with the default role name.
     */
    protected static UsableFieldCacheKey getDefaultCacheKey(
            UsableFieldCacheKey pCacheKey) {
        return new UsableFieldCacheKey(pCacheKey.getProcessName(),
                pCacheKey.getFieldsContainerId(),
                pCacheKey.getCriterionLabelKey());
    }
}
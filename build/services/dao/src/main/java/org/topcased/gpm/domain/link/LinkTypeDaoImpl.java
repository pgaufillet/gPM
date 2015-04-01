/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.link;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;

/**
 * @see org.topcased.gpm.domain.link.LinkType
 * @author llatil
 */
public class LinkTypeDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.link.LinkType, java.lang.String>
        implements org.topcased.gpm.domain.link.LinkTypeDao {

    static final String GET_LINK_TYPES_QUERY_PART1 =
            "select distinct linkType from "
                    + LinkType.class.getName()
                    + " as linkType where linkType.businessProcess.id = :businessProcessId "
                    + "and (linkType.originType.id = :linkedTypeId " + "or (";

    static final String GET_LINK_TYPES_QUERY_PART_CREATION =
            "linkType.unidirectionalCreation = :unidirectionalCreation and ";

    static final String GET_LINK_TYPES_QUERY_PART_NAVIGATION =
            "linkType.unidirectionalNavigation = :unidirectionalNavigation and ";

    static final String GET_LINK_TYPES_QUERY_PART2 =
            "linkType.destType.id = :linkedTypeId)) order by linkType.name";

    /**
     * Constructor
     */
    public LinkTypeDaoImpl() {
        super(org.topcased.gpm.domain.link.LinkType.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkTypeDaoBase#getLinkTypes(java.lang.String,
     *      org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      org.topcased.gpm.domain.link.LinkNavigation)
     */
    @SuppressWarnings("rawtypes")
    public List getLinkTypes(final String pTypeIdToLink,
            final BusinessProcess pBusinessProcess,
            final LinkNavigation pNavigation) {
        final StringBuilder lStringQuery =
                new StringBuilder(GET_LINK_TYPES_QUERY_PART1);
        appendNavigationPart(lStringQuery, pNavigation);
        lStringQuery.append(GET_LINK_TYPES_QUERY_PART2);

        final Query lQuery = getSession().createQuery(lStringQuery.toString());
        lQuery.setParameter("businessProcessId", pBusinessProcess.getId());
        lQuery.setParameter("linkedTypeId", pTypeIdToLink);
        lQuery.setCacheable(true);
        setNavigationParameters(lQuery, pNavigation);

        return lQuery.list();
    }

    static final String GET_LINK_TYPES_ID_QUERY_PART1 =
            "select distinct linkType.id, linkType.name from "
                    + LinkType.class.getName()
                    + " linkType where (linkType.originType.id = :linkedTypeId "
                    + "or (";

    static final String GET_LINK_TYPES_ID_QUERY_PART_CREATION =
            "linkType.unidirectionalCreation = :unidirectionalCreation and ";

    static final String GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION =
            "linkType.unidirectionalNavigation = :unidirectionalNavigation and ";

    static final String GET_LINK_TYPES_ID_QUERY_PART2 =
            "linkType.destType.id = :linkedTypeId)) order by linkType.name";

    private static final String PARAMETER_UNIDIRECTIONAL_CREATION =
            "unidirectionalCreation";

    private static final String PARAMETER_UNIDIRECTIONAL_NAVIGATION =
            "unidirectionalNavigation";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkTypeDaoBase#getLinkTypesId(java.lang.String,
     *      org.topcased.gpm.domain.link.LinkNavigation)
     */
    @SuppressWarnings("unchecked")
    public List<String> getLinkTypesId(final String pTypeIdToLink,
            final LinkNavigation pNavigation) {
        final StringBuilder lStringQuery =
                new StringBuilder(GET_LINK_TYPES_ID_QUERY_PART1);
        appendNavigationPart(lStringQuery, pNavigation);
        lStringQuery.append(GET_LINK_TYPES_ID_QUERY_PART2);

        final Query lQuery = getSession().createQuery(lStringQuery.toString());
        lQuery.setParameter("linkedTypeId", pTypeIdToLink);
        lQuery.setCacheable(true);
        setNavigationParameters(lQuery, pNavigation);

        List<Object[]> lTupleResults = lQuery.list();
        List<String> lResults = new ArrayList<String>(lTupleResults.size());
        for (Object[] lTuple : lTupleResults) {
            lResults.add((String) lTuple[0]);
        }
        return lResults;
    }

    private final void appendNavigationPart(final StringBuilder pQuery,
            final LinkNavigation pNavigation) {
        if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL_CREATION)) {
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_CREATION);
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION);
        }
        else if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL_NAVIGATION)) {
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_CREATION);
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION);
        }
        else if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL)) {
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_CREATION);
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION);
        }
        else if (pNavigation.equals(LinkNavigation.BIDIRECTIONAL_CREATION)) {
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_CREATION);
        }
        else if (pNavigation.equals(LinkNavigation.BIDIRECTIONAL_NAVIGATION)) {
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION);
        }
        else { //Default BIDIRECTIONAL
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_CREATION);
            pQuery.append(GET_LINK_TYPES_ID_QUERY_PART_NAVIGATION);
        }
    }

    private final void setNavigationParameters(final Query pQuery,
            final LinkNavigation pNavigation) {
        if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL_CREATION)) {
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_CREATION, true);
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_NAVIGATION, false);
        }
        else if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL_NAVIGATION)) {
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_CREATION, false);
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_NAVIGATION, true);
        }
        else if (pNavigation.equals(LinkNavigation.UNIDIRECTIONAL)) {
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_CREATION, true);
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_NAVIGATION, true);
        }
        else if (pNavigation.equals(LinkNavigation.BIDIRECTIONAL_CREATION)) {
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_CREATION, false);
        }
        else if (pNavigation.equals(LinkNavigation.BIDIRECTIONAL_NAVIGATION)) {
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_NAVIGATION, false);
        }
        else { //Default BIDIRECTIONAL
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_CREATION, false);
            pQuery.setParameter(PARAMETER_UNIDIRECTIONAL_NAVIGATION, false);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getLinkType(java.lang.String,
     *      org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.link.LinkType getLinkType(
            final java.lang.String pName,
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.name = :name and linkType.businessProcess.id = :businessProcessId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("name", pName);
            lQueryObject.setParameter("businessProcessId",
                    pBusinessProcess.getId());
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.link.LinkType lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.link.LinkType"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.link.LinkType) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getField(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.Field getField(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer,
            final java.lang.String pFieldName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.container.id = :containerId and linkType.fieldName = :fieldName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
            lQueryObject.setParameter("fieldName", pFieldName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.Field lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.Field"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.Field) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer load(
            final java.lang.String pIdent, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.ident = :ident and linkType.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("ident", pIdent);
            lQueryObject.setParameter("eagerLoading",
                    java.lang.Boolean.valueOf(pEagerLoading));
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.FieldsContainer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.FieldsContainer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.FieldsContainer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFieldNames(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String[] getFieldNames(final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.containerId = :containerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            java.lang.String[] lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String[]"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String[]) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getMultipleFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getMultipleFields(final java.lang.String pContainer,
            final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.container = :container and linkType.includeMultivalued = :includeMultivalued";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("container", pContainer);
            lQueryObject.setParameter("includeMultivalued",
                    java.lang.Boolean.valueOf(pIncludeMultivalued));
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFirstLevelFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getFirstLevelFields(
            final java.lang.String pContainer, final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.container = :container and linkType.includeMultivalued = :includeMultivalued";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("container", pContainer);
            lQueryObject.setParameter("includeMultivalued",
                    java.lang.Boolean.valueOf(pIncludeMultivalued));
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getAllChoiceValues(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllChoiceValues(
            final java.lang.String pId, final java.lang.String pEnvironmentName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.id = :id and linkType.environmentName = :environmentName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("environmentName", pEnvironmentName);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFieldsContainerInfo(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainerInfo getFieldsContainerInfo(
            final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.id = :id";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.FieldsContainerInfo lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.FieldsContainerInfo"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.FieldsContainerInfo) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFieldsContainerId(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFieldsContainerId(final java.lang.String pName,
            final java.lang.String pBusinessProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.name = :name and linkType.businessProcessName = :businessProcessName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("name", pName);
            lQueryObject.setParameter("businessProcessName",
                    pBusinessProcessName);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFieldsContainer(org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer getFieldsContainer(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.businessProcess.id = :businessProcessId and linkType.name = :name";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("businessProcessId",
                    pBusinessProcess.getId());
            lQueryObject.setParameter("name", pName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.FieldsContainer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.FieldsContainer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.FieldsContainer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getFieldsContainerId(java.lang.Class)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getFieldsContainerId(final java.lang.Class pType) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.type = :type";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("type", pType);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#isFieldsContainerExists(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean isFieldsContainerExists(
            final java.lang.String pProcessName,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.processName = :processName and linkType.typeName = :typeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("processName", pProcessName);
            lQueryObject.setParameter("typeName", pTypeName);
            java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final org.topcased.gpm.domain.extensions.ExtensionsContainer pContainer,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.container.id = :containerId and linkType.extensionName = :extensionName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
            lQueryObject.setParameter("extensionName", pExtensionName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.extensions.ExtensionPoint lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.extensions.ExtensionPoint"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.extensions.ExtensionPoint) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getExtensionPoint(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final java.lang.String pContainerId,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.containerId = :containerId and linkType.extensionName = :extensionName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
            lQueryObject.setParameter("extensionName", pExtensionName);
            final java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.extensions.ExtensionPoint lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.extensions.ExtensionPoint"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.extensions.ExtensionPoint) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getAllExtensionPoints(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllExtensionPoints(
            final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.containerId = :containerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setCacheable(true);
            final java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.attrContainer.id = :attrContainerId and linkType.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            lQueryObject.setCacheable(true);
            final java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkType#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.LinkType as linkType where linkType.attrContainer.id = :attrContainerId and linkType.attrName = :attrName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameter("attrName", pAttrName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.attributes.Attribute lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.attributes.Attribute"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.attributes.Attribute) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}
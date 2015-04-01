/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.fields;

import java.util.List;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.topcased.gpm.domain.dictionary.Environment;

/**
 * @author ahaugommard
 */
public class FieldsContainerDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.FieldsContainer, java.lang.String>
        implements org.topcased.gpm.domain.fields.FieldsContainerDao {

    /**
     * Constructor
     */
    public FieldsContainerDaoImpl() {
        super(org.topcased.gpm.domain.fields.FieldsContainer.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getField(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    public Field getField(final FieldsContainer pContainer,
            final String pFieldKey) {
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.fields.Field as field "
                                + "WHERE field.labelKey = :key AND "
                                + "field.container.id = :containerId");
        lQuery.setParameter("key", pFieldKey);
        lQuery.setParameter("containerId", pContainer.getId());

        Field lField = null;
        try {
            lField = (Field) lQuery.uniqueResult();
        }
        catch (HibernateException e) {
        }
        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public FieldsContainer load(final String pId, final boolean pEagerLoading) {
        String lEagerStr = StringUtils.EMPTY;
        if (pEagerLoading) {
            lEagerStr = "left join fetch container.fields ";
        }

        final java.util.List lList;
        lList =
                this.getHibernateTemplate().find(
                        "from org.topcased.gpm.domain.fields.FieldsContainer as container "
                                + lEagerStr + " where container.id = ?", pId);

        final Object lEntity =
                lList != null && !lList.isEmpty() ? lList.iterator().next()
                        : null;
        return (FieldsContainer) lEntity;
    }

    static final String GET_CONTAINERID_QUERY =
            "select container.id from " + FieldsContainer.class.getName()
                    + " as container where container.name = :containerName "
                    + "and container.businessProcess.name = :businessProcName";

    /**
     * Get the technical identifier of a fields containers (type) from its name.
     * 
     * @param pContainerName
     *            Name of the type
     * @param pBusinessProcessName
     *            Name of business process
     * @return Identifier of the fields container
     */
    public String getFieldsContainerId(final String pContainerName,
            final String pBusinessProcessName) {
        final Query lQuery = getSession().createQuery(GET_CONTAINERID_QUERY);
        lQuery.setParameter("businessProcName", pBusinessProcessName);
        lQuery.setParameter("containerName", pContainerName);

        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getFieldNames(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String[] getFieldNames(final String pContainerId) {
        final String lQueryStr =
                "select labelKey from " + Field.class.getName() + " as field "
                        + "where field.container.id = :containerId";

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("containerId", pContainerId);

        final List<String> lNames = lQuery.list();
        return lNames.toArray(new String[lNames.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getFirstLevelFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public List getFirstLevelFields(final String pContainerId,
            final boolean pIncludeMultivalued) {
        final String lQueryStrBase =
                "from " + Field.class.getName() + " as field "
                        + " where field.container.id = :containerId and"
                        + " (not exists ( from "
                        + MultipleField.class.getName() + " as multifield "
                        + " where multifield.container.id = :containerId and "
                        + " field in (elements(multifield.fields))))";
        final String lQueryExcludeMultivalued =
                " and field.multiValued = false";

        String lQueryStr = lQueryStrBase;
        if (!pIncludeMultivalued) {
            lQueryStr += lQueryExcludeMultivalued;
        }

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("containerId", pContainerId);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getMultipleFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public List getMultipleFields(final String pContainerId,
            final boolean pIncludeMultivalued) {
        final String lQueryStrWithoutMultivalues =
                "from "
                        + MultipleField.class.getName()
                        + " as multifield where multifield.container.id = :containerId and "
                        + " multifield.multiValued = false";

        final String lQueryStrWithMultivalues =
                "from "
                        + MultipleField.class.getName()
                        + " as multifield where multifield.container.id = :containerId";

        String lQueryStr = lQueryStrWithMultivalues;
        if (!pIncludeMultivalued) {
            lQueryStr = lQueryStrWithoutMultivalues;
        }
        Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("containerId", pContainerId);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getAllChoiceValues(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getAllChoiceValues(final String pContainerId,
            final String pEnvironmentName) {
        final String lQueryStr =
                "select distinct catValue.value from "
                        + Environment.class.getName()
                        + " env, "
                        + ChoiceField.class.getName()
                        + " as choicefield left join choicefield.category.categoryValues as catValue "
                        + " where choicefield.container.id = :containerId "
                        + " and env.name = :envName"
                        + " and env in elements(catValue.environments)"
                        + " order by " + " catValue.value";

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("containerId", pContainerId);
        lQuery.setParameter("envName", pEnvironmentName);
        return (List<String>) lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getFieldsContainerInfo(java.lang.String)
     */
    public FieldsContainerInfo getFieldsContainerInfo(final String pContainerId) {
        final String lQueryStr =
                "select new "
                        + FieldsContainerInfo.class.getName()
                        + "(container.id, container.name, container.description)"
                        + " from " + FieldsContainer.class.getName()
                        + " container " + " where container.id=:containerId";

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("containerId", pContainerId);

        return (FieldsContainerInfo) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#getFieldsContainerId(java.lang.Class)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<String> getFieldsContainerId(final Class pType) {
        final String lQueryStr =
                "select type.id" + " from " + pType.getName() + " type ";

        final Query lQuery = getSession(false).createQuery(lQueryStr);

        return (List<String>) lQuery.list();
    }

    private static final String FIELDS_CONTAINER_EXIST =
            "SELECT fc.id FROM " + FieldsContainer.class.getName()
                    + " fc WHERE fc.name = :typeName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainerDaoBase#isFieldsContainerExists(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Boolean isFieldsContainerExists(String pProcessName, String pTypeName) {
        final Query lQuery = createQuery(FIELDS_CONTAINER_EXIST);
        lQuery.setParameter("typeName", pTypeName);
        return hasResult(lQuery);
    }

    /**
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getFieldsContainer(org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer getFieldsContainer(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.businessProcess.id = :businessProcessId and fieldsContainer.name = :name";
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
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final org.topcased.gpm.domain.extensions.ExtensionsContainer pContainer,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.container.id = :containerId and fieldsContainer.extensionName = :extensionName";
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
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getExtensionPoint(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final java.lang.String pContainerId,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.containerId = :containerId and fieldsContainer.extensionName = :extensionName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
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
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getAllExtensionPoints(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllExtensionPoints(
            final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.containerId = :containerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.attrContainer.id = :attrContainerId and fieldsContainer.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.fields.FieldsContainer#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.FieldsContainer as fieldsContainer where fieldsContainer.attrContainer.id = :attrContainerId and fieldsContainer.attrName = :attrName";
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

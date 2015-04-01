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
package org.topcased.gpm.domain.revision;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.dynamic.container.revision.DynamicRevisionGeneratorFactory;
import org.topcased.gpm.domain.fields.ValuesContainer;

/**
 * Override Revision DAO method
 * 
 * @see org.topcased.gpm.domain.revision.Revision
 * @author mfranche
 */
public class RevisionDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.revision.Revision, java.lang.String>
        implements org.topcased.gpm.domain.revision.RevisionDao {

    /**
     * Constructor
     */
    public RevisionDaoImpl() {
        super(org.topcased.gpm.domain.revision.Revision.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevision(java.lang.String,
     *      java.lang.String)
     */
    public Revision getRevision(final String pSheetId, final String pRevisionId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select rev FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where rev.id = :revisionId "
                                + "and valuesCont.id = :sheetId "
                                + "and rev in elements(valuesCont.revisions)");

        lQuery.setParameter("sheetId", pSheetId);
        lQuery.setParameter("revisionId", pRevisionId);

        return (Revision) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevisionByLabel(java.lang.String,
     *      java.lang.String)
     */
    public Revision getRevisionByLabel(final String pSheetId,
            final String pRevisionLabel) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select rev FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where rev.label = :revisionLabel "
                                + "and valuesCont.id = :sheetId "
                                + "and rev in elements(valuesCont.revisions)");

        lQuery.setParameter("sheetId", pSheetId);
        lQuery.setParameter("revisionLabel", pRevisionLabel);

        return (Revision) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevisionsCount(java.lang.String)
     */
    public Integer getRevisionsCount(final String pSheetId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select count(rev) FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where valuesCont.id = :sheetId "
                                + "and rev in elements(valuesCont.revisions)");

        lQuery.setParameter("sheetId", pSheetId);

        return ((Long) lQuery.uniqueResult()).intValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDao#getRevisions(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Revision> getRevisions(final String pContainerId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select rev FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where valuesCont.id = :sheetId "
                                + "and rev in elements(valuesCont.revisions) "
                                + "order by rev.creationDate");

        lQuery.setParameter("sheetId", pContainerId);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevisionIdByLabel(java.lang.String,
     *      java.lang.String)
     */
    public String getRevisionIdByLabel(final String pContainerId,
            final String pRevisionLabel) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select rev.id FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where rev.label = :revisionLabel "
                                + "and valuesCont.id = :containerId "
                                + "and rev in elements(valuesCont.revisions)");

        lQuery.setParameter("containerId", pContainerId);
        lQuery.setParameter("revisionLabel", pRevisionLabel);

        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevisionLabels(java.lang.String,
     *      int)
     */
    @SuppressWarnings("unchecked")
    public List<String> getRevisionLabels(final String pContainerId,
            final int pLastItems) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select rev.label FROM org.topcased.gpm.domain.revision.Revision AS rev, "
                                + "org.topcased.gpm.domain.fields.ValuesContainer AS valuesCont "
                                + "where valuesCont.id = :sheetId "
                                + "and rev in elements(valuesCont.revisions) "
                                + "order by rev.creationDate desc");

        lQuery.setParameter("sheetId", pContainerId);

        lQuery.setFirstResult(0);
        lQuery.setMaxResults(pLastItems);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getRevisionContainer(org.topcased.gpm.domain.revision.Revision)
     */
    public ValuesContainer getRevisionContainer(final Revision pRevisionEntity) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select valuesContainer "
                                + "FROM org.topcased.gpm.domain.fields.ValuesContainer AS valuesContainer, "
                                + "org.topcased.gpm.domain.revision.Revision as revision "
                                + "where :revision in elements(valuesContainer.revisions)");

        lQuery.setParameter("revision", pRevisionEntity);
        return (ValuesContainer) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.revision.RevisionDaoBase#getNewRevision(java.lang.String)
     */
    public Revision getNewRevision(final String pTypeId) {
        return DynamicRevisionGeneratorFactory.getInstance().getDynamicObjectGenerator(
                pTypeId).create();
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.ValuesContainer load(
            final java.lang.String pId, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.id = :id and revision.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("eagerLoading",
                    java.lang.Boolean.valueOf(pEagerLoading));
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.ValuesContainer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.ValuesContainer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.ValuesContainer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#getVersion(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Integer getVersion(final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.id = :id";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Integer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Integer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Integer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#getCount(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Long getCount(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Long lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Long"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Long) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#getAll(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#deleteContainers(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteContainers(
            final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
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
     * @see org.topcased.gpm.domain.revision.Revision#getTypeId(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeId(final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.ValuesContainerId = :ValuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("ValuesContainerId", pValuesContainerId);
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
     * @see org.topcased.gpm.domain.revision.Revision#getTypesId(java.util.
     *      Collection<String>)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getTypesId(
            final java.util.Collection<String> pIds) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.Ids = :Ids";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("Ids", pIds);
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
     * @see org.topcased.gpm.domain.revision.Revision#getLinkedElements(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getLinkedElements(final java.lang.String pId,
            final java.lang.String pLinkTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision "
                            + "where revision.id = :id and revision.linkTypeName = :linkTypeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("linkTypeName", pLinkTypeName);
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
     * @see org.topcased.gpm.domain.revision.Revision#removeSubField(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Object removeSubField(final java.lang.Object pSubField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.subField = :subField";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("subField", pSubField);
            java.util.List lResults = lQueryObject.list();
            java.lang.Object lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Object"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Object) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.revision.Revision#getAllId(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllId(final java.lang.String pTypeId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.typeId = :typeId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("typeId", pTypeId);
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
     * @see org.topcased.gpm.domain.revision.Revision#getIdByReference(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getIdByReference(final java.lang.String pReference,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.reference = :reference and revision.typeName = :typeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("reference", pReference);
            lQueryObject.setParameter("typeName", pTypeName);
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
     * @see org.topcased.gpm.domain.revision.Revision#getTypeName(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeName(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
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
     * @see org.topcased.gpm.domain.revision.Revision#getFunctionalReference(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFunctionalReference(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
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
     * @see org.topcased.gpm.domain.revision.Revision#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.attrContainer.id = :attrContainerId";
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
     * @see org.topcased.gpm.domain.revision.Revision#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.attrContainer.id = :attrContainerId and revision.attrNames = :attrNames";
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
     * @see org.topcased.gpm.domain.revision.Revision#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.revision.Revision as revision where revision.attrContainer.id = :attrContainerId and revision.attrName = :attrName";
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
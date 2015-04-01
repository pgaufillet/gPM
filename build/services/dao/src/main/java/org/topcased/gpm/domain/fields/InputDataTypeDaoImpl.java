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
package org.topcased.gpm.domain.fields;

/**
 * @see org.topcased.gpm.domain.fields.InputDataType
 * @author ahaugommard
 */
public class InputDataTypeDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.InputDataType, java.lang.String>
        implements org.topcased.gpm.domain.fields.InputDataTypeDao {
    /**
     * Constructro
     */
    public InputDataTypeDaoImpl() {
        super(org.topcased.gpm.domain.fields.InputDataType.class);
    }

    /**
     * @see org.topcased.gpm.domain.fields.InputDataType#getField(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.Field getField(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer,
            final java.lang.String pFieldName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.container.id = :containerId and inputDataType.fieldName = :fieldName";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer load(
            final java.lang.String pIdent, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.ident = :ident and inputDataType.eagerLoading = :eagerLoading";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFieldNames(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String[] getFieldNames(final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.containerId = :containerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getMultipleFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getMultipleFields(final java.lang.String pContainer,
            final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.container = :container and inputDataType.includeMultivalued = :includeMultivalued";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFirstLevelFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getFirstLevelFields(
            final java.lang.String pContainer, final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.container = :container and inputDataType.includeMultivalued = :includeMultivalued";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getAllChoiceValues(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllChoiceValues(
            final java.lang.String pId, final java.lang.String pEnvironmentName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.id = :id and inputDataType.environmentName = :environmentName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("environmentName", pEnvironmentName);
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFieldsContainerInfo(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainerInfo getFieldsContainerInfo(
            final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.id = :id";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFieldsContainerId(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFieldsContainerId(final java.lang.String pName,
            final java.lang.String pBusinessProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.name = :name and inputDataType.businessProcessName = :businessProcessName";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFieldsContainer(org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer getFieldsContainer(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.businessProcess.id = :businessProcessId and inputDataType.name = :name";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getFieldsContainerId(java.lang.Class)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getFieldsContainerId(final java.lang.Class pType) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.type = :type";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("type", pType);
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
     * @see org.topcased.gpm.domain.fields.InputDataType#isFieldsContainerExists(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean isFieldsContainerExists(
            final java.lang.String pProcessName,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.processName = :processName and inputDataType.typeName = :typeName";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final org.topcased.gpm.domain.extensions.ExtensionsContainer pContainer,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.container.id = :containerId and inputDataType.extensionName = :extensionName";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getExtensionPoint(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final java.lang.String pContainerId,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.containerId = :containerId and inputDataType.extensionName = :extensionName";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getAllExtensionPoints(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllExtensionPoints(
            final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.containerId = :containerId";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.attrContainer.id = :attrContainerId";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.attrContainer.id = :attrContainerId and inputDataType.attrNames = :attrNames";
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
     * @see org.topcased.gpm.domain.fields.InputDataType#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.InputDataType as inputDataType where inputDataType.attrContainer.id = :attrContainerId and inputDataType.attrName = :attrName";
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
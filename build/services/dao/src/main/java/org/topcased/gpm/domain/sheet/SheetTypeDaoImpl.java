/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.sheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.process.ProcessDefinition;

/**
 * SheetTypeDaoImpl
 * 
 * @author srene
 */
public class SheetTypeDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.sheet.SheetType, java.lang.String>
        implements org.topcased.gpm.domain.sheet.SheetTypeDao {
    // The log4j logger object for this class.
//    private static final Logger LOGGER =
//            org.apache.log4j.Logger.getLogger(SheetTypeDaoImpl.class);

    // cache sheet type name list per business process
    private Map<String, List<String>> sheetTypeNamesPerBusinessProcess =
            new HashMap<String, List<String>>();

    // cache sheet type list per business process 
    private Map<String, List<SheetType>> sheetTypePerBusinessProcess =
            new HashMap<String, List<SheetType>>();

    // cache sheet type id list per business process 
    private Map<String, List<String>> sheetTypeIdPerBusinessProcess =
            new HashMap<String, List<String>>();

    /**
     * Create a DAO.
     */
    public SheetTypeDaoImpl() {
        super(org.topcased.gpm.domain.sheet.SheetType.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetTypeDaoBase#
     *      getNode(org.topcased.gpm.domain.sheet.SheetType, java.lang.String)
     */
    public Node getNode(final String pSheetTypeId, final String pStateName) {

        final Query lQuery =
                getSession(false).createQuery(
                        "SELECT node from "
                                + "org.topcased.gpm.domain.process.Node as node, "
                                + "org.topcased.gpm.domain.sheet.SheetType as sheetType "
                                + "WHERE  sheetType.id = :sheetTypeId "
                                + "AND node.name= :nodeName "
                                + "AND node.processDefinition.id=sheetType.processDefinition.id");

        lQuery.setParameter("sheetTypeId", pSheetTypeId);
        lQuery.setParameter("nodeName", pStateName);
        lQuery.setCacheable(true);
        Node lNode;
        try {
            lNode = (Node) lQuery.uniqueResult();
        }
        catch (HibernateException e) {
            return null;
        }
        return lNode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetTypeDaoBase#
     *      getSheetTypes(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("unchecked")
    public List<SheetType> getSheetTypes(final BusinessProcess pBusinessProcess) {
        if (sheetTypePerBusinessProcess.get(pBusinessProcess.getName()) == null) {
            final Query lQuery =
                    getSession(false).createQuery(
                            "FROM org.topcased.gpm.domain.sheet.SheetType as sheetType "
                                    + "WHERE sheetType.businessProcess.id = :businessProcessId "
                                    + "ORDER BY sheetType.name");
            lQuery.setParameter("businessProcessId", pBusinessProcess.getId());
            lQuery.setCacheable(true);
            sheetTypePerBusinessProcess.put(pBusinessProcess.getName(),
                    (List<SheetType>) lQuery.list());
        }

        return sheetTypePerBusinessProcess.get(pBusinessProcess.getName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetTypeDaoBase#
     *      getSheetTypes(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSheetTypesId(final String pBusinessProcessName) {
        if (sheetTypeIdPerBusinessProcess.get(pBusinessProcessName) == null) {
            final Query lQuery =
                    getSession(false).createQuery(
                            "select sheetType.id from org.topcased.gpm.domain.sheet.SheetType as sheetType "
                                    + "WHERE sheetType.businessProcess.name = :businessProcessName "
                                    + "ORDER BY sheetType.name");
            lQuery.setParameter("businessProcessName", pBusinessProcessName);
            lQuery.setCacheable(true);
            sheetTypeIdPerBusinessProcess.put(pBusinessProcessName,
                    lQuery.list());
        }
        return sheetTypeIdPerBusinessProcess.get(pBusinessProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetTypeDaoBase#
     *      getStateNames(org.topcased.gpm.domain.sheet.SheetType)
     */
    @SuppressWarnings("unchecked")
    public String[] getStateNames(final SheetType pSheetType) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select node.name FROM org.jbpm.graph.def.Node node "
                                + "WHERE node.processDefinition.name = :procDefName "
                                + "order by node.name");
        lQuery.setParameter("procDefName",
                pSheetType.getProcessDefinition().getName());
        lQuery.setCacheable(true);
        List<String> lList = lQuery.list();
        return lList.toArray(new String[lList.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetTypeDaoBase#getSheetTypeNames(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSheetTypeNames(final String pBusinessProcName) {
        if (sheetTypeNamesPerBusinessProcess.get(pBusinessProcName) == null) {
            final Query lQuery =
                    getSession(false).createQuery(
                            "select sheetType.name from org.topcased.gpm.domain.sheet.SheetType as sheetType "
                                    + "WHERE sheetType.businessProcess.name = :businessProcessName "
                                    + "ORDER BY sheetType.name");
            lQuery.setParameter("businessProcessName", pBusinessProcName);
            lQuery.setCacheable(true);
            sheetTypeNamesPerBusinessProcess.put(pBusinessProcName,
                    lQuery.list());
        }
        return sheetTypeNamesPerBusinessProcess.get(pBusinessProcName);
    }

    /**
     * @see org.topcased.gpm.domain.sheet.SheetType#getSheetType(org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.sheet.SheetType getSheetType(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.businessProcess.id = :businessProcessId and sheetType.name = :name";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("businessProcessId",
                    pBusinessProcess.getId());
            lQueryObject.setParameter("name", pName);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.sheet.SheetType lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.sheet.SheetType"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.sheet.SheetType) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.SheetType#getField(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.Field getField(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer,
            final java.lang.String pFieldName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.container.id = :containerId and sheetType.fieldName = :fieldName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
            lQueryObject.setParameter("fieldName", pFieldName);
            lQueryObject.setCacheable(true);
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
     * @see org.topcased.gpm.domain.sheet.SheetType#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer load(
            final java.lang.String pIdent, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.ident = :ident and sheetType.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("ident", pIdent);
            lQueryObject.setParameter("eagerLoading",
                    java.lang.Boolean.valueOf(pEagerLoading));
            lQueryObject.setCacheable(true);
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFieldNames(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String[] getFieldNames(final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.containerId = :containerId";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getMultipleFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getMultipleFields(final java.lang.String pContainer,
            final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.container = :container and sheetType.includeMultivalued = :includeMultivalued";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFirstLevelFields(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getFirstLevelFields(
            final java.lang.String pContainer, final boolean pIncludeMultivalued) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.container = :container and sheetType.includeMultivalued = :includeMultivalued";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getAllChoiceValues(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllChoiceValues(
            final java.lang.String pId, final java.lang.String pEnvironmentName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.id = :id and sheetType.environmentName = :environmentName";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFieldsContainerInfo(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainerInfo getFieldsContainerInfo(
            final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.id = :id";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFieldsContainerId(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFieldsContainerId(final java.lang.String pName,
            final java.lang.String pBusinessProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.name = :name and sheetType.businessProcessName = :businessProcessName";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFieldsContainer(org.topcased.gpm.domain.businessProcess.BusinessProcess,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.FieldsContainer getFieldsContainer(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.businessProcess.id = :businessProcessId and sheetType.name = :name";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getFieldsContainerId(java.lang.Class)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getFieldsContainerId(
            final java.lang.Class pType) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.type = :type";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#isFieldsContainerExists(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean isFieldsContainerExists(
            final java.lang.String pProcessName,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.processName = :processName and sheetType.typeName = :typeName";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final org.topcased.gpm.domain.extensions.ExtensionsContainer pContainer,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.container.id = :containerId and sheetType.extensionName = :extensionName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
            lQueryObject.setParameter("extensionName", pExtensionName);
            lQueryObject.setCacheable(true);
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getExtensionPoint(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final java.lang.String pContainerId,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.containerId = :containerId and sheetType.extensionName = :extensionName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainerId);
            lQueryObject.setParameter("extensionName", pExtensionName);
            lQueryObject.setCacheable(true);
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getAllExtensionPoints(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllExtensionPoints(
            final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.containerId = :containerId";
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
     * @see org.topcased.gpm.domain.sheet.SheetType#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.SheetType#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.attrContainer.id = :attrContainerId and sheetType.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            lQueryObject.setCacheable(true);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.SheetType#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.SheetType as sheetType where sheetType.attrContainer.id = :attrContainerId and sheetType.attrName = :attrName";
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

    @Override
    public ProcessDefinition getProcessDefinitionBySheetTypeId(
            final String pSheetTypeId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "SELECT proc FROM SheetType as st, ProcessDefinition as proc WHERE st.id  =:sheetTypeId AND st.processDefinition.id = proc.id");
        lQuery.setParameter("sheetTypeId", pSheetTypeId);
        lQuery.setCacheable(true);
        ProcessDefinition lProcessDefinition = null;
        try {
            lProcessDefinition = (ProcessDefinition) lQuery.uniqueResult();
        }
        catch (org.hibernate.HibernateException e) {
            throw super.convertHibernateAccessException(e);
        }
        return lProcessDefinition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkSheetTypeIds(String[] pIds) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select count(sheetType.id) from "
                                + SheetType.class.getName()
                                + " sheetType where sheetType.id in (:containerIds)");
        lQuery.setParameterList("containerIds", pIds);
        final long lSize = (Long) lQuery.uniqueResult();
        return (pIds.length == lSize);
    }

}

/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;










//import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * @see org.topcased.gpm.domain.process.Node
 * @author nbousquet
 */
public class NodeDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.process.Node, java.lang.String>
        implements org.topcased.gpm.domain.process.NodeDao {

    /**
     * Constructor
     */
    public NodeDaoImpl() {
        super(org.topcased.gpm.domain.process.Node.class);
    }

    /**
     * Get node names
     * 
     * @param pSheetType the sheet type
     * @return the node names
     */
    @SuppressWarnings("unchecked")
	public String[] getNodeNames(final SheetType pSheetType) {

        final String lQueryStr =
                "SELECT node.name FROM org.topcased.gpm.domain.process.Node as node "
                        + "WHERE node.processDefinition.id = :processDefinitionId";
        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("processDefinitionId",
                pSheetType.getProcessDefinition().getId());
        lQuery.setCacheable(true);
        List<String> lList = lQuery.list();
        return lList.toArray(new String[lList.size()]);
    }

    /**
     * Get an extension points
     * 
     * @param pContainer the container
     * @return all extension points
     */
    @SuppressWarnings("rawtypes")
	public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final org.topcased.gpm.domain.extensions.ExtensionsContainer pContainer,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.container.id = :containerId and node.extensionName = :extensionName";
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
     * Get an extension point
     * 
     * @param pContainerId the container Id
     * @param pExtensionName the extension point name
     * @return all extension points
     */
     @SuppressWarnings("rawtypes")
	public org.topcased.gpm.domain.extensions.ExtensionPoint getExtensionPoint(
            final java.lang.String pContainerId,
            final java.lang.String pExtensionName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.containerId = :containerId and node.extensionName = :extensionName";
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
     * Get all extension points
     * 
     * @param pContainerId the container Id
     * @return all extension points
     */
    @SuppressWarnings("rawtypes")
	public java.util.List getAllExtensionPoints(
            final java.lang.String pContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.containerId = :containerId";
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
     * Get all attribute names
     * 
     * @param pAttrContainer the attribute container
     * @return all attribute names
     */
    @SuppressWarnings("rawtypes")
	public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.attrContainer.id = :attrContainerId";
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
     * Get several attributes
     * 
     * @param pAttrContainer the attribute container
     * @param pAttrNames the attribute names
     * @return the matching attributes
     */
    @SuppressWarnings("rawtypes")
	public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.attrContainer.id = :attrContainerId and node.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            final java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * Get an attribute
     * 
     * @param pAttrContainer the attribute container
     * @param pAttrName the attribute name
     * @return the attribute
     */
    @SuppressWarnings("rawtypes")
	public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.process.Node as node where node.attrContainer.id = :attrContainerId and node.attrName = :attrName";
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
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	@Override
    public Set<Transition> getTransitions(Node pNode) {
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.process.Transition as transition "
                                + "WHERE transition.fromNode.id = :nodeId");
        lQuery.setParameter("nodeId", pNode.getId());
        lQuery.setCacheable(true);
        List<Transition> lTransitions;
        try {
            lTransitions = (List<Transition>) lQuery.list();
        }
        catch (HibernateException e) {
            return new HashSet<Transition>();
        }
        return new HashSet<Transition>(lTransitions);
    }

    /** {@inheritDoc} */
    @Override
    public Transition getTransitionByName(Node pNode, String pTransitionName) {
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.process.Transition as transition "
                                + "WHERE transition.fromNode.id = :nodeId AND transition.name = :transitionName");
        lQuery.setParameter("nodeId", pNode.getId());
        lQuery.setParameter("transitionName", pTransitionName);

        Transition lTransition = null;
        try {
            lTransition = (Transition) lQuery.uniqueResult();
        }
        catch (HibernateException e) {
//            LOGGER.error(e);
          //FIXME cf us61
            return null;
        }
        return lTransition;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	@Override
    public List<String> getTransitionsNames(Node pNode) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select transition.name FROM org.topcased.gpm.domain.process.Transition as transition "
                                + "WHERE transition.fromNode.id = :nodeId");
        lQuery.setParameter("nodeId", pNode.getId());
        lQuery.setCacheable(true);
        List<String> lTransitions = new ArrayList<String>();
        try {
            lTransitions = (List<String>) lQuery.list();
        }
        catch (HibernateException e) {
//            LOGGER.error(e);
          //FIXME cf us61
            return lTransitions;
        }
        return lTransitions;
    }
}

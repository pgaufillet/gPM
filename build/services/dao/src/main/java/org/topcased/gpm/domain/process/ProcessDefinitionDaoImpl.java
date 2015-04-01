/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;





//import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * @see org.topcased.gpm.domain.process.ProcessDefinition
 * @author Atos
 */
public class ProcessDefinitionDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.process.ProcessDefinition, java.lang.String>
        implements org.topcased.gpm.domain.process.ProcessDefinitionDao {

    private static final String FROM_NODE = "FROM org.topcased.gpm.domain.process.Node as node ";
    private static final String PDID = "processDefinitionId";
    
    // cache the nodes names list per process definition ids
    private Map<String, List<String>> nodeNamesPerProcessDefintion =
            new HashMap<String, List<String>>();

    // cache the nodes  list per process definition ids
    private Map<String, List<Node>> nodesPerProcessDefintion =
            new HashMap<String, List<Node>>();

    public ProcessDefinitionDaoImpl() {
        super(org.topcased.gpm.domain.process.ProcessDefinition.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNode(final String pProcessDefinitionId,
            final String pStateName) {
        final Query lQuery =
                getSession(false).createQuery(FROM_NODE
                                + "WHERE node.processDefinition.id = :processDefinitionId AND "
                                + "node.name = :stateName");
        lQuery.setParameter("stateName", pStateName);
        lQuery.setParameter(PDID, pProcessDefinitionId);

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
     */
    @SuppressWarnings("unchecked")
	@Override
    public Set<Node> getNodes(String pProcessDefinitionId) {
        if (nodesPerProcessDefintion.get(pProcessDefinitionId) == null) {
            final Query lQuery =
                    getSession(false).createQuery(FROM_NODE
                                    + "WHERE node.processDefinition.id = :processDefinitionId");
            lQuery.setParameter(PDID, pProcessDefinitionId);
            lQuery.setCacheable(true);
            List<Node> lNodes;
            try {
                lNodes = (List<Node>) lQuery.list();
                nodesPerProcessDefintion.put(pProcessDefinitionId, lNodes);
            }
            catch (HibernateException e) {
//                LOGGER.error(e);
              //FIXME cf us 61
                return null;
            }
        }

        return new HashSet<Node>(
                nodesPerProcessDefintion.get(pProcessDefinitionId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessDefinition getProcessDefinitionByName(
            String pProcessDefinitionName) {
        ProcessDefinition lPd = null;
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.process.ProcessDefinition as processDefinition "
                                + "WHERE processDefinition.name = :processDefinitionName");
        lQuery.setParameter("processDefinitionName", pProcessDefinitionName);
        lQuery.setCacheable(true);
        try {
            lPd = (ProcessDefinition) lQuery.uniqueResult();
        }
        catch (HibernateException e) {
//            LOGGER.error(e);
          //FIXME cf us 61
            return null;
        }
        return lPd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getStartState(String pProcessDefinitionId) {
        final Query lQuery =
                getSession(false).createQuery(FROM_NODE
                                + "WHERE node.processDefinition.id = :processDefinitionId AND node.type = :type");
        final String lStateType =
                String.valueOf(ProcessDefinition.stateType.STARTSTATE.name());
        lQuery.setParameter(PDID, pProcessDefinitionId);
        lQuery.setParameter("type", lStateType);
        lQuery.setCacheable(true);
        Node lStartState = null;
        try {
            lStartState = (Node) lQuery.uniqueResult();
        }
        catch (HibernateException e) {
//            LOGGER.error(e);
          //FIXME cf us 61
        }
        return lStartState;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public Set<Node> getEndStates(String pProcessDefinitionId) {
        final Query lQuery =
                getSession(false).createQuery(FROM_NODE
                                + "WHERE node.processDefinition.id = :processDefinitionId AND node.type = :type");
        lQuery.setParameter(PDID, pProcessDefinitionId);
        lQuery.setParameter("type", ProcessDefinition.stateType.ENDSTATE.name());

        List<Node> lNodes;
        try {
            lNodes = (List<Node>) lQuery.list();
        }
        catch (HibernateException e) {
//            LOGGER.error(e);
          //FIXME cf us 61
            return null;
        }
        return new HashSet<Node>(lNodes);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean deleteNodeFromProcessDefinition(final Node pNode) {
        boolean lRet = false;
        final Query lQuery =
                getSession(false).createQuery(FROM_NODE
                                + "WHERE node.processDefinition.id = :processDefinitionId AND "
                                + "node.name = :stateName");
        lQuery.setParameter("stateName", pNode.getName());
        lQuery.setParameter(PDID,
                pNode.getProcessDefinition().getId());
        Iterator lResult = lQuery.iterate();
        while (lResult.hasNext()) {
            getSession().delete(lResult.next());
            lRet = true;

        }
        return lRet;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public Set<String> getNodesNames(String pProcessDefinitionId) {
        if (nodeNamesPerProcessDefintion.get(pProcessDefinitionId) == null) {
            final Query lQuery =
                    getSession(false).createQuery(
                            "select node.name " + FROM_NODE
                            + "WHERE node.processDefinition.id = :processDefinitionId");
            lQuery.setParameter(PDID, pProcessDefinitionId);
            lQuery.setCacheable(true);
            List<String> lNodes;
            try {
                lNodes = (List<String>) lQuery.list();
                nodeNamesPerProcessDefintion.put(pProcessDefinitionId, lNodes);
            }
            catch (HibernateException e) {
//                LOGGER.error(e);
              //FIXME cf us 61
                return new HashSet<String>();
            }
        }
        return new HashSet<String>(
                nodeNamesPerProcessDefintion.get(pProcessDefinitionId));
    }
}

/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

/**
 * Interface used by all gPM DAO.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of the entity.
 * @param <K>
 *            The type of the entity key.
 */
public interface IDao<T extends PersistentObject, K extends Serializable> {
    /**
     * Loads an entity from the persistent store.
     * 
     * @param pId
     *            The id of the entity.
     * @return The entity.
     * @throws HibernateObjectRetrievalFailureException
     *             If the entity doesn't exists.
     * @see IDao#exist(String).
     */
    public T load(K pId) throws HibernateObjectRetrievalFailureException;

    /**
     * Test if an entity exists for a specific id.
     * 
     * @param pId
     *            The id.
     * @return If the entity exists.
     */
    public boolean exist(K pId);

    /**
     * Loads all entities of type T.
     * 
     * @return The loaded entities.
     */
    public Collection<T> loadAll();

    /**
     * Creates an entity on the persistent store.
     * 
     * @param pEntity
     *            The entity to create.
     */
    public void create(T pEntity);

    /**
     * Removes an entity of the persistent store.
     * 
     * @param pEntity
     *            The entity to remove.
     */
    public void remove(T pEntity);

    /**
     * Removes an entity of the persistent store.
     * 
     * @param pId
     *            The id of the entity to remove.
     */
    public void remove(K pId);

    /**
     * Create a query for execute a read only request on data base.
     * 
     * @param pRequest
     *            The HQL request.
     * @return The query object to execute.
     */
    public Query createQuery(String pRequest);

    /**
     * Execute a query object returning a list of entities.
     * 
     * @param pQuery
     *            The query to execute.
     * @return The list of entities.
     */
    public List<T> execute(Query pQuery);

    /**
     * Execute a query object returning a list of entities values.
     * 
     * @param <E>
     *            The type of the entities values.
     * @param pQuery
     *            The query to execute.
     * @param pClass
     *            Class type of the entities values.
     * @return The list of entities values.
     */
    public <E> List<E> execute(Query pQuery, Class<E> pClass);

    /**
     * Execute a query object returning an iteration of entities.
     * 
     * @param pQuery
     *            The query to execute.
     * @return The iteration of entities.
     */
    public Iterator<T> iterate(Query pQuery);

    /**
     * Execute a query object returning an iteration of entities values.
     * 
     * @param <E>
     *            The type of the entities values.
     * @param pQuery
     *            The query to execute.
     * @param pClass
     *            Class type of the entities values.
     * @return The iteration of entities values.
     */
    public <E> Iterator<E> iterate(Query pQuery, Class<E> pClass);

    /**
     * Get the first result of a query.
     * 
     * @param pQuery
     *            The query to execute.
     * @return The first result.
     */
    public T getFirst(Query pQuery);

    /**
     * Get the first result of a sorted query.
     * 
     * @param pQuery
     *            The query to execute.
     * @param pClass
     *            Class type of the entities values.
     * @return The first result.
     */
    public <E> E getFirst(Query pQuery, Class<E> pClass);

    /**
     * Get the first result of a sorted query.
     * 
     * @param pQuery
     *            The query to execute.
     * @param pComparator
     *            Comparator used to sort the results.
     * @return The first result.
     */
    public T getFirst(Query pQuery, Comparator<T> pComparator);

    /**
     * Test if a query return result. The query cannot be executed after this
     * test.
     * 
     * @param pQuery
     *            The query to execute.
     * @return If the query return results.
     */
    public boolean hasResult(Query pQuery);

    /**
     * Flush the current session
     */
    public void flush();

    /**
     * Evict an entity of the current session. A flush must be done first, if
     * the element has been modified.
     * 
     * @param pEntity
     *            The entity to evict.
     */
    public void evict(T pEntity);
}
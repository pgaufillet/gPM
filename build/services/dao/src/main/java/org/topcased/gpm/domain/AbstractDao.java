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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Abstract class used by all gPM DAO.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of the entity.
 * @param <K>
 *            The type of the entity key.
 */
public abstract class AbstractDao<T extends PersistentObject, K extends Serializable>
        extends HibernateDaoSupport implements IDao<T, K> {
    private final Class<? extends T> entityClass;

    /**
     * Create a DAO for a specific entity.
     * 
     * @param pEntityClass
     *            The class of the entity.
     */
    public AbstractDao(final Class<? extends T> pEntityClass) {
        entityClass = pEntityClass;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#load(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public T load(final K pId) throws HibernateObjectRetrievalFailureException {
        return (T) getHibernateTemplate().load(entityClass, pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#exist(java.io.Serializable)
     */
    public boolean exist(final K pId) {
        final Query lQuery =
                createQuery("select id from " + entityClass.getName()
                        + " where id = :id");

        lQuery.setParameter("id", pId);

        return !lQuery.list().isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#loadAll()
     */
    @SuppressWarnings("unchecked")
    public Collection<T> loadAll() {
        return getHibernateTemplate().loadAll(entityClass);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#create(org.topcased.gpm.domain.PersistentObject)
     */
    public void create(final T pEntity) {
        getHibernateTemplate().save(pEntity);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#remove(org.topcased.gpm.domain.PersistentObject)
     */
    public void remove(final T pEntity) {
        getHibernateTemplate().delete(pEntity);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#remove(java.io.Serializable)
     */
    public void remove(final K pId) {
        final T lEntity = load(pId);

        if (lEntity != null) {
            remove(lEntity);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#createQuery(java.lang.String)
     */
    public Query createQuery(final String pRequest) {
        return getSession(false).createQuery(pRequest);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#execute(org.hibernate.Query)
     */
    @SuppressWarnings("unchecked")
    public List<T> execute(final Query pQuery) {
        return (List<T>) pQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#execute(org.hibernate.Query,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> execute(final Query pQuery, final Class<E> pClass) {
        return (List<E>) pQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#iterate(org.hibernate.Query)
     */
    @SuppressWarnings("unchecked")
    public Iterator<T> iterate(Query pQuery) {
        return (Iterator<T>) pQuery.iterate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#iterate(org.hibernate.Query,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <E> Iterator<E> iterate(final Query pQuery, final Class<E> pClass) {
        return (Iterator<E>) pQuery.iterate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#getFirst(org.hibernate.Query)
     */
    public T getFirst(final Query pQuery) {
        return getFirst(pQuery, entityClass);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#getFirst(org.hibernate.Query,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <E> E getFirst(final Query pQuery, final Class<E> pClass) {
        pQuery.setMaxResults(1);

        final Iterator<E> lResults = pQuery.iterate();
        final E lResult;

        if (lResults.hasNext()) {
            lResult = lResults.next();
        }
        else {
            lResult = null;
        }
        closeIteratorResult(lResults);

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#getFirst(org.hibernate.Query,
     *      java.util.Comparator)
     */
    @SuppressWarnings("unchecked")
    public T getFirst(final Query pQuery, final Comparator<T> pComparator) {
        final List<T> lResults = pQuery.list();
        final T lResult;

        if (lResults == null || lResults.isEmpty()) {
            lResult = null;
        }
        else {
            Collections.sort(lResults, pComparator);
            lResult = lResults.get(0);
        }

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#hasResult(org.hibernate.Query)
     */
    public boolean hasResult(Query pQuery) {
        pQuery.setMaxResults(1);
        final Iterator<?> lResults = pQuery.iterate();
        final boolean lHasNext = lResults.hasNext();

        closeIteratorResult(lResults);

        return lHasNext;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#flush()
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.IDao#evict(org.topcased.gpm.domain.PersistentObject)
     */
    public void evict(final T pEntity) {
        getSession().evict(pEntity);
    }

    /**
     * Close iterator result.
     * 
     * @param pResults
     *            The iterator result to close.
     */
    private void closeIteratorResult(final Iterator<?> pResults) {
        while (pResults.hasNext()) {
            pResults.next();
        }
    }
}
/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.session;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * Session factory that can be recreated
 * 
 * @author tpanuel
 */
public class DynamicSessionFactory implements SessionFactory {
    private static final long serialVersionUID = 6995471254708008169L;

    private SessionFactory sessionFactory;

    /**
     * Default constructor for dynamic session factory
     */
    public DynamicSessionFactory(SessionFactory pStaticSessionFactory) {
        sessionFactory = pStaticSessionFactory;
    }

    /**
     * Replace the session factory
     * 
     * @param pSessionFactory
     *            The new session factory
     */
    public void setDynamicSessionFactory(SessionFactory pSessionFactory) {
        // Replace session factory
        sessionFactory = pSessionFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#close()
     */
    public void close() throws HibernateException {
        sessionFactory.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evict(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
	public void evict(Class pArg0) throws HibernateException {
        sessionFactory.evict(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evict(java.lang.Class,
     *      java.io.Serializable)
     */
    @SuppressWarnings("rawtypes")
	public void evict(Class pArg0, Serializable pArg1)
        throws HibernateException {
        sessionFactory.evict(pArg0, pArg1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String)
     */
    public void evictCollection(String pArg0) throws HibernateException {
        sessionFactory.evictCollection(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictCollection(java.lang.String,
     *      java.io.Serializable)
     */
    public void evictCollection(String pArg0, Serializable pArg1)
        throws HibernateException {
        sessionFactory.evictCollection(pArg0, pArg1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictEntity(java.lang.String)
     */
    public void evictEntity(String pArg0) throws HibernateException {
        sessionFactory.evictEntity(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictEntity(java.lang.String,
     *      java.io.Serializable)
     */
    public void evictEntity(String pArg0, Serializable pArg1)
        throws HibernateException {
        sessionFactory.evictEntity(pArg0, pArg1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictQueries()
     */
    public void evictQueries() throws HibernateException {
        sessionFactory.evictQueries();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#evictQueries(java.lang.String)
     */
    public void evictQueries(String pArg0) throws HibernateException {
        sessionFactory.evictQueries(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getAllClassMetadata()
     */
    @SuppressWarnings("rawtypes")
	public Map getAllClassMetadata() throws HibernateException {
        return sessionFactory.getAllClassMetadata();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getAllCollectionMetadata()
     */
    @SuppressWarnings("rawtypes")
	public Map getAllCollectionMetadata() throws HibernateException {
        return sessionFactory.getAllCollectionMetadata();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
	public ClassMetadata getClassMetadata(Class pArg0)
        throws HibernateException {
        return sessionFactory.getClassMetadata(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
     */
    public ClassMetadata getClassMetadata(String pArg0)
        throws HibernateException {
        return sessionFactory.getClassMetadata(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getCollectionMetadata(java.lang.String)
     */
    public CollectionMetadata getCollectionMetadata(String pArg0)
        throws HibernateException {
        return sessionFactory.getCollectionMetadata(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    public Session getCurrentSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getDefinedFilterNames()
     */
    @SuppressWarnings("rawtypes")
	public Set getDefinedFilterNames() {
        return sessionFactory.getDefinedFilterNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getFilterDefinition(java.lang.String)
     */
    public FilterDefinition getFilterDefinition(String pArg0)
        throws HibernateException {
        return sessionFactory.getFilterDefinition(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#getStatistics()
     */
    public Statistics getStatistics() {
        return sessionFactory.getStatistics();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#isClosed()
     */
    public boolean isClosed() {
        return sessionFactory.isClosed();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openSession()
     */
    public Session openSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection)
     */
    public Session openSession(Connection pArg0) {
        return sessionFactory.openSession(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openSession(org.hibernate.Interceptor)
     */
    public Session openSession(Interceptor pArg0) throws HibernateException {
        return sessionFactory.openSession(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openSession(java.sql.Connection,
     *      org.hibernate.Interceptor)
     */
    public Session openSession(Connection pArg0, Interceptor pArg1) {
        return sessionFactory.openSession(pArg0, pArg1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openStatelessSession()
     */
    public StatelessSession openStatelessSession() {
        return sessionFactory.openStatelessSession();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.SessionFactory#openStatelessSession(java.sql.Connection)
     */
    public StatelessSession openStatelessSession(Connection pArg0) {
        return sessionFactory.openStatelessSession(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    public Reference getReference() throws NamingException {
        return sessionFactory.getReference();
    }
}

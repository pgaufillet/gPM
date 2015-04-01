/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.session;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.dynamic.DynamicClassRegister;
import org.topcased.gpm.domain.dynamic.container.link.DynamicLinkGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.product.DynamicProductGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.revision.DynamicRevisionGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.sheet.DynamicSheetGeneratorFactory;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.link.LinkType;
import org.topcased.gpm.domain.product.ProductType;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.domain.util.driver.DriverSQL;

/**
 * Custom SessionFactory bean implementation.
 * <p>
 * This specific implementation simply creates the database schema (tables) when
 * the bean properties has been defined.
 * 
 * @author llatil
 */
public class GpmSessionFactory extends AnnotationSessionFactoryBean implements
        InitializingBean {
    /** Name of the table checked to detect if tables must be created in DB */
    private static final String TABLE_NAME_CHECKED = "sheet";

    /** The Hibernate session factory */
    private DynamicSessionFactory dynamicSessionFactory = null;

    /**
     * Get the Hibernate session
     * 
     * @return Hibernate session
     */
    final public static Session getHibernateSession() {
        return getHibernateSessionFactory().getCurrentSession();
    }

    /**
     * Get the Hibernate session factory
     * 
     * @return Hibernate session factory
     */
    final public static SessionFactory getHibernateSessionFactory() {
        return getInstance().getSessionFactory();
    }

    /**
     * Get the singleton instance of this class
     * 
     * @return The GpmSessionFactory singleton
     */
    final public static GpmSessionFactory getInstance() {
        return (GpmSessionFactory) ContextLocator.getContext().getBean(
                "&sessionFactory");
    }

    /**
     * Check if the database contains the required tables
     * 
     * @return True if the tables exist
     * @throws SQLException
     */
    public boolean hasTables() {
        boolean lHasTables = false;

        try {
            final Connection lConnection = getConnection();
            final DatabaseMetaData lMetadata = lConnection.getMetaData();
            final ResultSet lResultSet;

            // This is an ugly hack !
            if (lMetadata.getDatabaseProductName().equalsIgnoreCase("Oracle")) {
                lResultSet =
                        lConnection.getMetaData().getTables(null,
                                lMetadata.getUserName(), null, null);
            }
            else {
                lResultSet =
                        lConnection.getMetaData().getTables(null, null, null,
                                null);
            }

            while (lResultSet.next()) {
                if (TABLE_NAME_CHECKED.equalsIgnoreCase(lResultSet.getString("TABLE_NAME"))) {
                    lHasTables = true;
                    break;
                }
            }

            lResultSet.close();
            lConnection.close();
        }
        catch (SQLException e) {
            throw new GDMException(e);
        }

        return lHasTables;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#newSessionFactory(org.hibernate.cfg.Configuration)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected SessionFactory buildSessionFactory() throws Exception {
        SessionFactory lSessionFactory = super.buildSessionFactory();

        // If no dynamic session factory, the previous session factory created
        // contains only static model
        if (dynamicSessionFactory == null) {
            // Create dynamic session factory
            dynamicSessionFactory = new DynamicSessionFactory(lSessionFactory);

            // Update data base properties on DynamicObjectNamesUtils
            try {
                final Connection lConnection = getConnection();
                final DatabaseMetaData lMetadata = lConnection.getMetaData();

                DynamicObjectNamesUtils.getInstance().initDbNameMaxSize(
                        lMetadata.getMaxTableNameLength(),
                        lMetadata.getMaxColumnNameLength());
                lConnection.close();
            }
            catch (SQLException e) {
                throw new GDMException("Cannot read data base informations");
            }

            // Create the driver SQL
            driverSQL =
                    DriverSQL.getDriverSQL(Dialect.getDialect(getConfiguration().getProperties()));

            // Load dynamic model, if static data base has been created
            if (hasTables()) {
                final Session lStaticSession =
                        dynamicSessionFactory.openSession();

                // Initialize generator for sheets and revisions
                for (SheetType lType : (List<SheetType>) lStaticSession.createQuery(
                        "from SheetType").list()) {
                    DynamicSheetGeneratorFactory.getInstance().initDynamicObjectGenerator(
                            lType.getId(), lType);
                    // Create revision schema only if is revision enabled
                    for (Attribute lAttribute : lType.getAttributes()) {
                        if (lAttribute.getName().equals(
                                RevisionService.REVISION_ENABLED_ATTRIBUTE_NAME)) {
                            final List<AttributeValue> lAttributeValues =
                                    lAttribute.getAttributeValues();

                            if (lAttributeValues != null
                                    && !lAttributeValues.isEmpty()
                                    && Boolean.valueOf(lAttributeValues.get(0).getValue())) {
                                DynamicRevisionGeneratorFactory.getInstance().initDynamicObjectGenerator(
                                        lType.getId(), lType);
                            }
                            break;
                        }
                    }
                }

                // Initialize generator for links
                for (LinkType lType : (List<LinkType>) lStaticSession.createQuery(
                        "from LinkType").list()) {
                    DynamicLinkGeneratorFactory.getInstance().initDynamicObjectGenerator(
                            lType.getId(), lType);
                }

                // Initialize generator for products
                for (ProductType lType : (List<ProductType>) lStaticSession.createQuery(
                        "from ProductType").list()) {
                    DynamicProductGeneratorFactory.getInstance().initDynamicObjectGenerator(
                            lType.getId(), lType);
                }

                // Close session
                lStaticSession.close();
            }

            if (!DynamicClassRegister.getDynamicClasses().isEmpty()) {
                // Need to create a new session factory with dynamic model
                lSessionFactory = super.buildSessionFactory();
            }
        }

        return dynamicSessionFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#newSessionFactory(org.hibernate.cfg.Configuration)
     */
    @Override
    protected SessionFactory newSessionFactory(Configuration pConfiguration) {
        // Close old session factory
        if (dynamicSessionFactory != null) {
            dynamicSessionFactory.close();
        }

        // Create new session factory
        final SessionFactory lNewSessionFactory =
                super.newSessionFactory(pConfiguration);

        // Replace old session factory on dynamic session factory
        if (dynamicSessionFactory != null) {
            dynamicSessionFactory.setDynamicSessionFactory(lNewSessionFactory);
        }

        return lNewSessionFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean#postProcessAnnotationConfiguration(org.hibernate.cfg.AnnotationConfiguration)
     */
    @Override
    protected void postProcessAnnotationConfiguration(
            AnnotationConfiguration pConfiguration) throws HibernateException {
        // No dynamic session factory -> only load static schema
        if (dynamicSessionFactory != null) {
            // Add dynamic mapping
            for (Class<?> lDynamicClass : DynamicClassRegister.getDynamicClasses()) {
                pConfiguration.addAnnotatedClass(lDynamicClass);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#createDatabaseSchema()
     */
    @Override
    public void createDatabaseSchema() {
        dropDatabaseSchema();
        super.createDatabaseSchema();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#updateDatabaseSchema()
     */
    @Override
    public void updateDatabaseSchema() {
        rebuildSessionFactory();
        // Hack for fix bug(?) of Hibernate
        // Execute creation script and not update script
        // Catching exception on schema's elements already created
        try {
            final Connection lConnection = getConnection();
            final Statement lStatement = lConnection.createStatement();
            final List<String> lScript = getDynamicSchemaCreationScript();

            for (String lCommand : lScript) {
                try {
                    lStatement.execute(lCommand);
                }
                catch (SQLException e) {
                    // Do nothing, the element is already present on the data base
                }
            }

            // Commit and close
            lConnection.commit();
            lStatement.close();
        }
        catch (SQLException e) {
            throw new GDMException(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#dropDatabaseSchema()
     */
    @Override
    public void dropDatabaseSchema() {
        super.dropDatabaseSchema();
        // Reset all dynamic generators
        DynamicSheetGeneratorFactory.getInstance().reset();
        DynamicRevisionGeneratorFactory.getInstance().reset();
        DynamicLinkGeneratorFactory.getInstance().reset();
        DynamicProductGeneratorFactory.getInstance().reset();
        // Re build dynamic session
        rebuildSessionFactory();
    }

    /**
     * Get a connection on the DB
     * 
     * @return A connection on the DB
     */
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        }
        catch (SQLException e) {
            throw new GDMException("Cannot connect to data base" + e);
        }
    }

    /**
     * Re build the session factory -> current session is committed and closed
     */
    private void rebuildSessionFactory() {
        // Commit current transaction
        final HibernateTransactionManager lTransactionMgr =
                (HibernateTransactionManager) ContextLocator.getContext().getBean(
                        "transactionManager");
        // Get current transaction : null if no transaction open
        final TransactionStatus lTransactionStatus =
                lTransactionMgr.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));

        // Commit if a transaction is open
        if (lTransactionStatus != null) {
            // Commit transaction
            lTransactionMgr.commit(lTransactionStatus);
        }

        boolean lSessionToOpen = false;

        // Close current session
        try {
            dynamicSessionFactory.getCurrentSession().close();
            lSessionToOpen = true;
        }
        catch (HibernateException e) {
            // When no current session exist, Hibernate send an exception
        }

        // Re build session factory
        try {
            buildSessionFactory();
        }
        catch (Exception e) {
            throw new GDMException("Cannot rebuild session factory");
        }

        // Re-open session, if previously closed
        if (lSessionToOpen) {
            dynamicSessionFactory.openSession();
        }
    }

    /**
     * Get the dynamic schema creation script
     * 
     * @return The dynamic schema creation script
     */
    public List<String> getDynamicSchemaCreationScript() {
        return getDynamicSchemaScript(false, true);
    }

    /**
     * Get the dynamic schema deletion script
     * 
     * @return The dynamic schema deletion script
     */
    public List<String> getDynamicDropSchemaScript() {
        return getDynamicSchemaScript(true, false);
    }

    private List<String> getDynamicSchemaScript(boolean pDeletion,
            boolean pCreation) {
        final List<String> lCommandList = new ArrayList<String>();

        try {
            final Connection lConnection = getConnection();
            final Configuration lConfiguration = getConfiguration();
            final Dialect lDialect =
                    Dialect.getDialect(lConfiguration.getProperties());

            if (pDeletion) {
                final String[] lDeletionCommands =
                        lConfiguration.generateDropSchemaScript(lDialect);

                if (lDeletionCommands != null) {
                    lCommandList.addAll(Arrays.asList(lDeletionCommands));
                }
            }

            if (pCreation) {
                final String[] lCreationCommands =
                        lConfiguration.generateSchemaCreationScript(lDialect);

                if (lCreationCommands != null) {
                    lCommandList.addAll(Arrays.asList(lCreationCommands));
                }
            }

            // Close connection
            lConnection.close();
        }
        catch (Exception e) {
            throw new GDMException(e);
        }

        return lCommandList;
    }

    /** Store data to don't use reflection each time data source is need */
    private DataSource privateDataSource = null;

    /**
     * Getter on the data source
     * 
     * @return The data source
     */
    private DataSource getDataSource() {
        // Search data source, using reflection, only one time
        if (privateDataSource == null) {
            // Search data source in parent private fields
            final Field[] lFields =
                    LocalSessionFactoryBean.class.getDeclaredFields();

            for (Field lField : lFields) {
                if (lField.getName().equals("dataSource")) {
                    lField.setAccessible(true);
                    try {
                        privateDataSource = (DataSource) lField.get(this);
                        break;
                    }
                    catch (IllegalArgumentException e) {
                        throw new RuntimeException(
                                "Cannot access to data source");
                    }
                    catch (IllegalAccessException e) {
                        throw new RuntimeException(
                                "Cannot access to data source");
                    }
                }
            }
            // Data source has not been initialized
            if (privateDataSource == null) {
                throw new RuntimeException("Cannot access to data source");
            }
        }

        return privateDataSource;
    }

    private DriverSQL driverSQL;

    /**
     * Get the data base driver that can be used to generated SQL request.
     * 
     * @return The driver.
     */
    public DriverSQL getDriverSQL() {
        return driverSQL;
    }
}
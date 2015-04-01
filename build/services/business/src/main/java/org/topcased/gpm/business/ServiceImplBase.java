/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business;

import java.util.Collection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.topcased.gpm.business.atomic.service.AtomicActionsManager;
import org.topcased.gpm.business.attributes.impl.AttributesServiceImpl;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessManager;
import org.topcased.gpm.business.cache.CacheableFactory;
import org.topcased.gpm.business.cache.CacheableObjectFactoriesMgr;
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.extensions.impl.ExtensionsServiceImpl;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.fields.FieldsManager;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.impl.FieldsContainerServiceImpl;
import org.topcased.gpm.business.i18n.impl.I18nServiceImpl;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.lifecycle.impl.LifeCycleServiceImpl;
import org.topcased.gpm.business.link.impl.LinkServiceImpl;
import org.topcased.gpm.business.product.impl.AvailableProductsManager;
import org.topcased.gpm.business.product.impl.ProductHierarchyManager;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.revision.impl.RevisionServiceImpl;
import org.topcased.gpm.business.search.impl.SearchServiceImpl;
import org.topcased.gpm.business.search.impl.filter.FilterDataKey;
import org.topcased.gpm.business.search.impl.filter.FilterDataManager;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.topcased.gpm.business.sheet.export.impl.SheetExportServiceImpl;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.business.transformation.impl.DataTransformationServiceImpl;
import org.topcased.gpm.business.values.impl.ValuesServiceImpl;
import org.topcased.gpm.domain.accesscontrol.AccessControlDao;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;
import org.topcased.gpm.domain.attributes.GpmDao;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.businessProcess.BusinessProcessDao;
import org.topcased.gpm.domain.dictionary.EnvironmentDao;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.extensions.ExtensionsContainer;
import org.topcased.gpm.domain.extensions.ExtensionsContainerDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.FieldsContainerDao;
import org.topcased.gpm.domain.fields.LockDao;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.fields.ValuesContainerDao;
import org.topcased.gpm.domain.link.LinkType;
import org.topcased.gpm.domain.link.LinkTypeDao;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.process.NodeDao;
import org.topcased.gpm.domain.process.ProcessDefinitionDao;
import org.topcased.gpm.domain.process.TransitionDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.product.ProductDao;
import org.topcased.gpm.domain.product.ProductType;
import org.topcased.gpm.domain.product.ProductTypeDao;
import org.topcased.gpm.domain.revision.Revision;
import org.topcased.gpm.domain.revision.RevisionDao;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetDao;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.domain.sheet.SheetTypeDao;
import org.topcased.gpm.domain.util.IdentityVisitor;
import org.topcased.gpm.util.lang.CopyUtils;
import org.topcased.gpm.util.proxy.ImmutableGpmObject;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Service common base implementation.
 * 
 * @author llatil
 */
public class ServiceImplBase implements InitializingBean,
        ApplicationContextAware {

//    private static final Logger LOGGER =
//            Logger.getLogger(ServiceImplBase.class);

    public static final int CACHE_IMMUTABLE_OBJECT = 0x00;

    public static final int CACHE_MUTABLE_OBJECT = 0x01;

    public static final int CACHE_EVICT_ENTITY = 0x02;

    private CacheableFactory[] factories;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (null != factories) {
            cacheableObjectFactories.register(factories);
        }
    }

    /**
     * Add a new factory used to create cached element from DB entity
     * 
     * @param pFactory
     *            Factory object.
     */
    public void registerFactory(CacheableFactory pFactory) {
        factories = new CacheableFactory[] { pFactory };
    }

    /**
     * Add a list of new factories.
     * <p>
     * These factories are actually registered in the 'afterPropertiesSet()'
     * method.
     * 
     * @param pFactories
     *            List of factories.
     */
    public void registerFactories(CacheableFactory... pFactories) {
        factories = pFactories;
    }

    /**
     * Gets a sheet from its ID.
     * 
     * @param pSheetId
     *            The id.
     * @return The sheet
     * @throws IllegalArgumentException
     *             The given container identifier is null
     * @throws InvalidIdentifierException
     *             The given identifier does not exist in the database.
     */
    protected final Sheet getSheet(final String pSheetId)
        throws IllegalArgumentException, InvalidIdentifierException {
        return fieldsContainerServiceImpl.getValuesContainer(Sheet.class,
                pSheetId);
    }

    /**
     * Gets a values container from its ID.
     * 
     * @param pContainerId
     *            The id.
     * @return The sheet
     * @throws IllegalArgumentException
     *             The sheet hasn't been found.
     */
    protected final ValuesContainer getValuesContainer(final String pContainerId)
        throws IllegalArgumentException {
        return fieldsContainerServiceImpl.getValuesContainer(
                ValuesContainer.class, pContainerId);
    }

    /**
     * Clean a values container
     * 
     * @param <T>
     *            The type of the values container
     * @param pContainerId
     *            The id of the values container
     * @param pClass
     *            The class of the values container
     * @return The reseted values container
     */
    @SuppressWarnings("unchecked")
    protected final <T extends ValuesContainer> T getAndReset(
            final String pContainerId, Class<T> pClass) {
        if (pContainerId == null) {
            return null;
        }
        else {
            final ValuesContainer lValuesContainer =
                    valuesContainerDao.load(pContainerId, false);

            if (null != lValuesContainer) {
                // If values container found, reset the collections
                if (lValuesContainer.getAttributes() != null) {
                    lValuesContainer.getAttributes().clear();
                }
                if (lValuesContainer.getEnvironments() != null) {
                    lValuesContainer.getEnvironments().clear();
                }
            }

            return (T) lValuesContainer;
        }
    }

    /**
     * Gets a sheet from its ID, with possible eager loading.
     * 
     * @param pSheetId
     *            The id.
     * @param pEager
     *            Eager loading
     * @return The sheet
     * @throws IllegalArgumentException
     *             The sheet hasn't been found.
     */
    protected final Sheet getSheet(final String pSheetId, final boolean pEager)
        throws IllegalArgumentException {
        return fieldsContainerServiceImpl.getValuesContainer(Sheet.class,
                pSheetId, pEager);
    }

    /**
     * Get a collection of all revisions on container.
     * 
     * @param pContainerId
     *            Container identifier
     * @return Collection containing all revisions
     */
    @SuppressWarnings("unchecked")
    protected Collection<Revision> getRevisions(String pContainerId) {
        return getRevisionDao().getRevisions(pContainerId);
    }

    /**
     * Get a revision from its container id and its own id.
     * 
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @return The revision
     * @throws InvalidIdentifierException
     *             When the revision id doesn't correspond with the container id
     * @throws IllegalArgumentException
     *             When the revision Id is a null pointer.
     */
    protected Revision getRevision(String pContainerId, String pRevisionId)
        throws InvalidIdentifierException {
        if (pRevisionId == null) {
            throw new InvalidIdentifierException(null,
                    "Revision id null is invalid.");
        }
        Revision lFoundRevision =
                getRevisionDao().getRevision(pContainerId, pRevisionId);

        if (lFoundRevision == null) {
            throw new InvalidIdentifierException("Revision id " + pRevisionId
                    + " has not been found on container " + pContainerId);

        }
        return lFoundRevision;
    }

    /**
     * Get a revision from its container if and its label.
     * 
     * @param pContainerId
     *            The container identifier
     * @param pLabel
     *            The revision label
     * @return The found revision
     * @throws InvalidIdentifierException
     *             When the revision label is not corresponding to the container
     *             identifier.
     */
    protected Revision getRevisionByLabel(String pContainerId, String pLabel)
        throws InvalidIdentifierException {
        return getRevisionDao().getRevisionByLabel(pContainerId, pLabel);
    }

    /**
     * Get a product from its ID.
     * 
     * @param pProductId
     *            The id.
     * @return The product.
     */
    protected final Product getProduct(final String pProductId) {
        Product lProduct = (Product) getProductDao().load(pProductId);
        if (null == lProduct) {
            throw new IllegalArgumentException("Product id " + pProductId
                    + " invalid.");
        }
        return lProduct;
    }

    /**
     * Get a product from its ID.
     * 
     * @param pBusinessProcessName
     *            Business process name.
     * @param pProductName
     *            Name of product.
     * @param pNullIfError
     *            If true, returns 'null' when the product does not exist
     *            (otherwise an exception is raised).
     * @return The product.
     * @throws InvalidNameException
     *             When the product name does not exist.
     */
    protected final Product getProduct(final String pBusinessProcessName,
            final String pProductName, boolean pNullIfError) {
        Product lProduct =
                getProductDao().getProduct(pBusinessProcessName, pProductName);

        if (null == lProduct && !pNullIfError) {
            throw new InvalidNameException(pProductName);
        }

        return lProduct;
    }

    /**
     * Get a product from its ID.
     * 
     * @param pBusinessProcessName
     *            Business process name
     * @param pProductName
     *            Name of product.
     * @return The product.
     */
    protected final Product getProduct(final String pBusinessProcessName,
            final String pProductName) {
        return getProduct(pBusinessProcessName, pProductName, false);
    }

    /**
     * Get the list of products available for a given business process.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcessName
     *            Business process the returned products belong
     * @param pEndUser
     *            The end user
     * @return List of Product
     * @throws InvalidTokenException
     *             The user token is blank.
     */
    @SuppressWarnings("unchecked")
    protected Collection<Product> getProductsForUser(String pUserToken,
            String pBusinessProcessName, EndUser pEndUser) {
        if (getEndUserDao().hasGlobalRole(pEndUser.getLogin(),
                pBusinessProcessName)) {
            BusinessProcess lProc = getBusinessProcess(pBusinessProcessName);
            Query lQuery =
                    GpmSessionFactory.getHibernateSession().createFilter(
                            lProc.getProducts(), "order by this.name");
            return lQuery.list();
        }
        else {
            return getEndUserDao().getProducts(pEndUser, pBusinessProcessName);
        }
    }

    /**
     * Get a sheet type from its ID.
     * 
     * @param pSheetTypeId
     *            The sheet type id.
     * @return The sheet type
     * @throws InvalidIdentifierException
     *             When the type identifier is invalid.
     */
    protected final SheetType getSheetType(final String pSheetTypeId)
        throws InvalidIdentifierException {
        return getSheetType(pSheetTypeId, false);
    }

    /**
     * Get a sheet type from its ID.
     * 
     * @param pSheetTypeId
     *            The sheet type id.
     * @param pNullIfError
     *            If true, returns 'null' when the product does not exist
     *            (otherwise an exception is raised).
     * @return The sheet type
     * @throws InvalidIdentifierException
     *             When the type identifier is invalid (only thrown if
     *             pNullIfError is false)
     */
    protected final SheetType getSheetType(final String pSheetTypeId,
            boolean pNullIfError) throws InvalidIdentifierException {
        if (getSheetTypeDao().exist(pSheetTypeId)) {
            return getSheetTypeDao().load(pSheetTypeId);
        }
        else {
            if (pNullIfError) {
                return null;
            }
            else {
                throw new InvalidIdentifierException("Sheet type id "
                        + pSheetTypeId + " invalid.");
            }
        }
    }

    /**
     * Get a link type from its ID.
     * 
     * @param pLinkTypeId
     *            The id.
     * @return The link type
     */
    protected final LinkType getLinkType(final String pLinkTypeId) {
        LinkType lLinkType = (LinkType) linkTypeDao.load(pLinkTypeId);
        if (null == lLinkType) {
            throw new IllegalArgumentException("Link type id " + pLinkTypeId
                    + " invalid.");
        }
        return lLinkType;
    }

    /**
     * Get a link type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pLinkTypeName
     *            Name of link type.
     * @param pNullIfError
     *            If true, returns null if link type name is invalid
     * @return The link type
     */
    protected final LinkType getLinkType(final String pBusinessProcessName,
            final String pLinkTypeName, boolean pNullIfError) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        LinkType lLinkType =
                linkTypeDao.getLinkType(pLinkTypeName, lBusinessProcess);

        if (null == lLinkType && !pNullIfError) {
            throw new InvalidNameException(pLinkTypeName,
                    "Link type name {0} invalid");
        }
        return lLinkType;
    }

    /**
     * Get a link type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pLinkTypeName
     *            Name of link type.
     * @return The link type
     */
    protected final LinkType getLinkType(final String pBusinessProcessName,
            final String pLinkTypeName) {
        return getLinkType(pBusinessProcessName, pLinkTypeName, false);
    }

    /**
     * Get a fields container from its ID.
     * 
     * @param pFieldsContainerId
     *            The id.
     * @return The sheet type
     */
    protected final FieldsContainer getFieldsContainer(
            final String pFieldsContainerId) {
        FieldsContainer lFieldsContainer =
                (FieldsContainer) getFieldsContainerDao().load(
                        pFieldsContainerId);
        if (null == lFieldsContainer) {
            throw new IllegalArgumentException("Fields container id "
                    + pFieldsContainerId + " invalid.");
        }
        return lFieldsContainer;
    }

    /**
     * Get a fields container from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pName
     *            Name of the type.
     * @param pNullIfError
     *            Returns null if the sheet type name does not exist
     * @return The sheet type
     */
    protected final FieldsContainer getFieldsContainer(
            final String pBusinessProcessName, final String pName,
            boolean pNullIfError) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);

        FieldsContainer lFieldsContainer =
                getFieldsContainerDao().getFieldsContainer(lBusinessProcess,
                        pName);
        if (null == lFieldsContainer && !pNullIfError) {
            throw new InvalidNameException(pName, "Type name {0} invalid");
        }
        return lFieldsContainer;
    }

    /**
     * Get a fields container from its identifier.
     * 
     * @param <C>
     *            Actual type of the fields container (ex: SheetType)
     * @param pClazz
     *            Actual class of the fields container (ex: SheetType)
     * @param pId
     *            The identifier of the object.
     * @return The values container (typed according to clazz parameter)
     * @throws IllegalArgumentException
     *             The container hasn't been found.
     */
    @SuppressWarnings("unchecked")
    public final <C extends FieldsContainer> C getFieldsContainer(
            Class<C> pClazz, String pId) throws IllegalArgumentException {
        ExtensionsContainer lExtContainer =
                (ExtensionsContainer) getFieldsContainerDao().load(pId);

        if (!(lExtContainer instanceof FieldsContainer)) {
            throw new InvalidIdentifierException(pId,
                    "Container id ''{0}'' invalid");
        }

        return (C) lExtContainer;
    }

    /**
     * Get a sheet type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pSheetTypeName
     *            Name of sheet type.
     * @param pNullIfError
     *            Returns null if the sheet type name does not exist
     * @return The sheet type
     */
    protected final SheetType getSheetType(final String pBusinessProcessName,
            final String pSheetTypeName, boolean pNullIfError) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        SheetType lSheetType =
                getSheetTypeDao().getSheetType(lBusinessProcess, pSheetTypeName);

        if (null == lSheetType && !pNullIfError) {
            throw new InvalidNameException(pSheetTypeName,
                    "Sheet type name {0} invalid");
        }
        return lSheetType;
    }

    /**
     * Get a sheet type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pSheetTypeName
     *            Name of sheet type.
     * @return The sheet type
     */
    protected final SheetType getSheetType(final String pBusinessProcessName,
            final String pSheetTypeName) {
        return getSheetType(pBusinessProcessName, pSheetTypeName, false);
    }

    /**
     * Gets a sheet type from its name.
     * 
     * @param pSheetTypeName
     *            Name of sheet type.
     * @param pBusinessProcess
     *            Business process.
     * @return The sheet type
     */
    protected final SheetType getSheetType(final String pSheetTypeName,
            final BusinessProcess pBusinessProcess) {
        SheetType lSheetType =
                getSheetTypeDao().getSheetType(pBusinessProcess, pSheetTypeName);
        if (null == lSheetType) {
            throw new InvalidNameException(pSheetTypeName,
                    "Sheet type name {0} invalid");
        }
        return lSheetType;
    }

    /**
     * Gets a business process from its name.
     * 
     * @param pProcessName
     *            Name of business process.
     * @return The business process
     * @throws InvalidNameException
     *             The business process is invalid.
     */
    protected final BusinessProcess getBusinessProcess(final String pProcessName)
        throws InvalidNameException {
        BusinessProcess lBusinessProc =
                getBusinessProcessDao().getBusinessProcess(pProcessName);

        if (null == lBusinessProc) {
            throw new InvalidNameException(pProcessName,
                    "Business process {0} invalid");
        }
        return lBusinessProc;
    }

    /**
     * Gets the business process name from the roleToken.
     * 
     * @param pRoleToken
     *            the role token of a user logged on the business process
     * @return Name of business process.
     */
    protected final String getBusinessProcessName(final String pRoleToken) {
        return getAuthService().getProcessNameFromToken(pRoleToken);
    }

    /**
     * Get the life cycle service.
     * 
     * @return Life cycle service.
     */
    final protected LifeCycleServiceImpl getLifeCycleService() {
        return lifeCycleServiceImpl;
    }

    /**
     * Sets the life cycle service impl.
     * 
     * @param pLifeCycleService
     *            the new life cycle service impl
     */
    public void setLifeCycleServiceImpl(LifeCycleServiceImpl pLifeCycleService) {
        lifeCycleServiceImpl = pLifeCycleService;
    }

    /** The life cycle service impl. */
    private LifeCycleServiceImpl lifeCycleServiceImpl;

    /**
     * Get the sheet service.
     * 
     * @return Sheet service.
     */
    final protected SheetServiceImpl getSheetService() {
        return sheetServiceImpl;
    }

    /**
     * Sets the sheet service impl.
     * 
     * @param pSheetServiceImpl
     *            the new sheet service impl
     */
    public void setSheetServiceImpl(SheetServiceImpl pSheetServiceImpl) {
        sheetServiceImpl = pSheetServiceImpl;
    }

    /** The sheet service impl. */
    private SheetServiceImpl sheetServiceImpl;

    /**
     * Get the product service.
     * 
     * @return product service.
     */
    final protected ProductServiceImpl getProductService() {
        return productServiceImpl;
    }

    /**
     * Sets the product service impl.
     * 
     * @param pProductServiceImpl
     *            the new product service impl
     */
    public void setProductServiceImpl(ProductServiceImpl pProductServiceImpl) {
        productServiceImpl = pProductServiceImpl;
    }

    /** The product service impl. */
    private ProductServiceImpl productServiceImpl;

    /**
     * Get the link service.
     * 
     * @return Link service.
     */
    final protected LinkServiceImpl getLinkService() {
        return linkServiceImpl;
    }

    /**
     * Sets the link service impl.
     * 
     * @param pLinkServiceImpl
     *            the new link service impl
     */
    public void setLinkServiceImpl(LinkServiceImpl pLinkServiceImpl) {
        linkServiceImpl = pLinkServiceImpl;
    }

    /** The link service impl. */
    private LinkServiceImpl linkServiceImpl;

    /**
     * Get the revision service.
     * 
     * @return Link service.
     */
    final protected RevisionServiceImpl getRevisionService() {
        return revisionServiceImpl;
    }

    /**
     * Sets the revision service impl.
     * 
     * @param pRevisionServiceImpl
     *            the new revision service impl
     */
    public void setRevisionServiceImpl(RevisionServiceImpl pRevisionServiceImpl) {
        revisionServiceImpl = pRevisionServiceImpl;
    }

    /** The link service impl. */
    private RevisionServiceImpl revisionServiceImpl;

    /**
     * Get the environment service.
     * 
     * @return Environment service.
     */
    final protected EnvironmentServiceImpl getEnvService() {
        return environmentServiceImpl;
    }

    /**
     * Sets the environment service impl.
     * 
     * @param pEnvironmentServiceImpl
     *            the new environment service impl
     */
    public void setEnvironmentServiceImpl(
            EnvironmentServiceImpl pEnvironmentServiceImpl) {
        environmentServiceImpl = pEnvironmentServiceImpl;
    }

    /** The environment service impl. */
    private EnvironmentServiceImpl environmentServiceImpl;

    /**
     * Get the sheet export service.
     * 
     * @return Sheet export service.
     */
    final protected SheetExportServiceImpl getSheetExportService() {
        return sheetExportService;
    }

    /**
     * Set the sheet export service.
     * 
     * @param pSheetExportService
     *            the new sheet export service impl
     */
    public void setSheetExportServiceImpl(
            SheetExportServiceImpl pSheetExportService) {
        sheetExportService = pSheetExportService;
    }

    /** The sheet export service impl. */
    private SheetExportServiceImpl sheetExportService;

    /**
     * Get the authorization service.
     * 
     * @return Authorization service.
     */
    final protected AuthorizationServiceImpl getAuthService() {
        return authorizationService;
    }

    /**
     * Sets the authorization service impl.
     * 
     * @param pAuthorizationServiceImpl
     *            the new authorization service impl
     */
    public void setAuthorizationServiceImpl(
            AuthorizationServiceImpl pAuthorizationServiceImpl) {
        authorizationService = pAuthorizationServiceImpl;
    }

    /** The authorization service impl. */
    protected AuthorizationServiceImpl authorizationService;

    /**
     * Get the i18n service.
     * 
     * @return i18n service.
     */
    final protected I18nService getI18nService() {
        return i18nService;
    }

    /**
     * Set the I18N service.
     * 
     * @param pI18nService
     *            I18N service.
     */
    final public void setI18nServiceImpl(final I18nServiceImpl pI18nService) {
        i18nService = pI18nService;
    }

    // Use the implementation of the service directly (to avoid the interceptors
    // overhead of the service.
    /** The i18n service impl. */
    protected I18nServiceImpl i18nService;

    /**
     * Get the extensions point service.
     * 
     * @return Extensions service.
     */
    final protected ExtensionsService getExtensionsService() {
        return extensionsServiceImpl;
    }

    public final void setExtensionsServiceImpl(
            ExtensionsServiceImpl pExtensionsServiceImpl) {
        extensionsServiceImpl = pExtensionsServiceImpl;
    }

    protected ExtensionsServiceImpl extensionsServiceImpl;

    /**
     * Get the serialization service.
     * 
     * @return serialization service.
     */
    final protected SerializationService getSerializationService() {
        return ServiceLocator.instance().getSerializationService();
    }

    /** The attributes service impl. */
    private AttributesServiceImpl attributesServiceImpl;

    /**
     * Sets the attributes service impl.
     * 
     * @param pAttributesServiceImpl
     *            the new attributes service impl
     */
    public final void setAttributesServiceImpl(
            AttributesServiceImpl pAttributesServiceImpl) {
        attributesServiceImpl = pAttributesServiceImpl;
    }

    /**
     * Get the attributes service.
     * 
     * @return attributes service.
     */
    public final AttributesServiceImpl getAttributesService() {
        return attributesServiceImpl;
    }

    /**
     * Get the fields service.
     * 
     * @return Fields service.
     */
    final protected FieldsService getFieldsService() {
        return ServiceLocator.instance().getFieldsService();
    }

    private SearchServiceImpl searchService;

    /**
     * Set the search service implementation (to be called by Spring Fw only)
     * 
     * @param pSearchService
     *            Search service bean
     */
    public final void setSearchServiceImpl(SearchServiceImpl pSearchService) {
        searchService = pSearchService;
    }

    /**
     * Get the search service.
     * 
     * @return search service.
     */
    final protected SearchServiceImpl getSearchService() {
        return searchService;
    }

    protected ValuesServiceImpl valuesServiceImpl;

    /**
     * Set the values service impl.
     * 
     * @param pValuesServiceImpl
     *            The new values service impl
     */
    public void setValuesServiceImpl(final ValuesServiceImpl pValuesServiceImpl) {
        valuesServiceImpl = pValuesServiceImpl;
    }

    /** SheetType DAO. */
    protected org.topcased.gpm.domain.sheet.SheetTypeDao sheetTypeDao;

    /**
     * Sets the reference to <code>sheetType</code>'s DAO.
     * 
     * @param pSheetTypeDao
     *            Data Access Object
     */
    final public void setSheetTypeDao(final SheetTypeDao pSheetTypeDao) {
        sheetTypeDao = pSheetTypeDao;
    }

    /**
     * Gets the reference to <code>sheetType</code>'s DAO.
     * 
     * @return Data Access Object
     */
    final protected SheetTypeDao getSheetTypeDao() {
        return sheetTypeDao;
    }

    /** Sheet DAO. */
    protected org.topcased.gpm.domain.sheet.SheetDao sheetDao;

    /**
     * Sets the reference to <code>sheet</code>'s DAO.
     * 
     * @param pSheetDao
     *            Data Access Object
     */
    final public void setSheetDao(final SheetDao pSheetDao) {
        sheetDao = pSheetDao;
    }

    /**
     * Gets the reference to <code>sheet</code>'s DAO.
     * 
     * @return Data Access Object
     */
    final protected SheetDao getSheetDao() {
        return sheetDao;
    }

    /** Revision DAO. */
    protected org.topcased.gpm.domain.revision.RevisionDao revisionDao;

    /**
     * Sets the reference to Revision DAO.
     * 
     * @param pRevisionDao
     *            Data Access object
     */
    final public void setRevisionDao(final RevisionDao pRevisionDao) {
        revisionDao = pRevisionDao;
    }

    /**
     * Gets the reference to Revision DAO.
     * 
     * @return Data Access Object
     */
    final protected RevisionDao getRevisionDao() {
        return revisionDao;
    }

    /** BusinessProcess DAO. */
    protected BusinessProcessDao businessProcessDao;

    /**
     * Gets the reference to BusinessProcess DAO.
     * 
     * @return Data Access Object
     */
    final protected BusinessProcessDao getBusinessProcessDao() {
        return businessProcessDao;
    }

    /**
     * Sets the reference to BusinessProcess DAO.
     * 
     * @param pBusinessProcessDao
     *            Data Access Object
     */
    final public void setBusinessProcessDao(
            final BusinessProcessDao pBusinessProcessDao) {
        businessProcessDao = pBusinessProcessDao;
    }

    /** The environment dao. */
    private EnvironmentDao environmentDao;

    /**
     * Get the environment DAO.
     * 
     * @return Returns the environmentDao.
     */
    final protected EnvironmentDao getEnvironmentDao() {
        return environmentDao;
    }

    /**
     * Set the environment DAO.
     * 
     * @param pEnvironmentDao
     *            The environmentDao to set.
     */
    final public void setEnvironmentDao(final EnvironmentDao pEnvironmentDao) {
        environmentDao = pEnvironmentDao;
    }

    /** The product dao. */
    private ProductDao productDao;

    /**
     * Get the Product DAO.
     * 
     * @return Returns the productDao.
     */
    final protected ProductDao getProductDao() {
        return productDao;
    }

    /**
     * Set the Product DAO.
     * 
     * @param pProductDao
     *            The productDao to set.
     */
    final public void setProductDao(final ProductDao pProductDao) {
        productDao = pProductDao;
    }

    /** The fields container dao. */
    private FieldsContainerDao fieldsContainerDao;

    /**
     * Get the Fields Container DAO.
     * 
     * @return Returns the fieldsContainerDao.
     */
    protected final FieldsContainerDao getFieldsContainerDao() {
        return fieldsContainerDao;
    }

    /**
     * Set the Fields Container DAO.
     * 
     * @param pFieldsContainerDao
     *            The fieldsContainerDao to set.
     */
    public final void setFieldsContainerDao(
            final FieldsContainerDao pFieldsContainerDao) {
        fieldsContainerDao = pFieldsContainerDao;
    }

    /** The lock dao. */
    private LockDao lockDao;

    /**
     * get LockDao.
     * 
     * @return the LockDao
     */
    public LockDao getLockDao() {
        return lockDao;
    }

    /**
     * set containerLockDao.
     * 
     * @param pLockDao
     *            the DAO to set
     */
    public void setLockDao(LockDao pLockDao) {
        lockDao = pLockDao;
    }

    /** The fields manager. */
    protected FieldsManager fieldsManager;

    /**
     * Get the Field Manager.
     * 
     * @return Returns the fieldsMgr.
     */
    protected final FieldsManager getFieldsManager() {
        return fieldsManager;
    }

    /**
     * Set the Field Manager.
     * 
     * @param pFieldsMgr
     *            The fieldsMgr to set.
     */
    public final void setFieldsManager(final FieldsManager pFieldsMgr) {
        fieldsManager = pFieldsMgr;
    }

    /** The product type dao. */
    private ProductTypeDao productTypeDao;

    /**
     * getProductTypeDao.
     * 
     * @return the ProductTypeDao
     */
    public ProductTypeDao getProductTypeDao() {
        return productTypeDao;
    }

    /**
     * setProductTypeDao.
     * 
     * @param pProductTypeDao
     *            the ProductTypeDao to set
     */
    public void setProductTypeDao(final ProductTypeDao pProductTypeDao) {
        productTypeDao = pProductTypeDao;
    }

    /**
     * Get a product type from its ID.
     * 
     * @param pProductTypeId
     *            The id.
     * @return The product type
     */
    protected final ProductType getProductType(final String pProductTypeId) {
        ProductType lProductType =
                (ProductType) getProductTypeDao().load(pProductTypeId);
        if (null == lProductType) {
            throw new IllegalArgumentException("Product type id "
                    + pProductTypeId + " invalid.");
        }
        return lProductType;
    }

    /**
     * Get a product type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pProductTypeName
     *            Name of product type.
     * @param pNullIfError
     *            Returns null (instead of throwing an exception) in case of
     *            error (invalid product name).
     * @return The product type
     */
    protected final ProductType getProductType(
            final String pBusinessProcessName, final String pProductTypeName,
            boolean pNullIfError) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        ProductType lProductType =
                getProductTypeDao().getProductType(lBusinessProcess,
                        pProductTypeName);

        if (null == lProductType && !pNullIfError) {
            throw new InvalidNameException(pProductTypeName,
                    "Product type name {0} invalid");
        }
        return lProductType;
    }

    /**
     * Get a product type from its name.
     * 
     * @param pBusinessProcessName
     *            Name of business process.
     * @param pProductTypeName
     *            Name of product type.
     * @return The product type
     */
    protected final ProductType getProductType(
            final String pBusinessProcessName, final String pProductTypeName) {
        return getProductType(pBusinessProcessName, pProductTypeName, false);
    }

    /**
     * Gets a product type from its name.
     * 
     * @param pProductTypeName
     *            Name of product type.
     * @param pBusinessProcess
     *            Business process.
     * @return The product type
     */
    protected final ProductType getProductType(final String pProductTypeName,
            final BusinessProcess pBusinessProcess) {
        ProductType lProductType =
                getProductTypeDao().getProductType(pBusinessProcess,
                        pProductTypeName);
        if (null == lProductType) {
            throw new InvalidNameException(pProductTypeName,
                    "Product type name {0} invalid");
        }
        return lProductType;
    }

    /**
     * Get the language name to use for a given role session.
     * 
     * @param pToken
     *            Role / user session token
     * @return Name of language of to use
     */
    protected final String getLanguageFromToken(final String pToken) {
        if (!StringUtils.isBlank(pToken)) {
            return getI18nService().getPreferredLanguage(pToken);
        }
        return null;
    }

    /**
     * Gets the extension point from an extensions container
     * 
     * @param pExtensionsContainerId
     *            The id of the extensions container
     * @param pExtPointName
     *            The extension point name
     * @param pContext
     *            Current execution context
     * @return The extension point, or null if not defined or disabled in the
     *         given context
     */
    protected ExtensionPoint getExecutableExtensionPoint(
            final String pExtensionsContainerId, final String pExtPointName,
            final Context pContext) {
        ExtensionPoint lExtensionPoint = null;

        if (extensionsServiceImpl.isExtensionPointEnabled(pExtPointName, pContext)) {
            lExtensionPoint =
                    getExtensionsContainerDao().getExtensionPoint(pExtensionsContainerId, pExtPointName);
        }

        return lExtensionPoint;
    }

    /**
     * Gets the extension point on a node
     * 
     * @param pNode
     *            The node
     * @param pExtPointName
     *            The extension point name
     * @param pContext
     *            Current execution context
     * @return The extension point, or null if not defined or disabled in the
     *         given context
     */
    protected ExtensionPoint getExecutableExtensionPoint(final Node pNode,
            final String pExtPointName, final Context pContext) {
        ExtensionPoint lExtensionPoint = null;

        if (extensionsServiceImpl.isExtensionPointEnabled(pExtPointName,
                pContext)) {
            lExtensionPoint =
                    getExtensionsContainerDao().getExtensionPoint(pNode,
                            pExtPointName);
        }

        return lExtensionPoint;
    }

    /**
     * Add a new element in cache, cache only contain immutable object
     * 
     * @param pElement
     *            Element to cache
     */
    protected final void addElementInCache(CacheableGpmObject pElement) {
        final CacheableGpmObject lClone;

        // If the element is already immutable, only copy it
        if (pElement instanceof ImmutableGpmObject) {
            lClone = CopyUtils.deepClone(pElement);
        }
        else {
            lClone = CopyUtils.getImmutableCopy(pElement);
        }

        final Element lElem = new Element(lClone.getId(), lClone);
        gpmGlobalCache.put(lElem);
    }

    /**
     * Get a GPM object from the cache.
     * 
     * @param <C>
     *            Actual class of the cached object
     * @param pClazz
     *            Expected class of the cached object
     * @param pElemId
     *            Identifier of the object
     * @param pCacheFlags
     *            Specify cache management properties (see CACHE_* values)
     * @return Object retrieved from the cache, or null if not available in
     *         cache.
     * @throws InvalidIdentifierException
     *             If the object found in cache is not compatible with the
     *             expected class.
     */
    @SuppressWarnings("unchecked")
    protected final <C extends CacheableGpmObject> C getCachedElement(
            Class<C> pClazz, String pElemId, final int pCacheFlags) {
        final Element lCachedElement = gpmGlobalCache.get(pElemId);

        if (lCachedElement == null) {
            return null;
        }
        // else
        final C lCachedObject = (C) lCachedElement.getObjectValue();

        if ((pCacheFlags & CACHE_MUTABLE_OBJECT) != 0) {
            return CopyUtils.getMutableCopy(lCachedObject);
        }
        // else
        return lCachedObject;
    }

    /**
     * Get a GPM object from the cache, or create it.
     * <p>
     * This method creates the cacheable object corresponding to the given
     * entity identifier and class if it does not exist in cache yet.
     * 
     * @param <C>
     *            Actual class of the cached object
     * @param <E>
     *            Class of the entity object
     * @param pCachedClass
     *            Expected class of the cached object
     * @param pEntityClass
     *            Class of the entity object
     * @param pElemId
     *            Identifier of the object
     * @param pCacheFlags
     *            Specify cache management properties (see CACHE_* values)
     * @return Object retrieved from the cache, or created from the DB content.
     *         Can be null if not available.
     * @throws InvalidIdentifierException
     *             If the object found in cache is not compatible with the
     *             expected class.
     */
    @SuppressWarnings("unchecked")
    protected final <C extends CacheableGpmObject, E> C getCachedElement(
            final Class<C> pCachedClass, final Class<E> pEntityClass,
            final String pElemId, final int pCacheFlags) {
        C lCachedObject = getCachedElement(pCachedClass, pElemId, pCacheFlags);

        if (lCachedObject == null) {
            final Session lHibernateSession =
                    GpmSessionFactory.getHibernateSession();
            E lEntity = (E) lHibernateSession.get(pEntityClass, pElemId);
            if (null == lEntity) {
                return null;
            }
            E lActualEntity = IdentityVisitor.getIdentity(lEntity);

            CacheableFactory lFactory =
                    cacheableObjectFactories.getFactoryForEntity(lActualEntity);
            lCachedObject = (C) lFactory.createCacheableObject(lActualEntity);

            if ((pCacheFlags & CACHE_EVICT_ENTITY) != 0) {
                lHibernateSession.evict(lEntity);
            }
            else {
                // Add element in cache (only if CACHE_EVICT_ENTITY is not set)
                addElementInCache(lCachedObject);
            }
            if ((pCacheFlags & CACHE_MUTABLE_OBJECT) == 0) {
                // An immutable object is required.
                // Get it from the cache  (as it has been converted to an immutable on in the cache)
                C lCachedElement =
                        getCachedElement(pCachedClass, pElemId, pCacheFlags);
                if (lCachedElement != null) {
                    return lCachedElement;
                }
            }
        }
        return lCachedObject;
    }

    /**
     * Get a cached field container.
     * <p>
     * Note: If the object is not present in the cache, it is read from the DB
     * (and stored in the cache).
     * 
     * @param pElemId
     *            Identifier of the element
     * @param pCacheFlags
     *            Specify cache management properties (see CACHE_* values)
     * @return The cached element, or null if not found
     */
    protected final CacheableFieldsContainer getCachedFieldsContainer(
            String pElemId, final int pCacheFlags) {
        return getCachedElement(CacheableFieldsContainer.class,
                FieldsContainer.class, pElemId, pCacheFlags);
    }

    /**
     * Get a cached values container.
     * <p>
     * Note: If the object is not present in the cache, it is read from the DB
     * (and stored in the cache).
     * 
     * @param pElemId
     *            Identifier of the element
     * @param pCacheFlags
     *            Specify cache management properties (see CACHE_* values)
     * @return The cached element, or null if not found
     */
    public final CacheableValuesContainer getCachedValuesContainer(
            String pElemId, final int pCacheFlags) {
        CacheableValuesContainer lCachedContainer =
                getCachedElement(CacheableValuesContainer.class,
                        ValuesContainer.class, pElemId, pCacheFlags);
        if (lCachedContainer != null) {
            // If sheet content available in cache, check the sheet version
            Integer lActualVersion =
                    getValuesContainerDao().getVersion(pElemId);

            // Note: Nominally, the sheet version should never be null here, as
            // it has been found in cache. Nevertheless, check for no version
            // (null), and remove the obsolete element from cache if required.
            // And check that element found in cache is still valid (comparing the
            // versions)
            if (lActualVersion == null
                    || lActualVersion != lCachedContainer.getVersion()) {
                removeElementFromCache(pElemId);
                lCachedContainer =
                        getCachedValuesContainer(pElemId, pCacheFlags);
            }
        }
        return lCachedContainer;
    }

    /**
     * Get a values container from the cache. Returns null if the identifier is
     * not available from cache.
     * 
     * @param pClazz
     *            Expected actual class of the values container
     * @param pId
     *            Identifier
     * @param <C>
     *            CacheableValuesContainer
     * @param pCacheFlags
     *            The properties of the cache
     * @return Cacheable values container from the cache (or null if not found).
     * @throws InvalidIdentifierException
     *             If the object found in cache is not compatible with the
     *             expected class.
     */
    protected final <C extends CacheableValuesContainer> C getCachedValuesContainer(
            Class<C> pClazz, String pId, final int pCacheFlags) {
        C lCachedContainer = getCachedElement(pClazz, pId, pCacheFlags);

        if (lCachedContainer != null) {
            // If sheet content available in cache, check the sheet version
            Integer lActualVersion = getValuesContainerDao().getVersion(pId);

            // Note: Nominally, the sheet version should never be null here, as
            // it has been found in cache. Nevertheless, check for no version
            // (null), and remove the obsolete element from cache if required.
            // And check that element found in cache is still valid (comparing the
            // versions)
            if (lActualVersion == null
                    || lActualVersion != lCachedContainer.getVersion()) {
                removeElementFromCache(pId);
                lCachedContainer = null;
            }
        }

        return lCachedContainer;
    }

    /**
     * Get a GPM object from the cache, or create it.
     * <p>
     * This method creates the cacheable object corresponding to the given
     * entity if it does not exist in cache yet.
     * 
     * @param <C>
     *            Actual class of the cached object
     * @param <E>
     *            Class of the entity object
     * @param pCachedClass
     *            Expected class of the cached object
     * @param pEntityObject
     *            Entity object used to create the cached value
     * @return Object retrieved from the cache, or null if not available in
     *         cache.
     * @throws InvalidIdentifierException
     *             If the object found in cache is not compatible with the
     *             expected class.
     */
    @SuppressWarnings("unchecked")
    protected final <C extends CacheableGpmObject, E> C createCacheableElement(
            Class<C> pCachedClass, Object pEntityObject) {
        CacheableFactory lFactory =
                cacheableObjectFactories.getFactoryForEntity(pEntityObject);
        C lCachedObject = (C) lFactory.createCacheableObject(pEntityObject);
        addElementInCache(lCachedObject);
        return lCachedObject;
    }

    /**
     * Gets an element from the cache.
     * 
     * @param pCacheKey
     *            the cache key
     * @return the cached object or null if not available in cache
     */
    protected final Object getCached(String pCacheKey) {
        Element lCachedElement = gpmGlobalCache.get(pCacheKey);

        if (null != lCachedElement) {
            return lCachedElement.getObjectValue();
        }
        return null;
    }

    /**
     * Gets an element from the cache.
     * 
     * @param <T>
     *            The type of cache element
     * @param pCacheKey
     *            the cache key
     * @param pClazz
     *            Class of the element to retrieve
     * @return the cached object or null if not available in cache
     * @throws InvalidIdentifierException
     *             If element exists in cache, but its class is not assignable
     *             to pClazz
     */
    @SuppressWarnings("unchecked")
    protected final <T> T getCached(String pCacheKey, Class<T> pClazz) {
        Element lCachedElement = gpmGlobalCache.get(pCacheKey);

        if (null != lCachedElement) {
            Object lValue = lCachedElement.getObjectValue();
            if (!pClazz.isInstance(lValue)) {
                throw new InvalidIdentifierException(pCacheKey);
            }
            return (T) lCachedElement.getObjectValue();
        }
        return null;
    }

    /**
     * Adds the cached.
     * 
     * @param pCacheKey
     *            the cache key
     * @param pElement
     *            the element
     */
    protected final void addCached(String pCacheKey, Object pElement) {
        if (null == pCacheKey || null == pElement) {
            throw new IllegalArgumentException("Null unexpected");
        }
        Element lElem = new Element(pCacheKey, pElement);
        gpmGlobalCache.put(lElem);
    }

    /**
     * Remove an element from the cache. This method does nothing if the
     * specified element is not in cache.<br />
     * 
     * @param pElemId
     *            Identifier of the element to remove
     * @return true if the element was in cached (and has been removed).
     */
    public final boolean removeElementFromCache(String pElemId) {
        return removeElementFromCache(pElemId, false);
    }

    /**
     * Remove an element from the cache. This method does nothing if the
     * specified element is not in cache.
     * 
     * @param pElemId
     *            Identifier of the element to remove
     * @param pFilterImpact
     *            If true remove all elements from executableFiltersCache and
     *            usableFieldDatasCache.
     * @return true if the element was in cached (and has been removed).
     */
    protected final boolean removeElementFromCache(String pElemId,
            boolean pFilterImpact) {
        if (pFilterImpact) {
            clearFilterCaches();
        }
        searchService.clearUsableFieldManager();
        return gpmGlobalCache.remove(pElemId);
    }

    protected ProductHierarchyManager productHierarchyManager;

    /**
     * Setter on ProductHierarchyManager used for Spring injection.
     * 
     * @param pProductHierarchyManager
     *            The new ProductHierarchyManager.
     */
    public void setProductHierarchyManager(
            final ProductHierarchyManager pProductHierarchyManager) {
        productHierarchyManager = pProductHierarchyManager;
    }

    protected AvailableProductsManager availableProductsManager;

    /**
     * Setter on AvailableProductsManager used for Spring injection.
     * 
     * @param pAvailableProductsManager
     *            The new AvailableProductsManager.
     */
    public void setAvailableProductsManager(
            final AvailableProductsManager pAvailableProductsManager) {
        availableProductsManager = pAvailableProductsManager;
    }

    /**
     * Remove all elements of the productNamesCache.
     * 
     * @see Cache#removeAll();
     */
    protected final void clearProductNamesCache() {
        availableProductsManager.depreciateAll();
        productHierarchyManager.depreciateAll();
    }

    /** The access control dao. */
    private AccessControlDao accessControlDao;

    /**
     * Gets the access control dao.
     * 
     * @return the access control dao
     */
    public final AccessControlDao getAccessControlDao() {
        return accessControlDao;
    }

    /**
     * Sets the access control dao.
     * 
     * @param pAccessControlDao
     *            the new access control dao
     */
    public final void setAccessControlDao(AccessControlDao pAccessControlDao) {
        accessControlDao = pAccessControlDao;
    }

    /** The values container dao. */
    private ValuesContainerDao valuesContainerDao;

    /**
     * getValuesContainerDao.
     * 
     * @return the ValuesContainerDao
     */
    public ValuesContainerDao getValuesContainerDao() {
        return valuesContainerDao;
    }

    /**
     * Set the ValuesContainer DAO.
     * 
     * @param pValuesContainerDao
     *            the ValuesContainerDao to set
     */
    public void setValuesContainerDao(
            final ValuesContainerDao pValuesContainerDao) {
        valuesContainerDao = pValuesContainerDao;
    }

    /** The extensions container dao. */
    private ExtensionsContainerDao extensionsContainerDao;

    /**
     * getExtensionsContainerDao.
     * 
     * @return the ExtensionsContainerDao
     */
    public ExtensionsContainerDao getExtensionsContainerDao() {
        return extensionsContainerDao;
    }

    /**
     * setExtensionsContainerDao.
     * 
     * @param pExtensionsContainerDao
     *            the ExtensionsContainerDao to set
     */
    public void setExtensionsContainerDao(
            ExtensionsContainerDao pExtensionsContainerDao) {
        extensionsContainerDao = pExtensionsContainerDao;
    }

    /** The gpmDao. */
    private GpmDao gpmDao;

    /**
     * getGpmDao.
     * 
     * @return the GpmDao
     */
    public GpmDao getGpmDao() {
        return gpmDao;
    }

    /**
     * setGpmDao.
     * 
     * @param pGpmDao
     *            the GpmDao to set
     */
    public void setGpmDao(GpmDao pGpmDao) {
        gpmDao = pGpmDao;
    }

    /** The end user dao. */
    private EndUserDao endUserDao;

    /**
     * Get the EndUser DAO.
     * 
     * @return EndUser DAO
     */
    protected final EndUserDao getEndUserDao() {
        return endUserDao;
    }

    /**
     * Set the EndUser DAO.
     * 
     * @param pEndUserDao
     *            EndUser DAO
     */
    public final void setEndUserDao(final EndUserDao pEndUserDao) {
        endUserDao = pEndUserDao;
    }

    /** The link type dao. */
    private LinkTypeDao linkTypeDao;

    /**
     * getLinkTypeDao.
     * 
     * @return the LinkTypeDao
     */
    public final LinkTypeDao getLinkTypeDao() {
        return linkTypeDao;
    }

    /**
     * setLinkTypeDao.
     * 
     * @param pLinkTypeDao
     *            the LinkTypeDao to set
     */
    public final void setLinkTypeDao(final LinkTypeDao pLinkTypeDao) {
        linkTypeDao = pLinkTypeDao;
    }

    /** The node dao. */
    private NodeDao nodeDao;

    /**
     * Get the Node DAO.
     * 
     * @return the nodeDao
     */
    public final NodeDao getNodeDao() {
        return nodeDao;
    }

    /**
     * Set the Node DAO.
     * 
     * @param pNodeDao
     *            the nodeDao to set
     */
    public final void setNodeDao(final NodeDao pNodeDao) {
        nodeDao = pNodeDao;
    }

    protected CacheableObjectFactoriesMgr cacheableObjectFactories;

    public final void setCacheableObjectFactories(
            CacheableObjectFactoriesMgr pCacheableFactoriesMgr) {
        cacheableObjectFactories = pCacheableFactoriesMgr;
    }

    /** The gpm global cache. */
    protected Cache gpmGlobalCache;

    public final void setGpmGlobalCache(Cache pGpmGlobalCache) {
        gpmGlobalCache = pGpmGlobalCache;
    }

    /**
     * Get a sheet type from a sheet.
     * 
     * @param pSheet
     *            The sheet.
     * @return The sheet type
     */
    protected final SheetType getSheetType(final Sheet pSheet) {
        String lSheetTypeId = pSheet.getSheetTypeId();
        return getSheetType(lSheetTypeId);
    }

    protected ApplicationContext mainContext;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext pMainContext)
        throws BeansException {
        mainContext = pMainContext;
    }

    protected FieldsContainerServiceImpl fieldsContainerServiceImpl;

    public FieldsContainerServiceImpl getFieldsContainerServiceImpl() {
        return fieldsContainerServiceImpl;
    }

    public void setFieldsContainerServiceImpl(
            FieldsContainerServiceImpl pFieldsContainerServiceImpl) {
        fieldsContainerServiceImpl = pFieldsContainerServiceImpl;
    }

    /** Data Transformation service implementation */
    protected DataTransformationServiceImpl dataTransformationServiceImpl;

    /**
     * Getter on the 'data transformation' service implementation
     * 
     * @return Data Transformation service implementation
     */
    public DataTransformationServiceImpl getDataTransformationServiceImpl() {
        return dataTransformationServiceImpl;
    }

    /**
     * Setter on the 'data transformation' service implementation
     * 
     * @param pDataTransformationServiceImpl
     *            The new Data Transformation service implementation
     */
    public void setDataTransformationServiceImpl(
            DataTransformationServiceImpl pDataTransformationServiceImpl) {
        dataTransformationServiceImpl = pDataTransformationServiceImpl;
    }

    protected FilterAccessManager filterAccessManager;

    protected FilterDataManager filterDataManager;

    /**
     * Remove all elements for filter's caches.
     */
    protected final void clearFilterCaches() {
        searchService.clearUsableFieldManager();
        filterDataManager.depreciateAll();
    }

    /**
     * Remove the cached elements associated to a specific filter.
     * 
     * @param pFilterId
     *            The specific filter id.
     */
    public void clearFilterCaches(final String pFilterId) {
        filterDataManager.depreciateElement(new FilterDataKey(pFilterId));
    }

    /**
     * Setter on manager used by Spring injection.
     * 
     * @param pFilterAccessManager
     *            The manager.
     */
    public void setFilterAccessManager(
            final FilterAccessManager pFilterAccessManager) {
        filterAccessManager = pFilterAccessManager;
    }

    /**
     * Setter on manager used by Spring injection.
     * 
     * @param pFilterDataManager
     *            The manager.
     */
    public void setFilterDataManager(final FilterDataManager pFilterDataManager) {
        filterDataManager = pFilterDataManager;
    }

    protected AtomicActionsManager atomicActionsManager;

    /**
     * Setter on manager used by Spring injection.
     * 
     * @param pAtomicActionsManager
     *            The manager.
     */
    public void setAtomicActionsManager(
            final AtomicActionsManager pAtomicActionsManager) {
        atomicActionsManager = pAtomicActionsManager;
    }

    /** ProcessDefinition DAO. */
    protected org.topcased.gpm.domain.process.ProcessDefinitionDao processDefinitionDao;

    /**
     * Sets the reference to <code>processDefinition </code>'s DAO.
     * 
     * @param pProcessDefinitionDao
     *            Data Access Object
     */
    final public void setProcessDefinitionDao(
            final ProcessDefinitionDao pProcessDefinitionDao) {
        processDefinitionDao = pProcessDefinitionDao;
    }

    /**
     * Gets the reference to <code>processDefinition</code>'s DAO.
     * 
     * @return Data Access Object
     */
    final protected ProcessDefinitionDao getProcessDefinitionDao() {
        return processDefinitionDao;
    }

    /** Transition DAO. */
    protected org.topcased.gpm.domain.process.TransitionDao transitionDao;

    /**
     * Sets the reference to <code>Transition </code>'s DAO.
     * 
     * @param pTransitionDao
     *            Data Access Object
     */
    final public void setTransitionDao(final TransitionDao pTransitionDao) {
        transitionDao = pTransitionDao;
    }

    /**
     * Gets the reference to <code>TransitionDao</code>'s DAO.
     * 
     * @return Data Access Object
     */
    final protected TransitionDao getTransitionDao() {
        return transitionDao;
    }
}

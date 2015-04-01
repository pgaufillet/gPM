/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.ProductLink;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.link.LinkDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.product.ProductDao;

/**
 * Manager used to export product links.
 * 
 * @author tpanuel
 */
public class ProductLinkExportManager extends
        AbstractValuesContainerExportManager<ProductLink> {
    private LinkDao linkDao;

    private ProductDao productDao;

    /**
     * Create a product link export manager.
     */
    public ProductLinkExportManager() {
        super("productLinks");
    }

    /**
     * Setter for spring injection.
     * 
     * @param pLinkDao
     *            The DAO.
     */
    public void setLinkDao(final LinkDao pLinkDao) {
        linkDao = pLinkDao;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pProductDao
     *            The DAO.
     */
    public void setProductDao(final ProductDao pProductDao) {
        productDao = pProductDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#getAllElementsId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    protected Iterator<String> getAllElementsId(final String pRoleToken,
            final Collection<String> pProductsName,
            final Collection<String> pTypesName) {
        return linkDao.productLinksIterator(
                authorizationServiceImpl.getProcessName(pRoleToken),
                pProductsName, pTypesName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return linkDao.isProductLink(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getProductLinksFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#marshal(org.topcased.gpm.business.fields.impl.CacheableValuesContainer,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ProductLink marshal(final CacheableValuesContainer pCacheable,
            final ExportProperties pExportProperties) {
        final ProductLink lProductLink = new ProductLink();

        pCacheable.marshal(lProductLink);

        // If no export UID
        if (!pExportProperties.isExportUID()) {
            final Product lProductOrigin =
                    productDao.load(lProductLink.getOriginId());
            final Product lProductDestination =
                    productDao.load(lProductLink.getDestinationId());

            // Replace product id by product name
            lProductLink.setOriginId(null);
            lProductLink.setDestinationId(null);
            lProductLink.setOriginProductName(lProductOrigin.getName());
            lProductLink.setDestinationProductName(lProductDestination.getName());

            // Evict for limit hibernate session size
            productDao.evict(lProductOrigin);
            productDao.evict(lProductDestination);
        }

        return lProductLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    @Override
    protected List<String> getProductNames(final String pElementId) {
        final Link lLink = linkDao.load(pElementId);
        final List<String> lProductNames = new ArrayList<String>();

        lProductNames.add(((Product) lLink.getOrigin()).getName());
        lProductNames.add(((Product) lLink.getDestination()).getName());

        return lProductNames;
    }
}
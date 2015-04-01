/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.AbstractLinkImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.serialization.data.ProductLink;

/**
 * ProductLinkImportManager handle the importation of product's links.
 * 
 * @author mkargbo
 */
public class ProductLinkImportManager extends
        AbstractLinkImportManager<ProductLink> {

    /** LINKED_NAME_NOT_EXISTS */
    private static final String LINKED_NAME_NOT_EXISTS =
            "Origin or destination (name) does not exist for this link.";

    /** LINKED_NAME_NOT_EXISTS */
    private static final String LINKED_NAME_BLANK =
            "Origin or destination (name) is blank.";

    private ProductServiceImpl productServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getProductLinksFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ImportException
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#canImportWithoutIdentifiers(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link,
     *      org.topcased.gpm.business.link.impl.CacheableLinkType,
     *      ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImportWithoutIdentifiers(String pRoleToken,
            ProductLink pElement, CacheableLinkType pLinkType,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        //Looking with product name
        final boolean lLinkedExists;
        if (StringUtils.isBlank(pElement.getOriginProductName())
                || StringUtils.isBlank(pElement.getDestinationProductName())) {
            onFailure(pElement, pProperties, pReport, LINKED_NAME_BLANK);
            lLinkedExists = false;
        }
        else if ((!productServiceImpl.isProductExists(pRoleToken,
                pElement.getOriginProductName()))
                || (!productServiceImpl.isProductExists(pRoleToken,
                        pElement.getDestinationProductName()))) {
            onFailure(pElement, pProperties, pReport, LINKED_NAME_NOT_EXISTS);
            lLinkedExists = false;
        }
        else {
            lLinkedExists = true;
        }
        return lLinkedExists;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#getLinkIdentifier(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link)
     */
    @Override
    protected String getLinkIdentifier(String pRoleToken, ProductLink pLink) {
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        return linkServiceImpl.getProductLinkId(lProcessName,
                pLink.getOriginProductName(),
                pLink.getDestinationProductName(), pLink.getType());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#getLinkedIdentifier(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link, boolean)
     */
    @Override
    protected String getLinkedIdentifier(String pRoleToken,
            ProductLink pElement, boolean pOrigin) {
        final String lProductName;
        if (pOrigin) {
            lProductName = pElement.getOriginProductName();
        }
        else {
            lProductName = pElement.getDestinationProductName();
        }
        return productServiceImpl.getProductId(pRoleToken, lProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#isLinkExists(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link)
     */
    @Override
    protected boolean isLinkExists(String pRoleToken, ProductLink pLink) {
        return linkServiceImpl.isProductLinkExists(pLink.getType(),
                pLink.getOriginProductName(), pLink.getDestinationProductName());
    }

    public void setProductServiceImpl(ProductServiceImpl pProductServiceImpl) {
        productServiceImpl = pProductServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(ProductLink pElement) {
        final String lIdentifier;
        if (StringUtils.isBlank(pElement.getId())) {
            StringBuilder lBuilder = new StringBuilder();
            lBuilder.append("Origin: ");
            if (StringUtils.isBlank(pElement.getOriginId())) {
                lBuilder.append(pElement.getOriginProductName());
            }
            else {
                lBuilder.append(pElement.getOriginId());
            }
            lBuilder.append("Destination: ");
            if (StringUtils.isBlank(pElement.getDestinationId())) {
                lBuilder.append(pElement.getDestinationProductName());
            }
            else {
                lBuilder.append(pElement.getDestinationId());
            }
            lIdentifier = lBuilder.toString();
        }
        else {
            lIdentifier = pElement.getId();
        }
        return lIdentifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.PRODUCT_LINK;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final ProductLink pElement) {
        final List<String> lProductNames = new ArrayList<String>();

        lProductNames.add(pElement.getOriginProductName());
        lProductNames.add(pElement.getDestinationProductName());

        return lProductNames;
    }
}

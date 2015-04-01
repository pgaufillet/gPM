/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.sheet;

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
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;

/**
 * SheetLinkImportManager handles sheet's link importation
 * 
 * @author mkargbo
 */
public class SheetLinkImportManager extends
        AbstractLinkImportManager<SheetLink> {

    /** LINKED_NOT_EXISTS */
    private static final String LINKED_NOT_EXISTS =
            "Origin or destination (reference) does not exist for this link.";

    /** LINKED_BLANK */
    private static final String LINKED_BLANK =
            "Origin or destination (reference) is blank.";

    private SheetServiceImpl sheetServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getSheetLinksFlag();
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
            SheetLink pElement, CacheableLinkType pLinkType,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        final boolean lLinkedExists;
        //Looking with reference
        if (StringUtils.isBlank(pElement.getOriginReference())
                || StringUtils.isBlank(pElement.getDestinationReference())) {
            onFailure(pElement, pProperties, pReport, LINKED_BLANK);
            lLinkedExists = false;
        }
        else if ((!sheetServiceImpl.isReferenceExists(
                pLinkType.getOriginTypeName(), pElement.getOriginProductName(),
                pElement.getOriginReference()))
                || (!sheetServiceImpl.isReferenceExists(
                        pLinkType.getDestinationTypeName(),
                        pElement.getDestinationProductName(),
                        pElement.getDestinationReference()))) {
            onFailure(pElement, pProperties, pReport, LINKED_NOT_EXISTS);
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
    protected String getLinkIdentifier(String pRoleToken, SheetLink pLink) {
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        return linkServiceImpl.getSheetLinkId(lProcessName,
                pLink.getOriginProductName(), pLink.getOriginReference(),
                pLink.getDestinationProductName(),
                pLink.getDestinationReference(), pLink.getType());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#getLinkedIdentifier(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link, boolean)
     */
    @Override
    protected String getLinkedIdentifier(String pRoleToken, SheetLink pElement,
            boolean pOrigin) {
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        final String lLinkedProductName;
        final String lLinkedReference;
        if (pOrigin) {
            lLinkedProductName = pElement.getOriginProductName();
            lLinkedReference = pElement.getOriginReference();
        }
        else {
            lLinkedProductName = pElement.getDestinationProductName();
            lLinkedReference = pElement.getDestinationReference();
        }
        return sheetServiceImpl.getSheetIdByReference(lProcessName,
                lLinkedProductName, lLinkedReference);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractLinkImportManager#isLinkExists(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link)
     */
    @Override
    protected boolean isLinkExists(String pRoleToken, SheetLink pLink) {
        return linkServiceImpl.isSheetLinkExists(pLink.getType(),
                pLink.getOriginReference(), pLink.getDestinationReference());
    }

    public void setSheetServiceImpl(SheetServiceImpl pSheetServiceImpl) {
        sheetServiceImpl = pSheetServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(SheetLink pElement) {
        final String lIdentifier;
        if (StringUtils.isBlank(pElement.getId())) {
            StringBuilder lBuilder = new StringBuilder();
            lBuilder.append("Origin: ");
            if (StringUtils.isBlank(pElement.getOriginId())) {
                lBuilder.append(pElement.getOriginReference()).append(" (").append(
                        pElement.getOriginProductName()).append(")");
            }
            else {
                lBuilder.append(pElement.getOriginId());
            }
            lBuilder.append("Destination: ");
            if (StringUtils.isBlank(pElement.getDestinationId())) {
                lBuilder.append(pElement.getDestinationReference()).append(" (").append(
                        pElement.getDestinationProductName()).append(")");
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
        return ElementType.SHEET_LINK;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final SheetLink pElement) {
        final List<String> lProductNames = new ArrayList<String>();

        lProductNames.add(pElement.getOriginProductName());
        lProductNames.add(pElement.getDestinationProductName());

        return lProductNames;
    }
}
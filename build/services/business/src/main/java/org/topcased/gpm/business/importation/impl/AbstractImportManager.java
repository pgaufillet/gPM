/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.authorization.impl.RoleSelector;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.report.FailureImportTermination;
import org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler;
import org.topcased.gpm.business.importation.impl.report.ImportTermination;
import org.topcased.gpm.business.importation.impl.report.Termination;

/**
 * AbstractImportManager handle general algorithms for elements import.
 * 
 * @param <S>
 *            Serialization object
 * @param <B>
 *            Business object (use in Service for creating, updating and
 *            removing)
 * @author mkargbo
 */
public abstract class AbstractImportManager<S, B> implements
        IImportTerminationHandler<S> {

//    private static final Logger LOGGER =
//            Logger.getLogger(AbstractImportManager.class);

    protected AuthorizationServiceImpl authorizationServiceImpl;

    public void setAuthorizationServiceImpl(
            final AuthorizationServiceImpl pAuthorizationServiceImpl) {
        authorizationServiceImpl = pAuthorizationServiceImpl;
    }

    /**
     * Import the element according to the property flag.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element to import
     * @param pProperties
     *            Import properties
     * @param pContext
     *            Context for importation
     * @return an {@link ImportExecutionReport}
     * @throws ImportException
     *             For any importation errors.
     */
    public ImportExecutionReport importData(final String pRoleToken,
            final S pElement, final ImportProperties pProperties,
            final Context pContext) throws ImportException {
        final ImportExecutionReport lReport = new ImportExecutionReport();
        final RoleSelector lRoleSelector = new RoleSelector(pRoleToken);

        try {
            importData(lRoleSelector, pElement, pProperties, lReport, pContext);
        }
        finally {
            lRoleSelector.close();
        }

        return lReport;
    }

    /**
     * Import the element according to the property flag.
     * <p>
     * <ul>
     * <li>SKIP: Skip the element. Add skip message in the report.</li>
     * <li>ERROR: Raise an exception or fill the report for this element.</li>
     * </ul>
     * <p>
     * Before importation, the context can be filled with specific elements.
     * </p>
     * 
     * @param pRoleSelector
     *            Use to select role in case of import of several products
     * @param pElement
     *            Element to import
     * @param pProperties
     *            Import properties
     * @param pReport
     *            Import report to fill
     * @param pContext
     *            Context for importation
     * @throws ImportException
     *             For any importation errors.
     * @throws AuthorizationException
     *             No role for the product.
     */
    public void importData(final RoleSelector pRoleSelector, final S pElement,
            final ImportProperties pProperties,
            final ImportExecutionReport pReport, final Context pContext)
        throws ImportException, AuthorizationException {
        switch (getImportFlag(pProperties)) {
            case SKIP:
//                if (LOGGER.isInfoEnabled()) {
//                    LOGGER.info(pElement.getClass().getCanonicalName()
//                            + " has been skipped");
//                }
                return;
            case ERROR:
                throw new ImportException(ImportMessage.DO_NOT_IMPORT_TYPE,
                        pElement);
            default:
                final String lSelectedRoleToken;

                // Select a role for the product of the current element
                if (pRoleSelector.isProcessRole()
                        || skipRoleSelection(pRoleSelector.getRoleToken(),
                                pElement)) {
                    lSelectedRoleToken = pRoleSelector.getRoleToken();
                }
                else {
                    lSelectedRoleToken =
                            pRoleSelector.selectRoleToken(getProductNames(pElement));

                }

                if (lSelectedRoleToken == null) {
                    onFailure(pElement, pProperties, pReport,
                            "Cannot import without "
                                    + "having role on those products: "
                                    + getProductNames(pElement));
                }
                else {
                    final String lIdentifier =
                            isElementExists(lSelectedRoleToken, pElement,
                                    pProperties, pReport);

                    final B lBusinessObject =
                            getBusinessObject(lSelectedRoleToken, pElement,
                                    pProperties);

                    fillContext(pElement, pContext);
                    final String[] lAdditionalArguments =
                            getAdditionalElements(pElement);
                    final String lElementId =
                            doImportData(lSelectedRoleToken, lBusinessObject,
                                    lIdentifier, pProperties, pContext,
                                    lAdditionalArguments);
                    pReport.getSheetIdList().add(lElementId);
                    doAdditionalImport(lSelectedRoleToken, pElement,
                            lElementId, pContext);

                    if (canImport(lSelectedRoleToken, pElement, lIdentifier,
                            pProperties, pReport)) {
                        //Import successful
                        onSuccess(lElementId, pReport);
                    }
                    else {
                        onFailure(
                                pElement,
                                pProperties,
                                pReport,
                                ImportException.ImportMessage.CANNOT_IMPORT_ELEMENT.getValue());
                    }
                }
        }
    }

    /**
     * Import algorithm (creating, updating and removing).
     * 
     * @param pRoleToken
     *            Role token
     * @param pBusinessElement
     *            Business element
     * @param pElementId
     *            Identifier of the element if exists, null otherwise.
     * @param pProperties
     *            Import properties
     * @param pContext
     *            Context for importation
     * @param pAdditionalElements
     *            Additional useful element needed for the importation
     * @return Identifier of the imported element (can be same as elementId
     *         parameter).
     * @throws ImportException
     *             For any importation errors.
     */
    protected String doImportData(final String pRoleToken,
            final B pBusinessElement, final String pElementId,
            final ImportProperties pProperties, final Context pContext,
            final String... pAdditionalElements) throws ImportException {
        String lElementId = pElementId;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                lElementId =
                        createElement(pRoleToken, pBusinessElement, pContext,
                                pAdditionalElements);
                break;
            case UPDATE_ONLY:
                updateElement(pRoleToken, pBusinessElement, pElementId,
                        pContext, pProperties.isIgnoreVersion(),
                        pAdditionalElements);
                break;
            case ERASE:
                if (StringUtils.isNotBlank(pElementId)) {
                    removeElement(pRoleToken, pElementId, pContext,
                            pAdditionalElements);
                }
                lElementId =
                        createElement(pRoleToken, pBusinessElement, pContext,
                                pAdditionalElements);
                break;
            case CREATE_OR_UPDATE:
                if (StringUtils.isNotBlank(pElementId)) {
                    updateElement(pRoleToken, pBusinessElement, pElementId,
                            pContext, pProperties.isIgnoreVersion(),
                            pAdditionalElements);
                }
                else {
                    lElementId =
                            createErasedElement(pRoleToken, pBusinessElement,
                                    pContext, pAdditionalElements);
                }
                break;
            default:
                //Do nothing (should never happened)
        }
        return lElementId;
    }

    /**
     * Get import flag.
     * <p>
     * This import flag would be use by this manager.
     * 
     * @param pProperties
     *            Import properties containing the flag.
     * @return Flag for this manager.
     */
    protected abstract ImportFlag getImportFlag(
            final ImportProperties pProperties);

    /**
     * Determine if the element can be import.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element to test
     * @param pElementId
     *            Identifier of the element
     * @param pProperties
     *            Import properties
     * @param pReport
     *            Report to fill with errors
     * @return True if the element can be import, false otherwise
     * @throws ImportException
     *             On import failure (cannot import)
     */
    protected abstract boolean canImport(final String pRoleToken,
            final S pElement, final String pElementId,
            final ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException;

    /**
     * Retrieve the element technical identifier.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element to search
     * @param pProperties
     *            Import properties
     * @param pReport
     *            TODO
     * @return Identifier of the element if exists, EMPTY string otherwise.
     * @throws ImportException
     *             For any importation errors.
     */
    protected abstract String isElementExists(final String pRoleToken,
            final S pElement, final ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException;

    /**
     * Create the business object from a serialize object.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Serialize object to transform.
     * @param pProperties
     *            The import properties.
     * @return Business object corresponding to the element.
     */
    protected abstract B getBusinessObject(final String pRoleToken,
            final S pElement, final ImportProperties pProperties);

    /**
     * Create the business element using additional argument.
     * 
     * @param pRoleToken
     *            Role token
     * @param pBusinessElement
     *            Element to create
     * @param pContext
     *            Context for creation
     * @param pAdditionalArguments
     *            Additional arguments used for creation.
     * @return Identifier of the created element.
     */
    protected abstract String createElement(final String pRoleToken,
            final B pBusinessElement, final Context pContext,
            final String... pAdditionalArguments);

    /**
     * Create the business element for 'ERASE' flag.
     * 
     * @param pRoleToken
     *            Role token.
     * @param pBusinessElement
     *            Element to create
     * @param pContext
     *            Context for creation
     * @param pAdditionalArguments
     *            TODO
     * @return Identifier of the created element.
     */
    protected String createErasedElement(final String pRoleToken,
            final B pBusinessElement, final Context pContext,
            final String... pAdditionalArguments) {
        return createElement(pRoleToken, pBusinessElement, pContext,
                pAdditionalArguments);
    }

    /**
     * Update the business element.
     * 
     * @param pRoleToken
     *            Role token
     * @param pBusinessElement
     *            Element to update
     * @param pElementId
     *            Identifier of the element to update (exists)
     * @param pIgnoreVersion
     *            <tt>true</tt> if version must be ignored, <tt>false</tt>
     *            otherwise
     * @param pContext
     *            Context for updating.
     * @param pAdditionalArguments
     *            TODO
     */
    protected abstract void updateElement(final String pRoleToken,
            final B pBusinessElement, final String pElementId,
            final Context pContext, boolean pIgnoreVersion,
            String... pAdditionalArguments);

    /**
     * Remove the business element.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElementId
     *            Identifier of the element to remove.
     * @param pContext
     *            Context for removing.
     * @param pAdditionalArguments
     *            TODO
     */
    protected abstract void removeElement(final String pRoleToken,
            final String pElementId, final Context pContext,
            String... pAdditionalArguments);

    /**
     * Algorithm to import additional elements linked to the element.
     * <p>
     * Default to nothing.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Based element
     * @param pElementId
     *            Identifier of the element.
     * @param pContext
     *            Context for importation.
     */
    protected void doAdditionalImport(final String pRoleToken,
            final S pElement, final String pElementId, final Context pContext) {
        //Do nothing
    }

    /**
     * Get the error message when the element cannot be imported.
     * 
     * @return Error message.
     */
    protected String getCanImportErrorMessage() {
        return ImportException.ImportMessage.CANNOT_IMPORT_ELEMENT.getValue();
    }

    /**
     * Fill the context for the element importation.
     * <p>
     * Default do nothing.
     * </p>
     * 
     * @param pElement
     *            Element to import.
     * @param pContext
     *            Context to fill.
     */
    protected void fillContext(final S pElement, final Context pContext) {
        //Do nothing
    }

    /**
     * Getting additional useful elements for importation.
     * 
     * @param pElement
     *            Element to import.
     * @return Additional elements for importation.
     */
    protected String[] getAdditionalElements(final S pElement) {
        return new String[0];
    }

    /**
     * {@inheritDoc}
     * <p>
     * Raise an ImportException if the property is defined, fill the report
     * otherwise.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#onFailure(java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport,
     *      java.lang.String)
     */
    public void onFailure(final S pElement, final ImportProperties pProperties,
            final ImportExecutionReport pReport, final String pMessage)
        throws ImportException {
        if (pProperties.isExceptionRaising()) {
            throw new ImportException(pMessage, pElement);
        }
        else {
            pReport.add(getElementIdentifier(pElement), getElementType(),
                    new FailureImportTermination(pMessage));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fill the report.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#onSuccess(java.lang.String,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    public void onSuccess(final String pElementId, final ImportExecutionReport pReport) {
        pReport.add(pElementId, getElementType(), new ImportTermination(
                Termination.SUCCESS));
    }

    /**
     * The get the product names associate to an element id.
     * 
     * @param pElement
     *            The element.
     * @return The product name.
     */
    protected abstract List<String> getProductNames(final S pElement);

    /**
     * Test if the role selection can be skipped for a specific element.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pElement
     *            The element.
     * @return If the role selection can be skipped.
     */
    protected boolean skipRoleSelection(final String pRoleToken,
            final S pElement) {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Raise an ImportException if the property is defined, fill the report
     * otherwise.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#onFailure(java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport,
     *      java.lang.String)
     */
    public void onFailure(final S pElement, final ImportProperties pProperties,
            final ImportExecutionReport pReport, final String pMessage,
            final String pLabelKey) throws ImportException {
        if (pProperties.isExceptionRaising()) {
            throw new ImportException(pMessage, pElement);
        }
        else {
            pReport.add(getElementIdentifier(pElement), getElementType(),
                    new FailureImportTermination(pMessage, pLabelKey));
        }
    }

}

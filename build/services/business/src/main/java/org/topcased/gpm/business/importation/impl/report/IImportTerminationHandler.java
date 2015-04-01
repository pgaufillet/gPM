/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.report;

import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;

/**
 * Interface that provide the import termination for the importation report.
 * 
 * @param <S>
 *            Serialization object to import.
 * @author mkargbo
 */
public interface IImportTerminationHandler<S> {

    /**
     * Get the element's identifier.
     * 
     * @param pElement
     *            Element to import
     * @return Technical or functional element.
     */
    public String getElementIdentifier(final S pElement);

    /**
     * Get the element's type
     * 
     * @return Type of the element to import.
     */
    public ElementType getElementType();

    /**
     * Algorithm to execute when the importation fails.
     * 
     * @param pElement
     *            Element to import.
     * @param pProperties
     *            Import properties.
     * @param pReport
     *            Import report.
     * @param pMessage
     *            Failure error's message.
     * @throws ImportException
     *             Import exception.
     */
    public void onFailure(final S pElement, final ImportProperties pProperties,
            final ImportExecutionReport pReport, final String pMessage)
        throws ImportException;

    /**
     * Algorithm to execute when the importation fails.
     * 
     * @param pElement
     *            Element to import.
     * @param pProperties
     *            Import properties.
     * @param pReport
     *            Import report.
     * @param pMessage
     *            Failure error's message.
     * @param pLabelField
     *            The Label field in error.
     * @throws ImportException
     *             Import exception.
     */
    public void onFailure(final S pElement, final ImportProperties pProperties,
            final ImportExecutionReport pReport, final String pMessage,
            String pLabelField) throws ImportException;

    /**
     * Algorithm to execute when the importation succeed.
     * 
     * @param pElementId
     *            Identifier of the imported element.
     * @param pReport
     *            Import report.
     */
    public void onSuccess(final String pElementId,
            final ImportExecutionReport pReport);
}

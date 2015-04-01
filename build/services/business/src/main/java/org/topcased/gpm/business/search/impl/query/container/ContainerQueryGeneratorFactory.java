/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.container;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.fields.FieldsContainerType;

/**
 * ContainerQueryGeneratorFactory can create a generator instance for container.
 * 
 * @author mkargbo
 */
public class ContainerQueryGeneratorFactory {

    private static final ContainerQueryGeneratorFactory INSTANCE =
            new ContainerQueryGeneratorFactory();

    /**
     * Get the ContainerQueryGeneratorFactory unique instance
     * 
     * @return ContainerQueryGeneratorFactory instance
     */
    public static final ContainerQueryGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get the generator for container according to this container type.
     * 
     * @param pFieldsContainerId
     *            Identifier of the fields container of the container.
     * @param pFieldsContainerType
     *            Type of the fields container of the container.
     * @param pAdditionalConstraints
     *            The additional constraints.
     * @return The container generator.
     * @throws NotImplementedException
     *             If the fields container type is not supported.
     */
    public IContainerQueryGenerator getContainerQueryGenerator(
            final String pFieldsContainerId,
            final FieldsContainerType pFieldsContainerType,
            final FilterAdditionalConstraints pAdditionalConstraints)
        throws NotImplementedException {
        switch (pFieldsContainerType) {
            case SHEET:
                return new SheetQueryGenerator(pFieldsContainerId,
                        pAdditionalConstraints);
            case LINK:
                return new LinkQueryGenerator(pFieldsContainerId,
                        pAdditionalConstraints);
            case PRODUCT:
                return new ProductQueryGenerator(pFieldsContainerId,
                        pAdditionalConstraints);
            default:
                throw new NotImplementedException("Type '"
                        + pFieldsContainerType + "' is not implemented.");
        }
    }
}
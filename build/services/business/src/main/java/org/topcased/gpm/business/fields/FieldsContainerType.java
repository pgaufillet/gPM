/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;

/**
 * FieldsContainerType.
 * <p>
 * Available types of fields container.
 * 
 * @author mkargbo
 */
public enum FieldsContainerType {

    PRODUCT("PRODUCT"), SHEET("SHEET"), LINK("LINK");

    /** The type's name */
    private String typeName;

    private FieldsContainerType(String pTypeName) {
        typeName = pTypeName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    public final String toString() {
        return typeName;
    }

    /**
     * Gets the FieldsContainerType according to a cacheable fields container.
     * 
     * @param pCacheableFieldsContainer
     *            CacheableFieldsContainer
     * @return The FieldsContainerType.
     * @throws RuntimeException
     *             If the class is not a {@link CacheableProductType}, a
     *             {@link CacheableSheetType} or a {@link CacheableLinkType}
     */
    public static FieldsContainerType valueOf(
            CacheableFieldsContainer pCacheableFieldsContainer) {
        if (CacheableProductType.class.isInstance(pCacheableFieldsContainer)) {
            return PRODUCT;
        }
        else if (CacheableSheetType.class.isInstance(pCacheableFieldsContainer)) {
            return SHEET;
        }
        else if (CacheableLinkType.class.isInstance(pCacheableFieldsContainer)) {
            return LINK;
        }
        throw new RuntimeException("Invalid class "
                + pCacheableFieldsContainer.getClass().getName());
    }
}

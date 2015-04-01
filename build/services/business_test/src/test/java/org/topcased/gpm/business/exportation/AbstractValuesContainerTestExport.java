/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import java.util.List;

import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;

/**
 * Abstract class for test export on a specific type.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of exported elements.
 */
public abstract class AbstractValuesContainerTestExport<T extends ValuesContainerData>
        extends AbstractTestExport<T> {
    /**
     * Create AbstractValuesContainerTestExport.
     * 
     * @param pTagName
     *            The name of the tag.
     * @param pSerializableClass
     *            The class of the objects.
     */
    public AbstractValuesContainerTestExport(final String pTagName,
            final Class<T> pSerializableClass) {
        super(pTagName, pSerializableClass);
    }

    /**
     * Test the export of all elements of the type using admin role.
     */
    public void testNoExportUID() {
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.ALL);
        lProperties.setExportUID(false);

        checkExport(adminRoleToken, lProperties, getExpectedIdsForAll());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final T pObject, final ExportProperties pProperties) {
        if (pProperties.isExportUID()) {
            return pObject.getId();
        }
        else {
            // Check that UID are not exported
            assertNull("Id has been exported.", pObject.getId());
            checkUidNotExportedOnFields(pObject.getFieldValues());

            return getId(pObject);
        }
    }

    private void checkUidNotExportedOnFields(final List<FieldValueData> pValues) {
        if (pValues != null) {
            for (FieldValueData lValue : pValues) {
                if (lValue != null) {
                    // Check sub fields
                    checkUidNotExportedOnFields(lValue.getFieldValues());
                    // Check pointer fields
                    if (lValue instanceof PointerFieldValueData) {
                        assertNull(
                                "Referenced container id has been exported.",
                                ((PointerFieldValueData) lValue).getReferencedContainerId());
                    }
                    // Check attached fields
                    else if (lValue instanceof AttachedFieldValueData) {
                        assertNull("Attached field id has been exported.",
                                ((AttachedFieldValueData) lValue).getId());
                    }
                }
            }
        }
    }

    /**
     * Get the id of an object using its functional information.
     * 
     * @param pObject
     *            The object.
     * @return The id.
     */
    protected abstract String getId(final T pObject);
}
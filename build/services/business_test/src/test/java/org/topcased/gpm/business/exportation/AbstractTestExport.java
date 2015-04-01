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

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.converter.XMLConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * Abstract class for test export on a specific type.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of exported elements.
 */
public abstract class AbstractTestExport<T extends Serializable> extends AbstractBusinessServiceTestCase {
    protected final static Set<String> productNamesWithRoleOn =
            new HashSet<String>();

    static {
        productNamesWithRoleOn.add(GpmTestValues.PRODUCT1_NAME);
        productNamesWithRoleOn.add(GpmTestValues.PRODUCT_PRODUCT2);
    }

    private static final String VERSION_TAG = "version";

    private static final String DICTIONARY_TAG = "dictionary";

    private static final String EXPORT_FILE =
            System.getProperty("java.io.tmpdir")
                    + System.getProperty("file.separator")
                    + "tmpExportFile.xml";

    private final String tagName;

    private final Class<T> serializableClass;

    /**
     * Create AbstractTestExport.
     * 
     * @param pTagName
     *            The name of the tag.
     * @param pSerializableClass
     *            The class of the objects.
     */
    public AbstractTestExport(final String pTagName,
            final Class<T> pSerializableClass) {
        tagName = pTagName;
        serializableClass = pSerializableClass;
    }

    /**
     * Test the export of all elements of the type using admin role.
     */
    public void testAllWithAdmin() {
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.ALL);

        checkExport(adminRoleToken, lProperties, getExpectedIdsForAll());
    }

    /**
     * The expected ids on an export of all the elements.
     * 
     * @return The ids.
     */
    protected abstract Set<String> getExpectedIdsForAll();

    /**
     * Test the export of elements limited by product name using admin role.
     */
    public void testLimitedByProductNameAdmin() {
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.ALL);

        // Set limited product names
        final ArrayList<String> lLimitedProductNames =
                new ArrayList<String>(getLimitedProductNames());

        lProperties.setLimitedProductsName(lLimitedProductNames.toArray(new String[lLimitedProductNames.size()]));

        checkExport(adminRoleToken, lProperties,
                getExpectedIdsForLimitedByProduct());
    }

    /**
     * The expected ids on an export of limited by products.
     * 
     * @return The ids.
     */
    protected abstract Set<String> getExpectedIdsForLimitedByProduct();

    /**
     * Get the limited type names.
     * 
     * @return The limited type names.
     */
    protected abstract Set<String> getLimitedProductNames();

    /**
     * Test the export of elements limited by type name using admin role.
     */
    public void testLimitedByTypeNameAdmin() {
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.ALL);

        // Set limited product names
        final ArrayList<String> lLimitedTypeNames =
                new ArrayList<String>(getLimitedTypeNames());

        lProperties.setLimitedTypesName(lLimitedTypeNames.toArray(new String[lLimitedTypeNames.size()]));

        checkExport(adminRoleToken, lProperties,
                getExpectedIdsForLimitedByType());
    }

    /**
     * The expected ids on an export of limited by types.
     * 
     * @return The ids.
     */
    protected abstract Set<String> getExpectedIdsForLimitedByType();

    /**
     * Get the limited type names.
     * 
     * @return The limited type names.
     */
    protected abstract Set<String> getLimitedTypeNames();

    /**
     * Test the export of all elements of the type using admin role.
     */
    public void testLimitedByIdWithAdmin() {
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.LIMITED);

        final Set<String> lExpectedResults = getExpectedIdsForLimitedById();

        if (!lExpectedResults.isEmpty()) {
            lProperties.setLimitedElementsId(new ArrayList<String>(
                    lExpectedResults).toArray(new String[lExpectedResults.size()]));
        }

        checkExport(adminRoleToken, lProperties, lExpectedResults);
    }

    /**
     * The expected ids on an export of limited the elements.
     * 
     * @return The ids.
     */
    protected Set<String> getExpectedIdsForLimitedById() {
        final Set<String> lExpectedResults = new HashSet<String>();
        boolean lExportElement = true;

        for (String lExpectedResult : getExpectedIdsForAll()) {
            if (lExportElement) {
                lExpectedResults.add(lExpectedResult);
            }
            lExportElement = !lExportElement;
        }

        return lExpectedResults;
    }

    /**
     * Test the export of elements limited by product name using admin role.
     */
    public void testAutoRoleTokenSelection() {
        final String lRoleToken =
                authorizationService.selectRole(authorizationService.login(
                        GpmTestValues.USER_VIEWER3, "pwd3"), "viewer",
                        GpmTestValues.PRODUCT1_NAME, getProcessName());
        final ExportProperties lProperties = new ExportProperties();

        // Attached files are not exported
        lProperties.setAttachedFilePath(null);
        lProperties.setAllFlags(ExportFlag.NO);
        setSpecificFlag(lProperties, ExportFlag.ALL);

        checkExport(lRoleToken, lProperties, getIdsWithRoleOn());
    }

    /**
     * Get the ids with role on.
     * 
     * @return The ids with role on.
     */
    protected abstract Set<String> getIdsWithRoleOn();

    /**
     * Realize and check an export.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProperties
     *            The export properties.
     * @param pExpectedId
     *            The expected elements.
     */
    protected void checkExport(final String pRoleToken,
            final ExportProperties pProperties, final Set<String> pExpectedIds) {
        try {
            final OutputStream lOut = new FileOutputStream(EXPORT_FILE);

            // Export
            serviceLocator.getExportService().exportData(pRoleToken, lOut,
                    pProperties);

            final InputStream lIn = new FileInputStream(EXPORT_FILE);

            final XMLConverter lConverter = new XMLConverter(lIn);
            final HierarchicalStreamReader lHierarchicalReader =
                    lConverter.createHierarchicalStreamReader();
            final Set<String> lIds = new HashSet<String>();

            // Read the id of the exported objects
            readTag(lHierarchicalReader, lConverter, lIds, pProperties);

            // Check export content
            for (String lExpectedResult : pExpectedIds) {
                if (!lIds.contains(lExpectedResult)) {
                    fail("This element has not been exported: "
                            + getElementInfo(lExpectedResult));
                }
            }
            for (String lId : lIds) {
                if (!pExpectedIds.contains(lId)) {
                    fail("This element may not been exported: "
                            + getElementInfo(lId));
                }
            }

            // Close in stream
            lIn.close();
        }
        catch (FileNotFoundException e) {
            fail("Cannot access to file.");
        }
        catch (IOException e) {
            fail("Cannot access to file.");
        }
    }

    @SuppressWarnings("unchecked")
    private void readTag(final HierarchicalStreamReader pHierarchicalReader,
            final XMLConverter pConverter, final Set<String> pIds,
            final ExportProperties pProperties) throws IOException {
        // Read the id of the exported objects
        while (pHierarchicalReader.hasMoreChildren()) {
            pHierarchicalReader.moveDown();

            final String lCurerntTagName = pHierarchicalReader.getNodeName();

            // Skip version tag
            if (!StringUtils.equals(lCurerntTagName, VERSION_TAG)) {
                // For dictionary, explore sub tag (cat & env)
                if (StringUtils.equals(lCurerntTagName, DICTIONARY_TAG)) {
                    readTag(pHierarchicalReader, pConverter, pIds, pProperties);
                }
                else {
                    // Check that only the good tag has been exported
                    assertEquals("Invalid tag is detected.", lCurerntTagName,
                            tagName);

                    // The current tag
                    final ObjectInputStream lTag =
                            pConverter.createObjectInputStream();

                    // Read the content of the current tag
                    try {
                        while (true) {
                            final Object lObject = lTag.readObject();

                            assertNotNull("Tag cannot contains null object.",
                                    lObject);
                            assertTrue(
                                    "Invalid tag content.",
                                    serializableClass.isAssignableFrom(lObject.getClass()));
                            pIds.add(getId((T) lObject, pProperties));
                        }
                    }
                    catch (EOFException e) {
                        // End of the object stream
                    }
                    catch (ClassNotFoundException e) {
                        fail("Invalid tag content.");
                    }
                }
            }
            pHierarchicalReader.moveUp();
        }
    }

    /**
     * Set the specific flag.
     * 
     * @param pProperties
     *            The export properties.
     * @param pFlag
     *            The flag.
     */
    protected abstract void setSpecificFlag(ExportProperties pProperties,
            ExportFlag pFlag);

    /**
     * Get the id of and exported element.
     * 
     * @param pObject
     *            The exported element.
     * @param pProperties
     *            The export properties.
     * @return The id.
     */
    protected abstract String getId(T pObject, ExportProperties pProperties);

    /**
     * Get element info for display an error message.
     * 
     * @param pElementId
     *            The element id.
     * @return The element info.
     */
    protected abstract String getElementInfo(String pElementId);
}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.util.resources.BasicResourcesLoader;
import org.topcased.gpm.util.resources.IResourcesLoader;

/**
 * AbstractImportTest: Basic importation tests implementation.
 * <p>
 * Elements that should be import (expected elements) have been identified by an
 * array .<br />
 * This array contains the elements (string) that identified the object to
 * import.
 * </p>
 * <blockquote> e.g.: A filter is identified by its name, process name, product
 * name and user login. Then the array should contain those 4 elements.
 * </blockquote>
 * 
 * @param <E>
 *            Business object representing the element to import.
 * @author mkargbo
 */
public abstract class AbstractImportTest<E> extends
        AbstractBusinessServiceTestCase {
    protected static final Context CONTEXT = Context.getEmptyContext();

    protected static final Map<String, String> EMPTY_MAP =
            Collections.emptyMap();

    protected ImportService importService;

    private IResourcesLoader resourcesLoader;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        importService = serviceLocator.getImportService();
        resourcesLoader = new BasicResourcesLoader();
    }

    /**
     * Set the import flag to the properties.
     * 
     * @param pProperties
     *            Import properties.
     * @param pFlag
     *            Flag to set.
     */
    protected abstract void setImportFlag(final ImportProperties pProperties,
            final ImportProperties.ImportFlag pFlag);

    /**
     * Get the expected elements that should be import.
     * 
     * @return List of array containing identifiers of an element.
     */
    protected abstract Set<String[]> getImportedElement();

    /**
     * File to import.
     * 
     * @return Path of the file to import.
     */
    protected abstract String getImportFile();

    /**
     * File to import for admin user (default same as
     * {@link AbstractImportTest#getImportFile()}
     * 
     * @return Path of the file to import for admin user
     */
    protected String getAdminImportFile() {
        return getImportFile();
    }

    /**
     * File to import for not admin user (default same as
     * {@link AbstractImportTest#getImportFile()}
     * 
     * @return Path of the file to import for not admin user
     */
    protected String getNotAdminImportFile() {
        return getImportFile();
    }

    /**
     * File to import (updating elements)
     * 
     * @return Path of the file to import (updating elements).
     */
    protected abstract String getImportFileForUpdating();

    /**
     * File to import (updating elements) for admin user (default same as
     * {@link AbstractImportTest#getImportFileForUpdating()}
     * 
     * @return Path of the file to import (updating elements) for admin user.
     */
    protected String getAdminImportFileForUpdating() {
        return getImportFileForUpdating();
    }

    /**
     * File to import (updating elements) for not admin user (default same as
     * {@link AbstractImportTest#getImportFileForUpdating()}
     * 
     * @return Path of the file to import (updating elements) for not admin
     *         user.
     */
    protected String getNotAdminImportFileForUpdating() {
        return getImportFileForUpdating();
    }

    /**
     * Remove the element from database using services.
     * 
     * @param pElementId
     *            Identifier of the element to remove.
     */
    protected abstract void removeElement(String pElementId);

    /**
     * Get the technical identifier of the element.
     * 
     * @param pElementRef
     *            Element identifiers.
     * @return Technical identifier of an element.
     */
    protected abstract String getElementId(final String... pElementRef);

    /**
     * Get the business object representing the element to import.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElementId
     *            Technical identifier of the element
     * @return Business object representation of the element.
     */
    protected abstract E getBusinessObject(final String pRoleToken,
            final String pElementId);

    /**
     * Import a file using a specified role and flag.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFlag
     *            Import flag
     * @param pFileToImport
     *            File to import.
     * @throws ImportException
     *             Errors during importation.
     */
    protected void doImport(final String pRoleToken,
            final ImportProperties.ImportFlag pFlag, final String pFileToImport)
        throws ImportException {
        try {
            ImportProperties lImportProperties = new ImportProperties();
            setImportFlag(lImportProperties, pFlag);
            importService.importData(pRoleToken,
                    resourcesLoader.getAsStream(pFileToImport),
                    lImportProperties, CONTEXT);
        }
        catch (SchemaValidationException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Delete expected imported elements.
     */
    protected void deleteElements() {
        //Delete elements
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            removeElement(lId);
        }
    }

    /**
     * Erase test pre condition.
     * 
     * @return Pre condition result.
     */
    protected abstract Object erasePreCondition();

    /**
     * Erase test assertion
     * 
     * @param pArgs
     *            Objects using for assertion.
     */
    protected abstract void eraseAssertion(Object... pArgs);

    /**
     * Test 'SKIP' import flag.
     * <p>
     * Remove elements and try to import them with 'SKIP' import's flag.<br />
     * Then, test element's identifier existence.
     * </p>
     */
    public void testSkipImport() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.SKIP, getAdminImportFile());
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }

        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            assertTrue(StringUtils.isBlank(lId));
        }
    }

    /**
     * Test 'ERROR' import flag.
     * <p>
     * Try to import elements with 'ERROR' import's flag.<br />
     * An ImportException must be raise with message
     * 'ImportMessage.DO_NOT_IMPORT_TYPE'.
     * </p>
     */
    public void testErrorImport() {
        try {
            doImport(adminRoleToken, ImportFlag.ERROR, getAdminImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            assertEquals("Bad import exception. Wrong message:'"
                    + e.getMessage() + "'",
                    ImportMessage.DO_NOT_IMPORT_TYPE.getValue(), e.getMessage());
        }
    }

    /**
     * Test 'CREATE_ONLY' import flag.
     * <p>
     * Elements to import already exists: an import exception must be raise.
     * </p>
     */
    public void testCreateOnlyExistsImport() {
        try {
            doImport(adminRoleToken, ImportFlag.CREATE_ONLY,
                    getAdminImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            assertEquals("Bad ImportException, the message '" + e.getMessage()
                    + "' is wrong", ImportMessage.OBJECT_EXISTS.getValue(),
                    e.getMessage());
        }
    }

    /**
     * Test 'CREATE_ONLY' import flag for an admin user.
     * <p>
     * Test the existence of imported elements. All elements fields must be
     * imported.
     * </p>
     */
    public void testCreateOnlyImportAdmin() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.CREATE_ONLY,
                    getAdminImportFile());
            createOnlyAdminAssertion();
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Create only test for admin user assertion
     */
    protected abstract void createOnlyAdminAssertion();

    /**
     * Test 'CREATE_ONLY' import flag for a notadmin user.
     * <p>
     * Test the existence of imported elements. Some fields (not all) must be
     * imported.
     * </p>
     */
    public void testCreateOnlyImportNotAdmin() {
        deleteElements();
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY,
                    getNotAdminImportFile());
            createOnlyNotAdminAssertion();
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Create only test for not admin user assertion
     */
    protected abstract void createOnlyNotAdminAssertion();

    /**
     * Test 'UPDATE_ONLY' import flag.
     * <p>
     * Throw an import exception when the element to import does not exist.
     */
    public void testUpdateOnlyExistsImport() {
        deleteElements();
        try {
            doImport(adminRoleToken, ImportFlag.UPDATE_ONLY,
                    getAdminImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            assertEquals("Bad ImportException, the message '" + e.getMessage()
                    + "' is wrong", ImportMessage.OBJECT_NOT_EXISTS.getValue(),
                    e.getMessage());
        }
    }

    /**
     * Test 'UPDATE_ONLY' import's flag for an 'admin' user.
     */
    public void testUpdateOnlyImportAdmin() {
        //Retrieve elements identifier.
        final Set<String> lElementsId = new HashSet<String>();
        for (String[] lRef : getImportedElement()) {
            lElementsId.add(getElementId(lRef));
        }

        try {
            doImport(adminRoleToken, ImportFlag.UPDATE_ONLY,
                    getAdminImportFileForUpdating());

            updateOnlyAdminAssertion(lElementsId);
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Update only test for admin user assertion
     * 
     * @param pElementsId
     *            List of element's identifiers.
     */
    protected abstract void updateOnlyAdminAssertion(
            Collection<String> pElementsId);

    /**
     * Test 'UPDATE_ONLY' import's flag for an 'notadmin' user.
     */
    public void testUpdateOnlyImportNotAdmin() {
        //Retrieve elements identifier.
        final Set<String> lElementsId = new HashSet<String>();
        for (String[] lRef : getImportedElement()) {
            lElementsId.add(getElementId(lRef));
        }

        try {
            doImport(normalRoleToken, ImportFlag.UPDATE_ONLY,
                    getNotAdminImportFileForUpdating());
            updateOnlyNotAdminAssertion(lElementsId);
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * Update only test for not admin user assertion
     * 
     * @param pElementsId
     *            List of element's identifiers.
     */
    protected abstract void updateOnlyNotAdminAssertion(
            Collection<String> pElementsId);

    /**
     * Test 'ERASE' import's flag.
     * <p>
     * Update elements, retrieve their identifiers and version.<br />
     * Test imported elements identifiers and version (not same as before) and
     * imported fields (existence).
     */
    public abstract void testEraseImport();
}

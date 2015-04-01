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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.authorization.impl.RoleSelector;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.SystemException;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.domain.accesscontrol.RoleDao;

/**
 * Manager used to export serializable elements. A manager can only export
 * elements of specific type.
 * 
 * @author tpanuel
 * @param <S>
 *            The type of exported object can be a collection.
 */
public abstract class AbstractExportManager<S> {
    private final String nodeName;

    protected AuthorizationServiceImpl authorizationServiceImpl;

    protected RoleDao roleDao;

    /**
     * Create an abstract export manager specifying the node name.
     * 
     * @param pNodeName
     *            The name of the node.
     */
    public AbstractExportManager(final String pNodeName) {
        nodeName = pNodeName;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pAuthorizationServiceImpl
     *            The service implementation.
     */
    public void setAuthorizationServiceImpl(
            final AuthorizationServiceImpl pAuthorizationServiceImpl) {
        authorizationServiceImpl = pAuthorizationServiceImpl;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pRoleDao
     *            The DAO.
     */
    public void setRoleDao(final RoleDao pRoleDao) {
        roleDao = pRoleDao;
    }

    /**
     * Export the data associate to a specific type.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pWriter
     *            The writer.
     * @param pExportProperties
     *            The export properties.
     * @throws SystemException
     *             If the file cannot be written.
     * @throws AuthorizationException
     *             The element cannot be exported.
     */
    public void exportData(final String pRoleToken,
            final OutputStreamWriter pWriter,
            final ExportProperties pExportProperties) throws SystemException,
        AuthorizationException {
        // Node will be create if at less one element is exported
        XMLConverter lConverter = null;
        final Iterator<String> lElementIds;
        // Element on different product can be exported
        final RoleSelector lRoleSelector = new RoleSelector(pRoleToken);

        // Get the id of all the elements that must be exported
        // Id can be invalid or linked to a wrong type
        // The wrong id will be ignore at the export.
        switch (getExportFlag(pExportProperties)) {
            case NO:
                // Skip all the element of this type
                return;
            case LIMITED:
                final List<String> lValidIds = new LinkedList<String>();

                for (String lLimitedElementsId : pExportProperties.getLimitedElementsId()) {
                    if (isValidIdentifier(lLimitedElementsId)) {
                        lValidIds.add(lLimitedElementsId);
                    }
                }
                lElementIds = lValidIds.iterator();
                break;
            case ALL:
            default:
                lElementIds = getAllElementsId(pRoleToken, pExportProperties);
        }

        try {
            // Export all elements
            while (lElementIds.hasNext()) {
                // The current element
                final String lElementId = lElementIds.next();
                final String lElementRoleToken;

                // Select a role for the product of the current element
                if (lRoleSelector.isProcessRole()
                        || skipRoleSelection(pRoleToken, lElementId)) {
                    lElementRoleToken = lRoleSelector.getRoleToken();
                }
                else {
                    lElementRoleToken =
                            lRoleSelector.selectRoleToken(getProductNames(lElementId));
                }

                if (lElementRoleToken == null) {
                    // No role for the current element product
                    // The element cannot be exported
                    switch (getExportFlag(pExportProperties)) {
                        case LIMITED:
                            // Element id is specified by the user -> exception
                            throw new AuthorizationException(lElementId,
                                    "This element cannot be exported.");
                        default:
                            // Do nothing : the element is not exported
                    }
                }
                else {
                    final S lSerializable =
                            getExportedElement(lElementRoleToken, lElementId,
                                    pExportProperties);

                    // Serializable can be null, if wrong access rights
                    if (lSerializable != null) {
                        final Collection<?> lSerializables;

                        if (lSerializable instanceof Collection) {
                            lSerializables = (Collection<?>) lSerializable;
                        }
                        else {
                            lSerializables =
                                    Collections.singleton(lSerializable);
                        }

                        if (!lSerializables.isEmpty()) {
                            if (lConverter == null) {
                                lConverter =
                                        new XMLConverter(nodeName, pWriter);
                            }
                            for (Object lObject : lSerializables) {
                                lConverter.writeObject(lObject);
                            }
                            lConverter.flush();
                        }
                    }
                }
            }
            if (lConverter != null) {
                lConverter.flush();
                // Close node
                pWriter.write("\n</" + nodeName + ">\n");
            }
        }
        catch (IOException e) {
            throw new SystemException("Error writing output stream.", e);
        }
        finally {
            // Close all temporary used to export
            lRoleSelector.close();
        }
    }

    /**
     * Get the export flag associate to the specific type.
     * 
     * @param pExportProperties
     *            The export properties.
     * @return The export flag.
     */
    abstract protected ExportFlag getExportFlag(
            ExportProperties pExportProperties);

    /**
     * Get the id of all the element of a specific type : the export properties
     * is on ALL mode.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pExportProperties
     *            The export properties.
     * @return The id of all the element of a specific type.
     */
    abstract protected Iterator<String> getAllElementsId(String pRoleToken,
            ExportProperties pExportProperties);

    /**
     * Test if the identifier is valid : if it's a right type.
     * 
     * @param pId
     *            The element identifier.
     * @return If the identifier is valid.
     */
    abstract protected boolean isValidIdentifier(String pId);

    /**
     * Get a serializable element to export from it's id.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pElementId
     *            The element id.
     * @param pExportProperties
     *            The export properties.
     * @return The serializable element.
     */
    abstract protected S getExportedElement(String pRoleToken,
            String pElementId, ExportProperties pExportProperties);

    /**
     * The get the product names associate to an element id.
     * 
     * @param pElementId
     *            The element id.
     * @return The product name.
     */
    abstract protected List<String> getProductNames(final String pElementId);

    /**
     * Test if the role selection can be skipped for a specific element.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pElementId
     *            The element id.
     * @return If the role selection can be skipped.
     */
    protected boolean skipRoleSelection(final String pRoleToken,
            final String pElementId) {
        return false;
    }
}
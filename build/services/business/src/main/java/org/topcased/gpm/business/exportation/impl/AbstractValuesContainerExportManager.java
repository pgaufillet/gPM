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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.exception.SystemException;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.impl.FieldsContainerServiceImpl;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;
import org.topcased.gpm.domain.fields.AttachedFieldValue;
import org.topcased.gpm.domain.fields.AttachedFieldValueDao;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Manager used to export values container : sheet, link or product.
 * 
 * @author tpanuel
 * @param <S>
 *            The type of exported object.
 */
public abstract class AbstractValuesContainerExportManager<S extends ValuesContainerData>
        extends AbstractExportManager<S> {
    protected FieldsContainerServiceImpl fieldsContainerServiceImpl;

    protected AttachedFieldValueDao attachedFieldValueDao;

    /**
     * Create an abstract values container manager specifying the node name.
     * 
     * @param pNodeName
     *            The name of the node.
     */
    public AbstractValuesContainerExportManager(final String pNodeName) {
        super(pNodeName);
    }

    /**
     * Setter for spring injection.
     * 
     * @param pFieldsContainerServiceImpl
     *            The service implementation.
     */
    public void setFieldsContainerServiceImpl(
            final FieldsContainerServiceImpl pFieldsContainerServiceImpl) {
        fieldsContainerServiceImpl = pFieldsContainerServiceImpl;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pAttachedFieldValueDao
     *            The DAO implementation.
     */
    public void setAttachedFieldValueDao(
            final AttachedFieldValueDao pAttachedFieldValueDao) {
        attachedFieldValueDao = pAttachedFieldValueDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getAllElementsId(java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Iterator<String> getAllElementsId(final String pRoleToken,
            final ExportProperties pExportProperties) {
        final Collection<String> lProductsName;
        final Collection<String> lTypesName;

        if (pExportProperties.getLimitedProductsName() == null
                || pExportProperties.getLimitedProductsName().length == 0) {
            lProductsName = Collections.emptySet();
        }
        else {
            lProductsName = new HashSet<String>();

            for (String lProductName : pExportProperties.getLimitedProductsName()) {
                if (authorizationServiceImpl.hasRoleOnProduct(pRoleToken,
                        lProductName)) {
                    lProductsName.add(lProductName);
                }
            }
        }

        if (pExportProperties.getLimitedTypesName() == null
                || pExportProperties.getLimitedTypesName().length == 0) {
            lTypesName = Collections.emptySet();
        }
        else {
            final AccessControlContextData lAccessControlContext =
                    new AccessControlContextData();
            final String lRoleName =
                    authorizationServiceImpl.getRoleNameFromToken(pRoleToken);

            lTypesName = new HashSet<String>();
            lAccessControlContext.setRoleName(lRoleName);

            for (String lTypeName : pExportProperties.getLimitedTypesName()) {
                final String lTypeId =
                        fieldsContainerServiceImpl.getFieldsContainerId(
                                pRoleToken, lTypeName);

                lAccessControlContext.setContainerTypeId(lTypeId);
                if (!authorizationServiceImpl.getTypeAccessControl(pRoleToken,
                        lAccessControlContext).getConfidential()
                        || authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)) {
                    lTypesName.add(lTypeName);
                }
            }
        }

        return getAllElementsId(pRoleToken, lProductsName, lTypesName);
    }

    /**
     * Get the id of all the element of a specific type.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductsName
     *            The products name.
     * @param pTypesName
     *            The types name.
     * @return The id of all the element of a specific type.
     */
    abstract protected Iterator<String> getAllElementsId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected S getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        final CacheProperties lCacheProperties = new CacheProperties(true);

        // Hibernate session must be evicted because all the export is on one transaction
        lCacheProperties.setCacheEvictProperty();

        // Load cacheable element
        final CacheableValuesContainer lCachedElement =
                fieldsContainerServiceImpl.getValuesContainer(pRoleToken,
                        pElementId,
                        FieldsContainerService.FIELD_NOT_CONFIDENTIAL
                                | FieldsContainerService.FIELD_EXPORT,
                        lCacheProperties);
        final S lSerializedObject = marshal(lCachedElement, pExportProperties);
        // Check that the container is not confidential
        final TypeAccessControlData lAccess =
                authorizationServiceImpl.getTypeAccessControl(pRoleToken,
                        getAccessControlContextData(pRoleToken,
                                lSerializedObject));

        if (lAccess.getConfidential()) {
            // Return null if the container is confidential
            return null;
        }
        else {
            final String lAttachedFilePath =
                    pExportProperties.getAttachedFilePath();

            // Export attached files
            if (StringUtils.isNotBlank(lAttachedFilePath)) {
                final List<? extends AttachedFieldValueData> lAttachedFileValues =
                        lCachedElement.getAllAttachedFileValues();

                if (lAttachedFileValues != null) {
                    for (AttachedFieldValueData lFileValue : lAttachedFileValues) {
                        final AttachedFieldValue lAttachedFieldValue =
                                (AttachedFieldValue) attachedFieldValueDao.load(lFileValue.getId());

                        if (lAttachedFieldValue != null) {
                            byte[] lFileContent = null;
                            if (lAttachedFieldValue.getAttachedFieldContent() != null) {
                                lFileContent =
                                        lAttachedFieldValue.getAttachedFieldContent().getContent();
                            }
                            final String lDbFilename =
                                    lAttachedFieldValue.getName();
                            final int lExtIndex = lDbFilename.lastIndexOf(".");
                            String lActualFilename =
                                    lAttachedFieldValue.getId();

                            if (lExtIndex != -1) {
                                lActualFilename +=
                                        lDbFilename.substring(lExtIndex);
                            }
                            try {
                                if (lAttachedFilePath != null) {
                                    lActualFilename =
                                            new File(lAttachedFilePath,
                                                    lActualFilename).getCanonicalPath();
                                }
                            }
                            catch (IOException e) {
                                throw new SystemException("Invalid path '"
                                        + lAttachedFilePath + "'", e);
                            }

                            lFileValue.setFilename(lActualFilename);
                            lFileValue.setId(null);

                            try {
                                final OutputStream lContentOutStream =
                                        new FileOutputStream(lActualFilename);

                                try {
                                    if (lFileContent != null) {
                                        if (ReadProperties.getInstance().isObfAttachedFiles()) {
                                            createObfuscatedFile(
                                                    lFileContent.length,
                                                    lFileValue.getFilename());
                                        }
                                        else {
                                            lContentOutStream.write(
                                                    lFileContent, 0,
                                                    lFileContent.length);
                                        }
                                    }
                                }
                                catch (IOException e) {
                                    throw new SystemException(
                                            "Cannot write file '"
                                                    + lActualFilename + "'", e);
                                }
                                finally {
                                    if (lContentOutStream != null) {
                                        //release java memory (release hibernate memory associated to the file)
                                        attachedFieldValueDao.evict(lAttachedFieldValue);
                                        lContentOutStream.close();
                                    }
                                }
                            }
                            catch (IOException e) {
                                throw new SystemException("Cannot close file '"
                                        + lActualFilename + "'", e);
                            }
                        }
                    }
                }
            }

            // Udpate serializable object
            updateSerializableObject(pRoleToken, lSerializedObject,
                    pExportProperties);

            return lSerializedObject;
        }
    }

    /**
     * Convert a cacheable object to a serializable one.
     * 
     * @param pCacheable
     *            The cachebale object.
     * @param pExportProperties
     *            The export properties.
     * @return The serializable object.
     */
    abstract protected S marshal(CacheableValuesContainer pCacheable,
            ExportProperties pExportProperties);

    /**
     * Update the serializable object before export it.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pContainer
     *            The serializable object.
     * @param pExportProperties
     *            The export properties.
     */
    protected void updateSerializableObject(final String pRoleToken,
            final S pContainer, final ExportProperties pExportProperties) {
        // Remove UUID if necessary
        if (!pExportProperties.isExportUID()) {
            // Remove the container id
            pContainer.setId(null);
            // Remove the pointer fields
            removeUUID(pRoleToken, pContainer.getFieldValues());
        }
    }

    private void removeUUID(final String pRoleToken,
            final List<FieldValueData> pValues) {
        if (pValues != null) {
            for (FieldValueData lValue : pValues) {
                if (lValue != null) {
                    // Sub fields
                    removeUUID(pRoleToken, lValue.getFieldValues());
                    // Pointer fields
                    if (lValue instanceof PointerFieldValueData) {
                        final PointerFieldValueData lPointer =
                                (PointerFieldValueData) lValue;
                        final CacheableValuesContainer lReferencedContainer =
                                fieldsContainerServiceImpl.getValuesContainer(
                                        pRoleToken,
                                        lPointer.getReferencedContainerId(), 0,
                                        CacheProperties.IMMUTABLE);

                        // Remove the pointed container UUID...
                        lPointer.setReferencedContainerId(null);
                        // ... but fill its functional reference
                        lPointer.setReferencedContainerRef(lReferencedContainer.getFunctionalReference());
                        lPointer.setReferencedProduct(lReferencedContainer.getProductName());
                    }
                    // Attached fields
                    else if (lValue instanceof AttachedFieldValueData) {
                        ((AttachedFieldValueData) lValue).setId(null);
                    }
                }
            }
        }
    }

    /**
     * Get the access control context data.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSerializedObject
     *            The object to check access rights.
     * @return The access control context data.
     */
    protected AccessControlContextData getAccessControlContextData(
            final String pRoleToken, final S pSerializedObject) {
        final AccessControlContextData lContext =
                new AccessControlContextData();

        lContext.setRoleName(authorizationServiceImpl.getRoleNameFromToken(pRoleToken));
        lContext.setProductName(pSerializedObject.getProductName());
        lContext.setContainerTypeId(fieldsContainerServiceImpl.getFieldsContainerIdByValuesContainer(
                pRoleToken, pSerializedObject.getId()));
        lContext.setValuesContainerId(pSerializedObject.getId());

        return lContext;
    }

    /**
     * Creates a file of a given size with the given filename.
     * The file is filled with "x" 
     * @param pFileLength the size of the file
     * @param pFileName the name of the file
     */
    private void createObfuscatedFile(int pFileLength, String pFileName) {
        MappedByteBuffer out = null;
        RandomAccessFile lRandomAccessFile = null;
        try {
            File lTestFile = new File(pFileName);
            lRandomAccessFile = new RandomAccessFile(lTestFile, "rw");
            out =
                    lRandomAccessFile.getChannel().map(
                            FileChannel.MapMode.READ_WRITE, 0, pFileLength);

            for (int i = 0; i < pFileLength; i++) {
                out.put((byte) 'x');
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if (lRandomAccessFile != null) {
        		try {
					lRandomAccessFile.close();
				} catch (IOException lEx) {
					lEx.printStackTrace();
				}
        	}
        }
    }
}

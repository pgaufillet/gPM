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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldsManager;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.impl.FieldsContainerServiceImpl;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;

/**
 * AbstractValuesContainerImportManager handles values container importation
 * <p>
 * Basic access control verification (canImport)
 * </p>
 * 
 * @param <SV>
 *            Serialize object
 * @param <BV>
 *            Business object
 * @param <BT>
 *            Business type object
 * @author mkargbo
 */
public abstract class AbstractValuesContainerImportManager<SV extends ValuesContainerData, BV extends CacheableValuesContainer, BT extends CacheableFieldsContainer>
        extends AbstractImportManager<SV, BV> {

    protected FieldsContainerServiceImpl fieldsContainerServiceImpl;

    protected FieldsManager fieldsManager;

    /**
     * {@inheritDoc}
     * <p>
     * Can import a values container if:
     * <ol>
     * <li>its type not confidential.</li>
     * <li>Mandatory fields have been filled and field's to import are not
     * confidential.</li>
     * <li>Creation: The type can be create.</li>
     * <li>Erase: The type can be create and delete.</li>
     * <li>Updating: The type can be update and the fields can be update.</li>
     * </ol>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected boolean canImport(String pRoleToken, SV pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        boolean lCanImport = true;
        if (!authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)) {
            long lTypeAccessFlag = FieldsContainerService.NOT_CONFIDENTIAL;
            long lFieldAccessFlag =
                    FieldsContainerService.FIELD_NOT_CONFIDENTIAL;
            switch (getImportFlag(pProperties)) {
                case CREATE_ONLY:
                    lTypeAccessFlag =
                            lTypeAccessFlag | FieldsContainerService.CREATE;
                    break;
                case ERASE:
                    lTypeAccessFlag =
                            lTypeAccessFlag | FieldsContainerService.CREATE
                                    | FieldsContainerService.DELETE;
                    break;
                case UPDATE_ONLY:
                    lTypeAccessFlag =
                            lTypeAccessFlag | FieldsContainerService.UPDATE;
                    lFieldAccessFlag =
                            lFieldAccessFlag
                                    | FieldsContainerService.FIELD_UPDATE;
                    break;
                case CREATE_OR_UPDATE:
                    if (StringUtils.isBlank(pElementId)) {
                        lTypeAccessFlag =
                                lTypeAccessFlag | FieldsContainerService.CREATE;
                    }
                    else {
                        lTypeAccessFlag =
                                lTypeAccessFlag | FieldsContainerService.UPDATE;
                        lFieldAccessFlag =
                                lFieldAccessFlag
                                        | FieldsContainerService.FIELD_UPDATE;
                    }
                    break;
                default: //
            }
            lCanImport =
                    checkAccess(pRoleToken, pElement, pProperties, pReport,
                            lTypeAccessFlag, lFieldAccessFlag);
        }
        return lCanImport;
    }

    private boolean checkAccess(String pRoleToken, SV pElement,
            ImportProperties pProperties, final ImportExecutionReport pReport,
            final long pTypeAccess, final long pFieldAccess)
        throws ImportException {
        //Type access
        boolean lNotConfidential =
                ((pTypeAccess & FieldsContainerService.NOT_CONFIDENTIAL) != 0);
        boolean lCreate = ((pTypeAccess & FieldsContainerService.CREATE) != 0);
        boolean lUptade = ((pTypeAccess & FieldsContainerService.UPDATE) != 0);
        boolean lDelete = ((pTypeAccess & FieldsContainerService.DELETE) != 0);

        boolean lCanImport = true;
        final AccessControlContextData lAccessControlContext =
                getAccessControlContext(pRoleToken, pElement);
        final TypeAccessControlData lTypeAccessControl =
                authorizationServiceImpl.getTypeAccessControl(pRoleToken,
                        lAccessControlContext);

        if (lNotConfidential
                && (lTypeAccessControl.getConfidential().compareTo(
                        !lNotConfidential) != 0)) {
            onFailure(pElement, pProperties, pReport,
                    "This element is confidential.");
            lCanImport = false;
        }

        if (lCreate
                && (lTypeAccessControl.getCreatable().compareTo(lCreate) != 0)) {
            onFailure(pElement, pProperties, pReport,
                    "This element cannot be created.");
            lCanImport = false;
        }

        if (lUptade
                && (lTypeAccessControl.getUpdatable().compareTo(lUptade) != 0)) {
            onFailure(pElement, pProperties, pReport,
                    "This element cannot be updated.");
            lCanImport = false;
        }
        if (lDelete
                && (lTypeAccessControl.getDeletable().compareTo(lDelete) != 0)) {
            onFailure(pElement, pProperties, pReport,
                    "This element cannot be deleted.");
            lCanImport = false;
        }

        //Field access
        //Compute defined field access
        boolean lFieldNotConfidential =
                ((pFieldAccess & FieldsContainerService.FIELD_NOT_CONFIDENTIAL) != 0);
        boolean lFieldUpdate =
                ((pFieldAccess & FieldsContainerService.FIELD_UPDATE) != 0);

        Collection<String> lLabelKey = getFieldsLabelKey(pElement);
        BT lBusinessType = getBusinessTypeObject(pRoleToken, pElement);
        for (org.topcased.gpm.business.serialization.data.Field lField : lBusinessType.getAllFields()) {
            FieldAccessData lFieldAccessData =
                    authorizationServiceImpl.getFieldAccess(pRoleToken,
                            lAccessControlContext, lField.getId());
            if (lFieldAccessData.getMandatory()
                    && !(lLabelKey.contains(lField.getLabelKey()))) {
                onFailure(pElement, pProperties, pReport, "The field '"
                        + lField.getLabelKey() + "' is mandatory.");
                lCanImport = false;
            }
            else if (lLabelKey.contains(lField.getLabelKey())) {
                if (lFieldNotConfidential
                        && (lFieldAccessData.getConfidential().compareTo(
                                !lFieldNotConfidential) != 0)) {
                    onFailure(pElement, pProperties, pReport, "The field '"
                            + lField.getLabelKey() + "' is confidential.");
                    lCanImport = false;
                }
                if (lFieldUpdate
                        && (lFieldAccessData.getUpdatable().compareTo(
                                lFieldUpdate) != 0)) {
                    onFailure(pElement, pProperties, pReport, "The field '"
                            + lField.getLabelKey() + "' cannot be updated.");
                    lCanImport = false;
                }
            }
        }
        return lCanImport;
    }

    /**
     * Get label key of fields of the element to import
     * 
     * @param pElement
     *            Element to import
     * @return Field's label key to import
     */
    private Collection<String> getFieldsLabelKey(final SV pElement) {
        Collection<String> lLabelKey = new HashSet<String>();
        List<FieldValueData> lFieldValues = pElement.getFieldValues();
        if (CollectionUtils.isNotEmpty(lFieldValues)) {
            for (FieldValueData lFieldValueData : lFieldValues) {
                lLabelKey.add(lFieldValueData.getName());
                if (!CollectionUtils.isEmpty(lFieldValueData.getFieldValues())) {
                    List<FieldValueData> lSubFieldValues =
                            lFieldValueData.getFieldValues();
                    for (FieldValueData lSubFieldValueData : lSubFieldValues) {
                        lLabelKey.add(lSubFieldValueData.getName());
                    }
                }
            }
        }
        return lLabelKey;
    }

    /**
     * Get access control context for the values container.
     * <p>
     * Set the roleName, productName, typeId and valuesContainerId
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element to test
     * @return True if the type of the element is not confidential, false
     *         otherwise.
     */
    protected AccessControlContextData getAccessControlContext(
            String pRoleToken, SV pElement) {
        final AccessControlContextData lAccessControlContext =
                new AccessControlContextData();
        String lRoleName =
                authorizationServiceImpl.getRoleNameFromToken(pRoleToken);
        lAccessControlContext.setRoleName(lRoleName);
        lAccessControlContext.setProductName(pElement.getProductName());
        String lTypeId =
                fieldsContainerServiceImpl.getFieldsContainerId(pRoleToken,
                        pElement.getType());
        lAccessControlContext.setContainerTypeId(lTypeId);
        lAccessControlContext.setValuesContainerId(pElement.getId());
        return lAccessControlContext;
    }

    /**
     * Get the business object for the type of the element.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element of the searched type.
     * @return Type of the element.
     */
    protected abstract BT getBusinessTypeObject(String pRoleToken,
            final SV pElement);

    /**
     * {@inheritDoc}
     * <p>
     * Additionally fill the attached files.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getBusinessObject(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected BV getBusinessObject(String pRoleToken, SV pElement,
            ImportProperties pProperties) {
        BT lBusinessType = getBusinessTypeObject(pRoleToken, pElement);
        BV lBusinessObject =
                doGetBusinessObject(pRoleToken, pElement, lBusinessType,
                        pProperties);
        BV lCheckedBusinessObject =
                authorizationServiceImpl.getCheckedValuesContainer(pRoleToken,
                        getAccessControlContext(pRoleToken, pElement),
                        lBusinessType, lBusinessObject,
                        getFieldAccessFlag(getImportFlag(pProperties)));
        if (pProperties.isImportFileContent()) {
            fieldsManager.fillAttachedFilesContent(lCheckedBusinessObject);
        }
        return lCheckedBusinessObject;
    }

    private long getFieldAccessFlag(ImportProperties.ImportFlag pFlag) {
        long lFlag = FieldsContainerService.FIELD_NOT_CONFIDENTIAL;
        switch (pFlag) {
            case CREATE_OR_UPDATE:
            case UPDATE_ONLY:
                lFlag = lFlag | FieldsContainerService.FIELD_UPDATE;
                break;
            default:
        }
        return lFlag;

    }

    /**
     * Get the business object.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element matching with business object.
     * @param pBusinessType
     *            Business Type of the business object
     * @param pProperties
     *            Import properties.
     * @return Business object corresponding to the element.
     */
    protected abstract BV doGetBusinessObject(String pRoleToken, SV pElement,
            BT pBusinessType, ImportProperties pProperties);

    public void setAuthorizationServiceImpl(
            AuthorizationServiceImpl pAuthorizationServiceImpl) {
        authorizationServiceImpl = pAuthorizationServiceImpl;
    }

    public void setFieldsContainerServiceImpl(
            FieldsContainerServiceImpl pFieldsContainerServiceImpl) {
        fieldsContainerServiceImpl = pFieldsContainerServiceImpl;
    }

    public void setFieldsManager(FieldsManager pFieldsManager) {
        fieldsManager = pFieldsManager;
    }

}

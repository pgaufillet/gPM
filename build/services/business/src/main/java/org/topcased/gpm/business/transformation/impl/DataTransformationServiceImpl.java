/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.transformation.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductData;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.revision.impl.CacheableRevision;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.LockData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.transformation.service.DataTransformationService;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Data transformation service implementation
 * 
 * @author tpanuel
 */
public class DataTransformationServiceImpl extends ServiceImplBase implements
        DataTransformationService {

    public FieldGroupData[] getFieldGroupData(String pRoleToken,
            CacheableSheet pCacheableSheet) {
        final CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        pCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);
        final Product lDomainProduct =
                getProduct(lCacheableSheetType.getBusinessProcessName(),
                        pCacheableSheet.getProductName());
        // Get the display groups for this sheet type.
        final Collection<? extends DisplayGroup> lGroups =
                lCacheableSheetType.getDisplayGroups();
        final FieldGroupData[] lGroupsData = new FieldGroupData[lGroups.size()];
        final String lLang = getI18nService().getPreferredLanguage(pRoleToken);
        int lGroupIdx = 0;

        for (DisplayGroup lDisplayGroup : lGroups) {
            final String lLocalizedGroupName =
                    getI18nService().getValue(lDisplayGroup.getName(), lLang);
            final FieldGroupData lFieldGroupData =
                    new FieldGroupData(
                            lDisplayGroup.getName(),
                            lLocalizedGroupName,
                            lDisplayGroup.getOpened(),
                            new MultipleLineFieldData[lDisplayGroup.getFields().size()]);
            int i = 0;

            for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                final Field lField =
                        lCacheableSheetType.getFieldFromLabel(lFieldRef.getName());

                if (null == lField) {
                    throw new GDMException("Field '" + lFieldRef.getName()
                            + "' unknown");
                }
                lFieldGroupData.getMultipleLineFieldDatas()[i] =
                        dataTransformationManager.createMultipleLineFieldData(
                                pRoleToken, lCacheableSheetType,
                                pCacheableSheet, lField, lDomainProduct.getId());
                i++;
            }
            lGroupsData[lGroupIdx] = lFieldGroupData;
            lGroupIdx++;
        }
        return lGroupsData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getSheetDataFromCacheableSheet(String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet)
     */
    @SuppressWarnings("unchecked")
    public SheetData getSheetDataFromCacheableSheet(String pRoleToken,
            CacheableSheet pCacheableSheet) {
        final CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        pCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);
        final SheetData lSheetData = new SheetData();
        final Product lDomainProduct =
                getProduct(lCacheableSheetType.getBusinessProcessName(),
                        pCacheableSheet.getProductName());

        lSheetData.setId(pCacheableSheet.getId());
        lSheetData.setVersion(pCacheableSheet.getVersion());
        lSheetData.setProductName(pCacheableSheet.getProductName());
        lSheetData.setSheetReference(pCacheableSheet.getFunctionalReference());
        lSheetData.setEnvironmentNames(pCacheableSheet.getEnvironmentNames().toArray(
                new String[pCacheableSheet.getEnvironmentNames().size()]));

        final Lock lLock =
                atomicActionsManager.getSheetLock(pCacheableSheet.getId());

        // Set Lock
        if (lLock != null) {
            final LockData lContainerLockData =
                    new LockData(lLock.getOwner(),
                            LockType.fromString(lLock.getType().toString()));

            lSheetData.setLock(lContainerLockData);
        }

        // Init type informations
        lSheetData.setProcessName(lCacheableSheetType.getBusinessProcessName());
        lSheetData.setSheetTypeName(lCacheableSheetType.getName());
        lSheetData.setSheetTypeId(lCacheableSheetType.getId());

        // Get the display groups for this sheet type.
        if (pRoleToken != null) {
            final Collection<? extends DisplayGroup> lGroups =
                    lCacheableSheetType.getDisplayGroups();
            final FieldGroupData[] lGroupsData =
                    new FieldGroupData[lGroups.size()];
            final String lLang =
                    getI18nService().getPreferredLanguage(pRoleToken);
            int lGroupIdx = 0;

            for (DisplayGroup lDisplayGroup : lGroups) {
                final String lLocalizedGroupName =
                        getI18nService().getValue(lDisplayGroup.getName(),
                                lLang);
                final FieldGroupData lFieldGroupData =
                        new FieldGroupData(
                                lDisplayGroup.getName(),
                                lLocalizedGroupName,
                                lDisplayGroup.getOpened(),
                                new MultipleLineFieldData[lDisplayGroup.getFields().size()]);
                int i = 0;

                for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                    final Field lField =
                            lCacheableSheetType.getFieldFromLabel(lFieldRef.getName());

                    if (null == lField) {
                        throw new GDMException("Field '" + lFieldRef.getName()
                                + "' unknown");
                    }
                    lFieldGroupData.getMultipleLineFieldDatas()[i] =
                            dataTransformationManager.createMultipleLineFieldData(
                                    pRoleToken, lCacheableSheetType,
                                    pCacheableSheet, lField,
                                    lDomainProduct.getId());
                    i++;
                }
                lGroupsData[lGroupIdx] = lFieldGroupData;
                lGroupIdx++;
            }
            lSheetData.setFieldGroupDatas(lGroupsData);

            // Set functional reference
            final Map<String, Object> lReferenceValue =
                    (Map<String, Object>) pCacheableSheet.getValue(FieldsService.REFERENCE_FIELD_NAME);

            lSheetData.setReference(dataTransformationManager.createLineFieldData(
                    pRoleToken, lCacheableSheetType, pCacheableSheet, null,
                    lReferenceValue, lCacheableSheetType.getReferenceField(),
                    lDomainProduct.getId(), null));
        }

        return lSheetData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getLinkDataFromCacheableLink(java.lang.String,
     *      org.topcased.gpm.business.link.impl.CacheableLink)
     */
    public LinkData getLinkDataFromCacheableLink(String pRoleToken,
            CacheableLink pCacheableLink) {
        final CacheableLinkType lLinkType =
                getLinkService().getCacheableLinkType(
                        pCacheableLink.getTypeId(), CacheProperties.IMMUTABLE);
        final LinkData lLinkData = new LinkData();

        lLinkData.setId(pCacheableLink.getId());
        lLinkData.setOriginId(pCacheableLink.getOriginId());
        lLinkData.setDestinationId(pCacheableLink.getDestinationId());

        lLinkData.setOriginType(lLinkType.getOriginTypeName());
        lLinkData.setDestinationType(lLinkType.getDestinationTypeName());

        // Store the functional references of linked elements.
        lLinkData.setOriginRef(pCacheableLink.getOriginRef());
        lLinkData.setDestinationRef(pCacheableLink.getDestinationRef());

        // Set the type name
        lLinkData.setLinkTypeName(lLinkType.getName());
        lLinkData.setLinkTypeId(lLinkType.getId());

        // Set the environments names
        lLinkData.setEnvironmentNames(pCacheableLink.getEnvironmentNames().toArray(
                new String[pCacheableLink.getEnvironmentNames().size()]));

        // Set values
        if (pRoleToken != null) {
            final List<MultipleLineFieldData> lMlfDataList =
                    new ArrayList<MultipleLineFieldData>(
                            lLinkType.getFields().size());
            final String lContainer4Env;

            if (pCacheableLink.getId() != null) {
                lContainer4Env = pCacheableLink.getId();
            }
            else {
                // Get environments from Origin sheet
                lContainer4Env = pCacheableLink.getOriginId();
            }

            for (Field lField : lLinkType.getFields()) {
                lMlfDataList.add(dataTransformationManager.createMultipleLineFieldData(
                        pRoleToken, lLinkType, pCacheableLink, lField, /* environment */
                        lContainer4Env));
            }

            lLinkData.setMultipleLineFieldDatas(lMlfDataList.toArray(new MultipleLineFieldData[lMlfDataList.size()]));
        }

        return lLinkData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getProductDataFromCacheableProduct(java.lang.String,
     *      org.topcased.gpm.business.product.impl.CacheableProduct)
     */
    public ProductData getProductDataFromCacheableProduct(String pRoleToken,
            CacheableProduct pCacheableProduct) {
        final CacheableProductType lProductType =
                getProductService().getCacheableProductType(
                        pCacheableProduct.getTypeId(),
                        CacheProperties.IMMUTABLE);
        final ProductData lProductData = new ProductData();

        lProductData.setId(pCacheableProduct.getId());
        lProductData.setName(pCacheableProduct.getProductName());
        lProductData.setDescription(pCacheableProduct.getDescription());

        lProductData.setProcessName(lProductType.getBusinessProcessName());
        lProductData.setProductTypeId(lProductType.getId());
        lProductData.setProductTypeName(lProductType.getName());

        // Set environments
        if (pCacheableProduct.getEnvironmentNames() != null) {
            lProductData.setEnvironmentNames(pCacheableProduct.getEnvironmentNames().toArray(
                    new String[0]));
        }

        // Set child product
        if (pCacheableProduct.getChildrenNames() != null) {
            lProductData.setChildrenNames(pCacheableProduct.getChildrenNames().toArray(
                    new String[0]));
        }

        // Set values
        if (pRoleToken != null) {
            final List<MultipleLineFieldData> lMlfDataList =
                    new ArrayList<MultipleLineFieldData>(
                            lProductType.getFields().size());

            // Convert values to old structures
            for (Field lField : lProductType.getFields()) {
                lMlfDataList.add(dataTransformationManager.createMultipleLineFieldData(
                        pRoleToken, lProductType, pCacheableProduct, lField,
                        pCacheableProduct.getId()));
            }

            lProductData.setMultipleLineFieldDatas(lMlfDataList.toArray(new MultipleLineFieldData[lMlfDataList.size()]));
        }

        return lProductData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getRevisionDataFromCacheableRevision(java.lang.String,
     *      org.topcased.gpm.business.revision.impl.CacheableRevision,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public RevisionData getRevisionDataFromCacheableRevision(String pRoleToken,
            CacheableRevision pCacheableRevision, String pRevisedContainerId) {
        final CacheableValuesContainer lRevisedContainer =
                getCachedValuesContainer(pRevisedContainerId,
                        CacheProperties.IMMUTABLE.getCacheFlags());
        final CacheableFieldsContainer lRevisedContainerType =
                getCachedFieldsContainer(lRevisedContainer.getTypeId(),
                        CacheProperties.IMMUTABLE.getCacheFlags());
        final RevisionData lRevisionData = new RevisionData();

        lRevisionData.setId(pCacheableRevision.getId());

        lRevisionData.setAuthor(pCacheableRevision.getAuthor());
        lRevisionData.setCreationDate(pCacheableRevision.getCreationDate());
        lRevisionData.setLabel(pCacheableRevision.getLabel());

        // Get the display groups for this sheet type
        if (pRoleToken != null) {
            if (lRevisedContainerType instanceof CacheableSheetType) {
                final CacheableSheet lRevisedSheet =
                        (CacheableSheet) lRevisedContainer;
                final CacheableSheetType lRevisedSheetType =
                        (CacheableSheetType) lRevisedContainerType;
                final Product lDomainProduct =
                        getProduct(lRevisedSheetType.getBusinessProcessName(),
                                lRevisedSheet.getProductName());
                final Collection<? extends DisplayGroup> lGroups =
                        lRevisedSheetType.getDisplayGroups();
                final FieldGroupData[] lGroupsData =
                        new FieldGroupData[lGroups.size()];
                final String lLang =
                        getI18nService().getPreferredLanguage(pRoleToken);
                int lGroupIdx = 0;

                for (DisplayGroup lDisplayGroup : lGroups) {
                    final String lLocalizedGroupName =
                            getI18nService().getValue(lDisplayGroup.getName(),
                                    lLang);
                    final FieldGroupData lFieldGroupData =
                            new FieldGroupData(
                                    lDisplayGroup.getName(),
                                    lLocalizedGroupName,
                                    lDisplayGroup.getOpened(),
                                    new MultipleLineFieldData[lDisplayGroup.getFields().size()]);
                    int i = 0;

                    for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                        final Field lField =
                                lRevisedContainerType.getFieldFromLabel(lFieldRef.getName());

                        if (null == lField) {
                            throw new GDMException("Field '"
                                    + lFieldRef.getName() + "' unknown");
                        }
                        lFieldGroupData.getMultipleLineFieldDatas()[i] =
                                dataTransformationManager.createMultipleLineFieldData(
                                        pRoleToken, lRevisedContainerType,
                                        pCacheableRevision, lField,
                                        lDomainProduct.getId());
                        i++;
                    }
                    lGroupsData[lGroupIdx] = lFieldGroupData;
                    lGroupIdx++;
                }
                lRevisionData.setFieldGroupDatas(lGroupsData);

                // Set functional reference
                final Map<String, Object> lReferenceValue =
                        (Map<String, Object>) pCacheableRevision.getValue(FieldsService.REFERENCE_FIELD_NAME);

                lRevisionData.setReference(dataTransformationManager.createLineFieldData(
                        pRoleToken, lRevisedContainerType, pCacheableRevision,
                        null, lReferenceValue,
                        lRevisedSheetType.getReferenceField(),
                        lDomainProduct.getId(), null));
            }
            else {
                throw new RuntimeException(
                        "Old structure only support revised sheet and not : "
                                + lRevisedContainerType.getClass());
            }
        }

        // Set attributes data
        if (pCacheableRevision.getRevisionAttributes() != null) {
            final List<Attribute> lCacheableAttributes =
                    pCacheableRevision.getRevisionAttributes().getAllAttributes();

            if (lCacheableAttributes != null && !lCacheableAttributes.isEmpty()) {
                final AttributeData[] lAttributeDatas =
                        new AttributeData[lCacheableAttributes.size()];
                int i = 0;

                for (Attribute lCacheableAttribute : lCacheableAttributes) {
                    final AttributeData lAttributeData = new AttributeData();

                    lAttributeData.setName(lCacheableAttribute.getName());
                    lAttributeData.setValues(lCacheableAttribute.getValues());
                    lAttributeDatas[i] = lAttributeData;
                    i++;
                }
                lRevisionData.setAttributeDatas(lAttributeDatas);
            }
        }

        return lRevisionData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getCacheableSheetFromSheetData(java.lang.String,
     *      org.topcased.gpm.business.sheet.service.SheetData)
     */
    public CacheableSheet getCacheableSheetFromSheetData(String pRoleToken,
            SheetData pSheetData) {
        final CacheableSheet lCacheableSheet = new CacheableSheet();

        lCacheableSheet.setId(pSheetData.getId());
        lCacheableSheet.setProductName(pSheetData.getProductName());
        lCacheableSheet.setFunctionalReference(pSheetData.getSheetReference());
        lCacheableSheet.setVersion(pSheetData.getVersion());

        lCacheableSheet.setTypeId(pSheetData.getSheetTypeId());
        // If type name is not specified, get it from type id
        lCacheableSheet.setTypeName(pSheetData.getSheetTypeName());

        lCacheableSheet.setEnvironmentNames(Arrays.asList(pSheetData.getEnvironmentNames()));

        // Set sheet state
        if (pSheetData.getId() != null) {
            try {
                lCacheableSheet.setCurrentStateName(getLifeCycleService().getProcessStateName(
                        pSheetData.getId()));
            }
            catch (InvalidIdentifierException e) {
                // An invalid identifier here is not necessarily an error, as
                // this method is invoked also when a sheet is created with a 'forced' id.
            }
        }
        else {
            lCacheableSheet.setCurrentStateName(getSheetService().getCacheableSheetType(
                    pSheetData.getSheetTypeId()).getInitialStateName());
        }

        // Set functional reference
        if (pSheetData.getReference() != null
                && pSheetData.getReference().getFieldDatas() != null) {
            final MultipleLineFieldData lReferenceFieldData =
                    new MultipleLineFieldData();

            // Compute a MultipleLineFieldData describing the field functional reference
            lReferenceFieldData.setLabelKey(FieldsService.REFERENCE_FIELD_NAME);
            lReferenceFieldData.setLineFieldDatas(new LineFieldData[] { pSheetData.getReference() });
            // The functional reference field is multiple...
            lReferenceFieldData.setMultiField(true);
            // .. and mono valued
            lReferenceFieldData.setMultiLined(false);

            // Add the functional reference field on the CacheableSheet
            lCacheableSheet.addMultipleLineFieldDatas(new MultipleLineFieldData[] { lReferenceFieldData });
        }

        // Set values
        if (pSheetData.getFieldGroupDatas() != null) {
            for (FieldGroupData lFieldGroup : pSheetData.getFieldGroupDatas()) {
                lCacheableSheet.addMultipleLineFieldDatas(lFieldGroup.getMultipleLineFieldDatas());
            }
        }

        // Some informations of the CacheableSheet are not present on the SheetData
        // - Attributes

        return lCacheableSheet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getCacheableLinkFromLinkData(java.lang.String,
     *      org.topcased.gpm.business.link.service.LinkData)
     */
    public CacheableLink getCacheableLinkFromLinkData(String pRoleToken,
            LinkData pLinkData) {
        final CacheableLink lCacheableLink = new CacheableLink();

        lCacheableLink.setId(pLinkData.getId());

        lCacheableLink.setTypeId(pLinkData.getLinkTypeId());
        // If type name is not specified, get it from type id
        lCacheableLink.setTypeName(pLinkData.getLinkTypeName());

        lCacheableLink.setDestinationId(pLinkData.getDestinationId());
        lCacheableLink.setDestinationRef(pLinkData.getDestinationRef());
        lCacheableLink.setOriginId(pLinkData.getOriginId());
        lCacheableLink.setOriginRef(pLinkData.getOriginRef());

        lCacheableLink.setEnvironmentNames(getCachedValuesContainer(
                pLinkData.getOriginId(),
                CacheProperties.IMMUTABLE.getCacheFlags()).getEnvironmentNames());

        // Set values
        lCacheableLink.addMultipleLineFieldDatas(pLinkData.getMultipleLineFieldDatas());

        // Some informations of the CacheableLink are not present on the LinkData
        // - Attributes
        // - Lock informations
        // - Functional Reference
        // - Version

        return lCacheableLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getCacheableProductFromProductData(java.lang.String,
     *      org.topcased.gpm.business.product.service.ProductData)
     */
    public CacheableProduct getCacheableProductFromProductData(
            String pRoleToken, ProductData pProductData) {
        final CacheableProduct lCacheableProduct = new CacheableProduct();

        lCacheableProduct.setId(pProductData.getId());
        lCacheableProduct.setProductName(pProductData.getName());

        lCacheableProduct.setTypeId(pProductData.getProductTypeId());
        lCacheableProduct.setTypeName(pProductData.getProductTypeName());

        if (pProductData.getChildrenNames() != null) {
            lCacheableProduct.setChildrenNames(Arrays.asList(pProductData.getChildrenNames()));
        }
        if (pProductData.getEnvironmentNames() != null) {
            lCacheableProduct.setEnvironmentNames(Arrays.asList(pProductData.getEnvironmentNames()));
        }

        // Set values
        lCacheableProduct.addMultipleLineFieldDatas(pProductData.getMultipleLineFieldDatas());

        // Some informations of the CacheableProduct are not present on the ProductData
        // - Attributes
        // - Lock informations
        // - Functional Reference
        // - Parents names
        // - Version

        return lCacheableProduct;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.transformation.service.DataTransformationService#getCacheableRevisionFromRevisionData(java.lang.String,
     *      org.topcased.gpm.business.revision.RevisionData, java.lang.String)
     */
    public CacheableRevision getCacheableRevisionFromRevisionData(
            String pRoleToken, RevisionData pRevisionData,
            String pRevisedContainerId) {
        final CacheableRevision lCacheableRevision = new CacheableRevision();
        final CacheableValuesContainer lRevisedContainer =
                getCachedValuesContainer(pRevisedContainerId,
                        CacheProperties.IMMUTABLE.getCacheFlags());
        final CacheableFieldsContainer lRevisedContainerType =
                getCachedFieldsContainer(lRevisedContainer.getTypeId(),
                        CacheProperties.IMMUTABLE.getCacheFlags());

        lCacheableRevision.setId(pRevisionData.getId());

        lCacheableRevision.setTypeId(lRevisedContainerType.getId());
        lCacheableRevision.setTypeName(lRevisedContainerType.getName());

        lCacheableRevision.setAuthor(pRevisionData.getAuthor());
        lCacheableRevision.setCreationDate(pRevisionData.getCreationDate());
        lCacheableRevision.setLabel(pRevisionData.getLabel());

        // Set values
        if (pRevisionData.getFieldGroupDatas() != null) {
            for (FieldGroupData lFieldGroup : pRevisionData.getFieldGroupDatas()) {
                lCacheableRevision.addMultipleLineFieldDatas(lFieldGroup.getMultipleLineFieldDatas());
            }
        }

        // Set functional reference
        if (pRevisionData.getReference() != null
                && pRevisionData.getReference().getFieldDatas() != null) {
            final MultipleLineFieldData lReferenceFieldData =
                    new MultipleLineFieldData();

            // Compute a MultipleLineFieldData describing the field functional reference
            lReferenceFieldData.setLabelKey(FieldsService.REFERENCE_FIELD_NAME);
            lReferenceFieldData.setLineFieldDatas(new LineFieldData[] { pRevisionData.getReference() });
            // The functional reference field is multiple...
            lReferenceFieldData.setMultiField(true);
            // .. and mono valued
            lReferenceFieldData.setMultiLined(false);

            // Add the functional reference field on the CacheableRevision
            lCacheableRevision.addMultipleLineFieldDatas(new MultipleLineFieldData[] { lReferenceFieldData });
        }

        // Set revision attributes
        final CacheableAttributesContainer lRevisionAttributes =
                new CacheableAttributesContainer();

        if (pRevisionData.getAttributeDatas() != null) {
            final List<Attribute> lAttributes = new LinkedList<Attribute>();

            for (AttributeData lAttributeData : pRevisionData.getAttributeDatas()) {
                final Attribute lAttribute = new Attribute();

                lAttribute.setName(lAttributeData.getName());
                lAttribute.setValues(lAttributeData.getValues());
                lAttributes.add(lAttribute);
            }
            lRevisionAttributes.addAttributes(lAttributes);
        }
        lCacheableRevision.setRevisionAttributes(lRevisionAttributes);

        // Some informations of the CacheableProduct are not present on the ProductData
        // - Attributes
        // - Environments
        // - Lock informations
        // - Product name
        // - Type id
        // - Type name
        // - Version

        return lCacheableRevision;
    }

    private DataTransformationManager dataTransformationManager;

    /**
     * Getter on the data transformation manager
     * 
     * @return The data transformation manager
     */
    public DataTransformationManager getDataTransformationManager() {
        return dataTransformationManager;
    }

    /**
     * Setter on the data transformation manager
     * 
     * @param pDataTransformationManager
     *            The new data transformation manager
     */
    public void setDataTransformationManager(
            DataTransformationManager pDataTransformationManager) {
        dataTransformationManager = pDataTransformationManager;
    }
}
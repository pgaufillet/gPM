/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.exportation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exportation.impl.ExportationData;
import org.topcased.gpm.business.exportation.impl.ReadProperties;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.common.MigrationOptions;
import org.topcased.gpm.serialization.common.MigrationProducer;
import org.topcased.gpm.serialization.common.MigrationStopProperties;
import org.topcased.gpm.serialization.common.MigrationTaskManager;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Exportation producer
 * 
 * @author nveillet
 */
public class ExportationProducer extends MigrationProducer {

    private static final int PACKAGE_NUMBER_SIZE = 5;

    static {
        ELEMENTS_TYPE.add(MigrationElementType.USER);
        ELEMENTS_TYPE.add(MigrationElementType.PRODUCT);
        ELEMENTS_TYPE.add(MigrationElementType.ENVIRONMENT);
        ELEMENTS_TYPE.add(MigrationElementType.DICTIONARY);
        ELEMENTS_TYPE.add(MigrationElementType.PRODUCT_LINK);
        ELEMENTS_TYPE.add(MigrationElementType.USER);
        ELEMENTS_TYPE.add(MigrationElementType.FILTER);
        ELEMENTS_TYPE.add(MigrationElementType.SHEET);
        ELEMENTS_TYPE.add(MigrationElementType.SHEET_LINK);
    }

    private List<String> exportableProductLinkTypes;

    private List<String> exportableProductsName;

    private List<String> exportableProductTypes;

    private List<String> exportableSheetLinkTypes;

    private List<String> exportableSheetTypes;

    private ExportService exportService;

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     */
    public ExportationProducer(MigrationTaskManager pTaskManager,
            MigrationOptions pOptions) {
        super(pTaskManager, pOptions);
    }

    /**
     * Create the directory of a specified element type
     * 
     * @param pElementType
     *            the element type
     */
    private void createDirectory(final MigrationElementType pElementType) {
        File lDirectory =
                new File(
                        getOptions().getBaseDirectory()
                                + File.separator
                                + getOptions().getElementDirectories().get(
                                        pElementType));

        if (!lDirectory.exists()) {
            boolean lSuccess = lDirectory.mkdir();
            if (!lSuccess) {
                throw new GDMException("Impossible to create directory "
                        + lDirectory.getName());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationProducer#execute()
     */
    @Override
    protected void execute() throws InterruptedException {

        logHeader("EXPORTATION");

        if (getOptions().isExportAttachedFiles()) {
            createDirectory(MigrationElementType.ATTACHED_FILE);
        }

        for (MigrationElementType lElementType : ELEMENTS_TYPE) {

            int lPackageNumber = 0;

            if (!isError()) {
                switch (lElementType) {
                    case PRODUCT:
                        lPackageNumber = exportProducts();
                        break;
                    case PRODUCT_LINK:
                        lPackageNumber = exportProductsLink();
                        break;
                    case SHEET:
                        lPackageNumber = exportSheets();
                        break;
                    case SHEET_LINK:
                        lPackageNumber = exportSheetsLink();
                        break;
                    default:
                        lPackageNumber = exportElements(lElementType);
                        break;
                }
            }

            waitLogs(lElementType, lPackageNumber);
        }

        getTaskManager().addTask(new MigrationStopProperties());
    }

    /**
     * Export
     * 
     * @param pElementType
     *            the element type
     * @param pElementsId
     *            the elements identifier
     * @param pMaxElementNumber
     *            the maximum element per package number
     * @param pContainerType
     *            the container type
     * @param pProductName
     *            the product name
     * @param pDestinationProductName
     *            the destination product name (for links)
     * @return the package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int export(final MigrationElementType pElementType,
            final Iterator<String> pElementsId,
            final Integer pMaxElementNumber, final String pContainerType,
            final String pProductName, final String pDestinationProductName)
        throws InterruptedException {
        int lPackageNumber = 0;

        while (pElementsId.hasNext()) {
            int lIndexElement = 0;
            List<String> lLimitedElementsId = new ArrayList<String>();

            while (pElementsId.hasNext() && lIndexElement < pMaxElementNumber) {
                lLimitedElementsId.add(pElementsId.next());
                lIndexElement++;
            }

            ExportationProperties lProperties = new ExportationProperties();
            lProperties.setElementType(pElementType);
            lProperties.setElementsId(lLimitedElementsId);
            lProperties.setFileName(generateFileName(pElementType,
                    pContainerType, pProductName, pDestinationProductName,
                    lPackageNumber));

            getTaskManager().addTask(lProperties);

            lPackageNumber++;
        }

        return lPackageNumber;
    }

    /**
     * Export items
     * 
     * @param pElementType
     *            the element type
     * @return the package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int exportElements(final MigrationElementType pElementType)
        throws InterruptedException {
        Integer lMaxNumElement =
                getOptions().getElementCount().get(pElementType);

        if (lMaxNumElement > 0) {

            createDirectory(pElementType);

            Iterator<String> lElementsId =
                    getElementsId(pElementType, null, null);

            return export(pElementType, lElementsId, lMaxNumElement, null,
                    null, null);
        }

        return 0;
    }

    /**
     * Export the products
     * 
     * @return package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int exportProducts() throws InterruptedException {
        Integer lMaxNumElement =
                getOptions().getElementCount().get(MigrationElementType.PRODUCT);

        if (lMaxNumElement > 0) {

            createDirectory(MigrationElementType.PRODUCT);

            int lPackageNumber = 0;

            for (String lProductTypeName : getExportableProductTypes()) {
                Iterator<String> lElementsId =
                        getElementsId(MigrationElementType.PRODUCT,
                                lProductTypeName,
                                getOptions().getExportLimitedProductsName());

                lPackageNumber +=
                        export(MigrationElementType.PRODUCT, lElementsId,
                                lMaxNumElement, lProductTypeName, null, null);
            }
            if (ReadProperties.getInstance().isObfProducts()
                    && ReadProperties.getInstance().isProductMapping()) {
                createMappingFile(ReadProperties.getInstance().getDirectory()
                        + ReadProperties.PRODUCT_MAPPING_FILE);
            }
            return lPackageNumber;
        }

        return 0;
    }

    /**
     * Export the product links
     * 
     * @return package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int exportProductsLink() throws InterruptedException {
        Integer lMaxNumElement =
                getOptions().getElementCount().get(
                        MigrationElementType.PRODUCT_LINK);

        if (lMaxNumElement > 0) {

            createDirectory(MigrationElementType.PRODUCT_LINK);

            int lPackageNumber = 0;

            for (String lProductLinkTypeName : getExportableProductLinkTypes()) {
                Iterator<String> lElementsId =
                        getElementsId(MigrationElementType.PRODUCT_LINK,
                                lProductLinkTypeName,
                                getOptions().getExportLimitedProductsName());

                lPackageNumber +=
                        export(MigrationElementType.PRODUCT_LINK, lElementsId,
                                lMaxNumElement, lProductLinkTypeName, null,
                                null);
            }

            return lPackageNumber;
        }

        return 0;
    }

    /**
     * Export the sheets
     * 
     * @return package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int exportSheets() throws InterruptedException {
        Integer lMaxNumElement =
                getOptions().getElementCount().get(MigrationElementType.SHEET);

        if (lMaxNumElement > 0) {

            createDirectory(MigrationElementType.SHEET);

            int lPackageNumber = 0;

            for (String lSheetTypeName : getExportableSheetTypes()) {
                for (String lProductName : getExportableProductsName()) {
                    Iterator<String> lElementsId =
                            getElementsId(MigrationElementType.SHEET,
                                    lSheetTypeName, Arrays.asList(lProductName));

                    lPackageNumber +=
                            export(MigrationElementType.SHEET, lElementsId,
                                    lMaxNumElement, lSheetTypeName,
                                    lProductName, null);
                }
            }

            return lPackageNumber;
        }

        return 0;
    }

    /**
     * Export the sheet links
     * 
     * @return package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private int exportSheetsLink() throws InterruptedException {
        Integer lMaxNumElement =
                getOptions().getElementCount().get(
                        MigrationElementType.SHEET_LINK);

        if (lMaxNumElement > 0) {

            createDirectory(MigrationElementType.SHEET_LINK);

            int lPackageNumber = 0;

            for (String lSheetLinkTypeName : getExportableSheetLinkTypes()) {

                for (String lOriginProduct : getExportableProductsName()) {

                    List<String> lDestinationProducts =
                            new ArrayList<String>(getExportableProductsName());
                    lDestinationProducts.retainAll(getSheetLinkDestinationProductsName(
                            lSheetLinkTypeName, lOriginProduct));

                    for (String lDestinationProduct : lDestinationProducts) {

                        List<String> lProductsName =
                                Arrays.asList(lOriginProduct,
                                        lDestinationProduct);

                        Iterator<String> lElementsId =
                                getElementsId(MigrationElementType.SHEET_LINK,
                                        lSheetLinkTypeName, lProductsName);

                        lPackageNumber +=
                                export(MigrationElementType.SHEET_LINK,
                                        lElementsId, lMaxNumElement,
                                        lSheetLinkTypeName, lOriginProduct,
                                        lDestinationProduct);
                    }
                }
            }

            return lPackageNumber;
        }

        return 0;
    }

    /**
     * Generate the file name to export.
     * 
     * @param pElementType
     *            the element type
     * @param pContainerType
     *            the container type
     * @param pProductName
     *            the product name
     * @param pProductName
     *            the destination product name for links
     * @param pPackageNumber
     *            the package number
     * @return the file name
     */
    private String generateFileName(final MigrationElementType pElementType,
            final String pContainerType, final String pProductName,
            final String pDestinationProductName, final int pPackageNumber) {
        String lFileName = getOptions().getProcessName();

        lFileName += "_" + pElementType.getName();

        if (pContainerType != null) {
            lFileName += "_" + pContainerType;
        }

        // Append the name of the product to the filename. 
        // The product name may be obfuscated : determine the correct name to use.
        ReadProperties lReadProperties = ReadProperties.getInstance();
        ExportationData lExportationData = ExportationData.getInstance();

        if (pProductName != null
                && lExportationData.getProductNames().get(pProductName) != null
                && lReadProperties.isObfProducts()) {
            lFileName +=
                    "_" + lExportationData.getProductNames().get(pProductName);
        }
        else {
            if (pProductName != null) {
                lFileName += "_" + pProductName;
            }
        }

        if (pDestinationProductName != null
                && lExportationData.getProductNames().get(
                        pDestinationProductName) != null
                && lReadProperties.isObfProducts()) {
            lFileName +=
                    "_"
                            + lExportationData.getProductNames().get(
                                    pDestinationProductName);
        }
        else {
            if (pDestinationProductName != null) {
                lFileName += "_" + pDestinationProductName;
            }
        }

        String lPackageNumber = String.valueOf(pPackageNumber);
        while (lPackageNumber.length() <= PACKAGE_NUMBER_SIZE) {
            lPackageNumber = "0" + lPackageNumber;
        }
        lFileName += "_" + lPackageNumber;

        lFileName += ".xml";

        return lFileName;
    }

    /**
     * Get all elements id of a specified type.
     * 
     * @param pElementType
     *            the element type
     * @param pContainerTypeName
     *            the container type
     * @param pProductsName
     *            the products name
     * @return the elements id.
     */
    private Iterator<String> getElementsId(
            final MigrationElementType pElementType,
            final String pContainerTypeName, final List<String> pProductsName) {

        List<String> lElementIds;

        switch (pElementType) {
            case DICTIONARY:
                lElementIds =
                        getExportService().getDictionaryCategoriesId(
                                getOptions().getRoleToken());
                break;
            case ENVIRONMENT:
                lElementIds =
                        getExportService().getEnvironmentsId(
                                getOptions().getRoleToken());
                break;
            case USER:
                lElementIds =
                        getExportService().getUsersId(
                                getOptions().getRoleToken());
                break;
            case FILTER:
                lElementIds =
                        getExportService().getFiltersId(
                                getOptions().getRoleToken(),
                                new ArrayList<String>());
                break;
            case PRODUCT:
                lElementIds =
                        getExportService().getProductsId(
                                getOptions().getRoleToken(), pProductsName,
                                Arrays.asList(pContainerTypeName));
                break;
            case PRODUCT_LINK:
                lElementIds =
                        getExportService().getProductLinksId(
                                getOptions().getRoleToken(), pProductsName,
                                Arrays.asList(pContainerTypeName));
                break;
            case SHEET:
                lElementIds =
                        getExportService().getSheetsId(
                                getOptions().getRoleToken(), pProductsName,
                                Arrays.asList(pContainerTypeName));
                break;
            case SHEET_LINK:
                lElementIds =
                        getExportService().getSheetLinksId(
                                getOptions().getRoleToken(),
                                pProductsName.get(0), pProductsName.get(1),
                                Arrays.asList(pContainerTypeName));
                break;
            default:
                lElementIds = new ArrayList<String>();
                break;
        }

        return lElementIds.iterator();
    }

    /**
     * Get the link types
     */
    private void getExportableLinkTypes() {
        if (exportableProductLinkTypes == null
                || exportableSheetLinkTypes == null) {

            exportableSheetLinkTypes = new ArrayList<String>();
            exportableProductLinkTypes = new ArrayList<String>();

            List<CacheableFieldsContainer> lLinkTypes =
                    ServiceLocator.instance().getFieldsContainerService().getFieldsContainer(
                            getOptions().getRoleToken(), LinkType.class, 0);

            for (CacheableFieldsContainer lLinkType : lLinkTypes) {
                CacheableFieldsContainer lOriginType =
                        ServiceLocator.instance().getFieldsService().getCacheableFieldsContainer(
                                getOptions().getRoleToken(),
                                ((CacheableLinkType) lLinkType).getOriginTypeId(),
                                CacheProperties.IMMUTABLE);
                if (lOriginType instanceof CacheableSheetType) {
                    exportableSheetLinkTypes.add(lLinkType.getName());
                }
                else if (lOriginType instanceof CacheableProductType) {
                    exportableProductLinkTypes.add(lLinkType.getName());
                }
            }
        }
    }

    /**
     * Get the product link types
     * 
     * @return the product link types
     */
    private List<String> getExportableProductLinkTypes() {
        getExportableLinkTypes();
        return exportableProductLinkTypes;
    }

    /**
     * Get the products name
     * 
     * @return the products name
     */
    private List<String> getExportableProductsName() {
        if (exportableProductsName == null) {

            exportableProductsName =
                    getOptions().getExportLimitedProductsName();

            if (exportableProductsName.isEmpty()) {

                List<String> lProductsId =
                        getExportService().getProductsId(
                                getOptions().getRoleToken(),
                                new ArrayList<String>(),
                                new ArrayList<String>());

                for (String lProductId : lProductsId) {
                    CacheableProduct lCacheableProduct =
                            ServiceLocator.instance().getProductService().getCacheableProduct(
                                    getOptions().getRoleToken(), lProductId,
                                    CacheProperties.IMMUTABLE);
                    exportableProductsName.add(lCacheableProduct.getProductName());
                }
            }
        }

        return exportableProductsName;
    }

    /**
     * Get the product types
     * 
     * @return the product types
     */
    private List<String> getExportableProductTypes() {
        if (exportableProductTypes == null) {

            exportableProductTypes = new ArrayList<String>();

            if (exportableProductTypes.isEmpty()) {

                List<CacheableFieldsContainer> lProductTypes =
                        ServiceLocator.instance().getFieldsContainerService().getFieldsContainer(
                                getOptions().getRoleToken(), ProductType.class,
                                0);

                for (CacheableFieldsContainer lProductType : lProductTypes) {
                    exportableProductTypes.add(lProductType.getName());
                }
            }
        }

        return exportableProductTypes;
    }

    /**
     * Get the sheet link types
     * 
     * @return the sheet link types
     */
    private List<String> getExportableSheetLinkTypes() {
        getExportableLinkTypes();
        return exportableSheetLinkTypes;
    }

    /**
     * Get the sheet types
     * 
     * @return the sheet types
     */
    private List<String> getExportableSheetTypes() {
        if (exportableSheetTypes == null) {

            exportableSheetTypes = getOptions().getExportLimitedSheetTypes();

            if (exportableSheetTypes.isEmpty()) {

                List<CacheableFieldsContainer> lSheetTypes =
                        ServiceLocator.instance().getFieldsContainerService().getFieldsContainer(
                                getOptions().getRoleToken(), SheetType.class, 0);

                for (CacheableFieldsContainer lSheetType : lSheetTypes) {
                    exportableSheetTypes.add(lSheetType.getName());
                }
            }
        }

        return exportableSheetTypes;
    }

    /**
     * get export service
     * 
     * @return the export service
     */
    private ExportService getExportService() {
        if (exportService == null) {
            exportService = ServiceLocator.instance().getExportService();
        }
        return exportService;
    }

    /**
     * Get the name of the products accessible from a specific one through a
     * sheet link of a specific type from destination to origin.
     * 
     * @param pSheetLinkTypeName
     *            the sheet link type name
     * @param pOriginProductName
     *            the origin product name
     * @return the sheet link destination products name
     */
    private List<String> getSheetLinkDestinationProductsName(
            String pSheetLinkTypeName, String pOriginProductName) {
        return getExportService().getSheetLinkDestinationProductsName(
                getOptions().getRoleToken(), pSheetLinkTypeName,
                pOriginProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationProducer#isErrorStop()
     */
    @Override
    protected boolean isErrorStop() {
        return getOptions().isExportErrorStop();
    }

    private void createMappingFile(String pFileName) {
        try {
            // create a csv file with semicolumn separator
            FileWriter lCSVWriter = new FileWriter(pFileName);
            final HashMap<String, String> lProductNames = ExportationData.getInstance().getProductNames();
            final char lStringQuote = '"';
            final char lStringSemiColonn = ';';
            final char lLineReturn = '\n';
            for (Entry<String, String> lEntry : lProductNames.entrySet()) {
                StringBuffer lStringBuffer = new StringBuffer();  
                lStringBuffer.append(lStringQuote);
                lStringBuffer.append(lEntry.getKey());
                lStringBuffer.append(lStringQuote);               
                lStringBuffer.append(lStringSemiColonn);
                lStringBuffer.append(lStringQuote);
                lStringBuffer.append(lEntry.getValue());
                lStringBuffer.append(lStringQuote);            
                lStringBuffer.append(lLineReturn);
                
                lCSVWriter.write(lStringBuffer.toString());
            }
            lCSVWriter.close();
        }
        catch (IOException e) {
            throw new GDMException("I/O error...");
        }
    }
}

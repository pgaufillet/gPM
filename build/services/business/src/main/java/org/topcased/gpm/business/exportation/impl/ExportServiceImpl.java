/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.exception.SystemException;
import org.topcased.gpm.business.exportation.ExportExecutionReport;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.domain.link.LinkDao;

/**
 * gPM instance export service.
 * 
 * @author llatil
 */
public class ExportServiceImpl extends ServiceImplBase implements ExportService {
    /** The Constant DEFAULT_ENCODING. */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /** The Constant XML_HEADER. */
    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"{0}\"?>\n\n"
                    + "<gpm xmlns=\"http://www.airbus.com/topcased/gPM\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xsi:schemaLocation=\"http://www.airbus.com/topcased/gPM\">\n";

    /** The Constant XML_HEADER_COMMENT. */
    private static final String XML_HEADER_COMMENT =
            "<!--\n" + "   Instance: ''{0}'' \n" + "   Exported on: {1}\n"
                    + "-->\n";

    /** The Constant XML_FORMAT_VERSION. */
    private static final String XML_FORMAT_VERSION = "<version>{0}</version>\n";

    /** The Constant XML_FOOTER. */
    private static final String XML_FOOTER = "</gpm>";

    private ProductExportManager productExportManager;

    private ProductLinkExportManager productLinkExportManager;

    private SheetExportManager sheetExportManager;

    private SheetLinkExportManager sheetLinkExportManager;

    private CategoryExportManager categoryExportManager;

    private EnvironmentExportManager environmentExportManager;

    private FilterExportManager filterExportManager;

    private UserExportManager userExportManager;

    private UserRoleExportManager userRoleExportManager;

    private LinkDao linkDao;

    /**
     * Setter for Spring injection.
     * 
     * @param pProductExportManager
     *            The manager to inject.
     */
    public void setProductExportManager(
            final ProductExportManager pProductExportManager) {
        productExportManager = pProductExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pProductLinkExportManager
     *            The manager to inject.
     */
    public void setProductLinkExportManager(
            final ProductLinkExportManager pProductLinkExportManager) {
        productLinkExportManager = pProductLinkExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pSheetExportManager
     *            The manager to inject.
     */
    public void setSheetExportManager(
            final SheetExportManager pSheetExportManager) {
        sheetExportManager = pSheetExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pSheetLinkExportManager
     *            The manager to inject.
     */
    public void setSheetLinkExportManager(
            final SheetLinkExportManager pSheetLinkExportManager) {
        sheetLinkExportManager = pSheetLinkExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pCategoryExportManager
     *            The manager to inject.
     */
    public void setCategoryExportManager(
            final CategoryExportManager pCategoryExportManager) {
        categoryExportManager = pCategoryExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pEnvironmentExportManager
     *            The manager to inject.
     */
    public void setEnvironmentExportManager(
            final EnvironmentExportManager pEnvironmentExportManager) {
        environmentExportManager = pEnvironmentExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pFilterExportManager
     *            The manager to inject.
     */
    public void setFilterExportManager(
            final FilterExportManager pFilterExportManager) {
        filterExportManager = pFilterExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pUserExportManager
     *            The manager to inject.
     */
    public void setUserExportManager(final UserExportManager pUserExportManager) {
        userExportManager = pUserExportManager;
    }

    /**
     * Setter for Spring injection.
     * 
     * @param pUserRoleExportManager
     *            The manager to inject.
     */
    public void setUserRoleExportManager(
            final UserRoleExportManager pUserRoleExportManager) {
        userRoleExportManager = pUserRoleExportManager;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pLinkDao
     *            The DAO.
     */
    public void setLinkDao(final LinkDao pLinkDao) {
        linkDao = pLinkDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#exportData(java.lang.String,
     *      java.io.OutputStream,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    public ExportExecutionReport exportData(final String pRoleToken,
            final OutputStream pOutputStream, final ExportProperties pProperties)
        throws SystemException {
        final String lInstanceName =
                getAuthService().getProcessNameFromToken(pRoleToken);

        try {
            final OutputStreamWriter lContentStreamWriter =
                    new OutputStreamWriter(pOutputStream, DEFAULT_ENCODING);

            try {
                // Export header
                lContentStreamWriter.write(MessageFormat.format(XML_HEADER,
                        DEFAULT_ENCODING));
                lContentStreamWriter.write(MessageFormat.format(
                        XML_HEADER_COMMENT, lInstanceName,
                        DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                DateFormat.SHORT).format(new Date())));
                lContentStreamWriter.write(MessageFormat.format(
                        XML_FORMAT_VERSION, XMLConverter.INSTANCE_FMT_VERSION));

                // Export data
                userExportManager.exportData(pRoleToken, lContentStreamWriter,
                        pProperties);
                // Category and environment are on the same tag
                if (!pProperties.getCategoriesFlag().equals(ExportFlag.NO)
                        || !pProperties.getEnvironmentsFlag().equals(
                                ExportFlag.NO)) {
                    lContentStreamWriter.write("<dictionary>\n");
                    categoryExportManager.exportData(pRoleToken,
                            lContentStreamWriter, pProperties);
                    environmentExportManager.exportData(pRoleToken,
                            lContentStreamWriter, pProperties);
                    lContentStreamWriter.write("</dictionary>\n");
                }
                productExportManager.exportData(pRoleToken,
                        lContentStreamWriter, pProperties);
                productLinkExportManager.exportData(pRoleToken,
                        lContentStreamWriter, pProperties);
                userRoleExportManager.exportData(pRoleToken,
                        lContentStreamWriter, pProperties);
                filterExportManager.exportData(pRoleToken,
                        lContentStreamWriter, pProperties);
                sheetExportManager.exportData(pRoleToken, lContentStreamWriter,
                        pProperties);
                sheetLinkExportManager.exportData(pRoleToken,
                        lContentStreamWriter, pProperties);

                // Export footer
                lContentStreamWriter.write(XML_FOOTER);
            }
            catch (IOException e) {
                throw new SystemException("Error writing to output stream.", e);
            }
            finally {
                if (lContentStreamWriter != null) {
                    lContentStreamWriter.close();
                }
            }
        }
        catch (IOException e) {
            throw new SystemException("Error closing the output stream.", e);
        }

        return new ExportExecutionReport();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getDictionaryCategoriesId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getDictionaryCategoriesId(String pRoleToken) {
        return IteratorUtils.toList(categoryExportManager.getAllElementsId(
                pRoleToken, new ExportProperties()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getEnvironmentsId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getEnvironmentsId(String pRoleToken) {
        return IteratorUtils.toList(environmentExportManager.getAllElementsId(
                pRoleToken, new ExportProperties()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getFiltersId(java.lang.String,
     *      java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public List<String> getFiltersId(String pRoleToken,
            Collection<String> pProductsName) {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setLimitedProductsName(pProductsName.toArray(new String[pProductsName.size()]));
        return IteratorUtils.toList(filterExportManager.getAllElementsId(
                pRoleToken, lExportProperties));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getProductLinksId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public List<String> getProductLinksId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName) {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setLimitedProductsName(pProductsName.toArray(new String[pProductsName.size()]));
        lExportProperties.setLimitedTypesName(pTypesName.toArray(new String[pTypesName.size()]));
        return IteratorUtils.toList(productLinkExportManager.getAllElementsId(
                pRoleToken, lExportProperties));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getProductsId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public List<String> getProductsId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName) {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setLimitedProductsName(pProductsName.toArray(new String[pProductsName.size()]));
        lExportProperties.setLimitedTypesName(pTypesName.toArray(new String[pTypesName.size()]));
        return IteratorUtils.toList(productExportManager.getAllElementsId(
                pRoleToken, lExportProperties));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getSheetLinkDestinationProductsName(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public List<String> getSheetLinkDestinationProductsName(String pRoleToken,
            String pSheetLinkTypeName, String pOriginProductName) {
        return linkDao.getSheetLinkDestProducts(
                getAuthService().getProcessName(pRoleToken),
                pSheetLinkTypeName, pOriginProductName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getSheetLinksId(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSheetLinksId(String pRoleToken,
            String pOriginProductName, String pDestinationProductName,
            Collection<String> pTypesName) {
        return IteratorUtils.toList(linkDao.sheetLinksIterator(
                getAuthService().getProcessName(pRoleToken),
                pOriginProductName, pDestinationProductName, pTypesName));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getSheetsId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSheetsId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName) {
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setLimitedProductsName(pProductsName.toArray(new String[pProductsName.size()]));
        lExportProperties.setLimitedTypesName(pTypesName.toArray(new String[pTypesName.size()]));
        return IteratorUtils.toList(sheetExportManager.getAllElementsId(
                pRoleToken, lExportProperties));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.service.ExportService#getUsersId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getUsersId(String pRoleToken) {
        return IteratorUtils.toList(userExportManager.getAllElementsId(
                pRoleToken, new ExportProperties()));
    }
}
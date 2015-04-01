/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.AddProductLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.AddSheetLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.DeleteProductLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.DeleteSheetLinkPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.EditCreateProductLinkFilterPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.EditCreateSheetLinkFilterPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.EditDeleteProductLinkFilterPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.EditDeleteSheetLinkFilterPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.EditInitializeSheetFilterPopupCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.InitializeSheetPopupCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.filter.ExecutePopupFilterResult;

import com.google.inject.Inject;

/**
 * The presenter for the FilterView.
 * 
 * @author tpanuel
 */
public class FilterPopupPresenter extends PopupPresenter<FilterPopupDisplay> {
    private final AddSheetLinkPopupCommand addSheetLinkCommand;

    private final DeleteSheetLinkPopupCommand deleteSheetLinkCommand;

    private final InitializeSheetPopupCommand initSheetLinkCommand;

    private final AddProductLinkPopupCommand addProductLinkCommand;

    private final DeleteProductLinkPopupCommand deleteProductLinkCommand;

    private final EditCreateSheetLinkFilterPopupCommand editCreateSheetLinkFilterPopupCommand;

    private final EditDeleteSheetLinkFilterPopupCommand editDeleteSheetLinkFilterPopupCommand;

    private final EditCreateProductLinkFilterPopupCommand editCreateProductLinkFilterPopupCommand;

    private final EditDeleteProductLinkFilterPopupCommand editDeleteProductLinkFilterPopupCommand;

    private final EditInitializeSheetFilterPopupCommand editInitializeSheetFilterPopupCommand;

    private String typeName;

    private String filterId;

    /**
     * Create a presenter for the FilterView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pAddSheetLinkCommand
     *            Add sheet link command
     * @param pDeleteSheetLinkCommand
     *            Delete sheet link command
     * @param pInitSheetLinkCommand
     *            Initialize sheet link command
     * @param pAddProductLinkCommand
     *            Add product link command
     * @param pDeleteProductLinkCommand
     *            Delete product link command
     * @param pEditCreateSheetLinkFilterPopupCommand
     *            Edit sheet link creation filter command
     * @param pEditDeleteSheetLinkFilterPopupCommand
     *            Edit sheet link deletion filter command
     * @param pEditCreateProductLinkFilterPopupCommand
     *            Edit product link creation filter command
     * @param pEditDeleteProductLinkFilterPopupCommand
     *            Edit product link deletion filter command
     * @param pEditInitializeSheetFilterPopupCommand
     *            Edit sheet initialization filter command
     */
    @Inject
    public FilterPopupPresenter(
            final FilterPopupDisplay pDisplay,
            final EventBus pEventBus,
            final AddSheetLinkPopupCommand pAddSheetLinkCommand,
            final DeleteSheetLinkPopupCommand pDeleteSheetLinkCommand,
            final InitializeSheetPopupCommand pInitSheetLinkCommand,
            final AddProductLinkPopupCommand pAddProductLinkCommand,
            final DeleteProductLinkPopupCommand pDeleteProductLinkCommand,
            final EditCreateSheetLinkFilterPopupCommand pEditCreateSheetLinkFilterPopupCommand,
            final EditDeleteSheetLinkFilterPopupCommand pEditDeleteSheetLinkFilterPopupCommand,
            final EditCreateProductLinkFilterPopupCommand pEditCreateProductLinkFilterPopupCommand,
            final EditDeleteProductLinkFilterPopupCommand pEditDeleteProductLinkFilterPopupCommand,
            final EditInitializeSheetFilterPopupCommand pEditInitializeSheetFilterPopupCommand) {
        super(pDisplay, pEventBus);
        addSheetLinkCommand = pAddSheetLinkCommand;
        deleteSheetLinkCommand = pDeleteSheetLinkCommand;
        initSheetLinkCommand = pInitSheetLinkCommand;
        addProductLinkCommand = pAddProductLinkCommand;
        deleteProductLinkCommand = pDeleteProductLinkCommand;

        editCreateSheetLinkFilterPopupCommand =
                pEditCreateSheetLinkFilterPopupCommand;
        editDeleteSheetLinkFilterPopupCommand =
                pEditDeleteSheetLinkFilterPopupCommand;
        editCreateProductLinkFilterPopupCommand =
                pEditCreateProductLinkFilterPopupCommand;
        editDeleteProductLinkFilterPopupCommand =
                pEditDeleteProductLinkFilterPopupCommand;
        editInitializeSheetFilterPopupCommand =
                pEditInitializeSheetFilterPopupCommand;
    }

    /**
     * Set the type of popup filter.
     * 
     * @param pType
     *            The type of popup filter.
     */
    public void setPopupFilterType(final FilterPopupType pType) {
        switch (pType) {
            case INIT_SHEET:
                getDisplay().setHeaderText(
                        CONSTANTS.filterPopupInitializeSheetTitle());
                getDisplay().setMultivalued(false);
                getDisplay().setValidationButtonInfo(
                        CONSTANTS.filterPopupInitializeSheetButton(),
                        initSheetLinkCommand);
                getDisplay().setUpdateFilterButtonInfo(
                        editInitializeSheetFilterPopupCommand);
                break;
            case LINK_SHEET:
                getDisplay().setHeaderText(
                        CONSTANTS.filterPopupLinkSheetTitle());
                getDisplay().setMultivalued(true);
                getDisplay().setValidationButtonInfo(
                        CONSTANTS.filterPopupLinkButton(), addSheetLinkCommand);
                getDisplay().setUpdateFilterButtonInfo(
                        editCreateSheetLinkFilterPopupCommand);
                break;
            case UNLINK_SHEET:
                getDisplay().setHeaderText(
                        CONSTANTS.filterPopupUnlinkSheetTitle());
                getDisplay().setMultivalued(true);
                getDisplay().setValidationButtonInfo(
                        CONSTANTS.filterPopupUnlinkButton(),
                        deleteSheetLinkCommand);
                getDisplay().setUpdateFilterButtonInfo(
                        editDeleteSheetLinkFilterPopupCommand);
                break;
            case LINK_PRODUCT:
                getDisplay().setHeaderText(
                        CONSTANTS.filterPopupLinkProductTitle());
                getDisplay().setMultivalued(true);
                getDisplay().setValidationButtonInfo(
                        CONSTANTS.filterPopupLinkButton(),
                        addProductLinkCommand);
                getDisplay().setUpdateFilterButtonInfo(
                        editCreateProductLinkFilterPopupCommand);
                break;
            case UNLINK_PRODUCT:
                getDisplay().setHeaderText(
                        CONSTANTS.filterPopupUnlinkProductTitle());
                getDisplay().setMultivalued(true);
                getDisplay().setValidationButtonInfo(
                        CONSTANTS.filterPopupUnlinkButton(),
                        deleteProductLinkCommand);
                getDisplay().setUpdateFilterButtonInfo(
                        editDeleteProductLinkFilterPopupCommand);
                break;
            default:
                // Nothing to do
        }
    }

    /**
     * Set the filter result.
     * 
     * @param pResult
     *            The filter result.
     */
    public void setFilterResult(final ExecutePopupFilterResult pResult) {
        getDisplay().initTable();
        for (final String lColumnName : pResult.getColumnNames()) {
            getDisplay().addValuesColumn(lColumnName);
        }
        getDisplay().setData(pResult.getResultValues());
        typeName = pResult.getTypeName();
        filterId = pResult.getFilterId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_FILTER_POPUP;
    }

    /**
     * Get the selected element ids - if the table is displayed on multi valued
     * mode.
     * 
     * @return The selected element ids.
     */
    public List<String> getSelectedElementIds() {
        return getDisplay().getSelectedElementIds();
    }

    /**
     * Get the selected element id - if the table is displayed on mono valued
     * mode.
     * 
     * @return The selected element id.
     */
    public String getSelectedElementId() {
        final List<String> lSelectedElements =
                getDisplay().getSelectedElementIds();

        if (lSelectedElements == null || lSelectedElements.isEmpty()) {
            return null;
        }
        else {
            return lSelectedElements.get(0);
        }
    }

    /**
     * Get the type name.
     * 
     * @return The type name.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * set the type name.
     * 
     * @param pTypeName
     *            the type name to set
     */
    public void setTypeName(String pTypeName) {
        typeName = pTypeName;
    }

    /**
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }
}
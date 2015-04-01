/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.listing;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.component.client.button.GpmToolTipImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmIFrameLayoutPanel;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.popup.GpmToolTipPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.table.GpmTable;
import org.topcased.gpm.ui.component.client.table.GpmTablePager;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;
import org.topcased.gpm.ui.component.client.util.IToolTipCreationHandler;
import org.topcased.gpm.ui.facade.shared.filter.result.UiFilterExecutionReport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for a listing.
 * 
 * @author tpanuel
 */
public class ListingView extends GpmLayoutPanelWithMenu implements
        ListingDisplay {
    private static final String STYLE_REPORT_CONTENT =
            ComponentResources.INSTANCE.css().gpmExecutionReportContent();

    private static final String STYLE_REPORT_TITLE =
            ComponentResources.INSTANCE.css().gpmExecutionReportTitle();

    private static final String STYLE_REPORT_TAB1 =
            ComponentResources.INSTANCE.css().gpmExecutionReportTabLevel1();

    private static final String STYLE_REPORT_TAB2 =
            ComponentResources.INSTANCE.css().gpmExecutionReportTabLevel2();

    private final GpmMenuTitle filterTitle;

    private final GpmTablePager pager;

    private GpmTable table;

    private GpmToolTipImageButton executionReport;

    /**
     * Create the listing.
     */
    public ListingView() {
        super(true);
        filterTitle = new GpmMenuTitle(true);
        pager = new GpmTablePager();
        getMenu().addTitle(filterTitle);
        setContent(new LayoutPanel());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(final GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setFilterInfo(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setFilterInfo(final String pFilterName,
            final String pFilterDescription) {
        filterTitle.setHTML(pFilterName);
        filterTitle.setTitle(pFilterDescription);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#initTable()
     */
    @Override
    public void initTable() {
        final VerticalPanel lPanel = new VerticalPanel();
        lPanel.setWidth("100%");
        table = new GpmTable(true);
        lPanel.add(table);
        lPanel.add(pager);
        setContent(new ScrollPanel(lPanel));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#addValuesColumn(java.lang.String)
     */
    @Override
    public void addValuesColumn(final String pColumnName) {
        table.addValuesColumn(pColumnName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setData(java.util.List)
     */
    @Override
    public void setData(final List<FilterResult> pData) {
        pager.initialize(table, pData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setVisuHandler(org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler)
     */
    @Override
    public void setVisuHandler(final GpmBasicActionHandler<String> pHandler) {
        table.addHandleredColumn(true, pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setEditHandler(org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler)
     */
    @Override
    public void setEditHandler(final GpmBasicActionHandler<String> pHandler) {
        table.addHandleredColumn(false, pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setIFrameURL(java.lang.String)
     */
    @Override
    public void setIFrameURL(final String pURL) {
        setContent(new GpmIFrameLayoutPanel(pURL));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#getSelectedElementIds()
     */
    @Override
    public List<String> getSelectedElementIds() {
        if (table == null) {
            return new ArrayList<String>();
        }
        else {
            return table.getSelectedIds();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    @Override
    public void doMaximize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    @Override
    public void doMinimize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void doRestore() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void setContent(final Widget pPanel) {
        super.setContent(pPanel);
        pPanel.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTabLayoutPanelContent());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.listing.ListingDisplay#setFilterExecutionReport(org.topcased.gpm.ui.facade.shared.filter.result.UiFilterExecutionReport)
     */
    @Override
    public void setFilterExecutionReport(UiFilterExecutionReport pReport) {
        if (executionReport == null) { // Initialize
            executionReport =
                    new GpmToolTipImageButton(
                            ComponentResources.INSTANCE.images().filterReport());
            getMenu().addTitleButton(executionReport);
            executionReport.setTitle(CONSTANTS.filterExecutionReport());
        }
        executionReport.setDynamicWidgetCreationHandler(buildExecutionReportPopupHandler(pReport));
    }

    /**
     * Return a String containing the strings in argument joined with commas
     * 
     * @param pStringCollection
     *            the strings to join
     * @return the String with the joined strings
     */
    private String join(Iterable<String> pStringCollection) {
        String lBuildString = "";

        // Non executable products
        for (String lString : pStringCollection) {
            lBuildString += lString + ", ";
        }
        if (lBuildString.equals("")) {
            // Iterator was empty
            return lBuildString;
        }
        else {
            // Remove the last ", " and return
            return lBuildString.substring(0, lBuildString.length() - 2);
        }
    }

    /**
     * Build a handler for creating the popup for the execution report
     * 
     * @param pUiFilterExecutionReport
     *            the execution report to display
     * @return the handler for popup creation
     */
    private IToolTipCreationHandler buildExecutionReportPopupHandler(
            UiFilterExecutionReport pUiFilterExecutionReport) {
        final FlowPanel lPane = new FlowPanel();
        Label lLabel;

        // Non executable products
        if (!pUiFilterExecutionReport.getNonExecutableProducts().isEmpty()) {
            lLabel =
                    new Label(
                            MESSAGES.filterExecutionReportNonExecutableProductsTitle());
            lLabel.setStyleName(STYLE_REPORT_TITLE);
            lPane.add(lLabel);

            lLabel =
                    new Label(
                            join(pUiFilterExecutionReport.getNonExecutableProducts()));
            lLabel.setStyleName(STYLE_REPORT_TAB1);
            lPane.add(lLabel);
        }

        // Executable products with affected roles
        if (!pUiFilterExecutionReport.getExecutableProducts().isEmpty()) {
            lLabel =
                    new Label(
                            MESSAGES.filterExecutionReportExecutableProductsTitle());
            lLabel.setStyleName(STYLE_REPORT_TITLE);
            lPane.add(lLabel);
            for (String lRole : pUiFilterExecutionReport.getExecutableProducts().keySet()) {
                final String lText =
                        MESSAGES.filterExecutionReportExecutableProductsText(lRole);
                String lJoined =
                        join(pUiFilterExecutionReport.getExecutableProducts().get(
                                lRole));
                lLabel = new Label(lText + lJoined);
                lLabel.setStyleName(STYLE_REPORT_TAB1);
                lPane.add(lLabel);
            }
        }

        // Additional constraints
        HashMap<String, HashMap<String, HashSet<String>>> lConstr =
                pUiFilterExecutionReport.getAdditionalConstraints();
        if (!lConstr.isEmpty()) {
            lLabel =
                    new Label(
                            MESSAGES.filterExecutionReportAddConstraintsTitle());
            lLabel.addStyleName(ComponentResources.INSTANCE.css().gpmExecutionReportTitle());
            lPane.add(lLabel);
            for (String lType : lConstr.keySet()) {
                lLabel =
                        new Label(
                                MESSAGES.filterExecutionReportAddConstraintsTypeText(lType));
                lLabel.setStyleName(STYLE_REPORT_TAB1);
                lPane.add(lLabel);
                for (String lRole : lConstr.get(lType).keySet()) {
                    lLabel =
                            new Label(
                                    MESSAGES.filterExecutionReportAddConstraintsRoleText(lRole)
                                            + join(lConstr.get(lType).get(lRole)));
                    lLabel.setStyleName(STYLE_REPORT_TAB2);
                    lPane.add(lLabel);
                }
            }
        }

        return new IToolTipCreationHandler() {
            @Override
            public void fillPopupContent(GpmToolTipPanel pToolTip) {
                pToolTip.setHeader(new Label(CONSTANTS.filterExecutionReport()));
                pToolTip.addContentStyleName(STYLE_REPORT_CONTENT);
                pToolTip.setContent(lPane);
            }
        };
    }
}
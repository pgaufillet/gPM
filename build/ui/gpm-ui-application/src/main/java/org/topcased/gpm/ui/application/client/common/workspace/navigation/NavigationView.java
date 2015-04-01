/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.navigation;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.tree.TreeFilterItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.field.GpmDropDownListBoxWidget;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.layout.stack.GpmStackLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for the tree and tab filter navigation.
 * 
 * @author tpanuel
 */
public class NavigationView extends GpmStackLayoutPanel implements
        NavigationDisplay {
    /**
     * Empty drop down list entry.
     */
    public final static String EMPTY_ENTRY = "&nbsp;";

    private final GpmLayoutPanelWithMenu tablePanel;

    private final GpmLayoutPanelWithMenu treePanel;

    /** Panel containing the filter list in Tree filters or nothing if empty */
    private final FlowPanel filterSelectorPanel;

    private final FlowPanel tableFilters;

    private FlexTable processFilters;

    private FlexTable productFilters;

    private FlexTable userFilters;

    private boolean processOddEven;

    private boolean productOddEven;

    private boolean userOddEven;

    private final GpmDropDownListBoxWidget<String> treeFilterList;

    private final GpmDynamicTree<TreeFilterItem> tree;

    /**
     * Create the view for the tree and tab filter navigation.
     */
    public NavigationView() {
        tablePanel = new GpmLayoutPanelWithMenu(false);
        treePanel = new GpmLayoutPanelWithMenu(false);
        tree = new GpmDynamicTree<TreeFilterItem>();

        processOddEven = true;
        productOddEven = true;
        userOddEven = true;

        treeFilterList = new GpmDropDownListBoxWidget<String>();
        InlineLabel lLabel =
                new InlineLabel(CONSTANTS.navigationTreeTabSelection());
        lLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
        treeFilterList.addItem(lLabel.getElement().getString(), EMPTY_ENTRY,
                EMPTY_ENTRY);
        treeFilterList.addStyleName(INSTANCE.css().gpmMenuListBox());
        treeFilterList.setTitle(CONSTANTS.navigationTreeTabSelection());

        // TABLE
        tableFilters = new FlowPanel();
        tableFilters.setStylePrimaryName(INSTANCE.css().gpmValuesContainerPanel());
        tablePanel.setContent(new ScrollPanel(tableFilters));
        addPanel(CONSTANTS.navigationFilterTabTitle(), tablePanel, true);

        // TREE
        filterSelectorPanel = new FlowPanel();
        treePanel.getMenu().addTitle(filterSelectorPanel);
        treePanel.setContent(new ScrollPanel(tree));
        addPanel(CONSTANTS.navigationTreeTabTitle(), treePanel, false);
    }

    private void initDisclosurePanel(final String pHeaderText,
            final Widget pContent) {
        final GpmDisclosurePanel lDisclosurePanel = new GpmDisclosurePanel();

        pContent.setStylePrimaryName(INSTANCE.css().gpmFilterTable());
        lDisclosurePanel.setButtonText(pHeaderText);
        lDisclosurePanel.setContent(pContent);
        lDisclosurePanel.open();
        tableFilters.add(lDisclosurePanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#setTableToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setTableToolBar(GpmToolBar pTableToolBar) {
        tablePanel.getMenu().setToolBar(pTableToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#setTreeToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    public void setTreeToolBar(GpmToolBar pTreeToolBar) {
        treePanel.getMenu().setToolBar(pTreeToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#addTableProcessFilter(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addTableProcessFilter(final String pFilterId,
            final String pFilterName, final String pFilterDescription,
            final ClickHandler pExecuteHandler,
            final ClickHandler pEditHandler, final ClickHandler pDeleteHandler) {
        if (processFilters == null) {
            processFilters = new FlexTable();
            initDisclosurePanel(CONSTANTS.navigationFilterTabProcessTitle(),
                    processFilters);
        }
        addTableFilter(processFilters, pFilterId, pFilterName,
                pFilterDescription, pExecuteHandler, pEditHandler,
                pDeleteHandler, processOddEven);
        processOddEven = !processOddEven;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#addTableProductFilter(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addTableProductFilter(final String pFilterId,
            final String pFilterName, final String pFilterDescription,
            final ClickHandler pExecuteHandler,
            final ClickHandler pEditHandler, final ClickHandler pDeleteHandler) {
        if (productFilters == null) {
            productFilters = new FlexTable();
            initDisclosurePanel(CONSTANTS.navigationFilterTabProductTitle(),
                    productFilters);
        }
        addTableFilter(productFilters, pFilterId, pFilterName,
                pFilterDescription, pExecuteHandler, pEditHandler,
                pDeleteHandler, productOddEven);
        productOddEven = !productOddEven;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#addTableUserFilter(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addTableUserFilter(final String pFilterId,
            final String pFilterName, final String pFilterDescription,
            final ClickHandler pExecuteHandler,
            final ClickHandler pEditHandler, final ClickHandler pDeleteHandler) {
        if (userFilters == null) {
            userFilters = new FlexTable();
            initDisclosurePanel(CONSTANTS.navigationFilterTabUserTitle(),
                    userFilters);
        }
        addTableFilter(userFilters, pFilterId, pFilterName, pFilterDescription,
                pExecuteHandler, pEditHandler, pDeleteHandler, userOddEven);
        userOddEven = !userOddEven;
    }

    /**
     * Add a filter to the table
     * 
     * @param pTable
     *            the table to add the filter into
     * @param pFilterId
     *            the filter id
     * @param pFilterName
     *            the filter name
     * @param pFilterDescription
     *            the filter description
     * @param pExecuteHandler
     *            the handler for filter execution
     * @param pEditHandler
     *            the handler for filter edition
     * @param pDeleteHandler
     *            the handler for filter deletion
     * @param pOddEven
     *            indicates if the row is odd or even (for CSS styles)
     */
    private void addTableFilter(final FlexTable pTable, final String pFilterId,
            final String pFilterName, final String pFilterDescription,
            final ClickHandler pExecuteHandler,
            final ClickHandler pEditHandler, final ClickHandler pDeleteHandler,
            final boolean pOddEven) {
        final int lRowNb = pTable.getRowCount();

        // The edit button
        if (pEditHandler != null) {
            final GpmImageButton lEditFilter =
                    new GpmImageButton(pFilterId,
                            INSTANCE.images().filterEdit());

            lEditFilter.setTitle(Ui18n.CONSTANTS.sheetFilterEditButton());
            lEditFilter.addClickHandler(pEditHandler);
            pTable.setWidget(lRowNb, 0, lEditFilter);
        }

        // The delete button
        if (pDeleteHandler != null) {
            final GpmImageButton lDeleteFilter =
                    new GpmImageButton(pFilterId,
                            INSTANCE.images().filterDelete());

            lDeleteFilter.setTitle(Ui18n.CONSTANTS.sheetFilterDeleteButton());
            lDeleteFilter.addClickHandler(pDeleteHandler);
            pTable.setWidget(lRowNb, 1, lDeleteFilter);
        }

        // The filter name
        HTML lFilterName;
        if (pExecuteHandler != null) {
            lFilterName = new GpmTextButton(pFilterId, pFilterName);
            lFilterName.addClickHandler(pExecuteHandler);
        }
        else {
            lFilterName = new HTML(pFilterName);
            lFilterName.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTextButton());
        }
        lFilterName.setTitle(pFilterDescription);
        pTable.setWidget(lRowNb, 2, lFilterName);

        if (pOddEven) {
            pTable.getRowFormatter().setStyleName(lRowNb,
                    INSTANCE.css().evenRow());
        }
        else {
            pTable.getRowFormatter().setStyleName(lRowNb,
                    INSTANCE.css().oddRow());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#addTreeFilter(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void addTreeFilter(final String pFilterId, final String pFilterName) {
        treeFilterList.addItem(pFilterName, pFilterName, pFilterId);
        if (treeFilterList.getItems().size() > 1) {
            filterSelectorPanel.add(treeFilterList);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#initTreeFilterList()
     */
    @Override
    public void initTreeFilterList() {
        treeFilterList.setValue(EMPTY_ENTRY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#clearFilters()
     */
    @Override
    public void clearFilters() {
        tableFilters.clear();
        processFilters = null;
        productFilters = null;
        userFilters = null;
        processOddEven = true;
        productOddEven = true;
        userOddEven = true;
        treeFilterList.reset();
        InlineLabel lLabel =
                new InlineLabel(CONSTANTS.navigationTreeTabSelection());
        lLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
        filterSelectorPanel.clear();
        treeFilterList.addItem(lLabel.getElement().getString(), EMPTY_ENTRY,
                EMPTY_ENTRY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#resetTreeFilter()
     */
    @Override
    public void resetTreeFilter() {
        tree.resetTree();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#setTreeFilterManager(org.topcased.gpm.ui.application.client.common.tree.TreeFilterManager)
     */
    @Override
    public void setTreeFilterManager(TreeFilterManager pManager) {
        tree.setDynamicTreeManager(pManager);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#setTreeFilterRootItems(java.util.List)
     */
    @Override
    public void setTreeFilterRootItems(final List<TreeFilterItem> pRootItems) {
        if (tree.getParent() instanceof ScrollPanel) {
            ScrollPanel lScrollPanel = (ScrollPanel) tree.getParent();
            lScrollPanel.ensureVisible(tree);
        }
        tree.setRootItems(pRootItems);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#addFilterChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    @Override
    public void addFilterChangeHandler(ValueChangeHandler<String> pHandler) {
        treeFilterList.addValueChangeHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay#getSelectedTreeFilter()
     */
    @Override
    public String getSelectedTreeFilter() {
        return treeFilterList.getValue();
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

    @Override
    public int getHeight() {
        // Not used
        return 0;
    }

    @Override
    public int getMinHeight() {
        return Math.max(tablePanel.getMinHeight(), treePanel.getMinHeight());
    }

    @Override
    public int getMinWidth() {
        return Math.max(tablePanel.getMinWidth(), treePanel.getMinWidth());
    }

    @Override
    public int getWidth() {
        // Not used
        return 0;
    }
}
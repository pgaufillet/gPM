/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmWorkspaceLayoutPanel.
 * 
 * @author jeballar
 */
public class GpmWorkspaceLayoutPanel extends GpmSplitLayoutPanel {
    private static final String STYLE_WORKSPACE_LAYOUT =
            ComponentResources.INSTANCE.css().gpmWorkspaceLayout();

    /**
     * CSS Style affected to the detail, listing and navigation panels (not to
     * content)
     */
    private static final String STYLE_WORKSPACE_LAYOUT_CHILD =
            ComponentResources.INSTANCE.css().gpmWorkspaceLayoutChild();

    /** Navigation panel */
    private LayoutPanel navigationPanel = new LayoutPanel();

    /** Listing panel */
    private LayoutPanel listingPanel = new LayoutPanel();

    /** Detail panel */
    private InternDetailResizeListener detailPanel =
            new InternDetailResizeListener();

    /** Detail panel Content */
    private IResizableLayoutPanel detailContent;

    /** Navigation panel Content */
    private IResizableLayoutPanel navigationContent;

    /** Listing panel Content */
    private IResizableLayoutPanel listingContent;

    /** Stores the min width of the detail panel */
    private int detailMinWidth = -1;

    /** Stores the min width of the listing panel */
    private int listingMinWidth = -1;

    /** Indicate detail panel status */
    private boolean detailMinimized = false;

    /** Indicate detail panel status */
    private boolean detailMaximized = false;

    /** Indicate listing panel minimize status */
    private boolean listingMinimized = false;

    /** Indicate listing panel maximized status */
    private boolean listingMaximized = false;

    /** Indicate navigation panel status */
    private boolean navigationMinimized = false;

    /**
     * Indicate the status (minimized/maximized panels) of the window. For
     * example, "mD" means minimized Detail.
     */
    private enum Status {
        /** Display all panels */
        FULL,
        /** Only Listing panel minimized */
        mL,
        /** Only Detail panel minimized */
        mD,
        /** Only Navigation panel minimized */
        mN,
        /** Detail panel maximized */
        MD,
        /** Listing panel maximized */
        ML,
        /** Detail panel hidden */
        HD,
        /** Detail panel hidden, Navigation panel minimized */
        HDmN
    };

    private Status status = Status.FULL;

    /**
     * Constructor
     */
    public GpmWorkspaceLayoutPanel() {
        super();
        insert(navigationPanel, Direction.WEST, 0, null);
        insert(listingPanel, Direction.NORTH, 0, null);
        insert(detailPanel, Direction.CENTER, 0, null);

        navigationPanel.setStyleName(STYLE_WORKSPACE_LAYOUT_CHILD);
        listingPanel.setStyleName(STYLE_WORKSPACE_LAYOUT_CHILD);
        detailPanel.setStyleName(STYLE_WORKSPACE_LAYOUT_CHILD);
        setStylePrimaryName(STYLE_WORKSPACE_LAYOUT);
    }

    /**
     * Set a new navigation panel
     * 
     * @param pNavigationPanel
     *            The new navigation panel
     * @param pSize
     *            The size to give to the navigation panel
     */
    public void setNavigationPanel(IResizableLayoutPanel pNavigationPanel,
            int pSize) {
        navigationContent = pNavigationPanel;
        navigationPanel.clear();
        navigationPanel.add(pNavigationPanel.asWidget());
        setWidgetMinSize(navigationPanel, pNavigationPanel.getMinWidth());
        getAssociatedSplitter(navigationPanel).setAssociatedWidgetSize(pSize);
    }

    /**
     * Set a new listing panel
     * 
     * @param pListingPanel
     *            The new listing panel
     * @param pSize
     *            The size to give to the listing panel
     */
    public void setListingPanel(IResizableLayoutPanel pListingPanel, int pSize) {
        listingContent = pListingPanel;
        listingPanel.clear();
        listingPanel.add(pListingPanel.asWidget());
        listingMinWidth = pListingPanel.getMinWidth();
        setWidgetMinSize(listingPanel, pListingPanel.getMinHeight());
        getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(pSize);
    }

    /**
     * Set a new detail panel
     * 
     * @param pDetailPanel
     *            The new detail panel
     */
    public void setDetailPanel(IResizableLayoutPanel pDetailPanel) {
        detailContent = pDetailPanel;
        detailPanel.setMinHeight(pDetailPanel.getMinHeight());
        detailMinWidth = pDetailPanel.getMinWidth();
        detailPanel.clear();
        detailPanel.add(pDetailPanel.asWidget());
    }

    /**
     * Change window organization to status in argument
     * 
     * @param pStatus
     *            Status to display
     */
    private void goToStatus(Status pStatus) {
        if (pStatus == Status.FULL) {
            restoreDetailContent();
            restoreListingContent();
            restoreNavigationContent();
            internalShowDetailPanel();
            setBounds();
            restorePanel(listingPanel);
            restorePanel(navigationPanel);
            enableSplitter(navigationPanel, true);
            enableSplitter(listingPanel, true);
        }
        else if (pStatus == Status.mD) {
            minimizeDetailContent();
            restoreListingContent();
            restoreNavigationContent();
            internalShowDetailPanel();
            setBounds();
            onResize();
            restorePanel(listingPanel);
            // Save panel status before maximizing listing for detail reducing
            getAssociatedSplitter(listingPanel).saveState();
            getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(
                    Integer.MAX_VALUE);
            restorePanel(navigationPanel);
            enableSplitter(navigationPanel, true);
            enableSplitter(listingPanel, false);
        }
        else if (pStatus == Status.mL) {
            restoreDetailContent();
            minimizeListingContent();
            restoreNavigationContent();
            internalShowDetailPanel();
            setBounds();
            minimizePanel(listingPanel);
            restorePanel(navigationPanel);
            enableSplitter(navigationPanel, true);
            enableSplitter(listingPanel, false);
        }
        else if (pStatus == Status.mN) {
            restoreDetailContent();
            restoreListingContent();
            minimizeNavigationContent();
            internalShowDetailPanel();
            setBounds();
            restorePanel(listingPanel);
            minimizePanel(navigationPanel);
            enableSplitter(navigationPanel, false);
            enableSplitter(listingPanel, true);
        }
        else if (pStatus == Status.MD) {
            maximizeDetailContent();
            minimizeListingContent();
            minimizeNavigationContent();
            internalShowDetailPanel();
            setBounds();
            minimizePanel(listingPanel);
            minimizePanel(navigationPanel);
            enableSplitter(navigationPanel, false);
            enableSplitter(listingPanel, false);
        }
        else if (pStatus == Status.ML) {
            minimizeDetailContent();
            maximizeListingContent();
            minimizeNavigationContent();
            internalShowDetailPanel();
            setBounds();
            onResize();
            restorePanel(listingPanel);
            // Save panel status before maximizing listing for detail reducing
            getAssociatedSplitter(listingPanel).saveState();
            getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(
                    Integer.MAX_VALUE);
            minimizePanel(navigationPanel);
            enableSplitter(navigationPanel, false);
            enableSplitter(listingPanel, false);
        }
        else if (pStatus == Status.HD) {
            restoreListingContent();
            restoreNavigationContent();
            internalHideDetailPanel();
            setBounds();
            onResize();
            restorePanel(listingPanel);
            restorePanel(navigationPanel);
            // Save panel status before maximizing listing for detail reducing
            getAssociatedSplitter(listingPanel).saveState();
            getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(
                    Integer.MAX_VALUE);
            enableSplitter(navigationPanel, true);
        }
        else if (pStatus == Status.HDmN) {
            restoreListingContent();
            minimizeNavigationContent();
            internalHideDetailPanel();
            setBounds();
            onResize();
            restorePanel(listingPanel);
            minimizePanel(navigationPanel);
            // Save panel status before maximizing listing for detail reducing
            getAssociatedSplitter(listingPanel).saveState();
            getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(
                    Integer.MAX_VALUE);
            minimizePanel(navigationPanel);
            enableSplitter(navigationPanel, false);
        }
        status = pStatus;
    }

    /**
     * Unhide the detail panel
     */
    private void internalShowDetailPanel() {
        if (!detailPanel.isVisible()) {
            detailPanel.setVisible(true);
            showSplitter(listingPanel, true);
        }
    }

    /**
     * Hide the detail panel
     */
    private void internalHideDetailPanel() {
        if (detailPanel.isVisible()) {
            detailPanel.setVisible(false);
            showSplitter(listingPanel, false);
        }
    }

    /**
     * Calls the doRestore method on detailContent if necessary
     */
    private void restoreDetailContent() {
        if (detailMinimized || detailMaximized) {
            detailContent.doRestore();
        }
        detailMaximized = false;
        detailMinimized = false;
    }

    /**
     * Calls the doMaximize method on detailContent if necessary
     */
    private void maximizeDetailContent() {
        if (detailMinimized) {
            detailContent.doRestore();
            detailMinimized = false;
        }
        if (!detailMaximized) {
            detailContent.doMaximize();
            detailMaximized = true;
        }
    }

    /**
     * Calls the doMinimize method on detailContent if necessary
     */
    private void minimizeDetailContent() {
        if (detailMaximized) {
            detailContent.doRestore();
            detailMaximized = false;
        }
        if (!detailMinimized) {
            detailContent.doMinimize();
            detailMinimized = true;
        }
    }

    /**
     * Calls the doRestore method on listingContent if necessary
     */
    private void restoreListingContent() {
        if (listingMinimized || listingMaximized) {
            listingContent.doRestore();
        }
        listingMinimized = false;
        listingMaximized = false;
    }

    /**
     * Calls the doMaximize method on listingContent if necessary
     */
    private void maximizeListingContent() {
        if (listingMinimized) {
            listingContent.doRestore();
            listingMinimized = false;
        }
        if (!listingMaximized) {
            listingContent.doMaximize();
            listingMaximized = true;
        }
    }

    /**
     * Calls the doMinimize method on listingContent if necessary
     */
    private void minimizeListingContent() {
        if (listingMaximized) {
            listingContent.doRestore();
            listingMaximized = false;
        }
        if (!listingMinimized) {
            listingContent.doMinimize();
            listingMinimized = true;
        }
    }

    /**
     * Calls the doRestore method on navigationContent if necessary
     */
    private void restoreNavigationContent() {
        if (navigationMinimized) {
            navigationContent.doRestore();
            navigationMinimized = false;
        }
    }

    /**
     * Calls the doMinimize method on navigationContent if necessary
     */
    private void minimizeNavigationContent() {
        if (!navigationMinimized) {
            navigationContent.doMinimize();
            navigationMinimized = true;
        }
    }

    /**
     * Sets all limits in case they changed
     */
    private void setBounds() {
        if (listingContent != null) {
            setWidgetMinSize(listingPanel, listingContent.getMinHeight());
            listingMinWidth = listingContent.getMinWidth();
        }
        if (navigationContent != null) {
            setWidgetMinSize(navigationPanel, navigationContent.getMinWidth());
        }
        if (detailContent != null) {
            detailMinWidth = detailContent.getMinWidth();
        }
        if (detailPanel.isVisible() && detailContent != null) {
            detailPanel.setMinHeight(detailContent.getMinHeight());
        }
        else {
            detailPanel.setMinHeight(0);
        }
    }

    /**
     * Completly hide the detail panel
     */
    public void hideDetailPanel() {
        if (status == Status.mN || status == Status.MD || status == Status.ML) {
            goToStatus(Status.HDmN);
        }
        else {
            goToStatus(Status.HD);
        }
    }

    /**
     * Completly show the detail panel
     */
    public void showDetailPanel() {
        if (status == Status.HDmN) {
            goToStatus(Status.mN);
        }
        else {
            goToStatus(Status.FULL);
        }
    }

    /**
     * Maximize the detail panel if shown, or restores it if minimized or
     * maximized
     */
    public void maximizeOrRestoreDetailPanel() {
        if (status == Status.mD || status == Status.MD) {
            goToStatus(Status.FULL);
        }
        else if (status == Status.ML) {
            goToStatus(Status.mN);
        }
        else if (status != Status.HD && status != Status.HDmN) {
            goToStatus(Status.MD);
        }
    }

    /**
     * Restore the detail panel
     */
    public void restoreDetailPanel() {
        if (status == Status.ML) {
            goToStatus(Status.mN);
        }
        else if (status != Status.HD && status != Status.HDmN) {
            goToStatus(Status.FULL);
        }
    }

    /**
     * Minimize the detail panel
     */
    public void minimizeDetailPanel() {
        if (status == Status.mN) {
            goToStatus(Status.ML);
        }
        else if (status == Status.MD) {
            goToStatus(Status.ML);
        }
        else if (status != Status.HD && status != Status.HDmN) {
            goToStatus(Status.mD);
        }
    }

    /**
     * Minimize the listing panel
     */
    public void minimizeListingPanel() {
        if (status == Status.mN) {
            goToStatus(Status.MD);
        }
        else if (status == Status.ML) {
            goToStatus(Status.MD);
        }
        else if (status != Status.HD && status != Status.HDmN) {
            goToStatus(Status.mL);
        }
    }

    /**
     * Restore the listing panel
     */
    public void restoreListingPanel() {
        if (status == Status.MD) {
            goToStatus(Status.mN);
        }
        else if (status == Status.HDmN) {
            goToStatus(Status.HD);
        }
        else if (status != Status.HD) {
            goToStatus(Status.FULL);
        }
    }

    /**
     * Maximize the listing panel if shown, or restores it if minimized or
     * maximized
     */
    public void maximizeOrRestoreListingPanel() {
        if (status == Status.mL || status == Status.ML) {
            goToStatus(Status.FULL);
        }
        else if (status == Status.HD) {
            goToStatus(Status.HDmN);
        }
        else if (status == Status.HDmN) {
            goToStatus(Status.HD);
        }
        else if (status == Status.MD) {
        	goToStatus(Status.mN);
        }
        else {
            goToStatus(Status.ML);
        }
    }

    /**
     * Minimize navigation panel
     */
    public void minimizeNavigationPanel() {
        if (status == Status.FULL) {
            goToStatus(Status.mN);
        }
        else if (status == Status.mD) {
            goToStatus(Status.ML);
        }
        else if (status == Status.mL) {
            goToStatus(Status.MD);
        }
        else if (status == Status.HD) {
            goToStatus(Status.HDmN);
        }
    }

    /**
     * Restore navigation panel
     */
    public void restoreNavigationPanel() {
        if (status == Status.ML) {
            goToStatus(Status.mD);
        }
        else if (status == Status.MD) {
            goToStatus(Status.mL);
        }
        else if (status == Status.HDmN) {
            goToStatus(Status.HD);
        }
        else if (status != Status.HD) {
            goToStatus(Status.FULL);
        }
    }

    /**
     * Make detail panel visible
     */
    public void makeDetailPanelVisible() {
        if (status == Status.HDmN) {
            goToStatus(Status.mN);
        }
        else if (status == Status.HD || status == Status.mD
                || status == Status.ML) {
            goToStatus(Status.FULL);
        }
    }

    /**
     * Minimize the panel
     * 
     * @param pWidget
     *            the contained panel to minimize
     */
    private void minimizePanel(Widget pWidget) {
        getAssociatedSplitter(pWidget).minimize();
    }

    /**
     * Enable or disable the splitter associated to the widget
     * 
     * @param pWidget
     *            The widget
     * @param pEnable
     *            Indicate if the Splitter must be enabled or not
     */
    private void enableSplitter(Widget pWidget, boolean pEnable) {
        getAssociatedSplitter(pWidget).enable(pEnable);
    }

    /**
     * Hide or show the splitter associated to the widget
     * 
     * @param pWidget
     *            The widget
     * @param pShow
     *            Indicate if the Splitter must be visible or not
     */
    private void showSplitter(Widget pWidget, boolean pShow) {
        if (pShow) {
            getAssociatedSplitter(pWidget).show();
        }
        else {
            getAssociatedSplitter(pWidget).hide();
        }
    }

    /**
     * Restore the panel
     * 
     * @param pWidget
     *            the contained panel to minimize
     */
    private void restorePanel(Widget pWidget) {
        getAssociatedSplitter(pWidget).restore();
    }

    /**
     * Private class used for size control on Content panel for GPM
     * WorkSpaceLayoutPanel. Allows the content panel to have a minimum vertical
     * size
     */
    private class InternDetailResizeListener extends LayoutPanel implements
            RequiresResize {

        private static final int DEFAULT_MIN_SIZE = 100;

        /** Minimum size of the detailPanel */
        private int minHeight = DEFAULT_MIN_SIZE;

        /**
         * set minSize
         * 
         * @param pMinSize
         *            the minSize to set
         */
        public void setMinHeight(int pMinSize) {
            minHeight = pMinSize;
        }

        /**
         * Gives the max size behavior for the listing and navigation panels,
         * depending on the browser size and the detail and listing panels
         * minimum sizes
         * 
         * @see com.google.gwt.user.client.ui.LayoutPanel#onResize()
         */
        public void onResize() {
            super.onResize();
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    setBounds();
                    final Element lSplitElement =
                            getElement().getParentElement().getParentElement();
                    // If SplitLayoutPanel top position is defined, sets the maximum size of the 
                    // listing Panel using : SPLITTER_WIDGET_CLIENT_HEIGHT 
                    //                       - SplitterSize - SplitLayout.AbsoluteTop() 
                    if (lSplitElement.getStyle().getTop() != null) {
                        int lCalculated = lSplitElement.getClientHeight();
                        if (lCalculated != 0) {
                            // Avoid resizing when element not yet displayed
                            if (isVisible()) {
                                lCalculated -= splitterSize + minHeight;
                            }
                            setWidgetMaxSize(listingPanel, lCalculated);
                        }
                    }
                    if (lSplitElement.getStyle().getLeft() != null) {
                        if (lSplitElement.getClientWidth() != 0) {
                            // Avoid resizing when element not yet displayed
                            int lCalculated =
                                    lSplitElement.getClientWidth()
                                            - splitterSize
                                            - Math.max(listingMinWidth,
                                                    detailMinWidth);
                            setWidgetMaxSize(navigationPanel, lCalculated);
                        }
                    }

                    // Specific behavior added when detail panel is reduced, and we want to
                    // minimized the gPM banner
                    // If widget size was not re set, minimize gPM banner would cause the minimized
                    // detail panel to have a bad size
                    if (status == Status.mD || status == Status.ML
                            || status == Status.HD || status == Status.HDmN) {
                        getAssociatedSplitter(listingPanel).setAssociatedWidgetSize(
                                Integer.MAX_VALUE);
                    }
                }
            });
        }
    }
}

/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.ArrayList;
import java.util.Iterator;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.layout.tab.GpmTab;
import org.topcased.gpm.ui.component.client.layout.tab.GpmTabCloseButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * This class uses the GWT TabLayoutPanel container. It allows tabs closable
 * tabs, and can manages it using identifiers. <h3>CSS style rules</h3>. The
 * tabs can have two different behavior:
 * <ul>
 * <li>Classic: The clickable tab titles can jump to a new tab line when there
 * are many</li>
 * <li>Expanded: All tabs will have the same size, dividing the tab line space
 * between them, and never jumps to a new line</li>
 * </ul>
 * <dl>
 * <dt>gpm-GpmTabAddButton</dt>
 * <dd>Widget default style</dd>
 * </dl>
 * 
 * @author frosier
 */
public class GpmTabLayoutPanel extends ResizeComposite {
    private static final String ADD_TAB_ID = "AddTabId";

    private static final int ONE_HUNDRED = 100;

    protected static final int TAB_HEADER_SIZE = 22;

    /**
     * The tab layout panel.
     */
    private TabLayoutWidget tabLayoutPanel;

    /**
     * Potential adding new tab button.
     */
    private HasClickHandlers addTabButton;

    /**
     * Creates an empty tab layout panel defined initialized with the given
     * height.
     * 
     * @param pAddButton
     *            <code>true</code> to have a button to add new tabs in the tab
     *            layout panel.
     * @param pTabsExpanded
     *            Indicates if the tabs of the panel must be expanded to its
     *            whole width, or if they must keep their original size
     */
    public GpmTabLayoutPanel(final boolean pAddButton,
            final boolean pTabsExpanded) {
        tabLayoutPanel = new TabLayoutWidget(TAB_HEADER_SIZE, pTabsExpanded);
        initWidget(tabLayoutPanel);

        // Do not select the latest tab if it is 'Add new tab button'
        tabLayoutPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> pEvent) {
                if (pEvent.getItem() >= (tabLayoutPanel.getWidgetCount() - 1)
                        && getWidgetTabIndex(ADD_TAB_ID) == pEvent.getItem()) {
                    pEvent.cancel();
                }
            }
        });

        // Initialize "Add" button if required
        if (pAddButton) {
            final GpmHorizontalPanel lAddButtonTabHeader =
                    new GpmHorizontalPanel();
            final GpmDoubleImageButton lGpmImageButton =
                    new GpmDoubleImageButton(INSTANCE.images().add(),
                            INSTANCE.images().addHover());

            lGpmImageButton.addStyleName(ComponentResources.INSTANCE.css().gpmTabAddButton());
            //Due to tab that must only contains Widgets.
            addTabButton = lGpmImageButton;

            final Widget lEmptyContent = new FlowPanel();

            lEmptyContent.getElement().setId(ADD_TAB_ID);
            lAddButtonTabHeader.add(lGpmImageButton);

            tabLayoutPanel.add(lEmptyContent, lAddButtonTabHeader);
        }
    }

    /**
     * Adds a widget to the panel. If the Widget is already attached, it will be
     * moved to the right-most index : if an element with the same id already,
     * it is replaced (except for static tab : not closeable).
     * 
     * @param pWidget
     *            The widget to be added.
     * @param pTabText
     *            The text to be shown on its tab.
     * @param pCloseHandler
     *            The handler used to close the tab. Null if no close button.
     * @return The added tab identifier.
     */
    public int add(final Widget pWidget, final String pTabText,
            final ClickHandler pCloseHandler) {
        final String lTabId = pWidget.getElement().getId();
        final int lOldTabIndex = getWidgetTabIndex(lTabId);
        final int lAddedTabIndex;

        // If an element with the same id already exists
        // Element that cannot be closed, cannot be replaced
        if (pCloseHandler == null || lOldTabIndex == -1) {
            // Add the element on the last position
            if (addTabButton != null) {
                lAddedTabIndex = tabLayoutPanel.getWidgetCount() - 1;
            }
            else {
                lAddedTabIndex = tabLayoutPanel.getWidgetCount();
            }
        }
        else {
            // Remove and replace the element
            tabLayoutPanel.remove(lOldTabIndex);
            lAddedTabIndex = lOldTabIndex;
        }
        tabLayoutPanel.insert(pWidget, buildTabHeader(pTabText, lTabId,
                pCloseHandler), lAddedTabIndex);

        return lAddedTabIndex;
    }

    /**
     * W Add a tab and select it : if an element with the same id already, it is
     * replaced.
     * 
     * @param pWidget
     *            The widget to be added.
     * @param pTabText
     *            The text to be shown on its tab.
     * @param pCloseHandler
     *            The handler used to close the tab. Null if no close button.
     * @return The last tab index.
     */
    public int addAndSelect(final Widget pWidget, final String pTabText,
            final ClickHandler pCloseHandler) {
        final int lIndex = add(pWidget, pTabText, pCloseHandler);

        // If it's the first element force the change of tab
        if (lIndex == 0 && tabLayoutPanel.getWidgetCount() > 1) {
            tabLayoutPanel.selectTab(1);
        }
        tabLayoutPanel.selectTab(lIndex);

        return lIndex;
    }

    /**
     * Select the tab by its identifier.
     * 
     * @param pTabId
     *            The tab identifier.
     */
    public void selectTab(final String pTabId) {
        final int lTabIndex = getWidgetTabIndex(pTabId);

        if (lTabIndex != -1) {
            tabLayoutPanel.selectTab(lTabIndex);
        }
    }

    /**
     * Remove the tab identified by the parameter.
     * 
     * @param pTabId
     *            The tab identifier.
     * @return <code>false</code> if the widget is not present.
     */
    public boolean remove(final String pTabId) {
        return tabLayoutPanel.remove(getWidgetTabIndex(pTabId));
    }

    /**
     * Determines if the tab defined by its identifier is open.
     * 
     * @param pTabId
     *            The tab identifier.
     * @return <code>true</code> the tab defined by the id parameter is open.
     */
    public boolean isOpen(final String pTabId) {
        return tabLayoutPanel.getSelectedIndex() == getWidgetTabIndex(pTabId);
    }

    /**
     * <p>
     * Add a selection handler on the tab layout panel.
     * </p>
     * 
     * @param pHandler
     *            The selection handler.
     * @return The handler registration.
     */
    public HandlerRegistration addSelectionHandler(
            final SelectionHandler<Integer> pHandler) {
        return tabLayoutPanel.addSelectionHandler(pHandler);
    }

    /**
     * Build the tab header, widget contained in clickable tab.
     * 
     * @param pTabText
     *            The text to be shown on its tab.
     * @param pCloseHandler
     *            The handler used to close the tab. Null if no close button.
     * @return The Tab header widget with tab name and close button, if it is
     *         required.
     */
    private Widget buildTabHeader(final String pTabText, final String pTabId,
            final ClickHandler pCloseHandler) {
        final GpmHorizontalPanel lTabHeader = new GpmHorizontalPanel();
        final Widget lHeaderTextContent = new HTML(pTabText);

        lTabHeader.add(lHeaderTextContent);

        if (pCloseHandler != null) {
            final GpmTabCloseButton lCloseButton =
                    new GpmTabCloseButton(pTabId);

            lCloseButton.addClickHandler(pCloseHandler);
            lTabHeader.add(lCloseButton);
        }

        return lTabHeader;
    }

    /**
     * Get the index from the tab identifier.
     * 
     * @param pTabId
     *            The tab identifier.
     * @return The matched index.
     */
    private int getWidgetTabIndex(final String pTabId) {
        int lTabIndex = -1;
        int i = 0;

        while (i < tabLayoutPanel.getWidgetCount() && lTabIndex == -1) {
            final Widget lContentChild = tabLayoutPanel.getWidget(i);

            if (lContentChild.getElement().getId().equals(pTabId)) {
                lTabIndex = tabLayoutPanel.getWidgetIndex(lContentChild);
            }
            i++;
        }

        return lTabIndex;
    }

    public HasClickHandlers getAddTabButton() {
        return addTabButton;
    }

    /**
     * Return the currently selected/displayed widget
     * 
     * @return The currently selected/displayed widget
     */
    public Widget getCurrentWidget() {
        if (tabLayoutPanel.getSelectedIndex() == -1) {
            return null;
        }
        else {
            return tabLayoutPanel.getWidget(tabLayoutPanel.getSelectedIndex());
        }
    }

    /**
     * TabLayoutPanel of GWT redefined for gPM
     * 
     * @author frosier
     */
    public class TabLayoutWidget extends ResizeComposite implements HasWidgets,
            ProvidesResize, IndexedPanel, HasBeforeSelectionHandlers<Integer>,
            HasSelectionHandlers<Integer> {
        /** Pixels from where a tab will go to the next line */
        private static final int JUMP_TO_LINE_MARGIN = 5;

        private WidgetCollection children = new WidgetCollection(this);

        private FlexTable tabBarExpand = new FlexTable();

        // A VerticalPanel is used for compatibility reasons
        private VerticalPanel tabBarNoExpand = new VerticalPanel();

        private ArrayList<GpmTab> tabs = new ArrayList<GpmTab>();

        private final double initialBarHeight;

        private double barHeight;

        private LayoutPanel panel;

        private int selectedIndex = -1;

        private boolean tabsExpanded;

        /**
         * Creates an empty tab panel.
         * 
         * @param pBarHeight
         *            the size of the tab bar
         * @param pTabsExpanded
         *            Indicates if the tabs of the panel must be expanded to its
         *            whole width, or if they must keep their original size
         */
        public TabLayoutWidget(final double pBarHeight,
                final boolean pTabsExpanded) {
            Widget lTabBar;

            initialBarHeight = pBarHeight;
            barHeight = pBarHeight;
            tabsExpanded = pTabsExpanded;

            panel = new LayoutPanel();
            initWidget(panel);

            if (tabsExpanded) {
                tabBarExpand.insertRow(0); // Init first row
                tabBarExpand.setWidth("100%");
                tabBarExpand.setCellSpacing(0);
                tabBarExpand.setCellPadding(0);
                lTabBar = tabBarExpand;
            }
            else {
                lTabBar = tabBarNoExpand;
            }

            panel.add(lTabBar);

            lTabBar.getElement().getParentElement().addClassName(
                    ComponentResources.INSTANCE.css().gpmTabLayoutPanelTabs());
            panel.setWidgetLeftRight(lTabBar, 0, Unit.PX, 0, Unit.PX);
            panel.setWidgetTopHeight(lTabBar, 0, Unit.PX, pBarHeight, Unit.PX);
            panel.setWidgetVerticalPosition(lTabBar, Alignment.BEGIN);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
         */
        public void add(Widget pWidget) {
            insert(pWidget, getWidgetCount());
        }

        /**
         * Adds a widget to the panel. If the Widget is already attached, it
         * will be moved to the right-most index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pText
         *            the text to be shown on its tab
         */
        public void add(Widget pChild, String pText) {
            insert(pChild, pText, getWidgetCount());
        }

        /**
         * Adds a widget to the panel. If the Widget is already attached, it
         * will be moved to the right-most index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pText
         *            the text to be shown on its tab
         * @param pAsHtml
         *            <code>true</code> to treat the specified text as HTML
         */
        public void add(Widget pChild, String pText, boolean pAsHtml) {
            insert(pChild, pText, pAsHtml, getWidgetCount());
        }

        /**
         * Adds a widget to the panel. If the Widget is already attached, it
         * will be moved to the right-most index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pTab
         *            the widget to be placed in the associated tab
         */
        public void add(Widget pChild, Widget pTab) {
            insert(pChild, pTab, getWidgetCount());
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers#addBeforeSelectionHandler(com.google.gwt.event.logical.shared.BeforeSelectionHandler)
         */
        public HandlerRegistration addBeforeSelectionHandler(
                BeforeSelectionHandler<Integer> pHandler) {
            return addHandler(pHandler, BeforeSelectionEvent.getType());
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
         */
        public HandlerRegistration addSelectionHandler(
                SelectionHandler<Integer> pHandler) {
            return addHandler(pHandler, SelectionEvent.getType());
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.HasWidgets#clear()
         */
        public void clear() {
            Iterator<Widget> lIterator = iterator();
            while (lIterator.hasNext()) {
                lIterator.next();
                lIterator.remove();
            }
        }

        /**
         * Gets the index of the currently-selected tab.
         * 
         * @return the selected index, or <code>-1</code> if none is selected.
         */
        public int getSelectedIndex() {
            return selectedIndex;
        }

        /**
         * Gets the widget in the tab at the given index.
         * 
         * @param pIndex
         *            the index of the tab to be retrieved
         * @return the tab's widget
         */
        public Widget getTabWidget(int pIndex) {
            return tabs.get(pIndex).getWidget();
        }

        /**
         * Gets the widget in the tab associated with the given child widget.
         * 
         * @param pChild
         *            the child whose tab is to be retrieved
         * @return the tab's widget
         */
        public Widget getTabWidget(Widget pChild) {
            return getTabWidget(getWidgetIndex(pChild));
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
         */
        public Widget getWidget(int pIndex) {
            return children.get(pIndex);
        }

        public int getWidgetCount() {
            return children.size();
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
         */
        public int getWidgetIndex(Widget pChild) {
            return children.indexOf(pChild);
        }

        /**
         * Inserts a widget into the panel. If the Widget is already attached,
         * it will be moved to the requested index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pBeforeIndex
         *            the index before which it will be inserted
         */
        public void insert(Widget pChild, int pBeforeIndex) {
            insert(pChild, "", pBeforeIndex);
        }

        /**
         * Inserts a widget into the panel. If the Widget is already attached,
         * it will be moved to the requested index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pText
         *            the text to be shown on its tab
         * @param pAsHtml
         *            <code>true</code> to treat the specified text as HTML
         * @param pBeforeIndex
         *            the index before which it will be inserted
         */
        public void insert(Widget pChild, String pText, boolean pAsHtml,
                int pBeforeIndex) {
            Widget lContents;
            if (pAsHtml) {
                lContents = new HTML(pText);
            }
            else {
                lContents = new Label(pText);
            }
            insert(pChild, lContents, pBeforeIndex);
        }

        /**
         * Inserts a widget into the panel. If the Widget is already attached,
         * it will be moved to the requested index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pText
         *            the text to be shown on its tab
         * @param pBeforeIndex
         *            the index before which it will be inserted
         */
        public void insert(Widget pChild, String pText, int pBeforeIndex) {
            insert(pChild, pText, false, pBeforeIndex);
        }

        /**
         * Inserts a widget into the panel. If the Widget is already attached,
         * it will be moved to the requested index.
         * 
         * @param pChild
         *            the widget to be added
         * @param pTab
         *            the widget to be placed in the associated tab
         * @param pBeforeIndex
         *            the index before which it will be inserted
         */
        public void insert(Widget pChild, Widget pTab, int pBeforeIndex) {
            insert(pChild, new GpmTab(pTab, tabsExpanded), pBeforeIndex);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
         */
        public Iterator<Widget> iterator() {
            return children.iterator();
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
         */
        public boolean remove(int pIndex) {
            if ((pIndex < 0) || (pIndex >= getWidgetCount())) {
                return false;
            }

            Widget lChild = children.get(pIndex);
            if (tabsExpanded) {
                tabBarExpand.removeCell(0, pIndex);
            }
            //else 
            // Widget construction in doResize using the tabs variable
            //
            panel.remove(lChild);
            lChild.removeStyleName(ComponentResources.INSTANCE.css().gpmTabLayoutPanelContent());

            children.remove(pIndex);
            tabs.remove(pIndex);

            if (pIndex == selectedIndex) {
                // If the selected tab is being removed, select the first tab (if
                // there
                // is one).
                selectedIndex = -1;
                if (getWidgetCount() > 0) {
                    selectTab(0);
                }
            }
            else if (pIndex < selectedIndex) {
                // If the selectedIndex is greater than the one being removed, it
                // needs
                // to be adjusted.
                --selectedIndex;
            }
            doResize();
            return true;
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
         */
        public boolean remove(Widget pWidget) {
            int lIndex = children.indexOf(pWidget);
            if (lIndex == -1) {
                return false;
            }

            return remove(lIndex);
        }

        /**
         * Programmatically selects the specified tab.
         * 
         * @param pIndex
         *            the index of the tab to be selected
         */
        public void selectTab(int pIndex) {
            if (pIndex == selectedIndex) {
                return;
            }

            // Fire the before selection event, giving the recipients a chance to
            // cancel the selection.
            BeforeSelectionEvent<Integer> lEvent =
                    BeforeSelectionEvent.fire(this, pIndex);
            if ((lEvent != null) && lEvent.isCanceled()) {
                return;
            }

            // Update the tabs being selected and unselected.
            if (selectedIndex != -1) {
                Widget lChild = children.get(selectedIndex);
                Element lContainer = panel.getWidgetContainerElement(lChild);
                lContainer.getStyle().setVisibility(Visibility.HIDDEN);
                lChild.setVisible(false);
                tabs.get(selectedIndex).setSelected(false);
            }

            Widget lChild = children.get(pIndex);
            Element lContainer = panel.getWidgetContainerElement(lChild);
            lContainer.getStyle().setVisibility(Visibility.VISIBLE);
            lChild.setVisible(true);
            
            for (GpmTab lGpmTab : tabs) {
                lGpmTab.setSelected(false);
            }
            
            tabs.get(pIndex).setSelected(true);
            selectedIndex = pIndex;

            // Fire the selection event.
            SelectionEvent.fire(this, pIndex);

            // Force content to "refresh". Added to correct problem when changing tab in Workspace
            // after minimizing/maximizing banner
            if (lChild instanceof RequiresResize) {
                ((RequiresResize) lChild).onResize();
            }
            // Force TabBar refresh when selecting Tabs
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    onResize();
                }
            });
        }

        /**
         * Programmatically selects the specified tab.
         * 
         * @param pChild
         *            the child whose tab is to be selected
         */
        public void selectTab(Widget pChild) {
            selectTab(getWidgetIndex(pChild));
        }

        /**
         * Sets a tab's text contents.
         * 
         * @param pIndex
         *            the index of the tab whose text is to be set
         * @param pText
         *            the object's new text
         */
        public void setTabText(int pIndex, String pText) {
            tabs.get(pIndex).setWidget(new Label(pText));
        }

        private void insert(final Widget pChild, GpmTab pTab, int pBeforeIndex) {
            // Check to see if the TabPanel already contains the Widget. If so,
            // remove it and see if we need to shift the position to the left.
            int lIdx = getWidgetIndex(pChild);
            if (lIdx != -1) {
                remove(pChild);
                if (lIdx < pBeforeIndex) {
                    pBeforeIndex--;
                }
            }

            children.insert(pChild, pBeforeIndex);
            tabs.add(pBeforeIndex, pTab);

            if (tabsExpanded) {
                tabBarExpand.insertCell(0, pBeforeIndex);
                tabBarExpand.setWidget(0, pBeforeIndex, pTab);
                // Define the same width for every Tab
                for (int i = 0; i < children.size(); i++) {
                    tabBarExpand.getCellFormatter().setWidth(0, i,
                            ONE_HUNDRED / children.size() + "%");
                }
            }
            //else 
            // Widget construction in doResize using the tabs variable
            //
            pTab.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent pEvent) {
                    selectTab(pChild);
                }
            });

            panel.insert(pChild, pBeforeIndex);
            layoutChild(pChild);

            if (selectedIndex == -1) {
                selectTab(0);
            }
            doResize();
        }

        private void layoutChild(Widget pChild) {
            panel.setWidgetLeftRight(pChild, 0, Unit.PX, 0, Unit.PX);
            panel.setWidgetTopBottom(pChild, barHeight, Unit.PX, 0, Unit.PX);
            panel.getWidgetContainerElement(pChild).getStyle().setVisibility(
                    Visibility.HIDDEN);
            pChild.addStyleName(ComponentResources.INSTANCE.css().gpmTabLayoutPanelContent());
            pChild.setVisible(false);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.ResizeComposite#onResize()
         */
        @Override
        public void onResize() {
            super.onResize();
            doResize();
        }

        /**
         * Build a new Tab line with correct style properties
         * 
         * @return a new Tab line
         */
        private GpmFlowLayoutPanel buildNewTabLine() {
            GpmFlowLayoutPanel lNewLine = new GpmFlowLayoutPanel();
            lNewLine.getElement().getStyle().setWidth(ONE_HUNDRED, Unit.PCT);
            return lNewLine;
        }

        /**
         * Method to allow the tabBar to wrap or unwrap. Called by add(),
         * remove() and resize() methods
         */
        public void doResize() {
            Widget lTabBar;

            int lLines = 0;
            int lMaxLineWidth = panel.getOffsetWidth();

            if (tabsExpanded) {
                lTabBar = tabBarExpand;
            }
            else {
                GpmFlowLayoutPanel lCurrentLine = buildNewTabLine();
                GpmFlowLayoutPanel lNewLine = buildNewTabLine();
                int lCount = tabs.size();
                lTabBar = tabBarNoExpand;

                tabBarNoExpand.clear();
                tabBarNoExpand.add(lCurrentLine);
                tabBarNoExpand.add(lNewLine);

                int lLineSize = 0;

                for (int i = 0; i < lCount; i++) {
                    Widget lChild = tabs.get(i);
                    lNewLine.add(lChild);

                    // If tab not long enough
                    if (lLineSize + lChild.getOffsetWidth() > lMaxLineWidth
                            - JUMP_TO_LINE_MARGIN) {
                        // Create a new newLine
                        lCurrentLine = lNewLine;
                        lNewLine = buildNewTabLine();
                        tabBarNoExpand.add(lNewLine);
                        // Leave the widget in the new line, increase line count, reset size
                        lLines++;
                        lLineSize = lChild.getOffsetWidth();
                    }
                    else {
                        // Reattach widget to previous line
                        lLineSize += lChild.getOffsetWidth();
                        lCurrentLine.add(lChild);
                    }
                }
            }

            barHeight = (lLines + 1) * initialBarHeight;
            for (Widget lChild : children) {
                panel.setWidgetTopBottom(lChild, barHeight, Unit.PX, 0, Unit.PX);
            }

            final Widget lForDeffered = lTabBar;
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    panel.setWidgetTopHeight(lForDeffered, 0, Unit.PX,
                            barHeight, Unit.PX);
                }
            });
        }
    }
}
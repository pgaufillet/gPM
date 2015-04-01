/*
 * Copyright 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.resources.ComponentCssResource;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;

/**
 * A standard menu bar widget. A menu bar can contain any number of menu items,
 * each of which can either fire a {@link com.google.gwt.user.client.Command} or
 * open a cascaded menu bar.
 * <p>
 * <img class='gallery' src='doc-files/MenuBar.png'/>
 * </p>
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-MenuBar</dt>
 * <dd>the menu bar itself</dd>
 * <dt>.gwt-MenuBar-horizontal</dt>
 * <dd>dependent style applied to horizontal menu bars</dd>
 * <dt>.gwt-MenuBar-vertical</dt>
 * <dd>dependent style applied to vertical menu bars</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItem</dt>
 * <dd>menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItem-selected</dt>
 * <dd>selected menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItemSeparator</dt>
 * <dd>section breaks between menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItemSeparator .menuSeparatorInner</dt>
 * <dd>inner component of section separators</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopLeft</dt>
 * <dd>the top left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopCenter</dt>
 * <dd>the top center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopRight</dt>
 * <dd>the top right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleLeft</dt>
 * <dd>the middle left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleCenter</dt>
 * <dd>the middle center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleRight</dt>
 * <dd>the middle right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomLeft</dt>
 * <dd>the bottom left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomCenter</dt>
 * <dd>the bottom center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomRight</dt>
 * <dd>the bottom right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * </dl>
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.MenuBarExample}
 * </p>
 * <h3>Use in UiBinder Templates</h3>
 * <p>
 * MenuBar elements in UiBinder template files can have a <code>vertical</code>
 * boolean attribute (which defaults to false), and may have only MenuItem
 * elements as children. MenuItems may contain HTML and MenuBars.
 * <p>
 * For example:
 * 
 * <pre>
 * &lt;g:MenuBar>
 *   &lt;g:MenuItem>Higgledy
 *     &lt;g:MenuBar vertical="true">
 *       &lt;g:MenuItem>able&lt;/g:MenuItem>
 *       &lt;g:MenuItem>baker&lt;/g:MenuItem>
 *       &lt;g:MenuItem>charlie&lt;/g:MenuItem>
 *     &lt;/g:MenuBar>
 *   &lt;/g:MenuItem>
 *   &lt;g:MenuItem>Piggledy
 *     &lt;g:MenuBar vertical="true">
 *       &lt;g:MenuItem>foo&lt;/g:MenuItem>
 *       &lt;g:MenuItem>bar&lt;/g:MenuItem>
 *       &lt;g:MenuItem>baz&lt;/g:MenuItem>
 *     &lt;/g:MenuBar>
 *   &lt;/g:MenuItem>
 *   &lt;g:MenuItem>&lt;b>Pop!&lt;/b>
 *     &lt;g:MenuBar vertical="true">
 *       &lt;g:MenuItem>uno&lt;/g:MenuItem>
 *       &lt;g:MenuItem>dos&lt;/g:MenuItem>
 *       &lt;g:MenuItem>tres&lt;/g:MenuItem>
 *     &lt;/g:MenuBar>
 *   &lt;/g:MenuItem>
 * &lt;/g:MenuBar>
 * </pre>
 */
// Nothing we can do about MenuBar implementing PopupListener until next
// release.
@SuppressWarnings("deprecation")
public class MenuBar extends Widget implements PopupListener, HasAnimation,
        HasCloseHandlers<PopupPanel> {

    /**
     * An {@link ImageBundle} that provides images for {@link MenuBar}.
     * 
     * @deprecated replaced by {@link Resources}
     */
    @Deprecated
    public interface MenuBarImages extends ImageBundle {
        /**
         * An image indicating a {@link MenuItem} has an associated submenu.
         * 
         * @return a prototype of this image
         */
        AbstractImagePrototype menuBarSubMenuIcon();
    }

    /**
     * A ClientBundle that contains the default resources for this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * An image indicating a {@link MenuItem} has an associated submenu.
         */
        @ImageOptions(flipRtl = true)
        ImageResource menuBarSubMenuIcon();
    }

    protected final static ComponentCssResource CSS =
            ComponentResources.INSTANCE.css();

    private static final String STYLENAME_DEFAULT = "gwt-MenuBar";

    /**
     * List of all {@link MenuItem}s and {@link MenuItemSeparator}s.
     */
    private ArrayList<UIObject> allItems = new ArrayList<UIObject>();

    /**
     * List of {@link MenuItem}s, not including {@link MenuItemSeparator}s.
     */
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();

    private Element body;

    private AbstractImagePrototype subMenuIcon = null;

    private boolean isAnimationEnabled = false;

    private MenuBar parentMenu;

    private PopupPanel popup;

    private MenuItem selectedItem;

    private MenuBar shownChildMenu;

    private boolean vertical;

    private boolean autoOpen;

    private boolean focusOnHover = true;

    // Gpm new behavior to allow right alignment
    private boolean rightAlign = false;

    /**
     * Creates an empty horizontal menu bar.
     */
    public MenuBar() {
        this(false);
    }

    /**
     * Creates an empty menu bar.
     * 
     * @param pVertical
     *            <code>true</code> to orient the menu bar vertically
     */
    public MenuBar(boolean pVertical) {
        this(pVertical, GWT.<Resources> create(Resources.class));
    }

    public MenuBar(boolean pVertical, boolean pRightAlign) {
        this(pVertical, GWT.<Resources> create(Resources.class));
        this.rightAlign = pRightAlign;
    }

    /**
     * Creates an empty menu bar that uses the specified image bundle for menu
     * images.
     * 
     * @param pVertical
     *            <code>true</code> to orient the menu bar vertically
     * @param pImages
     *            a bundle that provides images for this menu
     * @deprecated replaced by {@link #MenuBar(boolean, Resources)}
     */
    @Deprecated
    public MenuBar(boolean pVertical, MenuBarImages pImages) {
        init(pVertical, pImages.menuBarSubMenuIcon());
    }

    /**
     * Creates an empty menu bar that uses the specified ClientBundle for menu
     * images.
     * 
     * @param pVertical
     *            <code>true</code> to orient the menu bar vertically
     * @param pResources
     *            a bundle that provides images for this menu
     */
    public MenuBar(boolean pVertical, Resources pResources) {
        init(pVertical,
                AbstractImagePrototype.create(pResources.menuBarSubMenuIcon()));
    }

    /**
     * Creates an empty horizontal menu bar that uses the specified image bundle
     * for menu images.
     * 
     * @param pImages
     *            a bundle that provides images for this menu
     * @deprecated replaced by {@link #MenuBar(Resources)}
     */
    @Deprecated
    public MenuBar(MenuBarImages pImages) {
        this(false, pImages);
    }

    /**
     * Creates an empty horizontal menu bar that uses the specified ClientBundle
     * for menu images.
     * 
     * @param pResources
     *            a bundle that provides images for this menu
     */
    public MenuBar(Resources pResources) {
        this(false, pResources);
    }

    public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> pHandler) {
        return addHandler(pHandler, CloseEvent.getType());
    }

    /**
     * Adds a menu item to the bar.
     * 
     * @param pItem
     *            the item to be added
     * @return the {@link MenuItem} object
     */
    public MenuItem addItem(MenuItem pItem) {
        return insertItem(pItem, allItems.size());
    }

    /**
     * Adds a menu item to the bar, that will fire the given command when it is
     * selected.
     * 
     * @param pText
     *            the item's text
     * @param pAsHTML
     *            <code>true</code> to treat the specified text as html
     * @param pCmd
     *            the command to be fired
     * @return the {@link MenuItem} object created
     */
    public MenuItem addItem(String pText, boolean pAsHTML, Command pCmd) {
        return addItem(new MenuItem(pText, pAsHTML, pCmd));
    }

    /**
     * Adds a menu item to the bar, that will open the specified menu when it is
     * selected.
     * 
     * @param pText
     *            the item's text
     * @param pAsHTML
     *            <code>true</code> to treat the specified text as html
     * @param pPopup
     *            the menu to be cascaded from it
     * @return the {@link MenuItem} object created
     */
    public MenuItem addItem(String pText, boolean pAsHTML, MenuBar pPopup) {
        return addItem(new MenuItem(pText, pAsHTML, pPopup));
    }

    /**
     * Adds a menu item to the bar, that will fire the given command when it is
     * selected.
     * 
     * @param pText
     *            the item's text
     * @param pCmd
     *            the command to be fired
     * @return the {@link MenuItem} object created
     */
    public MenuItem addItem(String pText, Command pCmd) {
        return addItem(new MenuItem(pText, pCmd));
    }

    /**
     * Adds a menu item to the bar, that will open the specified menu when it is
     * selected.
     * 
     * @param pText
     *            the item's text
     * @param pPopup
     *            the menu to be cascaded from it
     * @return the {@link MenuItem} object created
     */
    public MenuItem addItem(String pText, MenuBar pPopup) {
        return addItem(new MenuItem(pText, pPopup));
    }

    /**
     * Adds a thin line to the {@link MenuBar} to separate sections of
     * {@link MenuItem}s.
     * 
     * @return the {@link MenuItemSeparator} object created
     */
    public MenuItemSeparator addSeparator() {
        return addSeparator(new MenuItemSeparator());
    }

    /**
     * Adds a thin line to the {@link MenuBar} to separate sections of
     * {@link MenuItem}s.
     * 
     * @param pSeparator
     *            the {@link MenuItemSeparator} to be added
     * @return the {@link MenuItemSeparator} object
     */
    public MenuItemSeparator addSeparator(MenuItemSeparator pSeparator) {
        return insertSeparator(pSeparator, allItems.size());
    }

    /**
     * Removes all menu items from this menu bar.
     */
    public void clearItems() {
        // Deselect the current item
        selectItem(null);

        Element lContainer = getItemContainerElement();
        while (DOM.getChildCount(lContainer) > 0) {
            DOM.removeChild(lContainer, DOM.getChild(lContainer, 0));
        }

        // Set the parent of all items to null
        for (UIObject lItem : allItems) {
            setItemColSpan(lItem, 1);
            if (lItem instanceof MenuItemSeparator) {
                ((MenuItemSeparator) lItem).setParentMenu(null);
            }
            else {
                ((MenuItem) lItem).setParentMenu(null);
            }
        }

        // Clear out all of the items and separators
        items.clear();
        allItems.clear();
    }

    /**
     * Give this MenuBar focus.
     */
    public void focus() {
        FocusPanel.impl.focus(getElement());
    }

    /**
     * Gets whether this menu bar's child menus will open when the mouse is
     * moved over it.
     * 
     * @return <code>true</code> if child menus will auto-open
     */
    public boolean getAutoOpen() {
        return autoOpen;
    }

    /**
     * Get the index of a {@link MenuItem}.
     * 
     * @return the index of the item, or -1 if it is not contained by this
     *         MenuBar
     */
    public int getItemIndex(MenuItem pItem) {
        return allItems.indexOf(pItem);
    }

    /**
     * Get the index of a {@link MenuItemSeparator}.
     * 
     * @return the index of the separator, or -1 if it is not contained by this
     *         MenuBar
     */
    public int getSeparatorIndex(MenuItemSeparator pItem) {
        return allItems.indexOf(pItem);
    }

    /**
     * Adds a menu item to the bar at a specific index.
     * 
     * @param pItem
     *            the item to be inserted
     * @param pBeforeIndex
     *            the index where the item should be inserted
     * @return the {@link MenuItem} object
     * @throws IndexOutOfBoundsException
     *             if <code>beforeIndex</code> is out of range
     */
    public MenuItem insertItem(MenuItem pItem, int pBeforeIndex)
        throws IndexOutOfBoundsException {
        // Check the bounds
        if (pBeforeIndex < 0 || pBeforeIndex > allItems.size()) {
            throw new IndexOutOfBoundsException();
        }

        // Add to the list of items
        allItems.add(pBeforeIndex, pItem);
        int lItemsIndex = 0;
        for (int i = 0; i < pBeforeIndex; i++) {
            if (allItems.get(i) instanceof MenuItem) {
                lItemsIndex++;
            }
        }
        items.add(lItemsIndex, pItem);

        // Setup the menu item
        addItemElement(pBeforeIndex, pItem.getElement());
        pItem.setParentMenu(this);
        pItem.setSelectionStyle(false);
        updateSubmenuIcon(pItem);
        return pItem;
    }

    /**
     * Adds a thin line to the {@link MenuBar} to separate sections of
     * {@link MenuItem}s at the specified index.
     * 
     * @param pBeforeIndex
     *            the index where the seperator should be inserted
     * @return the {@link MenuItemSeparator} object
     * @throws IndexOutOfBoundsException
     *             if <code>beforeIndex</code> is out of range
     */
    public MenuItemSeparator insertSeparator(int pBeforeIndex) {
        return insertSeparator(new MenuItemSeparator(), pBeforeIndex);
    }

    /**
     * Adds a thin line to the {@link MenuBar} to separate sections of
     * {@link MenuItem}s at the specified index.
     * 
     * @param pSeparator
     *            the {@link MenuItemSeparator} to be inserted
     * @param pBeforeIndex
     *            the index where the seperator should be inserted
     * @return the {@link MenuItemSeparator} object
     * @throws IndexOutOfBoundsException
     *             if <code>beforeIndex</code> is out of range
     */
    public MenuItemSeparator insertSeparator(MenuItemSeparator pSeparator,
            int pBeforeIndex) throws IndexOutOfBoundsException {
        // Check the bounds
        if (pBeforeIndex < 0 || pBeforeIndex > allItems.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (vertical) {
            setItemColSpan(pSeparator, 2);
        }
        addItemElement(pBeforeIndex, pSeparator.getElement());
        pSeparator.setParentMenu(this);
        allItems.add(pBeforeIndex, pSeparator);
        return pSeparator;
    }

    public boolean isAnimationEnabled() {
        return isAnimationEnabled;
    }

    /**
     * Check whether or not this widget will steal keyboard focus when the mouse
     * hovers over it.
     * 
     * @return true if enabled, false if disabled
     */
    public boolean isFocusOnHoverEnabled() {
        return focusOnHover;
    }

    /**
     * Moves the menu selection down to the next item. If there is no selection,
     * selects the first item. If there are no items at all, does nothing.
     */
    public void moveSelectionDown() {
        if (selectFirstItemIfNoneSelected()) {
            return;
        }

        if (vertical) {
            selectNextItem();
        }
        else {
            if (selectedItem.getSubMenu() != null
                    && !selectedItem.getSubMenu().getItems().isEmpty()
                    && (shownChildMenu == null || shownChildMenu.getSelectedItem() == null)) {
                if (shownChildMenu == null) {
                    doItemAction(selectedItem, false, true);
                }
                selectedItem.getSubMenu().focus();
            }
            else if (parentMenu != null) {
                if (parentMenu.vertical) {
                    parentMenu.selectNextItem();
                }
                else {
                    parentMenu.moveSelectionDown();
                }
            }
        }
    }

    /**
     * Moves the menu selection up to the previous item. If there is no
     * selection, selects the first item. If there are no items at all, does
     * nothing.
     */
    public void moveSelectionUp() {
        if (selectFirstItemIfNoneSelected()) {
            return;
        }

        if ((shownChildMenu == null) && vertical) {
            selectPrevItem();
        }
        else if ((parentMenu != null) && parentMenu.vertical) {
            parentMenu.selectPrevItem();
        }
        else {
            close();
        }
    }

    @Override
    public void onBrowserEvent(Event pEvent) {
        MenuItem lItem = findItem(DOM.eventGetTarget(pEvent));
        switch (DOM.eventGetType(pEvent)) {
            case Event.ONCLICK: {
                FocusPanel.impl.focus(getElement());
                // Fire an item's command when the user clicks on it.
                if (lItem != null) {
                    doItemAction(lItem, true, true);
                }
                break;
            }

            case Event.ONMOUSEOVER: {
                if (lItem != null) {
                    itemOver(lItem, true);
                }
                break;
            }

            case Event.ONMOUSEOUT: {
                if (lItem != null) {
                    itemOver(null, true);
                }
                break;
            }

            case Event.ONFOCUS: {
                selectFirstItemIfNoneSelected();
                break;
            }

            case Event.ONKEYDOWN: {
                int lKeyCode = DOM.eventGetKeyCode(pEvent);
                switch (lKeyCode) {
                    case KeyCodes.KEY_LEFT:
                        if (LocaleInfo.getCurrentLocale().isRTL()) {
                            moveToNextItem();
                        }
                        else {
                            moveToPrevItem();
                        }
                        eatEvent(pEvent);
                        break;
                    case KeyCodes.KEY_RIGHT:
                        if (LocaleInfo.getCurrentLocale().isRTL()) {
                            moveToPrevItem();
                        }
                        else {
                            moveToNextItem();
                        }
                        eatEvent(pEvent);
                        break;
                    case KeyCodes.KEY_UP:
                        moveSelectionUp();
                        eatEvent(pEvent);
                        break;
                    case KeyCodes.KEY_DOWN:
                        moveSelectionDown();
                        eatEvent(pEvent);
                        break;
                    case KeyCodes.KEY_ESCAPE:
                        closeAllParents();
                        // Ensure the popup is closed even if it has not been enetered
                        // with the mouse or key navigation
                        if (parentMenu == null && popup != null) {
                            popup.hide();
                        }
                        eatEvent(pEvent);
                        break;
                    case KeyCodes.KEY_ENTER:
                        if (!selectFirstItemIfNoneSelected()) {
                            doItemAction(selectedItem, true, true);
                            eatEvent(pEvent);
                        }
                        break;
                    default:
                        break;
                } // end switch(keyCode)

                break;
            } // end case Event.ONKEYDOWN
            default:
                break;
        } // end switch (DOM.eventGetType(event))
        super.onBrowserEvent(pEvent);
    }

    /**
     * Closes the menu bar.
     * 
     * @deprecated Use {@link #addCloseHandler(CloseHandler)} instead
     */
    @Deprecated
    public void onPopupClosed(PopupPanel pSender, boolean pAutoClosed) {
        // If the menu popup was auto-closed, close all of its parents as well.
        if (pAutoClosed) {
            closeAllParents();
        }

        // When the menu popup closes, remember that no item is
        // currently showing a popup menu.
        onHide(!pAutoClosed);
        CloseEvent.fire(MenuBar.this, pSender);
        shownChildMenu = null;
        popup = null;
        if (parentMenu != null && parentMenu.popup != null) {
            parentMenu.popup.setPreviewingAllNativeEvents(true);
        }
    }

    /**
     * Removes the specified menu item from the bar.
     * 
     * @param pItem
     *            the item to be removed
     */
    public void removeItem(MenuItem pItem) {
        // Unselect if the item is currently selected
        if (selectedItem == pItem) {
            selectItem(null);
        }

        if (removeItemElement(pItem)) {
            setItemColSpan(pItem, 1);
            items.remove(pItem);
            pItem.setParentMenu(null);
        }
    }

    /**
     * Removes the specified {@link MenuItemSeparator} from the bar.
     * 
     * @param pSeparator
     *            the separator to be removed
     */
    public void removeSeparator(MenuItemSeparator pSeparator) {
        if (removeItemElement(pSeparator)) {
            pSeparator.setParentMenu(null);
        }
    }

    /**
     * Select the given MenuItem, which must be a direct child of this MenuBar.
     * 
     * @param pItem
     *            the MenuItem to select, or null to clear selection
     */
    public void selectItem(MenuItem pItem) {
        assert pItem == null || pItem.getParentMenu() == this;

        if (pItem == selectedItem) {
            return;
        }

        if (selectedItem != null) {
            selectedItem.setSelectionStyle(false);
            // Set the style of the submenu indicator
            if (vertical) {
                Element lTr = DOM.getParent(selectedItem.getElement());
                if (DOM.getChildCount(lTr) == 2) {
                    Element lTd = DOM.getChild(lTr, 1);
                    setStyleName(lTd, CSS.gpmSubMenuIconSelected(), false);
                }
            }
        }

        if (pItem != null) {
            pItem.setSelectionStyle(true);

            // Set the style of the submenu indicator
            if (vertical) {
                Element lTr = DOM.getParent(pItem.getElement());
                if (DOM.getChildCount(lTr) == 2) {
                    Element lTd = DOM.getChild(lTr, 1);
                    setStyleName(lTd, CSS.gpmSubMenuIconSelected(), true);
                }
            }

            Accessibility.setState(getElement(),
                    Accessibility.STATE_ACTIVEDESCENDANT,
                    DOM.getElementAttribute(pItem.getElement(), "id"));
        }

        selectedItem = pItem;
    }

    public void setAnimationEnabled(boolean pEnable) {
        isAnimationEnabled = pEnable;
    }

    /**
     * Sets whether this menu bar's child menus will open when the mouse is
     * moved over it.
     * 
     * @param pAutoOpen
     *            <code>true</code> to cause child menus to auto-open
     */
    public void setAutoOpen(boolean pAutoOpen) {
        this.autoOpen = pAutoOpen;
    }

    /**
     * Enable or disable auto focus when the mouse hovers over the MenuBar. This
     * allows the MenuBar to respond to keyboard events without the user having
     * to click on it, but it will steal focus from other elements on the page.
     * Enabled by default.
     * 
     * @param pEnabled
     *            true to enable, false to disable
     */
    public void setFocusOnHoverEnabled(boolean pEnabled) {
        focusOnHover = pEnabled;
    }

    /**
     * Returns a list containing the <code>MenuItem</code> objects in the menu
     * bar. If there are no items in the menu bar, then an empty
     * <code>List</code> object will be returned.
     * 
     * @return a list containing the <code>MenuItem</code> objects in the menu
     *         bar
     */
    protected List<MenuItem> getItems() {
        return this.items;
    }

    /**
     * Returns the <code>MenuItem</code> that is currently selected
     * (highlighted) by the user. If none of the items in the menu are currently
     * selected, then <code>null</code> will be returned.
     * 
     * @return the <code>MenuItem</code> that is currently selected, or
     *         <code>null</code> if no items are currently selected
     */
    protected MenuItem getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    protected void onDetach() {
        // When the menu is detached, make sure to close all of its children.
        if (popup != null) {
            popup.hide();
        }

        super.onDetach();
    }

    /**
     * <b>Affected Elements:</b>
     * <ul>
     * <li>-item# = the {@link MenuItem} at the specified index.</li>
     * </ul>
     * 
     * @see UIObject#onEnsureDebugId(String)
     */
    @Override
    protected void onEnsureDebugId(String pBaseID) {
        super.onEnsureDebugId(pBaseID);
        setMenuItemDebugIds(pBaseID);
    }

    /*
     * Closes all parent menu popups.
     */
    void closeAllParents() {
        MenuBar lCurMenu = this;
        while (lCurMenu.parentMenu != null) {
            lCurMenu.close();
            lCurMenu = lCurMenu.parentMenu;
        }
    }

    /*
     * Performs the action associated with the given menu item. If the item has a
     * popup associated with it, the popup will be shown. If it has a command
     * associated with it, and 'fireCommand' is true, then the command will be
     * fired. Popups associated with other items will be hidden.
     * 
     * @param item the item whose popup is to be shown. @param fireCommand
     * <code>true</code> if the item's command should be fired, <code>false</code>
     * otherwise.
     */
    void doItemAction(final MenuItem pItem, boolean pFireCommand, boolean pFocus) {
        // Ensure that the item is selected.
        selectItem(pItem);

        if (pItem != null) {
            // if the command should be fired and the item has one, fire it
            if (pFireCommand && pItem.getCommand() != null) {
                // Close this menu and all of its parents.
                closeAllParents();

                // Fire the item's command.
                Command lCmd = pItem.getCommand();
                DeferredCommand.addCommand(lCmd);

                // hide any open submenus of this item
                if (shownChildMenu != null) {
                    shownChildMenu.onHide(pFocus);
                    popup.hide();
                    shownChildMenu = null;
                    selectItem(null);
                }
            }
            else if (pItem.getSubMenu() != null) {
                if (shownChildMenu == null) {
                    // open this submenu
                    openPopup(pItem);
                }
                else if (pItem.getSubMenu() != shownChildMenu) {
                    // close the other submenu and open this one
                    shownChildMenu.onHide(pFocus);
                    popup.hide();
                    openPopup(pItem);
                }
                else if (pFireCommand && !autoOpen) {
                    // close this submenu
                    shownChildMenu.onHide(pFocus);
                    popup.hide();
                    shownChildMenu = null;
                    selectItem(pItem);
                }
            }
            else if (autoOpen && shownChildMenu != null) {
                // close submenu
                shownChildMenu.onHide(pFocus);
                popup.hide();
                shownChildMenu = null;
            }
        }
    }

    void itemOver(MenuItem pItem, boolean pFocus) {
        if (pItem == null) {
            // Don't clear selection if the currently selected item's menu is showing.
            if ((selectedItem != null)
                    && (shownChildMenu == selectedItem.getSubMenu())) {
                return;
            }
        }

        // Style the item selected when the mouse enters.
        selectItem(pItem);
        if (pFocus && focusOnHover) {
            focus();
        }

        // If child menus are being shown, or this menu is itself
        // a child menu, automatically show an item's child menu
        // when the mouse enters.
        if (pItem != null) {
            if ((shownChildMenu != null) || (parentMenu != null) || autoOpen) {
                doItemAction(pItem, false, focusOnHover);
            }
        }
    }

    /**
     * Set the IDs of the menu items.
     * 
     * @param pBaseID
     *            the base ID
     */
    void setMenuItemDebugIds(String pBaseID) {
        int lItemCount = 0;
        for (MenuItem lItem : items) {
            lItem.ensureDebugId(pBaseID + "-item" + lItemCount);
            lItemCount++;
        }
    }

    /**
     * Show or hide the icon used for items with a submenu.
     * 
     * @param pItem
     *            the item with or without a submenu
     */
    void updateSubmenuIcon(MenuItem pItem) {
        // The submenu icon only applies to vertical menus
        if (!vertical) {
            return;
        }

        // Get the index of the MenuItem
        int lIdx = allItems.indexOf(pItem);
        if (lIdx == -1) {
            return;
        }

        Element lContainer = getItemContainerElement();
        Element lTr = DOM.getChild(lContainer, lIdx);
        int lTdCount = DOM.getChildCount(lTr);
        MenuBar lSubmenu = pItem.getSubMenu();
        if (lSubmenu == null) {
            // Remove the submenu indicator
            if (lTdCount == 2) {
                DOM.removeChild(lTr, DOM.getChild(lTr, 1));
            }
            setItemColSpan(pItem, 2);
        }
        else if (lTdCount == 1) {
            // Show the submenu indicator
            setItemColSpan(pItem, 1);
            Element lTd = DOM.createTD();
            DOM.setElementProperty(lTd, "vAlign", "middle");
            DOM.setInnerHTML(lTd, subMenuIcon.getHTML());
            setStyleName(lTd, CSS.gpmSubMenuIcon());
            DOM.appendChild(lTr, lTd);
        }
    }

    /**
     * Physically add the td element of a {@link MenuItem} or
     * {@link MenuItemSeparator} to this {@link MenuBar}.
     * 
     * @param pBeforeIndex
     *            the index where the seperator should be inserted
     * @param pTdElem
     *            the td element to be added
     */
    private void addItemElement(int pBeforeIndex, Element pTdElem) {
        if (vertical) {
            Element lTr = DOM.createTR();
            DOM.insertChild(body, lTr, pBeforeIndex);
            DOM.appendChild(lTr, pTdElem);
        }
        else {
            Element lTr = DOM.getChild(body, 0);
            DOM.insertChild(lTr, pTdElem, pBeforeIndex);
        }
    }

    /**
     * Closes this menu (if it is a popup).
     */
    private void close() {
        if (parentMenu != null) {
            parentMenu.popup.hide();
            parentMenu.focus();
        }
    }

    private void eatEvent(Event pEvent) {
        DOM.eventCancelBubble(pEvent, true);
        DOM.eventPreventDefault(pEvent);
    }

    private MenuItem findItem(Element pHItem) {
        for (MenuItem lItem : items) {
            if (DOM.isOrHasChild(lItem.getElement(), pHItem)) {
                return lItem;
            }
        }
        return null;
    }

    private Element getItemContainerElement() {
        if (vertical) {
            return body;
        }
        else {
            return DOM.getChild(body, 0);
        }
    }

    private void init(boolean pVertical, AbstractImagePrototype pSubMenuIcon) {
        this.subMenuIcon = pSubMenuIcon;

        Element lTable = DOM.createTable();
        body = DOM.createTBody();
        DOM.appendChild(lTable, body);

        if (!pVertical) {
            Element lTr = DOM.createTR();
            DOM.appendChild(body, lTr);
        }

        this.vertical = pVertical;

        Element lOuter = FocusPanel.impl.createFocusable();
        DOM.appendChild(lOuter, lTable);
        setElement(lOuter);

        Accessibility.setRole(getElement(), Accessibility.ROLE_MENUBAR);

        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
                | Event.ONFOCUS | Event.ONKEYDOWN);

        setStyleName(STYLENAME_DEFAULT);
        if (pVertical) {
            addStyleDependentName("vertical");
        }
        else {
            addStyleDependentName("horizontal");
        }

        // Hide focus outline in Mozilla/Webkit/Opera
        DOM.setStyleAttribute(getElement(), "outline", "0px");

        // Hide focus outline in IE 6/7
        DOM.setElementAttribute(getElement(), "hideFocus", "true");
    }

    private void moveToNextItem() {
        if (selectFirstItemIfNoneSelected()) {
            return;
        }

        if (!vertical) {
            selectNextItem();
        }
        else {
            if (selectedItem.getSubMenu() != null
                    && !selectedItem.getSubMenu().getItems().isEmpty()
                    && (shownChildMenu == null || shownChildMenu.getSelectedItem() == null)) {
                if (shownChildMenu == null) {
                    doItemAction(selectedItem, false, true);
                }
                selectedItem.getSubMenu().focus();
            }
            else if (parentMenu != null) {
                if (!parentMenu.vertical) {
                    parentMenu.selectNextItem();
                }
                else {
                    parentMenu.moveToNextItem();
                }
            }
        }
    }

    private void moveToPrevItem() {
        if (selectFirstItemIfNoneSelected()) {
            return;
        }

        if (!vertical) {
            selectPrevItem();
        }
        else {
            if ((parentMenu != null) && (!parentMenu.vertical)) {
                parentMenu.selectPrevItem();
            }
            else {
                close();
            }
        }
    }

    /*
     * This method is called when a menu bar is hidden, so that it can hide any
     * child popups that are currently being shown.
     */
    private void onHide(boolean pFocus) {
        if (shownChildMenu != null) {
            shownChildMenu.onHide(pFocus);
            popup.hide();
            if (pFocus) {
                focus();
            }
        }
    }

    /*
     * This method is called when a menu bar is shown.
     */
    private void onShow() {
        // clear the selection; a keyboard user can cursor down to the first item
        selectItem(null);
    }

    private void openPopup(final MenuItem pItem) {
        // Only the last popup to be opened should preview all event
        if (parentMenu != null && parentMenu.popup != null) {
            parentMenu.popup.setPreviewingAllNativeEvents(false);
        }

        // Create a new popup for this item, and position it next to
        // the item (below if this is a horizontal menu bar, to the
        // right if it's a vertical bar).
        popup = new PopupPanel(true, false) {
            {
                setWidget(pItem.getSubMenu());
                setPreviewingAllNativeEvents(true);
                pItem.getSubMenu().onShow();
            }

            @Override
            protected void onPreviewNativeEvent(NativePreviewEvent pEvent) {
                // Hook the popup panel's event preview. We use this to keep it
                // from
                // auto-hiding when the parent menu is clicked.
                if (!pEvent.isCanceled()) {

                    switch (pEvent.getTypeInt()) {
                        case Event.ONMOUSEDOWN:
                            // If the event target is part of the parent menu,
                            // suppress the
                            // event altogether.
                            EventTarget lTarget =
                                    pEvent.getNativeEvent().getEventTarget();
                            Element lParentMenuElement =
                                    pItem.getParentMenu().getElement();
                            if (lParentMenuElement.isOrHasChild(Element.as(lTarget))) {
                                pEvent.cancel();
                                return;
                            }
                            super.onPreviewNativeEvent(pEvent);
                            if (pEvent.isCanceled()) {
                                selectItem(null);
                            }
                            return;
                        default:
                            break;
                    }
                }
                super.onPreviewNativeEvent(pEvent);
            }
        };

        popup.setAnimationType(AnimationType.ONE_WAY_CORNER);
        popup.setAnimationEnabled(isAnimationEnabled);
        popup.setStyleName(CSS.gpmMenuPopup());
        popup.addPopupListener(this);

        shownChildMenu = pItem.getSubMenu();
        pItem.getSubMenu().parentMenu = this;

        // Show the popup, ensuring that the menubar's event preview remains on
        // top
        // of the popup's.
        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

            public void setPosition(int pOffsetWidth, int pOffsetHeight) {

                // depending on the bidi direction position a menu on the left
                // or right of its base item

                if (LocaleInfo.getCurrentLocale().isRTL() || rightAlign) {
                    if (vertical) {
                        popup.setPopupPosition(MenuBar.this.getAbsoluteLeft()
                                - pOffsetWidth + 1, pItem.getAbsoluteTop());
                    }
                    else {
                        popup.setPopupPosition(pItem.getAbsoluteLeft()
                                + pItem.getOffsetWidth() - (pOffsetWidth + 1),
                                pItem.getAbsoluteTop()
                                        + pItem.getOffsetHeight() - 1);
                    }
                }
                else {
                    if (vertical) {
                        popup.setPopupPosition(MenuBar.this.getAbsoluteLeft()
                                + MenuBar.this.getOffsetWidth() - 1,
                                pItem.getAbsoluteTop());
                    }
                    else {
                        popup.setPopupPosition(pItem.getAbsoluteLeft(),
                                MenuBar.this.getAbsoluteTop()
                                        + MenuBar.this.getOffsetHeight() - 1);
                    }
                }
            }
        });
    }

    /**
     * Removes the specified item from the {@link MenuBar} and the physical DOM
     * structure.
     * 
     * @param pItem
     *            the item to be removed
     * @return true if the item was removed
     */
    private boolean removeItemElement(UIObject pItem) {
        int lIdx = allItems.indexOf(pItem);
        if (lIdx == -1) {
            return false;
        }

        Element lContainer = getItemContainerElement();
        DOM.removeChild(lContainer, DOM.getChild(lContainer, lIdx));
        allItems.remove(lIdx);
        return true;
    }

    /**
     * Selects the first item in the menu if no items are currently selected.
     * Has no effect if there are no items.
     * 
     * @return true if no item was previously selected, false otherwise
     */
    private boolean selectFirstItemIfNoneSelected() {
        if (selectedItem == null) {
            if (items.size() > 0) {
                MenuItem lNextItem = items.get(0);
                selectItem(lNextItem);
            }
            return true;
        }
        return false;
    }

    private void selectNextItem() {
        if (selectedItem == null) {
            return;
        }

        int lIndex = items.indexOf(selectedItem);
        // We know that selectedItem is set to an item that is contained in the
        // items collection.
        // Therefore, we know that index can never be -1.
        assert (lIndex != -1);

        MenuItem lItemToBeSelected;

        if (lIndex < items.size() - 1) {
            lItemToBeSelected = items.get(lIndex + 1);
        }
        else { // we're at the end, loop around to the start
            lItemToBeSelected = items.get(0);
        }

        selectItem(lItemToBeSelected);
        if (shownChildMenu != null) {
            doItemAction(lItemToBeSelected, false, true);
        }
    }

    private void selectPrevItem() {
        if (selectedItem == null) {
            return;
        }

        int lIndex = items.indexOf(selectedItem);
        // We know that selectedItem is set to an item that is contained in the
        // items collection.
        // Therefore, we know that index can never be -1.
        assert (lIndex != -1);

        MenuItem lItemToBeSelected;
        if (lIndex > 0) {
            lItemToBeSelected = items.get(lIndex - 1);

        }
        else { // we're at the start, loop around to the end
            lItemToBeSelected = items.get(items.size() - 1);
        }

        selectItem(lItemToBeSelected);
        if (shownChildMenu != null) {
            doItemAction(lItemToBeSelected, false, true);
        }
    }

    /**
     * Set the colspan of a {@link MenuItem} or {@link MenuItemSeparator}.
     * 
     * @param pItem
     *            the {@link MenuItem} or {@link MenuItemSeparator}
     * @param pColspan
     *            the colspan
     */
    private void setItemColSpan(UIObject pItem, int pColspan) {
        DOM.setElementPropertyInt(pItem.getElement(), "colSpan", pColspan);
    }
}

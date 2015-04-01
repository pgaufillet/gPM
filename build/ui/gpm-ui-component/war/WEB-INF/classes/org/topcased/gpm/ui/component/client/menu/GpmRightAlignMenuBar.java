/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import org.topcased.gpm.ui.component.client.resources.ComponentCssResource;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * This component is based on the Gwt MenuBar component. <br />
 * The sub menus are aligned from the right side of the opener component. <h3>
 * CSS style Rules</h3>
 * <dl>
 * <dt>gpm-GpmRightAlignMenuBar</dt>
 * <dd>Style of the GpmRightAlignMenuBar it self.</dd>
 * </dl>
 * 
 * @author frosier
 */
public class GpmRightAlignMenuBar extends MenuBar {
    protected final static ComponentCssResource CSS =
            ComponentResources.INSTANCE.css();

    /**
     * Creates an empty menu bar, oriented vertically.
     */
    public GpmRightAlignMenuBar() {
        super(true, true);
    }

    /**
     * Creates an empty menu bar.
     * 
     * @param pVertical
     *            <code>true</code> to orient the menu bar vertically
     */
    public GpmRightAlignMenuBar(final boolean pVertical) {
        super(pVertical, true);
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmRightAlignMenuBar());
    }

    /**
     * Adds a menu item in the bar.
     * 
     * @param pMenuName
     *            The menu's name.
     * @param pCommand
     *            The menu's command.
     * @return the added menu item.
     */
    public MenuItem addEntry(final String pMenuName, final Command pCommand) {
        final MenuItem lMenuItem = new MenuItem(pMenuName, true, pCommand);

        addItem(lMenuItem);
        lMenuItem.setStylePrimaryName(CSS.gpmMenuItem());

        return lMenuItem;
    }

    /**
     * Adds a menu item in the bar.
     * 
     * @param pMenuItem
     *            The sub-menu's item text.
     * @param pSubMenuBar
     *            The sub-menu to be displayed when it is selected.
     * @return the added sub menu.
     */
    public MenuItem addSubMenu(final String pMenuItem,
            final GpmRightAlignMenuBar pSubMenuBar) {
        final MenuItem lMenuItem = new MenuItem(pMenuItem, true, pSubMenuBar);

        addItem(lMenuItem);
        lMenuItem.setStylePrimaryName(CSS.gpmMenuItem());

        return lMenuItem;
    }

    /**
     * Override to add the "push" and "pull" behavior on buttons in the menu
     * bar.
     * 
     * @param pEvent
     *            The event caught when browsing on the bar.
     */
    @Override
    public void onBrowserEvent(final Event pEvent) {
        super.onBrowserEvent(pEvent);

        final MenuItem lMenuItem = findItem(DOM.eventGetTarget(pEvent));

        if (lMenuItem != null) {
            updateMenuItemStyle(pEvent, lMenuItem);
        }
    }

    private MenuItem findItem(Element pItem) {
        for (MenuItem lItem : getItems()) {
            if (DOM.isOrHasChild(lItem.getElement(), pItem)) {
                return lItem;
            }
        }
        return null;
    }

    /**
     * Update the menu item style.
     * 
     * @param pEvent
     *            The event.
     * @param pMenuItem
     *            The menu item.
     */
    protected void updateMenuItemStyle(final Event pEvent,
            final MenuItem pMenuItem) {
        switch (DOM.eventGetType(pEvent)) {
            case Event.ONMOUSEUP:
            case Event.ONMOUSEOVER:
                pMenuItem.addStyleName(CSS.gpmMenuItemSelected());
                break;
            case Event.ONCLICK:
            case Event.ONMOUSEOUT:
                pMenuItem.removeStyleName(CSS.gpmMenuItemSelected());
                break;
            default:
                //Do nothing
        }
    }
}
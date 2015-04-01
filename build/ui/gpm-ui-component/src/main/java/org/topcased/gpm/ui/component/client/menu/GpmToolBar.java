/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * This component based on GWT MenuBar open the popup of sub menus from right to
 * left.
 * <p>
 * It uses a command pattern, when adding an entry (an image button) in the bar,
 * a command must be provided, that will execute on click event.
 * </p>
 * 
 * @author frosier
 */
public class GpmToolBar extends GpmRightAlignMenuBar {
    private boolean needSeparator;

    private Command saveCommand;

    /**
     * Creates an empty ToolBar that begins with a separator. <br />
     * It is horizontally oriented from left to right for its menu items.
     */
    public GpmToolBar() {
        // Horizontal oriented menu bar
        super(false);

        setStylePrimaryName(CSS.gpmToolBar());

        // Adding of a separator before toolBar menu items
        needSeparator = true;

        // Activate animation
        setAnimationEnabled(false);

        // filtered events
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
                | Event.ONFOCUS | Event.ONKEYDOWN | Event.ONMOUSEDOWN
                | Event.ONMOUSEUP);
    }

    /**
     * Add an item entry in the tool bar.
     * 
     * @param pImageResource
     *            Icon of the menu.
     * @param pDescription
     *            The description of the item.
     * @param pCommand
     *            The command to execute on onClick() item event.
     * @return The added menu item.
     */
    public MenuItem addEntry(final ImageResource pImageResource,
            final String pDescription, final Command pCommand) {
        addSeparatorIfNeed();
        final AbstractImagePrototype lAbstractImagePrototype =
                AbstractImagePrototype.create(pImageResource);
        final MenuItem lMenuItem =
                super.addEntry(lAbstractImagePrototype.getHTML(), pCommand);

        lMenuItem.setTitle(pDescription);
        lMenuItem.setStylePrimaryName(CSS.gpmMenuButton());

        if (pDescription.equalsIgnoreCase("Save sheet")) {
            saveCommand = pCommand;
        }

        return lMenuItem;
    }

    /**
     * Add a sub menu in the Tool bar.
     * <p>
     * It constructs a new menu item that cascades to a sub-menu when it is
     * selected, and add it to the current Toolbar instance.
     * </p>
     * 
     * @param pImageResource
     *            Icon of this menu.
     * @param pDescription
     *            The tool-tip description displayed on over the item.
     * @param pSubMenuBar
     *            The sub-menu to be displayed when it is selected.
     * @return the added menu item.
     */
    public MenuItem addSubMenu(final ImageResource pImageResource,
            final String pDescription, final GpmRightAlignMenuBar pSubMenuBar) {
        addSeparatorIfNeed();
        final AbstractImagePrototype lAbstractImagePrototype =
                AbstractImagePrototype.create(pImageResource);
        final MenuItem lMenuItem =
                new MenuItem(lAbstractImagePrototype.getHTML(), true,
                        pSubMenuBar);

        lMenuItem.setTitle(pDescription);
        lMenuItem.setStylePrimaryName(CSS.gpmMenuButton());
        addItem(lMenuItem);

        return lMenuItem;
    }

    /**
     * The next button will be added on a separated tool bar.
     */
    public void addSeparatedToolBar() {
        needSeparator = true;
    }

    /**
     * Add a separator if needed
     */
    protected void addSeparatorIfNeed() {
        // Add separator only if need
        if (needSeparator) {
            addItem(new GpmMenuSeparator());
            needSeparator = false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.menu.GpmRightAlignMenuBar#updateMenuItemStyle(com.google.gwt.user.client.Event,
     *      com.google.gwt.user.client.ui.MenuItem)
     */
    @Override
    protected void updateMenuItemStyle(final Event pEvent,
            final MenuItem pMenuItem) {
        if (!(pMenuItem instanceof GpmMenuSeparator)) {
            switch (DOM.eventGetType(pEvent)) {
                case Event.ONMOUSEDOWN:
                    pMenuItem.addStyleName(CSS.gpmMenuButtonActive());
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOVER:
                    pMenuItem.addStyleName(CSS.gpmMenuButtonSelected());
                    break;
                case Event.ONCLICK:
                case Event.ONMOUSEOUT:
                    pMenuItem.removeStyleName(CSS.gpmMenuButtonActive());
                    pMenuItem.removeStyleName(CSS.gpmMenuButtonSelected());
                    break;
                default:
                    //Do nothing
            }
        }
    }

    /**
     * Execute the save command to save the sheet
     */
    public void executeSaveCommand() {
        saveCommand.execute();
    }
}
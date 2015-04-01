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

import org.topcased.gpm.ui.component.client.menu.GpmMenuLayoutPanel;
import org.topcased.gpm.ui.component.client.util.GpmDecoratorLayoutPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * It is a layout panel witch display vertically both following components :
 * <ul>
 * <li>the menu sub component</li>
 * <li>the content sub component</li>
 * </ul>
 * </p>
 * <p>
 * The separator between the menu and the content is positioned underneath the
 * sub menu component. Its position is calculated from the height of the sub
 * menu component.
 * </p>
 * 
 * @author frosier
 */
public class GpmLayoutPanelWithMenu extends LayoutPanel implements ISizeAware {
    private GpmMenuLayoutPanel menu;

    private Widget content;

    private static final int INITIAL_MENU_HEIGHT = 27;

    /**
     * Creates an empty LayoutPanel (content) with menu.
     * 
     * @param pDecorate
     *            If the menu is decorated.
     */
    public GpmLayoutPanelWithMenu(final boolean pDecorate) {
        super();
        menu = new GpmMenuLayoutPanel();
        if (pDecorate) {
            add(new GpmDecoratorLayoutPanel(menu));
        }
        else {
            add(menu);
        }
    }

    /**
     * Set the content.
     * 
     * @param pPanel
     *            The content.
     */
    public void setContent(final Widget pPanel) {
        if (content != null) {
            remove(content);
        }
        content = pPanel;
        add(pPanel);
        resize(INITIAL_MENU_HEIGHT);
    }

    /**
     * Get the menu.
     * 
     * @return The menu.
     */
    public GpmMenuLayoutPanel getMenu() {
        return menu;
    }

    /**
     * Get the content.
     * 
     * @return The content.
     */
    public Widget getContent() {
        return content;
    }

    /**
     * Resize the children considering the given height of the menu.
     * 
     * @param pMenuHeight
     *            The height of the menu.
     */
    private void resize(final int pMenuHeight) {
        setWidgetLeftRight(getWidget(0), 0, Unit.PX, 0, Unit.PX);
        setWidgetTopHeight(getWidget(0), 0, Unit.PX, pMenuHeight, Unit.PX);
        setWidgetLeftRight(getWidget(1), 0, Unit.PX, 0, Unit.PX);
        setWidgetTopBottom(getWidget(1), pMenuHeight, Unit.PX, 0, Unit.PX);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.LayoutPanel#onResize()
     */
    @Override
    public void onResize() {
        super.onResize();
        if (content != null) {
            final int lMenuHeight = menu.getHeight();

            if (lMenuHeight > 0) {
                resize(lMenuHeight);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        int lHeight = 0;

        if (menu != null) {
            lHeight += menu.getHeight();
        }
        if (content != null) {
            // TODO : check if it works (with last component !)
            lHeight += content.getOffsetHeight();
        }

        return lHeight;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        int lMinHeight = 0;
        if (menu != null) {
            lMinHeight = menu.getMinHeight();
        }
        return lMinHeight;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        int lMinMenuWidth = 0;
        if (menu != null) {
            lMinMenuWidth = menu.getMinWidth();
        }
        return lMinMenuWidth;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.client.component.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        // TODO check if it works !
        return getOffsetWidth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }
}
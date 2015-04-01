/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.tree;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * AbstractGpmTreeWidget
 * 
 * @author jeballar
 */
public abstract class AbstractGpmTreeWidget extends FlowPanel {

    /** Space between field values and icon in a sheet (in pixels) */
    private static final double FIELD_MARGIN = 4;

    private List<GpmTreeChoiceFieldItem> rootItems =
            new ArrayList<GpmTreeChoiceFieldItem>();

    /** The tree */
    private GpmDynamicTree<GpmTreeChoiceFieldItem> tree;

    /**
     * Add an item into the root values
     * 
     * @param pValue
     *            the item to add
     */
    public void addRootItem(GpmTreeChoiceFieldItem pValue) {
        rootItems.add(pValue);
    }

    /**
     * Initialize component
     */
    protected void init() {
        getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        getElement().getStyle().setProperty("clear", "left");
    }

    /**
     * Get the list of root items of the Tree
     * 
     * @return The list of root items of the Tree
     */
    public List<GpmTreeChoiceFieldItem> getRootItems() {
        return rootItems;
    }

    /**
     * Set styles of Widget in argument.
     * 
     * @param pWidget
     *            the widget to add style to
     */
    protected void initStyle(Widget pWidget) {
        pWidget.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        pWidget.getElement().getStyle().setMarginRight(FIELD_MARGIN, Unit.PX);
    }

    /**
     * Initialize the tree if necessary and returns it
     * 
     * @return Return the tree
     */
    protected GpmDynamicTree<GpmTreeChoiceFieldItem> getTree() {
        if (tree == null) {
            tree = new GpmDynamicTree<GpmTreeChoiceFieldItem>();
            tree.setDynamicTreeManager(registerTreeManager());
        }
        return tree;
    }

    /**
     * This method is called to register a manager for the Tree when it is
     * created
     * 
     * @return the manager for the tree
     */
    protected abstract GpmDynamicTreeManager<GpmTreeChoiceFieldItem> registerTreeManager();

    /**
     * Indicates if the widget enabled or not
     * 
     * @return <CODE>true</CODE> if the widget is enabled, else
     *         <CODE>false</CODE>
     */
    public abstract boolean isEnabled();

    /**
     * Set the widget enabled or not
     * 
     * @param pEnable
     *            if the widget must be enabled
     */
    public abstract void setEnabled(boolean pEnable);
}

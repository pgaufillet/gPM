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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * GpmMultivaluedTreeWidget
 * 
 * @author jeballar
 */
public class GpmMultivaluedTreeWidget extends AbstractGpmTreeWidget implements
        ClickHandler {

    private static final double POPUP_RATIO = .7;

    private FlowPanel pane = null;

    private GpmImageButton multivaluedButton;

    private GpmMultivaluedTreePopup multivaluedPopup = null;

    private List<GpmTreeChoiceFieldItem> selectedItems =
            new ArrayList<GpmTreeChoiceFieldItem>();

    /**
     * Constructor
     */
    public GpmMultivaluedTreeWidget() {
        super();
        init();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#init()
     */
    protected void init() {
        super.init();
        pane = new FlowPanel();
        multivaluedButton =
                new GpmImageButton(
                        ComponentResources.INSTANCE.images().treeIcon());

        multivaluedButton.addClickHandler(this);
        add(pane);
        add(multivaluedButton);
        initStyle(pane);
        initStyle(multivaluedButton);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#registerTreeManager()
     */
    @Override
    protected GpmDynamicTreeManager<GpmTreeChoiceFieldItem> registerTreeManager() {
        return new MultivaluedTreeChoiceFieldManager();
    }

    /**
     * {@inheritDoc}. This class is clickHandler only for multivalued field
     * button
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        if (multivaluedPopup == null) {
            GpmDynamicTree<GpmTreeChoiceFieldItem> lTree = getTree();
            lTree.setRootItems(getRootItems());
            multivaluedPopup = new GpmMultivaluedTreePopup(getTree());
            multivaluedPopup.setRatioSize(POPUP_RATIO, POPUP_RATIO);
            multivaluedPopup.addButton(CONSTANTS.ok()).addClickHandler(
                    new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent pEvent) {
                            selectedItems = multivaluedPopup.getSelectedItems();
                            setValues(selectedItems);
                            multivaluedPopup.hide();
                        }
                    });
        }
        multivaluedPopup.setValues(selectedItems);
        multivaluedPopup.center();
    }

    /**
     * Set the displayed values. <br/>
     * Note: This method does not set the selected values in Popup
     * 
     * @param pSelectedItems
     *            the selected items
     */
    private void setValues(List<GpmTreeChoiceFieldItem> pSelectedItems) {
        pane.clear();
        for (GpmTreeChoiceFieldItem lItem : pSelectedItems) {
            pane.add(new HTML(lItem.toString()));
        }
    }

    /**
     * get selected items
     * 
     * @return the selected items
     */
    public List<GpmTreeChoiceFieldItem> getSelectedItems() {
        return selectedItems;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return multivaluedButton.isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnable) {
        multivaluedButton.setEnabled(pEnable);
    }

    /**
     * Internal class for tree management
     */
    class MultivaluedTreeChoiceFieldManager extends
            GpmDynamicTreeManager<GpmTreeChoiceFieldItem> {

        @Override
        public List<GpmTreeChoiceFieldItem> createSubItems(
                GpmTreeChoiceFieldItem pItem) {
            return pItem.getSubItems();
        }

        @Override
        public void onSelection(GpmTreeChoiceFieldItem pItem) {
            // Do nothing
        }

        @Override
        public void onDoubleClick(GpmTreeChoiceFieldItem pItem) {
            GpmTreeChoiceFieldItem lItem =
                    (GpmTreeChoiceFieldItem) getTree().getSelectedItem();
            if (lItem != null) {
                multivaluedPopup.selectValue(lItem);
            }
        }
    }

    /**
     * Set the following value selected or not
     * 
     * @param pValue
     *            The value to change selection
     * @param pSelected
     *            Select or deselect the value
     */
    public void setValueSelected(String pValue, boolean pSelected) {
        setValueSelectedRecursive(getRootItems(), pValue, pSelected);
        setValues(selectedItems);
    }

    /**
     * Recurse into tree and select the value in argument. The method does
     * nothing if value is not in tree.
     * 
     * @param pItems
     *            the items to recurse on
     * @param pValue
     *            the value to select
     * @return <CODE>true</CODE> if a value has been
     */
    private boolean setValueSelectedRecursive(
            List<GpmTreeChoiceFieldItem> pItems, String pValue,
            boolean pSelected) {
        for (GpmTreeChoiceFieldItem lItem : pItems) {
            if (lItem.getValue().equals(pValue)) {
                if (multivaluedPopup != null) { // Popup initialized
                    if (pSelected && !lItem.isSelected()) {
                        multivaluedPopup.selectValue(lItem);
                    }
                    else if (!pSelected && lItem.isSelected()) {
                        multivaluedPopup.unSelectValue(lItem);
                    }
                }
                else { // Popup not initialized
                    if (!selectedItems.contains(lItem)) {
                        selectedItems.add(lItem);
                    }
                }
                return true;
            }
            else {
                if (setValueSelectedRecursive(lItem.getSubItems(), pValue,
                        pSelected)) {
                    return true;
                }
            }
        }
        return false;
    }
}

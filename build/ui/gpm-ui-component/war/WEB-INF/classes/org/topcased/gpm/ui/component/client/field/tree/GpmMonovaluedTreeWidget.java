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

import java.util.List;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * GpmMonovaluedTreeWidget
 * 
 * @author jeballar
 */
public class GpmMonovaluedTreeWidget extends AbstractGpmTreeWidget {

    private static final String STYLE_POPUP_CONTENT =
            ComponentResources.INSTANCE.css().gpmTreeChoiceFieldPopup();

    private static final String STYLE_FIELD =
            ComponentResources.INSTANCE.css().gpmTreeChoiceField();

    private static final double EMPTY_WIDTH = 200;

    private PopupPanel popup = null;

    private boolean enabled = true;

    private GpmTreeChoiceFieldItem selected = null;

    private HTML displayedText;

    /**
     * Constructor
     * 
     * @param pEnabled
     *            Indicate if the widget is editable by the user or not
     */
    public GpmMonovaluedTreeWidget(boolean pEnabled) {
        super();
        init();
        setEnabled(pEnabled);
        sinkEvents(Event.ONCLICK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#init()
     */
    protected void init() {
        super.init();
        displayedText = new HTML("&nbsp;");
        displayedText.getElement().getStyle().setWidth(EMPTY_WIDTH, Unit.PX);
        displayedText.setStyleName(STYLE_FIELD);
        add(displayedText);
        initStyle(displayedText);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#registerTreeManager()
     */
    @Override
    protected GpmDynamicTreeManager<GpmTreeChoiceFieldItem> registerTreeManager() {
        return new MonovaluedTreeChoiceFieldManager();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(Event pEvent) { // Only sinked event is ONCLICK
        super.onBrowserEvent(pEvent);
        if (!enabled) {
            return; // Widget is disabled, do nothing
        }
        if (popup == null) {
            GpmDynamicTree<GpmTreeChoiceFieldItem> lTree = getTree();
            lTree.setRootItems(getRootItems());
            popup = new PopupPanel(true);
            popup.clear();
            popup.addStyleName(STYLE_POPUP_CONTENT);
            ScrollPanel lPanel = new ScrollPanel();
            lPanel.add(lTree);
            popup.add(lPanel);
        }
        if (selected != null) {
            setValue(selected);
        }
        popup.setVisible(false); // mask, show, check the size, and unmask
        popup.setPixelSize((Window.getClientWidth() - getAbsoluteLeft()) / 2,
                Window.getClientHeight() / 2);
        popup.showRelativeTo(this);
        if (popup.getAbsoluteLeft() + popup.getOffsetWidth() > Window.getClientWidth()) {
            popup.setWidth(Window.getClientWidth() - popup.getAbsoluteLeft()
                    + "px");
        }
        if (popup.getAbsoluteTop() + popup.getOffsetHeight() > Window.getClientHeight()) {
            popup.setHeight(Window.getClientHeight() - popup.getAbsoluteTop()
                    + "px");
        }
        popup.setVisible(true);
    }

    /**
     * Set the value in argument as selected
     * 
     * @param pValue
     *            the value to select in display
     */
    private void setValue(final GpmTreeChoiceFieldItem pValue) {
        getTree().setSelectedItem(pValue, false);
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                getTree().expandTo(pValue);
            }
        });
    }

    /**
     * Set the content of the text label (the selected value)
     * 
     * @param pValue
     *            the text value
     */
    private void setTextValue(String pValue) {
        if (pValue == null || pValue.equals("")) {
            displayedText.getElement().getStyle().setWidth(EMPTY_WIDTH, Unit.PX);
        }
        else {
            displayedText.getElement().getStyle().clearWidth();
        }
        displayedText.setHTML(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.tree.AbstractGpmTreeWidget#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnable) {
        enabled = pEnable;
        if (pEnable) {
            displayedText.addStyleName(STYLE_FIELD);
        }
        else {
            displayedText.removeStyleName(STYLE_FIELD);
        }
    }

    /**
     * Internal class for Tree management
     */
    class MonovaluedTreeChoiceFieldManager extends
            GpmDynamicTreeManager<GpmTreeChoiceFieldItem> {

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#createSubItems(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
         */
        @Override
        public List<GpmTreeChoiceFieldItem> createSubItems(
                GpmTreeChoiceFieldItem pItem) {
            return pItem.getSubItems();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#onSelection(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
         */
        @Override
        public void onSelection(GpmTreeChoiceFieldItem pItem) {
            selectValue(pItem);
        }
    }

    /**
     * Select the value in argument
     * 
     * @param pItem
     *            the value to select
     */
    private void selectValue(GpmTreeChoiceFieldItem pItem) {
        setTextValue(pItem.toString());
        if (popup != null) {
            popup.hide();
        }
        selected = pItem;
    }

    /**
     * Get the represented value of the selected element
     * 
     * @return The represented value of the selected element
     */
    public String getValue() {
        if (selected == null) {
            return null;
        }
        return selected.getValue();
    }

    /**
     * Select the value in argument if it exists in the Tree, else does nothing
     * 
     * @param pValue
     *            the value to select
     */
    public void selectValue(String pValue) {
        selectRecursive(getRootItems(), pValue);
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
    private boolean selectRecursive(List<GpmTreeChoiceFieldItem> pItems,
            String pValue) {
        for (GpmTreeChoiceFieldItem lItem : pItems) {
            if (lItem.getValue().equals(pValue)) {
                selectValue(lItem);
                return true;
            }
            else {
                if (selectRecursive(lItem.getSubItems(), pValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}

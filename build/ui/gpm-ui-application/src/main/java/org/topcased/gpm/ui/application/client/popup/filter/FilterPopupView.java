/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.table.GpmTable;
import org.topcased.gpm.ui.component.client.table.GpmTablePager;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for execute a filter table on a popup.
 * 
 * @author tpanuel
 */
public class FilterPopupView extends PopupView implements FilterPopupDisplay {

    private final static double RATIO_WIDTH = 0.6;

    private final static double RATIO_HEIGHT = 0.6;

    private final ScrollPanel content;

    private final Button okButton;

    private final Button updateFilterButton;

    private HandlerRegistration okButtonHandler;

    private HandlerRegistration updateFilterButtonHandler;

    private GpmTable table;

    private boolean multivalued;

    private final GpmTablePager pager;

    /**
     * Create a product selection view.
     */
    public FilterPopupView() {
        super();
        content = new ScrollPanel();
        pager = new GpmTablePager();
        content.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        okButton = addButton("");
        updateFilterButton =
                addButton(Ui18n.CONSTANTS.filterPopupEditFilterButton());
        setContent(content);
        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#initTable()
     */
    @Override
    public void initTable() {
        final FlowPanel lPanel = new FlowPanel();

        table = new GpmTable(multivalued);
        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        lPanel.add(table);
        lPanel.add(pager);
        content.clear();
        content.setWidget(lPanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#addValuesColumn(java.lang.String)
     */
    @Override
    public void addValuesColumn(final String pColumnName) {
        table.addValuesColumn(pColumnName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay#setData(java.util.List)
     */
    @Override
    public void setData(final List<FilterResult> pData) {
        pager.initialize(table, pData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.FilterPopupDisplay#setMultivalued(boolean)
     */
    @Override
    public void setMultivalued(final boolean pMultivalued) {
        multivalued = pMultivalued;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.FilterPopupDisplay#setValidationButtonInfo(java.lang.String,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setValidationButtonInfo(final String pButtonText,
            final ClickHandler pClickHandler) {
        okButton.setText(pButtonText);
        if (okButtonHandler != null) {
            okButtonHandler.removeHandler();
        }
        okButtonHandler = okButton.addClickHandler(pClickHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.FilterPopupDisplay#setUpdateFilterButtonInfo(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setUpdateFilterButtonInfo(final ClickHandler pClickHandler) {
        if (updateFilterButtonHandler != null) {
            updateFilterButtonHandler.removeHandler();
        }
        updateFilterButtonHandler =
                updateFilterButton.addClickHandler(pClickHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.FilterPopupDisplay#getSelectedElementIds()
     */
    @Override
    public List<String> getSelectedElementIds() {
        if (table == null) {
            return new ArrayList<String>();
        }
        else {
            return table.getSelectedIds();
        }
    }
}
/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.table;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmHorizontalPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A pager for a gPM Table.
 * 
 * @author tpanuel
 */
public class GpmTablePager extends HorizontalPanel {
    private final static int PAGE_SIZE = 50;

    private final GpmHorizontalPanel pager;

    private final TextBox resultPerPage;

    private final HTML totalResult;

    private final GpmImageButton first;

    private final GpmImageButton previous;

    private final GpmImageButton next;

    private final GpmImageButton last;

    private final HTML currentPageLabel;

    private int pageSize;

    private int currentPage;

    private int nbPage;

    private GpmTable table;

    private List<FilterResult> data;

    /**
     * Create a GpmTablePager.
     */
    public GpmTablePager() {
        // Initialize total result
        final GpmHorizontalPanel lTotalResult = new GpmHorizontalPanel();
        final HTML lTotalResultLabel =
                new HTML(Ui18n.CONSTANTS.pagerNbResults());

        lTotalResultLabel.setStyleName(ComponentResources.INSTANCE.css().gpmTablePagerLabel());
        totalResult = new HTML();
        totalResult.setStyleName(ComponentResources.INSTANCE.css().gpmTablePagerLabel());
        lTotalResult.add(lTotalResultLabel);
        lTotalResult.add(totalResult);

        // Initialize pager
        pager = new GpmHorizontalPanel();
        currentPageLabel = new HTML();
        currentPageLabel.setStyleName(ComponentResources.INSTANCE.css().gpmTablePagerLabel());
        // Create buttons
        first =
                new GpmImageButton(null,
                        ComponentResources.INSTANCE.images().firstPage());
        previous =
                new GpmImageButton(null,
                        ComponentResources.INSTANCE.images().previousPage());
        next =
                new GpmImageButton(null,
                        ComponentResources.INSTANCE.images().nextPage());
        last =
                new GpmImageButton(null,
                        ComponentResources.INSTANCE.images().lastPage());
        // Set button description
        first.setTitle(Ui18n.CONSTANTS.pagerFirst());
        previous.setTitle(Ui18n.CONSTANTS.pagerPrevious());
        next.setTitle(Ui18n.CONSTANTS.pagerNext());
        last.setTitle(Ui18n.CONSTANTS.pagerLast());
        // Set button css
        first.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        previous.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        next.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        last.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        // Add button handler
        first.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                currentPage = 0;
                refresh();
            }
        });
        previous.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                currentPage--;
                refresh();
            }
        });
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                currentPage++;
                refresh();
            }
        });
        last.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                currentPage = nbPage - 1;
                refresh();
            }
        });

        // Initialize element by page
        final GpmHorizontalPanel lElementByPage = new GpmHorizontalPanel();
        final HTML lElementByPageLabel =
                new HTML(Ui18n.CONSTANTS.pagerResultsPerPage());
        final GpmImageButton lRefreshButton =
                new GpmImageButton(null,
                        ComponentResources.INSTANCE.images().refresh());

        lElementByPageLabel.setStyleName(ComponentResources.INSTANCE.css().gpmTablePagerLabel());
        resultPerPage = new TextBox();
        resultPerPage.addStyleName(ComponentResources.INSTANCE.css().gpmTablePagerTextBox());
        lRefreshButton.setTitle(Ui18n.CONSTANTS.pagerRefresh());
        lRefreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                try {
                    pageSize = Integer.valueOf(resultPerPage.getValue());
                    if (pageSize < 1) {
                        pageSize = PAGE_SIZE;
                        resultPerPage.setValue(String.valueOf(PAGE_SIZE));
                    }
                }
                catch (NumberFormatException e) {
                    pageSize = PAGE_SIZE;
                    resultPerPage.setValue(String.valueOf(PAGE_SIZE));
                }
                initPagger();
            }
        });
        lRefreshButton.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        lElementByPage.add(lElementByPageLabel);
        lElementByPage.add(resultPerPage);
        lElementByPage.add(lRefreshButton);

        // Add elements
        add(lTotalResult);
        add(pager);
        add(lElementByPage);
        setStyleName(ComponentResources.INSTANCE.css().gpmTablePager());
    }

    /**
     * Initialize the pager and the table.
     * 
     * @param pTable
     *            The gPM table.
     * @param pData
     *            The data.
     */
    public void initialize(final GpmTable pTable, final List<FilterResult> pData) {
        table = pTable;
        data = pData;
        pageSize = PAGE_SIZE;
        resultPerPage.setValue(String.valueOf(PAGE_SIZE));
        initPagger();
    }

    private void initPagger() {
        currentPage = 0;
        nbPage = ((data.size() - 1) / pageSize) + 1;
        refresh();
    }

    private void refresh() {
        // Refresh table
        final List<FilterResult> lDisplayedElements =
                new ArrayList<FilterResult>(pageSize);
        final int lFirstElement = currentPage * pageSize;
        int lLastElement = lFirstElement + pageSize;

        if (lLastElement > data.size()) {
            lLastElement = data.size();
        }
        for (int i = lFirstElement; i < lLastElement; i++) {
            lDisplayedElements.add(data.get(i));
        }
        totalResult.setHTML(String.valueOf(data.size()));
        table.setData(lDisplayedElements);
        // Refresh pager
        pager.clear();
        if (nbPage > 1) {
            if (currentPage > 0) {
                pager.add(first);
                pager.add(previous);
            }
            else {
                pager.add(createEmptyButton(first));
                pager.add(createEmptyButton(previous));
            }
            currentPageLabel.setHTML((currentPage + 1) + "/" + nbPage);
            pager.add(currentPageLabel);
            if ((currentPage + 1) < nbPage) {
                pager.add(next);
                pager.add(last);
            }
            else {
                pager.add(createEmptyButton(next));
                pager.add(createEmptyButton(last));
            }
        }
    }

    private Widget createEmptyButton(final GpmImageButton pButton) {
        final SimplePanel lEmpty = new SimplePanel();

        lEmpty.setWidth(pButton.getWidth() + "px");
        lEmpty.getElement().setInnerHTML("&nbsp;");

        return lEmpty;
    }
}

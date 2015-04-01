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
import org.topcased.gpm.business.util.search.FilterResultId;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.GpmTableResources;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * A gPM Table.
 * 
 * @author tpanuel
 */
public class GpmTable extends CellTable<FilterResult> {
    private final static String EMPTY = "&nbsp;";

    private final static GpmTableResources TABLE_RESOURCES =
            new GpmTableResources();

    private final GpmCompositeColumn firstColumn;

    private final Column<FilterResult, FilterResultId> selectionColumn;

    private final GpmSelectAllHeader header;

    private int nbValuesColumns;

    /**
     * Create a gPM table.
     * 
     * @param pMultivalued
     *            If several rows can be selected.
     */
    public GpmTable(final boolean pMultivalued) {
        super(0, TABLE_RESOURCES);

        firstColumn = new GpmCompositeColumn();
        // If the several rows can be selected -> check box
        if (pMultivalued) {
            selectionColumn = new GpmCheckBoxColumn();
            header = new GpmSelectAllHeader();
            firstColumn.addSubColumn(selectionColumn);
            addColumn(firstColumn, header);
            // Set handler on check box
            ((GpmCheckBoxCell) selectionColumn.getCell()).setHandler(new GpmCheckBoxHandler() {
                @Override
                public void onClick(final boolean pSelected) {
                    header.getCell().selectAll(false);
                }
            });
            header.getCell().setHandler(new GpmCheckBoxHandler() {
                @Override
                public void onClick(final boolean pSelected) {
                    ((GpmCheckBoxColumn) selectionColumn).selectAll(!pSelected);
                }
            });
        }
        // Else -> radio button
        else {
            selectionColumn = new GpmRadioButtonColumn();
            header = null;
            firstColumn.addSubColumn(selectionColumn);
            addColumn(firstColumn, EMPTY);
        }
        nbValuesColumns = 0;
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTable());
    }

    /**
     * Add a column.
     * 
     * @param pColumnName
     *            The name of the column.
     */
    public void addValuesColumn(final String pColumnName) {
        addColumn(new GpmValuesColumn(nbValuesColumns), pColumnName);
        nbValuesColumns++;
    }

    /**
     * Set data.
     * 
     * @param pData
     *            The data
     */
    public void setData(final List<FilterResult> pData) {
        if (selectionColumn instanceof GpmCheckBoxColumn) {
            // Clear displayed elements list
            ((GpmCheckBoxCell) selectionColumn.getCell()).clearDisplayedElements();

            // Unselect all checkbox
            header.getCell().selectAll(false);
            ((GpmCheckBoxColumn) selectionColumn).selectAll(false);

            // Set the father elements to the check box columns
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    final Element lParent = getElement();

                    for (int i = 0; i < lParent.getChildCount(); i++) {
                        final Element lSubElement =
                                (Element) lParent.getChild(i);

                        if (lSubElement.getNodeName().equalsIgnoreCase("THEAD")) {
                            header.getCell().setFather(lSubElement);
                        }
                    }
                    for (int i = 0; i < lParent.getChildCount(); i++) {
                        final Element lSubElement =
                                (Element) lParent.getChild(i);

                        if (lSubElement.getNodeName().equalsIgnoreCase("TBODY")
                                && lSubElement.getChildCount() > 0
                                && !lSubElement.getStyle().getDisplay().equalsIgnoreCase(
                                        "none")) {
                            ((GpmCheckBoxCell) selectionColumn.getCell()).setFather(lSubElement);
                        }
                    }
                }
            });
        }
        else if (selectionColumn instanceof GpmRadioButtonColumn) {
            ((GpmRadioButtonCell) selectionColumn.getCell()).clearSelectedId();
        }

        setPageSize(pData.size());
        setData(0, pData.size(), pData);
    }

    /**
     * Add a handlered column.
     * 
     * @param pVisu
     *            If it's to open the element on visu mode.
     * @param pHandler
     *            The handler.
     */
    public void addHandleredColumn(final boolean pVisu,
            final GpmBasicActionHandler<String> pHandler) {
        final GpmImageColumn lGpmImageColumn = new GpmImageColumn(pVisu);

        lGpmImageColumn.setHandler(pHandler);
        firstColumn.addSubColumn(lGpmImageColumn);
    }

    /**
     * Get the selected elements id.
     * 
     * @return The selected elements id.
     */
    public List<String> getSelectedIds() {
        if (selectionColumn instanceof GpmCheckBoxColumn) {
            return ((GpmCheckBoxColumn) selectionColumn).getSelectedIds();
        }
        else {
            final List<String> lSelectedValues = new ArrayList<String>();
            final String lSelectedValue =
                    ((GpmRadioButtonCell) selectionColumn.getCell()).getSelectedId();

            if (lSelectedValue != null) {
                lSelectedValues.add(lSelectedValue);
            }

            return lSelectedValues;
        }
    }
}
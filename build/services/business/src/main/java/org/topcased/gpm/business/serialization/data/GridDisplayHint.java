/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Represents a GridDisplayHint element in the schema.
 * 
 * @author mkargbo
 */
@XStreamAlias("gridDisplayHint")
public class GridDisplayHint extends DisplayHint {

    /** serialVersionUID. */
    private static final long serialVersionUID = -3232984682776190891L;

    /** The width. */
    @XStreamAlias("width")
    @XStreamAsAttribute
    private int width;

    /** The height. */
    @XStreamAlias("height")
    @XStreamAsAttribute
    private int height;

    /** The column separator. */
    @XStreamAlias("columnSeparator")
    @XStreamAsAttribute
    private String columnSeparator;

    /** The columns. */
    @XStreamImplicit(itemFieldName = "gridColumn")
    private List<GridColumn> columns;

    /**
     * Gets the width.
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     * 
     * @param pWidth
     *            the new width
     */
    public void setWidth(int pWidth) {
        width = pWidth;
    }

    /**
     * Gets the height.
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height.
     * 
     * @param pHeight
     *            the new height
     */
    public void setHeight(int pHeight) {
        height = pHeight;
    }

    /**
     * Gets the column separator.
     * 
     * @return the column separator
     */
    public String getColumnSeparator() {
        return columnSeparator;
    }

    /**
     * Sets the column separator.
     * 
     * @param pColumnSeparator
     *            the new column separator
     */
    public void setColumnSeparator(String pColumnSeparator) {
        columnSeparator = pColumnSeparator;
    }

    /**
     * Gets the columns.
     * 
     * @return the columns
     */
    public List<GridColumn> getColumns() {
        return columns;
    }

    /**
     * Sets the columns.
     * 
     * @param pColumns
     *            the new columns
     */
    public void setColumns(List<GridColumn> pColumns) {
        columns = pColumns;
    }
}

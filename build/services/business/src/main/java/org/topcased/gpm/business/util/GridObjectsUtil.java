/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.facilities.GridColumnData;
import org.topcased.gpm.business.facilities.GridDisplayHintData;
import org.topcased.gpm.domain.facilities.GridColumn;
import org.topcased.gpm.domain.facilities.GridColumnEditorType;
import org.topcased.gpm.domain.facilities.GridDisplayHint;

/**
 * Util for Grid object Conversion methods
 * 
 * @author mkargbo
 */
public class GridObjectsUtil {

    /**
     * Convert a GridColumn data object to a new GridColumn entity object
     * 
     * @param pGridColumnData
     *            Data object
     * @return A new data object.
     */
    public static org.topcased.gpm.domain.facilities.GridColumn createGridColumn(
            GridColumnData pGridColumnData) {
        GridColumn lGridColumn = GridColumn.newInstance();

        lGridColumn.setName(pGridColumnData.getName());
        lGridColumn.setEditorType(pGridColumnData.getEditorType());

        return lGridColumn;
    }

    /**
     * Convert a GridColumn entity to a new GridColumn data object.
     * 
     * @param pGridColumn
     *            Entity object
     * @return A new data object.
     */
    public static GridColumnData createGridColumnData(
            org.topcased.gpm.domain.facilities.GridColumn pGridColumn) {

        GridColumnData lGridColumnData = new GridColumnData();
        lGridColumnData.setName(pGridColumn.getName());
        lGridColumnData.setEditorType(pGridColumn.getEditorType());

        return lGridColumnData;
    }

    /**
     * Convert a GridDisplayHint data object to a new GridDisplayHint entity.
     * 
     * @param pGridDisplayHintData
     *            Data object
     * @return A new entity
     */
    public static org.topcased.gpm.domain.facilities.GridDisplayHint createGridDisplayHint(
            GridDisplayHintData pGridDisplayHintData) {
        GridDisplayHint lGridDisplayHint = GridDisplayHint.newInstance();

        lGridDisplayHint.setWidth(pGridDisplayHintData.getWidth());
        lGridDisplayHint.setHeight(pGridDisplayHintData.getHeight());
        lGridDisplayHint.setColumnSeparator(pGridDisplayHintData.getColumnSeparator());

        ArrayList<GridColumn> lGridColumns =
                new ArrayList<GridColumn>(
                        pGridDisplayHintData.getColumns().length);

        for (GridColumnData lGridColumnData : pGridDisplayHintData.getColumns()) {
            GridColumn lGridColumn = createGridColumn(lGridColumnData);
            lGridColumns.add(lGridColumn);
        }
        lGridDisplayHint.setColumns(lGridColumns);

        return lGridDisplayHint;
    }

    /**
     * Convert a GridDisplayHint data object to the GridDisplayHint entity. The
     * entity is updated.
     * 
     * @param pGridDisplayHintData
     *            Data object
     * @param pGridDisplayHint
     *            The entity that will be updated.
     */
    public static void updateGridDisplayHint(
            GridDisplayHintData pGridDisplayHintData,
            final GridDisplayHint pGridDisplayHint) {

        pGridDisplayHint.setWidth(pGridDisplayHintData.getWidth());
        pGridDisplayHint.setHeight(pGridDisplayHintData.getHeight());
        pGridDisplayHint.setColumnSeparator(pGridDisplayHintData.getColumnSeparator());

        ArrayList<GridColumn> lGridColumns =
                new ArrayList<GridColumn>(
                        pGridDisplayHintData.getColumns().length);

        for (GridColumnData lGridColumnData : pGridDisplayHintData.getColumns()) {
            GridColumn lGridColumn = createGridColumn(lGridColumnData);
            lGridColumns.add(lGridColumn);
        }
        pGridDisplayHint.setColumns(lGridColumns);
    }

    /**
     * Convert a GridDisplayHint entity to a new GridDisplayHint data object.
     * 
     * @param pGridDisplayHint
     *            Entity object
     * @return A new data object.
     */
    public static GridDisplayHintData createGridDisplayHintData(
            org.topcased.gpm.domain.facilities.GridDisplayHint pGridDisplayHint) {
        GridDisplayHintData lGridDisplayHintData = new GridDisplayHintData();

        lGridDisplayHintData.setWidth(pGridDisplayHint.getWidth());
        lGridDisplayHintData.setHeight(pGridDisplayHint.getHeight());
        lGridDisplayHintData.setColumnSeparator(pGridDisplayHint.getColumnSeparator());

        GridColumnData[] lGridColumnDatas =
                new GridColumnData[pGridDisplayHint.getColumns().size()];

        int i = 0;
        for (GridColumn lGridColumn : pGridDisplayHint.getColumns()) {
            GridColumnData lGridColumnData = createGridColumnData(lGridColumn);
            lGridColumnDatas[i] = lGridColumnData;
            i++;
        }
        lGridDisplayHintData.setColumns(lGridColumnDatas);

        return lGridDisplayHintData;
    }

    /**
     * Convert a GridColumn serialization object to a new GridColumn data object
     * 
     * @param pSerializationDataObject
     *            Serialization object
     * @return A new data object.
     */
    public static GridColumnData createGridColumnData(
            org.topcased.gpm.business.serialization.data.GridColumn pSerializationDataObject) {
        GridColumnData lDataObject = new GridColumnData();

        lDataObject.setName(pSerializationDataObject.getName());
        lDataObject.setEditorType(
                GridColumnEditorType.fromString(pSerializationDataObject.getEditorType()));

        return lDataObject;
    }

    /**
     * Convert a GridColumn entity to a GridColumn serialization data.
     * 
     * @param pEntity
     *            Entity object
     * @return New serialization object.
     */
    public static org.topcased.gpm.business.serialization.data.GridColumn
    createGridColumnSerializationData(GridColumn pEntity) {
        org.topcased.gpm.business.serialization.data.GridColumn lSerializationDataObject =
                new org.topcased.gpm.business.serialization.data.GridColumn();
        lSerializationDataObject.setName(pEntity.getName());
        lSerializationDataObject.setEditorType(pEntity.getEditorType().getValue());

        return lSerializationDataObject;
    }

    /**
     * Convert a GridDisplayHint entity to a new GridDisplayHint serialization
     * object
     * 
     * @param pFacilitesObject
     *            Entity object
     * @return A new GridDisplayHint serialization object.
     */
    public static org.topcased.gpm.business.serialization.data.GridDisplayHint
    createGridDisplayHintSerializationData(GridDisplayHint pFacilitesObject) {
        org.topcased.gpm.business.serialization.data.GridDisplayHint lSerializationDataObject =
                new org.topcased.gpm.business.serialization.data.GridDisplayHint();

        lSerializationDataObject.setWidth(pFacilitesObject.getWidth());
        lSerializationDataObject.setHeight(pFacilitesObject.getHeight());
        lSerializationDataObject.setColumnSeparator(pFacilitesObject.getColumnSeparator());

        List<org.topcased.gpm.business.serialization.data.GridColumn> lGridColumnObjects =
                new ArrayList<org.topcased.gpm.business.serialization.data.GridColumn>(
                        pFacilitesObject.getColumns().size());
        for (org.topcased.gpm.domain.facilities.GridColumn lGridColumnFacilitiesObject :
                pFacilitesObject.getColumns()) {
            org.topcased.gpm.business.serialization.data.GridColumn lGridColumnData =
                    createGridColumnSerializationData(lGridColumnFacilitiesObject);
            lGridColumnObjects.add(lGridColumnData);
        }
        lSerializationDataObject.setColumns(lGridColumnObjects);

        return lSerializationDataObject;
    }

    /**
     * Convert a GridDisplayHint serialization object to a new GridDisplayHint
     * data object.
     * 
     * @param pSerializationDataObject
     *            Serialization object
     * @return A new data object.
     */
    public static GridDisplayHintData createGridDisplayHintData(
            org.topcased.gpm.business.serialization.data.GridDisplayHint pSerializationDataObject) {
        GridDisplayHintData lDataObject = new GridDisplayHintData();

        lDataObject.setWidth(pSerializationDataObject.getWidth());
        lDataObject.setHeight(pSerializationDataObject.getHeight());
        lDataObject.setColumnSeparator(pSerializationDataObject.getColumnSeparator());

        GridColumnData[] lGridColumnDataObjects =
                new GridColumnData[pSerializationDataObject.getColumns().size()];
        int i = 0;
        for (org.topcased.gpm.business.serialization.data.GridColumn lGridColumnSerializationData :
                pSerializationDataObject.getColumns()) {
            GridColumnData lGridColumnDataObject =
                    createGridColumnData(lGridColumnSerializationData);
            lGridColumnDataObjects[i] = lGridColumnDataObject;
            i++;
        }
        lDataObject.setColumns(lGridColumnDataObjects);

        return lDataObject;
    }
}

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

import org.topcased.gpm.business.util.search.FilterResultId;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * An image cell for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmImageCell extends AbstractCell<FilterResultId> {
    private final static String VISU_IMG =
            initImage(ComponentResources.INSTANCE.images().sheetVisu(),
                    Ui18n.CONSTANTS.visualize());

    private final static String EDIT_IMG =
            initImage(ComponentResources.INSTANCE.images().sheetEdit(),
                    Ui18n.CONSTANTS.edit());

    private final static String initImage(final ImageResource pResource,
            String pTitle) {
        final Image lImage = new Image(pResource);

        lImage.setTitle(pTitle);
        lImage.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTableImage());

        return lImage.toString();
    }

    private final static String CLICK_EVENT = "click";

    private final boolean visu;

    private GpmBasicActionHandler<String> handler;

    /**
     * Create a GpmImageCell.
     * 
     * @param pVisu
     *            If it's to open the element on visu mode.
     */
    public GpmImageCell(final boolean pVisu) {
        visu = pVisu;
    }

    /**
     * Set the handler call when the image is clicked.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setHandler(final GpmBasicActionHandler<String> pHandler) {
        handler = pHandler;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.dom.client.Element,
     *      java.lang.Object, java.lang.Object,
     *      com.google.gwt.dom.client.NativeEvent,
     *      com.google.gwt.cell.client.ValueUpdater)
     */
    @Override
    public Object onBrowserEvent(final Element pParent,
            final FilterResultId pValue, final Object pViewData,
            final NativeEvent pEvent,
            final ValueUpdater<FilterResultId> pValueUpdater) {
        if (pEvent.getType().equals(CLICK_EVENT)) {
            if (pValue.isOnlyInfo() || (visu && pValue.getVisuLocker() == null)
                    || (!visu && pValue.getEditLocker() == null)) {
                handler.execute(pValue.getId());
            }
        }

        return pViewData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#render(java.lang.Object,
     *      java.lang.Object, java.lang.StringBuilder)
     */
    @Override
    public void render(final FilterResultId pValue, final Object pViewData,
            final StringBuilder pSb) {
        if (visu) {
            if (pValue.getVisuLocker() == null) {
                pSb.append(VISU_IMG);
            }
            else {
                pSb.append(initImage(
                        ComponentResources.INSTANCE.images().sheetLock(),
                        pValue.getVisuLocker()));
            }

        }
        else {
            if (pValue.getEditLocker() == null) {
                pSb.append(EDIT_IMG);
            }
            else {
                pSb.append(initImage(
                        ComponentResources.INSTANCE.images().sheetLock(),
                        pValue.getEditLocker()));
            }
        }
    }
}
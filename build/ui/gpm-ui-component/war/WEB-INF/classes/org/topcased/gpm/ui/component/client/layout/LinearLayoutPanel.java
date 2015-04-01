/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Linear layout panel.
 * 
 * @author jeballar
 */
public class LinearLayoutPanel extends FlowPanel implements RequiresResize,
        ProvidesResize {

    /**
     * Layout data.
     */
    public static class LayoutData {

        private double size = -1;

        private SimplePanel container;

        /**
         * Constructor
         * 
         * @param pContainer
         *            container
         * @param pSize
         *            size
         */
        public LayoutData(SimplePanel pContainer, double pSize) {
            container = pContainer;
            size = pSize;
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.FlowPanel#add(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void add(Widget pWidget) {
        super.add(initializeWidget(pWidget));
        doLayout();
    }

    /**
     * Constructor
     */
    public LinearLayoutPanel() {
        this(false);
    }

    /**
     * Constructor
     * 
     * @param pVertical
     *            Indicates if the layout direction must be vertical
     */
    public LinearLayoutPanel(boolean pVertical) {
        if (pVertical) {
            setLayoutDirection(LayoutDirection.VERTICAL);
        }
    }

    /**
     * Layout direction.
     */
    public static enum LayoutDirection {
        HORIZONTAL, VERTICAL
    }

    private LayoutDirection layoutDirection = LayoutDirection.HORIZONTAL;

    /**
     * Initialize widget.
     * 
     * @param pWidget
     *            Widget
     * @return Container
     */
    private SimplePanel initializeWidget(Widget pWidget) {
        Object lOldLayout = pWidget.getLayoutData();
        double lSize = -1;
        if (lOldLayout instanceof LayoutData) {
            lSize = ((LayoutData) lOldLayout).size;
        }

        SimplePanel lContainer = new SimplePanel();
        LayoutData lLayoutData = new LayoutData(lContainer, lSize);
        pWidget.setLayoutData(lLayoutData);
        lContainer.setLayoutData(lLayoutData);

        initializeContainer(lContainer);

        Style lStyle = pWidget.getElement().getStyle();
        lStyle.setPosition(Position.ABSOLUTE);
        lStyle.setTop(0, Unit.PX);
        lStyle.setBottom(0, Unit.PX);
        lStyle.setLeft(0, Unit.PX);
        lStyle.setRight(0, Unit.PX);

        lContainer.setWidget(pWidget);
        return lContainer;
    }

    /**
     * Initialize container.
     * 
     * @param pContainer
     *            Container
     */
    private void initializeContainer(Widget pContainer) {
        Style lStyle = pContainer.getElement().getStyle();
        lStyle.setPosition(Position.RELATIVE);

        if (layoutDirection.equals(LayoutDirection.HORIZONTAL)) {
            if (!LocaleInfo.getCurrentLocale().isRTL()) {
                lStyle.setFloat(Float.LEFT);
            }
            else {
                lStyle.setFloat(Float.RIGHT);
            }
            lStyle.setHeight(100, Unit.PCT);
        }
        else {
            lStyle.setWidth(100, Unit.PCT);
        }
        lStyle.setOverflow(Overflow.HIDDEN);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ComplexPanel#remove(int)
     */
    @Override
    public boolean remove(int pIndex) {
        SimplePanel lSimplePanel = (SimplePanel) super.getWidget(pIndex);
        Widget lWidget = lSimplePanel.getWidget();
        boolean lRemoved = super.remove(pIndex);
        lSimplePanel.remove(lWidget);
        doLayout();
        return lRemoved;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ComplexPanel#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget pWidget) {
        pWidget.removeFromParent();
        LayoutData lLayout = getWidgetLayoutData(pWidget);
        if (lLayout != null) {
            boolean lRemove = super.remove(lLayout.container);
            doLayout();
            return lRemove;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.FlowPanel#insert(com.google.gwt.user.client.ui.Widget,
     *      int)
     */
    @Override
    public void insert(Widget pWidget, int pBeforeIndex) {
        super.insert(initializeWidget(pWidget), pBeforeIndex);
        doLayout();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ComplexPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public int getWidgetIndex(Widget pChild) {
        return super.getWidgetIndex(((LayoutData) pChild.getLayoutData()).container);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ComplexPanel#getWidget(int)
     */
    @Override
    public Widget getWidget(int pIndex) {
        return ((SimplePanel) super.getWidget(pIndex)).getWidget();
    }

    private final ArrayList<LayoutData> cache = new ArrayList<LayoutData>();

    /**
     * Do layout.
     */
    private void doLayout() {
        cache.clear();
        double lFractionableSize = 100;
        for (Widget lWidget : this) {
            LayoutData lLayoutData = (LayoutData) lWidget.getLayoutData();
            double lSize = lLayoutData.size;
            if (lSize >= 0) {
                if (layoutDirection.equals(LayoutDirection.HORIZONTAL)) {
                    lLayoutData.container.setWidth(lSize + "%");
                }
                else {
                    lLayoutData.container.setHeight(lSize + "%");
                }
                lFractionableSize -= lSize;
            }
            else {
                cache.add(lLayoutData);
            }
        }
        int lRest = (int) (lFractionableSize / cache.size());
        int lExtra = (int) (lFractionableSize % cache.size());

        for (LayoutData lLayoutData : cache) {
            int lSize = lRest;
            if (lExtra > 0) {
                lSize++;
                lExtra--;
            }
            String lRestSize = lSize + "%";
            if (layoutDirection.equals(LayoutDirection.HORIZONTAL)) {
                lLayoutData.container.setWidth(lRestSize);
            }
            else {
                lLayoutData.container.setHeight(lRestSize);
            }
        }
    }

    /**
     * Set cell size.
     * 
     * @param pWidget
     *            Widget
     * @param pSize
     *            size
     * @param pUnit
     *            Unit
     */
    public void setCellSize(Widget pWidget, int pSize, Unit pUnit) {
        LayoutData lLayoutData = getWidgetLayoutData(pWidget);
        assert pUnit.equals(Unit.PX) || pUnit.equals(Unit.PCT) : "The only units allowed are PX and PCT";
        if (pUnit.equals(Unit.PX)) {
            lLayoutData.size = pSize * 100 / getOffsetWidth();
        }
        else {
            lLayoutData.size = pSize;
        }
        doLayout();
    }

    /**
     * Get widget layout data.
     * 
     * @param pWidget
     *            Widget
     * @return Layout data
     */
    private LayoutData getWidgetLayoutData(Widget pWidget) {
        LayoutData lLayoutData = (LayoutData) pWidget.getLayoutData();
        if (lLayoutData == null) {
            return null;
        }
        assert getWidgetIndex(lLayoutData.container) != -1 : "Widget isn't child of this panel";
        return lLayoutData;
    }

    /**
     * Restore the cell width to auto.
     * 
     * @param pWidget
     *            Widget
     */
    public void setCellWidthAuto(Widget pWidget) {
        LayoutData lWidgetLayoutData = getWidgetLayoutData(pWidget);
        lWidgetLayoutData.size = (-1);
        doLayout();
    }

    /**
     * Sets the layout direction
     * 
     * @param pLayoutDirection
     *            the layoutDirection to set
     */
    public void setLayoutDirection(LayoutDirection pLayoutDirection) {
        this.layoutDirection = pLayoutDirection;
        for (Widget lWidget : this) {
            initializeContainer(lWidget);
        }
        doLayout();
    }

    /**
     * Return the layout direction
     * 
     * @return the layoutDirection
     */
    public LayoutDirection getLayoutDirection() {
        return layoutDirection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.FlowPanel#clear()
     */
    @Override
    public void clear() {
        for (Widget lPanel : this) {
            try {
                SimplePanel lSimplePanel = (SimplePanel) lPanel;
                Widget lWidget = lSimplePanel.getWidget();
                lSimplePanel.remove(lWidget);
            }
            catch (Exception lE) {
                // TODO Remove  this
                // NOTHING
            }
        }
        super.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.RequiresResize#onResize()
     */
    @Override
    public void onResize() {
        for (Widget lChild : getChildren()) {
            if (lChild instanceof RequiresResize) {
                ((RequiresResize) lChild).onResize();
            }
        }
    }
}

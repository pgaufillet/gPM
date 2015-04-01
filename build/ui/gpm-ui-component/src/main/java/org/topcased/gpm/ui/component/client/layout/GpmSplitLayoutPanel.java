/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmSplitLayoutPanel
 * 
 * @author jeballar
 */
public class GpmSplitLayoutPanel extends DockLayoutPanel {
    /** Default splitter size */
    private static final int DEFAULT_SPLITTER_SIZE = 3;

    /** Global style */
    private static final String STYLE_GPM_SPLIT_LAYOUT_PANEL =
            ComponentResources.INSTANCE.css().gpmSplitLayoutPanel();

    private static final String STYLE_GPM_SPLIT_LAYOUT_HORIZONTAL_DRAGGER =
            ComponentResources.INSTANCE.css().gpmSplitLayoutHorizontalDragger();

    private static final String STYLE_GPM_SPLIT_LAYOUT_VERTICAL_DRAGGER =
            ComponentResources.INSTANCE.css().gpmSplitLayoutVerticalDragger();

    /** Size of the splitters */
    protected final int splitterSize;

    /**
     * Default constructor
     */
    public GpmSplitLayoutPanel() {
        super(Unit.PX);
        splitterSize = DEFAULT_SPLITTER_SIZE;
        setStyleName(STYLE_GPM_SPLIT_LAYOUT_PANEL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.DockLayoutPanel#insert(com.google.gwt.user.client.ui.Widget,
     *      com.google.gwt.user.client.ui.DockLayoutPanel.Direction, double,
     *      com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void insert(Widget pChild, Direction pDirection, double pSize,
            Widget pBefore) {
        super.insert(pChild, pDirection, pSize, pBefore);
        if (pDirection != Direction.CENTER) {
            insertSplitter(pBefore);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.DockLayoutPanel#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget pChild) {
        assert !(pChild instanceof Splitter) : "Splitters may not be directly removed";

        if (super.remove(pChild)) {
            // Remove the associated splitter, if any.
            int lIdx = getWidgetIndex(pChild);
            if (lIdx < getWidgetCount() - 1) {
                super.remove(lIdx + 1);
            }
            return true;
        }
        return false;
    }

    /**
     * Sets the minimum allowable size for the given widget.
     * <p>
     * Its assocated splitter cannot be dragged to a position that would make it
     * smaller than this size. This method has no effect for the
     * {@link DockLayoutPanel.Direction#CENTER} widget.
     * </p>
     * 
     * @param pChild
     *            the child whose minimum size will be set
     * @param pMinSize
     *            the minimum size for this widget
     */
    public void setWidgetMinSize(Widget pChild, int pMinSize) {
        Splitter lSplitter = getAssociatedSplitter(pChild);
        lSplitter.setMinSize(pMinSize);
    }

    /**
     * Sets the maximum allowable size for the given widget.
     * <p>
     * Its assocated splitter cannot be dragged to a position that would make it
     * bigger than this size. This method has no effect for the
     * {@link DockLayoutPanel.Direction#CENTER} widget.
     * </p>
     * 
     * @param pChild
     *            the child whose minimum size will be set
     * @param pMaxSize
     *            the minimum size for this widget
     */
    public void setWidgetMaxSize(Widget pChild, int pMaxSize) {
        Splitter lSplitter = getAssociatedSplitter(pChild);
        lSplitter.setMaxSize(pMaxSize);
    }

    /**
     * Get the splitter associated to a Widget
     * 
     * @param pChild
     *            The child to extract the associated splitter from
     * @return The associated splitter
     */
    public Splitter getAssociatedSplitter(Widget pChild) {
        // If a widget has a next sibling, it must be a splitter, because the
        // only
        // widget that *isn't* followed by a splitter must be the CENTER, which
        // has
        // no associated splitter.
        int lIdx = getWidgetIndex(pChild);
        if (lIdx < getWidgetCount() - 2) {
            Widget lSplitter = getWidget(lIdx + 1);
            assert lSplitter instanceof Splitter : "Expected child widget to be splitter";
            return (Splitter) lSplitter;
        }
        return null;
    }

    /**
     * Inserts a splitter before a Widget
     * 
     * @param pBefore
     *            The widget before witch insert the splitter
     */
    public void insertSplitter(Widget pBefore) {
        assert getChildren().size() > 0 : "Can't add a splitter before any children";
        assert getCenter() == null : "Can't add a splitter after the CENTER widget";

        Widget lLastChild = getChildren().get(getChildren().size() - 1);
        LayoutData lLastChildLayout = (LayoutData) lLastChild.getLayoutData();
        Splitter lSplitter = null;
        switch (lLastChildLayout.direction) {
            case WEST:
                lSplitter = new HSplitter(lLastChild, false);
                break;
            case EAST:
                lSplitter = new HSplitter(lLastChild, true);
                break;
            case NORTH:
                lSplitter = new VSplitter(lLastChild, false);
                break;
            case SOUTH:
                lSplitter = new VSplitter(lLastChild, true);
                break;
            default:
                assert false : "Unexpected direction";
        }

        super.insert(lSplitter, lLastChildLayout.direction, splitterSize,
                pBefore);
    }

    /**
     * HSplitter
     */
    public class HSplitter extends Splitter {

        /**
         * Constructor
         * 
         * @param pTarget
         *            The associated widget
         * @param pReverse
         *            Reverse the split widgets
         */
        public HSplitter(Widget pTarget, boolean pReverse) {
            super(pTarget, pReverse);
            getElement().getStyle().setPropertyPx("width", splitterSize);
            setStyleName(STYLE_GPM_SPLIT_LAYOUT_HORIZONTAL_DRAGGER);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getAbsolutePosition()
         */
        @Override
        protected int getAbsolutePosition() {
            return getAbsoluteLeft();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getEventPosition(com.google.gwt.user.client.Event)
         */
        @Override
        protected int getEventPosition(Event pEvent) {
            return pEvent.getClientX();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getTargetPosition()
         */
        @Override
        protected int getTargetPosition() {
            return target.getAbsoluteLeft();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getTargetSize()
         */
        @Override
        protected int getTargetSize() {
            return target.getOffsetWidth();
        }
    }

    /**
     * Splitter
     */
    public abstract class Splitter extends Widget {
        protected final Widget target;

        private int offset;

        private boolean mouseDown;

        private Command layoutCommand;

        private final boolean reverse;

        private int minSize;

        private int maxSize = Integer.MAX_VALUE;

        private int savedSize = -1;

        private boolean hidden = false;

        /**
         * Constructor
         * 
         * @param pTarget
         *            The associated widget
         * @param pReverse
         *            Reverse the split widgets
         */
        public Splitter(final Widget pTarget, final boolean pReverse) {
            target = pTarget;
            reverse = pReverse;
            setElement(Document.get().createDivElement());
            sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE
                    | Event.ONDBLCLICK);
        }

        /**
         * Minimize the panel
         */
        public void minimize() {
            saveState();
            setAssociatedWidgetSize(0);
        }

        /**
         * Enable or disable the splitter
         * 
         * @param pEnabled
         *            Indicate if the splitter must be enabled or not
         */
        public void enable(final boolean pEnabled) {
            setVisible(pEnabled);
        }

        /**
         * Hide the splitter
         */
        public void show() {
            hidden = false;
            ((LayoutData) getLayoutData()).size = splitterSize;
            forceLayoutRefresh();
        }

        /**
         * Show the splitter
         */
        public void hide() {
            hidden = true;
            ((LayoutData) getLayoutData()).size = 0;
            forceLayoutRefresh();
        }

        /**
         * Deactivate the min and max limits
         */
        public void saveState() {
            if (savedSize == -1) {
                savedSize = (int) ((LayoutData) target.getLayoutData()).size;
            }
        }

        /**
         * Minimize the pannel
         */
        public void restore() {
            if (!hidden) {
                loadState();
            }
        }

        /**
         * Activate the min and max limits
         */
        private void loadState() {
            if (savedSize != -1) {
                setAssociatedWidgetSize(savedSize);
                savedSize = -1;
            }
        }

        /**
         * Reverse split Widgets
         * 
         * @return The new Splitter
         */
        public Splitter reverse() {
            if (this instanceof HSplitter) {
                return new HSplitter(target, !reverse);
            }
            else {
                return new VSplitter(target, !reverse);
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
         */
        @Override
        public void onBrowserEvent(final Event pEvent) {
            switch (pEvent.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                    mouseDown = true;
                    offset = getEventPosition(pEvent) - getAbsolutePosition();
                    Event.setCapture(getElement());
                    pEvent.preventDefault();
                    break;
                case Event.ONMOUSEUP:
                    mouseDown = false;
                    Event.releaseCapture(getElement());
                    pEvent.preventDefault();
                    break;
                case Event.ONMOUSEMOVE:
                    if (mouseDown) {
                        int lSize;
                        if (reverse) {
                            lSize =
                                    getTargetPosition() + getTargetSize()
                                            - getEventPosition(pEvent) - offset;
                        }
                        else {
                            lSize =
                                    getEventPosition(pEvent)
                                            - getTargetPosition() - offset;
                        }
                        setAssociatedWidgetSize(lSize);
                        pEvent.preventDefault();
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * Set the minimum size
         * 
         * @param pMinSize
         *            the new minimum size
         */
        public void setMinSize(final int pMinSize) {
            minSize = pMinSize;
            setAssociatedWidgetSize((int) ((LayoutData) target.getLayoutData()).size);
        }

        /**
         * Set the maximum size
         * 
         * @param pMaxSize
         *            the maximum size
         */
        public void setMaxSize(final int pMaxSize) {
            maxSize = pMaxSize;
            setAssociatedWidgetSize((int) ((LayoutData) target.getLayoutData()).size);
        }

        /**
         * Get the absolute position
         * 
         * @return the absolute position
         */
        protected abstract int getAbsolutePosition();

        /**
         * Get the Event position
         * 
         * @param pEvent
         *            the Event
         * @return the Event position
         */
        protected abstract int getEventPosition(Event pEvent);

        /**
         * Get the target position
         * 
         * @return the target position
         */
        protected abstract int getTargetPosition();

        /**
         * Get the target size
         * 
         * @return the target size
         */
        protected abstract int getTargetSize();

        /**
         * Set the associated widget size
         * 
         * @param pSize
         *            the new associated widget size
         */
        public void setAssociatedWidgetSize(final int pSize) {
            int lSize = pSize;

            if (lSize > maxSize) {
                lSize = maxSize;
            }
            if (lSize < minSize) {
                lSize = minSize;
            }

            final LayoutData lLayout = (LayoutData) target.getLayoutData();

            if (lSize != lLayout.size) {
                lLayout.size = lSize;
                forceLayoutRefresh();
            }
        }

        private void forceLayoutRefresh() {
            // Defer actually updating the layout, so that if we receive many
            // mouse events before layout/paint occurs, we'll only update once.
            if (layoutCommand == null) {
                layoutCommand = new Command() {
                    public void execute() {
                        layoutCommand = null;
                        forceLayout();
                    }
                };
                DeferredCommand.addCommand(layoutCommand);
            }
        }

        /**
         * Return the associated widget size
         * 
         * @return The associated widget size
         */
        public int getAssociatedWidgetSize() {
            return (int) ((LayoutData) target.getLayoutData()).size;
        }
    }

    /**
     * VSplitter
     */
    public class VSplitter extends Splitter {
        /**
         * Constructor
         * 
         * @param pTarget
         *            The Widget associated with the splitter
         * @param pReverse
         *            Reverse the order of split widgets
         */
        public VSplitter(Widget pTarget, boolean pReverse) {
            super(pTarget, pReverse);
            getElement().getStyle().setPropertyPx("height", splitterSize);
            setStyleName(STYLE_GPM_SPLIT_LAYOUT_VERTICAL_DRAGGER);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getAbsolutePosition()
         */
        @Override
        protected int getAbsolutePosition() {
            return getAbsoluteTop();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getEventPosition(com.google.gwt.user.client.Event)
         */
        @Override
        protected int getEventPosition(Event pEvent) {
            return pEvent.getClientY();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getTargetPosition()
         */
        @Override
        protected int getTargetPosition() {
            return target.getAbsoluteTop();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.GpmSplitLayoutPanel.MySplitLayout.Splitter#getTargetSize()
         */
        @Override
        protected int getTargetSize() {
            return target.getOffsetHeight();
        }
    }
}